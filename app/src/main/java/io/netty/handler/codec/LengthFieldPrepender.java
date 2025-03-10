package io.netty.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.ObjectUtil;
import java.nio.ByteOrder;
import java.util.List;

@ChannelHandler.Sharable
/* loaded from: classes4.dex */
public class LengthFieldPrepender extends MessageToMessageEncoder<ByteBuf> {
    private final ByteOrder byteOrder;
    private final int lengthAdjustment;
    private final int lengthFieldLength;
    private final boolean lengthIncludesLengthFieldLength;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageToMessageEncoder
    public /* bridge */ /* synthetic */ void encode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List list) throws Exception {
        encode2(channelHandlerContext, byteBuf, (List<Object>) list);
    }

    public LengthFieldPrepender(int lengthFieldLength) {
        this(lengthFieldLength, false);
    }

    public LengthFieldPrepender(int lengthFieldLength, boolean lengthIncludesLengthFieldLength) {
        this(lengthFieldLength, 0, lengthIncludesLengthFieldLength);
    }

    public LengthFieldPrepender(int lengthFieldLength, int lengthAdjustment) {
        this(lengthFieldLength, lengthAdjustment, false);
    }

    public LengthFieldPrepender(int lengthFieldLength, int lengthAdjustment, boolean lengthIncludesLengthFieldLength) {
        this(ByteOrder.BIG_ENDIAN, lengthFieldLength, lengthAdjustment, lengthIncludesLengthFieldLength);
    }

    public LengthFieldPrepender(ByteOrder byteOrder, int lengthFieldLength, int lengthAdjustment, boolean lengthIncludesLengthFieldLength) {
        if (lengthFieldLength != 1 && lengthFieldLength != 2 && lengthFieldLength != 3 && lengthFieldLength != 4 && lengthFieldLength != 8) {
            throw new IllegalArgumentException("lengthFieldLength must be either 1, 2, 3, 4, or 8: " + lengthFieldLength);
        }
        this.byteOrder = (ByteOrder) ObjectUtil.checkNotNull(byteOrder, "byteOrder");
        this.lengthFieldLength = lengthFieldLength;
        this.lengthIncludesLengthFieldLength = lengthIncludesLengthFieldLength;
        this.lengthAdjustment = lengthAdjustment;
    }

    /* renamed from: encode, reason: avoid collision after fix types in other method */
    protected void encode2(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        int length = msg.readableBytes() + this.lengthAdjustment;
        if (this.lengthIncludesLengthFieldLength) {
            length += this.lengthFieldLength;
        }
        ObjectUtil.checkPositiveOrZero(length, "length");
        switch (this.lengthFieldLength) {
            case 1:
                if (length >= 256) {
                    throw new IllegalArgumentException("length does not fit into a byte: " + length);
                }
                out.add(ctx.alloc().buffer(1).order(this.byteOrder).writeByte((byte) length));
                break;
            case 2:
                if (length >= 65536) {
                    throw new IllegalArgumentException("length does not fit into a short integer: " + length);
                }
                out.add(ctx.alloc().buffer(2).order(this.byteOrder).writeShort((short) length));
                break;
            case 3:
                if (length >= 16777216) {
                    throw new IllegalArgumentException("length does not fit into a medium integer: " + length);
                }
                out.add(ctx.alloc().buffer(3).order(this.byteOrder).writeMedium(length));
                break;
            case 4:
                out.add(ctx.alloc().buffer(4).order(this.byteOrder).writeInt(length));
                break;
            case 5:
            case 6:
            case 7:
            default:
                throw new Error("should not reach here");
            case 8:
                out.add(ctx.alloc().buffer(8).order(this.byteOrder).writeLong(length));
                break;
        }
        out.add(msg.retain());
    }
}
