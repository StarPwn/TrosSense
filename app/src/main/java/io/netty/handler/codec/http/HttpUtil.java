package io.netty.handler.codec.http;

import io.netty.util.AsciiString;
import io.netty.util.CharsetUtil;
import io.netty.util.NetUtil;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.StringUtil;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes4.dex */
public final class HttpUtil {
    private static final AsciiString CHARSET_EQUALS = AsciiString.of(((Object) HttpHeaderValues.CHARSET) + "=");
    private static final AsciiString SEMICOLON = AsciiString.cached(";");
    private static final String COMMA_STRING = String.valueOf(StringUtil.COMMA);

    private HttpUtil() {
    }

    public static boolean isOriginForm(URI uri) {
        return isOriginForm(uri.toString());
    }

    public static boolean isOriginForm(String uri) {
        return uri.startsWith("/");
    }

    public static boolean isAsteriskForm(URI uri) {
        return isAsteriskForm(uri.toString());
    }

    public static boolean isAsteriskForm(String uri) {
        return "*".equals(uri);
    }

    public static boolean isKeepAlive(HttpMessage message) {
        return !message.headers().containsValue(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE, true) && (message.protocolVersion().isKeepAliveDefault() || message.headers().containsValue(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE, true));
    }

    public static void setKeepAlive(HttpMessage message, boolean keepAlive) {
        setKeepAlive(message.headers(), message.protocolVersion(), keepAlive);
    }

    public static void setKeepAlive(HttpHeaders h, HttpVersion httpVersion, boolean keepAlive) {
        if (httpVersion.isKeepAliveDefault()) {
            if (keepAlive) {
                h.remove(HttpHeaderNames.CONNECTION);
                return;
            } else {
                h.set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
                return;
            }
        }
        if (keepAlive) {
            h.set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        } else {
            h.remove(HttpHeaderNames.CONNECTION);
        }
    }

    public static long getContentLength(HttpMessage message) {
        String value = message.headers().get(HttpHeaderNames.CONTENT_LENGTH);
        if (value != null) {
            return Long.parseLong(value);
        }
        long webSocketContentLength = getWebSocketContentLength(message);
        if (webSocketContentLength >= 0) {
            return webSocketContentLength;
        }
        throw new NumberFormatException("header not found: " + ((Object) HttpHeaderNames.CONTENT_LENGTH));
    }

    public static long getContentLength(HttpMessage message, long defaultValue) {
        String value = message.headers().get(HttpHeaderNames.CONTENT_LENGTH);
        if (value != null) {
            return Long.parseLong(value);
        }
        long webSocketContentLength = getWebSocketContentLength(message);
        if (webSocketContentLength >= 0) {
            return webSocketContentLength;
        }
        return defaultValue;
    }

