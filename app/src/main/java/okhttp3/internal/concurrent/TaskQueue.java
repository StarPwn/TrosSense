package okhttp3.internal.concurrent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.RejectedExecutionException;
import java.util.logging.Level;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import okhttp3.internal.Util;

/* compiled from: TaskQueue.kt */
@Metadata(d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010!\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u00002\u00020\u0001:\u00013B\u0017\b\u0000\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u0006\u0010!\u001a\u00020\"J\r\u0010#\u001a\u00020\u000eH\u0000¢\u0006\u0002\b$J8\u0010%\u001a\u00020\"2\u0006\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010&\u001a\u00020'2\b\b\u0002\u0010(\u001a\u00020\u000e2\u000e\b\u0004\u0010)\u001a\b\u0012\u0004\u0012\u00020\"0*H\u0086\bø\u0001\u0000J\u0006\u0010+\u001a\u00020,J.\u0010-\u001a\u00020\"2\u0006\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010&\u001a\u00020'2\u000e\b\u0004\u0010)\u001a\b\u0012\u0004\u0012\u00020'0*H\u0086\bø\u0001\u0000J\u0018\u0010-\u001a\u00020\"2\u0006\u0010.\u001a\u00020\b2\b\b\u0002\u0010&\u001a\u00020'J%\u0010/\u001a\u00020\u000e2\u0006\u0010.\u001a\u00020\b2\u0006\u0010&\u001a\u00020'2\u0006\u00100\u001a\u00020\u000eH\u0000¢\u0006\u0002\b1J\u0006\u0010\u001c\u001a\u00020\"J\b\u00102\u001a\u00020\u0005H\u0016R\u001c\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0080\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\fR\u001a\u0010\r\u001a\u00020\u000eX\u0080\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012R\u001a\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\b0\u0014X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0014\u0010\u0004\u001a\u00020\u0005X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u0017\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\b0\u001a8F¢\u0006\u0006\u001a\u0004\b\u001b\u0010\u0016R\u001a\u0010\u001c\u001a\u00020\u000eX\u0080\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001d\u0010\u0010\"\u0004\b\u001e\u0010\u0012R\u0014\u0010\u0002\u001a\u00020\u0003X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010 \u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u00064"}, d2 = {"Lokhttp3/internal/concurrent/TaskQueue;", "", "taskRunner", "Lokhttp3/internal/concurrent/TaskRunner;", "name", "", "(Lokhttp3/internal/concurrent/TaskRunner;Ljava/lang/String;)V", "activeTask", "Lokhttp3/internal/concurrent/Task;", "getActiveTask$okhttp", "()Lokhttp3/internal/concurrent/Task;", "setActiveTask$okhttp", "(Lokhttp3/internal/concurrent/Task;)V", "cancelActiveTask", "", "getCancelActiveTask$okhttp", "()Z", "setCancelActiveTask$okhttp", "(Z)V", "futureTasks", "", "getFutureTasks$okhttp", "()Ljava/util/List;", "getName$okhttp", "()Ljava/lang/String;", "scheduledTasks", "", "getScheduledTasks", "shutdown", "getShutdown$okhttp", "setShutdown$okhttp", "getTaskRunner$okhttp", "()Lokhttp3/internal/concurrent/TaskRunner;", "cancelAll", "", "cancelAllAndDecide", "cancelAllAndDecide$okhttp", "execute", "delayNanos", "", "cancelable", "block", "Lkotlin/Function0;", "idleLatch", "Ljava/util/concurrent/CountDownLatch;", "schedule", "task", "scheduleAndDecide", "recurrence", "scheduleAndDecide$okhttp", "toString", "AwaitIdleTask", "okhttp"}, k = 1, mv = {1, 6, 0}, xi = 48)
/* loaded from: classes5.dex */
public final class TaskQueue {
    private Task activeTask;
    private boolean cancelActiveTask;
    private final List<Task> futureTasks;
    private final String name;
    private boolean shutdown;
    private final TaskRunner taskRunner;

