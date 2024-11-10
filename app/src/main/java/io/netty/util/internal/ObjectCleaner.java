package io.netty.util.internal;

import io.netty.util.concurrent.FastThreadLocalThread;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/* loaded from: classes4.dex */
public final class ObjectCleaner {
    private static final int REFERENCE_QUEUE_POLL_TIMEOUT_MS = Math.max(500, SystemPropertyUtil.getInt("io.netty.util.internal.ObjectCleaner.refQueuePollTimeout", 10000));
    static final String CLEANER_THREAD_NAME = ObjectCleaner.class.getSimpleName() + "Thread";
    private static final Set<AutomaticCleanerReference> LIVE_SET = new ConcurrentSet();
    private static final ReferenceQueue<Object> REFERENCE_QUEUE = new ReferenceQueue<>();
    private static final AtomicBoolean CLEANER_RUNNING = new AtomicBoolean(false);
    private static final Runnable CLEANER_TASK = new Runnable() { // from class: io.netty.util.internal.ObjectCleaner.1
        @Override // java.lang.Runnable
        public void run() {
            boolean interrupted = false;
            while (true) {
                if (!ObjectCleaner.LIVE_SET.isEmpty()) {
                    try {
                        AutomaticCleanerReference reference = (AutomaticCleanerReference) ObjectCleaner.REFERENCE_QUEUE.remove(ObjectCleaner.REFERENCE_QUEUE_POLL_TIMEOUT_MS);
                        if (reference != null) {
                            try {
                                reference.cleanup();
                            } catch (Throwable th) {
                            }
                            ObjectCleaner.LIVE_SET.remove(reference);
                        }
                    } catch (InterruptedException e) {
                        interrupted = true;
                    }
                } else {
                    ObjectCleaner.CLEANER_RUNNING.set(false);
                    if (ObjectCleaner.LIVE_SET.isEmpty() || !ObjectCleaner.CLEANER_RUNNING.compareAndSet(false, true)) {
                        break;
                    }
                }
            }
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    };

    public static void register(Object object, Runnable cleanupTask) {
        AutomaticCleanerReference reference = new AutomaticCleanerReference(object, (Runnable) ObjectUtil.checkNotNull(cleanupTask, "cleanupTask"));
        LIVE_SET.add(reference);
        if (CLEANER_RUNNING.compareAndSet(false, true)) {
            final Thread cleanupThread = new FastThreadLocalThread(CLEANER_TASK);
            cleanupThread.setPriority(1);
            AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: io.netty.util.internal.ObjectCleaner.2
                @Override // java.security.PrivilegedAction
                public Void run() {
                    cleanupThread.setContextClassLoader(null);
                    return null;
                }
            });
            cleanupThread.setName(CLEANER_THREAD_NAME);
            cleanupThread.setDaemon(true);
            cleanupThread.start();
        }
    }

    public static int getLiveSetCount() {
        return LIVE_SET.size();
    }

    private ObjectCleaner() {
    }

    /* loaded from: classes4.dex */
    private static final class AutomaticCleanerReference extends WeakReference<Object> {
        private final Runnable cleanupTask;

        AutomaticCleanerReference(Object referent, Runnable cleanupTask) {
            super(referent, ObjectCleaner.REFERENCE_QUEUE);
            this.cleanupTask = cleanupTask;
        }

        void cleanup() {
            this.cleanupTask.run();
        }

        @Override // java.lang.ref.Reference
        public Thread get() {
            return null;
        }

        @Override // java.lang.ref.Reference
        public void clear() {
            ObjectCleaner.LIVE_SET.remove(this);
            super.clear();
        }
    }
}
