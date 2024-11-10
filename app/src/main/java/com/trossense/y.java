package com.trossense;

import android.graphics.Color;
import com.trossense.clients.TrosSense;
import kotlin.text.Typography;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public class y extends p {
    private static final String[] q;
    private ag o;
    final ag p;

    static {
        String[] strArr = new String[2];
        int length = "匒全込前\u0003\u001b`\u001f".length();
        int i = 0;
        char c = 4;
        int i2 = -1;
        while (true) {
            int i3 = i2 + 1;
            int i4 = c + i3;
            int i5 = i + 1;
            strArr[i] = a(25, a("匒全込前\u0003\u001b`\u001f".substring(i3, i4)));
            if (i4 >= length) {
                q = strArr;
                return;
            } else {
                i2 = i4;
                c = "匒全込前\u0003\u001b`\u001f".charAt(i4);
                i = i5;
            }
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public y(ag agVar, ag agVar2) {
        super(0.0f, 0.0f);
        this.p = agVar;
        this.o = agVar2;
    }

    private static String a(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 74;
                    break;
                case 1:
                    i2 = 28;
                    break;
                case 2:
                    i2 = 126;
                    break;
                case 3:
                    i2 = 98;
                    break;
                case 4:
                    i2 = 45;
                    break;
                case 5:
                    i2 = 53;
                    break;
                default:
                    i2 = 60;
                    break;
            }
            cArr[i3] = (char) (c ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    private static char[] a(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ Typography.less);
        }
        return charArray;
    }

    @Override // com.trossense.p
    public void d(long j) {
        long j2 = j ^ 111589528524585L;
        long j3 = j ^ 121281231864540L;
        long j4 = j ^ 76134438077603L;
        int b = (int) (this.o.A.b() * 255.0f * (1.0f - this.o.B.b()));
        ag.o().a(TrosSense.INSTANCE.isEnglishLanguage ? q[1] : q[0], j3, this.a + 20.0f, this.b, this.e, 0.25f, Color.argb(b, 255, 255, 255));
        da.a(((this.a + this.d) - 20.0f) - 200.0f, this.b, j2, 200.0f, this.e, 10.0f, Color.argb(b, 91, 91, 91));
        da.a(((this.a + this.d) - 17.0f) - 200.0f, this.b + 3.0f, j2, 194.0f, this.e - 6.0f, 7.0f, Color.argb(b, 45, 45, 45));
        ag.o().a(ag.b(this.p).n(j4), j3, (this.a + this.d) - 200.0f, this.b, this.e, 0.25f, Color.argb(b, 255, 255, 255));
    }
}
