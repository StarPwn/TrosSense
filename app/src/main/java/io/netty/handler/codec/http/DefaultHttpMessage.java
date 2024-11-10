package io.netty.handler.codec.http;

import io.netty.util.internal.ObjectUtil;

/* loaded from: classes4.dex */
public abstract class DefaultHttpMessage extends DefaultHttpObject implements HttpMessage {
    private static final int HASH_CODE_PRIME = 31;
    private final HttpHeaders headers;
    private HttpVersion version;

    protected DefaultHttpMessage(HttpVersion version) {
        this(version, DefaultHttpHeadersFactory.headersFactory());
    }

    @Deprecated
    protected DefaultHttpMessage(HttpVersion version, boolean validateHeaders, boolean singleFieldHeaders) {
        this(version, DefaultHttpHeadersFactory.headersFactory().withValidation(validateHeaders).withCombiningHeaders(singleFieldHeaders));
    }

    protected DefaultHttpMessage(HttpVersion version, HttpHeadersFactory headersFactory) {
        this(version, headersFactory.newHeaders());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public DefaultHttpMessage(HttpVersion version, HttpHeaders headers) {
        this.version = (HttpVersion) ObjectUtil.checkNotNull(version, "version");
        this.headers = (HttpHeaders) ObjectUtil.checkNotNull(headers, "headers");
    }

    @Override // io.netty.handler.codec.http.HttpMessage
    public HttpHeaders headers() {
        return this.headers;
    }

    @Override // io.netty.handler.codec.http.HttpMessage
    @Deprecated
    public HttpVersion getProtocolVersion() {
        return protocolVersion();
    }

    @Override // io.netty.handler.codec.http.HttpMessage
    public HttpVersion protocolVersion() {
        return this.version;
    }

    @Override // io.netty.handler.codec.http.DefaultHttpObject
    public int hashCode() {
        int result = (1 * 31) + this.headers.hashCode();
        return (((result * 31) + this.version.hashCode()) * 31) + super.hashCode();
    }

    @Override // io.netty.handler.codec.http.DefaultHttpObject
    public boolean equals(Object o) {
        if (!(o instanceof DefaultHttpMessage)) {
            return false;
        }
        DefaultHttpMessage other = (DefaultHttpMessage) o;
        return headers().equals(other.headers()) && protocolVersion().equals(other.protocolVersion()) && super.equals(o);
    }

    public HttpMessage setProtocolVersion(HttpVersion version) {
        this.version = (HttpVersion) ObjectUtil.checkNotNull(version, "version");
        return this;
    }
}
