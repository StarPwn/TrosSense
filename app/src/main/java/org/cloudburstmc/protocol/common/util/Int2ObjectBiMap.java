package org.cloudburstmc.protocol.common.util;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.Objects;
import java.util.function.ObjIntConsumer;

/* loaded from: classes5.dex */
public class Int2ObjectBiMap<T> {
    private final Object2IntMap<T> backwards;
    private final Int2ObjectMap<T> forwards;

    public Int2ObjectBiMap() {
        this(2);
    }

    public Int2ObjectBiMap(int initialCapacity) {
        this(initialCapacity, 0.5f);
    }

    public Int2ObjectBiMap(T noEntryValue) {
        this(2, 0.5f, -1, noEntryValue);
    }

    public Int2ObjectBiMap(int initialCapacity, float loadFactor) {
        this(initialCapacity, loadFactor, -1);
    }

    public Int2ObjectBiMap(int initialCapacity, float loadFactor, int noEntryKey) {
        this(initialCapacity, loadFactor, noEntryKey, null);
    }

    public Int2ObjectBiMap(int initialCapacity, float loadFactor, int noEntryKey, T noEntryValue) {
        this.forwards = new Int2ObjectOpenHashMap(initialCapacity, loadFactor);
        this.backwards = new Object2IntOpenHashMap(initialCapacity, loadFactor);
        this.forwards.defaultReturnValue(noEntryValue);
        this.backwards.defaultReturnValue(noEntryKey);
    }

    public T get(int key) {
        T value = this.forwards.get(key);
        if (value == null) {
            return this.forwards.defaultReturnValue();
        }
        return value;
    }

    public int get(T value) {
        return this.backwards.get((Object) value).intValue();
    }

    public void put(int key, T value) {
        Preconditions.checkNotNull(value, "value");
        this.forwards.put(key, (int) value);
        this.backwards.put((Object2IntMap<T>) value, key);
    }

    public boolean containsKey(int key) {
        return this.forwards.containsKey(key);
    }

    public boolean containsValue(T value) {
        return this.forwards.containsValue(value);
    }

    public boolean remove(int key) {
        if (!this.forwards.containsKey(key)) {
            return false;
        }
        T value = this.forwards.get(key);
        if (!this.backwards.containsKey(value)) {
            return false;
        }
        this.forwards.remove(key);
        this.backwards.removeInt(value);
        return true;
    }

    public boolean remove(T t) {
        if (!this.backwards.containsKey(t)) {
            return false;
        }
        int value = this.backwards.getInt(t);
        if (!this.forwards.containsKey(value)) {
            return false;
        }
        this.backwards.removeInt(t);
        this.forwards.remove(value);
        return true;
    }

    public void forEach(ObjIntConsumer<T> objIntConsumer) {
        ObjectIterator it2 = Int2ObjectMaps.fastIterable(this.forwards).iterator();
        while (it2.hasNext()) {
            Int2ObjectMap.Entry<T> entry = (Int2ObjectMap.Entry) it2.next();
            objIntConsumer.accept(entry.getValue(), entry.getIntKey());
        }
    }

    public int hashCode() {
        return this.forwards.hashCode();
    }

    public String toString() {
        return this.forwards.toString();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Int2ObjectBiMap<?> that = (Int2ObjectBiMap) o;
        if (Objects.equals(this.forwards, that.forwards) && Objects.equals(this.backwards, that.backwards)) {
            return true;
        }
        return false;
    }
}
