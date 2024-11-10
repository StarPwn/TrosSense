package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class AvailableEntityIdentifiersPacket implements BedrockPacket {
    private NbtMap identifiers;

    public void setIdentifiers(NbtMap identifiers) {
        this.identifiers = identifiers;
    }

    protected boolean canEqual(Object other) {
        return other instanceof AvailableEntityIdentifiersPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AvailableEntityIdentifiersPacket)) {
            return false;
        }
        AvailableEntityIdentifiersPacket other = (AvailableEntityIdentifiersPacket) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$identifiers = this.identifiers;
        Object other$identifiers = other.identifiers;
        return this$identifiers != null ? this$identifiers.equals(other$identifiers) : other$identifiers == null;
    }

    public int hashCode() {
        Object $identifiers = this.identifiers;
        int result = (1 * 59) + ($identifiers == null ? 43 : $identifiers.hashCode());
        return result;
    }

    public String toString() {
        return "AvailableEntityIdentifiersPacket(identifiers=" + this.identifiers + ")";
    }

    public NbtMap getIdentifiers() {
        return this.identifiers;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.AVAILABLE_ENTITY_IDENTIFIERS;
    }
}
