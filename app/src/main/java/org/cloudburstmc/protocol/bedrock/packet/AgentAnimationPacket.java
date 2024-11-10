package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class AgentAnimationPacket implements BedrockPacket {
    private byte animation;
    private long runtimeEntityId;

    public void setAnimation(byte animation) {
        this.animation = animation;
    }

    public void setRuntimeEntityId(long runtimeEntityId) {
        this.runtimeEntityId = runtimeEntityId;
    }

    protected boolean canEqual(Object other) {
        return other instanceof AgentAnimationPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AgentAnimationPacket)) {
            return false;
        }
        AgentAnimationPacket other = (AgentAnimationPacket) o;
        return other.canEqual(this) && this.animation == other.animation && this.runtimeEntityId == other.runtimeEntityId;
    }

    public int hashCode() {
        int result = (1 * 59) + this.animation;
        long $runtimeEntityId = this.runtimeEntityId;
        return (result * 59) + ((int) (($runtimeEntityId >>> 32) ^ $runtimeEntityId));
    }

    public String toString() {
        return "AgentAnimationPacket(animation=" + ((int) this.animation) + ", runtimeEntityId=" + this.runtimeEntityId + ")";
    }

    public byte getAnimation() {
        return this.animation;
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
        return BedrockPacketType.AGENT_ANIMATION;
    }
}
