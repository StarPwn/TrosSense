package io.airlift.compress.snappy;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/* loaded from: classes.dex */
public final class SnappyFramedInputStream extends InputStream {
    private byte[] buffer;
    private boolean closed;
    private final SnappyDecompressor decompressor;
    private boolean eof;
    private final byte[] frameHeader;
    private final InputStream in;
    private byte[] input;
    private int position;
    private byte[] uncompressed;
    private int valid;
    private final boolean verifyChecksums;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public enum FrameAction {
        RAW,
        SKIP,
        UNCOMPRESS
    }

    public SnappyFramedInputStream(InputStream in) throws IOException {
        this(in, true);
    }

    public SnappyFramedInputStream(InputStream in, boolean verifyChecksums) throws IOException {
        this.decompressor = new SnappyDecompressor();
        this.input = new byte[0];
        this.uncompressed = new byte[0];
        this.in = in;
        this.verifyChecksums = verifyChecksums;
        allocateBuffersBasedOnSize(65541);
        this.frameHeader = new byte[4];
        byte[] actualHeader = new byte[SnappyFramed.HEADER_BYTES.length];
        int read = SnappyInternalUtils.readBytes(in, actualHeader, 0, actualHeader.length);
        if (read < SnappyFramed.HEADER_BYTES.length) {
            throw new EOFException("encountered EOF while reading stream header");
        }
        if (!Arrays.equals(SnappyFramed.HEADER_BYTES, actualHeader)) {
            throw new IOException("invalid stream header");
        }
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        if (this.closed || !ensureBuffer()) {
            return -1;
        }
        byte[] bArr = this.buffer;
        int i = this.position;
        this.position = i + 1;
        return bArr[i] & 255;
    }

    @Override // java.io.InputStream
    public int read(byte[] output, int offset, int length) throws IOException {
        SnappyInternalUtils.checkNotNull(output, "output is null", new Object[0]);
        SnappyInternalUtils.checkPositionIndexes(offset, offset + length, output.length);
        if (this.closed) {
            throw new IOException("Stream is closed");
        }
        if (length == 0) {
            return 0;
        }
        if (!ensureBuffer()) {
            return -1;
        }
        int size = Math.min(length, available());
        System.arraycopy(this.buffer, this.position, output, offset, size);
        this.position += size;
        return size;
    }

    @Override // java.io.InputStream
    public int available() throws IOException {
        if (this.closed) {
            return 0;
        }
        return this.valid - this.position;
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        try {
            this.in.close();
        } finally {
            if (!this.closed) {
                this.closed = true;
            }
        }
    }

    private boolean ensureBuffer() throws IOException {
        if (available() > 0) {
            return true;
        }
        if (this.eof) {
            return false;
        }
        if (!readBlockHeader()) {
            this.eof = true;
            return false;
        }
        FrameMetaData frameMetaData = getFrameMetaData(this.frameHeader);
        if (FrameAction.SKIP == frameMetaData.frameAction) {
            SnappyInternalUtils.skip(this.in, frameMetaData.length);
            return ensureBuffer();
        }
        if (frameMetaData.length > this.input.length) {
            allocateBuffersBasedOnSize(frameMetaData.length);
        }
        int actualRead = SnappyInternalUtils.readBytes(this.in, this.input, 0, frameMetaData.length);
        if (actualRead != frameMetaData.length) {
            throw new EOFException("unexpectd EOF when reading frame");
        }
        FrameData frameData = getFrameData(this.input);
        if (FrameAction.UNCOMPRESS == frameMetaData.frameAction) {
            int uncompressedLength = SnappyDecompressor.getUncompressedLength(this.input, frameData.offset);
            if (uncompressedLength > this.uncompressed.length) {
                this.uncompressed = new byte[uncompressedLength];
            }
            this.valid = this.decompressor.decompress(this.input, frameData.offset, actualRead - frameData.offset, this.uncompressed, 0, this.uncompressed.length);
            this.buffer = this.uncompressed;
            this.position = 0;
        } else {
            this.position = frameData.offset;
            this.buffer = this.input;
            this.valid = actualRead;
        }
        if (this.verifyChecksums) {
            int actualCrc32c = Crc32C.maskedCrc32c(this.buffer, this.position, this.valid - this.position);
            if (frameData.checkSum != actualCrc32c) {
                throw new IOException("Corrupt input: invalid checksum");
            }
        }
        return true;
    }

    private void allocateBuffersBasedOnSize(int size) {
        if (this.input.length < size) {
            this.input = new byte[size];
        }
        if (this.uncompressed.length < size) {
            this.uncompressed = new byte[size];
        }
    }

    private static FrameMetaData getFrameMetaData(byte[] frameHeader) throws IOException {
        FrameAction frameAction;
        int minLength;
        int length = (frameHeader[1] & 255) | ((frameHeader[2] & 255) << 8) | ((frameHeader[3] & 255) << 16);
        int flag = frameHeader[0] & 255;
        switch (flag) {
            case 0:
                frameAction = FrameAction.UNCOMPRESS;
                minLength = 5;
                break;
            case 1:
                frameAction = FrameAction.RAW;
                minLength = 5;
                break;
            case 255:
                if (length != 6) {
                    throw new IOException("stream identifier chunk with invalid length: " + length);
                }
                frameAction = FrameAction.SKIP;
                minLength = 6;
                break;
            default:
                if (flag <= 127) {
                    throw new IOException("unsupported unskippable chunk: " + Integer.toHexString(flag));
                }
                frameAction = FrameAction.SKIP;
                minLength = 0;
                break;
        }
        if (length < minLength) {
            throw new IOException("invalid length: " + length + " for chunk flag: " + Integer.toHexString(flag));
        }
        return new FrameMetaData(frameAction, length);
    }

    private static FrameData getFrameData(byte[] content) {
        int crc32c = ((content[3] & 255) << 24) | ((content[2] & 255) << 16) | ((content[1] & 255) << 8) | (content[0] & 255);
        return new FrameData(crc32c, 4);
    }

    private boolean readBlockHeader() throws IOException {
        int read = SnappyInternalUtils.readBytes(this.in, this.frameHeader, 0, this.frameHeader.length);
        if (read == -1) {
            return false;
        }
        if (read < this.frameHeader.length) {
            throw new EOFException("encountered EOF while reading block header");
        }
        return true;
    }

    /* loaded from: classes.dex */
    public static final class FrameMetaData {
        final FrameAction frameAction;
        final int length;

        public FrameMetaData(FrameAction frameAction, int length) {
            this.frameAction = frameAction;
            this.length = length;
        }
    }

    /* loaded from: classes.dex */
    public static final class FrameData {
        final int checkSum;
        final int offset;

        public FrameData(int checkSum, int offset) {
            this.checkSum = checkSum;
            this.offset = offset;
        }
    }
}
