package io.netty.buffer;

import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import java.nio.ByteBuffer;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public final class ReadOnlyUnsafeDirectByteBuf extends ReadOnlyByteBufferBuf {
    private final long memoryAddress;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ReadOnlyUnsafeDirectByteBuf(ByteBufAllocator allocator, ByteBuffer byteBuffer) {
        super(allocator, byteBuffer);
        this.memoryAddress = PlatformDependent.directBufferAddress(this.buffer);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.ReadOnlyByteBufferBuf, io.netty.buffer.AbstractByteBuf
    public byte _getByte(int index) {
        return UnsafeByteBufUtil.getByte(addr(index));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.ReadOnlyByteBufferBuf, io.netty.buffer.AbstractByteBuf
    public short _getShort(int index) {
        return UnsafeByteBufUtil.getShort(addr(index));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.ReadOnlyByteBufferBuf, io.netty.buffer.AbstractByteBuf
    public int _getUnsignedMedium(int index) {
        return UnsafeByteBufUtil.getUnsignedMedium(addr(index));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.ReadOnlyByteBufferBuf, io.netty.buffer.AbstractByteBuf
    public int _getInt(int index) {
        return UnsafeByteBufUtil.getInt(addr(index));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.ReadOnlyByteBufferBuf, io.netty.buffer.AbstractByteBuf
    public long _getLong(int index) {
        return UnsafeByteBufUtil.getLong(addr(index));
    }

    @Override // io.netty.buffer.ReadOnlyByteBufferBuf, io.netty.buffer.ByteBuf
    public ByteBuf getBytes(int index, ByteBuf dst, int dstIndex, int length) {
        checkIndex(index, length);
        ObjectUtil.checkNotNull(dst, "dst");
        if (dstIndex < 0 || dstIndex > dst.capacity() - length) {
            throw new IndexOutOfBoundsException("dstIndex: " + dstIndex);
        }
        if (dst.hasMemoryAddress()) {
            PlatformDependent.copyMemory(addr(index), dst.memoryAddress() + dstIndex, length);
        } else if (dst.hasArray()) {
            PlatformDependent.copyMemory(addr(index), dst.array(), dst.arrayOffset() + dstIndex, length);
        } else {
            dst.setBytes(dstIndex, this, index, length);
        }
        return this;
    }

    @Override // io.netty.buffer.ReadOnlyByteBufferBuf, io.netty.buffer.ByteBuf
    public ByteBuf getBytes(int index, byte[] dst, int dstIndex, int length) {
        checkIndex(index, length);
        ObjectUtil.checkNotNull(dst, "dst");
        if (dstIndex < 0 || dstIndex > dst.length - length) {
            throw new IndexOutOfBoundsException(String.format("dstIndex: %d, length: %d (expected: range(0, %d))", Integer.valueOf(dstIndex), Integer.valueOf(length), Integer.valueOf(dst.length)));
        }
        if (length != 0) {
            PlatformDependent.copyMemory(addr(index), dst, dstIndex, length);
        }
        return this;
    }

    @Override // io.netty.buffer.ReadOnlyByteBufferBuf, io.netty.buffer.ByteBuf
    public ByteBuf copy(int index, int length) {
        checkIndex(index, length);
        ByteBuf copy = alloc().directBuffer(length, maxCapacity());
        if (length != 0) {
            if (copy.hasMemoryAddress()) {
                PlatformDependent.copyMemory(addr(index), copy.memoryAddress(), length);
                copy.setIndex(0, length);
            } else {
                copy.writeBytes(this, index, length);
            }
        }
        return copy;
    }

    @Override // io.netty.buffer.ReadOnlyByteBufferBuf, io.netty.buffer.ByteBuf
    public boolean hasMemoryAddress() {
        return true;
    }

    @Override // io.netty.buffer.ReadOnlyByteBufferBuf, io.netty.buffer.ByteBuf
    public long memoryAddress() {
        return this.memoryAddress;
    }

    private long addr(int index) {
        return this.memoryAddress + index;
    }
}
