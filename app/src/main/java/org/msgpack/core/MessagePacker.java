package org.msgpack.core;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import java.time.Instant;
import org.msgpack.core.MessagePack;
import org.msgpack.core.buffer.MessageBuffer;
import org.msgpack.core.buffer.MessageBufferOutput;
import org.msgpack.value.Value;

/* loaded from: classes5.dex */
public class MessagePacker implements Closeable, Flushable {
    private static final boolean CORRUPTED_CHARSET_ENCODER;
    private static final long NANOS_PER_SECOND = 1000000000;
    private static final int UTF_8_MAX_CHAR_SIZE = 6;
    private MessageBuffer buffer;
    private final int bufferFlushThreshold;
    private CharsetEncoder encoder;
    protected MessageBufferOutput out;
    private final int smallStringOptimizationThreshold;
    private final boolean str8FormatSupport;
    private int position = 0;
    private long totalFlushBytes = 0;

    static {
        boolean z = false;
        try {
            Class<?> cls = Class.forName("android.os.Build$VERSION");
            int i = cls.getField("SDK_INT").getInt(cls.getConstructor(new Class[0]).newInstance(new Object[0]));
            if (i >= 14 && i < 21) {
                z = true;
            }
        } catch (ClassNotFoundException e) {
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
        } catch (InstantiationException e3) {
            e3.printStackTrace();
        } catch (NoSuchFieldException e4) {
            e4.printStackTrace();
        } catch (NoSuchMethodException e5) {
            e5.printStackTrace();
        } catch (InvocationTargetException e6) {
            e6.printStackTrace();
        }
        CORRUPTED_CHARSET_ENCODER = z;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public MessagePacker(MessageBufferOutput messageBufferOutput, MessagePack.PackerConfig packerConfig) {
        this.out = (MessageBufferOutput) Preconditions.checkNotNull(messageBufferOutput, "MessageBufferOutput is null");
        this.smallStringOptimizationThreshold = packerConfig.getSmallStringOptimizationThreshold();
        this.bufferFlushThreshold = packerConfig.getBufferFlushThreshold();
        this.str8FormatSupport = packerConfig.isStr8FormatSupport();
    }

    public MessageBufferOutput reset(MessageBufferOutput messageBufferOutput) throws IOException {
        MessageBufferOutput messageBufferOutput2 = (MessageBufferOutput) Preconditions.checkNotNull(messageBufferOutput, "MessageBufferOutput is null");
        flush();
        MessageBufferOutput messageBufferOutput3 = this.out;
        this.out = messageBufferOutput2;
        this.totalFlushBytes = 0L;
        return messageBufferOutput3;
    }

    public long getTotalWrittenBytes() {
        return this.totalFlushBytes + this.position;
    }

    public void clear() {
        this.position = 0;
    }

    @Override // java.io.Flushable
    public void flush() throws IOException {
        if (this.position > 0) {
            flushBuffer();
        }
        this.out.flush();
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        try {
            flush();
        } finally {
            this.out.close();
        }
    }

    private void flushBuffer() throws IOException {
        this.out.writeBuffer(this.position);
        this.buffer = null;
        this.totalFlushBytes += this.position;
        this.position = 0;
    }

    private void ensureCapacity(int i) throws IOException {
        if (this.buffer == null) {
            this.buffer = this.out.next(i);
        } else if (this.position + i >= this.buffer.size()) {
            flushBuffer();
            this.buffer = this.out.next(i);
        }
    }

    private void writeByte(byte b) throws IOException {
        ensureCapacity(1);
        MessageBuffer messageBuffer = this.buffer;
        int i = this.position;
        this.position = i + 1;
        messageBuffer.putByte(i, b);
    }

    private void writeByteAndByte(byte b, byte b2) throws IOException {
        ensureCapacity(2);
        MessageBuffer messageBuffer = this.buffer;
        int i = this.position;
        this.position = i + 1;
        messageBuffer.putByte(i, b);
        MessageBuffer messageBuffer2 = this.buffer;
        int i2 = this.position;
        this.position = i2 + 1;
        messageBuffer2.putByte(i2, b2);
    }

    private void writeByteAndShort(byte b, short s) throws IOException {
        ensureCapacity(3);
        MessageBuffer messageBuffer = this.buffer;
        int i = this.position;
        this.position = i + 1;
        messageBuffer.putByte(i, b);
        this.buffer.putShort(this.position, s);
        this.position += 2;
    }

    private void writeByteAndInt(byte b, int i) throws IOException {
        ensureCapacity(5);
        MessageBuffer messageBuffer = this.buffer;
        int i2 = this.position;
        this.position = i2 + 1;
        messageBuffer.putByte(i2, b);
        this.buffer.putInt(this.position, i);
        this.position += 4;
    }

    private void writeByteAndFloat(byte b, float f) throws IOException {
        ensureCapacity(5);
        MessageBuffer messageBuffer = this.buffer;
        int i = this.position;
        this.position = i + 1;
        messageBuffer.putByte(i, b);
        this.buffer.putFloat(this.position, f);
        this.position += 4;
    }

    private void writeByteAndDouble(byte b, double d) throws IOException {
        ensureCapacity(9);
        MessageBuffer messageBuffer = this.buffer;
        int i = this.position;
        this.position = i + 1;
        messageBuffer.putByte(i, b);
        this.buffer.putDouble(this.position, d);
        this.position += 8;
    }

    private void writeByteAndLong(byte b, long j) throws IOException {
        ensureCapacity(9);
        MessageBuffer messageBuffer = this.buffer;
        int i = this.position;
        this.position = i + 1;
        messageBuffer.putByte(i, b);
        this.buffer.putLong(this.position, j);
        this.position += 8;
    }

    private void writeShort(short s) throws IOException {
        ensureCapacity(2);
        this.buffer.putShort(this.position, s);
        this.position += 2;
    }

    private void writeInt(int i) throws IOException {
        ensureCapacity(4);
        this.buffer.putInt(this.position, i);
        this.position += 4;
    }

    private void writeLong(long j) throws IOException {
        ensureCapacity(8);
        this.buffer.putLong(this.position, j);
        this.position += 8;
    }

    public MessagePacker packNil() throws IOException {
        writeByte(MessagePack.Code.NIL);
        return this;
    }

    public MessagePacker packBoolean(boolean z) throws IOException {
        writeByte(z ? MessagePack.Code.TRUE : MessagePack.Code.FALSE);
        return this;
    }

    public MessagePacker packByte(byte b) throws IOException {
        if (b < -32) {
            writeByteAndByte(MessagePack.Code.INT8, b);
        } else {
            writeByte(b);
        }
        return this;
    }

    public MessagePacker packShort(short s) throws IOException {
        if (s < -32) {
            if (s < -128) {
                writeByteAndShort(MessagePack.Code.INT16, s);
            } else {
                writeByteAndByte(MessagePack.Code.INT8, (byte) s);
            }
        } else if (s < 128) {
            writeByte((byte) s);
        } else if (s < 256) {
            writeByteAndByte(MessagePack.Code.UINT8, (byte) s);
        } else {
            writeByteAndShort(MessagePack.Code.UINT16, s);
        }
        return this;
    }

    public MessagePacker packInt(int i) throws IOException {
        if (i < -32) {
            if (i < -32768) {
                writeByteAndInt(MessagePack.Code.INT32, i);
            } else if (i < -128) {
                writeByteAndShort(MessagePack.Code.INT16, (short) i);
            } else {
                writeByteAndByte(MessagePack.Code.INT8, (byte) i);
            }
        } else if (i < 128) {
            writeByte((byte) i);
        } else if (i < 256) {
            writeByteAndByte(MessagePack.Code.UINT8, (byte) i);
        } else if (i < 65536) {
            writeByteAndShort(MessagePack.Code.UINT16, (short) i);
        } else {
            writeByteAndInt(MessagePack.Code.UINT32, i);
        }
        return this;
    }

    public MessagePacker packLong(long j) throws IOException {
        if (j < -32) {
            if (j < -32768) {
                if (j < -2147483648L) {
                    writeByteAndLong(MessagePack.Code.INT64, j);
                } else {
                    writeByteAndInt(MessagePack.Code.INT32, (int) j);
                }
            } else if (j < -128) {
                writeByteAndShort(MessagePack.Code.INT16, (short) j);
            } else {
                writeByteAndByte(MessagePack.Code.INT8, (byte) j);
            }
        } else if (j < 128) {
            writeByte((byte) j);
        } else if (j < 65536) {
            if (j < 256) {
                writeByteAndByte(MessagePack.Code.UINT8, (byte) j);
            } else {
                writeByteAndShort(MessagePack.Code.UINT16, (short) j);
            }
        } else if (j < 4294967296L) {
            writeByteAndInt(MessagePack.Code.UINT32, (int) j);
        } else {
            writeByteAndLong(MessagePack.Code.UINT64, j);
        }
        return this;
    }

    public MessagePacker packBigInteger(BigInteger bigInteger) throws IOException {
        if (bigInteger.bitLength() <= 63) {
            packLong(bigInteger.longValue());
        } else if (bigInteger.bitLength() == 64 && bigInteger.signum() == 1) {
            writeByteAndLong(MessagePack.Code.UINT64, bigInteger.longValue());
        } else {
            throw new IllegalArgumentException("MessagePack cannot serialize BigInteger larger than 2^64-1");
        }
        return this;
    }

    public MessagePacker packFloat(float f) throws IOException {
        writeByteAndFloat(MessagePack.Code.FLOAT32, f);
        return this;
    }

    public MessagePacker packDouble(double d) throws IOException {
        writeByteAndDouble(MessagePack.Code.FLOAT64, d);
        return this;
    }

    private void packStringWithGetBytes(String str) throws IOException {
        byte[] bytes = str.getBytes(MessagePack.UTF8);
        packRawStringHeader(bytes.length);
        addPayload(bytes);
    }

    private void prepareEncoder() {
        if (this.encoder == null) {
            this.encoder = MessagePack.UTF8.newEncoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE);
        }
        this.encoder.reset();
    }

