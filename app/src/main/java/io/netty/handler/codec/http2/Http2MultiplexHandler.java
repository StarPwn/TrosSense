package io.netty.handler.codec.http2;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ServerChannel;
import io.netty.channel.socket.ChannelInputShutdownReadComplete;
import io.netty.channel.socket.ChannelOutputShutdownEvent;
import io.netty.handler.codec.http2.Http2FrameCodec;
import io.netty.handler.codec.http2.Http2FrameStreamEvent;
import io.netty.handler.ssl.SslCloseCompletionEvent;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.internal.ObjectUtil;
import java.util.ArrayDeque;
import java.util.Queue;
import javax.net.ssl.SSLException;

/* loaded from: classes4.dex */
public final class Http2MultiplexHandler extends Http2ChannelDuplexHandler {
    static final ChannelFutureListener CHILD_CHANNEL_REGISTRATION_LISTENER = new ChannelFutureListener() { // from class: io.netty.handler.codec.http2.Http2MultiplexHandler.1
        @Override // io.netty.util.concurrent.GenericFutureListener
        public void operationComplete(ChannelFuture future) {
            Http2MultiplexHandler.registerDone(future);
        }
    };
    private volatile ChannelHandlerContext ctx;
    private int idCount;
    private final ChannelHandler inboundStreamHandler;
    private boolean parentReadInProgress;
    private final Queue<AbstractHttp2StreamChannel> readCompletePendingQueue;
    private final ChannelHandler upgradeStreamHandler;

    static /* synthetic */ int access$004(Http2MultiplexHandler x0) {
        int i = x0.idCount + 1;
        x0.idCount = i;
        return i;
    }

    public Http2MultiplexHandler(ChannelHandler inboundStreamHandler) {
        this(inboundStreamHandler, null);
    }

    public Http2MultiplexHandler(ChannelHandler inboundStreamHandler, ChannelHandler upgradeStreamHandler) {
        this.readCompletePendingQueue = new MaxCapacityQueue(new ArrayDeque(8), 100);
        this.inboundStreamHandler = (ChannelHandler) ObjectUtil.checkNotNull(inboundStreamHandler, "inboundStreamHandler");
        this.upgradeStreamHandler = upgradeStreamHandler;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void registerDone(ChannelFuture future) {
        if (!future.isSuccess()) {
            Channel childChannel = future.channel();
            if (childChannel.isRegistered()) {
                childChannel.close();
            } else {
                childChannel.unsafe().closeForcibly();
            }
        }
    }

    @Override // io.netty.handler.codec.http2.Http2ChannelDuplexHandler
    protected void handlerAdded0(ChannelHandlerContext ctx) {
        if (ctx.executor() != ctx.channel().eventLoop()) {
            throw new IllegalStateException("EventExecutor must be EventLoop of Channel");
        }
        this.ctx = ctx;
    }

    @Override // io.netty.handler.codec.http2.Http2ChannelDuplexHandler
    protected void handlerRemoved0(ChannelHandlerContext ctx) {
        this.readCompletePendingQueue.clear();
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        this.parentReadInProgress = true;
        if (msg instanceof Http2StreamFrame) {
            if (msg instanceof Http2WindowUpdateFrame) {
                return;
            }
            Http2StreamFrame streamFrame = (Http2StreamFrame) msg;
            Http2FrameCodec.DefaultHttp2FrameStream s = (Http2FrameCodec.DefaultHttp2FrameStream) streamFrame.stream();
            AbstractHttp2StreamChannel channel = (AbstractHttp2StreamChannel) s.attachment;
            if (msg instanceof Http2ResetFrame) {
                channel.pipeline().fireUserEventTriggered(msg);
                return;
            } else {
                channel.fireChildRead(streamFrame);
                return;
            }
        }
        if (msg instanceof Http2GoAwayFrame) {
            onHttp2GoAwayFrame(ctx, (Http2GoAwayFrame) msg);
        }
        ctx.fireChannelRead(msg);
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        if (ctx.channel().isWritable()) {
            forEachActiveStream(AbstractHttp2StreamChannel.WRITABLE_VISITOR);
        }
        ctx.fireChannelWritabilityChanged();
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        AbstractHttp2StreamChannel ch;
        if (evt instanceof Http2FrameStreamEvent) {
            Http2FrameStreamEvent event = (Http2FrameStreamEvent) evt;
            Http2FrameCodec.DefaultHttp2FrameStream stream = (Http2FrameCodec.DefaultHttp2FrameStream) event.stream();
            if (event.type() == Http2FrameStreamEvent.Type.State) {
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
                    if (stream.id() == 1 && !isServer(ctx)) {
                        if (this.upgradeStreamHandler == null) {
                            throw Http2Exception.connectionError(Http2Error.INTERNAL_ERROR, "Client is misconfigured for upgrade requests", new Object[0]);
                        }
                        ch = new Http2MultiplexHandlerStreamChannel(stream, this.upgradeStreamHandler);
                        ch.closeOutbound();
                    } else {
                        ch = new Http2MultiplexHandlerStreamChannel(stream, this.inboundStreamHandler);
                    }
                    ChannelFuture future = ctx.channel().eventLoop().register(ch);
                    if (future.isDone()) {
                        registerDone(future);
                        return;
                    } else {
                        future.addListener((GenericFutureListener<? extends Future<? super Void>>) CHILD_CHANNEL_REGISTRATION_LISTENER);
                        return;
                    }
                }
                return;
            }
            return;
        }
        if (evt == ChannelInputShutdownReadComplete.INSTANCE) {
            forEachActiveStream(AbstractHttp2StreamChannel.CHANNEL_INPUT_SHUTDOWN_READ_COMPLETE_VISITOR);
        } else if (evt == ChannelOutputShutdownEvent.INSTANCE) {
            forEachActiveStream(AbstractHttp2StreamChannel.CHANNEL_OUTPUT_SHUTDOWN_EVENT_VISITOR);
        } else if (evt == SslCloseCompletionEvent.SUCCESS) {
            forEachActiveStream(AbstractHttp2StreamChannel.SSL_CLOSE_COMPLETION_EVENT_VISITOR);
        }
        ctx.fireUserEventTriggered(evt);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Http2StreamChannel newOutboundStream() {
        return new Http2MultiplexHandlerStreamChannel((Http2FrameCodec.DefaultHttp2FrameStream) newStream(), null);
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler, io.netty.channel.ChannelInboundHandler
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof Http2FrameStreamException) {
            Http2FrameStreamException exception = (Http2FrameStreamException) cause;
            Http2FrameStream stream = exception.stream();
            AbstractHttp2StreamChannel childChannel = (AbstractHttp2StreamChannel) ((Http2FrameCodec.DefaultHttp2FrameStream) stream).attachment;
            try {
                childChannel.pipeline().fireExceptionCaught(cause.getCause());
                return;
            } finally {
                childChannel.closeWithError(exception.error());
            }
        }
        if (cause instanceof Http2MultiplexActiveStreamsException) {
            fireExceptionCaughtForActiveStream(cause.getCause());
            return;
        }
        if (cause.getCause() instanceof SSLException) {
            fireExceptionCaughtForActiveStream(cause);
        }
        ctx.fireExceptionCaught(cause);
    }

