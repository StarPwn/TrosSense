package io.netty.handler.traffic;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/* loaded from: classes4.dex */
public class ChannelTrafficShapingHandler extends AbstractTrafficShapingHandler {
    private final ArrayDeque<ToSend> messagesQueue;
    private long queueSize;

    public ChannelTrafficShapingHandler(long writeLimit, long readLimit, long checkInterval, long maxTime) {
        super(writeLimit, readLimit, checkInterval, maxTime);
        this.messagesQueue = new ArrayDeque<>();
    }

    public ChannelTrafficShapingHandler(long writeLimit, long readLimit, long checkInterval) {
        super(writeLimit, readLimit, checkInterval);
        this.messagesQueue = new ArrayDeque<>();
    }

    public ChannelTrafficShapingHandler(long writeLimit, long readLimit) {
        super(writeLimit, readLimit);
        this.messagesQueue = new ArrayDeque<>();
    }

    public ChannelTrafficShapingHandler(long checkInterval) {
        super(checkInterval);
        this.messagesQueue = new ArrayDeque<>();
    }

    @Override // io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        TrafficCounter trafficCounter = new TrafficCounter(this, ctx.executor(), "ChannelTC" + ctx.channel().hashCode(), this.checkInterval);
        setTrafficCounter(trafficCounter);
        trafficCounter.start();
        super.handlerAdded(ctx);
    }

    @Override // io.netty.handler.traffic.AbstractTrafficShapingHandler, io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        this.trafficCounter.stop();
        synchronized (this) {
            if (ctx.channel().isActive()) {
                Iterator<ToSend> it2 = this.messagesQueue.iterator();
                while (it2.hasNext()) {
                    ToSend toSend = it2.next();
                    long size = calculateSize(toSend.toSend);
                    this.trafficCounter.bytesRealWriteFlowControl(size);
                    this.queueSize -= size;
                    ctx.write(toSend.toSend, toSend.promise);
                }
            } else {
                Iterator<ToSend> it3 = this.messagesQueue.iterator();
                while (it3.hasNext()) {
                    ToSend toSend2 = it3.next();
                    if (toSend2.toSend instanceof ByteBuf) {
                        ((ByteBuf) toSend2.toSend).release();
                    }
                }
            }
            this.messagesQueue.clear();
        }
        releaseWriteSuspended(ctx);
        releaseReadSuspended(ctx);
        super.handlerRemoved(ctx);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class ToSend {
        final ChannelPromise promise;
        final long relativeTimeAction;
        final Object toSend;

        private ToSend(long delay, Object toSend, ChannelPromise promise) {
            this.relativeTimeAction = delay;
            this.toSend = toSend;
            this.promise = promise;
        }
    }

    @Override // io.netty.handler.traffic.AbstractTrafficShapingHandler
    void submitWrite(final ChannelHandlerContext ctx, Object msg, long size, long delay, long now, ChannelPromise promise) {
        synchronized (this) {
            try {
                if (delay == 0) {
                    try {
                        if (this.messagesQueue.isEmpty()) {
                            this.trafficCounter.bytesRealWriteFlowControl(size);
                            ctx.write(msg, promise);
                            return;
                        }
                    } catch (Throwable th) {
                        th = th;
                        throw th;
                    }
                }
                ToSend newToSend = new ToSend(delay + now, msg, promise);
                this.messagesQueue.addLast(newToSend);
                this.queueSize += size;
                checkWriteSuspend(ctx, delay, this.queueSize);
                final long futureNow = newToSend.relativeTimeAction;
                ctx.executor().schedule(new Runnable() { // from class: io.netty.handler.traffic.ChannelTrafficShapingHandler.1
                    @Override // java.lang.Runnable
                    public void run() {
                        ChannelTrafficShapingHandler.this.sendAllValid(ctx, futureNow);
                    }
                }, delay, TimeUnit.MILLISECONDS);
            } catch (Throwable th2) {
                th = th2;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendAllValid(ChannelHandlerContext ctx, long now) {
        synchronized (this) {
            ToSend newToSend = this.messagesQueue.pollFirst();
            while (true) {
                if (newToSend != null) {
                    if (newToSend.relativeTimeAction <= now) {
                        long size = calculateSize(newToSend.toSend);
                        this.trafficCounter.bytesRealWriteFlowControl(size);
                        this.queueSize -= size;
                        ctx.write(newToSend.toSend, newToSend.promise);
                        newToSend = this.messagesQueue.pollFirst();
                    } else {
                        this.messagesQueue.addFirst(newToSend);
                        break;
                    }
                } else {
                    break;
                }
            }
            if (this.messagesQueue.isEmpty()) {
                releaseWriteSuspended(ctx);
            }
        }
        ctx.flush();
    }

    public long queueSize() {
        return this.queueSize;
    }
}
