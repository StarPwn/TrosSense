package io.netty.handler.codec.compression;

import com.trossense.bl;
import io.netty.buffer.ByteBuf;
import io.netty.util.concurrent.FastThreadLocal;
import io.netty.util.internal.SystemPropertyUtil;
import java.util.Arrays;

/* loaded from: classes4.dex */
public final class Snappy {
    private static final int COPY_1_BYTE_OFFSET = 1;
    private static final int COPY_2_BYTE_OFFSET = 2;
    private static final int COPY_4_BYTE_OFFSET = 3;
    private static final int LITERAL = 0;
    private static final int MAX_HT_SIZE = 16384;
    private static final int MIN_COMPRESSIBLE_BYTES = 15;
    private static final int NOT_ENOUGH_INPUT = -1;
    private static final int PREAMBLE_NOT_FULL = -1;
    private final boolean reuseHashtable;
    private State state;
    private byte tag;
    private int written;
    private static final FastThreadLocal<short[]> HASH_TABLE = new FastThreadLocal<>();
    private static final boolean DEFAULT_REUSE_HASHTABLE = SystemPropertyUtil.getBoolean("io.netty.handler.codec.compression.snappy.reuseHashTable", false);

    /* loaded from: classes4.dex */
    private enum State {
        READING_PREAMBLE,
        READING_TAG,
        READING_LITERAL,
        READING_COPY
    }

    public Snappy() {
        this(DEFAULT_REUSE_HASHTABLE);
    }

    Snappy(boolean reuseHashtable) {
        this.state = State.READING_PREAMBLE;
        this.reuseHashtable = reuseHashtable;
    }

    public static Snappy withHashTableReuse() {
        return new Snappy(true);
    }

    public void reset() {
        this.state = State.READING_PREAMBLE;
        this.tag = (byte) 0;
        this.written = 0;
    }

