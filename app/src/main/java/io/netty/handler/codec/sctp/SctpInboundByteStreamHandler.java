package io.netty.handler.codec.sctp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.sctp.SctpMessage;
import io.netty.handler.codec.CodecException;
import io.netty.handler.codec.MessageToMessageDecoder;
import java.util.List;

/* loaded from: classes4.dex */
public class SctpInboundByteStreamHandler extends MessageToMessageDecoder<SctpMessage> {
    private final int protocolIdentifier;
    private final int streamIdentifier;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageToMessageDecoder
    public /* bridge */ /* synthetic */ void decode(ChannelHandlerContext channelHandlerContext, SctpMessage sctpMessage, List list) throws Exception {
        decode2(channelHandlerContext, sctpMessage, (List<Object>) list);
    }

    public SctpInboundByteStreamHandler(int protocolIdentifier, int streamIdentifier) {
        this.protocolIdentifier = protocolIdentifier;
        this.streamIdentifier = streamIdentifier;
    }

    @Override // io.netty.handler.codec.MessageToMessageDecoder
    public final boolean acceptInboundMessage(Object msg) throws Exception {
        if (super.acceptInboundMessage(msg)) {
            return acceptInboundMessage((SctpMessage) msg);
        }
        return false;
    }

    protected boolean acceptInboundMessage(SctpMessage msg) {
        return msg.protocolIdentifier() == this.protocolIdentifier && msg.streamIdentifier() == this.streamIdentifier;
    }

    /* renamed from: decode, reason: avoid collision after fix types in other method */
    protected void decode2(ChannelHandlerContext ctx, SctpMessage msg, List<Object> out) throws Exception {
        if (!msg.isComplete()) {
            throw new CodecException(String.format("Received SctpMessage is not complete, please add %s in the pipeline before this handler", SctpMessageCompletionHandler.class.getSimpleName()));
        }
        out.add(msg.content().retain());
    }
}
