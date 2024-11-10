package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class NetworkStackLatencyPacket implements BedrockPacket {
    private boolean fromServer;
    private long timestamp;

    public void setFromServer(boolean fromServer) {
        this.fromServer = fromServer;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    protected boolean canEqual(Object other) {
        return other instanceof NetworkStackLatencyPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof NetworkStackLatencyPacket)) {
            return false;
        }
        NetworkStackLatencyPacket other = (NetworkStackLatencyPacket) o;
        return other.canEqual(this) && this.timestamp == other.timestamp && this.fromServer == other.fromServer;
    }

    public int hashCode() {
        long $timestamp = this.timestamp;
        int result = (1 * 59) + ((int) (($timestamp >>> 32) ^ $timestamp));
        return (result * 59) + (this.fromServer ? 79 : 97);
    }

    public String toString() {
        return "NetworkStackLatencyPacket(timestamp=" + this.timestamp + ", fromServer=" + this.fromServer + ")";
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public boolean isFromServer() {
        return this.fromServer;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.NETWORK_STACK_LATENCY;
    }
}
