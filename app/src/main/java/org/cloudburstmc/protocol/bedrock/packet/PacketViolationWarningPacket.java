package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.bedrock.data.PacketViolationSeverity;
import org.cloudburstmc.protocol.bedrock.data.PacketViolationType;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class PacketViolationWarningPacket implements BedrockPacket {
    private String context;
    private int packetCauseId;
    private PacketViolationSeverity severity;
    private PacketViolationType type;

    public void setContext(String context) {
        this.context = context;
    }

    public void setPacketCauseId(int packetCauseId) {
        this.packetCauseId = packetCauseId;
    }

    public void setSeverity(PacketViolationSeverity severity) {
        this.severity = severity;
    }

    public void setType(PacketViolationType type) {
        this.type = type;
    }

    protected boolean canEqual(Object other) {
        return other instanceof PacketViolationWarningPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PacketViolationWarningPacket)) {
            return false;
        }
        PacketViolationWarningPacket other = (PacketViolationWarningPacket) o;
        if (!other.canEqual(this) || this.packetCauseId != other.packetCauseId) {
            return false;
        }
        Object this$type = this.type;
        Object other$type = other.type;
        if (this$type != null ? !this$type.equals(other$type) : other$type != null) {
            return false;
        }
        Object this$severity = this.severity;
        Object other$severity = other.severity;
        if (this$severity != null ? !this$severity.equals(other$severity) : other$severity != null) {
            return false;
        }
        Object this$context = this.context;
        Object other$context = other.context;
        return this$context != null ? this$context.equals(other$context) : other$context == null;
    }

    public int hashCode() {
        int result = (1 * 59) + this.packetCauseId;
        Object $type = this.type;
        int result2 = (result * 59) + ($type == null ? 43 : $type.hashCode());
        Object $severity = this.severity;
        int result3 = (result2 * 59) + ($severity == null ? 43 : $severity.hashCode());
        Object $context = this.context;
        return (result3 * 59) + ($context != null ? $context.hashCode() : 43);
    }

    public String toString() {
        return "PacketViolationWarningPacket(type=" + this.type + ", severity=" + this.severity + ", packetCauseId=" + this.packetCauseId + ", context=" + this.context + ")";
    }

    public PacketViolationType getType() {
        return this.type;
    }

    public PacketViolationSeverity getSeverity() {
        return this.severity;
    }

    public int getPacketCauseId() {
        return this.packetCauseId;
    }

    public String getContext() {
        return this.context;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.PACKET_VIOLATION_WARNING;
    }
}
