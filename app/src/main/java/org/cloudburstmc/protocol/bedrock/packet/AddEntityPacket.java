package org.cloudburstmc.protocol.bedrock.packet;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import org.cloudburstmc.math.vector.Vector2f;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.protocol.bedrock.data.AttributeData;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataMap;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityLinkData;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityProperties;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class AddEntityPacket implements BedrockPacket {
    private float bodyRotation;
    private int entityType;
    private float headRotation;
    private String identifier;
    private Vector3f motion;
    private Vector3f position;
    private Vector2f rotation;
    private long runtimeEntityId;
    private long uniqueEntityId;
    private final List<AttributeData> attributes = new ObjectArrayList();
    private final EntityDataMap metadata = new EntityDataMap();
    private final List<EntityLinkData> entityLinks = new ObjectArrayList();
    private final EntityProperties properties = new EntityProperties();

    public void setBodyRotation(float bodyRotation) {
        this.bodyRotation = bodyRotation;
    }

    public void setEntityType(int entityType) {
        this.entityType = entityType;
    }

    public void setHeadRotation(float headRotation) {
        this.headRotation = headRotation;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public void setMotion(Vector3f motion) {
        this.motion = motion;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void setRotation(Vector2f rotation) {
        this.rotation = rotation;
    }

    public void setRuntimeEntityId(long runtimeEntityId) {
        this.runtimeEntityId = runtimeEntityId;
    }

    public void setUniqueEntityId(long uniqueEntityId) {
        this.uniqueEntityId = uniqueEntityId;
    }

    protected boolean canEqual(Object other) {
        return other instanceof AddEntityPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AddEntityPacket)) {
            return false;
        }
        AddEntityPacket other = (AddEntityPacket) o;
        if (!other.canEqual(this) || this.uniqueEntityId != other.uniqueEntityId || this.runtimeEntityId != other.runtimeEntityId || this.entityType != other.entityType || Float.compare(this.headRotation, other.headRotation) != 0 || Float.compare(this.bodyRotation, other.bodyRotation) != 0) {
            return false;
        }
        Object this$attributes = this.attributes;
        Object other$attributes = other.attributes;
        if (this$attributes != null ? !this$attributes.equals(other$attributes) : other$attributes != null) {
            return false;
        }
        Object this$metadata = this.metadata;
        Object other$metadata = other.metadata;
        if (this$metadata != null ? !this$metadata.equals(other$metadata) : other$metadata != null) {
            return false;
        }
        Object this$entityLinks = this.entityLinks;
        Object other$entityLinks = other.entityLinks;
        if (this$entityLinks != null ? !this$entityLinks.equals(other$entityLinks) : other$entityLinks != null) {
            return false;
        }
        Object this$identifier = this.identifier;
        Object other$identifier = other.identifier;
        if (this$identifier != null ? !this$identifier.equals(other$identifier) : other$identifier != null) {
            return false;
        }
        Object this$position = this.position;
        Object other$position = other.position;
        if (this$position != null ? !this$position.equals(other$position) : other$position != null) {
            return false;
        }
        Object this$motion = this.motion;
        Object other$motion = other.motion;
        if (this$motion != null ? !this$motion.equals(other$motion) : other$motion != null) {
            return false;
        }
        Object this$rotation = this.rotation;
        Object other$rotation = other.rotation;
        if (this$rotation == null) {
            if (other$rotation != null) {
                return false;
            }
        } else if (!this$rotation.equals(other$rotation)) {
            return false;
        }
        Object other$rotation2 = this.properties;
        Object other$properties = other.properties;
        return other$rotation2 == null ? other$properties == null : other$rotation2.equals(other$properties);
    }

    public int hashCode() {
        long $uniqueEntityId = this.uniqueEntityId;
        int result = (1 * 59) + ((int) (($uniqueEntityId >>> 32) ^ $uniqueEntityId));
        long $runtimeEntityId = this.runtimeEntityId;
        int result2 = (((((((result * 59) + ((int) (($runtimeEntityId >>> 32) ^ $runtimeEntityId))) * 59) + this.entityType) * 59) + Float.floatToIntBits(this.headRotation)) * 59) + Float.floatToIntBits(this.bodyRotation);
        Object $attributes = this.attributes;
        int result3 = (result2 * 59) + ($attributes == null ? 43 : $attributes.hashCode());
        Object $metadata = this.metadata;
        int result4 = (result3 * 59) + ($metadata == null ? 43 : $metadata.hashCode());
        Object $entityLinks = this.entityLinks;
        int result5 = (result4 * 59) + ($entityLinks == null ? 43 : $entityLinks.hashCode());
        Object $identifier = this.identifier;
        int result6 = (result5 * 59) + ($identifier == null ? 43 : $identifier.hashCode());
        Object $position = this.position;
        int result7 = (result6 * 59) + ($position == null ? 43 : $position.hashCode());
        Object $motion = this.motion;
        int result8 = (result7 * 59) + ($motion == null ? 43 : $motion.hashCode());
        Object $rotation = this.rotation;
        int result9 = (result8 * 59) + ($rotation == null ? 43 : $rotation.hashCode());
        Object $properties = this.properties;
        return (result9 * 59) + ($properties != null ? $properties.hashCode() : 43);
    }

    public String toString() {
        return "AddEntityPacket(attributes=" + this.attributes + ", metadata=" + this.metadata + ", entityLinks=" + this.entityLinks + ", uniqueEntityId=" + this.uniqueEntityId + ", runtimeEntityId=" + this.runtimeEntityId + ", identifier=" + this.identifier + ", entityType=" + this.entityType + ", position=" + this.position + ", motion=" + this.motion + ", rotation=" + this.rotation + ", headRotation=" + this.headRotation + ", bodyRotation=" + this.bodyRotation + ", properties=" + this.properties + ")";
    }

    public List<AttributeData> getAttributes() {
        return this.attributes;
    }

    public EntityDataMap getMetadata() {
        return this.metadata;
    }

    public List<EntityLinkData> getEntityLinks() {
        return this.entityLinks;
    }

    public long getUniqueEntityId() {
        return this.uniqueEntityId;
    }

    public long getRuntimeEntityId() {
        return this.runtimeEntityId;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public int getEntityType() {
        return this.entityType;
    }

    public Vector3f getPosition() {
        return this.position;
    }

    public Vector3f getMotion() {
        return this.motion;
    }

    public Vector2f getRotation() {
        return this.rotation;
    }

    public float getHeadRotation() {
        return this.headRotation;
    }

    public float getBodyRotation() {
        return this.bodyRotation;
    }

    public EntityProperties getProperties() {
        return this.properties;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.ADD_ENTITY;
    }
}
