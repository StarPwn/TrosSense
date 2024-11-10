package io.netty.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.SwappedByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ByteProcessor;
import io.netty.util.Signal;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.StringUtil;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.charset.Charset;

/* loaded from: classes4.dex */
final class ReplayingDecoderByteBuf extends ByteBuf {
    private ByteBuf buffer;
    private SwappedByteBuf swapped;
    private boolean terminated;
    private static final Signal REPLAY = ReplayingDecoder.REPLAY;
    static final ReplayingDecoderByteBuf EMPTY_BUFFER = new ReplayingDecoderByteBuf(Unpooled.EMPTY_BUFFER);

    static {
        EMPTY_BUFFER.terminate();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ReplayingDecoderByteBuf() {
    }

    ReplayingDecoderByteBuf(ByteBuf buffer) {
        setCumulation(buffer);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setCumulation(ByteBuf buffer) {
        this.buffer = buffer;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void terminate() {
        this.terminated = true;
    }

    @Override // io.netty.buffer.ByteBuf
    public int capacity() {
        if (this.terminated) {
            return this.buffer.capacity();
        }
        return Integer.MAX_VALUE;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf capacity(int newCapacity) {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public int maxCapacity() {
        return capacity();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBufAllocator alloc() {
        return this.buffer.alloc();
    }

    @Override // io.netty.buffer.ByteBuf
    public boolean isReadOnly() {
        return false;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf asReadOnly() {
        return Unpooled.unmodifiableBuffer(this);
    }

    @Override // io.netty.buffer.ByteBuf
    public boolean isDirect() {
        return this.buffer.isDirect();
    }

    @Override // io.netty.buffer.ByteBuf
    public boolean hasArray() {
        return false;
    }

    @Override // io.netty.buffer.ByteBuf
    public byte[] array() {
        throw new UnsupportedOperationException();
    }

    @Override // io.netty.buffer.ByteBuf
    public int arrayOffset() {
        throw new UnsupportedOperationException();
    }

    @Override // io.netty.buffer.ByteBuf
    public boolean hasMemoryAddress() {
        return false;
    }

    @Override // io.netty.buffer.ByteBuf
    public long memoryAddress() {
        throw new UnsupportedOperationException();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf clear() {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public boolean equals(Object obj) {
        return this == obj;
    }

    @Override // io.netty.buffer.ByteBuf, java.lang.Comparable
    public int compareTo(ByteBuf buffer) {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf copy() {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf copy(int index, int length) {
        checkIndex(index, length);
        return this.buffer.copy(index, length);
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf discardReadBytes() {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf ensureWritable(int writableBytes) {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public int ensureWritable(int minWritableBytes, boolean force) {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf duplicate() {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf retainedDuplicate() {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public boolean getBoolean(int index) {
        checkIndex(index, 1);
        return this.buffer.getBoolean(index);
    }

    @Override // io.netty.buffer.ByteBuf
    public byte getByte(int index) {
        checkIndex(index, 1);
        return this.buffer.getByte(index);
    }

    @Override // io.netty.buffer.ByteBuf
    public short getUnsignedByte(int index) {
        checkIndex(index, 1);
        return this.buffer.getUnsignedByte(index);
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf getBytes(int index, byte[] dst, int dstIndex, int length) {
        checkIndex(index, length);
        this.buffer.getBytes(index, dst, dstIndex, length);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf getBytes(int index, byte[] dst) {
        checkIndex(index, dst.length);
        this.buffer.getBytes(index, dst);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf getBytes(int index, ByteBuffer dst) {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf getBytes(int index, ByteBuf dst, int dstIndex, int length) {
        checkIndex(index, length);
        this.buffer.getBytes(index, dst, dstIndex, length);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf getBytes(int index, ByteBuf dst, int length) {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf getBytes(int index, ByteBuf dst) {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public int getBytes(int index, GatheringByteChannel out, int length) {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public int getBytes(int index, FileChannel out, long position, int length) {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf getBytes(int index, OutputStream out, int length) {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public int getInt(int index) {
        checkIndex(index, 4);
        return this.buffer.getInt(index);
    }

    @Override // io.netty.buffer.ByteBuf
    public int getIntLE(int index) {
        checkIndex(index, 4);
        return this.buffer.getIntLE(index);
    }

    @Override // io.netty.buffer.ByteBuf
    public long getUnsignedInt(int index) {
        checkIndex(index, 4);
        return this.buffer.getUnsignedInt(index);
    }

    @Override // io.netty.buffer.ByteBuf
    public long getUnsignedIntLE(int index) {
        checkIndex(index, 4);
        return this.buffer.getUnsignedIntLE(index);
    }

    @Override // io.netty.buffer.ByteBuf
    public long getLong(int index) {
        checkIndex(index, 8);
        return this.buffer.getLong(index);
    }

    @Override // io.netty.buffer.ByteBuf
    public long getLongLE(int index) {
        checkIndex(index, 8);
        return this.buffer.getLongLE(index);
    }

    @Override // io.netty.buffer.ByteBuf
    public int getMedium(int index) {
        checkIndex(index, 3);
        return this.buffer.getMedium(index);
    }

    @Override // io.netty.buffer.ByteBuf
    public int getMediumLE(int index) {
        checkIndex(index, 3);
        return this.buffer.getMediumLE(index);
    }

    @Override // io.netty.buffer.ByteBuf
    public int getUnsignedMedium(int index) {
        checkIndex(index, 3);
        return this.buffer.getUnsignedMedium(index);
    }

    @Override // io.netty.buffer.ByteBuf
    public int getUnsignedMediumLE(int index) {
        checkIndex(index, 3);
        return this.buffer.getUnsignedMediumLE(index);
    }

    @Override // io.netty.buffer.ByteBuf
    public short getShort(int index) {
        checkIndex(index, 2);
        return this.buffer.getShort(index);
    }

    @Override // io.netty.buffer.ByteBuf
    public short getShortLE(int index) {
        checkIndex(index, 2);
        return this.buffer.getShortLE(index);
    }

    @Override // io.netty.buffer.ByteBuf
    public int getUnsignedShort(int index) {
        checkIndex(index, 2);
        return this.buffer.getUnsignedShort(index);
    }

    @Override // io.netty.buffer.ByteBuf
    public int getUnsignedShortLE(int index) {
        checkIndex(index, 2);
        return this.buffer.getUnsignedShortLE(index);
    }

    @Override // io.netty.buffer.ByteBuf
    public char getChar(int index) {
        checkIndex(index, 2);
        return this.buffer.getChar(index);
    }

    @Override // io.netty.buffer.ByteBuf
    public float getFloat(int index) {
        checkIndex(index, 4);
        return this.buffer.getFloat(index);
    }

    @Override // io.netty.buffer.ByteBuf
    public double getDouble(int index) {
        checkIndex(index, 8);
        return this.buffer.getDouble(index);
    }

    @Override // io.netty.buffer.ByteBuf
    public CharSequence getCharSequence(int index, int length, Charset charset) {
        checkIndex(index, length);
        return this.buffer.getCharSequence(index, length, charset);
    }

    @Override // io.netty.buffer.ByteBuf
    public int hashCode() {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public int indexOf(int fromIndex, int toIndex, byte value) {
        if (fromIndex == toIndex) {
            return -1;
        }
        if (Math.max(fromIndex, toIndex) > this.buffer.writerIndex()) {
            throw REPLAY;
        }
        return this.buffer.indexOf(fromIndex, toIndex, value);
    }

    @Override // io.netty.buffer.ByteBuf
    public int bytesBefore(byte value) {
        int bytes = this.buffer.bytesBefore(value);
        if (bytes < 0) {
            throw REPLAY;
        }
        return bytes;
    }

    @Override // io.netty.buffer.ByteBuf
    public int bytesBefore(int length, byte value) {
        return bytesBefore(this.buffer.readerIndex(), length, value);
    }

    @Override // io.netty.buffer.ByteBuf
    public int bytesBefore(int index, int length, byte value) {
        int writerIndex = this.buffer.writerIndex();
        if (index >= writerIndex) {
            throw REPLAY;
        }
        if (index <= writerIndex - length) {
            return this.buffer.bytesBefore(index, length, value);
        }
        int res = this.buffer.bytesBefore(index, writerIndex - index, value);
        if (res < 0) {
            throw REPLAY;
        }
        return res;
    }

    @Override // io.netty.buffer.ByteBuf
    public int forEachByte(ByteProcessor processor) {
        int ret = this.buffer.forEachByte(processor);
        if (ret < 0) {
            throw REPLAY;
        }
        return ret;
    }

    @Override // io.netty.buffer.ByteBuf
    public int forEachByte(int index, int length, ByteProcessor processor) {
        int writerIndex = this.buffer.writerIndex();
        if (index >= writerIndex) {
            throw REPLAY;
        }
        if (index <= writerIndex - length) {
            return this.buffer.forEachByte(index, length, processor);
        }
        int ret = this.buffer.forEachByte(index, writerIndex - index, processor);
        if (ret < 0) {
            throw REPLAY;
        }
        return ret;
    }

    @Override // io.netty.buffer.ByteBuf
    public int forEachByteDesc(ByteProcessor processor) {
        if (this.terminated) {
            return this.buffer.forEachByteDesc(processor);
        }
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public int forEachByteDesc(int index, int length, ByteProcessor processor) {
        if (index + length > this.buffer.writerIndex()) {
            throw REPLAY;
        }
        return this.buffer.forEachByteDesc(index, length, processor);
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf markReaderIndex() {
        this.buffer.markReaderIndex();
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf markWriterIndex() {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteOrder order() {
        return this.buffer.order();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf order(ByteOrder endianness) {
        if (ObjectUtil.checkNotNull(endianness, "endianness") == order()) {
            return this;
        }
        SwappedByteBuf swapped = this.swapped;
        if (swapped == null) {
            SwappedByteBuf swapped2 = new SwappedByteBuf(this);
            this.swapped = swapped2;
            return swapped2;
        }
        return swapped;
    }

    @Override // io.netty.buffer.ByteBuf
    public boolean isReadable() {
        return !this.terminated || this.buffer.isReadable();
    }

    @Override // io.netty.buffer.ByteBuf
    public boolean isReadable(int size) {
        return !this.terminated || this.buffer.isReadable(size);
    }

    @Override // io.netty.buffer.ByteBuf
    public int readableBytes() {
        if (this.terminated) {
            return this.buffer.readableBytes();
        }
        return Integer.MAX_VALUE - this.buffer.readerIndex();
    }

    @Override // io.netty.buffer.ByteBuf
    public boolean readBoolean() {
        checkReadableBytes(1);
        return this.buffer.readBoolean();
    }

    @Override // io.netty.buffer.ByteBuf
    public byte readByte() {
        checkReadableBytes(1);
        return this.buffer.readByte();
    }

    @Override // io.netty.buffer.ByteBuf
    public short readUnsignedByte() {
        checkReadableBytes(1);
        return this.buffer.readUnsignedByte();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf readBytes(byte[] dst, int dstIndex, int length) {
        checkReadableBytes(length);
        this.buffer.readBytes(dst, dstIndex, length);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf readBytes(byte[] dst) {
        checkReadableBytes(dst.length);
        this.buffer.readBytes(dst);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf readBytes(ByteBuffer dst) {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf readBytes(ByteBuf dst, int dstIndex, int length) {
        checkReadableBytes(length);
        this.buffer.readBytes(dst, dstIndex, length);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf readBytes(ByteBuf dst, int length) {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf readBytes(ByteBuf dst) {
        checkReadableBytes(dst.writableBytes());
        this.buffer.readBytes(dst);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public int readBytes(GatheringByteChannel out, int length) {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public int readBytes(FileChannel out, long position, int length) {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf readBytes(int length) {
        checkReadableBytes(length);
        return this.buffer.readBytes(length);
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf readSlice(int length) {
        checkReadableBytes(length);
        return this.buffer.readSlice(length);
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf readRetainedSlice(int length) {
        checkReadableBytes(length);
        return this.buffer.readRetainedSlice(length);
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf readBytes(OutputStream out, int length) {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public int readerIndex() {
        return this.buffer.readerIndex();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf readerIndex(int readerIndex) {
        this.buffer.readerIndex(readerIndex);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public int readInt() {
        checkReadableBytes(4);
        return this.buffer.readInt();
    }

    @Override // io.netty.buffer.ByteBuf
    public int readIntLE() {
        checkReadableBytes(4);
        return this.buffer.readIntLE();
    }

    @Override // io.netty.buffer.ByteBuf
    public long readUnsignedInt() {
        checkReadableBytes(4);
        return this.buffer.readUnsignedInt();
    }

    @Override // io.netty.buffer.ByteBuf
    public long readUnsignedIntLE() {
        checkReadableBytes(4);
        return this.buffer.readUnsignedIntLE();
    }

    @Override // io.netty.buffer.ByteBuf
    public long readLong() {
        checkReadableBytes(8);
        return this.buffer.readLong();
    }

    @Override // io.netty.buffer.ByteBuf
    public long readLongLE() {
        checkReadableBytes(8);
        return this.buffer.readLongLE();
    }

    @Override // io.netty.buffer.ByteBuf
    public int readMedium() {
        checkReadableBytes(3);
        return this.buffer.readMedium();
    }

    @Override // io.netty.buffer.ByteBuf
    public int readMediumLE() {
        checkReadableBytes(3);
        return this.buffer.readMediumLE();
    }

    @Override // io.netty.buffer.ByteBuf
    public int readUnsignedMedium() {
        checkReadableBytes(3);
        return this.buffer.readUnsignedMedium();
    }

    @Override // io.netty.buffer.ByteBuf
    public int readUnsignedMediumLE() {
        checkReadableBytes(3);
        return this.buffer.readUnsignedMediumLE();
    }

    @Override // io.netty.buffer.ByteBuf
    public short readShort() {
        checkReadableBytes(2);
        return this.buffer.readShort();
    }

    @Override // io.netty.buffer.ByteBuf
    public short readShortLE() {
        checkReadableBytes(2);
        return this.buffer.readShortLE();
    }

    @Override // io.netty.buffer.ByteBuf
    public int readUnsignedShort() {
        checkReadableBytes(2);
        return this.buffer.readUnsignedShort();
    }

    @Override // io.netty.buffer.ByteBuf
    public int readUnsignedShortLE() {
        checkReadableBytes(2);
        return this.buffer.readUnsignedShortLE();
    }

    @Override // io.netty.buffer.ByteBuf
    public char readChar() {
        checkReadableBytes(2);
        return this.buffer.readChar();
    }

    @Override // io.netty.buffer.ByteBuf
    public float readFloat() {
        checkReadableBytes(4);
        return this.buffer.readFloat();
    }

    @Override // io.netty.buffer.ByteBuf
    public double readDouble() {
        checkReadableBytes(8);
        return this.buffer.readDouble();
    }

    @Override // io.netty.buffer.ByteBuf
    public CharSequence readCharSequence(int length, Charset charset) {
        checkReadableBytes(length);
        return this.buffer.readCharSequence(length, charset);
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf resetReaderIndex() {
        this.buffer.resetReaderIndex();
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf resetWriterIndex() {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setBoolean(int index, boolean value) {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setByte(int index, int value) {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setBytes(int index, byte[] src, int srcIndex, int length) {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setBytes(int index, byte[] src) {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setBytes(int index, ByteBuffer src) {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setBytes(int index, ByteBuf src, int srcIndex, int length) {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setBytes(int index, ByteBuf src, int length) {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setBytes(int index, ByteBuf src) {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public int setBytes(int index, InputStream in, int length) {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setZero(int index, int length) {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public int setBytes(int index, ScatteringByteChannel in, int length) {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public int setBytes(int index, FileChannel in, long position, int length) {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setIndex(int readerIndex, int writerIndex) {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setInt(int index, int value) {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setIntLE(int index, int value) {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setLong(int index, long value) {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setLongLE(int index, long value) {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setMedium(int index, int value) {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setMediumLE(int index, int value) {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setShort(int index, int value) {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setShortLE(int index, int value) {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setChar(int index, int value) {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setFloat(int index, float value) {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setDouble(int index, double value) {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf skipBytes(int length) {
        checkReadableBytes(length);
        this.buffer.skipBytes(length);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf slice() {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf retainedSlice() {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf slice(int index, int length) {
        checkIndex(index, length);
        return this.buffer.slice(index, length);
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf retainedSlice(int index, int length) {
        checkIndex(index, length);
        return this.buffer.retainedSlice(index, length);
    }

    @Override // io.netty.buffer.ByteBuf
    public int nioBufferCount() {
        return this.buffer.nioBufferCount();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuffer nioBuffer() {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuffer nioBuffer(int index, int length) {
        checkIndex(index, length);
        return this.buffer.nioBuffer(index, length);
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuffer[] nioBuffers() {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuffer[] nioBuffers(int index, int length) {
        checkIndex(index, length);
        return this.buffer.nioBuffers(index, length);
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuffer internalNioBuffer(int index, int length) {
        checkIndex(index, length);
        return this.buffer.internalNioBuffer(index, length);
    }

    @Override // io.netty.buffer.ByteBuf
    public String toString(int index, int length, Charset charset) {
        checkIndex(index, length);
        return this.buffer.toString(index, length, charset);
    }

    @Override // io.netty.buffer.ByteBuf
    public String toString(Charset charsetName) {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public String toString() {
        return StringUtil.simpleClassName(this) + "(ridx=" + readerIndex() + ", widx=" + writerIndex() + ')';
    }

    @Override // io.netty.buffer.ByteBuf
    public boolean isWritable() {
        return false;
    }

    @Override // io.netty.buffer.ByteBuf
    public boolean isWritable(int size) {
        return false;
    }

    @Override // io.netty.buffer.ByteBuf
    public int writableBytes() {
        return 0;
    }

    @Override // io.netty.buffer.ByteBuf
    public int maxWritableBytes() {
        return 0;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeBoolean(boolean value) {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeByte(int value) {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeBytes(byte[] src, int srcIndex, int length) {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeBytes(byte[] src) {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeBytes(ByteBuffer src) {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeBytes(ByteBuf src, int srcIndex, int length) {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeBytes(ByteBuf src, int length) {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeBytes(ByteBuf src) {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public int writeBytes(InputStream in, int length) {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public int writeBytes(ScatteringByteChannel in, int length) {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public int writeBytes(FileChannel in, long position, int length) {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeInt(int value) {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeIntLE(int value) {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeLong(long value) {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeLongLE(long value) {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeMedium(int value) {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeMediumLE(int value) {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeZero(int length) {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public int writerIndex() {
        return this.buffer.writerIndex();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writerIndex(int writerIndex) {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeShort(int value) {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeShortLE(int value) {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeChar(int value) {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeFloat(float value) {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeDouble(double value) {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public int setCharSequence(int index, CharSequence sequence, Charset charset) {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public int writeCharSequence(CharSequence sequence, Charset charset) {
        throw reject();
    }

    private void checkIndex(int index, int length) {
        if (index + length > this.buffer.writerIndex()) {
            throw REPLAY;
        }
    }

    private void checkReadableBytes(int readableBytes) {
        if (this.buffer.readableBytes() < readableBytes) {
            throw REPLAY;
        }
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf discardSomeReadBytes() {
        throw reject();
    }

    @Override // io.netty.util.ReferenceCounted
    public int refCnt() {
        return this.buffer.refCnt();
    }

    @Override // io.netty.buffer.ByteBuf, io.netty.util.ReferenceCounted
    public ByteBuf retain() {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf, io.netty.util.ReferenceCounted
    public ByteBuf retain(int increment) {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf, io.netty.util.ReferenceCounted
    public ByteBuf touch() {
        this.buffer.touch();
        return this;
    }

    @Override // io.netty.buffer.ByteBuf, io.netty.util.ReferenceCounted
    public ByteBuf touch(Object hint) {
        this.buffer.touch(hint);
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public boolean release() {
        throw reject();
    }

    @Override // io.netty.util.ReferenceCounted
    public boolean release(int decrement) {
        throw reject();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf unwrap() {
        throw reject();
    }

    private static UnsupportedOperationException reject() {
        return new UnsupportedOperationException("not a replayable operation");
    }
}
