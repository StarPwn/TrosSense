package io.netty.channel;

import io.netty.util.ReferenceCountUtil;
import io.netty.util.internal.TypeParameterMatcher;

/* loaded from: classes4.dex */
public abstract class SimpleChannelInboundHandler<I> extends ChannelInboundHandlerAdapter {
    private final boolean autoRelease;
    private final TypeParameterMatcher matcher;

    protected abstract void channelRead0(ChannelHandlerContext channelHandlerContext, I i) throws Exception;

    /* JADX INFO: Access modifiers changed from: protected */
    public SimpleChannelInboundHandler() {
        this(true);
    }

    protected SimpleChannelInboundHandler(boolean autoRelease) {
        this.matcher = TypeParameterMatcher.find(this, SimpleChannelInboundHandler.class, "I");
        this.autoRelease = autoRelease;
    }

    protected SimpleChannelInboundHandler(Class<? extends I> inboundMessageType) {
        this(inboundMessageType, true);
    }

    protected SimpleChannelInboundHandler(Class<? extends I> inboundMessageType, boolean autoRelease) {
        this.matcher = TypeParameterMatcher.get(inboundMessageType);
        this.autoRelease = autoRelease;
    }

    public boolean acceptInboundMessage(Object msg) throws Exception {
        return this.matcher.match(msg);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelRead(ChannelHandlerContext ctx, Object obj) throws Exception {
        boolean release = true;
        try {
            if (acceptInboundMessage(obj)) {
                channelRead0(ctx, obj);
            } else {
                release = false;
                ctx.fireChannelRead(obj);
            }
        } finally {
            if (this.autoRelease && 1 != 0) {
                ReferenceCountUtil.release(obj);
            }
        }
    }
}
