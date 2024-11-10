package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.bedrock.data.CodeBuilderCategoryType;
import org.cloudburstmc.protocol.bedrock.data.CodeBuilderOperationType;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class CodeBuilderSourcePacket implements BedrockPacket {
    private CodeBuilderCategoryType category;
    private CodeBuilderOperationType operation;
    private String value;

    public void setCategory(CodeBuilderCategoryType category) {
        this.category = category;
    }

    public void setOperation(CodeBuilderOperationType operation) {
        this.operation = operation;
    }

    public void setValue(String value) {
        this.value = value;
    }

    protected boolean canEqual(Object other) {
        return other instanceof CodeBuilderSourcePacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CodeBuilderSourcePacket)) {
            return false;
        }
        CodeBuilderSourcePacket other = (CodeBuilderSourcePacket) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$operation = this.operation;
        Object other$operation = other.operation;
        if (this$operation != null ? !this$operation.equals(other$operation) : other$operation != null) {
            return false;
        }
        Object this$category = this.category;
        Object other$category = other.category;
        if (this$category != null ? !this$category.equals(other$category) : other$category != null) {
            return false;
        }
        Object this$value = this.value;
        Object other$value = other.value;
        return this$value != null ? this$value.equals(other$value) : other$value == null;
    }

    public int hashCode() {
        Object $operation = this.operation;
        int result = (1 * 59) + ($operation == null ? 43 : $operation.hashCode());
        Object $category = this.category;
        int result2 = (result * 59) + ($category == null ? 43 : $category.hashCode());
        Object $value = this.value;
        return (result2 * 59) + ($value != null ? $value.hashCode() : 43);
    }

    public String toString() {
        return "CodeBuilderSourcePacket(operation=" + this.operation + ", category=" + this.category + ", value=" + this.value + ")";
    }

    public CodeBuilderOperationType getOperation() {
        return this.operation;
    }

    public CodeBuilderCategoryType getCategory() {
        return this.category;
    }

    public String getValue() {
        return this.value;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.CODE_BUILDER_SOURCE;
    }
}
