package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.bedrock.data.command.CommandOriginData;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class CommandRequestPacket implements BedrockPacket {
    private String command;
    private CommandOriginData commandOriginData;
    private boolean internal;
    private int version;

    public void setCommand(String command) {
        this.command = command;
    }

    public void setCommandOriginData(CommandOriginData commandOriginData) {
        this.commandOriginData = commandOriginData;
    }

    public void setInternal(boolean internal) {
        this.internal = internal;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    protected boolean canEqual(Object other) {
        return other instanceof CommandRequestPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CommandRequestPacket)) {
            return false;
        }
        CommandRequestPacket other = (CommandRequestPacket) o;
        if (!other.canEqual(this) || this.internal != other.internal || this.version != other.version) {
            return false;
        }
        Object this$command = this.command;
        Object other$command = other.command;
        if (this$command != null ? !this$command.equals(other$command) : other$command != null) {
            return false;
        }
        Object this$commandOriginData = this.commandOriginData;
        Object other$commandOriginData = other.commandOriginData;
        return this$commandOriginData != null ? this$commandOriginData.equals(other$commandOriginData) : other$commandOriginData == null;
    }

    public int hashCode() {
        int result = (1 * 59) + (this.internal ? 79 : 97);
        int result2 = (result * 59) + this.version;
        Object $command = this.command;
        int result3 = (result2 * 59) + ($command == null ? 43 : $command.hashCode());
        Object $commandOriginData = this.commandOriginData;
        return (result3 * 59) + ($commandOriginData != null ? $commandOriginData.hashCode() : 43);
    }

    public String toString() {
        return "CommandRequestPacket(command=" + this.command + ", commandOriginData=" + this.commandOriginData + ", internal=" + this.internal + ", version=" + this.version + ")";
    }

    public String getCommand() {
        return this.command;
    }

    public CommandOriginData getCommandOriginData() {
        return this.commandOriginData;
    }

    public boolean isInternal() {
        return this.internal;
    }

    public int getVersion() {
        return this.version;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.COMMAND_REQUEST;
    }
}
