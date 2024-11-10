package io.netty.handler.codec.compression;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.EncoderException;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.internal.ObjectUtil;
import java.nio.ByteBuffer;

/* loaded from: classes4.dex */
public final class ZstdEncoder extends MessageToByteEncoder<ByteBuf> {
    private final int blockSize;
    private ByteBuf buffer;
    private final int compressionLevel;
    private final int maxEncodeSize;

    public ZstdEncoder() {
        this(ZstdConstants.DEFAULT_COMPRESSION_LEVEL, 65536, ZstdConstants.MAX_BLOCK_SIZE);
    }

    public ZstdEncoder(int compressionLevel) {
        this(compressionLevel, 65536, ZstdConstants.MAX_BLOCK_SIZE);
    }

    public ZstdEncoder(int blockSize, int maxEncodeSize) {
        this(ZstdConstants.DEFAULT_COMPRESSION_LEVEL, blockSize, maxEncodeSize);
    }

    public ZstdEncoder(int compressionLevel, int blockSize, int maxEncodeSize) {
        super(true);
        try {
            Zstd.ensureAvailability();
            this.compressionLevel = ObjectUtil.checkInRange(compressionLevel, ZstdConstants.MIN_COMPRESSION_LEVEL, ZstdConstants.MAX_COMPRESSION_LEVEL, "compressionLevel");
            this.blockSize = ObjectUtil.checkPositive(blockSize, "blockSize");
            this.maxEncodeSize = ObjectUtil.checkPositive(maxEncodeSize, "maxEncodeSize");
        } catch (Throwable throwable) {
            throw new ExceptionInInitializerError(throwable);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageToByteEncoder
    public ByteBuf allocateBuffer(ChannelHandlerContext ctx, ByteBuf msg, boolean preferDirect) {
        if (this.buffer == null) {
            throw new IllegalStateException("not added to a pipeline,or has been removed,buffer is null");
        }
        int remaining = msg.readableBytes() + this.buffer.readableBytes();
        if (remaining < 0) {
            throw new EncoderException("too much data to allocate a buffer for compression");
        }
        long bufferSize = 0;
        while (remaining > 0) {
            int curSize = Math.min(this.blockSize, remaining);
            remaining -= curSize;
            bufferSize += com.github.luben.zstd.Zstd.compressBound(curSize);
        }
        if (bufferSize > this.maxEncodeSize || 0 > bufferSize) {
            throw new EncoderException("requested encode buffer size (" + bufferSize + " bytes) exceeds the maximum allowable size (" + this.maxEncodeSize + " bytes)");
        }
        return ctx.alloc().directBuffer((int) bufferSize);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageToByteEncoder
    public void encode(ChannelHandlerContext ctx, ByteBuf in, ByteBuf out) {
        if (this.buffer == null) {
            throw new IllegalStateException("not added to a pipeline,or has been removed,buffer is null");
        }
        ByteBuf buffer = this.buffer;
        while (true) {
            int length = in.readableBytes();
            if (length > 0) {
                int nextChunkSize = Math.min(length, buffer.writableBytes());
                in.readBytes(buffer, nextChunkSize);
                if (!buffer.isWritable()) {
                    flushBufferedData(out);
                }
            } else {
                return;
            }
        }
    }

    private void flushBufferedData(ByteBuf out) {
        int flushableBytes = this.buffer.readableBytes();
        if (flushableBytes == 0) {
            return;
        }
        int bufSize = (int) com.github.luben.zstd.Zstd.compressBound(flushableBytes);
        out.ensureWritable(bufSize);
        int idx = out.writerIndex();
        try {
            ByteBuffer outNioBuffer = out.internalNioBuffer(idx, out.writableBytes());
            int compressedLength = com.github.luben.zstd.Zstd.compress(outNioBuffer, this.buffer.internalNioBuffer(this.buffer.readerIndex(), flushableBytes), this.compressionLevel);
            out.writerIndex(idx + compressedLength);
            this.buffer.clear();
        } catch (Exception e) {
            throw new CompressionException(e);
        }
    }

    @Override // io.netty.channel.ChannelOutboundHandlerAdapter, io.netty.channel.ChannelOutboundHandler
    public void flush(ChannelHandlerContext ctx) {
        if (this.buffer != null && this.buffer.isReadable()) {
            ByteBuf buf = allocateBuffer(ctx, Unpooled.EMPTY_BUFFER, isPreferDirect());
            flushBufferedData(buf);
            ctx.write(buf);
        }
        ctx.flush();
    }

    @Override // io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler
    public void handlerAdded(ChannelHandlerContext ctx) {
        this.buffer = ctx.alloc().directBuffer(this.blockSize);
        this.buffer.clear();
    }

    @Override // io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
        if (this.buffer != null) {
            this.buffer.release();
            this.buffer = null;
        }
    }
}
