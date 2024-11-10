package org.cloudburstmc.nbt;

import java.util.HashMap;
import java.util.Map;

/* loaded from: classes5.dex */
public final class NbtType<T> {
    private final Enum enumeration;
    private final Class<T> tagClass;
    public static final NbtType<Void> END = new NbtType<>(Void.class, Enum.END);
    public static final NbtType<Byte> BYTE = new NbtType<>(Byte.class, Enum.BYTE);
    public static final NbtType<Short> SHORT = new NbtType<>(Short.class, Enum.SHORT);
    public static final NbtType<Integer> INT = new NbtType<>(Integer.class, Enum.INT);
    public static final NbtType<Long> LONG = new NbtType<>(Long.class, Enum.LONG);
    public static final NbtType<Float> FLOAT = new NbtType<>(Float.class, Enum.FLOAT);
    public static final NbtType<Double> DOUBLE = new NbtType<>(Double.class, Enum.DOUBLE);
    public static final NbtType<byte[]> BYTE_ARRAY = new NbtType<>(byte[].class, Enum.BYTE_ARRAY);
    public static final NbtType<String> STRING = new NbtType<>(String.class, Enum.STRING);
    public static final NbtType<NbtList> LIST = new NbtType<>(NbtList.class, Enum.LIST);
    public static final NbtType<NbtMap> COMPOUND = new NbtType<>(NbtMap.class, Enum.COMPOUND);
    public static final NbtType<int[]> INT_ARRAY = new NbtType<>(int[].class, Enum.INT_ARRAY);
    public static final NbtType<long[]> LONG_ARRAY = new NbtType<>(long[].class, Enum.LONG_ARRAY);
    private static final NbtType<?>[] BY_ID = {END, BYTE, SHORT, INT, LONG, FLOAT, DOUBLE, BYTE_ARRAY, STRING, LIST, COMPOUND, INT_ARRAY, LONG_ARRAY};
    private static final Map<Class<?>, NbtType<?>> BY_CLASS = new HashMap();

    static {
        for (NbtType<?> type : BY_ID) {
            BY_CLASS.put(type.getTagClass(), type);
        }
    }

    private NbtType(Class<T> tagClass, Enum enumeration) {
        this.tagClass = tagClass;
        this.enumeration = enumeration;
    }

    public static NbtType<?> byId(int id) {
        if (id >= 0 && id < BY_ID.length) {
            return BY_ID[id];
        }
        throw new IndexOutOfBoundsException("Tag type id must be greater than 0 and less than " + (BY_ID.length - 1));
    }

    public static <T> NbtType<T> byClass(Class<T> tagClass) {
        NbtType<T> type = (NbtType) BY_CLASS.get(tagClass);
        if (type == null) {
            throw new IllegalArgumentException("Tag of class " + tagClass + " does not exist");
        }
        return type;
    }

    public Class<T> getTagClass() {
        return this.tagClass;
    }

    public int getId() {
        return this.enumeration.ordinal();
    }

    public String getTypeName() {
        return this.enumeration.getName();
    }

    public Enum getEnum() {
        return this.enumeration;
    }

    /* loaded from: classes5.dex */
    public enum Enum {
        END,
        BYTE,
        SHORT,
        INT,
        LONG,
        FLOAT,
        DOUBLE,
        BYTE_ARRAY,
        STRING,
        LIST,
        COMPOUND,
        INT_ARRAY,
        LONG_ARRAY;

        private final String name = "TAG_" + name();

        Enum() {
        }

        public String getName() {
            return this.name;
        }
    }
}
