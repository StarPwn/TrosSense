package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class SetDefaultGameTypePacket implements BedrockPacket {
    private int gamemode;

    public void setGamemode(int gamemode) {
        this.gamemode = gamemode;
    }

    protected boolean canEqual(Object other) {
        return other instanceof SetDefaultGameTypePacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SetDefaultGameTypePacket)) {
            return false;
        }
        SetDefaultGameTypePacket other = (SetDefaultGameTypePacket) o;
        return other.canEqual(this) && this.gamemode == other.gamemode;
    }

    public int hashCode() {
        int result = (1 * 59) + this.gamemode;
        return result;
    }

    public String toString() {
        return "SetDefaultGameTypePacket(gamemode=" + this.gamemode + ")";
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
        return BedrockPacketType.SET_DEFAULT_GAME_TYPE;
    }
}
