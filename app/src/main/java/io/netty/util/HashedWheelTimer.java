package io.netty.util;

import com.trossense.bl;
import io.netty.util.concurrent.ImmediateExecutor;
import io.netty.util.internal.MathUtil;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.Collections;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicLong;

/* loaded from: classes4.dex */
public class HashedWheelTimer implements Timer {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int INSTANCE_COUNT_LIMIT = 64;
    public static final int WORKER_STATE_INIT = 0;
    public static final int WORKER_STATE_SHUTDOWN = 2;
    public static final int WORKER_STATE_STARTED = 1;
    private final Queue<HashedWheelTimeout> cancelledTimeouts;
    private final ResourceLeakTracker<HashedWheelTimer> leak;
    private final int mask;
    private final long maxPendingTimeouts;
    private final AtomicLong pendingTimeouts;
    private volatile long startTime;
    private final CountDownLatch startTimeInitialized;
    private final Executor taskExecutor;
    private final long tickDuration;
    private final Queue<HashedWheelTimeout> timeouts;
    private final HashedWheelBucket[] wheel;
    private final Worker worker;
    private volatile int workerState;
    private final Thread workerThread;
    static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) HashedWheelTimer.class);
    private static final AtomicInteger INSTANCE_COUNTER = new AtomicInteger();
    private static final AtomicBoolean WARNED_TOO_MANY_INSTANCES = new AtomicBoolean();
    private static final long MILLISECOND_NANOS = TimeUnit.MILLISECONDS.toNanos(1);
    private static final ResourceLeakDetector<HashedWheelTimer> leakDetector = ResourceLeakDetectorFactory.instance().newResourceLeakDetector(HashedWheelTimer.class, 1);
    private static final AtomicIntegerFieldUpdater<HashedWheelTimer> WORKER_STATE_UPDATER = AtomicIntegerFieldUpdater.newUpdater(HashedWheelTimer.class, "workerState");

    public HashedWheelTimer() {
        this(Executors.defaultThreadFactory());
    }

    public HashedWheelTimer(long tickDuration, TimeUnit unit) {
        this(Executors.defaultThreadFactory(), tickDuration, unit);
    }

    public HashedWheelTimer(long tickDuration, TimeUnit unit, int ticksPerWheel) {
        this(Executors.defaultThreadFactory(), tickDuration, unit, ticksPerWheel);
    }

    public HashedWheelTimer(ThreadFactory threadFactory) {
        this(threadFactory, 100L, TimeUnit.MILLISECONDS);
    }

    public HashedWheelTimer(ThreadFactory threadFactory, long tickDuration, TimeUnit unit) {
        this(threadFactory, tickDuration, unit, 512);
    }

    public HashedWheelTimer(ThreadFactory threadFactory, long tickDuration, TimeUnit unit, int ticksPerWheel) {
        this(threadFactory, tickDuration, unit, ticksPerWheel, true);
    }

    public HashedWheelTimer(ThreadFactory threadFactory, long tickDuration, TimeUnit unit, int ticksPerWheel, boolean leakDetection) {
        this(threadFactory, tickDuration, unit, ticksPerWheel, leakDetection, -1L);
    }

    public HashedWheelTimer(ThreadFactory threadFactory, long tickDuration, TimeUnit unit, int ticksPerWheel, boolean leakDetection, long maxPendingTimeouts) {
        this(threadFactory, tickDuration, unit, ticksPerWheel, leakDetection, maxPendingTimeouts, ImmediateExecutor.INSTANCE);
    }

    public HashedWheelTimer(ThreadFactory threadFactory, long j, TimeUnit timeUnit, int i, boolean z, long j2, Executor executor) {
        this.worker = new Worker();
        this.startTimeInitialized = new CountDownLatch(1);
        this.timeouts = PlatformDependent.newMpscQueue();
        this.cancelledTimeouts = PlatformDependent.newMpscQueue();
        this.pendingTimeouts = new AtomicLong(0L);
        ObjectUtil.checkNotNull(threadFactory, "threadFactory");
        ObjectUtil.checkNotNull(timeUnit, "unit");
        ObjectUtil.checkPositive(j, "tickDuration");
        ObjectUtil.checkPositive(i, "ticksPerWheel");
        this.taskExecutor = (Executor) ObjectUtil.checkNotNull(executor, "taskExecutor");
        this.wheel = createWheel(i);
        this.mask = this.wheel.length - 1;
        long nanos = timeUnit.toNanos(j);
        if (nanos < Long.MAX_VALUE / this.wheel.length) {
            if (nanos < MILLISECOND_NANOS) {
                logger.warn("Configured tickDuration {} smaller than {}, using 1ms.", Long.valueOf(j), Long.valueOf(MILLISECOND_NANOS));
                this.tickDuration = MILLISECOND_NANOS;
            } else {
                this.tickDuration = nanos;
            }
            this.workerThread = threadFactory.newThread(this.worker);
            this.leak = (z || !this.workerThread.isDaemon()) ? leakDetector.track(this) : null;
            this.maxPendingTimeouts = j2;
            if (INSTANCE_COUNTER.incrementAndGet() > 64 && WARNED_TOO_MANY_INSTANCES.compareAndSet(false, true)) {
                reportTooManyInstances();
                return;
            }
            return;
        }
        throw new IllegalArgumentException(String.format("tickDuration: %d (expected: 0 < tickDuration in nanos < %d", Long.valueOf(j), Long.valueOf(Long.MAX_VALUE / this.wheel.length)));
    }

    protected void finalize() throws Throwable {
        try {
            super.finalize();
        } finally {
            if (WORKER_STATE_UPDATER.getAndSet(this, 2) != 2) {
                INSTANCE_COUNTER.decrementAndGet();
            }
        }
    }

    private static HashedWheelBucket[] createWheel(int ticksPerWheel) {
        HashedWheelBucket[] wheel = new HashedWheelBucket[MathUtil.findNextPositivePowerOfTwo(ticksPerWheel)];
        for (int i = 0; i < wheel.length; i++) {
            wheel[i] = new HashedWheelBucket();
        }
        return wheel;
    }

    public void start() {
        switch (WORKER_STATE_UPDATER.get(this)) {
            case 0:
                if (WORKER_STATE_UPDATER.compareAndSet(this, 0, 1)) {
                    this.workerThread.start();
                    break;
                }
                break;
            case 1:
                break;
            case 2:
                throw new IllegalStateException("cannot be started once stopped");
            default:
                throw new Error("Invalid WorkerState");
        }
        while (this.startTime == 0) {
            try {
                this.startTimeInitialized.await();
            } catch (InterruptedException e) {
            }
        }
    }

    @Override // io.netty.util.Timer
    public Set<Timeout> stop() {
        if (Thread.currentThread() == this.workerThread) {
            throw new IllegalStateException(HashedWheelTimer.class.getSimpleName() + ".stop() cannot be called from " + TimerTask.class.getSimpleName());
        }
        if (!WORKER_STATE_UPDATER.compareAndSet(this, 1, 2)) {
            if (WORKER_STATE_UPDATER.getAndSet(this, 2) != 2) {
                INSTANCE_COUNTER.decrementAndGet();
                if (this.leak != null) {
                    boolean closed = this.leak.close(this);
                    if (!closed) {
                        throw new AssertionError();
                    }
                }
            }
            return Collections.emptySet();
        }
        boolean interrupted = false;
        while (this.workerThread.isAlive()) {
            try {
                this.workerThread.interrupt();
                try {
                    this.workerThread.join(100L);
                } catch (InterruptedException e) {
                    interrupted = true;
                }
            } catch (Throwable th) {
                INSTANCE_COUNTER.decrementAndGet();
                if (this.leak != null) {
                    boolean closed2 = this.leak.close(this);
                    if (!closed2) {
                        throw new AssertionError();
                    }
                }
                throw th;
            }
        }
        if (interrupted) {
            Thread.currentThread().interrupt();
        }
        INSTANCE_COUNTER.decrementAndGet();
        if (this.leak != null) {
            boolean closed3 = this.leak.close(this);
            if (!closed3) {
                throw new AssertionError();
            }
        }
        return this.worker.unprocessedTimeouts();
    }

    @Override // io.netty.util.Timer
    public Timeout newTimeout(TimerTask task, long delay, TimeUnit unit) {
        ObjectUtil.checkNotNull(task, "task");
        ObjectUtil.checkNotNull(unit, "unit");
        long pendingTimeoutsCount = this.pendingTimeouts.incrementAndGet();
        if (this.maxPendingTimeouts > 0 && pendingTimeoutsCount > this.maxPendingTimeouts) {
            this.pendingTimeouts.decrementAndGet();
            throw new RejectedExecutionException("Number of pending timeouts (" + pendingTimeoutsCount + ") is greater than or equal to maximum allowed pending timeouts (" + this.maxPendingTimeouts + ")");
        }
        start();
        long deadline = (System.nanoTime() + unit.toNanos(delay)) - this.startTime;
        if (delay > 0 && deadline < 0) {
            deadline = Long.MAX_VALUE;
        }
        HashedWheelTimeout timeout = new HashedWheelTimeout(this, task, deadline);
        this.timeouts.add(timeout);
        return timeout;
    }

    public long pendingTimeouts() {
        return this.pendingTimeouts.get();
    }

    private static void reportTooManyInstances() {
        if (logger.isErrorEnabled()) {
            String resourceType = StringUtil.simpleClassName((Class<?>) HashedWheelTimer.class);
            logger.error("You are creating too many " + resourceType + " instances. " + resourceType + " is a shared resource that must be reused across the JVM, so that only a few instances are created.");
        }
    }

    /* loaded from: classes4.dex */
    private final class Worker implements Runnable {
        private long tick;
        private final Set<Timeout> unprocessedTimeouts;

        private Worker() {
            this.unprocessedTimeouts = new HashSet();
        }

        @Override // java.lang.Runnable
        public void run() {
            HashedWheelTimer.this.startTime = System.nanoTime();
            if (HashedWheelTimer.this.startTime == 0) {
                HashedWheelTimer.this.startTime = 1L;
            }
            HashedWheelTimer.this.startTimeInitialized.countDown();
            do {
                long deadline = waitForNextTick();
                if (deadline > 0) {
                    int idx = (int) (this.tick & HashedWheelTimer.this.mask);
                    processCancelledTasks();
                    HashedWheelBucket bucket = HashedWheelTimer.this.wheel[idx];
                    transferTimeoutsToBuckets();
                    bucket.expireTimeouts(deadline);
                    this.tick++;
                }
            } while (HashedWheelTimer.WORKER_STATE_UPDATER.get(HashedWheelTimer.this) == 1);
            for (HashedWheelBucket bucket2 : HashedWheelTimer.this.wheel) {
                bucket2.clearTimeouts(this.unprocessedTimeouts);
            }
            while (true) {
                HashedWheelTimeout timeout = (HashedWheelTimeout) HashedWheelTimer.this.timeouts.poll();
                if (timeout != null) {
                    if (!timeout.isCancelled()) {
                        this.unprocessedTimeouts.add(timeout);
                    }
                } else {
                    processCancelledTasks();
                    return;
                }
            }
        }

        private void transferTimeoutsToBuckets() {
            HashedWheelTimeout timeout;
            for (int i = 0; i < 100000 && (timeout = (HashedWheelTimeout) HashedWheelTimer.this.timeouts.poll()) != null; i++) {
                if (timeout.state() != 1) {
                    long calculated = timeout.deadline / HashedWheelTimer.this.tickDuration;
                    timeout.remainingRounds = (calculated - this.tick) / HashedWheelTimer.this.wheel.length;
                    long ticks = Math.max(calculated, this.tick);
                    int stopIndex = (int) (HashedWheelTimer.this.mask & ticks);
                    HashedWheelBucket bucket = HashedWheelTimer.this.wheel[stopIndex];
                    bucket.addTimeout(timeout);
                }
            }
        }

        private void processCancelledTasks() {
            while (true) {
                HashedWheelTimeout timeout = (HashedWheelTimeout) HashedWheelTimer.this.cancelledTimeouts.poll();
                if (timeout != null) {
                    try {
                        timeout.remove();
                    } catch (Throwable t) {
                        if (HashedWheelTimer.logger.isWarnEnabled()) {
                            HashedWheelTimer.logger.warn("An exception was thrown while process a cancellation task", t);
                        }
                    }
                } else {
                    return;
                }
            }
        }

        private long waitForNextTick() {
            long deadline = HashedWheelTimer.this.tickDuration * (this.tick + 1);
            while (true) {
                long currentTime = System.nanoTime() - HashedWheelTimer.this.startTime;
                long sleepTimeMs = ((deadline - currentTime) + 999999) / 1000000;
                if (sleepTimeMs <= 0) {
                    if (currentTime == Long.MIN_VALUE) {
                        return -9223372036854775807L;
                    }
                    return currentTime;
                }
                if (PlatformDependent.isWindows()) {
                    sleepTimeMs = (sleepTimeMs / 10) * 10;
                    if (sleepTimeMs == 0) {
                        sleepTimeMs = 1;
                    }
                }
                try {
                    Thread.sleep(sleepTimeMs);
                } catch (InterruptedException e) {
                    if (HashedWheelTimer.WORKER_STATE_UPDATER.get(HashedWheelTimer.this) == 2) {
                        return Long.MIN_VALUE;
                    }
                }
            }
        }

        public Set<Timeout> unprocessedTimeouts() {
            return Collections.unmodifiableSet(this.unprocessedTimeouts);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class HashedWheelTimeout implements Timeout, Runnable {
        private static final AtomicIntegerFieldUpdater<HashedWheelTimeout> STATE_UPDATER = AtomicIntegerFieldUpdater.newUpdater(HashedWheelTimeout.class, "state");
        private static final int ST_CANCELLED = 1;
        private static final int ST_EXPIRED = 2;
        private static final int ST_INIT = 0;
        HashedWheelBucket bucket;
        private final long deadline;
        HashedWheelTimeout next;
        HashedWheelTimeout prev;
        long remainingRounds;
        private volatile int state = 0;
        private final TimerTask task;
        private final HashedWheelTimer timer;

        HashedWheelTimeout(HashedWheelTimer timer, TimerTask task, long deadline) {
            this.timer = timer;
            this.task = task;
            this.deadline = deadline;
        }

        @Override // io.netty.util.Timeout
        public Timer timer() {
            return this.timer;
        }

        @Override // io.netty.util.Timeout
        public TimerTask task() {
            return this.task;
        }

        @Override // io.netty.util.Timeout
        public boolean cancel() {
            if (compareAndSetState(0, 1)) {
                this.timer.cancelledTimeouts.add(this);
                return true;
            }
            return false;
        }

        void remove() {
            HashedWheelBucket bucket = this.bucket;
            if (bucket == null) {
                this.timer.pendingTimeouts.decrementAndGet();
            } else {
                bucket.remove(this);
            }
        }

        public boolean compareAndSetState(int expected, int state) {
            return STATE_UPDATER.compareAndSet(this, expected, state);
        }

        public int state() {
            return this.state;
        }

        @Override // io.netty.util.Timeout
        public boolean isCancelled() {
            return state() == 1;
        }

        @Override // io.netty.util.Timeout
        public boolean isExpired() {
            return state() == 2;
        }

        public void expire() {
            if (compareAndSetState(0, 2)) {
                try {
                    this.timer.taskExecutor.execute(this);
                } catch (Throwable t) {
                    if (HashedWheelTimer.logger.isWarnEnabled()) {
                        HashedWheelTimer.logger.warn("An exception was thrown while submit " + TimerTask.class.getSimpleName() + " for execution.", t);
                    }
                }
            }
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                this.task.run(this);
            } catch (Throwable t) {
                if (HashedWheelTimer.logger.isWarnEnabled()) {
                    HashedWheelTimer.logger.warn("An exception was thrown by " + TimerTask.class.getSimpleName() + '.', t);
                }
            }
        }

        public String toString() {
            long currentTime = System.nanoTime();
            long remaining = (this.deadline - currentTime) + this.timer.startTime;
            StringBuilder buf = new StringBuilder(bl.cq).append(StringUtil.simpleClassName(this)).append('(').append("deadline: ");
            if (remaining > 0) {
                buf.append(remaining).append(" ns later");
            } else if (remaining < 0) {
                buf.append(-remaining).append(" ns ago");
            } else {
                buf.append("now");
            }
            if (isCancelled()) {
                buf.append(", cancelled");
            }
            return buf.append(", task: ").append(task()).append(')').toString();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class HashedWheelBucket {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private HashedWheelTimeout head;
        private HashedWheelTimeout tail;

        private HashedWheelBucket() {
        }

        public void addTimeout(HashedWheelTimeout timeout) {
            if (timeout.bucket != null) {
                throw new AssertionError();
            }
            timeout.bucket = this;
            if (this.head == null) {
                this.tail = timeout;
                this.head = timeout;
            } else {
                this.tail.next = timeout;
                timeout.prev = this.tail;
                this.tail = timeout;
            }
        }

        public void expireTimeouts(long deadline) {
            HashedWheelTimeout timeout = this.head;
            while (timeout != null) {
                HashedWheelTimeout next = timeout.next;
                if (timeout.remainingRounds <= 0) {
                    next = remove(timeout);
                    if (timeout.deadline <= deadline) {
                        timeout.expire();
                    } else {
                        throw new IllegalStateException(String.format("timeout.deadline (%d) > deadline (%d)", Long.valueOf(timeout.deadline), Long.valueOf(deadline)));
                    }
                } else if (timeout.isCancelled()) {
                    next = remove(timeout);
                } else {
                    timeout.remainingRounds--;
                }
                timeout = next;
            }
        }

        public HashedWheelTimeout remove(HashedWheelTimeout timeout) {
            HashedWheelTimeout next = timeout.next;
            if (timeout.prev != null) {
                timeout.prev.next = next;
            }
            if (timeout.next != null) {
                timeout.next.prev = timeout.prev;
            }
            if (timeout == this.head) {
                if (timeout == this.tail) {
                    this.tail = null;
                    this.head = null;
                } else {
                    this.head = next;
                }
            } else if (timeout == this.tail) {
                this.tail = timeout.prev;
            }
            timeout.prev = null;
            timeout.next = null;
            timeout.bucket = null;
            timeout.timer.pendingTimeouts.decrementAndGet();
            return next;
        }

        public void clearTimeouts(Set<Timeout> set) {
            while (true) {
                HashedWheelTimeout timeout = pollTimeout();
                if (timeout == null) {
                    return;
                }
                if (!timeout.isExpired() && !timeout.isCancelled()) {
                    set.add(timeout);
                }
            }
        }

        private HashedWheelTimeout pollTimeout() {
            HashedWheelTimeout head = this.head;
            if (head == null) {
                return null;
            }
            HashedWheelTimeout next = head.next;
            if (next == null) {
                this.head = null;
                this.tail = null;
            } else {
                this.head = next;
                next.prev = null;
            }
            head.next = null;
            head.prev = null;
            head.bucket = null;
            return head;
        }
    }
}
