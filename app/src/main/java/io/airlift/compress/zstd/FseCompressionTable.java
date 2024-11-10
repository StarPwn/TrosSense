package io.airlift.compress.zstd;

/* loaded from: classes.dex */
class FseCompressionTable {
    private final int[] deltaFindState;
    private final int[] deltaNumberOfBits;
    private int log2Size;
    private final short[] nextState;

    public FseCompressionTable(int maxTableLog, int maxSymbol) {
        this.nextState = new short[1 << maxTableLog];
        this.deltaNumberOfBits = new int[maxSymbol + 1];
        this.deltaFindState = new int[maxSymbol + 1];
    }

    public static FseCompressionTable newInstance(short[] normalizedCounts, int maxSymbol, int tableLog) {
        FseCompressionTable result = new FseCompressionTable(tableLog, maxSymbol);
        result.initialize(normalizedCounts, maxSymbol, tableLog);
        return result;
    }

    public void initializeRleTable(int symbol) {
        this.log2Size = 0;
        this.nextState[0] = 0;
        this.nextState[1] = 0;
        this.deltaFindState[symbol] = 0;
        this.deltaNumberOfBits[symbol] = 0;
    }

    public void initialize(short[] normalizedCounts, int maxSymbol, int tableLog) {
        int tableSize = 1 << tableLog;
        byte[] table = new byte[tableSize];
        int highThreshold = tableSize - 1;
        this.log2Size = tableLog;
        int[] cumulative = new int[257];
        cumulative[0] = 0;
        for (int i = 1; i <= maxSymbol + 1; i++) {
            if (normalizedCounts[i - 1] == -1) {
                cumulative[i] = cumulative[i - 1] + 1;
                table[highThreshold] = (byte) (i - 1);
                highThreshold--;
            } else {
                int highThreshold2 = i - 1;
                cumulative[i] = cumulative[highThreshold2] + normalizedCounts[i - 1];
            }
        }
        int i2 = maxSymbol + 1;
        cumulative[i2] = tableSize + 1;
        int position = spreadSymbols(normalizedCounts, maxSymbol, tableSize, highThreshold, table);
        if (position != 0) {
            throw new AssertionError("Spread symbols failed");
        }
        for (int i3 = 0; i3 < tableSize; i3++) {
            byte symbol = table[i3];
            short[] sArr = this.nextState;
            int i4 = cumulative[symbol];
            cumulative[symbol] = i4 + 1;
            sArr[i4] = (short) (tableSize + i3);
        }
        int total = 0;
        for (int symbol2 = 0; symbol2 <= maxSymbol; symbol2++) {
            switch (normalizedCounts[symbol2]) {
                case -1:
                case 1:
                    this.deltaNumberOfBits[symbol2] = (tableLog << 16) - tableSize;
                    this.deltaFindState[symbol2] = total - 1;
                    total++;
                    break;
                case 0:
                    this.deltaNumberOfBits[symbol2] = ((tableLog + 1) << 16) - tableSize;
                    break;
                default:
                    int maxBitsOut = tableLog - Util.highestBit(normalizedCounts[symbol2] - 1);
                    int minStatePlus = normalizedCounts[symbol2] << maxBitsOut;
                    this.deltaNumberOfBits[symbol2] = (maxBitsOut << 16) - minStatePlus;
                    this.deltaFindState[symbol2] = total - normalizedCounts[symbol2];
                    total += normalizedCounts[symbol2];
                    break;
            }
        }
    }

    public int begin(byte symbol) {
        int outputBits = (this.deltaNumberOfBits[symbol] + 32768) >>> 16;
        int base = ((outputBits << 16) - this.deltaNumberOfBits[symbol]) >>> outputBits;
        return this.nextState[this.deltaFindState[symbol] + base];
    }

    public int encode(BitOutputStream stream, int state, int symbol) {
        int outputBits = (this.deltaNumberOfBits[symbol] + state) >>> 16;
        stream.addBits(state, outputBits);
        return this.nextState[(state >>> outputBits) + this.deltaFindState[symbol]];
    }

    public void finish(BitOutputStream stream, int state) {
        stream.addBits(state, this.log2Size);
        stream.flush();
    }

    private static int calculateStep(int tableSize) {
        return (tableSize >>> 1) + (tableSize >>> 3) + 3;
    }

    public static int spreadSymbols(short[] normalizedCounters, int maxSymbolValue, int tableSize, int highThreshold, byte[] symbols) {
        int mask = tableSize - 1;
        int step = calculateStep(tableSize);
        int position = 0;
        byte symbol = 0;
        while (symbol <= maxSymbolValue) {
            for (int i = 0; i < normalizedCounters[symbol]; i++) {
                symbols[position] = symbol;
                do {
                    position = (position + step) & mask;
                } while (position > highThreshold);
            }
            int i2 = symbol + 1;
            symbol = (byte) i2;
        }
        return position;
    }
}
