package org.cloudburstmc.protocol.common.util.stream;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.ByteBufUtil;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/* loaded from: classes5.dex */
public class LittleEndianByteBufOutputStream extends ByteBufOutputStream {
    private final ByteBuf buffer;

    public LittleEndianByteBufOutputStream(ByteBuf buffer) {
        super(buffer);
        this.buffer = buffer;
    }

    @Override // io.netty.buffer.ByteBufOutputStream, java.io.DataOutput
    public void writeChar(int v) throws IOException {
        this.buffer.writeChar(Character.reverseBytes((char) v));
    }

    @Override // io.netty.buffer.ByteBufOutputStream, java.io.DataOutput
    public void writeDouble(double v) throws IOException {
        this.buffer.writeDoubleLE(v);
    }

    @Override // io.netty.buffer.ByteBufOutputStream, java.io.DataOutput
    public void writeFloat(float v) throws IOException {
        this.buffer.writeFloatLE(v);
    }

    @Override // io.netty.buffer.ByteBufOutputStream, java.io.DataOutput
    public void writeShort(int val) throws IOException {
        this.buffer.writeShortLE(val);
    }

    @Override // io.netty.buffer.ByteBufOutputStream, java.io.DataOutput
    public void writeLong(long val) throws IOException {
        this.buffer.writeLongLE(val);
    }

    @Override // io.netty.buffer.ByteBufOutputStream, java.io.DataOutput
    public void writeInt(int val) throws IOException {
        this.buffer.writeIntLE(val);
    }

    @Override // io.netty.buffer.ByteBufOutputStream, java.io.DataOutput
    public void writeUTF(String string) throws IOException {
        int length = ByteBufUtil.utf8Bytes(string);
        writeShort(length);
        this.buffer.writeCharSequence(string, StandardCharsets.UTF_8);
    }
}
