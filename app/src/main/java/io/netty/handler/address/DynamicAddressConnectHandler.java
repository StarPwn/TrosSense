package io.netty.handler.address;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.net.SocketAddress;

/* loaded from: classes4.dex */
public abstract class DynamicAddressConnectHandler extends ChannelOutboundHandlerAdapter {
    @Override // io.netty.channel.ChannelOutboundHandlerAdapter, io.netty.channel.ChannelOutboundHandler
    public final void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
        try {
            SocketAddress remote = remoteAddress(remoteAddress, localAddress);
            SocketAddress local = localAddress(remoteAddress, localAddress);
            ctx.connect(remote, local, promise).addListener((GenericFutureListener<? extends Future<? super Void>>) new ChannelFutureListener() { // from class: io.netty.handler.address.DynamicAddressConnectHandler.1
                @Override // io.netty.util.concurrent.GenericFutureListener
                public void operationComplete(ChannelFuture future) {
                    if (future.isSuccess()) {
                        future.channel().pipeline().remove(DynamicAddressConnectHandler.this);
                    }
                }
            });
        } catch (Exception e) {
            promise.setFailure((Throwable) e);
        }
    }

    protected SocketAddress localAddress(SocketAddress remoteAddress, SocketAddress localAddress) throws Exception {
        return localAddress;
    }

    protected SocketAddress remoteAddress(SocketAddress remoteAddress, SocketAddress localAddress) throws Exception {
        return remoteAddress;
    }
}
