package com.trossense;

import java.lang.invoke.MethodHandles;
import java.util.function.BooleanSupplier;

@cg(a = "Notification", b = b.Render, c = "提示")
/* loaded from: classes3.dex */
public class b_ extends bm {
    private static az j;
    private static az k;
    private static az l;
    private static final long n;
    private an m;

    static {
        long a = dj.a(-5645574875814136343L, 8079273810823525832L, MethodHandles.lookup().lookupClass()).a(250062024221595L);
        n = a;
        long j2 = (a ^ 26702679703498L) ^ 91277207278520L;
        String[] strArr = new String[3];
        int length = "!z7)A\u000b\n.\f<|6&\f\u0016\n17,3\t\u0011<|6&\f\u0016\n14:(\u0003\u001bP<m>".length();
        char c = '\b';
        int i = -1;
        int i2 = 0;
        while (true) {
            int i3 = i + 1;
            int i4 = c + i3;
            int i5 = i2 + 1;
            strArr[i2] = b(32, b("!z7)A\u000b\n.\f<|6&\f\u0016\n17,3\t\u0011<|6&\f\u0016\n14:(\u0003\u001bP<m>".substring(i3, i4)));
            if (i4 >= length) {
                j = a1.a(strArr[0], 80, j2);
                k = a1.a(strArr[2], 45, j2);
                l = a1.a(strArr[1], 35, j2);
                return;
            } else {
                i2 = i5;
                i = i4;
                c = "!z7)A\u000b\n.\f<|6&\f\u0016\n17,3\t\u0011<|6&\f\u0016\n14:(\u0003\u001bP<m>".charAt(i4);
            }
        }
    }

    public b_(long j2) {
        super((j2 ^ n) ^ 139516644000578L);
        an anVar = new an(this);
        this.m = anVar;
        anVar.a(new BooleanSupplier() { // from class: com.trossense.b_$$ExternalSyntheticLambda0
            @Override // java.util.function.BooleanSupplier
            public final boolean getAsBoolean() {
                return b_.this.m177lambda$new$0$comtrossenseb_();
            }
        });
    }

    private static String b(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 104;
                    break;
                case 1:
                    i2 = 57;
                    break;
                case 2:
                    i2 = 120;
                    break;
                case 3:
                    i2 = 103;
                    break;
                case 4:
                    i2 = 79;
                    break;
                case 5:
                    i2 = 95;
                    break;
                default:
                    i2 = 94;
                    break;
            }
            cArr[i3] = (char) (c ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    private static char[] b(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ '^');
        }
        return charArray;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static az o() {
        return j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static az p() {
        return k;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static az q() {
        return l;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-trossense-b_, reason: not valid java name */
    public boolean m177lambda$new$0$comtrossenseb_() {
        return !l();
    }

    public an n() {
        return this.m;
    }
}