    /* JADX WARN: Code restructure failed: missing block: B:16:0x0077, code lost:            encodeLiteral(r21, r22, r3 - r11);     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x007c, code lost:            r5 = r3;        r9 = findMatchingLength(r21, r15 + 4, r3 + 4, r23) + 4;        r3 = r3 + r9;        r13 = r5 - r15;        encodeCopy(r22, r13, r9);        r5 = r21.readerIndex() + r9;        r21.readerIndex(r5);        r5 = r3 - 1;        r11 = r3;     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x009d, code lost:            if (r3 < (r23 - 4)) goto L19;     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x00a1, code lost:            r6 = hash(r21, r5, r8);        r7[r6] = (short) ((r3 - r3) - 1);        r9 = hash(r21, r5 + 1, r8);        r15 = r3 + r7[r9];        r6 = r3 - r3;        r7[r9] = (short) r6;     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x00c9, code lost:            if (r21.getInt(r5 + 1) == r21.getInt(r15)) goto L22;     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x009f, code lost:            r9 = r11;     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void encode(io.netty.buffer.ByteBuf r21, io.netty.buffer.ByteBuf r22, int r23) {
        /*
            Method dump skipped, instructions count: 238
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.handler.codec.compression.Snappy.encode(io.netty.buffer.ByteBuf, io.netty.buffer.ByteBuf, int):void");
    }

    private static int hash(ByteBuf in, int index, int shift) {
        return (in.getInt(index) * 506832829) >>> shift;
    }

    private short[] getHashTable(int hashTableSize) {
        if (this.reuseHashtable) {
            return getHashTableFastThreadLocalArrayFill(hashTableSize);
        }
        return new short[hashTableSize];
    }

    public static short[] getHashTableFastThreadLocalArrayFill(int hashTableSize) {
        short[] hashTable = HASH_TABLE.get();
        if (hashTable == null || hashTable.length < hashTableSize) {
            short[] hashTable2 = new short[hashTableSize];
            HASH_TABLE.set(hashTable2);
            return hashTable2;
        }
        Arrays.fill(hashTable, 0, hashTableSize, (short) 0);
        return hashTable;
    }

    private static int findMatchingLength(ByteBuf in, int minIndex, int inIndex, int maxIndex) {
        int matched = 0;
        while (inIndex <= maxIndex - 4 && in.getInt(inIndex) == in.getInt(minIndex + matched)) {
            inIndex += 4;
            matched += 4;
        }
        while (inIndex < maxIndex && in.getByte(minIndex + matched) == in.getByte(inIndex)) {
            inIndex++;
            matched++;
        }
        return matched;
    }

    private static int bitsToEncode(int value) {
        int highestOneBit = Integer.highestOneBit(value);
        int bitLength = 0;
        while (true) {
            int i = highestOneBit >> 1;
            highestOneBit = i;
            if (i != 0) {
                bitLength++;
            } else {
                return bitLength;
            }
        }
    }

    static void encodeLiteral(ByteBuf in, ByteBuf out, int length) {
        if (length < 61) {
            out.writeByte((length - 1) << 2);
        } else {
            int bitLength = bitsToEncode(length - 1);
            int bytesToEncode = (bitLength / 8) + 1;
            out.writeByte((bytesToEncode + 59) << 2);
            for (int i = 0; i < bytesToEncode; i++) {
                out.writeByte(((length - 1) >> (i * 8)) & 255);
            }
        }
        out.writeBytes(in, length);
    }

    private static void encodeCopyWithOffset(ByteBuf out, int offset, int length) {
        if (length < 12 && offset < 2048) {
            out.writeByte(((length - 4) << 2) | 1 | ((offset >> 8) << 5));
            out.writeByte(offset & 255);
        } else {
            out.writeByte(((length - 1) << 2) | 2);
            out.writeByte(offset & 255);
            out.writeByte((offset >> 8) & 255);
        }
    }

    private static void encodeCopy(ByteBuf out, int offset, int length) {
        while (length >= 68) {
            encodeCopyWithOffset(out, offset, 64);
            length -= 64;
        }
        if (length > 64) {
            encodeCopyWithOffset(out, offset, 60);
            length -= 60;
        }
        encodeCopyWithOffset(out, offset, length);
    }

    /* JADX WARN: Code restructure failed: missing block: B:31:0x0001, code lost:            continue;     */
    /* JADX WARN: Failed to find 'out' block for switch in B:4:0x0012. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:46:0x008b  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x008a A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void decode(io.netty.buffer.ByteBuf r4, io.netty.buffer.ByteBuf r5) {
        /*
            r3 = this;
        L1:
            boolean r0 = r4.isReadable()
            if (r0 == 0) goto La6
            int[] r0 = io.netty.handler.codec.compression.Snappy.AnonymousClass1.$SwitchMap$io$netty$handler$codec$compression$Snappy$State
            io.netty.handler.codec.compression.Snappy$State r1 = r3.state
            int r1 = r1.ordinal()
            r0 = r0[r1]
            r1 = -1
            switch(r0) {
                case 1: goto L73;
                case 2: goto L84;
                case 3: goto L60;
                case 4: goto L17;
                default: goto L15;
            }
        L15:
            goto La4
        L17:
            byte r0 = r3.tag
            r0 = r0 & 3
            switch(r0) {
                case 1: goto L4b;
                case 2: goto L36;
                case 3: goto L20;
                default: goto L1e;
            }
        L1e:
            goto La4
        L20:
            byte r0 = r3.tag
            int r2 = r3.written
            int r0 = decodeCopyWith4ByteOffset(r0, r4, r5, r2)
            if (r0 == r1) goto L35
            io.netty.handler.codec.compression.Snappy$State r1 = io.netty.handler.codec.compression.Snappy.State.READING_TAG
            r3.state = r1
            int r1 = r3.written
            int r1 = r1 + r0
            r3.written = r1
            goto La4
        L35:
            return
        L36:
            byte r0 = r3.tag
            int r2 = r3.written
            int r0 = decodeCopyWith2ByteOffset(r0, r4, r5, r2)
            if (r0 == r1) goto L4a
            io.netty.handler.codec.compression.Snappy$State r1 = io.netty.handler.codec.compression.Snappy.State.READING_TAG
            r3.state = r1
            int r1 = r3.written
            int r1 = r1 + r0
            r3.written = r1
            goto La4
        L4a:
            return
        L4b:
            byte r0 = r3.tag
            int r2 = r3.written
            int r0 = decodeCopyWith1ByteOffset(r0, r4, r5, r2)
            if (r0 == r1) goto L5f
            io.netty.handler.codec.compression.Snappy$State r1 = io.netty.handler.codec.compression.Snappy.State.READING_TAG
            r3.state = r1
            int r1 = r3.written
            int r1 = r1 + r0
            r3.written = r1
            goto La4
        L5f:
            return
        L60:
            byte r0 = r3.tag
            int r0 = decodeLiteral(r0, r4, r5)
            if (r0 == r1) goto L72
            io.netty.handler.codec.compression.Snappy$State r1 = io.netty.handler.codec.compression.Snappy.State.READING_TAG
            r3.state = r1
            int r1 = r3.written
            int r1 = r1 + r0
            r3.written = r1
            goto La4
        L72:
            return
        L73:
            int r0 = readPreamble(r4)
            if (r0 != r1) goto L7a
            return
        L7a:
            if (r0 != 0) goto L7d
            return
        L7d:
            r5.ensureWritable(r0)
            io.netty.handler.codec.compression.Snappy$State r1 = io.netty.handler.codec.compression.Snappy.State.READING_TAG
            r3.state = r1
        L84:
            boolean r0 = r4.isReadable()
            if (r0 != 0) goto L8b
            return
        L8b:
            byte r0 = r4.readByte()
            r3.tag = r0
            byte r0 = r3.tag
            r0 = r0 & 3
            switch(r0) {
                case 0: goto L9e;
                case 1: goto L99;
                case 2: goto L99;
                case 3: goto L99;
                default: goto L98;
            }
        L98:
            goto La3
        L99:
            io.netty.handler.codec.compression.Snappy$State r0 = io.netty.handler.codec.compression.Snappy.State.READING_COPY
            r3.state = r0
            goto La3
        L9e:
            io.netty.handler.codec.compression.Snappy$State r0 = io.netty.handler.codec.compression.Snappy.State.READING_LITERAL
            r3.state = r0
        La3:
        La4:
            goto L1
        La6:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.handler.codec.compression.Snappy.decode(io.netty.buffer.ByteBuf, io.netty.buffer.ByteBuf):void");
    }

    private static int readPreamble(ByteBuf in) {
        int length = 0;
        int byteIndex = 0;
        while (in.isReadable()) {
            int current = in.readUnsignedByte();
            int byteIndex2 = byteIndex + 1;
            length |= (current & 127) << (byteIndex * 7);
            if ((current & 128) == 0) {
                return length;
            }
            if (byteIndex2 >= 4) {
                throw new DecompressionException("Preamble is greater than 4 bytes");
            }
            byteIndex = byteIndex2;
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getPreamble(ByteBuf in) {
        if (this.state == State.READING_PREAMBLE) {
            int readerIndex = in.readerIndex();
            try {
                return readPreamble(in);
            } finally {
                in.readerIndex(readerIndex);
            }
        }
        return 0;
    }

    static int decodeLiteral(byte tag, ByteBuf in, ByteBuf out) {
        int length;
        in.markReaderIndex();
        switch ((tag >> 2) & 63) {
            case 60:
                if (!in.isReadable()) {
                    return -1;
                }
                length = in.readUnsignedByte();
                break;
            case 61:
                int length2 = in.readableBytes();
                if (length2 >= 2) {
                    length = in.readUnsignedShortLE();
                    break;
                } else {
                    return -1;
                }
            case 62:
                int length3 = in.readableBytes();
                if (length3 >= 3) {
                    length = in.readUnsignedMediumLE();
                    break;
                } else {
                    return -1;
                }
            case 63:
                int length4 = in.readableBytes();
                if (length4 >= 4) {
                    length = in.readIntLE();
                    break;
                } else {
                    return -1;
                }
            default:
                length = (tag >> 2) & 63;
                break;
        }
        int length5 = length + 1;
        if (in.readableBytes() < length5) {
            in.resetReaderIndex();
            return -1;
        }
        out.writeBytes(in, length5);
        return length5;
    }

    private static int decodeCopyWith1ByteOffset(byte tag, ByteBuf in, ByteBuf out, int writtenSoFar) {
        if (!in.isReadable()) {
            return -1;
        }
        int initialIndex = out.writerIndex();
        int length = ((tag & 28) >> 2) + 4;
        int offset = (((tag & bl.cW) << 8) >> 5) | in.readUnsignedByte();
        validateOffset(offset, writtenSoFar);
        out.markReaderIndex();
        if (offset < length) {
            for (int copies = length / offset; copies > 0; copies--) {
                out.readerIndex(initialIndex - offset);
                out.readBytes(out, offset);
            }
            if (length % offset != 0) {
                out.readerIndex(initialIndex - offset);
                out.readBytes(out, length % offset);
            }
        } else {
            out.readerIndex(initialIndex - offset);
            out.readBytes(out, length);
        }
        out.resetReaderIndex();
        return length;
    }

    private static int decodeCopyWith2ByteOffset(byte tag, ByteBuf in, ByteBuf out, int writtenSoFar) {
        if (in.readableBytes() < 2) {
            return -1;
        }
        int initialIndex = out.writerIndex();
        int length = ((tag >> 2) & 63) + 1;
        int offset = in.readUnsignedShortLE();
        validateOffset(offset, writtenSoFar);
        out.markReaderIndex();
        if (offset < length) {
            for (int copies = length / offset; copies > 0; copies--) {
                out.readerIndex(initialIndex - offset);
                out.readBytes(out, offset);
            }
            if (length % offset != 0) {
                out.readerIndex(initialIndex - offset);
                out.readBytes(out, length % offset);
            }
        } else {
            out.readerIndex(initialIndex - offset);
            out.readBytes(out, length);
        }
        out.resetReaderIndex();
        return length;
    }

    private static int decodeCopyWith4ByteOffset(byte tag, ByteBuf in, ByteBuf out, int writtenSoFar) {
        if (in.readableBytes() < 4) {
            return -1;
        }
        int initialIndex = out.writerIndex();
        int length = ((tag >> 2) & 63) + 1;
        int offset = in.readIntLE();
        validateOffset(offset, writtenSoFar);
        out.markReaderIndex();
        if (offset < length) {
            for (int copies = length / offset; copies > 0; copies--) {
                out.readerIndex(initialIndex - offset);
                out.readBytes(out, offset);
            }
            if (length % offset != 0) {
                out.readerIndex(initialIndex - offset);
                out.readBytes(out, length % offset);
            }
        } else {
            out.readerIndex(initialIndex - offset);
            out.readBytes(out, length);
        }
        out.resetReaderIndex();
        return length;
    }

    private static void validateOffset(int offset, int chunkSizeSoFar) {
        if (offset == 0) {
            throw new DecompressionException("Offset is less than minimum permissible value");
        }
        if (offset < 0) {
            throw new DecompressionException("Offset is greater than maximum value supported by this implementation");
        }
        if (offset > chunkSizeSoFar) {
            throw new DecompressionException("Offset exceeds size of chunk");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int calculateChecksum(ByteBuf data) {
        return calculateChecksum(data, data.readerIndex(), data.readableBytes());
    }

    static int calculateChecksum(ByteBuf data, int offset, int length) {
        Crc32c crc32 = new Crc32c();
        try {
            crc32.update(data, offset, length);
            return maskChecksum(crc32.getValue());
        } finally {
            crc32.reset();
        }
    }

    static void validateChecksum(int expectedChecksum, ByteBuf data) {
        validateChecksum(expectedChecksum, data, data.readerIndex(), data.readableBytes());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void validateChecksum(int expectedChecksum, ByteBuf data, int offset, int length) {
        int actualChecksum = calculateChecksum(data, offset, length);
        if (actualChecksum != expectedChecksum) {
            throw new DecompressionException("mismatching checksum: " + Integer.toHexString(actualChecksum) + " (expected: " + Integer.toHexString(expectedChecksum) + ')');
        }
    }

    static int maskChecksum(long checksum) {
        return (int) (((checksum >> 15) | (checksum << 17)) - 1568478504);
    }
}
