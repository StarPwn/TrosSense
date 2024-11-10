package io.netty.handler.codec.protobuf;

import com.google.protobuf.nano.MessageNano;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.internal.ObjectUtil;
import java.util.List;

@ChannelHandler.Sharable
/* loaded from: classes4.dex */
public class ProtobufDecoderNano extends MessageToMessageDecoder<ByteBuf> {
    private final Class<? extends MessageNano> clazz;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageToMessageDecoder
    public /* bridge */ /* synthetic */ void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List list) throws Exception {
        decode2(channelHandlerContext, byteBuf, (List<Object>) list);
    }

    public ProtobufDecoderNano(Class<? extends MessageNano> clazz) {
        this.clazz = (Class) ObjectUtil.checkNotNull(clazz, "You must provide a Class");
    }

    /* renamed from: decode, reason: avoid collision after fix types in other method */
    protected void decode2(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        byte[] array;
        int offset;
        int length = msg.readableBytes();
        if (msg.hasArray()) {
            array = msg.array();
            offset = msg.arrayOffset() + msg.readerIndex();
        } else {
            array = ByteBufUtil.getBytes(msg, msg.readerIndex(), length, false);
            offset = 0;
        }
        MessageNano prototype = this.clazz.getConstructor(new Class[0]).newInstance(new Object[0]);
        out.add(MessageNano.mergeFrom(prototype, array, offset, length));
    }
}
