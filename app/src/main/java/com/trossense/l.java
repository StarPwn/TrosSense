package com.trossense;

import android.content.DialogInterface;
import android.widget.EditText;
import com.trossense.sdk.PointerHolder;
import java.lang.invoke.MethodHandles;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public class l implements DialogInterface.OnClickListener {
    private static final long d = dj.a(-3692765332830296989L, 7871416207920742877L, MethodHandles.lookup().lookupClass()).a(83957991012162L);
    private static final String[] e;
    final EditText a;
    final EditText b;
    final g c;

    static {
        String[] strArr = new String[3];
        int length = "N.CF$@7[\"U\t+S3[9HW\u007f\r.N-\u0016\n(R?OB1\u001ft\u000ex\u0015\u0000?\u0002u\u000br\b\u001c2\nn\n{\u000b\u0002$e(_9\u0014gxY5]\bZ@o\t词欨硕辡兮悘盟卛宍".length();
        int i = 0;
        char c = 25;
        int i2 = -1;
        while (true) {
            int i3 = i2 + 1;
            int i4 = c + i3;
            int i5 = i + 1;
            strArr[i] = a(39, a("N.CF$@7[\"U\t+S3[9HW\u007f\r.N-\u0016\n(R?OB1\u001ft\u000ex\u0015\u0000?\u0002u\u000br\b\u001c2\nn\n{\u000b\u0002$e(_9\u0014gxY5]\bZ@o\t词欨硕辡兮悘盟卛宍".substring(i3, i4)));
            if (i4 >= length) {
                e = strArr;
                return;
            } else {
                i2 = i4;
                c = "N.CF$@7[\"U\t+S3[9HW\u007f\r.N-\u0016\n(R?OB1\u001ft\u000ex\u0015\u0000?\u0002u\u000br\b\u001c2\nn\n{\u000b\u0002$e(_9\u0014gxY5]\bZ@o\t词欨硕辡兮悘盟卛宍".charAt(i4);
                i = i5;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public l(g gVar, EditText editText, EditText editText2) {
        this.c = gVar;
        this.a = editText;
        this.b = editText2;
    }

    private static String a(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 29;
                    break;
                case 1:
                    i2 = 108;
                    break;
                case 2:
                    i2 = 28;
                    break;
                case 3:
                    i2 = 21;
                    break;
                case 4:
                    i2 = 44;
                    break;
                case 5:
                    i2 = 23;
                    break;
                default:
                    i2 = 124;
                    break;
            }
            cArr[i3] = (char) (c ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    private static char[] a(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ '|');
        }
        return charArray;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public void onClick(DialogInterface dialogInterface, int i) {
        boolean b = g.b();
        g.a(this.c, dialogInterface, false);
        String trim = this.a.getText().toString().trim();
        String trim2 = this.b.getText().toString().trim();
        if (trim.isEmpty()) {
            g.a(this.c, e[2]);
        }
        String[] strArr = e;
        g.a(this.c).newCall(new Request.Builder().url(strArr[1]).post(RequestBody.create(MediaType.parse(strArr[0]), VerifyManager.d(trim2, trim))).build()).enqueue(new m(this));
        if (PointerHolder.s() == null) {
            g.b(!b);
        }
    }
}
