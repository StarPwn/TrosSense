package org.cloudburstmc.protocol.bedrock.packet;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.UUID;
import org.cloudburstmc.protocol.bedrock.data.inventory.CraftingType;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;
import org.cloudburstmc.protocol.common.PacketSignal;

@Deprecated
/* loaded from: classes5.dex */
public class CraftingEventPacket implements BedrockPacket {
    private byte containerId;
    private final List<ItemData> inputs = new ObjectArrayList();
    private final List<ItemData> outputs = new ObjectArrayList();
    private CraftingType type;
    private UUID uuid;

    public void setContainerId(byte containerId) {
        this.containerId = containerId;
    }

    public void setType(CraftingType type) {
        this.type = type;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    protected boolean canEqual(Object other) {
        return other instanceof CraftingEventPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CraftingEventPacket)) {
            return false;
        }
        CraftingEventPacket other = (CraftingEventPacket) o;
        if (!other.canEqual(this) || this.containerId != other.containerId) {
            return false;
        }
        Object this$inputs = this.inputs;
        Object other$inputs = other.inputs;
        if (this$inputs != null ? !this$inputs.equals(other$inputs) : other$inputs != null) {
            return false;
        }
        Object this$outputs = this.outputs;
        Object other$outputs = other.outputs;
        if (this$outputs != null ? !this$outputs.equals(other$outputs) : other$outputs != null) {
            return false;
        }
        Object this$type = this.type;
        Object other$type = other.type;
        if (this$type != null ? !this$type.equals(other$type) : other$type != null) {
            return false;
        }
        Object this$uuid = this.uuid;
        Object other$uuid = other.uuid;
        return this$uuid != null ? this$uuid.equals(other$uuid) : other$uuid == null;
    }

    public int hashCode() {
        int result = (1 * 59) + this.containerId;
        Object $inputs = this.inputs;
        int result2 = (result * 59) + ($inputs == null ? 43 : $inputs.hashCode());
        Object $outputs = this.outputs;
        int result3 = (result2 * 59) + ($outputs == null ? 43 : $outputs.hashCode());
        Object $type = this.type;
        int result4 = (result3 * 59) + ($type == null ? 43 : $type.hashCode());
        Object $uuid = this.uuid;
        return (result4 * 59) + ($uuid != null ? $uuid.hashCode() : 43);
    }

    public String toString() {
        return "CraftingEventPacket(inputs=" + this.inputs + ", outputs=" + this.outputs + ", containerId=" + ((int) this.containerId) + ", type=" + this.type + ", uuid=" + this.uuid + ")";
    }

    public List<ItemData> getInputs() {
        return this.inputs;
    }

    public List<ItemData> getOutputs() {
        return this.outputs;
    }

    public byte getContainerId() {
        return this.containerId;
    }

    public CraftingType getType() {
        return this.type;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.CRAFTING_EVENT;
    }
}
