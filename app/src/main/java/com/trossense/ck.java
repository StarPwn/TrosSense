package com.trossense;

/* loaded from: classes3.dex */
/* synthetic */ class ck {
    static final int[] a;

    static {
        int[] iArr = new int[c.values().length];
        a = iArr;
        try {
            iArr[c.ENABLE.ordinal()] = 1;
        } catch (NoSuchFieldError e) {
        }
        try {
            a[c.DISABLE.ordinal()] = 2;
        } catch (NoSuchFieldError e2) {
        }
    }
}
