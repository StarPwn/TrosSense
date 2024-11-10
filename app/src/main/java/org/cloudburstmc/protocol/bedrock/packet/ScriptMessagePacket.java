package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class ScriptMessagePacket implements BedrockPacket {
    private String channel;
    private String message;

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    protected boolean canEqual(Object other) {
        return other instanceof ScriptMessagePacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ScriptMessagePacket)) {
            return false;
        }
        ScriptMessagePacket other = (ScriptMessagePacket) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$channel = this.channel;
        Object other$channel = other.channel;
        if (this$channel != null ? !this$channel.equals(other$channel) : other$channel != null) {
            return false;
        }
        Object this$message = this.message;
        Object other$message = other.message;
        return this$message != null ? this$message.equals(other$message) : other$message == null;
    }

    public int hashCode() {
        Object $channel = this.channel;
        int result = (1 * 59) + ($channel == null ? 43 : $channel.hashCode());
        Object $message = this.message;
        return (result * 59) + ($message != null ? $message.hashCode() : 43);
    }

    public String toString() {
        return "ScriptMessagePacket(channel=" + this.channel + ", message=" + this.message + ")";
    }

    public String getChannel() {
        return this.channel;
    }

    public String getMessage() {
        return this.message;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.SCRIPT_MESSAGE;
    }
}
