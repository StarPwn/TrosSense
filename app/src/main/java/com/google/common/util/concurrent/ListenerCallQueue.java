package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.common.collect.Queues;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: classes.dex */
final class ListenerCallQueue<L> implements Runnable {
    private static final Logger logger = Logger.getLogger(ListenerCallQueue.class.getName());
    private final Executor executor;
    private boolean isThreadScheduled;
    private final L listener;
    private final Queue<Callback<L>> waitQueue = Queues.newArrayDeque();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static abstract class Callback<L> {
        private final String methodCall;

        abstract void call(L l);

        /* JADX INFO: Access modifiers changed from: package-private */
        public Callback(String methodCall) {
            this.methodCall = methodCall;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void enqueueOn(Iterable<ListenerCallQueue<L>> queues) {
            for (ListenerCallQueue<L> queue : queues) {
                queue.add(this);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ListenerCallQueue(L l, Executor executor) {
        this.listener = (L) Preconditions.checkNotNull(l);
        this.executor = (Executor) Preconditions.checkNotNull(executor);
    }

    synchronized void add(Callback<L> callback) {
        this.waitQueue.add(callback);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void execute() {
        boolean scheduleTaskRunner = false;
        synchronized (this) {
            if (!this.isThreadScheduled) {
                this.isThreadScheduled = true;
                scheduleTaskRunner = true;
            }
        }
        if (scheduleTaskRunner) {
            try {
                this.executor.execute(this);
            } catch (RuntimeException e) {
                synchronized (this) {
                    this.isThreadScheduled = false;
                    logger.log(Level.SEVERE, "Exception while running callbacks for " + this.listener + " on " + this.executor, (Throwable) e);
                    throw e;
                }
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x0022, code lost:            r2.call(r8.listener);     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x0028, code lost:            r3 = move-exception;     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x0029, code lost:            com.google.common.util.concurrent.ListenerCallQueue.logger.log(java.util.logging.Level.SEVERE, "Exception while executing callback: " + r8.listener + "." + ((com.google.common.util.concurrent.ListenerCallQueue.Callback) r2).methodCall, (java.lang.Throwable) r3);     */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0018  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0020 A[ORIG_RETURN, RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:40:0x005a  */
    @Override // java.lang.Runnable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void run() {
        /*
            r8 = this;
            r0 = 1
        L1:
            r1 = 0
            monitor-enter(r8)     // Catch: java.lang.Throwable -> L57
            boolean r2 = r8.isThreadScheduled     // Catch: java.lang.Throwable -> L54
            com.google.common.base.Preconditions.checkState(r2)     // Catch: java.lang.Throwable -> L54
            java.util.Queue<com.google.common.util.concurrent.ListenerCallQueue$Callback<L>> r2 = r8.waitQueue     // Catch: java.lang.Throwable -> L54
            java.lang.Object r2 = r2.poll()     // Catch: java.lang.Throwable -> L54
            com.google.common.util.concurrent.ListenerCallQueue$Callback r2 = (com.google.common.util.concurrent.ListenerCallQueue.Callback) r2     // Catch: java.lang.Throwable -> L54
            if (r2 != 0) goto L21
            r8.isThreadScheduled = r1     // Catch: java.lang.Throwable -> L54
            r0 = 0
            monitor-exit(r8)     // Catch: java.lang.Throwable -> L54
            if (r0 == 0) goto L20
            monitor-enter(r8)
            r8.isThreadScheduled = r1     // Catch: java.lang.Throwable -> L1d
            monitor-exit(r8)     // Catch: java.lang.Throwable -> L1d
            goto L20
        L1d:
            r1 = move-exception
            monitor-exit(r8)     // Catch: java.lang.Throwable -> L1d
            throw r1
        L20:
            return
        L21:
            monitor-exit(r8)     // Catch: java.lang.Throwable -> L54
            L r3 = r8.listener     // Catch: java.lang.RuntimeException -> L28 java.lang.Throwable -> L57
            r2.call(r3)     // Catch: java.lang.RuntimeException -> L28 java.lang.Throwable -> L57
            goto L53
        L28:
            r3 = move-exception
            java.util.logging.Logger r4 = com.google.common.util.concurrent.ListenerCallQueue.logger     // Catch: java.lang.Throwable -> L57
            java.util.logging.Level r5 = java.util.logging.Level.SEVERE     // Catch: java.lang.Throwable -> L57
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L57
            r6.<init>()     // Catch: java.lang.Throwable -> L57
            java.lang.String r7 = "Exception while executing callback: "
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch: java.lang.Throwable -> L57
            L r7 = r8.listener     // Catch: java.lang.Throwable -> L57
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch: java.lang.Throwable -> L57
            java.lang.String r7 = "."
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch: java.lang.Throwable -> L57
            java.lang.String r7 = com.google.common.util.concurrent.ListenerCallQueue.Callback.access$000(r2)     // Catch: java.lang.Throwable -> L57
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch: java.lang.Throwable -> L57
            java.lang.String r6 = r6.toString()     // Catch: java.lang.Throwable -> L57
            r4.log(r5, r6, r3)     // Catch: java.lang.Throwable -> L57
        L53:
            goto L1
        L54:
            r2 = move-exception
            monitor-exit(r8)     // Catch: java.lang.Throwable -> L54
            throw r2     // Catch: java.lang.Throwable -> L57
        L57:
            r2 = move-exception
            if (r0 == 0) goto L62
            monitor-enter(r8)
            r8.isThreadScheduled = r1     // Catch: java.lang.Throwable -> L5f
            monitor-exit(r8)     // Catch: java.lang.Throwable -> L5f
            goto L62
        L5f:
            r1 = move-exception
            monitor-exit(r8)     // Catch: java.lang.Throwable -> L5f
            throw r1
        L62:
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.util.concurrent.ListenerCallQueue.run():void");
    }
}
