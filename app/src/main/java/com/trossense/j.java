package com.trossense;

import android.app.Activity;
import android.content.DialogInterface;
import android.widget.EditText;
import java.lang.invoke.MethodHandles;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public class j implements DialogInterface.OnClickListener {
    private static final long f = dj.a(1853304027722016504L, -5672750304957408887L, MethodHandles.lookup().lookupClass()).a(92407372713383L);
    private static final String[] g;
    final EditText a;
    final EditText b;
    final EditText c;
    final Activity d;
    final g e;

    static {
        String[] strArr = new String[3];
        int length = "f\u0004\u0012vQ=|s\b\u00049^.xs\u0013\u0019g\npef\u0007G:\r旲攩目泪凲a诧棒构伊皆迭儨'z\u0015\u001erDb?&RD0J\u007f>#XY,Gw%\"QZ2Q\u0018cw\u0013Ep\u001b*ya\u0015\u000fp".length();
        int i = 0;
        char c = 25;
        int i2 = -1;
        while (true) {
            int i3 = i2 + 1;
            int i4 = c + i3;
            int i5 = i + 1;
            strArr[i] = a(22, a("f\u0004\u0012vQ=|s\b\u00049^.xs\u0013\u0019g\npef\u0007G:\r旲攩目泪凲a诧棒构伊皆迭儨'z\u0015\u001erDb?&RD0J\u007f>#XY,Gw%\"QZ2Q\u0018cw\u0013Ep\u001b*ya\u0015\u000fp".substring(i3, i4)));
            if (i4 >= length) {
                g = strArr;
                return;
            } else {
                i2 = i4;
                c = "f\u0004\u0012vQ=|s\b\u00049^.xs\u0013\u0019g\npef\u0007G:\r旲攩目泪凲a诧棒构伊皆迭儨'z\u0015\u001erDb?&RD0J\u007f>#XY,Gw%\"QZ2Q\u0018cw\u0013Ep\u001b*ya\u0015\u000fp".charAt(i4);
                i = i5;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public j(g gVar, EditText editText, EditText editText2, EditText editText3, Activity activity) {
        this.e = gVar;
        this.a = editText;
        this.b = editText2;
        this.c = editText3;
        this.d = activity;
    }

    private static String a(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 4;
                    break;
                case 1:
                    i2 = 119;
                    break;
                case 2:
                    i2 = 124;
                    break;
                case 3:
                    i2 = 20;
                    break;
                case 4:
                    i2 = 104;
                    break;
                case 5:
                    i2 = 91;
                    break;
                default:
                    i2 = 6;
                    break;
            }
            cArr[i3] = (char) (c ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    private static char[] a(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ 6);
        }
        return charArray;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public void onClick(DialogInterface dialogInterface, int i) {
        g.a(this.e, dialogInterface, false);
        String trim = this.a.getText().toString().trim();
        String trim2 = this.b.getText().toString().trim();
        String trim3 = this.c.getText().toString().trim();
        if (trim.isEmpty() || trim2.isEmpty() || trim3.isEmpty()) {
            g.a(this.e, g[1]);
            return;
        }
        String[] strArr = g;
        g.a(this.e).newCall(new Request.Builder().url(strArr[2]).post(RequestBody.create(MediaType.parse(strArr[0]), VerifyManager.b(trim, trim2, trim3))).build()).enqueue(new k(this, trim, trim2, dialogInterface));
    }
}
