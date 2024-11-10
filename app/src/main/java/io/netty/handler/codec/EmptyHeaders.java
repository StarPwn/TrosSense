package io.netty.handler.codec;

import io.netty.handler.codec.Headers;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* loaded from: classes4.dex */
public class EmptyHeaders<K, V, T extends Headers<K, V, T>> implements Headers<K, V, T> {
    @Override // io.netty.handler.codec.Headers
    public V get(K name) {
        return null;
    }

    @Override // io.netty.handler.codec.Headers
    public V get(K name, V defaultValue) {
        return defaultValue;
    }

    @Override // io.netty.handler.codec.Headers
    public V getAndRemove(K name) {
        return null;
    }

    @Override // io.netty.handler.codec.Headers
    public V getAndRemove(K name, V defaultValue) {
        return defaultValue;
    }

    @Override // io.netty.handler.codec.Headers
    public List<V> getAll(K name) {
        return Collections.emptyList();
    }

    @Override // io.netty.handler.codec.Headers
    public List<V> getAllAndRemove(K name) {
        return Collections.emptyList();
    }

    @Override // io.netty.handler.codec.Headers
    public Boolean getBoolean(K name) {
        return null;
    }

    @Override // io.netty.handler.codec.Headers
    public boolean getBoolean(K name, boolean defaultValue) {
        return defaultValue;
    }

    @Override // io.netty.handler.codec.Headers
    public Byte getByte(K name) {
        return null;
    }

    @Override // io.netty.handler.codec.Headers
    public byte getByte(K name, byte defaultValue) {
        return defaultValue;
    }

    @Override // io.netty.handler.codec.Headers
    public Character getChar(K name) {
        return null;
    }

    @Override // io.netty.handler.codec.Headers
    public char getChar(K name, char defaultValue) {
        return defaultValue;
    }

    @Override // io.netty.handler.codec.Headers
    public Short getShort(K name) {
        return null;
    }

    @Override // io.netty.handler.codec.Headers
    public short getShort(K name, short defaultValue) {
        return defaultValue;
    }

    @Override // io.netty.handler.codec.Headers
    public Integer getInt(K name) {
        return null;
    }

    @Override // io.netty.handler.codec.Headers
    public int getInt(K name, int defaultValue) {
        return defaultValue;
    }

    @Override // io.netty.handler.codec.Headers
    public Long getLong(K name) {
        return null;
    }

    @Override // io.netty.handler.codec.Headers
    public long getLong(K name, long defaultValue) {
        return defaultValue;
    }

    @Override // io.netty.handler.codec.Headers
    public Float getFloat(K name) {
        return null;
    }

    @Override // io.netty.handler.codec.Headers
    public float getFloat(K name, float defaultValue) {
        return defaultValue;
    }

    @Override // io.netty.handler.codec.Headers
    public Double getDouble(K name) {
        return null;
    }

    @Override // io.netty.handler.codec.Headers
    public double getDouble(K name, double defaultValue) {
        return defaultValue;
    }

    @Override // io.netty.handler.codec.Headers
    public Long getTimeMillis(K name) {
        return null;
    }

    @Override // io.netty.handler.codec.Headers
    public long getTimeMillis(K name, long defaultValue) {
        return defaultValue;
    }

    @Override // io.netty.handler.codec.Headers
    public Boolean getBooleanAndRemove(K name) {
        return null;
    }

    @Override // io.netty.handler.codec.Headers
    public boolean getBooleanAndRemove(K name, boolean defaultValue) {
        return defaultValue;
    }

    @Override // io.netty.handler.codec.Headers
    public Byte getByteAndRemove(K name) {
        return null;
    }

    @Override // io.netty.handler.codec.Headers
    public byte getByteAndRemove(K name, byte defaultValue) {
        return defaultValue;
    }

    @Override // io.netty.handler.codec.Headers
    public Character getCharAndRemove(K name) {
        return null;
    }

    @Override // io.netty.handler.codec.Headers
    public char getCharAndRemove(K name, char defaultValue) {
        return defaultValue;
    }

    @Override // io.netty.handler.codec.Headers
    public Short getShortAndRemove(K name) {
        return null;
    }

    @Override // io.netty.handler.codec.Headers
    public short getShortAndRemove(K name, short defaultValue) {
        return defaultValue;
    }

    @Override // io.netty.handler.codec.Headers
    public Integer getIntAndRemove(K name) {
        return null;
    }

    @Override // io.netty.handler.codec.Headers
    public int getIntAndRemove(K name, int defaultValue) {
        return defaultValue;
    }

    @Override // io.netty.handler.codec.Headers
    public Long getLongAndRemove(K name) {
        return null;
    }

    @Override // io.netty.handler.codec.Headers
    public long getLongAndRemove(K name, long defaultValue) {
        return defaultValue;
    }

