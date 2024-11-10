package org.cloudburstmc.protocol.bedrock.packet;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import org.cloudburstmc.protocol.bedrock.data.command.CommandOriginData;
import org.cloudburstmc.protocol.bedrock.data.command.CommandOutputMessage;
import org.cloudburstmc.protocol.bedrock.data.command.CommandOutputType;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class CommandOutputPacket implements BedrockPacket {
    private CommandOriginData commandOriginData;
    private String data;
    private final List<CommandOutputMessage> messages = new ObjectArrayList();
    private int successCount;
    private CommandOutputType type;

    public void setCommandOriginData(CommandOriginData commandOriginData) {
        this.commandOriginData = commandOriginData;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }

    public void setType(CommandOutputType type) {
        this.type = type;
    }

    protected boolean canEqual(Object other) {
        return other instanceof CommandOutputPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CommandOutputPacket)) {
            return false;
        }
        CommandOutputPacket other = (CommandOutputPacket) o;
        if (!other.canEqual(this) || this.successCount != other.successCount) {
            return false;
        }
        Object this$messages = this.messages;
        Object other$messages = other.messages;
        if (this$messages != null ? !this$messages.equals(other$messages) : other$messages != null) {
            return false;
        }
        Object this$commandOriginData = this.commandOriginData;
        Object other$commandOriginData = other.commandOriginData;
        if (this$commandOriginData != null ? !this$commandOriginData.equals(other$commandOriginData) : other$commandOriginData != null) {
            return false;
        }
        Object this$type = this.type;
        Object other$type = other.type;
        if (this$type != null ? !this$type.equals(other$type) : other$type != null) {
            return false;
        }
        Object this$data = this.data;
        Object other$data = other.data;
        return this$data != null ? this$data.equals(other$data) : other$data == null;
    }

    public int hashCode() {
        int result = (1 * 59) + this.successCount;
        Object $messages = this.messages;
        int result2 = (result * 59) + ($messages == null ? 43 : $messages.hashCode());
        Object $commandOriginData = this.commandOriginData;
        int result3 = (result2 * 59) + ($commandOriginData == null ? 43 : $commandOriginData.hashCode());
        Object $type = this.type;
        int result4 = (result3 * 59) + ($type == null ? 43 : $type.hashCode());
        Object $data = this.data;
        return (result4 * 59) + ($data != null ? $data.hashCode() : 43);
    }

    public String toString() {
        return "CommandOutputPacket(messages=" + this.messages + ", commandOriginData=" + this.commandOriginData + ", type=" + this.type + ", successCount=" + this.successCount + ", data=" + this.data + ")";
    }

    public List<CommandOutputMessage> getMessages() {
        return this.messages;
    }

    public CommandOriginData getCommandOriginData() {
        return this.commandOriginData;
    }

    public CommandOutputType getType() {
        return this.type;
    }

    public int getSuccessCount() {
        return this.successCount;
    }

    public String getData() {
        return this.data;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.COMMAND_OUTPUT;
    }
}
