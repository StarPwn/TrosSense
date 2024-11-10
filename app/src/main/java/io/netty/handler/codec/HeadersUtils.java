package io.netty.handler.codec;

import io.netty.util.internal.ObjectUtil;
import java.util.AbstractCollection;
import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import okhttp3.HttpUrl;

/* loaded from: classes4.dex */
public final class HeadersUtils {
    private HeadersUtils() {
    }

    public static <K, V> List<String> getAllAsString(Headers<K, V, ?> headers, K name) {
        final List<V> allNames = headers.getAll(name);
        return new AbstractList<String>() { // from class: io.netty.handler.codec.HeadersUtils.1
            @Override // java.util.AbstractList, java.util.List
            public String get(int index) {
                Object obj = allNames.get(index);
                if (obj != null) {
                    return obj.toString();
                }
                return null;
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
            public int size() {
                return allNames.size();
            }
        };
    }

    public static <K, V> String getAsString(Headers<K, V, ?> headers, K name) {
        V orig = headers.get(name);
        if (orig != null) {
            return orig.toString();
        }
        return null;
    }

    public static Iterator<Map.Entry<String, String>> iteratorAsString(Iterable<Map.Entry<CharSequence, CharSequence>> headers) {
        return new StringEntryIterator(headers.iterator());
    }

    public static <K, V> String toString(Class<?> headersClass, Iterator<Map.Entry<K, V>> headersIt, int size) {
        String simpleName = headersClass.getSimpleName();
        if (size == 0) {
            return simpleName + HttpUrl.PATH_SEGMENT_ENCODE_SET_URI;
        }
        StringBuilder sb = new StringBuilder(simpleName.length() + 2 + (size * 20)).append(simpleName).append('[');
        while (headersIt.hasNext()) {
            Map.Entry<K, V> next = headersIt.next();
            sb.append(next.getKey()).append(": ").append(next.getValue()).append(", ");
        }
        sb.setLength(sb.length() - 2);
        return sb.append(']').toString();
    }

    public static Set<String> namesAsString(Headers<CharSequence, CharSequence, ?> headers) {
        return new DelegatingNameSet(headers);
    }

    /* loaded from: classes4.dex */
    private static final class StringEntryIterator implements Iterator<Map.Entry<String, String>> {
        private final Iterator<Map.Entry<CharSequence, CharSequence>> iter;

        StringEntryIterator(Iterator<Map.Entry<CharSequence, CharSequence>> iter) {
            this.iter = iter;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.iter.hasNext();
        }

        @Override // java.util.Iterator
        public Map.Entry<String, String> next() {
            return new StringEntry(this.iter.next());
        }

        @Override // java.util.Iterator
        public void remove() {
            this.iter.remove();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class StringEntry implements Map.Entry<String, String> {
        private final Map.Entry<CharSequence, CharSequence> entry;
        private String name;
        private String value;

        StringEntry(Map.Entry<CharSequence, CharSequence> entry) {
            this.entry = entry;
        }

        @Override // java.util.Map.Entry
        public String getKey() {
            if (this.name == null) {
                this.name = this.entry.getKey().toString();
            }
            return this.name;
        }

        @Override // java.util.Map.Entry
        public String getValue() {
            if (this.value == null && this.entry.getValue() != null) {
                this.value = this.entry.getValue().toString();
            }
            return this.value;
        }

        @Override // java.util.Map.Entry
        public String setValue(String value) {
            String old = getValue();
            this.entry.setValue(value);
            return old;
        }

        public String toString() {
            return this.entry.toString();
        }
    }

    /* loaded from: classes4.dex */
    private static final class StringIterator<T> implements Iterator<String> {
        private final Iterator<T> iter;

        StringIterator(Iterator<T> iter) {
            this.iter = iter;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.iter.hasNext();
        }

        @Override // java.util.Iterator
        public String next() {
            T next = this.iter.next();
            if (next != null) {
                return next.toString();
            }
            return null;
        }

        @Override // java.util.Iterator
        public void remove() {
            this.iter.remove();
        }
    }

    /* loaded from: classes4.dex */
    private static final class DelegatingNameSet extends AbstractCollection<String> implements Set<String> {
        private final Headers<CharSequence, CharSequence, ?> headers;

        DelegatingNameSet(Headers<CharSequence, CharSequence, ?> headers) {
            this.headers = (Headers) ObjectUtil.checkNotNull(headers, "headers");
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.headers.names().size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean isEmpty() {
            return this.headers.isEmpty();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object o) {
            return this.headers.contains(o.toString());
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<String> iterator() {
            return new StringIterator(this.headers.names().iterator());
        }
    }
}
