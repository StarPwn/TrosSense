package org.cloudburstmc.protocol.bedrock.data.entity;

/* loaded from: classes5.dex */
public final class IntEntityProperty implements EntityProperty {
    private final int index;
    private final int value;

    public IntEntityProperty(int index, int value) {
        this.index = index;
        this.value = value;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof IntEntityProperty)) {
            return false;
        }
        IntEntityProperty other = (IntEntityProperty) o;
        return getIndex() == other.getIndex() && getValue() == other.getValue();
    }

    public int hashCode() {
        int result = (1 * 59) + getIndex();
        return (result * 59) + getValue();
    }

    public String toString() {
        return "IntEntityProperty(index=" + getIndex() + ", value=" + getValue() + ")";
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.entity.EntityProperty
    public int getIndex() {
        return this.index;
    }

    public int getValue() {
        return this.value;
    }
}
