package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.compression.Brotli;
import io.netty.handler.codec.compression.BrotliDecoder;
import io.netty.handler.codec.compression.SnappyFrameDecoder;
import io.netty.handler.codec.compression.ZlibCodecFactory;
import io.netty.handler.codec.compression.ZlibWrapper;
import io.netty.handler.codec.compression.Zstd;
import io.netty.handler.codec.compression.ZstdDecoder;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http2.Http2Connection;
import io.netty.util.AsciiString;
import io.netty.util.internal.ObjectUtil;

/* loaded from: classes4.dex */
public class DelegatingDecompressorFrameListener extends Http2FrameListenerDecorator {
    private final Http2Connection connection;
    private boolean flowControllerInitialized;
    private final Http2Connection.PropertyKey propertyKey;
    private final boolean strict;

    public DelegatingDecompressorFrameListener(Http2Connection connection, Http2FrameListener listener) {
        this(connection, listener, true);
    }

    public DelegatingDecompressorFrameListener(Http2Connection connection, Http2FrameListener listener, boolean strict) {
        super(listener);
        this.connection = connection;
        this.strict = strict;
        this.propertyKey = connection.newKey();
        connection.addListener(new Http2ConnectionAdapter() { // from class: io.netty.handler.codec.http2.DelegatingDecompressorFrameListener.1
            @Override // io.netty.handler.codec.http2.Http2ConnectionAdapter, io.netty.handler.codec.http2.Http2Connection.Listener
            public void onStreamRemoved(Http2Stream stream) {
                Http2Decompressor decompressor = DelegatingDecompressorFrameListener.this.decompressor(stream);
                if (decompressor != null) {
                    DelegatingDecompressorFrameListener.cleanup(decompressor);
                }
            }
        });
    }

