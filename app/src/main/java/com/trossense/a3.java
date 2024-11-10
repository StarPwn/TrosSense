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
public final class a3 implements ViewBinding {
    private static boolean g;
    private static final long h = dj.a(2658585930578692519L, 8577690924167758282L, MethodHandles.lookup().lookupClass()).a(145632424438622L);
    private static final String i;
    private final ConstraintLayout a;
    public final Button b;
    public final Button c;
    public final EditText d;
    public final EditText e;
    public final EditText f;

    static {
        if (c()) {
            b(true);
        }
        i = a(52, a("\u0019\u0016,{$Gct\r:y8@v1\u001b\u007f~$Lst\b6|%\tM\u0010E\u007f"));
    }

    private a3(ConstraintLayout constraintLayout, Button button, Button button2, EditText editText, EditText editText2, EditText editText3) {
        this.a = constraintLayout;
        this.b = button;
        this.c = button2;
        this.d = editText;
        this.e = editText2;
        this.f = editText3;
    }

    public static a3 a(long j, LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        long j2 = (j ^ h) ^ 60106748164256L;
        View inflate = layoutInflater.inflate(R.layout.activity_main, viewGroup, false);
        if (z) {
            viewGroup.addView(inflate);
        }
        return a(inflate, j2);
    }

    public static a3 a(LayoutInflater layoutInflater, long j) {
        return a((j ^ h) ^ 32032546060953L, layoutInflater, null, false);
    }

    /* JADX WARN: Code restructure failed: missing block: B:16:0x0033, code lost:            if (r0 != false) goto L17;     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x0064, code lost:            if (r0 != false) goto L37;     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static com.trossense.a3 a(android.view.View r10, long r11) {
        /*
            long r0 = com.trossense.a3.h
            long r11 = r11 ^ r0
            boolean r0 = c()
            int r1 = com.trossense.R.id.buttonLogin
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r10, r1)
            r5 = r2
            android.widget.Button r5 = (android.widget.Button) r5
            r2 = 0
            if (r5 != 0) goto L22
            int r4 = (r11 > r2 ? 1 : (r11 == r2 ? 0 : -1))
            if (r4 < 0) goto L1c
            if (r0 == 0) goto L7a
            r1 = 5
            goto L1d
        L1c:
            r1 = r0
        L1d:
            int[] r1 = new int[r1]
            com.trossense.sdk.PointerHolder.b(r1)
        L22:
            int r1 = com.trossense.R.id.buttonRegister
            android.view.View r4 = androidx.viewbinding.ViewBindings.findChildViewById(r10, r1)
            r6 = r4
            android.widget.Button r6 = (android.widget.Button) r6
            int r11 = (r11 > r2 ? 1 : (r11 == r2 ? 0 : -1))
            if (r11 <= 0) goto L3f
            if (r6 != 0) goto L38
            if (r11 < 0) goto L36
            if (r0 == 0) goto L7a
            goto L38
        L36:
            r12 = r0
            goto L3a
        L38:
            int r12 = com.trossense.R.id.editTextPassword
        L3a:
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r10, r12)
            goto L41
        L3f:
            r12 = r1
            r1 = r6
        L41:
            r7 = r1
            android.widget.EditText r7 = (android.widget.EditText) r7
            if (r11 <= 0) goto L5c
            if (r7 != 0) goto L51
            if (r11 <= 0) goto L4f
            if (r0 == 0) goto L4d
            goto L51
        L4d:
            r1 = r12
            goto L7a
        L4f:
            r12 = r0
            goto L53
        L51:
            int r12 = com.trossense.R.id.editTextQQ
        L53:
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r10, r12)
            android.widget.EditText r1 = (android.widget.EditText) r1
            r8 = r1
            r1 = r12
            goto L5e
        L5c:
            r1 = r12
            r8 = r7
        L5e:
            if (r11 < 0) goto L74
            if (r8 != 0) goto L69
            if (r11 < 0) goto L67
            if (r0 == 0) goto L7a
            goto L69
        L67:
            r11 = r0
            goto L6b
        L69:
            int r11 = com.trossense.R.id.editTextUsername
        L6b:
            android.view.View r12 = androidx.viewbinding.ViewBindings.findChildViewById(r10, r11)
            android.widget.EditText r12 = (android.widget.EditText) r12
            r1 = r11
            r9 = r12
            goto L75
        L74:
            r9 = r8
        L75:
            if (r9 != 0) goto L8e
            if (r0 == 0) goto L7a
            goto L8e
        L7a:
            android.content.res.Resources r10 = r10.getResources()
            java.lang.String r10 = r10.getResourceName(r1)
            java.lang.NullPointerException r11 = new java.lang.NullPointerException
            java.lang.String r12 = com.trossense.a3.i
            java.lang.String r10 = r12.concat(r10)
            r11.<init>(r10)
            throw r11
        L8e:
            com.trossense.a3 r11 = new com.trossense.a3
            r4 = r10
            androidx.constraintlayout.widget.ConstraintLayout r4 = (androidx.constraintlayout.widget.ConstraintLayout) r4
            r3 = r11
            r3.<init>(r4, r5, r6, r7, r8, r9)
            return r11
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.a3.a(android.view.View, long):com.trossense.a3");
    }

    private static String a(int i2, char[] cArr) {
        int i3;
        int length = cArr.length;
        for (int i4 = 0; length > i4; i4++) {
            char c = cArr[i4];
            switch (i4 % 7) {
                case 0:
                    i3 = 96;
                    break;
                case 1:
                    i3 = 75;
                    break;
                case 2:
                    i3 = 107;
                    break;
                case 3:
                    i3 = 60;
                    break;
                case 4:
                    i3 = 121;
                    break;
                case 5:
                    i3 = 29;
                    break;
                default:
                    i3 = 48;
                    break;
            }
            cArr[i4] = (char) (c ^ (i3 ^ i2));
        }
        return new String(cArr).intern();
    }

    private static char[] a(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ '0');
        }
        return charArray;
    }

    public static void b(boolean z) {
        g = z;
    }

    public static boolean b() {
        return g;
    }

    public static boolean c() {
        return !b();
    }

    @Override // androidx.viewbinding.ViewBinding
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public ConstraintLayout getRoot() {
        return this.a;
    }
}
