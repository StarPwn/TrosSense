package io.airlift.compress.snappy;

import java.io.IOException;
import java.io.OutputStream;

/* loaded from: classes.dex */
public final class SnappyFramedOutputStream extends OutputStream {
    public static final int DEFAULT_BLOCK_SIZE = 65536;
    public static final double DEFAULT_MIN_COMPRESSION_RATIO = 0.85d;
    public static final int MAX_BLOCK_SIZE = 65536;
    private final int blockSize;
    private final byte[] buffer;
    private boolean closed;
    private final SnappyCompressor compressor;
    private final double minCompressionRatio;
    private final OutputStream out;
    private final byte[] outputBuffer;
    private int position;
    private final boolean writeChecksums;

    public SnappyFramedOutputStream(OutputStream out) throws IOException {
        this(out, true);
    }

    public static SnappyFramedOutputStream newChecksumFreeBenchmarkOutputStream(OutputStream out) throws IOException {
        return new SnappyFramedOutputStream(out, false);
    }

    private SnappyFramedOutputStream(OutputStream out, boolean writeChecksums) throws IOException {
        this(out, writeChecksums, 65536, 0.85d);
    }

    public SnappyFramedOutputStream(OutputStream out, boolean writeChecksums, int blockSize, double minCompressionRatio) throws IOException {
        this.compressor = new SnappyCompressor();
        boolean z = false;
        this.out = (OutputStream) SnappyInternalUtils.checkNotNull(out, "out is null", new Object[0]);
        this.writeChecksums = writeChecksums;
        SnappyInternalUtils.checkArgument(minCompressionRatio > 0.0d && minCompressionRatio <= 1.0d, "minCompressionRatio %1s must be between (0,1.0].", Double.valueOf(minCompressionRatio));
        this.minCompressionRatio = minCompressionRatio;
        this.blockSize = blockSize;
        this.buffer = new byte[blockSize];
        this.outputBuffer = new byte[this.compressor.maxCompressedLength(blockSize)];
        out.write(SnappyFramed.HEADER_BYTES);
        if (blockSize > 0 && blockSize <= 65536) {
            z = true;
        }
        SnappyInternalUtils.checkArgument(z, "blockSize must be in (0, 65536]", Integer.valueOf(blockSize));
    }

    @Override // java.io.OutputStream
    public void write(int b) throws IOException {
        if (this.closed) {
            throw new IOException("Stream is closed");
        }
        if (this.position >= this.blockSize) {
            flushBuffer();
        }
        byte[] bArr = this.buffer;
        int i = this.position;
        this.position = i + 1;
        bArr[i] = (byte) b;
    }

    @Override // java.io.OutputStream
    public void write(byte[] input, int offset, int length) throws IOException {
        SnappyInternalUtils.checkNotNull(input, "input is null", new Object[0]);
        SnappyInternalUtils.checkPositionIndexes(offset, offset + length, input.length);
        if (this.closed) {
            throw new IOException("Stream is closed");
        }
        int free = this.blockSize - this.position;
        if (free >= length) {
            copyToBuffer(input, offset, length);
            return;
        }
        if (this.position > 0) {
            copyToBuffer(input, offset, free);
            flushBuffer();
            offset += free;
            length -= free;
        }
        while (length >= this.blockSize) {
            writeCompressed(input, offset, this.blockSize);
            offset += this.blockSize;
            length -= this.blockSize;
        }
        copyToBuffer(input, offset, length);
    }

    @Override // java.io.OutputStream, java.io.Flushable
    public void flush() throws IOException {
        if (this.closed) {
            throw new IOException("Stream is closed");
        }
        flushBuffer();
        this.out.flush();
    }

    @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.closed) {
            return;
        }
        try {
            flush();
            this.out.close();
        } finally {
            this.closed = true;
        }
    }

    private void copyToBuffer(byte[] input, int offset, int length) {
        System.arraycopy(input, offset, this.buffer, this.position, length);
        this.position += length;
    }

    private void flushBuffer() throws IOException {
        if (this.position > 0) {
            writeCompressed(this.buffer, 0, this.position);
            this.position = 0;
        }
    }

    private void writeCompressed(byte[] input, int offset, int length) throws IOException {
        int crc32c = this.writeChecksums ? Crc32C.maskedCrc32c(input, offset, length) : 0;
        int compressed = this.compressor.compress(input, offset, length, this.outputBuffer, 0, this.outputBuffer.length);
        if (compressed / length <= this.minCompressionRatio) {
            writeBlock(this.out, this.outputBuffer, 0, compressed, true, crc32c);
        } else {
            writeBlock(this.out, input, offset, length, false, crc32c);
        }
    }

    private void writeBlock(OutputStream outputStream, byte[] bArr, int i, int i2, boolean z, int i3) throws IOException {
        outputStream.write(!z ? 1 : 0);
        int i4 = i2 + 4;
        outputStream.write(i4);
        outputStream.write(i4 >>> 8);
        outputStream.write(i4 >>> 16);
        outputStream.write(i3);
        outputStream.write(i3 >>> 8);
        outputStream.write(i3 >>> 16);
        outputStream.write(i3 >>> 24);
        outputStream.write(bArr, i, i2);
    }
}
