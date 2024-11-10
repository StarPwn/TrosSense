package org.cloudburstmc.protocol.bedrock.codec.v291.serializer;

import io.netty.buffer.ByteBuf;
import java.util.List;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityLinkData;
import org.cloudburstmc.protocol.bedrock.packet.AddPlayerPacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class AddPlayerSerializer_v291 implements BedrockPacketSerializer<AddPlayerPacket> {
    public static final AddPlayerSerializer_v291 INSTANCE = new AddPlayerSerializer_v291();

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, AddPlayerPacket packet) {
        helper.writeUuid(buffer, packet.getUuid());
        helper.writeString(buffer, packet.getUsername());
        VarInts.writeLong(buffer, packet.getUniqueEntityId());
        VarInts.writeUnsignedLong(buffer, packet.getRuntimeEntityId());
        helper.writeString(buffer, packet.getPlatformChatId());
        helper.writeVector3f(buffer, packet.getPosition());
        helper.writeVector3f(buffer, packet.getMotion());
        helper.writeVector3f(buffer, packet.getRotation());
        helper.writeItem(buffer, packet.getHand());
        helper.writeEntityData(buffer, packet.getMetadata());
        AdventureSettingsSerializer_v291.INSTANCE.serialize(buffer, helper, packet.getAdventureSettings());
        List<EntityLinkData> entityLinks = packet.getEntityLinks();
        helper.getClass();
        helper.writeArray(buffer, entityLinks, new AddEntitySerializer_v291$$ExternalSyntheticLambda1(helper));
        helper.writeString(buffer, packet.getDeviceId());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, AddPlayerPacket packet) {
        packet.setUuid(helper.readUuid(buffer));
        packet.setUsername(helper.readString(buffer));
        packet.setUniqueEntityId(VarInts.readLong(buffer));
        packet.setRuntimeEntityId(VarInts.readUnsignedLong(buffer));
        packet.setPlatformChatId(helper.readString(buffer));
        packet.setPosition(helper.readVector3f(buffer));
        packet.setMotion(helper.readVector3f(buffer));
        packet.setRotation(helper.readVector3f(buffer));
        packet.setHand(helper.readItem(buffer));
        helper.readEntityData(buffer, packet.getMetadata());
        AdventureSettingsSerializer_v291.INSTANCE.deserialize(buffer, helper, packet.getAdventureSettings());
        List<EntityLinkData> entityLinks = packet.getEntityLinks();
        helper.getClass();
        helper.readArray(buffer, entityLinks, new AddEntitySerializer_v291$$ExternalSyntheticLambda3(helper));
        packet.setDeviceId(helper.readString(buffer));
    }
}
