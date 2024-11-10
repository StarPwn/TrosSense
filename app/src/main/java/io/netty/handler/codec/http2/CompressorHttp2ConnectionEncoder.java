package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.compression.Brotli;
import io.netty.handler.codec.compression.BrotliEncoder;
import io.netty.handler.codec.compression.BrotliOptions;
import io.netty.handler.codec.compression.CompressionOptions;
import io.netty.handler.codec.compression.DeflateOptions;
import io.netty.handler.codec.compression.GzipOptions;
import io.netty.handler.codec.compression.SnappyFrameEncoder;
import io.netty.handler.codec.compression.SnappyOptions;
import io.netty.handler.codec.compression.StandardCompressionOptions;
import io.netty.handler.codec.compression.ZlibCodecFactory;
import io.netty.handler.codec.compression.ZlibWrapper;
import io.netty.handler.codec.compression.Zstd;
import io.netty.handler.codec.compression.ZstdEncoder;
import io.netty.handler.codec.compression.ZstdOptions;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http2.Http2Connection;
import io.netty.util.AsciiString;
import io.netty.util.internal.ObjectUtil;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes4.dex */
public class CompressorHttp2ConnectionEncoder extends DecoratingHttp2ConnectionEncoder {
    public static final int DEFAULT_COMPRESSION_LEVEL = 6;
    public static final int DEFAULT_MEM_LEVEL = 8;
    public static final int DEFAULT_WINDOW_BITS = 15;
    private BrotliOptions brotliOptions;
    private int compressionLevel;
    private DeflateOptions deflateOptions;
    private GzipOptions gzipCompressionOptions;
    private int memLevel;
    private final Http2Connection.PropertyKey propertyKey;
    private SnappyOptions snappyOptions;
    private final boolean supportsCompressionOptions;
    private int windowBits;
    private ZstdOptions zstdOptions;

    public CompressorHttp2ConnectionEncoder(Http2ConnectionEncoder delegate) {
        this(delegate, defaultCompressionOptions());
    }

    private static CompressionOptions[] defaultCompressionOptions() {
        List<CompressionOptions> compressionOptions = new ArrayList<>();
        compressionOptions.add(StandardCompressionOptions.gzip());
        compressionOptions.add(StandardCompressionOptions.deflate());
        compressionOptions.add(StandardCompressionOptions.snappy());
        if (Brotli.isAvailable()) {
            compressionOptions.add(StandardCompressionOptions.brotli());
        }
        if (Zstd.isAvailable()) {
            compressionOptions.add(StandardCompressionOptions.zstd());
        }
        return (CompressionOptions[]) compressionOptions.toArray(new CompressionOptions[0]);
    }

    @Deprecated
    public CompressorHttp2ConnectionEncoder(Http2ConnectionEncoder delegate, int compressionLevel, int windowBits, int memLevel) {
        super(delegate);
        this.compressionLevel = ObjectUtil.checkInRange(compressionLevel, 0, 9, "compressionLevel");
        this.windowBits = ObjectUtil.checkInRange(windowBits, 9, 15, "windowBits");
        this.memLevel = ObjectUtil.checkInRange(memLevel, 1, 9, "memLevel");
        this.propertyKey = connection().newKey();
        connection().addListener(new Http2ConnectionAdapter() { // from class: io.netty.handler.codec.http2.CompressorHttp2ConnectionEncoder.1
            @Override // io.netty.handler.codec.http2.Http2ConnectionAdapter, io.netty.handler.codec.http2.Http2Connection.Listener
            public void onStreamRemoved(Http2Stream stream) {
                EmbeddedChannel compressor = (EmbeddedChannel) stream.getProperty(CompressorHttp2ConnectionEncoder.this.propertyKey);
                if (compressor != null) {
                    CompressorHttp2ConnectionEncoder.this.cleanup(stream, compressor);
                }
            }
        });
        this.supportsCompressionOptions = false;
    }

