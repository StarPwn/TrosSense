package com.trossense;

import android.app.Activity;
import android.content.DialogInterface;
import android.widget.EditText;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public class n implements DialogInterface.OnClickListener {
    private static final String[] d;
    final EditText a;
    final Activity b;
    final g c;

    static {
        String[] strArr = new String[2];
        int length = "0\u001c\b)\t2\u0001%\u0010\u001ef\u0006!\u0005%\u000b\u00038R\u007f\u00180\u001f]e$,\r\u0004-\u001cmBpJ^o\u0012pCu@Cs\u001fxXtI@m\t\u0017\u001e!\u000b_,S'\u001f=".length();
        int i = 0;
        char c = 25;
        int i2 = -1;
        while (true) {
            int i3 = i2 + 1;
            int i4 = c + i3;
            int i5 = i + 1;
            strArr[i] = a(35, a("0\u001c\b)\t2\u0001%\u0010\u001ef\u0006!\u0005%\u000b\u00038R\u007f\u00180\u001f]e$,\r\u0004-\u001cmBpJ^o\u0012pCu@Cs\u001fxXtI@m\t\u0017\u001e!\u000b_,S'\u001f=".substring(i3, i4)));
            if (i4 >= length) {
                d = strArr;
                return;
            } else {
                i2 = i4;
                c = "0\u001c\b)\t2\u0001%\u0010\u001ef\u0006!\u0005%\u000b\u00038R\u007f\u00180\u001f]e$,\r\u0004-\u001cmBpJ^o\u0012pCu@Cs\u001fxXtI@m\t\u0017\u001e!\u000b_,S'\u001f=".charAt(i4);
                i = i5;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public n(g gVar, EditText editText, Activity activity) {
        this.c = gVar;
        this.a = editText;
        this.b = activity;
    }

    private static String a(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 103;
                    break;
                case 1:
                    i2 = 90;
                    break;
                case 2:
                    i2 = 83;
                    break;
                case 3:
                    i2 = 126;
                    break;
                case 4:
                    i2 = 5;
                    break;
                case 5:
                    i2 = 97;
                    break;
                default:
                    i2 = 78;
                    break;
            }
            cArr[i3] = (char) (c ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    private static char[] a(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ 'N');
        }
        return charArray;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public void onClick(DialogInterface dialogInterface, int i) {
        g.a(this.c, dialogInterface, false);
        String trim = this.a.getText().toString().trim();
        String[] strArr = d;
        g.a(this.c).newCall(new Request.Builder().url(strArr[1]).post(RequestBody.create(MediaType.parse(strArr[0]), VerifyManager.c(trim))).build()).enqueue(new o(this, dialogInterface, trim));
    }
}
