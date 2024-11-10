package org.cloudburstmc.protocol.bedrock.codec;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;

/* loaded from: classes5.dex */
public interface BedrockPacketSerializer<T extends BedrockPacket> {
    void deserialize(ByteBuf byteBuf, BedrockCodecHelper bedrockCodecHelper, T t);

    void serialize(ByteBuf byteBuf, BedrockCodecHelper bedrockCodecHelper, T t);
}
