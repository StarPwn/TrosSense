package com.trossense;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import java.lang.invoke.MethodHandles;

/* loaded from: classes3.dex */
public final class a2 implements ViewBinding {
    private static final long f = dj.a(-4449427213649496880L, -6316683275092908281L, MethodHandles.lookup().lookupClass()).a(66047511438282L);
    private static final String g = a(89, a("\u0018#H d\u0019-u8^\"x\u001e80.\u001b%d\u0012=u=R'eW\u0003\u0011p\u001b"));
    private final ConstraintLayout a;
    public final Button b;
    public final EditText c;
    public final EditText d;
    public final Button e;

    private a2(ConstraintLayout constraintLayout, Button button, EditText editText, EditText editText2, Button button2) {
        this.a = constraintLayout;
        this.b = button;
        this.c = editText;
        this.d = editText2;
        this.e = button2;
    }

    public static a2 a(LayoutInflater layoutInflater, long j) {
        return a(layoutInflater, null, (j ^ f) ^ 11771211831141L, false);
    }

    public static a2 a(LayoutInflater layoutInflater, ViewGroup viewGroup, long j, boolean z) {
        long j2 = (j ^ f) ^ 91069280599852L;
        View inflate = layoutInflater.inflate(R.layout.activity_buy, viewGroup, false);
        if (z) {
            viewGroup.addView(inflate);
        }
        return a(inflate, j2);
    }

    /* JADX WARN: Code restructure failed: missing block: B:14:0x002e, code lost:            if (r0 != false) goto L17;     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x0045, code lost:            if (r0 != false) goto L26;     */
    /* JADX WARN: Code restructure failed: missing block: B:5:0x0018, code lost:            if (r0 != false) goto L9;     */
    /* JADX WARN: Removed duplicated region for block: B:12:0x002a  */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0041  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x007f  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x0051  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x003e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static com.trossense.a2 a(android.view.View r9, long r10) {
        /*
            long r0 = com.trossense.a2.f
            long r10 = r10 ^ r0
            boolean r0 = com.trossense.a3.c()
            int r1 = com.trossense.R.id.buttonBuy
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r9, r1)
            r5 = r2
            android.widget.Button r5 = (android.widget.Button) r5
            r2 = 0
            if (r5 != 0) goto L1d
            int r4 = (r10 > r2 ? 1 : (r10 == r2 ? 0 : -1))
            if (r4 <= 0) goto L1b
            if (r0 == 0) goto L5c
            goto L1d
        L1b:
            r1 = r0
            goto L1f
        L1d:
            int r1 = com.trossense.R.id.editCard
        L1f:
            android.view.View r4 = androidx.viewbinding.ViewBindings.findChildViewById(r9, r1)
            r6 = r4
            android.widget.EditText r6 = (android.widget.EditText) r6
            int r10 = (r10 > r2 ? 1 : (r10 == r2 ? 0 : -1))
            if (r10 <= 0) goto L3e
            if (r6 != 0) goto L33
            if (r10 <= 0) goto L31
            if (r0 == 0) goto L5c
            goto L33
        L31:
            r11 = r0
            goto L35
        L33:
            int r11 = com.trossense.R.id.fixedTextUsername
        L35:
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r9, r11)
            android.widget.EditText r1 = (android.widget.EditText) r1
            r7 = r1
            r1 = r11
            goto L3f
        L3e:
            r7 = r6
        L3f:
            if (r10 <= 0) goto L51
            if (r7 != 0) goto L4a
            if (r10 <= 0) goto L48
            if (r0 == 0) goto L5c
            goto L4a
        L48:
            r11 = r0
            goto L4c
        L4a:
            int r11 = com.trossense.R.id.query
        L4c:
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r9, r11)
            goto L53
        L51:
            r11 = r1
            r1 = r7
        L53:
            r8 = r1
            android.widget.Button r8 = (android.widget.Button) r8
            if (r8 != 0) goto L70
            if (r0 == 0) goto L5b
            goto L70
        L5b:
            r1 = r11
        L5c:
            android.content.res.Resources r9 = r9.getResources()
            java.lang.String r9 = r9.getResourceName(r1)
            java.lang.NullPointerException r10 = new java.lang.NullPointerException
            java.lang.String r11 = com.trossense.a2.g
            java.lang.String r9 = r11.concat(r9)
            r10.<init>(r9)
            throw r10
        L70:
            com.trossense.a2 r11 = new com.trossense.a2
            r4 = r9
            androidx.constraintlayout.widget.ConstraintLayout r4 = (androidx.constraintlayout.widget.ConstraintLayout) r4
            r3 = r11
            r3.<init>(r4, r5, r6, r7, r8)
            int[] r9 = com.trossense.sdk.PointerHolder.s()
            if (r9 != 0) goto L8b
            if (r10 < 0) goto L87
            if (r0 == 0) goto L85
            r0 = 0
            goto L87
        L85:
            r9 = 1
            goto L88
        L87:
            r9 = r0
        L88:
            com.trossense.a3.b(r9)
        L8b:
            return r11
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.a2.a(android.view.View, long):com.trossense.a2");
    }

    private static String a(int i, char[] cArr) {
        int length = cArr.length;
        for (int i2 = 0; length > i2; i2++) {
            char c = cArr[i2];
            int i3 = 19;
            switch (i2 % 7) {
                case 0:
                    i3 = 12;
                    break;
                case 2:
                    i3 = 98;
                    break;
                case 3:
                    i3 = 10;
                    break;
                case 4:
                    i3 = 84;
                    break;
                case 5:
                    i3 = 46;
                    break;
            }
            cArr[i2] = (char) (c ^ (i ^ i3));
        }
        return new String(cArr).intern();
    }

    private static char[] a(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ 19);
        }
        return charArray;
    }

    @Override // androidx.viewbinding.ViewBinding
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public ConstraintLayout getRoot() {
        return this.a;
    }
}
