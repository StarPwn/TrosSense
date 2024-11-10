package org.cloudburstmc.protocol.bedrock.netty.codec.compression;

import org.cloudburstmc.protocol.bedrock.data.CompressionAlgorithm;
import org.cloudburstmc.protocol.bedrock.data.PacketCompressionAlgorithm;
import org.cloudburstmc.protocol.bedrock.netty.BedrockBatchWrapper;
import org.cloudburstmc.protocol.common.util.Zlib;

/* loaded from: classes5.dex */
public class SimpleCompressionStrategy implements CompressionStrategy {
    private final BatchCompression compression;
    private final BatchCompression none = new NoopCompression();
    private final BatchCompression snappy;
    private final BatchCompression zlib;

    public SimpleCompressionStrategy(BatchCompression compression) {
        this.compression = compression;
        if (compression.getAlgorithm() == PacketCompressionAlgorithm.ZLIB) {
            this.zlib = compression;
            this.snappy = new SnappyCompression();
        } else if (compression.getAlgorithm() == PacketCompressionAlgorithm.SNAPPY) {
            this.zlib = new ZlibCompression(Zlib.RAW);
            this.snappy = compression;
        } else {
            this.zlib = new ZlibCompression(Zlib.RAW);
            this.snappy = new SnappyCompression();
        }
    }

    @Override // org.cloudburstmc.protocol.bedrock.netty.codec.compression.CompressionStrategy
    public BatchCompression getCompression(BedrockBatchWrapper wrapper) {
        return this.compression;
    }

    @Override // org.cloudburstmc.protocol.bedrock.netty.codec.compression.CompressionStrategy
    public BatchCompression getCompression(CompressionAlgorithm algorithm) {
        if (algorithm == PacketCompressionAlgorithm.ZLIB) {
            return this.zlib;
        }
        if (algorithm == PacketCompressionAlgorithm.SNAPPY) {
            return this.snappy;
        }
        if (algorithm == PacketCompressionAlgorithm.NONE) {
            return this.none;
        }
        return this.compression;
    }

    @Override // org.cloudburstmc.protocol.bedrock.netty.codec.compression.CompressionStrategy
    public BatchCompression getDefaultCompression() {
        return this.compression;
    }
}
