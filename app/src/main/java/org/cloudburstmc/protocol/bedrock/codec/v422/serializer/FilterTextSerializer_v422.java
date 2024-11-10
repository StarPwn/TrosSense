package org.cloudburstmc.protocol.bedrock.codec.v422.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.FilterTextPacket;

/* loaded from: classes5.dex */
public class FilterTextSerializer_v422 implements BedrockPacketSerializer<FilterTextPacket> {
    public static final FilterTextSerializer_v422 INSTANCE = new FilterTextSerializer_v422();

    protected FilterTextSerializer_v422() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, FilterTextPacket packet) {
        helper.writeString(buffer, packet.getText());
        buffer.writeBoolean(packet.isFromServer());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, FilterTextPacket packet) {
        packet.setText(helper.readString(buffer));
        packet.setFromServer(buffer.readBoolean());
    }
}
