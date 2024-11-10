package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class SpawnExperienceOrbPacket implements BedrockPacket {
    private int amount;
    private Vector3f position;

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    protected boolean canEqual(Object other) {
        return other instanceof SpawnExperienceOrbPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SpawnExperienceOrbPacket)) {
            return false;
        }
        SpawnExperienceOrbPacket other = (SpawnExperienceOrbPacket) o;
        if (!other.canEqual(this) || this.amount != other.amount) {
            return false;
        }
        Object this$position = this.position;
        Object other$position = other.position;
        return this$position != null ? this$position.equals(other$position) : other$position == null;
    }

    public int hashCode() {
        int result = (1 * 59) + this.amount;
        Object $position = this.position;
        return (result * 59) + ($position == null ? 43 : $position.hashCode());
    }

    public String toString() {
        return "SpawnExperienceOrbPacket(position=" + this.position + ", amount=" + this.amount + ")";
    }

    public Vector3f getPosition() {
        return this.position;
    }

    public int getAmount() {
        return this.amount;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.SPAWN_EXPERIENCE_ORB;
    }
}
