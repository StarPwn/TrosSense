package io.netty.handler.codec.http2;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.socket.ChannelInputShutdownReadComplete;
import io.netty.channel.socket.ChannelOutputShutdownEvent;
import io.netty.handler.codec.http2.Http2FrameCodec;
import io.netty.handler.ssl.SslCloseCompletionEvent;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.util.ArrayDeque;
import java.util.Queue;

@Deprecated
/* loaded from: classes4.dex */
public class Http2MultiplexCodec extends Http2FrameCodec {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    volatile ChannelHandlerContext ctx;
    private int idCount;
    private final ChannelHandler inboundStreamHandler;
    private boolean parentReadInProgress;
    private final Queue<AbstractHttp2StreamChannel> readCompletePendingQueue;
    private final ChannelHandler upgradeStreamHandler;

    static /* synthetic */ int access$004(Http2MultiplexCodec x0) {
        int i = x0.idCount + 1;
        x0.idCount = i;
        return i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Http2MultiplexCodec(Http2ConnectionEncoder encoder, Http2ConnectionDecoder decoder, Http2Settings initialSettings, ChannelHandler inboundStreamHandler, ChannelHandler upgradeStreamHandler, boolean decoupleCloseAndGoAway, boolean flushPreface) {
        super(encoder, decoder, initialSettings, decoupleCloseAndGoAway, flushPreface);
        this.readCompletePendingQueue = new MaxCapacityQueue(new ArrayDeque(8), 100);
        this.inboundStreamHandler = inboundStreamHandler;
        this.upgradeStreamHandler = upgradeStreamHandler;
    }

    @Override // io.netty.handler.codec.http2.Http2ConnectionHandler
    public void onHttpClientUpgrade() throws Http2Exception {
        if (this.upgradeStreamHandler == null) {
            throw Http2Exception.connectionError(Http2Error.INTERNAL_ERROR, "Client is misconfigured for upgrade requests", new Object[0]);
        }
        super.onHttpClientUpgrade();
    }

    @Override // io.netty.handler.codec.http2.Http2FrameCodec
    public final void handlerAdded0(ChannelHandlerContext ctx) throws Exception {
        if (ctx.executor() != ctx.channel().eventLoop()) {
            throw new IllegalStateException("EventExecutor must be EventLoop of Channel");
        }
        this.ctx = ctx;
    }

    @Override // io.netty.handler.codec.http2.Http2ConnectionHandler, io.netty.handler.codec.ByteToMessageDecoder
    public final void handlerRemoved0(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved0(ctx);
        this.readCompletePendingQueue.clear();
    }

    @Override // io.netty.handler.codec.http2.Http2FrameCodec
    final void onHttp2Frame(ChannelHandlerContext ctx, Http2Frame frame) {
        if (frame instanceof Http2StreamFrame) {
            Http2StreamFrame streamFrame = (Http2StreamFrame) frame;
            AbstractHttp2StreamChannel channel = (AbstractHttp2StreamChannel) ((Http2FrameCodec.DefaultHttp2FrameStream) streamFrame.stream()).attachment;
            channel.fireChildRead(streamFrame);
        } else {
            if (frame instanceof Http2GoAwayFrame) {
                onHttp2GoAwayFrame(ctx, (Http2GoAwayFrame) frame);
            }
            ctx.fireChannelRead((Object) frame);
        }
    }

    @Override // io.netty.handler.codec.http2.Http2FrameCodec
    final void onHttp2StreamStateChanged(ChannelHandlerContext ctx, Http2FrameCodec.DefaultHttp2FrameStream stream) {
        Http2MultiplexCodecStreamChannel streamChannel;
        switch (stream.state()) {
            case HALF_CLOSED_LOCAL:
                if (stream.id() != 1) {
                    return;
                }
                break;
            case HALF_CLOSED_REMOTE:
            case OPEN:
                break;
            case CLOSED:
                AbstractHttp2StreamChannel channel = (AbstractHttp2StreamChannel) stream.attachment;
                if (channel != null) {
                    channel.streamClosed();
                    return;
                }
                return;
            default:
                return;
        }
        if (stream.attachment == null) {
            if (stream.id() == 1 && !connection().isServer()) {
                if (this.upgradeStreamHandler == null) {
                    throw new AssertionError();
                }
                streamChannel = new Http2MultiplexCodecStreamChannel(stream, this.upgradeStreamHandler);
                streamChannel.closeOutbound();
            } else {
                streamChannel = new Http2MultiplexCodecStreamChannel(stream, this.inboundStreamHandler);
            }
            ChannelFuture future = ctx.channel().eventLoop().register(streamChannel);
            if (future.isDone()) {
                Http2MultiplexHandler.registerDone(future);
            } else {
                future.addListener((GenericFutureListener<? extends Future<? super Void>>) Http2MultiplexHandler.CHILD_CHANNEL_REGISTRATION_LISTENER);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Http2StreamChannel newOutboundStream() {
        return new Http2MultiplexCodecStreamChannel(newStream(), null);
    }

    @Override // io.netty.handler.codec.http2.Http2FrameCodec
    final void onHttp2FrameStreamException(ChannelHandlerContext ctx, Http2FrameStreamException cause) {
        Http2FrameStream stream = cause.stream();
        AbstractHttp2StreamChannel channel = (AbstractHttp2StreamChannel) ((Http2FrameCodec.DefaultHttp2FrameStream) stream).attachment;
        try {
            channel.pipeline().fireExceptionCaught(cause.getCause());
        } finally {
            channel.closeWithError(cause.error());
        }
    }

    private void onHttp2GoAwayFrame(ChannelHandlerContext ctx, final Http2GoAwayFrame goAwayFrame) {
        if (goAwayFrame.lastStreamId() == Integer.MAX_VALUE) {
            return;
        }
        try {
            forEachActiveStream(new Http2FrameStreamVisitor() { // from class: io.netty.handler.codec.http2.Http2MultiplexCodec.1
                @Override // io.netty.handler.codec.http2.Http2FrameStreamVisitor
                public boolean visit(Http2FrameStream stream) {
                    int streamId = stream.id();
                    AbstractHttp2StreamChannel channel = (AbstractHttp2StreamChannel) ((Http2FrameCodec.DefaultHttp2FrameStream) stream).attachment;
                    if (streamId > goAwayFrame.lastStreamId() && Http2MultiplexCodec.this.connection().local().isValidStreamId(streamId)) {
                        channel.pipeline().fireUserEventTriggered((Object) goAwayFrame.retainedDuplicate());
                        return true;
                    }
                    return true;
                }
            });
        } catch (Http2Exception e) {
            ctx.fireExceptionCaught((Throwable) e);
            ctx.close();
        }
    }

    @Override // io.netty.handler.codec.http2.Http2ConnectionHandler, io.netty.handler.codec.ByteToMessageDecoder, io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public final void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        processPendingReadCompleteQueue();
        channelReadComplete0(ctx);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processPendingReadCompleteQueue() {
        this.parentReadInProgress = true;
        while (true) {
            try {
                AbstractHttp2StreamChannel childChannel = this.readCompletePendingQueue.poll();
                if (childChannel != null) {
                    childChannel.fireChildReadComplete();
                } else {
                    return;
                }
            } finally {
                this.parentReadInProgress = false;
                this.readCompletePendingQueue.clear();
                flush0(this.ctx);
            }
        }
    }

    @Override // io.netty.handler.codec.ByteToMessageDecoder, io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public final void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        this.parentReadInProgress = true;
        super.channelRead(ctx, msg);
    }

    @Override // io.netty.handler.codec.http2.Http2ConnectionHandler, io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public final void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        if (ctx.channel().isWritable()) {
            forEachActiveStream(AbstractHttp2StreamChannel.WRITABLE_VISITOR);
        }
        super.channelWritabilityChanged(ctx);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // io.netty.handler.codec.http2.Http2FrameCodec
    public final void onUserEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt == ChannelInputShutdownReadComplete.INSTANCE) {
            forEachActiveStream(AbstractHttp2StreamChannel.CHANNEL_INPUT_SHUTDOWN_READ_COMPLETE_VISITOR);
        } else if (evt == ChannelOutputShutdownEvent.INSTANCE) {
            forEachActiveStream(AbstractHttp2StreamChannel.CHANNEL_OUTPUT_SHUTDOWN_EVENT_VISITOR);
        } else if (evt == SslCloseCompletionEvent.SUCCESS) {
            forEachActiveStream(AbstractHttp2StreamChannel.SSL_CLOSE_COMPLETION_EVENT_VISITOR);
        }
        super.onUserEventTriggered(ctx, evt);
    }

