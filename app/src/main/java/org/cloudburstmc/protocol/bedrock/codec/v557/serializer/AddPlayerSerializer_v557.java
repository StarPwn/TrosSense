package org.cloudburstmc.protocol.bedrock.codec.v557.serializer;

import io.netty.buffer.ByteBuf;
import java.util.List;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.AddEntitySerializer_v291$$ExternalSyntheticLambda1;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.AddEntitySerializer_v291$$ExternalSyntheticLambda3;
import org.cloudburstmc.protocol.bedrock.codec.v534.serializer.AddPlayerSerializer_v534;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityLinkData;
import org.cloudburstmc.protocol.bedrock.packet.AddPlayerPacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class AddPlayerSerializer_v557 extends AddPlayerSerializer_v534 {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v534.serializer.AddPlayerSerializer_v534, org.cloudburstmc.protocol.bedrock.codec.v503.serializer.AddPlayerSerializer_v503, org.cloudburstmc.protocol.bedrock.codec.v388.serializer.AddPlayerSerializer_v388, org.cloudburstmc.protocol.bedrock.codec.v291.serializer.AddPlayerSerializer_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, AddPlayerPacket packet) {
        helper.writeUuid(buffer, packet.getUuid());
        helper.writeString(buffer, packet.getUsername());
        VarInts.writeUnsignedLong(buffer, packet.getRuntimeEntityId());
        helper.writeString(buffer, packet.getPlatformChatId());
        helper.writeVector3f(buffer, packet.getPosition());
        helper.writeVector3f(buffer, packet.getMotion());
        helper.writeVector3f(buffer, packet.getRotation());
        helper.writeItem(buffer, packet.getHand());
        VarInts.writeInt(buffer, packet.getGameType().ordinal());
        helper.writeEntityData(buffer, packet.getMetadata());
        helper.writeEntityProperties(buffer, packet.getProperties());
        helper.writePlayerAbilities(buffer, packet);
        List<EntityLinkData> entityLinks = packet.getEntityLinks();
        helper.getClass();
        helper.writeArray(buffer, entityLinks, new AddEntitySerializer_v291$$ExternalSyntheticLambda1(helper));
        helper.writeString(buffer, packet.getDeviceId());
        buffer.writeIntLE(packet.getBuildPlatform());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v534.serializer.AddPlayerSerializer_v534, org.cloudburstmc.protocol.bedrock.codec.v503.serializer.AddPlayerSerializer_v503, org.cloudburstmc.protocol.bedrock.codec.v388.serializer.AddPlayerSerializer_v388, org.cloudburstmc.protocol.bedrock.codec.v291.serializer.AddPlayerSerializer_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, AddPlayerPacket packet) {
        packet.setUuid(helper.readUuid(buffer));
        packet.setUsername(helper.readString(buffer));
        packet.setRuntimeEntityId(VarInts.readUnsignedLong(buffer));
        packet.setPlatformChatId(helper.readString(buffer));
        packet.setPosition(helper.readVector3f(buffer));
        packet.setMotion(helper.readVector3f(buffer));
        packet.setRotation(helper.readVector3f(buffer));
        packet.setHand(helper.readItem(buffer));
        packet.setGameType(VALUES[VarInts.readInt(buffer)]);
        helper.readEntityData(buffer, packet.getMetadata());
        helper.readEntityProperties(buffer, packet.getProperties());
        helper.readPlayerAbilities(buffer, packet);
        List<EntityLinkData> entityLinks = packet.getEntityLinks();
        helper.getClass();
        helper.readArray(buffer, entityLinks, new AddEntitySerializer_v291$$ExternalSyntheticLambda3(helper));
        packet.setDeviceId(helper.readString(buffer));
        packet.setBuildPlatform(buffer.readIntLE());
    }
}