    private int encodeStringToBufferAt(int i, String str) {
        prepareEncoder();
        ByteBuffer sliceAsByteBuffer = this.buffer.sliceAsByteBuffer(i, this.buffer.size() - i);
        int position = sliceAsByteBuffer.position();
        CoderResult encode = this.encoder.encode(CharBuffer.wrap(str), sliceAsByteBuffer, true);
        if (encode.isError()) {
            try {
                encode.throwException();
            } catch (CharacterCodingException e) {
                throw new MessageStringCodingException(e);
            }
        }
        if (encode.isUnderflow() && !encode.isOverflow() && this.encoder.flush(sliceAsByteBuffer).isUnderflow()) {
            return sliceAsByteBuffer.position() - position;
        }
        return -1;
    }

    public MessagePacker packString(String str) throws IOException {
        if (str.length() <= 0) {
            packRawStringHeader(0);
            return this;
        }
        if (CORRUPTED_CHARSET_ENCODER || str.length() < this.smallStringOptimizationThreshold) {
            packStringWithGetBytes(str);
            return this;
        }
        if (str.length() < 256) {
            ensureCapacity((str.length() * 6) + 2 + 1);
            int encodeStringToBufferAt = encodeStringToBufferAt(this.position + 2, str);
            if (encodeStringToBufferAt >= 0) {
                if (this.str8FormatSupport && encodeStringToBufferAt < 256) {
                    MessageBuffer messageBuffer = this.buffer;
                    int i = this.position;
                    this.position = i + 1;
                    messageBuffer.putByte(i, MessagePack.Code.STR8);
                    MessageBuffer messageBuffer2 = this.buffer;
                    int i2 = this.position;
                    this.position = i2 + 1;
                    messageBuffer2.putByte(i2, (byte) encodeStringToBufferAt);
                    this.position += encodeStringToBufferAt;
                } else {
                    if (encodeStringToBufferAt >= 65536) {
                        throw new IllegalArgumentException("Unexpected UTF-8 encoder state");
                    }
                    this.buffer.putMessageBuffer(this.position + 3, this.buffer, this.position + 2, encodeStringToBufferAt);
                    MessageBuffer messageBuffer3 = this.buffer;
                    int i3 = this.position;
                    this.position = i3 + 1;
                    messageBuffer3.putByte(i3, MessagePack.Code.STR16);
                    this.buffer.putShort(this.position, (short) encodeStringToBufferAt);
                    this.position += 2;
                    this.position += encodeStringToBufferAt;
                }
                return this;
            }
        } else if (str.length() < 65536) {
            ensureCapacity((str.length() * 6) + 3 + 2);
            int encodeStringToBufferAt2 = encodeStringToBufferAt(this.position + 3, str);
            if (encodeStringToBufferAt2 >= 0) {
                if (encodeStringToBufferAt2 < 65536) {
                    MessageBuffer messageBuffer4 = this.buffer;
                    int i4 = this.position;
                    this.position = i4 + 1;
                    messageBuffer4.putByte(i4, MessagePack.Code.STR16);
                    this.buffer.putShort(this.position, (short) encodeStringToBufferAt2);
                    this.position += 2;
                    this.position += encodeStringToBufferAt2;
                } else {
                    this.buffer.putMessageBuffer(this.position + 5, this.buffer, this.position + 3, encodeStringToBufferAt2);
                    MessageBuffer messageBuffer5 = this.buffer;
                    int i5 = this.position;
                    this.position = i5 + 1;
                    messageBuffer5.putByte(i5, MessagePack.Code.STR32);
                    this.buffer.putInt(this.position, encodeStringToBufferAt2);
                    this.position += 4;
                    this.position += encodeStringToBufferAt2;
                }
                return this;
            }
        }
        packStringWithGetBytes(str);
        return this;
    }

