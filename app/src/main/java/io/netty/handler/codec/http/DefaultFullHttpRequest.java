package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.util.IllegalReferenceCountException;
import io.netty.util.internal.ObjectUtil;

/* loaded from: classes4.dex */
public class DefaultFullHttpRequest extends DefaultHttpRequest implements FullHttpRequest {
    private final ByteBuf content;
    private int hash;
    private final HttpHeaders trailingHeader;

    public DefaultFullHttpRequest(HttpVersion httpVersion, HttpMethod method, String uri) {
        this(httpVersion, method, uri, Unpooled.buffer(0), DefaultHttpHeadersFactory.headersFactory(), DefaultHttpHeadersFactory.trailersFactory());
    }

    public DefaultFullHttpRequest(HttpVersion httpVersion, HttpMethod method, String uri, ByteBuf content) {
        this(httpVersion, method, uri, content, DefaultHttpHeadersFactory.headersFactory(), DefaultHttpHeadersFactory.trailersFactory());
    }

    @Deprecated
    public DefaultFullHttpRequest(HttpVersion httpVersion, HttpMethod method, String uri, boolean validateHeaders) {
        this(httpVersion, method, uri, Unpooled.buffer(0), DefaultHttpHeadersFactory.headersFactory().withValidation(validateHeaders), DefaultHttpHeadersFactory.trailersFactory().withValidation(validateHeaders));
    }

    @Deprecated
    public DefaultFullHttpRequest(HttpVersion httpVersion, HttpMethod method, String uri, ByteBuf content, boolean validateHeaders) {
        this(httpVersion, method, uri, content, DefaultHttpHeadersFactory.headersFactory().withValidation(validateHeaders), DefaultHttpHeadersFactory.trailersFactory().withValidation(validateHeaders));
    }

    public DefaultFullHttpRequest(HttpVersion httpVersion, HttpMethod method, String uri, ByteBuf content, HttpHeadersFactory headersFactory, HttpHeadersFactory trailersFactory) {
        this(httpVersion, method, uri, content, headersFactory.newHeaders(), trailersFactory.newHeaders());
    }

    public DefaultFullHttpRequest(HttpVersion httpVersion, HttpMethod method, String uri, ByteBuf content, HttpHeaders headers, HttpHeaders trailingHeader) {
        super(httpVersion, method, uri, headers);
        this.content = (ByteBuf) ObjectUtil.checkNotNull(content, "content");
        this.trailingHeader = (HttpHeaders) ObjectUtil.checkNotNull(trailingHeader, "trailingHeader");
    }

    @Override // io.netty.handler.codec.http.LastHttpContent
    public HttpHeaders trailingHeaders() {
        return this.trailingHeader;
    }

    @Override // io.netty.buffer.ByteBufHolder
    public ByteBuf content() {
        return this.content;
    }

    @Override // io.netty.util.ReferenceCounted
    public int refCnt() {
        return this.content.refCnt();
    }

    @Override // io.netty.util.ReferenceCounted
    public FullHttpRequest retain() {
        this.content.retain();
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public FullHttpRequest retain(int increment) {
        this.content.retain(increment);
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public FullHttpRequest touch() {
        this.content.touch();
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public FullHttpRequest touch(Object hint) {
        this.content.touch(hint);
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public boolean release() {
        return this.content.release();
    }

    @Override // io.netty.util.ReferenceCounted
    public boolean release(int decrement) {
        return this.content.release(decrement);
    }

    @Override // io.netty.handler.codec.http.DefaultHttpRequest, io.netty.handler.codec.http.DefaultHttpMessage, io.netty.handler.codec.http.HttpMessage, io.netty.handler.codec.http.HttpRequest, io.netty.handler.codec.http.FullHttpRequest
    public FullHttpRequest setProtocolVersion(HttpVersion version) {
        super.setProtocolVersion(version);
        return this;
    }

    @Override // io.netty.handler.codec.http.DefaultHttpRequest, io.netty.handler.codec.http.HttpRequest, io.netty.handler.codec.http.FullHttpRequest
    public FullHttpRequest setMethod(HttpMethod method) {
        super.setMethod(method);
        return this;
    }

    @Override // io.netty.handler.codec.http.DefaultHttpRequest, io.netty.handler.codec.http.HttpRequest, io.netty.handler.codec.http.FullHttpRequest
    public FullHttpRequest setUri(String uri) {
        super.setUri(uri);
        return this;
    }

    @Override // io.netty.handler.codec.http.LastHttpContent, io.netty.handler.codec.http.HttpContent, io.netty.buffer.ByteBufHolder
    public FullHttpRequest copy() {
        return replace(content().copy());
    }

    @Override // io.netty.handler.codec.http.LastHttpContent, io.netty.handler.codec.http.HttpContent, io.netty.buffer.ByteBufHolder
    public FullHttpRequest duplicate() {
        return replace(content().duplicate());
    }

    @Override // io.netty.handler.codec.http.LastHttpContent, io.netty.handler.codec.http.HttpContent, io.netty.buffer.ByteBufHolder
    public FullHttpRequest retainedDuplicate() {
        return replace(content().retainedDuplicate());
    }

    @Override // io.netty.handler.codec.http.LastHttpContent, io.netty.handler.codec.http.HttpContent, io.netty.buffer.ByteBufHolder
    public FullHttpRequest replace(ByteBuf content) {
        FullHttpRequest request = new DefaultFullHttpRequest(protocolVersion(), method(), uri(), content, headers().copy(), trailingHeaders().copy());
        request.setDecoderResult(decoderResult());
        return request;
    }

    @Override // io.netty.handler.codec.http.DefaultHttpRequest, io.netty.handler.codec.http.DefaultHttpMessage, io.netty.handler.codec.http.DefaultHttpObject
    public int hashCode() {
        int hash;
        int hash2 = this.hash;
        if (hash2 == 0) {
            if (ByteBufUtil.isAccessible(content())) {
                try {
                    hash = content().hashCode() + 31;
                } catch (IllegalReferenceCountException e) {
                    hash = 31;
                }
            } else {
                hash = 31;
            }
            int hash3 = (((hash * 31) + trailingHeaders().hashCode()) * 31) + super.hashCode();
            this.hash = hash3;
            return hash3;
        }
        return hash2;
    }

    @Override // io.netty.handler.codec.http.DefaultHttpRequest, io.netty.handler.codec.http.DefaultHttpMessage, io.netty.handler.codec.http.DefaultHttpObject
    public boolean equals(Object o) {
        if (!(o instanceof DefaultFullHttpRequest)) {
            return false;
        }
        DefaultFullHttpRequest other = (DefaultFullHttpRequest) o;
        return super.equals(other) && content().equals(other.content()) && trailingHeaders().equals(other.trailingHeaders());
    }

    @Override // io.netty.handler.codec.http.DefaultHttpRequest
    public String toString() {
        return HttpMessageUtil.appendFullRequest(new StringBuilder(256), this).toString();
    }
}
