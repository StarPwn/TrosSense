package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http2.Http2Exception;
import io.netty.handler.codec.http2.Http2Stream;
import io.netty.util.AsciiString;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.net.SocketAddress;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/* loaded from: classes4.dex */
public class Http2ConnectionHandler extends ByteToMessageDecoder implements Http2LifecycleManager, ChannelOutboundHandler {
    private BaseDecoder byteDecoder;
    private ChannelFutureListener closeListener;
    private final Http2ConnectionDecoder decoder;
    private final boolean decoupleCloseAndGoAway;
    private final Http2ConnectionEncoder encoder;
    private final boolean flushPreface;
    private long gracefulShutdownTimeoutMillis;
    private final Http2Settings initialSettings;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) Http2ConnectionHandler.class);
    private static final Http2Headers HEADERS_TOO_LARGE_HEADERS = ReadOnlyHttp2Headers.serverHeaders(false, HttpResponseStatus.REQUEST_HEADER_FIELDS_TOO_LARGE.codeAsText(), new AsciiString[0]);
    private static final ByteBuf HTTP_1_X_BUF = Unpooled.unreleasableBuffer(Unpooled.wrappedBuffer(new byte[]{72, 84, 84, 80, 47, 49, 46})).asReadOnly();

    /* JADX INFO: Access modifiers changed from: protected */
    public Http2ConnectionHandler(Http2ConnectionDecoder decoder, Http2ConnectionEncoder encoder, Http2Settings initialSettings) {
        this(decoder, encoder, initialSettings, false);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Http2ConnectionHandler(Http2ConnectionDecoder decoder, Http2ConnectionEncoder encoder, Http2Settings initialSettings, boolean decoupleCloseAndGoAway) {
        this(decoder, encoder, initialSettings, decoupleCloseAndGoAway, true);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Http2ConnectionHandler(Http2ConnectionDecoder decoder, Http2ConnectionEncoder encoder, Http2Settings initialSettings, boolean decoupleCloseAndGoAway, boolean flushPreface) {
        this.initialSettings = (Http2Settings) ObjectUtil.checkNotNull(initialSettings, "initialSettings");
        this.decoder = (Http2ConnectionDecoder) ObjectUtil.checkNotNull(decoder, "decoder");
        this.encoder = (Http2ConnectionEncoder) ObjectUtil.checkNotNull(encoder, "encoder");
        this.decoupleCloseAndGoAway = decoupleCloseAndGoAway;
        this.flushPreface = flushPreface;
        if (encoder.connection() != decoder.connection()) {
            throw new IllegalArgumentException("Encoder and Decoder do not share the same connection object");
        }
    }

    public long gracefulShutdownTimeoutMillis() {
        return this.gracefulShutdownTimeoutMillis;
    }

    public void gracefulShutdownTimeoutMillis(long gracefulShutdownTimeoutMillis) {
        if (gracefulShutdownTimeoutMillis < -1) {
            throw new IllegalArgumentException("gracefulShutdownTimeoutMillis: " + gracefulShutdownTimeoutMillis + " (expected: -1 for indefinite or >= 0)");
        }
        this.gracefulShutdownTimeoutMillis = gracefulShutdownTimeoutMillis;
    }

    public Http2Connection connection() {
        return this.encoder.connection();
    }

    public Http2ConnectionDecoder decoder() {
        return this.decoder;
    }

    public Http2ConnectionEncoder encoder() {
        return this.encoder;
    }

    private boolean prefaceSent() {
        return this.byteDecoder != null && this.byteDecoder.prefaceSent();
    }

    public void onHttpClientUpgrade() throws Http2Exception {
        if (connection().isServer()) {
            throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Client-side HTTP upgrade requested for a server", new Object[0]);
        }
        if (!prefaceSent()) {
            throw Http2Exception.connectionError(Http2Error.INTERNAL_ERROR, "HTTP upgrade must occur after preface was sent", new Object[0]);
        }
        if (this.decoder.prefaceReceived()) {
            throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "HTTP upgrade must occur before HTTP/2 preface is received", new Object[0]);
        }
        connection().local().createStream(1, true);
    }

    public void onHttpServerUpgrade(Http2Settings settings) throws Http2Exception {
        if (!connection().isServer()) {
            throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Server-side HTTP upgrade requested for a client", new Object[0]);
        }
        if (!prefaceSent()) {
            throw Http2Exception.connectionError(Http2Error.INTERNAL_ERROR, "HTTP upgrade must occur after preface was sent", new Object[0]);
        }
        if (this.decoder.prefaceReceived()) {
            throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "HTTP upgrade must occur before HTTP/2 preface is received", new Object[0]);
        }
        this.encoder.remoteSettings(settings);
        connection().remote().createStream(1, true);
    }

    @Override // io.netty.channel.ChannelOutboundHandler
    public void flush(ChannelHandlerContext ctx) {
        try {
            this.encoder.flowController().writePendingBytes();
            ctx.flush();
        } catch (Http2Exception e) {
            onError(ctx, true, e);
        } catch (Throwable cause) {
            onError(ctx, true, Http2Exception.connectionError(Http2Error.INTERNAL_ERROR, cause, "Error flushing", new Object[0]));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public abstract class BaseDecoder {
        public abstract void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception;

        private BaseDecoder() {
        }

        public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        }

        public void channelActive(ChannelHandlerContext ctx) throws Exception {
        }

        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            Http2ConnectionHandler.this.encoder().close();
            Http2ConnectionHandler.this.decoder().close();
            Http2ConnectionHandler.this.connection().close(ctx.voidPromise());
        }

        public boolean prefaceSent() {
            return true;
        }
    }

    /* loaded from: classes4.dex */
    private final class PrefaceDecoder extends BaseDecoder {
        private ByteBuf clientPrefaceString;
        private boolean prefaceSent;

        PrefaceDecoder(ChannelHandlerContext ctx) throws Exception {
            super();
            this.clientPrefaceString = Http2ConnectionHandler.clientPrefaceString(Http2ConnectionHandler.this.encoder.connection());
            sendPreface(ctx);
        }

        @Override // io.netty.handler.codec.http2.Http2ConnectionHandler.BaseDecoder
        public boolean prefaceSent() {
            return this.prefaceSent;
        }

        @Override // io.netty.handler.codec.http2.Http2ConnectionHandler.BaseDecoder
        public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
            try {
                if (ctx.channel().isActive() && readClientPrefaceString(in) && verifyFirstFrameIsSettings(in)) {
                    Http2ConnectionHandler.this.byteDecoder = new FrameDecoder();
                    Http2ConnectionHandler.this.byteDecoder.decode(ctx, in, out);
                }
            } catch (Throwable e) {
                Http2ConnectionHandler.this.onError(ctx, false, e);
            }
        }

        @Override // io.netty.handler.codec.http2.Http2ConnectionHandler.BaseDecoder
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            sendPreface(ctx);
            if (Http2ConnectionHandler.this.flushPreface) {
                ctx.flush();
            }
        }

        @Override // io.netty.handler.codec.http2.Http2ConnectionHandler.BaseDecoder
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            cleanup();
            super.channelInactive(ctx);
        }

        @Override // io.netty.handler.codec.http2.Http2ConnectionHandler.BaseDecoder
        public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
            cleanup();
        }

        private void cleanup() {
            if (this.clientPrefaceString != null) {
                this.clientPrefaceString.release();
                this.clientPrefaceString = null;
            }
        }

        private boolean readClientPrefaceString(ByteBuf in) throws Http2Exception {
            if (this.clientPrefaceString == null) {
                return true;
            }
            int prefaceRemaining = this.clientPrefaceString.readableBytes();
            int bytesRead = Math.min(in.readableBytes(), prefaceRemaining);
            if (bytesRead == 0 || !ByteBufUtil.equals(in, in.readerIndex(), this.clientPrefaceString, this.clientPrefaceString.readerIndex(), bytesRead)) {
                int http1Index = ByteBufUtil.indexOf(Http2ConnectionHandler.HTTP_1_X_BUF, in.slice(in.readerIndex(), Math.min(in.readableBytes(), 1024)));
                if (http1Index != -1) {
                    String chunk = in.toString(in.readerIndex(), http1Index - in.readerIndex(), CharsetUtil.US_ASCII);
                    throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Unexpected HTTP/1.x request: %s", chunk);
                }
                String receivedBytes = ByteBufUtil.hexDump(in, in.readerIndex(), Math.min(in.readableBytes(), this.clientPrefaceString.readableBytes()));
                throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "HTTP/2 client preface string missing or corrupt. Hex dump for received bytes: %s", receivedBytes);
            }
            in.skipBytes(bytesRead);
            this.clientPrefaceString.skipBytes(bytesRead);
            if (!this.clientPrefaceString.isReadable()) {
                this.clientPrefaceString.release();
                this.clientPrefaceString = null;
                return true;
            }
            return false;
        }

        private boolean verifyFirstFrameIsSettings(ByteBuf in) throws Http2Exception {
            if (in.readableBytes() < 5) {
                return false;
            }
            short frameType = in.getUnsignedByte(in.readerIndex() + 3);
            short flags = in.getUnsignedByte(in.readerIndex() + 4);
            if (frameType != 4 || (flags & 1) != 0) {
                throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "First received frame was not SETTINGS. Hex dump for first 5 bytes: %s", ByteBufUtil.hexDump(in, in.readerIndex(), 5));
            }
            return true;
        }

        private void sendPreface(ChannelHandlerContext ctx) throws Exception {
            if (this.prefaceSent || !ctx.channel().isActive()) {
                return;
            }
            this.prefaceSent = true;
            boolean isClient = true ^ Http2ConnectionHandler.this.connection().isServer();
            if (isClient) {
                ctx.write(Http2CodecUtil.connectionPrefaceBuf()).addListener((GenericFutureListener<? extends Future<? super Void>>) ChannelFutureListener.CLOSE_ON_FAILURE);
            }
            Http2ConnectionHandler.this.encoder.writeSettings(ctx, Http2ConnectionHandler.this.initialSettings, ctx.newPromise()).addListener((GenericFutureListener<? extends Future<? super Void>>) ChannelFutureListener.CLOSE_ON_FAILURE);
            if (isClient) {
                Http2ConnectionHandler.this.userEventTriggered(ctx, Http2ConnectionPrefaceAndSettingsFrameWrittenEvent.INSTANCE);
            }
        }
    }

    /* loaded from: classes4.dex */
    private final class FrameDecoder extends BaseDecoder {
        private FrameDecoder() {
            super();
        }

        @Override // io.netty.handler.codec.http2.Http2ConnectionHandler.BaseDecoder
        public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
            try {
                Http2ConnectionHandler.this.decoder.decodeFrame(ctx, in, out);
            } catch (Throwable e) {
                Http2ConnectionHandler.this.onError(ctx, false, e);
            }
        }
    }

    @Override // io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        this.encoder.lifecycleManager(this);
        this.decoder.lifecycleManager(this);
        this.encoder.flowController().channelHandlerContext(ctx);
        this.decoder.flowController().channelHandlerContext(ctx);
        this.byteDecoder = new PrefaceDecoder(ctx);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.ByteToMessageDecoder
    public void handlerRemoved0(ChannelHandlerContext ctx) throws Exception {
        if (this.byteDecoder != null) {
            this.byteDecoder.handlerRemoved(ctx);
            this.byteDecoder = null;
        }
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        if (this.byteDecoder == null) {
            this.byteDecoder = new PrefaceDecoder(ctx);
        }
        this.byteDecoder.channelActive(ctx);
        super.channelActive(ctx);
    }

    @Override // io.netty.handler.codec.ByteToMessageDecoder, io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        if (this.byteDecoder != null) {
            this.byteDecoder.channelInactive(ctx);
            this.byteDecoder = null;
        }
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        try {
            if (ctx.channel().isWritable()) {
                flush(ctx);
            }
            this.encoder.flowController().channelWritabilityChanged();
        } finally {
            super.channelWritabilityChanged(ctx);
        }
    }

    @Override // io.netty.handler.codec.ByteToMessageDecoder
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        this.byteDecoder.decode(ctx, in, out);
    }

    @Override // io.netty.channel.ChannelOutboundHandler
    public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) throws Exception {
        ctx.bind(localAddress, promise);
    }

    @Override // io.netty.channel.ChannelOutboundHandler
    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
        ctx.connect(remoteAddress, localAddress, promise);
    }

    @Override // io.netty.channel.ChannelOutboundHandler
    public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        ctx.disconnect(promise);
    }

    @Override // io.netty.channel.ChannelOutboundHandler
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        if (this.decoupleCloseAndGoAway) {
            ctx.close(promise);
            return;
        }
        ChannelPromise promise2 = promise.unvoid();
        if (!ctx.channel().isActive() || !prefaceSent()) {
            ctx.close(promise2);
            return;
        }
        ChannelFuture f = connection().goAwaySent() ? ctx.write(Unpooled.EMPTY_BUFFER) : goAway(ctx, null, ctx.newPromise());
        ctx.flush();
        doGracefulShutdown(ctx, f, promise2);
    }

    private ChannelFutureListener newClosingChannelFutureListener(ChannelHandlerContext ctx, ChannelPromise promise) {
        long gracefulShutdownTimeoutMillis = this.gracefulShutdownTimeoutMillis;
        return gracefulShutdownTimeoutMillis < 0 ? new ClosingChannelFutureListener(ctx, promise) : new ClosingChannelFutureListener(ctx, promise, gracefulShutdownTimeoutMillis, TimeUnit.MILLISECONDS);
    }

    private void doGracefulShutdown(ChannelHandlerContext ctx, ChannelFuture future, ChannelPromise promise) {
        final ChannelFutureListener listener = newClosingChannelFutureListener(ctx, promise);
        if (isGracefulShutdownComplete()) {
            future.addListener((GenericFutureListener<? extends Future<? super Void>>) listener);
            return;
        }
        if (this.closeListener == null) {
            this.closeListener = listener;
        } else if (promise != null) {
            final ChannelFutureListener oldCloseListener = this.closeListener;
            this.closeListener = new ChannelFutureListener() { // from class: io.netty.handler.codec.http2.Http2ConnectionHandler.1
                @Override // io.netty.util.concurrent.GenericFutureListener
                public void operationComplete(ChannelFuture future2) throws Exception {
                    try {
                        oldCloseListener.operationComplete(future2);
                    } finally {
                        listener.operationComplete(future2);
                    }
                }
            };
        }
    }

    @Override // io.netty.channel.ChannelOutboundHandler
    public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        ctx.deregister(promise);
    }

    @Override // io.netty.channel.ChannelOutboundHandler
    public void read(ChannelHandlerContext ctx) throws Exception {
        ctx.read();
    }

    @Override // io.netty.channel.ChannelOutboundHandler
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ctx.write(msg, promise);
    }

    @Override // io.netty.handler.codec.ByteToMessageDecoder, io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        try {
            channelReadComplete0(ctx);
        } finally {
            flush(ctx);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void channelReadComplete0(ChannelHandlerContext ctx) {
        discardSomeReadBytes();
        if (!ctx.channel().config().isAutoRead()) {
            ctx.read();
        }
        ctx.fireChannelReadComplete();
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler, io.netty.channel.ChannelInboundHandler
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (Http2CodecUtil.getEmbeddedHttp2Exception(cause) != null) {
            onError(ctx, false, cause);
        } else {
            super.exceptionCaught(ctx, cause);
        }
    }

    @Override // io.netty.handler.codec.http2.Http2LifecycleManager
    public void closeStreamLocal(Http2Stream stream, ChannelFuture future) {
        switch (stream.state()) {
            case HALF_CLOSED_LOCAL:
            case OPEN:
                stream.closeLocalSide();
                return;
            default:
                closeStream(stream, future);
                return;
        }
    }

    @Override // io.netty.handler.codec.http2.Http2LifecycleManager
    public void closeStreamRemote(Http2Stream stream, ChannelFuture future) {
        switch (stream.state()) {
            case OPEN:
            case HALF_CLOSED_REMOTE:
                stream.closeRemoteSide();
                return;
            default:
                closeStream(stream, future);
                return;
        }
    }

    @Override // io.netty.handler.codec.http2.Http2LifecycleManager
    public void closeStream(final Http2Stream stream, ChannelFuture future) {
        if (future.isDone()) {
            doCloseStream(stream, future);
        } else {
            future.addListener((GenericFutureListener<? extends Future<? super Void>>) new ChannelFutureListener() { // from class: io.netty.handler.codec.http2.Http2ConnectionHandler.2
                @Override // io.netty.util.concurrent.GenericFutureListener
                public void operationComplete(ChannelFuture future2) {
                    Http2ConnectionHandler.this.doCloseStream(stream, future2);
                }
            });
        }
    }

    @Override // io.netty.handler.codec.http2.Http2LifecycleManager
    public void onError(ChannelHandlerContext ctx, boolean outbound, Throwable cause) {
        Http2Exception embedded = Http2CodecUtil.getEmbeddedHttp2Exception(cause);
        if (Http2Exception.isStreamError(embedded)) {
            onStreamError(ctx, outbound, cause, (Http2Exception.StreamException) embedded);
        } else if (embedded instanceof Http2Exception.CompositeStreamException) {
            Http2Exception.CompositeStreamException compositException = (Http2Exception.CompositeStreamException) embedded;
            Iterator<Http2Exception.StreamException> it2 = compositException.iterator();
            while (it2.hasNext()) {
                Http2Exception.StreamException streamException = it2.next();
                onStreamError(ctx, outbound, cause, streamException);
            }
        } else {
            onConnectionError(ctx, outbound, cause, embedded);
        }
        ctx.flush();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isGracefulShutdownComplete() {
        return connection().numActiveStreams() == 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onConnectionError(ChannelHandlerContext ctx, boolean outbound, Throwable cause, Http2Exception http2Ex) {
        if (http2Ex == null) {
            http2Ex = new Http2Exception(Http2Error.INTERNAL_ERROR, cause.getMessage(), cause);
        }
        ChannelPromise promise = ctx.newPromise();
        ChannelFuture future = goAway(ctx, http2Ex, ctx.newPromise());
        if (http2Ex.shutdownHint() == Http2Exception.ShutdownHint.GRACEFUL_SHUTDOWN) {
            doGracefulShutdown(ctx, future, promise);
        } else {
            future.addListener((GenericFutureListener<? extends Future<? super Void>>) newClosingChannelFutureListener(ctx, promise));
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onStreamError(ChannelHandlerContext ctx, boolean outbound, Throwable cause, Http2Exception.StreamException http2Ex) {
        int streamId = http2Ex.streamId();
        Http2Stream stream = connection().stream(streamId);
        if ((http2Ex instanceof Http2Exception.HeaderListSizeException) && ((Http2Exception.HeaderListSizeException) http2Ex).duringDecode() && connection().isServer()) {
            if (stream == null) {
                try {
                    stream = this.encoder.connection().remote().createStream(streamId, true);
                } catch (Http2Exception e) {
                    resetUnknownStream(ctx, streamId, http2Ex.error().code(), ctx.newPromise());
                    return;
                }
            }
            if (stream != null && !stream.isHeadersSent()) {
                try {
                    handleServerHeaderDecodeSizeError(ctx, stream);
                } catch (Throwable cause2) {
                    onError(ctx, outbound, Http2Exception.connectionError(Http2Error.INTERNAL_ERROR, cause2, "Error DecodeSizeError", new Object[0]));
                }
            }
        }
        if (stream == null) {
            if (!outbound || connection().local().mayHaveCreatedStream(streamId)) {
                resetUnknownStream(ctx, streamId, http2Ex.error().code(), ctx.newPromise());
                return;
            }
            return;
        }
        resetStream(ctx, stream, http2Ex.error().code(), ctx.newPromise());
    }

    protected void handleServerHeaderDecodeSizeError(ChannelHandlerContext ctx, Http2Stream stream) {
        encoder().writeHeaders(ctx, stream.id(), HEADERS_TOO_LARGE_HEADERS, 0, true, ctx.newPromise());
    }

    protected Http2FrameWriter frameWriter() {
        return encoder().frameWriter();
    }

    private ChannelFuture resetUnknownStream(final ChannelHandlerContext ctx, int streamId, long errorCode, ChannelPromise promise) {
        ChannelFuture future = frameWriter().writeRstStream(ctx, streamId, errorCode, promise);
        if (future.isDone()) {
            closeConnectionOnError(ctx, future);
        } else {
            future.addListener((GenericFutureListener<? extends Future<? super Void>>) new ChannelFutureListener() { // from class: io.netty.handler.codec.http2.Http2ConnectionHandler.3
                @Override // io.netty.util.concurrent.GenericFutureListener
                public void operationComplete(ChannelFuture future2) throws Exception {
                    Http2ConnectionHandler.this.closeConnectionOnError(ctx, future2);
                }
            });
        }
        return future;
    }

    @Override // io.netty.handler.codec.http2.Http2LifecycleManager
    public ChannelFuture resetStream(ChannelHandlerContext ctx, int streamId, long errorCode, ChannelPromise promise) {
        Http2Stream stream = connection().stream(streamId);
        if (stream == null) {
            return resetUnknownStream(ctx, streamId, errorCode, promise.unvoid());
        }
        return resetStream(ctx, stream, errorCode, promise);
    }

    private ChannelFuture resetStream(final ChannelHandlerContext ctx, final Http2Stream stream, long errorCode, ChannelPromise promise) {
        ChannelFuture future;
        ChannelPromise promise2 = promise.unvoid();
        if (stream.isResetSent()) {
            return promise2.setSuccess();
        }
        stream.resetSent();
        if (stream.state() == Http2Stream.State.IDLE || (connection().local().created(stream) && !stream.isHeadersSent() && !stream.isPushPromiseSent())) {
            future = promise2.setSuccess();
        } else {
            future = frameWriter().writeRstStream(ctx, stream.id(), errorCode, promise2);
        }
        if (future.isDone()) {
            processRstStreamWriteResult(ctx, stream, future);
        } else {
            future.addListener((GenericFutureListener<? extends Future<? super Void>>) new ChannelFutureListener() { // from class: io.netty.handler.codec.http2.Http2ConnectionHandler.4
                @Override // io.netty.util.concurrent.GenericFutureListener
                public void operationComplete(ChannelFuture future2) throws Exception {
                    Http2ConnectionHandler.this.processRstStreamWriteResult(ctx, stream, future2);
                }
            });
        }
        return future;
    }

    @Override // io.netty.handler.codec.http2.Http2LifecycleManager
    public ChannelFuture goAway(final ChannelHandlerContext ctx, final int lastStreamId, final long errorCode, final ByteBuf debugData, ChannelPromise promise) {
        ChannelPromise promise2 = promise.unvoid();
        Http2Connection connection = connection();
        try {
            if (!connection.goAwaySent(lastStreamId, errorCode, debugData)) {
                debugData.release();
                promise2.trySuccess();
                return promise2;
            }
            debugData.retain();
            ChannelFuture future = frameWriter().writeGoAway(ctx, lastStreamId, errorCode, debugData, promise2);
            if (future.isDone()) {
                processGoAwayWriteResult(ctx, lastStreamId, errorCode, debugData, future);
            } else {
                future.addListener((GenericFutureListener<? extends Future<? super Void>>) new ChannelFutureListener() { // from class: io.netty.handler.codec.http2.Http2ConnectionHandler.5
                    @Override // io.netty.util.concurrent.GenericFutureListener
                    public void operationComplete(ChannelFuture future2) throws Exception {
                        Http2ConnectionHandler.processGoAwayWriteResult(ctx, lastStreamId, errorCode, debugData, future2);
                    }
                });
            }
            return future;
        } catch (Throwable cause) {
            debugData.release();
            promise2.tryFailure(cause);
            return promise2;
        }
    }

    private void checkCloseConnection(ChannelFuture future) {
        if (this.closeListener != null && isGracefulShutdownComplete()) {
            ChannelFutureListener closeListener = this.closeListener;
            this.closeListener = null;
            try {
                closeListener.operationComplete(future);
            } catch (Exception e) {
                throw new IllegalStateException("Close listener threw an unexpected exception", e);
            }
        }
    }

    private ChannelFuture goAway(ChannelHandlerContext ctx, Http2Exception cause, ChannelPromise promise) {
        int lastKnownStream;
        long errorCode = (cause != null ? cause.error() : Http2Error.NO_ERROR).code();
        if (cause != null && cause.shutdownHint() == Http2Exception.ShutdownHint.HARD_SHUTDOWN) {
            lastKnownStream = Integer.MAX_VALUE;
        } else {
            lastKnownStream = connection().remote().lastStreamCreated();
        }
        return goAway(ctx, lastKnownStream, errorCode, Http2CodecUtil.toByteBuf(ctx, cause), promise);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processRstStreamWriteResult(ChannelHandlerContext ctx, Http2Stream stream, ChannelFuture future) {
        if (future.isSuccess()) {
            closeStream(stream, future);
        } else {
            onConnectionError(ctx, true, future.cause(), null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void closeConnectionOnError(ChannelHandlerContext ctx, ChannelFuture future) {
        if (!future.isSuccess()) {
            onConnectionError(ctx, true, future.cause(), null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doCloseStream(Http2Stream stream, ChannelFuture future) {
        stream.close();
        checkCloseConnection(future);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static ByteBuf clientPrefaceString(Http2Connection connection) {
        if (connection.isServer()) {
            return Http2CodecUtil.connectionPrefaceBuf();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void processGoAwayWriteResult(ChannelHandlerContext ctx, int lastStreamId, long errorCode, ByteBuf debugData, ChannelFuture future) {
        try {
            if (!future.isSuccess()) {
                if (logger.isDebugEnabled()) {
                    logger.debug("{} Sending GOAWAY failed: lastStreamId '{}', errorCode '{}', debugData '{}'. Forcing shutdown of the connection.", ctx.channel(), Integer.valueOf(lastStreamId), Long.valueOf(errorCode), debugData.toString(CharsetUtil.UTF_8), future.cause());
                }
                ctx.close();
            } else if (errorCode != Http2Error.NO_ERROR.code()) {
                if (logger.isDebugEnabled()) {
                    logger.debug("{} Sent GOAWAY: lastStreamId '{}', errorCode '{}', debugData '{}'. Forcing shutdown of the connection.", ctx.channel(), Integer.valueOf(lastStreamId), Long.valueOf(errorCode), debugData.toString(CharsetUtil.UTF_8), future.cause());
                }
                ctx.close();
            }
        } finally {
            debugData.release();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class ClosingChannelFutureListener implements ChannelFutureListener {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private boolean closed;
        private final ChannelHandlerContext ctx;
        private final ChannelPromise promise;
        private final Future<?> timeoutTask;

        ClosingChannelFutureListener(ChannelHandlerContext ctx, ChannelPromise promise) {
            this.ctx = ctx;
            this.promise = promise;
            this.timeoutTask = null;
        }

        ClosingChannelFutureListener(ChannelHandlerContext ctx, ChannelPromise promise, long timeout, TimeUnit unit) {
            this.ctx = ctx;
            this.promise = promise;
            this.timeoutTask = ctx.executor().schedule(new Runnable() { // from class: io.netty.handler.codec.http2.Http2ConnectionHandler.ClosingChannelFutureListener.1
                @Override // java.lang.Runnable
                public void run() {
                    ClosingChannelFutureListener.this.doClose();
                }
            }, timeout, unit);
        }

        @Override // io.netty.util.concurrent.GenericFutureListener
        public void operationComplete(ChannelFuture sentGoAwayFuture) {
            if (this.timeoutTask != null) {
                this.timeoutTask.cancel(false);
            }
            doClose();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void doClose() {
            if (this.closed) {
                if (this.timeoutTask == null) {
                    throw new AssertionError();
                }
                return;
            }
            this.closed = true;
            if (this.promise == null) {
                this.ctx.close();
            } else {
                this.ctx.close(this.promise);
            }
        }
    }
}
