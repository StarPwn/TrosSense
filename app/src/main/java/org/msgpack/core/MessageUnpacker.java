package org.msgpack.core;

import com.google.common.base.Ascii;
import io.netty.handler.codec.rtsp.RtspHeaders;
import java.io.Closeable;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import java.time.Instant;
import kotlin.UShort;
import org.msgpack.core.MessagePack;
import org.msgpack.core.buffer.MessageBuffer;
import org.msgpack.core.buffer.MessageBufferInput;
import org.msgpack.value.ImmutableValue;
import org.msgpack.value.Value;
import org.msgpack.value.ValueFactory;
import org.msgpack.value.Variable;

/* loaded from: classes5.dex */
public class MessageUnpacker implements Closeable {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final MessageBuffer EMPTY_BUFFER = MessageBuffer.wrap(new byte[0]);
    private static final String EMPTY_STRING = "";
    private final CodingErrorAction actionOnMalformedString;
    private final CodingErrorAction actionOnUnmappableString;
    private final boolean allowReadingBinaryAsString;
    private final boolean allowReadingStringAsBinary;
    private CharBuffer decodeBuffer;
    private StringBuilder decodeStringBuffer;
    private CharsetDecoder decoder;
    private MessageBufferInput in;
    private int nextReadPosition;
    private int position;
    private final int stringDecoderBufferSize;
    private final int stringSizeLimit;
    private long totalReadBytes;
    private MessageBuffer buffer = EMPTY_BUFFER;
    private final MessageBuffer numberBuffer = MessageBuffer.allocate(8);

    /* JADX INFO: Access modifiers changed from: protected */
    public MessageUnpacker(MessageBufferInput messageBufferInput, MessagePack.UnpackerConfig unpackerConfig) {
        this.in = (MessageBufferInput) Preconditions.checkNotNull(messageBufferInput, "MessageBufferInput is null");
        this.allowReadingStringAsBinary = unpackerConfig.getAllowReadingStringAsBinary();
        this.allowReadingBinaryAsString = unpackerConfig.getAllowReadingBinaryAsString();
        this.actionOnMalformedString = unpackerConfig.getActionOnMalformedString();
        this.actionOnUnmappableString = unpackerConfig.getActionOnUnmappableString();
        this.stringSizeLimit = unpackerConfig.getStringSizeLimit();
        this.stringDecoderBufferSize = unpackerConfig.getStringDecoderBufferSize();
    }

    public MessageBufferInput reset(MessageBufferInput messageBufferInput) throws IOException {
        MessageBufferInput messageBufferInput2 = (MessageBufferInput) Preconditions.checkNotNull(messageBufferInput, "MessageBufferInput is null");
        MessageBufferInput messageBufferInput3 = this.in;
        this.in = messageBufferInput2;
        this.buffer = EMPTY_BUFFER;
        this.position = 0;
        this.totalReadBytes = 0L;
        return messageBufferInput3;
    }

    public long getTotalReadBytes() {
        return this.totalReadBytes + this.position;
    }

    private MessageBuffer getNextBuffer() throws IOException {
        MessageBuffer next = this.in.next();
        if (next == null) {
            throw new MessageInsufficientBufferException();
        }
        if (this.buffer == null) {
            throw new AssertionError();
        }
        this.totalReadBytes += this.buffer.size();
        return next;
    }

    private void nextBuffer() throws IOException {
        this.buffer = getNextBuffer();
        this.position = 0;
    }

    private MessageBuffer prepareNumberBuffer(int i) throws IOException {
        int i2;
        int size = this.buffer.size() - this.position;
        if (size >= i) {
            this.nextReadPosition = this.position;
            this.position += i;
            return this.buffer;
        }
        if (size > 0) {
            this.numberBuffer.putMessageBuffer(0, this.buffer, this.position, size);
            i -= size;
            i2 = size + 0;
        } else {
            i2 = 0;
        }
        while (true) {
            nextBuffer();
            int size2 = this.buffer.size();
            if (size2 >= i) {
                this.numberBuffer.putMessageBuffer(i2, this.buffer, 0, i);
                this.position = i;
                this.nextReadPosition = 0;
                return this.numberBuffer;
            }
            this.numberBuffer.putMessageBuffer(i2, this.buffer, 0, size2);
            i -= size2;
            i2 += size2;
        }
    }

