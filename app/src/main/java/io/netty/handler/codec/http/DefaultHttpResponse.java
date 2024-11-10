package io.netty.handler.codec.http;

import androidx.core.app.NotificationCompat;
import io.netty.util.internal.ObjectUtil;

/* loaded from: classes4.dex */
public class DefaultHttpResponse extends DefaultHttpMessage implements HttpResponse {
    private HttpResponseStatus status;

    public DefaultHttpResponse(HttpVersion version, HttpResponseStatus status) {
        this(version, status, DefaultHttpHeadersFactory.headersFactory());
    }

    @Deprecated
    public DefaultHttpResponse(HttpVersion version, HttpResponseStatus status, boolean validateHeaders) {
        this(version, status, DefaultHttpHeadersFactory.headersFactory().withValidation(validateHeaders));
    }

    @Deprecated
    public DefaultHttpResponse(HttpVersion version, HttpResponseStatus status, boolean validateHeaders, boolean singleFieldHeaders) {
        this(version, status, DefaultHttpHeadersFactory.headersFactory().withValidation(validateHeaders).withCombiningHeaders(singleFieldHeaders));
    }

    public DefaultHttpResponse(HttpVersion version, HttpResponseStatus status, HttpHeadersFactory headersFactory) {
        this(version, status, headersFactory.newHeaders());
    }

    public DefaultHttpResponse(HttpVersion version, HttpResponseStatus status, HttpHeaders headers) {
        super(version, headers);
        this.status = (HttpResponseStatus) ObjectUtil.checkNotNull(status, NotificationCompat.CATEGORY_STATUS);
    }

    @Override // io.netty.handler.codec.http.HttpResponse
    @Deprecated
    public HttpResponseStatus getStatus() {
        return status();
    }

    @Override // io.netty.handler.codec.http.HttpResponse
    public HttpResponseStatus status() {
        return this.status;
    }

    public HttpResponse setStatus(HttpResponseStatus status) {
        this.status = (HttpResponseStatus) ObjectUtil.checkNotNull(status, NotificationCompat.CATEGORY_STATUS);
        return this;
    }

    @Override // io.netty.handler.codec.http.DefaultHttpMessage, io.netty.handler.codec.http.HttpMessage, io.netty.handler.codec.http.HttpRequest, io.netty.handler.codec.http.FullHttpRequest
    public HttpResponse setProtocolVersion(HttpVersion version) {
        super.setProtocolVersion(version);
        return this;
    }

    public String toString() {
        return HttpMessageUtil.appendResponse(new StringBuilder(256), this).toString();
    }

    @Override // io.netty.handler.codec.http.DefaultHttpMessage, io.netty.handler.codec.http.DefaultHttpObject
    public int hashCode() {
        int result = (1 * 31) + this.status.hashCode();
        return (result * 31) + super.hashCode();
    }

    @Override // io.netty.handler.codec.http.DefaultHttpMessage, io.netty.handler.codec.http.DefaultHttpObject
    public boolean equals(Object o) {
        if (!(o instanceof DefaultHttpResponse)) {
            return false;
        }
        DefaultHttpResponse other = (DefaultHttpResponse) o;
        return this.status.equals(other.status()) && super.equals(o);
    }
}
