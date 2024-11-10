package org.cloudburstmc.protocol.bedrock.data.entity;

import io.netty.util.internal.StringUtil;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.cloudburstmc.nbt.NbtUtils;
import org.cloudburstmc.protocol.common.util.Preconditions;

/* loaded from: classes5.dex */
public final class EntityDataMap implements Map<EntityDataType<?>, Object> {
    private final Map<EntityDataType<?>, Object> map = new LinkedHashMap();

    public EnumSet<EntityFlag> getOrCreateFlags() {
        EnumSet<EntityFlag> flags = (EnumSet) get((EntityDataType) EntityDataTypes.FLAGS);
        if (flags == null) {
            flags = (EnumSet) get((EntityDataType) EntityDataTypes.FLAGS_2);
            if (flags == null) {
                flags = EnumSet.noneOf(EntityFlag.class);
            }
            putFlags(flags);
        }
        return flags;
    }

    public EnumSet<EntityFlag> getFlags() {
        return (EnumSet) get((EntityDataType) EntityDataTypes.FLAGS);
    }

    public EntityFlag setFlag(EntityFlag flag, boolean value) {
        Objects.requireNonNull(flag, "flag");
        EnumSet<EntityFlag> flags = getOrCreateFlags();
        if (value) {
            flags.add(flag);
        } else {
            flags.remove(flag);
        }
        return flag;
    }

    public EnumSet<EntityFlag> putFlags(EnumSet<EntityFlag> flags) {
        Objects.requireNonNull(flags, "flags");
        this.map.put(EntityDataTypes.FLAGS, flags);
        this.map.put(EntityDataTypes.FLAGS_2, flags);
        return flags;
    }

    public <T> T get(EntityDataType<T> entityDataType) {
        return (T) this.map.get(entityDataType);
    }

    private <T> T getOrDefault(EntityDataType<T> entityDataType, T t) {
        Objects.requireNonNull(entityDataType, "type");
        return (T) this.map.getOrDefault(entityDataType, t);
    }

    public <T> void putType(EntityDataType<T> type, T value) {
        put((EntityDataType<?>) type, (Object) value);
    }

    @Override // java.util.Map
    public int size() {
        return this.map.size();
    }

    @Override // java.util.Map
    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    @Override // java.util.Map
    public boolean containsKey(Object key) {
        return this.map.containsKey(key);
    }

    @Override // java.util.Map
    public boolean containsValue(Object value) {
        return this.map.containsValue(value);
    }

    @Override // java.util.Map
    public Object get(Object key) {
        return this.map.get(key);
    }

    @Override // java.util.Map
    public Object put(EntityDataType<?> key, Object value) {
        Preconditions.checkNotNull(key, "type");
        Preconditions.checkNotNull(value, "value was null for %s", key);
        Preconditions.checkArgument(key.isInstance(value), "value with type %s is not an instance of %s", value.getClass(), key);
        if (key == EntityDataTypes.FLAGS || key == EntityDataTypes.FLAGS_2) {
            return putFlags((EnumSet) value);
        }
        return this.map.put(key, value);
    }

    @Override // java.util.Map
    public Object remove(Object key) {
        return this.map.remove(key);
    }

    @Override // java.util.Map
    public void putAll(Map<? extends EntityDataType<?>, ? extends Object> map) {
        Preconditions.checkNotNull(map, "map");
        this.map.putAll(map);
    }

    @Override // java.util.Map
    public void clear() {
        this.map.clear();
    }

    @Override // java.util.Map
    public Set<EntityDataType<?>> keySet() {
        return this.map.keySet();
    }

    @Override // java.util.Map
    public Collection<Object> values() {
        return this.map.values();
    }

    @Override // java.util.Map
    public Set<Map.Entry<EntityDataType<?>, Object>> entrySet() {
        return this.map.entrySet();
    }

    @Override // java.util.Map
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EntityDataMap that = (EntityDataMap) o;
        return this.map.equals(that.map);
    }

    @Override // java.util.Map
    public int hashCode() {
        return this.map.hashCode();
    }

    public String toString() {
        Iterator<Map.Entry<EntityDataType<?>, Object>> i = this.map.entrySet().iterator();
        if (!i.hasNext()) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        while (i.hasNext()) {
            Map.Entry<EntityDataType<?>, Object> e = i.next();
            EntityDataType<?> key = e.getKey();
            if (key != EntityDataTypes.FLAGS_2) {
                String stringVal = NbtUtils.toString(e.getValue());
                sb.append(key.toString()).append('=').append(stringVal);
                if (!i.hasNext()) {
                    return sb.append('}').toString();
                }
                sb.append(StringUtil.COMMA).append(' ');
            }
        }
        return sb.toString();
    }
}
