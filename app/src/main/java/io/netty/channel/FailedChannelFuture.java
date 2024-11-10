package io.netty.channel;

import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public final class FailedChannelFuture extends CompleteChannelFuture {
    private final Throwable cause;

    /* JADX INFO: Access modifiers changed from: package-private */
    public FailedChannelFuture(Channel channel, EventExecutor executor, Throwable cause) {
        super(channel, executor);
        this.cause = (Throwable) ObjectUtil.checkNotNull(cause, "cause");
    }

    @Override // io.netty.util.concurrent.Future
    public Throwable cause() {
        return this.cause;
    }

    @Override // io.netty.util.concurrent.Future
    public boolean isSuccess() {
        return false;
    }

    @Override // io.netty.channel.CompleteChannelFuture, io.netty.util.concurrent.CompleteFuture, io.netty.util.concurrent.Future
    public Future<Void> sync() {
        PlatformDependent.throwException(this.cause);
        return this;
    }

    @Override // io.netty.channel.CompleteChannelFuture, io.netty.util.concurrent.CompleteFuture, io.netty.util.concurrent.Future
    public Future<Void> syncUninterruptibly() {
        PlatformDependent.throwException(this.cause);
        return this;
    }
}
