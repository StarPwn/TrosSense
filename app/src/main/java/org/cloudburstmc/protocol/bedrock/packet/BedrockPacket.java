package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.MinecraftPacket;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public interface BedrockPacket extends MinecraftPacket {
    BedrockPacketType getPacketType();

    PacketSignal handle(BedrockPacketHandler bedrockPacketHandler);
}
