package io.netty.channel.kqueue;

import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.EventLoopTaskQueueFactory;
import io.netty.channel.SelectStrategy;
import io.netty.channel.SingleThreadEventLoop;
import io.netty.channel.kqueue.AbstractKQueueChannel;
import io.netty.channel.unix.FileDescriptor;
import io.netty.channel.unix.IovArray;
import io.netty.util.IntSupplier;
import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.collection.IntObjectMap;
import io.netty.util.concurrent.RejectedExecutionHandler;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.IOException;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public final class KQueueEventLoop extends SingleThreadEventLoop {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int KQUEUE_MAX_TIMEOUT_SECONDS = 86399;
    private static final int KQUEUE_WAKE_UP_IDENT = 0;
    private final boolean allowGrowing;
    private final KQueueEventArray changeList;
    private final IntObjectMap<AbstractKQueueChannel> channels;
    private final KQueueEventArray eventList;
    private volatile int ioRatio;
    private final IovArray iovArray;
    private final FileDescriptor kqueueFd;
    private final IntSupplier selectNowSupplier;
    private final SelectStrategy selectStrategy;
    private volatile int wakenUp;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) KQueueEventLoop.class);
    private static final AtomicIntegerFieldUpdater<KQueueEventLoop> WAKEN_UP_UPDATER = AtomicIntegerFieldUpdater.newUpdater(KQueueEventLoop.class, "wakenUp");

    static {
        KQueue.ensureAvailability();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public KQueueEventLoop(EventLoopGroup parent, Executor executor, int maxEvents, SelectStrategy strategy, RejectedExecutionHandler rejectedExecutionHandler, EventLoopTaskQueueFactory taskQueueFactory, EventLoopTaskQueueFactory tailTaskQueueFactory) {
        super(parent, executor, false, newTaskQueue(taskQueueFactory), newTaskQueue(tailTaskQueueFactory), rejectedExecutionHandler);
        this.iovArray = new IovArray();
        this.selectNowSupplier = new IntSupplier() { // from class: io.netty.channel.kqueue.KQueueEventLoop.1
            @Override // io.netty.util.IntSupplier
            public int get() throws Exception {
                return KQueueEventLoop.this.kqueueWaitNow();
            }
        };
        this.channels = new IntObjectHashMap(4096);
        this.ioRatio = 50;
        this.selectStrategy = (SelectStrategy) ObjectUtil.checkNotNull(strategy, "strategy");
        this.kqueueFd = Native.newKQueue();
        if (maxEvents == 0) {
            this.allowGrowing = true;
            maxEvents = 4096;
        } else {
            this.allowGrowing = false;
        }
        this.changeList = new KQueueEventArray(maxEvents);
        this.eventList = new KQueueEventArray(maxEvents);
        int result = Native.keventAddUserEvent(this.kqueueFd.intValue(), 0);
        if (result < 0) {
            cleanup();
            throw new IllegalStateException("kevent failed to add user event with errno: " + (-result));
        }
    }

    private static Queue<Runnable> newTaskQueue(EventLoopTaskQueueFactory queueFactory) {
        if (queueFactory == null) {
            return newTaskQueue0(DEFAULT_MAX_PENDING_TASKS);
        }
        return queueFactory.newTaskQueue(DEFAULT_MAX_PENDING_TASKS);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void add(AbstractKQueueChannel ch) {
        if (!inEventLoop()) {
            throw new AssertionError();
        }
        AbstractKQueueChannel old = this.channels.put(ch.fd().intValue(), (int) ch);
        if (old != null && old.isOpen()) {
            throw new AssertionError();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void evSet(AbstractKQueueChannel ch, short filter, short flags, int fflags) {
        if (!inEventLoop()) {
            throw new AssertionError();
        }
        this.changeList.evSet(ch, filter, flags, fflags);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void remove(AbstractKQueueChannel ch) throws Exception {
        if (!inEventLoop()) {
            throw new AssertionError();
        }
        int fd = ch.fd().intValue();
        AbstractKQueueChannel old = this.channels.remove(fd);
        if (old != null && old != ch) {
            this.channels.put(fd, (int) old);
            if (ch.isOpen()) {
                throw new AssertionError();
            }
        } else if (ch.isOpen()) {
            ch.unregisterFilters();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public IovArray cleanArray() {
        this.iovArray.clear();
        return this.iovArray;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.util.concurrent.SingleThreadEventExecutor
    public void wakeup(boolean inEventLoop) {
        if (!inEventLoop && WAKEN_UP_UPDATER.compareAndSet(this, 0, 1)) {
            wakeup();
        }
    }

    private void wakeup() {
        Native.keventTriggerUserEvent(this.kqueueFd.intValue(), 0);
    }

    private int kqueueWait(boolean oldWakeup) throws IOException {
        if (oldWakeup && hasTasks()) {
            return kqueueWaitNow();
        }
        long totalDelay = delayNanos(System.nanoTime());
        int delaySeconds = (int) Math.min(totalDelay / 1000000000, 86399L);
        int delayNanos = (int) (totalDelay % 1000000000);
        return kqueueWait(delaySeconds, delayNanos);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int kqueueWaitNow() throws IOException {
        return kqueueWait(0, 0);
    }

    private int kqueueWait(int timeoutSec, int timeoutNs) throws IOException {
        int numEvents = Native.keventWait(this.kqueueFd.intValue(), this.changeList, this.eventList, timeoutSec, timeoutNs);
        this.changeList.clear();
        return numEvents;
    }

    private void processReady(int ready) {
        for (int i = 0; i < ready; i++) {
            short filter = this.eventList.filter(i);
            short flags = this.eventList.flags(i);
            int fd = this.eventList.fd(i);
            if (filter == Native.EVFILT_USER || (Native.EV_ERROR & flags) != 0) {
                if (filter == Native.EVFILT_USER && (filter != Native.EVFILT_USER || fd != 0)) {
                    throw new AssertionError();
                }
            } else {
                AbstractKQueueChannel channel = this.channels.get(fd);
                if (channel == null) {
                    logger.warn("events[{}]=[{}, {}] had no channel!", Integer.valueOf(i), Integer.valueOf(this.eventList.fd(i)), Short.valueOf(filter));
                } else {
                    AbstractKQueueChannel.AbstractKQueueUnsafe unsafe = (AbstractKQueueChannel.AbstractKQueueUnsafe) channel.unsafe();
                    if (filter == Native.EVFILT_WRITE) {
                        unsafe.writeReady();
                    } else if (filter == Native.EVFILT_READ) {
                        unsafe.readReady(this.eventList.data(i));
                    } else if (filter == Native.EVFILT_SOCK && (this.eventList.fflags(i) & Native.NOTE_RDHUP) != 0) {
                        unsafe.readEOF();
                    }
                    if ((Native.EV_EOF & flags) != 0) {
                        unsafe.readEOF();
                    }
                }
            }
        }
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:4:0x000e. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:34:0x0048  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x0098 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:52:0x0001 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:64:0x0057 A[Catch: all -> 0x00a4, Error -> 0x00c2, TRY_LEAVE, TryCatch #5 {all -> 0x00a4, blocks: (B:2:0x0001, B:25:0x002a, B:28:0x0036, B:30:0x003f, B:31:0x0042, B:35:0x0053, B:36:0x0081, B:38:0x0085, B:40:0x008d, B:62:0x004f, B:63:0x0052, B:64:0x0057, B:66:0x0072, B:71:0x0062, B:72:0x0071), top: B:1:0x0001, outer: #4 }] */
    @Override // io.netty.util.concurrent.SingleThreadEventExecutor
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected void run() {
        /*
            Method dump skipped, instructions count: 232
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.channel.kqueue.KQueueEventLoop.run():void");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.util.concurrent.SingleThreadEventExecutor
    public Queue<Runnable> newTaskQueue(int maxPendingTasks) {
        return newTaskQueue0(maxPendingTasks);
    }

    private static Queue<Runnable> newTaskQueue0(int maxPendingTasks) {
        return maxPendingTasks == Integer.MAX_VALUE ? PlatformDependent.newMpscQueue() : PlatformDependent.newMpscQueue(maxPendingTasks);
    }

    public int getIoRatio() {
        return this.ioRatio;
    }

    public void setIoRatio(int ioRatio) {
        if (ioRatio <= 0 || ioRatio > 100) {
            throw new IllegalArgumentException("ioRatio: " + ioRatio + " (expected: 0 < ioRatio <= 100)");
        }
        this.ioRatio = ioRatio;
    }

    @Override // io.netty.channel.SingleThreadEventLoop
    public int registeredChannels() {
        return this.channels.size();
    }

    @Override // io.netty.channel.SingleThreadEventLoop
    public Iterator<Channel> registeredChannelsIterator() {
        if (!inEventLoop()) {
            throw new AssertionError();
        }
        IntObjectMap<AbstractKQueueChannel> ch = this.channels;
        if (ch.isEmpty()) {
            return SingleThreadEventLoop.ChannelsReadOnlyIterator.empty();
        }
        return new SingleThreadEventLoop.ChannelsReadOnlyIterator(ch.values());
    }

    @Override // io.netty.util.concurrent.SingleThreadEventExecutor
    protected void cleanup() {
        try {
            try {
                this.kqueueFd.close();
            } catch (IOException e) {
                logger.warn("Failed to close the kqueue fd.", (Throwable) e);
            }
        } finally {
            this.changeList.free();
            this.eventList.free();
        }
    }

    private void closeAll() {
        try {
            kqueueWaitNow();
        } catch (IOException e) {
        }
        AbstractKQueueChannel[] localChannels = (AbstractKQueueChannel[]) this.channels.values().toArray(new AbstractKQueueChannel[0]);
        for (AbstractKQueueChannel ch : localChannels) {
            ch.unsafe().close(ch.unsafe().voidPromise());
        }
    }

    private static void handleLoopException(Throwable t) {
        logger.warn("Unexpected exception in the selector loop.", t);
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
        }
    }
}
