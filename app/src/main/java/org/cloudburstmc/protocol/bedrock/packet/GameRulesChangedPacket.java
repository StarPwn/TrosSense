package org.cloudburstmc.protocol.bedrock.packet;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import org.cloudburstmc.protocol.bedrock.data.GameRuleData;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class GameRulesChangedPacket implements BedrockPacket {
    private final List<GameRuleData<?>> gameRules = new ObjectArrayList();

    protected boolean canEqual(Object other) {
        return other instanceof GameRulesChangedPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof GameRulesChangedPacket)) {
            return false;
        }
        GameRulesChangedPacket other = (GameRulesChangedPacket) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$gameRules = this.gameRules;
        Object other$gameRules = other.gameRules;
        return this$gameRules != null ? this$gameRules.equals(other$gameRules) : other$gameRules == null;
    }

    public int hashCode() {
        Object $gameRules = this.gameRules;
        int result = (1 * 59) + ($gameRules == null ? 43 : $gameRules.hashCode());
        return result;
    }

    public String toString() {
        return "GameRulesChangedPacket(gameRules=" + this.gameRules + ")";
    }

    public List<GameRuleData<?>> getGameRules() {
        return this.gameRules;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.GAME_RULES_CHANGED;
    }
}
