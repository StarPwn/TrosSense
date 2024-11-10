package io.airlift.compress.zstd;

/* loaded from: classes.dex */
interface BlockCompressor {
    public static final BlockCompressor UNSUPPORTED = new BlockCompressor() { // from class: io.airlift.compress.zstd.BlockCompressor$$ExternalSyntheticLambda0
        @Override // io.airlift.compress.zstd.BlockCompressor
        public final int compressBlock(Object obj, long j, int i, SequenceStore sequenceStore, BlockCompressionState blockCompressionState, RepeatedOffsets repeatedOffsets, CompressionParameters compressionParameters) {
            return BlockCompressor.lambda$static$0(obj, j, i, sequenceStore, blockCompressionState, repeatedOffsets, compressionParameters);
        }
    };

    int compressBlock(Object inputBase, long inputAddress, int inputSize, SequenceStore output, BlockCompressionState state, RepeatedOffsets offsets, CompressionParameters parameters);

    static /* synthetic */ int lambda$static$0(Object inputBase, long inputAddress, int inputSize, SequenceStore sequenceStore, BlockCompressionState blockCompressionState, RepeatedOffsets offsets, CompressionParameters parameters) {
        throw new UnsupportedOperationException();
    }
}
