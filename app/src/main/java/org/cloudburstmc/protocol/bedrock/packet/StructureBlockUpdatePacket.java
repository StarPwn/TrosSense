package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.protocol.bedrock.data.structure.StructureEditorData;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class StructureBlockUpdatePacket implements BedrockPacket {
    private Vector3i blockPosition;
    private StructureEditorData editorData;
    private boolean powered;
    private boolean waterlogged;

    /* loaded from: classes5.dex */
    public enum Type {
        NONE,
        SAVE,
        LOAD
    }

    public void setBlockPosition(Vector3i blockPosition) {
        this.blockPosition = blockPosition;
    }

    public void setEditorData(StructureEditorData editorData) {
        this.editorData = editorData;
    }

    public void setPowered(boolean powered) {
        this.powered = powered;
    }

    public void setWaterlogged(boolean waterlogged) {
        this.waterlogged = waterlogged;
    }

    protected boolean canEqual(Object other) {
        return other instanceof StructureBlockUpdatePacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof StructureBlockUpdatePacket)) {
            return false;
        }
        StructureBlockUpdatePacket other = (StructureBlockUpdatePacket) o;
        if (!other.canEqual(this) || this.powered != other.powered || this.waterlogged != other.waterlogged) {
            return false;
        }
        Object this$blockPosition = this.blockPosition;
        Object other$blockPosition = other.blockPosition;
        if (this$blockPosition != null ? !this$blockPosition.equals(other$blockPosition) : other$blockPosition != null) {
            return false;
        }
        Object this$editorData = this.editorData;
        Object other$editorData = other.editorData;
        return this$editorData != null ? this$editorData.equals(other$editorData) : other$editorData == null;
    }

    public int hashCode() {
        int result = (1 * 59) + (this.powered ? 79 : 97);
        int result2 = result * 59;
        int i = this.waterlogged ? 79 : 97;
        Object $blockPosition = this.blockPosition;
        int result3 = ((result2 + i) * 59) + ($blockPosition == null ? 43 : $blockPosition.hashCode());
        Object $editorData = this.editorData;
        return (result3 * 59) + ($editorData != null ? $editorData.hashCode() : 43);
    }

    public String toString() {
        return "StructureBlockUpdatePacket(blockPosition=" + this.blockPosition + ", editorData=" + this.editorData + ", powered=" + this.powered + ", waterlogged=" + this.waterlogged + ")";
    }

    public Vector3i getBlockPosition() {
        return this.blockPosition;
    }

    public StructureEditorData getEditorData() {
        return this.editorData;
    }

    public boolean isPowered() {
        return this.powered;
    }

    public boolean isWaterlogged() {
        return this.waterlogged;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.STRUCTURE_BLOCK_UPDATE;
    }
}