    private static int utf8MultibyteCharacterSize(byte b) {
        return Integer.numberOfLeadingZeros((~(b & 255)) << 24);
    }

    public boolean hasNext() throws IOException {
        return ensureBuffer();
    }

    private boolean ensureBuffer() throws IOException {
        while (this.buffer.size() <= this.position) {
            MessageBuffer next = this.in.next();
            if (next == null) {
                return false;
            }
            this.totalReadBytes += this.buffer.size();
            this.buffer = next;
            this.position = 0;
        }
        return true;
    }

    public MessageFormat getNextFormat() throws IOException {
        if (!ensureBuffer()) {
            throw new MessageInsufficientBufferException();
        }
        return MessageFormat.valueOf(this.buffer.getByte(this.position));
    }

    private byte readByte() throws IOException {
        if (this.buffer.size() > this.position) {
            byte b = this.buffer.getByte(this.position);
            this.position++;
            return b;
        }
        nextBuffer();
        if (this.buffer.size() > 0) {
            byte b2 = this.buffer.getByte(0);
            this.position = 1;
            return b2;
        }
        return readByte();
    }

    private short readShort() throws IOException {
        return prepareNumberBuffer(2).getShort(this.nextReadPosition);
    }

    private int readInt() throws IOException {
        return prepareNumberBuffer(4).getInt(this.nextReadPosition);
    }

    private long readLong() throws IOException {
        return prepareNumberBuffer(8).getLong(this.nextReadPosition);
    }

    private float readFloat() throws IOException {
        return prepareNumberBuffer(4).getFloat(this.nextReadPosition);
    }

    private double readDouble() throws IOException {
        return prepareNumberBuffer(8).getDouble(this.nextReadPosition);
    }

    public void skipValue() throws IOException {
        skipValue(1);
    }

    public void skipValue(int i) throws IOException {
        while (i > 0) {
            byte readByte = readByte();
            switch (MessageFormat.valueOf(readByte)) {
                case FIXMAP:
                    i += (readByte & 15) * 2;
                    break;
                case FIXARRAY:
                    i += readByte & 15;
                    break;
                case FIXSTR:
                    skipPayload(readByte & Ascii.US);
                    break;
                case INT8:
                case UINT8:
                    skipPayload(1);
                    break;
                case INT16:
                case UINT16:
                    skipPayload(2);
                    break;
                case INT32:
                case UINT32:
                case FLOAT32:
                    skipPayload(4);
                    break;
                case INT64:
                case UINT64:
                case FLOAT64:
                    skipPayload(8);
                    break;
                case BIN8:
                case STR8:
                    skipPayload(readNextLength8());
                    break;
                case BIN16:
                case STR16:
                    skipPayload(readNextLength16());
                    break;
                case BIN32:
                case STR32:
                    skipPayload(readNextLength32());
                    break;
                case FIXEXT1:
                    skipPayload(2);
                    break;
                case FIXEXT2:
                    skipPayload(3);
                    break;
                case FIXEXT4:
                    skipPayload(5);
                    break;
                case FIXEXT8:
                    skipPayload(9);
                    break;
                case FIXEXT16:
                    skipPayload(17);
                    break;
                case EXT8:
                    skipPayload(readNextLength8() + 1);
                    break;
                case EXT16:
                    skipPayload(readNextLength16() + 1);
                    break;
                case EXT32:
                    int readNextLength32 = readNextLength32();
                    skipPayload(1);
                    skipPayload(readNextLength32);
                    break;
                case ARRAY16:
                    i += readNextLength16();
                    break;
                case ARRAY32:
                    i += readNextLength32();
                    break;
                case MAP16:
                    i += readNextLength16() * 2;
                    break;
                case MAP32:
                    i += readNextLength32() * 2;
                    break;
                case NEVER_USED:
                    throw new MessageNeverUsedFormatException("Encountered 0xC1 \"NEVER_USED\" byte");
            }
            i--;
        }
    }

