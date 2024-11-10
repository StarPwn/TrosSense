package io.netty.channel.pool;

import io.netty.channel.pool.ChannelPool;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.GlobalEventExecutor;
import io.netty.util.concurrent.Promise;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.ReadOnlyIterator;
import java.io.Closeable;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/* loaded from: classes4.dex */
public abstract class AbstractChannelPoolMap<K, P extends ChannelPool> implements ChannelPoolMap<K, P>, Iterable<Map.Entry<K, P>>, Closeable {
    private final ConcurrentMap<K, P> map = PlatformDependent.newConcurrentHashMap();

    protected abstract P newPool(K k);

    @Override // io.netty.channel.pool.ChannelPoolMap
    public final P get(K key) {
        P pool = this.map.get(ObjectUtil.checkNotNull(key, "key"));
        if (pool == null) {
            P pool2 = newPool(key);
            P old = this.map.putIfAbsent(key, pool2);
            if (old != null) {
                poolCloseAsyncIfSupported(pool2);
                return old;
            }
            return pool2;
        }
        return pool;
    }

    public final boolean remove(K key) {
        P pool = this.map.remove(ObjectUtil.checkNotNull(key, "key"));
        if (pool != null) {
            poolCloseAsyncIfSupported(pool);
            return true;
        }
        return false;
    }

    private Future<Boolean> removeAsyncIfSupported(K key) {
        P pool = this.map.remove(ObjectUtil.checkNotNull(key, "key"));
        if (pool != null) {
            final Promise<Boolean> removePromise = GlobalEventExecutor.INSTANCE.newPromise();
            poolCloseAsyncIfSupported(pool).addListener(new GenericFutureListener<Future<? super Void>>() { // from class: io.netty.channel.pool.AbstractChannelPoolMap.1
                @Override // io.netty.util.concurrent.GenericFutureListener
                public void operationComplete(Future<? super Void> future) throws Exception {
                    if (future.isSuccess()) {
                        removePromise.setSuccess(Boolean.TRUE);
                    } else {
                        removePromise.setFailure(future.cause());
                    }
                }
            });
            return removePromise;
        }
        return GlobalEventExecutor.INSTANCE.newSucceededFuture(Boolean.FALSE);
    }

    private static Future<Void> poolCloseAsyncIfSupported(ChannelPool pool) {
        if (pool instanceof SimpleChannelPool) {
            return ((SimpleChannelPool) pool).closeAsync();
        }
        try {
            pool.close();
            return GlobalEventExecutor.INSTANCE.newSucceededFuture(null);
        } catch (Exception e) {
            return GlobalEventExecutor.INSTANCE.newFailedFuture(e);
        }
    }

    @Override // java.lang.Iterable
    public final Iterator<Map.Entry<K, P>> iterator() {
        return new ReadOnlyIterator(this.map.entrySet().iterator());
    }

    public final int size() {
        return this.map.size();
    }

    public final boolean isEmpty() {
        return this.map.isEmpty();
    }

    @Override // io.netty.channel.pool.ChannelPoolMap
    public final boolean contains(K key) {
        return this.map.containsKey(ObjectUtil.checkNotNull(key, "key"));
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public final void close() {
        for (K key : this.map.keySet()) {
            removeAsyncIfSupported(key).syncUninterruptibly();
        }
    }
}
