package io.netty.channel;

import io.netty.util.concurrent.AbstractFuture;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.internal.ObjectUtil;
import java.util.concurrent.TimeUnit;

/* loaded from: classes4.dex */
public final class VoidChannelPromise extends AbstractFuture<Void> implements ChannelPromise {
    private final Channel channel;
    private final ChannelFutureListener fireExceptionListener;

    public VoidChannelPromise(Channel channel, boolean fireException) {
        ObjectUtil.checkNotNull(channel, "channel");
        this.channel = channel;
        if (fireException) {
            this.fireExceptionListener = new ChannelFutureListener() { // from class: io.netty.channel.VoidChannelPromise.1
                @Override // io.netty.util.concurrent.GenericFutureListener
                public void operationComplete(ChannelFuture future) throws Exception {
                    Throwable cause = future.cause();
                    if (cause != null) {
                        VoidChannelPromise.this.fireException0(cause);
                    }
                }
            };
        } else {
            this.fireExceptionListener = null;
        }
    }

    @Override // io.netty.util.concurrent.Future
    public Future<Void> addListener(GenericFutureListener<? extends Future<? super Void>> listener) {
        fail();
        return this;
    }

    @Override // io.netty.util.concurrent.Future
    public Future<Void> addListeners(GenericFutureListener<? extends Future<? super Void>>... listeners) {
        fail();
        return this;
    }

    @Override // io.netty.util.concurrent.Future
    public Future<Void> removeListener(GenericFutureListener<? extends Future<? super Void>> listener) {
        return this;
    }

    @Override // io.netty.util.concurrent.Future
    public Future<Void> removeListeners(GenericFutureListener<? extends Future<? super Void>>... listeners) {
        return this;
    }

    @Override // io.netty.util.concurrent.Future
    public Future<Void> await() throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        return this;
    }

    @Override // io.netty.util.concurrent.Future
    public boolean await(long timeout, TimeUnit unit) {
        fail();
        return false;
    }

    @Override // io.netty.util.concurrent.Future
    public boolean await(long timeoutMillis) {
        fail();
        return false;
    }

    @Override // io.netty.util.concurrent.Future
    public Future<Void> awaitUninterruptibly() {
        fail();
        return this;
    }

    @Override // io.netty.util.concurrent.Future
    public boolean awaitUninterruptibly(long timeout, TimeUnit unit) {
        fail();
        return false;
    }

    @Override // io.netty.util.concurrent.Future
    public boolean awaitUninterruptibly(long timeoutMillis) {
        fail();
        return false;
    }

    @Override // io.netty.channel.ChannelPromise, io.netty.channel.ChannelFuture
    public Channel channel() {
        return this.channel;
    }

    @Override // java.util.concurrent.Future
    public boolean isDone() {
        return false;
    }

    @Override // io.netty.util.concurrent.Future
    public boolean isSuccess() {
        return false;
    }

    @Override // io.netty.util.concurrent.Promise
    public boolean setUncancellable() {
        return true;
    }

    @Override // io.netty.util.concurrent.Future
    public boolean isCancellable() {
        return false;
    }

    @Override // java.util.concurrent.Future
    public boolean isCancelled() {
        return false;
    }

    @Override // io.netty.util.concurrent.Future
    public Throwable cause() {
        return null;
    }

    @Override // io.netty.util.concurrent.Future
    public Future<Void> sync() {
        fail();
        return this;
    }

    @Override // io.netty.util.concurrent.Future
    public Future<Void> syncUninterruptibly() {
        fail();
        return this;
    }

    @Override // io.netty.util.concurrent.Promise, io.netty.channel.ChannelPromise
    public VoidChannelPromise setFailure(Throwable cause) {
        fireException0(cause);
        return this;
    }

    @Override // io.netty.channel.ChannelPromise
    public VoidChannelPromise setSuccess() {
        return this;
    }

    @Override // io.netty.util.concurrent.Promise
    public boolean tryFailure(Throwable cause) {
        fireException0(cause);
        return false;
    }

    @Override // io.netty.util.concurrent.Future, java.util.concurrent.Future
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override // io.netty.channel.ChannelPromise
    public boolean trySuccess() {
        return false;
    }

    private static void fail() {
        throw new IllegalStateException("void future");
    }

    @Override // io.netty.util.concurrent.Promise, io.netty.util.concurrent.ProgressivePromise
    public VoidChannelPromise setSuccess(Void result) {
        return this;
    }

    @Override // io.netty.util.concurrent.Promise
    public boolean trySuccess(Void result) {
        return false;
    }

    @Override // io.netty.util.concurrent.Future
    public Void getNow() {
        return null;
    }

    @Override // io.netty.channel.ChannelPromise
    public ChannelPromise unvoid() {
        ChannelPromise promise = new DefaultChannelPromise(this.channel);
        if (this.fireExceptionListener != null) {
            promise.addListener(this.fireExceptionListener);
        }
        return promise;
    }

    @Override // io.netty.channel.ChannelFuture
    public boolean isVoid() {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fireException0(Throwable cause) {
        if (this.fireExceptionListener != null && this.channel.isRegistered()) {
            this.channel.pipeline().fireExceptionCaught(cause);
        }
    }
}
