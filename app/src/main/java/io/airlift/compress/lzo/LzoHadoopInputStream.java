package io.airlift.compress.lzo;

import io.airlift.compress.hadoop.HadoopInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/* loaded from: classes.dex */
class LzoHadoopInputStream extends HadoopInputStream {
    private final InputStream in;
    private int uncompressedBlockLength;
    private final byte[] uncompressedChunk;
    private int uncompressedChunkLength;
    private int uncompressedChunkOffset;
    private final LzoDecompressor decompressor = new LzoDecompressor();
    private byte[] compressed = new byte[0];

    public LzoHadoopInputStream(InputStream in, int maxUncompressedLength) {
        this.in = (InputStream) Objects.requireNonNull(in, "in is null");
        this.uncompressedChunk = new byte[maxUncompressedLength + 8];
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        while (this.uncompressedChunkOffset >= this.uncompressedChunkLength) {
            int compressedChunkLength = bufferCompressedData();
            if (compressedChunkLength < 0) {
                return -1;
            }
            this.uncompressedChunkLength = this.decompressor.decompress(this.compressed, 0, compressedChunkLength, this.uncompressedChunk, 0, this.uncompressedChunk.length);
        }
        byte[] bArr = this.uncompressedChunk;
        int i = this.uncompressedChunkOffset;
        this.uncompressedChunkOffset = i + 1;
        return bArr[i] & 255;
    }

    @Override // io.airlift.compress.hadoop.HadoopInputStream, java.io.InputStream
    public int read(byte[] output, int offset, int length) throws IOException {
        while (this.uncompressedChunkOffset >= this.uncompressedChunkLength) {
            int compressedChunkLength = bufferCompressedData();
            if (compressedChunkLength < 0) {
                return -1;
            }
            if (length >= this.uncompressedBlockLength) {
                this.uncompressedChunkLength = this.decompressor.decompress(this.compressed, 0, compressedChunkLength, output, offset, length);
                this.uncompressedChunkOffset = this.uncompressedChunkLength;
                return this.uncompressedChunkLength;
            }
            this.uncompressedChunkLength = this.decompressor.decompress(this.compressed, 0, compressedChunkLength, this.uncompressedChunk, 0, this.uncompressedChunk.length);
        }
        int size = Math.min(length, this.uncompressedChunkLength - this.uncompressedChunkOffset);
        System.arraycopy(this.uncompressedChunk, this.uncompressedChunkOffset, output, offset, size);
        this.uncompressedChunkOffset += size;
        return size;
    }

    @Override // io.airlift.compress.hadoop.HadoopInputStream
    public void resetState() {
        this.uncompressedBlockLength = 0;
        this.uncompressedChunkOffset = 0;
        this.uncompressedChunkLength = 0;
    }

    @Override // io.airlift.compress.hadoop.HadoopInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.in.close();
    }

    private int bufferCompressedData() throws IOException {
        this.uncompressedBlockLength -= this.uncompressedChunkOffset;
        this.uncompressedChunkOffset = 0;
        this.uncompressedChunkLength = 0;
        while (this.uncompressedBlockLength == 0) {
            this.uncompressedBlockLength = readBigEndianInt();
            if (this.uncompressedBlockLength == -1) {
                this.uncompressedBlockLength = 0;
                return -1;
            }
        }
        int compressedChunkLength = readBigEndianInt();
        if (compressedChunkLength == -1) {
            return -1;
        }
        if (this.compressed.length < compressedChunkLength) {
            this.compressed = new byte[compressedChunkLength + 8];
        }
        readInput(compressedChunkLength, this.compressed);
        return compressedChunkLength;
    }

    private void readInput(int length, byte[] buffer) throws IOException {
        int offset = 0;
        while (offset < length) {
            int size = this.in.read(buffer, offset, length - offset);
            if (size == -1) {
                throw new EOFException("encountered EOF while reading block data");
            }
            offset += size;
        }
    }

    private int readBigEndianInt() throws IOException {
        int b1 = this.in.read();
        if (b1 < 0) {
            return -1;
        }
        int b2 = this.in.read();
        int b3 = this.in.read();
        int b4 = this.in.read();
        if ((b2 | b3 | b4) < 0) {
            throw new IOException("Stream is truncated");
        }
        return (b1 << 24) + (b2 << 16) + (b3 << 8) + b4;
    }
}