    private static MessagePackException unexpected(String str, byte b) {
        MessageFormat valueOf = MessageFormat.valueOf(b);
        if (valueOf == MessageFormat.NEVER_USED) {
            return new MessageNeverUsedFormatException(String.format("Expected %s, but encountered 0xC1 \"NEVER_USED\" byte", str));
        }
        String name = valueOf.getValueType().name();
        return new MessageTypeException(String.format("Expected %s, but got %s (%02x)", str, name.substring(0, 1) + name.substring(1).toLowerCase(), Byte.valueOf(b)));
    }

    private static MessagePackException unexpectedExtension(String str, int i, int i2) {
        return new MessageTypeException(String.format("Expected extension type %s (%d), but got extension type %d", str, Integer.valueOf(i), Integer.valueOf(i2)));
    }

    public ImmutableValue unpackValue() throws IOException {
        MessageFormat nextFormat = getNextFormat();
        int i = 0;
        switch (nextFormat.getValueType()) {
            case NIL:
                readByte();
                return ValueFactory.newNil();
            case BOOLEAN:
                return ValueFactory.newBoolean(unpackBoolean());
            case INTEGER:
                if (nextFormat == MessageFormat.UINT64) {
                    return ValueFactory.newInteger(unpackBigInteger());
                }
                return ValueFactory.newInteger(unpackLong());
            case FLOAT:
                return ValueFactory.newFloat(unpackDouble());
            case STRING:
                int unpackRawStringHeader = unpackRawStringHeader();
                if (unpackRawStringHeader > this.stringSizeLimit) {
                    throw new MessageSizeException(String.format("cannot unpack a String of size larger than %,d: %,d", Integer.valueOf(this.stringSizeLimit), Integer.valueOf(unpackRawStringHeader)), unpackRawStringHeader);
                }
                return ValueFactory.newString(readPayload(unpackRawStringHeader), true);
            case BINARY:
                return ValueFactory.newBinary(readPayload(unpackBinaryHeader()), true);
            case ARRAY:
                int unpackArrayHeader = unpackArrayHeader();
                Value[] valueArr = new Value[unpackArrayHeader];
                while (i < unpackArrayHeader) {
                    valueArr[i] = unpackValue();
                    i++;
                }
                return ValueFactory.newArray(valueArr, true);
            case MAP:
                int unpackMapHeader = unpackMapHeader() * 2;
                Value[] valueArr2 = new Value[unpackMapHeader];
                while (i < unpackMapHeader) {
                    valueArr2[i] = unpackValue();
                    int i2 = i + 1;
                    valueArr2[i2] = unpackValue();
                    i = i2 + 1;
                }
                return ValueFactory.newMap(valueArr2, true);
            case EXTENSION:
                ExtensionTypeHeader unpackExtensionTypeHeader = unpackExtensionTypeHeader();
                switch (unpackExtensionTypeHeader.getType()) {
                    case -1:
                        return ValueFactory.newTimestamp(unpackTimestamp(unpackExtensionTypeHeader));
                    default:
                        return ValueFactory.newExtension(unpackExtensionTypeHeader.getType(), readPayload(unpackExtensionTypeHeader.getLength()));
                }
            default:
                throw new MessageNeverUsedFormatException("Unknown value type");
        }
    }

