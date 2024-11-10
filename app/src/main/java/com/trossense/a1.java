package com.trossense;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;

/* loaded from: classes3.dex */
public class a1 {
    private static final long b = dj.a(-7121139778787081352L, -413439875488990090L, MethodHandles.lookup().lookupClass()).a(20473411189756L);
    private static final HashMap<ay, az> a = new HashMap<>();

    /* JADX WARN: Code restructure failed: missing block: B:9:0x0030, code lost:            if (r7 < 0) goto L10;     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static com.trossense.az a(long r7, com.trossense.ay r9) {
        /*
            long r0 = com.trossense.a1.b
            long r7 = r7 ^ r0
            r0 = 75338311842022(0x44851150b8e6, double:3.7222071696818E-310)
            long r0 = r0 ^ r7
            r2 = 75562316232911(0x44b9390478cf, double:3.73327445708737E-310)
            long r2 = r2 ^ r7
            java.util.HashMap<com.trossense.ay, com.trossense.az> r4 = com.trossense.a1.a
            boolean r5 = r4.containsKey(r9)
            if (r5 == 0) goto L1e
            java.lang.Object r7 = r4.get(r9)
            com.trossense.az r7 = (com.trossense.az) r7
            return r7
        L1e:
            int r5 = r9.b()
            r6 = 35
            if (r5 >= r6) goto L32
            com.trossense.az r5 = new com.trossense.az
            r6 = 0
            r5.<init>(r9, r0, r6)
            r0 = 0
            int r7 = (r7 > r0 ? 1 : (r7 == r0 ? 0 : -1))
            if (r7 >= 0) goto L37
        L32:
            com.trossense.az r5 = new com.trossense.az
            r5.<init>(r2, r9)
        L37:
            r4.put(r9, r5)
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.a1.a(long, com.trossense.ay):com.trossense.az");
    }

    public static az a(String str, int i, long j) {
        return a((j ^ b) ^ 88864391096942L, new ay(str, i));
    }
}
