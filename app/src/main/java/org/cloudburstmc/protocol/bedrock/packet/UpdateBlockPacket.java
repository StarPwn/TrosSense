package org.cloudburstmc.protocol.bedrock.packet;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.protocol.bedrock.data.definitions.BlockDefinition;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class UpdateBlockPacket implements BedrockPacket {
    public static final Set<Flag> FLAG_ALL = Collections.unmodifiableSet(EnumSet.of(Flag.NEIGHBORS, Flag.NETWORK));
    public static final Set<Flag> FLAG_ALL_PRIORITY = Collections.unmodifiableSet(EnumSet.of(Flag.NEIGHBORS, Flag.NETWORK, Flag.PRIORITY));
    Vector3i blockPosition;
    int dataLayer;
    BlockDefinition definition;
    final Set<Flag> flags = EnumSet.noneOf(Flag.class);

    /* loaded from: classes5.dex */
    public enum Flag {
        NEIGHBORS,
        NETWORK,
        NO_GRAPHIC,
        UNUSED,
        PRIORITY
    }

    public void setBlockPosition(Vector3i blockPosition) {
        this.blockPosition = blockPosition;
    }

    public void setDataLayer(int dataLayer) {
        this.dataLayer = dataLayer;
    }

    public void setDefinition(BlockDefinition definition) {
        this.definition = definition;
    }

    protected boolean canEqual(Object other) {
        return other instanceof UpdateBlockPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UpdateBlockPacket)) {
            return false;
        }
        UpdateBlockPacket other = (UpdateBlockPacket) o;
        if (!other.canEqual(this) || this.dataLayer != other.dataLayer) {
            return false;
        }
        Object this$flags = this.flags;
        Object other$flags = other.flags;
        if (this$flags != null ? !this$flags.equals(other$flags) : other$flags != null) {
            return false;
        }
        Object this$blockPosition = this.blockPosition;
        Object other$blockPosition = other.blockPosition;
        if (this$blockPosition != null ? !this$blockPosition.equals(other$blockPosition) : other$blockPosition != null) {
            return false;
        }
        Object this$definition = this.definition;
        Object other$definition = other.definition;
        return this$definition != null ? this$definition.equals(other$definition) : other$definition == null;
    }

    public int hashCode() {
        int result = (1 * 59) + this.dataLayer;
        Object $flags = this.flags;
        int result2 = (result * 59) + ($flags == null ? 43 : $flags.hashCode());
        Object $blockPosition = this.blockPosition;
        int result3 = (result2 * 59) + ($blockPosition == null ? 43 : $blockPosition.hashCode());
        Object $definition = this.definition;
        return (result3 * 59) + ($definition != null ? $definition.hashCode() : 43);
    }

    public String toString() {
        return "UpdateBlockPacket(flags=" + this.flags + ", blockPosition=" + this.blockPosition + ", definition=" + this.definition + ", dataLayer=" + this.dataLayer + ")";
    }

    public Set<Flag> getFlags() {
        return this.flags;
    }

    public Vector3i getBlockPosition() {
        return this.blockPosition;
    }

    public BlockDefinition getDefinition() {
        return this.definition;
    }

    public int getDataLayer() {
        return this.dataLayer;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.UPDATE_BLOCK;
    }
}
