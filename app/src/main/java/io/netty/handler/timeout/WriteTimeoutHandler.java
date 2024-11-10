package io.netty.handler.timeout;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.internal.ObjectUtil;
import java.util.concurrent.TimeUnit;

/* loaded from: classes4.dex */
public class WriteTimeoutHandler extends ChannelOutboundHandlerAdapter {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final long MIN_TIMEOUT_NANOS = TimeUnit.MILLISECONDS.toNanos(1);
    private boolean closed;
    private WriteTimeoutTask lastTask;
    private final long timeoutNanos;

    public WriteTimeoutHandler(int timeoutSeconds) {
        this(timeoutSeconds, TimeUnit.SECONDS);
    }

    public WriteTimeoutHandler(long timeout, TimeUnit unit) {
        ObjectUtil.checkNotNull(unit, "unit");
        if (timeout <= 0) {
            this.timeoutNanos = 0L;
        } else {
            this.timeoutNanos = Math.max(unit.toNanos(timeout), MIN_TIMEOUT_NANOS);
        }
    }

    @Override // io.netty.channel.ChannelOutboundHandlerAdapter, io.netty.channel.ChannelOutboundHandler
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (this.timeoutNanos > 0) {
            promise = promise.unvoid();
            scheduleTimeout(ctx, promise);
        }
        ctx.write(msg, promise);
    }

    @Override // io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        if (!ctx.executor().inEventLoop()) {
            throw new AssertionError();
        }
        WriteTimeoutTask task = this.lastTask;
        this.lastTask = null;
        while (task != null) {
            if (!task.ctx.executor().inEventLoop()) {
                throw new AssertionError();
            }
            task.scheduledFuture.cancel(false);
            WriteTimeoutTask prev = task.prev;
            task.prev = null;
            task.next = null;
            task = prev;
        }
    }

    private void scheduleTimeout(ChannelHandlerContext ctx, ChannelPromise promise) {
        WriteTimeoutTask task = new WriteTimeoutTask(ctx, promise);
        task.scheduledFuture = ctx.executor().schedule((Runnable) task, this.timeoutNanos, TimeUnit.NANOSECONDS);
        if (!task.scheduledFuture.isDone()) {
            addWriteTimeoutTask(task);
            promise.addListener((GenericFutureListener<? extends Future<? super Void>>) task);
        }
    }

    private void addWriteTimeoutTask(WriteTimeoutTask task) {
        if (!task.ctx.executor().inEventLoop()) {
            throw new AssertionError();
        }
        if (this.lastTask != null) {
            this.lastTask.next = task;
            task.prev = this.lastTask;
        }
        this.lastTask = task;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeWriteTimeoutTask(WriteTimeoutTask task) {
        if (!task.ctx.executor().inEventLoop()) {
            throw new AssertionError();
        }
        if (task == this.lastTask) {
            if (task.next != null) {
                throw new AssertionError();
            }
            this.lastTask = this.lastTask.prev;
            if (this.lastTask != null) {
                this.lastTask.next = null;
            }
        } else {
            if (task.prev == null && task.next == null) {
                return;
            }
            if (task.prev == null) {
                task.next.prev = null;
            } else {
                task.prev.next = task.next;
                task.next.prev = task.prev;
            }
        }
        task.prev = null;
        task.next = null;
    }

    protected void writeTimedOut(ChannelHandlerContext ctx) throws Exception {
        if (!this.closed) {
            ctx.fireExceptionCaught((Throwable) WriteTimeoutException.INSTANCE);
            ctx.close();
            this.closed = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class WriteTimeoutTask implements Runnable, ChannelFutureListener {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private final ChannelHandlerContext ctx;
        WriteTimeoutTask next;
        WriteTimeoutTask prev;
        private final ChannelPromise promise;
        Future<?> scheduledFuture;

        WriteTimeoutTask(ChannelHandlerContext ctx, ChannelPromise promise) {
            this.ctx = ctx;
            this.promise = promise;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (!this.promise.isDone()) {
                try {
                    WriteTimeoutHandler.this.writeTimedOut(this.ctx);
                } catch (Throwable t) {
                    this.ctx.fireExceptionCaught(t);
                }
            }
            WriteTimeoutHandler.this.removeWriteTimeoutTask(this);
        }

        @Override // io.netty.util.concurrent.GenericFutureListener
        public void operationComplete(ChannelFuture future) throws Exception {
            this.scheduledFuture.cancel(false);
            if (this.ctx.executor().inEventLoop()) {
                WriteTimeoutHandler.this.removeWriteTimeoutTask(this);
            } else {
                if (!this.promise.isDone()) {
                    throw new AssertionError();
                }
                this.ctx.executor().execute(this);
            }
        }
    }
}
