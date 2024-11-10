package io.netty.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import java.util.List;

/* loaded from: classes4.dex */
public class LineBasedFrameDecoder extends ByteToMessageDecoder {
    private int discardedBytes;
    private boolean discarding;
    private final boolean failFast;
    private final int maxLength;
    private int offset;
    private final boolean stripDelimiter;

    public LineBasedFrameDecoder(int maxLength) {
        this(maxLength, true, false);
    }

    public LineBasedFrameDecoder(int maxLength, boolean stripDelimiter, boolean failFast) {
        this.maxLength = maxLength;
        this.failFast = failFast;
        this.stripDelimiter = stripDelimiter;
    }

    @Override // io.netty.handler.codec.ByteToMessageDecoder
    protected final void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        Object decoded = decode(ctx, in);
        if (decoded != null) {
            out.add(decoded);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Object decode(ChannelHandlerContext ctx, ByteBuf buffer) throws Exception {
        int delimLength;
        int eol = findEndOfLine(buffer);
        if (!this.discarding) {
            if (eol >= 0) {
                int length = eol - buffer.readerIndex();
                delimLength = buffer.getByte(eol) != 13 ? 1 : 2;
                if (length > this.maxLength) {
                    buffer.readerIndex(eol + delimLength);
                    fail(ctx, length);
                    return null;
                }
                if (this.stripDelimiter) {
                    ByteBuf frame = buffer.readRetainedSlice(length);
                    buffer.skipBytes(delimLength);
                    return frame;
                }
                ByteBuf frame2 = buffer.readRetainedSlice(length + delimLength);
                return frame2;
            }
            int length2 = buffer.readableBytes();
            if (length2 > this.maxLength) {
                this.discardedBytes = length2;
                buffer.readerIndex(buffer.writerIndex());
                this.discarding = true;
                this.offset = 0;
                if (this.failFast) {
                    fail(ctx, "over " + this.discardedBytes);
                }
            }
            return null;
        }
        if (eol >= 0) {
            int length3 = (this.discardedBytes + eol) - buffer.readerIndex();
            delimLength = buffer.getByte(eol) != 13 ? 1 : 2;
            buffer.readerIndex(eol + delimLength);
            this.discardedBytes = 0;
            this.discarding = false;
            if (!this.failFast) {
                fail(ctx, length3);
            }
        } else {
            this.discardedBytes += buffer.readableBytes();
            buffer.readerIndex(buffer.writerIndex());
            this.offset = 0;
        }
        return null;
    }

    private void fail(ChannelHandlerContext ctx, int length) {
        fail(ctx, String.valueOf(length));
    }

    private void fail(ChannelHandlerContext ctx, String length) {
        ctx.fireExceptionCaught((Throwable) new TooLongFrameException("frame length (" + length + ") exceeds the allowed maximum (" + this.maxLength + ')'));
    }

    private int findEndOfLine(ByteBuf buffer) {
        int totalLength = buffer.readableBytes();
        int i = buffer.indexOf(buffer.readerIndex() + this.offset, buffer.readerIndex() + totalLength, (byte) 10);
        if (i >= 0) {
            this.offset = 0;
            if (i > 0 && buffer.getByte(i - 1) == 13) {
                return i - 1;
            }
            return i;
        }
        this.offset = totalLength;
        return i;
    }
}
