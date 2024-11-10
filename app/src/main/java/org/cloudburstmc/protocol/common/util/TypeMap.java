package org.cloudburstmc.protocol.common.util;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import it.unimi.dsi.fastutil.ints.Int2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import org.cloudburstmc.protocol.common.util.TypeMap;

/* loaded from: classes5.dex */
public final class TypeMap<T> {
    private static final InternalLogger log = InternalLoggerFactory.getInstance((Class<?>) TypeMap.class);
    private final Object2IntMap<T> toId;
    private final Int2ObjectMap<T> toObject;
    private final String type;

    private TypeMap(String type, Object2IntMap<T> toId, Int2ObjectMap<T> toObject) {
        this.type = type;
        this.toId = toId;
        this.toObject = toObject;
    }

    public int getId(T value) {
        Preconditions.checkNotNull(value, "value");
        int index = this.toId.getInt(value);
        Preconditions.checkArgument(index != -1, "No id found for %s", value);
        return index;
    }

    public int getIdUnsafe(T value) {
        Preconditions.checkNotNull(value, "value");
        return this.toId.getInt(value);
    }

    public T getType(int id) {
        T value = this.toObject.get(id);
        Preconditions.checkNotNull((Object) value, "type null for id %s", id);
        return value;
    }

    public T getTypeUnsafe(int id) {
        return this.toObject.get(id);
    }

    public Builder<T> toBuilder() {
        final Builder<T> builder = new Builder<>(this.type);
        Int2ObjectMap<T> int2ObjectMap = this.toObject;
        Objects.requireNonNull(builder);
        int2ObjectMap.forEach(new BiConsumer() { // from class: org.cloudburstmc.protocol.common.util.TypeMap$$ExternalSyntheticLambda0
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                TypeMap.Builder.this.insert(((Integer) obj).intValue(), (int) obj2);
            }
        });
        return builder;
    }

    public void forEach(BiConsumer<Integer, T> consumer) {
        this.toObject.forEach(consumer);
    }

    public static <T> Builder<T> builder(Class<T> typeClass) {
        return new Builder<>(typeClass.getSimpleName());
    }

    public static <T> Builder<T> builder(String type) {
        return new Builder<>(type);
    }

    public static <T> TypeMap<T> empty(Class<T> typeClass) {
        return empty(typeClass.getSimpleName());
    }

    public static <T> TypeMap<T> empty(String type) {
        return new TypeMap<>(type, Object2IntMaps.emptyMap(), Int2ObjectMaps.emptyMap());
    }

    private static int powerOfTwoCeiling(int value) {
        int value2 = value - 1;
        int value3 = value2 | (value2 >> 1);
        int value4 = value3 | (value3 >> 2);
        int value5 = value4 | (value4 >> 4);
        int value6 = value5 | (value5 >> 8);
        return (value6 | (value6 >> 16)) + 1;
    }

    public String prettyPrint() {
        TreeMap<Integer, T> map = new TreeMap<>(this.toObject);
        StringJoiner joiner = new StringJoiner("\n");
        for (Map.Entry<Integer, T> entry : map.entrySet()) {
            joiner.add(entry.getKey() + " => " + entry.getValue());
        }
        return joiner.toString();
    }

    public static <T extends Enum<T>> TypeMap<T> fromEnum(Class<T> clazz) {
        return fromEnum(clazz, -1);
    }

    public static <T extends Enum<T>> TypeMap<T> fromEnum(Class<T> clazz, int maxIndex) {
        EnumSet<T> values = EnumSet.allOf(clazz);
        Builder<T> builder = builder(clazz);
        Iterator var4 = values.iterator();
        while (var4.hasNext()) {
            Enum r3 = (Enum) var4.next();
            if (maxIndex != -1 && r3.ordinal() > maxIndex) {
                break;
            }
            builder.insert(r3.ordinal(), (int) r3);
        }
        return builder.build();
    }

    /* loaded from: classes5.dex */
    public static class Builder<T> {
        private final String type;
        private final Int2ObjectAVLTreeMap<Object> types;

        public Builder<T> insert(int index, T value) {
            Preconditions.checkNotNull(value, "value");
            Preconditions.checkArgument(this.types.get(index) == null, "Cannot insert into non-null value at index " + index);
            this.types.put(index, (int) value);
            return this;
        }

        public Builder<T> shift(int startIndex, int amount) {
            Int2ObjectSortedMap<Object> shiftMap = this.types.tailMap(startIndex);
            Int2ObjectArrayMap<Object> tmp = new Int2ObjectArrayMap<>(shiftMap.size());
            ObjectBidirectionalIterator var5 = shiftMap.int2ObjectEntrySet().iterator();
            while (var5.hasNext()) {
                Int2ObjectMap.Entry<Object> entry = (Int2ObjectMap.Entry) var5.next();
                tmp.put(entry.getIntKey() + amount, (int) entry.getValue());
                this.types.put(entry.getIntKey(), (int) null);
            }
            this.types.putAll(tmp);
            return this;
        }

        public Builder<T> replace(int index, T value) {
            Preconditions.checkNotNull(value, "value");
            Preconditions.checkArgument(this.types.get(index) != null, "Cannot update null value");
            this.types.put(index, (int) value);
            return this;
        }

        public Builder<T> update(int oldIndex, int newIndex, T value) {
            Preconditions.checkNotNull(value, "value");
            Preconditions.checkArgument(this.types.get(oldIndex) == value, "oldIndex value does not equal expected");
            this.types.remove(oldIndex);
            this.types.put(newIndex, (int) value);
            return this;
        }

        public Builder<T> insert(final int offset, TypeMap<? extends T> map) {
            Preconditions.checkNotNull(map, "map");
            ((TypeMap) map).toObject.forEach(new BiConsumer() { // from class: org.cloudburstmc.protocol.common.util.TypeMap$Builder$$ExternalSyntheticLambda0
                @Override // java.util.function.BiConsumer
                public final void accept(Object obj, Object obj2) {
                    TypeMap.Builder.this.m2105x5ffb247c(offset, (Integer) obj, obj2);
                }
            });
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$insert$0$org-cloudburstmc-protocol-common-util-TypeMap$Builder, reason: not valid java name */
        public /* synthetic */ void m2105x5ffb247c(int offset, Integer index, Object value) {
            int newIndex = index.intValue() + offset;
            Preconditions.checkNotNull(value, "value");
            this.types.put(newIndex, (int) value);
        }

        public Builder<T> remove(int index) {
            this.types.remove(index);
            return this;
        }

        public TypeMap<T> build() {
            Object2IntOpenHashMap object2IntOpenHashMap = new Object2IntOpenHashMap();
            object2IntOpenHashMap.defaultReturnValue(-1);
            Int2ObjectOpenHashMap int2ObjectOpenHashMap = new Int2ObjectOpenHashMap();
            ObjectBidirectionalIterator var3 = this.types.int2ObjectEntrySet().iterator();
            while (var3.hasNext()) {
                Int2ObjectMap.Entry<Object> entry = (Int2ObjectMap.Entry) var3.next();
                Object type = entry.getValue();
                if (type != null) {
                    object2IntOpenHashMap.put((Object2IntOpenHashMap) type, entry.getIntKey());
                    int2ObjectOpenHashMap.put(entry.getIntKey(), (int) type);
                }
            }
            return new TypeMap<>(this.type, object2IntOpenHashMap, int2ObjectOpenHashMap);
        }

        private Builder(String type) {
            this.types = new Int2ObjectAVLTreeMap<>();
            this.type = type;
        }
    }
}
