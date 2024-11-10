package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class UpdateEquipPacket implements BedrockPacket {
    private int size;
    private NbtMap tag;
    private long uniqueEntityId;
    private short windowId;
    private short windowType;

    public void setSize(int size) {
        this.size = size;
    }

    public void setTag(NbtMap tag) {
        this.tag = tag;
    }

    public void setUniqueEntityId(long uniqueEntityId) {
        this.uniqueEntityId = uniqueEntityId;
    }

    public void setWindowId(short windowId) {
        this.windowId = windowId;
    }

    public void setWindowType(short windowType) {
        this.windowType = windowType;
    }

    protected boolean canEqual(Object other) {
        return other instanceof UpdateEquipPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UpdateEquipPacket)) {
            return false;
        }
        UpdateEquipPacket other = (UpdateEquipPacket) o;
        if (!other.canEqual(this) || this.windowId != other.windowId || this.windowType != other.windowType || this.size != other.size || this.uniqueEntityId != other.uniqueEntityId) {
            return false;
        }
        Object this$tag = this.tag;
        Object other$tag = other.tag;
        return this$tag != null ? this$tag.equals(other$tag) : other$tag == null;
    }

    public int hashCode() {
        int result = (1 * 59) + this.windowId;
        int result2 = (((result * 59) + this.windowType) * 59) + this.size;
        long $uniqueEntityId = this.uniqueEntityId;
        int result3 = (result2 * 59) + ((int) (($uniqueEntityId >>> 32) ^ $uniqueEntityId));
        Object $tag = this.tag;
        return (result3 * 59) + ($tag == null ? 43 : $tag.hashCode());
    }

    public String toString() {
        return "UpdateEquipPacket(windowId=" + ((int) this.windowId) + ", windowType=" + ((int) this.windowType) + ", size=" + this.size + ", uniqueEntityId=" + this.uniqueEntityId + ", tag=" + this.tag + ")";
    }

    public short getWindowId() {
        return this.windowId;
    }

    public short getWindowType() {
        return this.windowType;
    }

    public int getSize() {
        return this.size;
    }

    public long getUniqueEntityId() {
        return this.uniqueEntityId;
    }

    public NbtMap getTag() {
        return this.tag;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.UPDATE_EQUIP;
    }
}
