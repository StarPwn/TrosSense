package io.netty.handler.stream;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.ObjectUtil;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import org.jose4j.jwk.RsaJsonWebKey;

/* loaded from: classes4.dex */
public class ChunkedFile implements ChunkedInput<ByteBuf> {
    private final int chunkSize;
    private final long endOffset;
    private final RandomAccessFile file;
    private long offset;
    private final long startOffset;

    public ChunkedFile(File file) throws IOException {
        this(file, 8192);
    }

    public ChunkedFile(File file, int chunkSize) throws IOException {
        this(new RandomAccessFile(file, RsaJsonWebKey.PRIME_FACTOR_OTHER_MEMBER_NAME), chunkSize);
    }

    public ChunkedFile(RandomAccessFile file) throws IOException {
        this(file, 8192);
    }

    public ChunkedFile(RandomAccessFile file, int chunkSize) throws IOException {
        this(file, 0L, file.length(), chunkSize);
    }

    public ChunkedFile(RandomAccessFile file, long offset, long length, int chunkSize) throws IOException {
        ObjectUtil.checkNotNull(file, "file");
        ObjectUtil.checkPositiveOrZero(offset, "offset");
        ObjectUtil.checkPositiveOrZero(length, "length");
        ObjectUtil.checkPositive(chunkSize, "chunkSize");
        this.file = file;
        this.startOffset = offset;
        this.offset = offset;
        this.endOffset = offset + length;
        this.chunkSize = chunkSize;
        file.seek(offset);
    }

    public long startOffset() {
        return this.startOffset;
    }

    public long endOffset() {
        return this.endOffset;
    }

    public long currentOffset() {
        return this.offset;
    }

    @Override // io.netty.handler.stream.ChunkedInput
    public boolean isEndOfInput() throws Exception {
        return this.offset >= this.endOffset || !this.file.getChannel().isOpen();
    }

    @Override // io.netty.handler.stream.ChunkedInput
    public void close() throws Exception {
        this.file.close();
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
        long offset = this.offset;
        if (offset >= this.endOffset) {
            return null;
        }
        int chunkSize = (int) Math.min(this.chunkSize, this.endOffset - offset);
        ByteBuf buf = allocator.heapBuffer(chunkSize);
        boolean release = true;
        try {
            this.file.readFully(buf.array(), buf.arrayOffset(), chunkSize);
            buf.writerIndex(chunkSize);
            this.offset = chunkSize + offset;
            release = false;
            return buf;
        } finally {
            if (release) {
                buf.release();
            }
        }
    }

    @Override // io.netty.handler.stream.ChunkedInput
    public long length() {
        return this.endOffset - this.startOffset;
    }

    @Override // io.netty.handler.stream.ChunkedInput
    public long progress() {
        return this.offset - this.startOffset;
    }
}
