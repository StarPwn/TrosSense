package io.netty.util;

import io.netty.util.concurrent.FastThreadLocal;
import io.netty.util.concurrent.FastThreadLocalThread;
import io.netty.util.internal.ObjectPool;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue;
import java.lang.Thread;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/* loaded from: classes4.dex */
public abstract class Recycler<T> {
    private static final boolean BATCH_FAST_TL_ONLY;
    private static final boolean BLOCKING_POOL;
    private static final int DEFAULT_INITIAL_MAX_CAPACITY_PER_THREAD = 4096;
    private static final int DEFAULT_MAX_CAPACITY_PER_THREAD;
    private static final int DEFAULT_QUEUE_CHUNK_SIZE_PER_THREAD;
    private static final int RATIO;
    private final int chunkSize;
    private final int interval;
    private final int maxCapacityPerThread;
    private final FastThreadLocal<LocalPool<T>> threadLocal;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) Recycler.class);
    private static final EnhancedHandle<?> NOOP_HANDLE = new EnhancedHandle<Object>() { // from class: io.netty.util.Recycler.1
        @Override // io.netty.util.internal.ObjectPool.Handle
        public void recycle(Object object) {
        }

        @Override // io.netty.util.Recycler.EnhancedHandle
        public void unguardedRecycle(Object object) {
        }

        public String toString() {
            return "NOOP_HANDLE";
        }
    };

    /* loaded from: classes4.dex */
    public interface Handle<T> extends ObjectPool.Handle<T> {
    }

    protected abstract T newObject(Handle<T> handle);

    static {
        int maxCapacityPerThread = SystemPropertyUtil.getInt("io.netty.recycler.maxCapacityPerThread", SystemPropertyUtil.getInt("io.netty.recycler.maxCapacity", 4096));
        if (maxCapacityPerThread < 0) {
            maxCapacityPerThread = 4096;
        }
        DEFAULT_MAX_CAPACITY_PER_THREAD = maxCapacityPerThread;
        DEFAULT_QUEUE_CHUNK_SIZE_PER_THREAD = SystemPropertyUtil.getInt("io.netty.recycler.chunkSize", 32);
        RATIO = Math.max(0, SystemPropertyUtil.getInt("io.netty.recycler.ratio", 8));
        BLOCKING_POOL = SystemPropertyUtil.getBoolean("io.netty.recycler.blocking", false);
        BATCH_FAST_TL_ONLY = SystemPropertyUtil.getBoolean("io.netty.recycler.batchFastThreadLocalOnly", true);
        if (logger.isDebugEnabled()) {
            if (DEFAULT_MAX_CAPACITY_PER_THREAD == 0) {
                logger.debug("-Dio.netty.recycler.maxCapacityPerThread: disabled");
                logger.debug("-Dio.netty.recycler.ratio: disabled");
                logger.debug("-Dio.netty.recycler.chunkSize: disabled");
                logger.debug("-Dio.netty.recycler.blocking: disabled");
                logger.debug("-Dio.netty.recycler.batchFastThreadLocalOnly: disabled");
                return;
            }
            logger.debug("-Dio.netty.recycler.maxCapacityPerThread: {}", Integer.valueOf(DEFAULT_MAX_CAPACITY_PER_THREAD));
            logger.debug("-Dio.netty.recycler.ratio: {}", Integer.valueOf(RATIO));
            logger.debug("-Dio.netty.recycler.chunkSize: {}", Integer.valueOf(DEFAULT_QUEUE_CHUNK_SIZE_PER_THREAD));
            logger.debug("-Dio.netty.recycler.blocking: {}", Boolean.valueOf(BLOCKING_POOL));
            logger.debug("-Dio.netty.recycler.batchFastThreadLocalOnly: {}", Boolean.valueOf(BATCH_FAST_TL_ONLY));
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Recycler() {
        this(DEFAULT_MAX_CAPACITY_PER_THREAD);
    }

    protected Recycler(int maxCapacityPerThread) {
        this(maxCapacityPerThread, RATIO, DEFAULT_QUEUE_CHUNK_SIZE_PER_THREAD);
    }

    @Deprecated
    protected Recycler(int maxCapacityPerThread, int maxSharedCapacityFactor) {
        this(maxCapacityPerThread, RATIO, DEFAULT_QUEUE_CHUNK_SIZE_PER_THREAD);
    }

    @Deprecated
    protected Recycler(int maxCapacityPerThread, int maxSharedCapacityFactor, int ratio, int maxDelayedQueuesPerThread) {
        this(maxCapacityPerThread, ratio, DEFAULT_QUEUE_CHUNK_SIZE_PER_THREAD);
    }

    @Deprecated
    protected Recycler(int maxCapacityPerThread, int maxSharedCapacityFactor, int ratio, int maxDelayedQueuesPerThread, int delayedQueueRatio) {
        this(maxCapacityPerThread, ratio, DEFAULT_QUEUE_CHUNK_SIZE_PER_THREAD);
    }

    protected Recycler(int maxCapacityPerThread, int ratio, int chunkSize) {
        this.threadLocal = new FastThreadLocal<LocalPool<T>>() { // from class: io.netty.util.Recycler.2
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // io.netty.util.concurrent.FastThreadLocal
            public LocalPool<T> initialValue() {
                return new LocalPool<>(Recycler.this.maxCapacityPerThread, Recycler.this.interval, Recycler.this.chunkSize);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // io.netty.util.concurrent.FastThreadLocal
            public void onRemoval(LocalPool<T> value) throws Exception {
                super.onRemoval((AnonymousClass2) value);
                MessagePassingQueue<DefaultHandle<T>> handles = ((LocalPool) value).pooledHandles;
                ((LocalPool) value).pooledHandles = null;
                ((LocalPool) value).owner = null;
                handles.clear();
            }
        };
        this.interval = Math.max(0, ratio);
        if (maxCapacityPerThread <= 0) {
            this.maxCapacityPerThread = 0;
            this.chunkSize = 0;
        } else {
            this.maxCapacityPerThread = Math.max(4, maxCapacityPerThread);
            this.chunkSize = Math.max(2, Math.min(chunkSize, this.maxCapacityPerThread >> 1));
        }
    }

    public final T get() {
        if (this.maxCapacityPerThread == 0) {
            return newObject(NOOP_HANDLE);
        }
        LocalPool<T> localPool = this.threadLocal.get();
        DefaultHandle<T> claim = localPool.claim();
        if (claim == null) {
            DefaultHandle<T> newHandle = localPool.newHandle();
            if (newHandle != null) {
                T newObject = newObject(newHandle);
                newHandle.set(newObject);
                return newObject;
            }
            return newObject(NOOP_HANDLE);
        }
        return claim.get();
    }

    @Deprecated
    public final boolean recycle(T o, Handle<T> handle) {
        if (handle == NOOP_HANDLE) {
            return false;
        }
        handle.recycle(o);
        return true;
    }

    final int threadLocalSize() {
        LocalPool<T> localPool = this.threadLocal.getIfExists();
        if (localPool == null) {
            return 0;
        }
        return ((LocalPool) localPool).pooledHandles.size() + ((LocalPool) localPool).batch.size();
    }

    /* loaded from: classes4.dex */
    public static abstract class EnhancedHandle<T> implements Handle<T> {
        public abstract void unguardedRecycle(Object obj);

        private EnhancedHandle() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class DefaultHandle<T> extends EnhancedHandle<T> {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private static final int STATE_AVAILABLE = 1;
        private static final int STATE_CLAIMED = 0;
        private static final AtomicIntegerFieldUpdater<DefaultHandle<?>> STATE_UPDATER = AtomicIntegerFieldUpdater.newUpdater(DefaultHandle.class, "state");
        private final LocalPool<T> localPool;
        private volatile int state;
        private T value;

        DefaultHandle(LocalPool<T> localPool) {
            super();
            this.localPool = localPool;
        }

        @Override // io.netty.util.internal.ObjectPool.Handle
        public void recycle(Object object) {
            if (object != this.value) {
                throw new IllegalArgumentException("object does not belong to handle");
            }
            this.localPool.release(this, true);
        }

        @Override // io.netty.util.Recycler.EnhancedHandle
        public void unguardedRecycle(Object object) {
            if (object != this.value) {
                throw new IllegalArgumentException("object does not belong to handle");
            }
            this.localPool.release(this, false);
        }

        T get() {
            return this.value;
        }

        void set(T value) {
            this.value = value;
        }

        void toClaimed() {
            if (this.state != 1) {
                throw new AssertionError();
            }
            STATE_UPDATER.lazySet(this, 0);
        }

        void toAvailable() {
            int prev = STATE_UPDATER.getAndSet(this, 1);
            if (prev == 1) {
                throw new IllegalStateException("Object has been recycled already.");
            }
        }

        void unguardedToAvailable() {
            int prev = this.state;
            if (prev != 1) {
                STATE_UPDATER.lazySet(this, 1);
                return;
            }
            throw new IllegalStateException("Object has been recycled already.");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class LocalPool<T> implements MessagePassingQueue.Consumer<DefaultHandle<T>> {
        private final ArrayDeque<DefaultHandle<T>> batch;
        private final int chunkSize;
        private volatile Thread owner;
        private volatile MessagePassingQueue<DefaultHandle<T>> pooledHandles;
        private int ratioCounter;
        private final int ratioInterval;

        LocalPool(int maxCapacity, int ratioInterval, int chunkSize) {
            this.ratioInterval = ratioInterval;
            this.chunkSize = chunkSize;
            this.batch = new ArrayDeque<>(chunkSize);
            Thread currentThread = Thread.currentThread();
            this.owner = (!Recycler.BATCH_FAST_TL_ONLY || (currentThread instanceof FastThreadLocalThread)) ? currentThread : null;
            if (Recycler.BLOCKING_POOL) {
                this.pooledHandles = new BlockingMessageQueue(maxCapacity);
            } else {
                this.pooledHandles = (MessagePassingQueue) PlatformDependent.newMpscQueue(chunkSize, maxCapacity);
            }
            this.ratioCounter = ratioInterval;
        }

        DefaultHandle<T> claim() {
            MessagePassingQueue<DefaultHandle<T>> handles = this.pooledHandles;
            if (handles == null) {
                return null;
            }
            if (this.batch.isEmpty()) {
                handles.drain(this, this.chunkSize);
            }
            DefaultHandle<T> handle = this.batch.pollFirst();
            if (handle != null) {
                handle.toClaimed();
            }
            return handle;
        }

        void release(DefaultHandle<T> handle, boolean guarded) {
            if (guarded) {
                handle.toAvailable();
            } else {
                handle.unguardedToAvailable();
            }
            Thread owner = this.owner;
            if (owner != null && Thread.currentThread() == owner && this.batch.size() < this.chunkSize) {
                accept((DefaultHandle) handle);
                return;
            }
            if (owner != null && isTerminated(owner)) {
                this.owner = null;
                this.pooledHandles = null;
            } else {
                MessagePassingQueue<DefaultHandle<T>> handles = this.pooledHandles;
                if (handles != null) {
                    handles.relaxedOffer(handle);
                }
            }
        }

        private static boolean isTerminated(Thread owner) {
            if (PlatformDependent.isJ9Jvm()) {
                if (!owner.isAlive()) {
                    return true;
                }
            } else if (owner.getState() == Thread.State.TERMINATED) {
                return true;
            }
            return false;
        }

        DefaultHandle<T> newHandle() {
            int i = this.ratioCounter + 1;
            this.ratioCounter = i;
            if (i >= this.ratioInterval) {
                this.ratioCounter = 0;
                return new DefaultHandle<>(this);
            }
            return null;
        }

        @Override // io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue.Consumer
        public void accept(DefaultHandle<T> e) {
            this.batch.addLast(e);
        }
    }

    /* loaded from: classes4.dex */
    private static final class BlockingMessageQueue<T> implements MessagePassingQueue<T> {
        private final Queue<T> deque = new ArrayDeque();
        private final int maxCapacity;

        BlockingMessageQueue(int maxCapacity) {
            this.maxCapacity = maxCapacity;
        }

        @Override // io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
        public synchronized boolean offer(T e) {
            if (this.deque.size() == this.maxCapacity) {
                return false;
            }
            return this.deque.offer(e);
        }

        @Override // io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
        public synchronized T poll() {
            return this.deque.poll();
        }

        @Override // io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
        public synchronized T peek() {
            return this.deque.peek();
        }

        @Override // io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
        public synchronized int size() {
            return this.deque.size();
        }

        @Override // io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
        public synchronized void clear() {
            this.deque.clear();
        }

        @Override // io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
        public synchronized boolean isEmpty() {
            return this.deque.isEmpty();
        }

        @Override // io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
        public int capacity() {
            return this.maxCapacity;
        }

        @Override // io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
        public boolean relaxedOffer(T e) {
            return offer(e);
        }

        @Override // io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
        public T relaxedPoll() {
            return poll();
        }

        @Override // io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
        public T relaxedPeek() {
            return peek();
        }

        @Override // io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
        public int drain(MessagePassingQueue.Consumer<T> c, int limit) {
            int i = 0;
            while (i < limit) {
                T obj = poll();
                if (obj == null) {
                    break;
                }
                c.accept(obj);
                i++;
            }
            return i;
        }

        @Override // io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
        public int fill(MessagePassingQueue.Supplier<T> s, int limit) {
            throw new UnsupportedOperationException();
        }

        @Override // io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
        public int drain(MessagePassingQueue.Consumer<T> c) {
            throw new UnsupportedOperationException();
        }

        @Override // io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
        public int fill(MessagePassingQueue.Supplier<T> s) {
            throw new UnsupportedOperationException();
        }

        @Override // io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
        public void drain(MessagePassingQueue.Consumer<T> c, MessagePassingQueue.WaitStrategy wait, MessagePassingQueue.ExitCondition exit) {
            throw new UnsupportedOperationException();
        }

        @Override // io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue
        public void fill(MessagePassingQueue.Supplier<T> s, MessagePassingQueue.WaitStrategy wait, MessagePassingQueue.ExitCondition exit) {
            throw new UnsupportedOperationException();
        }
    }
}
