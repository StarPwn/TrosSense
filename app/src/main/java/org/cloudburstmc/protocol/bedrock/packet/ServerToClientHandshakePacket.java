package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.bedrock.annotation.NoEncryption;
import org.cloudburstmc.protocol.common.PacketSignal;

@NoEncryption
/* loaded from: classes5.dex */
public class ServerToClientHandshakePacket implements BedrockPacket {
    private String jwt;

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    protected boolean canEqual(Object other) {
        return other instanceof ServerToClientHandshakePacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ServerToClientHandshakePacket)) {
            return false;
        }
        ServerToClientHandshakePacket other = (ServerToClientHandshakePacket) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$jwt = this.jwt;
        Object other$jwt = other.jwt;
        return this$jwt != null ? this$jwt.equals(other$jwt) : other$jwt == null;
    }

    public int hashCode() {
        Object $jwt = this.jwt;
        int result = (1 * 59) + ($jwt == null ? 43 : $jwt.hashCode());
        return result;
    }

    public String toString() {
        return "ServerToClientHandshakePacket(jwt=" + this.jwt + ")";
    }

    public String getJwt() {
        return this.jwt;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.SERVER_TO_CLIENT_HANDSHAKE;
    }
}
