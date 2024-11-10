package io.netty.handler.codec.http.websocketx;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.PromiseNotifier;
import java.net.SocketAddress;
import java.nio.channels.ClosedChannelException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/* loaded from: classes4.dex */
abstract class WebSocketProtocolHandler extends MessageToMessageDecoder<WebSocketFrame> implements ChannelOutboundHandler {
    private ChannelPromise closeSent;
    private final WebSocketCloseStatus closeStatus;
    private final boolean dropPongFrames;
    private final long forceCloseTimeoutMillis;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageToMessageDecoder
    public /* bridge */ /* synthetic */ void decode(ChannelHandlerContext channelHandlerContext, WebSocketFrame webSocketFrame, List list) throws Exception {
        decode(channelHandlerContext, webSocketFrame, (List<Object>) list);
    }

    WebSocketProtocolHandler() {
        this(true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public WebSocketProtocolHandler(boolean dropPongFrames) {
        this(dropPongFrames, null, 0L);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public WebSocketProtocolHandler(boolean dropPongFrames, WebSocketCloseStatus closeStatus, long forceCloseTimeoutMillis) {
        this.dropPongFrames = dropPongFrames;
        this.closeStatus = closeStatus;
        this.forceCloseTimeoutMillis = forceCloseTimeoutMillis;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    public void decode(ChannelHandlerContext ctx, WebSocketFrame frame, List<Object> out) throws Exception {
        if (frame instanceof PingWebSocketFrame) {
            frame.content().retain();
            ctx.writeAndFlush(new PongWebSocketFrame(frame.content()));
            readIfNeeded(ctx);
        } else if ((frame instanceof PongWebSocketFrame) && this.dropPongFrames) {
            readIfNeeded(ctx);
        } else {
            out.add(frame.retain());
        }
    }

    private static void readIfNeeded(ChannelHandlerContext ctx) {
        if (!ctx.channel().config().isAutoRead()) {
            ctx.read();
        }
    }

    public void close(final ChannelHandlerContext ctx, final ChannelPromise promise) throws Exception {
        if (this.closeStatus == null || !ctx.channel().isActive()) {
            ctx.close(promise);
            return;
        }
        if (this.closeSent == null) {
            write(ctx, new CloseWebSocketFrame(this.closeStatus), ctx.newPromise());
        }
        flush(ctx);
        applyCloseSentTimeout(ctx);
        this.closeSent.addListener((GenericFutureListener<? extends Future<? super Void>>) new ChannelFutureListener() { // from class: io.netty.handler.codec.http.websocketx.WebSocketProtocolHandler.1
            @Override // io.netty.util.concurrent.GenericFutureListener
            public void operationComplete(ChannelFuture future) {
                ctx.close(promise);
            }
        });
    }

    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (this.closeSent != null) {
            ReferenceCountUtil.release(msg);
            promise.setFailure((Throwable) new ClosedChannelException());
        } else if (msg instanceof CloseWebSocketFrame) {
            closeSent(promise.unvoid());
            ctx.write(msg).addListener((GenericFutureListener<? extends Future<? super Void>>) new PromiseNotifier(false, this.closeSent));
        } else {
            ctx.write(msg, promise);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void closeSent(ChannelPromise promise) {
        this.closeSent = promise;
    }

    private void applyCloseSentTimeout(ChannelHandlerContext ctx) {
        if (this.closeSent.isDone() || this.forceCloseTimeoutMillis < 0) {
            return;
        }
        final Future<?> timeoutTask = ctx.executor().schedule(new Runnable() { // from class: io.netty.handler.codec.http.websocketx.WebSocketProtocolHandler.2
            @Override // java.lang.Runnable
            public void run() {
                if (!WebSocketProtocolHandler.this.closeSent.isDone()) {
                    WebSocketProtocolHandler.this.closeSent.tryFailure(WebSocketProtocolHandler.this.buildHandshakeException("send close frame timed out"));
                }
            }
        }, this.forceCloseTimeoutMillis, TimeUnit.MILLISECONDS);
        this.closeSent.addListener((GenericFutureListener<? extends Future<? super Void>>) new ChannelFutureListener() { // from class: io.netty.handler.codec.http.websocketx.WebSocketProtocolHandler.3
            @Override // io.netty.util.concurrent.GenericFutureListener
            public void operationComplete(ChannelFuture future) {
                timeoutTask.cancel(false);
            }
        });
    }

    protected WebSocketHandshakeException buildHandshakeException(String message) {
        return new WebSocketHandshakeException(message);
    }

    public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) throws Exception {
        ctx.bind(localAddress, promise);
    }

    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
        ctx.connect(remoteAddress, localAddress, promise);
    }

    public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        ctx.disconnect(promise);
    }

    public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        ctx.deregister(promise);
    }

    public void read(ChannelHandlerContext ctx) throws Exception {
        ctx.read();
    }

    public void flush(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler, io.netty.channel.ChannelInboundHandler
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.fireExceptionCaught(cause);
        ctx.close();
    }
}
