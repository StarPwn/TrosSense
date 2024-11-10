package io.netty.handler.codec.compression;

import androidx.core.app.FrameMetricsAggregator;
import io.netty.buffer.ByteBuf;
import java.lang.reflect.Array;
import java.util.Arrays;

/* loaded from: classes4.dex */
final class Bzip2HuffmanStageEncoder {
    private static final int HUFFMAN_HIGH_SYMBOL_COST = 15;
    private final int[][] huffmanCodeLengths;
    private final int[][] huffmanMergedCodeSymbols;
    private final int mtfAlphabetSize;
    private final char[] mtfBlock;
    private final int mtfLength;
    private final int[] mtfSymbolFrequencies;
    private final byte[] selectors;
    private final Bzip2BitWriter writer;

    /* JADX INFO: Access modifiers changed from: package-private */
    public Bzip2HuffmanStageEncoder(Bzip2BitWriter writer, char[] mtfBlock, int mtfLength, int mtfAlphabetSize, int[] mtfSymbolFrequencies) {
        this.writer = writer;
        this.mtfBlock = mtfBlock;
        this.mtfLength = mtfLength;
        this.mtfAlphabetSize = mtfAlphabetSize;
        this.mtfSymbolFrequencies = mtfSymbolFrequencies;
        int totalTables = selectTableCount(mtfLength);
        this.huffmanCodeLengths = (int[][]) Array.newInstance((Class<?>) Integer.TYPE, totalTables, mtfAlphabetSize);
        this.huffmanMergedCodeSymbols = (int[][]) Array.newInstance((Class<?>) Integer.TYPE, totalTables, mtfAlphabetSize);
        this.selectors = new byte[((mtfLength + 50) - 1) / 50];
    }

    private static int selectTableCount(int mtfLength) {
        if (mtfLength >= 2400) {
            return 6;
        }
        if (mtfLength >= 1200) {
            return 5;
        }
        if (mtfLength >= 600) {
            return 4;
        }
        if (mtfLength >= 200) {
            return 3;
        }
        return 2;
    }

    private static void generateHuffmanCodeLengths(int alphabetSize, int[] symbolFrequencies, int[] codeLengths) {
        int[] mergedFrequenciesAndIndices = new int[alphabetSize];
        int[] sortedFrequencies = new int[alphabetSize];
        for (int i = 0; i < alphabetSize; i++) {
            mergedFrequenciesAndIndices[i] = (symbolFrequencies[i] << 9) | i;
        }
        Arrays.sort(mergedFrequenciesAndIndices);
        for (int i2 = 0; i2 < alphabetSize; i2++) {
            sortedFrequencies[i2] = mergedFrequenciesAndIndices[i2] >>> 9;
        }
        Bzip2HuffmanAllocator.allocateHuffmanCodeLengths(sortedFrequencies, 20);
        for (int i3 = 0; i3 < alphabetSize; i3++) {
            codeLengths[mergedFrequenciesAndIndices[i3] & FrameMetricsAggregator.EVERY_DURATION] = sortedFrequencies[i3];
        }
    }

    private void generateHuffmanOptimisationSeeds() {
        int[][] huffmanCodeLengths = this.huffmanCodeLengths;
        int[] mtfSymbolFrequencies = this.mtfSymbolFrequencies;
        int mtfAlphabetSize = this.mtfAlphabetSize;
        int totalTables = huffmanCodeLengths.length;
        int remainingLength = this.mtfLength;
        int lowCostEnd = -1;
        for (int i = 0; i < totalTables; i++) {
            int targetCumulativeFrequency = remainingLength / (totalTables - i);
            int lowCostStart = lowCostEnd + 1;
            int actualCumulativeFrequency = 0;
            while (actualCumulativeFrequency < targetCumulativeFrequency && lowCostEnd < mtfAlphabetSize - 1) {
                lowCostEnd++;
                actualCumulativeFrequency += mtfSymbolFrequencies[lowCostEnd];
            }
            if (lowCostEnd > lowCostStart && i != 0 && i != totalTables - 1 && ((totalTables - i) & 1) == 0) {
                actualCumulativeFrequency -= mtfSymbolFrequencies[lowCostEnd];
                lowCostEnd--;
            }
            int[] tableCodeLengths = huffmanCodeLengths[i];
            for (int j = 0; j < mtfAlphabetSize; j++) {
                if (j < lowCostStart || j > lowCostEnd) {
                    tableCodeLengths[j] = 15;
                }
            }
            remainingLength -= actualCumulativeFrequency;
        }
    }