    public Variable unpackValue(Variable variable) throws IOException {
        MessageFormat nextFormat = getNextFormat();
        int i = 0;
        switch (nextFormat.getValueType()) {
            case NIL:
                readByte();
                variable.setNilValue();
                return variable;
            case BOOLEAN:
                variable.setBooleanValue(unpackBoolean());
                return variable;
            case INTEGER:
                switch (nextFormat) {
                    case UINT64:
                        variable.setIntegerValue(unpackBigInteger());
                        return variable;
                    default:
                        variable.setIntegerValue(unpackLong());
                        return variable;
                }
            case FLOAT:
                variable.setFloatValue(unpackDouble());
                return variable;
            case STRING:
                int unpackRawStringHeader = unpackRawStringHeader();
                if (unpackRawStringHeader > this.stringSizeLimit) {
                    throw new MessageSizeException(String.format("cannot unpack a String of size larger than %,d: %,d", Integer.valueOf(this.stringSizeLimit), Integer.valueOf(unpackRawStringHeader)), unpackRawStringHeader);
                }
                variable.setStringValue(readPayload(unpackRawStringHeader));
                return variable;
            case BINARY:
                variable.setBinaryValue(readPayload(unpackBinaryHeader()));
                return variable;
            case ARRAY:
                int unpackArrayHeader = unpackArrayHeader();
                Value[] valueArr = new Value[unpackArrayHeader];
                while (i < unpackArrayHeader) {
                    valueArr[i] = unpackValue();
                    i++;
                }
                variable.setArrayValue(valueArr);
                return variable;
            case MAP:
                int unpackMapHeader = unpackMapHeader() * 2;
                Value[] valueArr2 = new Value[unpackMapHeader];
                while (i < unpackMapHeader) {
                    valueArr2[i] = unpackValue();
                    int i2 = i + 1;
                    valueArr2[i2] = unpackValue();
                    i = i2 + 1;
                }
                variable.setMapValue(valueArr2);
                return variable;
            case EXTENSION:
                ExtensionTypeHeader unpackExtensionTypeHeader = unpackExtensionTypeHeader();
                switch (unpackExtensionTypeHeader.getType()) {
                    case -1:
                        variable.setTimestampValue(unpackTimestamp(unpackExtensionTypeHeader));
                        return variable;
                    default:
                        variable.setExtensionValue(unpackExtensionTypeHeader.getType(), readPayload(unpackExtensionTypeHeader.getLength()));
                        return variable;
                }
            default:
                throw new MessageFormatException("Unknown value type");
        }
    }

    public void unpackNil() throws IOException {
        byte readByte = readByte();
        if (readByte == -64) {
        } else {
            throw unexpected("Nil", readByte);
        }
    }

    public boolean tryUnpackNil() throws IOException {
        if (!ensureBuffer()) {
            throw new MessageInsufficientBufferException();
        }
        if (this.buffer.getByte(this.position) == -64) {
            readByte();
            return true;
        }
        return false;
    }

    public boolean unpackBoolean() throws IOException {
        byte readByte = readByte();
        if (readByte == -62) {
            return false;
        }
        if (readByte == -61) {
            return true;
        }
        throw unexpected("boolean", readByte);
    }

    public byte unpackByte() throws IOException {
        byte readByte = readByte();
        if (MessagePack.Code.isFixInt(readByte)) {
            return readByte;
        }
        switch (readByte) {
            case -52:
                byte readByte2 = readByte();
                if (readByte2 < 0) {
                    throw overflowU8(readByte2);
                }
                return readByte2;
            case -51:
                short readShort = readShort();
                if (readShort < 0 || readShort > 127) {
                    throw overflowU16(readShort);
                }
                return (byte) readShort;
            case -50:
                int readInt = readInt();
                if (readInt < 0 || readInt > 127) {
                    throw overflowU32(readInt);
                }
                return (byte) readInt;
            case -49:
                long readLong = readLong();
                if (readLong < 0 || readLong > 127) {
                    throw overflowU64(readLong);
                }
                return (byte) readLong;
            case -48:
                return readByte();
            case -47:
                short readShort2 = readShort();
                if (readShort2 < -128 || readShort2 > 127) {
                    throw overflowI16(readShort2);
                }
                return (byte) readShort2;
            case -46:
                int readInt2 = readInt();
                if (readInt2 < -128 || readInt2 > 127) {
                    throw overflowI32(readInt2);
                }
                return (byte) readInt2;
            case -45:
                long readLong2 = readLong();
                if (readLong2 < -128 || readLong2 > 127) {
                    throw overflowI64(readLong2);
                }
                return (byte) readLong2;
            default:
                throw unexpected("Integer", readByte);
        }
    }

