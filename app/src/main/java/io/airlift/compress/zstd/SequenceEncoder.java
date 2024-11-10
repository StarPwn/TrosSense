package io.airlift.compress.zstd;

import io.airlift.compress.zstd.CompressionParameters;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class SequenceEncoder {
    private static final int DEFAULT_LITERAL_LENGTH_NORMALIZED_COUNTS_LOG = 6;
    private static final int DEFAULT_MATCH_LENGTH_NORMALIZED_COUNTS_LOG = 6;
    private static final int DEFAULT_OFFSET_NORMALIZED_COUNTS_LOG = 5;
    private static final short[] DEFAULT_LITERAL_LENGTH_NORMALIZED_COUNTS = {4, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 2, 1, 1, 1, 1, 1, -1, -1, -1, -1};
    private static final short[] DEFAULT_MATCH_LENGTH_NORMALIZED_COUNTS = {1, 4, 3, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, -1, -1, -1, -1, -1, -1, -1};
    private static final short[] DEFAULT_OFFSET_NORMALIZED_COUNTS = {1, 1, 1, 1, 1, 1, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, -1, -1, -1, -1, -1};
    private static final FseCompressionTable DEFAULT_LITERAL_LENGTHS_TABLE = FseCompressionTable.newInstance(DEFAULT_LITERAL_LENGTH_NORMALIZED_COUNTS, 35, 6);
    private static final FseCompressionTable DEFAULT_MATCH_LENGTHS_TABLE = FseCompressionTable.newInstance(DEFAULT_MATCH_LENGTH_NORMALIZED_COUNTS, 52, 6);
    private static final FseCompressionTable DEFAULT_OFFSETS_TABLE = FseCompressionTable.newInstance(DEFAULT_OFFSET_NORMALIZED_COUNTS, 28, 5);

    private SequenceEncoder() {
    }

    public static int compressSequences(Object outputBase, final long outputAddress, int outputSize, SequenceStore sequences, CompressionParameters.Strategy strategy, SequenceEncodingContext workspace) {
        long output;
        String str;
        long headerAddress;
        int[] counts;
        int sequenceCount;
        SequenceEncodingContext sequenceEncodingContext;
        FseCompressionTable literalLengthTable;
        long output2;
        int[] counts2;
        int sequenceCount2;
        FseCompressionTable offsetCodeTable;
        long output3;
        FseCompressionTable matchLengthTable;
        long outputLimit = outputAddress + outputSize;
        Util.checkArgument(outputLimit - outputAddress > 4, "Output buffer too small");
        int sequenceCount3 = sequences.sequenceCount;
        if (sequenceCount3 < 127) {
            UnsafeUtil.UNSAFE.putByte(outputBase, outputAddress, (byte) sequenceCount3);
            output = outputAddress + 1;
        } else if (sequenceCount3 < 32512) {
            UnsafeUtil.UNSAFE.putByte(outputBase, outputAddress, (byte) ((sequenceCount3 >>> 8) | 128));
            UnsafeUtil.UNSAFE.putByte(outputBase, outputAddress + 1, (byte) sequenceCount3);
            output = outputAddress + 2;
        } else {
            UnsafeUtil.UNSAFE.putByte(outputBase, outputAddress, (byte) -1);
            long output4 = outputAddress + 1;
            UnsafeUtil.UNSAFE.putShort(outputBase, output4, (short) (sequenceCount3 - 32512));
            output = output4 + 2;
        }
        if (sequenceCount3 == 0) {
            return (int) (output - outputAddress);
        }
        long output5 = output + 1;
        long headerAddress2 = output;
        int[] counts3 = workspace.counts;
        Histogram.count(sequences.literalLengthCodes, sequenceCount3, workspace.counts);
        int maxSymbol = Histogram.findMaxSymbol(counts3, 35);
        int largestCount = Histogram.findLargestCount(counts3, maxSymbol);
        int literalsLengthEncodingType = selectEncodingType(largestCount, sequenceCount3, 6, true, strategy);
        switch (literalsLengthEncodingType) {
            case 0:
                str = "not yet implemented";
                headerAddress = headerAddress2;
                counts = counts3;
                sequenceCount = sequenceCount3;
                sequenceEncodingContext = workspace;
                FseCompressionTable literalLengthTable2 = DEFAULT_LITERAL_LENGTHS_TABLE;
                literalLengthTable = literalLengthTable2;
                output2 = output5;
                break;
            case 1:
                str = "not yet implemented";
                headerAddress = headerAddress2;
                counts = counts3;
                sequenceCount = sequenceCount3;
                sequenceEncodingContext = workspace;
                UnsafeUtil.UNSAFE.putByte(outputBase, output5, sequences.literalLengthCodes[0]);
                sequenceEncodingContext.literalLengthTable.initializeRleTable(maxSymbol);
                FseCompressionTable literalLengthTable3 = sequenceEncodingContext.literalLengthTable;
                literalLengthTable = literalLengthTable3;
                output2 = output5 + 1;
                break;
            case 2:
                str = "not yet implemented";
                headerAddress = headerAddress2;
                counts = counts3;
                sequenceCount = sequenceCount3;
                sequenceEncodingContext = workspace;
                FseCompressionTable literalLengthTable4 = sequenceEncodingContext.literalLengthTable;
                literalLengthTable = literalLengthTable4;
                output2 = output5 + buildCompressionTable(workspace.literalLengthTable, outputBase, output5, outputLimit, sequenceCount3, 9, sequences.literalLengthCodes, workspace.counts, maxSymbol, workspace.normalizedCounts);
                break;
            default:
                throw new UnsupportedOperationException("not yet implemented");
        }
        int sequenceCount4 = sequenceCount;
        Histogram.count(sequences.offsetCodes, sequenceCount4, sequenceEncodingContext.counts);
        int[] counts4 = counts;
        int maxSymbol2 = Histogram.findMaxSymbol(counts4, 31);
        int largestCount2 = Histogram.findLargestCount(counts4, maxSymbol2);
        boolean defaultAllowed = maxSymbol2 < 28;
        int offsetEncodingType = selectEncodingType(largestCount2, sequenceCount4, 5, defaultAllowed, strategy);
        switch (offsetEncodingType) {
            case 0:
                counts2 = counts4;
                sequenceCount2 = sequenceCount4;
                FseCompressionTable offsetCodeTable2 = DEFAULT_OFFSETS_TABLE;
                offsetCodeTable = offsetCodeTable2;
                break;
            case 1:
                counts2 = counts4;
                sequenceCount2 = sequenceCount4;
                long output6 = output2;
                UnsafeUtil.UNSAFE.putByte(outputBase, output6, sequences.offsetCodes[0]);
                output2 = output6 + 1;
                sequenceEncodingContext.offsetCodeTable.initializeRleTable(maxSymbol2);
                FseCompressionTable offsetCodeTable3 = sequenceEncodingContext.offsetCodeTable;
                offsetCodeTable = offsetCodeTable3;
                break;
            case 2:
                counts2 = counts4;
                sequenceCount2 = sequenceCount4;
                output2 += buildCompressionTable(sequenceEncodingContext.offsetCodeTable, outputBase, output2, output2 + outputSize, sequenceCount4, 8, sequences.offsetCodes, sequenceEncodingContext.counts, maxSymbol2, sequenceEncodingContext.normalizedCounts);
                FseCompressionTable offsetCodeTable4 = sequenceEncodingContext.offsetCodeTable;
                offsetCodeTable = offsetCodeTable4;
                break;
            default:
                throw new UnsupportedOperationException(str);
        }
        int sequenceCount5 = sequenceCount2;
        Histogram.count(sequences.matchLengthCodes, sequenceCount5, sequenceEncodingContext.counts);
        int[] counts5 = counts2;
        int maxSymbol3 = Histogram.findMaxSymbol(counts5, 52);
        int largestCount3 = Histogram.findLargestCount(counts5, maxSymbol3);
        int matchLengthEncodingType = selectEncodingType(largestCount3, sequenceCount5, 6, true, strategy);
        switch (matchLengthEncodingType) {
            case 0:
                FseCompressionTable matchLengthTable2 = DEFAULT_MATCH_LENGTHS_TABLE;
                output3 = output2;
                matchLengthTable = matchLengthTable2;
                break;
            case 1:
                UnsafeUtil.UNSAFE.putByte(outputBase, output2, sequences.matchLengthCodes[0]);
                sequenceEncodingContext.matchLengthTable.initializeRleTable(maxSymbol3);
                FseCompressionTable matchLengthTable3 = sequenceEncodingContext.matchLengthTable;
                output3 = output2 + 1;
                matchLengthTable = matchLengthTable3;
                break;
            case 2:
                FseCompressionTable matchLengthTable4 = sequenceEncodingContext.matchLengthTable;
                output3 = output2 + buildCompressionTable(sequenceEncodingContext.matchLengthTable, outputBase, output2, outputLimit, sequenceCount5, 9, sequences.matchLengthCodes, sequenceEncodingContext.counts, maxSymbol3, sequenceEncodingContext.normalizedCounts);
                matchLengthTable = matchLengthTable4;
                break;
            default:
                throw new UnsupportedOperationException(str);
        }
        UnsafeUtil.UNSAFE.putByte(outputBase, headerAddress, (byte) ((literalsLengthEncodingType << 6) | (offsetEncodingType << 4) | (matchLengthEncodingType << 2)));
        return (int) ((output3 + encodeSequences(outputBase, output3, outputLimit, matchLengthTable, offsetCodeTable, literalLengthTable, sequences)) - outputAddress);
    }

    private static int buildCompressionTable(FseCompressionTable table, Object outputBase, long output, long outputLimit, int sequenceCount, int maxTableLog, byte[] codes, int[] counts, int maxSymbol, short[] normalizedCounts) {
        int sequenceCount2 = sequenceCount;
        int tableLog = FiniteStateEntropy.optimalTableLog(maxTableLog, sequenceCount2, maxSymbol);
        if (counts[codes[sequenceCount2 - 1]] > 1) {
            byte b = codes[sequenceCount2 - 1];
            counts[b] = counts[b] - 1;
            sequenceCount2--;
        }
        FiniteStateEntropy.normalizeCounts(normalizedCounts, tableLog, counts, sequenceCount2, maxSymbol);
        table.initialize(normalizedCounts, maxSymbol, tableLog);
        return FiniteStateEntropy.writeNormalizedCounts(outputBase, output, (int) (outputLimit - output), normalizedCounts, maxSymbol, tableLog);
    }

    private static int encodeSequences(Object outputBase, long output, long outputLimit, FseCompressionTable matchLengthTable, FseCompressionTable offsetsTable, FseCompressionTable literalLengthTable, SequenceStore sequences) {
        byte[] matchLengthCodes = sequences.matchLengthCodes;
        byte[] offsetCodes = sequences.offsetCodes;
        byte[] literalLengthCodes = sequences.literalLengthCodes;
        BitOutputStream blockStream = new BitOutputStream(outputBase, output, (int) (outputLimit - output));
        int sequenceCount = sequences.sequenceCount;
        int matchLengthState = matchLengthTable.begin(matchLengthCodes[sequenceCount - 1]);
        int offsetState = offsetsTable.begin(offsetCodes[sequenceCount - 1]);
        int literalLengthState = literalLengthTable.begin(literalLengthCodes[sequenceCount - 1]);
        blockStream.addBits(sequences.literalLengths[sequenceCount - 1], Constants.LITERALS_LENGTH_BITS[literalLengthCodes[sequenceCount - 1]]);
        blockStream.addBits(sequences.matchLengths[sequenceCount - 1], Constants.MATCH_LENGTH_BITS[matchLengthCodes[sequenceCount - 1]]);
        blockStream.addBits(sequences.offsets[sequenceCount - 1], offsetCodes[sequenceCount - 1]);
        blockStream.flush();
        if (sequenceCount >= 2) {
            int n = sequenceCount - 2;
            while (n >= 0) {
                byte literalLengthCode = literalLengthCodes[n];
                byte offsetCode = offsetCodes[n];
                byte[] offsetCodes2 = offsetCodes;
                byte matchLengthCode = matchLengthCodes[n];
                byte[] matchLengthCodes2 = matchLengthCodes;
                int literalLengthBits = Constants.LITERALS_LENGTH_BITS[literalLengthCode];
                byte[] literalLengthCodes2 = literalLengthCodes;
                int matchLengthBits = Constants.MATCH_LENGTH_BITS[matchLengthCode];
                offsetState = offsetsTable.encode(blockStream, offsetState, offsetCode);
                matchLengthState = matchLengthTable.encode(blockStream, matchLengthState, matchLengthCode);
                literalLengthState = literalLengthTable.encode(blockStream, literalLengthState, literalLengthCode);
                int offsetBits = offsetCode + matchLengthBits;
                int i = offsetBits + literalLengthBits;
                int sequenceCount2 = sequenceCount;
                if (i >= 31) {
                    blockStream.flush();
                }
                blockStream.addBits(sequences.literalLengths[n], literalLengthBits);
                if (literalLengthBits + matchLengthBits > 24) {
                    blockStream.flush();
                }
                blockStream.addBits(sequences.matchLengths[n], matchLengthBits);
                if (offsetCode + matchLengthBits + literalLengthBits > 56) {
                    blockStream.flush();
                }
                blockStream.addBits(sequences.offsets[n], offsetCode);
                blockStream.flush();
                n--;
                offsetCodes = offsetCodes2;
                sequenceCount = sequenceCount2;
                matchLengthCodes = matchLengthCodes2;
                literalLengthCodes = literalLengthCodes2;
            }
        }
        matchLengthTable.finish(blockStream, matchLengthState);
        offsetsTable.finish(blockStream, offsetState);
        literalLengthTable.finish(blockStream, literalLengthState);
        int streamSize = blockStream.close();
        Util.checkArgument(streamSize > 0, "Output buffer too small");
        return streamSize;
    }

    private static int selectEncodingType(int largestCount, int sequenceCount, int defaultNormalizedCountsLog, boolean isDefaultTableAllowed, CompressionParameters.Strategy strategy) {
        if (largestCount == sequenceCount) {
            if (isDefaultTableAllowed && sequenceCount <= 2) {
                return 0;
            }
            return 1;
        }
        if (strategy.ordinal() < CompressionParameters.Strategy.LAZY.ordinal()) {
            if (isDefaultTableAllowed) {
                int factor = 10 - strategy.ordinal();
                long minNumberOfSequences = ((1 << defaultNormalizedCountsLog) * factor) >> 3;
                if (sequenceCount < minNumberOfSequences || largestCount < (sequenceCount >> (defaultNormalizedCountsLog - 1))) {
                    return 0;
                }
            }
            return 2;
        }
        throw new UnsupportedOperationException("not yet implemented");
    }
}
