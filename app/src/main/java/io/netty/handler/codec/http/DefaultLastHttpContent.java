package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.StringUtil;
import java.util.Iterator;
import java.util.Map;

/* loaded from: classes4.dex */
public class DefaultLastHttpContent extends DefaultHttpContent implements LastHttpContent {
    private final HttpHeaders trailingHeaders;

    public DefaultLastHttpContent() {
        this(Unpooled.buffer(0));
    }

    public DefaultLastHttpContent(ByteBuf content) {
        this(content, DefaultHttpHeadersFactory.trailersFactory());
    }

    @Deprecated
    public DefaultLastHttpContent(ByteBuf content, boolean validateHeaders) {
        this(content, DefaultHttpHeadersFactory.trailersFactory().withValidation(validateHeaders));
    }

    public DefaultLastHttpContent(ByteBuf content, HttpHeadersFactory trailersFactory) {
        super(content);
        this.trailingHeaders = trailersFactory.newHeaders();
    }

    public DefaultLastHttpContent(ByteBuf content, HttpHeaders trailingHeaders) {
        super(content);
        this.trailingHeaders = (HttpHeaders) ObjectUtil.checkNotNull(trailingHeaders, "trailingHeaders");
    }

    @Override // io.netty.handler.codec.http.DefaultHttpContent, io.netty.handler.codec.http.HttpContent, io.netty.buffer.ByteBufHolder
    public LastHttpContent copy() {
        return replace(content().copy());
    }

    @Override // io.netty.handler.codec.http.DefaultHttpContent, io.netty.handler.codec.http.HttpContent, io.netty.buffer.ByteBufHolder
    public LastHttpContent duplicate() {
        return replace(content().duplicate());
    }

    @Override // io.netty.handler.codec.http.DefaultHttpContent, io.netty.handler.codec.http.HttpContent, io.netty.buffer.ByteBufHolder
    public LastHttpContent retainedDuplicate() {
        return replace(content().retainedDuplicate());
    }

    @Override // io.netty.handler.codec.http.DefaultHttpContent, io.netty.handler.codec.http.HttpContent, io.netty.buffer.ByteBufHolder
    public LastHttpContent replace(ByteBuf content) {
        return new DefaultLastHttpContent(content, trailingHeaders().copy());
    }

    @Override // io.netty.handler.codec.http.DefaultHttpContent, io.netty.util.ReferenceCounted
    public LastHttpContent retain(int increment) {
        super.retain(increment);
        return this;
    }

    @Override // io.netty.handler.codec.http.DefaultHttpContent, io.netty.util.ReferenceCounted
    public LastHttpContent retain() {
        super.retain();
        return this;
    }

    @Override // io.netty.handler.codec.http.DefaultHttpContent, io.netty.util.ReferenceCounted
    public LastHttpContent touch() {
        super.touch();
        return this;
    }

    @Override // io.netty.handler.codec.http.DefaultHttpContent, io.netty.util.ReferenceCounted
    public LastHttpContent touch(Object hint) {
        super.touch(hint);
        return this;
    }

    @Override // io.netty.handler.codec.http.LastHttpContent
    public HttpHeaders trailingHeaders() {
        return this.trailingHeaders;
    }

    @Override // io.netty.handler.codec.http.DefaultHttpContent
    public String toString() {
        StringBuilder buf = new StringBuilder(super.toString());
        buf.append(StringUtil.NEWLINE);
        appendHeaders(buf);
        buf.setLength(buf.length() - StringUtil.NEWLINE.length());
        return buf.toString();
    }

    private void appendHeaders(StringBuilder buf) {
        Iterator<Map.Entry<String, String>> it2 = trailingHeaders().iterator();
        while (it2.hasNext()) {
            Map.Entry<String, String> e = it2.next();
            buf.append(e.getKey());
            buf.append(": ");
            buf.append(e.getValue());
            buf.append(StringUtil.NEWLINE);
        }
    }
}
