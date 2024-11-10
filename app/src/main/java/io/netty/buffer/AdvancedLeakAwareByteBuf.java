package io.netty.buffer;

import io.netty.util.ByteProcessor;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.ResourceLeakTracker;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.charset.Charset;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public final class AdvancedLeakAwareByteBuf extends SimpleLeakAwareByteBuf {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) AdvancedLeakAwareByteBuf.class);
    private static final String PROP_ACQUIRE_AND_RELEASE_ONLY = "io.netty.leakDetection.acquireAndReleaseOnly";
    private static final boolean ACQUIRE_AND_RELEASE_ONLY = SystemPropertyUtil.getBoolean(PROP_ACQUIRE_AND_RELEASE_ONLY, false);

    @Override // io.netty.buffer.SimpleLeakAwareByteBuf
    protected /* bridge */ /* synthetic */ SimpleLeakAwareByteBuf newLeakAwareByteBuf(ByteBuf byteBuf, ByteBuf byteBuf2, ResourceLeakTracker resourceLeakTracker) {
        return newLeakAwareByteBuf(byteBuf, byteBuf2, (ResourceLeakTracker<ByteBuf>) resourceLeakTracker);
    }

    static {
        if (logger.isDebugEnabled()) {
            logger.debug("-D{}: {}", PROP_ACQUIRE_AND_RELEASE_ONLY, Boolean.valueOf(ACQUIRE_AND_RELEASE_ONLY));
        }
        ResourceLeakDetector.addExclusions(AdvancedLeakAwareByteBuf.class, "touch", "recordLeakNonRefCountingOperation");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AdvancedLeakAwareByteBuf(ByteBuf buf, ResourceLeakTracker<ByteBuf> leak) {
        super(buf, leak);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AdvancedLeakAwareByteBuf(ByteBuf wrapped, ByteBuf trackedByteBuf, ResourceLeakTracker<ByteBuf> leak) {
        super(wrapped, trackedByteBuf, leak);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void recordLeakNonRefCountingOperation(ResourceLeakTracker<ByteBuf> leak) {
        if (!ACQUIRE_AND_RELEASE_ONLY) {
            leak.record();
        }
    }

    @Override // io.netty.buffer.SimpleLeakAwareByteBuf, io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf order(ByteOrder endianness) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.order(endianness);
    }

    @Override // io.netty.buffer.SimpleLeakAwareByteBuf, io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf slice() {
        recordLeakNonRefCountingOperation(this.leak);
        return super.slice();
    }

    @Override // io.netty.buffer.SimpleLeakAwareByteBuf, io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf slice(int index, int length) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.slice(index, length);
    }

    @Override // io.netty.buffer.SimpleLeakAwareByteBuf, io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf retainedSlice() {
        recordLeakNonRefCountingOperation(this.leak);
        return super.retainedSlice();
    }

    @Override // io.netty.buffer.SimpleLeakAwareByteBuf, io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf retainedSlice(int index, int length) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.retainedSlice(index, length);
    }

    @Override // io.netty.buffer.SimpleLeakAwareByteBuf, io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf retainedDuplicate() {
        recordLeakNonRefCountingOperation(this.leak);
        return super.retainedDuplicate();
    }

    @Override // io.netty.buffer.SimpleLeakAwareByteBuf, io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf readRetainedSlice(int length) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.readRetainedSlice(length);
    }

    @Override // io.netty.buffer.SimpleLeakAwareByteBuf, io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf duplicate() {
        recordLeakNonRefCountingOperation(this.leak);
        return super.duplicate();
    }

    @Override // io.netty.buffer.SimpleLeakAwareByteBuf, io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf readSlice(int length) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.readSlice(length);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf discardReadBytes() {
        recordLeakNonRefCountingOperation(this.leak);
        return super.discardReadBytes();
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf discardSomeReadBytes() {
        recordLeakNonRefCountingOperation(this.leak);
        return super.discardSomeReadBytes();
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf ensureWritable(int minWritableBytes) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.ensureWritable(minWritableBytes);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public int ensureWritable(int minWritableBytes, boolean force) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.ensureWritable(minWritableBytes, force);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public boolean getBoolean(int index) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.getBoolean(index);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public byte getByte(int index) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.getByte(index);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public short getUnsignedByte(int index) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.getUnsignedByte(index);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public short getShort(int index) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.getShort(index);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public int getUnsignedShort(int index) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.getUnsignedShort(index);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public int getMedium(int index) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.getMedium(index);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public int getUnsignedMedium(int index) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.getUnsignedMedium(index);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public int getInt(int index) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.getInt(index);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public long getUnsignedInt(int index) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.getUnsignedInt(index);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public long getLong(int index) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.getLong(index);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public char getChar(int index) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.getChar(index);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public float getFloat(int index) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.getFloat(index);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public double getDouble(int index) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.getDouble(index);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf getBytes(int index, ByteBuf dst) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.getBytes(index, dst);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf getBytes(int index, ByteBuf dst, int length) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.getBytes(index, dst, length);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf getBytes(int index, ByteBuf dst, int dstIndex, int length) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.getBytes(index, dst, dstIndex, length);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf getBytes(int index, byte[] dst) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.getBytes(index, dst);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf getBytes(int index, byte[] dst, int dstIndex, int length) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.getBytes(index, dst, dstIndex, length);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf getBytes(int index, ByteBuffer dst) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.getBytes(index, dst);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf getBytes(int index, OutputStream out, int length) throws IOException {
        recordLeakNonRefCountingOperation(this.leak);
        return super.getBytes(index, out, length);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public int getBytes(int index, GatheringByteChannel out, int length) throws IOException {
        recordLeakNonRefCountingOperation(this.leak);
        return super.getBytes(index, out, length);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public CharSequence getCharSequence(int index, int length, Charset charset) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.getCharSequence(index, length, charset);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf setBoolean(int index, boolean value) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.setBoolean(index, value);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf setByte(int index, int value) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.setByte(index, value);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf setShort(int index, int value) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.setShort(index, value);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf setMedium(int index, int value) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.setMedium(index, value);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf setInt(int index, int value) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.setInt(index, value);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf setLong(int index, long value) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.setLong(index, value);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf setChar(int index, int value) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.setChar(index, value);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf setFloat(int index, float value) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.setFloat(index, value);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf setDouble(int index, double value) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.setDouble(index, value);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf setBytes(int index, ByteBuf src) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.setBytes(index, src);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf setBytes(int index, ByteBuf src, int length) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.setBytes(index, src, length);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf setBytes(int index, ByteBuf src, int srcIndex, int length) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.setBytes(index, src, srcIndex, length);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf setBytes(int index, byte[] src) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.setBytes(index, src);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf setBytes(int index, byte[] src, int srcIndex, int length) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.setBytes(index, src, srcIndex, length);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf setBytes(int index, ByteBuffer src) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.setBytes(index, src);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public int setBytes(int index, InputStream in, int length) throws IOException {
        recordLeakNonRefCountingOperation(this.leak);
        return super.setBytes(index, in, length);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public int setBytes(int index, ScatteringByteChannel in, int length) throws IOException {
        recordLeakNonRefCountingOperation(this.leak);
        return super.setBytes(index, in, length);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf setZero(int index, int length) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.setZero(index, length);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public int setCharSequence(int index, CharSequence sequence, Charset charset) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.setCharSequence(index, sequence, charset);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public boolean readBoolean() {
        recordLeakNonRefCountingOperation(this.leak);
        return super.readBoolean();
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public byte readByte() {
        recordLeakNonRefCountingOperation(this.leak);
        return super.readByte();
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public short readUnsignedByte() {
        recordLeakNonRefCountingOperation(this.leak);
        return super.readUnsignedByte();
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public short readShort() {
        recordLeakNonRefCountingOperation(this.leak);
        return super.readShort();
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public int readUnsignedShort() {
        recordLeakNonRefCountingOperation(this.leak);
        return super.readUnsignedShort();
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public int readMedium() {
        recordLeakNonRefCountingOperation(this.leak);
        return super.readMedium();
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public int readUnsignedMedium() {
        recordLeakNonRefCountingOperation(this.leak);
        return super.readUnsignedMedium();
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public int readInt() {
        recordLeakNonRefCountingOperation(this.leak);
        return super.readInt();
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public long readUnsignedInt() {
        recordLeakNonRefCountingOperation(this.leak);
        return super.readUnsignedInt();
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public long readLong() {
        recordLeakNonRefCountingOperation(this.leak);
        return super.readLong();
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public char readChar() {
        recordLeakNonRefCountingOperation(this.leak);
        return super.readChar();
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public float readFloat() {
        recordLeakNonRefCountingOperation(this.leak);
        return super.readFloat();
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public double readDouble() {
        recordLeakNonRefCountingOperation(this.leak);
        return super.readDouble();
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf readBytes(int length) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.readBytes(length);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf readBytes(ByteBuf dst) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.readBytes(dst);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf readBytes(ByteBuf dst, int length) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.readBytes(dst, length);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf readBytes(ByteBuf dst, int dstIndex, int length) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.readBytes(dst, dstIndex, length);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf readBytes(byte[] dst) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.readBytes(dst);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf readBytes(byte[] dst, int dstIndex, int length) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.readBytes(dst, dstIndex, length);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf readBytes(ByteBuffer dst) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.readBytes(dst);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf readBytes(OutputStream out, int length) throws IOException {
        recordLeakNonRefCountingOperation(this.leak);
        return super.readBytes(out, length);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public int readBytes(GatheringByteChannel out, int length) throws IOException {
        recordLeakNonRefCountingOperation(this.leak);
        return super.readBytes(out, length);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public CharSequence readCharSequence(int length, Charset charset) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.readCharSequence(length, charset);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf skipBytes(int length) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.skipBytes(length);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf writeBoolean(boolean value) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.writeBoolean(value);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf writeByte(int value) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.writeByte(value);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf writeShort(int value) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.writeShort(value);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf writeMedium(int value) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.writeMedium(value);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf writeInt(int value) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.writeInt(value);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf writeLong(long value) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.writeLong(value);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf writeChar(int value) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.writeChar(value);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf writeFloat(float value) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.writeFloat(value);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf writeDouble(double value) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.writeDouble(value);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf writeBytes(ByteBuf src) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.writeBytes(src);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf writeBytes(ByteBuf src, int length) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.writeBytes(src, length);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf writeBytes(ByteBuf src, int srcIndex, int length) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.writeBytes(src, srcIndex, length);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf writeBytes(byte[] src) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.writeBytes(src);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf writeBytes(byte[] src, int srcIndex, int length) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.writeBytes(src, srcIndex, length);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf writeBytes(ByteBuffer src) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.writeBytes(src);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public int writeBytes(InputStream in, int length) throws IOException {
        recordLeakNonRefCountingOperation(this.leak);
        return super.writeBytes(in, length);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public int writeBytes(ScatteringByteChannel in, int length) throws IOException {
        recordLeakNonRefCountingOperation(this.leak);
        return super.writeBytes(in, length);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf writeZero(int length) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.writeZero(length);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public int indexOf(int fromIndex, int toIndex, byte value) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.indexOf(fromIndex, toIndex, value);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public int bytesBefore(byte value) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.bytesBefore(value);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public int bytesBefore(int length, byte value) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.bytesBefore(length, value);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public int bytesBefore(int index, int length, byte value) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.bytesBefore(index, length, value);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public int forEachByte(ByteProcessor processor) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.forEachByte(processor);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public int forEachByte(int index, int length, ByteProcessor processor) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.forEachByte(index, length, processor);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public int forEachByteDesc(ByteProcessor processor) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.forEachByteDesc(processor);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public int forEachByteDesc(int index, int length, ByteProcessor processor) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.forEachByteDesc(index, length, processor);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf copy() {
        recordLeakNonRefCountingOperation(this.leak);
        return super.copy();
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf copy(int index, int length) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.copy(index, length);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public int nioBufferCount() {
        recordLeakNonRefCountingOperation(this.leak);
        return super.nioBufferCount();
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuffer nioBuffer() {
        recordLeakNonRefCountingOperation(this.leak);
        return super.nioBuffer();
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuffer nioBuffer(int index, int length) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.nioBuffer(index, length);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuffer[] nioBuffers() {
        recordLeakNonRefCountingOperation(this.leak);
        return super.nioBuffers();
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuffer[] nioBuffers(int index, int length) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.nioBuffers(index, length);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuffer internalNioBuffer(int index, int length) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.internalNioBuffer(index, length);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public String toString(Charset charset) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.toString(charset);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public String toString(int index, int length, Charset charset) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.toString(index, length, charset);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf capacity(int newCapacity) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.capacity(newCapacity);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public short getShortLE(int index) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.getShortLE(index);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public int getUnsignedShortLE(int index) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.getUnsignedShortLE(index);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public int getMediumLE(int index) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.getMediumLE(index);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public int getUnsignedMediumLE(int index) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.getUnsignedMediumLE(index);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public int getIntLE(int index) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.getIntLE(index);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public long getUnsignedIntLE(int index) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.getUnsignedIntLE(index);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public long getLongLE(int index) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.getLongLE(index);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf setShortLE(int index, int value) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.setShortLE(index, value);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf setIntLE(int index, int value) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.setIntLE(index, value);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf setMediumLE(int index, int value) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.setMediumLE(index, value);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf setLongLE(int index, long value) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.setLongLE(index, value);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public short readShortLE() {
        recordLeakNonRefCountingOperation(this.leak);
        return super.readShortLE();
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public int readUnsignedShortLE() {
        recordLeakNonRefCountingOperation(this.leak);
        return super.readUnsignedShortLE();
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public int readMediumLE() {
        recordLeakNonRefCountingOperation(this.leak);
        return super.readMediumLE();
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public int readUnsignedMediumLE() {
        recordLeakNonRefCountingOperation(this.leak);
        return super.readUnsignedMediumLE();
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public int readIntLE() {
        recordLeakNonRefCountingOperation(this.leak);
        return super.readIntLE();
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public long readUnsignedIntLE() {
        recordLeakNonRefCountingOperation(this.leak);
        return super.readUnsignedIntLE();
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public long readLongLE() {
        recordLeakNonRefCountingOperation(this.leak);
        return super.readLongLE();
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf writeShortLE(int value) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.writeShortLE(value);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf writeMediumLE(int value) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.writeMediumLE(value);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf writeIntLE(int value) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.writeIntLE(value);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf writeLongLE(long value) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.writeLongLE(value);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public int writeCharSequence(CharSequence sequence, Charset charset) {
        recordLeakNonRefCountingOperation(this.leak);
        return super.writeCharSequence(sequence, charset);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public int getBytes(int index, FileChannel out, long position, int length) throws IOException {
        recordLeakNonRefCountingOperation(this.leak);
        return super.getBytes(index, out, position, length);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public int setBytes(int index, FileChannel in, long position, int length) throws IOException {
        recordLeakNonRefCountingOperation(this.leak);
        return super.setBytes(index, in, position, length);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public int readBytes(FileChannel out, long position, int length) throws IOException {
        recordLeakNonRefCountingOperation(this.leak);
        return super.readBytes(out, position, length);
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public int writeBytes(FileChannel in, long position, int length) throws IOException {
        recordLeakNonRefCountingOperation(this.leak);
        return super.writeBytes(in, position, length);
    }

    @Override // io.netty.buffer.SimpleLeakAwareByteBuf, io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf
    public ByteBuf asReadOnly() {
        recordLeakNonRefCountingOperation(this.leak);
        return super.asReadOnly();
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf, io.netty.util.ReferenceCounted
    public ByteBuf retain() {
        this.leak.record();
        return super.retain();
    }

    @Override // io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf, io.netty.util.ReferenceCounted
    public ByteBuf retain(int increment) {
        this.leak.record();
        return super.retain(increment);
    }

    @Override // io.netty.buffer.SimpleLeakAwareByteBuf, io.netty.buffer.WrappedByteBuf, io.netty.util.ReferenceCounted
    public boolean release() {
        this.leak.record();
        return super.release();
    }

    @Override // io.netty.buffer.SimpleLeakAwareByteBuf, io.netty.buffer.WrappedByteBuf, io.netty.util.ReferenceCounted
    public boolean release(int decrement) {
        this.leak.record();
        return super.release(decrement);
    }

    @Override // io.netty.buffer.SimpleLeakAwareByteBuf, io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf, io.netty.util.ReferenceCounted
    public ByteBuf touch() {
        this.leak.record();
        return this;
    }

    @Override // io.netty.buffer.SimpleLeakAwareByteBuf, io.netty.buffer.WrappedByteBuf, io.netty.buffer.ByteBuf, io.netty.util.ReferenceCounted
    public ByteBuf touch(Object hint) {
        this.leak.record(hint);
        return this;
    }

    @Override // io.netty.buffer.SimpleLeakAwareByteBuf
    protected AdvancedLeakAwareByteBuf newLeakAwareByteBuf(ByteBuf buf, ByteBuf trackedByteBuf, ResourceLeakTracker<ByteBuf> leakTracker) {
        return new AdvancedLeakAwareByteBuf(buf, trackedByteBuf, leakTracker);
    }
}
