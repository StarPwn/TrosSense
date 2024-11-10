package io.airlift.compress.snappy;

import io.airlift.compress.hadoop.HadoopInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes.dex */
class SnappyHadoopInputStream extends HadoopInputStream {
    private final InputStream in;
    private int uncompressedBlockLength;
    private int uncompressedChunkLength;
    private int uncompressedChunkOffset;
    private final SnappyDecompressor decompressor = new SnappyDecompressor();
    private byte[] uncompressedChunk = new byte[0];
    private byte[] compressed = new byte[0];

    public SnappyHadoopInputStream(InputStream in) {
        this.in = in;
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        if (this.uncompressedChunkOffset >= this.uncompressedChunkLength) {
            readNextChunk(this.uncompressedChunk, 0, this.uncompressedChunk.length);
            if (this.uncompressedChunkLength == 0) {
                return -1;
            }
        }
        byte[] bArr = this.uncompressedChunk;
        int i = this.uncompressedChunkOffset;
        this.uncompressedChunkOffset = i + 1;
        return bArr[i] & 255;
    }

    @Override // io.airlift.compress.hadoop.HadoopInputStream, java.io.InputStream
    public int read(byte[] output, int offset, int length) throws IOException {
        if (this.uncompressedChunkOffset >= this.uncompressedChunkLength) {
            boolean directDecompress = readNextChunk(output, offset, length);
            if (this.uncompressedChunkLength == 0) {
                return -1;
            }
            if (directDecompress) {
                this.uncompressedChunkOffset += this.uncompressedChunkLength;
                return this.uncompressedChunkLength;
            }
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

    private boolean readNextChunk(byte[] userBuffer, int userOffset, int userLength) throws IOException {
        this.uncompressedBlockLength -= this.uncompressedChunkOffset;
        this.uncompressedChunkOffset = 0;
        this.uncompressedChunkLength = 0;
        while (this.uncompressedBlockLength == 0) {
            this.uncompressedBlockLength = readBigEndianInt();
            if (this.uncompressedBlockLength == -1) {
                this.uncompressedBlockLength = 0;
                return false;
            }
        }
        int compressedChunkLength = readBigEndianInt();
        if (compressedChunkLength == -1) {
            return false;
        }
        if (this.compressed.length < compressedChunkLength) {
            this.compressed = new byte[compressedChunkLength + 8];
        }
        readInput(compressedChunkLength, this.compressed);
        this.uncompressedChunkLength = SnappyDecompressor.getUncompressedLength(this.compressed, 0);
        if (this.uncompressedChunkLength > this.uncompressedBlockLength) {
            throw new IOException("Chunk uncompressed size is greater than block size");
        }
        boolean directUncompress = true;
        if (this.uncompressedChunkLength > userLength) {
            if (this.uncompressedChunk.length < this.uncompressedChunkLength) {
                this.uncompressedChunk = new byte[this.uncompressedChunkLength + 8];
            }
            directUncompress = false;
            userBuffer = this.uncompressedChunk;
            userOffset = 0;
            userLength = this.uncompressedChunk.length;
        }
        int bytes = this.decompressor.decompress(this.compressed, 0, compressedChunkLength, userBuffer, userOffset, userLength);
        if (this.uncompressedChunkLength != bytes) {
            throw new IOException("Expected to read " + this.uncompressedChunkLength + " bytes, but data only contained " + bytes + " bytes");
        }
        return directUncompress;
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
