package io.netty.handler.codec.http.cookie;

import io.netty.util.internal.ObjectUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/* loaded from: classes4.dex */
public final class ServerCookieDecoder extends CookieDecoder {
    private static final String RFC2965_DOMAIN = "$Domain";
    private static final String RFC2965_PATH = "$Path";
    private static final String RFC2965_PORT = "$Port";
    private static final String RFC2965_VERSION = "$Version";
    public static final ServerCookieDecoder STRICT = new ServerCookieDecoder(true);
    public static final ServerCookieDecoder LAX = new ServerCookieDecoder(false);

    private ServerCookieDecoder(boolean strict) {
        super(strict);
    }

    public List<Cookie> decodeAll(String header) {
        List<Cookie> cookies = new ArrayList<>();
        decode(cookies, header);
        return Collections.unmodifiableList(cookies);
    }

    public Set<Cookie> decode(String header) {
        Set<Cookie> cookies = new TreeSet<>();
        decode(cookies, header);
        return cookies;
    }

    private void decode(Collection<? super Cookie> cookies, String header) {
        int nameEnd;
        int valueEnd;
        int i;
        int i2;
        DefaultCookie cookie;
        int headerLen = ((String) ObjectUtil.checkNotNull(header, "header")).length();
        if (headerLen == 0) {
            return;
        }
        int i3 = 0;
        boolean rfc2965Style = false;
        if (header.regionMatches(true, 0, RFC2965_VERSION, 0, RFC2965_VERSION.length())) {
            i3 = header.indexOf(59) + 1;
            rfc2965Style = true;
        }
        while (i3 != headerLen) {
            char c = header.charAt(i3);
            if (c == '\t' || c == '\n' || c == 11 || c == '\f' || c == '\r' || c == ' ' || c == ',' || c == ';') {
                i3++;
            } else {
                int nameBegin = i3;
                while (true) {
                    char curChar = header.charAt(i3);
                    if (curChar == ';') {
                        int nameEnd2 = i3;
                        nameEnd = nameEnd2;
                        valueEnd = -1;
                        i = i3;
                        i2 = -1;
                        break;
                    }
                    if (curChar == '=') {
                        int nameEnd3 = i3;
                        int i4 = i3 + 1;
                        if (i4 == headerLen) {
                            nameEnd = nameEnd3;
                            valueEnd = 0;
                            i = i4;
                            i2 = 0;
                        } else {
                            int semiPos = header.indexOf(59, i4);
                            int valueEnd2 = semiPos > 0 ? semiPos : headerLen;
                            int i5 = valueEnd2;
                            nameEnd = nameEnd3;
                            valueEnd = valueEnd2;
                            i = i5;
                            i2 = i4;
                        }
                    } else {
                        i3++;
                        if (i3 == headerLen) {
                            nameEnd = headerLen;
                            valueEnd = -1;
                            i = i3;
                            i2 = -1;
                            break;
                        }
                    }
                }
                if ((!rfc2965Style || (!header.regionMatches(nameBegin, RFC2965_PATH, 0, RFC2965_PATH.length()) && !header.regionMatches(nameBegin, RFC2965_DOMAIN, 0, RFC2965_DOMAIN.length()) && !header.regionMatches(nameBegin, RFC2965_PORT, 0, RFC2965_PORT.length()))) && (cookie = initCookie(header, nameBegin, nameEnd, i2, valueEnd)) != null) {
                    cookies.add(cookie);
                }
                i3 = i;
            }
        }
    }
}
