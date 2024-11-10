package com.trossense;

import com.trossense.sdk.PointerHolder;
import java.lang.invoke.MethodHandles;
import java.util.TimerTask;
import kotlin.text.Typography;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public class dg extends TimerTask {
    private static final long c = dj.a(-2210491616906949387L, 5774422922066748112L, MethodHandles.lookup().lookupClass()).a(154926862625415L);
    private static final String[] d;
    final String a;
    final VerifyManager b;

    static {
        String[] strArr = new String[2];
        int length = "Mkri\u0010\u001en\u0011,(+\u001e\u0003o\u0014&57\u0013\u000bt\u0015/6)\u0005d2@m)h_T3\\\u0019Qz~m\u0005A-Dvh\"\nR)Dmu|^\f4Qy+!".length();
        int i = 0;
        char c2 = Typography.dollar;
        int i2 = -1;
        while (true) {
            int i3 = i2 + 1;
            int i4 = c2 + i3;
            int i5 = i + 1;
            strArr[i] = a(51, a("Mkri\u0010\u001en\u0011,(+\u001e\u0003o\u0014&57\u0013\u000bt\u0015/6)\u0005d2@m)h_T3\\\u0019Qz~m\u0005A-Dvh\"\nR)Dmu|^\f4Qy+!".substring(i3, i4)));
            if (i4 >= length) {
                d = strArr;
                return;
            } else {
                i2 = i4;
                c2 = "Mkri\u0010\u001en\u0011,(+\u001e\u0003o\u0014&57\u0013\u000bt\u0015/6)\u0005d2@m)h_T3\\\u0019Qz~m\u0005A-Dvh\"\nR)Dmu|^\f4Qy+!".charAt(i4);
                i = i5;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public dg(VerifyManager verifyManager, String str) {
        this.b = verifyManager;
        this.a = str;
    }

    private static String a(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c2 = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 22;
                    break;
                case 1:
                    i2 = 44;
                    break;
                case 2:
                    i2 = 53;
                    break;
                case 3:
                    i2 = 42;
                    break;
                case 4:
                    i2 = 25;
                    break;
                case 5:
                    i2 = 2;
                    break;
                default:
                    i2 = 114;
                    break;
            }
            cArr[i3] = (char) (c2 ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    private static char[] a(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ 'r');
        }
        return charArray;
    }

    @Override // java.util.TimerTask, java.lang.Runnable
    public void run() {
        String[] b = VerifyManager.b();
        String[] strArr = d;
        this.b.d.newCall(new Request.Builder().url(strArr[0]).post(RequestBody.create(MediaType.parse(strArr[1]), VerifyManager.c(this.a))).build()).enqueue(new e(this));
        if (b == null) {
            PointerHolder.b(new int[5]);
        }
    }
}
