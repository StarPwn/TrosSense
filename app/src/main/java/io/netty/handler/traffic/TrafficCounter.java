package io.netty.handler.traffic;

import com.trossense.bl;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/* loaded from: classes4.dex */
public class TrafficCounter {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) TrafficCounter.class);
    final AtomicLong checkInterval;
    private final AtomicLong cumulativeReadBytes;
    private final AtomicLong cumulativeWrittenBytes;
    private final AtomicLong currentReadBytes;
    private final AtomicLong currentWrittenBytes;
    final ScheduledExecutorService executor;
    private long lastCumulativeTime;
    private volatile long lastReadBytes;
    private long lastReadThroughput;
    private volatile long lastReadingTime;
    final AtomicLong lastTime;
    private long lastWriteThroughput;
    private volatile long lastWritingTime;
    private volatile long lastWrittenBytes;
    Runnable monitor;
    volatile boolean monitorActive;
    final String name;
    private long readingTime;
    private long realWriteThroughput;
    private final AtomicLong realWrittenBytes;
    volatile ScheduledFuture<?> scheduledFuture;
    final AbstractTrafficShapingHandler trafficShapingHandler;
    private long writingTime;

    public static long milliSecondFromNano() {
        return System.nanoTime() / 1000000;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class TrafficMonitoringTask implements Runnable {
        private TrafficMonitoringTask() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (!TrafficCounter.this.monitorActive) {
                return;
            }
            TrafficCounter.this.resetAccounting(TrafficCounter.milliSecondFromNano());
            if (TrafficCounter.this.trafficShapingHandler != null) {
                TrafficCounter.this.trafficShapingHandler.doAccounting(TrafficCounter.this);
            }
        }
    }

    public synchronized void start() {
        if (this.monitorActive) {
            return;
        }
        this.lastTime.set(milliSecondFromNano());
        long localCheckInterval = this.checkInterval.get();
        if (localCheckInterval > 0 && this.executor != null) {
            this.monitorActive = true;
            this.monitor = new TrafficMonitoringTask();
            this.scheduledFuture = this.executor.scheduleAtFixedRate(this.monitor, 0L, localCheckInterval, TimeUnit.MILLISECONDS);
        }
    }

    public synchronized void stop() {
        if (this.monitorActive) {
            this.monitorActive = false;
            resetAccounting(milliSecondFromNano());
            if (this.trafficShapingHandler != null) {
                this.trafficShapingHandler.doAccounting(this);
            }
            if (this.scheduledFuture != null) {
                this.scheduledFuture.cancel(true);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void resetAccounting(long newLastTime) {
        long interval = newLastTime - this.lastTime.getAndSet(newLastTime);
        if (interval == 0) {
            return;
        }
        if (logger.isDebugEnabled() && interval > (checkInterval() << 1)) {
            logger.debug("Acct schedule not ok: " + interval + " > 2*" + checkInterval() + " from " + this.name);
        }
        this.lastReadBytes = this.currentReadBytes.getAndSet(0L);
        this.lastWrittenBytes = this.currentWrittenBytes.getAndSet(0L);
        this.lastReadThroughput = (this.lastReadBytes * 1000) / interval;
        this.lastWriteThroughput = (this.lastWrittenBytes * 1000) / interval;
        this.realWriteThroughput = (this.realWrittenBytes.getAndSet(0L) * 1000) / interval;
        this.lastWritingTime = Math.max(this.lastWritingTime, this.writingTime);
        this.lastReadingTime = Math.max(this.lastReadingTime, this.readingTime);
    }

    public TrafficCounter(ScheduledExecutorService executor, String name, long checkInterval) {
        this.currentWrittenBytes = new AtomicLong();
        this.currentReadBytes = new AtomicLong();
        this.cumulativeWrittenBytes = new AtomicLong();
        this.cumulativeReadBytes = new AtomicLong();
        this.lastTime = new AtomicLong();
        this.realWrittenBytes = new AtomicLong();
        this.checkInterval = new AtomicLong(1000L);
        this.name = (String) ObjectUtil.checkNotNull(name, "name");
        this.trafficShapingHandler = null;
        this.executor = executor;
        init(checkInterval);
    }

    public TrafficCounter(AbstractTrafficShapingHandler trafficShapingHandler, ScheduledExecutorService executor, String name, long checkInterval) {
        this.currentWrittenBytes = new AtomicLong();
        this.currentReadBytes = new AtomicLong();
        this.cumulativeWrittenBytes = new AtomicLong();
        this.cumulativeReadBytes = new AtomicLong();
        this.lastTime = new AtomicLong();
        this.realWrittenBytes = new AtomicLong();
        this.checkInterval = new AtomicLong(1000L);
        this.name = (String) ObjectUtil.checkNotNull(name, "name");
        this.trafficShapingHandler = (AbstractTrafficShapingHandler) ObjectUtil.checkNotNullWithIAE(trafficShapingHandler, "trafficShapingHandler");
        this.executor = executor;
        init(checkInterval);
    }

    private void init(long checkInterval) {
        this.lastCumulativeTime = System.currentTimeMillis();
        this.writingTime = milliSecondFromNano();
        this.readingTime = this.writingTime;
        this.lastWritingTime = this.writingTime;
        this.lastReadingTime = this.writingTime;
        configure(checkInterval);
    }

    public void configure(long newCheckInterval) {
        long newInterval = (newCheckInterval / 10) * 10;
        if (this.checkInterval.getAndSet(newInterval) != newInterval) {
            if (newInterval <= 0) {
                stop();
                this.lastTime.set(milliSecondFromNano());
            } else {
                stop();
                start();
            }
        }
    }

    void bytesRecvFlowControl(long recv) {
        this.currentReadBytes.addAndGet(recv);
        this.cumulativeReadBytes.addAndGet(recv);
    }

    void bytesWriteFlowControl(long write) {
        this.currentWrittenBytes.addAndGet(write);
        this.cumulativeWrittenBytes.addAndGet(write);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void bytesRealWriteFlowControl(long write) {
        this.realWrittenBytes.addAndGet(write);
    }

    public long checkInterval() {
        return this.checkInterval.get();
    }

    public long lastReadThroughput() {
        return this.lastReadThroughput;
    }

    public long lastWriteThroughput() {
        return this.lastWriteThroughput;
    }

    public long lastReadBytes() {
        return this.lastReadBytes;
    }

    public long lastWrittenBytes() {
        return this.lastWrittenBytes;
    }

    public long currentReadBytes() {
        return this.currentReadBytes.get();
    }

    public long currentWrittenBytes() {
        return this.currentWrittenBytes.get();
    }

    public long lastTime() {
        return this.lastTime.get();
    }

    public long cumulativeWrittenBytes() {
        return this.cumulativeWrittenBytes.get();
    }

    public long cumulativeReadBytes() {
        return this.cumulativeReadBytes.get();
    }

    public long lastCumulativeTime() {
        return this.lastCumulativeTime;
    }

    public AtomicLong getRealWrittenBytes() {
        return this.realWrittenBytes;
    }

    public long getRealWriteThroughput() {
        return this.realWriteThroughput;
    }

    public void resetCumulativeTime() {
        this.lastCumulativeTime = System.currentTimeMillis();
        this.cumulativeReadBytes.set(0L);
        this.cumulativeWrittenBytes.set(0L);
    }

    public String name() {
        return this.name;
    }

    @Deprecated
    public long readTimeToWait(long size, long limitTraffic, long maxTime) {
        return readTimeToWait(size, limitTraffic, maxTime, milliSecondFromNano());
    }

    public long readTimeToWait(long size, long limitTraffic, long maxTime, long now) {
        bytesRecvFlowControl(size);
        if (size == 0 || limitTraffic == 0) {
            return 0L;
        }
        long lastTimeCheck = this.lastTime.get();
        long sum = this.currentReadBytes.get();
        long localReadingTime = this.readingTime;
        long lastRB = this.lastReadBytes;
        long interval = now - lastTimeCheck;
        long lastRB2 = this.lastReadingTime;
        long pastDelay = Math.max(lastRB2 - lastTimeCheck, 0L);
        if (interval <= 10) {
            long pastDelay2 = sum + lastRB;
            long lastinterval = this.checkInterval.get() + interval;
            long time = (((1000 * pastDelay2) / limitTraffic) - lastinterval) + pastDelay;
            if (time <= 10) {
                this.readingTime = Math.max(localReadingTime, now);
                return 0L;
            }
            if (logger.isDebugEnabled()) {
                logger.debug("Time: " + time + ':' + pastDelay2 + ':' + lastinterval + ':' + pastDelay);
            }
            if (time > maxTime && (now + time) - localReadingTime > maxTime) {
                time = maxTime;
            }
            this.readingTime = Math.max(localReadingTime, now + time);
            return time;
        }
        long time2 = (((1000 * sum) / limitTraffic) - interval) + pastDelay;
        if (time2 <= 10) {
            this.readingTime = Math.max(localReadingTime, now);
            return 0L;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Time: " + time2 + ':' + sum + ':' + interval + ':' + pastDelay);
        }
        if (time2 > maxTime && (now + time2) - localReadingTime > maxTime) {
            time2 = maxTime;
        }
        this.readingTime = Math.max(localReadingTime, now + time2);
        return time2;
    }

    @Deprecated
    public long writeTimeToWait(long size, long limitTraffic, long maxTime) {
        return writeTimeToWait(size, limitTraffic, maxTime, milliSecondFromNano());
    }

    public long writeTimeToWait(long size, long limitTraffic, long maxTime, long now) {
        bytesWriteFlowControl(size);
        if (size != 0 && limitTraffic != 0) {
            long lastTimeCheck = this.lastTime.get();
            long sum = this.currentWrittenBytes.get();
            long lastWB = this.lastWrittenBytes;
            long localWritingTime = this.writingTime;
            long pastDelay = Math.max(this.lastWritingTime - lastTimeCheck, 0L);
            long interval = now - lastTimeCheck;
            if (interval > 10) {
                long time = (((1000 * sum) / limitTraffic) - interval) + pastDelay;
                if (time <= 10) {
                    long lastWB2 = Math.max(localWritingTime, now);
                    this.writingTime = lastWB2;
                    return 0L;
                }
                if (logger.isDebugEnabled()) {
                    logger.debug("Time: " + time + ':' + sum + ':' + interval + ':' + pastDelay);
                }
                if (time > maxTime && (now + time) - localWritingTime > maxTime) {
                    time = maxTime;
                }
                this.writingTime = Math.max(localWritingTime, now + time);
                return time;
            }
            long lastWB3 = sum + lastWB;
            long lastinterval = this.checkInterval.get() + interval;
            long time2 = (((1000 * lastWB3) / limitTraffic) - lastinterval) + pastDelay;
            if (time2 <= 10) {
                this.writingTime = Math.max(localWritingTime, now);
                return 0L;
            }
            if (logger.isDebugEnabled()) {
                logger.debug("Time: " + time2 + ':' + lastWB3 + ':' + lastinterval + ':' + pastDelay);
            }
            if (time2 > maxTime && (now + time2) - localWritingTime > maxTime) {
                time2 = maxTime;
            }
            this.writingTime = Math.max(localWritingTime, now + time2);
            return time2;
        }
        return 0L;
    }

    public String toString() {
        return new StringBuilder(bl.b0).append("Monitor ").append(this.name).append(" Current Speed Read: ").append(this.lastReadThroughput >> 10).append(" KB/s, ").append("Asked Write: ").append(this.lastWriteThroughput >> 10).append(" KB/s, ").append("Real Write: ").append(this.realWriteThroughput >> 10).append(" KB/s, ").append("Current Read: ").append(this.currentReadBytes.get() >> 10).append(" KB, ").append("Current asked Write: ").append(this.currentWrittenBytes.get() >> 10).append(" KB, ").append("Current real Write: ").append(this.realWrittenBytes.get() >> 10).append(" KB").toString();
    }
}
