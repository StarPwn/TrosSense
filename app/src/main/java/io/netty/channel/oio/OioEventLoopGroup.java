package io.netty.channel.oio;

import io.netty.channel.ThreadPerChannelEventLoopGroup;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;

@Deprecated
/* loaded from: classes4.dex */
public class OioEventLoopGroup extends ThreadPerChannelEventLoopGroup {
    public OioEventLoopGroup() {
        this(0);
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public OioEventLoopGroup(int maxChannels) {
        this(maxChannels, (ThreadFactory) null);
    }

    public OioEventLoopGroup(int maxChannels, Executor executor) {
        super(maxChannels, executor, new Object[0]);
    }

    public OioEventLoopGroup(int maxChannels, ThreadFactory threadFactory) {
        super(maxChannels, threadFactory, new Object[0]);
    }
}
