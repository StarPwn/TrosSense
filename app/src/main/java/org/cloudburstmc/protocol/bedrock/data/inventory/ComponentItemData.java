package org.cloudburstmc.protocol.bedrock.data.inventory;

import org.cloudburstmc.nbt.NbtMap;

/* loaded from: classes5.dex */
public final class ComponentItemData {
    private final NbtMap data;
    private final String name;

    public ComponentItemData(String name, NbtMap data) {
        this.name = name;
        this.data = data;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ComponentItemData)) {
            return false;
        }
        ComponentItemData other = (ComponentItemData) o;
        Object this$name = getName();
        Object other$name = other.getName();
        if (this$name != null ? !this$name.equals(other$name) : other$name != null) {
            return false;
        }
        Object this$data = getData();
        Object other$data = other.getData();
        return this$data != null ? this$data.equals(other$data) : other$data == null;
    }

    public int hashCode() {
        Object $name = getName();
        int result = (1 * 59) + ($name == null ? 43 : $name.hashCode());
        Object $data = getData();
        return (result * 59) + ($data != null ? $data.hashCode() : 43);
    }

    public String toString() {
        return "ComponentItemData(name=" + getName() + ", data=" + getData() + ")";
    }

    public String getName() {
        return this.name;
    }

    public NbtMap getData() {
        return this.data;
    }
}
