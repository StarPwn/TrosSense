package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class BlockEntityDataPacket implements BedrockPacket {
    private Vector3i blockPosition;
    private NbtMap data;

    public void setBlockPosition(Vector3i blockPosition) {
        this.blockPosition = blockPosition;
    }

    public void setData(NbtMap data) {
        this.data = data;
    }

    protected boolean canEqual(Object other) {
        return other instanceof BlockEntityDataPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof BlockEntityDataPacket)) {
            return false;
        }
        BlockEntityDataPacket other = (BlockEntityDataPacket) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$blockPosition = this.blockPosition;
        Object other$blockPosition = other.blockPosition;
        if (this$blockPosition != null ? !this$blockPosition.equals(other$blockPosition) : other$blockPosition != null) {
            return false;
        }
        Object this$data = this.data;
        Object other$data = other.data;
        return this$data != null ? this$data.equals(other$data) : other$data == null;
    }

    public int hashCode() {
        Object $blockPosition = this.blockPosition;
        int result = (1 * 59) + ($blockPosition == null ? 43 : $blockPosition.hashCode());
        Object $data = this.data;
        return (result * 59) + ($data != null ? $data.hashCode() : 43);
    }

    public String toString() {
        return "BlockEntityDataPacket(blockPosition=" + this.blockPosition + ", data=" + this.data + ")";
    }

    public Vector3i getBlockPosition() {
        return this.blockPosition;
    }

    public NbtMap getData() {
        return this.data;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.BLOCK_ENTITY_DATA;
    }
}
