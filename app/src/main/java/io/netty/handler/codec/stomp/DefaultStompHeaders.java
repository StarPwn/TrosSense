package io.netty.handler.codec.stomp;

import io.netty.handler.codec.CharSequenceValueConverter;
import io.netty.handler.codec.DefaultHeaders;
import io.netty.handler.codec.HeadersUtils;
import io.netty.util.AsciiString;
import io.netty.util.HashingStrategy;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: classes4.dex */
public class DefaultStompHeaders extends DefaultHeaders<CharSequence, CharSequence, StompHeaders> implements StompHeaders {
    public DefaultStompHeaders() {
        super(AsciiString.CASE_SENSITIVE_HASHER, CharSequenceValueConverter.INSTANCE);
    }

    @Override // io.netty.handler.codec.stomp.StompHeaders
    public String getAsString(CharSequence name) {
        return HeadersUtils.getAsString(this, name);
    }

    @Override // io.netty.handler.codec.stomp.StompHeaders
    public List<String> getAllAsString(CharSequence name) {
        return HeadersUtils.getAllAsString(this, name);
    }

    @Override // io.netty.handler.codec.stomp.StompHeaders
    public Iterator<Map.Entry<String, String>> iteratorAsString() {
        return HeadersUtils.iteratorAsString(this);
    }

    @Override // io.netty.handler.codec.DefaultHeaders, io.netty.handler.codec.Headers
    public boolean contains(CharSequence name, CharSequence value) {
        return contains(name, value, false);
    }

    @Override // io.netty.handler.codec.stomp.StompHeaders
    public boolean contains(CharSequence name, CharSequence value, boolean ignoreCase) {
        return contains((DefaultStompHeaders) name, value, (HashingStrategy<? super CharSequence>) (ignoreCase ? AsciiString.CASE_INSENSITIVE_HASHER : AsciiString.CASE_SENSITIVE_HASHER));
    }

    @Override // io.netty.handler.codec.DefaultHeaders
    public DefaultHeaders<CharSequence, CharSequence, StompHeaders> copy() {
        DefaultStompHeaders copyHeaders = new DefaultStompHeaders();
        copyHeaders.addImpl(this);
        return copyHeaders;
    }
}
