package io.netty.util.concurrent;

import androidx.concurrent.futures.AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0;
import io.netty.util.concurrent.AbstractEventExecutor;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.ThreadExecutorMap;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.lang.Thread;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/* loaded from: classes4.dex */
public abstract class SingleThreadEventExecutor extends AbstractScheduledEventExecutor implements OrderedEventExecutor {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int ST_NOT_STARTED = 1;
    private static final int ST_SHUTDOWN = 4;
    private static final int ST_SHUTTING_DOWN = 3;
    private static final int ST_STARTED = 2;
    private static final int ST_TERMINATED = 5;
    private final boolean addTaskWakesUp;
    private final Executor executor;
    private volatile long gracefulShutdownQuietPeriod;
    private long gracefulShutdownStartTime;
    private volatile long gracefulShutdownTimeout;
    private volatile boolean interrupted;
    private long lastExecutionTime;
    private final int maxPendingTasks;
    private final RejectedExecutionHandler rejectedExecutionHandler;
    private final Set<Runnable> shutdownHooks;
    private volatile int state;
    private final Queue<Runnable> taskQueue;
    private final Promise<?> terminationFuture;
    private volatile Thread thread;
    private final CountDownLatch threadLock;
    private volatile ThreadProperties threadProperties;
    static final int DEFAULT_MAX_PENDING_EXECUTOR_TASKS = Math.max(16, SystemPropertyUtil.getInt("io.netty.eventexecutor.maxPendingTasks", Integer.MAX_VALUE));
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) SingleThreadEventExecutor.class);
    private static final Runnable NOOP_TASK = new Runnable() { // from class: io.netty.util.concurrent.SingleThreadEventExecutor.1
        @Override // java.lang.Runnable
        public void run() {
        }
    };
    private static final AtomicIntegerFieldUpdater<SingleThreadEventExecutor> STATE_UPDATER = AtomicIntegerFieldUpdater.newUpdater(SingleThreadEventExecutor.class, "state");
    private static final AtomicReferenceFieldUpdater<SingleThreadEventExecutor, ThreadProperties> PROPERTIES_UPDATER = AtomicReferenceFieldUpdater.newUpdater(SingleThreadEventExecutor.class, ThreadProperties.class, "threadProperties");
    private static final long SCHEDULE_PURGE_INTERVAL = TimeUnit.SECONDS.toNanos(1);

    @Deprecated
    /* loaded from: classes4.dex */
    protected interface NonWakeupRunnable extends AbstractEventExecutor.LazyRunnable {
    }

    protected abstract void run();

    /* JADX INFO: Access modifiers changed from: protected */
    public SingleThreadEventExecutor(EventExecutorGroup parent, ThreadFactory threadFactory, boolean addTaskWakesUp) {
        this(parent, new ThreadPerTaskExecutor(threadFactory), addTaskWakesUp);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public SingleThreadEventExecutor(EventExecutorGroup parent, ThreadFactory threadFactory, boolean addTaskWakesUp, int maxPendingTasks, RejectedExecutionHandler rejectedHandler) {
        this(parent, new ThreadPerTaskExecutor(threadFactory), addTaskWakesUp, maxPendingTasks, rejectedHandler);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public SingleThreadEventExecutor(EventExecutorGroup parent, Executor executor, boolean addTaskWakesUp) {
        this(parent, executor, addTaskWakesUp, DEFAULT_MAX_PENDING_EXECUTOR_TASKS, RejectedExecutionHandlers.reject());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public SingleThreadEventExecutor(EventExecutorGroup parent, Executor executor, boolean addTaskWakesUp, int maxPendingTasks, RejectedExecutionHandler rejectedHandler) {
        super(parent);
        this.threadLock = new CountDownLatch(1);
        this.shutdownHooks = new LinkedHashSet();
        this.state = 1;
        this.terminationFuture = new DefaultPromise(GlobalEventExecutor.INSTANCE);
        this.addTaskWakesUp = addTaskWakesUp;
        this.maxPendingTasks = Math.max(16, maxPendingTasks);
        this.executor = ThreadExecutorMap.apply(executor, this);
        this.taskQueue = newTaskQueue(this.maxPendingTasks);
        this.rejectedExecutionHandler = (RejectedExecutionHandler) ObjectUtil.checkNotNull(rejectedHandler, "rejectedHandler");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public SingleThreadEventExecutor(EventExecutorGroup parent, Executor executor, boolean addTaskWakesUp, Queue<Runnable> taskQueue, RejectedExecutionHandler rejectedHandler) {
        super(parent);
        this.threadLock = new CountDownLatch(1);
        this.shutdownHooks = new LinkedHashSet();
        this.state = 1;
        this.terminationFuture = new DefaultPromise(GlobalEventExecutor.INSTANCE);
        this.addTaskWakesUp = addTaskWakesUp;
        this.maxPendingTasks = DEFAULT_MAX_PENDING_EXECUTOR_TASKS;
        this.executor = ThreadExecutorMap.apply(executor, this);
        this.taskQueue = (Queue) ObjectUtil.checkNotNull(taskQueue, "taskQueue");
        this.rejectedExecutionHandler = (RejectedExecutionHandler) ObjectUtil.checkNotNull(rejectedHandler, "rejectedHandler");
    }

    @Deprecated
    protected Queue<Runnable> newTaskQueue() {
        return newTaskQueue(this.maxPendingTasks);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Queue<Runnable> newTaskQueue(int maxPendingTasks) {
        return new LinkedBlockingQueue(maxPendingTasks);
    }

    protected void interruptThread() {
        Thread currentThread = this.thread;
        if (currentThread == null) {
            this.interrupted = true;
        } else {
            currentThread.interrupt();
        }
    }

    protected Runnable pollTask() {
        if (!inEventLoop()) {
            throw new AssertionError();
        }
        return pollTaskFrom(this.taskQueue);
    }

    protected static Runnable pollTaskFrom(Queue<Runnable> taskQueue) {
        Runnable task;
        do {
            task = taskQueue.poll();
        } while (task == WAKEUP_TASK);
        return task;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Runnable takeTask() {
        Runnable task;
        if (!inEventLoop()) {
            throw new AssertionError();
        }
        if (!(this.taskQueue instanceof BlockingQueue)) {
            throw new UnsupportedOperationException();
        }
        BlockingQueue<Runnable> taskQueue = (BlockingQueue) this.taskQueue;
        do {
            ScheduledFutureTask<?> scheduledTask = peekScheduledTask();
            if (scheduledTask == null) {
                Runnable task2 = null;
                try {
                    task2 = taskQueue.take();
                    if (task2 == WAKEUP_TASK) {
                        return null;
                    }
                    return task2;
                } catch (InterruptedException e) {
                    return task2;
                }
            }
            long delayNanos = scheduledTask.delayNanos();
            task = null;
            if (delayNanos > 0) {
                try {
                    task = taskQueue.poll(delayNanos, TimeUnit.NANOSECONDS);
                } catch (InterruptedException e2) {
                    return null;
                }
            }
            if (task == null) {
                fetchFromScheduledTaskQueue();
                Runnable task3 = taskQueue.poll();
                task = task3;
            }
        } while (task == null);
        return task;
    }

    private boolean fetchFromScheduledTaskQueue() {
        Runnable scheduledTask;
        if (this.scheduledTaskQueue == null || this.scheduledTaskQueue.isEmpty()) {
            return true;
        }
        long nanoTime = getCurrentTimeNanos();
        do {
            scheduledTask = pollScheduledTask(nanoTime);
            if (scheduledTask == null) {
                return true;
            }
        } while (this.taskQueue.offer(scheduledTask));
        this.scheduledTaskQueue.add((ScheduledFutureTask) scheduledTask);
        return false;
    }

    private boolean executeExpiredScheduledTasks() {
        long nanoTime;
        Runnable pollScheduledTask;
        if (this.scheduledTaskQueue == null || this.scheduledTaskQueue.isEmpty() || (scheduledTask = pollScheduledTask((nanoTime = getCurrentTimeNanos()))) == null) {
            return false;
        }
        do {
            safeExecute(scheduledTask);
            pollScheduledTask = pollScheduledTask(nanoTime);
            Runnable scheduledTask = pollScheduledTask;
        } while (pollScheduledTask != null);
        return true;
    }

    protected Runnable peekTask() {
        if (!inEventLoop()) {
            throw new AssertionError();
        }
        return this.taskQueue.peek();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean hasTasks() {
        if (!inEventLoop()) {
            throw new AssertionError();
        }
        return !this.taskQueue.isEmpty();
    }

    public int pendingTasks() {
        return this.taskQueue.size();
    }

    protected void addTask(Runnable task) {
        ObjectUtil.checkNotNull(task, "task");
        if (!offerTask(task)) {
            reject(task);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean offerTask(Runnable task) {
        if (isShutdown()) {
            reject();
        }
        return this.taskQueue.offer(task);
    }

    protected boolean removeTask(Runnable task) {
        return this.taskQueue.remove(ObjectUtil.checkNotNull(task, "task"));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean runAllTasks() {
        boolean fetchedAll;
        if (!inEventLoop()) {
            throw new AssertionError();
        }
        boolean ranAtLeastOne = false;
        do {
            fetchedAll = fetchFromScheduledTaskQueue();
            if (runAllTasksFrom(this.taskQueue)) {
                ranAtLeastOne = true;
            }
        } while (!fetchedAll);
        if (ranAtLeastOne) {
            this.lastExecutionTime = getCurrentTimeNanos();
        }
        afterRunningAllTasks();
        return ranAtLeastOne;
    }

    /* JADX WARN: Incorrect condition in loop: B:5:0x0012 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected final boolean runScheduledAndExecutorTasks(int r5) {
        /*
            r4 = this;
            boolean r0 = r4.inEventLoop()
            if (r0 == 0) goto L29
            r0 = 0
        L7:
            java.util.Queue<java.lang.Runnable> r1 = r4.taskQueue
            boolean r1 = r4.runExistingTasksFrom(r1)
            boolean r2 = r4.executeExpiredScheduledTasks()
            r1 = r1 | r2
            if (r1 == 0) goto L18
            int r0 = r0 + 1
            if (r0 < r5) goto L7
        L18:
            if (r0 <= 0) goto L20
            long r2 = r4.getCurrentTimeNanos()
            r4.lastExecutionTime = r2
        L20:
            r4.afterRunningAllTasks()
            if (r0 <= 0) goto L27
            r2 = 1
            goto L28
        L27:
            r2 = 0
        L28:
            return r2
        L29:
            java.lang.AssertionError r0 = new java.lang.AssertionError
            r0.<init>()
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.util.concurrent.SingleThreadEventExecutor.runScheduledAndExecutorTasks(int):boolean");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final boolean runAllTasksFrom(Queue<Runnable> taskQueue) {
        Runnable task = pollTaskFrom(taskQueue);
        if (task == null) {
            return false;
        }
        do {
            safeExecute(task);
            task = pollTaskFrom(taskQueue);
        } while (task != null);
        return true;
    }

    private boolean runExistingTasksFrom(Queue<Runnable> taskQueue) {
        Runnable task;
        Runnable task2 = pollTaskFrom(taskQueue);
        if (task2 == null) {
            return false;
        }
        int remaining = Math.min(this.maxPendingTasks, taskQueue.size());
        safeExecute(task2);
        while (true) {
            int remaining2 = remaining - 1;
            if (remaining > 0 && (task = taskQueue.poll()) != null) {
                safeExecute(task);
                remaining = remaining2;
            } else {
                return true;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean runAllTasks(long timeoutNanos) {
        long lastExecutionTime;
        fetchFromScheduledTaskQueue();
        Runnable task = pollTask();
        if (task == null) {
            afterRunningAllTasks();
            return false;
        }
        long deadline = timeoutNanos > 0 ? getCurrentTimeNanos() + timeoutNanos : 0L;
        long runTasks = 0;
        while (true) {
            safeExecute(task);
            runTasks++;
            if ((63 & runTasks) == 0) {
                lastExecutionTime = getCurrentTimeNanos();
                if (lastExecutionTime >= deadline) {
                    break;
                }
            }
            task = pollTask();
            if (task == null) {
                lastExecutionTime = getCurrentTimeNanos();
                break;
            }
        }
        afterRunningAllTasks();
        this.lastExecutionTime = lastExecutionTime;
        return true;
    }

    protected void afterRunningAllTasks() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public long delayNanos(long currentTimeNanos) {
        long currentTimeNanos2 = currentTimeNanos - initialNanoTime();
        ScheduledFutureTask<?> scheduledTask = peekScheduledTask();
        if (scheduledTask == null) {
            return SCHEDULE_PURGE_INTERVAL;
        }
        return scheduledTask.delayNanos(currentTimeNanos2);
    }

    protected long deadlineNanos() {
        ScheduledFutureTask<?> scheduledTask = peekScheduledTask();
        if (scheduledTask == null) {
            return getCurrentTimeNanos() + SCHEDULE_PURGE_INTERVAL;
        }
        return scheduledTask.deadlineNanos();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void updateLastExecutionTime() {
        this.lastExecutionTime = getCurrentTimeNanos();
    }

    protected void cleanup() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void wakeup(boolean inEventLoop) {
        if (!inEventLoop) {
            this.taskQueue.offer(WAKEUP_TASK);
        }
    }

    @Override // io.netty.util.concurrent.EventExecutor
    public boolean inEventLoop(Thread thread) {
        return thread == this.thread;
    }

    public void addShutdownHook(final Runnable task) {
        if (inEventLoop()) {
            this.shutdownHooks.add(task);
        } else {
            execute(new Runnable() { // from class: io.netty.util.concurrent.SingleThreadEventExecutor.2
                @Override // java.lang.Runnable
                public void run() {
                    SingleThreadEventExecutor.this.shutdownHooks.add(task);
                }
            });
        }
    }

    public void removeShutdownHook(final Runnable task) {
        if (inEventLoop()) {
            this.shutdownHooks.remove(task);
        } else {
            execute(new Runnable() { // from class: io.netty.util.concurrent.SingleThreadEventExecutor.3
                @Override // java.lang.Runnable
                public void run() {
                    SingleThreadEventExecutor.this.shutdownHooks.remove(task);
                }
            });
        }
    }

    private boolean runShutdownHooks() {
        boolean ran = false;
        while (!this.shutdownHooks.isEmpty()) {
            List<Runnable> copy = new ArrayList<>(this.shutdownHooks);
            this.shutdownHooks.clear();
            for (Runnable task : copy) {
                try {
                    runTask(task);
                } finally {
                    try {
                    } finally {
                    }
                }
            }
        }
        if (ran) {
            this.lastExecutionTime = getCurrentTimeNanos();
        }
        return ran;
    }

    @Override // io.netty.util.concurrent.EventExecutorGroup
    public Future<?> shutdownGracefully(long quietPeriod, long timeout, TimeUnit unit) {
        int newState;
        ObjectUtil.checkPositiveOrZero(quietPeriod, "quietPeriod");
        if (timeout < quietPeriod) {
            throw new IllegalArgumentException("timeout: " + timeout + " (expected >= quietPeriod (" + quietPeriod + "))");
        }
        ObjectUtil.checkNotNull(unit, "unit");
        if (isShuttingDown()) {
            return terminationFuture();
        }
        boolean inEventLoop = inEventLoop();
        while (!isShuttingDown()) {
            boolean wakeup = true;
            int oldState = this.state;
            if (inEventLoop) {
                newState = 3;
            } else {
                switch (oldState) {
                    case 1:
                    case 2:
                        newState = 3;
                        break;
                    default:
                        newState = oldState;
                        wakeup = false;
                        break;
                }
            }
            if (STATE_UPDATER.compareAndSet(this, oldState, newState)) {
                this.gracefulShutdownQuietPeriod = unit.toNanos(quietPeriod);
                this.gracefulShutdownTimeout = unit.toNanos(timeout);
                if (ensureThreadStarted(oldState)) {
                    return this.terminationFuture;
                }
                if (wakeup) {
                    this.taskQueue.offer(WAKEUP_TASK);
                    if (!this.addTaskWakesUp) {
                        wakeup(inEventLoop);
                    }
                }
                return terminationFuture();
            }
        }
        return terminationFuture();
    }

    @Override // io.netty.util.concurrent.EventExecutorGroup
    public Future<?> terminationFuture() {
        return this.terminationFuture;
    }

    @Override // io.netty.util.concurrent.AbstractEventExecutor, java.util.concurrent.ExecutorService, io.netty.util.concurrent.EventExecutorGroup
    @Deprecated
    public void shutdown() {
        int newState;
        if (isShutdown()) {
            return;
        }
        boolean inEventLoop = inEventLoop();
        while (!isShuttingDown()) {
            boolean wakeup = true;
            int oldState = this.state;
            if (inEventLoop) {
                newState = 4;
            } else {
                switch (oldState) {
                    case 1:
                    case 2:
                    case 3:
                        newState = 4;
                        break;
                    default:
                        newState = oldState;
                        wakeup = false;
                        break;
                }
            }
            if (STATE_UPDATER.compareAndSet(this, oldState, newState)) {
                if (!ensureThreadStarted(oldState) && wakeup) {
                    this.taskQueue.offer(WAKEUP_TASK);
                    if (!this.addTaskWakesUp) {
                        wakeup(inEventLoop);
                        return;
                    }
                    return;
                }
                return;
            }
        }
    }

    @Override // io.netty.util.concurrent.EventExecutorGroup
    public boolean isShuttingDown() {
        return this.state >= 3;
    }

    @Override // java.util.concurrent.ExecutorService
    public boolean isShutdown() {
        return this.state >= 4;
    }

    @Override // java.util.concurrent.ExecutorService
    public boolean isTerminated() {
        return this.state == 5;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean confirmShutdown() {
        if (!isShuttingDown()) {
            return false;
        }
        if (!inEventLoop()) {
            throw new IllegalStateException("must be invoked from an event loop");
        }
        cancelScheduledTasks();
        if (this.gracefulShutdownStartTime == 0) {
            this.gracefulShutdownStartTime = getCurrentTimeNanos();
        }
        if (runAllTasks() || runShutdownHooks()) {
            if (isShutdown() || this.gracefulShutdownQuietPeriod == 0) {
                return true;
            }
            this.taskQueue.offer(WAKEUP_TASK);
            return false;
        }
        long nanoTime = getCurrentTimeNanos();
        if (isShutdown() || nanoTime - this.gracefulShutdownStartTime > this.gracefulShutdownTimeout || nanoTime - this.lastExecutionTime > this.gracefulShutdownQuietPeriod) {
            return true;
        }
        this.taskQueue.offer(WAKEUP_TASK);
        try {
            Thread.sleep(100L);
        } catch (InterruptedException e) {
        }
        return false;
    }

    @Override // java.util.concurrent.ExecutorService
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        ObjectUtil.checkNotNull(unit, "unit");
        if (inEventLoop()) {
            throw new IllegalStateException("cannot await termination of the current thread");
        }
        this.threadLock.await(timeout, unit);
        return isTerminated();
    }

    @Override // java.util.concurrent.Executor
    public void execute(Runnable task) {
        execute0(task);
    }

    @Override // io.netty.util.concurrent.AbstractEventExecutor
    public void lazyExecute(Runnable task) {
        lazyExecute0(task);
    }

    private void execute0(Runnable task) {
        ObjectUtil.checkNotNull(task, "task");
        execute(task, wakesUpForTask(task));
    }

    private void lazyExecute0(Runnable task) {
        execute((Runnable) ObjectUtil.checkNotNull(task, "task"), false);
    }

    private void execute(Runnable task, boolean immediate) {
        boolean inEventLoop = inEventLoop();
        addTask(task);
        if (!inEventLoop) {
            startThread();
            if (isShutdown()) {
                boolean reject = false;
                try {
                    if (removeTask(task)) {
                        reject = true;
                    }
                } catch (UnsupportedOperationException e) {
                }
                if (reject) {
                    reject();
                }
            }
        }
        if (!this.addTaskWakesUp && immediate) {
            wakeup(inEventLoop);
        }
    }

    @Override // java.util.concurrent.AbstractExecutorService, java.util.concurrent.ExecutorService
    public <T> T invokeAny(Collection<? extends Callable<T>> collection) throws InterruptedException, ExecutionException {
        throwIfInEventLoop("invokeAny");
        return (T) super.invokeAny(collection);
    }

    @Override // java.util.concurrent.AbstractExecutorService, java.util.concurrent.ExecutorService
    public <T> T invokeAny(Collection<? extends Callable<T>> collection, long j, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
        throwIfInEventLoop("invokeAny");
        return (T) super.invokeAny(collection, j, timeUnit);
    }

    @Override // java.util.concurrent.AbstractExecutorService, java.util.concurrent.ExecutorService
    public <T> List<java.util.concurrent.Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        throwIfInEventLoop("invokeAll");
        return super.invokeAll(tasks);
    }

    @Override // java.util.concurrent.AbstractExecutorService, java.util.concurrent.ExecutorService
    public <T> List<java.util.concurrent.Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        throwIfInEventLoop("invokeAll");
        return super.invokeAll(tasks, timeout, unit);
    }

    private void throwIfInEventLoop(String method) {
        if (inEventLoop()) {
            throw new RejectedExecutionException("Calling " + method + " from within the EventLoop is not allowed");
        }
    }

    public final ThreadProperties threadProperties() {
        ThreadProperties threadProperties = this.threadProperties;
        if (threadProperties == null) {
            Thread thread = this.thread;
            if (thread == null) {
                if (inEventLoop()) {
                    throw new AssertionError();
                }
                submit(NOOP_TASK).syncUninterruptibly();
                thread = this.thread;
                if (thread == null) {
                    throw new AssertionError();
                }
            }
            ThreadProperties threadProperties2 = new DefaultThreadProperties(thread);
            return !AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(PROPERTIES_UPDATER, this, null, threadProperties2) ? this.threadProperties : threadProperties2;
        }
        return threadProperties;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean wakesUpForTask(Runnable task) {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static void reject() {
        throw new RejectedExecutionException("event executor terminated");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void reject(Runnable task) {
        this.rejectedExecutionHandler.rejected(task, this);
    }

    private void startThread() {
        if (this.state == 1) {
            if (STATE_UPDATER.compareAndSet(this, 1, 2)) {
                boolean success = false;
                try {
                    doStartThread();
                    success = true;
                } finally {
                    if (!success) {
                        STATE_UPDATER.compareAndSet(this, 2, 1);
                    }
                }
            }
        }
    }

    private boolean ensureThreadStarted(int oldState) {
        if (oldState == 1) {
            try {
                doStartThread();
                return false;
            } catch (Throwable cause) {
                STATE_UPDATER.set(this, 5);
                this.terminationFuture.tryFailure(cause);
                if (!(cause instanceof Exception)) {
                    PlatformDependent.throwException(cause);
                }
                return true;
            }
        }
        return false;
    }

    private void doStartThread() {
        if (this.thread != null) {
            throw new AssertionError();
        }
        this.executor.execute(new Runnable() { // from class: io.netty.util.concurrent.SingleThreadEventExecutor.4
            @Override // java.lang.Runnable
            public void run() {
                int oldState;
                int oldState2;
                int oldState3;
                int oldState4;
                int oldState5;
                int oldState6;
                SingleThreadEventExecutor.this.thread = Thread.currentThread();
                if (SingleThreadEventExecutor.this.interrupted) {
                    SingleThreadEventExecutor.this.thread.interrupt();
                }
                SingleThreadEventExecutor.this.updateLastExecutionTime();
                try {
                    SingleThreadEventExecutor.this.run();
                    do {
                        oldState5 = SingleThreadEventExecutor.this.state;
                        if (oldState5 >= 3) {
                            break;
                        }
                    } while (!SingleThreadEventExecutor.STATE_UPDATER.compareAndSet(SingleThreadEventExecutor.this, oldState5, 3));
                    if (1 != 0 && SingleThreadEventExecutor.this.gracefulShutdownStartTime == 0 && SingleThreadEventExecutor.logger.isErrorEnabled()) {
                        SingleThreadEventExecutor.logger.error("Buggy " + EventExecutor.class.getSimpleName() + " implementation; " + SingleThreadEventExecutor.class.getSimpleName() + ".confirmShutdown() must be called before run() implementation terminates.");
                    }
                    do {
                        try {
                        } catch (Throwable th) {
                            try {
                                SingleThreadEventExecutor.this.cleanup();
                                FastThreadLocal.removeAll();
                                SingleThreadEventExecutor.STATE_UPDATER.set(SingleThreadEventExecutor.this, 5);
                                SingleThreadEventExecutor.this.threadLock.countDown();
                                int numUserTasks = SingleThreadEventExecutor.this.drainTasks();
                                if (numUserTasks > 0 && SingleThreadEventExecutor.logger.isWarnEnabled()) {
                                    SingleThreadEventExecutor.logger.warn("An event executor terminated with non-empty task queue (" + numUserTasks + ')');
                                }
                                SingleThreadEventExecutor.this.terminationFuture.setSuccess(null);
                                throw th;
                            } finally {
                                FastThreadLocal.removeAll();
                                SingleThreadEventExecutor.STATE_UPDATER.set(SingleThreadEventExecutor.this, 5);
                                SingleThreadEventExecutor.this.threadLock.countDown();
                                int numUserTasks2 = SingleThreadEventExecutor.this.drainTasks();
                                if (numUserTasks2 > 0 && SingleThreadEventExecutor.logger.isWarnEnabled()) {
                                    SingleThreadEventExecutor.logger.warn("An event executor terminated with non-empty task queue (" + numUserTasks2 + ')');
                                }
                                SingleThreadEventExecutor.this.terminationFuture.setSuccess(null);
                            }
                        }
                    } while (!SingleThreadEventExecutor.this.confirmShutdown());
                    do {
                        oldState6 = SingleThreadEventExecutor.this.state;
                        if (oldState6 >= 4) {
                            break;
                        }
                    } while (!SingleThreadEventExecutor.STATE_UPDATER.compareAndSet(SingleThreadEventExecutor.this, oldState6, 4));
                    SingleThreadEventExecutor.this.confirmShutdown();
                    try {
                        SingleThreadEventExecutor.this.cleanup();
                    } finally {
                        FastThreadLocal.removeAll();
                        SingleThreadEventExecutor.STATE_UPDATER.set(SingleThreadEventExecutor.this, 5);
                        SingleThreadEventExecutor.this.threadLock.countDown();
                        int numUserTasks3 = SingleThreadEventExecutor.this.drainTasks();
                        if (numUserTasks3 > 0 && SingleThreadEventExecutor.logger.isWarnEnabled()) {
                            SingleThreadEventExecutor.logger.warn("An event executor terminated with non-empty task queue (" + numUserTasks3 + ')');
                        }
                        SingleThreadEventExecutor.this.terminationFuture.setSuccess(null);
                    }
                } catch (Throwable t) {
                    try {
                        SingleThreadEventExecutor.logger.warn("Unexpected exception from an event executor: ", t);
                        do {
                            oldState3 = SingleThreadEventExecutor.this.state;
                            if (oldState3 >= 3) {
                                break;
                            }
                        } while (!SingleThreadEventExecutor.STATE_UPDATER.compareAndSet(SingleThreadEventExecutor.this, oldState3, 3));
                        if (0 != 0 && SingleThreadEventExecutor.this.gracefulShutdownStartTime == 0 && SingleThreadEventExecutor.logger.isErrorEnabled()) {
                            SingleThreadEventExecutor.logger.error("Buggy " + EventExecutor.class.getSimpleName() + " implementation; " + SingleThreadEventExecutor.class.getSimpleName() + ".confirmShutdown() must be called before run() implementation terminates.");
                        }
                        do {
                            try {
                            } catch (Throwable th2) {
                                try {
                                    SingleThreadEventExecutor.this.cleanup();
                                    FastThreadLocal.removeAll();
                                    SingleThreadEventExecutor.STATE_UPDATER.set(SingleThreadEventExecutor.this, 5);
                                    SingleThreadEventExecutor.this.threadLock.countDown();
                                    int numUserTasks4 = SingleThreadEventExecutor.this.drainTasks();
                                    if (numUserTasks4 > 0 && SingleThreadEventExecutor.logger.isWarnEnabled()) {
                                        SingleThreadEventExecutor.logger.warn("An event executor terminated with non-empty task queue (" + numUserTasks4 + ')');
                                    }
                                    SingleThreadEventExecutor.this.terminationFuture.setSuccess(null);
                                    throw th2;
                                } finally {
                                    FastThreadLocal.removeAll();
                                    SingleThreadEventExecutor.STATE_UPDATER.set(SingleThreadEventExecutor.this, 5);
                                    SingleThreadEventExecutor.this.threadLock.countDown();
                                    int numUserTasks5 = SingleThreadEventExecutor.this.drainTasks();
                                    if (numUserTasks5 > 0 && SingleThreadEventExecutor.logger.isWarnEnabled()) {
                                        SingleThreadEventExecutor.logger.warn("An event executor terminated with non-empty task queue (" + numUserTasks5 + ')');
                                    }
                                    SingleThreadEventExecutor.this.terminationFuture.setSuccess(null);
                                }
                            }
                        } while (!SingleThreadEventExecutor.this.confirmShutdown());
                        do {
                            oldState4 = SingleThreadEventExecutor.this.state;
                            if (oldState4 >= 4) {
                                break;
                            }
                        } while (!SingleThreadEventExecutor.STATE_UPDATER.compareAndSet(SingleThreadEventExecutor.this, oldState4, 4));
                        SingleThreadEventExecutor.this.confirmShutdown();
                        try {
                            SingleThreadEventExecutor.this.cleanup();
                            FastThreadLocal.removeAll();
                            SingleThreadEventExecutor.STATE_UPDATER.set(SingleThreadEventExecutor.this, 5);
                            SingleThreadEventExecutor.this.threadLock.countDown();
                            int numUserTasks6 = SingleThreadEventExecutor.this.drainTasks();
                            if (numUserTasks6 > 0 && SingleThreadEventExecutor.logger.isWarnEnabled()) {
                                SingleThreadEventExecutor.logger.warn("An event executor terminated with non-empty task queue (" + numUserTasks6 + ')');
                            }
                            SingleThreadEventExecutor.this.terminationFuture.setSuccess(null);
                        } finally {
                            FastThreadLocal.removeAll();
                            SingleThreadEventExecutor.STATE_UPDATER.set(SingleThreadEventExecutor.this, 5);
                            SingleThreadEventExecutor.this.threadLock.countDown();
                            int numUserTasks7 = SingleThreadEventExecutor.this.drainTasks();
                            if (numUserTasks7 > 0 && SingleThreadEventExecutor.logger.isWarnEnabled()) {
                                SingleThreadEventExecutor.logger.warn("An event executor terminated with non-empty task queue (" + numUserTasks7 + ')');
                            }
                            SingleThreadEventExecutor.this.terminationFuture.setSuccess(null);
                        }
                    } catch (Throwable th3) {
                        do {
                            oldState = SingleThreadEventExecutor.this.state;
                            if (oldState >= 3) {
                                break;
                            }
                        } while (!SingleThreadEventExecutor.STATE_UPDATER.compareAndSet(SingleThreadEventExecutor.this, oldState, 3));
                        if (0 != 0 && SingleThreadEventExecutor.this.gracefulShutdownStartTime == 0 && SingleThreadEventExecutor.logger.isErrorEnabled()) {
                            SingleThreadEventExecutor.logger.error("Buggy " + EventExecutor.class.getSimpleName() + " implementation; " + SingleThreadEventExecutor.class.getSimpleName() + ".confirmShutdown() must be called before run() implementation terminates.");
                        }
                        do {
                            try {
                            } catch (Throwable th4) {
                                try {
                                    SingleThreadEventExecutor.this.cleanup();
                                    FastThreadLocal.removeAll();
                                    SingleThreadEventExecutor.STATE_UPDATER.set(SingleThreadEventExecutor.this, 5);
                                    SingleThreadEventExecutor.this.threadLock.countDown();
                                    int numUserTasks8 = SingleThreadEventExecutor.this.drainTasks();
                                    if (numUserTasks8 > 0 && SingleThreadEventExecutor.logger.isWarnEnabled()) {
                                        SingleThreadEventExecutor.logger.warn("An event executor terminated with non-empty task queue (" + numUserTasks8 + ')');
                                    }
                                    SingleThreadEventExecutor.this.terminationFuture.setSuccess(null);
                                    throw th4;
                                } finally {
                                    FastThreadLocal.removeAll();
                                    SingleThreadEventExecutor.STATE_UPDATER.set(SingleThreadEventExecutor.this, 5);
                                    SingleThreadEventExecutor.this.threadLock.countDown();
                                    int numUserTasks9 = SingleThreadEventExecutor.this.drainTasks();
                                    if (numUserTasks9 > 0 && SingleThreadEventExecutor.logger.isWarnEnabled()) {
                                        SingleThreadEventExecutor.logger.warn("An event executor terminated with non-empty task queue (" + numUserTasks9 + ')');
                                    }
                                    SingleThreadEventExecutor.this.terminationFuture.setSuccess(false);
                                }
                            }
                        } while (!SingleThreadEventExecutor.this.confirmShutdown());
                        do {
                            oldState2 = SingleThreadEventExecutor.this.state;
                            if (oldState2 >= 4) {
                                break;
                            }
                        } while (!SingleThreadEventExecutor.STATE_UPDATER.compareAndSet(SingleThreadEventExecutor.this, oldState2, 4));
                        SingleThreadEventExecutor.this.confirmShutdown();
                        try {
                            SingleThreadEventExecutor.this.cleanup();
                            throw th3;
                        } finally {
                            FastThreadLocal.removeAll();
                            SingleThreadEventExecutor.STATE_UPDATER.set(SingleThreadEventExecutor.this, 5);
                            SingleThreadEventExecutor.this.threadLock.countDown();
                            int numUserTasks10 = SingleThreadEventExecutor.this.drainTasks();
                            if (numUserTasks10 > 0 && SingleThreadEventExecutor.logger.isWarnEnabled()) {
                                SingleThreadEventExecutor.logger.warn("An event executor terminated with non-empty task queue (" + numUserTasks10 + ')');
                            }
                            SingleThreadEventExecutor.this.terminationFuture.setSuccess(false);
                        }
                    }
                }
            }
        });
    }

    final int drainTasks() {
        int numTasks = 0;
        while (true) {
            Runnable runnable = this.taskQueue.poll();
            if (runnable != null) {
                if (WAKEUP_TASK != runnable) {
                    numTasks++;
                }
            } else {
                return numTasks;
            }
        }
    }

    /* loaded from: classes4.dex */
    private static final class DefaultThreadProperties implements ThreadProperties {
        private final Thread t;

        DefaultThreadProperties(Thread t) {
            this.t = t;
        }

        @Override // io.netty.util.concurrent.ThreadProperties
        public Thread.State state() {
            return this.t.getState();
        }

        @Override // io.netty.util.concurrent.ThreadProperties
        public int priority() {
            return this.t.getPriority();
        }

        @Override // io.netty.util.concurrent.ThreadProperties
        public boolean isInterrupted() {
            return this.t.isInterrupted();
        }

        @Override // io.netty.util.concurrent.ThreadProperties
        public boolean isDaemon() {
            return this.t.isDaemon();
        }

        @Override // io.netty.util.concurrent.ThreadProperties
        public String name() {
            return this.t.getName();
        }

        @Override // io.netty.util.concurrent.ThreadProperties
        public long id() {
            return this.t.getId();
        }

        @Override // io.netty.util.concurrent.ThreadProperties
        public StackTraceElement[] stackTrace() {
            return this.t.getStackTrace();
        }

        @Override // io.netty.util.concurrent.ThreadProperties
        public boolean isAlive() {
            return this.t.isAlive();
        }
    }
}
