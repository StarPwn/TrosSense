package org.cloudburstmc.nbt;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

/* loaded from: classes5.dex */
public class NbtMapBuilder extends LinkedHashMap<String, Object> {
    public static NbtMapBuilder from(NbtMap map) {
        NbtMapBuilder builder = new NbtMapBuilder();
        builder.putAll(map);
        return builder;
    }

    @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
    public Object put(String str, Object obj) {
        Objects.requireNonNull(obj, "value");
        if (obj instanceof Boolean) {
            obj = Byte.valueOf(((Boolean) obj).booleanValue() ? (byte) 1 : (byte) 0);
        }
        NbtType.byClass(obj.getClass());
        return super.put((NbtMapBuilder) str, (String) NbtUtils.copy(obj));
    }

    public NbtMapBuilder putBoolean(String str, boolean z) {
        put(str, (Object) Byte.valueOf(z ? (byte) 1 : (byte) 0));
        return this;
    }

    public NbtMapBuilder putByte(String name, byte value) {
        put(name, (Object) Byte.valueOf(value));
        return this;
    }

    public NbtMapBuilder putByteArray(String name, byte[] value) {
        put(name, (Object) value);
        return this;
    }

    public NbtMapBuilder putDouble(String name, double value) {
        put(name, (Object) Double.valueOf(value));
        return this;
    }

    public NbtMapBuilder putFloat(String name, float value) {
        put(name, (Object) Float.valueOf(value));
        return this;
    }

    public NbtMapBuilder putIntArray(String name, int[] value) {
        put(name, (Object) value);
        return this;
    }

    public NbtMapBuilder putLongArray(String name, long[] value) {
        put(name, (Object) value);
        return this;
    }

    public NbtMapBuilder putInt(String name, int value) {
        put(name, (Object) Integer.valueOf(value));
        return this;
    }

    public NbtMapBuilder putLong(String name, long value) {
        put(name, (Object) Long.valueOf(value));
        return this;
    }

    public NbtMapBuilder putShort(String name, short value) {
        put(name, (Object) Short.valueOf(value));
        return this;
    }

    public NbtMapBuilder putString(String name, String value) {
        put(name, (Object) value);
        return this;
    }

    public NbtMapBuilder putCompound(String name, NbtMap value) {
        put(name, (Object) value);
        return this;
    }

    @SafeVarargs
    public final <T> NbtMapBuilder putList(String name, NbtType<T> type, T... values) {
        put(name, (Object) new NbtList(type, values));
        return this;
    }

    public <T> NbtMapBuilder putList(String name, NbtType<T> type, List<T> list) {
        if (!(list instanceof NbtList)) {
            list = new NbtList(type, list);
        }
        put(name, (Object) list);
        return this;
    }

    public NbtMapBuilder rename(String oldName, String newName) {
        Object o = remove(oldName);
        if (o != null) {
            put(newName, o);
        }
        return this;
    }

    public NbtMap build() {
        if (isEmpty()) {
            return NbtMap.EMPTY;
        }
        return new NbtMap(new LinkedHashMap(this));
    }

    @Override // java.util.AbstractMap
    public String toString() {
        return NbtMap.mapToString(this);
    }
}
