package io.netty.handler.codec.compression;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.internal.ObjectUtil;

/* loaded from: classes4.dex */
public abstract class ZlibDecoder extends ByteToMessageDecoder {
    protected final int maxAllocation;

    public abstract boolean isClosed();

    public ZlibDecoder() {
        this(0);
    }

    public ZlibDecoder(int maxAllocation) {
        this.maxAllocation = ObjectUtil.checkPositiveOrZero(maxAllocation, "maxAllocation");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ByteBuf prepareDecompressBuffer(ChannelHandlerContext ctx, ByteBuf buffer, int preferredSize) {
        if (buffer == null) {
            if (this.maxAllocation == 0) {
                return ctx.alloc().heapBuffer(preferredSize);
            }
            return ctx.alloc().heapBuffer(Math.min(preferredSize, this.maxAllocation), this.maxAllocation);
        }
        if (buffer.ensureWritable(preferredSize, true) == 1) {
            decompressionBufferExhausted(buffer.duplicate());
            buffer.skipBytes(buffer.readableBytes());
            throw new DecompressionException("Decompression buffer has reached maximum size: " + buffer.maxCapacity());
        }
        return buffer;
    }

    protected void decompressionBufferExhausted(ByteBuf buffer) {
    }
}
