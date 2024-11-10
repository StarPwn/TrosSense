package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.math.vector.Vector2f;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class PlayerInputPacket implements BedrockPacket {
    private Vector2f inputMotion;
    private boolean jumping;
    private boolean sneaking;

    public void setInputMotion(Vector2f inputMotion) {
        this.inputMotion = inputMotion;
    }

    public void setJumping(boolean jumping) {
        this.jumping = jumping;
    }

    public void setSneaking(boolean sneaking) {
        this.sneaking = sneaking;
    }

    protected boolean canEqual(Object other) {
        return other instanceof PlayerInputPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PlayerInputPacket)) {
            return false;
        }
        PlayerInputPacket other = (PlayerInputPacket) o;
        if (!other.canEqual(this) || this.jumping != other.jumping || this.sneaking != other.sneaking) {
            return false;
        }
        Object this$inputMotion = this.inputMotion;
        Object other$inputMotion = other.inputMotion;
        return this$inputMotion != null ? this$inputMotion.equals(other$inputMotion) : other$inputMotion == null;
    }

    public int hashCode() {
        int result = (1 * 59) + (this.jumping ? 79 : 97);
        int result2 = result * 59;
        int i = this.sneaking ? 79 : 97;
        Object $inputMotion = this.inputMotion;
        return ((result2 + i) * 59) + ($inputMotion == null ? 43 : $inputMotion.hashCode());
    }

    public String toString() {
        return "PlayerInputPacket(inputMotion=" + this.inputMotion + ", jumping=" + this.jumping + ", sneaking=" + this.sneaking + ")";
    }

    public Vector2f getInputMotion() {
        return this.inputMotion;
    }

    public boolean isJumping() {
        return this.jumping;
    }

    public boolean isSneaking() {
        return this.sneaking;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.PLAYER_INPUT;
    }
}
