package io.netty.channel;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.util.UncheckedBooleanSupplier;
import io.netty.util.internal.ObjectUtil;
import java.util.AbstractMap;
import java.util.Map;

/* loaded from: classes4.dex */
public class DefaultMaxBytesRecvByteBufAllocator implements MaxBytesRecvByteBufAllocator {
    private volatile int maxBytesPerIndividualRead;
    private volatile int maxBytesPerRead;

    /* loaded from: classes4.dex */
    private final class HandleImpl implements RecvByteBufAllocator.ExtendedHandle {
        private int attemptBytesRead;
        private int bytesToRead;
        private final UncheckedBooleanSupplier defaultMaybeMoreSupplier;
        private int individualReadMax;
        private int lastBytesRead;

        private HandleImpl() {
            this.defaultMaybeMoreSupplier = new UncheckedBooleanSupplier() { // from class: io.netty.channel.DefaultMaxBytesRecvByteBufAllocator.HandleImpl.1
                @Override // io.netty.util.UncheckedBooleanSupplier, io.netty.util.BooleanSupplier
                public boolean get() {
                    return HandleImpl.this.attemptBytesRead == HandleImpl.this.lastBytesRead;
                }
            };
        }

        @Override // io.netty.channel.RecvByteBufAllocator.Handle
        public ByteBuf allocate(ByteBufAllocator alloc) {
            return alloc.ioBuffer(guess());
        }

        @Override // io.netty.channel.RecvByteBufAllocator.Handle
        public int guess() {
            return Math.min(this.individualReadMax, this.bytesToRead);
        }

        @Override // io.netty.channel.RecvByteBufAllocator.Handle
        public void reset(ChannelConfig config) {
            this.bytesToRead = DefaultMaxBytesRecvByteBufAllocator.this.maxBytesPerRead();
            this.individualReadMax = DefaultMaxBytesRecvByteBufAllocator.this.maxBytesPerIndividualRead();
        }

        @Override // io.netty.channel.RecvByteBufAllocator.Handle
        public void incMessagesRead(int amt) {
        }

        @Override // io.netty.channel.RecvByteBufAllocator.Handle
        public void lastBytesRead(int bytes) {
            this.lastBytesRead = bytes;
            this.bytesToRead -= bytes;
        }

        @Override // io.netty.channel.RecvByteBufAllocator.Handle
        public int lastBytesRead() {
            return this.lastBytesRead;
        }

        @Override // io.netty.channel.RecvByteBufAllocator.Handle
        public boolean continueReading() {
            return continueReading(this.defaultMaybeMoreSupplier);
        }

        @Override // io.netty.channel.RecvByteBufAllocator.ExtendedHandle
        public boolean continueReading(UncheckedBooleanSupplier maybeMoreDataSupplier) {
            return this.bytesToRead > 0 && maybeMoreDataSupplier.get();
        }

        @Override // io.netty.channel.RecvByteBufAllocator.Handle
        public void readComplete() {
        }

        @Override // io.netty.channel.RecvByteBufAllocator.Handle
        public void attemptedBytesRead(int bytes) {
            this.attemptBytesRead = bytes;
        }

        @Override // io.netty.channel.RecvByteBufAllocator.Handle
        public int attemptedBytesRead() {
            return this.attemptBytesRead;
        }
    }

    public DefaultMaxBytesRecvByteBufAllocator() {
        this(65536, 65536);
    }

    public DefaultMaxBytesRecvByteBufAllocator(int maxBytesPerRead, int maxBytesPerIndividualRead) {
        checkMaxBytesPerReadPair(maxBytesPerRead, maxBytesPerIndividualRead);
        this.maxBytesPerRead = maxBytesPerRead;
        this.maxBytesPerIndividualRead = maxBytesPerIndividualRead;
    }

    @Override // io.netty.channel.RecvByteBufAllocator
    public RecvByteBufAllocator.Handle newHandle() {
        return new HandleImpl();
    }

    @Override // io.netty.channel.MaxBytesRecvByteBufAllocator
    public int maxBytesPerRead() {
        return this.maxBytesPerRead;
    }

    @Override // io.netty.channel.MaxBytesRecvByteBufAllocator
    public DefaultMaxBytesRecvByteBufAllocator maxBytesPerRead(int maxBytesPerRead) {
        ObjectUtil.checkPositive(maxBytesPerRead, "maxBytesPerRead");
        synchronized (this) {
            int maxBytesPerIndividualRead = maxBytesPerIndividualRead();
            if (maxBytesPerRead < maxBytesPerIndividualRead) {
                throw new IllegalArgumentException("maxBytesPerRead cannot be less than maxBytesPerIndividualRead (" + maxBytesPerIndividualRead + "): " + maxBytesPerRead);
            }
            this.maxBytesPerRead = maxBytesPerRead;
        }
        return this;
    }

    @Override // io.netty.channel.MaxBytesRecvByteBufAllocator
    public int maxBytesPerIndividualRead() {
        return this.maxBytesPerIndividualRead;
    }

    @Override // io.netty.channel.MaxBytesRecvByteBufAllocator
    public DefaultMaxBytesRecvByteBufAllocator maxBytesPerIndividualRead(int maxBytesPerIndividualRead) {
        ObjectUtil.checkPositive(maxBytesPerIndividualRead, "maxBytesPerIndividualRead");
        synchronized (this) {
            int maxBytesPerRead = maxBytesPerRead();
            if (maxBytesPerIndividualRead > maxBytesPerRead) {
                throw new IllegalArgumentException("maxBytesPerIndividualRead cannot be greater than maxBytesPerRead (" + maxBytesPerRead + "): " + maxBytesPerIndividualRead);
            }
            this.maxBytesPerIndividualRead = maxBytesPerIndividualRead;
        }
        return this;
    }

    @Override // io.netty.channel.MaxBytesRecvByteBufAllocator
    public synchronized Map.Entry<Integer, Integer> maxBytesPerReadPair() {
        return new AbstractMap.SimpleEntry(Integer.valueOf(this.maxBytesPerRead), Integer.valueOf(this.maxBytesPerIndividualRead));
    }

    private static void checkMaxBytesPerReadPair(int maxBytesPerRead, int maxBytesPerIndividualRead) {
        ObjectUtil.checkPositive(maxBytesPerRead, "maxBytesPerRead");
        ObjectUtil.checkPositive(maxBytesPerIndividualRead, "maxBytesPerIndividualRead");
        if (maxBytesPerRead < maxBytesPerIndividualRead) {
            throw new IllegalArgumentException("maxBytesPerRead cannot be less than maxBytesPerIndividualRead (" + maxBytesPerIndividualRead + "): " + maxBytesPerRead);
        }
    }

    @Override // io.netty.channel.MaxBytesRecvByteBufAllocator
    public DefaultMaxBytesRecvByteBufAllocator maxBytesPerReadPair(int maxBytesPerRead, int maxBytesPerIndividualRead) {
        checkMaxBytesPerReadPair(maxBytesPerRead, maxBytesPerIndividualRead);
        synchronized (this) {
            this.maxBytesPerRead = maxBytesPerRead;
            this.maxBytesPerIndividualRead = maxBytesPerIndividualRead;
        }
        return this;
    }
}
