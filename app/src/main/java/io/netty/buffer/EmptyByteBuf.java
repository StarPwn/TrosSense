package io.netty.buffer;

import io.netty.util.ByteProcessor;
import io.netty.util.internal.EmptyArrays;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ReadOnlyBufferException;
import java.nio.channels.FileChannel;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.charset.Charset;

/* loaded from: classes4.dex */
public final class EmptyByteBuf extends ByteBuf {
    private static final ByteBuffer EMPTY_BYTE_BUFFER = ByteBuffer.allocateDirect(0);
    private static final long EMPTY_BYTE_BUFFER_ADDRESS;
    static final int EMPTY_BYTE_BUF_HASH_CODE = 1;
    private final ByteBufAllocator alloc;
    private final ByteOrder order;
    private final String str;
    private EmptyByteBuf swapped;

    static {
        long emptyByteBufferAddress = 0;
        try {
            if (PlatformDependent.hasUnsafe()) {
                emptyByteBufferAddress = PlatformDependent.directBufferAddress(EMPTY_BYTE_BUFFER);
            }
        } catch (Throwable th) {
        }
        EMPTY_BYTE_BUFFER_ADDRESS = emptyByteBufferAddress;
    }

    public EmptyByteBuf(ByteBufAllocator alloc) {
        this(alloc, ByteOrder.BIG_ENDIAN);
    }

    private EmptyByteBuf(ByteBufAllocator alloc, ByteOrder order) {
        this.alloc = (ByteBufAllocator) ObjectUtil.checkNotNull(alloc, "alloc");
        this.order = order;
        this.str = StringUtil.simpleClassName(this) + (order == ByteOrder.BIG_ENDIAN ? "BE" : "LE");
    }

