package io.airlift.compress.lzo;

import io.airlift.compress.hadoop.HadoopInputStream;
import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Objects;
import java.util.zip.Adler32;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

/* loaded from: classes.dex */
class LzopHadoopInputStream extends HadoopInputStream {
    private static final int LZOP_FILE_VERSION_MIN = 2368;
    private static final int LZOP_FLAG_ADLER32_COMPRESSED = 2;
    private static final int LZOP_FLAG_ADLER32_DECOMPRESSED = 1;
    private static final int LZOP_FLAG_CHARACTER_SET_MASK = 15728640;
    private static final int LZOP_FLAG_CRC32_COMPRESSED = 512;
    private static final int LZOP_FLAG_CRC32_DECOMPRESSED = 256;
    private static final int LZOP_FLAG_CRC32_HEADER = 4096;
    private static final int LZOP_FLAG_IO_MASK = 12;
    private static final int LZOP_FLAG_OPERATING_SYSTEM_MASK = -16777216;
    private static final int LZOP_FORMAT_VERSION_MAX = 4112;
    private static final int LZO_VERSION_MAX = 8352;
    private final boolean adler32Compressed;
    private final boolean adler32Decompressed;
    private final boolean crc32Compressed;
    private final boolean crc32Decompressed;
    private boolean finished;
    private final InputStream in;
    private final byte[] uncompressedChunk;
    private int uncompressedLength;
    private int uncompressedOffset;
    private final LzoDecompressor decompressor = new LzoDecompressor();
    private byte[] compressed = new byte[0];

    public LzopHadoopInputStream(InputStream in, int maxUncompressedLength) throws IOException {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        this.in = (InputStream) Objects.requireNonNull(in, "in is null");
        this.uncompressedChunk = new byte[maxUncompressedLength + 8];
        byte[] magic = new byte[LzoConstants.LZOP_MAGIC.length];
        readInput(magic, 0, magic.length);
        if (!Arrays.equals(magic, LzoConstants.LZOP_MAGIC)) {
            throw new IOException("Not an LZOP file");
        }
        byte[] header = new byte[25];
        readInput(header, 0, header.length);
        ByteArrayInputStream headerStream = new ByteArrayInputStream(header);
        int lzopFileVersion = readBigEndianShort(headerStream);
        if (lzopFileVersion < LZOP_FILE_VERSION_MIN) {
            throw new IOException(String.format("Unsupported LZOP file version 0x%08X", Integer.valueOf(lzopFileVersion)));
        }
        int lzoVersion = readBigEndianShort(headerStream);
        if (lzoVersion > LZO_VERSION_MAX) {
            throw new IOException(String.format("Unsupported LZO version 0x%08X", Integer.valueOf(lzoVersion)));
        }
        int lzopFormatVersion = readBigEndianShort(headerStream);
        if (lzopFormatVersion > LZOP_FORMAT_VERSION_MAX) {
            throw new IOException(String.format("Unsupported LZOP format version 0x%08X", Integer.valueOf(lzopFormatVersion)));
        }
        int variant = headerStream.read();
        if (variant != 1) {
            throw new IOException(String.format("Unsupported LZO variant %s", Integer.valueOf(variant)));
        }
        headerStream.read();
        int flags = readBigEndianInt(headerStream) & (-13) & 16777215 & (-15728641);
        if ((flags & 1) == 0) {
            z = false;
        } else {
            z = true;
        }
        this.adler32Decompressed = z;
        if ((flags & 2) == 0) {
            z2 = false;
        } else {
            z2 = true;
        }
        this.adler32Compressed = z2;
        if ((flags & 256) == 0) {
            z3 = false;
        } else {
            z3 = true;
        }
        this.crc32Decompressed = z3;
        if ((flags & 512) == 0) {
            z4 = false;
        } else {
            z4 = true;
        }
        this.crc32Compressed = z4;
        boolean crc32Header = (flags & 4096) != 0;
        int flags2 = flags & (-2) & (-3) & (-257) & (-513) & (-4097);
        if (flags2 != 0) {
            throw new IOException(String.format("Unsupported LZO flags 0x%08X", Integer.valueOf(flags2)));
        }
        readBigEndianInt(headerStream);
        readBigEndianInt(headerStream);
        readBigEndianInt(headerStream);
        int fileNameLength = headerStream.read();
        byte[] fileName = new byte[fileNameLength];
        readInput(fileName, 0, fileName.length);
        int headerChecksumValue = readBigEndianInt(in);
        Checksum headerChecksum = crc32Header ? new CRC32() : new Adler32();
        headerChecksum.update(header, 0, header.length);
        headerChecksum.update(fileName, 0, fileName.length);
        if (headerChecksumValue != ((int) headerChecksum.getValue())) {
            throw new IOException("Invalid header checksum");
        }
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        if (this.finished) {
            return -1;
        }
        while (this.uncompressedOffset >= this.uncompressedLength) {
            int compressedLength = bufferCompressedData();
            if (this.finished) {
                return -1;
            }
            decompress(compressedLength, this.uncompressedChunk, 0, this.uncompressedChunk.length);
        }
        byte[] bArr = this.uncompressedChunk;
        int i = this.uncompressedOffset;
        this.uncompressedOffset = i + 1;
        return bArr[i] & 255;
    }

