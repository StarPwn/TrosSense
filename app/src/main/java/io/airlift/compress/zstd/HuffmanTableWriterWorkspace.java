package io.airlift.compress.zstd;

/* loaded from: classes.dex */
class HuffmanTableWriterWorkspace {
    public final byte[] weights = new byte[255];
    public final int[] counts = new int[13];
    public final short[] normalizedCounts = new short[13];
    public final FseCompressionTable fseTable = new FseCompressionTable(6, 12);
}
