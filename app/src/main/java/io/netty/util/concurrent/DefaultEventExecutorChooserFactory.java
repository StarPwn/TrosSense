package io.netty.util.concurrent;

import io.netty.util.concurrent.EventExecutorChooserFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/* loaded from: classes4.dex */
public final class DefaultEventExecutorChooserFactory implements EventExecutorChooserFactory {
    public static final DefaultEventExecutorChooserFactory INSTANCE = new DefaultEventExecutorChooserFactory();

    private DefaultEventExecutorChooserFactory() {
    }

    @Override // io.netty.util.concurrent.EventExecutorChooserFactory
    public EventExecutorChooserFactory.EventExecutorChooser newChooser(EventExecutor[] executors) {
        if (isPowerOfTwo(executors.length)) {
            return new PowerOfTwoEventExecutorChooser(executors);
        }
        return new GenericEventExecutorChooser(executors);
    }

    private static boolean isPowerOfTwo(int val) {
        return ((-val) & val) == val;
    }

    /* loaded from: classes4.dex */
    private static final class PowerOfTwoEventExecutorChooser implements EventExecutorChooserFactory.EventExecutorChooser {
        private final EventExecutor[] executors;
        private final AtomicInteger idx = new AtomicInteger();

        PowerOfTwoEventExecutorChooser(EventExecutor[] executors) {
            this.executors = executors;
        }

        @Override // io.netty.util.concurrent.EventExecutorChooserFactory.EventExecutorChooser
        public EventExecutor next() {
            return this.executors[this.idx.getAndIncrement() & (this.executors.length - 1)];
        }
    }

    /* loaded from: classes4.dex */
    private static final class GenericEventExecutorChooser implements EventExecutorChooserFactory.EventExecutorChooser {
        private final EventExecutor[] executors;
        private final AtomicLong idx = new AtomicLong();

        GenericEventExecutorChooser(EventExecutor[] executors) {
            this.executors = executors;
        }

        @Override // io.netty.util.concurrent.EventExecutorChooserFactory.EventExecutorChooser
        public EventExecutor next() {
            return this.executors[(int) Math.abs(this.idx.getAndIncrement() % this.executors.length)];
        }
    }
}