    public CompressorHttp2ConnectionEncoder(Http2ConnectionEncoder delegate, CompressionOptions... compressionOptionsArgs) {
        super(delegate);
        ObjectUtil.checkNotNull(compressionOptionsArgs, "CompressionOptions");
        ObjectUtil.deepCheckNotNull("CompressionOptions", compressionOptionsArgs);
        for (CompressionOptions compressionOptions : compressionOptionsArgs) {
            if (Brotli.isAvailable() && (compressionOptions instanceof BrotliOptions)) {
                this.brotliOptions = (BrotliOptions) compressionOptions;
            } else if (compressionOptions instanceof GzipOptions) {
                this.gzipCompressionOptions = (GzipOptions) compressionOptions;
            } else if (compressionOptions instanceof DeflateOptions) {
                this.deflateOptions = (DeflateOptions) compressionOptions;
            } else if (compressionOptions instanceof ZstdOptions) {
                this.zstdOptions = (ZstdOptions) compressionOptions;
            } else if (compressionOptions instanceof SnappyOptions) {
                this.snappyOptions = (SnappyOptions) compressionOptions;
            } else {
                throw new IllegalArgumentException("Unsupported " + CompressionOptions.class.getSimpleName() + ": " + compressionOptions);
            }
        }
        this.supportsCompressionOptions = true;
        this.propertyKey = connection().newKey();
        connection().addListener(new Http2ConnectionAdapter() { // from class: io.netty.handler.codec.http2.CompressorHttp2ConnectionEncoder.2
            @Override // io.netty.handler.codec.http2.Http2ConnectionAdapter, io.netty.handler.codec.http2.Http2Connection.Listener
            public void onStreamRemoved(Http2Stream stream) {
                EmbeddedChannel compressor = (EmbeddedChannel) stream.getProperty(CompressorHttp2ConnectionEncoder.this.propertyKey);
                if (compressor != null) {
                    CompressorHttp2ConnectionEncoder.this.cleanup(stream, compressor);
                }
            }
        });
    }

