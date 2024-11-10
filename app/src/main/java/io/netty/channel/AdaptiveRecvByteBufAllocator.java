package io.netty.channel;

import io.netty.channel.DefaultMaxMessagesRecvByteBufAllocator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.util.internal.ObjectUtil;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes4.dex */
public class AdaptiveRecvByteBufAllocator extends DefaultMaxMessagesRecvByteBufAllocator {

    @Deprecated
    public static final AdaptiveRecvByteBufAllocator DEFAULT;
    static final int DEFAULT_INITIAL = 2048;
    static final int DEFAULT_MAXIMUM = 65536;
    static final int DEFAULT_MINIMUM = 64;
    private static final int INDEX_DECREMENT = 1;
    private static final int INDEX_INCREMENT = 4;
    private static final int[] SIZE_TABLE;
    private final int initialIndex;
    private final int maxCapacity;
    private final int maxIndex;
    private final int minCapacity;
    private final int minIndex;

    static {
        List<Integer> sizeTable = new ArrayList<>();
        for (int i = 16; i < 512; i += 16) {
            sizeTable.add(Integer.valueOf(i));
        }
        for (int i2 = 512; i2 > 0; i2 <<= 1) {
            sizeTable.add(Integer.valueOf(i2));
        }
        int i3 = sizeTable.size();
        SIZE_TABLE = new int[i3];
        for (int i4 = 0; i4 < SIZE_TABLE.length; i4++) {
            SIZE_TABLE[i4] = sizeTable.get(i4).intValue();
        }
        DEFAULT = new AdaptiveRecvByteBufAllocator();
    }

    private static int getSizeTableIndex(int size) {
        int low = 0;
        int high = SIZE_TABLE.length - 1;
        while (high >= low) {
            if (high == low) {
                return high;
            }
            int mid = (low + high) >>> 1;
            int a = SIZE_TABLE[mid];
            int b = SIZE_TABLE[mid + 1];
            if (size > b) {
                low = mid + 1;
            } else if (size < a) {
                high = mid - 1;
            } else {
                if (size == a) {
                    return mid;
                }
                return mid + 1;
            }
        }
        return low;
    }

    /* loaded from: classes4.dex */
    private final class HandleImpl extends DefaultMaxMessagesRecvByteBufAllocator.MaxMessageHandle {
        private boolean decreaseNow;
        private int index;
        private final int maxCapacity;
        private final int maxIndex;
        private final int minCapacity;
        private final int minIndex;
        private int nextReceiveBufferSize;

        HandleImpl(int minIndex, int maxIndex, int initialIndex, int minCapacity, int maxCapacity) {
            super();
            this.minIndex = minIndex;
            this.maxIndex = maxIndex;
            this.index = initialIndex;
            this.nextReceiveBufferSize = Math.max(AdaptiveRecvByteBufAllocator.SIZE_TABLE[this.index], minCapacity);
            this.minCapacity = minCapacity;
            this.maxCapacity = maxCapacity;
        }

        @Override // io.netty.channel.DefaultMaxMessagesRecvByteBufAllocator.MaxMessageHandle, io.netty.channel.RecvByteBufAllocator.Handle
        public void lastBytesRead(int bytes) {
            if (bytes == attemptedBytesRead()) {
                record(bytes);
            }
            super.lastBytesRead(bytes);
        }

        @Override // io.netty.channel.RecvByteBufAllocator.Handle
        public int guess() {
            return this.nextReceiveBufferSize;
        }

        private void record(int actualReadBytes) {
            if (actualReadBytes <= AdaptiveRecvByteBufAllocator.SIZE_TABLE[Math.max(0, this.index - 1)]) {
                if (this.decreaseNow) {
                    this.index = Math.max(this.index - 1, this.minIndex);
                    this.nextReceiveBufferSize = Math.max(AdaptiveRecvByteBufAllocator.SIZE_TABLE[this.index], this.minCapacity);
                    this.decreaseNow = false;
                    return;
                }
                this.decreaseNow = true;
                return;
            }
            if (actualReadBytes >= this.nextReceiveBufferSize) {
                this.index = Math.min(this.index + 4, this.maxIndex);
                this.nextReceiveBufferSize = Math.min(AdaptiveRecvByteBufAllocator.SIZE_TABLE[this.index], this.maxCapacity);
                this.decreaseNow = false;
            }
        }

        @Override // io.netty.channel.DefaultMaxMessagesRecvByteBufAllocator.MaxMessageHandle, io.netty.channel.RecvByteBufAllocator.Handle
        public void readComplete() {
            record(totalBytesRead());
        }
    }

    public AdaptiveRecvByteBufAllocator() {
        this(64, 2048, 65536);
    }

    public AdaptiveRecvByteBufAllocator(int minimum, int initial, int maximum) {
        ObjectUtil.checkPositive(minimum, "minimum");
        if (initial < minimum) {
            throw new IllegalArgumentException("initial: " + initial);
        }
        if (maximum < initial) {
            throw new IllegalArgumentException("maximum: " + maximum);
        }
        int minIndex = getSizeTableIndex(minimum);
        if (SIZE_TABLE[minIndex] < minimum) {
            this.minIndex = minIndex + 1;
        } else {
            this.minIndex = minIndex;
        }
        int maxIndex = getSizeTableIndex(maximum);
        if (SIZE_TABLE[maxIndex] > maximum) {
            this.maxIndex = maxIndex - 1;
        } else {
            this.maxIndex = maxIndex;
        }
        int initialIndex = getSizeTableIndex(initial);
        if (SIZE_TABLE[initialIndex] > initial) {
            this.initialIndex = initialIndex - 1;
        } else {
            this.initialIndex = initialIndex;
        }
        this.minCapacity = minimum;
        this.maxCapacity = maximum;
    }

    @Override // io.netty.channel.RecvByteBufAllocator
    public RecvByteBufAllocator.Handle newHandle() {
        return new HandleImpl(this.minIndex, this.maxIndex, this.initialIndex, this.minCapacity, this.maxCapacity);
    }

    @Override // io.netty.channel.DefaultMaxMessagesRecvByteBufAllocator
    public AdaptiveRecvByteBufAllocator respectMaybeMoreData(boolean respectMaybeMoreData) {
        super.respectMaybeMoreData(respectMaybeMoreData);
        return this;
    }
}
