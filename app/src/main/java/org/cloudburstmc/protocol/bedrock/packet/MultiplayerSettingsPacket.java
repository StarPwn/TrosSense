package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.bedrock.data.MultiplayerMode;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class MultiplayerSettingsPacket implements BedrockPacket {
    private MultiplayerMode mode;

    public void setMode(MultiplayerMode mode) {
        this.mode = mode;
    }

    protected boolean canEqual(Object other) {
        return other instanceof MultiplayerSettingsPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MultiplayerSettingsPacket)) {
            return false;
        }
        MultiplayerSettingsPacket other = (MultiplayerSettingsPacket) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$mode = this.mode;
        Object other$mode = other.mode;
        return this$mode != null ? this$mode.equals(other$mode) : other$mode == null;
    }

    public int hashCode() {
        Object $mode = this.mode;
        int result = (1 * 59) + ($mode == null ? 43 : $mode.hashCode());
        return result;
    }

    public String toString() {
        return "MultiplayerSettingsPacket(mode=" + this.mode + ")";
    }

    public MultiplayerMode getMode() {
        return this.mode;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.MULTIPLAYER_SETTINGS;
    }
}
