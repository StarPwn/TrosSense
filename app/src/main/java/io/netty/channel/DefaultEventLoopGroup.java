package io.netty.channel;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;

/* loaded from: classes4.dex */
public class DefaultEventLoopGroup extends MultithreadEventLoopGroup {
    public DefaultEventLoopGroup() {
        this(0);
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public DefaultEventLoopGroup(int nThreads) {
        this(nThreads, (ThreadFactory) null);
    }

    public DefaultEventLoopGroup(ThreadFactory threadFactory) {
        this(0, threadFactory);
    }

    public DefaultEventLoopGroup(int nThreads, ThreadFactory threadFactory) {
        super(nThreads, threadFactory, new Object[0]);
    }

    public DefaultEventLoopGroup(int nThreads, Executor executor) {
        super(nThreads, executor, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.MultithreadEventLoopGroup, io.netty.util.concurrent.MultithreadEventExecutorGroup
    public EventLoop newChild(Executor executor, Object... args) throws Exception {
        return new DefaultEventLoop(this, executor);
    }
}
