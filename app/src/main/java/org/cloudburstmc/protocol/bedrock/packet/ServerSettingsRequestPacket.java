package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class ServerSettingsRequestPacket implements BedrockPacket {
    protected boolean canEqual(Object other) {
        return other instanceof ServerSettingsRequestPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ServerSettingsRequestPacket)) {
            return false;
        }
        ServerSettingsRequestPacket other = (ServerSettingsRequestPacket) o;
        return other.canEqual(this);
    }

    public int hashCode() {
        return 1;
    }

    public String toString() {
        return "ServerSettingsRequestPacket()";
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.SERVER_SETTINGS_REQUEST;
    }
}
