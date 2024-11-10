package io.airlift.compress.zstd;

import com.google.common.primitives.Longs;
import io.airlift.compress.zstd.BitInputStream;
import sun.misc.Unsafe;

/* loaded from: classes.dex */
class FiniteStateEntropy {
    public static final int MAX_SYMBOL = 255;
    public static final int MAX_TABLE_LOG = 12;
    public static final int MIN_TABLE_LOG = 5;
    private static final int[] REST_TO_BEAT = {0, 473195, 504333, 520860, 550000, 700000, 750000, 830000};
    private static final short UNASSIGNED = -2;

    private FiniteStateEntropy() {
    }

    public static int decompress(Table table, final Object inputBase, final long inputAddress, final long inputLimit, byte[] outputBuffer) {
        long outputAddress;
        long outputLimit;
        byte[] numbersOfBits;
        int[] newStates;
        byte[] symbols;
        long bits;
        long output;
        long outputAddress2 = Unsafe.ARRAY_BYTE_BASE_OFFSET;
        long output2 = outputBuffer.length + outputAddress2;
        BitInputStream.Initializer initializer = new BitInputStream.Initializer(inputBase, inputAddress, inputLimit);
        initializer.initialize();
        int bitsConsumed = initializer.getBitsConsumed();
        long currentAddress = initializer.getCurrentAddress();
        long bits2 = initializer.getBits();
        int state1 = (int) BitInputStream.peekBits(bitsConsumed, bits2, table.log2Size);
        long input = inputAddress;
        int state12 = state1;
        BitInputStream.Loader loader = new BitInputStream.Loader(inputBase, inputAddress, currentAddress, bits2, bitsConsumed + table.log2Size);
        loader.load();
        long bits3 = loader.getBits();
        int bitsConsumed2 = loader.getBitsConsumed();
        long currentAddress2 = loader.getCurrentAddress();
        int state2 = (int) BitInputStream.peekBits(bitsConsumed2, bits3, table.log2Size);
        int state22 = state2;
        BitInputStream.Loader loader2 = new BitInputStream.Loader(inputBase, input, currentAddress2, bits3, bitsConsumed2 + table.log2Size);
        loader2.load();
        long bits4 = loader2.getBits();
        int bitsConsumed3 = loader2.getBitsConsumed();
        long currentAddress3 = loader2.getCurrentAddress();
        byte[] symbols2 = table.symbol;
        byte[] numbersOfBits2 = table.numberOfBits;
        int[] newStates2 = table.newState;
        long currentAddress4 = currentAddress3;
        long bits5 = bits4;
        long output3 = outputAddress2;
        while (true) {
            if (output3 > output2 - 4) {
                outputAddress = outputAddress2;
                outputLimit = output2;
                numbersOfBits = numbersOfBits2;
                newStates = newStates2;
                symbols = symbols2;
                bits = bits5;
                break;
            }
            UnsafeUtil.UNSAFE.putByte(outputBuffer, output3, symbols2[state12]);
            int numberOfBits = numbersOfBits2[state12];
            outputAddress = outputAddress2;
            int state13 = (int) (newStates2[state12] + BitInputStream.peekBits(bitsConsumed3, bits5, numberOfBits));
            int bitsConsumed4 = bitsConsumed3 + numberOfBits;
            outputLimit = output2;
            UnsafeUtil.UNSAFE.putByte(outputBuffer, output3 + 1, symbols2[state22]);
            int numberOfBits2 = numbersOfBits2[state22];
            int state23 = (int) (newStates2[state22] + BitInputStream.peekBits(bitsConsumed4, bits5, numberOfBits2));
            int bitsConsumed5 = bitsConsumed4 + numberOfBits2;
            UnsafeUtil.UNSAFE.putByte(outputBuffer, output3 + 2, symbols2[state13]);
            int numberOfBits3 = numbersOfBits2[state13];
            int state14 = (int) (newStates2[state13] + BitInputStream.peekBits(bitsConsumed5, bits5, numberOfBits3));
            int bitsConsumed6 = bitsConsumed5 + numberOfBits3;
            UnsafeUtil.UNSAFE.putByte(outputBuffer, 3 + output3, symbols2[state23]);
            int numberOfBits4 = numbersOfBits2[state23];
            int state24 = (int) (newStates2[state23] + BitInputStream.peekBits(bitsConsumed6, bits5, numberOfBits4));
            long output4 = output3 + 4;
            numbersOfBits = numbersOfBits2;
            newStates = newStates2;
            symbols = symbols2;
            BitInputStream.Loader loader3 = new BitInputStream.Loader(inputBase, input, currentAddress4, bits5, bitsConsumed6 + numberOfBits4);
            boolean done = loader3.load();
            bitsConsumed3 = loader3.getBitsConsumed();
            bits5 = loader3.getBits();
            currentAddress4 = loader3.getCurrentAddress();
            if (done) {
                state12 = state14;
                state22 = state24;
                output3 = output4;
                bits = bits5;
                break;
            }
            state12 = state14;
            state22 = state24;
            output3 = output4;
            numbersOfBits2 = numbersOfBits;
            outputAddress2 = outputAddress;
            output2 = outputLimit;
            newStates2 = newStates;
            symbols2 = symbols;
        }
        while (true) {
            long input2 = input;
            Util.verify(output3 <= outputLimit - 2, input2, "Output buffer is too small");
            long output5 = output3 + 1;
            UnsafeUtil.UNSAFE.putByte(outputBuffer, output3, symbols[state12]);
            int numberOfBits5 = numbersOfBits[state12];
            int state15 = (int) (newStates[state12] + BitInputStream.peekBits(bitsConsumed3, bits, numberOfBits5));
            long output6 = currentAddress4;
            BitInputStream.Loader loader4 = new BitInputStream.Loader(inputBase, input2, output6, bits, bitsConsumed3 + numberOfBits5);
            loader4.load();
            int bitsConsumed7 = loader4.getBitsConsumed();
            long bits6 = loader4.getBits();
            long currentAddress5 = loader4.getCurrentAddress();
            if (loader4.isOverflow()) {
                output = output5 + 1;
                UnsafeUtil.UNSAFE.putByte(outputBuffer, output5, symbols[state22]);
                break;
            }
            Util.verify(output5 <= outputLimit - 2, input2, "Output buffer is too small");
            long output7 = output5 + 1;
            UnsafeUtil.UNSAFE.putByte(outputBuffer, output5, symbols[state22]);
            int numberOfBits1 = numbersOfBits[state22];
            int state25 = (int) (newStates[state22] + BitInputStream.peekBits(bitsConsumed7, bits6, numberOfBits1));
            BitInputStream.Loader loader5 = new BitInputStream.Loader(inputBase, input2, currentAddress5, bits6, bitsConsumed7 + numberOfBits1);
            loader5.load();
            bitsConsumed3 = loader5.getBitsConsumed();
            bits = loader5.getBits();
            currentAddress4 = loader5.getCurrentAddress();
            if (loader5.isOverflow()) {
                output = output7 + 1;
                UnsafeUtil.UNSAFE.putByte(outputBuffer, output7, symbols[state15]);
                break;
            }
            output3 = output7;
            state12 = state15;
            input = input2;
            state22 = state25;
        }
        return (int) (output - outputAddress);
    }

