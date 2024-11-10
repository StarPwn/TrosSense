package io.netty.channel;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.util.UncheckedBooleanSupplier;
import io.netty.util.internal.ObjectUtil;

/* loaded from: classes4.dex */
public abstract class DefaultMaxMessagesRecvByteBufAllocator implements MaxMessagesRecvByteBufAllocator {
    private final boolean ignoreBytesRead;
    private volatile int maxMessagesPerRead;
    private volatile boolean respectMaybeMoreData;

    public DefaultMaxMessagesRecvByteBufAllocator() {
        this(1);
    }

    public DefaultMaxMessagesRecvByteBufAllocator(int maxMessagesPerRead) {
        this(maxMessagesPerRead, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DefaultMaxMessagesRecvByteBufAllocator(int maxMessagesPerRead, boolean ignoreBytesRead) {
        this.respectMaybeMoreData = true;
        this.ignoreBytesRead = ignoreBytesRead;
        maxMessagesPerRead(maxMessagesPerRead);
    }

    @Override // io.netty.channel.MaxMessagesRecvByteBufAllocator
    public int maxMessagesPerRead() {
        return this.maxMessagesPerRead;
    }

    @Override // io.netty.channel.MaxMessagesRecvByteBufAllocator
    public MaxMessagesRecvByteBufAllocator maxMessagesPerRead(int maxMessagesPerRead) {
        ObjectUtil.checkPositive(maxMessagesPerRead, "maxMessagesPerRead");
        this.maxMessagesPerRead = maxMessagesPerRead;
        return this;
    }

    public DefaultMaxMessagesRecvByteBufAllocator respectMaybeMoreData(boolean respectMaybeMoreData) {
        this.respectMaybeMoreData = respectMaybeMoreData;
        return this;
    }

    public final boolean respectMaybeMoreData() {
        return this.respectMaybeMoreData;
    }

    /* loaded from: classes4.dex */
    public abstract class MaxMessageHandle implements RecvByteBufAllocator.ExtendedHandle {
        private int attemptedBytesRead;
        private ChannelConfig config;
        private final UncheckedBooleanSupplier defaultMaybeMoreSupplier = new UncheckedBooleanSupplier() { // from class: io.netty.channel.DefaultMaxMessagesRecvByteBufAllocator.MaxMessageHandle.1
            @Override // io.netty.util.UncheckedBooleanSupplier, io.netty.util.BooleanSupplier
            public boolean get() {
                return MaxMessageHandle.this.attemptedBytesRead == MaxMessageHandle.this.lastBytesRead;
            }
        };
        private int lastBytesRead;
        private int maxMessagePerRead;
        private final boolean respectMaybeMoreData;
        private int totalBytesRead;
        private int totalMessages;

        public MaxMessageHandle() {
            this.respectMaybeMoreData = DefaultMaxMessagesRecvByteBufAllocator.this.respectMaybeMoreData;
        }

        @Override // io.netty.channel.RecvByteBufAllocator.Handle
        public void reset(ChannelConfig config) {
            this.config = config;
            this.maxMessagePerRead = DefaultMaxMessagesRecvByteBufAllocator.this.maxMessagesPerRead();
            this.totalBytesRead = 0;
            this.totalMessages = 0;
        }

        @Override // io.netty.channel.RecvByteBufAllocator.Handle
        public ByteBuf allocate(ByteBufAllocator alloc) {
            return alloc.ioBuffer(guess());
        }

        @Override // io.netty.channel.RecvByteBufAllocator.Handle
        public final void incMessagesRead(int amt) {
            this.totalMessages += amt;
        }

        @Override // io.netty.channel.RecvByteBufAllocator.Handle
        public void lastBytesRead(int bytes) {
            this.lastBytesRead = bytes;
            if (bytes > 0) {
                this.totalBytesRead += bytes;
            }
        }

        @Override // io.netty.channel.RecvByteBufAllocator.Handle
        public final int lastBytesRead() {
            return this.lastBytesRead;
        }

        @Override // io.netty.channel.RecvByteBufAllocator.Handle
        public boolean continueReading() {
            return continueReading(this.defaultMaybeMoreSupplier);
        }

        @Override // io.netty.channel.RecvByteBufAllocator.ExtendedHandle
        public boolean continueReading(UncheckedBooleanSupplier maybeMoreDataSupplier) {
            return this.config.isAutoRead() && (!this.respectMaybeMoreData || maybeMoreDataSupplier.get()) && this.totalMessages < this.maxMessagePerRead && (DefaultMaxMessagesRecvByteBufAllocator.this.ignoreBytesRead || this.totalBytesRead > 0);
        }

        @Override // io.netty.channel.RecvByteBufAllocator.Handle
        public void readComplete() {
        }

        @Override // io.netty.channel.RecvByteBufAllocator.Handle
        public int attemptedBytesRead() {
            return this.attemptedBytesRead;
        }

        @Override // io.netty.channel.RecvByteBufAllocator.Handle
        public void attemptedBytesRead(int bytes) {
            this.attemptedBytesRead = bytes;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public final int totalBytesRead() {
            if (this.totalBytesRead < 0) {
                return Integer.MAX_VALUE;
            }
            return this.totalBytesRead;
        }
    }
}
