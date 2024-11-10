package io.netty.handler.codec.http2;

import io.netty.handler.codec.CharSequenceValueConverter;
import io.netty.handler.codec.DefaultHeaders;
import io.netty.handler.codec.http.HttpHeaderValidationUtil;
import io.netty.handler.codec.http2.Http2Headers;
import io.netty.util.AsciiString;
import io.netty.util.ByteProcessor;
import io.netty.util.HashingStrategy;
import io.netty.util.internal.PlatformDependent;
import java.util.Iterator;

/* loaded from: classes4.dex */
public class DefaultHttp2Headers extends DefaultHeaders<CharSequence, CharSequence, Http2Headers> implements Http2Headers {
    private DefaultHeaders.HeaderEntry<CharSequence, CharSequence> firstNonPseudo;
    private static final ByteProcessor HTTP2_NAME_VALIDATOR_PROCESSOR = new ByteProcessor() { // from class: io.netty.handler.codec.http2.DefaultHttp2Headers.1
        @Override // io.netty.util.ByteProcessor
        public boolean process(byte value) {
            return !AsciiString.isUpperCase(value);
        }
    };
    static final DefaultHeaders.NameValidator<CharSequence> HTTP2_NAME_VALIDATOR = new DefaultHeaders.NameValidator<CharSequence>() { // from class: io.netty.handler.codec.http2.DefaultHttp2Headers.2
        @Override // io.netty.handler.codec.DefaultHeaders.NameValidator
        public void validateName(CharSequence name) {
            if (name == null || name.length() == 0) {
                PlatformDependent.throwException(Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "empty headers are not allowed [%s]", name));
            }
            if (Http2Headers.PseudoHeaderName.hasPseudoHeaderFormat(name)) {
                if (!Http2Headers.PseudoHeaderName.isPseudoHeader(name)) {
                    PlatformDependent.throwException(Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Invalid HTTP/2 pseudo-header '%s' encountered.", name));
                    return;
                }
                return;
            }
            if (name instanceof AsciiString) {
                try {
                    int index = ((AsciiString) name).forEachByte(DefaultHttp2Headers.HTTP2_NAME_VALIDATOR_PROCESSOR);
                    if (index != -1) {
                        PlatformDependent.throwException(Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "invalid header name [%s]", name));
                        return;
                    }
                    return;
                } catch (Http2Exception e) {
                    PlatformDependent.throwException(e);
                    return;
                } catch (Throwable t) {
                    PlatformDependent.throwException(Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, t, "unexpected error. invalid header name [%s]", name));
                    return;
                }
            }
            for (int i = 0; i < name.length(); i++) {
                if (AsciiString.isUpperCase(name.charAt(i))) {
                    PlatformDependent.throwException(Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "invalid header name [%s]", name));
                }
            }
        }
    };
    private static final DefaultHeaders.ValueValidator<CharSequence> VALUE_VALIDATOR = new DefaultHeaders.ValueValidator<CharSequence>() { // from class: io.netty.handler.codec.http2.DefaultHttp2Headers.3
        @Override // io.netty.handler.codec.DefaultHeaders.ValueValidator
        public void validate(CharSequence value) {
            int index = HttpHeaderValidationUtil.validateValidHeaderValue(value);
            if (index != -1) {
                throw new IllegalArgumentException("a header value contains prohibited character 0x" + Integer.toHexString(value.charAt(index)) + " at index " + index + '.');
            }
        }
    };

    @Override // io.netty.handler.codec.http2.Http2Headers
    public /* bridge */ /* synthetic */ Iterator valueIterator(CharSequence x0) {
        return super.valueIterator((DefaultHttp2Headers) x0);
    }

    public DefaultHttp2Headers() {
        this(true);
    }

    public DefaultHttp2Headers(boolean validate) {
        super(AsciiString.CASE_SENSITIVE_HASHER, CharSequenceValueConverter.INSTANCE, validate ? HTTP2_NAME_VALIDATOR : DefaultHeaders.NameValidator.NOT_NULL);
        this.firstNonPseudo = this.head;
    }

    public DefaultHttp2Headers(boolean validate, int arraySizeHint) {
        super(AsciiString.CASE_SENSITIVE_HASHER, CharSequenceValueConverter.INSTANCE, validate ? HTTP2_NAME_VALIDATOR : DefaultHeaders.NameValidator.NOT_NULL, arraySizeHint);
        this.firstNonPseudo = this.head;
    }

    public DefaultHttp2Headers(boolean validate, boolean validateValues, int arraySizeHint) {
        super(AsciiString.CASE_SENSITIVE_HASHER, CharSequenceValueConverter.INSTANCE, validate ? HTTP2_NAME_VALIDATOR : DefaultHeaders.NameValidator.NOT_NULL, arraySizeHint, validateValues ? VALUE_VALIDATOR : DefaultHeaders.ValueValidator.NO_VALIDATION);
        this.firstNonPseudo = this.head;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.DefaultHeaders
    public void validateName(DefaultHeaders.NameValidator<CharSequence> validator, boolean forAdd, CharSequence name) {
        super.validateName((DefaultHeaders.NameValidator<boolean>) validator, forAdd, (boolean) name);
        if (nameValidator() == HTTP2_NAME_VALIDATOR && forAdd && Http2Headers.PseudoHeaderName.hasPseudoHeaderFormat(name) && contains(name)) {
            PlatformDependent.throwException(Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Duplicate HTTP/2 pseudo-header '%s' encountered.", name));
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.DefaultHeaders
    public void validateValue(DefaultHeaders.ValueValidator<CharSequence> validator, CharSequence name, CharSequence value) {
        super.validateValue(validator, (DefaultHeaders.ValueValidator<CharSequence>) name, value);
        if (nameValidator() == HTTP2_NAME_VALIDATOR) {
            if ((value == null || value.length() == 0) && Http2Headers.PseudoHeaderName.hasPseudoHeaderFormat(name)) {
                PlatformDependent.throwException(Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "HTTP/2 pseudo-header '%s' must not be empty.", name));
            }
        }
    }

    @Override // io.netty.handler.codec.DefaultHeaders, io.netty.handler.codec.Headers
    public Http2Headers clear() {
        this.firstNonPseudo = this.head;
        return (Http2Headers) super.clear();
    }

    @Override // io.netty.handler.codec.DefaultHeaders
    public boolean equals(Object o) {
        return (o instanceof Http2Headers) && equals((Http2Headers) o, AsciiString.CASE_SENSITIVE_HASHER);
    }

    @Override // io.netty.handler.codec.DefaultHeaders
    public int hashCode() {
        return hashCode(AsciiString.CASE_SENSITIVE_HASHER);
    }

    @Override // io.netty.handler.codec.http2.Http2Headers
    public Http2Headers method(CharSequence value) {
        set((DefaultHttp2Headers) Http2Headers.PseudoHeaderName.METHOD.value(), (AsciiString) value);
        return this;
    }

    @Override // io.netty.handler.codec.http2.Http2Headers
    public Http2Headers scheme(CharSequence value) {
        set((DefaultHttp2Headers) Http2Headers.PseudoHeaderName.SCHEME.value(), (AsciiString) value);
        return this;
    }

    @Override // io.netty.handler.codec.http2.Http2Headers
    public Http2Headers authority(CharSequence value) {
        set((DefaultHttp2Headers) Http2Headers.PseudoHeaderName.AUTHORITY.value(), (AsciiString) value);
        return this;
    }

    @Override // io.netty.handler.codec.http2.Http2Headers
    public Http2Headers path(CharSequence value) {
        set((DefaultHttp2Headers) Http2Headers.PseudoHeaderName.PATH.value(), (AsciiString) value);
        return this;
    }

    @Override // io.netty.handler.codec.http2.Http2Headers
    public Http2Headers status(CharSequence value) {
        set((DefaultHttp2Headers) Http2Headers.PseudoHeaderName.STATUS.value(), (AsciiString) value);
        return this;
    }

    @Override // io.netty.handler.codec.http2.Http2Headers
    public CharSequence method() {
        return get(Http2Headers.PseudoHeaderName.METHOD.value());
    }

    @Override // io.netty.handler.codec.http2.Http2Headers
    public CharSequence scheme() {
        return get(Http2Headers.PseudoHeaderName.SCHEME.value());
    }

    @Override // io.netty.handler.codec.http2.Http2Headers
    public CharSequence authority() {
        return get(Http2Headers.PseudoHeaderName.AUTHORITY.value());
    }

    @Override // io.netty.handler.codec.http2.Http2Headers
    public CharSequence path() {
        return get(Http2Headers.PseudoHeaderName.PATH.value());
    }

    @Override // io.netty.handler.codec.http2.Http2Headers
    public CharSequence status() {
        return get(Http2Headers.PseudoHeaderName.STATUS.value());
    }

    @Override // io.netty.handler.codec.DefaultHeaders, io.netty.handler.codec.Headers
    public boolean contains(CharSequence name, CharSequence value) {
        return contains(name, value, false);
    }

    @Override // io.netty.handler.codec.http2.Http2Headers
    public boolean contains(CharSequence name, CharSequence value, boolean caseInsensitive) {
        return contains((DefaultHttp2Headers) name, value, (HashingStrategy<? super CharSequence>) (caseInsensitive ? AsciiString.CASE_INSENSITIVE_HASHER : AsciiString.CASE_SENSITIVE_HASHER));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.DefaultHeaders
    public final DefaultHeaders.HeaderEntry<CharSequence, CharSequence> newHeaderEntry(int h, CharSequence name, CharSequence value, DefaultHeaders.HeaderEntry<CharSequence, CharSequence> next) {
        return new Http2HeaderEntry(h, name, value, next);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class Http2HeaderEntry extends DefaultHeaders.HeaderEntry<CharSequence, CharSequence> {
        /* JADX WARN: Multi-variable type inference failed */
        Http2HeaderEntry(int hash, CharSequence key, CharSequence charSequence, DefaultHeaders.HeaderEntry<CharSequence, CharSequence> headerEntry) {
            super(hash, key);
            this.value = charSequence;
            this.next = headerEntry;
            if (Http2Headers.PseudoHeaderName.hasPseudoHeaderFormat(key)) {
                this.after = DefaultHttp2Headers.this.firstNonPseudo;
                this.before = DefaultHttp2Headers.this.firstNonPseudo.before();
            } else {
                this.after = DefaultHttp2Headers.this.head;
                this.before = DefaultHttp2Headers.this.head.before();
                if (DefaultHttp2Headers.this.firstNonPseudo == DefaultHttp2Headers.this.head) {
                    DefaultHttp2Headers.this.firstNonPseudo = this;
                }
            }
            pointNeighborsToThis();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // io.netty.handler.codec.DefaultHeaders.HeaderEntry
        public void remove() {
            if (this == DefaultHttp2Headers.this.firstNonPseudo) {
                DefaultHttp2Headers.this.firstNonPseudo = DefaultHttp2Headers.this.firstNonPseudo.after();
            }
            super.remove();
        }
    }
}
