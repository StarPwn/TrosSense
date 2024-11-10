package io.netty.channel;

import io.netty.util.concurrent.EventExecutor;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public final class SucceededChannelFuture extends CompleteChannelFuture {
    /* JADX INFO: Access modifiers changed from: package-private */
    public SucceededChannelFuture(Channel channel, EventExecutor executor) {
        super(channel, executor);
    }

    @Override // io.netty.util.concurrent.Future
    public Throwable cause() {
        return null;
    }

    @Override // io.netty.util.concurrent.Future
    public boolean isSuccess() {
        return true;
    }
}
