package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.bedrock.data.DisconnectFailReason;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class DisconnectPacket implements BedrockPacket {
    private String kickMessage;
    private boolean messageSkipped;
    private DisconnectFailReason reason = DisconnectFailReason.UNKNOWN;

    public void setKickMessage(String kickMessage) {
        this.kickMessage = kickMessage;
    }

    public void setMessageSkipped(boolean messageSkipped) {
        this.messageSkipped = messageSkipped;
    }

    public void setReason(DisconnectFailReason reason) {
        this.reason = reason;
    }

    protected boolean canEqual(Object other) {
        return other instanceof DisconnectPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DisconnectPacket)) {
            return false;
        }
        DisconnectPacket other = (DisconnectPacket) o;
        if (!other.canEqual(this) || this.messageSkipped != other.messageSkipped) {
            return false;
        }
        Object this$reason = this.reason;
        Object other$reason = other.reason;
        if (this$reason != null ? !this$reason.equals(other$reason) : other$reason != null) {
            return false;
        }
        Object this$kickMessage = this.kickMessage;
        Object other$kickMessage = other.kickMessage;
        return this$kickMessage != null ? this$kickMessage.equals(other$kickMessage) : other$kickMessage == null;
    }

    public int hashCode() {
        int result = (1 * 59) + (this.messageSkipped ? 79 : 97);
        Object $reason = this.reason;
        int result2 = (result * 59) + ($reason == null ? 43 : $reason.hashCode());
        Object $kickMessage = this.kickMessage;
        return (result2 * 59) + ($kickMessage != null ? $kickMessage.hashCode() : 43);
    }

    public String toString() {
        return "DisconnectPacket(reason=" + this.reason + ", messageSkipped=" + this.messageSkipped + ", kickMessage=" + this.kickMessage + ")";
    }

    public DisconnectFailReason getReason() {
        return this.reason;
    }

    public boolean isMessageSkipped() {
        return this.messageSkipped;
    }

    public String getKickMessage() {
        return this.kickMessage;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.DISCONNECT;
    }
}
