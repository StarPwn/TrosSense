package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class SetLocalPlayerAsInitializedPacket implements BedrockPacket {
    private long runtimeEntityId;

    public void setRuntimeEntityId(long runtimeEntityId) {
        this.runtimeEntityId = runtimeEntityId;
    }

    protected boolean canEqual(Object other) {
        return other instanceof SetLocalPlayerAsInitializedPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SetLocalPlayerAsInitializedPacket)) {
            return false;
        }
        SetLocalPlayerAsInitializedPacket other = (SetLocalPlayerAsInitializedPacket) o;
        return other.canEqual(this) && this.runtimeEntityId == other.runtimeEntityId;
    }

    public int hashCode() {
        long $runtimeEntityId = this.runtimeEntityId;
        int result = (1 * 59) + ((int) (($runtimeEntityId >>> 32) ^ $runtimeEntityId));
        return result;
    }

    public String toString() {
        return "SetLocalPlayerAsInitializedPacket(runtimeEntityId=" + this.runtimeEntityId + ")";
    }

    public long getRuntimeEntityId() {
        return this.runtimeEntityId;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.SET_LOCAL_PLAYER_AS_INITIALIZED;
    }
}
