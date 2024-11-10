package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.handler.codec.UnsupportedValueConverter;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpScheme;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http2.Http2Headers;
import io.netty.util.AsciiString;
import io.netty.util.ByteProcessor;
import io.netty.util.internal.InternalThreadLocalMap;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.StringUtil;
import java.net.URI;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: classes4.dex */
public final class HttpConversionUtil {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final AsciiString EMPTY_REQUEST_PATH;
    private static final CharSequenceMap<AsciiString> HTTP_TO_HTTP2_HEADER_BLACKLIST = new CharSequenceMap<>();
    public static final HttpMethod OUT_OF_MESSAGE_SEQUENCE_METHOD;
    public static final String OUT_OF_MESSAGE_SEQUENCE_PATH = "";
    public static final HttpResponseStatus OUT_OF_MESSAGE_SEQUENCE_RETURN_CODE;

    static {
        HTTP_TO_HTTP2_HEADER_BLACKLIST.add((CharSequenceMap<AsciiString>) HttpHeaderNames.CONNECTION, AsciiString.EMPTY_STRING);
        AsciiString keepAlive = HttpHeaderNames.KEEP_ALIVE;
        HTTP_TO_HTTP2_HEADER_BLACKLIST.add((CharSequenceMap<AsciiString>) keepAlive, AsciiString.EMPTY_STRING);
        AsciiString proxyConnection = HttpHeaderNames.PROXY_CONNECTION;
        HTTP_TO_HTTP2_HEADER_BLACKLIST.add((CharSequenceMap<AsciiString>) proxyConnection, AsciiString.EMPTY_STRING);
        HTTP_TO_HTTP2_HEADER_BLACKLIST.add((CharSequenceMap<AsciiString>) HttpHeaderNames.TRANSFER_ENCODING, AsciiString.EMPTY_STRING);
        HTTP_TO_HTTP2_HEADER_BLACKLIST.add((CharSequenceMap<AsciiString>) HttpHeaderNames.HOST, AsciiString.EMPTY_STRING);
        HTTP_TO_HTTP2_HEADER_BLACKLIST.add((CharSequenceMap<AsciiString>) HttpHeaderNames.UPGRADE, AsciiString.EMPTY_STRING);
        HTTP_TO_HTTP2_HEADER_BLACKLIST.add((CharSequenceMap<AsciiString>) ExtensionHeaderNames.STREAM_ID.text(), AsciiString.EMPTY_STRING);
        HTTP_TO_HTTP2_HEADER_BLACKLIST.add((CharSequenceMap<AsciiString>) ExtensionHeaderNames.SCHEME.text(), AsciiString.EMPTY_STRING);
        HTTP_TO_HTTP2_HEADER_BLACKLIST.add((CharSequenceMap<AsciiString>) ExtensionHeaderNames.PATH.text(), AsciiString.EMPTY_STRING);
        OUT_OF_MESSAGE_SEQUENCE_METHOD = HttpMethod.OPTIONS;
        OUT_OF_MESSAGE_SEQUENCE_RETURN_CODE = HttpResponseStatus.OK;
        EMPTY_REQUEST_PATH = AsciiString.cached("/");
    }

    private HttpConversionUtil() {
    }

    /* loaded from: classes4.dex */
    public enum ExtensionHeaderNames {
        STREAM_ID("x-http2-stream-id"),
        SCHEME("x-http2-scheme"),
        PATH("x-http2-path"),
        STREAM_PROMISE_ID("x-http2-stream-promise-id"),
        STREAM_DEPENDENCY_ID("x-http2-stream-dependency-id"),
        STREAM_WEIGHT("x-http2-stream-weight");

        private final AsciiString text;

        ExtensionHeaderNames(String text) {
            this.text = AsciiString.cached(text);
        }

        public AsciiString text() {
            return this.text;
        }
    }

