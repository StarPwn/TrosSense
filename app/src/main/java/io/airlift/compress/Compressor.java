package io.airlift.compress;

import java.nio.ByteBuffer;

/* loaded from: classes.dex */
public interface Compressor {
    int compress(byte[] input, int inputOffset, int inputLength, byte[] output, int outputOffset, int maxOutputLength);

    void compress(ByteBuffer input, ByteBuffer output);

    int maxCompressedLength(int uncompressedSize);
}
