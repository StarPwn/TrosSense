package org.cloudburstmc.protocol.bedrock.codec.v448.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.data.SimulationType;
import org.cloudburstmc.protocol.bedrock.packet.SimulationTypePacket;

/* loaded from: classes5.dex */
public class SimulationTypeSerializer_v448 implements BedrockPacketSerializer<SimulationTypePacket> {
    public static final SimulationTypeSerializer_v448 INSTANCE = new SimulationTypeSerializer_v448();
    private static final SimulationType[] VALUES = SimulationType.values();

    protected SimulationTypeSerializer_v448() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, SimulationTypePacket packet) {
        buffer.writeByte(packet.getType().ordinal());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, SimulationTypePacket packet) {
        packet.setType(VALUES[buffer.readUnsignedByte()]);
    }
}
