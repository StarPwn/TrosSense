package io.netty.handler.stream;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.ObjectUtil;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;

/* loaded from: classes4.dex */
public class ChunkedNioStream implements ChunkedInput<ByteBuf> {
    private final ByteBuffer byteBuffer;
    private final int chunkSize;
    private final ReadableByteChannel in;
    private long offset;

    public ChunkedNioStream(ReadableByteChannel in) {
        this(in, 8192);
    }

    public ChunkedNioStream(ReadableByteChannel in, int chunkSize) {
        this.in = (ReadableByteChannel) ObjectUtil.checkNotNull(in, "in");
        this.chunkSize = ObjectUtil.checkPositive(chunkSize, "chunkSize");
        this.byteBuffer = ByteBuffer.allocate(chunkSize);
    }

    public long transferredBytes() {
        return this.offset;
    }

    @Override // io.netty.handler.stream.ChunkedInput
    public boolean isEndOfInput() throws Exception {
        int b;
        if (this.byteBuffer.position() > 0) {
            return false;
        }
        if (!this.in.isOpen() || (b = this.in.read(this.byteBuffer)) < 0) {
            return true;
        }
        this.offset += b;
        return false;
    }

    @Override // io.netty.handler.stream.ChunkedInput
    public void close() throws Exception {
        this.in.close();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // io.netty.handler.stream.ChunkedInput
    @Deprecated
    public ByteBuf readChunk(ChannelHandlerContext ctx) throws Exception {
        return readChunk(ctx.alloc());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // io.netty.handler.stream.ChunkedInput
    public ByteBuf readChunk(ByteBufAllocator allocator) throws Exception {
        if (isEndOfInput()) {
            return null;
        }
        int readBytes = this.byteBuffer.position();
        do {
            int localReadBytes = this.in.read(this.byteBuffer);
            if (localReadBytes < 0) {
                break;
            }
            readBytes += localReadBytes;
            this.offset += localReadBytes;
        } while (readBytes != this.chunkSize);
        this.byteBuffer.flip();
        boolean release = true;
        ByteBuf buffer = allocator.buffer(this.byteBuffer.remaining());
        try {
            buffer.writeBytes(this.byteBuffer);
            this.byteBuffer.clear();
            release = false;
            return buffer;
        } finally {
            if (release) {
                buffer.release();
            }
        }
    }

    @Override // io.netty.handler.stream.ChunkedInput
    public long length() {
        return -1L;
    }

    @Override // io.netty.handler.stream.ChunkedInput
    public long progress() {
        return this.offset;
    }
}
