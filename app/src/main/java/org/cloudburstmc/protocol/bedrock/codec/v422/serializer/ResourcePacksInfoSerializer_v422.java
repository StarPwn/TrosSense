package org.cloudburstmc.protocol.bedrock.codec.v422.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v332.serializer.ResourcePacksInfoSerializer_v332;
import org.cloudburstmc.protocol.bedrock.packet.ResourcePacksInfoPacket;

/* loaded from: classes5.dex */
public class ResourcePacksInfoSerializer_v422 extends ResourcePacksInfoSerializer_v332 {
    public static final ResourcePacksInfoSerializer_v422 INSTANCE = new ResourcePacksInfoSerializer_v422();

    @Override // org.cloudburstmc.protocol.bedrock.codec.v332.serializer.ResourcePacksInfoSerializer_v332, org.cloudburstmc.protocol.bedrock.codec.v291.serializer.ResourcePacksInfoSerializer_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, ResourcePacksInfoPacket packet) {
        buffer.writeBoolean(packet.isForcedToAccept());
        buffer.writeBoolean(packet.isScriptingEnabled());
        writePacks(buffer, packet.getBehaviorPackInfos(), helper, false);
        writePacks(buffer, packet.getResourcePackInfos(), helper, true);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v332.serializer.ResourcePacksInfoSerializer_v332, org.cloudburstmc.protocol.bedrock.codec.v291.serializer.ResourcePacksInfoSerializer_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, ResourcePacksInfoPacket packet) {
        packet.setForcedToAccept(buffer.readBoolean());
        packet.setScriptingEnabled(buffer.readBoolean());
        readPacks(buffer, packet.getBehaviorPackInfos(), helper, false);
        readPacks(buffer, packet.getResourcePackInfos(), helper, true);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v332.serializer.ResourcePacksInfoSerializer_v332, org.cloudburstmc.protocol.bedrock.codec.v291.serializer.ResourcePacksInfoSerializer_v291
    public void writeEntry(ByteBuf buffer, BedrockCodecHelper helper, ResourcePacksInfoPacket.Entry entry, boolean resource) {
        super.writeEntry(buffer, helper, entry, resource);
        if (resource) {
            buffer.writeBoolean(entry.isRaytracingCapable());
        }
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v332.serializer.ResourcePacksInfoSerializer_v332, org.cloudburstmc.protocol.bedrock.codec.v291.serializer.ResourcePacksInfoSerializer_v291
    public ResourcePacksInfoPacket.Entry readEntry(ByteBuf buffer, BedrockCodecHelper helper, boolean resource) {
        String packId = helper.readString(buffer);
        String packVersion = helper.readString(buffer);
        long packSize = buffer.readLongLE();
        String contentKey = helper.readString(buffer);
        String subPackName = helper.readString(buffer);
        String contentId = helper.readString(buffer);
        boolean isScripting = buffer.readBoolean();
        boolean raytracingCapable = resource && buffer.readBoolean();
        return new ResourcePacksInfoPacket.Entry(packId, packVersion, packSize, contentKey, subPackName, contentId, isScripting, raytracingCapable);
    }
}
