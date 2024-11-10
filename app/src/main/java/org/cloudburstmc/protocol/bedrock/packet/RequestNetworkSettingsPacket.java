package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class RequestNetworkSettingsPacket implements BedrockPacket {
    private int protocolVersion;

    public void setProtocolVersion(int protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    protected boolean canEqual(Object other) {
        return other instanceof RequestNetworkSettingsPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RequestNetworkSettingsPacket)) {
            return false;
        }
        RequestNetworkSettingsPacket other = (RequestNetworkSettingsPacket) o;
        return other.canEqual(this) && this.protocolVersion == other.protocolVersion;
    }

    public int hashCode() {
        int result = (1 * 59) + this.protocolVersion;
        return result;
    }

    public String toString() {
        return "RequestNetworkSettingsPacket(protocolVersion=" + this.protocolVersion + ")";
    }

    public int getProtocolVersion() {
        return this.protocolVersion;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.REQUEST_NETWORK_SETTINGS;
    }
}
