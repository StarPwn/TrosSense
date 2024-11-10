package org.cloudburstmc.protocol.bedrock.data.entity;

/* loaded from: classes5.dex */
public final class FloatEntityProperty implements EntityProperty {
    private final int index;
    private final float value;

    public FloatEntityProperty(int index, float value) {
        this.index = index;
        this.value = value;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof FloatEntityProperty)) {
            return false;
        }
        FloatEntityProperty other = (FloatEntityProperty) o;
        return getIndex() == other.getIndex() && Float.compare(getValue(), other.getValue()) == 0;
    }

    public int hashCode() {
        int result = (1 * 59) + getIndex();
        return (result * 59) + Float.floatToIntBits(getValue());
    }

    public String toString() {
        return "FloatEntityProperty(index=" + getIndex() + ", value=" + getValue() + ")";
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.entity.EntityProperty
    public int getIndex() {
        return this.index;
    }

    public float getValue() {
        return this.value;
    }
}
