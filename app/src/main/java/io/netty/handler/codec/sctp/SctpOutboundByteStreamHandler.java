package io.netty.handler.codec.sctp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.sctp.SctpMessage;
import io.netty.handler.codec.MessageToMessageEncoder;
import java.util.List;

/* loaded from: classes4.dex */
public class SctpOutboundByteStreamHandler extends MessageToMessageEncoder<ByteBuf> {
    private final int protocolIdentifier;
    private final int streamIdentifier;
    private final boolean unordered;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageToMessageEncoder
    public /* bridge */ /* synthetic */ void encode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List list) throws Exception {
        encode2(channelHandlerContext, byteBuf, (List<Object>) list);
    }

    public SctpOutboundByteStreamHandler(int streamIdentifier, int protocolIdentifier) {
        this(streamIdentifier, protocolIdentifier, false);
    }

    public SctpOutboundByteStreamHandler(int streamIdentifier, int protocolIdentifier, boolean unordered) {
        this.streamIdentifier = streamIdentifier;
        this.protocolIdentifier = protocolIdentifier;
        this.unordered = unordered;
    }

    /* renamed from: encode, reason: avoid collision after fix types in other method */
    protected void encode2(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        out.add(new SctpMessage(this.protocolIdentifier, this.streamIdentifier, this.unordered, msg.retain()));
    }
}