    final void flush0(ChannelHandlerContext ctx) {
        flush(ctx);
    }

    /* loaded from: classes4.dex */
    private final class Http2MultiplexCodecStreamChannel extends AbstractHttp2StreamChannel {
        Http2MultiplexCodecStreamChannel(Http2FrameCodec.DefaultHttp2FrameStream stream, ChannelHandler inboundHandler) {
            super(stream, Http2MultiplexCodec.access$004(Http2MultiplexCodec.this), inboundHandler);
        }

        @Override // io.netty.handler.codec.http2.AbstractHttp2StreamChannel
        protected boolean isParentReadInProgress() {
            return Http2MultiplexCodec.this.parentReadInProgress;
        }

        @Override // io.netty.handler.codec.http2.AbstractHttp2StreamChannel
        protected void addChannelToReadCompletePendingQueue() {
            while (!Http2MultiplexCodec.this.readCompletePendingQueue.offer(this)) {
                Http2MultiplexCodec.this.processPendingReadCompleteQueue();
            }
        }

        @Override // io.netty.handler.codec.http2.AbstractHttp2StreamChannel
        protected ChannelHandlerContext parentContext() {
            return Http2MultiplexCodec.this.ctx;
        }

        @Override // io.netty.handler.codec.http2.AbstractHttp2StreamChannel
        protected ChannelFuture write0(ChannelHandlerContext ctx, Object msg) {
            ChannelPromise promise = ctx.newPromise();
            Http2MultiplexCodec.this.write(ctx, msg, promise);
            return promise;
        }

        @Override // io.netty.handler.codec.http2.AbstractHttp2StreamChannel
        protected void flush0(ChannelHandlerContext ctx) {
            Http2MultiplexCodec.this.flush0(ctx);
        }
    }
}
