package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.bedrock.data.camera.CameraFadeInstruction;
import org.cloudburstmc.protocol.bedrock.data.camera.CameraSetInstruction;
import org.cloudburstmc.protocol.common.PacketSignal;
import org.cloudburstmc.protocol.common.util.OptionalBoolean;

/* loaded from: classes5.dex */
public class CameraInstructionPacket implements BedrockPacket {
    private OptionalBoolean clear = OptionalBoolean.empty();
    private CameraFadeInstruction fadeInstruction;
    private CameraSetInstruction setInstruction;

    public void setFadeInstruction(CameraFadeInstruction fadeInstruction) {
        this.fadeInstruction = fadeInstruction;
    }

    public void setSetInstruction(CameraSetInstruction setInstruction) {
        this.setInstruction = setInstruction;
    }

    protected boolean canEqual(Object other) {
        return other instanceof CameraInstructionPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CameraInstructionPacket)) {
            return false;
        }
        CameraInstructionPacket other = (CameraInstructionPacket) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$setInstruction = this.setInstruction;
        Object other$setInstruction = other.setInstruction;
        if (this$setInstruction != null ? !this$setInstruction.equals(other$setInstruction) : other$setInstruction != null) {
            return false;
        }
        Object this$fadeInstruction = this.fadeInstruction;
        Object other$fadeInstruction = other.fadeInstruction;
        if (this$fadeInstruction != null ? !this$fadeInstruction.equals(other$fadeInstruction) : other$fadeInstruction != null) {
            return false;
        }
        Object this$clear = this.clear;
        Object other$clear = other.clear;
        return this$clear != null ? this$clear.equals(other$clear) : other$clear == null;
    }

    public int hashCode() {
        Object $setInstruction = this.setInstruction;
        int result = (1 * 59) + ($setInstruction == null ? 43 : $setInstruction.hashCode());
        Object $fadeInstruction = this.fadeInstruction;
        int result2 = (result * 59) + ($fadeInstruction == null ? 43 : $fadeInstruction.hashCode());
        Object $clear = this.clear;
        return (result2 * 59) + ($clear != null ? $clear.hashCode() : 43);
    }

    public String toString() {
        return "CameraInstructionPacket(setInstruction=" + this.setInstruction + ", fadeInstruction=" + this.fadeInstruction + ", clear=" + this.clear + ")";
    }

    public CameraSetInstruction getSetInstruction() {
        return this.setInstruction;
    }

    public CameraFadeInstruction getFadeInstruction() {
        return this.fadeInstruction;
    }

    public OptionalBoolean getClear() {
        return this.clear;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.CAMERA_INSTRUCTION;
    }

    public void setClear(boolean value) {
        this.clear = OptionalBoolean.of(value);
    }

    public void setClear(OptionalBoolean clear) {
        this.clear = clear;
    }
}