    @Override // io.netty.handler.codec.Headers
    public Float getFloatAndRemove(K name) {
        return null;
    }

    @Override // io.netty.handler.codec.Headers
    public float getFloatAndRemove(K name, float defaultValue) {
        return defaultValue;
    }

    @Override // io.netty.handler.codec.Headers
    public Double getDoubleAndRemove(K name) {
        return null;
    }

    @Override // io.netty.handler.codec.Headers
    public double getDoubleAndRemove(K name, double defaultValue) {
        return defaultValue;
    }

    @Override // io.netty.handler.codec.Headers
    public Long getTimeMillisAndRemove(K name) {
        return null;
    }

    @Override // io.netty.handler.codec.Headers
    public long getTimeMillisAndRemove(K name, long defaultValue) {
        return defaultValue;
    }

    @Override // io.netty.handler.codec.Headers
    public boolean contains(K name) {
        return false;
    }

    @Override // io.netty.handler.codec.Headers
    public boolean contains(K name, V value) {
        return false;
    }

    @Override // io.netty.handler.codec.Headers
    public boolean containsObject(K name, Object value) {
        return false;
    }

    @Override // io.netty.handler.codec.Headers
    public boolean containsBoolean(K name, boolean value) {
        return false;
    }

    @Override // io.netty.handler.codec.Headers
    public boolean containsByte(K name, byte value) {
        return false;
    }

    @Override // io.netty.handler.codec.Headers
    public boolean containsChar(K name, char value) {
        return false;
    }

    @Override // io.netty.handler.codec.Headers
    public boolean containsShort(K name, short value) {
        return false;
    }

    @Override // io.netty.handler.codec.Headers
    public boolean containsInt(K name, int value) {
        return false;
    }

    @Override // io.netty.handler.codec.Headers
    public boolean containsLong(K name, long value) {
        return false;
    }

    @Override // io.netty.handler.codec.Headers
    public boolean containsFloat(K name, float value) {
        return false;
    }

    @Override // io.netty.handler.codec.Headers
    public boolean containsDouble(K name, double value) {
        return false;
    }

    @Override // io.netty.handler.codec.Headers
    public boolean containsTimeMillis(K name, long value) {
        return false;
    }

    @Override // io.netty.handler.codec.Headers
    public int size() {
        return 0;
    }

    @Override // io.netty.handler.codec.Headers
    public boolean isEmpty() {
        return true;
    }

    @Override // io.netty.handler.codec.Headers
    public Set<K> names() {
        return Collections.emptySet();
    }

    @Override // io.netty.handler.codec.Headers
    public T add(K name, V value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public T add(K name, Iterable<? extends V> values) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public T add(K name, V... values) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public T addObject(K name, Object value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public T addObject(K name, Iterable<?> values) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public T addObject(K name, Object... values) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public T addBoolean(K name, boolean value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public T addByte(K name, byte value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public T addChar(K name, char value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public T addShort(K name, short value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public T addInt(K name, int value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public T addLong(K name, long value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public T addFloat(K name, float value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public T addDouble(K name, double value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public T addTimeMillis(K name, long value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public T add(Headers<? extends K, ? extends V, ?> headers) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public T set(K name, V value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public T set(K name, Iterable<? extends V> values) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public T set(K name, V... values) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public T setObject(K name, Object value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public T setObject(K name, Iterable<?> values) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public T setObject(K name, Object... values) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public T setBoolean(K name, boolean value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public T setByte(K name, byte value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public T setChar(K name, char value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public T setShort(K name, short value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public T setInt(K name, int value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public T setLong(K name, long value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public T setFloat(K name, float value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public T setDouble(K name, double value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public T setTimeMillis(K name, long value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public T set(Headers<? extends K, ? extends V, ?> headers) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public T setAll(Headers<? extends K, ? extends V, ?> headers) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.Headers
    public boolean remove(K name) {
        return false;
    }

    @Override // io.netty.handler.codec.Headers
    public T clear() {
        return thisT();
    }

    public Iterator<V> valueIterator(K name) {
        List<V> empty = Collections.emptyList();
        return empty.iterator();
    }

    @Override // io.netty.handler.codec.Headers, java.lang.Iterable
    public Iterator<Map.Entry<K, V>> iterator() {
        List<Map.Entry<K, V>> empty = Collections.emptyList();
        return empty.iterator();
    }

    public boolean equals(Object o) {
        if (!(o instanceof Headers)) {
            return false;
        }
        Headers<?, ?, ?> rhs = (Headers) o;
        return isEmpty() && rhs.isEmpty();
    }

    public int hashCode() {
        return -1028477387;
    }

    public String toString() {
        return getClass().getSimpleName() + "[]";
    }

    private T thisT() {
        return this;
    }
}
