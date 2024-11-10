package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class ClientCacheStatusPacket implements BedrockPacket {
    private boolean supported;

    public void setSupported(boolean supported) {
        this.supported = supported;
    }

    protected boolean canEqual(Object other) {
        return other instanceof ClientCacheStatusPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ClientCacheStatusPacket)) {
            return false;
        }
        ClientCacheStatusPacket other = (ClientCacheStatusPacket) o;
        return other.canEqual(this) && this.supported == other.supported;
    }

    public int hashCode() {
        int result = (1 * 59) + (this.supported ? 79 : 97);
        return result;
    }

    public String toString() {
        return "ClientCacheStatusPacket(supported=" + this.supported + ")";
    }

    public boolean isSupported() {
        return this.supported;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.CLIENT_CACHE_STATUS;
    }
}
