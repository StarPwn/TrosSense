package io.netty.buffer;

import io.netty.util.CharsetUtil;
import io.netty.util.internal.ObjectUtil;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/* loaded from: classes4.dex */
public class ByteBufOutputStream extends OutputStream implements DataOutput {
    private final ByteBuf buffer;
    private boolean closed;
    private final int startIndex;
    private DataOutputStream utf8out;

    public ByteBufOutputStream(ByteBuf buffer) {
        this.buffer = (ByteBuf) ObjectUtil.checkNotNull(buffer, "buffer");
        this.startIndex = buffer.writerIndex();
    }

    public int writtenBytes() {
        return this.buffer.writerIndex() - this.startIndex;
    }

    @Override // java.io.OutputStream, java.io.DataOutput
    public void write(byte[] b, int off, int len) throws IOException {
        if (len == 0) {
            return;
        }
        this.buffer.writeBytes(b, off, len);
    }

    @Override // java.io.OutputStream, java.io.DataOutput
    public void write(byte[] b) throws IOException {
        this.buffer.writeBytes(b);
    }

    @Override // java.io.OutputStream, java.io.DataOutput
    public void write(int b) throws IOException {
        this.buffer.writeByte(b);
    }

    @Override // java.io.DataOutput
    public void writeBoolean(boolean v) throws IOException {
        this.buffer.writeBoolean(v);
    }

    @Override // java.io.DataOutput
    public void writeByte(int v) throws IOException {
        this.buffer.writeByte(v);
    }

    @Override // java.io.DataOutput
    public void writeBytes(String s) throws IOException {
        this.buffer.writeCharSequence(s, CharsetUtil.US_ASCII);
    }

    @Override // java.io.DataOutput
    public void writeChar(int v) throws IOException {
        this.buffer.writeChar(v);
    }

    @Override // java.io.DataOutput
    public void writeChars(String s) throws IOException {
        int len = s.length();
        for (int i = 0; i < len; i++) {
            this.buffer.writeChar(s.charAt(i));
        }
    }

    @Override // java.io.DataOutput
    public void writeDouble(double v) throws IOException {
        this.buffer.writeDouble(v);
    }

    @Override // java.io.DataOutput
    public void writeFloat(float v) throws IOException {
        this.buffer.writeFloat(v);
    }

    @Override // java.io.DataOutput
    public void writeInt(int v) throws IOException {
        this.buffer.writeInt(v);
    }

    @Override // java.io.DataOutput
    public void writeLong(long v) throws IOException {
        this.buffer.writeLong(v);
    }

    @Override // java.io.DataOutput
    public void writeShort(int v) throws IOException {
        this.buffer.writeShort((short) v);
    }

    @Override // java.io.DataOutput
    public void writeUTF(String s) throws IOException {
        DataOutputStream out = this.utf8out;
        if (out == null) {
            if (this.closed) {
                throw new IOException("The stream is closed");
            }
            DataOutputStream dataOutputStream = new DataOutputStream(this);
            out = dataOutputStream;
            this.utf8out = dataOutputStream;
        }
        out.writeUTF(s);
    }

    public ByteBuf buffer() {
        return this.buffer;
    }

    @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.closed) {
            return;
        }
        this.closed = true;
        try {
            super.close();
        } finally {
            if (this.utf8out != null) {
                this.utf8out.close();
            }
        }
    }
}
