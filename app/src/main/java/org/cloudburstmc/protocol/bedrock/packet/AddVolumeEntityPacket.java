package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class AddVolumeEntityPacket implements BedrockPacket {
    private NbtMap data;
    private int dimension;
    private String engineVersion;
    private int id;
    private String identifier;
    private String instanceName;
    private Vector3i maxBounds;
    private Vector3i minBounds;

    public void setData(NbtMap data) {
        this.data = data;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    public void setEngineVersion(String engineVersion) {
        this.engineVersion = engineVersion;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public void setMaxBounds(Vector3i maxBounds) {
        this.maxBounds = maxBounds;
    }

    public void setMinBounds(Vector3i minBounds) {
        this.minBounds = minBounds;
    }

    public String toString() {
        return "AddVolumeEntityPacket(id=" + getId() + ", data=" + getData() + ", engineVersion=" + getEngineVersion() + ", identifier=" + getIdentifier() + ", instanceName=" + getInstanceName() + ", minBounds=" + getMinBounds() + ", maxBounds=" + getMaxBounds() + ", dimension=" + getDimension() + ")";
    }

    protected boolean canEqual(Object other) {
        return other instanceof AddVolumeEntityPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AddVolumeEntityPacket)) {
            return false;
        }
        AddVolumeEntityPacket other = (AddVolumeEntityPacket) o;
        if (!other.canEqual(this) || this.id != other.id || this.dimension != other.dimension) {
            return false;
        }
        Object this$data = this.data;
        Object other$data = other.data;
        if (this$data != null ? !this$data.equals(other$data) : other$data != null) {
            return false;
        }
        Object this$engineVersion = this.engineVersion;
        Object other$engineVersion = other.engineVersion;
        if (this$engineVersion != null ? !this$engineVersion.equals(other$engineVersion) : other$engineVersion != null) {
            return false;
        }
        Object this$identifier = this.identifier;
        Object other$identifier = other.identifier;
        if (this$identifier != null ? !this$identifier.equals(other$identifier) : other$identifier != null) {
            return false;
        }
        Object this$instanceName = this.instanceName;
        Object other$instanceName = other.instanceName;
        if (this$instanceName != null ? !this$instanceName.equals(other$instanceName) : other$instanceName != null) {
            return false;
        }
        Object this$minBounds = this.minBounds;
        Object other$minBounds = other.minBounds;
        if (this$minBounds != null ? !this$minBounds.equals(other$minBounds) : other$minBounds != null) {
            return false;
        }
        Object this$maxBounds = this.maxBounds;
        Object other$maxBounds = other.maxBounds;
        if (this$maxBounds == null) {
            if (other$maxBounds == null) {
                return true;
            }
        } else if (this$maxBounds.equals(other$maxBounds)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result = (1 * 59) + this.id;
        int result2 = (result * 59) + this.dimension;
        Object $data = this.data;
        int result3 = (result2 * 59) + ($data == null ? 43 : $data.hashCode());
        Object $engineVersion = this.engineVersion;
        int result4 = (result3 * 59) + ($engineVersion == null ? 43 : $engineVersion.hashCode());
        Object $identifier = this.identifier;
        int result5 = (result4 * 59) + ($identifier == null ? 43 : $identifier.hashCode());
        Object $instanceName = this.instanceName;
        int result6 = (result5 * 59) + ($instanceName == null ? 43 : $instanceName.hashCode());
        Object $minBounds = this.minBounds;
        int result7 = (result6 * 59) + ($minBounds == null ? 43 : $minBounds.hashCode());
        Object $maxBounds = this.maxBounds;
        return (result7 * 59) + ($maxBounds != null ? $maxBounds.hashCode() : 43);
    }

    public int getId() {
        return this.id;
    }

    public NbtMap getData() {
        return this.data;
    }

    public String getEngineVersion() {
        return this.engineVersion;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public String getInstanceName() {
        return this.instanceName;
    }

    public Vector3i getMinBounds() {
        return this.minBounds;
    }

    public Vector3i getMaxBounds() {
        return this.maxBounds;
    }

    public int getDimension() {
        return this.dimension;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.ADD_VOLUME_ENTITY;
    }
}
