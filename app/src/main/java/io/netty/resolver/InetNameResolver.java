package io.netty.resolver;

import io.netty.util.concurrent.EventExecutor;
import java.net.InetAddress;
import java.net.InetSocketAddress;

/* loaded from: classes4.dex */
public abstract class InetNameResolver extends SimpleNameResolver<InetAddress> {
    private volatile AddressResolver<InetSocketAddress> addressResolver;

    /* JADX INFO: Access modifiers changed from: protected */
    public InetNameResolver(EventExecutor executor) {
        super(executor);
    }

    public AddressResolver<InetSocketAddress> asAddressResolver() {
        AddressResolver<InetSocketAddress> result = this.addressResolver;
        if (result == null) {
            synchronized (this) {
                result = this.addressResolver;
                if (result == null) {
                    InetSocketAddressResolver inetSocketAddressResolver = new InetSocketAddressResolver(executor(), this);
                    result = inetSocketAddressResolver;
                    this.addressResolver = inetSocketAddressResolver;
                }
            }
        }
        return result;
    }
}
