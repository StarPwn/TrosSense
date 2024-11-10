package com.trossense;

import android.app.Activity;
import android.content.DialogInterface;
import android.widget.EditText;
import java.lang.invoke.MethodHandles;
import kotlin.text.Typography;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public class h implements DialogInterface.OnClickListener {
    private static final long e = dj.a(5225958555329263289L, 5035416361596311910L, MethodHandles.lookup().lookupClass()).a(244065920937688L);
    private static final String[] f;
    final EditText a;
    final EditText b;
    final Activity c;
    final g d;

    static {
        String[] strArr = new String[3];
        int length = "\u001dL6\ry8<A\u000blOw%=D\u0001qSz-&E\brMlB`\u0010Jm\u0011,pz\u001b\r斕数盆瘆弖;诤梵柝伢盹运兲\u0019\u0001]:\tlg\u007f\u0014Q,Fct{\u0014J1\u00187*f\u0001^oE".length();
        int i = 0;
        char c = Typography.dollar;
        int i2 = -1;
        while (true) {
            int i3 = i2 + 1;
            int i4 = c + i3;
            int i5 = i + 1;
            strArr[i] = a(93, a("\u001dL6\ry8<A\u000blOw%=D\u0001qSz-&E\brMlB`\u0010Jm\u0011,pz\u001b\r斕数盆瘆弖;诤梵柝伢盹运兲\u0019\u0001]:\tlg\u007f\u0014Q,Fct{\u0014J1\u00187*f\u0001^oE".substring(i3, i4)));
            if (i4 >= length) {
                f = strArr;
                return;
            } else {
                i2 = i4;
                c = "\u001dL6\ry8<A\u000blOw%=D\u0001qSz-&E\brMlB`\u0010Jm\u0011,pz\u001b\r斕数盆瘆弖;诤梵柝伢盹运兲\u0019\u0001]:\tlg\u007f\u0014Q,Fct{\u0014J1\u00187*f\u0001^oE".charAt(i4);
                i = i5;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public h(g gVar, EditText editText, EditText editText2, Activity activity) {
        this.d = gVar;
        this.a = editText;
        this.b = editText2;
        this.c = activity;
    }

    private static String a(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 40;
                    break;
                case 1:
                    i2 = 101;
                    break;
                case 2:
                    i2 = 31;
                    break;
                case 3:
                    i2 = 32;
                    break;
                case 4:
                    i2 = 30;
                    break;
                case 5:
                    i2 = 74;
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
        g.a(this.d, dialogInterface, false);
        String trim = this.a.getText().toString().trim();
        String trim2 = this.b.getText().toString().trim();
        if (trim.isEmpty() || trim2.isEmpty()) {
            g.a(this.d, f[1]);
            return;
        }
        String[] strArr = f;
        g.a(this.d).newCall(new Request.Builder().url(strArr[0]).post(RequestBody.create(MediaType.parse(strArr[2]), VerifyManager.a(trim, trim2))).build()).enqueue(new i(this, trim, trim2, dialogInterface));
    }
}
