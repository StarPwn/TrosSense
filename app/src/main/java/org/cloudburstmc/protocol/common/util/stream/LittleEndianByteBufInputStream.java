package org.cloudburstmc.protocol.common.util.stream;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/* loaded from: classes5.dex */
public class LittleEndianByteBufInputStream extends ByteBufInputStream {
    private final ByteBuf buffer;

    public LittleEndianByteBufInputStream(ByteBuf buffer) {
        super(buffer);
        this.buffer = buffer;
    }

    @Override // io.netty.buffer.ByteBufInputStream, java.io.DataInput
    public char readChar() throws IOException {
        return Character.reverseBytes(this.buffer.readChar());
    }

    @Override // io.netty.buffer.ByteBufInputStream, java.io.DataInput
    public double readDouble() throws IOException {
        return this.buffer.readDoubleLE();
    }

    @Override // io.netty.buffer.ByteBufInputStream, java.io.DataInput
    public float readFloat() throws IOException {
        return this.buffer.readFloatLE();
    }

    @Override // io.netty.buffer.ByteBufInputStream, java.io.DataInput
    public short readShort() throws IOException {
        return this.buffer.readShortLE();
    }

    @Override // io.netty.buffer.ByteBufInputStream, java.io.DataInput
    public int readUnsignedShort() throws IOException {
        return this.buffer.readUnsignedShortLE();
    }

    @Override // io.netty.buffer.ByteBufInputStream, java.io.DataInput
    public long readLong() throws IOException {
        return this.buffer.readLongLE();
    }

    @Override // io.netty.buffer.ByteBufInputStream, java.io.DataInput
    public int readInt() throws IOException {
        return this.buffer.readIntLE();
    }

    @Override // io.netty.buffer.ByteBufInputStream, java.io.DataInput
    public String readUTF() throws IOException {
        int length = readUnsignedShort();
        return (String) this.buffer.readCharSequence(length, StandardCharsets.UTF_8);
    }
}
