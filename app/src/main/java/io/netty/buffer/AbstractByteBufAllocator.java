package io.netty.buffer;

import io.netty.util.ResourceLeakDetector;
import io.netty.util.ResourceLeakTracker;
import io.netty.util.internal.MathUtil;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;

/* loaded from: classes4.dex */
public abstract class AbstractByteBufAllocator implements ByteBufAllocator {
    static final int CALCULATE_THRESHOLD = 4194304;
    static final int DEFAULT_INITIAL_CAPACITY = 256;
    static final int DEFAULT_MAX_CAPACITY = Integer.MAX_VALUE;
    static final int DEFAULT_MAX_COMPONENTS = 16;
    private final boolean directByDefault;
    private final ByteBuf emptyBuf;

    protected abstract ByteBuf newDirectBuffer(int i, int i2);

    protected abstract ByteBuf newHeapBuffer(int i, int i2);

    static {
        ResourceLeakDetector.addExclusions(AbstractByteBufAllocator.class, "toLeakAwareBuffer");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static ByteBuf toLeakAwareBuffer(ByteBuf buf) {
        switch (ResourceLeakDetector.getLevel()) {
            case SIMPLE:
                ResourceLeakTracker<ByteBuf> leak = AbstractByteBuf.leakDetector.track(buf);
                if (leak != null) {
                    return new SimpleLeakAwareByteBuf(buf, leak);
                }
                return buf;
            case ADVANCED:
            case PARANOID:
                ResourceLeakTracker<ByteBuf> leak2 = AbstractByteBuf.leakDetector.track(buf);
                if (leak2 != null) {
                    return new AdvancedLeakAwareByteBuf(buf, leak2);
                }
                return buf;
            default:
                return buf;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static CompositeByteBuf toLeakAwareBuffer(CompositeByteBuf buf) {
        switch (ResourceLeakDetector.getLevel()) {
            case SIMPLE:
                ResourceLeakTracker<ByteBuf> leak = AbstractByteBuf.leakDetector.track(buf);
                if (leak != null) {
                    return new SimpleLeakAwareCompositeByteBuf(buf, leak);
                }
                return buf;
            case ADVANCED:
            case PARANOID:
                ResourceLeakTracker<ByteBuf> leak2 = AbstractByteBuf.leakDetector.track(buf);
                if (leak2 != null) {
                    return new AdvancedLeakAwareCompositeByteBuf(buf, leak2);
                }
                return buf;
            default:
                return buf;
        }
    }

    protected AbstractByteBufAllocator() {
        this(false);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractByteBufAllocator(boolean preferDirect) {
        this.directByDefault = preferDirect && PlatformDependent.hasUnsafe();
        this.emptyBuf = new EmptyByteBuf(this);
    }

    @Override // io.netty.buffer.ByteBufAllocator
    public ByteBuf buffer() {
        if (this.directByDefault) {
            return directBuffer();
        }
        return heapBuffer();
    }

    @Override // io.netty.buffer.ByteBufAllocator
    public ByteBuf buffer(int initialCapacity) {
        if (this.directByDefault) {
            return directBuffer(initialCapacity);
        }
        return heapBuffer(initialCapacity);
    }

    @Override // io.netty.buffer.ByteBufAllocator
    public ByteBuf buffer(int initialCapacity, int maxCapacity) {
        if (this.directByDefault) {
            return directBuffer(initialCapacity, maxCapacity);
        }
        return heapBuffer(initialCapacity, maxCapacity);
    }

    @Override // io.netty.buffer.ByteBufAllocator
    public ByteBuf ioBuffer() {
        if (PlatformDependent.hasUnsafe() || isDirectBufferPooled()) {
            return directBuffer(256);
        }
        return heapBuffer(256);
    }

    @Override // io.netty.buffer.ByteBufAllocator
    public ByteBuf ioBuffer(int initialCapacity) {
        if (PlatformDependent.hasUnsafe() || isDirectBufferPooled()) {
            return directBuffer(initialCapacity);
        }
        return heapBuffer(initialCapacity);
    }

    @Override // io.netty.buffer.ByteBufAllocator
    public ByteBuf ioBuffer(int initialCapacity, int maxCapacity) {
        if (PlatformDependent.hasUnsafe() || isDirectBufferPooled()) {
            return directBuffer(initialCapacity, maxCapacity);
        }
        return heapBuffer(initialCapacity, maxCapacity);
    }

    @Override // io.netty.buffer.ByteBufAllocator
    public ByteBuf heapBuffer() {
        return heapBuffer(256, Integer.MAX_VALUE);
    }

    @Override // io.netty.buffer.ByteBufAllocator
    public ByteBuf heapBuffer(int initialCapacity) {
        return heapBuffer(initialCapacity, Integer.MAX_VALUE);
    }

    @Override // io.netty.buffer.ByteBufAllocator
    public ByteBuf heapBuffer(int initialCapacity, int maxCapacity) {
        if (initialCapacity == 0 && maxCapacity == 0) {
            return this.emptyBuf;
        }
        validate(initialCapacity, maxCapacity);
        return newHeapBuffer(initialCapacity, maxCapacity);
    }

    @Override // io.netty.buffer.ByteBufAllocator
    public ByteBuf directBuffer() {
        return directBuffer(256, Integer.MAX_VALUE);
    }

    @Override // io.netty.buffer.ByteBufAllocator
    public ByteBuf directBuffer(int initialCapacity) {
        return directBuffer(initialCapacity, Integer.MAX_VALUE);
    }

    @Override // io.netty.buffer.ByteBufAllocator
    public ByteBuf directBuffer(int initialCapacity, int maxCapacity) {
        if (initialCapacity == 0 && maxCapacity == 0) {
            return this.emptyBuf;
        }
        validate(initialCapacity, maxCapacity);
        return newDirectBuffer(initialCapacity, maxCapacity);
    }

    @Override // io.netty.buffer.ByteBufAllocator
    public CompositeByteBuf compositeBuffer() {
        if (this.directByDefault) {
            return compositeDirectBuffer();
        }
        return compositeHeapBuffer();
    }

    @Override // io.netty.buffer.ByteBufAllocator
    public CompositeByteBuf compositeBuffer(int maxNumComponents) {
        if (this.directByDefault) {
            return compositeDirectBuffer(maxNumComponents);
        }
        return compositeHeapBuffer(maxNumComponents);
    }

    @Override // io.netty.buffer.ByteBufAllocator
    public CompositeByteBuf compositeHeapBuffer() {
        return compositeHeapBuffer(16);
    }

    @Override // io.netty.buffer.ByteBufAllocator
    public CompositeByteBuf compositeHeapBuffer(int maxNumComponents) {
        return toLeakAwareBuffer(new CompositeByteBuf(this, false, maxNumComponents));
    }

    @Override // io.netty.buffer.ByteBufAllocator
    public CompositeByteBuf compositeDirectBuffer() {
        return compositeDirectBuffer(16);
    }

    @Override // io.netty.buffer.ByteBufAllocator
    public CompositeByteBuf compositeDirectBuffer(int maxNumComponents) {
        return toLeakAwareBuffer(new CompositeByteBuf(this, true, maxNumComponents));
    }

    private static void validate(int initialCapacity, int maxCapacity) {
        ObjectUtil.checkPositiveOrZero(initialCapacity, "initialCapacity");
        if (initialCapacity > maxCapacity) {
            throw new IllegalArgumentException(String.format("initialCapacity: %d (expected: not greater than maxCapacity(%d)", Integer.valueOf(initialCapacity), Integer.valueOf(maxCapacity)));
        }
    }

    public String toString() {
        return StringUtil.simpleClassName(this) + "(directByDefault: " + this.directByDefault + ')';
    }

    @Override // io.netty.buffer.ByteBufAllocator
    public int calculateNewCapacity(int minNewCapacity, int maxCapacity) {
        ObjectUtil.checkPositiveOrZero(minNewCapacity, "minNewCapacity");
        if (minNewCapacity > maxCapacity) {
            throw new IllegalArgumentException(String.format("minNewCapacity: %d (expected: not greater than maxCapacity(%d)", Integer.valueOf(minNewCapacity), Integer.valueOf(maxCapacity)));
        }
        if (minNewCapacity == 4194304) {
            return 4194304;
        }
        if (minNewCapacity > 4194304) {
            int newCapacity = (minNewCapacity / 4194304) * 4194304;
            if (newCapacity > maxCapacity - 4194304) {
                return maxCapacity;
            }
            return 4194304 + newCapacity;
        }
        return Math.min(MathUtil.findNextPositivePowerOfTwo(Math.max(minNewCapacity, 64)), maxCapacity);
    }
}
