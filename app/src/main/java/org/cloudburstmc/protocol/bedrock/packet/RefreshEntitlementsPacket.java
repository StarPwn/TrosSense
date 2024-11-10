package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class RefreshEntitlementsPacket implements BedrockPacket {
    protected boolean canEqual(Object other) {
        return other instanceof RefreshEntitlementsPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RefreshEntitlementsPacket)) {
            return false;
        }
        RefreshEntitlementsPacket other = (RefreshEntitlementsPacket) o;
        return other.canEqual(this);
    }

    public int hashCode() {
        return 1;
    }

    public String toString() {
        return "RefreshEntitlementsPacket()";
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.REFRESH_ENTITLEMENTS;
    }
}
