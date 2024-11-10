package io.netty.channel;

import io.netty.channel.ChannelFlushPromiseNotifier;
import io.netty.util.concurrent.DefaultProgressivePromise;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/* loaded from: classes4.dex */
public class DefaultChannelProgressivePromise extends DefaultProgressivePromise<Void> implements ChannelProgressivePromise, ChannelFlushPromiseNotifier.FlushCheckpoint {
    private final Channel channel;
    private long checkpoint;

    public DefaultChannelProgressivePromise(Channel channel) {
        this.channel = channel;
    }

    public DefaultChannelProgressivePromise(Channel channel, EventExecutor executor) {
        super(executor);
        this.channel = channel;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.util.concurrent.DefaultPromise
    public EventExecutor executor() {
        EventExecutor e = super.executor();
        if (e == null) {
            return channel().eventLoop();
        }
        return e;
    }

    @Override // io.netty.channel.ChannelFuture
    public Channel channel() {
        return this.channel;
    }

    @Override // io.netty.channel.ChannelPromise
    public ChannelProgressivePromise setSuccess() {
        return setSuccess((Void) null);
    }

    @Override // io.netty.util.concurrent.DefaultProgressivePromise, io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Promise, io.netty.util.concurrent.ProgressivePromise
    public ChannelProgressivePromise setSuccess(Void result) {
        super.setSuccess((DefaultChannelProgressivePromise) result);
        return this;
    }

    @Override // io.netty.channel.ChannelPromise
    public boolean trySuccess() {
        return trySuccess(null);
    }

    @Override // io.netty.util.concurrent.DefaultProgressivePromise, io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Promise, io.netty.channel.ChannelPromise
    public ChannelProgressivePromise setFailure(Throwable cause) {
        super.setFailure(cause);
        return this;
    }

    @Override // io.netty.util.concurrent.DefaultProgressivePromise, io.netty.util.concurrent.ProgressivePromise
    public ChannelProgressivePromise setProgress(long progress, long total) {
        super.setProgress(progress, total);
        return this;
    }

    @Override // io.netty.util.concurrent.DefaultProgressivePromise, io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Future
    public Future<Void> addListener(GenericFutureListener<? extends Future<? super Void>> listener) {
        super.addListener((GenericFutureListener) listener);
        return this;
    }

    @Override // io.netty.util.concurrent.DefaultProgressivePromise, io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Future
    public Future<Void> addListeners(GenericFutureListener<? extends Future<? super Void>>... listeners) {
        super.addListeners((GenericFutureListener[]) listeners);
        return this;
    }

    @Override // io.netty.util.concurrent.DefaultProgressivePromise, io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Future
    public Future<Void> removeListener(GenericFutureListener<? extends Future<? super Void>> listener) {
        super.removeListener((GenericFutureListener) listener);
        return this;
    }

    @Override // io.netty.util.concurrent.DefaultProgressivePromise, io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Future
    public Future<Void> removeListeners(GenericFutureListener<? extends Future<? super Void>>... listeners) {
        super.removeListeners((GenericFutureListener[]) listeners);
        return this;
    }

    @Override // io.netty.util.concurrent.DefaultProgressivePromise, io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Future
    public Future<Void> sync() throws InterruptedException {
        super.sync();
        return this;
    }

    @Override // io.netty.util.concurrent.DefaultProgressivePromise, io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Future
    public Future<Void> syncUninterruptibly() {
        super.syncUninterruptibly();
        return this;
    }

    @Override // io.netty.util.concurrent.DefaultProgressivePromise, io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Future
    public Future<Void> await() throws InterruptedException {
        super.await();
        return this;
    }

    @Override // io.netty.util.concurrent.DefaultProgressivePromise, io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Future
    public Future<Void> awaitUninterruptibly() {
        super.awaitUninterruptibly();
        return this;
    }

    @Override // io.netty.channel.ChannelFlushPromiseNotifier.FlushCheckpoint
    public long flushCheckpoint() {
        return this.checkpoint;
    }

    @Override // io.netty.channel.ChannelFlushPromiseNotifier.FlushCheckpoint
    public void flushCheckpoint(long checkpoint) {
        this.checkpoint = checkpoint;
    }

    @Override // io.netty.channel.ChannelFlushPromiseNotifier.FlushCheckpoint
    public ChannelProgressivePromise promise() {
        return this;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.util.concurrent.DefaultPromise
    public void checkDeadLock() {
        if (channel().isRegistered()) {
            super.checkDeadLock();
        }
    }

    @Override // io.netty.channel.ChannelPromise
    public ChannelProgressivePromise unvoid() {
        return this;
    }

    @Override // io.netty.channel.ChannelFuture
    public boolean isVoid() {
        return false;
    }
}
