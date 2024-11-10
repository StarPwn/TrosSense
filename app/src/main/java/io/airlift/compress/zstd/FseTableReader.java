package io.airlift.compress.zstd;

import io.airlift.compress.zstd.FiniteStateEntropy;

/* loaded from: classes.dex */
class FseTableReader {
    private final short[] nextSymbol = new short[256];
    private final short[] normalizedCounters = new short[256];

    public int readFseTable(FiniteStateEntropy.Table table, Object inputBase, long inputAddress, long inputLimit, int maxSymbol, int maxTableLog) {
        boolean z;
        short count;
        int bitCount;
        boolean previousIsZero;
        int remaining;
        FseTableReader fseTableReader = this;
        int i = maxSymbol;
        long input = inputAddress;
        Util.verify(inputLimit - inputAddress >= 4, input, "Not enough input bytes");
        int symbolNumber = 0;
        boolean previousIsZero2 = false;
        int bitStream = UnsafeUtil.UNSAFE.getInt(inputBase, input);
        int tableLog = (bitStream & 15) + 5;
        int numberOfBits = tableLog + 1;
        int bitStream2 = bitStream >>> 4;
        int bitCount2 = 4;
        Util.verify(tableLog <= maxTableLog, input, "FSE table size exceeds maximum allowed size");
        int remaining2 = (1 << tableLog) + 1;
        int threshold = 1 << tableLog;
        for (int i2 = 1; remaining2 > i2 && symbolNumber <= i; i2 = 1) {
            if (previousIsZero2) {
                int n0 = symbolNumber;
                while ((bitStream2 & 65535) == 65535) {
                    n0 += 24;
                    if (input < inputLimit - 5) {
                        input += 2;
                        bitStream2 = UnsafeUtil.UNSAFE.getInt(inputBase, input) >>> bitCount2;
                    } else {
                        bitStream2 >>>= 16;
                        bitCount2 += 16;
                    }
                }
                while ((bitStream2 & 3) == 3) {
                    n0 += 3;
                    bitStream2 >>>= 2;
                    bitCount2 += 2;
                }
                int n02 = n0 + (bitStream2 & 3);
                bitCount2 += 2;
                Util.verify(n02 <= i, input, "Symbol larger than max value");
                while (symbolNumber < n02) {
                    fseTableReader.normalizedCounters[symbolNumber] = 0;
                    symbolNumber++;
                }
                z = false;
                if (input > inputLimit - 7 && (bitCount2 >>> 3) + input > inputLimit - 4) {
                    bitStream2 >>>= 2;
                }
                input += bitCount2 >>> 3;
                bitCount2 &= 7;
                bitStream2 = UnsafeUtil.UNSAFE.getInt(inputBase, input) >>> bitCount2;
            } else {
                z = false;
            }
            short max = (short) (((threshold * 2) - 1) - remaining2);
            if (((threshold - 1) & bitStream2) < max) {
                count = (short) ((threshold - 1) & bitStream2);
                bitCount = bitCount2 + (numberOfBits - 1);
            } else {
                count = (short) (((threshold * 2) - 1) & bitStream2);
                if (count >= threshold) {
                    count = (short) (count - max);
                }
                bitCount = bitCount2 + numberOfBits;
            }
            short count2 = (short) (count - 1);
            int remaining3 = remaining2 - Math.abs((int) count2);
            int symbolNumber2 = symbolNumber + 1;
            fseTableReader.normalizedCounters[symbolNumber] = count2;
            boolean previousIsZero3 = count2 == 0 ? true : z;
            while (remaining3 < threshold) {
                numberOfBits--;
                threshold >>>= 1;
            }
            if (input > inputLimit - 7) {
                previousIsZero = previousIsZero3;
                remaining = remaining3;
                if ((bitCount >> 3) + input > inputLimit - 4) {
                    bitCount2 = bitCount - ((int) (((inputLimit - 4) - input) * 8));
                    input = inputLimit - 4;
                    bitStream2 = UnsafeUtil.UNSAFE.getInt(inputBase, input) >>> (bitCount2 & 31);
                    i = maxSymbol;
                    symbolNumber = symbolNumber2;
                    remaining2 = remaining;
                    previousIsZero2 = previousIsZero;
                }
            } else {
                previousIsZero = previousIsZero3;
                remaining = remaining3;
            }
            input += bitCount >>> 3;
            bitCount2 = bitCount & 7;
            bitStream2 = UnsafeUtil.UNSAFE.getInt(inputBase, input) >>> (bitCount2 & 31);
            i = maxSymbol;
            symbolNumber = symbolNumber2;
            remaining2 = remaining;
            previousIsZero2 = previousIsZero;
        }
        Util.verify(remaining2 == 1 && bitCount2 <= 32, input, "Input is corrupted");
        int maxSymbol2 = symbolNumber - 1;
        Util.verify(maxSymbol2 <= 255, input, "Max symbol value too large (too many symbols for FSE)");
        long input2 = input + ((bitCount2 + 7) >> 3);
        int symbolCount = maxSymbol2 + 1;
        int tableSize = 1 << tableLog;
        int highThreshold = tableSize - 1;
        table.log2Size = tableLog;
        byte symbol = 0;
        while (symbol < symbolCount) {
            int remaining4 = remaining2;
            int threshold2 = threshold;
            if (fseTableReader.normalizedCounters[symbol] == -1) {
                table.symbol[highThreshold] = symbol;
                fseTableReader.nextSymbol[symbol] = 1;
                highThreshold--;
            } else {
                fseTableReader.nextSymbol[symbol] = fseTableReader.normalizedCounters[symbol];
            }
            symbol = (byte) (symbol + 1);
            remaining2 = remaining4;
            threshold = threshold2;
        }
        int position = FseCompressionTable.spreadSymbols(fseTableReader.normalizedCounters, maxSymbol2, tableSize, highThreshold, table.symbol);
        Util.verify(position == 0, input2, "Input is corrupted");
        int i3 = 0;
        while (i3 < tableSize) {
            byte symbol2 = table.symbol[i3];
            short[] sArr = fseTableReader.nextSymbol;
            short nextState = sArr[symbol2];
            sArr[symbol2] = (short) (nextState + 1);
            table.numberOfBits[i3] = (byte) (tableLog - Util.highestBit(nextState));
            table.newState[i3] = (short) ((nextState << table.numberOfBits[i3]) - tableSize);
            i3++;
            fseTableReader = this;
            maxSymbol2 = maxSymbol2;
        }
        return (int) (input2 - inputAddress);
    }

    public static void initializeRleTable(FiniteStateEntropy.Table table, byte value) {
        table.log2Size = 0;
        table.symbol[0] = value;
        table.newState[0] = 0;
        table.numberOfBits[0] = 0;
    }
}
