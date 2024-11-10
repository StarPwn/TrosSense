package io.netty.handler.codec.http;

import io.netty.handler.codec.DateFormatter;
import io.netty.handler.codec.http.cookie.CookieHeaderNames;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Deprecated
/* loaded from: classes4.dex */
public final class CookieDecoder {
    private static final String COMMENT = "Comment";
    private static final String COMMENTURL = "CommentURL";
    private static final String DISCARD = "Discard";
    private static final String PORT = "Port";
    private static final String VERSION = "Version";
    private final InternalLogger logger = InternalLoggerFactory.getInstance(getClass());
    private final boolean strict;
    private static final CookieDecoder STRICT = new CookieDecoder(true);
    private static final CookieDecoder LAX = new CookieDecoder(false);

    public static Set<Cookie> decode(String header) {
        return decode(header, true);
    }

    public static Set<Cookie> decode(String header, boolean strict) {
        return (strict ? STRICT : LAX).doDecode(header);
    }

    private Set<Cookie> doDecode(String header) {
        int i;
        boolean discard;
        List<String> values;
        String commentURL;
        String domain;
        boolean httpOnly;
        String commentURL2;
        String domain2;
        List<String> names = new ArrayList<>(8);
        List<String> values2 = new ArrayList<>(8);
        extractKeyValuePairs(header, names, values2);
        if (names.isEmpty()) {
            return Collections.emptySet();
        }
        int i2 = 0;
        if (names.get(0).equalsIgnoreCase(VERSION)) {
            try {
                int version = Integer.parseInt(values2.get(0));
                i2 = version;
            } catch (NumberFormatException e) {
            }
            i = 1;
        } else {
            i = 0;
        }
        if (names.size() <= i) {
            return Collections.emptySet();
        }
        Set<Cookie> cookies = new TreeSet<>();
        while (i < names.size()) {
            String name = names.get(i);
            String value = values2.get(i);
            if (value == null) {
                value = "";
            }
            Cookie c = initCookie(name, value);
            if (c == null) {
                return cookies;
            }
            boolean discard2 = false;
            boolean secure = false;
            List<Integer> ports = new ArrayList<>(2);
            int i3 = i + 1;
            String domain3 = null;
            Set<Cookie> cookies2 = cookies;
            String path = null;
            boolean httpOnly2 = false;
            int i4 = i2;
            int version2 = i;
            String name2 = name;
            int j = i3;
            int version3 = i4;
            String value2 = value;
            String value3 = null;
            String comment = null;
            long maxAge = Long.MIN_VALUE;
            while (true) {
                discard = discard2;
                if (j >= names.size()) {
                    values = values2;
                    commentURL = value3;
                    domain = domain3;
                    break;
                }
                String name3 = names.get(j);
                values = values2;
                String value4 = values2.get(j);
                if (DISCARD.equalsIgnoreCase(name3)) {
                    discard = true;
                    value2 = value4;
                } else if (CookieHeaderNames.SECURE.equalsIgnoreCase(name3)) {
                    secure = true;
                    value2 = value4;
                } else if (CookieHeaderNames.HTTPONLY.equalsIgnoreCase(name3)) {
                    httpOnly2 = true;
                    value2 = value4;
                } else if (COMMENT.equalsIgnoreCase(name3)) {
                    comment = value4;
                    value2 = value4;
                } else if (COMMENTURL.equalsIgnoreCase(name3)) {
                    value3 = value4;
                    value2 = value4;
                } else if (CookieHeaderNames.DOMAIN.equalsIgnoreCase(name3)) {
                    domain3 = value4;
                    value2 = value4;
                } else if (CookieHeaderNames.PATH.equalsIgnoreCase(name3)) {
                    path = value4;
                    value2 = value4;
                } else if (!"Expires".equalsIgnoreCase(name3)) {
                    commentURL = value3;
                    domain = domain3;
                    if (CookieHeaderNames.MAX_AGE.equalsIgnoreCase(name3)) {
                        value2 = value4;
                        maxAge = Integer.parseInt(value4);
                        value3 = commentURL;
                        domain3 = domain;
                    } else if (VERSION.equalsIgnoreCase(name3)) {
                        version3 = Integer.parseInt(value4);
                        value2 = value4;
                        value3 = commentURL;
                        domain3 = domain;
                    } else {
                        if (!PORT.equalsIgnoreCase(name3)) {
                            break;
                        }
                        String[] portList = value4.split(",");
                        value2 = value4;
                        for (String s1 : portList) {
                            try {
                                ports.add(Integer.valueOf(s1));
                            } catch (NumberFormatException e2) {
                            }
                        }
                        value3 = commentURL;
                        domain3 = domain;
                    }
                } else {
                    Date date = DateFormatter.parseHttpDate(value4);
                    if (date != null) {
                        long maxAgeMillis = date.getTime() - System.currentTimeMillis();
                        commentURL2 = value3;
                        domain2 = domain3;
                        maxAge = (maxAgeMillis / 1000) + (maxAgeMillis % 1000 != 0 ? 1 : 0);
                    } else {
                        commentURL2 = value3;
                        domain2 = domain3;
                    }
                    value2 = value4;
                    value3 = commentURL2;
                    domain3 = domain2;
                }
                j++;
                version2++;
                name2 = name3;
                discard2 = discard;
                values2 = values;
            }
            c.setVersion(version3);
            c.setMaxAge(maxAge);
            c.setPath(path);
            c.setDomain(domain);
            c.setSecure(secure);
            c.setHttpOnly(httpOnly2);
            if (version3 > 0) {
                c.setComment(comment);
            }
            if (version3 > 1) {
                c.setCommentUrl(commentURL);
                c.setPorts(ports);
                httpOnly = discard;
                c.setDiscard(httpOnly);
            } else {
                httpOnly = discard;
            }
            cookies2.add(c);
            i = version2 + 1;
            cookies = cookies2;
            i2 = version3;
            values2 = values;
        }
        return cookies;
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Failed to find switch 'out' block (already processed)
        	at jadx.core.dex.visitors.regions.RegionMaker.calcSwitchOut(RegionMaker.java:923)
        	at jadx.core.dex.visitors.regions.RegionMaker.processSwitch(RegionMaker.java:797)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:157)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:91)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeEndlessLoop(RegionMaker.java:411)
        	at jadx.core.dex.visitors.regions.RegionMaker.processLoop(RegionMaker.java:201)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:135)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:91)
        	at jadx.core.dex.visitors.regions.RegionMaker.processIf(RegionMaker.java:740)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:152)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:91)
        	at jadx.core.dex.visitors.regions.RegionMaker.processIf(RegionMaker.java:740)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:152)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:91)
        	at jadx.core.dex.visitors.regions.RegionMaker.processLoop(RegionMaker.java:263)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:135)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:91)
        	at jadx.core.dex.visitors.regions.RegionMaker.processFallThroughCases(RegionMaker.java:841)
        	at jadx.core.dex.visitors.regions.RegionMaker.processSwitch(RegionMaker.java:800)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:157)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:91)
        	at jadx.core.dex.visitors.regions.RegionMaker.processLoop(RegionMaker.java:263)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:135)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:91)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:52)
        */
    private static void extractKeyValuePairs(java.lang.String r10, java.util.List<java.lang.String> r11, java.util.List<java.lang.String> r12) {
        /*
            Method dump skipped, instructions count: 264
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.handler.codec.http.CookieDecoder.extractKeyValuePairs(java.lang.String, java.util.List, java.util.List):void");
    }

    private CookieDecoder(boolean strict) {
        this.strict = strict;
    }

    private DefaultCookie initCookie(String name, String value) {
        int invalidOctetPos;
        int invalidOctetPos2;
        if (name == null || name.length() == 0) {
            this.logger.debug("Skipping cookie with null name");
            return null;
        }
        if (value == null) {
            this.logger.debug("Skipping cookie with null value");
            return null;
        }
        CharSequence unwrappedValue = CookieUtil.unwrapValue(value);
        if (unwrappedValue == null) {
            this.logger.debug("Skipping cookie because starting quotes are not properly balanced in '{}'", unwrappedValue);
            return null;
        }
        if (this.strict && (invalidOctetPos2 = CookieUtil.firstInvalidCookieNameOctet(name)) >= 0) {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Skipping cookie because name '{}' contains invalid char '{}'", name, Character.valueOf(name.charAt(invalidOctetPos2)));
            }
            return null;
        }
        boolean wrap = unwrappedValue.length() != value.length();
        if (this.strict && (invalidOctetPos = CookieUtil.firstInvalidCookieValueOctet(unwrappedValue)) >= 0) {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Skipping cookie because value '{}' contains invalid char '{}'", unwrappedValue, Character.valueOf(unwrappedValue.charAt(invalidOctetPos)));
            }
            return null;
        }
        DefaultCookie cookie = new DefaultCookie(name, unwrappedValue.toString());
        cookie.setWrap(wrap);
        return cookie;
    }
}
