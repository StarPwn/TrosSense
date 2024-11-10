package org.cloudburstmc.protocol.bedrock.codec;

import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;

@FunctionalInterface
/* loaded from: classes5.dex */
public interface BedrockPacketFactory<T extends BedrockPacket> {
    BedrockPacket newInstance();

    default Class<BedrockPacket> getPacketClass() {
        return newInstance().getClass();
    }
}
