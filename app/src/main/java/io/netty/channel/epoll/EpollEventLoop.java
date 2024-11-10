package io.netty.channel.epoll;

import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.EventLoopTaskQueueFactory;
import io.netty.channel.SelectStrategy;
import io.netty.channel.SingleThreadEventLoop;
import io.netty.channel.epoll.AbstractEpollChannel;
import io.netty.channel.unix.FileDescriptor;
import io.netty.channel.unix.IovArray;
import io.netty.util.IntSupplier;
import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.collection.IntObjectMap;
import io.netty.util.concurrent.RejectedExecutionHandler;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.IOException;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicLong;

/* loaded from: classes4.dex */
public class EpollEventLoop extends SingleThreadEventLoop {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final long AWAKE = -1;
    private static final long MAX_SCHEDULED_TIMERFD_NS = 999999999;
    private static final long NONE = Long.MAX_VALUE;
    private final boolean allowGrowing;
    private final IntObjectMap<AbstractEpollChannel> channels;
    private NativeDatagramPacketArray datagramPacketArray;
    private FileDescriptor epollFd;
    private FileDescriptor eventFd;
    private final EpollEventArray events;
    private volatile int ioRatio;
    private IovArray iovArray;
    private final AtomicLong nextWakeupNanos;
    private boolean pendingWakeup;
    private final IntSupplier selectNowSupplier;
    private final SelectStrategy selectStrategy;
    private FileDescriptor timerFd;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) EpollEventLoop.class);
    private static final long EPOLL_WAIT_MILLIS_THRESHOLD = SystemPropertyUtil.getLong("io.netty.channel.epoll.epollWaitThreshold", 10);

    static {
        Epoll.ensureAvailability();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public EpollEventLoop(EventLoopGroup parent, Executor executor, int maxEvents, SelectStrategy strategy, RejectedExecutionHandler rejectedExecutionHandler, EventLoopTaskQueueFactory taskQueueFactory, EventLoopTaskQueueFactory tailTaskQueueFactory) {
        super(parent, executor, false, newTaskQueue(taskQueueFactory), newTaskQueue(tailTaskQueueFactory), rejectedExecutionHandler);
        this.channels = new IntObjectHashMap(4096);
        this.selectNowSupplier = new IntSupplier() { // from class: io.netty.channel.epoll.EpollEventLoop.1
            @Override // io.netty.util.IntSupplier
            public int get() throws Exception {
                return EpollEventLoop.this.epollWaitNow();
            }
        };
        this.nextWakeupNanos = new AtomicLong(-1L);
        this.ioRatio = 50;
        this.selectStrategy = (SelectStrategy) ObjectUtil.checkNotNull(strategy, "strategy");
        if (maxEvents == 0) {
            this.allowGrowing = true;
            this.events = new EpollEventArray(4096);
        } else {
            this.allowGrowing = false;
            this.events = new EpollEventArray(maxEvents);
        }
        openFileDescriptors();
    }

    public void openFileDescriptors() {
        boolean success = false;
        FileDescriptor epollFd = null;
        FileDescriptor eventFd = null;
        FileDescriptor timerFd = null;
        try {
            FileDescriptor newEpollCreate = Native.newEpollCreate();
            epollFd = newEpollCreate;
            this.epollFd = newEpollCreate;
            FileDescriptor newEventFd = Native.newEventFd();
            eventFd = newEventFd;
            this.eventFd = newEventFd;
            try {
                Native.epollCtlAdd(epollFd.intValue(), eventFd.intValue(), Native.EPOLLIN | Native.EPOLLET);
                FileDescriptor newTimerFd = Native.newTimerFd();
                timerFd = newTimerFd;
                this.timerFd = newTimerFd;
                try {
                    Native.epollCtlAdd(epollFd.intValue(), timerFd.intValue(), Native.EPOLLIN | Native.EPOLLET);
                    success = true;
                } catch (IOException e) {
                    throw new IllegalStateException("Unable to add timerFd filedescriptor to epoll", e);
                }
            } catch (IOException e2) {
                throw new IllegalStateException("Unable to add eventFd filedescriptor to epoll", e2);
            }
        } finally {
            if (!success) {
                closeFileDescriptor(epollFd);
                closeFileDescriptor(eventFd);
                closeFileDescriptor(timerFd);
            }
        }
    }

    private static void closeFileDescriptor(FileDescriptor fd) {
        if (fd != null) {
            try {
                fd.close();
            } catch (Exception e) {
            }
        }
    }

    private static Queue<Runnable> newTaskQueue(EventLoopTaskQueueFactory queueFactory) {
        if (queueFactory == null) {
            return newTaskQueue0(DEFAULT_MAX_PENDING_TASKS);
        }
        return queueFactory.newTaskQueue(DEFAULT_MAX_PENDING_TASKS);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public IovArray cleanIovArray() {
        if (this.iovArray == null) {
            this.iovArray = new IovArray();
        } else {
            this.iovArray.clear();
        }
        return this.iovArray;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public NativeDatagramPacketArray cleanDatagramPacketArray() {
        if (this.datagramPacketArray == null) {
            this.datagramPacketArray = new NativeDatagramPacketArray();
        } else {
            this.datagramPacketArray.clear();
        }
        return this.datagramPacketArray;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.util.concurrent.SingleThreadEventExecutor
    public void wakeup(boolean inEventLoop) {
        if (!inEventLoop && this.nextWakeupNanos.getAndSet(-1L) != -1) {
            Native.eventFdWrite(this.eventFd.intValue(), 1L);
        }
    }

    @Override // io.netty.util.concurrent.AbstractScheduledEventExecutor
    protected boolean beforeScheduledTaskSubmitted(long deadlineNanos) {
        return deadlineNanos < this.nextWakeupNanos.get();
    }

    @Override // io.netty.util.concurrent.AbstractScheduledEventExecutor
    protected boolean afterScheduledTaskSubmitted(long deadlineNanos) {
        return deadlineNanos < this.nextWakeupNanos.get();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void add(AbstractEpollChannel ch) throws IOException {
        if (!inEventLoop()) {
            throw new AssertionError();
        }
        int fd = ch.socket.intValue();
        Native.epollCtlAdd(this.epollFd.intValue(), fd, ch.flags);
        AbstractEpollChannel old = this.channels.put(fd, (int) ch);
        if (old != null && old.isOpen()) {
            throw new AssertionError();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void modify(AbstractEpollChannel ch) throws IOException {
        if (!inEventLoop()) {
            throw new AssertionError();
        }
        Native.epollCtlMod(this.epollFd.intValue(), ch.socket.intValue(), ch.flags);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void remove(AbstractEpollChannel ch) throws IOException {
        if (!inEventLoop()) {
            throw new AssertionError();
        }
        int fd = ch.socket.intValue();
        AbstractEpollChannel old = this.channels.remove(fd);
        if (old != null && old != ch) {
            this.channels.put(fd, (int) old);
            if (ch.isOpen()) {
                throw new AssertionError();
            }
        } else if (ch.isOpen()) {
            Native.epollCtlDel(this.epollFd.intValue(), fd);
        }
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
        IntObjectMap<AbstractEpollChannel> ch = this.channels;
        if (ch.isEmpty()) {
            return SingleThreadEventLoop.ChannelsReadOnlyIterator.empty();
        }
        return new SingleThreadEventLoop.ChannelsReadOnlyIterator(ch.values());
    }

    private long epollWait(long deadlineNanos) throws IOException {
        if (deadlineNanos == Long.MAX_VALUE) {
            return Native.epollWait(this.epollFd, this.events, this.timerFd, Integer.MAX_VALUE, 0, EPOLL_WAIT_MILLIS_THRESHOLD);
        }
        long totalDelay = deadlineToDelayNanos(deadlineNanos);
        int delaySeconds = (int) Math.min(totalDelay / 1000000000, 2147483647L);
        int delayNanos = (int) Math.min(totalDelay - (delaySeconds * 1000000000), MAX_SCHEDULED_TIMERFD_NS);
        return Native.epollWait(this.epollFd, this.events, this.timerFd, delaySeconds, delayNanos, EPOLL_WAIT_MILLIS_THRESHOLD);
    }

    private int epollWaitNoTimerChange() throws IOException {
        return Native.epollWait(this.epollFd, this.events, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int epollWaitNow() throws IOException {
        return Native.epollWait(this.epollFd, this.events, true);
    }

    private int epollBusyWait() throws IOException {
        return Native.epollBusyWait(this.epollFd, this.events);
    }

    private int epollWaitTimeboxed() throws IOException {
        return Native.epollWait(this.epollFd, this.events, 1000);
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:4:0x0011. Please report as an issue. */
    /* JADX WARN: Finally extract failed */
    /* JADX WARN: Removed duplicated region for block: B:29:0x00c6  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0130 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:53:0x0005 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:63:0x00e0  */
    @Override // io.netty.util.concurrent.SingleThreadEventExecutor
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected void run() {
        /*
            Method dump skipped, instructions count: 386
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.channel.epoll.EpollEventLoop.run():void");
    }

    void handleLoopException(Throwable t) {
        logger.warn("Unexpected exception in the selector loop.", t);
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
        }
    }

    private void closeAll() {
        AbstractEpollChannel[] localChannels = (AbstractEpollChannel[]) this.channels.values().toArray(new AbstractEpollChannel[0]);
        for (AbstractEpollChannel ch : localChannels) {
            ch.unsafe().close(ch.unsafe().voidPromise());
        }
    }

    private boolean processReady(EpollEventArray events, int ready) {
        boolean timerFired = false;
        for (int i = 0; i < ready; i++) {
            int fd = events.fd(i);
            if (fd == this.eventFd.intValue()) {
                this.pendingWakeup = false;
            } else if (fd == this.timerFd.intValue()) {
                timerFired = true;
            } else {
                long ev = events.events(i);
                AbstractEpollChannel ch = this.channels.get(fd);
                if (ch != null) {
                    AbstractEpollChannel.AbstractEpollUnsafe unsafe = (AbstractEpollChannel.AbstractEpollUnsafe) ch.unsafe();
                    if (((Native.EPOLLERR | Native.EPOLLOUT) & ev) != 0) {
                        unsafe.epollOutReady();
                    }
                    if (((Native.EPOLLERR | Native.EPOLLIN) & ev) != 0) {
                        unsafe.epollInReady();
                    }
                    if ((Native.EPOLLRDHUP & ev) != 0) {
                        unsafe.epollRdHupReady();
                    }
                } else {
                    try {
                        Native.epollCtlDel(this.epollFd.intValue(), fd);
                    } catch (IOException e) {
                    }
                }
            }
        }
        return timerFired;
    }

    @Override // io.netty.util.concurrent.SingleThreadEventExecutor
    protected void cleanup() {
        try {
            closeFileDescriptors();
        } finally {
            if (this.iovArray != null) {
                this.iovArray.release();
                this.iovArray = null;
            }
            if (this.datagramPacketArray != null) {
                this.datagramPacketArray.release();
                this.datagramPacketArray = null;
            }
            this.events.free();
        }
    }

    public void closeFileDescriptors() {
        int count;
        while (this.pendingWakeup) {
            try {
                count = epollWaitTimeboxed();
            } catch (IOException e) {
            }
            if (count == 0) {
                break;
            }
            int i = 0;
            while (true) {
                if (i >= count) {
                    break;
                }
                if (this.events.fd(i) != this.eventFd.intValue()) {
                    i++;
                } else {
                    this.pendingWakeup = false;
                    break;
                }
            }
        }
        try {
            this.eventFd.close();
        } catch (IOException e2) {
            logger.warn("Failed to close the event fd.", (Throwable) e2);
        }
        try {
            this.timerFd.close();
        } catch (IOException e3) {
            logger.warn("Failed to close the timer fd.", (Throwable) e3);
        }
        try {
            this.epollFd.close();
        } catch (IOException e4) {
            logger.warn("Failed to close the epoll fd.", (Throwable) e4);
        }
    }
}
