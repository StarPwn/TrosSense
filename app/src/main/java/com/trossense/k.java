package com.trossense;

import android.app.Activity;
import android.content.DialogInterface;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public class k implements Callback {
    private static final long e = dj.a(5023303318192381886L, -8803253202636672852L, MethodHandles.lookup().lookupClass()).a(155005750970478L);
    private static final String[] f;
    final String a;
    final String b;
    final DialogInterface c;
    final j d;

    /* JADX WARN: Failed to find 'out' block for switch in B:4:0x0045. Please report as an issue. */
    static {
        int i;
        int i2;
        String[] strArr = new String[6];
        String str = "诅汔外赊5讈梏柗佶皣缾绅\u000f诅汔外赊5讚畧戅君又肒巫裔沧冾\nAcD\f|\f<TcK\n@s@\u0006j\u000b*@sC";
        int length = "诅汔外赊5讈梏柗佶皣缾绅\u000f诅汔外赊5讚畧戅君又肒巫裔沧冾\nAcD\f|\f<TcK\n@s@\u0006j\u000b*@sC".length();
        char c = '\f';
        int i3 = 0;
        int i4 = -1;
        while (true) {
            int i5 = 121;
            int i6 = i4 + 1;
            String substring = str.substring(i6, i6 + c);
            boolean z = -1;
            while (true) {
                String a = a(i5, a(substring));
                i = i3 + 1;
                switch (z) {
                    case false:
                        break;
                    default:
                        strArr[i3] = a;
                        i4 = i6 + c;
                        if (i4 < length) {
                            break;
                        }
                        str = "變氛扸势\u0012變氛奙贅z丰台杇嘱厂胝沾冼一乗赿原哆";
                        length = "變氛扸势\u0012變氛奙贅z丰台杇嘱厂胝沾冼一乗赿原哆".length();
                        c = 4;
                        i2 = -1;
                        i3 = i;
                        i5 = 54;
                        i6 = i2 + 1;
                        substring = str.substring(i6, i6 + c);
                        z = false;
                        break;
                }
                strArr[i3] = a;
                i2 = i6 + c;
                if (i2 >= length) {
                    f = strArr;
                    return;
                }
                c = str.charAt(i2);
                i3 = i;
                i5 = 54;
                i6 = i2 + 1;
                substring = str.substring(i6, i6 + c);
                z = false;
            }
            c = str.charAt(i4);
            i3 = i;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public k(j jVar, String str, String str2, DialogInterface dialogInterface) {
        this.d = jVar;
        this.a = str;
        this.b = str2;
        this.c = dialogInterface;
    }

    private static String a(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 75;
                    break;
                case 1:
                    i2 = 111;
                    break;
                case 2:
                    i2 = 94;
                    break;
                case 3:
                    i2 = 22;
                    break;
                case 4:
                    i2 = 96;
                    break;
                case 5:
                    i2 = 6;
                    break;
                default:
                    i2 = 54;
                    break;
            }
            cArr[i3] = (char) (c ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    private static char[] a(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ '6');
        }
        return charArray;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onResponse$0$com-trossense-k, reason: not valid java name */
    public void m211lambda$onResponse$0$comtrossensek(DialogInterface dialogInterface, Activity activity, String str) {
        g.a(this.d.e, dialogInterface, true);
        dialogInterface.dismiss();
        g.a(this.d.e, activity, str);
    }

    @Override // okhttp3.Callback
    public void onFailure(Call call, IOException iOException) {
        g.a(this.d.e, f[0]);
    }

    @Override // okhttp3.Callback
    public void onResponse(Call call, Response response) throws IOException {
        boolean c = g.c();
        String string = response.body().string();
        String[] strArr = f;
        if (string.contains(strArr[2])) {
            g.a(this.d.e, strArr[4]);
            this.d.e.a(this.a, this.b);
            Activity activity = this.d.d;
            final DialogInterface dialogInterface = this.c;
            final Activity activity2 = this.d.d;
            final String str = this.a;
            activity.runOnUiThread(new Runnable() { // from class: com.trossense.k$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    k.this.m211lambda$onResponse$0$comtrossensek(dialogInterface, activity2, str);
                }
            });
            if (c) {
                return;
            }
        }
        if (string.contains(strArr[3])) {
            g.a(this.d.e, strArr[5]);
            if (c) {
                return;
            }
        }
        g.a(this.d.e, strArr[1]);
    }
}
