package io.netty.handler.codec.http.cookie;

import io.netty.handler.codec.DateFormatter;
import io.netty.handler.codec.http.cookie.CookieHeaderNames;
import io.netty.util.internal.ObjectUtil;
import java.util.Date;

/* loaded from: classes4.dex */
public final class ClientCookieDecoder extends CookieDecoder {
    public static final ClientCookieDecoder STRICT = new ClientCookieDecoder(true);
    public static final ClientCookieDecoder LAX = new ClientCookieDecoder(false);

    private ClientCookieDecoder(boolean strict) {
        super(strict);
    }

    public Cookie decode(String header) {
        int nameEnd;
        int valueEnd;
        int valueBegin;
        int valueEnd2;
        int headerLen = ((String) ObjectUtil.checkNotNull(header, "header")).length();
        if (headerLen == 0) {
            return null;
        }
        CookieBuilder cookieBuilder = null;
        int i = 0;
        while (i != headerLen) {
            char c = header.charAt(i);
            if (c == ',') {
                break;
            }
            if (c == '\t' || c == '\n' || c == 11 || c == '\f' || c == '\r' || c == ' ' || c == ';') {
                i++;
            } else {
                int nameBegin = i;
                while (true) {
                    char curChar = header.charAt(i);
                    if (curChar == ';') {
                        nameEnd = i;
                        valueEnd = -1;
                        valueBegin = -1;
                        break;
                    }
                    if (curChar == '=') {
                        int nameEnd2 = i;
                        i++;
                        if (i == headerLen) {
                            valueEnd = 0;
                            valueBegin = 0;
                            nameEnd = nameEnd2;
                        } else {
                            int semiPos = header.indexOf(59, i);
                            int valueEnd3 = semiPos > 0 ? semiPos : headerLen;
                            i = valueEnd3;
                            nameEnd = nameEnd2;
                            valueBegin = i;
                            valueEnd = valueEnd3;
                        }
                    } else {
                        i++;
                        if (i == headerLen) {
                            nameEnd = headerLen;
                            valueEnd = -1;
                            valueBegin = -1;
                            break;
                        }
                    }
                }
                if (valueEnd > 0 && header.charAt(valueEnd - 1) == ',') {
                    valueEnd2 = valueEnd - 1;
                } else {
                    valueEnd2 = valueEnd;
                }
                if (cookieBuilder == null) {
                    DefaultCookie cookie = initCookie(header, nameBegin, nameEnd, valueBegin, valueEnd2);
                    if (cookie == null) {
                        return null;
                    }
                    cookieBuilder = new CookieBuilder(cookie, header);
                } else {
                    cookieBuilder.appendAttribute(nameBegin, nameEnd, valueBegin, valueEnd2);
                }
            }
        }
        if (cookieBuilder != null) {
            return cookieBuilder.cookie();
        }
        return null;
    }

    /* loaded from: classes4.dex */
    private static class CookieBuilder {
        private final DefaultCookie cookie;
        private String domain;
        private int expiresEnd;
        private int expiresStart;
        private final String header;
        private boolean httpOnly;
        private long maxAge = Long.MIN_VALUE;
        private boolean partitioned;
        private String path;
        private CookieHeaderNames.SameSite sameSite;
        private boolean secure;

        CookieBuilder(DefaultCookie cookie, String header) {
            this.cookie = cookie;
            this.header = header;
        }

        private long mergeMaxAgeAndExpires() {
            Date expiresDate;
            if (this.maxAge != Long.MIN_VALUE) {
                return this.maxAge;
            }
            if (!isValueDefined(this.expiresStart, this.expiresEnd) || (expiresDate = DateFormatter.parseHttpDate(this.header, this.expiresStart, this.expiresEnd)) == null) {
                return Long.MIN_VALUE;
            }
            long maxAgeMillis = expiresDate.getTime() - System.currentTimeMillis();
            return (maxAgeMillis / 1000) + (maxAgeMillis % 1000 != 0 ? 1 : 0);
        }

        Cookie cookie() {
            this.cookie.setDomain(this.domain);
            this.cookie.setPath(this.path);
            this.cookie.setMaxAge(mergeMaxAgeAndExpires());
            this.cookie.setSecure(this.secure);
            this.cookie.setHttpOnly(this.httpOnly);
            this.cookie.setSameSite(this.sameSite);
            this.cookie.setPartitioned(this.partitioned);
            return this.cookie;
        }

        void appendAttribute(int keyStart, int keyEnd, int valueStart, int valueEnd) {
            int length = keyEnd - keyStart;
            if (length == 4) {
                parse4(keyStart, valueStart, valueEnd);
                return;
            }
            if (length == 6) {
                parse6(keyStart, valueStart, valueEnd);
                return;
            }
            if (length == 7) {
                parse7(keyStart, valueStart, valueEnd);
            } else if (length == 8) {
                parse8(keyStart, valueStart, valueEnd);
            } else if (length == 11) {
                parse11(keyStart);
            }
        }

        private void parse4(int nameStart, int valueStart, int valueEnd) {
            if (this.header.regionMatches(true, nameStart, CookieHeaderNames.PATH, 0, 4)) {
                this.path = computeValue(valueStart, valueEnd);
            }
        }

        private void parse6(int nameStart, int valueStart, int valueEnd) {
            if (this.header.regionMatches(true, nameStart, CookieHeaderNames.DOMAIN, 0, 5)) {
                this.domain = computeValue(valueStart, valueEnd);
            } else if (this.header.regionMatches(true, nameStart, CookieHeaderNames.SECURE, 0, 5)) {
                this.secure = true;
            }
        }

        private void setMaxAge(String value) {
            try {
                this.maxAge = Math.max(Long.parseLong(value), 0L);
            } catch (NumberFormatException e) {
            }
        }

        private void parse7(int nameStart, int valueStart, int valueEnd) {
            if (this.header.regionMatches(true, nameStart, "Expires", 0, 7)) {
                this.expiresStart = valueStart;
                this.expiresEnd = valueEnd;
            } else if (this.header.regionMatches(true, nameStart, CookieHeaderNames.MAX_AGE, 0, 7)) {
                setMaxAge(computeValue(valueStart, valueEnd));
            }
        }

        private void parse8(int nameStart, int valueStart, int valueEnd) {
            if (this.header.regionMatches(true, nameStart, CookieHeaderNames.HTTPONLY, 0, 8)) {
                this.httpOnly = true;
            } else if (this.header.regionMatches(true, nameStart, CookieHeaderNames.SAMESITE, 0, 8)) {
                this.sameSite = CookieHeaderNames.SameSite.of(computeValue(valueStart, valueEnd));
            }
        }

        private void parse11(int nameStart) {
            if (this.header.regionMatches(true, nameStart, CookieHeaderNames.PARTITIONED, 0, 11)) {
                this.partitioned = true;
            }
        }

        private static boolean isValueDefined(int valueStart, int valueEnd) {
            return (valueStart == -1 || valueStart == valueEnd) ? false : true;
        }

        private String computeValue(int valueStart, int valueEnd) {
            if (isValueDefined(valueStart, valueEnd)) {
                return this.header.substring(valueStart, valueEnd);
            }
            return null;
        }
    }
}