    public TaskQueue(TaskRunner taskRunner, String name) {
        Intrinsics.checkNotNullParameter(taskRunner, "taskRunner");
        Intrinsics.checkNotNullParameter(name, "name");
        this.taskRunner = taskRunner;
        this.name = name;
        this.futureTasks = new ArrayList();
    }

    /* renamed from: getTaskRunner$okhttp, reason: from getter */
    public final TaskRunner getTaskRunner() {
        return this.taskRunner;
    }

    /* renamed from: getName$okhttp, reason: from getter */
    public final String getName() {
        return this.name;
    }

    /* renamed from: getShutdown$okhttp, reason: from getter */
    public final boolean getShutdown() {
        return this.shutdown;
    }

    public final void setShutdown$okhttp(boolean z) {
        this.shutdown = z;
    }

    /* renamed from: getActiveTask$okhttp, reason: from getter */
    public final Task getActiveTask() {
        return this.activeTask;
    }

    public final void setActiveTask$okhttp(Task task) {
        this.activeTask = task;
    }

    public final List<Task> getFutureTasks$okhttp() {
        return this.futureTasks;
    }

    /* renamed from: getCancelActiveTask$okhttp, reason: from getter */
    public final boolean getCancelActiveTask() {
        return this.cancelActiveTask;
    }

    public final void setCancelActiveTask$okhttp(boolean z) {
        this.cancelActiveTask = z;
    }

    public final List<Task> getScheduledTasks() {
        List<Task> list;
        synchronized (this.taskRunner) {
            list = CollectionsKt.toList(getFutureTasks$okhttp());
        }
        return list;
    }

    public static /* synthetic */ void schedule$default(TaskQueue taskQueue, Task task, long j, int i, Object obj) {
        if ((i & 2) != 0) {
            j = 0;
        }
        taskQueue.schedule(task, j);
    }

    public final void schedule(Task task, long delayNanos) {
        Intrinsics.checkNotNullParameter(task, "task");
        synchronized (this.taskRunner) {
            if (getShutdown()) {
                if (task.getCancelable()) {
                    if (TaskRunner.INSTANCE.getLogger().isLoggable(Level.FINE)) {
                        TaskLoggerKt.access$log(task, this, "schedule canceled (queue is shutdown)");
                    }
                    return;
                } else {
                    if (TaskRunner.INSTANCE.getLogger().isLoggable(Level.FINE)) {
                        TaskLoggerKt.access$log(task, this, "schedule failed (queue is shutdown)");
                    }
                    throw new RejectedExecutionException();
                }
            }
            if (scheduleAndDecide$okhttp(task, delayNanos, false)) {
                getTaskRunner().kickCoordinator$okhttp(this);
            }
            Unit unit = Unit.INSTANCE;
        }
    }

    public static /* synthetic */ void schedule$default(TaskQueue taskQueue, String name, long delayNanos, Function0 block, int i, Object obj) {
        if ((i & 2) != 0) {
            delayNanos = 0;
        }
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(block, "block");
        taskQueue.schedule(new TaskQueue$schedule$2(name, block), delayNanos);
    }

    public final void schedule(String name, long delayNanos, Function0<Long> block) {
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(block, "block");
        schedule(new TaskQueue$schedule$2(name, block), delayNanos);
    }

    public static /* synthetic */ void execute$default(TaskQueue taskQueue, String name, long delayNanos, boolean cancelable, Function0 block, int i, Object obj) {
        if ((i & 2) != 0) {
            delayNanos = 0;
        }
        if ((i & 4) != 0) {
            cancelable = true;
        }
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(block, "block");
        taskQueue.schedule(new TaskQueue$execute$1(name, cancelable, block), delayNanos);
    }

    public final void execute(String name, long delayNanos, boolean cancelable, Function0<Unit> block) {
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(block, "block");
        schedule(new TaskQueue$execute$1(name, cancelable, block), delayNanos);
    }

