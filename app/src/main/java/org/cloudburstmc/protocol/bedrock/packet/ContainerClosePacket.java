package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class ContainerClosePacket implements BedrockPacket {
    private byte id;
    private boolean serverInitiated;

    public void setId(byte id) {
        this.id = id;
    }

    public void setServerInitiated(boolean serverInitiated) {
        this.serverInitiated = serverInitiated;
    }

    protected boolean canEqual(Object other) {
        return other instanceof ContainerClosePacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ContainerClosePacket)) {
            return false;
        }
        ContainerClosePacket other = (ContainerClosePacket) o;
        return other.canEqual(this) && this.id == other.id && this.serverInitiated == other.serverInitiated;
    }

    public int hashCode() {
        int result = (1 * 59) + this.id;
        return (result * 59) + (this.serverInitiated ? 79 : 97);
    }

    public String toString() {
        return "ContainerClosePacket(id=" + ((int) this.id) + ", serverInitiated=" + this.serverInitiated + ")";
    }

    public byte getId() {
        return this.id;
    }

    public boolean isServerInitiated() {
        return this.serverInitiated;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.CONTAINER_CLOSE;
    }
}
