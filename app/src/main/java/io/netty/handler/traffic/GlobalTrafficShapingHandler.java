package io.netty.handler.traffic;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicLong;

@ChannelHandler.Sharable
/* loaded from: classes4.dex */
public class GlobalTrafficShapingHandler extends AbstractTrafficShapingHandler {
    private final ConcurrentMap<Integer, PerChannel> channelQueues;
    long maxGlobalWriteSize;
    private final AtomicLong queuesSize;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class PerChannel {
        long lastReadTimestamp;
        long lastWriteTimestamp;
        ArrayDeque<ToSend> messagesQueue;
        long queueSize;

        private PerChannel() {
        }
    }

    void createGlobalTrafficCounter(ScheduledExecutorService executor) {
        TrafficCounter tc = new TrafficCounter(this, (ScheduledExecutorService) ObjectUtil.checkNotNull(executor, "executor"), "GlobalTC", this.checkInterval);
        setTrafficCounter(tc);
        tc.start();
    }

    @Override // io.netty.handler.traffic.AbstractTrafficShapingHandler
    protected int userDefinedWritabilityIndex() {
        return 2;
    }

    public GlobalTrafficShapingHandler(ScheduledExecutorService executor, long writeLimit, long readLimit, long checkInterval, long maxTime) {
        super(writeLimit, readLimit, checkInterval, maxTime);
        this.channelQueues = PlatformDependent.newConcurrentHashMap();
        this.queuesSize = new AtomicLong();
        this.maxGlobalWriteSize = 419430400L;
        createGlobalTrafficCounter(executor);
    }

    public GlobalTrafficShapingHandler(ScheduledExecutorService executor, long writeLimit, long readLimit, long checkInterval) {
        super(writeLimit, readLimit, checkInterval);
        this.channelQueues = PlatformDependent.newConcurrentHashMap();
        this.queuesSize = new AtomicLong();
        this.maxGlobalWriteSize = 419430400L;
        createGlobalTrafficCounter(executor);
    }

    public GlobalTrafficShapingHandler(ScheduledExecutorService executor, long writeLimit, long readLimit) {
        super(writeLimit, readLimit);
        this.channelQueues = PlatformDependent.newConcurrentHashMap();
        this.queuesSize = new AtomicLong();
        this.maxGlobalWriteSize = 419430400L;
        createGlobalTrafficCounter(executor);
    }

    public GlobalTrafficShapingHandler(ScheduledExecutorService executor, long checkInterval) {
        super(checkInterval);
        this.channelQueues = PlatformDependent.newConcurrentHashMap();
        this.queuesSize = new AtomicLong();
        this.maxGlobalWriteSize = 419430400L;
        createGlobalTrafficCounter(executor);
    }

    public GlobalTrafficShapingHandler(EventExecutor executor) {
        this.channelQueues = PlatformDependent.newConcurrentHashMap();
        this.queuesSize = new AtomicLong();
        this.maxGlobalWriteSize = 419430400L;
        createGlobalTrafficCounter(executor);
    }

    public long getMaxGlobalWriteSize() {
        return this.maxGlobalWriteSize;
    }

    public void setMaxGlobalWriteSize(long maxGlobalWriteSize) {
        this.maxGlobalWriteSize = maxGlobalWriteSize;
    }

    public long queuesSize() {
        return this.queuesSize.get();
    }

    public final void release() {
        this.trafficCounter.stop();
    }

    private PerChannel getOrSetPerChannel(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        Integer key = Integer.valueOf(channel.hashCode());
        PerChannel perChannel = this.channelQueues.get(key);
        if (perChannel == null) {
            PerChannel perChannel2 = new PerChannel();
            perChannel2.messagesQueue = new ArrayDeque<>();
            perChannel2.queueSize = 0L;
            perChannel2.lastReadTimestamp = TrafficCounter.milliSecondFromNano();
            perChannel2.lastWriteTimestamp = perChannel2.lastReadTimestamp;
            this.channelQueues.put(key, perChannel2);
            return perChannel2;
        }
        return perChannel;
    }

