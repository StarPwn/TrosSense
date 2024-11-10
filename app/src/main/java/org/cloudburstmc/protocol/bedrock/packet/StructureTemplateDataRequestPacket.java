package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.protocol.bedrock.data.structure.StructureSettings;
import org.cloudburstmc.protocol.bedrock.data.structure.StructureTemplateRequestOperation;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class StructureTemplateDataRequestPacket implements BedrockPacket {
    private String name;
    private StructureTemplateRequestOperation operation;
    private Vector3i position;
    private StructureSettings settings;

    public void setName(String name) {
        this.name = name;
    }

    public void setOperation(StructureTemplateRequestOperation operation) {
        this.operation = operation;
    }

    public void setPosition(Vector3i position) {
        this.position = position;
    }

    public void setSettings(StructureSettings settings) {
        this.settings = settings;
    }

    protected boolean canEqual(Object other) {
        return other instanceof StructureTemplateDataRequestPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof StructureTemplateDataRequestPacket)) {
            return false;
        }
        StructureTemplateDataRequestPacket other = (StructureTemplateDataRequestPacket) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$name = this.name;
        Object other$name = other.name;
        if (this$name != null ? !this$name.equals(other$name) : other$name != null) {
            return false;
        }
        Object this$position = this.position;
        Object other$position = other.position;
        if (this$position != null ? !this$position.equals(other$position) : other$position != null) {
            return false;
        }
        Object this$settings = this.settings;
        Object other$settings = other.settings;
        if (this$settings != null ? !this$settings.equals(other$settings) : other$settings != null) {
            return false;
        }
        Object this$operation = this.operation;
        Object other$operation = other.operation;
        return this$operation != null ? this$operation.equals(other$operation) : other$operation == null;
    }

    public int hashCode() {
        Object $name = this.name;
        int result = (1 * 59) + ($name == null ? 43 : $name.hashCode());
        Object $position = this.position;
        int result2 = (result * 59) + ($position == null ? 43 : $position.hashCode());
        Object $settings = this.settings;
        int result3 = (result2 * 59) + ($settings == null ? 43 : $settings.hashCode());
        Object $operation = this.operation;
        return (result3 * 59) + ($operation != null ? $operation.hashCode() : 43);
    }

    public String toString() {
        return "StructureTemplateDataRequestPacket(name=" + this.name + ", position=" + this.position + ", settings=" + this.settings + ", operation=" + this.operation + ")";
    }

    public String getName() {
        return this.name;
    }

    public Vector3i getPosition() {
        return this.position;
    }

    public StructureSettings getSettings() {
        return this.settings;
    }

    public StructureTemplateRequestOperation getOperation() {
        return this.operation;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.STRUCTURE_TEMPLATE_DATA_EXPORT_REQUEST;
    }
}
