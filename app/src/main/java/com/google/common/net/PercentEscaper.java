package com.google.common.net;

import com.google.common.base.Preconditions;
import com.google.common.escape.UnicodeEscaper;

/* loaded from: classes.dex */
public final class PercentEscaper extends UnicodeEscaper {
    private static final char[] PLUS_SIGN = {'+'};
    private static final char[] UPPER_HEX_DIGITS = "0123456789ABCDEF".toCharArray();
    private final boolean plusForSpace;
    private final boolean[] safeOctets;

    public PercentEscaper(String safeChars, boolean plusForSpace) {
        Preconditions.checkNotNull(safeChars);
        if (safeChars.matches(".*[0-9A-Za-z].*")) {
            throw new IllegalArgumentException("Alphanumeric characters are always 'safe' and should not be explicitly specified");
        }
        String safeChars2 = safeChars + "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        if (plusForSpace && safeChars2.contains(" ")) {
            throw new IllegalArgumentException("plusForSpace cannot be specified when space is a 'safe' character");
        }
        this.plusForSpace = plusForSpace;
        this.safeOctets = createSafeOctets(safeChars2);
    }

    private static boolean[] createSafeOctets(String safeChars) {
        int maxChar = -1;
        char[] safeCharArray = safeChars.toCharArray();
        for (char c : safeCharArray) {
            maxChar = Math.max((int) c, maxChar);
        }
        boolean[] octets = new boolean[maxChar + 1];
        for (char c2 : safeCharArray) {
            octets[c2] = true;
        }
        return octets;
    }

    @Override // com.google.common.escape.UnicodeEscaper
    protected int nextEscapeIndex(CharSequence csq, int index, int end) {
        Preconditions.checkNotNull(csq);
        while (index < end) {
            char c = csq.charAt(index);
            if (c >= this.safeOctets.length || !this.safeOctets[c]) {
                break;
            }
            index++;
        }
        return index;
    }

    @Override // com.google.common.escape.UnicodeEscaper, com.google.common.escape.Escaper
    public String escape(String s) {
        Preconditions.checkNotNull(s);
        int slen = s.length();
        for (int index = 0; index < slen; index++) {
            char c = s.charAt(index);
            if (c >= this.safeOctets.length || !this.safeOctets[c]) {
                return escapeSlow(s, index);
            }
        }
        return s;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.common.escape.UnicodeEscaper
    public char[] escape(int cp) {
        if (cp < this.safeOctets.length && this.safeOctets[cp]) {
            return null;
        }
        if (cp == 32 && this.plusForSpace) {
            return PLUS_SIGN;
        }
        if (cp <= 127) {
            char[] dest = {'%', UPPER_HEX_DIGITS[cp >>> 4], UPPER_HEX_DIGITS[cp & 15]};
            return dest;
        }
        if (cp <= 2047) {
            char[] dest2 = {'%', UPPER_HEX_DIGITS[(cp >>> 4) | 12], UPPER_HEX_DIGITS[cp & 15], '%', UPPER_HEX_DIGITS[(cp & 3) | 8], UPPER_HEX_DIGITS[cp & 15]};
            int cp2 = cp >>> 4;
            int cp3 = cp2 >>> 2;
            return dest2;
        }
        if (cp <= 65535) {
            char[] dest3 = {'%', 'E', r1[cp >>> 2], '%', UPPER_HEX_DIGITS[(cp & 3) | 8], UPPER_HEX_DIGITS[cp & 15], '%', UPPER_HEX_DIGITS[(cp & 3) | 8], UPPER_HEX_DIGITS[cp & 15]};
            int cp4 = cp >>> 4;
            int cp5 = cp4 >>> 2;
            int cp6 = cp5 >>> 4;
            char[] cArr = UPPER_HEX_DIGITS;
            return dest3;
        }
        if (cp <= 1114111) {
            char[] dest4 = {'%', 'F', UPPER_HEX_DIGITS[(cp >>> 2) & 7], '%', UPPER_HEX_DIGITS[(cp & 3) | 8], UPPER_HEX_DIGITS[cp & 15], '%', UPPER_HEX_DIGITS[(cp & 3) | 8], UPPER_HEX_DIGITS[cp & 15], '%', UPPER_HEX_DIGITS[(cp & 3) | 8], UPPER_HEX_DIGITS[cp & 15]};
            int cp7 = cp >>> 4;
            int cp8 = cp7 >>> 2;
            int cp9 = cp8 >>> 4;
            int cp10 = cp9 >>> 2;
            int cp11 = cp10 >>> 4;
            return dest4;
        }
        throw new IllegalArgumentException("Invalid unicode character value " + cp);
    }
}
