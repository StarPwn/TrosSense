package io.airlift.compress.zstd;

import io.airlift.compress.MalformedInputException;
import io.airlift.compress.zstd.CompressionParameters;
import kotlin.UShort;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class Util {
    private Util() {
    }

    public static int highestBit(int value) {
        return 31 - Integer.numberOfLeadingZeros(value);
    }

    public static boolean isPowerOf2(int value) {
        return ((value + (-1)) & value) == 0;
    }

    public static int mask(int bits) {
        return (1 << bits) - 1;
    }

    public static void verify(boolean condition, long offset, String reason) {
        if (!condition) {
            throw new MalformedInputException(offset, reason);
        }
    }

    public static void checkArgument(boolean condition, String reason) {
        if (!condition) {
            throw new IllegalArgumentException(reason);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void checkPositionIndexes(int start, int end, int size) {
        if (start < 0 || end < start || end > size) {
            throw new IndexOutOfBoundsException(badPositionIndexes(start, end, size));
        }
    }

    private static String badPositionIndexes(int start, int end, int size) {
        if (start < 0 || start > size) {
            return badPositionIndex(start, size, "start index");
        }
        if (end < 0 || end > size) {
            return badPositionIndex(end, size, "end index");
        }
        return String.format("end index (%s) must not be less than start index (%s)", Integer.valueOf(end), Integer.valueOf(start));
    }

    private static String badPositionIndex(int index, int size, String desc) {
        if (index < 0) {
            return String.format("%s (%s) must not be negative", desc, Integer.valueOf(index));
        }
        if (size < 0) {
            throw new IllegalArgumentException("negative size: " + size);
        }
        return String.format("%s (%s) must not be greater than size (%s)", desc, Integer.valueOf(index), Integer.valueOf(size));
    }

    public static void checkState(boolean condition, String reason) {
        if (!condition) {
            throw new IllegalStateException(reason);
        }
    }

    public static MalformedInputException fail(long offset, String reason) {
        throw new MalformedInputException(offset, reason);
    }

    public static int cycleLog(int hashLog, CompressionParameters.Strategy strategy) {
        if (strategy != CompressionParameters.Strategy.BTLAZY2 && strategy != CompressionParameters.Strategy.BTOPT && strategy != CompressionParameters.Strategy.BTULTRA) {
            return hashLog;
        }
        int cycleLog = hashLog - 1;
        return cycleLog;
    }

    public static int get24BitLittleEndian(Object inputBase, long inputAddress) {
        return (UnsafeUtil.UNSAFE.getShort(inputBase, inputAddress) & UShort.MAX_VALUE) | ((UnsafeUtil.UNSAFE.getByte(inputBase, 2 + inputAddress) & 255) << 16);
    }

    public static void put24BitLittleEndian(Object outputBase, long outputAddress, int value) {
        UnsafeUtil.UNSAFE.putShort(outputBase, outputAddress, (short) value);
        UnsafeUtil.UNSAFE.putByte(outputBase, 2 + outputAddress, (byte) (value >>> 16));
    }

    public static int minTableLog(int inputSize, int maxSymbolValue) {
        if (inputSize > 1) {
            int minBitsSrc = highestBit(inputSize - 1) + 1;
            int minBitsSymbols = highestBit(maxSymbolValue) + 2;
            return Math.min(minBitsSrc, minBitsSymbols);
        }
        throw new IllegalArgumentException("Not supported. RLE should be used instead");
    }
}
