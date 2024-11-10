package org.cloudburstmc.protocol.bedrock.packet;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import org.cloudburstmc.protocol.bedrock.data.command.CommandData;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class AvailableCommandsPacket implements BedrockPacket {
    private final List<CommandData> commands = new ObjectArrayList();

    protected boolean canEqual(Object other) {
        return other instanceof AvailableCommandsPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AvailableCommandsPacket)) {
            return false;
        }
        AvailableCommandsPacket other = (AvailableCommandsPacket) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$commands = this.commands;
        Object other$commands = other.commands;
        return this$commands != null ? this$commands.equals(other$commands) : other$commands == null;
    }

    public int hashCode() {
        Object $commands = this.commands;
        int result = (1 * 59) + ($commands == null ? 43 : $commands.hashCode());
        return result;
    }

    public String toString() {
        return "AvailableCommandsPacket(commands=" + this.commands + ")";
    }

    public List<CommandData> getCommands() {
        return this.commands;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.AVAILABLE_COMMANDS;
    }
}
