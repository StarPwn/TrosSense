package io.netty.channel;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerMask;
import io.netty.util.internal.InternalThreadLocalMap;
import java.util.Map;

/* loaded from: classes4.dex */
public abstract class ChannelHandlerAdapter implements ChannelHandler {
    boolean added;

    /* JADX INFO: Access modifiers changed from: protected */
    public void ensureNotSharable() {
        if (isSharable()) {
            throw new IllegalStateException("ChannelHandler " + getClass().getName() + " is not allowed to be shared");
        }
    }

    public boolean isSharable() {
        Class<?> clazz = getClass();
        Map<Class<?>, Boolean> cache = InternalThreadLocalMap.get().handlerSharableCache();
        Boolean sharable = cache.get(clazz);
        if (sharable == null) {
            sharable = Boolean.valueOf(clazz.isAnnotationPresent(ChannelHandler.Sharable.class));
            cache.put(clazz, sharable);
        }
        return sharable.booleanValue();
    }

    @Override // io.netty.channel.ChannelHandler
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
    }

    @Override // io.netty.channel.ChannelHandler
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
    }

    @Override // io.netty.channel.ChannelHandler, io.netty.channel.ChannelInboundHandler
    @ChannelHandlerMask.Skip
    @Deprecated
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.fireExceptionCaught(cause);
    }
}
