package io.airlift.compress.zstd;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Objects;
import sun.misc.Unsafe;

/* loaded from: classes.dex */
public class ZstdInputStream extends InputStream {
    private static final int MIN_BUFFER_SIZE = 4096;
    private boolean closed;
    private final ZstdIncrementalFrameDecompressor decompressor = new ZstdIncrementalFrameDecompressor();
    private byte[] inputBuffer = new byte[this.decompressor.getInputRequired()];
    private int inputBufferLimit;
    private int inputBufferOffset;
    private final InputStream inputStream;
    private byte[] singleByteOutputBuffer;

    public ZstdInputStream(InputStream inputStream) {
        this.inputStream = (InputStream) Objects.requireNonNull(inputStream, "inputStream is null");
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        if (this.singleByteOutputBuffer == null) {
            this.singleByteOutputBuffer = new byte[1];
        }
        int readSize = read(this.singleByteOutputBuffer, 0, 1);
        Util.checkState(readSize != 0, "A zero read size should never be returned");
        if (readSize != 1) {
            return -1;
        }
        return this.singleByteOutputBuffer[0] & 255;
    }

    @Override // java.io.InputStream
    public int read(final byte[] outputBuffer, final int outputOffset, final int outputLength) throws IOException {
        if (this.closed) {
            throw new IOException("Stream is closed");
        }
        if (outputBuffer == null) {
            throw new NullPointerException();
        }
        Util.checkPositionIndexes(outputOffset, outputOffset + outputLength, outputBuffer.length);
        if (outputLength == 0) {
            return 0;
        }
        int outputLimit = outputOffset + outputLength;
        int outputUsed = 0;
        while (outputUsed < outputLength) {
            boolean enoughInput = fillInputBufferIfNecessary(this.decompressor.getInputRequired());
            if (!enoughInput) {
                if (this.decompressor.isAtStoppingPoint()) {
                    if (outputUsed > 0) {
                        return outputUsed;
                    }
                    return -1;
                }
                throw new IOException("Not enough input bytes");
            }
            this.decompressor.partialDecompress(this.inputBuffer, this.inputBufferOffset + Unsafe.ARRAY_BYTE_BASE_OFFSET, this.inputBufferLimit + Unsafe.ARRAY_BYTE_BASE_OFFSET, outputBuffer, outputOffset + outputUsed, outputLimit);
            this.inputBufferOffset += this.decompressor.getInputConsumed();
            outputUsed += this.decompressor.getOutputBufferUsed();
        }
        return outputUsed;
    }

    private boolean fillInputBufferIfNecessary(int requiredSize) throws IOException {
        int readSize;
        if (this.inputBufferLimit - this.inputBufferOffset >= requiredSize) {
            return true;
        }
        if (this.inputBufferOffset > 0) {
            int copySize = this.inputBufferLimit - this.inputBufferOffset;
            System.arraycopy(this.inputBuffer, this.inputBufferOffset, this.inputBuffer, 0, copySize);
            this.inputBufferOffset = 0;
            this.inputBufferLimit = copySize;
        }
        if (this.inputBuffer.length < requiredSize) {
            this.inputBuffer = Arrays.copyOf(this.inputBuffer, Math.max(requiredSize, 4096));
        }
        while (this.inputBufferLimit < this.inputBuffer.length && (readSize = this.inputStream.read(this.inputBuffer, this.inputBufferLimit, this.inputBuffer.length - this.inputBufferLimit)) >= 0) {
            this.inputBufferLimit += readSize;
        }
        return this.inputBufferLimit >= requiredSize;
    }

    @Override // java.io.InputStream
    public int available() throws IOException {
        if (this.closed) {
            return 0;
        }
        return this.decompressor.getRequestedOutputSize();
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (!this.closed) {
            this.closed = true;
            this.inputStream.close();
        }
    }
}