    public MessagePacker packTimestamp(Instant instant) throws IOException {
        return packTimestamp(instant.getEpochSecond(), instant.getNano());
    }

    public MessagePacker packTimestamp(long j) throws IOException {
        return packTimestamp(Instant.ofEpochMilli(j));
    }

    public MessagePacker packTimestamp(long j, int i) throws IOException, ArithmeticException {
        long j2 = i;
        long addExact = Math.addExact(j, Math.floorDiv(j2, NANOS_PER_SECOND));
        long floorMod = Math.floorMod(j2, NANOS_PER_SECOND);
        if ((addExact >>> 34) == 0) {
            long j3 = (floorMod << 34) | addExact;
            if (((-4294967296L) & j3) == 0) {
                writeTimestamp32((int) addExact);
            } else {
                writeTimestamp64(j3);
            }
        } else {
            writeTimestamp96(addExact, (int) floorMod);
        }
        return this;
    }

    private void writeTimestamp32(int i) throws IOException {
        ensureCapacity(6);
        MessageBuffer messageBuffer = this.buffer;
        int i2 = this.position;
        this.position = i2 + 1;
        messageBuffer.putByte(i2, MessagePack.Code.FIXEXT4);
        MessageBuffer messageBuffer2 = this.buffer;
        int i3 = this.position;
        this.position = i3 + 1;
        messageBuffer2.putByte(i3, (byte) -1);
        this.buffer.putInt(this.position, i);
        this.position += 4;
    }

