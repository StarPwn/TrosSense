package io.netty.channel;

import io.netty.util.concurrent.EventExecutor;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public final class DefaultChannelHandlerContext extends AbstractChannelHandlerContext {
    private final ChannelHandler handler;

    /* JADX INFO: Access modifiers changed from: package-private */
    public DefaultChannelHandlerContext(DefaultChannelPipeline pipeline, EventExecutor executor, String name, ChannelHandler handler) {
        super(pipeline, executor, name, handler.getClass());
        this.handler = handler;
    }

    @Override // io.netty.channel.ChannelHandlerContext
    public ChannelHandler handler() {
        return this.handler;
    }
}
