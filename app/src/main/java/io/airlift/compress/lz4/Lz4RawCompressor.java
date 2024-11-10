package io.airlift.compress.lz4;

/* loaded from: classes.dex */
public final class Lz4RawCompressor {
    private static final int COPY_LENGTH = 8;
    private static final int HASH_LOG = 12;
    private static final int MATCH_FIND_LIMIT = 12;
    private static final int MAX_DISTANCE = 65535;
    private static final int MAX_INPUT_SIZE = 2113929216;
    public static final int MAX_TABLE_SIZE = 4096;
    private static final int MIN_LENGTH = 13;
    private static final int MIN_TABLE_SIZE = 16;
    private static final int ML_BITS = 4;
    private static final int ML_MASK = 15;
    private static final int RUN_BITS = 4;
    private static final int RUN_MASK = 15;
    private static final int SKIP_TRIGGER = 6;

    private Lz4RawCompressor() {
    }

    private static int hash(long value, int mask) {
        return (int) (((889523592379L * value) >>> 28) & mask);
    }

    public static int maxCompressedLength(int sourceLength) {
        return (sourceLength / 255) + sourceLength + 16;
    }

    /* JADX WARN: Code restructure failed: missing block: B:19:0x00c3, code lost:            r35 = r1;        r8 = r0;     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x00c8, code lost:            if (r35 <= r33) goto L54;     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x00cc, code lost:            if (r8 <= r48) goto L55;     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x00de, code lost:            if (io.airlift.compress.lz4.UnsafeUtil.UNSAFE.getByte(r47, r35 - 1) != io.airlift.compress.lz4.UnsafeUtil.UNSAFE.getByte(r47, r8 - 1)) goto L56;     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x00e0, code lost:            r35 = r35 - 1;        r8 = r8 - 1;     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x00e5, code lost:            r4 = (int) (r35 - r33);        r5 = r24;        r45 = r5;        r24 = r8;        r8 = emitLiteral(r47, r51, r33, r4, r5);     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x00fb, code lost:            r0 = count(r47, r35 + 4, r19, r24 + 4);        r8 = emitMatch(r51, r8, r45, (short) (r35 - r24), r0);        r1 = r35 + (r0 + 4);     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x0126, code lost:            if (r1 <= r17) goto L32;     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x0131, code lost:            r3 = r1 - 2;        r35 = r13;        r13 = r3 - r48;        r10[hash(io.airlift.compress.lz4.UnsafeUtil.UNSAFE.getLong(r47, r3), r12)] = (int) r13;        r5 = hash(io.airlift.compress.lz4.UnsafeUtil.UNSAFE.getLong(r47, r1), r12);        r13 = r48 + r10[r5];        r10[r5] = (int) (r1 - r48);     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x0160, code lost:            if ((r13 + okhttp3.internal.ws.WebSocketProtocol.PAYLOAD_SHORT_MAX) < r1) goto L57;     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x016e, code lost:            if (io.airlift.compress.lz4.UnsafeUtil.UNSAFE.getInt(r47, r13) == io.airlift.compress.lz4.UnsafeUtil.UNSAFE.getInt(r47, r1)) goto L37;     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x0176, code lost:            io.airlift.compress.lz4.UnsafeUtil.UNSAFE.putByte(r51, r8, (byte) 0);        r10 = r56;        r45 = r8;        r24 = r13;        r13 = r35;        r35 = r1;        r8 = r8 + 1;     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x0170, code lost:            r0 = 0;     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x0194, code lost:            r1 = r1 + 1;        r3 = hash(io.airlift.compress.lz4.UnsafeUtil.UNSAFE.getLong(r47, r1), r12);        r13 = r1;        r26 = r3;     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x01a3, code lost:            if (r23 == false) goto L43;     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x01b7, code lost:            return (int) (emitLastLiteral(r51, r8, r47, r1, r15 - r1) - r52);     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x018f, code lost:            r0 = 0;     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x0128, code lost:            r13 = r1;        r23 = true;        r0 = 0;     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static int compress(final java.lang.Object r47, final long r48, final int r50, final java.lang.Object r51, final long r52, final long r54, final int[] r56) {
        /*
            Method dump skipped, instructions count: 521
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.airlift.compress.lz4.Lz4RawCompressor.compress(java.lang.Object, long, int, java.lang.Object, long, long, int[]):int");
    }

    private static long emitLiteral(Object inputBase, Object outputBase, long input, int literalLength, long output) {
        long output2 = encodeRunLength(outputBase, output, literalLength);
        long outputLimit = literalLength + output2;
        do {
            UnsafeUtil.UNSAFE.putLong(outputBase, output2, UnsafeUtil.UNSAFE.getLong(inputBase, input));
            input += 8;
            output2 += 8;
        } while (output2 < outputLimit);
        return outputLimit;
    }

    private static long emitMatch(Object outputBase, long output, long tokenAddress, short offset, long matchLength) {
        UnsafeUtil.UNSAFE.putShort(outputBase, output, offset);
        long output2 = output + 2;
        if (matchLength >= 15) {
            UnsafeUtil.UNSAFE.putByte(outputBase, tokenAddress, (byte) (UnsafeUtil.UNSAFE.getByte(outputBase, tokenAddress) | 15));
            long remaining = matchLength - 15;
            while (remaining >= 510) {
                UnsafeUtil.UNSAFE.putShort(outputBase, output2, (short) -1);
                output2 += 2;
                remaining -= 510;
            }
            if (remaining >= 255) {
                UnsafeUtil.UNSAFE.putByte(outputBase, output2, (byte) -1);
                remaining -= 255;
                output2++;
            }
            long output3 = output2 + 1;
            UnsafeUtil.UNSAFE.putByte(outputBase, output2, (byte) remaining);
            return output3;
        }
        UnsafeUtil.UNSAFE.putByte(outputBase, tokenAddress, (byte) (UnsafeUtil.UNSAFE.getByte(outputBase, tokenAddress) | matchLength));
        return output2;
    }

    static int count(Object inputBase, final long inputAddress, final long inputLimit, final long matchAddress) {
        long input = inputAddress;
        long match = matchAddress;
        int remaining = (int) (inputLimit - inputAddress);
        int count = 0;
        while (count < remaining - 7) {
            long diff = UnsafeUtil.UNSAFE.getLong(inputBase, match) ^ UnsafeUtil.UNSAFE.getLong(inputBase, input);
            if (diff != 0) {
                return (Long.numberOfTrailingZeros(diff) >> 3) + count;
            }
            count += 8;
            input += 8;
            match += 8;
        }
        while (count < remaining && UnsafeUtil.UNSAFE.getByte(inputBase, match) == UnsafeUtil.UNSAFE.getByte(inputBase, input)) {
            count++;
            match++;
            input++;
        }
        return count;
    }

    private static long emitLastLiteral(final Object outputBase, final long outputAddress, final Object inputBase, final long inputAddress, final long length) {
        long output = encodeRunLength(outputBase, outputAddress, length);
        UnsafeUtil.UNSAFE.copyMemory(inputBase, inputAddress, outputBase, output, length);
        return output + length;
    }

    private static long encodeRunLength(final Object base, long output, final long length) {
        if (length >= 15) {
            long output2 = output + 1;
            UnsafeUtil.UNSAFE.putByte(base, output, (byte) -16);
            long remaining = length - 15;
            while (remaining >= 255) {
                UnsafeUtil.UNSAFE.putByte(base, output2, (byte) -1);
                remaining -= 255;
                output2++;
            }
            long output3 = 1 + output2;
            UnsafeUtil.UNSAFE.putByte(base, output2, (byte) remaining);
            return output3;
        }
        long output4 = 1 + output;
        UnsafeUtil.UNSAFE.putByte(base, output, (byte) (length << 4));
        return output4;
    }

    private static int computeTableSize(int inputSize) {
        int target = Integer.highestOneBit(inputSize - 1) << 1;
        return Math.max(Math.min(target, 4096), 16);
    }
}
