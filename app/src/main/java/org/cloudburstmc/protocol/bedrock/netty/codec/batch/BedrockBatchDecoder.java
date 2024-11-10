package org.cloudburstmc.protocol.bedrock.netty.codec.batch;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import java.util.List;
import org.cloudburstmc.protocol.bedrock.netty.BedrockBatchWrapper;
import org.cloudburstmc.protocol.common.util.VarInts;

@ChannelHandler.Sharable
/* loaded from: classes5.dex */
public class BedrockBatchDecoder extends MessageToMessageDecoder<BedrockBatchWrapper> {
    public static final String NAME = "bedrock-batch-decoder";

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageToMessageDecoder
    public /* bridge */ /* synthetic */ void decode(ChannelHandlerContext channelHandlerContext, BedrockBatchWrapper bedrockBatchWrapper, List list) throws Exception {
        decode2(channelHandlerContext, bedrockBatchWrapper, (List<Object>) list);
    }

    /* renamed from: decode, reason: avoid collision after fix types in other method */
    protected void decode2(ChannelHandlerContext ctx, BedrockBatchWrapper msg, List<Object> out) {
        if (msg.getUncompressed() == null) {
            throw new IllegalStateException("Batch packet was not decompressed");
        }
        ByteBuf buffer = msg.getUncompressed().slice();
        while (buffer.isReadable()) {
            int packetLength = VarInts.readUnsignedInt(buffer);
            ByteBuf packetBuf = buffer.readRetainedSlice(packetLength);
            out.add(packetBuf);
        }
    }
}
