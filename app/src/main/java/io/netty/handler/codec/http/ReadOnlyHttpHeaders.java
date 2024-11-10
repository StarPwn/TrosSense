package io.netty.handler.codec.http;

import io.netty.handler.codec.CharSequenceValueConverter;
import io.netty.util.AsciiString;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/* loaded from: classes4.dex */
public final class ReadOnlyHttpHeaders extends HttpHeaders {
    private final CharSequence[] nameValuePairs;

    public ReadOnlyHttpHeaders(boolean validateHeaders, CharSequence... nameValuePairs) {
        if ((nameValuePairs.length & 1) != 0) {
            throw newInvalidArraySizeException();
        }
        if (validateHeaders) {
            validateHeaders(nameValuePairs);
        }
        this.nameValuePairs = nameValuePairs;
    }

    private static IllegalArgumentException newInvalidArraySizeException() {
        return new IllegalArgumentException("nameValuePairs must be arrays of [name, value] pairs");
    }

    private static void validateHeaders(CharSequence... keyValuePairs) {
        for (int i = 0; i < keyValuePairs.length; i += 2) {
            DefaultHttpHeadersFactory.headersFactory().getNameValidator().validateName(keyValuePairs[i]);
        }
    }

    private CharSequence get0(CharSequence name) {
        int nameHash = AsciiString.hashCode(name);
        for (int i = 0; i < this.nameValuePairs.length; i += 2) {
            CharSequence roName = this.nameValuePairs[i];
            if (AsciiString.hashCode(roName) == nameHash && AsciiString.contentEqualsIgnoreCase(roName, name)) {
                return this.nameValuePairs[i + 1];
            }
        }
        return null;
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public String get(String name) {
        CharSequence value = get0(name);
        if (value == null) {
            return null;
        }
        return value.toString();
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public Integer getInt(CharSequence name) {
        CharSequence value = get0(name);
        if (value == null) {
            return null;
        }
        return Integer.valueOf(CharSequenceValueConverter.INSTANCE.convertToInt(value));
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public int getInt(CharSequence name, int defaultValue) {
        CharSequence value = get0(name);
        return value == null ? defaultValue : CharSequenceValueConverter.INSTANCE.convertToInt(value);
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public Short getShort(CharSequence name) {
        CharSequence value = get0(name);
        if (value == null) {
            return null;
        }
        return Short.valueOf(CharSequenceValueConverter.INSTANCE.convertToShort(value));
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public short getShort(CharSequence name, short defaultValue) {
        CharSequence value = get0(name);
        return value == null ? defaultValue : CharSequenceValueConverter.INSTANCE.convertToShort(value);
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public Long getTimeMillis(CharSequence name) {
        CharSequence value = get0(name);
        if (value == null) {
            return null;
        }
        return Long.valueOf(CharSequenceValueConverter.INSTANCE.convertToTimeMillis(value));
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public long getTimeMillis(CharSequence name, long defaultValue) {
        CharSequence value = get0(name);
        return value == null ? defaultValue : CharSequenceValueConverter.INSTANCE.convertToTimeMillis(value);
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public List<String> getAll(String name) {
        if (isEmpty()) {
            return Collections.emptyList();
        }
        int nameHash = AsciiString.hashCode(name);
        List<String> values = new ArrayList<>(4);
        for (int i = 0; i < this.nameValuePairs.length; i += 2) {
            CharSequence roName = this.nameValuePairs[i];
            if (AsciiString.hashCode(roName) == nameHash && AsciiString.contentEqualsIgnoreCase(roName, name)) {
                values.add(this.nameValuePairs[i + 1].toString());
            }
        }
        return values;
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public List<Map.Entry<String, String>> entries() {
        if (isEmpty()) {
            return Collections.emptyList();
        }
        List<Map.Entry<String, String>> entries = new ArrayList<>(size());
        for (int i = 0; i < this.nameValuePairs.length; i += 2) {
            entries.add(new AbstractMap.SimpleImmutableEntry<>(this.nameValuePairs[i].toString(), this.nameValuePairs[i + 1].toString()));
        }
        return entries;
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public boolean contains(String name) {
        return get0(name) != null;
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public boolean contains(String name, String value, boolean ignoreCase) {
        return containsValue(name, value, ignoreCase);
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public boolean containsValue(CharSequence name, CharSequence value, boolean ignoreCase) {
        if (ignoreCase) {
            for (int i = 0; i < this.nameValuePairs.length; i += 2) {
                if (AsciiString.contentEqualsIgnoreCase(this.nameValuePairs[i], name) && AsciiString.contentEqualsIgnoreCase(this.nameValuePairs[i + 1], value)) {
                    return true;
                }
            }
            return false;
        }
        for (int i2 = 0; i2 < this.nameValuePairs.length; i2 += 2) {
            if (AsciiString.contentEqualsIgnoreCase(this.nameValuePairs[i2], name) && AsciiString.contentEquals(this.nameValuePairs[i2 + 1], value)) {
                return true;
            }
        }
        return false;
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public Iterator<String> valueStringIterator(CharSequence name) {
        return new ReadOnlyStringValueIterator(name);
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public Iterator<CharSequence> valueCharSequenceIterator(CharSequence name) {
        return new ReadOnlyValueIterator(name);
    }

    @Override // io.netty.handler.codec.http.HttpHeaders, java.lang.Iterable
    public Iterator<Map.Entry<String, String>> iterator() {
        return new ReadOnlyStringIterator();
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public Iterator<Map.Entry<CharSequence, CharSequence>> iteratorCharSequence() {
        return new ReadOnlyIterator();
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public boolean isEmpty() {
        return this.nameValuePairs.length == 0;
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public int size() {
        return this.nameValuePairs.length >>> 1;
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public Set<String> names() {
        if (isEmpty()) {
            return Collections.emptySet();
        }
        Set<String> names = new LinkedHashSet<>(size());
        for (int i = 0; i < this.nameValuePairs.length; i += 2) {
            names.add(this.nameValuePairs[i].toString());
        }
        return names;
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public HttpHeaders add(String name, Object value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public HttpHeaders add(String name, Iterable<?> values) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public HttpHeaders addInt(CharSequence name, int value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public HttpHeaders addShort(CharSequence name, short value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public HttpHeaders set(String name, Object value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public HttpHeaders set(String name, Iterable<?> values) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public HttpHeaders setInt(CharSequence name, int value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public HttpHeaders setShort(CharSequence name, short value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public HttpHeaders remove(String name) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public HttpHeaders clear() {
        throw new UnsupportedOperationException("read only");
    }

    /* loaded from: classes4.dex */
    private final class ReadOnlyIterator implements Map.Entry<CharSequence, CharSequence>, Iterator<Map.Entry<CharSequence, CharSequence>> {
        private CharSequence key;
        private int nextNameIndex;
        private CharSequence value;

        private ReadOnlyIterator() {
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.nextNameIndex != ReadOnlyHttpHeaders.this.nameValuePairs.length;
        }

        @Override // java.util.Iterator
        public Map.Entry<CharSequence, CharSequence> next() {
            if (hasNext()) {
                this.key = ReadOnlyHttpHeaders.this.nameValuePairs[this.nextNameIndex];
                this.value = ReadOnlyHttpHeaders.this.nameValuePairs[this.nextNameIndex + 1];
                this.nextNameIndex += 2;
                return this;
            }
            throw new NoSuchElementException();
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException("read only");
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Map.Entry
        public CharSequence getKey() {
            return this.key;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Map.Entry
        public CharSequence getValue() {
            return this.value;
        }

        @Override // java.util.Map.Entry
        public CharSequence setValue(CharSequence value) {
            throw new UnsupportedOperationException("read only");
        }

        public String toString() {
            return this.key.toString() + '=' + this.value.toString();
        }
    }

    /* loaded from: classes4.dex */
    private final class ReadOnlyStringIterator implements Map.Entry<String, String>, Iterator<Map.Entry<String, String>> {
        private String key;
        private int nextNameIndex;
        private String value;

        private ReadOnlyStringIterator() {
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.nextNameIndex != ReadOnlyHttpHeaders.this.nameValuePairs.length;
        }

        @Override // java.util.Iterator
        public Map.Entry<String, String> next() {
            if (hasNext()) {
                this.key = ReadOnlyHttpHeaders.this.nameValuePairs[this.nextNameIndex].toString();
                this.value = ReadOnlyHttpHeaders.this.nameValuePairs[this.nextNameIndex + 1].toString();
                this.nextNameIndex += 2;
                return this;
            }
            throw new NoSuchElementException();
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException("read only");
        }

        @Override // java.util.Map.Entry
        public String getKey() {
            return this.key;
        }

        @Override // java.util.Map.Entry
        public String getValue() {
            return this.value;
        }

        @Override // java.util.Map.Entry
        public String setValue(String value) {
            throw new UnsupportedOperationException("read only");
        }

        public String toString() {
            return this.key + '=' + this.value;
        }
    }

    /* loaded from: classes4.dex */
    private final class ReadOnlyStringValueIterator implements Iterator<String> {
        private final CharSequence name;
        private final int nameHash;
        private int nextNameIndex = findNextValue();

        ReadOnlyStringValueIterator(CharSequence name) {
            this.name = name;
            this.nameHash = AsciiString.hashCode(name);
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.nextNameIndex != -1;
        }

        @Override // java.util.Iterator
        public String next() {
            if (hasNext()) {
                String value = ReadOnlyHttpHeaders.this.nameValuePairs[this.nextNameIndex + 1].toString();
                this.nextNameIndex = findNextValue();
                return value;
            }
            throw new NoSuchElementException();
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException("read only");
        }

        private int findNextValue() {
            for (int i = this.nextNameIndex; i < ReadOnlyHttpHeaders.this.nameValuePairs.length; i += 2) {
                CharSequence roName = ReadOnlyHttpHeaders.this.nameValuePairs[i];
                if (this.nameHash == AsciiString.hashCode(roName) && AsciiString.contentEqualsIgnoreCase(this.name, roName)) {
                    return i;
                }
            }
            return -1;
        }
    }

    /* loaded from: classes4.dex */
    private final class ReadOnlyValueIterator implements Iterator<CharSequence> {
        private final CharSequence name;
        private final int nameHash;
        private int nextNameIndex = findNextValue();

        ReadOnlyValueIterator(CharSequence name) {
            this.name = name;
            this.nameHash = AsciiString.hashCode(name);
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.nextNameIndex != -1;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Iterator
        public CharSequence next() {
            if (hasNext()) {
                CharSequence value = ReadOnlyHttpHeaders.this.nameValuePairs[this.nextNameIndex + 1];
                this.nextNameIndex = findNextValue();
                return value;
            }
            throw new NoSuchElementException();
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException("read only");
        }

        private int findNextValue() {
            for (int i = this.nextNameIndex; i < ReadOnlyHttpHeaders.this.nameValuePairs.length; i += 2) {
                CharSequence roName = ReadOnlyHttpHeaders.this.nameValuePairs[i];
                if (this.nameHash == AsciiString.hashCode(roName) && AsciiString.contentEqualsIgnoreCase(this.name, roName)) {
                    return i;
                }
            }
            return -1;
        }
    }
}
