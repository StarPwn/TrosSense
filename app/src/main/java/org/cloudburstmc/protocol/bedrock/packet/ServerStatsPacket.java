package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class ServerStatsPacket implements BedrockPacket {
    private float networkTime;
    private float serverTime;

    public void setNetworkTime(float networkTime) {
        this.networkTime = networkTime;
    }

    public void setServerTime(float serverTime) {
        this.serverTime = serverTime;
    }

    protected boolean canEqual(Object other) {
        return other instanceof ServerStatsPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ServerStatsPacket)) {
            return false;
        }
        ServerStatsPacket other = (ServerStatsPacket) o;
        return other.canEqual(this) && Float.compare(this.serverTime, other.serverTime) == 0 && Float.compare(this.networkTime, other.networkTime) == 0;
    }

    public int hashCode() {
        int result = (1 * 59) + Float.floatToIntBits(this.serverTime);
        return (result * 59) + Float.floatToIntBits(this.networkTime);
    }

    public String toString() {
        return "ServerStatsPacket(serverTime=" + this.serverTime + ", networkTime=" + this.networkTime + ")";
    }

    public float getServerTime() {
        return this.serverTime;
    }

    public float getNetworkTime() {
        return this.networkTime;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.SERVER_STATS;
    }
}