    public short unpackShort() throws IOException {
        byte readByte = readByte();
        if (MessagePack.Code.isFixInt(readByte)) {
            return readByte;
        }
        switch (readByte) {
            case -52:
                return (short) (readByte() & 255);
            case -51:
                short readShort = readShort();
                if (readShort < 0) {
                    throw overflowU16(readShort);
                }
                return readShort;
            case -50:
                int readInt = readInt();
                if (readInt < 0 || readInt > 32767) {
                    throw overflowU32(readInt);
                }
                return (short) readInt;
            case -49:
                long readLong = readLong();
                if (readLong < 0 || readLong > 32767) {
                    throw overflowU64(readLong);
                }
                return (short) readLong;
            case -48:
                return readByte();
            case -47:
                return readShort();
            case -46:
                int readInt2 = readInt();
                if (readInt2 < -32768 || readInt2 > 32767) {
                    throw overflowI32(readInt2);
                }
                return (short) readInt2;
            case -45:
                long readLong2 = readLong();
                if (readLong2 < -32768 || readLong2 > 32767) {
                    throw overflowI64(readLong2);
                }
                return (short) readLong2;
            default:
                throw unexpected("Integer", readByte);
        }
    }

    public int unpackInt() throws IOException {
        byte readByte = readByte();
        if (MessagePack.Code.isFixInt(readByte)) {
            return readByte;
        }
        switch (readByte) {
            case -52:
                return readByte() & 255;
            case -51:
                return readShort() & UShort.MAX_VALUE;
            case -50:
                int readInt = readInt();
                if (readInt < 0) {
                    throw overflowU32(readInt);
                }
                return readInt;
            case -49:
                long readLong = readLong();
                if (readLong < 0 || readLong > 2147483647L) {
                    throw overflowU64(readLong);
                }
                return (int) readLong;
            case -48:
                return readByte();
            case -47:
                return readShort();
            case -46:
                return readInt();
            case -45:
                long readLong2 = readLong();
                if (readLong2 < -2147483648L || readLong2 > 2147483647L) {
                    throw overflowI64(readLong2);
                }
                return (int) readLong2;
            default:
                throw unexpected("Integer", readByte);
        }
    }

    public long unpackLong() throws IOException {
        byte readByte = readByte();
        if (MessagePack.Code.isFixInt(readByte)) {
            return readByte;
        }
        switch (readByte) {
            case -52:
                return readByte() & 255;
            case -51:
                return readShort() & UShort.MAX_VALUE;
            case -50:
                int readInt = readInt();
                if (readInt < 0) {
                    return (readInt & Integer.MAX_VALUE) + 2147483648L;
                }
                return readInt;
            case -49:
                long readLong = readLong();
                if (readLong < 0) {
                    throw overflowU64(readLong);
                }
                return readLong;
            case -48:
                return readByte();
            case -47:
                return readShort();
            case -46:
                return readInt();
            case -45:
                return readLong();
            default:
                throw unexpected("Integer", readByte);
        }
    }

    public BigInteger unpackBigInteger() throws IOException {
        byte readByte = readByte();
        if (MessagePack.Code.isFixInt(readByte)) {
            return BigInteger.valueOf(readByte);
        }
        switch (readByte) {
            case -52:
                return BigInteger.valueOf(readByte() & 255);
            case -51:
                return BigInteger.valueOf(readShort() & UShort.MAX_VALUE);
            case -50:
                int readInt = readInt();
                if (readInt < 0) {
                    return BigInteger.valueOf((readInt & Integer.MAX_VALUE) + 2147483648L);
                }
                return BigInteger.valueOf(readInt);
            case -49:
                long readLong = readLong();
                if (readLong < 0) {
                    return BigInteger.valueOf(readLong + Long.MAX_VALUE + 1).setBit(63);
                }
                return BigInteger.valueOf(readLong);
            case -48:
                return BigInteger.valueOf(readByte());
            case -47:
                return BigInteger.valueOf(readShort());
            case -46:
                return BigInteger.valueOf(readInt());
            case -45:
                return BigInteger.valueOf(readLong());
            default:
                throw unexpected("Integer", readByte);
        }
    }

