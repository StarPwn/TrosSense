package io.airlift.compress.snappy;

import java.util.Arrays;
import kotlin.UShort;

/* loaded from: classes.dex */
public final class SnappyRawCompressor {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int BLOCK_LOG = 16;
    private static final int BLOCK_SIZE = 65536;
    private static final int HIGH_BIT_MASK = 128;
    private static final int INPUT_MARGIN_BYTES = 15;
    private static final int MAX_HASH_TABLE_BITS = 14;
    public static final int MAX_HASH_TABLE_SIZE = 16384;

    private SnappyRawCompressor() {
    }

    public static int maxCompressedLength(int sourceLength) {
        return sourceLength + 32 + (sourceLength / 6);
    }

    public static int compress(final Object inputBase, final long inputAddress, final long inputLimit, final Object outputBase, final long outputAddress, final long outputLimit, final short[] table) {
        int maxCompressedLength;
        long candidateIndex;
        int shift;
        int blockHashTableSize;
        Object obj = inputBase;
        long j = inputLimit;
        int maxCompressedLength2 = maxCompressedLength((int) (j - inputAddress));
        if (outputLimit - outputAddress < maxCompressedLength2) {
            throw new IllegalArgumentException("Output buffer must be at least " + maxCompressedLength2 + " bytes");
        }
        long output = writeUncompressedLength(outputBase, outputAddress, (int) (j - inputAddress));
        long blockAddress = inputAddress;
        while (blockAddress < j) {
            long blockLimit = Math.min(j, blockAddress + 65536);
            long input = blockAddress;
            if (blockLimit - blockAddress > 65536) {
                throw new AssertionError();
            }
            int blockHashTableSize2 = getHashTableSize((int) (blockLimit - blockAddress));
            Arrays.fill(table, 0, blockHashTableSize2, (short) 0);
            int shift2 = 32 - log2Floor(blockHashTableSize2);
            if (((blockHashTableSize2 - 1) & blockHashTableSize2) != 0) {
                throw new AssertionError("table must be power of two");
            }
            if (((-1) >>> shift2) != blockHashTableSize2 - 1) {
                throw new AssertionError();
            }
            long fastInputLimit = blockLimit - 15;
            long nextEmitAddress = input;
            while (input <= fastInputLimit) {
                if (nextEmitAddress > input) {
                    throw new AssertionError();
                }
                int skip = 32;
                long input2 = input + 1;
                long input3 = 0;
                while (true) {
                    candidateIndex = input3;
                    if ((skip >>> 5) + input2 > fastInputLimit) {
                        shift = shift2;
                        blockHashTableSize = blockHashTableSize2;
                        break;
                    }
                    int currentInt = UnsafeUtil.UNSAFE.getInt(obj, input2);
                    int hash = hashBytes(currentInt, shift2);
                    shift = shift2;
                    blockHashTableSize = blockHashTableSize2;
                    long candidateIndex2 = blockAddress + (table[hash] & UShort.MAX_VALUE);
                    if (candidateIndex2 < 0) {
                        throw new AssertionError();
                    }
                    if (candidateIndex2 >= input2) {
                        throw new AssertionError();
                    }
                    table[hash] = (short) (input2 - blockAddress);
                    if (currentInt != UnsafeUtil.UNSAFE.getInt(obj, candidateIndex2)) {
                        input2 += skip >>> 5;
                        skip++;
                        input3 = candidateIndex2;
                        blockHashTableSize2 = blockHashTableSize;
                        shift2 = shift;
                    } else {
                        candidateIndex = candidateIndex2;
                        break;
                    }
                }
                if ((skip >>> 5) + input2 > fastInputLimit) {
                    break;
                }
                if (nextEmitAddress + 16 > blockLimit) {
                    throw new AssertionError();
                }
                int literalLength = (int) (input2 - nextEmitAddress);
                long input4 = input2;
                int shift3 = shift;
                long candidateIndex3 = candidateIndex;
                long output2 = fastCopy(inputBase, nextEmitAddress, outputBase, emitLiteralLength(outputBase, output, literalLength), literalLength);
                while (blockLimit >= input4 + 4) {
                    int matched = count(inputBase, input4 + 4, candidateIndex3 + 4, blockLimit) + 4;
                    output2 = emitCopy(outputBase, output2, input4, candidateIndex3, matched);
                    input4 += matched;
                    if (input4 >= fastInputLimit) {
                        break;
                    }
                    long longValue = UnsafeUtil.UNSAFE.getLong(obj, input4 - 1);
                    int prevInt = (int) longValue;
                    int inputBytes = (int) (longValue >>> 8);
                    int prevHash = hashBytes(prevInt, shift3);
                    table[prevHash] = (short) ((input4 - blockAddress) - 1);
                    int curHash = hashBytes(inputBytes, shift3);
                    long candidateIndex4 = blockAddress + (table[curHash] & UShort.MAX_VALUE);
                    table[curHash] = (short) (input4 - blockAddress);
                    candidateIndex3 = candidateIndex4;
                    if (inputBytes != UnsafeUtil.UNSAFE.getInt(obj, candidateIndex4)) {
                        break;
                    }
                    nextEmitAddress = input4;
                    shift2 = shift3;
                    output = output2;
                    blockHashTableSize2 = blockHashTableSize;
                    input = input4;
                }
                throw new AssertionError();
            }
            if (nextEmitAddress >= blockLimit) {
                maxCompressedLength = maxCompressedLength2;
            } else {
                int literalLength2 = (int) (blockLimit - nextEmitAddress);
                long output3 = emitLiteralLength(outputBase, output, literalLength2);
                maxCompressedLength = maxCompressedLength2;
                UnsafeUtil.UNSAFE.copyMemory(inputBase, nextEmitAddress, outputBase, output3, literalLength2);
                output = output3 + literalLength2;
            }
            blockAddress += 65536;
            j = inputLimit;
            maxCompressedLength2 = maxCompressedLength;
            obj = inputBase;
        }
        return (int) (output - outputAddress);
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

    private static long emitLiteralLength(Object outputBase, long output, int literalLength) {
        long output2;
        int bytes;
        int n = literalLength - 1;
        if (n < 60) {
            long output3 = 1 + output;
            UnsafeUtil.UNSAFE.putByte(outputBase, output, (byte) (n << 2));
            return output3;
        }
        if (n < 256) {
            output2 = 1 + output;
            UnsafeUtil.UNSAFE.putByte(outputBase, output, (byte) -16);
            bytes = 1;
        } else if (n < 65536) {
            output2 = 1 + output;
            UnsafeUtil.UNSAFE.putByte(outputBase, output, (byte) -12);
            bytes = 2;
        } else if (n < 16777216) {
            output2 = 1 + output;
            UnsafeUtil.UNSAFE.putByte(outputBase, output, (byte) -8);
            bytes = 3;
        } else {
            output2 = 1 + output;
            UnsafeUtil.UNSAFE.putByte(outputBase, output, (byte) -4);
            bytes = 4;
        }
        UnsafeUtil.UNSAFE.putInt(outputBase, output2, n);
        return output2 + bytes;
    }

    private static long fastCopy(final Object inputBase, long input, final Object outputBase, long output, final int literalLength) {
        long outputLimit = literalLength + output;
        do {
            UnsafeUtil.UNSAFE.putLong(outputBase, output, UnsafeUtil.UNSAFE.getLong(inputBase, input));
            input += 8;
            output += 8;
        } while (output < outputLimit);
        return outputLimit;
    }

    private static long emitCopy(Object outputBase, long output, long input, long matchIndex, int matchLength) {
        long offset = input - matchIndex;
        long output2 = output;
        int matchLength2 = matchLength;
        while (matchLength2 >= 68) {
            long output3 = 1 + output2;
            UnsafeUtil.UNSAFE.putByte(outputBase, output2, (byte) -2);
            UnsafeUtil.UNSAFE.putShort(outputBase, output3, (short) offset);
            output2 = output3 + 2;
            matchLength2 -= 64;
        }
        if (matchLength2 > 64) {
            long output4 = output2 + 1;
            UnsafeUtil.UNSAFE.putByte(outputBase, output2, (byte) -18);
            UnsafeUtil.UNSAFE.putShort(outputBase, output4, (short) offset);
            output2 = output4 + 2;
            matchLength2 -= 60;
        }
        if (matchLength2 >= 12 || offset >= 2048) {
            long output5 = 1 + output2;
            UnsafeUtil.UNSAFE.putByte(outputBase, output2, (byte) (((matchLength2 - 1) << 2) + 2));
            UnsafeUtil.UNSAFE.putShort(outputBase, output5, (short) offset);
            return output5 + 2;
        }
        int lenMinus4 = matchLength2 - 4;
        long output6 = output2 + 1;
        UnsafeUtil.UNSAFE.putByte(outputBase, output2, (byte) ((lenMinus4 << 2) + 1 + ((offset >>> 8) << 5)));
        long output7 = 1 + output6;
        UnsafeUtil.UNSAFE.putByte(outputBase, output6, (byte) offset);
        return output7;
    }

    private static int getHashTableSize(int inputSize) {
        int target = Integer.highestOneBit(inputSize - 1) << 1;
        return Math.max(Math.min(target, 16384), 256);
    }

    private static int hashBytes(int value, int shift) {
        return (506832829 * value) >>> shift;
    }

    private static int log2Floor(int n) {
        if (n == 0) {
            return -1;
        }
        return Integer.numberOfLeadingZeros(n) ^ 31;
    }

    private static long writeUncompressedLength(Object outputBase, long outputAddress, int uncompressedLength) {
        if (uncompressedLength < 128 && uncompressedLength >= 0) {
            long outputAddress2 = 1 + outputAddress;
            UnsafeUtil.UNSAFE.putByte(outputBase, outputAddress, (byte) uncompressedLength);
            return outputAddress2;
        }
        if (uncompressedLength < 16384 && uncompressedLength > 0) {
            long outputAddress3 = outputAddress + 1;
            UnsafeUtil.UNSAFE.putByte(outputBase, outputAddress, (byte) (uncompressedLength | 128));
            long outputAddress4 = 1 + outputAddress3;
            UnsafeUtil.UNSAFE.putByte(outputBase, outputAddress3, (byte) (uncompressedLength >>> 7));
            return outputAddress4;
        }
        if (uncompressedLength < 2097152 && uncompressedLength > 0) {
            long outputAddress5 = outputAddress + 1;
            UnsafeUtil.UNSAFE.putByte(outputBase, outputAddress, (byte) (uncompressedLength | 128));
            long outputAddress6 = outputAddress5 + 1;
            UnsafeUtil.UNSAFE.putByte(outputBase, outputAddress5, (byte) ((uncompressedLength >>> 7) | 128));
            long outputAddress7 = 1 + outputAddress6;
            UnsafeUtil.UNSAFE.putByte(outputBase, outputAddress6, (byte) (uncompressedLength >>> 14));
            return outputAddress7;
        }
        if (uncompressedLength >= 268435456 || uncompressedLength <= 0) {
            long outputAddress8 = outputAddress + 1;
            UnsafeUtil.UNSAFE.putByte(outputBase, outputAddress, (byte) (uncompressedLength | 128));
            long outputAddress9 = outputAddress8 + 1;
            UnsafeUtil.UNSAFE.putByte(outputBase, outputAddress8, (byte) ((uncompressedLength >>> 7) | 128));
            long outputAddress10 = outputAddress9 + 1;
            UnsafeUtil.UNSAFE.putByte(outputBase, outputAddress9, (byte) ((uncompressedLength >>> 14) | 128));
            long outputAddress11 = outputAddress10 + 1;
            UnsafeUtil.UNSAFE.putByte(outputBase, outputAddress10, (byte) ((uncompressedLength >>> 21) | 128));
            long outputAddress12 = 1 + outputAddress11;
            UnsafeUtil.UNSAFE.putByte(outputBase, outputAddress11, (byte) (uncompressedLength >>> 28));
            return outputAddress12;
        }
        long outputAddress13 = outputAddress + 1;
        UnsafeUtil.UNSAFE.putByte(outputBase, outputAddress, (byte) (uncompressedLength | 128));
        long outputAddress14 = outputAddress13 + 1;
        UnsafeUtil.UNSAFE.putByte(outputBase, outputAddress13, (byte) ((uncompressedLength >>> 7) | 128));
        long outputAddress15 = outputAddress14 + 1;
        UnsafeUtil.UNSAFE.putByte(outputBase, outputAddress14, (byte) ((uncompressedLength >>> 14) | 128));
        long outputAddress16 = 1 + outputAddress15;
        UnsafeUtil.UNSAFE.putByte(outputBase, outputAddress15, (byte) (uncompressedLength >>> 21));
        return outputAddress16;
    }
}
