package io.netty.handler.codec.http.cookie;

import io.netty.handler.codec.DateFormatter;
import io.netty.util.internal.ObjectUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: classes4.dex */
public final class ServerCookieEncoder extends CookieEncoder {
    public static final ServerCookieEncoder STRICT = new ServerCookieEncoder(true);
    public static final ServerCookieEncoder LAX = new ServerCookieEncoder(false);

    private ServerCookieEncoder(boolean strict) {
        super(strict);
    }

    public String encode(String name, String value) {
        return encode(new DefaultCookie(name, value));
    }

    public String encode(Cookie cookie) {
        String name = ((Cookie) ObjectUtil.checkNotNull(cookie, "cookie")).name();
        String value = cookie.value() != null ? cookie.value() : "";
        validateCookie(name, value);
        StringBuilder buf = CookieUtil.stringBuilder();
        if (cookie.wrap()) {
            CookieUtil.addQuoted(buf, name, value);
        } else {
            CookieUtil.add(buf, name, value);
        }
        if (cookie.maxAge() != Long.MIN_VALUE) {
            CookieUtil.add(buf, CookieHeaderNames.MAX_AGE, cookie.maxAge());
            Date expires = new Date((cookie.maxAge() * 1000) + System.currentTimeMillis());
            buf.append("Expires");
            buf.append('=');
            DateFormatter.append(expires, buf);
            buf.append(';');
            buf.append(' ');
        }
        if (cookie.path() != null) {
            CookieUtil.add(buf, CookieHeaderNames.PATH, cookie.path());
        }
        if (cookie.domain() != null) {
            CookieUtil.add(buf, CookieHeaderNames.DOMAIN, cookie.domain());
        }
        if (cookie.isSecure()) {
            CookieUtil.add(buf, CookieHeaderNames.SECURE);
        }
        if (cookie.isHttpOnly()) {
            CookieUtil.add(buf, CookieHeaderNames.HTTPONLY);
        }
        if (cookie instanceof DefaultCookie) {
            DefaultCookie c = (DefaultCookie) cookie;
            if (c.sameSite() != null) {
                CookieUtil.add(buf, CookieHeaderNames.SAMESITE, c.sameSite().name());
            }
            if (c.isPartitioned()) {
                CookieUtil.add(buf, CookieHeaderNames.PARTITIONED);
            }
        }
        return CookieUtil.stripTrailingSeparator(buf);
    }

    private static List<String> dedup(List<String> encoded, Map<String, Integer> nameToLastIndex) {
        boolean[] isLastInstance = new boolean[encoded.size()];
        Iterator<Integer> it2 = nameToLastIndex.values().iterator();
        while (it2.hasNext()) {
            int idx = it2.next().intValue();
            isLastInstance[idx] = true;
        }
        List<String> dedupd = new ArrayList<>(nameToLastIndex.size());
        int n = encoded.size();
        for (int i = 0; i < n; i++) {
            if (isLastInstance[i]) {
                dedupd.add(encoded.get(i));
            }
        }
        return dedupd;
    }

    public List<String> encode(Cookie... cookies) {
        if (((Cookie[]) ObjectUtil.checkNotNull(cookies, "cookies")).length == 0) {
            return Collections.emptyList();
        }
        List<String> encoded = new ArrayList<>(cookies.length);
        Map<String, Integer> nameToIndex = (!this.strict || cookies.length <= 1) ? null : new HashMap<>();
        boolean hasDupdName = false;
        for (int i = 0; i < cookies.length; i++) {
            Cookie c = cookies[i];
            encoded.add(encode(c));
            if (nameToIndex != null) {
                hasDupdName |= nameToIndex.put(c.name(), Integer.valueOf(i)) != null;
            }
        }
        return hasDupdName ? dedup(encoded, nameToIndex) : encoded;
    }

    public List<String> encode(Collection<? extends Cookie> cookies) {
        if (((Collection) ObjectUtil.checkNotNull(cookies, "cookies")).isEmpty()) {
            return Collections.emptyList();
        }
        List<String> encoded = new ArrayList<>(cookies.size());
        Map<String, Integer> nameToIndex = (!this.strict || cookies.size() <= 1) ? null : new HashMap<>();
        int i = 0;
        boolean hasDupdName = false;
        for (Cookie c : cookies) {
            encoded.add(encode(c));
            if (nameToIndex != null) {
                int i2 = i + 1;
                hasDupdName = (nameToIndex.put(c.name(), Integer.valueOf(i)) != null) | hasDupdName;
                i = i2;
            }
        }
        return hasDupdName ? dedup(encoded, nameToIndex) : encoded;
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x005a  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0081  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x0086  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.util.List<java.lang.String> encode(java.lang.Iterable<? extends io.netty.handler.codec.http.cookie.Cookie> r12) {
        /*
            r11 = this;
            java.lang.String r0 = "cookies"
            java.lang.Object r0 = io.netty.util.internal.ObjectUtil.checkNotNull(r12, r0)
            java.lang.Iterable r0 = (java.lang.Iterable) r0
            java.util.Iterator r0 = r0.iterator()
            boolean r1 = r0.hasNext()
            if (r1 != 0) goto L17
            java.util.List r1 = java.util.Collections.emptyList()
            return r1
        L17:
            java.util.ArrayList r1 = new java.util.ArrayList
            r1.<init>()
            java.lang.Object r2 = r0.next()
            io.netty.handler.codec.http.cookie.Cookie r2 = (io.netty.handler.codec.http.cookie.Cookie) r2
            boolean r3 = r11.strict
            if (r3 == 0) goto L32
            boolean r3 = r0.hasNext()
            if (r3 == 0) goto L32
            java.util.HashMap r3 = new java.util.HashMap
            r3.<init>()
            goto L33
        L32:
            r3 = 0
        L33:
            r4 = 0
            java.lang.String r5 = r11.encode(r2)
            r1.add(r5)
            r5 = 0
            r6 = 1
            if (r3 == 0) goto L52
            java.lang.String r7 = r2.name()
            int r8 = r4 + 1
            java.lang.Integer r4 = java.lang.Integer.valueOf(r4)
            java.lang.Object r4 = r3.put(r7, r4)
            if (r4 == 0) goto L51
            r4 = r6
            goto L54
        L51:
            r4 = r8
        L52:
            r8 = r4
            r4 = r5
        L54:
            boolean r7 = r0.hasNext()
            if (r7 == 0) goto L7f
            java.lang.Object r7 = r0.next()
            io.netty.handler.codec.http.cookie.Cookie r7 = (io.netty.handler.codec.http.cookie.Cookie) r7
            java.lang.String r9 = r11.encode(r7)
            r1.add(r9)
            if (r3 == 0) goto L7e
            java.lang.String r9 = r7.name()
            int r10 = r8 + 1
            java.lang.Integer r8 = java.lang.Integer.valueOf(r8)
            java.lang.Object r8 = r3.put(r9, r8)
            if (r8 == 0) goto L7b
            r8 = r6
            goto L7c
        L7b:
            r8 = r5
        L7c:
            r4 = r4 | r8
            r8 = r10
        L7e:
            goto L54
        L7f:
            if (r4 == 0) goto L86
            java.util.List r5 = dedup(r1, r3)
            goto L87
        L86:
            r5 = r1
        L87:
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.handler.codec.http.cookie.ServerCookieEncoder.encode(java.lang.Iterable):java.util.List");
    }
}
