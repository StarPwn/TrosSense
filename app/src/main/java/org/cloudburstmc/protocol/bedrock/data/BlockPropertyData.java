package org.cloudburstmc.protocol.bedrock.data;

import org.cloudburstmc.nbt.NbtMap;

/* loaded from: classes5.dex */
public final class BlockPropertyData {
    private final String name;
    private final NbtMap properties;

    public BlockPropertyData(String name, NbtMap properties) {
        this.name = name;
        this.properties = properties;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof BlockPropertyData)) {
            return false;
        }
        BlockPropertyData other = (BlockPropertyData) o;
        Object this$name = getName();
        Object other$name = other.getName();
        if (this$name != null ? !this$name.equals(other$name) : other$name != null) {
            return false;
        }
        Object this$properties = getProperties();
        Object other$properties = other.getProperties();
        return this$properties != null ? this$properties.equals(other$properties) : other$properties == null;
    }

    public int hashCode() {
        Object $name = getName();
        int result = (1 * 59) + ($name == null ? 43 : $name.hashCode());
        Object $properties = getProperties();
        return (result * 59) + ($properties != null ? $properties.hashCode() : 43);
    }

    public String toString() {
        return "BlockPropertyData(name=" + getName() + ", properties=" + getProperties() + ")";
    }

    public String getName() {
        return this.name;
    }

    public NbtMap getProperties() {
        return this.properties;
    }
}
