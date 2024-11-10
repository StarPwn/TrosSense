package io.netty.handler.codec.serialization;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import io.netty.util.internal.ObjectUtil;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

@Deprecated
/* loaded from: classes4.dex */
public class ObjectEncoderOutputStream extends OutputStream implements ObjectOutput {
    private final int estimatedLength;
    private final DataOutputStream out;

    public ObjectEncoderOutputStream(OutputStream out) {
        this(out, 512);
    }

    public ObjectEncoderOutputStream(OutputStream out, int estimatedLength) {
        ObjectUtil.checkNotNull(out, "out");
        ObjectUtil.checkPositiveOrZero(estimatedLength, "estimatedLength");
        if (out instanceof DataOutputStream) {
            this.out = (DataOutputStream) out;
        } else {
            this.out = new DataOutputStream(out);
        }
        this.estimatedLength = estimatedLength;
    }

    @Override // java.io.ObjectOutput
    public void writeObject(Object obj) throws IOException {
        ByteBuf buf = Unpooled.buffer(this.estimatedLength);
        try {
            ObjectOutputStream oout = new CompactObjectOutputStream(new ByteBufOutputStream(buf));
            try {
                oout.writeObject(obj);
                oout.flush();
                oout.close();
                int objectSize = buf.readableBytes();
                writeInt(objectSize);
                buf.getBytes(0, this, objectSize);
            } catch (Throwable th) {
                oout.close();
                throw th;
            }
        } finally {
            buf.release();
        }
    }

    @Override // java.io.OutputStream, java.io.ObjectOutput, java.io.DataOutput
    public void write(int b) throws IOException {
        this.out.write(b);
    }

    @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable, java.io.ObjectOutput
    public void close() throws IOException {
        this.out.close();
    }

    @Override // java.io.OutputStream, java.io.Flushable, java.io.ObjectOutput
    public void flush() throws IOException {
        this.out.flush();
    }

    public final int size() {
        return this.out.size();
    }

    @Override // java.io.OutputStream, java.io.ObjectOutput, java.io.DataOutput
    public void write(byte[] b, int off, int len) throws IOException {
        this.out.write(b, off, len);
    }

    @Override // java.io.OutputStream, java.io.ObjectOutput, java.io.DataOutput
    public void write(byte[] b) throws IOException {
        this.out.write(b);
    }

    @Override // java.io.DataOutput
    public final void writeBoolean(boolean v) throws IOException {
        this.out.writeBoolean(v);
    }

    @Override // java.io.DataOutput
    public final void writeByte(int v) throws IOException {
        this.out.writeByte(v);
    }

    @Override // java.io.DataOutput
    public final void writeBytes(String s) throws IOException {
        this.out.writeBytes(s);
    }

    @Override // java.io.DataOutput
    public final void writeChar(int v) throws IOException {
        this.out.writeChar(v);
    }

    @Override // java.io.DataOutput
    public final void writeChars(String s) throws IOException {
        this.out.writeChars(s);
    }

    @Override // java.io.DataOutput
    public final void writeDouble(double v) throws IOException {
        this.out.writeDouble(v);
    }

    @Override // java.io.DataOutput
    public final void writeFloat(float v) throws IOException {
        this.out.writeFloat(v);
    }

    @Override // java.io.DataOutput
    public final void writeInt(int v) throws IOException {
        this.out.writeInt(v);
    }

    @Override // java.io.DataOutput
    public final void writeLong(long v) throws IOException {
        this.out.writeLong(v);
    }

    @Override // java.io.DataOutput
    public final void writeShort(int v) throws IOException {
        this.out.writeShort(v);
    }

    @Override // java.io.DataOutput
    public final void writeUTF(String str) throws IOException {
        this.out.writeUTF(str);
    }
}