    public static int getContentLength(HttpMessage message, int defaultValue) {
        return (int) Math.min(2147483647L, getContentLength(message, defaultValue));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int getWebSocketContentLength(HttpMessage message) {
        HttpHeaders h = message.headers();
        if (message instanceof HttpRequest) {
            HttpRequest req = (HttpRequest) message;
            if (HttpMethod.GET.equals(req.method()) && h.contains(HttpHeaderNames.SEC_WEBSOCKET_KEY1) && h.contains(HttpHeaderNames.SEC_WEBSOCKET_KEY2)) {
                return 8;
            }
            return -1;
        }
        if (message instanceof HttpResponse) {
            HttpResponse res = (HttpResponse) message;
            if (res.status().code() == 101 && h.contains(HttpHeaderNames.SEC_WEBSOCKET_ORIGIN) && h.contains(HttpHeaderNames.SEC_WEBSOCKET_LOCATION)) {
                return 16;
            }
            return -1;
        }
        return -1;
    }

    public static void setContentLength(HttpMessage message, long length) {
        message.headers().set(HttpHeaderNames.CONTENT_LENGTH, Long.valueOf(length));
    }

    public static boolean isContentLengthSet(HttpMessage m) {
        return m.headers().contains(HttpHeaderNames.CONTENT_LENGTH);
    }

    public static boolean is100ContinueExpected(HttpMessage message) {
        return isExpectHeaderValid(message) && message.headers().contains((CharSequence) HttpHeaderNames.EXPECT, (CharSequence) HttpHeaderValues.CONTINUE, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isUnsupportedExpectation(HttpMessage message) {
        String expectValue;
        return (!isExpectHeaderValid(message) || (expectValue = message.headers().get(HttpHeaderNames.EXPECT)) == null || HttpHeaderValues.CONTINUE.toString().equalsIgnoreCase(expectValue)) ? false : true;
    }

    private static boolean isExpectHeaderValid(HttpMessage message) {
        return (message instanceof HttpRequest) && message.protocolVersion().compareTo(HttpVersion.HTTP_1_1) >= 0;
    }

    public static void set100ContinueExpected(HttpMessage message, boolean expected) {
        if (expected) {
            message.headers().set(HttpHeaderNames.EXPECT, HttpHeaderValues.CONTINUE);
        } else {
            message.headers().remove(HttpHeaderNames.EXPECT);
        }
    }

    public static boolean isTransferEncodingChunked(HttpMessage message) {
        return message.headers().containsValue(HttpHeaderNames.TRANSFER_ENCODING, HttpHeaderValues.CHUNKED, true);
    }

    public static void setTransferEncodingChunked(HttpMessage m, boolean chunked) {
        if (chunked) {
            m.headers().set(HttpHeaderNames.TRANSFER_ENCODING, HttpHeaderValues.CHUNKED);
            m.headers().remove(HttpHeaderNames.CONTENT_LENGTH);
            return;
        }
        List<String> encodings = m.headers().getAll(HttpHeaderNames.TRANSFER_ENCODING);
        if (encodings.isEmpty()) {
            return;
        }
        List<CharSequence> values = new ArrayList<>(encodings);
        Iterator<CharSequence> valuesIt = values.iterator();
        while (valuesIt.hasNext()) {
            CharSequence value = valuesIt.next();
            if (HttpHeaderValues.CHUNKED.contentEqualsIgnoreCase(value)) {
                valuesIt.remove();
            }
        }
        if (values.isEmpty()) {
            m.headers().remove(HttpHeaderNames.TRANSFER_ENCODING);
        } else {
            m.headers().set((CharSequence) HttpHeaderNames.TRANSFER_ENCODING, (Iterable<?>) values);
        }
    }

    public static Charset getCharset(HttpMessage message) {
        return getCharset(message, CharsetUtil.ISO_8859_1);
    }

    public static Charset getCharset(CharSequence contentTypeValue) {
        if (contentTypeValue != null) {
            return getCharset(contentTypeValue, CharsetUtil.ISO_8859_1);
        }
        return CharsetUtil.ISO_8859_1;
    }

    public static Charset getCharset(HttpMessage message, Charset defaultCharset) {
        CharSequence contentTypeValue = message.headers().get(HttpHeaderNames.CONTENT_TYPE);
        if (contentTypeValue != null) {
            return getCharset(contentTypeValue, defaultCharset);
        }
        return defaultCharset;
    }

    public static Charset getCharset(CharSequence contentTypeValue, Charset defaultCharset) {
        CharSequence charsetRaw;
        if (contentTypeValue != null && (charsetRaw = getCharsetAsSequence(contentTypeValue)) != null) {
            if (charsetRaw.length() > 2 && charsetRaw.charAt(0) == '\"' && charsetRaw.charAt(charsetRaw.length() - 1) == '\"') {
                charsetRaw = charsetRaw.subSequence(1, charsetRaw.length() - 1);
            }
            try {
                return Charset.forName(charsetRaw.toString());
            } catch (IllegalCharsetNameException e) {
            } catch (UnsupportedCharsetException e2) {
            }
        }
        return defaultCharset;
    }

    @Deprecated
    public static CharSequence getCharsetAsString(HttpMessage message) {
        return getCharsetAsSequence(message);
    }

    public static CharSequence getCharsetAsSequence(HttpMessage message) {
        CharSequence contentTypeValue = message.headers().get(HttpHeaderNames.CONTENT_TYPE);
        if (contentTypeValue != null) {
            return getCharsetAsSequence(contentTypeValue);
        }
        return null;
    }

    public static CharSequence getCharsetAsSequence(CharSequence contentTypeValue) {
        int indexOfEncoding;
        ObjectUtil.checkNotNull(contentTypeValue, "contentTypeValue");
        int indexOfCharset = AsciiString.indexOfIgnoreCaseAscii(contentTypeValue, CHARSET_EQUALS, 0);
        if (indexOfCharset == -1 || (indexOfEncoding = CHARSET_EQUALS.length() + indexOfCharset) >= contentTypeValue.length()) {
            return null;
        }
        CharSequence charsetCandidate = contentTypeValue.subSequence(indexOfEncoding, contentTypeValue.length());
        int indexOfSemicolon = AsciiString.indexOfIgnoreCaseAscii(charsetCandidate, SEMICOLON, 0);
        if (indexOfSemicolon == -1) {
            return charsetCandidate;
        }
        return charsetCandidate.subSequence(0, indexOfSemicolon);
    }

    public static CharSequence getMimeType(HttpMessage message) {
        CharSequence contentTypeValue = message.headers().get(HttpHeaderNames.CONTENT_TYPE);
        if (contentTypeValue != null) {
            return getMimeType(contentTypeValue);
        }
        return null;
    }

    public static CharSequence getMimeType(CharSequence contentTypeValue) {
        ObjectUtil.checkNotNull(contentTypeValue, "contentTypeValue");
        int indexOfSemicolon = AsciiString.indexOfIgnoreCaseAscii(contentTypeValue, SEMICOLON, 0);
        if (indexOfSemicolon != -1) {
            return contentTypeValue.subSequence(0, indexOfSemicolon);
        }
        if (contentTypeValue.length() > 0) {
            return contentTypeValue;
        }
        return null;
    }

    public static String formatHostnameForHttp(InetSocketAddress addr) {
        String hostString = NetUtil.getHostname(addr);
        if (NetUtil.isValidIpV6Address(hostString)) {
            if (!addr.isUnresolved()) {
                hostString = NetUtil.toAddressString(addr.getAddress());
            }
            return '[' + hostString + ']';
        }
        return hostString;
    }

    public static long normalizeAndGetContentLength(List<? extends CharSequence> contentLengthFields, boolean isHttp10OrEarlier, boolean allowDuplicateContentLengths) {
        if (contentLengthFields.isEmpty()) {
            return -1L;
        }
        String firstField = contentLengthFields.get(0).toString();
        boolean z = true;
        if (contentLengthFields.size() <= 1 && firstField.indexOf(44) < 0) {
            z = false;
        }
        boolean multipleContentLengths = z;
        if (multipleContentLengths && !isHttp10OrEarlier) {
            if (allowDuplicateContentLengths) {
                String firstValue = null;
                for (CharSequence field : contentLengthFields) {
                    String[] tokens = field.toString().split(COMMA_STRING, -1);
                    for (String token : tokens) {
                        String trimmed = token.trim();
                        if (firstValue == null) {
                            firstValue = trimmed;
                        } else if (!trimmed.equals(firstValue)) {
                            throw new IllegalArgumentException("Multiple Content-Length values found: " + contentLengthFields);
                        }
                    }
                }
                firstField = firstValue;
            } else {
                throw new IllegalArgumentException("Multiple Content-Length values found: " + contentLengthFields);
            }
        }
        if (firstField.isEmpty() || !Character.isDigit(firstField.charAt(0))) {
            throw new IllegalArgumentException("Content-Length value is not a number: " + firstField);
        }
        try {
            long value = Long.parseLong(firstField);
            return ObjectUtil.checkPositiveOrZero(value, "Content-Length value");
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Content-Length value is not a number: " + firstField, e);
        }
    }
}