    /* JADX WARN: Code restructure failed: missing block: B:46:0x00b1, code lost:            if (r23 != false) goto L47;     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x00b3, code lost:            cleanup(r11, r12);     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x00c6, code lost:            return r24;     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x00c3, code lost:            if (r23 == false) goto L56;     */
    @Override // io.netty.handler.codec.http2.DecoratingHttp2FrameWriter, io.netty.handler.codec.http2.Http2DataWriter
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public io.netty.channel.ChannelFuture writeData(io.netty.channel.ChannelHandlerContext r19, int r20, io.netty.buffer.ByteBuf r21, int r22, boolean r23, io.netty.channel.ChannelPromise r24) {
        /*
            Method dump skipped, instructions count: 207
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.handler.codec.http2.CompressorHttp2ConnectionEncoder.writeData(io.netty.channel.ChannelHandlerContext, int, io.netty.buffer.ByteBuf, int, boolean, io.netty.channel.ChannelPromise):io.netty.channel.ChannelFuture");
    }

    @Override // io.netty.handler.codec.http2.DecoratingHttp2FrameWriter, io.netty.handler.codec.http2.Http2FrameWriter
    public ChannelFuture writeHeaders(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int padding, boolean endStream, ChannelPromise promise) {
        try {
            EmbeddedChannel compressor = newCompressor(ctx, headers, endStream);
            ChannelFuture future = super.writeHeaders(ctx, streamId, headers, padding, endStream, promise);
            bindCompressorToStream(compressor, streamId);
            return future;
        } catch (Throwable e) {
            promise.tryFailure(e);
            return promise;
        }
    }

    @Override // io.netty.handler.codec.http2.DecoratingHttp2FrameWriter, io.netty.handler.codec.http2.Http2FrameWriter
    public ChannelFuture writeHeaders(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int streamDependency, short weight, boolean exclusive, int padding, boolean endOfStream, ChannelPromise promise) {
        try {
            EmbeddedChannel compressor = newCompressor(ctx, headers, endOfStream);
            ChannelFuture future = super.writeHeaders(ctx, streamId, headers, streamDependency, weight, exclusive, padding, endOfStream, promise);
            bindCompressorToStream(compressor, streamId);
            return future;
        } catch (Throwable e) {
            promise.tryFailure(e);
            return promise;
        }
    }

    protected EmbeddedChannel newContentCompressor(ChannelHandlerContext ctx, CharSequence contentEncoding) throws Http2Exception {
        if (HttpHeaderValues.GZIP.contentEqualsIgnoreCase(contentEncoding) || HttpHeaderValues.X_GZIP.contentEqualsIgnoreCase(contentEncoding)) {
            return newCompressionChannel(ctx, ZlibWrapper.GZIP);
        }
        if (HttpHeaderValues.DEFLATE.contentEqualsIgnoreCase(contentEncoding) || HttpHeaderValues.X_DEFLATE.contentEqualsIgnoreCase(contentEncoding)) {
            return newCompressionChannel(ctx, ZlibWrapper.ZLIB);
        }
        if (Brotli.isAvailable() && this.brotliOptions != null && HttpHeaderValues.BR.contentEqualsIgnoreCase(contentEncoding)) {
            return new EmbeddedChannel(ctx.channel().id(), ctx.channel().metadata().hasDisconnect(), ctx.channel().config(), new BrotliEncoder(this.brotliOptions.parameters()));
        }
        if (this.zstdOptions != null && HttpHeaderValues.ZSTD.contentEqualsIgnoreCase(contentEncoding)) {
            return new EmbeddedChannel(ctx.channel().id(), ctx.channel().metadata().hasDisconnect(), ctx.channel().config(), new ZstdEncoder(this.zstdOptions.compressionLevel(), this.zstdOptions.blockSize(), this.zstdOptions.maxEncodeSize()));
        }
        if (this.snappyOptions != null && HttpHeaderValues.SNAPPY.contentEqualsIgnoreCase(contentEncoding)) {
            return new EmbeddedChannel(ctx.channel().id(), ctx.channel().metadata().hasDisconnect(), ctx.channel().config(), new SnappyFrameEncoder());
        }
        return null;
    }

    protected CharSequence getTargetContentEncoding(CharSequence contentEncoding) throws Http2Exception {
        return contentEncoding;
    }

    private EmbeddedChannel newCompressionChannel(ChannelHandlerContext ctx, ZlibWrapper wrapper) {
        if (this.supportsCompressionOptions) {
            if (wrapper == ZlibWrapper.GZIP && this.gzipCompressionOptions != null) {
                return new EmbeddedChannel(ctx.channel().id(), ctx.channel().metadata().hasDisconnect(), ctx.channel().config(), ZlibCodecFactory.newZlibEncoder(wrapper, this.gzipCompressionOptions.compressionLevel(), this.gzipCompressionOptions.windowBits(), this.gzipCompressionOptions.memLevel()));
            }
            if (wrapper == ZlibWrapper.ZLIB && this.deflateOptions != null) {
                return new EmbeddedChannel(ctx.channel().id(), ctx.channel().metadata().hasDisconnect(), ctx.channel().config(), ZlibCodecFactory.newZlibEncoder(wrapper, this.deflateOptions.compressionLevel(), this.deflateOptions.windowBits(), this.deflateOptions.memLevel()));
            }
            throw new IllegalArgumentException("Unsupported ZlibWrapper: " + wrapper);
        }
        return new EmbeddedChannel(ctx.channel().id(), ctx.channel().metadata().hasDisconnect(), ctx.channel().config(), ZlibCodecFactory.newZlibEncoder(wrapper, this.compressionLevel, this.windowBits, this.memLevel));
    }

    private EmbeddedChannel newCompressor(ChannelHandlerContext ctx, Http2Headers headers, boolean endOfStream) throws Http2Exception {
        if (endOfStream) {
            return null;
        }
        CharSequence encoding = headers.get(HttpHeaderNames.CONTENT_ENCODING);
        if (encoding == null) {
            encoding = HttpHeaderValues.IDENTITY;
        }
        EmbeddedChannel compressor = newContentCompressor(ctx, encoding);
        if (compressor != null) {
            CharSequence targetContentEncoding = getTargetContentEncoding(encoding);
            if (HttpHeaderValues.IDENTITY.contentEqualsIgnoreCase(targetContentEncoding)) {
                headers.remove(HttpHeaderNames.CONTENT_ENCODING);
            } else {
                headers.set((Http2Headers) HttpHeaderNames.CONTENT_ENCODING, (AsciiString) targetContentEncoding);
            }
            headers.remove(HttpHeaderNames.CONTENT_LENGTH);
        }
        return compressor;
    }

    private void bindCompressorToStream(EmbeddedChannel compressor, int streamId) {
        Http2Stream stream;
        if (compressor != null && (stream = connection().stream(streamId)) != null) {
            stream.setProperty(this.propertyKey, compressor);
        }
    }

    void cleanup(Http2Stream stream, EmbeddedChannel compressor) {
        compressor.finishAndReleaseAll();
        stream.removeProperty(this.propertyKey);
    }

    private static ByteBuf nextReadableBuf(EmbeddedChannel compressor) {
        while (true) {
            ByteBuf buf = (ByteBuf) compressor.readOutbound();
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
}
