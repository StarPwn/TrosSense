package org.cloudburstmc.nbt.util.stream;

import java.io.Closeable;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/* loaded from: classes5.dex */
public class LittleEndianDataOutputStream implements DataOutput, Closeable {
    protected final DataOutputStream stream;

    public LittleEndianDataOutputStream(OutputStream stream) {
        this.stream = new DataOutputStream(stream);
    }

    public LittleEndianDataOutputStream(DataOutputStream stream) {
        this.stream = stream;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.stream.close();
    }

    @Override // java.io.DataOutput
    public void write(int bytes) throws IOException {
        this.stream.write(bytes);
    }

    @Override // java.io.DataOutput
    public void write(byte[] bytes) throws IOException {
        this.stream.write(bytes);
    }

    @Override // java.io.DataOutput
    public void write(byte[] bytes, int offset, int length) throws IOException {
        this.stream.write(bytes, offset, length);
    }

    @Override // java.io.DataOutput
    public void writeBoolean(boolean value) throws IOException {
        this.stream.writeBoolean(value);
    }

    @Override // java.io.DataOutput
    public void writeByte(int value) throws IOException {
        this.stream.writeByte(value);
    }

    @Override // java.io.DataOutput
    public void writeShort(int value) throws IOException {
        this.stream.writeShort(Short.reverseBytes((short) value));
    }

    @Override // java.io.DataOutput
    public void writeChar(int value) throws IOException {
        this.stream.writeChar(Character.reverseBytes((char) value));
    }

    @Override // java.io.DataOutput
    public void writeInt(int value) throws IOException {
        this.stream.writeInt(Integer.reverseBytes(value));
    }

    @Override // java.io.DataOutput
    public void writeLong(long value) throws IOException {
        this.stream.writeLong(Long.reverseBytes(value));
    }

    @Override // java.io.DataOutput
    public void writeFloat(float value) throws IOException {
        this.stream.writeInt(Integer.reverseBytes(Float.floatToIntBits(value)));
    }

    @Override // java.io.DataOutput
    public void writeDouble(double value) throws IOException {
        this.stream.writeLong(Long.reverseBytes(Double.doubleToLongBits(value)));
    }

    @Override // java.io.DataOutput
    public void writeBytes(String string) throws IOException {
        this.stream.writeBytes(string);
    }

    @Override // java.io.DataOutput
    public void writeChars(String string) throws IOException {
        this.stream.writeChars(string);
    }

    @Override // java.io.DataOutput
    public void writeUTF(String string) throws IOException {
        byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
        writeShort(bytes.length);
        write(bytes);
    }
}
