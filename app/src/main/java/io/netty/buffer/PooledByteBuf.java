package io.netty.buffer;

import io.netty.util.Recycler;
import io.netty.util.internal.ObjectPool;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.FileChannel;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public abstract class PooledByteBuf<T> extends AbstractReferenceCountedByteBuf {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private ByteBufAllocator allocator;
    PoolThreadCache cache;
    protected PoolChunk<T> chunk;
    protected long handle;
    protected int length;
    int maxLength;
    protected T memory;
    protected int offset;
    private final Recycler.EnhancedHandle<PooledByteBuf<T>> recyclerHandle;
    ByteBuffer tmpNioBuf;

    protected abstract ByteBuffer newInternalNioBuffer(T t);

    /* JADX INFO: Access modifiers changed from: protected */
    public PooledByteBuf(ObjectPool.Handle<? extends PooledByteBuf<T>> recyclerHandle, int maxCapacity) {
        super(maxCapacity);
        this.recyclerHandle = (Recycler.EnhancedHandle) recyclerHandle;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void init(PoolChunk<T> chunk, ByteBuffer nioBuffer, long handle, int offset, int length, int maxLength, PoolThreadCache cache) {
        init0(chunk, nioBuffer, handle, offset, length, maxLength, cache);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void initUnpooled(PoolChunk<T> chunk, int length) {
        init0(chunk, null, 0L, 0, length, length, null);
    }

    private void init0(PoolChunk<T> chunk, ByteBuffer nioBuffer, long handle, int offset, int length, int maxLength, PoolThreadCache cache) {
        if (handle < 0) {
            throw new AssertionError();
        }
        if (chunk == null) {
            throw new AssertionError();
        }
        if (PoolChunk.isSubpage(handle) && chunk.arena.sizeClass.size2SizeIdx(maxLength) > chunk.arena.sizeClass.smallMaxSizeIdx) {
            throw new AssertionError("Allocated small sub-page handle for a buffer size that isn't \"small.\"");
        }
        chunk.incrementPinnedMemory(maxLength);
        this.chunk = chunk;
        this.memory = chunk.memory;
        this.tmpNioBuf = nioBuffer;
        this.allocator = chunk.arena.parent;
        this.cache = cache;
        this.handle = handle;
        this.offset = offset;
        this.length = length;
        this.maxLength = maxLength;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void reuse(int maxCapacity) {
        maxCapacity(maxCapacity);
        resetRefCnt();
        setIndex0(0, 0);
        discardMarks();
    }

    @Override // io.netty.buffer.ByteBuf
    public final int capacity() {
        return this.length;
    }

    @Override // io.netty.buffer.ByteBuf
    public int maxFastWritableBytes() {
        return Math.min(this.maxLength, maxCapacity()) - this.writerIndex;
    }

    @Override // io.netty.buffer.ByteBuf
    public final ByteBuf capacity(int newCapacity) {
        if (newCapacity == this.length) {
            ensureAccessible();
            return this;
        }
        checkNewCapacity(newCapacity);
        if (!this.chunk.unpooled) {
            if (newCapacity > this.length) {
                if (newCapacity <= this.maxLength) {
                    this.length = newCapacity;
                    return this;
                }
            } else if (newCapacity > (this.maxLength >>> 1) && (this.maxLength > 512 || newCapacity > this.maxLength - 16)) {
                this.length = newCapacity;
                trimIndicesToCapacity(newCapacity);
                return this;
            }
        }
        this.chunk.arena.reallocate(this, newCapacity);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public final ByteBufAllocator alloc() {
        return this.allocator;
    }

    @Override // io.netty.buffer.ByteBuf
    public final ByteOrder order() {
        return ByteOrder.BIG_ENDIAN;
    }

    @Override // io.netty.buffer.ByteBuf
    public final ByteBuf unwrap() {
        return null;
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public final ByteBuf retainedDuplicate() {
        return PooledDuplicatedByteBuf.newInstance(this, this, readerIndex(), writerIndex());
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public final ByteBuf retainedSlice() {
        int index = readerIndex();
        return retainedSlice(index, writerIndex() - index);
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public final ByteBuf retainedSlice(int index, int length) {
        return PooledSlicedByteBuf.newInstance(this, this, index, length);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final ByteBuffer internalNioBuffer() {
        ByteBuffer tmpNioBuf = this.tmpNioBuf;
        if (tmpNioBuf == null) {
            ByteBuffer tmpNioBuf2 = newInternalNioBuffer(this.memory);
            this.tmpNioBuf = tmpNioBuf2;
            return tmpNioBuf2;
        }
        tmpNioBuf.clear();
        return tmpNioBuf;
    }

    @Override // io.netty.buffer.AbstractReferenceCountedByteBuf
    protected final void deallocate() {
        if (this.handle >= 0) {
            long handle = this.handle;
            this.handle = -1L;
            this.memory = null;
            this.chunk.arena.free(this.chunk, this.tmpNioBuf, handle, this.maxLength, this.cache);
            this.tmpNioBuf = null;
            this.chunk = null;
            this.cache = null;
            this.recyclerHandle.unguardedRecycle(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final int idx(int index) {
        return this.offset + index;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final ByteBuffer _internalNioBuffer(int index, int length, boolean duplicate) {
        int index2 = idx(index);
        ByteBuffer buffer = duplicate ? newInternalNioBuffer(this.memory) : internalNioBuffer();
        buffer.limit(index2 + length).position(index2);
        return buffer;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ByteBuffer duplicateInternalNioBuffer(int index, int length) {
        checkIndex(index, length);
        return _internalNioBuffer(index, length, true);
    }

    @Override // io.netty.buffer.ByteBuf
    public final ByteBuffer internalNioBuffer(int index, int length) {
        checkIndex(index, length);
        return _internalNioBuffer(index, length, false);
    }

    @Override // io.netty.buffer.ByteBuf
    public final int nioBufferCount() {
        return 1;
    }

    @Override // io.netty.buffer.ByteBuf
    public final ByteBuffer nioBuffer(int index, int length) {
        return duplicateInternalNioBuffer(index, length).slice();
    }

    @Override // io.netty.buffer.ByteBuf
    public final ByteBuffer[] nioBuffers(int index, int length) {
        return new ByteBuffer[]{nioBuffer(index, length)};
    }

    @Override // io.netty.buffer.ByteBuf
    public final boolean isContiguous() {
        return true;
    }

    @Override // io.netty.buffer.ByteBuf
    public final int getBytes(int index, GatheringByteChannel out, int length) throws IOException {
        return out.write(duplicateInternalNioBuffer(index, length));
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public final int readBytes(GatheringByteChannel out, int length) throws IOException {
        checkReadableBytes(length);
        int readBytes = out.write(_internalNioBuffer(this.readerIndex, length, false));
        this.readerIndex += readBytes;
        return readBytes;
    }

    @Override // io.netty.buffer.ByteBuf
    public final int getBytes(int index, FileChannel out, long position, int length) throws IOException {
        return out.write(duplicateInternalNioBuffer(index, length), position);
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public final int readBytes(FileChannel out, long position, int length) throws IOException {
        checkReadableBytes(length);
        int readBytes = out.write(_internalNioBuffer(this.readerIndex, length, false), position);
        this.readerIndex += readBytes;
        return readBytes;
    }

    @Override // io.netty.buffer.ByteBuf
    public final int setBytes(int index, ScatteringByteChannel in, int length) throws IOException {
        try {
            return in.read(internalNioBuffer(index, length));
        } catch (ClosedChannelException e) {
            return -1;
        }
    }

    @Override // io.netty.buffer.ByteBuf
    public final int setBytes(int index, FileChannel in, long position, int length) throws IOException {
        try {
            return in.read(internalNioBuffer(index, length), position);
        } catch (ClosedChannelException e) {
            return -1;
        }
    }
}
