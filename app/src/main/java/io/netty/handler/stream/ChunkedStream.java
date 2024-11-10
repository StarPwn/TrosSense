package io.netty.handler.stream;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.ObjectUtil;
import java.io.InputStream;
import java.io.PushbackInputStream;

/* loaded from: classes4.dex */
public class ChunkedStream implements ChunkedInput<ByteBuf> {
    static final int DEFAULT_CHUNK_SIZE = 8192;
    private final int chunkSize;
    private boolean closed;
    private final PushbackInputStream in;
    private long offset;

    public ChunkedStream(InputStream in) {
        this(in, 8192);
    }

    public ChunkedStream(InputStream in, int chunkSize) {
        ObjectUtil.checkNotNull(in, "in");
        ObjectUtil.checkPositive(chunkSize, "chunkSize");
        if (in instanceof PushbackInputStream) {
            this.in = (PushbackInputStream) in;
        } else {
            this.in = new PushbackInputStream(in);
        }
        this.chunkSize = chunkSize;
    }

    public long transferredBytes() {
        return this.offset;
    }

    @Override // io.netty.handler.stream.ChunkedInput
    public boolean isEndOfInput() throws Exception {
        if (this.closed) {
            return true;
        }
        if (this.in.available() > 0) {
            return false;
        }
        int b = this.in.read();
        if (b < 0) {
            return true;
        }
        this.in.unread(b);
        return false;
    }

    @Override // io.netty.handler.stream.ChunkedInput
    public void close() throws Exception {
        this.closed = true;
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
        int chunkSize;
        if (isEndOfInput()) {
            return null;
        }
        int availableBytes = this.in.available();
        if (availableBytes <= 0) {
            chunkSize = this.chunkSize;
        } else {
            int chunkSize2 = this.chunkSize;
            chunkSize = Math.min(chunkSize2, this.in.available());
        }
        ByteBuf buffer = allocator.buffer(chunkSize);
        try {
            int written = buffer.writeBytes(this.in, chunkSize);
            if (written < 0) {
                return null;
            }
            this.offset += written;
            if (0 != 0) {
                buffer.release();
            }
            return buffer;
        } finally {
            if (1 != 0) {
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
