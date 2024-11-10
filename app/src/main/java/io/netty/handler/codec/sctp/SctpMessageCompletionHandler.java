package io.netty.handler.codec.sctp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.sctp.SctpMessage;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.collection.IntObjectMap;
import java.util.List;

/* loaded from: classes4.dex */
public class SctpMessageCompletionHandler extends MessageToMessageDecoder<SctpMessage> {
    private final IntObjectMap<ByteBuf> fragments = new IntObjectHashMap();

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageToMessageDecoder
    public /* bridge */ /* synthetic */ void decode(ChannelHandlerContext channelHandlerContext, SctpMessage sctpMessage, List list) throws Exception {
        decode2(channelHandlerContext, sctpMessage, (List<Object>) list);
    }

    /* renamed from: decode, reason: avoid collision after fix types in other method */
    protected void decode2(ChannelHandlerContext ctx, SctpMessage msg, List<Object> out) throws Exception {
        ByteBuf byteBuf = msg.content();
        int protocolIdentifier = msg.protocolIdentifier();
        int streamIdentifier = msg.streamIdentifier();
        boolean isComplete = msg.isComplete();
        boolean isUnordered = msg.isUnordered();
        ByteBuf frag = this.fragments.remove(streamIdentifier);
        if (frag == null) {
            frag = Unpooled.EMPTY_BUFFER;
        }
        if (isComplete && !frag.isReadable()) {
            out.add(msg);
        } else if (!isComplete && frag.isReadable()) {
            this.fragments.put(streamIdentifier, (int) Unpooled.wrappedBuffer(frag, byteBuf));
        } else if (isComplete && frag.isReadable()) {
            SctpMessage assembledMsg = new SctpMessage(protocolIdentifier, streamIdentifier, isUnordered, Unpooled.wrappedBuffer(frag, byteBuf));
            out.add(assembledMsg);
        } else {
            this.fragments.put(streamIdentifier, (int) byteBuf);
        }
        byteBuf.retain();
    }

    @Override // io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        for (ByteBuf buffer : this.fragments.values()) {
            buffer.release();
        }
        this.fragments.clear();
        super.handlerRemoved(ctx);
    }
}
