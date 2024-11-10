package com.trossense;

import com.trossense.sdk.PointerHolder;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/* loaded from: classes3.dex */
class e implements Callback {
    private static final long b = dj.a(-5534664316954113278L, -2807037798514763761L, MethodHandles.lookup().lookupClass()).a(268047808771152L);
    private static final String c = a(60, a("D\r)S'+\u000e"));
    final dg a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public e(dg dgVar) {
        this.a = dgVar;
    }

    private static String a(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c2 = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 11;
                    break;
                case 1:
                    i2 = 68;
                    break;
                case 2:
                    i2 = 118;
                    break;
                case 3:
                    i2 = 12;
                    break;
                case 4:
                    i2 = 126;
                    break;
                case 5:
                    i2 = 100;
                    break;
                default:
                    i2 = 65;
                    break;
            }
            cArr[i3] = (char) (c2 ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    private static char[] a(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ 'A');
        }
        return charArray;
    }

    @Override // okhttp3.Callback
    public void onFailure(Call call, IOException iOException) {
    }

    @Override // okhttp3.Callback
    public void onResponse(Call call, Response response) throws IOException {
        VerifyManager.b();
        String[] split = response.body().string().split(" ");
        if (split.length == 4 && split[0].equals(c)) {
            VerifyManager.e((int) Long.parseLong(split[1]), Long.parseLong(split[2]), split[3]);
        }
        if (PointerHolder.s() == null) {
            VerifyManager.b(new String[2]);
        }
    }
}
