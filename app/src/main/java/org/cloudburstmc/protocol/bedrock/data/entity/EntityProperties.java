package org.cloudburstmc.protocol.bedrock.data.entity;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;

/* loaded from: classes5.dex */
public final class EntityProperties {
    private final List<IntEntityProperty> intProperties = new ObjectArrayList();
    private final List<FloatEntityProperty> floatProperties = new ObjectArrayList();

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof EntityProperties)) {
            return false;
        }
        EntityProperties other = (EntityProperties) o;
        Object this$intProperties = getIntProperties();
        Object other$intProperties = other.getIntProperties();
        if (this$intProperties != null ? !this$intProperties.equals(other$intProperties) : other$intProperties != null) {
            return false;
        }
        Object this$floatProperties = getFloatProperties();
        Object other$floatProperties = other.getFloatProperties();
        return this$floatProperties != null ? this$floatProperties.equals(other$floatProperties) : other$floatProperties == null;
    }

    public int hashCode() {
        Object $intProperties = getIntProperties();
        int result = (1 * 59) + ($intProperties == null ? 43 : $intProperties.hashCode());
        Object $floatProperties = getFloatProperties();
        return (result * 59) + ($floatProperties != null ? $floatProperties.hashCode() : 43);
    }

    public String toString() {
        return "EntityProperties(intProperties=" + getIntProperties() + ", floatProperties=" + getFloatProperties() + ")";
    }

    public List<IntEntityProperty> getIntProperties() {
        return this.intProperties;
    }

    public List<FloatEntityProperty> getFloatProperties() {
        return this.floatProperties;
    }
}