    public static int compress(Object outputBase, long outputAddress, int outputSize, byte[] input, int inputSize, FseCompressionTable table) {
        return compress(outputBase, outputAddress, outputSize, input, Unsafe.ARRAY_BYTE_BASE_OFFSET, inputSize, table);
    }

    public static int compress(Object outputBase, long outputAddress, int outputSize, Object inputBase, long inputAddress, int inputSize, FseCompressionTable table) {
        int state2;
        long input;
        int state1;
        Util.checkArgument(outputSize >= 8, "Output buffer too small");
        long inputLimit = inputSize + inputAddress;
        if (inputSize <= 2) {
            return 0;
        }
        BitOutputStream stream = new BitOutputStream(outputBase, outputAddress, outputSize);
        if ((inputSize & 1) == 0) {
            long input2 = inputLimit - 1;
            state2 = table.begin(UnsafeUtil.UNSAFE.getByte(inputBase, input2));
            input = input2 - 1;
            state1 = table.begin(UnsafeUtil.UNSAFE.getByte(inputBase, input));
        } else {
            long input3 = inputLimit - 1;
            int state12 = table.begin(UnsafeUtil.UNSAFE.getByte(inputBase, input3));
            long input4 = input3 - 1;
            state2 = table.begin(UnsafeUtil.UNSAFE.getByte(inputBase, input4));
            input = input4 - 1;
            state1 = table.encode(stream, state12, UnsafeUtil.UNSAFE.getByte(inputBase, input));
            stream.flush();
        }
        if (((inputSize - 2) & 2) != 0) {
            long input5 = input - 1;
            state2 = table.encode(stream, state2, UnsafeUtil.UNSAFE.getByte(inputBase, input5));
            input = input5 - 1;
            state1 = table.encode(stream, state1, UnsafeUtil.UNSAFE.getByte(inputBase, input));
            stream.flush();
        }
        while (input > inputAddress) {
            long input6 = input - 1;
            int state22 = table.encode(stream, state2, UnsafeUtil.UNSAFE.getByte(inputBase, input6));
            long input7 = input6 - 1;
            int state13 = table.encode(stream, state1, UnsafeUtil.UNSAFE.getByte(inputBase, input7));
            long input8 = input7 - 1;
            state2 = table.encode(stream, state22, UnsafeUtil.UNSAFE.getByte(inputBase, input8));
            input = input8 - 1;
            state1 = table.encode(stream, state13, UnsafeUtil.UNSAFE.getByte(inputBase, input));
            stream.flush();
        }
        table.finish(stream, state2);
        table.finish(stream, state1);
        return stream.close();
    }

