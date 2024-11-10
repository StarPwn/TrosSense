package io.netty.channel;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

@Deprecated
/* loaded from: classes4.dex */
public class ThreadPerChannelEventLoop extends SingleThreadEventLoop {
    private Channel ch;
    private final ThreadPerChannelEventLoopGroup parent;

    public ThreadPerChannelEventLoop(ThreadPerChannelEventLoopGroup parent) {
        super((EventLoopGroup) parent, parent.executor, true);
        this.parent = parent;
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [io.netty.channel.ChannelFuture] */
    @Override // io.netty.channel.SingleThreadEventLoop, io.netty.channel.EventLoopGroup
    public ChannelFuture register(ChannelPromise promise) {
        return super.register(promise).addListener((GenericFutureListener<? extends Future<? super Void>>) new ChannelFutureListener() { // from class: io.netty.channel.ThreadPerChannelEventLoop.1
            @Override // io.netty.util.concurrent.GenericFutureListener
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    ThreadPerChannelEventLoop.this.ch = future.channel();
                } else {
                    ThreadPerChannelEventLoop.this.deregister();
                }
            }
        });
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [io.netty.channel.ChannelFuture] */
    @Override // io.netty.channel.SingleThreadEventLoop, io.netty.channel.EventLoopGroup
    @Deprecated
    public ChannelFuture register(Channel channel, ChannelPromise promise) {
        return super.register(channel, promise).addListener((GenericFutureListener<? extends Future<? super Void>>) new ChannelFutureListener() { // from class: io.netty.channel.ThreadPerChannelEventLoop.2
            @Override // io.netty.util.concurrent.GenericFutureListener
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    ThreadPerChannelEventLoop.this.ch = future.channel();
                } else {
                    ThreadPerChannelEventLoop.this.deregister();
                }
            }
        });
    }

    @Override // io.netty.util.concurrent.SingleThreadEventExecutor
    protected void run() {
        while (true) {
            Runnable task = takeTask();
            if (task != null) {
                task.run();
                updateLastExecutionTime();
            }
            Channel ch = this.ch;
            if (isShuttingDown()) {
                if (ch != null) {
                    ch.unsafe().close(ch.unsafe().voidPromise());
                }
                if (confirmShutdown()) {
                    return;
                }
            } else if (ch != null && !ch.isRegistered()) {
                runAllTasks();
                deregister();
            }
        }
    }

    protected void deregister() {
        this.ch = null;
        this.parent.activeChildren.remove(this);
        this.parent.idleChildren.add(this);
    }

    @Override // io.netty.channel.SingleThreadEventLoop
    public int registeredChannels() {
        return 1;
    }
}
