package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.util.IllegalReferenceCountException;
import io.netty.util.internal.ObjectUtil;

/* loaded from: classes4.dex */
public class DefaultFullHttpResponse extends DefaultHttpResponse implements FullHttpResponse {
    private final ByteBuf content;
    private int hash;
    private final HttpHeaders trailingHeaders;

    public DefaultFullHttpResponse(HttpVersion version, HttpResponseStatus status) {
        this(version, status, Unpooled.buffer(0), DefaultHttpHeadersFactory.headersFactory(), DefaultHttpHeadersFactory.trailersFactory());
    }

    public DefaultFullHttpResponse(HttpVersion version, HttpResponseStatus status, ByteBuf content) {
        this(version, status, content, DefaultHttpHeadersFactory.headersFactory(), DefaultHttpHeadersFactory.trailersFactory());
    }

    @Deprecated
    public DefaultFullHttpResponse(HttpVersion version, HttpResponseStatus status, boolean validateHeaders) {
        this(version, status, Unpooled.buffer(0), DefaultHttpHeadersFactory.headersFactory().withValidation(validateHeaders), DefaultHttpHeadersFactory.trailersFactory().withValidation(validateHeaders));
    }

    @Deprecated
    public DefaultFullHttpResponse(HttpVersion version, HttpResponseStatus status, boolean validateHeaders, boolean singleFieldHeaders) {
        this(version, status, Unpooled.buffer(0), DefaultHttpHeadersFactory.headersFactory().withValidation(validateHeaders).withCombiningHeaders(singleFieldHeaders), DefaultHttpHeadersFactory.trailersFactory().withValidation(validateHeaders).withCombiningHeaders(singleFieldHeaders));
    }

    @Deprecated
    public DefaultFullHttpResponse(HttpVersion version, HttpResponseStatus status, ByteBuf content, boolean validateHeaders) {
        this(version, status, content, DefaultHttpHeadersFactory.headersFactory().withValidation(validateHeaders), DefaultHttpHeadersFactory.trailersFactory().withValidation(validateHeaders));
    }

    @Deprecated
    public DefaultFullHttpResponse(HttpVersion version, HttpResponseStatus status, ByteBuf content, boolean validateHeaders, boolean singleFieldHeaders) {
        this(version, status, content, DefaultHttpHeadersFactory.headersFactory().withValidation(validateHeaders).withCombiningHeaders(singleFieldHeaders), DefaultHttpHeadersFactory.trailersFactory().withValidation(validateHeaders).withCombiningHeaders(singleFieldHeaders));
    }

    public DefaultFullHttpResponse(HttpVersion version, HttpResponseStatus status, ByteBuf content, HttpHeadersFactory headersFactory, HttpHeadersFactory trailersFactory) {
        this(version, status, content, headersFactory.newHeaders(), trailersFactory.newHeaders());
    }

    public DefaultFullHttpResponse(HttpVersion version, HttpResponseStatus status, ByteBuf content, HttpHeaders headers, HttpHeaders trailingHeaders) {
        super(version, status, headers);
        this.content = (ByteBuf) ObjectUtil.checkNotNull(content, "content");
        this.trailingHeaders = (HttpHeaders) ObjectUtil.checkNotNull(trailingHeaders, "trailingHeaders");
    }

    @Override // io.netty.handler.codec.http.LastHttpContent
    public HttpHeaders trailingHeaders() {
        return this.trailingHeaders;
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
    public FullHttpResponse retain() {
        this.content.retain();
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public FullHttpResponse retain(int increment) {
        this.content.retain(increment);
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public FullHttpResponse touch() {
        this.content.touch();
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public FullHttpResponse touch(Object hint) {
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

    @Override // io.netty.handler.codec.http.DefaultHttpResponse, io.netty.handler.codec.http.DefaultHttpMessage, io.netty.handler.codec.http.HttpMessage, io.netty.handler.codec.http.HttpRequest, io.netty.handler.codec.http.FullHttpRequest
    public FullHttpResponse setProtocolVersion(HttpVersion version) {
        super.setProtocolVersion(version);
        return this;
    }

    @Override // io.netty.handler.codec.http.DefaultHttpResponse, io.netty.handler.codec.http.HttpResponse, io.netty.handler.codec.http.FullHttpResponse
    public FullHttpResponse setStatus(HttpResponseStatus status) {
        super.setStatus(status);
        return this;
    }

    @Override // io.netty.handler.codec.http.LastHttpContent, io.netty.handler.codec.http.HttpContent, io.netty.buffer.ByteBufHolder
    public FullHttpResponse copy() {
        return replace(content().copy());
    }

    @Override // io.netty.handler.codec.http.LastHttpContent, io.netty.handler.codec.http.HttpContent, io.netty.buffer.ByteBufHolder
    public FullHttpResponse duplicate() {
        return replace(content().duplicate());
    }

    @Override // io.netty.handler.codec.http.LastHttpContent, io.netty.handler.codec.http.HttpContent, io.netty.buffer.ByteBufHolder
    public FullHttpResponse retainedDuplicate() {
        return replace(content().retainedDuplicate());
    }

    @Override // io.netty.handler.codec.http.LastHttpContent, io.netty.handler.codec.http.HttpContent, io.netty.buffer.ByteBufHolder
    public FullHttpResponse replace(ByteBuf content) {
        FullHttpResponse response = new DefaultFullHttpResponse(protocolVersion(), status(), content, headers().copy(), trailingHeaders().copy());
        response.setDecoderResult(decoderResult());
        return response;
    }

    @Override // io.netty.handler.codec.http.DefaultHttpResponse, io.netty.handler.codec.http.DefaultHttpMessage, io.netty.handler.codec.http.DefaultHttpObject
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

    @Override // io.netty.handler.codec.http.DefaultHttpResponse, io.netty.handler.codec.http.DefaultHttpMessage, io.netty.handler.codec.http.DefaultHttpObject
    public boolean equals(Object o) {
        if (!(o instanceof DefaultFullHttpResponse)) {
            return false;
        }
        DefaultFullHttpResponse other = (DefaultFullHttpResponse) o;
        return super.equals(other) && content().equals(other.content()) && trailingHeaders().equals(other.trailingHeaders());
    }

    @Override // io.netty.handler.codec.http.DefaultHttpResponse
    public String toString() {
        return HttpMessageUtil.appendFullResponse(new StringBuilder(256), this).toString();
    }
}
