package org.cloudburstmc.protocol.bedrock.codec.v486.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.data.CodeBuilderCategoryType;
import org.cloudburstmc.protocol.bedrock.data.CodeBuilderOperationType;
import org.cloudburstmc.protocol.bedrock.packet.CodeBuilderSourcePacket;

/* loaded from: classes5.dex */
public class CodeBuilderSourceSerializer_v486 implements BedrockPacketSerializer<CodeBuilderSourcePacket> {
    public static final CodeBuilderSourceSerializer_v486 INSTANCE = new CodeBuilderSourceSerializer_v486();

    protected CodeBuilderSourceSerializer_v486() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, CodeBuilderSourcePacket packet) {
        buffer.writeByte(packet.getOperation().ordinal());
        buffer.writeByte(packet.getCategory().ordinal());
        helper.writeString(buffer, packet.getValue());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, CodeBuilderSourcePacket packet) {
        packet.setOperation(CodeBuilderOperationType.values()[buffer.readByte()]);
        packet.setCategory(CodeBuilderCategoryType.values()[buffer.readByte()]);
        packet.setValue(helper.readString(buffer));
    }
}
