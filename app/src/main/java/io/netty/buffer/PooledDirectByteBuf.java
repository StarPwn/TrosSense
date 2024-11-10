package io.netty.buffer;

import io.netty.util.internal.ObjectPool;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public final class PooledDirectByteBuf extends PooledByteBuf<ByteBuffer> {
    private static final ObjectPool<PooledDirectByteBuf> RECYCLER = ObjectPool.newPool(new ObjectPool.ObjectCreator<PooledDirectByteBuf>() { // from class: io.netty.buffer.PooledDirectByteBuf.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // io.netty.util.internal.ObjectPool.ObjectCreator
        public PooledDirectByteBuf newObject(ObjectPool.Handle<PooledDirectByteBuf> handle) {
            return new PooledDirectByteBuf(handle, 0);
        }
    });

    /* JADX INFO: Access modifiers changed from: package-private */
    public static PooledDirectByteBuf newInstance(int maxCapacity) {
        PooledDirectByteBuf buf = RECYCLER.get();
        buf.reuse(maxCapacity);
        return buf;
    }

    private PooledDirectByteBuf(ObjectPool.Handle<PooledDirectByteBuf> recyclerHandle, int maxCapacity) {
        super(recyclerHandle, maxCapacity);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.PooledByteBuf
    public ByteBuffer newInternalNioBuffer(ByteBuffer memory) {
        return memory.duplicate();
    }

    @Override // io.netty.buffer.ByteBuf
    public boolean isDirect() {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Multi-variable type inference failed */
    @Override // io.netty.buffer.AbstractByteBuf
    public byte _getByte(int index) {
        return ((ByteBuffer) this.memory).get(idx(index));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Multi-variable type inference failed */
    @Override // io.netty.buffer.AbstractByteBuf
    public short _getShort(int index) {
        return ((ByteBuffer) this.memory).getShort(idx(index));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.AbstractByteBuf
    public short _getShortLE(int index) {
        return ByteBufUtil.swapShort(_getShort(index));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Multi-variable type inference failed */
    @Override // io.netty.buffer.AbstractByteBuf
    public int _getUnsignedMedium(int index) {
        int index2 = idx(index);
        return ((((ByteBuffer) this.memory).get(index2) & 255) << 16) | ((((ByteBuffer) this.memory).get(index2 + 1) & 255) << 8) | (((ByteBuffer) this.memory).get(index2 + 2) & 255);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Multi-variable type inference failed */
    @Override // io.netty.buffer.AbstractByteBuf
    public int _getUnsignedMediumLE(int index) {
        int index2 = idx(index);
        return (((ByteBuffer) this.memory).get(index2) & 255) | ((((ByteBuffer) this.memory).get(index2 + 1) & 255) << 8) | ((((ByteBuffer) this.memory).get(index2 + 2) & 255) << 16);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Multi-variable type inference failed */
    @Override // io.netty.buffer.AbstractByteBuf
    public int _getInt(int index) {
        return ((ByteBuffer) this.memory).getInt(idx(index));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.AbstractByteBuf
    public int _getIntLE(int index) {
        return ByteBufUtil.swapInt(_getInt(index));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Multi-variable type inference failed */
    @Override // io.netty.buffer.AbstractByteBuf
    public long _getLong(int index) {
        return ((ByteBuffer) this.memory).getLong(idx(index));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.AbstractByteBuf
    public long _getLongLE(int index) {
        return ByteBufUtil.swapLong(_getLong(index));
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf getBytes(int index, ByteBuf dst, int dstIndex, int length) {
        checkDstIndex(index, length, dstIndex, dst.capacity());
        if (dst.hasArray()) {
            getBytes(index, dst.array(), dst.arrayOffset() + dstIndex, length);
        } else if (dst.nioBufferCount() > 0) {
            for (ByteBuffer bb : dst.nioBuffers(dstIndex, length)) {
                int bbLen = bb.remaining();
                getBytes(index, bb);
                index += bbLen;
            }
        } else {
            dst.setBytes(dstIndex, this, index, length);
        }
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf getBytes(int index, byte[] dst, int dstIndex, int length) {
        checkDstIndex(index, length, dstIndex, dst.length);
        _internalNioBuffer(index, length, true).get(dst, dstIndex, length);
        return this;
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf readBytes(byte[] dst, int dstIndex, int length) {
        checkDstIndex(length, dstIndex, dst.length);
        _internalNioBuffer(this.readerIndex, length, false).get(dst, dstIndex, length);
        this.readerIndex += length;
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf getBytes(int index, ByteBuffer dst) {
        dst.put(duplicateInternalNioBuffer(index, dst.remaining()));
        return this;
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf readBytes(ByteBuffer dst) {
        int length = dst.remaining();
        checkReadableBytes(length);
        dst.put(_internalNioBuffer(this.readerIndex, length, false));
        this.readerIndex += length;
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf getBytes(int index, OutputStream out, int length) throws IOException {
        getBytes(index, out, length, false);
        return this;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void getBytes(int index, OutputStream out, int length, boolean internal) throws IOException {
        checkIndex(index, length);
        if (length == 0) {
            return;
        }
        ByteBufUtil.readBytes(alloc(), internal ? internalNioBuffer() : ((ByteBuffer) this.memory).duplicate(), idx(index), length, out);
    }

    @Override // io.netty.buffer.AbstractByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf readBytes(OutputStream out, int length) throws IOException {
        checkReadableBytes(length);
        getBytes(this.readerIndex, out, length, true);
        this.readerIndex += length;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Multi-variable type inference failed */
    @Override // io.netty.buffer.AbstractByteBuf
    public void _setByte(int index, int value) {
        ((ByteBuffer) this.memory).put(idx(index), (byte) value);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Multi-variable type inference failed */
    @Override // io.netty.buffer.AbstractByteBuf
    public void _setShort(int index, int value) {
        ((ByteBuffer) this.memory).putShort(idx(index), (short) value);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.AbstractByteBuf
    public void _setShortLE(int index, int value) {
        _setShort(index, ByteBufUtil.swapShort((short) value));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Multi-variable type inference failed */
    @Override // io.netty.buffer.AbstractByteBuf
    public void _setMedium(int index, int value) {
        int index2 = idx(index);
        ((ByteBuffer) this.memory).put(index2, (byte) (value >>> 16));
        ((ByteBuffer) this.memory).put(index2 + 1, (byte) (value >>> 8));
        ((ByteBuffer) this.memory).put(index2 + 2, (byte) value);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Multi-variable type inference failed */
    @Override // io.netty.buffer.AbstractByteBuf
    public void _setMediumLE(int index, int value) {
        int index2 = idx(index);
        ((ByteBuffer) this.memory).put(index2, (byte) value);
        ((ByteBuffer) this.memory).put(index2 + 1, (byte) (value >>> 8));
        ((ByteBuffer) this.memory).put(index2 + 2, (byte) (value >>> 16));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Multi-variable type inference failed */
    @Override // io.netty.buffer.AbstractByteBuf
    public void _setInt(int index, int value) {
        ((ByteBuffer) this.memory).putInt(idx(index), value);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.AbstractByteBuf
    public void _setIntLE(int index, int value) {
        _setInt(index, ByteBufUtil.swapInt(value));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Multi-variable type inference failed */
    @Override // io.netty.buffer.AbstractByteBuf
    public void _setLong(int index, long value) {
        ((ByteBuffer) this.memory).putLong(idx(index), value);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.buffer.AbstractByteBuf
    public void _setLongLE(int index, long value) {
        _setLong(index, ByteBufUtil.swapLong(value));
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setBytes(int index, ByteBuf src, int srcIndex, int length) {
        checkSrcIndex(index, length, srcIndex, src.capacity());
        if (src.hasArray()) {
            setBytes(index, src.array(), src.arrayOffset() + srcIndex, length);
        } else if (src.nioBufferCount() > 0) {
            for (ByteBuffer bb : src.nioBuffers(srcIndex, length)) {
                int bbLen = bb.remaining();
                setBytes(index, bb);
                index += bbLen;
            }
        } else {
            src.getBytes(srcIndex, this, index, length);
        }
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setBytes(int index, byte[] src, int srcIndex, int length) {
        checkSrcIndex(index, length, srcIndex, src.length);
        _internalNioBuffer(index, length, false).put(src, srcIndex, length);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setBytes(int index, ByteBuffer src) {
        int length = src.remaining();
        checkIndex(index, length);
        ByteBuffer tmpBuf = internalNioBuffer();
        if (src == tmpBuf) {
            src = src.duplicate();
        }
        int index2 = idx(index);
        tmpBuf.limit(index2 + length).position(index2);
        tmpBuf.put(src);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public int setBytes(int index, InputStream in, int length) throws IOException {
        checkIndex(index, length);
        byte[] tmp = ByteBufUtil.threadLocalTempArray(length);
        int readBytes = in.read(tmp, 0, length);
        if (readBytes <= 0) {
            return readBytes;
        }
        ByteBuffer tmpBuf = internalNioBuffer();
        tmpBuf.position(idx(index));
        tmpBuf.put(tmp, 0, readBytes);
        return readBytes;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf copy(int index, int length) {
        checkIndex(index, length);
        ByteBuf copy = alloc().directBuffer(length, maxCapacity());
        return copy.writeBytes(this, index, length);
    }

    @Override // io.netty.buffer.ByteBuf
    public boolean hasArray() {
        return false;
    }

    @Override // io.netty.buffer.ByteBuf
    public byte[] array() {
        throw new UnsupportedOperationException("direct buffer");
    }

    @Override // io.netty.buffer.ByteBuf
    public int arrayOffset() {
        throw new UnsupportedOperationException("direct buffer");
    }

    @Override // io.netty.buffer.ByteBuf
    public boolean hasMemoryAddress() {
        return false;
    }

    @Override // io.netty.buffer.ByteBuf
    public long memoryAddress() {
        throw new UnsupportedOperationException();
    }
}