    private void writeTimestamp64(long j) throws IOException {
        ensureCapacity(10);
        MessageBuffer messageBuffer = this.buffer;
        int i = this.position;
        this.position = i + 1;
        messageBuffer.putByte(i, MessagePack.Code.FIXEXT8);
        MessageBuffer messageBuffer2 = this.buffer;
        int i2 = this.position;
        this.position = i2 + 1;
        messageBuffer2.putByte(i2, (byte) -1);
        this.buffer.putLong(this.position, j);
        this.position += 8;
    }

    private void writeTimestamp96(long j, int i) throws IOException {
        ensureCapacity(15);
        MessageBuffer messageBuffer = this.buffer;
        int i2 = this.position;
        this.position = i2 + 1;
        messageBuffer.putByte(i2, MessagePack.Code.EXT8);
        MessageBuffer messageBuffer2 = this.buffer;
        int i3 = this.position;
        this.position = i3 + 1;
        messageBuffer2.putByte(i3, (byte) 12);
        MessageBuffer messageBuffer3 = this.buffer;
        int i4 = this.position;
        this.position = i4 + 1;
        messageBuffer3.putByte(i4, (byte) -1);
        this.buffer.putInt(this.position, i);
        this.position += 4;
        this.buffer.putLong(this.position, j);
        this.position += 8;
    }

