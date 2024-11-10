package io.netty.handler.codec.http.cookie;

import com.trossense.bl;
import io.netty.util.internal.InternalThreadLocalMap;
import java.util.BitSet;

/* loaded from: classes4.dex */
final class CookieUtil {
    private static final BitSet VALID_COOKIE_NAME_OCTETS = validCookieNameOctets();
    private static final BitSet VALID_COOKIE_VALUE_OCTETS = validCookieValueOctets();
    private static final BitSet VALID_COOKIE_ATTRIBUTE_VALUE_OCTETS = validCookieAttributeValueOctets();

    private static BitSet validCookieNameOctets() {
        BitSet bits = new BitSet();
        for (int i = 32; i < 127; i++) {
            bits.set(i);
        }
        int[] separators = {40, 41, 60, 62, 64, 44, 59, 58, 92, 34, 47, 91, 93, 63, 61, 123, bl.bm, 32, 9};
        for (int separator : separators) {
            bits.set(separator, false);
        }
        return bits;
    }

    private static BitSet validCookieValueOctets() {
        BitSet bits = new BitSet();
        bits.set(33);
        for (int i = 35; i <= 43; i++) {
            bits.set(i);
        }
        for (int i2 = 45; i2 <= 58; i2++) {
            bits.set(i2);
        }
        for (int i3 = 60; i3 <= 91; i3++) {
            bits.set(i3);
        }
        for (int i4 = 93; i4 <= 126; i4++) {
            bits.set(i4);
        }
        return bits;
    }

    private static BitSet validCookieAttributeValueOctets() {
        BitSet bits = new BitSet();
        for (int i = 32; i < 127; i++) {
            bits.set(i);
        }
        bits.set(59, false);
        return bits;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static StringBuilder stringBuilder() {
        return InternalThreadLocalMap.get().stringBuilder();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String stripTrailingSeparatorOrNull(StringBuilder buf) {
        if (buf.length() == 0) {
            return null;
        }
        return stripTrailingSeparator(buf);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String stripTrailingSeparator(StringBuilder buf) {
        if (buf.length() > 0) {
            buf.setLength(buf.length() - 2);
        }
        return buf.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void add(StringBuilder sb, String name, long val) {
        sb.append(name);
        sb.append('=');
        sb.append(val);
        sb.append(';');
        sb.append(' ');
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void add(StringBuilder sb, String name, String val) {
        sb.append(name);
        sb.append('=');
        sb.append(val);
        sb.append(';');
        sb.append(' ');
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void add(StringBuilder sb, String name) {
        sb.append(name);
        sb.append(';');
        sb.append(' ');
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void addQuoted(StringBuilder sb, String name, String val) {
        if (val == null) {
            val = "";
        }
        sb.append(name);
        sb.append('=');
        sb.append('\"');
        sb.append(val);
        sb.append('\"');
        sb.append(';');
        sb.append(' ');
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int firstInvalidCookieNameOctet(CharSequence cs) {
        return firstInvalidOctet(cs, VALID_COOKIE_NAME_OCTETS);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int firstInvalidCookieValueOctet(CharSequence cs) {
        return firstInvalidOctet(cs, VALID_COOKIE_VALUE_OCTETS);
    }

    static int firstInvalidOctet(CharSequence cs, BitSet bits) {
        for (int i = 0; i < cs.length(); i++) {
            char c = cs.charAt(i);
            if (!bits.get(c)) {
                return i;
            }
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static CharSequence unwrapValue(CharSequence cs) {
        int len = cs.length();
        if (len > 0 && cs.charAt(0) == '\"') {
            if (len < 2 || cs.charAt(len - 1) != '\"') {
                return null;
            }
            return len == 2 ? "" : cs.subSequence(1, len - 1);
        }
        return cs;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String validateAttributeValue(String name, String value) {
        if (value == null) {
            return null;
        }
        String value2 = value.trim();
        if (value2.isEmpty()) {
            return null;
        }
        int i = firstInvalidOctet(value2, VALID_COOKIE_ATTRIBUTE_VALUE_OCTETS);
        if (i != -1) {
            throw new IllegalArgumentException(name + " contains the prohibited characters: " + value2.charAt(i));
        }
        return value2;
    }

    private CookieUtil() {
    }
}
