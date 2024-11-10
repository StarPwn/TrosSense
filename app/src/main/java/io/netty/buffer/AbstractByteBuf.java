package io.netty.buffer;

import androidx.core.view.ViewCompat;
import io.netty.util.AsciiString;
import io.netty.util.ByteProcessor;
import io.netty.util.CharsetUtil;
import io.netty.util.IllegalReferenceCountException;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.ResourceLeakDetectorFactory;
import io.netty.util.internal.MathUtil;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;
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
import kotlin.UShort;

/* loaded from: classes4.dex */
public abstract class AbstractByteBuf extends ByteBuf {
    private static final String LEGACY_PROP_CHECK_ACCESSIBLE = "io.netty.buffer.bytebuf.checkAccessible";
    private static final String PROP_CHECK_ACCESSIBLE = "io.netty.buffer.checkAccessible";
    private static final String PROP_CHECK_BOUNDS = "io.netty.buffer.checkBounds";
    static final boolean checkAccessible;
    private static final boolean checkBounds;
    static final ResourceLeakDetector<ByteBuf> leakDetector;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) AbstractByteBuf.class);
    private int markedReaderIndex;
    private int markedWriterIndex;
    private int maxCapacity;
    int readerIndex;
    int writerIndex;

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract byte _getByte(int i);

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract int _getInt(int i);

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract int _getIntLE(int i);

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract long _getLong(int i);

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract long _getLongLE(int i);

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract short _getShort(int i);

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract short _getShortLE(int i);

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract int _getUnsignedMedium(int i);

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract int _getUnsignedMediumLE(int i);

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract void _setByte(int i, int i2);

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract void _setInt(int i, int i2);

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract void _setIntLE(int i, int i2);

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract void _setLong(int i, long j);

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract void _setLongLE(int i, long j);

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract void _setMedium(int i, int i2);

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract void _setMediumLE(int i, int i2);

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract void _setShort(int i, int i2);

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract void _setShortLE(int i, int i2);

    static {
        if (SystemPropertyUtil.contains(PROP_CHECK_ACCESSIBLE)) {
            checkAccessible = SystemPropertyUtil.getBoolean(PROP_CHECK_ACCESSIBLE, true);
        } else {
            checkAccessible = SystemPropertyUtil.getBoolean(LEGACY_PROP_CHECK_ACCESSIBLE, true);
        }
        checkBounds = SystemPropertyUtil.getBoolean(PROP_CHECK_BOUNDS, true);
        if (logger.isDebugEnabled()) {
            logger.debug("-D{}: {}", PROP_CHECK_ACCESSIBLE, Boolean.valueOf(checkAccessible));
            logger.debug("-D{}: {}", PROP_CHECK_BOUNDS, Boolean.valueOf(checkBounds));
        }
        leakDetector = ResourceLeakDetectorFactory.instance().newResourceLeakDetector(ByteBuf.class);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractByteBuf(int maxCapacity) {
        ObjectUtil.checkPositiveOrZero(maxCapacity, "maxCapacity");
        this.maxCapacity = maxCapacity;
    }

    @Override // io.netty.buffer.ByteBuf
    public boolean isReadOnly() {
        return false;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf asReadOnly() {
        if (isReadOnly()) {
            return this;
        }
        return Unpooled.unmodifiableBuffer(this);
    }

    @Override // io.netty.buffer.ByteBuf
    public int maxCapacity() {
        return this.maxCapacity;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void maxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    @Override // io.netty.buffer.ByteBuf
    public int readerIndex() {
        return this.readerIndex;
    }

    private static void checkIndexBounds(int readerIndex, int writerIndex, int capacity) {
        if (readerIndex < 0 || readerIndex > writerIndex || writerIndex > capacity) {
            throw new IndexOutOfBoundsException(String.format("readerIndex: %d, writerIndex: %d (expected: 0 <= readerIndex <= writerIndex <= capacity(%d))", Integer.valueOf(readerIndex), Integer.valueOf(writerIndex), Integer.valueOf(capacity)));
        }
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf readerIndex(int readerIndex) {
        if (checkBounds) {
            checkIndexBounds(readerIndex, this.writerIndex, capacity());
        }
        this.readerIndex = readerIndex;
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public int writerIndex() {
        return this.writerIndex;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writerIndex(int writerIndex) {
        if (checkBounds) {
            checkIndexBounds(this.readerIndex, writerIndex, capacity());
        }
        this.writerIndex = writerIndex;
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setIndex(int readerIndex, int writerIndex) {
        if (checkBounds) {
            checkIndexBounds(readerIndex, writerIndex, capacity());
        }
        setIndex0(readerIndex, writerIndex);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf clear() {
        this.writerIndex = 0;
        this.readerIndex = 0;
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public boolean isReadable() {
        return this.writerIndex > this.readerIndex;
    }

    @Override // io.netty.buffer.ByteBuf
    public boolean isReadable(int numBytes) {
        return this.writerIndex - this.readerIndex >= numBytes;
    }

    @Override // io.netty.buffer.ByteBuf
    public boolean isWritable() {
        return capacity() > this.writerIndex;
    }

    @Override // io.netty.buffer.ByteBuf
    public boolean isWritable(int numBytes) {
        return capacity() - this.writerIndex >= numBytes;
    }

    @Override // io.netty.buffer.ByteBuf
    public int readableBytes() {
        return this.writerIndex - this.readerIndex;
    }

    @Override // io.netty.buffer.ByteBuf
    public int writableBytes() {
        return capacity() - this.writerIndex;
    }

    @Override // io.netty.buffer.ByteBuf
    public int maxWritableBytes() {
        return maxCapacity() - this.writerIndex;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf markReaderIndex() {
        this.markedReaderIndex = this.readerIndex;
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf resetReaderIndex() {
        readerIndex(this.markedReaderIndex);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf markWriterIndex() {
        this.markedWriterIndex = this.writerIndex;
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf resetWriterIndex() {
        writerIndex(this.markedWriterIndex);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf discardReadBytes() {
        if (this.readerIndex == 0) {
            ensureAccessible();
            return this;
        }
        if (this.readerIndex != this.writerIndex) {
            setBytes(0, this, this.readerIndex, this.writerIndex - this.readerIndex);
            this.writerIndex -= this.readerIndex;
            adjustMarkers(this.readerIndex);
            this.readerIndex = 0;
        } else {
            ensureAccessible();
            adjustMarkers(this.readerIndex);
            this.readerIndex = 0;
            this.writerIndex = 0;
        }
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf discardSomeReadBytes() {
        if (this.readerIndex > 0) {
            if (this.readerIndex == this.writerIndex) {
                ensureAccessible();
                adjustMarkers(this.readerIndex);
                this.readerIndex = 0;
                this.writerIndex = 0;
                return this;
            }
            if (this.readerIndex >= (capacity() >>> 1)) {
                setBytes(0, this, this.readerIndex, this.writerIndex - this.readerIndex);
                this.writerIndex -= this.readerIndex;
                adjustMarkers(this.readerIndex);
                this.readerIndex = 0;
                return this;
            }
        }
        ensureAccessible();
        return this;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void adjustMarkers(int decrement) {
        if (this.markedReaderIndex <= decrement) {
            this.markedReaderIndex = 0;
            if (this.markedWriterIndex <= decrement) {
                this.markedWriterIndex = 0;
                return;
            } else {
                this.markedWriterIndex -= decrement;
                return;
            }
        }
        this.markedReaderIndex -= decrement;
        this.markedWriterIndex -= decrement;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void trimIndicesToCapacity(int newCapacity) {
        if (writerIndex() > newCapacity) {
            setIndex0(Math.min(readerIndex(), newCapacity), newCapacity);
        }
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf ensureWritable(int minWritableBytes) {
        ensureWritable0(ObjectUtil.checkPositiveOrZero(minWritableBytes, "minWritableBytes"));
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void ensureWritable0(int minWritableBytes) {
        int writerIndex = writerIndex();
        int targetCapacity = writerIndex + minWritableBytes;
        if ((targetCapacity <= capacity()) & (targetCapacity >= 0)) {
            ensureAccessible();
            return;
        }
        if (checkBounds && (targetCapacity < 0 || targetCapacity > this.maxCapacity)) {
            ensureAccessible();
            throw new IndexOutOfBoundsException(String.format("writerIndex(%d) + minWritableBytes(%d) exceeds maxCapacity(%d): %s", Integer.valueOf(writerIndex), Integer.valueOf(minWritableBytes), Integer.valueOf(this.maxCapacity), this));
        }
        int fastWritable = maxFastWritableBytes();
        int newCapacity = fastWritable >= minWritableBytes ? writerIndex + fastWritable : alloc().calculateNewCapacity(targetCapacity, this.maxCapacity);
        capacity(newCapacity);
    }

    @Override // io.netty.buffer.ByteBuf
    public int ensureWritable(int minWritableBytes, boolean force) {
        ensureAccessible();
        ObjectUtil.checkPositiveOrZero(minWritableBytes, "minWritableBytes");
        if (minWritableBytes <= writableBytes()) {
            return 0;
        }
        int maxCapacity = maxCapacity();
        int writerIndex = writerIndex();
        if (minWritableBytes > maxCapacity - writerIndex) {
            if (!force || capacity() == maxCapacity) {
                return 1;
            }
            capacity(maxCapacity);
            return 3;
        }
        int fastWritable = maxFastWritableBytes();
        int newCapacity = fastWritable >= minWritableBytes ? writerIndex + fastWritable : alloc().calculateNewCapacity(writerIndex + minWritableBytes, maxCapacity);
        capacity(newCapacity);
        return 2;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf order(ByteOrder endianness) {
        if (endianness == order()) {
            return this;
        }
        ObjectUtil.checkNotNull(endianness, "endianness");
        return newSwappedByteBuf();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public SwappedByteBuf newSwappedByteBuf() {
        return new SwappedByteBuf(this);
    }

    @Override // io.netty.buffer.ByteBuf
    public byte getByte(int index) {
        checkIndex(index);
        return _getByte(index);
    }

    @Override // io.netty.buffer.ByteBuf
    public boolean getBoolean(int index) {
        return getByte(index) != 0;
    }

    @Override // io.netty.buffer.ByteBuf
    public short getUnsignedByte(int index) {
        return (short) (getByte(index) & 255);
    }

    @Override // io.netty.buffer.ByteBuf
    public short getShort(int index) {
        checkIndex(index, 2);
        return _getShort(index);
    }

    @Override // io.netty.buffer.ByteBuf
    public short getShortLE(int index) {
        checkIndex(index, 2);
        return _getShortLE(index);
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
    public int getUnsignedMedium(int index) {
        checkIndex(index, 3);
        return _getUnsignedMedium(index);
    }

    @Override // io.netty.buffer.ByteBuf
    public int getUnsignedMediumLE(int index) {
        checkIndex(index, 3);
        return _getUnsignedMediumLE(index);
    }

    @Override // io.netty.buffer.ByteBuf
    public int getMedium(int index) {
        int value = getUnsignedMedium(index);
        if ((8388608 & value) != 0) {
            return value | ViewCompat.MEASURED_STATE_MASK;
        }
        return value;
    }

    @Override // io.netty.buffer.ByteBuf
    public int getMediumLE(int index) {
        int value = getUnsignedMediumLE(index);
        if ((8388608 & value) != 0) {
            return value | ViewCompat.MEASURED_STATE_MASK;
        }
        return value;
    }

    @Override // io.netty.buffer.ByteBuf
    public int getInt(int index) {
        checkIndex(index, 4);
        return _getInt(index);
    }

    @Override // io.netty.buffer.ByteBuf
    public int getIntLE(int index) {
        checkIndex(index, 4);
        return _getIntLE(index);
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
        checkIndex(index, 8);
        return _getLong(index);
    }

    @Override // io.netty.buffer.ByteBuf
    public long getLongLE(int index) {
        checkIndex(index, 8);
        return _getLongLE(index);
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
    public ByteBuf getBytes(int index, byte[] dst) {
        getBytes(index, dst, 0, dst.length);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf getBytes(int index, ByteBuf dst) {
        getBytes(index, dst, dst.writableBytes());
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf getBytes(int index, ByteBuf dst, int length) {
        getBytes(index, dst, dst.writerIndex(), length);
        dst.writerIndex(dst.writerIndex() + length);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public CharSequence getCharSequence(int index, int length, Charset charset) {
        if (CharsetUtil.US_ASCII.equals(charset) || CharsetUtil.ISO_8859_1.equals(charset)) {
            return new AsciiString(ByteBufUtil.getBytes(this, index, length, true), false);
        }
        return toString(index, length, charset);
    }

    @Override // io.netty.buffer.ByteBuf
    public CharSequence readCharSequence(int length, Charset charset) {
        CharSequence sequence = getCharSequence(this.readerIndex, length, charset);
        this.readerIndex += length;
        return sequence;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setByte(int index, int value) {
        checkIndex(index);
        _setByte(index, value);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setBoolean(int i, boolean z) {
        setByte(i, z ? 1 : 0);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setShort(int index, int value) {
        checkIndex(index, 2);
        _setShort(index, value);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setShortLE(int index, int value) {
        checkIndex(index, 2);
        _setShortLE(index, value);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setChar(int index, int value) {
        setShort(index, value);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setMedium(int index, int value) {
        checkIndex(index, 3);
        _setMedium(index, value);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setMediumLE(int index, int value) {
        checkIndex(index, 3);
        _setMediumLE(index, value);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setInt(int index, int value) {
        checkIndex(index, 4);
        _setInt(index, value);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setIntLE(int index, int value) {
        checkIndex(index, 4);
        _setIntLE(index, value);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setFloat(int index, float value) {
        setInt(index, Float.floatToRawIntBits(value));
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setLong(int index, long value) {
        checkIndex(index, 8);
        _setLong(index, value);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setLongLE(int index, long value) {
        checkIndex(index, 8);
        _setLongLE(index, value);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setDouble(int index, double value) {
        setLong(index, Double.doubleToRawLongBits(value));
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setBytes(int index, byte[] src) {
        setBytes(index, src, 0, src.length);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setBytes(int index, ByteBuf src) {
        setBytes(index, src, src.readableBytes());
        return this;
    }

    private static void checkReadableBounds(ByteBuf src, int length) {
        if (length > src.readableBytes()) {
            throw new IndexOutOfBoundsException(String.format("length(%d) exceeds src.readableBytes(%d) where src is: %s", Integer.valueOf(length), Integer.valueOf(src.readableBytes()), src));
        }
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setBytes(int index, ByteBuf src, int length) {
        checkIndex(index, length);
        ObjectUtil.checkNotNull(src, "src");
        if (checkBounds) {
            checkReadableBounds(src, length);
        }
        setBytes(index, src, src.readerIndex(), length);
        src.readerIndex(src.readerIndex() + length);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf setZero(int index, int length) {
        if (length == 0) {
            return this;
        }
        checkIndex(index, length);
        int nLong = length >>> 3;
        int nBytes = length & 7;
        for (int i = nLong; i > 0; i--) {
            _setLong(index, 0L);
            index += 8;
        }
        if (nBytes == 4) {
            _setInt(index, 0);
        } else if (nBytes < 4) {
            for (int i2 = nBytes; i2 > 0; i2--) {
                _setByte(index, 0);
                index++;
            }
        } else {
            _setInt(index, 0);
            int index2 = index + 4;
            for (int i3 = nBytes - 4; i3 > 0; i3--) {
                _setByte(index2, 0);
                index2++;
            }
        }
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public int setCharSequence(int index, CharSequence sequence, Charset charset) {
        return setCharSequence0(index, sequence, charset, false);
    }

    private int setCharSequence0(int index, CharSequence sequence, Charset charset, boolean expand) {
        if (charset.equals(CharsetUtil.UTF_8)) {
            int length = ByteBufUtil.utf8MaxBytes(sequence);
            if (expand) {
                ensureWritable0(length);
                checkIndex0(index, length);
            } else {
                checkIndex(index, length);
            }
            return ByteBufUtil.writeUtf8(this, index, length, sequence, sequence.length());
        }
        if (charset.equals(CharsetUtil.US_ASCII) || charset.equals(CharsetUtil.ISO_8859_1)) {
            int length2 = sequence.length();
            if (expand) {
                ensureWritable0(length2);
                checkIndex0(index, length2);
            } else {
                checkIndex(index, length2);
            }
            return ByteBufUtil.writeAscii(this, index, sequence, length2);
        }
        byte[] bytes = sequence.toString().getBytes(charset);
        if (expand) {
            ensureWritable0(bytes.length);
        }
        setBytes(index, bytes);
        return bytes.length;
    }

    @Override // io.netty.buffer.ByteBuf
    public byte readByte() {
        checkReadableBytes0(1);
        int i = this.readerIndex;
        byte b = _getByte(i);
        this.readerIndex = i + 1;
        return b;
    }

    @Override // io.netty.buffer.ByteBuf
    public boolean readBoolean() {
        return readByte() != 0;
    }

    @Override // io.netty.buffer.ByteBuf
    public short readUnsignedByte() {
        return (short) (readByte() & 255);
    }

    @Override // io.netty.buffer.ByteBuf
    public short readShort() {
        checkReadableBytes0(2);
        short v = _getShort(this.readerIndex);
        this.readerIndex += 2;
        return v;
    }

    @Override // io.netty.buffer.ByteBuf
    public short readShortLE() {
        checkReadableBytes0(2);
        short v = _getShortLE(this.readerIndex);
        this.readerIndex += 2;
        return v;
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
        int value = readUnsignedMedium();
        if ((8388608 & value) != 0) {
            return value | ViewCompat.MEASURED_STATE_MASK;
        }
        return value;
    }

    @Override // io.netty.buffer.ByteBuf
    public int readMediumLE() {
        int value = readUnsignedMediumLE();
        if ((8388608 & value) != 0) {
            return value | ViewCompat.MEASURED_STATE_MASK;
        }
        return value;
    }

    @Override // io.netty.buffer.ByteBuf
    public int readUnsignedMedium() {
        checkReadableBytes0(3);
        int v = _getUnsignedMedium(this.readerIndex);
        this.readerIndex += 3;
        return v;
    }

    @Override // io.netty.buffer.ByteBuf
    public int readUnsignedMediumLE() {
        checkReadableBytes0(3);
        int v = _getUnsignedMediumLE(this.readerIndex);
        this.readerIndex += 3;
        return v;
    }

    @Override // io.netty.buffer.ByteBuf
    public int readInt() {
        checkReadableBytes0(4);
        int v = _getInt(this.readerIndex);
        this.readerIndex += 4;
        return v;
    }

    @Override // io.netty.buffer.ByteBuf
    public int readIntLE() {
        checkReadableBytes0(4);
        int v = _getIntLE(this.readerIndex);
        this.readerIndex += 4;
        return v;
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
        checkReadableBytes0(8);
        long v = _getLong(this.readerIndex);
        this.readerIndex += 8;
        return v;
    }

    @Override // io.netty.buffer.ByteBuf
    public long readLongLE() {
        checkReadableBytes0(8);
        long v = _getLongLE(this.readerIndex);
        this.readerIndex += 8;
        return v;
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
        checkReadableBytes(length);
        if (length == 0) {
            return Unpooled.EMPTY_BUFFER;
        }
        ByteBuf buf = alloc().buffer(length, this.maxCapacity);
        buf.writeBytes(this, this.readerIndex, length);
        this.readerIndex += length;
        return buf;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf readSlice(int length) {
        checkReadableBytes(length);
        ByteBuf slice = slice(this.readerIndex, length);
        this.readerIndex += length;
        return slice;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf readRetainedSlice(int length) {
        checkReadableBytes(length);
        ByteBuf slice = retainedSlice(this.readerIndex, length);
        this.readerIndex += length;
        return slice;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf readBytes(byte[] dst, int dstIndex, int length) {
        checkReadableBytes(length);
        getBytes(this.readerIndex, dst, dstIndex, length);
        this.readerIndex += length;
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf readBytes(byte[] dst) {
        readBytes(dst, 0, dst.length);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf readBytes(ByteBuf dst) {
        readBytes(dst, dst.writableBytes());
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf readBytes(ByteBuf dst, int length) {
        if (checkBounds && length > dst.writableBytes()) {
            throw new IndexOutOfBoundsException(String.format("length(%d) exceeds dst.writableBytes(%d) where dst is: %s", Integer.valueOf(length), Integer.valueOf(dst.writableBytes()), dst));
        }
        readBytes(dst, dst.writerIndex(), length);
        dst.writerIndex(dst.writerIndex() + length);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf readBytes(ByteBuf dst, int dstIndex, int length) {
        checkReadableBytes(length);
        getBytes(this.readerIndex, dst, dstIndex, length);
        this.readerIndex += length;
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf readBytes(ByteBuffer dst) {
        int length = dst.remaining();
        checkReadableBytes(length);
        getBytes(this.readerIndex, dst);
        this.readerIndex += length;
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public int readBytes(GatheringByteChannel out, int length) throws IOException {
        checkReadableBytes(length);
        int readBytes = getBytes(this.readerIndex, out, length);
        this.readerIndex += readBytes;
        return readBytes;
    }

    @Override // io.netty.buffer.ByteBuf
    public int readBytes(FileChannel out, long position, int length) throws IOException {
        checkReadableBytes(length);
        int readBytes = getBytes(this.readerIndex, out, position, length);
        this.readerIndex += readBytes;
        return readBytes;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf readBytes(OutputStream out, int length) throws IOException {
        checkReadableBytes(length);
        getBytes(this.readerIndex, out, length);
        this.readerIndex += length;
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf skipBytes(int length) {
        checkReadableBytes(length);
        this.readerIndex += length;
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeBoolean(boolean z) {
        writeByte(z ? 1 : 0);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeByte(int value) {
        ensureWritable0(1);
        int i = this.writerIndex;
        this.writerIndex = i + 1;
        _setByte(i, value);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeShort(int value) {
        ensureWritable0(2);
        _setShort(this.writerIndex, value);
        this.writerIndex += 2;
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeShortLE(int value) {
        ensureWritable0(2);
        _setShortLE(this.writerIndex, value);
        this.writerIndex += 2;
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeMedium(int value) {
        ensureWritable0(3);
        _setMedium(this.writerIndex, value);
        this.writerIndex += 3;
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeMediumLE(int value) {
        ensureWritable0(3);
        _setMediumLE(this.writerIndex, value);
        this.writerIndex += 3;
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeInt(int value) {
        ensureWritable0(4);
        _setInt(this.writerIndex, value);
        this.writerIndex += 4;
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeIntLE(int value) {
        ensureWritable0(4);
        _setIntLE(this.writerIndex, value);
        this.writerIndex += 4;
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeLong(long value) {
        ensureWritable0(8);
        _setLong(this.writerIndex, value);
        this.writerIndex += 8;
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeLongLE(long value) {
        ensureWritable0(8);
        _setLongLE(this.writerIndex, value);
        this.writerIndex += 8;
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
    public ByteBuf writeBytes(byte[] src, int srcIndex, int length) {
        ensureWritable(length);
        setBytes(this.writerIndex, src, srcIndex, length);
        this.writerIndex += length;
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeBytes(byte[] src) {
        writeBytes(src, 0, src.length);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeBytes(ByteBuf src) {
        writeBytes(src, src.readableBytes());
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeBytes(ByteBuf src, int length) {
        if (checkBounds) {
            checkReadableBounds(src, length);
        }
        writeBytes(src, src.readerIndex(), length);
        src.readerIndex(src.readerIndex() + length);
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeBytes(ByteBuf src, int srcIndex, int length) {
        ensureWritable(length);
        setBytes(this.writerIndex, src, srcIndex, length);
        this.writerIndex += length;
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeBytes(ByteBuffer src) {
        int length = src.remaining();
        ensureWritable0(length);
        setBytes(this.writerIndex, src);
        this.writerIndex += length;
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public int writeBytes(InputStream in, int length) throws IOException {
        ensureWritable(length);
        int writtenBytes = setBytes(this.writerIndex, in, length);
        if (writtenBytes > 0) {
            this.writerIndex += writtenBytes;
        }
        return writtenBytes;
    }

    @Override // io.netty.buffer.ByteBuf
    public int writeBytes(ScatteringByteChannel in, int length) throws IOException {
        ensureWritable(length);
        int writtenBytes = setBytes(this.writerIndex, in, length);
        if (writtenBytes > 0) {
            this.writerIndex += writtenBytes;
        }
        return writtenBytes;
    }

    @Override // io.netty.buffer.ByteBuf
    public int writeBytes(FileChannel in, long position, int length) throws IOException {
        ensureWritable(length);
        int writtenBytes = setBytes(this.writerIndex, in, position, length);
        if (writtenBytes > 0) {
            this.writerIndex += writtenBytes;
        }
        return writtenBytes;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf writeZero(int length) {
        if (length == 0) {
            return this;
        }
        ensureWritable(length);
        int wIndex = this.writerIndex;
        checkIndex0(wIndex, length);
        int nLong = length >>> 3;
        int nBytes = length & 7;
        for (int i = nLong; i > 0; i--) {
            _setLong(wIndex, 0L);
            wIndex += 8;
        }
        if (nBytes == 4) {
            _setInt(wIndex, 0);
            wIndex += 4;
        } else if (nBytes < 4) {
            for (int i2 = nBytes; i2 > 0; i2--) {
                _setByte(wIndex, 0);
                wIndex++;
            }
        } else {
            _setInt(wIndex, 0);
            wIndex += 4;
            for (int i3 = nBytes - 4; i3 > 0; i3--) {
                _setByte(wIndex, 0);
                wIndex++;
            }
        }
        this.writerIndex = wIndex;
        return this;
    }

    @Override // io.netty.buffer.ByteBuf
    public int writeCharSequence(CharSequence sequence, Charset charset) {
        int written = setCharSequence0(this.writerIndex, sequence, charset, true);
        this.writerIndex += written;
        return written;
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf copy() {
        return copy(this.readerIndex, readableBytes());
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf duplicate() {
        ensureAccessible();
        return new UnpooledDuplicatedByteBuf(this);
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf retainedDuplicate() {
        return duplicate().retain();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf slice() {
        return slice(this.readerIndex, readableBytes());
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf retainedSlice() {
        return slice().retain();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf slice(int index, int length) {
        ensureAccessible();
        return new UnpooledSlicedByteBuf(this, index, length);
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuf retainedSlice(int index, int length) {
        return slice(index, length).retain();
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuffer nioBuffer() {
        return nioBuffer(this.readerIndex, readableBytes());
    }

    @Override // io.netty.buffer.ByteBuf
    public ByteBuffer[] nioBuffers() {
        return nioBuffers(this.readerIndex, readableBytes());
    }

    @Override // io.netty.buffer.ByteBuf
    public String toString(Charset charset) {
        return toString(this.readerIndex, readableBytes(), charset);
    }

    @Override // io.netty.buffer.ByteBuf
    public String toString(int index, int length, Charset charset) {
        return ByteBufUtil.decodeString(this, index, length, charset);
    }

    @Override // io.netty.buffer.ByteBuf
    public int indexOf(int fromIndex, int toIndex, byte value) {
        if (fromIndex <= toIndex) {
            return ByteBufUtil.firstIndexOf(this, fromIndex, toIndex, value);
        }
        return ByteBufUtil.lastIndexOf(this, fromIndex, toIndex, value);
    }

    @Override // io.netty.buffer.ByteBuf
    public int bytesBefore(byte value) {
        return bytesBefore(readerIndex(), readableBytes(), value);
    }

    @Override // io.netty.buffer.ByteBuf
    public int bytesBefore(int length, byte value) {
        checkReadableBytes(length);
        return bytesBefore(readerIndex(), length, value);
    }

    @Override // io.netty.buffer.ByteBuf
    public int bytesBefore(int index, int length, byte value) {
        int endIndex = indexOf(index, index + length, value);
        if (endIndex < 0) {
            return -1;
        }
        return endIndex - index;
    }

    @Override // io.netty.buffer.ByteBuf
    public int forEachByte(ByteProcessor processor) {
        ensureAccessible();
        try {
            return forEachByteAsc0(this.readerIndex, this.writerIndex, processor);
        } catch (Exception e) {
            PlatformDependent.throwException(e);
            return -1;
        }
    }

    @Override // io.netty.buffer.ByteBuf
    public int forEachByte(int index, int length, ByteProcessor processor) {
        checkIndex(index, length);
        try {
            return forEachByteAsc0(index, index + length, processor);
        } catch (Exception e) {
            PlatformDependent.throwException(e);
            return -1;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int forEachByteAsc0(int start, int end, ByteProcessor processor) throws Exception {
        while (start < end) {
            if (processor.process(_getByte(start))) {
                start++;
            } else {
                return start;
            }
        }
        return -1;
    }

    @Override // io.netty.buffer.ByteBuf
    public int forEachByteDesc(ByteProcessor processor) {
        ensureAccessible();
        try {
            return forEachByteDesc0(this.writerIndex - 1, this.readerIndex, processor);
        } catch (Exception e) {
            PlatformDependent.throwException(e);
            return -1;
        }
    }

    @Override // io.netty.buffer.ByteBuf
    public int forEachByteDesc(int index, int length, ByteProcessor processor) {
        checkIndex(index, length);
        try {
            return forEachByteDesc0((index + length) - 1, index, processor);
        } catch (Exception e) {
            PlatformDependent.throwException(e);
            return -1;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int forEachByteDesc0(int rStart, int rEnd, ByteProcessor processor) throws Exception {
        while (rStart >= rEnd) {
            if (processor.process(_getByte(rStart))) {
                rStart--;
            } else {
                return rStart;
            }
        }
        return -1;
    }

    @Override // io.netty.buffer.ByteBuf
    public int hashCode() {
        return ByteBufUtil.hashCode(this);
    }

    @Override // io.netty.buffer.ByteBuf
    public boolean equals(Object o) {
        return (o instanceof ByteBuf) && ByteBufUtil.equals(this, (ByteBuf) o);
    }

    @Override // io.netty.buffer.ByteBuf, java.lang.Comparable
    public int compareTo(ByteBuf that) {
        return ByteBufUtil.compare(this, that);
    }

    @Override // io.netty.buffer.ByteBuf
    public String toString() {
        if (refCnt() == 0) {
            return StringUtil.simpleClassName(this) + "(freed)";
        }
        StringBuilder buf = new StringBuilder().append(StringUtil.simpleClassName(this)).append("(ridx: ").append(this.readerIndex).append(", widx: ").append(this.writerIndex).append(", cap: ").append(capacity());
        if (this.maxCapacity != Integer.MAX_VALUE) {
            buf.append('/').append(this.maxCapacity);
        }
        ByteBuf unwrapped = unwrap();
        if (unwrapped != null) {
            buf.append(", unwrapped: ").append(unwrapped);
        }
        buf.append(')');
        return buf.toString();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void checkIndex(int index) {
        checkIndex(index, 1);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void checkIndex(int index, int fieldLength) {
        ensureAccessible();
        checkIndex0(index, fieldLength);
    }

    private static void checkRangeBounds(String indexName, int index, int fieldLength, int capacity) {
        if (MathUtil.isOutOfBounds(index, fieldLength, capacity)) {
            throw new IndexOutOfBoundsException(String.format("%s: %d, length: %d (expected: range(0, %d))", indexName, Integer.valueOf(index), Integer.valueOf(fieldLength), Integer.valueOf(capacity)));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void checkIndex0(int index, int fieldLength) {
        if (checkBounds) {
            checkRangeBounds("index", index, fieldLength, capacity());
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void checkSrcIndex(int index, int length, int srcIndex, int srcCapacity) {
        checkIndex(index, length);
        if (checkBounds) {
            checkRangeBounds("srcIndex", srcIndex, length, srcCapacity);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void checkDstIndex(int index, int length, int dstIndex, int dstCapacity) {
        checkIndex(index, length);
        if (checkBounds) {
            checkRangeBounds("dstIndex", dstIndex, length, dstCapacity);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void checkDstIndex(int length, int dstIndex, int dstCapacity) {
        checkReadableBytes(length);
        if (checkBounds) {
            checkRangeBounds("dstIndex", dstIndex, length, dstCapacity);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void checkReadableBytes(int minimumReadableBytes) {
        checkReadableBytes0(ObjectUtil.checkPositiveOrZero(minimumReadableBytes, "minimumReadableBytes"));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void checkNewCapacity(int newCapacity) {
        ensureAccessible();
        if (checkBounds) {
            if (newCapacity < 0 || newCapacity > maxCapacity()) {
                throw new IllegalArgumentException("newCapacity: " + newCapacity + " (expected: 0-" + maxCapacity() + ')');
            }
        }
    }

    private void checkReadableBytes0(int minimumReadableBytes) {
        ensureAccessible();
        if (checkBounds && this.readerIndex > this.writerIndex - minimumReadableBytes) {
            throw new IndexOutOfBoundsException(String.format("readerIndex(%d) + length(%d) exceeds writerIndex(%d): %s", Integer.valueOf(this.readerIndex), Integer.valueOf(minimumReadableBytes), Integer.valueOf(this.writerIndex), this));
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void ensureAccessible() {
        if (checkAccessible && !isAccessible()) {
            throw new IllegalReferenceCountException(0);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void setIndex0(int readerIndex, int writerIndex) {
        this.readerIndex = readerIndex;
        this.writerIndex = writerIndex;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void discardMarks() {
        this.markedWriterIndex = 0;
        this.markedReaderIndex = 0;
    }
}
