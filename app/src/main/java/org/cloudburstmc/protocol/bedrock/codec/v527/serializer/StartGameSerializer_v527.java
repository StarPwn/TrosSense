package org.cloudburstmc.protocol.bedrock.codec.v527.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v465.serializer.StartGameSerializer_v465;
import org.cloudburstmc.protocol.bedrock.packet.StartGamePacket;

/* loaded from: classes5.dex */
public class StartGameSerializer_v527 extends StartGameSerializer_v465 {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v440.serializer.StartGameSerializer_v440, org.cloudburstmc.protocol.bedrock.codec.v428.serializer.StartGameSerializer_v428, org.cloudburstmc.protocol.bedrock.codec.v419.serializer.StartGameSerializer_v419, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, StartGamePacket packet) {
        super.serialize(buffer, helper, packet);
        helper.writeTag(buffer, packet.getPlayerPropertyData());
        buffer.writeLongLE(packet.getBlockRegistryChecksum());
        helper.writeUuid(buffer, packet.getWorldTemplateId());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v440.serializer.StartGameSerializer_v440, org.cloudburstmc.protocol.bedrock.codec.v428.serializer.StartGameSerializer_v428, org.cloudburstmc.protocol.bedrock.codec.v419.serializer.StartGameSerializer_v419, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, StartGamePacket packet) {
        super.deserialize(buffer, helper, packet);
        packet.setPlayerPropertyData((NbtMap) helper.readTag(buffer, NbtMap.class));
        packet.setBlockRegistryChecksum(buffer.readLongLE());
        packet.setWorldTemplateId(helper.readUuid(buffer));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v419.serializer.StartGameSerializer_v419
    public long readSeed(ByteBuf buf) {
        return buf.readLongLE();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v419.serializer.StartGameSerializer_v419
    public void writeSeed(ByteBuf buf, long seed) {
        buf.writeLongLE(seed);
    }
}
