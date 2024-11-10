package io.netty.buffer;

import io.netty.util.internal.ObjectUtil;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import kotlin.UShort;

/* loaded from: classes4.dex */
public class ByteBufInputStream extends InputStream implements DataInput {
    private final ByteBuf buffer;
    private boolean closed;
    private final int endIndex;
    private StringBuilder lineBuf;
    private final boolean releaseOnClose;
    private final int startIndex;

    public ByteBufInputStream(ByteBuf buffer) {
        this(buffer, buffer.readableBytes());
    }

    public ByteBufInputStream(ByteBuf buffer, int length) {
        this(buffer, length, false);
    }

    public ByteBufInputStream(ByteBuf buffer, boolean releaseOnClose) {
        this(buffer, buffer.readableBytes(), releaseOnClose);
    }

    public ByteBufInputStream(ByteBuf buffer, int length, boolean releaseOnClose) {
        ObjectUtil.checkNotNull(buffer, "buffer");
        if (length < 0) {
            if (releaseOnClose) {
                buffer.release();
            }
            ObjectUtil.checkPositiveOrZero(length, "length");
        }
        if (length > buffer.readableBytes()) {
            if (releaseOnClose) {
                buffer.release();
            }
            throw new IndexOutOfBoundsException("Too many bytes to be read - Needs " + length + ", maximum is " + buffer.readableBytes());
        }
        this.releaseOnClose = releaseOnClose;
        this.buffer = buffer;
        this.startIndex = buffer.readerIndex();
        this.endIndex = this.startIndex + length;
        buffer.markReaderIndex();
    }

    public int readBytes() {
        return this.buffer.readerIndex() - this.startIndex;
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        try {
            super.close();
        } finally {
            if (this.releaseOnClose && !this.closed) {
                this.closed = true;
                this.buffer.release();
            }
        }
    }

    @Override // java.io.InputStream
    public int available() throws IOException {
        return this.endIndex - this.buffer.readerIndex();
    }

    @Override // java.io.InputStream
    public void mark(int readlimit) {
        this.buffer.markReaderIndex();
    }

    @Override // java.io.InputStream
    public boolean markSupported() {
        return true;
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        int available = available();
        if (available == 0) {
            return -1;
        }
        return this.buffer.readByte() & 255;
    }

    @Override // java.io.InputStream
    public int read(byte[] b, int off, int len) throws IOException {
        int available = available();
        if (available == 0) {
            return -1;
        }
        int len2 = Math.min(available, len);
        this.buffer.readBytes(b, off, len2);
        return len2;
    }

    @Override // java.io.InputStream
    public void reset() throws IOException {
        this.buffer.resetReaderIndex();
    }

    @Override // java.io.InputStream
    public long skip(long n) throws IOException {
        if (n > 2147483647L) {
            return skipBytes(Integer.MAX_VALUE);
        }
        return skipBytes((int) n);
    }

    @Override // java.io.DataInput
    public boolean readBoolean() throws IOException {
        checkAvailable(1);
        return read() != 0;
    }

    @Override // java.io.DataInput
    public byte readByte() throws IOException {
        int available = available();
        if (available == 0) {
            throw new EOFException();
        }
        return this.buffer.readByte();
    }

    @Override // java.io.DataInput
    public char readChar() throws IOException {
        return (char) readShort();
    }

    @Override // java.io.DataInput
    public double readDouble() throws IOException {
        return Double.longBitsToDouble(readLong());
    }

    @Override // java.io.DataInput
    public float readFloat() throws IOException {
        return Float.intBitsToFloat(readInt());
    }

    @Override // java.io.DataInput
    public void readFully(byte[] b) throws IOException {
        readFully(b, 0, b.length);
    }

    @Override // java.io.DataInput
    public void readFully(byte[] b, int off, int len) throws IOException {
        checkAvailable(len);
        this.buffer.readBytes(b, off, len);
    }

    @Override // java.io.DataInput
    public int readInt() throws IOException {
        checkAvailable(4);
        return this.buffer.readInt();
    }

    @Override // java.io.DataInput
    public String readLine() throws IOException {
        int available = available();
        if (available == 0) {
            return null;
        }
        if (this.lineBuf != null) {
            this.lineBuf.setLength(0);
        }
        while (true) {
            int c = this.buffer.readUnsignedByte();
            available--;
            switch (c) {
                case 10:
                    break;
                case 13:
                    if (available > 0 && ((char) this.buffer.getUnsignedByte(this.buffer.readerIndex())) == '\n') {
                        this.buffer.skipBytes(1);
                        int i = available - 1;
                        break;
                    }
                    break;
                default:
                    if (this.lineBuf == null) {
                        this.lineBuf = new StringBuilder();
                    }
                    this.lineBuf.append((char) c);
                    if (available <= 0) {
                        break;
                    }
            }
        }
        return (this.lineBuf == null || this.lineBuf.length() <= 0) ? "" : this.lineBuf.toString();
    }

    @Override // java.io.DataInput
    public long readLong() throws IOException {
        checkAvailable(8);
        return this.buffer.readLong();
    }

    @Override // java.io.DataInput
    public short readShort() throws IOException {
        checkAvailable(2);
        return this.buffer.readShort();
    }

    @Override // java.io.DataInput
    public String readUTF() throws IOException {
        return DataInputStream.readUTF(this);
    }

    @Override // java.io.DataInput
    public int readUnsignedByte() throws IOException {
        return readByte() & 255;
    }

    @Override // java.io.DataInput
    public int readUnsignedShort() throws IOException {
        return readShort() & UShort.MAX_VALUE;
    }

    @Override // java.io.DataInput
    public int skipBytes(int n) throws IOException {
        int nBytes = Math.min(available(), n);
        this.buffer.skipBytes(nBytes);
        return nBytes;
    }

    private void checkAvailable(int fieldSize) throws IOException {
        if (fieldSize < 0) {
            throw new IndexOutOfBoundsException("fieldSize cannot be a negative number");
        }
        if (fieldSize > available()) {
            throw new EOFException("fieldSize is too long! Length is " + fieldSize + ", but maximum is " + available());
        }
    }
}
