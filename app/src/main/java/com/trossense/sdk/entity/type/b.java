package com.trossense.sdk.entity.type;

/* loaded from: classes3.dex */
public class b extends EntityActor {
    private static int[] d;

    static {
        if (ab() == null) {
            a(new int[2]);
        }
    }

    public b(long j) {
        super(j);
    }

    public static void a(int[] iArr) {
        d = iArr;
    }

    public static int[] ab() {
        return d;
    }
}