    public static int optimalTableLog(int maxTableLog, int inputSize, int maxSymbol) {
        if (inputSize <= 1) {
            throw new IllegalArgumentException();
        }
        int result = Math.min(maxTableLog, Util.highestBit(inputSize - 1) - 2);
        return Math.min(Math.max(Math.max(result, Util.minTableLog(inputSize, maxSymbol)), 5), 12);
    }

    public static int normalizeCounts(short[] normalizedCounts, int tableLog, int[] counts, int total, int maxSymbol) {
        long step;
        long vstep;
        int lowThreshold;
        int i = total;
        short s = 0;
        Util.checkArgument(tableLog >= 5, "Unsupported FSE table size");
        Util.checkArgument(tableLog <= 12, "FSE table size too large");
        Util.checkArgument(tableLog >= Util.minTableLog(total, maxSymbol), "FSE table size too small");
        long scale = 62 - tableLog;
        long step2 = Longs.MAX_POWER_OF_TWO / i;
        long vstep2 = 1 << ((int) (scale - 20));
        int stillToDistribute = 1 << tableLog;
        int largest = 0;
        short largestProbability = 0;
        int lowThreshold2 = i >>> tableLog;
        int symbol = 0;
        while (symbol <= maxSymbol) {
            if (counts[symbol] == i) {
                throw new IllegalArgumentException();
            }
            if (counts[symbol] == 0) {
                normalizedCounts[symbol] = s;
                step = step2;
                vstep = vstep2;
                lowThreshold = lowThreshold2;
            } else if (counts[symbol] <= lowThreshold2) {
                normalizedCounts[symbol] = -1;
                stillToDistribute--;
                step = step2;
                vstep = vstep2;
                lowThreshold = lowThreshold2;
            } else {
                short probability = (short) ((counts[symbol] * step2) >>> ((int) scale));
                if (probability >= 8) {
                    step = step2;
                    vstep = vstep2;
                    lowThreshold = lowThreshold2;
                } else {
                    long restToBeat = REST_TO_BEAT[probability] * vstep2;
                    vstep = vstep2;
                    long j = counts[symbol] * step2;
                    step = step2;
                    long step3 = probability;
                    lowThreshold = lowThreshold2;
                    int lowThreshold3 = (int) scale;
                    long delta = j - (step3 << lowThreshold3);
                    if (delta > restToBeat) {
                        probability = (short) (probability + 1);
                    }
                }
                if (probability > largestProbability) {
                    largestProbability = probability;
                    largest = symbol;
                }
                normalizedCounts[symbol] = probability;
                stillToDistribute -= probability;
            }
            symbol++;
            i = total;
            vstep2 = vstep;
            step2 = step;
            lowThreshold2 = lowThreshold;
            s = 0;
        }
        if ((-stillToDistribute) >= (normalizedCounts[largest] >>> 1)) {
            normalizeCounts2(normalizedCounts, tableLog, counts, total, maxSymbol);
        } else {
            normalizedCounts[largest] = (short) (normalizedCounts[largest] + ((short) stillToDistribute));
        }
        return tableLog;
    }

