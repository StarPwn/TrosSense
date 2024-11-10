package io.netty.resolver;

import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.Promise;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.TypeParameterMatcher;
import java.net.SocketAddress;
import java.nio.channels.UnsupportedAddressTypeException;
import java.util.Collections;
import java.util.List;

/* loaded from: classes4.dex */
public abstract class AbstractAddressResolver<T extends SocketAddress> implements AddressResolver<T> {
    private final EventExecutor executor;
    private final TypeParameterMatcher matcher;

    protected abstract boolean doIsResolved(T t);

    protected abstract void doResolve(T t, Promise<T> promise) throws Exception;

    protected abstract void doResolveAll(T t, Promise<List<T>> promise) throws Exception;

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractAddressResolver(EventExecutor executor) {
        this.executor = (EventExecutor) ObjectUtil.checkNotNull(executor, "executor");
        this.matcher = TypeParameterMatcher.find(this, AbstractAddressResolver.class, "T");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractAddressResolver(EventExecutor executor, Class<? extends T> addressType) {
        this.executor = (EventExecutor) ObjectUtil.checkNotNull(executor, "executor");
        this.matcher = TypeParameterMatcher.get(addressType);
    }

    protected EventExecutor executor() {
        return this.executor;
    }

    @Override // io.netty.resolver.AddressResolver
    public boolean isSupported(SocketAddress address) {
        return this.matcher.match(address);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // io.netty.resolver.AddressResolver
    public final boolean isResolved(SocketAddress socketAddress) {
        if (!isSupported(socketAddress)) {
            throw new UnsupportedAddressTypeException();
        }
        return doIsResolved(socketAddress);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // io.netty.resolver.AddressResolver
    public final Future<T> resolve(SocketAddress socketAddress) {
        if (!isSupported((SocketAddress) ObjectUtil.checkNotNull(socketAddress, "address"))) {
            return executor().newFailedFuture(new UnsupportedAddressTypeException());
        }
        if (isResolved(socketAddress)) {
            return this.executor.newSucceededFuture(socketAddress);
        }
        try {
            Promise<T> promise = executor().newPromise();
            doResolve(socketAddress, promise);
            return promise;
        } catch (Exception e) {
            return executor().newFailedFuture(e);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // io.netty.resolver.AddressResolver
    public final Future<T> resolve(SocketAddress socketAddress, Promise<T> promise) {
        ObjectUtil.checkNotNull(socketAddress, "address");
        ObjectUtil.checkNotNull(promise, "promise");
        if (!isSupported(socketAddress)) {
            return promise.setFailure(new UnsupportedAddressTypeException());
        }
        if (isResolved(socketAddress)) {
            return promise.setSuccess(socketAddress);
        }
        try {
            doResolve(socketAddress, promise);
            return promise;
        } catch (Exception e) {
            return promise.setFailure(e);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // io.netty.resolver.AddressResolver
    public final Future<List<T>> resolveAll(SocketAddress socketAddress) {
        if (!isSupported((SocketAddress) ObjectUtil.checkNotNull(socketAddress, "address"))) {
            return executor().newFailedFuture(new UnsupportedAddressTypeException());
        }
        if (isResolved(socketAddress)) {
            return this.executor.newSucceededFuture(Collections.singletonList(socketAddress));
        }
        try {
            Promise<List<T>> promise = executor().newPromise();
            doResolveAll(socketAddress, promise);
            return promise;
        } catch (Exception e) {
            return executor().newFailedFuture(e);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // io.netty.resolver.AddressResolver
    public final Future<List<T>> resolveAll(SocketAddress socketAddress, Promise<List<T>> promise) {
        ObjectUtil.checkNotNull(socketAddress, "address");
        ObjectUtil.checkNotNull(promise, "promise");
        if (!isSupported(socketAddress)) {
            return promise.setFailure(new UnsupportedAddressTypeException());
        }
        if (isResolved(socketAddress)) {
            return promise.setSuccess(Collections.singletonList(socketAddress));
        }
        try {
            doResolveAll(socketAddress, promise);
            return promise;
        } catch (Exception e) {
            return promise.setFailure(e);
        }
    }

    @Override // io.netty.resolver.AddressResolver, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
    }
}
