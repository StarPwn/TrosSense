package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class DebugInfoPacket implements BedrockPacket {
    private String data;
    private long uniqueEntityId;

    public void setData(String data) {
        this.data = data;
    }

    public void setUniqueEntityId(long uniqueEntityId) {
        this.uniqueEntityId = uniqueEntityId;
    }

    protected boolean canEqual(Object other) {
        return other instanceof DebugInfoPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DebugInfoPacket)) {
            return false;
        }
        DebugInfoPacket other = (DebugInfoPacket) o;
        if (!other.canEqual(this) || this.uniqueEntityId != other.uniqueEntityId) {
            return false;
        }
        Object this$data = this.data;
        Object other$data = other.data;
        return this$data != null ? this$data.equals(other$data) : other$data == null;
    }

    public int hashCode() {
        long $uniqueEntityId = this.uniqueEntityId;
        int result = (1 * 59) + ((int) (($uniqueEntityId >>> 32) ^ $uniqueEntityId));
        Object $data = this.data;
        return (result * 59) + ($data == null ? 43 : $data.hashCode());
    }

    public String toString() {
        return "DebugInfoPacket(uniqueEntityId=" + this.uniqueEntityId + ", data=" + this.data + ")";
    }

    public long getUniqueEntityId() {
        return this.uniqueEntityId;
    }

    public String getData() {
        return this.data;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.DEBUG_INFO;
    }
}
