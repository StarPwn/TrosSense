package io.netty.channel;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.util.internal.ObjectUtil;

/* loaded from: classes4.dex */
public final class PreferHeapByteBufAllocator implements ByteBufAllocator {
    private final ByteBufAllocator allocator;

    public PreferHeapByteBufAllocator(ByteBufAllocator allocator) {
        this.allocator = (ByteBufAllocator) ObjectUtil.checkNotNull(allocator, "allocator");
    }

    @Override // io.netty.buffer.ByteBufAllocator
    public ByteBuf buffer() {
        return this.allocator.heapBuffer();
    }

    @Override // io.netty.buffer.ByteBufAllocator
    public ByteBuf buffer(int initialCapacity) {
        return this.allocator.heapBuffer(initialCapacity);
    }

    @Override // io.netty.buffer.ByteBufAllocator
    public ByteBuf buffer(int initialCapacity, int maxCapacity) {
        return this.allocator.heapBuffer(initialCapacity, maxCapacity);
    }

    @Override // io.netty.buffer.ByteBufAllocator
    public ByteBuf ioBuffer() {
        return this.allocator.heapBuffer();
    }

    @Override // io.netty.buffer.ByteBufAllocator
    public ByteBuf ioBuffer(int initialCapacity) {
        return this.allocator.heapBuffer(initialCapacity);
    }

    @Override // io.netty.buffer.ByteBufAllocator
    public ByteBuf ioBuffer(int initialCapacity, int maxCapacity) {
        return this.allocator.heapBuffer(initialCapacity, maxCapacity);
    }

    @Override // io.netty.buffer.ByteBufAllocator
    public ByteBuf heapBuffer() {
        return this.allocator.heapBuffer();
    }

    @Override // io.netty.buffer.ByteBufAllocator
    public ByteBuf heapBuffer(int initialCapacity) {
        return this.allocator.heapBuffer(initialCapacity);
    }

    @Override // io.netty.buffer.ByteBufAllocator
    public ByteBuf heapBuffer(int initialCapacity, int maxCapacity) {
        return this.allocator.heapBuffer(initialCapacity, maxCapacity);
    }

    @Override // io.netty.buffer.ByteBufAllocator
    public ByteBuf directBuffer() {
        return this.allocator.directBuffer();
    }

    @Override // io.netty.buffer.ByteBufAllocator
    public ByteBuf directBuffer(int initialCapacity) {
        return this.allocator.directBuffer(initialCapacity);
    }

    @Override // io.netty.buffer.ByteBufAllocator
    public ByteBuf directBuffer(int initialCapacity, int maxCapacity) {
        return this.allocator.directBuffer(initialCapacity, maxCapacity);
    }

    @Override // io.netty.buffer.ByteBufAllocator
    public CompositeByteBuf compositeBuffer() {
        return this.allocator.compositeHeapBuffer();
    }

    @Override // io.netty.buffer.ByteBufAllocator
    public CompositeByteBuf compositeBuffer(int maxNumComponents) {
        return this.allocator.compositeHeapBuffer(maxNumComponents);
    }

    @Override // io.netty.buffer.ByteBufAllocator
    public CompositeByteBuf compositeHeapBuffer() {
        return this.allocator.compositeHeapBuffer();
    }

    @Override // io.netty.buffer.ByteBufAllocator
    public CompositeByteBuf compositeHeapBuffer(int maxNumComponents) {
        return this.allocator.compositeHeapBuffer(maxNumComponents);
    }

    @Override // io.netty.buffer.ByteBufAllocator
    public CompositeByteBuf compositeDirectBuffer() {
        return this.allocator.compositeDirectBuffer();
    }

    @Override // io.netty.buffer.ByteBufAllocator
    public CompositeByteBuf compositeDirectBuffer(int maxNumComponents) {
        return this.allocator.compositeDirectBuffer(maxNumComponents);
    }

    @Override // io.netty.buffer.ByteBufAllocator
    public boolean isDirectBufferPooled() {
        return this.allocator.isDirectBufferPooled();
    }

    @Override // io.netty.buffer.ByteBufAllocator
    public int calculateNewCapacity(int minNewCapacity, int maxCapacity) {
        return this.allocator.calculateNewCapacity(minNewCapacity, maxCapacity);
    }
}
