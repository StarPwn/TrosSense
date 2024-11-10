package io.netty.handler.traffic;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.traffic.AbstractTrafficShapingHandler;
import io.netty.util.Attribute;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.AbstractCollection;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@ChannelHandler.Sharable
/* loaded from: classes4.dex */
public class GlobalChannelTrafficShapingHandler extends AbstractTrafficShapingHandler {
    private static final float DEFAULT_ACCELERATION = -0.1f;
    private static final float DEFAULT_DEVIATION = 0.1f;
    private static final float DEFAULT_SLOWDOWN = 0.4f;
    private static final float MAX_DEVIATION = 0.4f;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) GlobalChannelTrafficShapingHandler.class);
    private volatile float accelerationFactor;
    final ConcurrentMap<Integer, PerChannel> channelQueues;
    private final AtomicLong cumulativeReadBytes;
    private final AtomicLong cumulativeWrittenBytes;
    private volatile float maxDeviation;
    volatile long maxGlobalWriteSize;
    private final AtomicLong queuesSize;
    private volatile long readChannelLimit;
    private volatile boolean readDeviationActive;
    private volatile float slowDownFactor;
    private volatile long writeChannelLimit;
    private volatile boolean writeDeviationActive;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public static final class PerChannel {
        TrafficCounter channelTrafficCounter;
        long lastReadTimestamp;
        long lastWriteTimestamp;
        ArrayDeque<ToSend> messagesQueue;
        long queueSize;

        PerChannel() {
        }
    }

    void createGlobalTrafficCounter(ScheduledExecutorService executor) {
        setMaxDeviation(0.1f, 0.4f, DEFAULT_ACCELERATION);
        ObjectUtil.checkNotNullWithIAE(executor, "executor");
        TrafficCounter tc = new GlobalChannelTrafficCounter(this, executor, "GlobalChannelTC", this.checkInterval);
        setTrafficCounter(tc);
        tc.start();
    }

    @Override // io.netty.handler.traffic.AbstractTrafficShapingHandler
    protected int userDefinedWritabilityIndex() {
        return 3;
    }

    public GlobalChannelTrafficShapingHandler(ScheduledExecutorService executor, long writeGlobalLimit, long readGlobalLimit, long writeChannelLimit, long readChannelLimit, long checkInterval, long maxTime) {
        super(writeGlobalLimit, readGlobalLimit, checkInterval, maxTime);
        this.channelQueues = PlatformDependent.newConcurrentHashMap();
        this.queuesSize = new AtomicLong();
        this.cumulativeWrittenBytes = new AtomicLong();
        this.cumulativeReadBytes = new AtomicLong();
        this.maxGlobalWriteSize = 419430400L;
        createGlobalTrafficCounter(executor);
        this.writeChannelLimit = writeChannelLimit;
        this.readChannelLimit = readChannelLimit;
    }

    public GlobalChannelTrafficShapingHandler(ScheduledExecutorService executor, long writeGlobalLimit, long readGlobalLimit, long writeChannelLimit, long readChannelLimit, long checkInterval) {
        super(writeGlobalLimit, readGlobalLimit, checkInterval);
        this.channelQueues = PlatformDependent.newConcurrentHashMap();
        this.queuesSize = new AtomicLong();
        this.cumulativeWrittenBytes = new AtomicLong();
        this.cumulativeReadBytes = new AtomicLong();
        this.maxGlobalWriteSize = 419430400L;
        this.writeChannelLimit = writeChannelLimit;
        this.readChannelLimit = readChannelLimit;
        createGlobalTrafficCounter(executor);
    }

    public GlobalChannelTrafficShapingHandler(ScheduledExecutorService executor, long writeGlobalLimit, long readGlobalLimit, long writeChannelLimit, long readChannelLimit) {
        super(writeGlobalLimit, readGlobalLimit);
        this.channelQueues = PlatformDependent.newConcurrentHashMap();
        this.queuesSize = new AtomicLong();
        this.cumulativeWrittenBytes = new AtomicLong();
        this.cumulativeReadBytes = new AtomicLong();
        this.maxGlobalWriteSize = 419430400L;
        this.writeChannelLimit = writeChannelLimit;
        this.readChannelLimit = readChannelLimit;
        createGlobalTrafficCounter(executor);
    }

    public GlobalChannelTrafficShapingHandler(ScheduledExecutorService executor, long checkInterval) {
        super(checkInterval);
        this.channelQueues = PlatformDependent.newConcurrentHashMap();
        this.queuesSize = new AtomicLong();
        this.cumulativeWrittenBytes = new AtomicLong();
        this.cumulativeReadBytes = new AtomicLong();
        this.maxGlobalWriteSize = 419430400L;
        createGlobalTrafficCounter(executor);
    }

    public GlobalChannelTrafficShapingHandler(ScheduledExecutorService executor) {
        this.channelQueues = PlatformDependent.newConcurrentHashMap();
        this.queuesSize = new AtomicLong();
        this.cumulativeWrittenBytes = new AtomicLong();
        this.cumulativeReadBytes = new AtomicLong();
        this.maxGlobalWriteSize = 419430400L;
        createGlobalTrafficCounter(executor);
    }

    public float maxDeviation() {
        return this.maxDeviation;
    }

    public float accelerationFactor() {
        return this.accelerationFactor;
    }

    public float slowDownFactor() {
        return this.slowDownFactor;
    }

    public void setMaxDeviation(float maxDeviation, float slowDownFactor, float accelerationFactor) {
        if (maxDeviation > 0.4f) {
            throw new IllegalArgumentException("maxDeviation must be <= 0.4");
        }
        ObjectUtil.checkPositiveOrZero(slowDownFactor, "slowDownFactor");
        if (accelerationFactor > 0.0f) {
            throw new IllegalArgumentException("accelerationFactor must be <= 0");
        }
        this.maxDeviation = maxDeviation;
        this.accelerationFactor = accelerationFactor + 1.0f;
        this.slowDownFactor = 1.0f + slowDownFactor;
    }

    private void computeDeviationCumulativeBytes() {
        long maxWrittenBytes = 0;
        long maxReadBytes = 0;
        long minWrittenBytes = Long.MAX_VALUE;
        long minReadBytes = Long.MAX_VALUE;
        for (PerChannel perChannel : this.channelQueues.values()) {
            long value = perChannel.channelTrafficCounter.cumulativeWrittenBytes();
            if (maxWrittenBytes < value) {
                maxWrittenBytes = value;
            }
            if (minWrittenBytes > value) {
                minWrittenBytes = value;
            }
            long value2 = perChannel.channelTrafficCounter.cumulativeReadBytes();
            if (maxReadBytes < value2) {
                maxReadBytes = value2;
            }
            if (minReadBytes > value2) {
                minReadBytes = value2;
            }
        }
        boolean z = false;
        boolean multiple = this.channelQueues.size() > 1;
        this.readDeviationActive = multiple && minReadBytes < maxReadBytes / 2;
        if (multiple && minWrittenBytes < maxWrittenBytes / 2) {
            z = true;
        }
        this.writeDeviationActive = z;
        this.cumulativeWrittenBytes.set(maxWrittenBytes);
        this.cumulativeReadBytes.set(maxReadBytes);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.traffic.AbstractTrafficShapingHandler
    public void doAccounting(TrafficCounter counter) {
        computeDeviationCumulativeBytes();
        super.doAccounting(counter);
    }

    private long computeBalancedWait(float maxLocal, float maxGlobal, long wait) {
        float ratio;
        if (maxGlobal == 0.0f) {
            return wait;
        }
        float ratio2 = maxLocal / maxGlobal;
        if (ratio2 <= this.maxDeviation) {
            ratio = this.accelerationFactor;
        } else {
            if (ratio2 < 1.0f - this.maxDeviation) {
                return wait;
            }
            ratio = this.slowDownFactor;
            if (wait < 10) {
                wait = 10;
            }
        }
        return ((float) wait) * ratio;
    }

    public long getMaxGlobalWriteSize() {
        return this.maxGlobalWriteSize;
    }

    public void setMaxGlobalWriteSize(long maxGlobalWriteSize) {
        this.maxGlobalWriteSize = ObjectUtil.checkPositive(maxGlobalWriteSize, "maxGlobalWriteSize");
    }

    public long queuesSize() {
        return this.queuesSize.get();
    }

    public void configureChannel(long newWriteLimit, long newReadLimit) {
        this.writeChannelLimit = newWriteLimit;
        this.readChannelLimit = newReadLimit;
        long now = TrafficCounter.milliSecondFromNano();
        for (PerChannel perChannel : this.channelQueues.values()) {
            perChannel.channelTrafficCounter.resetAccounting(now);
        }
    }

    public long getWriteChannelLimit() {
        return this.writeChannelLimit;
    }

    public void setWriteChannelLimit(long writeLimit) {
        this.writeChannelLimit = writeLimit;
        long now = TrafficCounter.milliSecondFromNano();
        for (PerChannel perChannel : this.channelQueues.values()) {
            perChannel.channelTrafficCounter.resetAccounting(now);
        }
    }

    public long getReadChannelLimit() {
        return this.readChannelLimit;
    }

    public void setReadChannelLimit(long readLimit) {
        this.readChannelLimit = readLimit;
        long now = TrafficCounter.milliSecondFromNano();
        for (PerChannel perChannel : this.channelQueues.values()) {
            perChannel.channelTrafficCounter.resetAccounting(now);
        }
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
            perChannel2.channelTrafficCounter = new TrafficCounter(this, null, "ChannelTC" + ctx.channel().hashCode(), this.checkInterval);
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
        this.trafficCounter.resetCumulativeTime();
        super.handlerAdded(ctx);
    }

    @Override // io.netty.handler.traffic.AbstractTrafficShapingHandler, io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        this.trafficCounter.resetCumulativeTime();
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
                        perChannel.channelTrafficCounter.bytesRealWriteFlowControl(size);
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

    @Override // io.netty.handler.traffic.AbstractTrafficShapingHandler, io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        long now;
        long size = calculateSize(msg);
        long now2 = TrafficCounter.milliSecondFromNano();
        if (size > 0) {
            long waitGlobal = this.trafficCounter.readTimeToWait(size, getReadLimit(), this.maxTime, now2);
            Integer key = Integer.valueOf(ctx.channel().hashCode());
            PerChannel perChannel = this.channelQueues.get(key);
            long wait = 0;
            if (perChannel != null) {
                long wait2 = perChannel.channelTrafficCounter.readTimeToWait(size, this.readChannelLimit, this.maxTime, now2);
                if (!this.readDeviationActive) {
                    wait = wait2;
                } else {
                    long maxLocalRead = perChannel.channelTrafficCounter.cumulativeReadBytes();
                    long maxGlobalRead = this.cumulativeReadBytes.get();
                    if (maxLocalRead <= 0) {
                        maxLocalRead = 0;
                    }
                    if (maxGlobalRead < maxLocalRead) {
                        maxGlobalRead = maxLocalRead;
                    }
                    wait = computeBalancedWait((float) maxLocalRead, (float) maxGlobalRead, wait2);
                }
            }
            if (wait < waitGlobal) {
                wait = waitGlobal;
            }
            now = now2;
            long wait3 = checkWaitReadTime(ctx, wait, now2);
            if (wait3 >= 10) {
                Channel channel = ctx.channel();
                ChannelConfig config = channel.config();
                if (logger.isDebugEnabled()) {
                    logger.debug("Read Suspend: " + wait3 + ':' + config.isAutoRead() + ':' + isHandlerActive(ctx));
                }
                if (config.isAutoRead() && isHandlerActive(ctx)) {
                    config.setAutoRead(false);
                    channel.attr(READ_SUSPENDED).set(true);
                    Attribute<Runnable> attr = channel.attr(REOPEN_TASK);
                    Runnable reopenTask = attr.get();
                    if (reopenTask == null) {
                        reopenTask = new AbstractTrafficShapingHandler.ReopenReadTimerTask(ctx);
                        attr.set(reopenTask);
                    }
                    ctx.executor().schedule(reopenTask, wait3, TimeUnit.MILLISECONDS);
                    if (logger.isDebugEnabled()) {
                        logger.debug("Suspend final status => " + config.isAutoRead() + ':' + isHandlerActive(ctx) + " will reopened at: " + wait3);
                    }
                }
            }
        } else {
            now = now2;
        }
        informReadOperation(ctx, now);
        ctx.fireChannelRead(msg);
    }

    @Override // io.netty.handler.traffic.AbstractTrafficShapingHandler
    protected long checkWaitReadTime(ChannelHandlerContext ctx, long wait, long now) {
        Integer key = Integer.valueOf(ctx.channel().hashCode());
        PerChannel perChannel = this.channelQueues.get(key);
        if (perChannel != null && wait > this.maxTime && (now + wait) - perChannel.lastReadTimestamp > this.maxTime) {
            return this.maxTime;
        }
        return wait;
    }

    @Override // io.netty.handler.traffic.AbstractTrafficShapingHandler
    protected void informReadOperation(ChannelHandlerContext ctx, long now) {
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

    protected long maximumCumulativeWrittenBytes() {
        return this.cumulativeWrittenBytes.get();
    }

    protected long maximumCumulativeReadBytes() {
        return this.cumulativeReadBytes.get();
    }

    public Collection<TrafficCounter> channelTrafficCounters() {
        return new AbstractCollection<TrafficCounter>() { // from class: io.netty.handler.traffic.GlobalChannelTrafficShapingHandler.1
            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
            public Iterator<TrafficCounter> iterator() {
                return new Iterator<TrafficCounter>() { // from class: io.netty.handler.traffic.GlobalChannelTrafficShapingHandler.1.1
                    final Iterator<PerChannel> iter;

                    {
                        this.iter = GlobalChannelTrafficShapingHandler.this.channelQueues.values().iterator();
                    }

                    @Override // java.util.Iterator
                    public boolean hasNext() {
                        return this.iter.hasNext();
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.util.Iterator
                    public TrafficCounter next() {
                        return this.iter.next().channelTrafficCounter;
                    }

                    @Override // java.util.Iterator
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }

            @Override // java.util.AbstractCollection, java.util.Collection
            public int size() {
                return GlobalChannelTrafficShapingHandler.this.channelQueues.size();
            }
        };
    }

    @Override // io.netty.handler.traffic.AbstractTrafficShapingHandler, io.netty.channel.ChannelDuplexHandler, io.netty.channel.ChannelOutboundHandler
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        long wait;
        long size = calculateSize(msg);
        long now = TrafficCounter.milliSecondFromNano();
        if (size > 0) {
            long waitGlobal = this.trafficCounter.writeTimeToWait(size, getWriteLimit(), this.maxTime, now);
            Integer key = Integer.valueOf(ctx.channel().hashCode());
            PerChannel perChannel = this.channelQueues.get(key);
            long wait2 = 0;
            if (perChannel != null) {
                long wait3 = perChannel.channelTrafficCounter.writeTimeToWait(size, this.writeChannelLimit, this.maxTime, now);
                if (!this.writeDeviationActive) {
                    wait2 = wait3;
                } else {
                    long maxLocalWrite = perChannel.channelTrafficCounter.cumulativeWrittenBytes();
                    long maxGlobalWrite = this.cumulativeWrittenBytes.get();
                    if (maxLocalWrite <= 0) {
                        maxLocalWrite = 0;
                    }
                    if (maxGlobalWrite < maxLocalWrite) {
                        maxGlobalWrite = maxLocalWrite;
                    }
                    wait2 = computeBalancedWait((float) maxLocalWrite, (float) maxGlobalWrite, wait3);
                }
            }
            if (wait2 >= waitGlobal) {
                wait = wait2;
            } else {
                wait = waitGlobal;
            }
            if (wait >= 10) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Write suspend: " + wait + ':' + ctx.channel().config().isAutoRead() + ':' + isHandlerActive(ctx));
                }
                submitWrite(ctx, msg, size, wait, now, promise);
                return;
            }
        }
        submitWrite(ctx, msg, size, 0L, now, promise);
    }

    /* JADX WARN: Removed duplicated region for block: B:28:0x00bb  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x00c0  */
    @Override // io.netty.handler.traffic.AbstractTrafficShapingHandler
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected void submitWrite(final io.netty.channel.ChannelHandlerContext r24, java.lang.Object r25, long r26, long r28, long r30, io.netty.channel.ChannelPromise r32) {
        /*
            Method dump skipped, instructions count: 249
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.handler.traffic.GlobalChannelTrafficShapingHandler.submitWrite(io.netty.channel.ChannelHandlerContext, java.lang.Object, long, long, long, io.netty.channel.ChannelPromise):void");
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
                        perChannel.channelTrafficCounter.bytesRealWriteFlowControl(size);
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

    @Override // io.netty.handler.traffic.AbstractTrafficShapingHandler
    public String toString() {
        return new StringBuilder(340).append(super.toString()).append(" Write Channel Limit: ").append(this.writeChannelLimit).append(" Read Channel Limit: ").append(this.readChannelLimit).toString();
    }
}
