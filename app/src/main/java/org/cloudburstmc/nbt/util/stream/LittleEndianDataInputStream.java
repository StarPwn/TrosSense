package org.cloudburstmc.nbt.util.stream;

import java.io.Closeable;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/* loaded from: classes5.dex */
public class LittleEndianDataInputStream implements DataInput, Closeable {
    protected final DataInputStream stream;

    public LittleEndianDataInputStream(InputStream stream) {
        this.stream = new DataInputStream(stream);
    }

    public LittleEndianDataInputStream(DataInputStream stream) {
        this.stream = stream;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.stream.close();
    }

    @Override // java.io.DataInput
    public void readFully(byte[] bytes) throws IOException {
        this.stream.readFully(bytes);
    }

    @Override // java.io.DataInput
    public void readFully(byte[] bytes, int offset, int length) throws IOException {
        this.stream.readFully(bytes, offset, length);
    }

    @Override // java.io.DataInput
    public int skipBytes(int amount) throws IOException {
        return this.stream.skipBytes(amount);
    }

    @Override // java.io.DataInput
    public boolean readBoolean() throws IOException {
        return this.stream.readBoolean();
    }

    @Override // java.io.DataInput
    public byte readByte() throws IOException {
        return this.stream.readByte();
    }

    @Override // java.io.DataInput
    public int readUnsignedByte() throws IOException {
        return this.stream.readUnsignedByte();
    }

    @Override // java.io.DataInput
    public short readShort() throws IOException {
        return Short.reverseBytes(this.stream.readShort());
    }

    @Override // java.io.DataInput
    public int readUnsignedShort() throws IOException {
        return LittleEndianDataInputStream$$ExternalSyntheticBackport0.m(Short.reverseBytes(this.stream.readShort()));
    }

    @Override // java.io.DataInput
    public char readChar() throws IOException {
        return Character.reverseBytes(this.stream.readChar());
    }

    @Override // java.io.DataInput
    public int readInt() throws IOException {
        return Integer.reverseBytes(this.stream.readInt());
    }

    @Override // java.io.DataInput
    public long readLong() throws IOException {
        return Long.reverseBytes(this.stream.readLong());
    }

    @Override // java.io.DataInput
    public float readFloat() throws IOException {
        return Float.intBitsToFloat(Integer.reverseBytes(this.stream.readInt()));
    }

    @Override // java.io.DataInput
    public double readDouble() throws IOException {
        return Double.longBitsToDouble(Long.reverseBytes(this.stream.readLong()));
    }

    @Override // java.io.DataInput
    @Deprecated
    public String readLine() throws IOException {
        return this.stream.readLine();
    }

    @Override // java.io.DataInput
    public String readUTF() throws IOException {
        byte[] bytes = new byte[readUnsignedShort()];
        readFully(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
