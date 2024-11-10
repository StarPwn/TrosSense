package org.cloudburstmc.protocol.bedrock.data;

import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.protocol.bedrock.data.definitions.BlockDefinition;

/* loaded from: classes5.dex */
public final class BlockChangeEntry {
    private final BlockDefinition definition;
    private final long messageEntityId;
    private final MessageType messageType;
    private final Vector3i position;
    private final int updateFlags;

    /* loaded from: classes5.dex */
    public enum MessageType {
        NONE,
        CREATE,
        DESTROY
    }

    public BlockChangeEntry(Vector3i position, BlockDefinition definition, int updateFlags, long messageEntityId, MessageType messageType) {
        this.position = position;
        this.definition = definition;
        this.updateFlags = updateFlags;
        this.messageEntityId = messageEntityId;
        this.messageType = messageType;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof BlockChangeEntry)) {
            return false;
        }
        BlockChangeEntry other = (BlockChangeEntry) o;
        if (getUpdateFlags() != other.getUpdateFlags() || getMessageEntityId() != other.getMessageEntityId()) {
            return false;
        }
        Object this$position = getPosition();
        Object other$position = other.getPosition();
        if (this$position != null ? !this$position.equals(other$position) : other$position != null) {
            return false;
        }
        Object this$definition = getDefinition();
        Object other$definition = other.getDefinition();
        if (this$definition != null ? !this$definition.equals(other$definition) : other$definition != null) {
            return false;
        }
        Object this$messageType = getMessageType();
        Object other$messageType = other.getMessageType();
        return this$messageType != null ? this$messageType.equals(other$messageType) : other$messageType == null;
    }

    public int hashCode() {
        int result = (1 * 59) + getUpdateFlags();
        long $messageEntityId = getMessageEntityId();
        int result2 = (result * 59) + ((int) (($messageEntityId >>> 32) ^ $messageEntityId));
        Object $position = getPosition();
        int result3 = (result2 * 59) + ($position == null ? 43 : $position.hashCode());
        Object $definition = getDefinition();
        int result4 = (result3 * 59) + ($definition == null ? 43 : $definition.hashCode());
        Object $messageType = getMessageType();
        return (result4 * 59) + ($messageType != null ? $messageType.hashCode() : 43);
    }

    public String toString() {
        return "BlockChangeEntry(position=" + getPosition() + ", definition=" + getDefinition() + ", updateFlags=" + getUpdateFlags() + ", messageEntityId=" + getMessageEntityId() + ", messageType=" + getMessageType() + ")";
    }

    public Vector3i getPosition() {
        return this.position;
    }

    public BlockDefinition getDefinition() {
        return this.definition;
    }

    public int getUpdateFlags() {
        return this.updateFlags;
    }

    public long getMessageEntityId() {
        return this.messageEntityId;
    }

    public MessageType getMessageType() {
        return this.messageType;
    }
}