    @Override // io.airlift.compress.hadoop.HadoopInputStream, java.io.InputStream
    public int read(byte[] output, int offset, int length) throws IOException {
        if (this.finished) {
            return -1;
        }
        while (this.uncompressedOffset >= this.uncompressedLength) {
            int compressedLength = bufferCompressedData();
            if (this.finished) {
                return -1;
            }
            if (length >= this.uncompressedLength) {
                decompress(compressedLength, output, offset, length);
                this.uncompressedOffset = this.uncompressedLength;
                return this.uncompressedLength;
            }
            decompress(compressedLength, this.uncompressedChunk, 0, this.uncompressedChunk.length);
        }
        int size = Math.min(length, this.uncompressedLength - this.uncompressedOffset);
        System.arraycopy(this.uncompressedChunk, this.uncompressedOffset, output, offset, size);
        this.uncompressedOffset += size;
        return size;
    }

    @Override // io.airlift.compress.hadoop.HadoopInputStream
    public void resetState() {
        this.uncompressedLength = 0;
        this.uncompressedOffset = 0;
        this.finished = false;
    }

    @Override // io.airlift.compress.hadoop.HadoopInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.in.close();
    }

    private int bufferCompressedData() throws IOException {
        this.uncompressedOffset = 0;
        this.uncompressedLength = readBigEndianInt(this.in);
        if (this.uncompressedLength == -1) {
            throw new EOFException("encountered EOF while reading block data");
        }
        if (this.uncompressedLength == 0) {
            this.finished = true;
            return -1;
        }
        int compressedLength = readBigEndianInt(this.in);
        if (compressedLength == -1) {
            throw new EOFException("encountered EOF while reading block data");
        }
        skipChecksums(compressedLength < this.uncompressedLength);
        return compressedLength;
    }

    private void skipChecksums(boolean compressed) throws IOException {
        if (this.adler32Decompressed) {
            readBigEndianInt(this.in);
        }
        if (this.crc32Decompressed) {
            readBigEndianInt(this.in);
        }
        if (compressed && this.adler32Compressed) {
            readBigEndianInt(this.in);
        }
        if (compressed && this.crc32Compressed) {
            readBigEndianInt(this.in);
        }
    }

    private void decompress(int compressedLength, byte[] output, int outputOffset, int outputLength) throws IOException {
        if (this.uncompressedLength == compressedLength) {
            readInput(output, outputOffset, compressedLength);
            return;
        }
        if (this.compressed.length < compressedLength) {
            this.compressed = new byte[compressedLength + 8];
        }
        readInput(this.compressed, 0, compressedLength);
        int actualUncompressedLength = this.decompressor.decompress(this.compressed, 0, compressedLength, output, outputOffset, outputLength);
        if (actualUncompressedLength != this.uncompressedLength) {
            throw new IOException("Decompressor did not decompress the entire block");
        }
    }

    private void readInput(byte[] buffer, int offset, int length) throws IOException {
        while (length > 0) {
            int size = this.in.read(buffer, offset, length);
            if (size == -1) {
                throw new EOFException("encountered EOF while reading block data");
            }
            offset += size;
            length -= size;
        }
    }

    private static int readBigEndianShort(InputStream in) throws IOException {
        int b1 = in.read();
        if (b1 < 0) {
            return -1;
        }
        int b2 = in.read();
        if (b2 < 0) {
            throw new IOException("Stream is truncated");
        }
        return (b1 << 8) + b2;
    }

    private static int readBigEndianInt(InputStream in) throws IOException {
        int b1 = in.read();
        if (b1 < 0) {
            return -1;
        }
        int b2 = in.read();
        int b3 = in.read();
        int b4 = in.read();
        if ((b2 | b3 | b4) < 0) {
            throw new IOException("Stream is truncated");
        }
        return (b1 << 24) + (b2 << 16) + (b3 << 8) + b4;
    }
}
