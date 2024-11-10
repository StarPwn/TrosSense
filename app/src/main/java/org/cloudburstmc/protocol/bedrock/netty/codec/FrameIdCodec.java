package org.cloudburstmc.protocol.bedrock.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import java.util.List;
import org.cloudburstmc.netty.channel.raknet.RakReliability;
import org.cloudburstmc.netty.channel.raknet.packet.RakMessage;
import org.cloudburstmc.protocol.bedrock.netty.BedrockBatchWrapper;

@ChannelHandler.Sharable
/* loaded from: classes5.dex */
public class FrameIdCodec extends MessageToMessageCodec<RakMessage, BedrockBatchWrapper> {
    public static final String NAME = "frame-id-codec";
    private final int frameId;

    @Override // io.netty.handler.codec.MessageToMessageCodec
    protected /* bridge */ /* synthetic */ void decode(ChannelHandlerContext channelHandlerContext, RakMessage rakMessage, List list) throws Exception {
        decode2(channelHandlerContext, rakMessage, (List<Object>) list);
    }

    @Override // io.netty.handler.codec.MessageToMessageCodec
    protected /* bridge */ /* synthetic */ void encode(ChannelHandlerContext channelHandlerContext, BedrockBatchWrapper bedrockBatchWrapper, List list) throws Exception {
        encode2(channelHandlerContext, bedrockBatchWrapper, (List<Object>) list);
    }

    public FrameIdCodec(int frameId) {
        this.frameId = frameId;
    }

    /* renamed from: encode, reason: avoid collision after fix types in other method */
    protected void encode2(ChannelHandlerContext ctx, BedrockBatchWrapper msg, List<Object> out) throws Exception {
        if (msg.getCompressed() == null) {
            throw new IllegalStateException("Bedrock batch was not compressed");
        }
        CompositeByteBuf buf = ctx.alloc().compositeDirectBuffer(2);
        try {
            buf.addComponent(true, ctx.alloc().ioBuffer(1).writeByte(this.frameId));
            buf.addComponent(true, msg.getCompressed().retainedSlice());
            out.add(buf.retain());
        } finally {
            buf.release();
        }
    }

    /* renamed from: decode, reason: avoid collision after fix types in other method */
    protected void decode2(ChannelHandlerContext ctx, RakMessage msg, List<Object> out) throws Exception {
        if (msg.channel() != 0 && msg.reliability() != RakReliability.RELIABLE_ORDERED) {
            return;
        }
        ByteBuf in = msg.content();
        if (!in.isReadable()) {
            return;
        }
        int id = in.readUnsignedByte();
        if (id != this.frameId) {
            throw new IllegalStateException("Invalid frame ID: " + id);
        }
        out.add(BedrockBatchWrapper.newInstance(in.readRetainedSlice(in.readableBytes()), null));
    }
}
