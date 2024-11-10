package io.netty.buffer;

import io.netty.util.ByteProcessor;
import io.netty.util.internal.ObjectUtil;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.charset.Charset;
import kotlin.UShort;

@Deprecated
/* loaded from: classes4.dex */
public class SwappedByteBuf extends ByteBuf {
    private final ByteBuf buf;
    private final ByteOrder order;

    public SwappedByteBuf(ByteBuf buf) {
        this.buf = (ByteBuf) ObjectUtil.checkNotNull(buf, "buf");
        if (buf.order() == ByteOrder.BIG_ENDIAN) {
            this.order = ByteOrder.LITTLE_ENDIAN;
        } else {
            this.order = ByteOrder.BIG_ENDIAN;
        }
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteOrder order() {
        return this.order;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf order(ByteOrder endianness) {
        if (ObjectUtil.checkNotNull(endianness, "endianness") == this.order) {
            return this;
        }
        return this.buf;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf unwrap() {
        return this.buf;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBufAllocator alloc() {
        return this.buf.alloc();
    }

    @Override // io.netty.buffer.ByteBuf
    public int capacity() {
        return this.buf.capacity();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf capacity(int newCapacity) {
        this.buf.capacity(newCapacity);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public int maxCapacity() {
        return this.buf.maxCapacity();
    }

    @Override // io.netty.buffer.ByteBuf
    public boolean isReadOnly() {
        return this.buf.isReadOnly();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf asReadOnly() {
        return Unpooled.unmodifiableBuffer(this);
    }

    @Override // io.netty.buffer.ByteBuf
    public boolean isDirect() {
        return this.buf.isDirect();
    }

    @Override // io.netty.buffer.ByteBuf
    public int readerIndex() {
        return this.buf.readerIndex();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf readerIndex(int readerIndex) {
        this.buf.readerIndex(readerIndex);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public int writerIndex() {
        return this.buf.writerIndex();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writerIndex(int writerIndex) {
        this.buf.writerIndex(writerIndex);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setIndex(int readerIndex, int writerIndex) {
        this.buf.setIndex(readerIndex, writerIndex);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public int readableBytes() {
        return this.buf.readableBytes();
    }

    @Override // io.netty.buffer.ByteBuf
    public int writableBytes() {
        return this.buf.writableBytes();
    }

    @Override // io.netty.buffer.ByteBuf
    public int maxWritableBytes() {
        return this.buf.maxWritableBytes();
    }

    @Override // io.netty.buffer.ByteBuf
    public int maxFastWritableBytes() {
        return this.buf.maxFastWritableBytes();
    }

    @Override // io.netty.buffer.ByteBuf
    public boolean isReadable() {
        return this.buf.isReadable();
    }

    @Override // io.netty.buffer.ByteBuf
    public boolean isReadable(int size) {
        return this.buf.isReadable(size);
    }

    @Override // io.netty.buffer.ByteBuf
    public boolean isWritable() {
        return this.buf.isWritable();
    }

    @Override // io.netty.buffer.ByteBuf
    public boolean isWritable(int size) {
        return this.buf.isWritable(size);
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf clear() {
        this.buf.clear();
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf markReaderIndex() {
        this.buf.markReaderIndex();
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf resetReaderIndex() {
        this.buf.resetReaderIndex();
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf markWriterIndex() {
        this.buf.markWriterIndex();
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf resetWriterIndex() {
        this.buf.resetWriterIndex();
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf discardReadBytes() {
        this.buf.discardReadBytes();
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf discardSomeReadBytes() {
        this.buf.discardSomeReadBytes();
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf ensureWritable(int writableBytes) {
        this.buf.ensureWritable(writableBytes);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public int ensureWritable(int minWritableBytes, boolean force) {
        return this.buf.ensureWritable(minWritableBytes, force);
    }

    @Override // io.netty.buffer.ByteBuf
    public boolean getBoolean(int index) {
        return this.buf.getBoolean(index);
    }

    @Override // io.netty.buffer.ByteBuf
    public byte getByte(int index) {
        return this.buf.getByte(index);
    }

    @Override // io.netty.buffer.ByteBuf
    public short getUnsignedByte(int index) {
        return this.buf.getUnsignedByte(index);
    }

    @Override // io.netty.buffer.ByteBuf
    public short getShort(int index) {
        return ByteBufUtil.swapShort(this.buf.getShort(index));
    }

    @Override // io.netty.buffer.ByteBuf
    public short getShortLE(int index) {
        return this.buf.getShortLE(index);
    }

    @Override // io.netty.buffer.ByteBuf
    public int getUnsignedShort(int index) {
        return getShort(index) & UShort.MAX_VALUE;
    }

    @Override // io.netty.buffer.ByteBuf
    public int getUnsignedShortLE(int index) {
        return getShortLE(index) & UShort.MAX_VALUE;
    }

    @Override // io.netty.buffer.ByteBuf
    public int getMedium(int index) {
        return ByteBufUtil.swapMedium(this.buf.getMedium(index));
    }

    @Override // io.netty.buffer.ByteBuf
    public int getMediumLE(int index) {
        return this.buf.getMediumLE(index);
    }

    @Override // io.netty.buffer.ByteBuf
    public int getUnsignedMedium(int index) {
        return getMedium(index) & 16777215;
    }

    @Override // io.netty.buffer.ByteBuf
    public int getUnsignedMediumLE(int index) {
        return getMediumLE(index) & 16777215;
    }

    @Override // io.netty.buffer.ByteBuf
    public int getInt(int index) {
        return ByteBufUtil.swapInt(this.buf.getInt(index));
    }

    @Override // io.netty.buffer.ByteBuf
    public int getIntLE(int index) {
        return this.buf.getIntLE(index);
    }

    @Override // io.netty.buffer.ByteBuf
    public long getUnsignedInt(int index) {
        return getInt(index) & 4294967295L;
    }

    @Override // io.netty.buffer.ByteBuf
    public long getUnsignedIntLE(int index) {
        return getIntLE(index) & 4294967295L;
    }

    @Override // io.netty.buffer.ByteBuf
    public long getLong(int index) {
        return ByteBufUtil.swapLong(this.buf.getLong(index));
    }

    @Override // io.netty.buffer.ByteBuf
    public long getLongLE(int index) {
        return this.buf.getLongLE(index);
    }

    @Override // io.netty.buffer.ByteBuf
    public char getChar(int index) {
        return (char) getShort(index);
    }

    @Override // io.netty.buffer.ByteBuf
    public float getFloat(int index) {
        return Float.intBitsToFloat(getInt(index));
    }

    @Override // io.netty.buffer.ByteBuf
    public double getDouble(int index) {
        return Double.longBitsToDouble(getLong(index));
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf getBytes(int index, ByteBuf dst) {
        this.buf.getBytes(index, dst);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf getBytes(int index, ByteBuf dst, int length) {
        this.buf.getBytes(index, dst, length);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf getBytes(int index, ByteBuf dst, int dstIndex, int length) {
        this.buf.getBytes(index, dst, dstIndex, length);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf getBytes(int index, byte[] dst) {
        this.buf.getBytes(index, dst);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf getBytes(int index, byte[] dst, int dstIndex, int length) {
        this.buf.getBytes(index, dst, dstIndex, length);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf getBytes(int index, ByteBuffer dst) {
        this.buf.getBytes(index, dst);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf getBytes(int index, OutputStream out, int length) throws IOException {
        this.buf.getBytes(index, out, length);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public int getBytes(int index, GatheringByteChannel out, int length) throws IOException {
        return this.buf.getBytes(index, out, length);
    }

    @Override // io.netty.buffer.ByteBuf
    public int getBytes(int index, FileChannel out, long position, int length) throws IOException {
        return this.buf.getBytes(index, out, position, length);
    }

    @Override // io.netty.buffer.ByteBuf
    public CharSequence getCharSequence(int index, int length, Charset charset) {
        return this.buf.getCharSequence(index, length, charset);
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setBoolean(int index, boolean value) {
        this.buf.setBoolean(index, value);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setByte(int index, int value) {
        this.buf.setByte(index, value);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setShort(int index, int value) {
        this.buf.setShort(index, ByteBufUtil.swapShort((short) value));
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setShortLE(int index, int value) {
        this.buf.setShortLE(index, (short) value);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setMedium(int index, int value) {
        this.buf.setMedium(index, ByteBufUtil.swapMedium(value));
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setMediumLE(int index, int value) {
        this.buf.setMediumLE(index, value);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setInt(int index, int value) {
        this.buf.setInt(index, ByteBufUtil.swapInt(value));
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setIntLE(int index, int value) {
        this.buf.setIntLE(index, value);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setLong(int index, long value) {
        this.buf.setLong(index, ByteBufUtil.swapLong(value));
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setLongLE(int index, long value) {
        this.buf.setLongLE(index, value);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setChar(int index, int value) {
        setShort(index, value);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setFloat(int index, float value) {
        setInt(index, Float.floatToRawIntBits(value));
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setDouble(int index, double value) {
        setLong(index, Double.doubleToRawLongBits(value));
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setBytes(int index, ByteBuf src) {
        this.buf.setBytes(index, src);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setBytes(int index, ByteBuf src, int length) {
        this.buf.setBytes(index, src, length);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setBytes(int index, ByteBuf src, int srcIndex, int length) {
        this.buf.setBytes(index, src, srcIndex, length);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setBytes(int index, byte[] src) {
        this.buf.setBytes(index, src);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setBytes(int index, byte[] src, int srcIndex, int length) {
        this.buf.setBytes(index, src, srcIndex, length);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setBytes(int index, ByteBuffer src) {
        this.buf.setBytes(index, src);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public int setBytes(int index, InputStream in, int length) throws IOException {
        return this.buf.setBytes(index, in, length);
    }

    @Override // io.netty.buffer.ByteBuf
    public int setBytes(int index, ScatteringByteChannel in, int length) throws IOException {
        return this.buf.setBytes(index, in, length);
    }

    @Override // io.netty.buffer.ByteBuf
    public int setBytes(int index, FileChannel in, long position, int length) throws IOException {
        return this.buf.setBytes(index, in, position, length);
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setZero(int index, int length) {
        this.buf.setZero(index, length);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public int setCharSequence(int index, CharSequence sequence, Charset charset) {
        return this.buf.setCharSequence(index, sequence, charset);
    }

    @Override // io.netty.buffer.ByteBuf
    public boolean readBoolean() {
        return this.buf.readBoolean();
    }

    @Override // io.netty.buffer.ByteBuf
    public byte readByte() {
        return this.buf.readByte();
    }

    @Override // io.netty.buffer.ByteBuf
    public short readUnsignedByte() {
        return this.buf.readUnsignedByte();
    }

    @Override // io.netty.buffer.ByteBuf
    public short readShort() {
        return ByteBufUtil.swapShort(this.buf.readShort());
    }

    @Override // io.netty.buffer.ByteBuf
    public short readShortLE() {
        return this.buf.readShortLE();
    }

    @Override // io.netty.buffer.ByteBuf
    public int readUnsignedShort() {
        return readShort() & UShort.MAX_VALUE;
    }

    @Override // io.netty.buffer.ByteBuf
    public int readUnsignedShortLE() {
        return readShortLE() & UShort.MAX_VALUE;
    }

    @Override // io.netty.buffer.ByteBuf
    public int readMedium() {
        return ByteBufUtil.swapMedium(this.buf.readMedium());
    }

    @Override // io.netty.buffer.ByteBuf
    public int readMediumLE() {
        return this.buf.readMediumLE();
    }

    @Override // io.netty.buffer.ByteBuf
    public int readUnsignedMedium() {
        return readMedium() & 16777215;
    }

    @Override // io.netty.buffer.ByteBuf
    public int readUnsignedMediumLE() {
        return readMediumLE() & 16777215;
    }

    @Override // io.netty.buffer.ByteBuf
    public int readInt() {
        return ByteBufUtil.swapInt(this.buf.readInt());
    }

    @Override // io.netty.buffer.ByteBuf
    public int readIntLE() {
        return this.buf.readIntLE();
    }

    @Override // io.netty.buffer.ByteBuf
    public long readUnsignedInt() {
        return readInt() & 4294967295L;
    }

    @Override // io.netty.buffer.ByteBuf
    public long readUnsignedIntLE() {
        return readIntLE() & 4294967295L;
    }

    @Override // io.netty.buffer.ByteBuf
    public long readLong() {
        return ByteBufUtil.swapLong(this.buf.readLong());
    }

    @Override // io.netty.buffer.ByteBuf
    public long readLongLE() {
        return this.buf.readLongLE();
    }

    @Override // io.netty.buffer.ByteBuf
    public char readChar() {
        return (char) readShort();
    }

    @Override // io.netty.buffer.ByteBuf
    public float readFloat() {
        return Float.intBitsToFloat(readInt());
    }

    @Override // io.netty.buffer.ByteBuf
    public double readDouble() {
        return Double.longBitsToDouble(readLong());
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf readBytes(int length) {
        return this.buf.readBytes(length).order(order());
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf readSlice(int length) {
        return this.buf.readSlice(length).order(this.order);
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf readRetainedSlice(int length) {
        return this.buf.readRetainedSlice(length).order(this.order);
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf readBytes(ByteBuf dst) {
        this.buf.readBytes(dst);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf readBytes(ByteBuf dst, int length) {
        this.buf.readBytes(dst, length);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf readBytes(ByteBuf dst, int dstIndex, int length) {
        this.buf.readBytes(dst, dstIndex, length);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf readBytes(byte[] dst) {
        this.buf.readBytes(dst);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf readBytes(byte[] dst, int dstIndex, int length) {
        this.buf.readBytes(dst, dstIndex, length);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf readBytes(ByteBuffer dst) {
        this.buf.readBytes(dst);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf readBytes(OutputStream out, int length) throws IOException {
        this.buf.readBytes(out, length);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public int readBytes(GatheringByteChannel out, int length) throws IOException {
        return this.buf.readBytes(out, length);
    }

    @Override // io.netty.buffer.ByteBuf
    public int readBytes(FileChannel out, long position, int length) throws IOException {
        return this.buf.readBytes(out, position, length);
    }

    @Override // io.netty.buffer.ByteBuf
    public CharSequence readCharSequence(int length, Charset charset) {
        return this.buf.readCharSequence(length, charset);
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf skipBytes(int length) {
        this.buf.skipBytes(length);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeBoolean(boolean value) {
        this.buf.writeBoolean(value);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeByte(int value) {
        this.buf.writeByte(value);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeShort(int value) {
        this.buf.writeShort(ByteBufUtil.swapShort((short) value));
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeShortLE(int value) {
        this.buf.writeShortLE((short) value);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeMedium(int value) {
        this.buf.writeMedium(ByteBufUtil.swapMedium(value));
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeMediumLE(int value) {
        this.buf.writeMediumLE(value);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeInt(int value) {
        this.buf.writeInt(ByteBufUtil.swapInt(value));
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeIntLE(int value) {
        this.buf.writeIntLE(value);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeLong(long value) {
        this.buf.writeLong(ByteBufUtil.swapLong(value));
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeLongLE(long value) {
        this.buf.writeLongLE(value);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeChar(int value) {
        writeShort(value);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeFloat(float value) {
        writeInt(Float.floatToRawIntBits(value));
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeDouble(double value) {
        writeLong(Double.doubleToRawLongBits(value));
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeBytes(ByteBuf src) {
        this.buf.writeBytes(src);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeBytes(ByteBuf src, int length) {
        this.buf.writeBytes(src, length);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeBytes(ByteBuf src, int srcIndex, int length) {
        this.buf.writeBytes(src, srcIndex, length);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeBytes(byte[] src) {
        this.buf.writeBytes(src);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeBytes(byte[] src, int srcIndex, int length) {
        this.buf.writeBytes(src, srcIndex, length);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeBytes(ByteBuffer src) {
        this.buf.writeBytes(src);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public int writeBytes(InputStream in, int length) throws IOException {
        return this.buf.writeBytes(in, length);
    }

    @Override // io.netty.buffer.ByteBuf
    public int writeBytes(ScatteringByteChannel in, int length) throws IOException {
        return this.buf.writeBytes(in, length);
    }

    @Override // io.netty.buffer.ByteBuf
    public int writeBytes(FileChannel in, long position, int length) throws IOException {
        return this.buf.writeBytes(in, position, length);
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeZero(int length) {
        this.buf.writeZero(length);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public int writeCharSequence(CharSequence sequence, Charset charset) {
        return this.buf.writeCharSequence(sequence, charset);
    }

    @Override // io.netty.buffer.ByteBuf
    public int indexOf(int fromIndex, int toIndex, byte value) {
        return this.buf.indexOf(fromIndex, toIndex, value);
    }

    @Override // io.netty.buffer.ByteBuf
    public int bytesBefore(byte value) {
        return this.buf.bytesBefore(value);
    }

    @Override // io.netty.buffer.ByteBuf
    public int bytesBefore(int length, byte value) {
        return this.buf.bytesBefore(length, value);
    }

    @Override // io.netty.buffer.ByteBuf
    public int bytesBefore(int index, int length, byte value) {
        return this.buf.bytesBefore(index, length, value);
    }

    @Override // io.netty.buffer.ByteBuf
    public int forEachByte(ByteProcessor processor) {
        return this.buf.forEachByte(processor);
    }

    @Override // io.netty.buffer.ByteBuf
    public int forEachByte(int index, int length, ByteProcessor processor) {
        return this.buf.forEachByte(index, length, processor);
    }

    @Override // io.netty.buffer.ByteBuf
    public int forEachByteDesc(ByteProcessor processor) {
        return this.buf.forEachByteDesc(processor);
    }

    @Override // io.netty.buffer.ByteBuf
    public int forEachByteDesc(int index, int length, ByteProcessor processor) {
        return this.buf.forEachByteDesc(index, length, processor);
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf copy() {
        return this.buf.copy().order(this.order);
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf copy(int index, int length) {
        return this.buf.copy(index, length).order(this.order);
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf slice() {
        return this.buf.slice().order(this.order);
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf retainedSlice() {
        return this.buf.retainedSlice().order(this.order);
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf slice(int index, int length) {
        return this.buf.slice(index, length).order(this.order);
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf retainedSlice(int index, int length) {
        return this.buf.retainedSlice(index, length).order(this.order);
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf duplicate() {
        return this.buf.duplicate().order(this.order);
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf retainedDuplicate() {
        return this.buf.retainedDuplicate().order(this.order);
    }

    @Override // io.netty.buffer.ByteBuf
    public int nioBufferCount() {
        return this.buf.nioBufferCount();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuffer nioBuffer() {
        return this.buf.nioBuffer().order(this.order);
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuffer nioBuffer(int index, int length) {
        return this.buf.nioBuffer(index, length).order(this.order);
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuffer internalNioBuffer(int index, int length) {
        return nioBuffer(index, length);
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuffer[] nioBuffers() {
        ByteBuffer[] nioBuffers = this.buf.nioBuffers();
        for (int i = 0; i < nioBuffers.length; i++) {
            nioBuffers[i] = nioBuffers[i].order(this.order);
        }
        return nioBuffers;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuffer[] nioBuffers(int index, int length) {
        ByteBuffer[] nioBuffers = this.buf.nioBuffers(index, length);
        for (int i = 0; i < nioBuffers.length; i++) {
            nioBuffers[i] = nioBuffers[i].order(this.order);
        }
        return nioBuffers;
    }

    @Override // io.netty.buffer.ByteBuf
    public boolean hasArray() {
        return this.buf.hasArray();
    }

    @Override // io.netty.buffer.ByteBuf
    public byte[] array() {
        return this.buf.array();
    }

    @Override // io.netty.buffer.ByteBuf
    public int arrayOffset() {
        return this.buf.arrayOffset();
    }

    @Override // io.netty.buffer.ByteBuf
    public boolean hasMemoryAddress() {
        return this.buf.hasMemoryAddress();
    }

    @Override // io.netty.buffer.ByteBuf
    public boolean isContiguous() {
        return this.buf.isContiguous();
    }

    @Override // io.netty.buffer.ByteBuf
    public long memoryAddress() {
        return this.buf.memoryAddress();
    }

    @Override // io.netty.buffer.ByteBuf
    public String toString(Charset charset) {
        return this.buf.toString(charset);
    }

    @Override // io.netty.buffer.ByteBuf
    public String toString(int index, int length, Charset charset) {
        return this.buf.toString(index, length, charset);
    }

    @Override // io.netty.util.ReferenceCounted
    public int refCnt() {
        return this.buf.refCnt();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // io.netty.buffer.ByteBuf
    public final boolean isAccessible() {
        return this.buf.isAccessible();
    }

    @Override // io.netty.buffer.ByteBuf, io.netty.util.ReferenceCounted
    public ByteBuf retain() {
        this.buf.retain();
        return this;
    }

    @Override // io.netty.buffer.ByteBuf, io.netty.util.ReferenceCounted
    public ByteBuf retain(int increment) {
        this.buf.retain(increment);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf, io.netty.util.ReferenceCounted
    public ByteBuf touch() {
        this.buf.touch();
        return this;
    }

    @Override // io.netty.buffer.ByteBuf, io.netty.util.ReferenceCounted
    public ByteBuf touch(Object hint) {
        this.buf.touch(hint);
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public boolean release() {
        return this.buf.release();
    }

    @Override // io.netty.util.ReferenceCounted
    public boolean release(int decrement) {
        return this.buf.release(decrement);
    }

    @Override // io.netty.buffer.ByteBuf
    public int hashCode() {
        return this.buf.hashCode();
    }

    @Override // io.netty.buffer.ByteBuf
    public boolean equals(Object obj) {
        if (obj instanceof ByteBuf) {
            return ByteBufUtil.equals(this, (ByteBuf) obj);
        }
        return false;
    }

    @Override // io.netty.buffer.ByteBuf, java.lang.Comparable
    public int compareTo(ByteBuf buffer) {
        return ByteBufUtil.compare(this, buffer);
    }

    @Override // io.netty.buffer.ByteBuf
    public String toString() {
        return "Swapped(" + this.buf + ')';
    }
}
