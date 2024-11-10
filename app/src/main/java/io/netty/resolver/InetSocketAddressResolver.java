package io.netty.resolver;

import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import io.netty.util.concurrent.Promise;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes4.dex */
public class InetSocketAddressResolver extends AbstractAddressResolver<InetSocketAddress> {
    final NameResolver<InetAddress> nameResolver;

    public InetSocketAddressResolver(EventExecutor executor, NameResolver<InetAddress> nameResolver) {
        super(executor, InetSocketAddress.class);
        this.nameResolver = nameResolver;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.resolver.AbstractAddressResolver
    public boolean doIsResolved(InetSocketAddress address) {
        return !address.isUnresolved();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.resolver.AbstractAddressResolver
    public void doResolve(final InetSocketAddress unresolvedAddress, final Promise<InetSocketAddress> promise) throws Exception {
        this.nameResolver.resolve(unresolvedAddress.getHostName()).addListener(new FutureListener<InetAddress>() { // from class: io.netty.resolver.InetSocketAddressResolver.1
            @Override // io.netty.util.concurrent.GenericFutureListener
            public void operationComplete(Future<InetAddress> future) throws Exception {
                if (future.isSuccess()) {
                    promise.setSuccess(new InetSocketAddress(future.getNow(), unresolvedAddress.getPort()));
                } else {
                    promise.setFailure(future.cause());
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.resolver.AbstractAddressResolver
    public void doResolveAll(final InetSocketAddress unresolvedAddress, final Promise<List<InetSocketAddress>> promise) throws Exception {
        this.nameResolver.resolveAll(unresolvedAddress.getHostName()).addListener(new FutureListener<List<InetAddress>>() { // from class: io.netty.resolver.InetSocketAddressResolver.2
            @Override // io.netty.util.concurrent.GenericFutureListener
            public void operationComplete(Future<List<InetAddress>> future) throws Exception {
                if (future.isSuccess()) {
                    List<InetAddress> inetAddresses = future.getNow();
                    ArrayList arrayList = new ArrayList(inetAddresses.size());
                    for (InetAddress inetAddress : inetAddresses) {
                        arrayList.add(new InetSocketAddress(inetAddress, unresolvedAddress.getPort()));
                    }
                    promise.setSuccess(arrayList);
                    return;
                }
                promise.setFailure(future.cause());
            }
        });
    }

    @Override // io.netty.resolver.AbstractAddressResolver, io.netty.resolver.AddressResolver, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.nameResolver.close();
    }
}
