package org.cloudburstmc.protocol.bedrock.packet;

import java.util.Optional;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class SpawnParticleEffectPacket implements BedrockPacket {
    private int dimensionId;
    private String identifier;
    private Optional<String> molangVariablesJson;
    private Vector3f position;
    private long uniqueEntityId = -1;

    public void setDimensionId(int dimensionId) {
        this.dimensionId = dimensionId;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public void setMolangVariablesJson(Optional<String> molangVariablesJson) {
        this.molangVariablesJson = molangVariablesJson;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void setUniqueEntityId(long uniqueEntityId) {
        this.uniqueEntityId = uniqueEntityId;
    }

    protected boolean canEqual(Object other) {
        return other instanceof SpawnParticleEffectPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SpawnParticleEffectPacket)) {
            return false;
        }
        SpawnParticleEffectPacket other = (SpawnParticleEffectPacket) o;
        if (!other.canEqual(this) || this.dimensionId != other.dimensionId || this.uniqueEntityId != other.uniqueEntityId) {
            return false;
        }
        Object this$position = this.position;
        Object other$position = other.position;
        if (this$position != null ? !this$position.equals(other$position) : other$position != null) {
            return false;
        }
        Object this$identifier = this.identifier;
        Object other$identifier = other.identifier;
        if (this$identifier != null ? !this$identifier.equals(other$identifier) : other$identifier != null) {
            return false;
        }
        Object this$molangVariablesJson = this.molangVariablesJson;
        Object other$molangVariablesJson = other.molangVariablesJson;
        return this$molangVariablesJson != null ? this$molangVariablesJson.equals(other$molangVariablesJson) : other$molangVariablesJson == null;
    }

    public int hashCode() {
        int result = (1 * 59) + this.dimensionId;
        long $uniqueEntityId = this.uniqueEntityId;
        int result2 = (result * 59) + ((int) (($uniqueEntityId >>> 32) ^ $uniqueEntityId));
        Object $position = this.position;
        int result3 = (result2 * 59) + ($position == null ? 43 : $position.hashCode());
        Object $identifier = this.identifier;
        int result4 = (result3 * 59) + ($identifier == null ? 43 : $identifier.hashCode());
        Object $molangVariablesJson = this.molangVariablesJson;
        return (result4 * 59) + ($molangVariablesJson != null ? $molangVariablesJson.hashCode() : 43);
    }

    public String toString() {
        return "SpawnParticleEffectPacket(dimensionId=" + this.dimensionId + ", uniqueEntityId=" + this.uniqueEntityId + ", position=" + this.position + ", identifier=" + this.identifier + ", molangVariablesJson=" + this.molangVariablesJson + ")";
    }

    public int getDimensionId() {
        return this.dimensionId;
    }

    public long getUniqueEntityId() {
        return this.uniqueEntityId;
    }

    public Vector3f getPosition() {
        return this.position;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public Optional<String> getMolangVariablesJson() {
        return this.molangVariablesJson;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.SPAWN_PARTICLE_EFFECT;
    }
}
