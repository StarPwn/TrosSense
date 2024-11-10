package org.cloudburstmc.protocol.bedrock.packet;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class PlayerFogPacket implements BedrockPacket {
    private final List<String> fogStack = new ObjectArrayList();

    protected boolean canEqual(Object other) {
        return other instanceof PlayerFogPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PlayerFogPacket)) {
            return false;
        }
        PlayerFogPacket other = (PlayerFogPacket) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$fogStack = this.fogStack;
        Object other$fogStack = other.fogStack;
        return this$fogStack != null ? this$fogStack.equals(other$fogStack) : other$fogStack == null;
    }

    public int hashCode() {
        Object $fogStack = this.fogStack;
        int result = (1 * 59) + ($fogStack == null ? 43 : $fogStack.hashCode());
        return result;
    }

    public String toString() {
        return "PlayerFogPacket(fogStack=" + this.fogStack + ")";
    }

    public List<String> getFogStack() {
        return this.fogStack;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.PLAYER_FOG;
    }
}
