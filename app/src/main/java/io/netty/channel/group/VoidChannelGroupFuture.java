package io.netty.channel.group;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/* loaded from: classes4.dex */
final class VoidChannelGroupFuture implements ChannelGroupFuture {
    private static final Iterator<ChannelFuture> EMPTY = Collections.emptyList().iterator();
    private final ChannelGroup group;

    /* JADX INFO: Access modifiers changed from: package-private */
    public VoidChannelGroupFuture(ChannelGroup group) {
        this.group = group;
    }

    @Override // io.netty.channel.group.ChannelGroupFuture
    public ChannelGroup group() {
        return this.group;
    }

    @Override // io.netty.channel.group.ChannelGroupFuture
    public ChannelFuture find(Channel channel) {
        return null;
    }

    @Override // io.netty.channel.group.ChannelGroupFuture, io.netty.util.concurrent.Future
    public boolean isSuccess() {
        return false;
    }

    @Override // io.netty.util.concurrent.Future
    public ChannelGroupException cause() {
        return null;
    }

    @Override // io.netty.channel.group.ChannelGroupFuture
    public boolean isPartialSuccess() {
        return false;
    }

    @Override // io.netty.channel.group.ChannelGroupFuture
    public boolean isPartialFailure() {
        return false;
    }

    @Override // io.netty.util.concurrent.Future
    public Future<Void> addListener(GenericFutureListener<? extends Future<? super Void>> listener) {
        throw reject();
    }

    @Override // io.netty.util.concurrent.Future
    public Future<Void> addListeners(GenericFutureListener<? extends Future<? super Void>>... listeners) {
        throw reject();
    }

    @Override // io.netty.util.concurrent.Future
    public Future<Void> removeListener(GenericFutureListener<? extends Future<? super Void>> listener) {
        throw reject();
    }

    @Override // io.netty.util.concurrent.Future
    public Future<Void> removeListeners(GenericFutureListener<? extends Future<? super Void>>... listeners) {
        throw reject();
    }

    @Override // io.netty.util.concurrent.Future
    public Future<Void> await() {
        throw reject();
    }

    @Override // io.netty.util.concurrent.Future
    public Future<Void> awaitUninterruptibly() {
        throw reject();
    }

    @Override // io.netty.util.concurrent.Future
    public Future<Void> syncUninterruptibly() {
        throw reject();
    }

    @Override // io.netty.util.concurrent.Future
    public Future<Void> sync() {
        throw reject();
    }

    @Override // io.netty.channel.group.ChannelGroupFuture, java.lang.Iterable
    public Iterator<ChannelFuture> iterator() {
        return EMPTY;
    }

    @Override // io.netty.util.concurrent.Future
    public boolean isCancellable() {
        return false;
    }

    @Override // io.netty.util.concurrent.Future
    public boolean await(long timeout, TimeUnit unit) {
        throw reject();
    }

    @Override // io.netty.util.concurrent.Future
    public boolean await(long timeoutMillis) {
        throw reject();
    }

    @Override // io.netty.util.concurrent.Future
    public boolean awaitUninterruptibly(long timeout, TimeUnit unit) {
        throw reject();
    }

    @Override // io.netty.util.concurrent.Future
    public boolean awaitUninterruptibly(long timeoutMillis) {
        throw reject();
    }

    @Override // io.netty.util.concurrent.Future
    public Void getNow() {
        return null;
    }

    @Override // io.netty.util.concurrent.Future, java.util.concurrent.Future
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override // java.util.concurrent.Future
    public boolean isCancelled() {
        return false;
    }

    @Override // java.util.concurrent.Future
    public boolean isDone() {
        return false;
    }

    @Override // java.util.concurrent.Future
    public Void get() {
        throw reject();
    }

    @Override // java.util.concurrent.Future
    public Void get(long timeout, TimeUnit unit) {
        throw reject();
    }

    private static RuntimeException reject() {
        return new IllegalStateException("void future");
    }
}
