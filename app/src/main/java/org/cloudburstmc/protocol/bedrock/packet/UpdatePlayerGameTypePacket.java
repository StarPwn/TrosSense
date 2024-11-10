package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.bedrock.data.GameType;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class UpdatePlayerGameTypePacket implements BedrockPacket {
    private long entityId;
    private GameType gameType;

    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    protected boolean canEqual(Object other) {
        return other instanceof UpdatePlayerGameTypePacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UpdatePlayerGameTypePacket)) {
            return false;
        }
        UpdatePlayerGameTypePacket other = (UpdatePlayerGameTypePacket) o;
        if (!other.canEqual(this) || getEntityId() != other.getEntityId()) {
            return false;
        }
        Object this$gameType = getGameType();
        Object other$gameType = other.getGameType();
        return this$gameType != null ? this$gameType.equals(other$gameType) : other$gameType == null;
    }

    public int hashCode() {
        long $entityId = getEntityId();
        int result = (1 * 59) + ((int) (($entityId >>> 32) ^ $entityId));
        Object $gameType = getGameType();
        return (result * 59) + ($gameType == null ? 43 : $gameType.hashCode());
    }

    public String toString() {
        return "UpdatePlayerGameTypePacket(gameType=" + this.gameType + ", entityId=" + this.entityId + ")";
    }

    public GameType getGameType() {
        return this.gameType;
    }

    public long getEntityId() {
        return this.entityId;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.UPDATE_PLAYER_GAME_TYPE;
    }
}
