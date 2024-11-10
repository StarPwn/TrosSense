package io.airlift.compress.zstd;

import io.netty.handler.codec.http2.Http2CodecUtil;
import java.util.Arrays;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class HuffmanCompressionTable {
    private int maxNumberOfBits;
    private int maxSymbol;
    private final byte[] numberOfBits;
    private final short[] values;

    public HuffmanCompressionTable(int capacity) {
        this.values = new short[capacity];
        this.numberOfBits = new byte[capacity];
    }

    public static int optimalNumberOfBits(int maxNumberOfBits, int inputSize, int maxSymbol) {
        if (inputSize <= 1) {
            throw new IllegalArgumentException();
        }
        int result = Math.min(maxNumberOfBits, Util.highestBit(inputSize - 1) - 1);
        int result2 = Util.minTableLog(inputSize, maxSymbol);
        return Math.min(Math.max(Math.max(result, result2), 5), 12);
    }

    public void initialize(int[] counts, int maxSymbol, int maxNumberOfBits, HuffmanCompressionTableWorkspace workspace) {
        Util.checkArgument(maxSymbol <= 255, "Max symbol value too large");
        workspace.reset();
        NodeTable nodeTable = workspace.nodeTable;
        nodeTable.reset();
        int lastNonZero = buildTree(counts, maxSymbol, nodeTable);
        int maxNumberOfBits2 = setMaxHeight(nodeTable, lastNonZero, maxNumberOfBits, workspace);
        Util.checkArgument(maxNumberOfBits2 <= 12, "Max number of bits larger than max table size");
        int symbolCount = maxSymbol + 1;
        for (int node = 0; node < symbolCount; node++) {
            int symbol = nodeTable.symbols[node];
            this.numberOfBits[symbol] = nodeTable.numberOfBits[node];
        }
        short[] entriesPerRank = workspace.entriesPerRank;
        short[] valuesPerRank = workspace.valuesPerRank;
        for (int n = 0; n <= lastNonZero; n++) {
            byte b = nodeTable.numberOfBits[n];
            entriesPerRank[b] = (short) (entriesPerRank[b] + 1);
        }
        short startingValue = 0;
        for (int rank = maxNumberOfBits2; rank > 0; rank--) {
            valuesPerRank[rank] = startingValue;
            startingValue = (short) (((short) (entriesPerRank[rank] + startingValue)) >>> 1);
        }
        for (int n2 = 0; n2 <= maxSymbol; n2++) {
            short[] sArr = this.values;
            byte b2 = this.numberOfBits[n2];
            short s = valuesPerRank[b2];
            valuesPerRank[b2] = (short) (s + 1);
            sArr[n2] = s;
        }
        this.maxSymbol = maxSymbol;
        this.maxNumberOfBits = maxNumberOfBits2;
    }

    private int buildTree(int[] counts, int maxSymbol, NodeTable nodeTable) {
        int currentLeaf;
        int currentNonLeaf;
        short current = 0;
        for (int symbol = 0; symbol <= maxSymbol; symbol++) {
            int count = counts[symbol];
            int position = current;
            while (position > 1 && count > nodeTable.count[position - 1]) {
                nodeTable.copyNode(position - 1, position);
                position--;
            }
            nodeTable.count[position] = count;
            nodeTable.symbols[position] = symbol;
            current = (short) (current + 1);
        }
        int lastNonZero = maxSymbol;
        while (nodeTable.count[lastNonZero] == 0) {
            lastNonZero--;
        }
        int currentLeaf2 = lastNonZero;
        int currentNonLeaf2 = 256;
        nodeTable.count[256] = nodeTable.count[currentLeaf2] + nodeTable.count[currentLeaf2 - 1];
        nodeTable.parents[currentLeaf2] = Http2CodecUtil.MAX_WEIGHT;
        nodeTable.parents[currentLeaf2 - 1] = Http2CodecUtil.MAX_WEIGHT;
        short current2 = (short) (256 + 1);
        int currentLeaf3 = currentLeaf2 - 2;
        int root = (lastNonZero + 256) - 1;
        for (int n = current2; n <= root; n++) {
            nodeTable.count[n] = 1073741824;
        }
        while (current2 <= root) {
            if (currentLeaf3 >= 0 && nodeTable.count[currentLeaf3] < nodeTable.count[currentNonLeaf2]) {
                int i = currentNonLeaf2;
                currentNonLeaf2 = currentLeaf3;
                currentLeaf3--;
                currentLeaf = i;
            } else {
                currentLeaf = currentNonLeaf2 + 1;
            }
            if (currentLeaf3 >= 0 && nodeTable.count[currentLeaf3] < nodeTable.count[currentLeaf]) {
                currentNonLeaf = currentLeaf3 - 1;
            } else {
                currentNonLeaf = currentLeaf3;
                currentLeaf3 = currentLeaf;
                currentLeaf++;
            }
            nodeTable.count[current2] = nodeTable.count[currentNonLeaf2] + nodeTable.count[currentLeaf3];
            nodeTable.parents[currentNonLeaf2] = current2;
            nodeTable.parents[currentLeaf3] = current2;
            current2 = (short) (current2 + 1);
            currentNonLeaf2 = currentLeaf;
            currentLeaf3 = currentNonLeaf;
        }
        nodeTable.numberOfBits[root] = 0;
        for (int n2 = root - 1; n2 >= 256; n2--) {
            short parent = nodeTable.parents[n2];
            nodeTable.numberOfBits[n2] = (byte) (nodeTable.numberOfBits[parent] + 1);
        }
        for (int n3 = 0; n3 <= lastNonZero; n3++) {
            short parent2 = nodeTable.parents[n3];
            nodeTable.numberOfBits[n3] = (byte) (nodeTable.numberOfBits[parent2] + 1);
        }
        return lastNonZero;
    }

    public void encodeSymbol(BitOutputStream output, int symbol) {
        output.addBitsFast(this.values[symbol], this.numberOfBits[symbol]);
    }

    public int write(Object outputBase, long outputAddress, int outputSize, HuffmanTableWriterWorkspace workspace) {
        byte[] weights = workspace.weights;
        int maxNumberOfBits = this.maxNumberOfBits;
        int maxSymbol = this.maxSymbol;
        for (int symbol = 0; symbol < maxSymbol; symbol++) {
            int bits = this.numberOfBits[symbol];
            if (bits == 0) {
                weights[symbol] = 0;
            } else {
                weights[symbol] = (byte) ((maxNumberOfBits + 1) - bits);
            }
        }
        int size = compressWeights(outputBase, outputAddress + 1, outputSize - 1, weights, maxSymbol, workspace);
        if (maxSymbol > 127 && size > 127) {
            throw new AssertionError();
        }
        if (size != 0 && size != 1 && size < maxSymbol / 2) {
            UnsafeUtil.UNSAFE.putByte(outputBase, outputAddress, (byte) size);
            return size + 1;
        }
        Util.checkArgument(((maxSymbol + 1) / 2) + 1 <= outputSize, "Output size too small");
        UnsafeUtil.UNSAFE.putByte(outputBase, outputAddress, (byte) (maxSymbol + 127));
        long output = outputAddress + 1;
        weights[maxSymbol] = 0;
        for (int i = 0; i < maxSymbol; i += 2) {
            UnsafeUtil.UNSAFE.putByte(outputBase, output, (byte) ((weights[i] << 4) + weights[i + 1]));
            output++;
        }
        return (int) (output - outputAddress);
    }

    public boolean isValid(int[] counts, int maxSymbol) {
        if (maxSymbol > this.maxSymbol) {
            return false;
        }
        for (int symbol = 0; symbol <= maxSymbol; symbol++) {
            if (counts[symbol] != 0 && this.numberOfBits[symbol] == 0) {
                return false;
            }
        }
        return true;
    }

    public int estimateCompressedSize(int[] counts, int maxSymbol) {
        int numberOfBits = 0;
        for (int symbol = 0; symbol <= Math.min(maxSymbol, this.maxSymbol); symbol++) {
            numberOfBits += this.numberOfBits[symbol] * counts[symbol];
        }
        int symbol2 = numberOfBits >>> 3;
        return symbol2;
    }

    private static int setMaxHeight(NodeTable nodeTable, int lastNonZero, int maxNumberOfBits, HuffmanCompressionTableWorkspace workspace) {
        byte largestBits = nodeTable.numberOfBits[lastNonZero];
        if (largestBits <= maxNumberOfBits) {
            return largestBits;
        }
        int totalCost = 0;
        int baseCost = 1 << (largestBits - maxNumberOfBits);
        int n = lastNonZero;
        while (nodeTable.numberOfBits[n] > maxNumberOfBits) {
            totalCost += baseCost - (1 << (largestBits - nodeTable.numberOfBits[n]));
            nodeTable.numberOfBits[n] = (byte) maxNumberOfBits;
            n--;
        }
        while (nodeTable.numberOfBits[n] == maxNumberOfBits) {
            n--;
        }
        int totalCost2 = totalCost >>> (largestBits - maxNumberOfBits);
        int[] rankLast = workspace.rankLast;
        Arrays.fill(rankLast, -252645136);
        int i = maxNumberOfBits;
        for (int pos = n; pos >= 0; pos--) {
            if (nodeTable.numberOfBits[pos] < i) {
                i = nodeTable.numberOfBits[pos];
                rankLast[maxNumberOfBits - i] = pos;
            }
        }
        while (totalCost2 > 0) {
            int numberOfBitsToDecrease = Util.highestBit(totalCost2) + 1;
            while (numberOfBitsToDecrease > 1) {
                int highPosition = rankLast[numberOfBitsToDecrease];
                int lowPosition = rankLast[numberOfBitsToDecrease - 1];
                if (highPosition != -252645136) {
                    if (lowPosition == -252645136) {
                        break;
                    }
                    int highTotal = nodeTable.count[highPosition];
                    int lowTotal = nodeTable.count[lowPosition] * 2;
                    if (highTotal <= lowTotal) {
                        break;
                    }
                }
                numberOfBitsToDecrease--;
            }
            while (numberOfBitsToDecrease <= 12 && rankLast[numberOfBitsToDecrease] == -252645136) {
                numberOfBitsToDecrease++;
            }
            totalCost2 -= 1 << (numberOfBitsToDecrease - 1);
            if (rankLast[numberOfBitsToDecrease - 1] == -252645136) {
                rankLast[numberOfBitsToDecrease - 1] = rankLast[numberOfBitsToDecrease];
            }
            byte[] bArr = nodeTable.numberOfBits;
            int i2 = rankLast[numberOfBitsToDecrease];
            bArr[i2] = (byte) (bArr[i2] + 1);
            if (rankLast[numberOfBitsToDecrease] == 0) {
                rankLast[numberOfBitsToDecrease] = -252645136;
            } else {
                rankLast[numberOfBitsToDecrease] = rankLast[numberOfBitsToDecrease] - 1;
                if (nodeTable.numberOfBits[rankLast[numberOfBitsToDecrease]] != maxNumberOfBits - numberOfBitsToDecrease) {
                    rankLast[numberOfBitsToDecrease] = -252645136;
                }
            }
        }
        while (totalCost2 < 0) {
            if (rankLast[1] == -252645136) {
                while (nodeTable.numberOfBits[n] == maxNumberOfBits) {
                    n--;
                }
                byte[] bArr2 = nodeTable.numberOfBits;
                int i3 = n + 1;
                bArr2[i3] = (byte) (bArr2[i3] - 1);
                rankLast[1] = n + 1;
                totalCost2++;
            } else {
                byte[] bArr3 = nodeTable.numberOfBits;
                int i4 = rankLast[1] + 1;
                bArr3[i4] = (byte) (bArr3[i4] - 1);
                rankLast[1] = rankLast[1] + 1;
                totalCost2++;
            }
        }
        return maxNumberOfBits;
    }

    private static int compressWeights(Object outputBase, long outputAddress, int outputSize, byte[] weights, int weightsLength, HuffmanTableWriterWorkspace workspace) {
        if (weightsLength <= 1) {
            return 0;
        }
        int[] counts = workspace.counts;
        Histogram.count(weights, weightsLength, counts);
        int maxSymbol = Histogram.findMaxSymbol(counts, 12);
        int maxCount = Histogram.findLargestCount(counts, maxSymbol);
        if (maxCount == weightsLength) {
            return 1;
        }
        if (maxCount == 1) {
            return 0;
        }
        short[] normalizedCounts = workspace.normalizedCounts;
        int tableLog = FiniteStateEntropy.optimalTableLog(6, weightsLength, maxSymbol);
        FiniteStateEntropy.normalizeCounts(normalizedCounts, tableLog, counts, weightsLength, maxSymbol);
        long outputLimit = outputAddress + outputSize;
        int headerSize = FiniteStateEntropy.writeNormalizedCounts(outputBase, outputAddress, outputSize, normalizedCounts, maxSymbol, tableLog);
        long output = headerSize + outputAddress;
        FseCompressionTable compressionTable = workspace.fseTable;
        compressionTable.initialize(normalizedCounts, maxSymbol, tableLog);
        int compressedSize = FiniteStateEntropy.compress(outputBase, output, (int) (outputLimit - output), weights, weightsLength, compressionTable);
        if (compressedSize == 0) {
            return 0;
        }
        return (int) ((output + compressedSize) - outputAddress);
    }
}
