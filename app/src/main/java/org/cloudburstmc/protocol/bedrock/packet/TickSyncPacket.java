package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class TickSyncPacket implements BedrockPacket {
    private long requestTimestamp;
    private long responseTimestamp;

    public void setRequestTimestamp(long requestTimestamp) {
        this.requestTimestamp = requestTimestamp;
    }

    public void setResponseTimestamp(long responseTimestamp) {
        this.responseTimestamp = responseTimestamp;
    }

    protected boolean canEqual(Object other) {
        return other instanceof TickSyncPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TickSyncPacket)) {
            return false;
        }
        TickSyncPacket other = (TickSyncPacket) o;
        return other.canEqual(this) && this.requestTimestamp == other.requestTimestamp && this.responseTimestamp == other.responseTimestamp;
    }

    public int hashCode() {
        long $requestTimestamp = this.requestTimestamp;
        int result = (1 * 59) + ((int) (($requestTimestamp >>> 32) ^ $requestTimestamp));
        long $responseTimestamp = this.responseTimestamp;
        return (result * 59) + ((int) (($responseTimestamp >>> 32) ^ $responseTimestamp));
    }

    public String toString() {
        return "TickSyncPacket(requestTimestamp=" + this.requestTimestamp + ", responseTimestamp=" + this.responseTimestamp + ")";
    }

    public long getRequestTimestamp() {
        return this.requestTimestamp;
    }

    public long getResponseTimestamp() {
        return this.responseTimestamp;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.TICK_SYNC;
    }
}
