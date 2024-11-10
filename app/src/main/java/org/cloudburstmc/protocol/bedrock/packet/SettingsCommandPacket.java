package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class SettingsCommandPacket implements BedrockPacket {
    private String command;
    private boolean suppressingOutput;

    public void setCommand(String command) {
        this.command = command;
    }

    public void setSuppressingOutput(boolean suppressingOutput) {
        this.suppressingOutput = suppressingOutput;
    }

    protected boolean canEqual(Object other) {
        return other instanceof SettingsCommandPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SettingsCommandPacket)) {
            return false;
        }
        SettingsCommandPacket other = (SettingsCommandPacket) o;
        if (!other.canEqual(this) || this.suppressingOutput != other.suppressingOutput) {
            return false;
        }
        Object this$command = this.command;
        Object other$command = other.command;
        return this$command != null ? this$command.equals(other$command) : other$command == null;
    }

    public int hashCode() {
        int result = (1 * 59) + (this.suppressingOutput ? 79 : 97);
        Object $command = this.command;
        return (result * 59) + ($command == null ? 43 : $command.hashCode());
    }

    public String toString() {
        return "SettingsCommandPacket(command=" + this.command + ", suppressingOutput=" + this.suppressingOutput + ")";
    }

    public String getCommand() {
        return this.command;
    }

    public boolean isSuppressingOutput() {
        return this.suppressingOutput;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.SETTINGS_COMMAND;
    }
}
