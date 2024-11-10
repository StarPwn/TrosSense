package io.netty.resolver;

import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.Closeable;
import java.net.SocketAddress;
import java.util.IdentityHashMap;
import java.util.Map;

/* loaded from: classes4.dex */
public abstract class AddressResolverGroup<T extends SocketAddress> implements Closeable {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) AddressResolverGroup.class);
    private final Map<EventExecutor, AddressResolver<T>> resolvers = new IdentityHashMap();
    private final Map<EventExecutor, GenericFutureListener<Future<Object>>> executorTerminationListeners = new IdentityHashMap();

    protected abstract AddressResolver<T> newResolver(EventExecutor eventExecutor) throws Exception;

    public AddressResolver<T> getResolver(final EventExecutor executor) {
        AddressResolver<T> r;
        ObjectUtil.checkNotNull(executor, "executor");
        if (executor.isShuttingDown()) {
            throw new IllegalStateException("executor not accepting a task");
        }
        synchronized (this.resolvers) {
            r = this.resolvers.get(executor);
            if (r == null) {
                try {
                    final AddressResolver<T> newResolver = newResolver(executor);
                    this.resolvers.put(executor, newResolver);
                    FutureListener<Object> terminationListener = new FutureListener<Object>() { // from class: io.netty.resolver.AddressResolverGroup.1
                        @Override // io.netty.util.concurrent.GenericFutureListener
                        public void operationComplete(Future<Object> future) {
                            synchronized (AddressResolverGroup.this.resolvers) {
                                AddressResolverGroup.this.resolvers.remove(executor);
                                AddressResolverGroup.this.executorTerminationListeners.remove(executor);
                            }
                            newResolver.close();
                        }
                    };
                    this.executorTerminationListeners.put(executor, terminationListener);
                    executor.terminationFuture().addListener(terminationListener);
                    r = newResolver;
                } catch (Exception e) {
                    throw new IllegalStateException("failed to create a new resolver", e);
                }
            }
        }
        return r;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        int i;
        AddressResolver<T>[] rArray;
        Map.Entry<EventExecutor, GenericFutureListener<Future<Object>>>[] listeners;
        synchronized (this.resolvers) {
            rArray = (AddressResolver[]) this.resolvers.values().toArray(new AddressResolver[0]);
            this.resolvers.clear();
            listeners = (Map.Entry[]) this.executorTerminationListeners.entrySet().toArray(new Map.Entry[0]);
            this.executorTerminationListeners.clear();
        }
        for (Map.Entry<EventExecutor, GenericFutureListener<Future<Object>>> entry : listeners) {
            entry.getKey().terminationFuture().removeListener(entry.getValue());
        }
        for (AddressResolver<T> r : rArray) {
            try {
                r.close();
            } catch (Throwable t) {
                logger.warn("Failed to close a resolver:", t);
            }
        }
    }
}
