package com.google.thirdparty.publicsuffix;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

/* loaded from: classes.dex */
final class TrieParser {
    private static final Joiner PREFIX_JOINER = Joiner.on("");

    TrieParser() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ImmutableMap<String, PublicSuffixType> parseTrie(CharSequence encoded) {
        ImmutableMap.Builder<String, PublicSuffixType> builder = ImmutableMap.builder();
        int encodedLen = encoded.length();
        int idx = 0;
        while (idx < encodedLen) {
            idx += doParseTrieToBuilder(Lists.newLinkedList(), encoded.subSequence(idx, encodedLen), builder);
        }
        return builder.build();
    }

    /* JADX WARN: Code restructure failed: missing block: B:19:0x004f, code lost:            if (r2 != ',') goto L24;     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x0051, code lost:            if (r1 >= r0) goto L39;     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0053, code lost:            r1 = r1 + doParseTrieToBuilder(r9, r10.subSequence(r1, r0), r11);     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0060, code lost:            if (r10.charAt(r1) == '?') goto L38;     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0066, code lost:            if (r10.charAt(r1) != ',') goto L41;     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x0068, code lost:            r1 = r1 + 1;     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static int doParseTrieToBuilder(java.util.List<java.lang.CharSequence> r9, java.lang.CharSequence r10, com.google.common.collect.ImmutableMap.Builder<java.lang.String, com.google.thirdparty.publicsuffix.PublicSuffixType> r11) {
        /*
            int r0 = r10.length()
            r1 = 0
            r2 = 0
        L6:
            r3 = 58
            r4 = 33
            r5 = 44
            r6 = 63
            if (r1 >= r0) goto L24
            char r2 = r10.charAt(r1)
            r7 = 38
            if (r2 == r7) goto L24
            if (r2 == r6) goto L24
            if (r2 == r4) goto L24
            if (r2 == r3) goto L24
            if (r2 != r5) goto L21
            goto L24
        L21:
            int r1 = r1 + 1
            goto L6
        L24:
            r7 = 0
            java.lang.CharSequence r8 = r10.subSequence(r7, r1)
            java.lang.CharSequence r8 = reverse(r8)
            r9.add(r7, r8)
            if (r2 == r4) goto L38
            if (r2 == r6) goto L38
            if (r2 == r3) goto L38
            if (r2 != r5) goto L4b
        L38:
            com.google.common.base.Joiner r3 = com.google.thirdparty.publicsuffix.TrieParser.PREFIX_JOINER
            java.lang.String r3 = r3.join(r9)
            int r4 = r3.length()
            if (r4 <= 0) goto L4b
            com.google.thirdparty.publicsuffix.PublicSuffixType r4 = com.google.thirdparty.publicsuffix.PublicSuffixType.fromCode(r2)
            r11.put(r3, r4)
        L4b:
            int r1 = r1 + 1
            if (r2 == r6) goto L6b
            if (r2 == r5) goto L6b
        L51:
            if (r1 >= r0) goto L6b
            java.lang.CharSequence r3 = r10.subSequence(r1, r0)
            int r3 = doParseTrieToBuilder(r9, r3, r11)
            int r1 = r1 + r3
            char r3 = r10.charAt(r1)
            if (r3 == r6) goto L68
            char r3 = r10.charAt(r1)
            if (r3 != r5) goto L51
        L68:
            int r1 = r1 + 1
        L6b:
            r9.remove(r7)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.thirdparty.publicsuffix.TrieParser.doParseTrieToBuilder(java.util.List, java.lang.CharSequence, com.google.common.collect.ImmutableMap$Builder):int");
    }

    private static CharSequence reverse(CharSequence s) {
        return new StringBuilder(s).reverse();
    }
}
