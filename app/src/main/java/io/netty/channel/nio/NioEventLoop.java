package io.netty.channel.nio;

import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopException;
import io.netty.channel.EventLoopTaskQueueFactory;
import io.netty.channel.SelectStrategy;
import io.netty.channel.SingleThreadEventLoop;
import io.netty.channel.nio.AbstractNioChannel;
import io.netty.util.IntSupplier;
import io.netty.util.concurrent.RejectedExecutionHandler;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.ReflectionUtil;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.spi.SelectorProvider;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicLong;

/* loaded from: classes4.dex */
public final class NioEventLoop extends SingleThreadEventLoop {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final long AWAKE = -1;
    private static final int CLEANUP_INTERVAL = 256;
    private static final int MIN_PREMATURE_SELECTOR_RETURNS = 3;
    private static final long NONE = Long.MAX_VALUE;
    private static final int SELECTOR_AUTO_REBUILD_THRESHOLD;
    private int cancelledKeys;
    private volatile int ioRatio;
    private boolean needsToSelectAgain;
    private final AtomicLong nextWakeupNanos;
    private final SelectorProvider provider;
    private final IntSupplier selectNowSupplier;
    private final SelectStrategy selectStrategy;
    private SelectedSelectionKeySet selectedKeys;
    private Selector selector;
    private Selector unwrappedSelector;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) NioEventLoop.class);
    private static final boolean DISABLE_KEY_SET_OPTIMIZATION = SystemPropertyUtil.getBoolean("io.netty.noKeySetOptimization", false);

    static {
        if (PlatformDependent.javaVersion() < 7) {
            String bugLevel = SystemPropertyUtil.get("sun.nio.ch.bugLevel");
            if (bugLevel == null) {
                try {
                    AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: io.netty.channel.nio.NioEventLoop.2
                        @Override // java.security.PrivilegedAction
                        public Void run() {
                            System.setProperty("sun.nio.ch.bugLevel", "");
                            return null;
                        }
                    });
                } catch (SecurityException e) {
                    logger.debug("Unable to get/set System Property: sun.nio.ch.bugLevel", (Throwable) e);
                }
            }
        }
        int selectorAutoRebuildThreshold = SystemPropertyUtil.getInt("io.netty.selectorAutoRebuildThreshold", 512);
        if (selectorAutoRebuildThreshold < 3) {
            selectorAutoRebuildThreshold = 0;
        }
        SELECTOR_AUTO_REBUILD_THRESHOLD = selectorAutoRebuildThreshold;
        if (logger.isDebugEnabled()) {
            logger.debug("-Dio.netty.noKeySetOptimization: {}", Boolean.valueOf(DISABLE_KEY_SET_OPTIMIZATION));
            logger.debug("-Dio.netty.selectorAutoRebuildThreshold: {}", Integer.valueOf(SELECTOR_AUTO_REBUILD_THRESHOLD));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public NioEventLoop(NioEventLoopGroup parent, Executor executor, SelectorProvider selectorProvider, SelectStrategy strategy, RejectedExecutionHandler rejectedExecutionHandler, EventLoopTaskQueueFactory taskQueueFactory, EventLoopTaskQueueFactory tailTaskQueueFactory) {
        super(parent, executor, false, newTaskQueue(taskQueueFactory), newTaskQueue(tailTaskQueueFactory), rejectedExecutionHandler);
        this.selectNowSupplier = new IntSupplier() { // from class: io.netty.channel.nio.NioEventLoop.1
            @Override // io.netty.util.IntSupplier
            public int get() throws Exception {
                return NioEventLoop.this.selectNow();
            }
        };
        this.nextWakeupNanos = new AtomicLong(-1L);
        this.ioRatio = 50;
        this.provider = (SelectorProvider) ObjectUtil.checkNotNull(selectorProvider, "selectorProvider");
        this.selectStrategy = (SelectStrategy) ObjectUtil.checkNotNull(strategy, "selectStrategy");
        SelectorTuple selectorTuple = openSelector();
        this.selector = selectorTuple.selector;
        this.unwrappedSelector = selectorTuple.unwrappedSelector;
    }

    private static Queue<Runnable> newTaskQueue(EventLoopTaskQueueFactory queueFactory) {
        if (queueFactory == null) {
            return newTaskQueue0(DEFAULT_MAX_PENDING_TASKS);
        }
        return queueFactory.newTaskQueue(DEFAULT_MAX_PENDING_TASKS);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class SelectorTuple {
        final Selector selector;
        final Selector unwrappedSelector;

        SelectorTuple(Selector unwrappedSelector) {
            this.unwrappedSelector = unwrappedSelector;
            this.selector = unwrappedSelector;
        }

        SelectorTuple(Selector unwrappedSelector, Selector selector) {
            this.unwrappedSelector = unwrappedSelector;
            this.selector = selector;
        }
    }

    private SelectorTuple openSelector() {
        try {
            final Selector unwrappedSelector = this.provider.openSelector();
            if (DISABLE_KEY_SET_OPTIMIZATION) {
                return new SelectorTuple(unwrappedSelector);
            }
            Object maybeSelectorImplClass = AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: io.netty.channel.nio.NioEventLoop.3
                @Override // java.security.PrivilegedAction
                public Object run() {
                    try {
                        return Class.forName("sun.nio.ch.SelectorImpl", false, PlatformDependent.getSystemClassLoader());
                    } catch (Throwable cause) {
                        return cause;
                    }
                }
            });
            if (!(maybeSelectorImplClass instanceof Class) || !((Class) maybeSelectorImplClass).isAssignableFrom(unwrappedSelector.getClass())) {
                if (maybeSelectorImplClass instanceof Throwable) {
                    Throwable t = (Throwable) maybeSelectorImplClass;
                    logger.trace("failed to instrument a special java.util.Set into: {}", unwrappedSelector, t);
                }
                return new SelectorTuple(unwrappedSelector);
            }
            final Class<?> selectorImplClass = (Class) maybeSelectorImplClass;
            final SelectedSelectionKeySet selectedKeySet = new SelectedSelectionKeySet();
            Object maybeException = AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: io.netty.channel.nio.NioEventLoop.4
                @Override // java.security.PrivilegedAction
                public Object run() {
                    try {
                        Field selectedKeysField = selectorImplClass.getDeclaredField("selectedKeys");
                        Field publicSelectedKeysField = selectorImplClass.getDeclaredField("publicSelectedKeys");
                        if (PlatformDependent.javaVersion() >= 9 && PlatformDependent.hasUnsafe()) {
                            long selectedKeysFieldOffset = PlatformDependent.objectFieldOffset(selectedKeysField);
                            long publicSelectedKeysFieldOffset = PlatformDependent.objectFieldOffset(publicSelectedKeysField);
                            if (selectedKeysFieldOffset != -1 && publicSelectedKeysFieldOffset != -1) {
                                PlatformDependent.putObject(unwrappedSelector, selectedKeysFieldOffset, selectedKeySet);
                                PlatformDependent.putObject(unwrappedSelector, publicSelectedKeysFieldOffset, selectedKeySet);
                                return null;
                            }
                        }
                        Throwable cause = ReflectionUtil.trySetAccessible(selectedKeysField, true);
                        if (cause != null) {
                            return cause;
                        }
                        Throwable cause2 = ReflectionUtil.trySetAccessible(publicSelectedKeysField, true);
                        if (cause2 != null) {
                            return cause2;
                        }
                        selectedKeysField.set(unwrappedSelector, selectedKeySet);
                        publicSelectedKeysField.set(unwrappedSelector, selectedKeySet);
                        return null;
                    } catch (IllegalAccessException e) {
                        return e;
                    } catch (NoSuchFieldException e2) {
                        return e2;
                    }
                }
            });
            if (maybeException instanceof Exception) {
                this.selectedKeys = null;
                Exception e = (Exception) maybeException;
                logger.trace("failed to instrument a special java.util.Set into: {}", unwrappedSelector, e);
                return new SelectorTuple(unwrappedSelector);
            }
            this.selectedKeys = selectedKeySet;
            logger.trace("instrumented a special java.util.Set into: {}", unwrappedSelector);
            return new SelectorTuple(unwrappedSelector, new SelectedSelectionKeySetSelector(unwrappedSelector, selectedKeySet));
        } catch (IOException e2) {
            throw new ChannelException("failed to open a new selector", e2);
        }
    }

    public SelectorProvider selectorProvider() {
        return this.provider;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.util.concurrent.SingleThreadEventExecutor
    public Queue<Runnable> newTaskQueue(int maxPendingTasks) {
        return newTaskQueue0(maxPendingTasks);
    }

    private static Queue<Runnable> newTaskQueue0(int maxPendingTasks) {
        return maxPendingTasks == Integer.MAX_VALUE ? PlatformDependent.newMpscQueue() : PlatformDependent.newMpscQueue(maxPendingTasks);
    }

    public void register(final SelectableChannel ch, final int interestOps, final NioTask<?> task) {
        ObjectUtil.checkNotNull(ch, "ch");
        if (interestOps == 0) {
            throw new IllegalArgumentException("interestOps must be non-zero.");
        }
        if (((~ch.validOps()) & interestOps) != 0) {
            throw new IllegalArgumentException("invalid interestOps: " + interestOps + "(validOps: " + ch.validOps() + ')');
        }
        ObjectUtil.checkNotNull(task, "task");
        if (isShutdown()) {
            throw new IllegalStateException("event loop shut down");
        }
        if (inEventLoop()) {
            register0(ch, interestOps, task);
            return;
        }
        try {
            submit(new Runnable() { // from class: io.netty.channel.nio.NioEventLoop.5
                @Override // java.lang.Runnable
                public void run() {
                    NioEventLoop.this.register0(ch, interestOps, task);
                }
            }).sync();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void register0(SelectableChannel ch, int interestOps, NioTask<?> task) {
        try {
            ch.register(this.unwrappedSelector, interestOps, task);
        } catch (Exception e) {
            throw new EventLoopException("failed to register a channel", e);
        }
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

    public void rebuildSelector() {
        if (!inEventLoop()) {
            execute(new Runnable() { // from class: io.netty.channel.nio.NioEventLoop.6
                @Override // java.lang.Runnable
                public void run() {
                    NioEventLoop.this.rebuildSelector0();
                }
            });
        } else {
            rebuildSelector0();
        }
    }

    @Override // io.netty.channel.SingleThreadEventLoop
    public int registeredChannels() {
        return this.selector.keys().size() - this.cancelledKeys;
    }

    @Override // io.netty.channel.SingleThreadEventLoop
    public Iterator<Channel> registeredChannelsIterator() {
        if (!inEventLoop()) {
            throw new AssertionError();
        }
        final Set<SelectionKey> keys = this.selector.keys();
        if (keys.isEmpty()) {
            return SingleThreadEventLoop.ChannelsReadOnlyIterator.empty();
        }
        return new Iterator<Channel>() { // from class: io.netty.channel.nio.NioEventLoop.7
            boolean isDone;
            Channel next;
            final Iterator<SelectionKey> selectionKeyIterator;

            {
                this.selectionKeyIterator = ((Set) ObjectUtil.checkNotNull(keys, "selectionKeys")).iterator();
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                if (this.isDone) {
                    return false;
                }
                if (this.next != null) {
                    return true;
                }
                Channel cur = nextOrDone();
                this.next = cur;
                return cur != null;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.Iterator
            public Channel next() {
                if (this.isDone) {
                    throw new NoSuchElementException();
                }
                Channel cur = this.next;
                if (cur == null && (cur = nextOrDone()) == null) {
                    throw new NoSuchElementException();
                }
                this.next = nextOrDone();
                return cur;
            }

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException("remove");
            }

            private Channel nextOrDone() {
                Iterator<SelectionKey> it2 = this.selectionKeyIterator;
                while (it2.hasNext()) {
                    SelectionKey key = it2.next();
                    if (key.isValid()) {
                        Object attachment = key.attachment();
                        if (attachment instanceof AbstractNioChannel) {
                            return (AbstractNioChannel) attachment;
                        }
                    }
                }
                this.isDone = true;
                return null;
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void rebuildSelector0() {
        Selector oldSelector = this.selector;
        if (oldSelector == null) {
            return;
        }
        try {
            SelectorTuple newSelectorTuple = openSelector();
            int nChannels = 0;
            for (SelectionKey key : oldSelector.keys()) {
                Object a = key.attachment();
                try {
                    if (key.isValid() && key.channel().keyFor(newSelectorTuple.unwrappedSelector) == null) {
                        int interestOps = key.interestOps();
                        key.cancel();
                        SelectionKey newKey = key.channel().register(newSelectorTuple.unwrappedSelector, interestOps, a);
                        if (a instanceof AbstractNioChannel) {
                            ((AbstractNioChannel) a).selectionKey = newKey;
                        }
                        nChannels++;
                    }
                } catch (Exception e) {
                    logger.warn("Failed to re-register a Channel to the new Selector.", (Throwable) e);
                    if (a instanceof AbstractNioChannel) {
                        AbstractNioChannel ch = (AbstractNioChannel) a;
                        ch.unsafe().close(ch.unsafe().voidPromise());
                    } else {
                        NioTask<SelectableChannel> task = (NioTask) a;
                        invokeChannelUnregistered(task, key, e);
                    }
                }
            }
            this.selector = newSelectorTuple.selector;
            this.unwrappedSelector = newSelectorTuple.unwrappedSelector;
            try {
                oldSelector.close();
            } catch (Throwable t) {
                if (logger.isWarnEnabled()) {
                    logger.warn("Failed to close the old Selector.", t);
                }
            }
            if (logger.isInfoEnabled()) {
                logger.info("Migrated " + nChannels + " channel(s) to the new Selector.");
            }
        } catch (Exception e2) {
            logger.warn("Failed to create a new Selector.", (Throwable) e2);
        }
    }

    /* JADX WARN: Can't wrap try/catch for region: R(22:24|25|(1:27)|28|29|30|32|(2:34|35)|36|37|38|39|(2:(2:72|73)|42)(1:(4:79|80|81|82)(1:87))|(7:46|(1:48)|49|50|52|(3:54|55|(2:57|58)(1:59))(1:60)|17)|66|(1:70)|71|49|50|52|(0)(0)|17) */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x00e5, code lost:            r1 = move-exception;     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x00e6, code lost:            handleLoopException(r1);     */
    /* JADX WARN: Failed to find 'out' block for switch in B:4:0x000d. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:41:0x0064  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x00b6  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x00d9 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:60:0x0001 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:78:0x0076  */
    @Override // io.netty.util.concurrent.SingleThreadEventExecutor
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected void run() {
        /*
            Method dump skipped, instructions count: 396
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.channel.nio.NioEventLoop.run():void");
    }

    private boolean unexpectedSelectorWakeup(int selectCnt) {
        if (Thread.interrupted()) {
            if (logger.isDebugEnabled()) {
                logger.debug("Selector.select() returned prematurely because Thread.currentThread().interrupt() was called. Use NioEventLoop.shutdownGracefully() to shutdown the NioEventLoop.");
            }
            return true;
        }
        if (SELECTOR_AUTO_REBUILD_THRESHOLD > 0 && selectCnt >= SELECTOR_AUTO_REBUILD_THRESHOLD) {
            logger.warn("Selector.select() returned prematurely {} times in a row; rebuilding Selector {}.", Integer.valueOf(selectCnt), this.selector);
            rebuildSelector();
            return true;
        }
        return false;
    }

    private static void handleLoopException(Throwable t) {
        logger.warn("Unexpected exception in the selector loop.", t);
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
        }
    }

    private void processSelectedKeys() {
        if (this.selectedKeys != null) {
            processSelectedKeysOptimized();
        } else {
            processSelectedKeysPlain(this.selector.selectedKeys());
        }
    }

    @Override // io.netty.util.concurrent.SingleThreadEventExecutor
    protected void cleanup() {
        try {
            this.selector.close();
        } catch (IOException e) {
            logger.warn("Failed to close a selector.", (Throwable) e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void cancel(SelectionKey key) {
        key.cancel();
        this.cancelledKeys++;
        if (this.cancelledKeys >= 256) {
            this.cancelledKeys = 0;
            this.needsToSelectAgain = true;
        }
    }

    private void processSelectedKeysPlain(Set<SelectionKey> selectedKeys) {
        if (selectedKeys.isEmpty()) {
            return;
        }
        Iterator<SelectionKey> i = selectedKeys.iterator();
        while (true) {
            SelectionKey k = i.next();
            Object a = k.attachment();
            i.remove();
            if (a instanceof AbstractNioChannel) {
                processSelectedKey(k, (AbstractNioChannel) a);
            } else {
                NioTask<SelectableChannel> task = (NioTask) a;
                processSelectedKey(k, task);
            }
            if (i.hasNext()) {
                if (this.needsToSelectAgain) {
                    selectAgain();
                    Set<SelectionKey> selectedKeys2 = this.selector.selectedKeys();
                    if (!selectedKeys2.isEmpty()) {
                        i = selectedKeys2.iterator();
                    } else {
                        return;
                    }
                }
            } else {
                return;
            }
        }
    }

    private void processSelectedKeysOptimized() {
        int i = 0;
        while (i < this.selectedKeys.size) {
            SelectionKey k = this.selectedKeys.keys[i];
            this.selectedKeys.keys[i] = null;
            Object a = k.attachment();
            if (a instanceof AbstractNioChannel) {
                processSelectedKey(k, (AbstractNioChannel) a);
            } else {
                NioTask<SelectableChannel> task = (NioTask) a;
                processSelectedKey(k, task);
            }
            if (this.needsToSelectAgain) {
                this.selectedKeys.reset(i + 1);
                selectAgain();
                i = -1;
            }
            i++;
        }
    }

    private void processSelectedKey(SelectionKey k, AbstractNioChannel ch) {
        AbstractNioChannel.NioUnsafe unsafe = ch.unsafe();
        if (!k.isValid()) {
            try {
                EventLoop eventLoop = ch.eventLoop();
                if (eventLoop == this) {
                    unsafe.close(unsafe.voidPromise());
                    return;
                }
                return;
            } catch (Throwable th) {
                return;
            }
        }
        try {
            int readyOps = k.readyOps();
            if ((readyOps & 8) != 0) {
                int ops = k.interestOps();
                k.interestOps(ops & (-9));
                unsafe.finishConnect();
            }
            int ops2 = readyOps & 4;
            if (ops2 != 0) {
                unsafe.forceFlush();
            }
            if ((readyOps & 17) != 0 || readyOps == 0) {
                unsafe.read();
            }
        } catch (CancelledKeyException e) {
            unsafe.close(unsafe.voidPromise());
        }
    }

    private static void processSelectedKey(SelectionKey k, NioTask<SelectableChannel> task) {
        int state = 0;
        try {
            try {
                task.channelReady(k.channel(), k);
            } catch (Exception e) {
                k.cancel();
                invokeChannelUnregistered(task, k, e);
                switch (2) {
                    case 0:
                        break;
                    case 1:
                        if (k.isValid()) {
                            return;
                        }
                        break;
                    default:
                        return;
                }
            }
            switch (1) {
                case 0:
                    k.cancel();
                    invokeChannelUnregistered(task, k, null);
                    return;
                case 1:
                    if (k.isValid()) {
                        return;
                    }
                    break;
                default:
                    return;
            }
            invokeChannelUnregistered(task, k, null);
        } catch (Throwable th) {
            switch (state) {
                case 0:
                    k.cancel();
                    invokeChannelUnregistered(task, k, null);
                    break;
                case 1:
                    if (!k.isValid()) {
                        invokeChannelUnregistered(task, k, null);
                        break;
                    }
                    break;
            }
            throw th;
        }
    }

    private void closeAll() {
        selectAgain();
        Set<SelectionKey> keys = this.selector.keys();
        Collection<AbstractNioChannel> channels = new ArrayList<>(keys.size());
        for (SelectionKey k : keys) {
            Object a = k.attachment();
            if (a instanceof AbstractNioChannel) {
                channels.add((AbstractNioChannel) a);
            } else {
                k.cancel();
                NioTask<SelectableChannel> task = (NioTask) a;
                invokeChannelUnregistered(task, k, null);
            }
        }
        for (AbstractNioChannel ch : channels) {
            ch.unsafe().close(ch.unsafe().voidPromise());
        }
    }

    private static void invokeChannelUnregistered(NioTask<SelectableChannel> task, SelectionKey k, Throwable cause) {
        try {
            task.channelUnregistered(k.channel(), cause);
        } catch (Exception e) {
            logger.warn("Unexpected exception while running NioTask.channelUnregistered()", (Throwable) e);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.util.concurrent.SingleThreadEventExecutor
    public void wakeup(boolean inEventLoop) {
        if (!inEventLoop && this.nextWakeupNanos.getAndSet(-1L) != -1) {
            this.selector.wakeup();
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
    public Selector unwrappedSelector() {
        return this.unwrappedSelector;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int selectNow() throws IOException {
        return this.selector.selectNow();
    }

    private int select(long deadlineNanos) throws IOException {
        if (deadlineNanos == Long.MAX_VALUE) {
            return this.selector.select();
        }
        long timeoutMillis = deadlineToDelayNanos(995000 + deadlineNanos) / 1000000;
        return timeoutMillis <= 0 ? this.selector.selectNow() : this.selector.select(timeoutMillis);
    }

    private void selectAgain() {
        this.needsToSelectAgain = false;
        try {
            this.selector.selectNow();
        } catch (Throwable t) {
            logger.warn("Failed to update SelectionKeys.", t);
        }
    }
}
