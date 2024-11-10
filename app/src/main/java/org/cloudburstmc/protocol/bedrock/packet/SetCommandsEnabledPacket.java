package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class SetCommandsEnabledPacket implements BedrockPacket {
    private boolean commandsEnabled;

    public void setCommandsEnabled(boolean commandsEnabled) {
        this.commandsEnabled = commandsEnabled;
    }

    protected boolean canEqual(Object other) {
        return other instanceof SetCommandsEnabledPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SetCommandsEnabledPacket)) {
            return false;
        }
        SetCommandsEnabledPacket other = (SetCommandsEnabledPacket) o;
        return other.canEqual(this) && this.commandsEnabled == other.commandsEnabled;
    }

    public int hashCode() {
        int result = (1 * 59) + (this.commandsEnabled ? 79 : 97);
        return result;
    }

    public String toString() {
        return "SetCommandsEnabledPacket(commandsEnabled=" + this.commandsEnabled + ")";
    }

    public boolean isCommandsEnabled() {
        return this.commandsEnabled;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.SET_COMMANDS_ENABLED;
    }
}
