package org.cloudburstmc.protocol.bedrock.netty.codec.compression;

import org.cloudburstmc.protocol.bedrock.data.CompressionAlgorithm;
import org.cloudburstmc.protocol.bedrock.netty.BedrockBatchWrapper;

/* loaded from: classes5.dex */
public interface CompressionStrategy {
    BatchCompression getCompression(CompressionAlgorithm compressionAlgorithm);

    BatchCompression getCompression(BedrockBatchWrapper bedrockBatchWrapper);

    BatchCompression getDefaultCompression();
}
