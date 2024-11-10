package io.airlift.compress.lzo;

/* loaded from: classes.dex */
public final class LzoRawCompressor {
    private static final int COPY_LENGTH = 8;
    private static final int HASH_LOG = 12;
    public static final int LAST_LITERAL_SIZE = 5;
    private static final int MATCH_FIND_LIMIT = 12;
    private static final int MAX_DISTANCE = 49151;
    private static final int MAX_INPUT_SIZE = 2113929216;
    public static final int MAX_TABLE_SIZE = 4096;
    private static final int MIN_LENGTH = 13;
    public static final int MIN_MATCH = 4;
    private static final int MIN_TABLE_SIZE = 16;
    private static final int ML_BITS = 4;
    private static final int RUN_BITS = 4;
    private static final int RUN_MASK = 15;
    private static final int SKIP_TRIGGER = 6;

    private LzoRawCompressor() {
    }

    private static int hash(long value, int mask) {
        return (int) (((889523592379L * value) >>> 28) & mask);
    }

    public static int maxCompressedLength(int sourceLength) {
        return (sourceLength / 255) + sourceLength + 16;
    }

    /* JADX WARN: Code restructure failed: missing block: B:21:0x00d4, code lost:            r38 = r7;        r14 = r0;     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x00d9, code lost:            if (r38 <= r34) goto L55;     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x00dd, code lost:            if (r14 <= r43) goto L56;     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x00ef, code lost:            if (io.airlift.compress.lzo.UnsafeUtil.UNSAFE.getByte(r42, r38 - 1) != io.airlift.compress.lzo.UnsafeUtil.UNSAFE.getByte(r42, r14 - 1)) goto L57;     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x00f1, code lost:            r38 = r38 - 1;        r14 = r14 - 1;     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x00f6, code lost:            r7 = (int) (r38 - r34);        r0 = emitLiteral(r25, r42, r34, r46, r26, r7);        r25 = false;        r26 = r14;        r14 = r0;     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x010e, code lost:            r0 = r38 - r26;        r7 = (int) r0;        r38 = r38 + 4;        r0 = count(r42, r38, r26 + 4, r20);        r1 = r38 + r0;        r14 = emitCopy(r46, r14, r7, r0 + 4);     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x0130, code lost:            if (r1 <= r18) goto L34;     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x0137, code lost:            r3 = r1 - 2;        r51[hash(io.airlift.compress.lzo.UnsafeUtil.UNSAFE.getLong(r42, r3), r33)] = (int) (r3 - r43);        r5 = hash(io.airlift.compress.lzo.UnsafeUtil.UNSAFE.getLong(r42, r1), r33);        r6 = r43 + r51[r5];        r51[r5] = (int) (r1 - r43);     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x0166, code lost:            if ((r6 + 49151) < r1) goto L60;     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x0174, code lost:            if (io.airlift.compress.lzo.UnsafeUtil.UNSAFE.getInt(r42, r6) == io.airlift.compress.lzo.UnsafeUtil.UNSAFE.getInt(r42, r1)) goto L39;     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x0177, code lost:            r38 = r1;        r26 = r6;     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x017c, code lost:            r1 = r1 + 1;        r3 = hash(io.airlift.compress.lzo.UnsafeUtil.UNSAFE.getLong(r42, r1), r33);        r12 = r1;        r28 = r3;     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x018b, code lost:            if (r24 == false) goto L44;     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x01a2, code lost:            return (int) (emitLastLiteral(false, r46, r14, r42, r1, r16 - r1) - r47);     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x0132, code lost:            r12 = r1;        r24 = true;     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static int compress(final java.lang.Object r42, final long r43, final int r45, final java.lang.Object r46, final long r47, final long r49, final int[] r51) {
        /*
            Method dump skipped, instructions count: 487
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.airlift.compress.lzo.LzoRawCompressor.compress(java.lang.Object, long, int, java.lang.Object, long, long, int[]):int");
    }

    private static int count(Object inputBase, final long start, long matchStart, long matchLimit) {
        long current = start;
        while (current < matchLimit - 7) {
            long diff = UnsafeUtil.UNSAFE.getLong(inputBase, matchStart) ^ UnsafeUtil.UNSAFE.getLong(inputBase, current);
            if (diff != 0) {
                return (int) ((current + (Long.numberOfTrailingZeros(diff) >> 3)) - start);
            }
            current += 8;
            matchStart += 8;
        }
        if (current < matchLimit - 3 && UnsafeUtil.UNSAFE.getInt(inputBase, matchStart) == UnsafeUtil.UNSAFE.getInt(inputBase, current)) {
            current += 4;
            matchStart += 4;
        }
        if (current < matchLimit - 1 && UnsafeUtil.UNSAFE.getShort(inputBase, matchStart) == UnsafeUtil.UNSAFE.getShort(inputBase, current)) {
            current += 2;
            matchStart += 2;
        }
        if (current < matchLimit && UnsafeUtil.UNSAFE.getByte(inputBase, matchStart) == UnsafeUtil.UNSAFE.getByte(inputBase, current)) {
            current++;
        }
        return (int) (current - start);
    }

    private static long emitLastLiteral(boolean firstLiteral, final Object outputBase, long output, final Object inputBase, final long inputAddress, final long literalLength) {
        long output2 = encodeLiteralLength(firstLiteral, outputBase, output, literalLength);
        UnsafeUtil.UNSAFE.copyMemory(inputBase, inputAddress, outputBase, output2, literalLength);
        long output3 = output2 + literalLength;
        long output4 = 1 + output3;
        UnsafeUtil.UNSAFE.putByte(outputBase, output3, (byte) 17);
        UnsafeUtil.UNSAFE.putShort(outputBase, output4, (short) 0);
        return output4 + 2;
    }

    private static long emitLiteral(boolean firstLiteral, Object inputBase, long input, Object outputBase, long output, int literalLength) {
        long output2 = encodeLiteralLength(firstLiteral, outputBase, output, literalLength);
        long outputLimit = literalLength + output2;
        do {
            UnsafeUtil.UNSAFE.putLong(outputBase, output2, UnsafeUtil.UNSAFE.getLong(inputBase, input));
            input += 8;
            output2 += 8;
        } while (output2 < outputLimit);
        return outputLimit;
    }

    private static long encodeLiteralLength(boolean firstLiteral, final Object outBase, long output, long length) {
        if (firstLiteral && length < 238) {
            long output2 = 1 + output;
            UnsafeUtil.UNSAFE.putByte(outBase, output, (byte) (17 + length));
            return output2;
        }
        if (length < 4) {
            UnsafeUtil.UNSAFE.putByte(outBase, output - 2, (byte) (UnsafeUtil.UNSAFE.getByte(outBase, output - 2) | length));
            return output;
        }
        long length2 = length - 3;
        if (length2 > 15) {
            long output3 = output + 1;
            UnsafeUtil.UNSAFE.putByte(outBase, output, (byte) 0);
            long remaining = length2 - 15;
            while (remaining > 255) {
                UnsafeUtil.UNSAFE.putByte(outBase, output3, (byte) 0);
                remaining -= 255;
                output3++;
            }
            long output4 = 1 + output3;
            UnsafeUtil.UNSAFE.putByte(outBase, output3, (byte) remaining);
            return output4;
        }
        long output5 = 1 + output;
        UnsafeUtil.UNSAFE.putByte(outBase, output, (byte) length2);
        return output5;
    }

    private static long emitCopy(Object outputBase, long output, int matchOffset, int matchLength) {
        long output2;
        if (matchOffset > MAX_DISTANCE || matchOffset < 1) {
            throw new IllegalArgumentException("Unsupported copy offset: " + matchOffset);
        }
        if (matchLength <= 8 && matchOffset <= 2048) {
            int matchOffset2 = matchOffset - 1;
            long output3 = output + 1;
            UnsafeUtil.UNSAFE.putByte(outputBase, output, (byte) (((matchLength - 1) << 5) | ((matchOffset2 & 7) << 2)));
            long output4 = 1 + output3;
            UnsafeUtil.UNSAFE.putByte(outputBase, output3, (byte) (matchOffset2 >>> 3));
            return output4;
        }
        int matchLength2 = matchLength - 2;
        if (matchOffset >= 32768) {
            output2 = encodeMatchLength(outputBase, output, matchLength2, 7, 24);
        } else if (matchOffset > 16384) {
            output2 = encodeMatchLength(outputBase, output, matchLength2, 7, 16);
        } else {
            output2 = encodeMatchLength(outputBase, output, matchLength2, 31, 32);
            matchOffset--;
        }
        return encodeOffset(outputBase, output2, matchOffset);
    }

    private static long encodeOffset(final Object outputBase, final long outputAddress, final int offset) {
        UnsafeUtil.UNSAFE.putShort(outputBase, outputAddress, (short) (offset << 2));
        return 2 + outputAddress;
    }

    private static long encodeMatchLength(Object outputBase, long output, int matchLength, int baseMatchLength, int command) {
        if (matchLength <= baseMatchLength) {
            long output2 = 1 + output;
            UnsafeUtil.UNSAFE.putByte(outputBase, output, (byte) (command | matchLength));
            return output2;
        }
        long output3 = output + 1;
        UnsafeUtil.UNSAFE.putByte(outputBase, output, (byte) command);
        long remaining = matchLength - baseMatchLength;
        while (remaining > 510) {
            UnsafeUtil.UNSAFE.putShort(outputBase, output3, (short) 0);
            output3 += 2;
            remaining -= 510;
        }
        if (remaining > 255) {
            UnsafeUtil.UNSAFE.putByte(outputBase, output3, (byte) 0);
            remaining -= 255;
            output3++;
        }
        long output4 = 1 + output3;
        UnsafeUtil.UNSAFE.putByte(outputBase, output3, (byte) remaining);
        return output4;
    }

    private static int computeTableSize(int inputSize) {
        int target = Integer.highestOneBit(inputSize - 1) << 1;
        return Math.max(Math.min(target, 4096), 16);
    }
}
