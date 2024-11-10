package org.cloudburstmc.nbt;

import io.netty.util.internal.StringUtil;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;
import org.cloudburstmc.nbt.util.UnmodifiableEntrySet;
import org.cloudburstmc.nbt.util.function.BooleanConsumer;
import org.cloudburstmc.nbt.util.function.ByteConsumer;
import org.cloudburstmc.nbt.util.function.FloatConsumer;
import org.cloudburstmc.nbt.util.function.NumberConsumer;
import org.cloudburstmc.nbt.util.function.ShortConsumer;

/* loaded from: classes5.dex */
public class NbtMap extends AbstractMap<String, Object> {
    public static final NbtMap EMPTY = new NbtMap();
    private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    private static final int[] EMPTY_INT_ARRAY = new int[0];
    private static final long[] EMPTY_LONG_ARRAY = new long[0];
    private transient Set<Map.Entry<String, Object>> entrySet;
    private transient int hashCode;
    private transient boolean hashCodeGenerated;
    private transient Set<String> keySet;
    private final Map<String, Object> map;
    private transient Collection<Object> values;

    private NbtMap() {
        this.map = new LinkedHashMap();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public NbtMap(Map<String, Object> map) {
        this.map = map;
    }

    public static NbtMapBuilder builder() {
        return new NbtMapBuilder();
    }

    public static NbtMap fromMap(Map<String, Object> map) {
        return new NbtMap(Collections.unmodifiableMap(map));
    }

    public NbtMapBuilder toBuilder() {
        return NbtMapBuilder.from(this);
    }

    public boolean containsKey(String key, NbtType<?> type) {
        Object o = this.map.get(key);
        return o != null && o.getClass() == type.getTagClass();
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Object get(Object key) {
        return NbtUtils.copy(this.map.get(key));
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<String> keySet() {
        if (this.keySet == null) {
            this.keySet = Collections.unmodifiableSet(this.map.keySet());
        }
        return this.keySet;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<Map.Entry<String, Object>> entrySet() {
        if (this.entrySet == null) {
            this.entrySet = new UnmodifiableEntrySet(this.map.entrySet());
        }
        return this.entrySet;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Collection<Object> values() {
        if (this.values == null) {
            this.values = Collections.unmodifiableCollection(this.map.values());
        }
        return this.values;
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        Object tag = this.map.get(key);
        if (tag instanceof Byte) {
            return ((Byte) tag).byteValue() != 0;
        }
        return defaultValue;
    }

    public void listenForBoolean(String key, BooleanConsumer consumer) {
        Object tag = this.map.get(key);
        if (tag instanceof Byte) {
            consumer.accept(((Byte) tag).byteValue() != 0);
        }
    }

    public byte getByte(String key) {
        return getByte(key, (byte) 0);
    }

    public byte getByte(String key, byte defaultValue) {
        Object tag = this.map.get(key);
        if (tag instanceof Byte) {
            return ((Byte) tag).byteValue();
        }
        return defaultValue;
    }

    public void listenForByte(String key, ByteConsumer consumer) {
        Object tag = this.map.get(key);
        if (tag instanceof Byte) {
            consumer.accept(((Byte) tag).byteValue());
        }
    }

    public short getShort(String key) {
        return getShort(key, (short) 0);
    }

    public short getShort(String key, short defaultValue) {
        Object tag = this.map.get(key);
        if (tag instanceof Short) {
            return ((Short) tag).shortValue();
        }
        return defaultValue;
    }

    public void listenForShort(String key, ShortConsumer consumer) {
        Object tag = this.map.get(key);
        if (tag instanceof Short) {
            consumer.accept(((Short) tag).shortValue());
        }
    }

    public int getInt(String key) {
        return getInt(key, 0);
    }

    public int getInt(String key, int defaultValue) {
        Object tag = this.map.get(key);
        if (tag instanceof Integer) {
            return ((Integer) tag).intValue();
        }
        return defaultValue;
    }

    public void listenForInt(String key, IntConsumer consumer) {
        Object tag = this.map.get(key);
        if (tag instanceof Integer) {
            consumer.accept(((Integer) tag).intValue());
        }
    }

    public long getLong(String key) {
        return getLong(key, 0L);
    }

    public long getLong(String key, long defaultValue) {
        Object tag = this.map.get(key);
        if (tag instanceof Long) {
            return ((Long) tag).longValue();
        }
        return defaultValue;
    }

    public void listenForLong(String key, LongConsumer consumer) {
        Object tag = this.map.get(key);
        if (tag instanceof Long) {
            consumer.accept(((Long) tag).longValue());
        }
    }

    public float getFloat(String key) {
        return getFloat(key, 0.0f);
    }

    public float getFloat(String key, float defaultValue) {
        Object tag = this.map.get(key);
        if (tag instanceof Float) {
            return ((Float) tag).floatValue();
        }
        return defaultValue;
    }

    public void listenForFloat(String key, FloatConsumer consumer) {
        Object tag = this.map.get(key);
        if (tag instanceof Float) {
            consumer.accept(((Float) tag).floatValue());
        }
    }

    public double getDouble(String key) {
        return getDouble(key, 0.0d);
    }

    public double getDouble(String key, double defaultValue) {
        Object tag = this.map.get(key);
        if (tag instanceof Double) {
            return ((Double) tag).doubleValue();
        }
        return defaultValue;
    }

    public void listenForDouble(String key, DoubleConsumer consumer) {
        Object tag = this.map.get(key);
        if (tag instanceof Double) {
            consumer.accept(((Double) tag).doubleValue());
        }
    }

    public String getString(String key) {
        return getString(key, "");
    }

    public String getString(String key, String defaultValue) {
        Object tag = this.map.get(key);
        if (tag instanceof String) {
            return (String) tag;
        }
        return defaultValue;
    }

    public void listenForString(String key, Consumer<String> consumer) {
        Object tag = this.map.get(key);
        if (tag instanceof String) {
            consumer.accept((String) tag);
        }
    }

    public byte[] getByteArray(String key) {
        return getByteArray(key, EMPTY_BYTE_ARRAY);
    }

    public byte[] getByteArray(String key, byte[] defaultValue) {
        Object tag = this.map.get(key);
        if (tag instanceof byte[]) {
            byte[] bytes = (byte[]) tag;
            return Arrays.copyOf(bytes, bytes.length);
        }
        return defaultValue;
    }

    public void listenForByteArray(String key, Consumer<byte[]> consumer) {
        Object tag = this.map.get(key);
        if (tag instanceof byte[]) {
            byte[] bytes = (byte[]) tag;
            consumer.accept(Arrays.copyOf(bytes, bytes.length));
        }
    }

    public int[] getIntArray(String key) {
        return getIntArray(key, EMPTY_INT_ARRAY);
    }

    public int[] getIntArray(String key, int[] defaultValue) {
        Object tag = this.map.get(key);
        if (tag instanceof int[]) {
            int[] ints = (int[]) tag;
            return Arrays.copyOf(ints, ints.length);
        }
        return defaultValue;
    }

    public void listenForIntArray(String key, Consumer<int[]> consumer) {
        Object tag = this.map.get(key);
        if (tag instanceof int[]) {
            int[] ints = (int[]) tag;
            consumer.accept(Arrays.copyOf(ints, ints.length));
        }
    }

    public long[] getLongArray(String key) {
        return getLongArray(key, EMPTY_LONG_ARRAY);
    }

    public long[] getLongArray(String key, long[] defaultValue) {
        Object tag = this.map.get(key);
        if (tag instanceof long[]) {
            long[] longs = (long[]) tag;
            return Arrays.copyOf(longs, longs.length);
        }
        return defaultValue;
    }

    public void listenForLongArray(String key, Consumer<long[]> consumer) {
        Object tag = this.map.get(key);
        if (tag instanceof long[]) {
            long[] longs = (long[]) tag;
            consumer.accept(Arrays.copyOf(longs, longs.length));
        }
    }

    public NbtMap getCompound(String key) {
        return getCompound(key, EMPTY);
    }

    public NbtMap getCompound(String key, NbtMap defaultValue) {
        Object tag = this.map.get(key);
        if (tag instanceof NbtMap) {
            return (NbtMap) tag;
        }
        return defaultValue;
    }

    public void listenForCompound(String key, Consumer<NbtMap> consumer) {
        Object tag = this.map.get(key);
        if (tag instanceof NbtMap) {
            consumer.accept((NbtMap) tag);
        }
    }

    public <T> List<T> getList(String key, NbtType<T> type) {
        return getList(key, type, Collections.emptyList());
    }

    public <T> List<T> getList(String key, NbtType<T> type, List<T> defaultValue) {
        Object tag = this.map.get(key);
        if ((tag instanceof NbtList) && ((NbtList) tag).getType() == type) {
            return (NbtList) tag;
        }
        return defaultValue;
    }

    public <T> void listenForList(String key, NbtType<T> type, Consumer<List<T>> consumer) {
        Object tag = this.map.get(key);
        if ((tag instanceof NbtList) && ((NbtList) tag).getType() == type) {
            consumer.accept((NbtList) tag);
        }
    }

    public Number getNumber(String key) {
        return getNumber(key, 0);
    }

    public Number getNumber(String key, Number defaultValue) {
        Object tag = this.map.get(key);
        if (tag instanceof Number) {
            return (Number) tag;
        }
        return defaultValue;
    }

    public void listenForNumber(String key, NumberConsumer consumer) {
        Object tag = this.map.get(key);
        if (tag instanceof Number) {
            consumer.accept((Number) tag);
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Map)) {
            return false;
        }
        Map<?, ?> m = (Map) o;
        if (m.size() != size()) {
            return false;
        }
        if (this.hashCodeGenerated && (o instanceof NbtMap) && ((NbtMap) o).hashCodeGenerated && this.hashCode != ((NbtMap) o).hashCode) {
            return false;
        }
        try {
            for (Map.Entry<String, Object> e : entrySet()) {
                String key = e.getKey();
                Object value = e.getValue();
                if (value == null) {
                    if (m.get(key) != null || !m.containsKey(key)) {
                        return false;
                    }
                } else if (!Objects.deepEquals(value, m.get(key))) {
                    return false;
                }
            }
            return true;
        } catch (ClassCastException | NullPointerException e2) {
            return false;
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public int hashCode() {
        if (this.hashCodeGenerated) {
            return this.hashCode;
        }
        int h = 0;
        for (Map.Entry<String, Object> stringObjectEntry : this.map.entrySet()) {
            h += stringObjectEntry.hashCode();
        }
        this.hashCode = h;
        this.hashCodeGenerated = true;
        return h;
    }

    @Override // java.util.AbstractMap
    public String toString() {
        return mapToString(this.map);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String mapToString(Map<String, Object> map) {
        Iterator<Map.Entry<String, Object>> i = map.entrySet().iterator();
        if (!i.hasNext()) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder();
        sb.append('{').append('\n');
        while (true) {
            Map.Entry<String, Object> e = i.next();
            String key = e.getKey();
            String value = NbtUtils.toString(e.getValue());
            String string = NbtUtils.indent("\"" + key + "\": " + value);
            sb.append(string);
            if (!i.hasNext()) {
                return sb.append('\n').append('}').toString();
            }
            sb.append(StringUtil.COMMA).append('\n');
        }
    }
}
