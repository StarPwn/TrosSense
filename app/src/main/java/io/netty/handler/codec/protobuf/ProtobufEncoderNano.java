package io.netty.handler.codec.protobuf;

import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.MessageNano;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import java.util.List;

@ChannelHandler.Sharable
/* loaded from: classes4.dex */
public class ProtobufEncoderNano extends MessageToMessageEncoder<MessageNano> {
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageToMessageEncoder
    public /* bridge */ /* synthetic */ void encode(ChannelHandlerContext channelHandlerContext, MessageNano messageNano, List list) throws Exception {
        encode2(channelHandlerContext, messageNano, (List<Object>) list);
    }

    /* renamed from: encode, reason: avoid collision after fix types in other method */
    protected void encode2(ChannelHandlerContext ctx, MessageNano msg, List<Object> out) throws Exception {
        int size = msg.getSerializedSize();
        ByteBuf buffer = ctx.alloc().heapBuffer(size, size);
        byte[] array = buffer.array();
        CodedOutputByteBufferNano cobbn = CodedOutputByteBufferNano.newInstance(array, buffer.arrayOffset(), buffer.capacity());
        msg.writeTo(cobbn);
        buffer.writerIndex(size);
        out.add(buffer);
    }
}
