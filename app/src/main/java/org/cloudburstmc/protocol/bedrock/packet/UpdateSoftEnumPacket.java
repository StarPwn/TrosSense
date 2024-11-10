package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.bedrock.data.command.CommandEnumData;
import org.cloudburstmc.protocol.bedrock.data.command.SoftEnumUpdateType;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class UpdateSoftEnumPacket implements BedrockPacket {
    private CommandEnumData softEnum;
    private SoftEnumUpdateType type;

    public void setSoftEnum(CommandEnumData softEnum) {
        this.softEnum = softEnum;
    }

    public void setType(SoftEnumUpdateType type) {
        this.type = type;
    }

    protected boolean canEqual(Object other) {
        return other instanceof UpdateSoftEnumPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UpdateSoftEnumPacket)) {
            return false;
        }
        UpdateSoftEnumPacket other = (UpdateSoftEnumPacket) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$softEnum = this.softEnum;
        Object other$softEnum = other.softEnum;
        if (this$softEnum != null ? !this$softEnum.equals(other$softEnum) : other$softEnum != null) {
            return false;
        }
        Object this$type = this.type;
        Object other$type = other.type;
        return this$type != null ? this$type.equals(other$type) : other$type == null;
    }

    public int hashCode() {
        Object $softEnum = this.softEnum;
        int result = (1 * 59) + ($softEnum == null ? 43 : $softEnum.hashCode());
        Object $type = this.type;
        return (result * 59) + ($type != null ? $type.hashCode() : 43);
    }

    public String toString() {
        return "UpdateSoftEnumPacket(softEnum=" + this.softEnum + ", type=" + this.type + ")";
    }

    public CommandEnumData getSoftEnum() {
        return this.softEnum;
    }

    public SoftEnumUpdateType getType() {
        return this.type;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.UPDATE_SOFT_ENUM;
    }
}
