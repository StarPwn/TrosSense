package io.netty.handler.ssl.ocsp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.ssl.ReferenceCountedOpenSslEngine;
import io.netty.handler.ssl.SslHandshakeCompletionEvent;
import io.netty.util.internal.ObjectUtil;
import javax.net.ssl.SSLHandshakeException;

/* loaded from: classes4.dex */
public abstract class OcspClientHandler extends ChannelInboundHandlerAdapter {
    private final ReferenceCountedOpenSslEngine engine;

    protected abstract boolean verify(ChannelHandlerContext channelHandlerContext, ReferenceCountedOpenSslEngine referenceCountedOpenSslEngine) throws Exception;

    protected OcspClientHandler(ReferenceCountedOpenSslEngine engine) {
        this.engine = (ReferenceCountedOpenSslEngine) ObjectUtil.checkNotNull(engine, "engine");
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof SslHandshakeCompletionEvent) {
            ctx.pipeline().remove(this);
            SslHandshakeCompletionEvent event = (SslHandshakeCompletionEvent) evt;
            if (event.isSuccess() && !verify(ctx, this.engine)) {
                throw new SSLHandshakeException("Bad OCSP response");
            }
        }
        ctx.fireUserEventTriggered(evt);
    }
}
