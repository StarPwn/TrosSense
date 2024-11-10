package io.netty.handler.codec.http;

import io.netty.handler.codec.CharSequenceValueConverter;
import io.netty.handler.codec.DateFormatter;
import io.netty.handler.codec.DefaultHeaders;
import io.netty.handler.codec.DefaultHeadersImpl;
import io.netty.handler.codec.HeadersUtils;
import io.netty.handler.codec.ValueConverter;
import io.netty.util.AsciiString;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* loaded from: classes4.dex */
public class DefaultHttpHeaders extends HttpHeaders {
    private final DefaultHeaders<CharSequence, CharSequence, ?> headers;

    public DefaultHttpHeaders() {
        this(nameValidator(true), valueValidator(true));
    }

    @Deprecated
    public DefaultHttpHeaders(boolean validate) {
        this(nameValidator(validate), valueValidator(validate));
    }

    protected DefaultHttpHeaders(boolean validateValues, DefaultHeaders.NameValidator<CharSequence> nameValidator) {
        this(nameValidator, valueValidator(validateValues));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public DefaultHttpHeaders(DefaultHeaders.NameValidator<CharSequence> nameValidator, DefaultHeaders.ValueValidator<CharSequence> valueValidator) {
        this(nameValidator, valueValidator, 16);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public DefaultHttpHeaders(DefaultHeaders.NameValidator<CharSequence> nameValidator, DefaultHeaders.ValueValidator<CharSequence> valueValidator, int sizeHint) {
        this(new DefaultHeadersImpl(AsciiString.CASE_INSENSITIVE_HASHER, HeaderValueConverter.INSTANCE, nameValidator, sizeHint, valueValidator));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public DefaultHttpHeaders(DefaultHeaders<CharSequence, CharSequence, ?> headers) {
        this.headers = headers;
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public HttpHeaders add(HttpHeaders headers) {
        if (headers instanceof DefaultHttpHeaders) {
            this.headers.add(((DefaultHttpHeaders) headers).headers);
            return this;
        }
        return super.add(headers);
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public HttpHeaders set(HttpHeaders headers) {
        if (headers instanceof DefaultHttpHeaders) {
            this.headers.set(((DefaultHttpHeaders) headers).headers);
            return this;
        }
        return super.set(headers);
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public HttpHeaders add(String name, Object value) {
        this.headers.addObject((DefaultHeaders<CharSequence, CharSequence, ?>) name, value);
        return this;
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public HttpHeaders add(CharSequence name, Object value) {
        this.headers.addObject((DefaultHeaders<CharSequence, CharSequence, ?>) name, value);
        return this;
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public HttpHeaders add(String name, Iterable<?> values) {
        this.headers.addObject((DefaultHeaders<CharSequence, CharSequence, ?>) name, values);
        return this;
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public HttpHeaders add(CharSequence name, Iterable<?> values) {
        this.headers.addObject((DefaultHeaders<CharSequence, CharSequence, ?>) name, values);
        return this;
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public HttpHeaders addInt(CharSequence name, int value) {
        this.headers.addInt(name, value);
        return this;
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public HttpHeaders addShort(CharSequence name, short value) {
        this.headers.addShort(name, value);
        return this;
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public HttpHeaders remove(String name) {
        this.headers.remove(name);
        return this;
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public HttpHeaders remove(CharSequence name) {
        this.headers.remove(name);
        return this;
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public HttpHeaders set(String name, Object value) {
        this.headers.setObject((DefaultHeaders<CharSequence, CharSequence, ?>) name, value);
        return this;
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public HttpHeaders set(CharSequence name, Object value) {
        this.headers.setObject((DefaultHeaders<CharSequence, CharSequence, ?>) name, value);
        return this;
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public HttpHeaders set(String name, Iterable<?> values) {
        this.headers.setObject((DefaultHeaders<CharSequence, CharSequence, ?>) name, values);
        return this;
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public HttpHeaders set(CharSequence name, Iterable<?> values) {
        this.headers.setObject((DefaultHeaders<CharSequence, CharSequence, ?>) name, values);
        return this;
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public HttpHeaders setInt(CharSequence name, int value) {
        this.headers.setInt(name, value);
        return this;
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public HttpHeaders setShort(CharSequence name, short value) {
        this.headers.setShort(name, value);
        return this;
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public HttpHeaders clear() {
        this.headers.clear();
        return this;
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public String get(String name) {
        return get((CharSequence) name);
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public String get(CharSequence name) {
        return HeadersUtils.getAsString(this.headers, name);
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public Integer getInt(CharSequence name) {
        return this.headers.getInt(name);
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public int getInt(CharSequence name, int defaultValue) {
        return this.headers.getInt(name, defaultValue);
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public Short getShort(CharSequence name) {
        return this.headers.getShort(name);
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public short getShort(CharSequence name, short defaultValue) {
        return this.headers.getShort(name, defaultValue);
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public Long getTimeMillis(CharSequence name) {
        return this.headers.getTimeMillis(name);
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public long getTimeMillis(CharSequence name, long defaultValue) {
        return this.headers.getTimeMillis(name, defaultValue);
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public List<String> getAll(String name) {
        return getAll((CharSequence) name);
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public List<String> getAll(CharSequence name) {
        return HeadersUtils.getAllAsString(this.headers, name);
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public List<Map.Entry<String, String>> entries() {
        if (isEmpty()) {
            return Collections.emptyList();
        }
        List<Map.Entry<String, String>> entriesConverted = new ArrayList<>(this.headers.size());
        Iterator<Map.Entry<String, String>> it2 = iterator();
        while (it2.hasNext()) {
            Map.Entry<String, String> entry = it2.next();
            entriesConverted.add(entry);
        }
        return entriesConverted;
    }

    @Override // io.netty.handler.codec.http.HttpHeaders, java.lang.Iterable
    @Deprecated
    public Iterator<Map.Entry<String, String>> iterator() {
        return HeadersUtils.iteratorAsString(this.headers);
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public Iterator<Map.Entry<CharSequence, CharSequence>> iteratorCharSequence() {
        return this.headers.iterator();
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public Iterator<String> valueStringIterator(CharSequence name) {
        final Iterator<CharSequence> itr = valueCharSequenceIterator(name);
        return new Iterator<String>() { // from class: io.netty.handler.codec.http.DefaultHttpHeaders.1
            @Override // java.util.Iterator
            public boolean hasNext() {
                return itr.hasNext();
            }

            @Override // java.util.Iterator
            public String next() {
                return ((CharSequence) itr.next()).toString();
            }

            @Override // java.util.Iterator
            public void remove() {
                itr.remove();
            }
        };
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public Iterator<CharSequence> valueCharSequenceIterator(CharSequence name) {
        return this.headers.valueIterator(name);
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public boolean contains(String name) {
        return contains((CharSequence) name);
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public boolean contains(CharSequence name) {
        return this.headers.contains(name);
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public boolean isEmpty() {
        return this.headers.isEmpty();
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public int size() {
        return this.headers.size();
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public boolean contains(String name, String value, boolean ignoreCase) {
        return contains((CharSequence) name, (CharSequence) value, ignoreCase);
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public boolean contains(CharSequence name, CharSequence value, boolean ignoreCase) {
        return this.headers.contains(name, value, ignoreCase ? AsciiString.CASE_INSENSITIVE_HASHER : AsciiString.CASE_SENSITIVE_HASHER);
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public Set<String> names() {
        return HeadersUtils.namesAsString(this.headers);
    }

    public boolean equals(Object o) {
        return (o instanceof DefaultHttpHeaders) && this.headers.equals(((DefaultHttpHeaders) o).headers, AsciiString.CASE_SENSITIVE_HASHER);
    }

    public int hashCode() {
        return this.headers.hashCode(AsciiString.CASE_SENSITIVE_HASHER);
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public HttpHeaders copy() {
        return new DefaultHttpHeaders(this.headers.copy());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ValueConverter<CharSequence> valueConverter() {
        return HeaderValueConverter.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static DefaultHeaders.ValueValidator<CharSequence> valueValidator(boolean validate) {
        return validate ? DefaultHttpHeadersFactory.headersFactory().getValueValidator() : DefaultHttpHeadersFactory.headersFactory().withValidation(false).getValueValidator();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static DefaultHeaders.NameValidator<CharSequence> nameValidator(boolean validate) {
        return validate ? DefaultHttpHeadersFactory.headersFactory().getNameValidator() : DefaultHttpHeadersFactory.headersFactory().withNameValidation(false).getNameValidator();
    }

    /* loaded from: classes4.dex */
    private static class HeaderValueConverter extends CharSequenceValueConverter {
        static final HeaderValueConverter INSTANCE = new HeaderValueConverter();

        private HeaderValueConverter() {
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // io.netty.handler.codec.CharSequenceValueConverter, io.netty.handler.codec.ValueConverter
        public CharSequence convertObject(Object value) {
            if (value instanceof CharSequence) {
                return (CharSequence) value;
            }
            if (value instanceof Date) {
                return DateFormatter.format((Date) value);
            }
            if (value instanceof Calendar) {
                return DateFormatter.format(((Calendar) value).getTime());
            }
            return value.toString();
        }
    }
}
