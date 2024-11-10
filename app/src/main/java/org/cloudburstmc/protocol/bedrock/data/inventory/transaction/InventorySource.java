package org.cloudburstmc.protocol.bedrock.data.inventory.transaction;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.cloudburstmc.protocol.common.util.Preconditions;

/* loaded from: classes5.dex */
public final class InventorySource {
    private static final InventorySource CREATIVE_SOURCE = new InventorySource(Type.CREATIVE, -1, Flag.NONE);
    private static final InventorySource GLOBAL_SOURCE = new InventorySource(Type.GLOBAL, -1, Flag.NONE);
    private static final InventorySource INVALID_SOURCE = new InventorySource(Type.INVALID, -1, Flag.NONE);
    private final int containerId;
    private final Flag flag;
    private final Type type;

    /* loaded from: classes5.dex */
    public enum Flag {
        DROP_ITEM,
        PICKUP_ITEM,
        NONE
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof InventorySource)) {
            return false;
        }
        InventorySource other = (InventorySource) o;
        if (getContainerId() != other.getContainerId()) {
            return false;
        }
        Object this$type = getType();
        Object other$type = other.getType();
        if (this$type != null ? !this$type.equals(other$type) : other$type != null) {
            return false;
        }
        Object this$flag = getFlag();
        Object other$flag = other.getFlag();
        return this$flag != null ? this$flag.equals(other$flag) : other$flag == null;
    }

    public int hashCode() {
        int result = (1 * 59) + getContainerId();
        Object $type = getType();
        int result2 = (result * 59) + ($type == null ? 43 : $type.hashCode());
        Object $flag = getFlag();
        return (result2 * 59) + ($flag != null ? $flag.hashCode() : 43);
    }

    public String toString() {
        return "InventorySource(type=" + getType() + ", containerId=" + getContainerId() + ", flag=" + getFlag() + ")";
    }

    private InventorySource(Type type, int containerId, Flag flag) {
        this.type = type;
        this.containerId = containerId;
        this.flag = flag;
    }

    public Type getType() {
        return this.type;
    }

    public int getContainerId() {
        return this.containerId;
    }

    public Flag getFlag() {
        return this.flag;
    }

    public static InventorySource fromContainerWindowId(int containerId) {
        Preconditions.checkNotNull(Integer.valueOf(containerId), "containerId");
        return new InventorySource(Type.CONTAINER, containerId, Flag.NONE);
    }

    public static InventorySource fromCreativeInventory() {
        return CREATIVE_SOURCE;
    }

    public static InventorySource fromGlobalInventory() {
        return GLOBAL_SOURCE;
    }

    public static InventorySource fromInvalid() {
        return INVALID_SOURCE;
    }

    public static InventorySource fromNonImplementedTodo(int containerId) {
        Preconditions.checkNotNull(Integer.valueOf(containerId), "containerId");
        return new InventorySource(Type.NON_IMPLEMENTED_TODO, containerId, Flag.NONE);
    }

    public static InventorySource fromUntrackedInteractionUI(int containerId) {
        Preconditions.checkNotNull(Integer.valueOf(containerId), "containerId");
        return new InventorySource(Type.UNTRACKED_INTERACTION_UI, containerId, Flag.NONE);
    }

    public static InventorySource fromWorldInteraction(Flag flag) {
        Preconditions.checkNotNull(flag, "flag");
        return new InventorySource(Type.WORLD_INTERACTION, -1, flag);
    }

    /* loaded from: classes5.dex */
    public enum Type {
        INVALID(-1),
        CONTAINER(0),
        GLOBAL(1),
        WORLD_INTERACTION(2),
        CREATIVE(3),
        UNTRACKED_INTERACTION_UI(100),
        NON_IMPLEMENTED_TODO(99999);

        private static final Int2ObjectMap<Type> BY_ID = new Int2ObjectOpenHashMap(6);
        private final int id;

        static {
            for (Type type : values()) {
                BY_ID.put(type.id, (int) type);
            }
        }

        Type(int id) {
            this.id = id;
        }

        public static Type byId(int id) {
            Type type = BY_ID.get(id);
            return type == null ? INVALID : type;
        }

        public int id() {
            return this.id;
        }
    }
}
