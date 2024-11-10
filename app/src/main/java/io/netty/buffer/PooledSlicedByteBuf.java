package io.netty.buffer;

import io.netty.util.ByteProcessor;
import io.netty.util.internal.ObjectPool;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public final class PooledSlicedByteBuf extends AbstractPooledDerivedByteBuf {
    private static final ObjectPool<PooledSlicedByteBuf> RECYCLER = ObjectPool.newPool(new ObjectPool.ObjectCreator<PooledSlicedByteBuf>() { // from class: io.netty.buffer.PooledSlicedByteBuf.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // io.netty.util.internal.ObjectPool.ObjectCreator
        public PooledSlicedByteBuf newObject(ObjectPool.Handle<PooledSlicedByteBuf> handle) {
            return new PooledSlicedByteBuf(handle);
        }
    });
    int adjustment;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static PooledSlicedByteBuf newInstance(AbstractByteBuf unwrapped, ByteBuf wrapped, int index, int length) {
        AbstractUnpooledSlicedByteBuf.checkSliceOutOfBounds(index, length, unwrapped);
        return newInstance0(unwrapped, wrapped, index, length);
    }

    private static PooledSlicedByteBuf newInstance0(AbstractByteBuf unwrapped, ByteBuf wrapped, int adjustment, int length) {
        PooledSlicedByteBuf slice = RECYCLER.get();
        slice.init(unwrapped, wrapped, 0, length, length);
        slice.discardMarks();
        slice.adjustment = adjustment;
        return slice;
    }

    private PooledSlicedByteBuf(ObjectPool.Handle<PooledSlicedByteBuf> handle) {
        super(handle);
    }

    @Override // io.netty.buffer.ByteBuf
    public int capacity() {
        return maxCapacity();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf capacity(int newCapacity) {
        throw new UnsupportedOperationException("sliced buffer");
    }

    @Override // io.netty.buffer.ByteBuf
    public int arrayOffset() {
        return idx(unwrap().arrayOffset());
    }

    @Override // io.netty.buffer.ByteBuf
    public long memoryAddress() {
        return unwrap().memoryAddress() + this.adjustment;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuffer nioBuffer(int index, int length) {
        checkIndex0(index, length);
        return unwrap().nioBuffer(idx(index), length);
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuffer[] nioBuffers(int index, int length) {
        checkIndex0(index, length);
        return unwrap().nioBuffers(idx(index), length);
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf copy(int index, int length) {
        checkIndex0(index, length);
        return unwrap().copy(idx(index), length);
    }

    @Override // io.netty.buffer.AbstractPooledDerivedByteBuf, io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf slice(int index, int length) {
        checkIndex0(index, length);
        return super.slice(idx(index), length);
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf retainedSlice(int index, int length) {
        checkIndex0(index, length);
        return newInstance0(unwrap(), this, idx(index), length);
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf duplicate() {
        return duplicate0().setIndex(idx(readerIndex()), idx(writerIndex()));
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf retainedDuplicate() {
        return PooledDuplicatedByteBuf.newInstance(unwrap(), this, idx(readerIndex()), idx(writerIndex()));
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public byte getByte(int index) {
        checkIndex0(index, 1);
        return unwrap().getByte(idx(index));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.AbstractByteBuf
    public byte _getByte(int index) {
        return unwrap()._getByte(idx(index));
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public short getShort(int index) {
        checkIndex0(index, 2);
        return unwrap().getShort(idx(index));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.AbstractByteBuf
    public short _getShort(int index) {
        return unwrap()._getShort(idx(index));
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public short getShortLE(int index) {
        checkIndex0(index, 2);
        return unwrap().getShortLE(idx(index));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.AbstractByteBuf
    public short _getShortLE(int index) {
        return unwrap()._getShortLE(idx(index));
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public int getUnsignedMedium(int index) {
        checkIndex0(index, 3);
        return unwrap().getUnsignedMedium(idx(index));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.AbstractByteBuf
    public int _getUnsignedMedium(int index) {
        return unwrap()._getUnsignedMedium(idx(index));
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public int getUnsignedMediumLE(int index) {
        checkIndex0(index, 3);
        return unwrap().getUnsignedMediumLE(idx(index));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.AbstractByteBuf
    public int _getUnsignedMediumLE(int index) {
        return unwrap()._getUnsignedMediumLE(idx(index));
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public int getInt(int index) {
        checkIndex0(index, 4);
        return unwrap().getInt(idx(index));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.AbstractByteBuf
    public int _getInt(int index) {
        return unwrap()._getInt(idx(index));
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public int getIntLE(int index) {
        checkIndex0(index, 4);
        return unwrap().getIntLE(idx(index));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.AbstractByteBuf
    public int _getIntLE(int index) {
        return unwrap()._getIntLE(idx(index));
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public long getLong(int index) {
        checkIndex0(index, 8);
        return unwrap().getLong(idx(index));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.AbstractByteBuf
    public long _getLong(int index) {
        return unwrap()._getLong(idx(index));
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public long getLongLE(int index) {
        checkIndex0(index, 8);
        return unwrap().getLongLE(idx(index));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.AbstractByteBuf
    public long _getLongLE(int index) {
        return unwrap()._getLongLE(idx(index));
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf getBytes(int index, ByteBuf dst, int dstIndex, int length) {
        checkIndex0(index, length);
        unwrap().getBytes(idx(index), dst, dstIndex, length);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf getBytes(int index, byte[] dst, int dstIndex, int length) {
        checkIndex0(index, length);
        unwrap().getBytes(idx(index), dst, dstIndex, length);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf getBytes(int index, ByteBuffer dst) {
        checkIndex0(index, dst.remaining());
        unwrap().getBytes(idx(index), dst);
        return this;
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf setByte(int index, int value) {
        checkIndex0(index, 1);
        unwrap().setByte(idx(index), value);
        return this;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.AbstractByteBuf
    public void _setByte(int index, int value) {
        unwrap()._setByte(idx(index), value);
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf setShort(int index, int value) {
        checkIndex0(index, 2);
        unwrap().setShort(idx(index), value);
        return this;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.AbstractByteBuf
    public void _setShort(int index, int value) {
        unwrap()._setShort(idx(index), value);
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf setShortLE(int index, int value) {
        checkIndex0(index, 2);
        unwrap().setShortLE(idx(index), value);
        return this;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.AbstractByteBuf
    public void _setShortLE(int index, int value) {
        unwrap()._setShortLE(idx(index), value);
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf setMedium(int index, int value) {
        checkIndex0(index, 3);
        unwrap().setMedium(idx(index), value);
        return this;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.AbstractByteBuf
    public void _setMedium(int index, int value) {
        unwrap()._setMedium(idx(index), value);
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf setMediumLE(int index, int value) {
        checkIndex0(index, 3);
        unwrap().setMediumLE(idx(index), value);
        return this;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.AbstractByteBuf
    public void _setMediumLE(int index, int value) {
        unwrap()._setMediumLE(idx(index), value);
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf setInt(int index, int value) {
        checkIndex0(index, 4);
        unwrap().setInt(idx(index), value);
        return this;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.AbstractByteBuf
    public void _setInt(int index, int value) {
        unwrap()._setInt(idx(index), value);
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf setIntLE(int index, int value) {
        checkIndex0(index, 4);
        unwrap().setIntLE(idx(index), value);
        return this;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.AbstractByteBuf
    public void _setIntLE(int index, int value) {
        unwrap()._setIntLE(idx(index), value);
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf setLong(int index, long value) {
        checkIndex0(index, 8);
        unwrap().setLong(idx(index), value);
        return this;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.AbstractByteBuf
    public void _setLong(int index, long value) {
        unwrap()._setLong(idx(index), value);
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf setLongLE(int index, long value) {
        checkIndex0(index, 8);
        unwrap().setLongLE(idx(index), value);
        return this;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.AbstractByteBuf
    public void _setLongLE(int index, long value) {
        unwrap().setLongLE(idx(index), value);
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setBytes(int index, byte[] src, int srcIndex, int length) {
        checkIndex0(index, length);
        unwrap().setBytes(idx(index), src, srcIndex, length);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setBytes(int index, ByteBuf src, int srcIndex, int length) {
        checkIndex0(index, length);
        unwrap().setBytes(idx(index), src, srcIndex, length);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setBytes(int index, ByteBuffer src) {
        checkIndex0(index, src.remaining());
        unwrap().setBytes(idx(index), src);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf getBytes(int index, OutputStream out, int length) throws IOException {
        checkIndex0(index, length);
        unwrap().getBytes(idx(index), out, length);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public int getBytes(int index, GatheringByteChannel out, int length) throws IOException {
        checkIndex0(index, length);
        return unwrap().getBytes(idx(index), out, length);
    }

    @Override // io.netty.buffer.ByteBuf
    public int getBytes(int index, FileChannel out, long position, int length) throws IOException {
        checkIndex0(index, length);
        return unwrap().getBytes(idx(index), out, position, length);
    }

    @Override // io.netty.buffer.ByteBuf
    public int setBytes(int index, InputStream in, int length) throws IOException {
        checkIndex0(index, length);
        return unwrap().setBytes(idx(index), in, length);
    }

    @Override // io.netty.buffer.ByteBuf
    public int setBytes(int index, ScatteringByteChannel in, int length) throws IOException {
        checkIndex0(index, length);
        return unwrap().setBytes(idx(index), in, length);
    }

    @Override // io.netty.buffer.ByteBuf
    public int setBytes(int index, FileChannel in, long position, int length) throws IOException {
        checkIndex0(index, length);
        return unwrap().setBytes(idx(index), in, position, length);
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public int forEachByte(int index, int length, ByteProcessor processor) {
        checkIndex0(index, length);
        int ret = unwrap().forEachByte(idx(index), length, processor);
        if (ret < this.adjustment) {
            return -1;
        }
        return ret - this.adjustment;
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public int forEachByteDesc(int index, int length, ByteProcessor processor) {
        checkIndex0(index, length);
        int ret = unwrap().forEachByteDesc(idx(index), length, processor);
        if (ret < this.adjustment) {
            return -1;
        }
        return ret - this.adjustment;
    }

    private int idx(int index) {
        return this.adjustment + index;
    }
}
