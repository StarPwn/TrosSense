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
public class i implements Callback {
    private static final long e = dj.a(4089404502936109105L, -6986865280707811889L, MethodHandles.lookup().lookupClass()).a(8547554990773L);
    private static final String[] f;
    final String a;
    final String b;
    final DialogInterface c;
    final h d;

    /* JADX WARN: Failed to find 'out' block for switch in B:4:0x0045. Please report as an issue. */
    static {
        int i;
        int i2;
        String[] strArr = new String[4];
        String str = "认汜奘贪\b锿讚盗甶扞吂戲寠硴\u0004认汜批劐";
        int length = "认汜奘贪\b锿讚盗甶扞吂戲寠硴\u0004认汜批劐".length();
        char c = 14;
        int i3 = 0;
        int i4 = -1;
        while (true) {
            int i5 = 51;
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
                        str = "m&G!\f\u0018Kx&H\f诩民夕赧E讜棸査伳皠缓纵";
                        length = "m&G!\f\u0018Kx&H\f诩民夕赧E讜棸査伳皠缓纵".length();
                        c = '\n';
                        i2 = -1;
                        i3 = i;
                        i5 = 126;
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
                i5 = 126;
                i6 = i2 + 1;
                substring = str.substring(i6, i6 + c);
                z = false;
            }
            c = str.charAt(i4);
            i3 = i;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public i(h hVar, String str, String str2, DialogInterface dialogInterface) {
        this.d = hVar;
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
                    i2 = 96;
                    break;
                case 1:
                    i2 = 45;
                    break;
                case 2:
                    i2 = 90;
                    break;
                case 3:
                    i2 = 60;
                    break;
                case 4:
                    i2 = 23;
                    break;
                case 5:
                    i2 = 21;
                    break;
                default:
                    i2 = 70;
                    break;
            }
            cArr[i3] = (char) (c ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    private static char[] a(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ 'F');
        }
        return charArray;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onResponse$0$com-trossense-i, reason: not valid java name */
    public void m210lambda$onResponse$0$comtrossensei(DialogInterface dialogInterface, Activity activity, String str) {
        g.a(this.d.d, dialogInterface, true);
        dialogInterface.dismiss();
        g.a(this.d.d, activity, str);
    }

    @Override // okhttp3.Callback
    public void onFailure(Call call, IOException iOException) {
        g.a(this.d.d, f[3]);
    }

    @Override // okhttp3.Callback
    public void onResponse(Call call, Response response) throws IOException {
        boolean b = g.b();
        String string = response.body().string();
        String[] strArr = f;
        if (string.contains(strArr[2])) {
            g.a(this.d.d, strArr[1]);
            this.d.d.a(this.a, this.b);
            Activity activity = this.d.c;
            final DialogInterface dialogInterface = this.c;
            final Activity activity2 = this.d.c;
            final String str = this.a;
            activity.runOnUiThread(new Runnable() { // from class: com.trossense.i$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    i.this.m210lambda$onResponse$0$comtrossensei(dialogInterface, activity2, str);
                }
            });
            if (!b) {
                return;
            }
        }
        g.a(this.d.d, strArr[0]);
    }
}
