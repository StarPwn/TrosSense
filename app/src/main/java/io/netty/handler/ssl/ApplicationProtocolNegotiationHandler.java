package io.netty.handler.ssl;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.DecoderException;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.RecyclableArrayList;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import javax.net.ssl.SSLException;

/* loaded from: classes4.dex */
public abstract class ApplicationProtocolNegotiationHandler extends ChannelInboundHandlerAdapter {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) ApplicationProtocolNegotiationHandler.class);
    private final RecyclableArrayList bufferedMessages = RecyclableArrayList.newInstance();
    private ChannelHandlerContext ctx;
    private final String fallbackProtocol;
    private boolean sslHandlerChecked;

    protected abstract void configurePipeline(ChannelHandlerContext channelHandlerContext, String str) throws Exception;

    protected ApplicationProtocolNegotiationHandler(String fallbackProtocol) {
        this.fallbackProtocol = (String) ObjectUtil.checkNotNull(fallbackProtocol, "fallbackProtocol");
    }

    @Override // io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
        super.handlerAdded(ctx);
    }

    @Override // io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        fireBufferedMessages();
        this.bufferedMessages.recycle();
        super.handlerRemoved(ctx);
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        this.bufferedMessages.add(msg);
        if (!this.sslHandlerChecked) {
            this.sslHandlerChecked = true;
            if (ctx.pipeline().get(SslHandler.class) == null) {
                removeSelfIfPresent(ctx);
            }
        }
    }

    private void fireBufferedMessages() {
        if (!this.bufferedMessages.isEmpty()) {
            for (int i = 0; i < this.bufferedMessages.size(); i++) {
                this.ctx.fireChannelRead(this.bufferedMessages.get(i));
            }
            this.ctx.fireChannelReadComplete();
            this.bufferedMessages.clear();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:24:0x0043, code lost:            if (r0.isSuccess() == false) goto L29;     */
    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void userEventTriggered(io.netty.channel.ChannelHandlerContext r5, java.lang.Object r6) throws java.lang.Exception {
        /*
            r4 = this;
            boolean r0 = r6 instanceof io.netty.handler.ssl.SslHandshakeCompletionEvent
            if (r0 == 0) goto L51
            r0 = r6
            io.netty.handler.ssl.SslHandshakeCompletionEvent r0 = (io.netty.handler.ssl.SslHandshakeCompletionEvent) r0
            boolean r1 = r0.isSuccess()     // Catch: java.lang.Throwable -> L3b
            if (r1 == 0) goto L31
            io.netty.channel.ChannelPipeline r1 = r5.pipeline()     // Catch: java.lang.Throwable -> L3b
            java.lang.Class<io.netty.handler.ssl.SslHandler> r2 = io.netty.handler.ssl.SslHandler.class
            io.netty.channel.ChannelHandler r1 = r1.get(r2)     // Catch: java.lang.Throwable -> L3b
            io.netty.handler.ssl.SslHandler r1 = (io.netty.handler.ssl.SslHandler) r1     // Catch: java.lang.Throwable -> L3b
            if (r1 == 0) goto L29
            java.lang.String r2 = r1.applicationProtocol()     // Catch: java.lang.Throwable -> L3b
            if (r2 == 0) goto L23
            r3 = r2
            goto L25
        L23:
            java.lang.String r3 = r4.fallbackProtocol     // Catch: java.lang.Throwable -> L3b
        L25:
            r4.configurePipeline(r5, r3)     // Catch: java.lang.Throwable -> L3b
            goto L31
        L29:
            java.lang.IllegalStateException r2 = new java.lang.IllegalStateException     // Catch: java.lang.Throwable -> L3b
            java.lang.String r3 = "cannot find an SslHandler in the pipeline (required for application-level protocol negotiation)"
            r2.<init>(r3)     // Catch: java.lang.Throwable -> L3b
            throw r2     // Catch: java.lang.Throwable -> L3b
        L31:
            boolean r1 = r0.isSuccess()
            if (r1 == 0) goto L51
        L37:
            r4.removeSelfIfPresent(r5)
            goto L51
        L3b:
            r1 = move-exception
            r4.exceptionCaught(r5, r1)     // Catch: java.lang.Throwable -> L46
            boolean r1 = r0.isSuccess()
            if (r1 == 0) goto L51
            goto L37
        L46:
            r1 = move-exception
            boolean r2 = r0.isSuccess()
            if (r2 == 0) goto L50
            r4.removeSelfIfPresent(r5)
        L50:
            throw r1
        L51:
            boolean r0 = r6 instanceof io.netty.channel.socket.ChannelInputShutdownEvent
            if (r0 == 0) goto L58
            r4.fireBufferedMessages()
        L58:
            r5.fireUserEventTriggered(r6)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.handler.ssl.ApplicationProtocolNegotiationHandler.userEventTriggered(io.netty.channel.ChannelHandlerContext, java.lang.Object):void");
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        fireBufferedMessages();
        super.channelInactive(ctx);
    }

    private void removeSelfIfPresent(ChannelHandlerContext ctx) {
        ChannelPipeline pipeline = ctx.pipeline();
        if (!ctx.isRemoved()) {
            pipeline.remove(this);
        }
    }

    protected void handshakeFailure(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.warn("{} TLS handshake failed:", ctx.channel(), cause);
        ctx.close();
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler, io.netty.channel.ChannelInboundHandler
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof DecoderException) {
            Throwable wrapped = cause.getCause();
            if (wrapped instanceof SSLException) {
                try {
                    handshakeFailure(ctx, wrapped);
                    return;
                } finally {
                    removeSelfIfPresent(ctx);
                }
            }
        }
        logger.warn("{} Failed to select the application-level protocol:", ctx.channel(), cause);
        ctx.fireExceptionCaught(cause);
        ctx.close();
    }
}
