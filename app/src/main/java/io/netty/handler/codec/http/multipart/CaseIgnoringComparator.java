package io.netty.handler.codec.http.multipart;

import java.io.Serializable;
import java.util.Comparator;

/* loaded from: classes4.dex */
final class CaseIgnoringComparator implements Comparator<CharSequence>, Serializable {
    static final CaseIgnoringComparator INSTANCE = new CaseIgnoringComparator();
    private static final long serialVersionUID = 4582133183775373862L;

    private CaseIgnoringComparator() {
    }

    @Override // java.util.Comparator
    public int compare(CharSequence o1, CharSequence o2) {
        char c1;
        char c2;
        char c12;
        char c22;
        int o1Length = o1.length();
        int o2Length = o2.length();
        int min = Math.min(o1Length, o2Length);
        for (int i = 0; i < min; i++) {
            char c13 = o1.charAt(i);
            char c23 = o2.charAt(i);
            if (c13 != c23 && (c1 = Character.toUpperCase(c13)) != (c2 = Character.toUpperCase(c23)) && (c12 = Character.toLowerCase(c1)) != (c22 = Character.toLowerCase(c2))) {
                return c12 - c22;
            }
        }
        int i2 = o1Length - o2Length;
        return i2;
    }

    private Object readResolve() {
        return INSTANCE;
    }
}
