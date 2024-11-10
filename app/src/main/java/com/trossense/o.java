package com.trossense;

import android.app.Activity;
import android.content.DialogInterface;
import com.trossense.clients.TrosSense;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public class o implements Callback {
    private static final long d = dj.a(8750214043918169922L, 5759763009440231626L, MethodHandles.lookup().lookupClass()).a(162528690209439L);
    private static final String[] e;
    final DialogInterface a;
    final String b;
    final n c;

    /* JADX WARN: Failed to find 'out' block for switch in B:4:0x0045. Please report as an issue. */
    static {
        int i;
        int i2;
        String[] strArr = new String[7];
        String str = "秀)尾悥珹職吼３＄\f该汇夲赅|讍棛柷佥皇缱續\u0002刔钚\u000b骞评戓勿|惒彈剟违剪乫\u000e悺沤朊伹飍咜7骞评夲赅ｑｻ：";
        int length = "秀)尾悥珹職吼３＄\f该汇夲赅|讍棛柷佥皇缱續\u0002刔钚\u000b骞评戓勿|惒彈剟违剪乫\u000e悺沤朊伹飍咜7骞评夲赅ｑｻ：".length();
        char c = '\t';
        int i3 = 0;
        int i4 = -1;
        while (true) {
            int i5 = 77;
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
                        str = "/>.M{G&\u0002屓施";
                        length = "/>.M{G&\u0002屓施".length();
                        c = 7;
                        i2 = -1;
                        i3 = i;
                        i5 = 3;
                        i6 = i2 + 1;
                        substring = str.substring(i6, i6 + c);
                        z = false;
                        break;
                }
                strArr[i3] = a;
                i2 = i6 + c;
                if (i2 >= length) {
                    e = strArr;
                    return;
                }
                c = str.charAt(i2);
                i3 = i;
                i5 = 3;
                i6 = i2 + 1;
                substring = str.substring(i6, i6 + c);
                z = false;
            }
            c = str.charAt(i4);
            i3 = i;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public o(n nVar, DialogInterface dialogInterface, String str) {
        this.c = nVar;
        this.a = dialogInterface;
        this.b = str;
    }

    private static String a(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 95;
                    break;
                case 1:
                    i2 = 72;
                    break;
                case 2:
                    i2 = 78;
                    break;
                case 3:
                    i2 = 45;
                    break;
                case 4:
                    i2 = 29;
                    break;
                case 5:
                    i2 = 55;
                    break;
                default:
                    i2 = 86;
                    break;
            }
            cArr[i3] = (char) (c ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    private static char[] a(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ 'V');
        }
        return charArray;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onResponse$0$com-trossense-o, reason: not valid java name */
    public void m212lambda$onResponse$0$comtrossenseo(DialogInterface dialogInterface) {
        g.a(this.c.c, dialogInterface, true);
        dialogInterface.dismiss();
    }

    @Override // okhttp3.Callback
    public void onFailure(Call call, IOException iOException) {
        g.a(this.c.c, e[1]);
    }

    @Override // okhttp3.Callback
    public void onResponse(Call call, Response response) throws IOException {
        long j = (d ^ 8355775585188L) ^ 123987662782520L;
        long j2 = j >>> 8;
        int i = (int) ((j << 56) >>> 56);
        boolean b = g.b();
        String[] split = response.body().string().split(" ");
        if (split.length == 4) {
            String str = split[0];
            String[] strArr = e;
            if (str.equals(strArr[5])) {
                long parseLong = Long.parseLong(split[1]);
                VerifyManager.e((int) parseLong, Long.parseLong(split[2]), split[3]);
                g.a(this.c.c, strArr[3] + ((int) (parseLong / 3600000)) + strArr[6] + ((int) ((parseLong % 3600000) / 60000)) + strArr[2] + ((int) ((parseLong % 60000) / 1000)) + strArr[0]);
                Activity activity = this.c.b;
                final DialogInterface dialogInterface = this.a;
                activity.runOnUiThread(new Runnable() { // from class: com.trossense.o$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        o.this.m212lambda$onResponse$0$comtrossenseo(dialogInterface);
                    }
                });
                TrosSense.INSTANCE.a().a(this.b, j2, (byte) i);
                if (!b) {
                    return;
                }
            }
        }
        g.a(this.c.c, e[4]);
    }
}
