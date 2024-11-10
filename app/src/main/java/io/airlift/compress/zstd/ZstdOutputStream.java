package io.airlift.compress.zstd;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Objects;
import sun.misc.Unsafe;

/* loaded from: classes.dex */
public class ZstdOutputStream extends OutputStream {
    private boolean closed;
    private final byte[] compressed;
    private final OutputStream outputStream;
    private XxHash64 partialHash;
    private int uncompressedOffset;
    private int uncompressedPosition;
    private byte[] uncompressed = new byte[0];
    private final CompressionContext context = new CompressionContext(CompressionParameters.compute(3, -1), Unsafe.ARRAY_BYTE_BASE_OFFSET, Integer.MAX_VALUE);
    private final int maxBufferSize = this.context.parameters.getWindowSize() * 4;

    public ZstdOutputStream(OutputStream outputStream) throws IOException {
        this.outputStream = (OutputStream) Objects.requireNonNull(outputStream, "outputStream is null");
        int bufferSize = this.context.parameters.getBlockSize() + 3;
        this.compressed = new byte[(bufferSize >>> 8) + bufferSize + 8];
    }

    @Override // java.io.OutputStream
    public void write(int b) throws IOException {
        if (this.closed) {
            throw new IOException("Stream is closed");
        }
        growBufferIfNecessary(1);
        byte[] bArr = this.uncompressed;
        int i = this.uncompressedPosition;
        this.uncompressedPosition = i + 1;
        bArr[i] = (byte) b;
        compressIfNecessary();
    }

    @Override // java.io.OutputStream
    public void write(byte[] buffer) throws IOException {
        write(buffer, 0, buffer.length);
    }

    @Override // java.io.OutputStream
    public void write(byte[] buffer, int offset, int length) throws IOException {
        if (this.closed) {
            throw new IOException("Stream is closed");
        }
        growBufferIfNecessary(length);
        while (length > 0) {
            int writeSize = Math.min(length, this.uncompressed.length - this.uncompressedPosition);
            System.arraycopy(buffer, offset, this.uncompressed, this.uncompressedPosition, writeSize);
            this.uncompressedPosition += writeSize;
            length -= writeSize;
            offset += writeSize;
            compressIfNecessary();
        }
    }

    private void growBufferIfNecessary(int length) {
        if (this.uncompressedPosition + length <= this.uncompressed.length || this.uncompressed.length >= this.maxBufferSize) {
            return;
        }
        int newSize = (this.uncompressed.length + length) * 2;
        this.uncompressed = Arrays.copyOf(this.uncompressed, Math.max(Math.min(newSize, this.maxBufferSize), this.context.parameters.getBlockSize()));
    }

    private void compressIfNecessary() throws IOException {
        if (this.uncompressed.length >= this.maxBufferSize && this.uncompressedPosition == this.uncompressed.length && this.uncompressed.length - this.context.parameters.getWindowSize() > this.context.parameters.getBlockSize()) {
            writeChunk(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void finishWithoutClosingSource() throws IOException {
        writeChunk(true);
        this.closed = true;
    }

    @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        writeChunk(true);
        this.closed = true;
        this.outputStream.close();
    }

    private void writeChunk(boolean lastChunk) throws IOException {
        int blockSize;
        if (lastChunk) {
            blockSize = this.uncompressedPosition - this.uncompressedOffset;
        } else {
            int blockSize2 = this.context.parameters.getBlockSize();
            int chunkSize = ((this.uncompressedPosition - this.uncompressedOffset) - this.context.parameters.getWindowSize()) - blockSize2;
            Util.checkState(chunkSize > blockSize2, "Must write at least one full block");
            blockSize = (chunkSize / blockSize2) * blockSize2;
        }
        if (this.partialHash == null) {
            this.partialHash = new XxHash64();
            int inputSize = lastChunk ? blockSize : -1;
            int outputAddress = Unsafe.ARRAY_BYTE_BASE_OFFSET;
            int outputAddress2 = outputAddress + ZstdFrameCompressor.writeMagic(this.compressed, outputAddress, outputAddress + 4);
            this.outputStream.write(this.compressed, 0, (outputAddress2 + ZstdFrameCompressor.writeFrameHeader(this.compressed, outputAddress2, outputAddress2 + 14, inputSize, this.context.parameters.getWindowSize())) - Unsafe.ARRAY_BYTE_BASE_OFFSET);
        }
        this.partialHash.update(this.uncompressed, this.uncompressedOffset, blockSize);
        do {
            int blockSize3 = Math.min(blockSize, this.context.parameters.getBlockSize());
            int compressedSize = ZstdFrameCompressor.writeCompressedBlock(this.uncompressed, Unsafe.ARRAY_BYTE_BASE_OFFSET + this.uncompressedOffset, blockSize3, this.compressed, Unsafe.ARRAY_BYTE_BASE_OFFSET, this.compressed.length, this.context, lastChunk && blockSize3 == blockSize);
            this.outputStream.write(this.compressed, 0, compressedSize);
            this.uncompressedOffset += blockSize3;
            blockSize -= blockSize3;
        } while (blockSize > 0);
        if (lastChunk) {
            int hash = (int) this.partialHash.hash();
            this.outputStream.write(hash);
            this.outputStream.write(hash >> 8);
            this.outputStream.write(hash >> 16);
            this.outputStream.write(hash >> 24);
            return;
        }
        int slideWindowSize = this.uncompressedOffset - this.context.parameters.getWindowSize();
        this.context.slideWindow(slideWindowSize);
        System.arraycopy(this.uncompressed, slideWindowSize, this.uncompressed, 0, this.context.parameters.getWindowSize() + (this.uncompressedPosition - this.uncompressedOffset));
        this.uncompressedOffset -= slideWindowSize;
        this.uncompressedPosition -= slideWindowSize;
    }
}