    private void optimiseSelectorsAndHuffmanTables(boolean storeSelectors) {
        char[] mtfBlock = this.mtfBlock;
        byte[] selectors = this.selectors;
        int[][] huffmanCodeLengths = this.huffmanCodeLengths;
        int mtfLength = this.mtfLength;
        int mtfAlphabetSize = this.mtfAlphabetSize;
        int totalTables = huffmanCodeLengths.length;
        int[][] tableFrequencies = (int[][]) Array.newInstance((Class<?>) Integer.TYPE, totalTables, mtfAlphabetSize);
        int selectorIndex = 0;
        int groupStart = 0;
        while (groupStart < mtfLength) {
            int groupEnd = Math.min(groupStart + 50, mtfLength) - 1;
            int[] cost = new int[totalTables];
            for (int i = groupStart; i <= groupEnd; i++) {
                char c = mtfBlock[i];
                for (int j = 0; j < totalTables; j++) {
                    cost[j] = cost[j] + huffmanCodeLengths[j][c];
                }
            }
            byte bestTable = 0;
            int bestCost = cost[0];
            for (byte i2 = 1; i2 < totalTables; i2 = (byte) (i2 + 1)) {
                int tableCost = cost[i2];
                if (tableCost < bestCost) {
                    bestCost = tableCost;
                    bestTable = i2;
                }
            }
            int[] bestGroupFrequencies = tableFrequencies[bestTable];
            for (int i3 = groupStart; i3 <= groupEnd; i3++) {
                char c2 = mtfBlock[i3];
                bestGroupFrequencies[c2] = bestGroupFrequencies[c2] + 1;
            }
            if (storeSelectors) {
                selectors[selectorIndex] = bestTable;
                selectorIndex++;
            }
            groupStart = groupEnd + 1;
        }
        for (int i4 = 0; i4 < totalTables; i4++) {
            generateHuffmanCodeLengths(mtfAlphabetSize, tableFrequencies[i4], huffmanCodeLengths[i4]);
        }
    }

    private void assignHuffmanCodeSymbols() {
        int[][] huffmanMergedCodeSymbols = this.huffmanMergedCodeSymbols;
        int[][] huffmanCodeLengths = this.huffmanCodeLengths;
        int mtfAlphabetSize = this.mtfAlphabetSize;
        int totalTables = huffmanCodeLengths.length;
        for (int i = 0; i < totalTables; i++) {
            int[] tableLengths = huffmanCodeLengths[i];
            int minimumLength = 32;
            int maximumLength = 0;
            for (int j = 0; j < mtfAlphabetSize; j++) {
                int length = tableLengths[j];
                if (length > maximumLength) {
                    maximumLength = length;
                }
                if (length < minimumLength) {
                    minimumLength = length;
                }
            }
            int code = 0;
            for (int j2 = minimumLength; j2 <= maximumLength; j2++) {
                for (int k = 0; k < mtfAlphabetSize; k++) {
                    if ((huffmanCodeLengths[i][k] & 255) == j2) {
                        huffmanMergedCodeSymbols[i][k] = (j2 << 24) | code;
                        code++;
                    }
                }
                code <<= 1;
            }
        }
    }

    private void writeSelectorsAndHuffmanTables(ByteBuf out) {
        Bzip2BitWriter writer = this.writer;
        byte[] selectors = this.selectors;
        int totalSelectors = selectors.length;
        int[][] huffmanCodeLengths = this.huffmanCodeLengths;
        int totalTables = huffmanCodeLengths.length;
        int mtfAlphabetSize = this.mtfAlphabetSize;
        writer.writeBits(out, 3, totalTables);
        writer.writeBits(out, 15, totalSelectors);
        Bzip2MoveToFrontTable selectorMTF = new Bzip2MoveToFrontTable();
        char c = 0;
        for (byte selector : selectors) {
            writer.writeUnary(out, selectorMTF.valueToFront(selector));
        }
        int length = huffmanCodeLengths.length;
        int i = 0;
        while (i < length) {
            int[] tableLengths = huffmanCodeLengths[i];
            int currentLength = tableLengths[c];
            writer.writeBits(out, 5, currentLength);
            int j = 0;
            while (j < mtfAlphabetSize) {
                int codeLength = tableLengths[j];
                int totalSelectors2 = currentLength < codeLength ? 2 : 3;
                int delta = Math.abs(codeLength - currentLength);
                while (true) {
                    int delta2 = delta - 1;
                    if (delta > 0) {
                        int value = totalSelectors2;
                        writer.writeBits(out, 2, value);
                        selectors = selectors;
                        totalSelectors = totalSelectors;
                        delta = delta2;
                        totalSelectors2 = value;
                    }
                }
                writer.writeBoolean(out, false);
                currentLength = codeLength;
                j++;
                selectors = selectors;
            }
            i++;
            c = 0;
            selectors = selectors;
        }
    }

    private void writeBlockData(ByteBuf out) {
        Bzip2BitWriter writer = this.writer;
        int[][] huffmanMergedCodeSymbols = this.huffmanMergedCodeSymbols;
        byte[] selectors = this.selectors;
        int mtfLength = this.mtfLength;
        int selectorIndex = 0;
        int mergedCodeSymbol = 0;
        while (mergedCodeSymbol < mtfLength) {
            int groupEnd = Math.min(mergedCodeSymbol + 50, mtfLength) - 1;
            int selectorIndex2 = selectorIndex + 1;
            int[] tableMergedCodeSymbols = huffmanMergedCodeSymbols[selectors[selectorIndex]];
            while (mergedCodeSymbol <= groupEnd) {
                int mtfIndex = mergedCodeSymbol + 1;
                int mergedCodeSymbol2 = tableMergedCodeSymbols[this.mtfBlock[mergedCodeSymbol]];
                writer.writeBits(out, mergedCodeSymbol2 >>> 24, mergedCodeSymbol2);
                mergedCodeSymbol = mtfIndex;
            }
            selectorIndex = selectorIndex2;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void encode(ByteBuf out) {
        generateHuffmanOptimisationSeeds();
        int i = 3;
        while (i >= 0) {
            optimiseSelectorsAndHuffmanTables(i == 0);
            i--;
        }
        assignHuffmanCodeSymbols();
        writeSelectorsAndHuffmanTables(out);
        writeBlockData(out);
    }
}
