package com.trossense;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/* loaded from: classes3.dex */
class m implements Callback {
    private static final long b = dj.a(-3895910448945207925L, 1139685562112129695L, MethodHandles.lookup().lookupClass()).a(144647947313693L);
    private static final String[] c;
    final l a;

    /* JADX WARN: Failed to find 'out' block for switch in B:4:0x0044. Please report as an issue. */
    static {
        int i;
        int i2;
        String[] strArr = new String[4];
        String str = "卮寑泃冑外赓\u0000旯敟皯卼寡扠州袤甿迬\n|bH~B\u0005_ibG";
        int length = "卮寑泃冑外赓\u0000旯敟皯卼寡扠州袤甿迬\n|bH~B\u0005_ibG".length();
        char c2 = 17;
        int i3 = 0;
        int i4 = -1;
        while (true) {
            int i5 = 5;
            int i6 = i4 + 1;
            String substring = str.substring(i6, i6 + c2);
            boolean z = -1;
            while (true) {
                String a = a(i5, a(substring));
                i = i3 + 1;
                switch (z) {
                    case false:
                        break;
                    default:
                        strArr[i3] = a;
                        i4 = i6 + c2;
                        if (i4 < length) {
                            break;
                        }
                        str = "讍氠奯赍~说梙枟伂盚缹纎\u0006匛室涥勈扂劜";
                        length = "讍氠奯赍~说梙枟伂盚缹纎\u0006匛室涥勈扂劜".length();
                        c2 = '\f';
                        i2 = -1;
                        i3 = i;
                        i5 = 112;
                        i6 = i2 + 1;
                        substring = str.substring(i6, i6 + c2);
                        z = false;
                        break;
                }
                strArr[i3] = a;
                i2 = i6 + c2;
                if (i2 >= length) {
                    c = strArr;
                    return;
                }
                c2 = str.charAt(i2);
                i3 = i;
                i5 = 112;
                i6 = i2 + 1;
                substring = str.substring(i6, i6 + c2);
                z = false;
            }
            c2 = str.charAt(i4);
            i3 = i;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public m(l lVar) {
        this.a = lVar;
    }

    private static String a(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c2 = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 10;
                    break;
                case 1:
                    i2 = 18;
                    break;
                case 2:
                    i2 = 46;
                    break;
                case 3:
                    i2 = 24;
                    break;
                case 4:
                    i2 = 34;
                    break;
                case 5:
                    i2 = 115;
                    break;
                default:
                    i2 = 41;
                    break;
            }
            cArr[i3] = (char) (c2 ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    private static char[] a(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ ')');
        }
        return charArray;
    }

    @Override // okhttp3.Callback
    public void onFailure(Call call, IOException iOException) {
        g.a(this.a.c, c[2]);
    }

    @Override // okhttp3.Callback
    public void onResponse(Call call, Response response) throws IOException {
        boolean c2 = g.c();
        String string = response.body().string();
        String[] strArr = c;
        if (string.contains(strArr[1])) {
            g.a(this.a.c, strArr[3]);
            if (c2) {
                return;
            }
        }
        g.a(this.a.c, strArr[0]);
    }
}
