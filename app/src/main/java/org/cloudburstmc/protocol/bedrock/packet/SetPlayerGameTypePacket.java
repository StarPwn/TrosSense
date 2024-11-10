package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class SetPlayerGameTypePacket implements BedrockPacket {
    private int gamemode;

    public void setGamemode(int gamemode) {
        this.gamemode = gamemode;
    }

    protected boolean canEqual(Object other) {
        return other instanceof SetPlayerGameTypePacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SetPlayerGameTypePacket)) {
            return false;
        }
        SetPlayerGameTypePacket other = (SetPlayerGameTypePacket) o;
        return other.canEqual(this) && this.gamemode == other.gamemode;
    }

    public int hashCode() {
        int result = (1 * 59) + this.gamemode;
        return result;
    }

    public String toString() {
        return "SetPlayerGameTypePacket(gamemode=" + this.gamemode + ")";
    }

    public int getGamemode() {
        return this.gamemode;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.SET_PLAYER_GAME_TYPE;
    }
}
