package org.cloudburstmc.protocol.bedrock.codec.v486.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v465.serializer.AddVolumeEntitySerializer_v465;
import org.cloudburstmc.protocol.bedrock.packet.AddVolumeEntityPacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class AddVolumeEntitySerializer_v486 extends AddVolumeEntitySerializer_v465 {
    public static final AddVolumeEntitySerializer_v486 INSTANCE = new AddVolumeEntitySerializer_v486();

    protected AddVolumeEntitySerializer_v486() {
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v465.serializer.AddVolumeEntitySerializer_v465, org.cloudburstmc.protocol.bedrock.codec.v440.serializer.AddVolumeEntitySerializer_v440, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, AddVolumeEntityPacket packet) {
        VarInts.writeUnsignedInt(buffer, packet.getId());
        helper.writeTag(buffer, packet.getData());
        helper.writeString(buffer, packet.getIdentifier());
        helper.writeString(buffer, packet.getInstanceName());
        helper.writeString(buffer, packet.getEngineVersion());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v465.serializer.AddVolumeEntitySerializer_v465, org.cloudburstmc.protocol.bedrock.codec.v440.serializer.AddVolumeEntitySerializer_v440, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, AddVolumeEntityPacket packet) {
        packet.setId(VarInts.readUnsignedInt(buffer));
        packet.setData((NbtMap) helper.readTag(buffer, NbtMap.class));
        packet.setIdentifier(helper.readString(buffer));
        packet.setInstanceName(helper.readString(buffer));
        packet.setEngineVersion(helper.readString(buffer));
    }
}