    @Override // io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        getOrSetPerChannel(ctx);
        super.handlerAdded(ctx);
    }

    @Override // io.netty.handler.traffic.AbstractTrafficShapingHandler, io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        Integer key = Integer.valueOf(channel.hashCode());
        PerChannel perChannel = this.channelQueues.remove(key);
        if (perChannel != null) {
            synchronized (perChannel) {
                if (channel.isActive()) {
                    Iterator<ToSend> it2 = perChannel.messagesQueue.iterator();
                    while (it2.hasNext()) {
                        ToSend toSend = it2.next();
                        long size = calculateSize(toSend.toSend);
                        this.trafficCounter.bytesRealWriteFlowControl(size);
                        perChannel.queueSize -= size;
                        this.queuesSize.addAndGet(-size);
                        ctx.write(toSend.toSend, toSend.promise);
                    }
                } else {
                    this.queuesSize.addAndGet(-perChannel.queueSize);
                    Iterator<ToSend> it3 = perChannel.messagesQueue.iterator();
                    while (it3.hasNext()) {
                        ToSend toSend2 = it3.next();
                        if (toSend2.toSend instanceof ByteBuf) {
                            ((ByteBuf) toSend2.toSend).release();
                        }
                    }
                }
                perChannel.messagesQueue.clear();
            }
        }
        releaseWriteSuspended(ctx);
        releaseReadSuspended(ctx);
        super.handlerRemoved(ctx);
    }

    @Override // io.netty.handler.traffic.AbstractTrafficShapingHandler
    long checkWaitReadTime(ChannelHandlerContext ctx, long wait, long now) {
        Integer key = Integer.valueOf(ctx.channel().hashCode());
        PerChannel perChannel = this.channelQueues.get(key);
        if (perChannel != null && wait > this.maxTime && (now + wait) - perChannel.lastReadTimestamp > this.maxTime) {
            return this.maxTime;
        }
        return wait;
    }

    @Override // io.netty.handler.traffic.AbstractTrafficShapingHandler
    void informReadOperation(ChannelHandlerContext ctx, long now) {
        Integer key = Integer.valueOf(ctx.channel().hashCode());
        PerChannel perChannel = this.channelQueues.get(key);
        if (perChannel != null) {
            perChannel.lastReadTimestamp = now;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class ToSend {
        final ChannelPromise promise;
        final long relativeTimeAction;
        final long size;
        final Object toSend;

        private ToSend(long delay, Object toSend, long size, ChannelPromise promise) {
            this.relativeTimeAction = delay;
            this.toSend = toSend;
            this.size = size;
            this.promise = promise;
        }
    }

    /* JADX WARN: Can't wrap try/catch for region: R(7:(8:(4:13|14|15|(15:17|18|19|20|21|22|23|24|25|26|(1:28)|29|(1:31)|32|33))(1:55)|25|26|(0)|29|(0)|32|33)|19|20|21|22|23|24) */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x00de, code lost:            r0 = th;     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x00df, code lost:            r11 = r1;     */
    /* JADX WARN: Removed duplicated region for block: B:28:0x00b6  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x00bb  */
    @Override // io.netty.handler.traffic.AbstractTrafficShapingHandler
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    void submitWrite(final io.netty.channel.ChannelHandlerContext r24, java.lang.Object r25, long r26, long r28, long r30, io.netty.channel.ChannelPromise r32) {
        /*
            Method dump skipped, instructions count: 244
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.handler.traffic.GlobalTrafficShapingHandler.submitWrite(io.netty.channel.ChannelHandlerContext, java.lang.Object, long, long, long, io.netty.channel.ChannelPromise):void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendAllValid(ChannelHandlerContext ctx, PerChannel perChannel, long now) {
        synchronized (perChannel) {
            ToSend newToSend = perChannel.messagesQueue.pollFirst();
            while (true) {
                if (newToSend != null) {
                    if (newToSend.relativeTimeAction <= now) {
                        long size = newToSend.size;
                        this.trafficCounter.bytesRealWriteFlowControl(size);
                        perChannel.queueSize -= size;
                        this.queuesSize.addAndGet(-size);
                        ctx.write(newToSend.toSend, newToSend.promise);
                        perChannel.lastWriteTimestamp = now;
                        newToSend = perChannel.messagesQueue.pollFirst();
                    } else {
                        perChannel.messagesQueue.addFirst(newToSend);
                        break;
                    }
                } else {
                    break;
                }
            }
            if (perChannel.messagesQueue.isEmpty()) {
                releaseWriteSuspended(ctx);
            }
        }
        ctx.flush();
    }
}
