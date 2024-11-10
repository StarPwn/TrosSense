package io.netty.handler.codec;

import io.netty.handler.codec.Headers;
import io.netty.util.HashingStrategy;
import io.netty.util.internal.MathUtil;
import io.netty.util.internal.ObjectUtil;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/* loaded from: classes4.dex */
public class DefaultHeaders<K, V, T extends Headers<K, V, T>> implements Headers<K, V, T> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    static final int HASH_CODE_SEED = -1028477387;
    private final HeaderEntry<K, V>[] entries;
    private final byte hashMask;
    private final HashingStrategy<K> hashingStrategy;
    protected final HeaderEntry<K, V> head;
    private final NameValidator<K> nameValidator;
    int size;
    private final ValueConverter<V> valueConverter;
    private final ValueValidator<V> valueValidator;

    /* loaded from: classes4.dex */
    public interface NameValidator<K> {
        public static final NameValidator NOT_NULL = new NameValidator() { // from class: io.netty.handler.codec.DefaultHeaders.NameValidator.1
            @Override // io.netty.handler.codec.DefaultHeaders.NameValidator
            public void validateName(Object name) {
                ObjectUtil.checkNotNull(name, "name");
            }
        };

        void validateName(K k);
    }

    /* loaded from: classes4.dex */
    public interface ValueValidator<V> {
        public static final ValueValidator<?> NO_VALIDATION = new ValueValidator<Object>() { // from class: io.netty.handler.codec.DefaultHeaders.ValueValidator.1
            @Override // io.netty.handler.codec.DefaultHeaders.ValueValidator
            public void validate(Object value) {
            }
        };

        void validate(V v);
    }

    public DefaultHeaders(ValueConverter<V> valueConverter) {
        this(HashingStrategy.JAVA_HASHER, valueConverter);
    }

    public DefaultHeaders(ValueConverter<V> valueConverter, NameValidator<K> nameValidator) {
        this(HashingStrategy.JAVA_HASHER, valueConverter, nameValidator);
    }

    public DefaultHeaders(HashingStrategy<K> nameHashingStrategy, ValueConverter<V> valueConverter) {
        this(nameHashingStrategy, valueConverter, NameValidator.NOT_NULL);
    }

    public DefaultHeaders(HashingStrategy<K> nameHashingStrategy, ValueConverter<V> valueConverter, NameValidator<K> nameValidator) {
        this(nameHashingStrategy, valueConverter, nameValidator, 16);
    }

    public DefaultHeaders(HashingStrategy<K> nameHashingStrategy, ValueConverter<V> valueConverter, NameValidator<K> nameValidator, int arraySizeHint) {
        this(nameHashingStrategy, valueConverter, nameValidator, arraySizeHint, ValueValidator.NO_VALIDATION);
    }

    public DefaultHeaders(HashingStrategy<K> nameHashingStrategy, ValueConverter<V> valueConverter, NameValidator<K> nameValidator, int arraySizeHint, ValueValidator<V> valueValidator) {
        this.valueConverter = (ValueConverter) ObjectUtil.checkNotNull(valueConverter, "valueConverter");
        this.nameValidator = (NameValidator) ObjectUtil.checkNotNull(nameValidator, "nameValidator");
        this.hashingStrategy = (HashingStrategy) ObjectUtil.checkNotNull(nameHashingStrategy, "nameHashingStrategy");
        this.valueValidator = (ValueValidator) ObjectUtil.checkNotNull(valueValidator, "valueValidator");
        this.entries = new HeaderEntry[MathUtil.findNextPositivePowerOfTwo(Math.max(2, Math.min(arraySizeHint, 128)))];
        this.hashMask = (byte) (this.entries.length - 1);
        this.head = new HeaderEntry<>();
    }

    @Override // io.netty.handler.codec.Headers
    public V get(K name) {
        ObjectUtil.checkNotNull(name, "name");
        int h = this.hashingStrategy.hashCode(name);
        int i = index(h);
        V value = null;
        for (HeaderEntry<K, V> e = this.entries[i]; e != null; e = e.next) {
            if (e.hash == h && this.hashingStrategy.equals(name, e.key)) {
                value = e.value;
            }
        }
        return value;
    }

    @Override // io.netty.handler.codec.Headers
    public V get(K name, V defaultValue) {
        V value = get(name);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // io.netty.handler.codec.Headers
    public V getAndRemove(K k) {
        int hashCode = this.hashingStrategy.hashCode(k);
        return (V) remove0(hashCode, index(hashCode), ObjectUtil.checkNotNull(k, "name"));
    }

    @Override // io.netty.handler.codec.Headers
    public V getAndRemove(K name, V defaultValue) {
        V value = getAndRemove(name);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    @Override // io.netty.handler.codec.Headers
    public List<V> getAll(K name) {
        ObjectUtil.checkNotNull(name, "name");
        LinkedList<V> values = new LinkedList<>();
        int h = this.hashingStrategy.hashCode(name);
        int i = index(h);
        for (HeaderEntry<K, V> e = this.entries[i]; e != null; e = e.next) {
            if (e.hash == h && this.hashingStrategy.equals(name, e.key)) {
                values.addFirst(e.getValue());
            }
        }
        return values;
    }

    public Iterator<V> valueIterator(K name) {
        return new ValueIterator(name);
    }

    @Override // io.netty.handler.codec.Headers
    public List<V> getAllAndRemove(K name) {
        List<V> all = getAll(name);
        remove(name);
        return all;
    }

    @Override // io.netty.handler.codec.Headers
    public boolean contains(K name) {
        return get(name) != null;
    }

    @Override // io.netty.handler.codec.Headers
    public boolean containsObject(K name, Object value) {
        return contains(name, fromObject(name, value));
    }

    @Override // io.netty.handler.codec.Headers
    public boolean containsBoolean(K name, boolean value) {
        return contains(name, fromBoolean(name, value));
    }

    @Override // io.netty.handler.codec.Headers
    public boolean containsByte(K name, byte value) {
        return contains(name, fromByte(name, value));
    }

    @Override // io.netty.handler.codec.Headers
    public boolean containsChar(K name, char value) {
        return contains(name, fromChar(name, value));
    }

    @Override // io.netty.handler.codec.Headers
    public boolean containsShort(K name, short value) {
        return contains(name, fromShort(name, value));
    }

    @Override // io.netty.handler.codec.Headers
    public boolean containsInt(K name, int value) {
        return contains(name, fromInt(name, value));
    }

    @Override // io.netty.handler.codec.Headers
    public boolean containsLong(K name, long value) {
        return contains(name, fromLong(name, value));
    }

    @Override // io.netty.handler.codec.Headers
    public boolean containsFloat(K name, float value) {
        return contains(name, fromFloat(name, value));
    }

    @Override // io.netty.handler.codec.Headers
    public boolean containsDouble(K name, double value) {
        return contains(name, fromDouble(name, value));
    }

    @Override // io.netty.handler.codec.Headers
    public boolean containsTimeMillis(K name, long value) {
        return contains(name, fromTimeMillis(name, value));
    }

    @Override // io.netty.handler.codec.Headers
    public boolean contains(K name, V value) {
        return contains(name, value, HashingStrategy.JAVA_HASHER);
    }

    public final boolean contains(K k, V v, HashingStrategy<? super V> hashingStrategy) {
        ObjectUtil.checkNotNull(k, "name");
        int hashCode = this.hashingStrategy.hashCode(k);
        for (HeaderEntry<K, V> headerEntry = this.entries[index(hashCode)]; headerEntry != null; headerEntry = headerEntry.next) {
            if (headerEntry.hash == hashCode && this.hashingStrategy.equals(k, headerEntry.key) && hashingStrategy.equals(v, headerEntry.value)) {
                return true;
            }
        }
        return false;
    }

    @Override // io.netty.handler.codec.Headers
    public int size() {
        return this.size;
    }

    @Override // io.netty.handler.codec.Headers
    public boolean isEmpty() {
        return this.head == this.head.after;
    }

    @Override // io.netty.handler.codec.Headers
    public Set<K> names() {
        if (isEmpty()) {
            return Collections.emptySet();
        }
        Set<K> names = new LinkedHashSet<>(size());
        for (HeaderEntry<K, V> e = this.head.after; e != this.head; e = e.after) {
            names.add(e.getKey());
        }
        return names;
    }

    @Override // io.netty.handler.codec.Headers
    public T add(K name, V value) {
        validateName(this.nameValidator, true, name);
        validateValue(this.valueValidator, name, value);
        ObjectUtil.checkNotNull(value, "value");
        int h = this.hashingStrategy.hashCode(name);
        int i = index(h);
        add0(h, i, name, value);
        return thisT();
    }

    @Override // io.netty.handler.codec.Headers
    public T add(K name, Iterable<? extends V> values) {
        validateName(this.nameValidator, true, name);
        int h = this.hashingStrategy.hashCode(name);
        int i = index(h);
        for (V v : values) {
            validateValue(this.valueValidator, name, v);
            add0(h, i, name, v);
        }
        return thisT();
    }

    @Override // io.netty.handler.codec.Headers
    public T add(K name, V... values) {
        validateName(this.nameValidator, true, name);
        int h = this.hashingStrategy.hashCode(name);
        int i = index(h);
        for (V v : values) {
            validateValue(this.valueValidator, name, v);
            add0(h, i, name, v);
        }
        return thisT();
    }

    @Override // io.netty.handler.codec.Headers
    public T addObject(K name, Object value) {
        return add((DefaultHeaders<K, V, T>) name, (K) fromObject(name, value));
    }

    @Override // io.netty.handler.codec.Headers
    public T addObject(K name, Iterable<?> values) {
        for (Object value : values) {
            addObject((DefaultHeaders<K, V, T>) name, value);
        }
        return thisT();
    }

    @Override // io.netty.handler.codec.Headers
    public T addObject(K name, Object... values) {
        for (Object value : values) {
            addObject((DefaultHeaders<K, V, T>) name, value);
        }
        return thisT();
    }

    @Override // io.netty.handler.codec.Headers
    public T addInt(K name, int value) {
        return add((DefaultHeaders<K, V, T>) name, (K) fromInt(name, value));
    }

    @Override // io.netty.handler.codec.Headers
    public T addLong(K name, long value) {
        return add((DefaultHeaders<K, V, T>) name, (K) fromLong(name, value));
    }

    @Override // io.netty.handler.codec.Headers
    public T addDouble(K name, double value) {
        return add((DefaultHeaders<K, V, T>) name, (K) fromDouble(name, value));
    }

    @Override // io.netty.handler.codec.Headers
    public T addTimeMillis(K name, long value) {
        return add((DefaultHeaders<K, V, T>) name, (K) fromTimeMillis(name, value));
    }

    @Override // io.netty.handler.codec.Headers
    public T addChar(K name, char value) {
        return add((DefaultHeaders<K, V, T>) name, (K) fromChar(name, value));
    }

    @Override // io.netty.handler.codec.Headers
    public T addBoolean(K name, boolean value) {
        return add((DefaultHeaders<K, V, T>) name, (K) fromBoolean(name, value));
    }

    @Override // io.netty.handler.codec.Headers
    public T addFloat(K name, float value) {
        return add((DefaultHeaders<K, V, T>) name, (K) fromFloat(name, value));
    }

    @Override // io.netty.handler.codec.Headers
    public T addByte(K name, byte value) {
        return add((DefaultHeaders<K, V, T>) name, (K) fromByte(name, value));
    }

    @Override // io.netty.handler.codec.Headers
    public T addShort(K name, short value) {
        return add((DefaultHeaders<K, V, T>) name, (K) fromShort(name, value));
    }

    @Override // io.netty.handler.codec.Headers
    public T add(Headers<? extends K, ? extends V, ?> headers) {
        if (headers == this) {
            throw new IllegalArgumentException("can't add to itself.");
        }
        addImpl(headers);
        return thisT();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void addImpl(Headers<? extends K, ? extends V, ?> headers) {
        if (headers instanceof DefaultHeaders) {
            DefaultHeaders<? extends K, ? extends V, T> defaultHeaders = (DefaultHeaders) headers;
            HeaderEntry<K, V> headerEntry = defaultHeaders.head.after;
            if (defaultHeaders.hashingStrategy == this.hashingStrategy && defaultHeaders.nameValidator == this.nameValidator) {
                while (headerEntry != defaultHeaders.head) {
                    add0(headerEntry.hash, index(headerEntry.hash), headerEntry.key, headerEntry.value);
                    headerEntry = headerEntry.after;
                }
                return;
            } else {
                while (headerEntry != defaultHeaders.head) {
                    add((DefaultHeaders<K, V, T>) headerEntry.key, (K) headerEntry.value);
                    headerEntry = headerEntry.after;
                }
                return;
            }
        }
        for (Map.Entry<? extends K, ? extends V> header : headers) {
            add((DefaultHeaders<K, V, T>) header.getKey(), (K) header.getValue());
        }
    }

    @Override // io.netty.handler.codec.Headers
    public T set(K name, V value) {
        validateName(this.nameValidator, false, name);
        validateValue(this.valueValidator, name, value);
        ObjectUtil.checkNotNull(value, "value");
        int h = this.hashingStrategy.hashCode(name);
        int i = index(h);
        remove0(h, i, name);
        add0(h, i, name, value);
        return thisT();
    }

    @Override // io.netty.handler.codec.Headers
    public T set(K name, Iterable<? extends V> values) {
        V v;
        validateName(this.nameValidator, false, name);
        ObjectUtil.checkNotNull(values, "values");
        int h = this.hashingStrategy.hashCode(name);
        int i = index(h);
        remove0(h, i, name);
        Iterator<? extends V> it2 = values.iterator();
        while (it2.hasNext() && (v = it2.next()) != null) {
            validateValue(this.valueValidator, name, v);
            add0(h, i, name, v);
        }
        return thisT();
    }

    @Override // io.netty.handler.codec.Headers
    public T set(K name, V... values) {
        validateName(this.nameValidator, false, name);
        ObjectUtil.checkNotNull(values, "values");
        int h = this.hashingStrategy.hashCode(name);
        int i = index(h);
        remove0(h, i, name);
        for (V v : values) {
            if (v == null) {
                break;
            }
            validateValue(this.valueValidator, name, v);
            add0(h, i, name, v);
        }
        return thisT();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // io.netty.handler.codec.Headers
    public T setObject(K k, Object obj) {
        return (T) set((DefaultHeaders<K, V, T>) k, (K) ObjectUtil.checkNotNull(fromObject(k, obj), "convertedValue"));
    }

    @Override // io.netty.handler.codec.Headers
    public T setObject(K name, Iterable<?> values) {
        Object v;
        validateName(this.nameValidator, false, name);
        int h = this.hashingStrategy.hashCode(name);
        int i = index(h);
        remove0(h, i, name);
        Iterator<?> it2 = values.iterator();
        while (it2.hasNext() && (v = it2.next()) != null) {
            V converted = fromObject(name, v);
            validateValue(this.valueValidator, name, converted);
            add0(h, i, name, converted);
        }
        return thisT();
    }

    @Override // io.netty.handler.codec.Headers
    public T setObject(K name, Object... values) {
        validateName(this.nameValidator, false, name);
        int h = this.hashingStrategy.hashCode(name);
        int i = index(h);
        remove0(h, i, name);
        for (Object v : values) {
            if (v == null) {
                break;
            }
            V converted = fromObject(name, v);
            validateValue(this.valueValidator, name, converted);
            add0(h, i, name, converted);
        }
        return thisT();
    }

    @Override // io.netty.handler.codec.Headers
    public T setInt(K name, int value) {
        return set((DefaultHeaders<K, V, T>) name, (K) fromInt(name, value));
    }

    @Override // io.netty.handler.codec.Headers
    public T setLong(K name, long value) {
        return set((DefaultHeaders<K, V, T>) name, (K) fromLong(name, value));
    }

    @Override // io.netty.handler.codec.Headers
    public T setDouble(K name, double value) {
        return set((DefaultHeaders<K, V, T>) name, (K) fromDouble(name, value));
    }

    @Override // io.netty.handler.codec.Headers
    public T setTimeMillis(K name, long value) {
        return set((DefaultHeaders<K, V, T>) name, (K) fromTimeMillis(name, value));
    }

    @Override // io.netty.handler.codec.Headers
    public T setFloat(K name, float value) {
        return set((DefaultHeaders<K, V, T>) name, (K) fromFloat(name, value));
    }

    @Override // io.netty.handler.codec.Headers
    public T setChar(K name, char value) {
        return set((DefaultHeaders<K, V, T>) name, (K) fromChar(name, value));
    }

    @Override // io.netty.handler.codec.Headers
    public T setBoolean(K name, boolean value) {
        return set((DefaultHeaders<K, V, T>) name, (K) fromBoolean(name, value));
    }

    @Override // io.netty.handler.codec.Headers
    public T setByte(K name, byte value) {
        return set((DefaultHeaders<K, V, T>) name, (K) fromByte(name, value));
    }

    @Override // io.netty.handler.codec.Headers
    public T setShort(K name, short value) {
        return set((DefaultHeaders<K, V, T>) name, (K) fromShort(name, value));
    }

    @Override // io.netty.handler.codec.Headers
    public T set(Headers<? extends K, ? extends V, ?> headers) {
        if (headers != this) {
            clear();
            addImpl(headers);
        }
        return thisT();
    }

    @Override // io.netty.handler.codec.Headers
    public T setAll(Headers<? extends K, ? extends V, ?> headers) {
        if (headers != this) {
            for (K key : headers.names()) {
                remove(key);
            }
            addImpl(headers);
        }
        return thisT();
    }

    @Override // io.netty.handler.codec.Headers
    public boolean remove(K name) {
        return getAndRemove(name) != null;
    }

    @Override // io.netty.handler.codec.Headers
    public T clear() {
        Arrays.fill(this.entries, (Object) null);
        HeaderEntry<K, V> headerEntry = this.head;
        HeaderEntry<K, V> headerEntry2 = this.head;
        HeaderEntry<K, V> headerEntry3 = this.head;
        headerEntry2.after = headerEntry3;
        headerEntry.before = headerEntry3;
        this.size = 0;
        return thisT();
    }

    @Override // io.netty.handler.codec.Headers, java.lang.Iterable
    public Iterator<Map.Entry<K, V>> iterator() {
        return new HeaderIterator();
    }

    @Override // io.netty.handler.codec.Headers
    public Boolean getBoolean(K name) {
        V v = get(name);
        if (v != null) {
            try {
                return Boolean.valueOf(toBoolean(name, v));
            } catch (RuntimeException e) {
                return null;
            }
        }
        return null;
    }

    @Override // io.netty.handler.codec.Headers
    public boolean getBoolean(K name, boolean defaultValue) {
        Boolean v = getBoolean(name);
        return v != null ? v.booleanValue() : defaultValue;
    }

    @Override // io.netty.handler.codec.Headers
    public Byte getByte(K name) {
        V v = get(name);
        if (v != null) {
            try {
                return Byte.valueOf(toByte(name, v));
            } catch (RuntimeException e) {
                return null;
            }
        }
        return null;
    }

    @Override // io.netty.handler.codec.Headers
    public byte getByte(K name, byte defaultValue) {
        Byte v = getByte(name);
        return v != null ? v.byteValue() : defaultValue;
    }

    @Override // io.netty.handler.codec.Headers
    public Character getChar(K name) {
        V v = get(name);
        if (v != null) {
            try {
                return Character.valueOf(toChar(name, v));
            } catch (RuntimeException e) {
                return null;
            }
        }
        return null;
    }

    @Override // io.netty.handler.codec.Headers
    public char getChar(K name, char defaultValue) {
        Character v = getChar(name);
        return v != null ? v.charValue() : defaultValue;
    }

    @Override // io.netty.handler.codec.Headers
    public Short getShort(K name) {
        V v = get(name);
        if (v != null) {
            try {
                return Short.valueOf(toShort(name, v));
            } catch (RuntimeException e) {
                return null;
            }
        }
        return null;
    }

    @Override // io.netty.handler.codec.Headers
    public short getShort(K name, short defaultValue) {
        Short v = getShort(name);
        return v != null ? v.shortValue() : defaultValue;
    }

    @Override // io.netty.handler.codec.Headers
    public Integer getInt(K name) {
        V v = get(name);
        if (v != null) {
            try {
                return Integer.valueOf(toInt(name, v));
            } catch (RuntimeException e) {
                return null;
            }
        }
        return null;
    }

    @Override // io.netty.handler.codec.Headers
    public int getInt(K name, int defaultValue) {
        Integer v = getInt(name);
        return v != null ? v.intValue() : defaultValue;
    }

    @Override // io.netty.handler.codec.Headers
    public Long getLong(K name) {
        V v = get(name);
        if (v != null) {
            try {
                return Long.valueOf(toLong(name, v));
            } catch (RuntimeException e) {
                return null;
            }
        }
        return null;
    }

    @Override // io.netty.handler.codec.Headers
    public long getLong(K name, long defaultValue) {
        Long v = getLong(name);
        return v != null ? v.longValue() : defaultValue;
    }

    @Override // io.netty.handler.codec.Headers
    public Float getFloat(K name) {
        V v = get(name);
        if (v != null) {
            try {
                return Float.valueOf(toFloat(name, v));
            } catch (RuntimeException e) {
                return null;
            }
        }
        return null;
    }

    @Override // io.netty.handler.codec.Headers
    public float getFloat(K name, float defaultValue) {
        Float v = getFloat(name);
        return v != null ? v.floatValue() : defaultValue;
    }

    @Override // io.netty.handler.codec.Headers
    public Double getDouble(K name) {
        V v = get(name);
        if (v != null) {
            try {
                return Double.valueOf(toDouble(name, v));
            } catch (RuntimeException e) {
                return null;
            }
        }
        return null;
    }

    @Override // io.netty.handler.codec.Headers
    public double getDouble(K name, double defaultValue) {
        Double v = getDouble(name);
        return v != null ? v.doubleValue() : defaultValue;
    }

    @Override // io.netty.handler.codec.Headers
    public Long getTimeMillis(K name) {
        V v = get(name);
        if (v != null) {
            try {
                return Long.valueOf(toTimeMillis(name, v));
            } catch (RuntimeException e) {
                return null;
            }
        }
        return null;
    }

    @Override // io.netty.handler.codec.Headers
    public long getTimeMillis(K name, long defaultValue) {
        Long v = getTimeMillis(name);
        return v != null ? v.longValue() : defaultValue;
    }

    @Override // io.netty.handler.codec.Headers
    public Boolean getBooleanAndRemove(K name) {
        V v = getAndRemove(name);
        if (v != null) {
            try {
                return Boolean.valueOf(toBoolean(name, v));
            } catch (RuntimeException e) {
                return null;
            }
        }
        return null;
    }

    @Override // io.netty.handler.codec.Headers
    public boolean getBooleanAndRemove(K name, boolean defaultValue) {
        Boolean v = getBooleanAndRemove(name);
        return v != null ? v.booleanValue() : defaultValue;
    }

    @Override // io.netty.handler.codec.Headers
    public Byte getByteAndRemove(K name) {
        V v = getAndRemove(name);
        if (v != null) {
            try {
                return Byte.valueOf(toByte(name, v));
            } catch (RuntimeException e) {
                return null;
            }
        }
        return null;
    }

    @Override // io.netty.handler.codec.Headers
    public byte getByteAndRemove(K name, byte defaultValue) {
        Byte v = getByteAndRemove(name);
        return v != null ? v.byteValue() : defaultValue;
    }

    @Override // io.netty.handler.codec.Headers
    public Character getCharAndRemove(K name) {
        V v = getAndRemove(name);
        if (v != null) {
            try {
                return Character.valueOf(toChar(name, v));
            } catch (RuntimeException e) {
                return null;
            }
        }
        return null;
    }

    @Override // io.netty.handler.codec.Headers
    public char getCharAndRemove(K name, char defaultValue) {
        Character v = getCharAndRemove(name);
        return v != null ? v.charValue() : defaultValue;
    }

    @Override // io.netty.handler.codec.Headers
    public Short getShortAndRemove(K name) {
        V v = getAndRemove(name);
        if (v != null) {
            try {
                return Short.valueOf(toShort(name, v));
            } catch (RuntimeException e) {
                return null;
            }
        }
        return null;
    }

    @Override // io.netty.handler.codec.Headers
    public short getShortAndRemove(K name, short defaultValue) {
        Short v = getShortAndRemove(name);
        return v != null ? v.shortValue() : defaultValue;
    }

    @Override // io.netty.handler.codec.Headers
    public Integer getIntAndRemove(K name) {
        V v = getAndRemove(name);
        if (v != null) {
            try {
                return Integer.valueOf(toInt(name, v));
            } catch (RuntimeException e) {
                return null;
            }
        }
        return null;
    }

    @Override // io.netty.handler.codec.Headers
    public int getIntAndRemove(K name, int defaultValue) {
        Integer v = getIntAndRemove(name);
        return v != null ? v.intValue() : defaultValue;
    }

    @Override // io.netty.handler.codec.Headers
    public Long getLongAndRemove(K name) {
        V v = getAndRemove(name);
        if (v != null) {
            try {
                return Long.valueOf(toLong(name, v));
            } catch (RuntimeException e) {
                return null;
            }
        }
        return null;
    }

    @Override // io.netty.handler.codec.Headers
    public long getLongAndRemove(K name, long defaultValue) {
        Long v = getLongAndRemove(name);
        return v != null ? v.longValue() : defaultValue;
    }

    @Override // io.netty.handler.codec.Headers
    public Float getFloatAndRemove(K name) {
        V v = getAndRemove(name);
        if (v != null) {
            try {
                return Float.valueOf(toFloat(name, v));
            } catch (RuntimeException e) {
                return null;
            }
        }
        return null;
    }

    @Override // io.netty.handler.codec.Headers
    public float getFloatAndRemove(K name, float defaultValue) {
        Float v = getFloatAndRemove(name);
        return v != null ? v.floatValue() : defaultValue;
    }

    @Override // io.netty.handler.codec.Headers
    public Double getDoubleAndRemove(K name) {
        V v = getAndRemove(name);
        if (v != null) {
            try {
                return Double.valueOf(toDouble(name, v));
            } catch (RuntimeException e) {
                return null;
            }
        }
        return null;
    }

    @Override // io.netty.handler.codec.Headers
    public double getDoubleAndRemove(K name, double defaultValue) {
        Double v = getDoubleAndRemove(name);
        return v != null ? v.doubleValue() : defaultValue;
    }

    @Override // io.netty.handler.codec.Headers
    public Long getTimeMillisAndRemove(K name) {
        V v = getAndRemove(name);
        if (v != null) {
            try {
                return Long.valueOf(toTimeMillis(name, v));
            } catch (RuntimeException e) {
                return null;
            }
        }
        return null;
    }

    @Override // io.netty.handler.codec.Headers
    public long getTimeMillisAndRemove(K name, long defaultValue) {
        Long v = getTimeMillisAndRemove(name);
        return v != null ? v.longValue() : defaultValue;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Headers)) {
            return false;
        }
        return equals((Headers) o, HashingStrategy.JAVA_HASHER);
    }

    public int hashCode() {
        return hashCode(HashingStrategy.JAVA_HASHER);
    }

    public final boolean equals(Headers<K, V, ?> h2, HashingStrategy<V> valueHashingStrategy) {
        if (h2.size() != size()) {
            return false;
        }
        if (this == h2) {
            return true;
        }
        for (K name : names()) {
            List<V> otherValues = h2.getAll(name);
            List<V> values = getAll(name);
            if (otherValues.size() != values.size()) {
                return false;
            }
            for (int i = 0; i < otherValues.size(); i++) {
                if (!valueHashingStrategy.equals(otherValues.get(i), values.get(i))) {
                    return false;
                }
            }
        }
        return true;
    }

    public final int hashCode(HashingStrategy<V> valueHashingStrategy) {
        int result = HASH_CODE_SEED;
        for (K name : names()) {
            int result2 = (result * 31) + this.hashingStrategy.hashCode(name);
            List<V> values = getAll(name);
            for (int i = 0; i < values.size(); i++) {
                result2 = (result2 * 31) + valueHashingStrategy.hashCode(values.get(i));
            }
            result = result2;
        }
        return result;
    }

    public String toString() {
        return HeadersUtils.toString(getClass(), iterator(), size());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void validateName(NameValidator<K> validator, boolean forAdd, K name) {
        validator.validateName(name);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void validateValue(ValueValidator<V> validator, K name, V value) {
        try {
            validator.validate(value);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Validation failed for header '" + name + "'", e);
        }
    }

    protected HeaderEntry<K, V> newHeaderEntry(int h, K name, V value, HeaderEntry<K, V> next) {
        return new HeaderEntry<>(h, name, value, next, this.head);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ValueConverter<V> valueConverter() {
        return this.valueConverter;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public NameValidator<K> nameValidator() {
        return this.nameValidator;
    }

    protected ValueValidator<V> valueValidator() {
        return this.valueValidator;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int index(int hash) {
        return this.hashMask & hash;
    }

    private void add0(int h, int i, K name, V value) {
        this.entries[i] = newHeaderEntry(h, name, value, this.entries[i]);
        this.size++;
    }

    private V remove0(int h, int i, K name) {
        HeaderEntry<K, V> e = this.entries[i];
        if (e == null) {
            return null;
        }
        V value = null;
        for (HeaderEntry<K, V> next = e.next; next != null; next = e.next) {
            if (next.hash == h && this.hashingStrategy.equals(name, next.key)) {
                value = next.value;
                e.next = next.next;
                next.remove();
                this.size--;
            } else {
                e = next;
            }
        }
        HeaderEntry<K, V> e2 = this.entries[i];
        if (e2.hash == h && this.hashingStrategy.equals(name, e2.key)) {
            if (value == null) {
                value = e2.value;
            }
            this.entries[i] = e2.next;
            e2.remove();
            this.size--;
        }
        return value;
    }

    HeaderEntry<K, V> remove0(HeaderEntry<K, V> entry, HeaderEntry<K, V> previous) {
        int i = index(entry.hash);
        HeaderEntry<K, V> firstEntry = this.entries[i];
        if (firstEntry == entry) {
            this.entries[i] = entry.next;
            previous = this.entries[i];
        } else if (previous == null) {
            previous = firstEntry;
            HeaderEntry<K, V> next = firstEntry.next;
            while (next != null && next != entry) {
                previous = next;
                next = next.next;
            }
            if (next == null) {
                throw new AssertionError("Entry not found in its hash bucket: " + entry);
            }
            previous.next = entry.next;
        } else {
            HeaderEntry<K, V> next2 = entry.next;
            previous.next = next2;
        }
        entry.remove();
        this.size--;
        return previous;
    }

    private T thisT() {
        return this;
    }

    private V fromObject(K name, Object value) {
        try {
            return this.valueConverter.convertObject(ObjectUtil.checkNotNull(value, "value"));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to convert object value for header '" + name + '\'', e);
        }
    }

    private V fromBoolean(K name, boolean value) {
        try {
            return this.valueConverter.convertBoolean(value);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to convert boolean value for header '" + name + '\'', e);
        }
    }

    private V fromByte(K name, byte value) {
        try {
            return this.valueConverter.convertByte(value);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to convert byte value for header '" + name + '\'', e);
        }
    }

    private V fromChar(K name, char value) {
        try {
            return this.valueConverter.convertChar(value);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to convert char value for header '" + name + '\'', e);
        }
    }

    private V fromShort(K name, short value) {
        try {
            return this.valueConverter.convertShort(value);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to convert short value for header '" + name + '\'', e);
        }
    }

    private V fromInt(K name, int value) {
        try {
            return this.valueConverter.convertInt(value);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to convert int value for header '" + name + '\'', e);
        }
    }

    private V fromLong(K name, long value) {
        try {
            return this.valueConverter.convertLong(value);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to convert long value for header '" + name + '\'', e);
        }
    }

    private V fromFloat(K name, float value) {
        try {
            return this.valueConverter.convertFloat(value);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to convert float value for header '" + name + '\'', e);
        }
    }

    private V fromDouble(K name, double value) {
        try {
            return this.valueConverter.convertDouble(value);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to convert double value for header '" + name + '\'', e);
        }
    }

    private V fromTimeMillis(K name, long value) {
        try {
            return this.valueConverter.convertTimeMillis(value);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to convert millsecond value for header '" + name + '\'', e);
        }
    }

    private boolean toBoolean(K name, V value) {
        try {
            return this.valueConverter.convertToBoolean(value);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to convert header value to boolean for header '" + name + '\'');
        }
    }

    private byte toByte(K name, V value) {
        try {
            return this.valueConverter.convertToByte(value);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to convert header value to byte for header '" + name + '\'');
        }
    }

    private char toChar(K name, V value) {
        try {
            return this.valueConverter.convertToChar(value);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to convert header value to char for header '" + name + '\'');
        }
    }

    private short toShort(K name, V value) {
        try {
            return this.valueConverter.convertToShort(value);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to convert header value to short for header '" + name + '\'');
        }
    }

    private int toInt(K name, V value) {
        try {
            return this.valueConverter.convertToInt(value);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to convert header value to int for header '" + name + '\'');
        }
    }

    private long toLong(K name, V value) {
        try {
            return this.valueConverter.convertToLong(value);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to convert header value to long for header '" + name + '\'');
        }
    }

    private float toFloat(K name, V value) {
        try {
            return this.valueConverter.convertToFloat(value);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to convert header value to float for header '" + name + '\'');
        }
    }

    private double toDouble(K name, V value) {
        try {
            return this.valueConverter.convertToDouble(value);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to convert header value to double for header '" + name + '\'');
        }
    }

    private long toTimeMillis(K name, V value) {
        try {
            return this.valueConverter.convertToTimeMillis(value);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to convert header value to millsecond for header '" + name + '\'');
        }
    }

    public DefaultHeaders<K, V, T> copy() {
        DefaultHeaders<K, V, T> copy = new DefaultHeaders<>(this.hashingStrategy, this.valueConverter, this.nameValidator, this.entries.length);
        copy.addImpl(this);
        return copy;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class HeaderIterator implements Iterator<Map.Entry<K, V>> {
        private HeaderEntry<K, V> current;

        private HeaderIterator() {
            this.current = DefaultHeaders.this.head;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.current.after != DefaultHeaders.this.head;
        }

        @Override // java.util.Iterator
        public Map.Entry<K, V> next() {
            this.current = this.current.after;
            if (this.current == DefaultHeaders.this.head) {
                throw new NoSuchElementException();
            }
            return this.current;
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException("read only");
        }
    }

    /* loaded from: classes4.dex */
    private final class ValueIterator implements Iterator<V> {
        private final int hash;
        private final K name;
        private HeaderEntry<K, V> next;
        private HeaderEntry<K, V> previous;
        private HeaderEntry<K, V> removalPrevious;

        ValueIterator(K k) {
            this.name = (K) ObjectUtil.checkNotNull(k, "name");
            this.hash = DefaultHeaders.this.hashingStrategy.hashCode(k);
            calculateNext(DefaultHeaders.this.entries[DefaultHeaders.this.index(this.hash)]);
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.next != null;
        }

        @Override // java.util.Iterator
        public V next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            if (this.previous != null) {
                this.removalPrevious = this.previous;
            }
            this.previous = this.next;
            calculateNext(this.next.next);
            return this.previous.value;
        }

        @Override // java.util.Iterator
        public void remove() {
            if (this.previous == null) {
                throw new IllegalStateException();
            }
            this.removalPrevious = DefaultHeaders.this.remove0(this.previous, this.removalPrevious);
            this.previous = null;
        }

        private void calculateNext(HeaderEntry<K, V> entry) {
            while (entry != null) {
                if (entry.hash == this.hash && DefaultHeaders.this.hashingStrategy.equals(this.name, entry.key)) {
                    this.next = entry;
                    return;
                }
                entry = entry.next;
            }
            this.next = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes4.dex */
    public static class HeaderEntry<K, V> implements Map.Entry<K, V> {
        protected HeaderEntry<K, V> after;
        protected HeaderEntry<K, V> before;
        protected final int hash;
        protected final K key;
        protected HeaderEntry<K, V> next;
        protected V value;

        /* JADX INFO: Access modifiers changed from: protected */
        public HeaderEntry(int hash, K key) {
            this.hash = hash;
            this.key = key;
        }

        HeaderEntry(int hash, K key, V value, HeaderEntry<K, V> next, HeaderEntry<K, V> head) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
            this.after = head;
            this.before = head.before;
            pointNeighborsToThis();
        }

        HeaderEntry() {
            this.hash = -1;
            this.key = null;
            this.after = this;
            this.before = this;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public final void pointNeighborsToThis() {
            this.before.after = this;
            this.after.before = this;
        }

        public final HeaderEntry<K, V> before() {
            return this.before;
        }

        public final HeaderEntry<K, V> after() {
            return this.after;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public void remove() {
            this.before.after = this.after;
            this.after.before = this.before;
        }

        @Override // java.util.Map.Entry
        public final K getKey() {
            return this.key;
        }

        @Override // java.util.Map.Entry
        public final V getValue() {
            return this.value;
        }

        @Override // java.util.Map.Entry
        public final V setValue(V value) {
            ObjectUtil.checkNotNull(value, "value");
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        public final String toString() {
            return this.key.toString() + '=' + this.value.toString();
        }

        @Override // java.util.Map.Entry
        public boolean equals(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry<?, ?> other = (Map.Entry) o;
            if (getKey() == null) {
                if (other.getKey() != null) {
                    return false;
                }
            } else if (!getKey().equals(other.getKey())) {
                return false;
            }
            if (getValue() == null) {
                if (other.getValue() != null) {
                    return false;
                }
            } else if (!getValue().equals(other.getValue())) {
                return false;
            }
            return true;
        }

        @Override // java.util.Map.Entry
        public int hashCode() {
            return (this.key == null ? 0 : this.key.hashCode()) ^ (this.value != null ? this.value.hashCode() : 0);
        }
    }
}