    public float unpackFloat() throws IOException {
        byte readByte = readByte();
        switch (readByte) {
            case -54:
                return readFloat();
            case -53:
                return (float) readDouble();
            default:
                throw unexpected("Float", readByte);
        }
    }

    public double unpackDouble() throws IOException {
        byte readByte = readByte();
        switch (readByte) {
            case -54:
                return readFloat();
            case -53:
                return readDouble();
            default:
                throw unexpected("Float", readByte);
        }
    }

    private void resetDecoder() {
        if (this.decoder == null) {
            this.decodeBuffer = CharBuffer.allocate(this.stringDecoderBufferSize);
            this.decoder = MessagePack.UTF8.newDecoder().onMalformedInput(this.actionOnMalformedString).onUnmappableCharacter(this.actionOnUnmappableString);
        } else {
            this.decoder.reset();
        }
        if (this.decodeStringBuffer == null) {
            this.decodeStringBuffer = new StringBuilder();
        } else {
            this.decodeStringBuffer.setLength(0);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:42:0x00fa, code lost:            r3.throwException();     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x0102, code lost:            throw new org.msgpack.core.MessageFormatException("Unexpected UTF-8 multibyte sequence");     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.String unpackString() throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 329
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.msgpack.core.MessageUnpacker.unpackString():java.lang.String");
    }

    private void handleCoderError(CoderResult coderResult) throws CharacterCodingException {
        if ((coderResult.isMalformed() && this.actionOnMalformedString == CodingErrorAction.REPORT) || (coderResult.isUnmappable() && this.actionOnUnmappableString == CodingErrorAction.REPORT)) {
            coderResult.throwException();
        }
    }

    private String decodeStringFastPath(int i) {
        if (this.actionOnMalformedString == CodingErrorAction.REPLACE && this.actionOnUnmappableString == CodingErrorAction.REPLACE && this.buffer.hasArray()) {
            String str = new String(this.buffer.array(), this.buffer.arrayOffset() + this.position, i, MessagePack.UTF8);
            this.position += i;
            return str;
        }
        try {
            CharBuffer decode = this.decoder.decode(this.buffer.sliceAsByteBuffer(this.position, i));
            this.position += i;
            return decode.toString();
        } catch (CharacterCodingException e) {
            throw new MessageStringCodingException(e);
        }
    }

    public Instant unpackTimestamp() throws IOException {
        return unpackTimestamp(unpackExtensionTypeHeader());
    }

    public Instant unpackTimestamp(ExtensionTypeHeader extensionTypeHeader) throws IOException {
        if (extensionTypeHeader.getType() != -1) {
            throw unexpectedExtension(RtspHeaders.Names.TIMESTAMP, -1, extensionTypeHeader.getType());
        }
        switch (extensionTypeHeader.getLength()) {
            case 4:
                return Instant.ofEpochSecond(readInt() & 4294967295L);
            case 8:
                return Instant.ofEpochSecond(readLong() & 17179869183L, (int) (r0 >>> 34));
            case 12:
                return Instant.ofEpochSecond(readLong(), readInt() & 4294967295L);
            default:
                throw new MessageFormatException(String.format("Timestamp extension type (%d) expects 4, 8, or 12 bytes of payload but got %d bytes", (byte) -1, Integer.valueOf(extensionTypeHeader.getLength())));
        }
    }

    public int unpackArrayHeader() throws IOException {
        byte readByte = readByte();
        if (MessagePack.Code.isFixedArray(readByte)) {
            return readByte & 15;
        }
        switch (readByte) {
            case -36:
                return readNextLength16();
            case -35:
                return readNextLength32();
            default:
                throw unexpected("Array", readByte);
        }
    }

    public int unpackMapHeader() throws IOException {
        byte readByte = readByte();
        if (MessagePack.Code.isFixedMap(readByte)) {
            return readByte & 15;
        }
        switch (readByte) {
            case -34:
                return readNextLength16();
            case -33:
                return readNextLength32();
            default:
                throw unexpected("Map", readByte);
        }
    }

    public ExtensionTypeHeader unpackExtensionTypeHeader() throws IOException {
        byte readByte = readByte();
        switch (readByte) {
            case -57:
                MessageBuffer prepareNumberBuffer = prepareNumberBuffer(2);
                return new ExtensionTypeHeader(prepareNumberBuffer.getByte(this.nextReadPosition + 1), prepareNumberBuffer.getByte(this.nextReadPosition) & 255);
            case -56:
                MessageBuffer prepareNumberBuffer2 = prepareNumberBuffer(3);
                return new ExtensionTypeHeader(prepareNumberBuffer2.getByte(this.nextReadPosition + 2), prepareNumberBuffer2.getShort(this.nextReadPosition) & UShort.MAX_VALUE);
            case -55:
                MessageBuffer prepareNumberBuffer3 = prepareNumberBuffer(5);
                int i = prepareNumberBuffer3.getInt(this.nextReadPosition);
                if (i < 0) {
                    throw overflowU32Size(i);
                }
                return new ExtensionTypeHeader(prepareNumberBuffer3.getByte(this.nextReadPosition + 4), i);
            case -44:
                return new ExtensionTypeHeader(readByte(), 1);
            case -43:
                return new ExtensionTypeHeader(readByte(), 2);
            case -42:
                return new ExtensionTypeHeader(readByte(), 4);
            case -41:
                return new ExtensionTypeHeader(readByte(), 8);
            case -40:
                return new ExtensionTypeHeader(readByte(), 16);
            default:
                throw unexpected("Ext", readByte);
        }
    }

    private int tryReadStringHeader(byte b) throws IOException {
        switch (b) {
            case -39:
                return readNextLength8();
            case -38:
                return readNextLength16();
            case -37:
                return readNextLength32();
            default:
                return -1;
        }
    }

    private int tryReadBinaryHeader(byte b) throws IOException {
        switch (b) {
            case -60:
                return readNextLength8();
            case -59:
                return readNextLength16();
            case -58:
                return readNextLength32();
            default:
                return -1;
        }
    }

    public int unpackRawStringHeader() throws IOException {
        int tryReadBinaryHeader;
        byte readByte = readByte();
        if (MessagePack.Code.isFixedRaw(readByte)) {
            return readByte & Ascii.US;
        }
        int tryReadStringHeader = tryReadStringHeader(readByte);
        if (tryReadStringHeader >= 0) {
            return tryReadStringHeader;
        }
        if (this.allowReadingBinaryAsString && (tryReadBinaryHeader = tryReadBinaryHeader(readByte)) >= 0) {
            return tryReadBinaryHeader;
        }
        throw unexpected("String", readByte);
    }

    public int unpackBinaryHeader() throws IOException {
        int tryReadStringHeader;
        byte readByte = readByte();
        if (MessagePack.Code.isFixedRaw(readByte)) {
            return readByte & Ascii.US;
        }
        int tryReadBinaryHeader = tryReadBinaryHeader(readByte);
        if (tryReadBinaryHeader >= 0) {
            return tryReadBinaryHeader;
        }
        if (this.allowReadingStringAsBinary && (tryReadStringHeader = tryReadStringHeader(readByte)) >= 0) {
            return tryReadStringHeader;
        }
        throw unexpected("Binary", readByte);
    }

    private void skipPayload(int i) throws IOException {
        if (i < 0) {
            throw new IllegalArgumentException("payload size must be >= 0: " + i);
        }
        while (true) {
            int size = this.buffer.size() - this.position;
            if (size >= i) {
                this.position += i;
                return;
            } else {
                this.position += size;
                i -= size;
                nextBuffer();
            }
        }
    }

    public void readPayload(ByteBuffer byteBuffer) throws IOException {
        while (true) {
            int remaining = byteBuffer.remaining();
            int size = this.buffer.size() - this.position;
            if (size >= remaining) {
                this.buffer.getBytes(this.position, remaining, byteBuffer);
                this.position += remaining;
                return;
            } else {
                this.buffer.getBytes(this.position, size, byteBuffer);
                this.position += size;
                nextBuffer();
            }
        }
    }

    public void readPayload(MessageBuffer messageBuffer, int i, int i2) throws IOException {
        while (true) {
            int size = this.buffer.size() - this.position;
            if (size >= i2) {
                messageBuffer.putMessageBuffer(i, this.buffer, this.position, i2);
                this.position += i2;
                return;
            } else {
                messageBuffer.putMessageBuffer(i, this.buffer, this.position, size);
                i += size;
                i2 -= size;
                this.position += size;
                nextBuffer();
            }
        }
    }

    public void readPayload(byte[] bArr) throws IOException {
        readPayload(bArr, 0, bArr.length);
    }

    public byte[] readPayload(int i) throws IOException {
        byte[] bArr = new byte[i];
        readPayload(bArr);
        return bArr;
    }

    public void readPayload(byte[] bArr, int i, int i2) throws IOException {
        while (true) {
            int size = this.buffer.size() - this.position;
            if (size >= i2) {
                this.buffer.getBytes(this.position, bArr, i, i2);
                this.position += i2;
                return;
            } else {
                this.buffer.getBytes(this.position, bArr, i, size);
                i += size;
                i2 -= size;
                this.position += size;
                nextBuffer();
            }
        }
    }

    public MessageBuffer readPayloadAsReference(int i) throws IOException {
        if (this.buffer.size() - this.position >= i) {
            MessageBuffer slice = this.buffer.slice(this.position, i);
            this.position += i;
            return slice;
        }
        MessageBuffer allocate = MessageBuffer.allocate(i);
        readPayload(allocate, 0, i);
        return allocate;
    }

    private int readNextLength8() throws IOException {
        return readByte() & 255;
    }

    private int readNextLength16() throws IOException {
        return readShort() & UShort.MAX_VALUE;
    }

    private int readNextLength32() throws IOException {
        int readInt = readInt();
        if (readInt < 0) {
            throw overflowU32Size(readInt);
        }
        return readInt;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.totalReadBytes += this.position;
        this.buffer = EMPTY_BUFFER;
        this.position = 0;
        this.in.close();
    }

    private static MessageIntegerOverflowException overflowU8(byte b) {
        return new MessageIntegerOverflowException(BigInteger.valueOf(b & 255));
    }

    private static MessageIntegerOverflowException overflowU16(short s) {
        return new MessageIntegerOverflowException(BigInteger.valueOf(s & UShort.MAX_VALUE));
    }

    private static MessageIntegerOverflowException overflowU32(int i) {
        return new MessageIntegerOverflowException(BigInteger.valueOf((i & Integer.MAX_VALUE) + 2147483648L));
    }

    private static MessageIntegerOverflowException overflowU64(long j) {
        return new MessageIntegerOverflowException(BigInteger.valueOf(j + Long.MAX_VALUE + 1).setBit(63));
    }

    private static MessageIntegerOverflowException overflowI16(short s) {
        return new MessageIntegerOverflowException(BigInteger.valueOf(s));
    }

    private static MessageIntegerOverflowException overflowI32(int i) {
        return new MessageIntegerOverflowException(BigInteger.valueOf(i));
    }

    private static MessageIntegerOverflowException overflowI64(long j) {
        return new MessageIntegerOverflowException(BigInteger.valueOf(j));
    }

    private static MessageSizeException overflowU32Size(int i) {
        return new MessageSizeException((i & Integer.MAX_VALUE) + 2147483648L);
    }
}
