package io.netty.handler.codec.marshalling;

import io.netty.buffer.ByteBuf;
import java.io.IOException;
import org.jboss.marshalling.ByteInput;

/* loaded from: classes4.dex */
class ChannelBufferByteInput implements ByteInput {
    private final ByteBuf buffer;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ChannelBufferByteInput(ByteBuf buffer) {
        this.buffer = buffer;
    }

    public void close() throws IOException {
    }

    public int available() throws IOException {
        return this.buffer.readableBytes();
    }

    public int read() throws IOException {
        if (this.buffer.isReadable()) {
            return this.buffer.readByte() & 255;
        }
        return -1;
    }

    public int read(byte[] array) throws IOException {
        return read(array, 0, array.length);
    }

    public int read(byte[] dst, int dstIndex, int length) throws IOException {
        int available = available();
        if (available == 0) {
            return -1;
        }
        int length2 = Math.min(available, length);
        this.buffer.readBytes(dst, dstIndex, length2);
        return length2;
    }

    public long skip(long bytes) throws IOException {
        int readable = this.buffer.readableBytes();
        if (readable < bytes) {
            bytes = readable;
        }
        this.buffer.readerIndex((int) (this.buffer.readerIndex() + bytes));
        return bytes;
    }
}
