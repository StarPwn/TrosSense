package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class RemoveVolumeEntityPacket implements BedrockPacket {
    private int dimension;
    private int id;

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String toString() {
        return "RemoveVolumeEntityPacket(id=" + getId() + ", dimension=" + getDimension() + ")";
    }

    protected boolean canEqual(Object other) {
        return other instanceof RemoveVolumeEntityPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RemoveVolumeEntityPacket)) {
            return false;
        }
        RemoveVolumeEntityPacket other = (RemoveVolumeEntityPacket) o;
        return other.canEqual(this) && this.id == other.id && this.dimension == other.dimension;
    }

    public int hashCode() {
        int result = (1 * 59) + this.id;
        return (result * 59) + this.dimension;
    }

    public int getId() {
        return this.id;
    }

    public int getDimension() {
        return this.dimension;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.REMOVE_VOLUME_ENTITY;
    }
}