    private void fireExceptionCaughtForActiveStream(final Throwable cause) throws Http2Exception {
        forEachActiveStream(new Http2FrameStreamVisitor() { // from class: io.netty.handler.codec.http2.Http2MultiplexHandler.2
            @Override // io.netty.handler.codec.http2.Http2FrameStreamVisitor
            public boolean visit(Http2FrameStream stream) {
                AbstractHttp2StreamChannel childChannel = (AbstractHttp2StreamChannel) ((Http2FrameCodec.DefaultHttp2FrameStream) stream).attachment;
                childChannel.pipeline().fireExceptionCaught(cause);
                return true;
            }
        });
    }

    private static boolean isServer(ChannelHandlerContext ctx) {
        return ctx.channel().parent() instanceof ServerChannel;
    }

    private void onHttp2GoAwayFrame(ChannelHandlerContext ctx, final Http2GoAwayFrame goAwayFrame) {
        if (goAwayFrame.lastStreamId() == Integer.MAX_VALUE) {
            return;
        }
        try {
            final boolean server = isServer(ctx);
            forEachActiveStream(new Http2FrameStreamVisitor() { // from class: io.netty.handler.codec.http2.Http2MultiplexHandler.3
                @Override // io.netty.handler.codec.http2.Http2FrameStreamVisitor
                public boolean visit(Http2FrameStream stream) {
                    int streamId = stream.id();
                    if (streamId > goAwayFrame.lastStreamId() && Http2CodecUtil.isStreamIdValid(streamId, server)) {
                        AbstractHttp2StreamChannel childChannel = (AbstractHttp2StreamChannel) ((Http2FrameCodec.DefaultHttp2FrameStream) stream).attachment;
                        childChannel.pipeline().fireUserEventTriggered((Object) goAwayFrame.retainedDuplicate());
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

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        processPendingReadCompleteQueue();
        ctx.fireChannelReadComplete();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processPendingReadCompleteQueue() {
        this.parentReadInProgress = true;
        AbstractHttp2StreamChannel childChannel = this.readCompletePendingQueue.poll();
        if (childChannel == null) {
            this.parentReadInProgress = false;
            return;
        }
        do {
            try {
                childChannel.fireChildReadComplete();
                childChannel = this.readCompletePendingQueue.poll();
            } finally {
                this.parentReadInProgress = false;
                this.readCompletePendingQueue.clear();
                this.ctx.flush();
            }
        } while (childChannel != null);
    }

    /* loaded from: classes4.dex */
    private final class Http2MultiplexHandlerStreamChannel extends AbstractHttp2StreamChannel {
        Http2MultiplexHandlerStreamChannel(Http2FrameCodec.DefaultHttp2FrameStream stream, ChannelHandler inboundHandler) {
            super(stream, Http2MultiplexHandler.access$004(Http2MultiplexHandler.this), inboundHandler);
        }

        @Override // io.netty.handler.codec.http2.AbstractHttp2StreamChannel
        protected boolean isParentReadInProgress() {
            return Http2MultiplexHandler.this.parentReadInProgress;
        }

        @Override // io.netty.handler.codec.http2.AbstractHttp2StreamChannel
        protected void addChannelToReadCompletePendingQueue() {
            while (!Http2MultiplexHandler.this.readCompletePendingQueue.offer(this)) {
                Http2MultiplexHandler.this.processPendingReadCompleteQueue();
            }
        }

        @Override // io.netty.handler.codec.http2.AbstractHttp2StreamChannel
        protected ChannelHandlerContext parentContext() {
            return Http2MultiplexHandler.this.ctx;
        }
    }
}
