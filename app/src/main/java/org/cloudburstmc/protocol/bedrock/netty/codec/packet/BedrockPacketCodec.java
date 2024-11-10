package org.cloudburstmc.protocol.bedrock.netty.codec.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.List;
import java.util.Objects;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodec;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.compat.BedrockCompat;
import org.cloudburstmc.protocol.bedrock.netty.BedrockPacketWrapper;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.cloudburstmc.protocol.bedrock.packet.UnknownPacket;

/* loaded from: classes5.dex */
public abstract class BedrockPacketCodec extends MessageToMessageCodec<ByteBuf, BedrockPacketWrapper> {
    public static final String NAME = "bedrock-packet-codec";
    private static final InternalLogger log = InternalLoggerFactory.getInstance((Class<?>) BedrockPacketCodec.class);
    private BedrockCodec codec = BedrockCompat.CODEC;
    private BedrockCodecHelper helper = this.codec.createHelper();

    public abstract void decodeHeader(ByteBuf byteBuf, BedrockPacketWrapper bedrockPacketWrapper);

    public abstract void encodeHeader(ByteBuf byteBuf, BedrockPacketWrapper bedrockPacketWrapper);

    @Override // io.netty.handler.codec.MessageToMessageCodec
    protected /* bridge */ /* synthetic */ void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List list) throws Exception {
        decode2(channelHandlerContext, byteBuf, (List<Object>) list);
    }

    @Override // io.netty.handler.codec.MessageToMessageCodec
    protected /* bridge */ /* synthetic */ void encode(ChannelHandlerContext channelHandlerContext, BedrockPacketWrapper bedrockPacketWrapper, List list) throws Exception {
        encode2(channelHandlerContext, bedrockPacketWrapper, (List<Object>) list);
    }

    /* renamed from: encode, reason: avoid collision after fix types in other method */
    protected final void encode2(ChannelHandlerContext ctx, BedrockPacketWrapper msg, List<Object> out) throws Exception {
        if (msg.getPacketBuffer() != null) {
            out.add(msg.retain());
            return;
        }
        ByteBuf buf = ctx.alloc().buffer(128);
        try {
            BedrockPacket packet = msg.getPacket();
            msg.setPacketId(getPacketId(packet));
            encodeHeader(buf, msg);
            this.codec.tryEncode(this.helper, buf, packet);
            msg.setPacketBuffer(buf.retain());
            out.add(msg.retain());
        } finally {
            try {
            } finally {
            }
        }
    }

    /* renamed from: decode, reason: avoid collision after fix types in other method */
    protected final void decode2(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        BedrockPacketWrapper wrapper = new BedrockPacketWrapper();
        wrapper.setPacketBuffer(msg.retainedSlice());
        try {
            int index = msg.readerIndex();
            decodeHeader(msg, wrapper);
            wrapper.setHeaderLength(msg.readerIndex() - index);
            wrapper.setPacket(this.codec.tryDecode(this.helper, msg, wrapper.getPacketId()));
            out.add(wrapper.retain());
        } finally {
        }
    }

    public final int getPacketId(BedrockPacket packet) {
        if (packet instanceof UnknownPacket) {
            return ((UnknownPacket) packet).getPacketId();
        }
        return this.codec.getPacketDefinition(packet.getClass()).getId();
    }

    public final void setCodec(BedrockCodec codec) {
        this.codec = (BedrockCodec) Objects.requireNonNull(codec, "Codec cannot be null");
        this.helper = codec.createHelper();
    }

    public final BedrockCodec getCodec() {
        return this.codec;
    }

    public BedrockCodecHelper getHelper() {
        return this.helper;
    }
}