    private static int normalizeCounts2(short[] normalizedCounts, int tableLog, int[] counts, int total, int maxSymbol) {
        int lowThreshold;
        int lowOne;
        int distributed;
        int i = maxSymbol;
        int lowThreshold2 = total >>> tableLog;
        int lowOne2 = (total * 3) >>> (tableLog + 1);
        int distributed2 = 0;
        int distributed3 = total;
        for (int i2 = 0; i2 <= i; i2++) {
            if (counts[i2] == 0) {
                normalizedCounts[i2] = 0;
            } else if (counts[i2] <= lowThreshold2) {
                normalizedCounts[i2] = -1;
                distributed2++;
                distributed3 -= counts[i2];
            } else if (counts[i2] <= lowOne2) {
                normalizedCounts[i2] = 1;
                distributed2++;
                distributed3 -= counts[i2];
            } else {
                normalizedCounts[i2] = UNASSIGNED;
            }
        }
        int i3 = 1 << tableLog;
        int toDistribute = i3 - distributed2;
        if (distributed3 / toDistribute > lowOne2) {
            lowOne2 = (distributed3 * 3) / (toDistribute * 2);
            for (int i4 = 0; i4 <= i; i4++) {
                if (normalizedCounts[i4] == -2 && counts[i4] <= lowOne2) {
                    normalizedCounts[i4] = 1;
                    distributed2++;
                    distributed3 -= counts[i4];
                }
            }
            toDistribute = i3 - distributed2;
        }
        if (distributed2 == i + 1) {
            int maxValue = 0;
            int maxCount = 0;
            for (int i5 = 0; i5 <= i; i5++) {
                if (counts[i5] > maxCount) {
                    maxValue = i5;
                    maxCount = counts[i5];
                }
            }
            normalizedCounts[maxValue] = (short) (normalizedCounts[maxValue] + ((short) toDistribute));
            return 0;
        }
        if (distributed3 == 0) {
            int i6 = 0;
            while (toDistribute > 0) {
                if (normalizedCounts[i6] > 0) {
                    toDistribute--;
                    normalizedCounts[i6] = (short) (normalizedCounts[i6] + 1);
                }
                i6 = (i6 + 1) % (i + 1);
            }
            return 0;
        }
        long vStepLog = 62 - tableLog;
        long mid = (1 << ((int) (vStepLog - 1))) - 1;
        long rStep = (((1 << ((int) vStepLog)) * toDistribute) + mid) / distributed3;
        long tmpTotal = mid;
        int i7 = 0;
        while (i7 <= i) {
            int total2 = distributed3;
            if (normalizedCounts[i7] != -2) {
                lowThreshold = lowThreshold2;
                lowOne = lowOne2;
                distributed = distributed2;
            } else {
                lowThreshold = lowThreshold2;
                long end = (counts[i7] * rStep) + tmpTotal;
                lowOne = lowOne2;
                distributed = distributed2;
                int sStart = (int) (tmpTotal >>> ((int) vStepLog));
                int sEnd = (int) (end >>> ((int) vStepLog));
                int weight = sEnd - sStart;
                if (weight < 1) {
                    throw new AssertionError();
                }
                normalizedCounts[i7] = (short) weight;
                tmpTotal = end;
            }
            i7++;
            i = maxSymbol;
            lowThreshold2 = lowThreshold;
            distributed3 = total2;
            lowOne2 = lowOne;
            distributed2 = distributed;
        }
        return 0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static int writeNormalizedCounts(Object outputBase, long outputAddress, int outputSize, short[] normalizedCounts, int maxSymbol, int tableLog) {
        int symbol;
        int symbol2;
        int count = 1;
        Util.checkArgument(tableLog <= 12, "FSE table too large");
        Util.checkArgument(tableLog >= 5, "FSE table too small");
        long output = outputAddress;
        long outputLimit = outputAddress + outputSize;
        int tableSize = 1 << tableLog;
        int bitStream = tableLog - 5;
        int bitCount = 0 + 4;
        int remaining = tableSize + 1;
        int threshold = tableSize;
        int tableBitCount = tableLog + 1;
        int symbol3 = 0;
        boolean previousIs0 = false;
        while (remaining > count) {
            if (previousIs0) {
                int start = symbol3;
                while (normalizedCounts[symbol3] == 0) {
                    symbol3++;
                }
                while (symbol3 >= start + 24) {
                    start += 24;
                    int bitStream2 = (65535 << bitCount) | bitStream;
                    Util.checkArgument(output + 2 <= outputLimit, "Output buffer too small");
                    UnsafeUtil.UNSAFE.putShort(outputBase, output, (short) bitStream2);
                    output += 2;
                    bitStream = bitStream2 >>> 16;
                }
                while (symbol3 >= start + 3) {
                    start += 3;
                    bitStream |= 3 << bitCount;
                    bitCount += 2;
                }
                bitStream |= (symbol3 - start) << bitCount;
                bitCount += 2;
                if (bitCount > 16) {
                    Util.checkArgument(output + 2 <= outputLimit, "Output buffer too small");
                    UnsafeUtil.UNSAFE.putShort(outputBase, output, (short) bitStream);
                    output += 2;
                    bitStream >>>= 16;
                    bitCount -= 16;
                }
            }
            int symbol4 = symbol3 + 1;
            short s = normalizedCounts[symbol3];
            int max = ((threshold * 2) - 1) - remaining;
            if (s < 0) {
                symbol = symbol4;
                symbol2 = -s;
            } else {
                symbol = symbol4;
                symbol2 = s;
            }
            remaining -= symbol2;
            int count2 = s + 1;
            if (count2 >= threshold) {
                count2 += max;
            }
            int bitStream3 = (count2 << bitCount) | bitStream;
            bitCount = (bitCount + tableBitCount) - (count2 < max ? 1 : 0);
            previousIs0 = count2 == 1;
            if (remaining < 1) {
                throw new AssertionError();
            }
            while (remaining < threshold) {
                tableBitCount--;
                threshold >>= 1;
            }
            if (bitCount > 16) {
                Util.checkArgument(output + 2 <= outputLimit, "Output buffer too small");
                UnsafeUtil.UNSAFE.putShort(outputBase, output, (short) bitStream3);
                output += 2;
                bitStream3 >>>= 16;
                bitCount -= 16;
            }
            bitStream = bitStream3;
            symbol3 = symbol;
            count = 1;
        }
        int bitStream4 = count;
        Util.checkArgument(output + 2 <= outputLimit ? bitStream4 : 0, "Output buffer too small");
        UnsafeUtil.UNSAFE.putShort(outputBase, output, (short) bitStream);
        long output2 = output + ((bitCount + 7) / 8);
        Util.checkArgument(symbol3 <= maxSymbol + 1 ? bitStream4 : 0, "Error");
        return (int) (output2 - outputAddress);
    }

    /* loaded from: classes.dex */
    public static final class Table {
        int log2Size;
        final int[] newState;
        final byte[] numberOfBits;
        final byte[] symbol;

        public Table(int log2Capacity) {
            int capacity = 1 << log2Capacity;
            this.newState = new int[capacity];
            this.symbol = new byte[capacity];
            this.numberOfBits = new byte[capacity];
        }

        public Table(int log2Size, int[] newState, byte[] symbol, byte[] numberOfBits) {
            int size = 1 << log2Size;
            if (newState.length != size || symbol.length != size || numberOfBits.length != size) {
                throw new IllegalArgumentException("Expected arrays to match provided size");
            }
            this.log2Size = log2Size;
            this.newState = newState;
            this.symbol = symbol;
            this.numberOfBits = numberOfBits;
        }
    }
}
