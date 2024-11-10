package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.bedrock.data.SimulationType;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class SimulationTypePacket implements BedrockPacket {
    private SimulationType type;

    public void setType(SimulationType type) {
        this.type = type;
    }

    public String toString() {
        return "SimulationTypePacket(type=" + getType() + ")";
    }

    protected boolean canEqual(Object other) {
        return other instanceof SimulationTypePacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SimulationTypePacket)) {
            return false;
        }
        SimulationTypePacket other = (SimulationTypePacket) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$type = this.type;
        Object other$type = other.type;
        return this$type != null ? this$type.equals(other$type) : other$type == null;
    }

    public int hashCode() {
        Object $type = this.type;
        int result = (1 * 59) + ($type == null ? 43 : $type.hashCode());
        return result;
    }

    public SimulationType getType() {
        return this.type;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.SIMULATION_TYPE;
    }
}
