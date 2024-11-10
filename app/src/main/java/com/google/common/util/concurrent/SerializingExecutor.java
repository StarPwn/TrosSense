package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class SerializingExecutor implements Executor {
    private static final Logger log = Logger.getLogger(SerializingExecutor.class.getName());
    private final Executor executor;
    private final Deque<Runnable> queue = new ArrayDeque();
    private boolean isWorkerRunning = false;
    private int suspensions = 0;
    private final Object internalLock = new Object();

    public SerializingExecutor(Executor executor) {
        this.executor = (Executor) Preconditions.checkNotNull(executor);
    }

    @Override // java.util.concurrent.Executor
    public void execute(Runnable task) {
        synchronized (this.internalLock) {
            this.queue.add(task);
        }
        startQueueWorker();
    }

    public void executeFirst(Runnable task) {
        synchronized (this.internalLock) {
            this.queue.addFirst(task);
        }
        startQueueWorker();
    }

    public void suspend() {
        synchronized (this.internalLock) {
            this.suspensions++;
        }
    }

    public void resume() {
        synchronized (this.internalLock) {
            Preconditions.checkState(this.suspensions > 0);
            this.suspensions--;
        }
        startQueueWorker();
    }

    private void startQueueWorker() {
        synchronized (this.internalLock) {
            if (this.queue.peek() == null) {
                return;
            }
            if (this.suspensions > 0) {
                return;
            }
            if (this.isWorkerRunning) {
                return;
            }
            this.isWorkerRunning = true;
            try {
                this.executor.execute(new QueueWorker());
                if (0 != 0) {
                    synchronized (this.internalLock) {
                        this.isWorkerRunning = false;
                    }
                }
            } catch (Throwable th) {
                if (1 != 0) {
                    synchronized (this.internalLock) {
                        this.isWorkerRunning = false;
                    }
                }
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public final class QueueWorker implements Runnable {
        private QueueWorker() {
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                workOnQueue();
            } catch (Error e) {
                synchronized (SerializingExecutor.this.internalLock) {
                    SerializingExecutor.this.isWorkerRunning = false;
                    throw e;
                }
            }
        }

        private void workOnQueue() {
            Runnable task;
            while (true) {
                synchronized (SerializingExecutor.this.internalLock) {
                    task = SerializingExecutor.this.suspensions == 0 ? (Runnable) SerializingExecutor.this.queue.poll() : null;
                    if (task == null) {
                        SerializingExecutor.this.isWorkerRunning = false;
                        return;
                    }
                }
                try {
                    task.run();
                } catch (RuntimeException e) {
                    SerializingExecutor.log.log(Level.SEVERE, "Exception while executing runnable " + task, (Throwable) e);
                }
            }
        }
    }
}
