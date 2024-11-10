package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.protocol.bedrock.data.structure.StructureTemplateResponseType;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class StructureTemplateDataResponsePacket implements BedrockPacket {
    private String name;
    private boolean save;
    private NbtMap tag;
    private StructureTemplateResponseType type;

    public void setName(String name) {
        this.name = name;
    }

    public void setSave(boolean save) {
        this.save = save;
    }

    public void setTag(NbtMap tag) {
        this.tag = tag;
    }

    public void setType(StructureTemplateResponseType type) {
        this.type = type;
    }

    protected boolean canEqual(Object other) {
        return other instanceof StructureTemplateDataResponsePacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof StructureTemplateDataResponsePacket)) {
            return false;
        }
        StructureTemplateDataResponsePacket other = (StructureTemplateDataResponsePacket) o;
        if (!other.canEqual(this) || this.save != other.save) {
            return false;
        }
        Object this$name = this.name;
        Object other$name = other.name;
        if (this$name != null ? !this$name.equals(other$name) : other$name != null) {
            return false;
        }
        Object this$tag = this.tag;
        Object other$tag = other.tag;
        if (this$tag != null ? !this$tag.equals(other$tag) : other$tag != null) {
            return false;
        }
        Object this$type = this.type;
        Object other$type = other.type;
        return this$type != null ? this$type.equals(other$type) : other$type == null;
    }

    public int hashCode() {
        int result = (1 * 59) + (this.save ? 79 : 97);
        Object $name = this.name;
        int result2 = (result * 59) + ($name == null ? 43 : $name.hashCode());
        Object $tag = this.tag;
        int result3 = (result2 * 59) + ($tag == null ? 43 : $tag.hashCode());
        Object $type = this.type;
        return (result3 * 59) + ($type != null ? $type.hashCode() : 43);
    }

    public String toString() {
        return "StructureTemplateDataResponsePacket(name=" + this.name + ", save=" + this.save + ", tag=" + this.tag + ", type=" + this.type + ")";
    }

    public String getName() {
        return this.name;
    }

    public boolean isSave() {
        return this.save;
    }

    public NbtMap getTag() {
        return this.tag;
    }

    public StructureTemplateResponseType getType() {
        return this.type;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.STRUCTURE_TEMPLATE_DATA_EXPORT_RESPONSE;
    }
}
