package io.airlift.compress.zstd;

/* loaded from: classes.dex */
class SequenceEncodingContext {
    private static final int MAX_SEQUENCES = Math.max(35, 52);
    public final FseCompressionTable literalLengthTable = new FseCompressionTable(9, 35);
    public final FseCompressionTable offsetCodeTable = new FseCompressionTable(8, 31);
    public final FseCompressionTable matchLengthTable = new FseCompressionTable(9, 52);
    public final int[] counts = new int[MAX_SEQUENCES + 1];
    public final short[] normalizedCounts = new short[MAX_SEQUENCES + 1];
}
