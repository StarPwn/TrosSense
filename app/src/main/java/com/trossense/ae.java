package com.trossense;

/* loaded from: classes3.dex */
public class ae extends p {
    private static String[] p;
    private co o;

    static {
        if (m() == null) {
            b(new String[2]);
        }
    }

    public ae(float f, float f2, co coVar) {
        super(f, f2);
        this.o = coVar;
    }

    public static void b(String[] strArr) {
        p = strArr;
    }

    public static String[] m() {
        return p;
    }

    public co l() {
        return this.o;
    }
}
