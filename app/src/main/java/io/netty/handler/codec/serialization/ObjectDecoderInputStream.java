package io.netty.handler.codec.serialization;

import io.netty.util.internal.ObjectUtil;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.StreamCorruptedException;

@Deprecated
/* loaded from: classes4.dex */
public class ObjectDecoderInputStream extends InputStream implements ObjectInput {
    private final ClassResolver classResolver;
    private final DataInputStream in;
    private final int maxObjectSize;

    public ObjectDecoderInputStream(InputStream in) {
        this(in, (ClassLoader) null);
    }

    public ObjectDecoderInputStream(InputStream in, ClassLoader classLoader) {
        this(in, classLoader, 1048576);
    }

    public ObjectDecoderInputStream(InputStream in, int maxObjectSize) {
        this(in, null, maxObjectSize);
    }

    public ObjectDecoderInputStream(InputStream in, ClassLoader classLoader, int maxObjectSize) {
        ObjectUtil.checkNotNull(in, "in");
        ObjectUtil.checkPositive(maxObjectSize, "maxObjectSize");
        if (in instanceof DataInputStream) {
            this.in = (DataInputStream) in;
        } else {
            this.in = new DataInputStream(in);
        }
        this.classResolver = ClassResolvers.weakCachingResolver(classLoader);
        this.maxObjectSize = maxObjectSize;
    }

    @Override // java.io.ObjectInput
    public Object readObject() throws ClassNotFoundException, IOException {
        int dataLen = readInt();
        if (dataLen <= 0) {
            throw new StreamCorruptedException("invalid data length: " + dataLen);
        }
        if (dataLen > this.maxObjectSize) {
            throw new StreamCorruptedException("data length too big: " + dataLen + " (max: " + this.maxObjectSize + ')');
        }
        return new CompactObjectInputStream(this.in, this.classResolver).readObject();
    }

    @Override // java.io.InputStream, java.io.ObjectInput
    public int available() throws IOException {
        return this.in.available();
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable, java.io.ObjectInput
    public void close() throws IOException {
        this.in.close();
    }

    @Override // java.io.InputStream
    public void mark(int readlimit) {
        this.in.mark(readlimit);
    }

    @Override // java.io.InputStream
    public boolean markSupported() {
        return this.in.markSupported();
    }

    @Override // java.io.InputStream, java.io.ObjectInput
    public int read() throws IOException {
        return this.in.read();
    }

    @Override // java.io.InputStream, java.io.ObjectInput
    public final int read(byte[] b, int off, int len) throws IOException {
        return this.in.read(b, off, len);
    }

    @Override // java.io.InputStream, java.io.ObjectInput
    public final int read(byte[] b) throws IOException {
        return this.in.read(b);
    }

    @Override // java.io.DataInput
    public final boolean readBoolean() throws IOException {
        return this.in.readBoolean();
    }

    @Override // java.io.DataInput
    public final byte readByte() throws IOException {
        return this.in.readByte();
    }

    @Override // java.io.DataInput
    public final char readChar() throws IOException {
        return this.in.readChar();
    }

    @Override // java.io.DataInput
    public final double readDouble() throws IOException {
        return this.in.readDouble();
    }

    @Override // java.io.DataInput
    public final float readFloat() throws IOException {
        return this.in.readFloat();
    }

    @Override // java.io.DataInput
    public final void readFully(byte[] b, int off, int len) throws IOException {
        this.in.readFully(b, off, len);
    }

    @Override // java.io.DataInput
    public final void readFully(byte[] b) throws IOException {
        this.in.readFully(b);
    }

    @Override // java.io.DataInput
    public final int readInt() throws IOException {
        return this.in.readInt();
    }

    @Override // java.io.DataInput
    @Deprecated
    public final String readLine() throws IOException {
        return this.in.readLine();
    }

    @Override // java.io.DataInput
    public final long readLong() throws IOException {
        return this.in.readLong();
    }

    @Override // java.io.DataInput
    public final short readShort() throws IOException {
        return this.in.readShort();
    }

    @Override // java.io.DataInput
    public final int readUnsignedByte() throws IOException {
        return this.in.readUnsignedByte();
    }

    @Override // java.io.DataInput
    public final int readUnsignedShort() throws IOException {
        return this.in.readUnsignedShort();
    }

    @Override // java.io.DataInput
    public final String readUTF() throws IOException {
        return this.in.readUTF();
    }

    @Override // java.io.InputStream
    public void reset() throws IOException {
        this.in.reset();
    }

    @Override // java.io.InputStream, java.io.ObjectInput
    public long skip(long n) throws IOException {
        return this.in.skip(n);
    }

    @Override // java.io.DataInput
    public final int skipBytes(int n) throws IOException {
        return this.in.skipBytes(n);
    }
}