    public final CountDownLatch idleLatch() {
        synchronized (this.taskRunner) {
            if (getActiveTask() == null && getFutureTasks$okhttp().isEmpty()) {
                return new CountDownLatch(0);
            }
            Task existingTask = getActiveTask();
            if (existingTask instanceof AwaitIdleTask) {
                return ((AwaitIdleTask) existingTask).getLatch();
            }
            for (Task futureTask : getFutureTasks$okhttp()) {
                if (futureTask instanceof AwaitIdleTask) {
                    return ((AwaitIdleTask) futureTask).getLatch();
                }
            }
            AwaitIdleTask newTask = new AwaitIdleTask();
            if (scheduleAndDecide$okhttp(newTask, 0L, false)) {
                getTaskRunner().kickCoordinator$okhttp(this);
            }
            return newTask.getLatch();
        }
    }

    /* compiled from: TaskQueue.kt */
    @Metadata(d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\u0007\u001a\u00020\bH\u0016R\u0011\u0010\u0003\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\t"}, d2 = {"Lokhttp3/internal/concurrent/TaskQueue$AwaitIdleTask;", "Lokhttp3/internal/concurrent/Task;", "()V", "latch", "Ljava/util/concurrent/CountDownLatch;", "getLatch", "()Ljava/util/concurrent/CountDownLatch;", "runOnce", "", "okhttp"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* loaded from: classes5.dex */
    private static final class AwaitIdleTask extends Task {
        private final CountDownLatch latch;

        public AwaitIdleTask() {
            super(Intrinsics.stringPlus(Util.okHttpName, " awaitIdle"), false);
            this.latch = new CountDownLatch(1);
        }

        public final CountDownLatch getLatch() {
            return this.latch;
        }

        @Override // okhttp3.internal.concurrent.Task
        public long runOnce() {
            this.latch.countDown();
            return -1L;
        }
    }

    public final boolean scheduleAndDecide$okhttp(Task task, long delayNanos, boolean recurrence) {
        Intrinsics.checkNotNullParameter(task, "task");
        task.initQueue$okhttp(this);
        long now = this.taskRunner.getBackend().nanoTime();
        long executeNanoTime = now + delayNanos;
        int existingIndex = this.futureTasks.indexOf(task);
        if (existingIndex != -1) {
            if (task.getNextExecuteNanoTime() > executeNanoTime) {
                this.futureTasks.remove(existingIndex);
            } else {
                if (TaskRunner.INSTANCE.getLogger().isLoggable(Level.FINE)) {
                    TaskLoggerKt.access$log(task, this, "already scheduled");
                }
                return false;
            }
        }
        task.setNextExecuteNanoTime$okhttp(executeNanoTime);
        if (TaskRunner.INSTANCE.getLogger().isLoggable(Level.FINE)) {
            TaskLoggerKt.access$log(task, this, recurrence ? Intrinsics.stringPlus("run again after ", TaskLoggerKt.formatDuration(executeNanoTime - now)) : Intrinsics.stringPlus("scheduled after ", TaskLoggerKt.formatDuration(executeNanoTime - now)));
        }
        List $this$indexOfFirst$iv = this.futureTasks;
        int index$iv = 0;
        Iterator<Task> it2 = $this$indexOfFirst$iv.iterator();
        while (true) {
            if (it2.hasNext()) {
                Object item$iv = it2.next();
                Task it3 = (Task) item$iv;
                Task it4 = it3.getNextExecuteNanoTime() - now > delayNanos ? 1 : null;
                if (it4 != null) {
                    break;
                }
                index$iv++;
            } else {
                index$iv = -1;
                break;
            }
        }
        int insertAt = index$iv;
        if (insertAt == -1) {
            insertAt = this.futureTasks.size();
        }
        this.futureTasks.add(insertAt, task);
        return insertAt == 0;
    }

    public final void cancelAll() {
        if (!Util.assertionsEnabled || !Thread.holdsLock(this)) {
            Object $this$assertThreadDoesntHoldLock$iv = this.taskRunner;
            synchronized ($this$assertThreadDoesntHoldLock$iv) {
                if (cancelAllAndDecide$okhttp()) {
                    getTaskRunner().kickCoordinator$okhttp(this);
                }
                Unit unit = Unit.INSTANCE;
            }
            return;
        }
        throw new AssertionError("Thread " + ((Object) Thread.currentThread().getName()) + " MUST NOT hold lock on " + this);
    }

    public final void shutdown() {
        if (!Util.assertionsEnabled || !Thread.holdsLock(this)) {
            Object $this$assertThreadDoesntHoldLock$iv = this.taskRunner;
            synchronized ($this$assertThreadDoesntHoldLock$iv) {
                setShutdown$okhttp(true);
                if (cancelAllAndDecide$okhttp()) {
                    getTaskRunner().kickCoordinator$okhttp(this);
                }
                Unit unit = Unit.INSTANCE;
            }
            return;
        }
        throw new AssertionError("Thread " + ((Object) Thread.currentThread().getName()) + " MUST NOT hold lock on " + this);
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x002d, code lost:            r3 = r7.futureTasks.get(r1);     */
    /* JADX WARN: Code restructure failed: missing block: B:11:0x0042, code lost:            if (okhttp3.internal.concurrent.TaskRunner.Companion.getLogger().isLoggable(java.util.logging.Level.FINE) == false) goto L14;     */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x0044, code lost:            okhttp3.internal.concurrent.TaskLoggerKt.access$log(r3, r7, "canceled");     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x004a, code lost:            r0 = true;        r7.futureTasks.remove(r1);     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x0051, code lost:            if (r2 >= 0) goto L20;     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0053, code lost:            return r0;     */
    /* JADX WARN: Code restructure failed: missing block: B:7:0x001a, code lost:            if (r2 >= 0) goto L9;     */
    /* JADX WARN: Code restructure failed: missing block: B:8:0x001c, code lost:            r1 = r2;        r2 = r2 - 1;     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x002b, code lost:            if (r7.futureTasks.get(r1).getCancelable() == false) goto L19;     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final boolean cancelAllAndDecide$okhttp() {
        /*
            r7 = this;
            okhttp3.internal.concurrent.Task r0 = r7.activeTask
            r1 = 1
            if (r0 == 0) goto L12
            okhttp3.internal.concurrent.Task r0 = r7.activeTask
            kotlin.jvm.internal.Intrinsics.checkNotNull(r0)
            boolean r0 = r0.getCancelable()
            if (r0 == 0) goto L12
            r7.cancelActiveTask = r1
        L12:
            r0 = 0
            java.util.List<okhttp3.internal.concurrent.Task> r2 = r7.futureTasks
            int r2 = r2.size()
            int r2 = r2 - r1
            if (r2 < 0) goto L53
        L1c:
            r1 = r2
            int r2 = r2 + (-1)
            java.util.List<okhttp3.internal.concurrent.Task> r3 = r7.futureTasks
            java.lang.Object r3 = r3.get(r1)
            okhttp3.internal.concurrent.Task r3 = (okhttp3.internal.concurrent.Task) r3
            boolean r3 = r3.getCancelable()
            if (r3 == 0) goto L51
            java.util.List<okhttp3.internal.concurrent.Task> r3 = r7.futureTasks
            java.lang.Object r3 = r3.get(r1)
            okhttp3.internal.concurrent.Task r3 = (okhttp3.internal.concurrent.Task) r3
            r4 = 0
            okhttp3.internal.concurrent.TaskRunner$Companion r5 = okhttp3.internal.concurrent.TaskRunner.INSTANCE
            java.util.logging.Logger r5 = r5.getLogger()
            java.util.logging.Level r6 = java.util.logging.Level.FINE
            boolean r5 = r5.isLoggable(r6)
            if (r5 == 0) goto L4a
            r5 = 0
            java.lang.String r5 = "canceled"
            okhttp3.internal.concurrent.TaskLoggerKt.access$log(r3, r7, r5)
        L4a:
            r0 = 1
            java.util.List<okhttp3.internal.concurrent.Task> r3 = r7.futureTasks
            r3.remove(r1)
        L51:
            if (r2 >= 0) goto L1c
        L53:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.concurrent.TaskQueue.cancelAllAndDecide$okhttp():boolean");
    }

    public String toString() {
        return this.name;
    }
}