    @Override // io.netty.handler.codec.http2.Http2FrameListenerDecorator, io.netty.handler.codec.http2.Http2FrameListener
    public int onDataRead(ChannelHandlerContext ctx, int streamId, ByteBuf data, int padding, boolean endOfStream) throws Http2Exception {
        ByteBuf buf;
        ByteBuf nextBuf;
        boolean decompressedEndOfStream;
        int padding2 = padding;
        Http2Stream stream = this.connection.stream(streamId);
        Http2Decompressor decompressor = decompressor(stream);
        if (decompressor == null) {
            return this.listener.onDataRead(ctx, streamId, data, padding, endOfStream);
        }
        EmbeddedChannel channel = decompressor.decompressor();
        int compressedBytes = data.readableBytes() + padding2;
        decompressor.incrementCompressedBytes(compressedBytes);
        boolean z = true;
        try {
            channel.writeInbound(data.retain());
            ByteBuf buf2 = nextReadableBuf(channel);
            if (buf2 == null && endOfStream && channel.finish()) {
                buf = nextReadableBuf(channel);
            } else {
                buf = buf2;
            }
        } catch (Http2Exception e) {
            throw e;
        } catch (Throwable th) {
            t = th;
        }
        try {
            if (buf == null) {
                if (endOfStream) {
                    this.listener.onDataRead(ctx, streamId, Unpooled.EMPTY_BUFFER, padding, true);
                }
                decompressor.incrementDecompressedBytes(compressedBytes);
                return compressedBytes;
            }
            try {
                Http2LocalFlowController flowController = this.connection.local().flowController();
                decompressor.incrementDecompressedBytes(padding2);
                while (true) {
                    try {
                        ByteBuf nextBuf2 = nextReadableBuf(channel);
                        boolean decompressedEndOfStream2 = (nextBuf2 == null && endOfStream) ? z : false;
                        if (decompressedEndOfStream2 && channel.finish()) {
                            ByteBuf nextBuf3 = nextReadableBuf(channel);
                            nextBuf = nextBuf3;
                            decompressedEndOfStream = nextBuf3 == null ? z : false;
                        } else {
                            nextBuf = nextBuf2;
                            decompressedEndOfStream = decompressedEndOfStream2;
                        }
                        decompressor.incrementDecompressedBytes(buf.readableBytes());
                        Http2LocalFlowController flowController2 = flowController;
                        flowController2.consumeBytes(stream, this.listener.onDataRead(ctx, streamId, buf, padding2, decompressedEndOfStream));
                        if (nextBuf != null) {
                            padding2 = 0;
                            buf.release();
                            buf = nextBuf;
                            flowController = flowController2;
                            z = true;
                        } else {
                            buf.release();
                            return 0;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        buf.release();
                        throw th;
                    }
                }
            } catch (Throwable th3) {
                th = th3;
            }
        } catch (Http2Exception e2) {
            throw e2;
        } catch (Throwable th4) {
            t = th4;
            throw Http2Exception.streamError(stream.id(), Http2Error.INTERNAL_ERROR, t, "Decompressor error detected while delegating data read on streamId %d", Integer.valueOf(stream.id()));
        }
    }

    @Override // io.netty.handler.codec.http2.Http2FrameListenerDecorator, io.netty.handler.codec.http2.Http2FrameListener
    public void onHeadersRead(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int padding, boolean endStream) throws Http2Exception {
        initDecompressor(ctx, streamId, headers, endStream);
        this.listener.onHeadersRead(ctx, streamId, headers, padding, endStream);
    }

    @Override // io.netty.handler.codec.http2.Http2FrameListenerDecorator, io.netty.handler.codec.http2.Http2FrameListener
    public void onHeadersRead(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int streamDependency, short weight, boolean exclusive, int padding, boolean endStream) throws Http2Exception {
        initDecompressor(ctx, streamId, headers, endStream);
        this.listener.onHeadersRead(ctx, streamId, headers, streamDependency, weight, exclusive, padding, endStream);
    }

    protected EmbeddedChannel newContentDecompressor(ChannelHandlerContext ctx, CharSequence contentEncoding) throws Http2Exception {
        if (HttpHeaderValues.GZIP.contentEqualsIgnoreCase(contentEncoding) || HttpHeaderValues.X_GZIP.contentEqualsIgnoreCase(contentEncoding)) {
            return new EmbeddedChannel(ctx.channel().id(), ctx.channel().metadata().hasDisconnect(), ctx.channel().config(), ZlibCodecFactory.newZlibDecoder(ZlibWrapper.GZIP));
        }
        if (HttpHeaderValues.DEFLATE.contentEqualsIgnoreCase(contentEncoding) || HttpHeaderValues.X_DEFLATE.contentEqualsIgnoreCase(contentEncoding)) {
            ZlibWrapper wrapper = this.strict ? ZlibWrapper.ZLIB : ZlibWrapper.ZLIB_OR_NONE;
            return new EmbeddedChannel(ctx.channel().id(), ctx.channel().metadata().hasDisconnect(), ctx.channel().config(), ZlibCodecFactory.newZlibDecoder(wrapper));
        }
        if (Brotli.isAvailable() && HttpHeaderValues.BR.contentEqualsIgnoreCase(contentEncoding)) {
            return new EmbeddedChannel(ctx.channel().id(), ctx.channel().metadata().hasDisconnect(), ctx.channel().config(), new BrotliDecoder());
        }
        if (HttpHeaderValues.SNAPPY.contentEqualsIgnoreCase(contentEncoding)) {
            return new EmbeddedChannel(ctx.channel().id(), ctx.channel().metadata().hasDisconnect(), ctx.channel().config(), new SnappyFrameDecoder());
        }
        if (Zstd.isAvailable() && HttpHeaderValues.ZSTD.contentEqualsIgnoreCase(contentEncoding)) {
            return new EmbeddedChannel(ctx.channel().id(), ctx.channel().metadata().hasDisconnect(), ctx.channel().config(), new ZstdDecoder());
        }
        return null;
    }

    protected CharSequence getTargetContentEncoding(CharSequence contentEncoding) throws Http2Exception {
        return HttpHeaderValues.IDENTITY;
    }

    private void initDecompressor(ChannelHandlerContext ctx, int streamId, Http2Headers headers, boolean endOfStream) throws Http2Exception {
        Http2Stream stream = this.connection.stream(streamId);
        if (stream == null) {
            return;
        }
        Http2Decompressor decompressor = decompressor(stream);
        if (decompressor == null && !endOfStream) {
            CharSequence contentEncoding = headers.get(HttpHeaderNames.CONTENT_ENCODING);
            if (contentEncoding == null) {
                contentEncoding = HttpHeaderValues.IDENTITY;
            }
            EmbeddedChannel channel = newContentDecompressor(ctx, contentEncoding);
            if (channel != null) {
                decompressor = new Http2Decompressor(channel);
                stream.setProperty(this.propertyKey, decompressor);
                CharSequence targetContentEncoding = getTargetContentEncoding(contentEncoding);
                if (HttpHeaderValues.IDENTITY.contentEqualsIgnoreCase(targetContentEncoding)) {
                    headers.remove(HttpHeaderNames.CONTENT_ENCODING);
                } else {
                    headers.set((Http2Headers) HttpHeaderNames.CONTENT_ENCODING, (AsciiString) targetContentEncoding);
                }
            }
        }
        if (decompressor != null) {
            headers.remove(HttpHeaderNames.CONTENT_LENGTH);
            if (!this.flowControllerInitialized) {
                this.flowControllerInitialized = true;
                this.connection.local().flowController(new ConsumedBytesConverter(this.connection.local().flowController()));
            }
        }
    }

    Http2Decompressor decompressor(Http2Stream stream) {
        if (stream == null) {
            return null;
        }
        return (Http2Decompressor) stream.getProperty(this.propertyKey);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void cleanup(Http2Decompressor decompressor) {
        decompressor.decompressor().finishAndReleaseAll();
    }

    private static ByteBuf nextReadableBuf(EmbeddedChannel decompressor) {
        while (true) {
            ByteBuf buf = (ByteBuf) decompressor.readInbound();
            if (buf == null) {
                return null;
            }
            if (!buf.isReadable()) {
                buf.release();
            } else {
                return buf;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class ConsumedBytesConverter implements Http2LocalFlowController {
        private final Http2LocalFlowController flowController;

        ConsumedBytesConverter(Http2LocalFlowController flowController) {
            this.flowController = (Http2LocalFlowController) ObjectUtil.checkNotNull(flowController, "flowController");
        }

        @Override // io.netty.handler.codec.http2.Http2LocalFlowController
        public Http2LocalFlowController frameWriter(Http2FrameWriter frameWriter) {
            return this.flowController.frameWriter(frameWriter);
        }

        @Override // io.netty.handler.codec.http2.Http2FlowController
        public void channelHandlerContext(ChannelHandlerContext ctx) throws Http2Exception {
            this.flowController.channelHandlerContext(ctx);
        }

        @Override // io.netty.handler.codec.http2.Http2FlowController
        public void initialWindowSize(int newWindowSize) throws Http2Exception {
            this.flowController.initialWindowSize(newWindowSize);
        }

        @Override // io.netty.handler.codec.http2.Http2FlowController
        public int initialWindowSize() {
            return this.flowController.initialWindowSize();
        }

        @Override // io.netty.handler.codec.http2.Http2FlowController
        public int windowSize(Http2Stream stream) {
            return this.flowController.windowSize(stream);
        }

        @Override // io.netty.handler.codec.http2.Http2FlowController
        public void incrementWindowSize(Http2Stream stream, int delta) throws Http2Exception {
            this.flowController.incrementWindowSize(stream, delta);
        }

        @Override // io.netty.handler.codec.http2.Http2LocalFlowController
        public void receiveFlowControlledFrame(Http2Stream stream, ByteBuf data, int padding, boolean endOfStream) throws Http2Exception {
            this.flowController.receiveFlowControlledFrame(stream, data, padding, endOfStream);
        }

        @Override // io.netty.handler.codec.http2.Http2LocalFlowController
        public boolean consumeBytes(Http2Stream stream, int numBytes) throws Http2Exception {
            Http2Decompressor decompressor = DelegatingDecompressorFrameListener.this.decompressor(stream);
            if (decompressor != null) {
                numBytes = decompressor.consumeBytes(stream.id(), numBytes);
            }
            try {
                return this.flowController.consumeBytes(stream, numBytes);
            } catch (Http2Exception e) {
                throw e;
            } catch (Throwable t) {
                throw Http2Exception.streamError(stream.id(), Http2Error.INTERNAL_ERROR, t, "Error while returning bytes to flow control window", new Object[0]);
            }
        }

        @Override // io.netty.handler.codec.http2.Http2LocalFlowController
        public int unconsumedBytes(Http2Stream stream) {
            return this.flowController.unconsumedBytes(stream);
        }

        @Override // io.netty.handler.codec.http2.Http2LocalFlowController
        public int initialWindowSize(Http2Stream stream) {
            return this.flowController.initialWindowSize(stream);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class Http2Decompressor {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private int compressed;
        private int decompressed;
        private final EmbeddedChannel decompressor;

        Http2Decompressor(EmbeddedChannel decompressor) {
            this.decompressor = decompressor;
        }

        EmbeddedChannel decompressor() {
            return this.decompressor;
        }

        void incrementCompressedBytes(int delta) {
            if (delta < 0) {
                throw new AssertionError();
            }
            this.compressed += delta;
        }

        void incrementDecompressedBytes(int delta) {
            if (delta < 0) {
                throw new AssertionError();
            }
            this.decompressed += delta;
        }

        int consumeBytes(int streamId, int decompressedBytes) throws Http2Exception {
            ObjectUtil.checkPositiveOrZero(decompressedBytes, "decompressedBytes");
            if (this.decompressed - decompressedBytes < 0) {
                throw Http2Exception.streamError(streamId, Http2Error.INTERNAL_ERROR, "Attempting to return too many bytes for stream %d. decompressed: %d decompressedBytes: %d", Integer.valueOf(streamId), Integer.valueOf(this.decompressed), Integer.valueOf(decompressedBytes));
            }
            double consumedRatio = decompressedBytes / this.decompressed;
            int consumedCompressed = Math.min(this.compressed, (int) Math.ceil(this.compressed * consumedRatio));
            if (this.compressed - consumedCompressed < 0) {
                throw Http2Exception.streamError(streamId, Http2Error.INTERNAL_ERROR, "overflow when converting decompressed bytes to compressed bytes for stream %d.decompressedBytes: %d decompressed: %d compressed: %d consumedCompressed: %d", Integer.valueOf(streamId), Integer.valueOf(decompressedBytes), Integer.valueOf(this.decompressed), Integer.valueOf(this.compressed), Integer.valueOf(consumedCompressed));
            }
            this.decompressed -= decompressedBytes;
            this.compressed -= consumedCompressed;
            return consumedCompressed;
        }
    }
}
