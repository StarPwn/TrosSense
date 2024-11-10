package com.google.common.base;

import java.nio.charset.Charset;
import org.jose4j.lang.StringUtil;

/* loaded from: classes.dex */
public final class Charsets {
    public static final Charset US_ASCII = Charset.forName(StringUtil.US_ASCII);
    public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");
    public static final Charset UTF_8 = Charset.forName(StringUtil.UTF_8);
    public static final Charset UTF_16BE = Charset.forName("UTF-16BE");
    public static final Charset UTF_16LE = Charset.forName("UTF-16LE");
    public static final Charset UTF_16 = Charset.forName("UTF-16");

    private Charsets() {
    }
}
