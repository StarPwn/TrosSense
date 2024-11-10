package org.cloudburstmc.netty.handler.codec.raknet;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.internal.TypeParameterMatcher;

/* loaded from: classes5.dex */
public abstract class AdvancedChannelInboundHandler<T> extends ChannelInboundHandlerAdapter {
    private final TypeParameterMatcher matcher;

    protected abstract void channelRead0(ChannelHandlerContext channelHandlerContext, T t) throws Exception;

    public AdvancedChannelInboundHandler() {
        this.matcher = TypeParameterMatcher.find(this, AdvancedChannelInboundHandler.class, "T");
    }

    public AdvancedChannelInboundHandler(Class<? extends T> inboundMessageType) {
        this.matcher = TypeParameterMatcher.get(inboundMessageType);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean acceptInboundMessage(ChannelHandlerContext ctx, Object msg) throws Exception {
        return this.matcher.match(msg);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelRead(ChannelHandlerContext ctx, Object obj) throws Exception {
        boolean release = true;
        try {
            if (acceptInboundMessage(ctx, obj)) {
                channelRead0(ctx, obj);
            } else {
                release = false;
                ctx.fireChannelRead(obj);
            }
        } finally {
            if (1 != 0) {
                ReferenceCountUtil.release(obj);
            }
        }
    }
}
