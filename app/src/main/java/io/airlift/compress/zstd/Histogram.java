package io.airlift.compress.zstd;

import java.util.Arrays;
import sun.misc.Unsafe;

/* loaded from: classes.dex */
class Histogram {
    private Histogram() {
    }

    private static void count(Object inputBase, long inputAddress, int inputSize, int[] counts) {
        long input = inputAddress;
        Arrays.fill(counts, 0);
        for (int i = 0; i < inputSize; i++) {
            int symbol = UnsafeUtil.UNSAFE.getByte(inputBase, input) & 255;
            input++;
            counts[symbol] = counts[symbol] + 1;
        }
    }

    public static int findLargestCount(int[] counts, int maxSymbol) {
        int max = 0;
        for (int i = 0; i <= maxSymbol; i++) {
            if (counts[i] > max) {
                max = counts[i];
            }
        }
        return max;
    }

    public static int findMaxSymbol(int[] counts, int maxSymbol) {
        while (counts[maxSymbol] == 0) {
            maxSymbol--;
        }
        return maxSymbol;
    }

    public static void count(byte[] input, int length, int[] counts) {
        count(input, Unsafe.ARRAY_BYTE_BASE_OFFSET, length, counts);
    }
}