    public static HttpResponseStatus parseStatus(CharSequence status) throws Http2Exception {
        try {
            HttpResponseStatus result = HttpResponseStatus.parseLine(status);
            if (result != HttpResponseStatus.SWITCHING_PROTOCOLS) {
                return result;
            }
            throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Invalid HTTP/2 status code '%d'", Integer.valueOf(result.code()));
        } catch (Http2Exception e) {
            throw e;
        } catch (Throwable t) {
            throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, t, "Unrecognized HTTP status code '%s' encountered in translation to HTTP/1.x", status);
        }
    }

    public static FullHttpResponse toFullHttpResponse(int streamId, Http2Headers http2Headers, ByteBufAllocator alloc, boolean validateHttpHeaders) throws Http2Exception {
        return toFullHttpResponse(streamId, http2Headers, alloc.buffer(), validateHttpHeaders);
    }

    public static FullHttpResponse toFullHttpResponse(int streamId, Http2Headers http2Headers, ByteBuf content, boolean validateHttpHeaders) throws Http2Exception {
        HttpResponseStatus status = parseStatus(http2Headers.status());
        FullHttpResponse msg = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, content, validateHttpHeaders);
        try {
            addHttp2ToHttpHeaders(streamId, http2Headers, msg, false);
            return msg;
        } catch (Http2Exception e) {
            msg.release();
            throw e;
        } catch (Throwable t) {
            msg.release();
            throw Http2Exception.streamError(streamId, Http2Error.PROTOCOL_ERROR, t, "HTTP/2 to HTTP/1.x headers conversion error", new Object[0]);
        }
    }

    public static FullHttpRequest toFullHttpRequest(int streamId, Http2Headers http2Headers, ByteBufAllocator alloc, boolean validateHttpHeaders) throws Http2Exception {
        return toFullHttpRequest(streamId, http2Headers, alloc.buffer(), validateHttpHeaders);
    }

    private static String extractPath(CharSequence method, Http2Headers headers) {
        if (HttpMethod.CONNECT.asciiName().contentEqualsIgnoreCase(method)) {
            return ((CharSequence) ObjectUtil.checkNotNull(headers.authority(), "authority header cannot be null in the conversion to HTTP/1.x")).toString();
        }
        return ((CharSequence) ObjectUtil.checkNotNull(headers.path(), "path header cannot be null in conversion to HTTP/1.x")).toString();
    }

    public static FullHttpRequest toFullHttpRequest(int streamId, Http2Headers http2Headers, ByteBuf content, boolean validateHttpHeaders) throws Http2Exception {
        CharSequence method = (CharSequence) ObjectUtil.checkNotNull(http2Headers.method(), "method header cannot be null in conversion to HTTP/1.x");
        CharSequence path = extractPath(method, http2Headers);
        FullHttpRequest msg = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.valueOf(method.toString()), path.toString(), content, validateHttpHeaders);
        try {
            addHttp2ToHttpHeaders(streamId, http2Headers, msg, false);
            return msg;
        } catch (Http2Exception e) {
            msg.release();
            throw e;
        } catch (Throwable t) {
            msg.release();
            throw Http2Exception.streamError(streamId, Http2Error.PROTOCOL_ERROR, t, "HTTP/2 to HTTP/1.x headers conversion error", new Object[0]);
        }
    }

    public static HttpRequest toHttpRequest(int streamId, Http2Headers http2Headers, boolean validateHttpHeaders) throws Http2Exception {
        CharSequence method = (CharSequence) ObjectUtil.checkNotNull(http2Headers.method(), "method header cannot be null in conversion to HTTP/1.x");
        CharSequence path = extractPath(method, http2Headers);
        HttpRequest msg = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.valueOf(method.toString()), path.toString(), validateHttpHeaders);
        try {
            addHttp2ToHttpHeaders(streamId, http2Headers, msg.headers(), msg.protocolVersion(), false, true);
            return msg;
        } catch (Http2Exception e) {
            throw e;
        } catch (Throwable t) {
            throw Http2Exception.streamError(streamId, Http2Error.PROTOCOL_ERROR, t, "HTTP/2 to HTTP/1.x headers conversion error", new Object[0]);
        }
    }

    public static HttpResponse toHttpResponse(int streamId, Http2Headers http2Headers, boolean validateHttpHeaders) throws Http2Exception {
        HttpResponseStatus status = parseStatus(http2Headers.status());
        HttpResponse msg = new DefaultHttpResponse(HttpVersion.HTTP_1_1, status, validateHttpHeaders);
        try {
            addHttp2ToHttpHeaders(streamId, http2Headers, msg.headers(), msg.protocolVersion(), false, false);
            return msg;
        } catch (Http2Exception e) {
            throw e;
        } catch (Throwable t) {
            throw Http2Exception.streamError(streamId, Http2Error.PROTOCOL_ERROR, t, "HTTP/2 to HTTP/1.x headers conversion error", new Object[0]);
        }
    }

    public static void addHttp2ToHttpHeaders(int streamId, Http2Headers inputHeaders, FullHttpMessage destinationMessage, boolean addToTrailer) throws Http2Exception {
        addHttp2ToHttpHeaders(streamId, inputHeaders, addToTrailer ? destinationMessage.trailingHeaders() : destinationMessage.headers(), destinationMessage.protocolVersion(), addToTrailer, destinationMessage instanceof HttpRequest);
    }

    public static void addHttp2ToHttpHeaders(int streamId, Http2Headers inputHeaders, HttpHeaders outputHeaders, HttpVersion httpVersion, boolean isTrailer, boolean isRequest) throws Http2Exception {
        Http2ToHttpHeaderTranslator translator = new Http2ToHttpHeaderTranslator(streamId, outputHeaders, isRequest);
        try {
            translator.translateHeaders(inputHeaders);
            outputHeaders.remove(HttpHeaderNames.TRANSFER_ENCODING);
            outputHeaders.remove(HttpHeaderNames.TRAILER);
            if (!isTrailer) {
                outputHeaders.setInt(ExtensionHeaderNames.STREAM_ID.text(), streamId);
                HttpUtil.setKeepAlive(outputHeaders, httpVersion, true);
            }
        } catch (Http2Exception ex) {
            throw ex;
        } catch (Throwable t) {
            throw Http2Exception.streamError(streamId, Http2Error.PROTOCOL_ERROR, t, "HTTP/2 to HTTP/1.x headers conversion error", new Object[0]);
        }
    }

    public static Http2Headers toHttp2Headers(HttpMessage in, boolean validateHeaders) {
        HttpHeaders inHeaders = in.headers();
        Http2Headers out = new DefaultHttp2Headers(validateHeaders, inHeaders.size());
        if (in instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) in;
            String host = inHeaders.getAsString(HttpHeaderNames.HOST);
            if (HttpUtil.isOriginForm(request.uri()) || HttpUtil.isAsteriskForm(request.uri())) {
                out.path(new AsciiString(request.uri()));
                setHttp2Scheme(inHeaders, out);
            } else {
                URI requestTargetUri = URI.create(request.uri());
                out.path(toHttp2Path(requestTargetUri));
                host = StringUtil.isNullOrEmpty(host) ? requestTargetUri.getAuthority() : host;
                setHttp2Scheme(inHeaders, requestTargetUri, out);
            }
            setHttp2Authority(host, out);
            out.method(request.method().asciiName());
        } else if (in instanceof HttpResponse) {
            HttpResponse response = (HttpResponse) in;
            out.status(response.status().codeAsText());
        }
        toHttp2Headers(inHeaders, out);
        return out;
    }

    public static Http2Headers toHttp2Headers(HttpHeaders inHeaders, boolean validateHeaders) {
        if (inHeaders.isEmpty()) {
            return EmptyHttp2Headers.INSTANCE;
        }
        Http2Headers out = new DefaultHttp2Headers(validateHeaders, inHeaders.size());
        toHttp2Headers(inHeaders, out);
        return out;
    }

    private static CharSequenceMap<AsciiString> toLowercaseMap(Iterator<? extends CharSequence> valuesIter, int arraySizeHint) {
        int forEachByte;
        UnsupportedValueConverter<AsciiString> valueConverter = UnsupportedValueConverter.instance();
        CharSequenceMap<AsciiString> result = new CharSequenceMap<>(true, valueConverter, arraySizeHint);
        while (valuesIter.hasNext()) {
            AsciiString lowerCased = AsciiString.of(valuesIter.next()).toLowerCase();
            try {
                int index = lowerCased.forEachByte(ByteProcessor.FIND_COMMA);
                if (index != -1) {
                    int start = 0;
                    do {
                        result.add((CharSequenceMap<AsciiString>) lowerCased.subSequence(start, index, false).trim(), AsciiString.EMPTY_STRING);
                        start = index + 1;
                        if (start >= lowerCased.length()) {
                            break;
                        }
                        forEachByte = lowerCased.forEachByte(start, lowerCased.length() - start, ByteProcessor.FIND_COMMA);
                        index = forEachByte;
                    } while (forEachByte != -1);
                    result.add((CharSequenceMap<AsciiString>) lowerCased.subSequence(start, lowerCased.length(), false).trim(), AsciiString.EMPTY_STRING);
                } else {
                    result.add((CharSequenceMap<AsciiString>) lowerCased.trim(), AsciiString.EMPTY_STRING);
                }
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
        return result;
    }

    private static void toHttp2HeadersFilterTE(Map.Entry<CharSequence, CharSequence> entry, Http2Headers out) {
        if (AsciiString.indexOf(entry.getValue(), StringUtil.COMMA, 0) == -1) {
            if (AsciiString.contentEqualsIgnoreCase(AsciiString.trim(entry.getValue()), HttpHeaderValues.TRAILERS)) {
                out.add((Http2Headers) HttpHeaderNames.TE, HttpHeaderValues.TRAILERS);
                return;
            }
            return;
        }
        List<CharSequence> teValues = StringUtil.unescapeCsvFields(entry.getValue());
        for (CharSequence teValue : teValues) {
            if (AsciiString.contentEqualsIgnoreCase(AsciiString.trim(teValue), HttpHeaderValues.TRAILERS)) {
                out.add((Http2Headers) HttpHeaderNames.TE, HttpHeaderValues.TRAILERS);
                return;
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:33:0x0078, code lost:            r5 = true;     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static void toHttp2Headers(io.netty.handler.codec.http.HttpHeaders r10, io.netty.handler.codec.http2.Http2Headers r11) {
        /*
            java.util.Iterator r0 = r10.iteratorCharSequence()
            io.netty.util.AsciiString r1 = io.netty.handler.codec.http.HttpHeaderNames.CONNECTION
            java.util.Iterator r1 = r10.valueCharSequenceIterator(r1)
            r2 = 8
            io.netty.handler.codec.http2.CharSequenceMap r1 = toLowercaseMap(r1, r2)
        L10:
            boolean r2 = r0.hasNext()
            if (r2 == 0) goto L98
            java.lang.Object r2 = r0.next()
            java.util.Map$Entry r2 = (java.util.Map.Entry) r2
            java.lang.Object r3 = r2.getKey()
            java.lang.CharSequence r3 = (java.lang.CharSequence) r3
            io.netty.util.AsciiString r3 = io.netty.util.AsciiString.of(r3)
            io.netty.util.AsciiString r3 = r3.toLowerCase()
            io.netty.handler.codec.http2.CharSequenceMap<io.netty.util.AsciiString> r4 = io.netty.handler.codec.http2.HttpConversionUtil.HTTP_TO_HTTP2_HEADER_BLACKLIST
            boolean r4 = r4.contains(r3)
            if (r4 != 0) goto L96
            boolean r4 = r1.contains(r3)
            if (r4 != 0) goto L96
            io.netty.util.AsciiString r4 = io.netty.handler.codec.http.HttpHeaderNames.TE
            boolean r4 = r3.contentEqualsIgnoreCase(r4)
            if (r4 == 0) goto L44
            toHttp2HeadersFilterTE(r2, r11)
            goto L96
        L44:
            io.netty.util.AsciiString r4 = io.netty.handler.codec.http.HttpHeaderNames.COOKIE
            boolean r4 = r3.contentEqualsIgnoreCase(r4)
            if (r4 == 0) goto L8f
            java.lang.Object r4 = r2.getValue()
            java.lang.CharSequence r4 = (java.lang.CharSequence) r4
            r5 = 0
            r6 = 0
        L54:
            int r7 = r4.length()
            if (r6 >= r7) goto L83
            char r7 = r4.charAt(r6)
            r8 = 59
            if (r7 != r8) goto L7a
            int r8 = r6 + 1
            int r9 = r4.length()
            if (r8 >= r9) goto L78
            int r8 = r6 + 1
            char r8 = r4.charAt(r8)
            r9 = 32
            if (r8 == r9) goto L75
            goto L78
        L75:
            int r6 = r6 + 1
            goto L80
        L78:
            r5 = 1
            goto L83
        L7a:
            r8 = 255(0xff, float:3.57E-43)
            if (r7 <= r8) goto L80
            r5 = 1
            goto L83
        L80:
            int r6 = r6 + 1
            goto L54
        L83:
            if (r5 == 0) goto L8b
            io.netty.util.AsciiString r6 = io.netty.handler.codec.http.HttpHeaderNames.COOKIE
            r11.add(r6, r4)
            goto L8e
        L8b:
            splitValidCookieHeader(r11, r4)
        L8e:
            goto L96
        L8f:
            java.lang.Object r4 = r2.getValue()
            r11.add(r3, r4)
        L96:
            goto L10
        L98:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.handler.codec.http2.HttpConversionUtil.toHttp2Headers(io.netty.handler.codec.http.HttpHeaders, io.netty.handler.codec.http2.Http2Headers):void");
    }

    private static void splitValidCookieHeader(Http2Headers out, CharSequence valueCs) {
        int forEachByte;
        try {
            AsciiString value = AsciiString.of(valueCs);
            int index = value.forEachByte(ByteProcessor.FIND_SEMI_COLON);
            if (index != -1) {
                int start = 0;
                do {
                    out.add((Http2Headers) HttpHeaderNames.COOKIE, value.subSequence(start, index, false));
                    if (index + 1 >= value.length()) {
                        throw new AssertionError();
                    }
                    if (value.charAt(index + 1) != ' ') {
                        throw new AssertionError();
                    }
                    start = index + 2;
                    if (start >= value.length()) {
                        break;
                    }
                    forEachByte = value.forEachByte(start, value.length() - start, ByteProcessor.FIND_SEMI_COLON);
                    index = forEachByte;
                } while (forEachByte != -1);
                if (start >= value.length()) {
                    throw new AssertionError();
                }
                out.add((Http2Headers) HttpHeaderNames.COOKIE, value.subSequence(start, value.length(), false));
                return;
            }
            out.add((Http2Headers) HttpHeaderNames.COOKIE, value);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private static AsciiString toHttp2Path(URI uri) {
        StringBuilder pathBuilder = new StringBuilder(StringUtil.length(uri.getRawPath()) + StringUtil.length(uri.getRawQuery()) + StringUtil.length(uri.getRawFragment()) + 2);
        if (!StringUtil.isNullOrEmpty(uri.getRawPath())) {
            pathBuilder.append(uri.getRawPath());
        }
        if (!StringUtil.isNullOrEmpty(uri.getRawQuery())) {
            pathBuilder.append('?');
            pathBuilder.append(uri.getRawQuery());
        }
        if (!StringUtil.isNullOrEmpty(uri.getRawFragment())) {
            pathBuilder.append('#');
            pathBuilder.append(uri.getRawFragment());
        }
        String path = pathBuilder.toString();
        return path.isEmpty() ? EMPTY_REQUEST_PATH : new AsciiString(path);
    }

    static void setHttp2Authority(String authority, Http2Headers out) {
        if (authority != null) {
            if (authority.isEmpty()) {
                out.authority(AsciiString.EMPTY_STRING);
                return;
            }
            int start = authority.indexOf(64) + 1;
            int length = authority.length() - start;
            if (length == 0) {
                throw new IllegalArgumentException("authority: " + authority);
            }
            out.authority(new AsciiString(authority, start, length));
        }
    }

    private static void setHttp2Scheme(HttpHeaders in, Http2Headers out) {
        setHttp2Scheme(in, URI.create(""), out);
    }

    private static void setHttp2Scheme(HttpHeaders in, URI uri, Http2Headers out) {
        String value = uri.getScheme();
        if (!StringUtil.isNullOrEmpty(value)) {
            out.scheme(new AsciiString(value));
            return;
        }
        CharSequence cValue = in.get(ExtensionHeaderNames.SCHEME.text());
        if (cValue != null) {
            out.scheme(AsciiString.of(cValue));
        } else if (uri.getPort() == HttpScheme.HTTPS.port()) {
            out.scheme(HttpScheme.HTTPS.name());
        } else {
            if (uri.getPort() == HttpScheme.HTTP.port()) {
                out.scheme(HttpScheme.HTTP.name());
                return;
            }
            throw new IllegalArgumentException(":scheme must be specified. see https://tools.ietf.org/html/rfc7540#section-8.1.2.3");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class Http2ToHttpHeaderTranslator {
        private static final CharSequenceMap<AsciiString> REQUEST_HEADER_TRANSLATIONS = new CharSequenceMap<>();
        private static final CharSequenceMap<AsciiString> RESPONSE_HEADER_TRANSLATIONS = new CharSequenceMap<>();
        private final HttpHeaders output;
        private final int streamId;
        private final CharSequenceMap<AsciiString> translations;

        static {
            RESPONSE_HEADER_TRANSLATIONS.add((CharSequenceMap<AsciiString>) Http2Headers.PseudoHeaderName.AUTHORITY.value(), HttpHeaderNames.HOST);
            RESPONSE_HEADER_TRANSLATIONS.add((CharSequenceMap<AsciiString>) Http2Headers.PseudoHeaderName.SCHEME.value(), ExtensionHeaderNames.SCHEME.text());
            REQUEST_HEADER_TRANSLATIONS.add(RESPONSE_HEADER_TRANSLATIONS);
            RESPONSE_HEADER_TRANSLATIONS.add((CharSequenceMap<AsciiString>) Http2Headers.PseudoHeaderName.PATH.value(), ExtensionHeaderNames.PATH.text());
        }

        Http2ToHttpHeaderTranslator(int streamId, HttpHeaders output, boolean request) {
            this.streamId = streamId;
            this.output = output;
            this.translations = request ? REQUEST_HEADER_TRANSLATIONS : RESPONSE_HEADER_TRANSLATIONS;
        }

        void translateHeaders(Iterable<Map.Entry<CharSequence, CharSequence>> inputHeaders) throws Http2Exception {
            StringBuilder cookies = null;
            for (Map.Entry<CharSequence, CharSequence> entry : inputHeaders) {
                CharSequence name = entry.getKey();
                CharSequence value = entry.getValue();
                AsciiString translatedName = this.translations.get(name);
                if (translatedName != null) {
                    this.output.add(translatedName, AsciiString.of(value));
                } else if (Http2Headers.PseudoHeaderName.isPseudoHeader(name)) {
                    continue;
                } else {
                    if (name.length() == 0 || name.charAt(0) == ':') {
                        throw Http2Exception.streamError(this.streamId, Http2Error.PROTOCOL_ERROR, "Invalid HTTP/2 header '%s' encountered in translation to HTTP/1.x", name);
                    }
                    if (HttpHeaderNames.COOKIE.equals(name)) {
                        if (cookies == null) {
                            cookies = InternalThreadLocalMap.get().stringBuilder();
                        } else if (cookies.length() > 0) {
                            cookies.append("; ");
                        }
                        cookies.append(value);
                    } else {
                        this.output.add(name, value);
                    }
                }
            }
            if (cookies != null) {
                this.output.add(HttpHeaderNames.COOKIE, cookies.toString());
            }
        }
    }
}
