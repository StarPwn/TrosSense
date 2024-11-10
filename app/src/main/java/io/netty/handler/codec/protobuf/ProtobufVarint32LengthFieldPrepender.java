package io.netty.handler.codec.protobuf;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

@ChannelHandler.Sharable
/* loaded from: classes4.dex */
public class ProtobufVarint32LengthFieldPrepender extends MessageToByteEncoder<ByteBuf> {
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageToByteEncoder
    public void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception {
        int bodyLen = msg.readableBytes();
        int headerLen = computeRawVarint32Size(bodyLen);
        out.ensureWritable(headerLen + bodyLen);
        writeRawVarint32(out, bodyLen);
        out.writeBytes(msg, msg.readerIndex(), bodyLen);
    }

    static void writeRawVarint32(ByteBuf out, int value) {
        while ((value & (-128)) != 0) {
            out.writeByte((value & 127) | 128);
            value >>>= 7;
        }
        out.writeByte(value);
    }

    static int computeRawVarint32Size(int value) {
        if ((value & (-128)) == 0) {
            return 1;
        }
        if ((value & (-16384)) == 0) {
            return 2;
        }
        if (((-2097152) & value) == 0) {
            return 3;
        }
        if (((-268435456) & value) == 0) {
            return 4;
        }
        return 5;
    }
}
