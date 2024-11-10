package io.netty.channel;

import io.netty.util.concurrent.PromiseNotifier;

@Deprecated
/* loaded from: classes4.dex */
public final class ChannelPromiseNotifier extends PromiseNotifier<Void, ChannelFuture> implements ChannelFutureListener {
    public ChannelPromiseNotifier(ChannelPromise... promises) {
        super(promises);
    }

    public ChannelPromiseNotifier(boolean logNotifyFailure, ChannelPromise... promises) {
        super(logNotifyFailure, promises);
    }
}
