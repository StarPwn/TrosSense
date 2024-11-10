package org.cloudburstmc.protocol.bedrock.data.attribute;

/* loaded from: classes5.dex */
public final class AttributeModifierData {
    private final float amount;
    private final String id;
    private final String name;
    private final int operand;
    private final AttributeOperation operation;
    private final boolean serializable;

    public AttributeModifierData(String id, String name, float amount, AttributeOperation operation, int operand, boolean serializable) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.operation = operation;
        this.operand = operand;
        this.serializable = serializable;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AttributeModifierData)) {
            return false;
        }
        AttributeModifierData other = (AttributeModifierData) o;
        if (Float.compare(getAmount(), other.getAmount()) != 0 || getOperand() != other.getOperand() || isSerializable() != other.isSerializable()) {
            return false;
        }
        Object this$id = getId();
        Object other$id = other.getId();
        if (this$id != null ? !this$id.equals(other$id) : other$id != null) {
            return false;
        }
        Object this$name = getName();
        Object other$name = other.getName();
        if (this$name != null ? !this$name.equals(other$name) : other$name != null) {
            return false;
        }
        Object this$operation = getOperation();
        Object other$operation = other.getOperation();
        return this$operation != null ? this$operation.equals(other$operation) : other$operation == null;
    }

    public int hashCode() {
        int result = (1 * 59) + Float.floatToIntBits(getAmount());
        int result2 = ((result * 59) + getOperand()) * 59;
        int i = isSerializable() ? 79 : 97;
        Object $id = getId();
        int result3 = ((result2 + i) * 59) + ($id == null ? 43 : $id.hashCode());
        Object $name = getName();
        int result4 = (result3 * 59) + ($name == null ? 43 : $name.hashCode());
        Object $operation = getOperation();
        return (result4 * 59) + ($operation != null ? $operation.hashCode() : 43);
    }

    public String toString() {
        return "AttributeModifierData(id=" + getId() + ", name=" + getName() + ", amount=" + getAmount() + ", operation=" + getOperation() + ", operand=" + getOperand() + ", serializable=" + isSerializable() + ")";
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public float getAmount() {
        return this.amount;
    }

    public AttributeOperation getOperation() {
        return this.operation;
    }

    public int getOperand() {
        return this.operand;
    }

    public boolean isSerializable() {
        return this.serializable;
    }
}
