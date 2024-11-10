package io.netty.handler.codec.http;

import io.netty.handler.codec.DefaultHeaders;
import io.netty.handler.codec.Headers;
import io.netty.handler.codec.ValueConverter;
import io.netty.util.AsciiString;
import io.netty.util.HashingStrategy;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.StringUtil;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: classes4.dex */
public class CombinedHttpHeaders extends DefaultHttpHeaders {
    @Deprecated
    public CombinedHttpHeaders(boolean validate) {
        super(new CombinedHttpHeadersImpl(AsciiString.CASE_INSENSITIVE_HASHER, valueConverter(), nameValidator(validate), valueValidator(validate)));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CombinedHttpHeaders(DefaultHeaders.NameValidator<CharSequence> nameValidator, DefaultHeaders.ValueValidator<CharSequence> valueValidator) {
        super(new CombinedHttpHeadersImpl(AsciiString.CASE_INSENSITIVE_HASHER, valueConverter(), (DefaultHeaders.NameValidator) ObjectUtil.checkNotNull(nameValidator, "nameValidator"), (DefaultHeaders.ValueValidator) ObjectUtil.checkNotNull(valueValidator, "valueValidator")));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CombinedHttpHeaders(DefaultHeaders.NameValidator<CharSequence> nameValidator, DefaultHeaders.ValueValidator<CharSequence> valueValidator, int sizeHint) {
        super(new CombinedHttpHeadersImpl(AsciiString.CASE_INSENSITIVE_HASHER, valueConverter(), (DefaultHeaders.NameValidator) ObjectUtil.checkNotNull(nameValidator, "nameValidator"), (DefaultHeaders.ValueValidator) ObjectUtil.checkNotNull(valueValidator, "valueValidator"), sizeHint));
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public boolean containsValue(CharSequence name, CharSequence value, boolean ignoreCase) {
        return super.containsValue(name, StringUtil.trimOws(value), ignoreCase);
    }

    /* loaded from: classes4.dex */
    private static final class CombinedHttpHeadersImpl extends DefaultHeaders<CharSequence, CharSequence, CombinedHttpHeadersImpl> {
        private static final int VALUE_LENGTH_ESTIMATE = 10;
        private CsvValueEscaper<CharSequence> charSequenceEscaper;
        private CsvValueEscaper<Object> objectEscaper;

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes4.dex */
        public interface CsvValueEscaper<T> {
            CharSequence escape(CharSequence charSequence, T t);
        }

        @Override // io.netty.handler.codec.DefaultHeaders, io.netty.handler.codec.Headers
        public /* bridge */ /* synthetic */ Headers add(Headers headers) {
            return add((Headers<? extends CharSequence, ? extends CharSequence, ?>) headers);
        }

        @Override // io.netty.handler.codec.DefaultHeaders, io.netty.handler.codec.Headers
        public /* bridge */ /* synthetic */ Headers add(Object obj, Iterable iterable) {
            return add((CharSequence) obj, (Iterable<? extends CharSequence>) iterable);
        }

        @Override // io.netty.handler.codec.DefaultHeaders, io.netty.handler.codec.Headers
        public /* bridge */ /* synthetic */ Headers addObject(Object obj, Iterable iterable) {
            return addObject((CharSequence) obj, (Iterable<?>) iterable);
        }

        @Override // io.netty.handler.codec.DefaultHeaders, io.netty.handler.codec.Headers
        public /* bridge */ /* synthetic */ Headers set(Headers headers) {
            return set((Headers<? extends CharSequence, ? extends CharSequence, ?>) headers);
        }

        @Override // io.netty.handler.codec.DefaultHeaders, io.netty.handler.codec.Headers
        public /* bridge */ /* synthetic */ Headers set(Object obj, Iterable iterable) {
            return set((CharSequence) obj, (Iterable<? extends CharSequence>) iterable);
        }

        @Override // io.netty.handler.codec.DefaultHeaders, io.netty.handler.codec.Headers
        public /* bridge */ /* synthetic */ Headers setAll(Headers headers) {
            return setAll((Headers<? extends CharSequence, ? extends CharSequence, ?>) headers);
        }

        @Override // io.netty.handler.codec.DefaultHeaders, io.netty.handler.codec.Headers
        public /* bridge */ /* synthetic */ Headers setObject(Object obj, Iterable iterable) {
            return setObject((CharSequence) obj, (Iterable<?>) iterable);
        }

        private CsvValueEscaper<Object> objectEscaper() {
            if (this.objectEscaper == null) {
                this.objectEscaper = new CsvValueEscaper<Object>() { // from class: io.netty.handler.codec.http.CombinedHttpHeaders.CombinedHttpHeadersImpl.1
                    @Override // io.netty.handler.codec.http.CombinedHttpHeaders.CombinedHttpHeadersImpl.CsvValueEscaper
                    public CharSequence escape(CharSequence name, Object value) {
                        try {
                            CharSequence converted = (CharSequence) CombinedHttpHeadersImpl.this.valueConverter().convertObject(value);
                            return StringUtil.escapeCsv(converted, true);
                        } catch (IllegalArgumentException e) {
                            throw new IllegalArgumentException("Failed to convert object value for header '" + ((Object) name) + '\'', e);
                        }
                    }
                };
            }
            return this.objectEscaper;
        }

        private CsvValueEscaper<CharSequence> charSequenceEscaper() {
            if (this.charSequenceEscaper == null) {
                this.charSequenceEscaper = new CsvValueEscaper<CharSequence>() { // from class: io.netty.handler.codec.http.CombinedHttpHeaders.CombinedHttpHeadersImpl.2
                    @Override // io.netty.handler.codec.http.CombinedHttpHeaders.CombinedHttpHeadersImpl.CsvValueEscaper
                    public CharSequence escape(CharSequence name, CharSequence value) {
                        return StringUtil.escapeCsv(value, true);
                    }
                };
            }
            return this.charSequenceEscaper;
        }

        CombinedHttpHeadersImpl(HashingStrategy<CharSequence> nameHashingStrategy, ValueConverter<CharSequence> valueConverter, DefaultHeaders.NameValidator<CharSequence> nameValidator, DefaultHeaders.ValueValidator<CharSequence> valueValidator) {
            this(nameHashingStrategy, valueConverter, nameValidator, valueValidator, 16);
        }

        CombinedHttpHeadersImpl(HashingStrategy<CharSequence> nameHashingStrategy, ValueConverter<CharSequence> valueConverter, DefaultHeaders.NameValidator<CharSequence> nameValidator, DefaultHeaders.ValueValidator<CharSequence> valueValidator, int sizeHint) {
            super(nameHashingStrategy, valueConverter, nameValidator, sizeHint, valueValidator);
        }

        @Override // io.netty.handler.codec.DefaultHeaders
        public Iterator<CharSequence> valueIterator(CharSequence name) {
            Iterator<CharSequence> itr = super.valueIterator((CombinedHttpHeadersImpl) name);
            if (!itr.hasNext() || cannotBeCombined(name)) {
                return itr;
            }
            Iterator<CharSequence> unescapedItr = StringUtil.unescapeCsvFields(itr.next()).iterator();
            if (itr.hasNext()) {
                throw new IllegalStateException("CombinedHttpHeaders should only have one value");
            }
            return unescapedItr;
        }

        @Override // io.netty.handler.codec.DefaultHeaders, io.netty.handler.codec.Headers
        public List<CharSequence> getAll(CharSequence name) {
            List<CharSequence> values = super.getAll((CombinedHttpHeadersImpl) name);
            if (values.isEmpty() || cannotBeCombined(name)) {
                return values;
            }
            if (values.size() != 1) {
                throw new IllegalStateException("CombinedHttpHeaders should only have one value");
            }
            return StringUtil.unescapeCsvFields(values.get(0));
        }

        @Override // io.netty.handler.codec.DefaultHeaders, io.netty.handler.codec.Headers
        public CombinedHttpHeadersImpl add(Headers<? extends CharSequence, ? extends CharSequence, ?> headers) {
            if (headers == this) {
                throw new IllegalArgumentException("can't add to itself.");
            }
            if (headers instanceof CombinedHttpHeadersImpl) {
                if (isEmpty()) {
                    addImpl(headers);
                } else {
                    for (Map.Entry<? extends CharSequence, ? extends CharSequence> header : headers) {
                        addEscapedValue(header.getKey(), header.getValue());
                    }
                }
            } else {
                for (Map.Entry<? extends CharSequence, ? extends CharSequence> header2 : headers) {
                    add(header2.getKey(), header2.getValue());
                }
            }
            return this;
        }

        @Override // io.netty.handler.codec.DefaultHeaders, io.netty.handler.codec.Headers
        public CombinedHttpHeadersImpl set(Headers<? extends CharSequence, ? extends CharSequence, ?> headers) {
            if (headers == this) {
                return this;
            }
            clear();
            return add(headers);
        }

        @Override // io.netty.handler.codec.DefaultHeaders, io.netty.handler.codec.Headers
        public CombinedHttpHeadersImpl setAll(Headers<? extends CharSequence, ? extends CharSequence, ?> headers) {
            if (headers == this) {
                return this;
            }
            for (CharSequence key : headers.names()) {
                remove(key);
            }
            return add(headers);
        }

        @Override // io.netty.handler.codec.DefaultHeaders, io.netty.handler.codec.Headers
        public CombinedHttpHeadersImpl add(CharSequence name, CharSequence value) {
            return addEscapedValue(name, charSequenceEscaper().escape(name, value));
        }

        @Override // io.netty.handler.codec.DefaultHeaders, io.netty.handler.codec.Headers
        public CombinedHttpHeadersImpl add(CharSequence name, CharSequence... values) {
            return addEscapedValue(name, commaSeparate(name, charSequenceEscaper(), values));
        }

        public CombinedHttpHeadersImpl add(CharSequence name, Iterable<? extends CharSequence> values) {
            return addEscapedValue(name, commaSeparate(name, charSequenceEscaper(), values));
        }

        @Override // io.netty.handler.codec.DefaultHeaders, io.netty.handler.codec.Headers
        public CombinedHttpHeadersImpl addObject(CharSequence name, Object value) {
            return addEscapedValue(name, commaSeparate(name, objectEscaper(), value));
        }

        public CombinedHttpHeadersImpl addObject(CharSequence name, Iterable<?> values) {
            return addEscapedValue(name, commaSeparate(name, objectEscaper(), values));
        }

        @Override // io.netty.handler.codec.DefaultHeaders, io.netty.handler.codec.Headers
        public CombinedHttpHeadersImpl addObject(CharSequence name, Object... values) {
            return addEscapedValue(name, commaSeparate(name, objectEscaper(), values));
        }

        @Override // io.netty.handler.codec.DefaultHeaders, io.netty.handler.codec.Headers
        public CombinedHttpHeadersImpl set(CharSequence name, CharSequence... values) {
            set((CombinedHttpHeadersImpl) name, commaSeparate(name, charSequenceEscaper(), values));
            return this;
        }

        public CombinedHttpHeadersImpl set(CharSequence name, Iterable<? extends CharSequence> values) {
            set((CombinedHttpHeadersImpl) name, commaSeparate(name, charSequenceEscaper(), values));
            return this;
        }

        @Override // io.netty.handler.codec.DefaultHeaders, io.netty.handler.codec.Headers
        public CombinedHttpHeadersImpl setObject(CharSequence name, Object value) {
            set((CombinedHttpHeadersImpl) name, commaSeparate(name, objectEscaper(), value));
            return this;
        }

        @Override // io.netty.handler.codec.DefaultHeaders, io.netty.handler.codec.Headers
        public CombinedHttpHeadersImpl setObject(CharSequence name, Object... values) {
            set((CombinedHttpHeadersImpl) name, commaSeparate(name, objectEscaper(), values));
            return this;
        }

        public CombinedHttpHeadersImpl setObject(CharSequence name, Iterable<?> values) {
            set((CombinedHttpHeadersImpl) name, commaSeparate(name, objectEscaper(), values));
            return this;
        }

        private static boolean cannotBeCombined(CharSequence name) {
            return HttpHeaderNames.SET_COOKIE.contentEqualsIgnoreCase(name);
        }

        private CombinedHttpHeadersImpl addEscapedValue(CharSequence name, CharSequence escapedValue) {
            CharSequence currentValue = get(name);
            if (currentValue == null || cannotBeCombined(name)) {
                super.add((CombinedHttpHeadersImpl) name, escapedValue);
            } else {
                set((CombinedHttpHeadersImpl) name, commaSeparateEscapedValues(currentValue, escapedValue));
            }
            return this;
        }

        private static <T> CharSequence commaSeparate(CharSequence name, CsvValueEscaper<T> escaper, T... values) {
            StringBuilder sb = new StringBuilder(values.length * 10);
            if (values.length > 0) {
                int end = values.length - 1;
                for (int i = 0; i < end; i++) {
                    sb.append(escaper.escape(name, values[i])).append(StringUtil.COMMA);
                }
                sb.append(escaper.escape(name, values[end]));
            }
            return sb;
        }

        private static <T> CharSequence commaSeparate(CharSequence name, CsvValueEscaper<T> escaper, Iterable<? extends T> values) {
            StringBuilder sb = values instanceof Collection ? new StringBuilder(((Collection) values).size() * 10) : new StringBuilder();
            Iterator<? extends T> iterator = values.iterator();
            if (iterator.hasNext()) {
                T next = iterator.next();
                while (iterator.hasNext()) {
                    sb.append(escaper.escape(name, next)).append(StringUtil.COMMA);
                    next = iterator.next();
                }
                sb.append(escaper.escape(name, next));
            }
            return sb;
        }

        private static CharSequence commaSeparateEscapedValues(CharSequence currentValue, CharSequence value) {
            return new StringBuilder(currentValue.length() + 1 + value.length()).append(currentValue).append(StringUtil.COMMA).append(value);
        }
    }
}