    @Override // io.netty.buffer.ByteBuf
    public int capacity() {
        return 0;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf capacity(int newCapacity) {
        throw new ReadOnlyBufferException();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBufAllocator alloc() {
        return this.alloc;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteOrder order() {
        return this.order;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf unwrap() {
        return null;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf asReadOnly() {
        return Unpooled.unmodifiableBuffer(this);
    }

    @Override // io.netty.buffer.ByteBuf
    public boolean isReadOnly() {
        return false;
    }

    @Override // io.netty.buffer.ByteBuf
    public boolean isDirect() {
        return true;
    }

    @Override // io.netty.buffer.ByteBuf
    public int maxCapacity() {
        return 0;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf order(ByteOrder endianness) {
        if (ObjectUtil.checkNotNull(endianness, "endianness") == order()) {
            return this;
        }
        EmptyByteBuf swapped = this.swapped;
        if (swapped != null) {
            return swapped;
        }
        EmptyByteBuf swapped2 = new EmptyByteBuf(alloc(), endianness);
        this.swapped = swapped2;
        return swapped2;
    }

    @Override // io.netty.buffer.ByteBuf
    public int readerIndex() {
        return 0;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf readerIndex(int readerIndex) {
        return checkIndex(readerIndex);
    }

    @Override // io.netty.buffer.ByteBuf
    public int writerIndex() {
        return 0;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writerIndex(int writerIndex) {
        return checkIndex(writerIndex);
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setIndex(int readerIndex, int writerIndex) {
        checkIndex(readerIndex);
        checkIndex(writerIndex);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public int readableBytes() {
        return 0;
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
    public boolean isReadable() {
        return false;
    }

    @Override // io.netty.buffer.ByteBuf
    public boolean isWritable() {
        return false;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf clear() {
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf markReaderIndex() {
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf resetReaderIndex() {
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf markWriterIndex() {
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf resetWriterIndex() {
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf discardReadBytes() {
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf discardSomeReadBytes() {
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf ensureWritable(int minWritableBytes) {
        ObjectUtil.checkPositiveOrZero(minWritableBytes, "minWritableBytes");
        if (minWritableBytes != 0) {
            throw new IndexOutOfBoundsException();
        }
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public int ensureWritable(int minWritableBytes, boolean force) {
        ObjectUtil.checkPositiveOrZero(minWritableBytes, "minWritableBytes");
        if (minWritableBytes == 0) {
            return 0;
        }
        return 1;
    }

    @Override // io.netty.buffer.ByteBuf
    public boolean getBoolean(int index) {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public byte getByte(int index) {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public short getUnsignedByte(int index) {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public short getShort(int index) {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public short getShortLE(int index) {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public int getUnsignedShort(int index) {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public int getUnsignedShortLE(int index) {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public int getMedium(int index) {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public int getMediumLE(int index) {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public int getUnsignedMedium(int index) {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public int getUnsignedMediumLE(int index) {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public int getInt(int index) {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public int getIntLE(int index) {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public long getUnsignedInt(int index) {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public long getUnsignedIntLE(int index) {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public long getLong(int index) {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public long getLongLE(int index) {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public char getChar(int index) {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public float getFloat(int index) {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public double getDouble(int index) {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf getBytes(int index, ByteBuf dst) {
        return checkIndex(index, dst.writableBytes());
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf getBytes(int index, ByteBuf dst, int length) {
        return checkIndex(index, length);
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf getBytes(int index, ByteBuf dst, int dstIndex, int length) {
        return checkIndex(index, length);
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf getBytes(int index, byte[] dst) {
        return checkIndex(index, dst.length);
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf getBytes(int index, byte[] dst, int dstIndex, int length) {
        return checkIndex(index, length);
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf getBytes(int index, ByteBuffer dst) {
        return checkIndex(index, dst.remaining());
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf getBytes(int index, OutputStream out, int length) {
        return checkIndex(index, length);
    }

    @Override // io.netty.buffer.ByteBuf
    public int getBytes(int index, GatheringByteChannel out, int length) {
        checkIndex(index, length);
        return 0;
    }

    @Override // io.netty.buffer.ByteBuf
    public int getBytes(int index, FileChannel out, long position, int length) {
        checkIndex(index, length);
        return 0;
    }

    @Override // io.netty.buffer.ByteBuf
    public CharSequence getCharSequence(int index, int length, Charset charset) {
        checkIndex(index, length);
        return null;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setBoolean(int index, boolean value) {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setByte(int index, int value) {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setShort(int index, int value) {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setShortLE(int index, int value) {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setMedium(int index, int value) {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setMediumLE(int index, int value) {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setInt(int index, int value) {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setIntLE(int index, int value) {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setLong(int index, long value) {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setLongLE(int index, long value) {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setChar(int index, int value) {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setFloat(int index, float value) {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setDouble(int index, double value) {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setBytes(int index, ByteBuf src) {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setBytes(int index, ByteBuf src, int length) {
        return checkIndex(index, length);
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setBytes(int index, ByteBuf src, int srcIndex, int length) {
        return checkIndex(index, length);
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setBytes(int index, byte[] src) {
        return checkIndex(index, src.length);
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setBytes(int index, byte[] src, int srcIndex, int length) {
        return checkIndex(index, length);
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setBytes(int index, ByteBuffer src) {
        return checkIndex(index, src.remaining());
    }

    @Override // io.netty.buffer.ByteBuf
    public int setBytes(int index, InputStream in, int length) {
        checkIndex(index, length);
        return 0;
    }

    @Override // io.netty.buffer.ByteBuf
    public int setBytes(int index, ScatteringByteChannel in, int length) {
        checkIndex(index, length);
        return 0;
    }

    @Override // io.netty.buffer.ByteBuf
    public int setBytes(int index, FileChannel in, long position, int length) {
        checkIndex(index, length);
        return 0;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setZero(int index, int length) {
        return checkIndex(index, length);
    }

    @Override // io.netty.buffer.ByteBuf
    public int setCharSequence(int index, CharSequence sequence, Charset charset) {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public boolean readBoolean() {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public byte readByte() {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public short readUnsignedByte() {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public short readShort() {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public short readShortLE() {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public int readUnsignedShort() {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public int readUnsignedShortLE() {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public int readMedium() {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public int readMediumLE() {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public int readUnsignedMedium() {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public int readUnsignedMediumLE() {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public int readInt() {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public int readIntLE() {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public long readUnsignedInt() {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public long readUnsignedIntLE() {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public long readLong() {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public long readLongLE() {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public char readChar() {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public float readFloat() {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public double readDouble() {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf readBytes(int length) {
        return checkLength(length);
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf readSlice(int length) {
        return checkLength(length);
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf readRetainedSlice(int length) {
        return checkLength(length);
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf readBytes(ByteBuf dst) {
        return checkLength(dst.writableBytes());
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf readBytes(ByteBuf dst, int length) {
        return checkLength(length);
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf readBytes(ByteBuf dst, int dstIndex, int length) {
        return checkLength(length);
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf readBytes(byte[] dst) {
        return checkLength(dst.length);
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf readBytes(byte[] dst, int dstIndex, int length) {
        return checkLength(length);
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf readBytes(ByteBuffer dst) {
        return checkLength(dst.remaining());
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf readBytes(OutputStream out, int length) {
        return checkLength(length);
    }

    @Override // io.netty.buffer.ByteBuf
    public int readBytes(GatheringByteChannel out, int length) {
        checkLength(length);
        return 0;
    }

    @Override // io.netty.buffer.ByteBuf
    public int readBytes(FileChannel out, long position, int length) {
        checkLength(length);
        return 0;
    }

    @Override // io.netty.buffer.ByteBuf
    public CharSequence readCharSequence(int length, Charset charset) {
        checkLength(length);
        return "";
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf skipBytes(int length) {
        return checkLength(length);
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeBoolean(boolean value) {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeByte(int value) {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeShort(int value) {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeShortLE(int value) {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeMedium(int value) {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeMediumLE(int value) {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeInt(int value) {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeIntLE(int value) {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeLong(long value) {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeLongLE(long value) {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeChar(int value) {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeFloat(float value) {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeDouble(double value) {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeBytes(ByteBuf src) {
        return checkLength(src.readableBytes());
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeBytes(ByteBuf src, int length) {
        return checkLength(length);
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeBytes(ByteBuf src, int srcIndex, int length) {
        return checkLength(length);
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeBytes(byte[] src) {
        return checkLength(src.length);
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeBytes(byte[] src, int srcIndex, int length) {
        return checkLength(length);
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeBytes(ByteBuffer src) {
        return checkLength(src.remaining());
    }

    @Override // io.netty.buffer.ByteBuf
    public int writeBytes(InputStream in, int length) {
        checkLength(length);
        return 0;
    }

    @Override // io.netty.buffer.ByteBuf
    public int writeBytes(ScatteringByteChannel in, int length) {
        checkLength(length);
        return 0;
    }

    @Override // io.netty.buffer.ByteBuf
    public int writeBytes(FileChannel in, long position, int length) {
        checkLength(length);
        return 0;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeZero(int length) {
        return checkLength(length);
    }

    @Override // io.netty.buffer.ByteBuf
    public int writeCharSequence(CharSequence sequence, Charset charset) {
        throw new IndexOutOfBoundsException();
    }

    @Override // io.netty.buffer.ByteBuf
    public int indexOf(int fromIndex, int toIndex, byte value) {
        checkIndex(fromIndex);
        checkIndex(toIndex);
        return -1;
    }

    @Override // io.netty.buffer.ByteBuf
    public int bytesBefore(byte value) {
        return -1;
    }

    @Override // io.netty.buffer.ByteBuf
    public int bytesBefore(int length, byte value) {
        checkLength(length);
        return -1;
    }

    @Override // io.netty.buffer.ByteBuf
    public int bytesBefore(int index, int length, byte value) {
        checkIndex(index, length);
        return -1;
    }

    @Override // io.netty.buffer.ByteBuf
    public int forEachByte(ByteProcessor processor) {
        return -1;
    }

    @Override // io.netty.buffer.ByteBuf
    public int forEachByte(int index, int length, ByteProcessor processor) {
        checkIndex(index, length);
        return -1;
    }

    @Override // io.netty.buffer.ByteBuf
    public int forEachByteDesc(ByteProcessor processor) {
        return -1;
    }

    @Override // io.netty.buffer.ByteBuf
    public int forEachByteDesc(int index, int length, ByteProcessor processor) {
        checkIndex(index, length);
        return -1;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf copy() {
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf copy(int index, int length) {
        return checkIndex(index, length);
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf slice() {
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf retainedSlice() {
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf slice(int index, int length) {
        return checkIndex(index, length);
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf retainedSlice(int index, int length) {
        return checkIndex(index, length);
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf duplicate() {
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf retainedDuplicate() {
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public int nioBufferCount() {
        return 1;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuffer nioBuffer() {
        return EMPTY_BYTE_BUFFER;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuffer nioBuffer(int index, int length) {
        checkIndex(index, length);
        return nioBuffer();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuffer[] nioBuffers() {
        return new ByteBuffer[]{EMPTY_BYTE_BUFFER};
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuffer[] nioBuffers(int index, int length) {
        checkIndex(index, length);
        return nioBuffers();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuffer internalNioBuffer(int index, int length) {
        return EMPTY_BYTE_BUFFER;
    }

    @Override // io.netty.buffer.ByteBuf
    public boolean hasArray() {
        return true;
    }

    @Override // io.netty.buffer.ByteBuf
    public byte[] array() {
        return EmptyArrays.EMPTY_BYTES;
    }

    @Override // io.netty.buffer.ByteBuf
    public int arrayOffset() {
        return 0;
    }

    @Override // io.netty.buffer.ByteBuf
    public boolean hasMemoryAddress() {
        return EMPTY_BYTE_BUFFER_ADDRESS != 0;
    }

    @Override // io.netty.buffer.ByteBuf
    public long memoryAddress() {
        if (hasMemoryAddress()) {
            return EMPTY_BYTE_BUFFER_ADDRESS;
        }
        throw new UnsupportedOperationException();
    }

    @Override // io.netty.buffer.ByteBuf
    public boolean isContiguous() {
        return true;
    }

    @Override // io.netty.buffer.ByteBuf
    public String toString(Charset charset) {
        return "";
    }

    @Override // io.netty.buffer.ByteBuf
    public String toString(int index, int length, Charset charset) {
        checkIndex(index, length);
        return toString(charset);
    }

    @Override // io.netty.buffer.ByteBuf
    public int hashCode() {
        return 1;
    }

    @Override // io.netty.buffer.ByteBuf
    public boolean equals(Object obj) {
        return (obj instanceof ByteBuf) && !((ByteBuf) obj).isReadable();
    }

    @Override // io.netty.buffer.ByteBuf, java.lang.Comparable
    public int compareTo(ByteBuf buffer) {
        return buffer.isReadable() ? -1 : 0;
    }

    @Override // io.netty.buffer.ByteBuf
    public String toString() {
        return this.str;
    }

    @Override // io.netty.buffer.ByteBuf
    public boolean isReadable(int size) {
        return false;
    }

    @Override // io.netty.buffer.ByteBuf
    public boolean isWritable(int size) {
        return false;
    }

    @Override // io.netty.util.ReferenceCounted
    public int refCnt() {
        return 1;
    }

    @Override // io.netty.buffer.ByteBuf, io.netty.util.ReferenceCounted
    public ByteBuf retain() {
        return this;
    }

    @Override // io.netty.buffer.ByteBuf, io.netty.util.ReferenceCounted
    public ByteBuf retain(int increment) {
        return this;
    }

    @Override // io.netty.buffer.ByteBuf, io.netty.util.ReferenceCounted
    public ByteBuf touch() {
        return this;
    }

    @Override // io.netty.buffer.ByteBuf, io.netty.util.ReferenceCounted
    public ByteBuf touch(Object hint) {
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public boolean release() {
        return false;
    }

    @Override // io.netty.util.ReferenceCounted
    public boolean release(int decrement) {
        return false;
    }

    private ByteBuf checkIndex(int index) {
        if (index != 0) {
            throw new IndexOutOfBoundsException();
        }
        return this;
    }

    private ByteBuf checkIndex(int index, int length) {
        ObjectUtil.checkPositiveOrZero(length, "length");
        if (index != 0 || length != 0) {
            throw new IndexOutOfBoundsException();
        }
        return this;
    }

    private ByteBuf checkLength(int length) {
        ObjectUtil.checkPositiveOrZero(length, "length");
        if (length != 0) {
            throw new IndexOutOfBoundsException();
        }
        return this;
    }
}
