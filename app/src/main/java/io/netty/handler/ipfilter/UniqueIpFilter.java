package io.netty.handler.ipfilter;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.internal.ConcurrentSet;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Set;

@ChannelHandler.Sharable
/* loaded from: classes4.dex */
public class UniqueIpFilter extends AbstractRemoteAddressFilter<InetSocketAddress> {
    private final Set<InetAddress> connected = new ConcurrentSet();

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.ipfilter.AbstractRemoteAddressFilter
    public boolean accept(ChannelHandlerContext ctx, InetSocketAddress remoteAddress) throws Exception {
        final InetAddress remoteIp = remoteAddress.getAddress();
        if (!this.connected.add(remoteIp)) {
            return false;
        }
        ctx.channel().closeFuture().addListener((GenericFutureListener<? extends Future<? super Void>>) new ChannelFutureListener() { // from class: io.netty.handler.ipfilter.UniqueIpFilter.1
            @Override // io.netty.util.concurrent.GenericFutureListener
            public void operationComplete(ChannelFuture future) throws Exception {
                UniqueIpFilter.this.connected.remove(remoteIp);
            }
        });
        return true;
    }
}