    public MessagePacker packArrayHeader(int i) throws IOException {
        if (i < 0) {
            throw new IllegalArgumentException("array size must be >= 0");
        }
        if (i < 16) {
            writeByte((byte) (i | (-112)));
        } else if (i < 65536) {
            writeByteAndShort(MessagePack.Code.ARRAY16, (short) i);
        } else {
            writeByteAndInt(MessagePack.Code.ARRAY32, i);
        }
        return this;
    }

    public MessagePacker packMapHeader(int i) throws IOException {
        if (i < 0) {
            throw new IllegalArgumentException("map size must be >= 0");
        }
        if (i < 16) {
            writeByte((byte) (i | (-128)));
        } else if (i < 65536) {
            writeByteAndShort(MessagePack.Code.MAP16, (short) i);
        } else {
            writeByteAndInt(MessagePack.Code.MAP32, i);
        }
        return this;
    }

    public MessagePacker packValue(Value value) throws IOException {
        value.writeTo(this);
        return this;
    }

    public MessagePacker packExtensionTypeHeader(byte b, int i) throws IOException {
        if (i < 256) {
            if (i <= 0 || ((i - 1) & i) != 0) {
                writeByteAndByte(MessagePack.Code.EXT8, (byte) i);
                writeByte(b);
            } else if (i == 1) {
                writeByteAndByte(MessagePack.Code.FIXEXT1, b);
            } else if (i == 2) {
                writeByteAndByte(MessagePack.Code.FIXEXT2, b);
            } else if (i == 4) {
                writeByteAndByte(MessagePack.Code.FIXEXT4, b);
            } else if (i == 8) {
                writeByteAndByte(MessagePack.Code.FIXEXT8, b);
            } else if (i != 16) {
                writeByteAndByte(MessagePack.Code.EXT8, (byte) i);
                writeByte(b);
            } else {
                writeByteAndByte(MessagePack.Code.FIXEXT16, b);
            }
        } else if (i < 65536) {
            writeByteAndShort(MessagePack.Code.EXT16, (short) i);
            writeByte(b);
        } else {
            writeByteAndInt(MessagePack.Code.EXT32, i);
            writeByte(b);
        }
        return this;
    }

    public MessagePacker packBinaryHeader(int i) throws IOException {
        if (i < 256) {
            writeByteAndByte(MessagePack.Code.BIN8, (byte) i);
        } else if (i < 65536) {
            writeByteAndShort(MessagePack.Code.BIN16, (short) i);
        } else {
            writeByteAndInt(MessagePack.Code.BIN32, i);
        }
        return this;
    }

    public MessagePacker packRawStringHeader(int i) throws IOException {
        if (i < 32) {
            writeByte((byte) (i | (-96)));
        } else if (this.str8FormatSupport && i < 256) {
            writeByteAndByte(MessagePack.Code.STR8, (byte) i);
        } else if (i < 65536) {
            writeByteAndShort(MessagePack.Code.STR16, (short) i);
        } else {
            writeByteAndInt(MessagePack.Code.STR32, i);
        }
        return this;
    }

    public MessagePacker writePayload(byte[] bArr) throws IOException {
        return writePayload(bArr, 0, bArr.length);
    }

    public MessagePacker writePayload(byte[] bArr, int i, int i2) throws IOException {
        if (this.buffer == null || this.buffer.size() - this.position < i2 || i2 > this.bufferFlushThreshold) {
            flush();
            this.out.write(bArr, i, i2);
            this.totalFlushBytes += i2;
        } else {
            this.buffer.putBytes(this.position, bArr, i, i2);
            this.position += i2;
        }
        return this;
    }

    public MessagePacker addPayload(byte[] bArr) throws IOException {
        return addPayload(bArr, 0, bArr.length);
    }

    public MessagePacker addPayload(byte[] bArr, int i, int i2) throws IOException {
        if (this.buffer == null || this.buffer.size() - this.position < i2 || i2 > this.bufferFlushThreshold) {
            flush();
            this.out.add(bArr, i, i2);
            this.totalFlushBytes += i2;
        } else {
            this.buffer.putBytes(this.position, bArr, i, i2);
            this.position += i2;
        }
        return this;
    }
}
