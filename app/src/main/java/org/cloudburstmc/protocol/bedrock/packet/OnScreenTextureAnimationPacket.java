package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class OnScreenTextureAnimationPacket implements BedrockPacket {
    private long effectId;

    public void setEffectId(long effectId) {
        this.effectId = effectId;
    }

    protected boolean canEqual(Object other) {
        return other instanceof OnScreenTextureAnimationPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof OnScreenTextureAnimationPacket)) {
            return false;
        }
        OnScreenTextureAnimationPacket other = (OnScreenTextureAnimationPacket) o;
        return other.canEqual(this) && this.effectId == other.effectId;
    }

    public int hashCode() {
        long $effectId = this.effectId;
        int result = (1 * 59) + ((int) (($effectId >>> 32) ^ $effectId));
        return result;
    }

    public String toString() {
        return "OnScreenTextureAnimationPacket(effectId=" + this.effectId + ")";
    }

    public long getEffectId() {
        return this.effectId;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.ON_SCREEN_TEXTURE_ANIMATION;
    }
}
