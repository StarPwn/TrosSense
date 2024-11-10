package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class ClientToServerHandshakePacket implements BedrockPacket {
    protected boolean canEqual(Object other) {
        return other instanceof ClientToServerHandshakePacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ClientToServerHandshakePacket)) {
            return false;
        }
        ClientToServerHandshakePacket other = (ClientToServerHandshakePacket) o;
        return other.canEqual(this);
    }

    public int hashCode() {
        return 1;
    }

    public String toString() {
        return "ClientToServerHandshakePacket()";
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.CLIENT_TO_SERVER_HANDSHAKE;
    }
}
