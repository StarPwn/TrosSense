package org.cloudburstmc.protocol.bedrock.data;

import java.util.Collections;
import java.util.List;
import org.cloudburstmc.protocol.bedrock.data.attribute.AttributeModifierData;

/* loaded from: classes5.dex */
public final class AttributeData {
    private final float defaultValue;
    private final float maximum;
    private final float minimum;
    private final List<AttributeModifierData> modifiers;
    private final String name;
    private final float value;

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AttributeData)) {
            return false;
        }
        AttributeData other = (AttributeData) o;
        if (Float.compare(getMinimum(), other.getMinimum()) != 0 || Float.compare(getMaximum(), other.getMaximum()) != 0 || Float.compare(getValue(), other.getValue()) != 0 || Float.compare(getDefaultValue(), other.getDefaultValue()) != 0) {
            return false;
        }
        Object this$name = getName();
        Object other$name = other.getName();
        if (this$name != null ? !this$name.equals(other$name) : other$name != null) {
            return false;
        }
        Object this$modifiers = getModifiers();
        Object other$modifiers = other.getModifiers();
        return this$modifiers != null ? this$modifiers.equals(other$modifiers) : other$modifiers == null;
    }

    public int hashCode() {
        int result = (1 * 59) + Float.floatToIntBits(getMinimum());
        int result2 = (((((result * 59) + Float.floatToIntBits(getMaximum())) * 59) + Float.floatToIntBits(getValue())) * 59) + Float.floatToIntBits(getDefaultValue());
        Object $name = getName();
        int result3 = (result2 * 59) + ($name == null ? 43 : $name.hashCode());
        Object $modifiers = getModifiers();
        return (result3 * 59) + ($modifiers != null ? $modifiers.hashCode() : 43);
    }

    public String toString() {
        return "AttributeData(name=" + getName() + ", minimum=" + getMinimum() + ", maximum=" + getMaximum() + ", value=" + getValue() + ", defaultValue=" + getDefaultValue() + ", modifiers=" + getModifiers() + ")";
    }

    public AttributeData(String name, float minimum, float maximum, float value, float defaultValue, List<AttributeModifierData> modifiers) {
        this.name = name;
        this.minimum = minimum;
        this.maximum = maximum;
        this.value = value;
        this.defaultValue = defaultValue;
        this.modifiers = modifiers;
    }

    public String getName() {
        return this.name;
    }

    public float getMinimum() {
        return this.minimum;
    }

    public float getMaximum() {
        return this.maximum;
    }

    public float getValue() {
        return this.value;
    }

    public float getDefaultValue() {
        return this.defaultValue;
    }

    public List<AttributeModifierData> getModifiers() {
        return this.modifiers;
    }

    public AttributeData(String name, float minimum, float maximum, float value) {
        this(name, minimum, maximum, value, maximum, Collections.emptyList());
    }

    public AttributeData(String name, float minimum, float maximum, float value, float defaultValue) {
        this(name, minimum, maximum, value, defaultValue, Collections.emptyList());
    }
}
