package io.netty.channel.group;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.concurrent.BlockingOperationException;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.ImmediateEventExecutor;
import io.netty.util.internal.ObjectUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public final class DefaultChannelGroupFuture extends DefaultPromise<Void> implements ChannelGroupFuture {
    private final ChannelFutureListener childListener;
    private int failureCount;
    private final Map<Channel, ChannelFuture> futures;
    private final ChannelGroup group;
    private int successCount;

    static /* synthetic */ int access$008(DefaultChannelGroupFuture x0) {
        int i = x0.successCount;
        x0.successCount = i + 1;
        return i;
    }

    static /* synthetic */ int access$108(DefaultChannelGroupFuture x0) {
        int i = x0.failureCount;
        x0.failureCount = i + 1;
        return i;
    }

    DefaultChannelGroupFuture(ChannelGroup group, Collection<ChannelFuture> futures, EventExecutor executor) {
        super(executor);
        this.childListener = new ChannelFutureListener() { // from class: io.netty.channel.group.DefaultChannelGroupFuture.1
            static final /* synthetic */ boolean $assertionsDisabled = false;

            @Override // io.netty.util.concurrent.GenericFutureListener
            public void operationComplete(ChannelFuture future) throws Exception {
                boolean callSetDone;
                boolean success = future.isSuccess();
                synchronized (DefaultChannelGroupFuture.this) {
                    if (success) {
                        DefaultChannelGroupFuture.access$008(DefaultChannelGroupFuture.this);
                    } else {
                        DefaultChannelGroupFuture.access$108(DefaultChannelGroupFuture.this);
                    }
                    callSetDone = DefaultChannelGroupFuture.this.successCount + DefaultChannelGroupFuture.this.failureCount == DefaultChannelGroupFuture.this.futures.size();
                    if (DefaultChannelGroupFuture.this.successCount + DefaultChannelGroupFuture.this.failureCount > DefaultChannelGroupFuture.this.futures.size()) {
                        throw new AssertionError();
                    }
                }
                if (callSetDone) {
                    if (DefaultChannelGroupFuture.this.failureCount <= 0) {
                        DefaultChannelGroupFuture.this.setSuccess0();
                        return;
                    }
                    List<Map.Entry<Channel, Throwable>> failed = new ArrayList<>(DefaultChannelGroupFuture.this.failureCount);
                    for (ChannelFuture f : DefaultChannelGroupFuture.this.futures.values()) {
                        if (!f.isSuccess()) {
                            failed.add(new DefaultEntry<>(f.channel(), f.cause()));
                        }
                    }
                    DefaultChannelGroupFuture.this.setFailure0(new ChannelGroupException(failed));
                }
            }
        };
        this.group = (ChannelGroup) ObjectUtil.checkNotNull(group, "group");
        ObjectUtil.checkNotNull(futures, "futures");
        Map<Channel, ChannelFuture> futureMap = new LinkedHashMap<>();
        for (ChannelFuture f : futures) {
            futureMap.put(f.channel(), f);
        }
        this.futures = Collections.unmodifiableMap(futureMap);
        Iterator<ChannelFuture> it2 = this.futures.values().iterator();
        while (it2.hasNext()) {
            it2.next().addListener((GenericFutureListener<? extends Future<? super Void>>) this.childListener);
        }
        if (this.futures.isEmpty()) {
            setSuccess0();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DefaultChannelGroupFuture(ChannelGroup group, Map<Channel, ChannelFuture> futures, EventExecutor executor) {
        super(executor);
        this.childListener = new ChannelFutureListener() { // from class: io.netty.channel.group.DefaultChannelGroupFuture.1
            static final /* synthetic */ boolean $assertionsDisabled = false;

            @Override // io.netty.util.concurrent.GenericFutureListener
            public void operationComplete(ChannelFuture future) throws Exception {
                boolean callSetDone;
                boolean success = future.isSuccess();
                synchronized (DefaultChannelGroupFuture.this) {
                    if (success) {
                        DefaultChannelGroupFuture.access$008(DefaultChannelGroupFuture.this);
                    } else {
                        DefaultChannelGroupFuture.access$108(DefaultChannelGroupFuture.this);
                    }
                    callSetDone = DefaultChannelGroupFuture.this.successCount + DefaultChannelGroupFuture.this.failureCount == DefaultChannelGroupFuture.this.futures.size();
                    if (DefaultChannelGroupFuture.this.successCount + DefaultChannelGroupFuture.this.failureCount > DefaultChannelGroupFuture.this.futures.size()) {
                        throw new AssertionError();
                    }
                }
                if (callSetDone) {
                    if (DefaultChannelGroupFuture.this.failureCount <= 0) {
                        DefaultChannelGroupFuture.this.setSuccess0();
                        return;
                    }
                    List<Map.Entry<Channel, Throwable>> failed = new ArrayList<>(DefaultChannelGroupFuture.this.failureCount);
                    for (ChannelFuture f : DefaultChannelGroupFuture.this.futures.values()) {
                        if (!f.isSuccess()) {
                            failed.add(new DefaultEntry<>(f.channel(), f.cause()));
                        }
                    }
                    DefaultChannelGroupFuture.this.setFailure0(new ChannelGroupException(failed));
                }
            }
        };
        this.group = group;
        this.futures = Collections.unmodifiableMap(futures);
        for (ChannelFuture f : this.futures.values()) {
            f.addListener((GenericFutureListener<? extends Future<? super Void>>) this.childListener);
        }
        if (this.futures.isEmpty()) {
            setSuccess0();
        }
    }

    @Override // io.netty.channel.group.ChannelGroupFuture
    public ChannelGroup group() {
        return this.group;
    }

    @Override // io.netty.channel.group.ChannelGroupFuture
    public ChannelFuture find(Channel channel) {
        return this.futures.get(channel);
    }

    @Override // io.netty.channel.group.ChannelGroupFuture, java.lang.Iterable
    public Iterator<ChannelFuture> iterator() {
        return this.futures.values().iterator();
    }

    @Override // io.netty.channel.group.ChannelGroupFuture
    public synchronized boolean isPartialSuccess() {
        boolean z;
        if (this.successCount != 0) {
            z = this.successCount != this.futures.size();
        }
        return z;
    }

    @Override // io.netty.channel.group.ChannelGroupFuture
    public synchronized boolean isPartialFailure() {
        boolean z;
        if (this.failureCount != 0) {
            z = this.failureCount != this.futures.size();
        }
        return z;
    }

    @Override // io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Future
    public Future<Void> addListener(GenericFutureListener<? extends Future<? super Void>> listener) {
        super.addListener((GenericFutureListener) listener);
        return this;
    }

    @Override // io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Future
    public Future<Void> addListeners(GenericFutureListener<? extends Future<? super Void>>... listeners) {
        super.addListeners((GenericFutureListener[]) listeners);
        return this;
    }

    @Override // io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Future
    public Future<Void> removeListener(GenericFutureListener<? extends Future<? super Void>> listener) {
        super.removeListener((GenericFutureListener) listener);
        return this;
    }

    @Override // io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Future
    public Future<Void> removeListeners(GenericFutureListener<? extends Future<? super Void>>... listeners) {
        super.removeListeners((GenericFutureListener[]) listeners);
        return this;
    }

    @Override // io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Future
    public Future<Void> await() throws InterruptedException {
        super.await();
        return this;
    }

    @Override // io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Future
    public Future<Void> awaitUninterruptibly() {
        super.awaitUninterruptibly();
        return this;
    }

    @Override // io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Future
    public Future<Void> syncUninterruptibly() {
        super.syncUninterruptibly();
        return this;
    }

    @Override // io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Future
    public Future<Void> sync() throws InterruptedException {
        super.sync();
        return this;
    }

    @Override // io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Future
    public ChannelGroupException cause() {
        return (ChannelGroupException) super.cause();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setSuccess0() {
        super.setSuccess((DefaultChannelGroupFuture) null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setFailure0(ChannelGroupException cause) {
        super.setFailure((Throwable) cause);
    }

    @Override // io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Promise, io.netty.util.concurrent.ProgressivePromise
    public DefaultChannelGroupFuture setSuccess(Void result) {
        throw new IllegalStateException();
    }

    @Override // io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Promise
    public boolean trySuccess(Void result) {
        throw new IllegalStateException();
    }

    @Override // io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Promise, io.netty.channel.ChannelPromise
    public DefaultChannelGroupFuture setFailure(Throwable cause) {
        throw new IllegalStateException();
    }

    @Override // io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Promise
    public boolean tryFailure(Throwable cause) {
        throw new IllegalStateException();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.util.concurrent.DefaultPromise
    public void checkDeadLock() {
        EventExecutor e = executor();
        if (e != null && e != ImmediateEventExecutor.INSTANCE && e.inEventLoop()) {
            throw new BlockingOperationException();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class DefaultEntry<K, V> implements Map.Entry<K, V> {
        private final K key;
        private final V value;

        DefaultEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override // java.util.Map.Entry
        public K getKey() {
            return this.key;
        }

        @Override // java.util.Map.Entry
        public V getValue() {
            return this.value;
        }

        @Override // java.util.Map.Entry
        public V setValue(V value) {
            throw new UnsupportedOperationException("read-only");
        }
    }
}
