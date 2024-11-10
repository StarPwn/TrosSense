package io.netty.channel;

import io.netty.util.concurrent.DefaultThreadFactory;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;

/* loaded from: classes4.dex */
public class DefaultEventLoop extends SingleThreadEventLoop {
    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public DefaultEventLoop() {
        this((EventLoopGroup) null);
    }

    public DefaultEventLoop(ThreadFactory threadFactory) {
        this((EventLoopGroup) null, threadFactory);
    }

    public DefaultEventLoop(Executor executor) {
        this((EventLoopGroup) null, executor);
    }

    public DefaultEventLoop(EventLoopGroup parent) {
        this(parent, new DefaultThreadFactory((Class<?>) DefaultEventLoop.class));
    }

    public DefaultEventLoop(EventLoopGroup parent, ThreadFactory threadFactory) {
        super(parent, threadFactory, true);
    }

    public DefaultEventLoop(EventLoopGroup parent, Executor executor) {
        super(parent, executor, true);
    }

    @Override // io.netty.util.concurrent.SingleThreadEventExecutor
    protected void run() {
        do {
            Runnable task = takeTask();
            if (task != null) {
                runTask(task);
                updateLastExecutionTime();
            }
        } while (!confirmShutdown());
    }
}
