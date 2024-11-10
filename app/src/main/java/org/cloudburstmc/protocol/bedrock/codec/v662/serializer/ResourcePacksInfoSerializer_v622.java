package org.cloudburstmc.protocol.bedrock.codec.v662.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v618.serializer.ResourcePacksInfoSerializer_v618;
import org.cloudburstmc.protocol.bedrock.packet.ResourcePacksInfoPacket;

/* loaded from: classes5.dex */
public class ResourcePacksInfoSerializer_v622 extends ResourcePacksInfoSerializer_v618 {
    public static final ResourcePacksInfoSerializer_v622 INSTANCE = new ResourcePacksInfoSerializer_v622();

    protected ResourcePacksInfoSerializer_v622() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v618.serializer.ResourcePacksInfoSerializer_v618, org.cloudburstmc.protocol.bedrock.codec.v448.serializer.ResourcePacksInfoSerializer_v448, org.cloudburstmc.protocol.bedrock.codec.v422.serializer.ResourcePacksInfoSerializer_v422, org.cloudburstmc.protocol.bedrock.codec.v332.serializer.ResourcePacksInfoSerializer_v332, org.cloudburstmc.protocol.bedrock.codec.v291.serializer.ResourcePacksInfoSerializer_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, ResourcePacksInfoPacket packet) {
        buffer.writeBoolean(packet.isForcedToAccept());
        buffer.writeBoolean(packet.isHasAddonPacks());
        buffer.writeBoolean(packet.isScriptingEnabled());
        buffer.writeBoolean(packet.isForcingServerPacksEnabled());
        writePacks(buffer, packet.getBehaviorPackInfos(), helper, false);
        writePacks(buffer, packet.getResourcePackInfos(), helper, true);
        writeCDNEntries(buffer, packet, helper);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v618.serializer.ResourcePacksInfoSerializer_v618, org.cloudburstmc.protocol.bedrock.codec.v448.serializer.ResourcePacksInfoSerializer_v448, org.cloudburstmc.protocol.bedrock.codec.v422.serializer.ResourcePacksInfoSerializer_v422, org.cloudburstmc.protocol.bedrock.codec.v332.serializer.ResourcePacksInfoSerializer_v332, org.cloudburstmc.protocol.bedrock.codec.v291.serializer.ResourcePacksInfoSerializer_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, ResourcePacksInfoPacket packet) {
        packet.setForcedToAccept(buffer.readBoolean());
        packet.setHasAddonPacks(buffer.readBoolean());
        packet.setScriptingEnabled(buffer.readBoolean());
        packet.setForcingServerPacksEnabled(buffer.readBoolean());
        readPacks(buffer, packet.getBehaviorPackInfos(), helper, false);
        readPacks(buffer, packet.getResourcePackInfos(), helper, true);
        readCDNEntries(buffer, packet, helper);
    }
}
