package io.netty.resolver;

import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import io.netty.util.concurrent.Promise;
import io.netty.util.internal.PlatformDependent;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* loaded from: classes4.dex */
public class RoundRobinInetAddressResolver extends InetNameResolver {
    private final NameResolver<InetAddress> nameResolver;

    public RoundRobinInetAddressResolver(EventExecutor executor, NameResolver<InetAddress> nameResolver) {
        super(executor);
        this.nameResolver = nameResolver;
    }

    @Override // io.netty.resolver.SimpleNameResolver
    protected void doResolve(final String inetHost, final Promise<InetAddress> promise) throws Exception {
        this.nameResolver.resolveAll(inetHost).addListener(new FutureListener<List<InetAddress>>() { // from class: io.netty.resolver.RoundRobinInetAddressResolver.1
            @Override // io.netty.util.concurrent.GenericFutureListener
            public void operationComplete(Future<List<InetAddress>> future) throws Exception {
                if (future.isSuccess()) {
                    List<InetAddress> inetAddresses = future.getNow();
                    int numAddresses = inetAddresses.size();
                    if (numAddresses > 0) {
                        promise.setSuccess(inetAddresses.get(RoundRobinInetAddressResolver.randomIndex(numAddresses)));
                        return;
                    } else {
                        promise.setFailure(new UnknownHostException(inetHost));
                        return;
                    }
                }
                promise.setFailure(future.cause());
            }
        });
    }

    @Override // io.netty.resolver.SimpleNameResolver
    protected void doResolveAll(String inetHost, final Promise<List<InetAddress>> promise) throws Exception {
        this.nameResolver.resolveAll(inetHost).addListener(new FutureListener<List<InetAddress>>() { // from class: io.netty.resolver.RoundRobinInetAddressResolver.2
            @Override // io.netty.util.concurrent.GenericFutureListener
            public void operationComplete(Future<List<InetAddress>> future) throws Exception {
                if (future.isSuccess()) {
                    List<InetAddress> inetAddresses = future.getNow();
                    if (!inetAddresses.isEmpty()) {
                        ArrayList arrayList = new ArrayList(inetAddresses);
                        Collections.rotate(arrayList, RoundRobinInetAddressResolver.randomIndex(inetAddresses.size()));
                        promise.setSuccess(arrayList);
                        return;
                    }
                    promise.setSuccess(inetAddresses);
                    return;
                }
                promise.setFailure(future.cause());
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int randomIndex(int numAddresses) {
        if (numAddresses == 1) {
            return 0;
        }
        return PlatformDependent.threadLocalRandom().nextInt(numAddresses);
    }

    @Override // io.netty.resolver.SimpleNameResolver, io.netty.resolver.NameResolver, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.nameResolver.close();
    }
}
