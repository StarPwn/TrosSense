package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class MobArmorEquipmentPacket implements BedrockPacket {
    private ItemData boots;
    private ItemData chestplate;
    private ItemData helmet;
    private ItemData leggings;
    private long runtimeEntityId;

    public void setBoots(ItemData boots) {
        this.boots = boots;
    }

    public void setChestplate(ItemData chestplate) {
        this.chestplate = chestplate;
    }

    public void setHelmet(ItemData helmet) {
        this.helmet = helmet;
    }

    public void setLeggings(ItemData leggings) {
        this.leggings = leggings;
    }

    public void setRuntimeEntityId(long runtimeEntityId) {
        this.runtimeEntityId = runtimeEntityId;
    }

    protected boolean canEqual(Object other) {
        return other instanceof MobArmorEquipmentPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MobArmorEquipmentPacket)) {
            return false;
        }
        MobArmorEquipmentPacket other = (MobArmorEquipmentPacket) o;
        if (!other.canEqual(this) || this.runtimeEntityId != other.runtimeEntityId) {
            return false;
        }
        Object this$helmet = this.helmet;
        Object other$helmet = other.helmet;
        if (this$helmet != null ? !this$helmet.equals(other$helmet) : other$helmet != null) {
            return false;
        }
        Object this$chestplate = this.chestplate;
        Object other$chestplate = other.chestplate;
        if (this$chestplate != null ? !this$chestplate.equals(other$chestplate) : other$chestplate != null) {
            return false;
        }
        Object this$leggings = this.leggings;
        Object other$leggings = other.leggings;
        if (this$leggings != null ? !this$leggings.equals(other$leggings) : other$leggings != null) {
            return false;
        }
        Object this$boots = this.boots;
        Object other$boots = other.boots;
        return this$boots != null ? this$boots.equals(other$boots) : other$boots == null;
    }

    public int hashCode() {
        long $runtimeEntityId = this.runtimeEntityId;
        int result = (1 * 59) + ((int) (($runtimeEntityId >>> 32) ^ $runtimeEntityId));
        Object $helmet = this.helmet;
        int result2 = (result * 59) + ($helmet == null ? 43 : $helmet.hashCode());
        Object $chestplate = this.chestplate;
        int result3 = (result2 * 59) + ($chestplate == null ? 43 : $chestplate.hashCode());
        Object $leggings = this.leggings;
        int result4 = (result3 * 59) + ($leggings == null ? 43 : $leggings.hashCode());
        Object $boots = this.boots;
        return (result4 * 59) + ($boots != null ? $boots.hashCode() : 43);
    }

    public String toString() {
        return "MobArmorEquipmentPacket(runtimeEntityId=" + this.runtimeEntityId + ", helmet=" + this.helmet + ", chestplate=" + this.chestplate + ", leggings=" + this.leggings + ", boots=" + this.boots + ")";
    }

    public long getRuntimeEntityId() {
        return this.runtimeEntityId;
    }

    public ItemData getHelmet() {
        return this.helmet;
    }

    public ItemData getChestplate() {
        return this.chestplate;
    }

    public ItemData getLeggings() {
        return this.leggings;
    }

    public ItemData getBoots() {
        return this.boots;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.MOB_ARMOR_EQUIPMENT;
    }
}
