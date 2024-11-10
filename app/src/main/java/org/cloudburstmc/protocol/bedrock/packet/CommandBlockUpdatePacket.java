package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.protocol.bedrock.data.CommandBlockMode;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class CommandBlockUpdatePacket implements BedrockPacket {
    private boolean block;
    private Vector3i blockPosition;
    private String command;
    private boolean conditional;
    private boolean executingOnFirstTick;
    private String lastOutput;
    private long minecartRuntimeEntityId;
    private CommandBlockMode mode;
    private String name;
    private boolean outputTracked;
    private boolean redstoneMode;
    private long tickDelay;

    public void setBlock(boolean block) {
        this.block = block;
    }

    public void setBlockPosition(Vector3i blockPosition) {
        this.blockPosition = blockPosition;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setConditional(boolean conditional) {
        this.conditional = conditional;
    }

    public void setExecutingOnFirstTick(boolean executingOnFirstTick) {
        this.executingOnFirstTick = executingOnFirstTick;
    }

    public void setLastOutput(String lastOutput) {
        this.lastOutput = lastOutput;
    }

    public void setMinecartRuntimeEntityId(long minecartRuntimeEntityId) {
        this.minecartRuntimeEntityId = minecartRuntimeEntityId;
    }

    public void setMode(CommandBlockMode mode) {
        this.mode = mode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOutputTracked(boolean outputTracked) {
        this.outputTracked = outputTracked;
    }

    public void setRedstoneMode(boolean redstoneMode) {
        this.redstoneMode = redstoneMode;
    }

    public void setTickDelay(long tickDelay) {
        this.tickDelay = tickDelay;
    }

    protected boolean canEqual(Object other) {
        return other instanceof CommandBlockUpdatePacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CommandBlockUpdatePacket)) {
            return false;
        }
        CommandBlockUpdatePacket other = (CommandBlockUpdatePacket) o;
        if (!other.canEqual(this) || this.block != other.block || this.redstoneMode != other.redstoneMode || this.conditional != other.conditional || this.minecartRuntimeEntityId != other.minecartRuntimeEntityId || this.outputTracked != other.outputTracked || this.tickDelay != other.tickDelay || this.executingOnFirstTick != other.executingOnFirstTick) {
            return false;
        }
        Object this$blockPosition = this.blockPosition;
        Object other$blockPosition = other.blockPosition;
        if (this$blockPosition != null ? !this$blockPosition.equals(other$blockPosition) : other$blockPosition != null) {
            return false;
        }
        Object this$mode = this.mode;
        Object other$mode = other.mode;
        if (this$mode != null ? !this$mode.equals(other$mode) : other$mode != null) {
            return false;
        }
        Object this$command = this.command;
        Object other$command = other.command;
        if (this$command != null ? !this$command.equals(other$command) : other$command != null) {
            return false;
        }
        Object this$lastOutput = this.lastOutput;
        Object other$lastOutput = other.lastOutput;
        if (this$lastOutput != null ? !this$lastOutput.equals(other$lastOutput) : other$lastOutput != null) {
            return false;
        }
        Object this$name = this.name;
        Object other$name = other.name;
        return this$name != null ? this$name.equals(other$name) : other$name == null;
    }

    public int hashCode() {
        int result = (1 * 59) + (this.block ? 79 : 97);
        int result2 = ((result * 59) + (this.redstoneMode ? 79 : 97)) * 59;
        int i = this.conditional ? 79 : 97;
        long $minecartRuntimeEntityId = this.minecartRuntimeEntityId;
        int result3 = ((((result2 + i) * 59) + ((int) (($minecartRuntimeEntityId >>> 32) ^ $minecartRuntimeEntityId))) * 59) + (this.outputTracked ? 79 : 97);
        long $tickDelay = this.tickDelay;
        int result4 = ((result3 * 59) + ((int) (($tickDelay >>> 32) ^ $tickDelay))) * 59;
        int i2 = this.executingOnFirstTick ? 79 : 97;
        Object $blockPosition = this.blockPosition;
        int result5 = ((result4 + i2) * 59) + ($blockPosition == null ? 43 : $blockPosition.hashCode());
        Object $mode = this.mode;
        int result6 = (result5 * 59) + ($mode == null ? 43 : $mode.hashCode());
        Object $command = this.command;
        int result7 = (result6 * 59) + ($command == null ? 43 : $command.hashCode());
        Object $lastOutput = this.lastOutput;
        int result8 = (result7 * 59) + ($lastOutput == null ? 43 : $lastOutput.hashCode());
        Object $name = this.name;
        return (result8 * 59) + ($name != null ? $name.hashCode() : 43);
    }

    public String toString() {
        return "CommandBlockUpdatePacket(block=" + this.block + ", blockPosition=" + this.blockPosition + ", mode=" + this.mode + ", redstoneMode=" + this.redstoneMode + ", conditional=" + this.conditional + ", minecartRuntimeEntityId=" + this.minecartRuntimeEntityId + ", command=" + this.command + ", lastOutput=" + this.lastOutput + ", name=" + this.name + ", outputTracked=" + this.outputTracked + ", tickDelay=" + this.tickDelay + ", executingOnFirstTick=" + this.executingOnFirstTick + ")";
    }

    public boolean isBlock() {
        return this.block;
    }

    public Vector3i getBlockPosition() {
        return this.blockPosition;
    }

    public CommandBlockMode getMode() {
        return this.mode;
    }

    public boolean isRedstoneMode() {
        return this.redstoneMode;
    }

    public boolean isConditional() {
        return this.conditional;
    }

    public long getMinecartRuntimeEntityId() {
        return this.minecartRuntimeEntityId;
    }

    public String getCommand() {
        return this.command;
    }

    public String getLastOutput() {
        return this.lastOutput;
    }

    public String getName() {
        return this.name;
    }

    public boolean isOutputTracked() {
        return this.outputTracked;
    }

    public long getTickDelay() {
        return this.tickDelay;
    }

    public boolean isExecutingOnFirstTick() {
        return this.executingOnFirstTick;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.COMMAND_BLOCK_UPDATE;
    }
}
