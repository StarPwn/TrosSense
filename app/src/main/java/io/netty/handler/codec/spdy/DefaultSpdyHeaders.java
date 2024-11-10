package io.netty.handler.codec.spdy;

import io.netty.handler.codec.CharSequenceValueConverter;
import io.netty.handler.codec.DefaultHeaders;
import io.netty.handler.codec.HeadersUtils;
import io.netty.util.AsciiString;
import io.netty.util.HashingStrategy;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: classes4.dex */
public class DefaultSpdyHeaders extends DefaultHeaders<CharSequence, CharSequence, SpdyHeaders> implements SpdyHeaders {
    private static final DefaultHeaders.NameValidator<CharSequence> SpdyNameValidator = new DefaultHeaders.NameValidator<CharSequence>() { // from class: io.netty.handler.codec.spdy.DefaultSpdyHeaders.1
        @Override // io.netty.handler.codec.DefaultHeaders.NameValidator
        public void validateName(CharSequence name) {
            SpdyCodecUtil.validateHeaderName(name);
        }
    };

    public DefaultSpdyHeaders() {
        this(true);
    }

    public DefaultSpdyHeaders(boolean validate) {
        super(AsciiString.CASE_INSENSITIVE_HASHER, validate ? HeaderValueConverterAndValidator.INSTANCE : CharSequenceValueConverter.INSTANCE, validate ? SpdyNameValidator : DefaultHeaders.NameValidator.NOT_NULL);
    }

    @Override // io.netty.handler.codec.spdy.SpdyHeaders
    public String getAsString(CharSequence name) {
        return HeadersUtils.getAsString(this, name);
    }

    @Override // io.netty.handler.codec.spdy.SpdyHeaders
    public List<String> getAllAsString(CharSequence name) {
        return HeadersUtils.getAllAsString(this, name);
    }

    @Override // io.netty.handler.codec.spdy.SpdyHeaders
    public Iterator<Map.Entry<String, String>> iteratorAsString() {
        return HeadersUtils.iteratorAsString(this);
    }

    @Override // io.netty.handler.codec.DefaultHeaders, io.netty.handler.codec.Headers
    public boolean contains(CharSequence name, CharSequence value) {
        return contains(name, value, false);
    }

    @Override // io.netty.handler.codec.spdy.SpdyHeaders
    public boolean contains(CharSequence name, CharSequence value, boolean ignoreCase) {
        return contains((DefaultSpdyHeaders) name, value, (HashingStrategy<? super CharSequence>) (ignoreCase ? AsciiString.CASE_INSENSITIVE_HASHER : AsciiString.CASE_SENSITIVE_HASHER));
    }

    /* loaded from: classes4.dex */
    private static final class HeaderValueConverterAndValidator extends CharSequenceValueConverter {
        public static final HeaderValueConverterAndValidator INSTANCE = new HeaderValueConverterAndValidator();

        private HeaderValueConverterAndValidator() {
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // io.netty.handler.codec.CharSequenceValueConverter, io.netty.handler.codec.ValueConverter
        public CharSequence convertObject(Object value) {
            CharSequence seq = super.convertObject(value);
            SpdyCodecUtil.validateHeaderValue(seq);
            return seq;
        }
    }
}
