package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataMap;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class AddItemEntityPacket implements BedrockPacket {
    private boolean fromFishing;
    private ItemData itemInHand;
    private final EntityDataMap metadata = new EntityDataMap();
    private Vector3f motion;
    private Vector3f position;
    private long runtimeEntityId;
    private long uniqueEntityId;

    public void setFromFishing(boolean fromFishing) {
        this.fromFishing = fromFishing;
    }

    public void setItemInHand(ItemData itemInHand) {
        this.itemInHand = itemInHand;
    }

    public void setMotion(Vector3f motion) {
        this.motion = motion;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void setRuntimeEntityId(long runtimeEntityId) {
        this.runtimeEntityId = runtimeEntityId;
    }

    public void setUniqueEntityId(long uniqueEntityId) {
        this.uniqueEntityId = uniqueEntityId;
    }

    protected boolean canEqual(Object other) {
        return other instanceof AddItemEntityPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AddItemEntityPacket)) {
            return false;
        }
        AddItemEntityPacket other = (AddItemEntityPacket) o;
        if (!other.canEqual(this) || this.uniqueEntityId != other.uniqueEntityId || this.runtimeEntityId != other.runtimeEntityId || this.fromFishing != other.fromFishing) {
            return false;
        }
        Object this$metadata = this.metadata;
        Object other$metadata = other.metadata;
        if (this$metadata != null ? !this$metadata.equals(other$metadata) : other$metadata != null) {
            return false;
        }
        Object this$itemInHand = this.itemInHand;
        Object other$itemInHand = other.itemInHand;
        if (this$itemInHand != null ? !this$itemInHand.equals(other$itemInHand) : other$itemInHand != null) {
            return false;
        }
        Object this$position = this.position;
        Object other$position = other.position;
        if (this$position != null ? !this$position.equals(other$position) : other$position != null) {
            return false;
        }
        Object this$motion = this.motion;
        Object other$motion = other.motion;
        return this$motion != null ? this$motion.equals(other$motion) : other$motion == null;
    }

    public int hashCode() {
        long $uniqueEntityId = this.uniqueEntityId;
        int result = (1 * 59) + ((int) (($uniqueEntityId >>> 32) ^ $uniqueEntityId));
        long $runtimeEntityId = this.runtimeEntityId;
        int result2 = ((result * 59) + ((int) (($runtimeEntityId >>> 32) ^ $runtimeEntityId))) * 59;
        int i = this.fromFishing ? 79 : 97;
        Object $metadata = this.metadata;
        int result3 = ((result2 + i) * 59) + ($metadata == null ? 43 : $metadata.hashCode());
        Object $itemInHand = this.itemInHand;
        int result4 = (result3 * 59) + ($itemInHand == null ? 43 : $itemInHand.hashCode());
        Object $position = this.position;
        int result5 = (result4 * 59) + ($position == null ? 43 : $position.hashCode());
        Object $motion = this.motion;
        return (result5 * 59) + ($motion != null ? $motion.hashCode() : 43);
    }

    public String toString() {
        return "AddItemEntityPacket(metadata=" + this.metadata + ", uniqueEntityId=" + this.uniqueEntityId + ", runtimeEntityId=" + this.runtimeEntityId + ", itemInHand=" + this.itemInHand + ", position=" + this.position + ", motion=" + this.motion + ", fromFishing=" + this.fromFishing + ")";
    }

    public EntityDataMap getMetadata() {
        return this.metadata;
    }

    public long getUniqueEntityId() {
        return this.uniqueEntityId;
    }

    public long getRuntimeEntityId() {
        return this.runtimeEntityId;
    }

    public ItemData getItemInHand() {
        return this.itemInHand;
    }

    public Vector3f getPosition() {
        return this.position;
    }

    public Vector3f getMotion() {
        return this.motion;
    }

    public boolean isFromFishing() {
        return this.fromFishing;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.ADD_ITEM_ENTITY;
    }
}
