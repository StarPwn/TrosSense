package io.airlift.compress.snappy;

import io.airlift.compress.Compressor;
import java.nio.ByteBuffer;
import java.util.Objects;
import sun.misc.Unsafe;

/* loaded from: classes.dex */
public class SnappyCompressor implements Compressor {
    private final short[] table = new short[16384];

    @Override // io.airlift.compress.Compressor
    public int maxCompressedLength(int uncompressedSize) {
        return SnappyRawCompressor.maxCompressedLength(uncompressedSize);
    }

    @Override // io.airlift.compress.Compressor
    public int compress(byte[] input, int inputOffset, int inputLength, byte[] output, int outputOffset, int maxOutputLength) {
        verifyRange(input, inputOffset, inputLength);
        verifyRange(output, outputOffset, maxOutputLength);
        long inputAddress = Unsafe.ARRAY_BYTE_BASE_OFFSET + inputOffset;
        long inputLimit = inputAddress + inputLength;
        long outputAddress = Unsafe.ARRAY_BYTE_BASE_OFFSET + outputOffset;
        long outputLimit = outputAddress + maxOutputLength;
        return SnappyRawCompressor.compress(input, inputAddress, inputLimit, output, outputAddress, outputLimit, this.table);
    }

    @Override // io.airlift.compress.Compressor
    public void compress(ByteBuffer inputBuffer, ByteBuffer outputBuffer) {
        long inputAddress;
        long inputLimit;
        Object inputBase;
        long outputLimit;
        long outputAddress;
        Object outputBase;
        if (inputBuffer.isDirect()) {
            long address = UnsafeUtil.getAddress(inputBuffer);
            long inputAddress2 = inputBuffer.position() + address;
            long inputLimit2 = address + inputBuffer.limit();
            inputAddress = inputAddress2;
            inputLimit = inputLimit2;
            inputBase = null;
        } else if (inputBuffer.hasArray()) {
            Object inputBase2 = inputBuffer.array();
            long inputAddress3 = Unsafe.ARRAY_BYTE_BASE_OFFSET + inputBuffer.arrayOffset() + inputBuffer.position();
            inputAddress = inputAddress3;
            inputLimit = Unsafe.ARRAY_BYTE_BASE_OFFSET + inputBuffer.arrayOffset() + inputBuffer.limit();
            inputBase = inputBase2;
        } else {
            throw new IllegalArgumentException("Unsupported input ByteBuffer implementation " + inputBuffer.getClass().getName());
        }
        if (outputBuffer.isDirect()) {
            long address2 = UnsafeUtil.getAddress(outputBuffer);
            long outputAddress2 = outputBuffer.position() + address2;
            long outputLimit2 = address2 + outputBuffer.limit();
            outputLimit = outputLimit2;
            outputAddress = outputAddress2;
            outputBase = null;
        } else if (outputBuffer.hasArray()) {
            Object outputBase2 = outputBuffer.array();
            long outputAddress3 = Unsafe.ARRAY_BYTE_BASE_OFFSET + outputBuffer.arrayOffset() + outputBuffer.position();
            outputLimit = Unsafe.ARRAY_BYTE_BASE_OFFSET + outputBuffer.arrayOffset() + outputBuffer.limit();
            outputAddress = outputAddress3;
            outputBase = outputBase2;
        } else {
            throw new IllegalArgumentException("Unsupported output ByteBuffer implementation " + outputBuffer.getClass().getName());
        }
        synchronized (inputBuffer) {
            synchronized (outputBuffer) {
                int written = SnappyRawCompressor.compress(inputBase, inputAddress, inputLimit, outputBase, outputAddress, outputLimit, this.table);
                outputBuffer.position(outputBuffer.position() + written);
            }
        }
    }

    private static void verifyRange(byte[] data, int offset, int length) {
        Objects.requireNonNull(data, "data is null");
        if (offset < 0 || length < 0 || offset + length > data.length) {
            throw new IllegalArgumentException(String.format("Invalid offset or length (%s, %s) in array of length %s", Integer.valueOf(offset), Integer.valueOf(length), Integer.valueOf(data.length)));
        }
    }
}
