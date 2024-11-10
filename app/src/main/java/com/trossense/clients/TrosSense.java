package com.trossense.clients;

import android.app.Activity;
import android.content.Context;
import com.trossense.VerifyManager;
import com.trossense.bi;
import com.trossense.ch;
import com.trossense.cw;
import com.trossense.cx;
import com.trossense.dj;
import com.trossense.sdk.entity.type.EntityLocalPlayer;
import java.lang.invoke.MethodHandles;

/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* loaded from: classes3.dex */
public final class TrosSense {
    public static final TrosSense INSTANCE;
    private static final /* synthetic */ TrosSense[] f;
    private static int[] g;
    private static final long h = dj.a(4685274407554999766L, 3418596361967786819L, MethodHandles.lookup().lookupClass()).a(188003273974981L);
    public static boolean isInit;
    private bi a;
    public String accountPath;
    public Activity activity;
    private ch b;
    private cx c;
    public Context context;
    private cw d;
    private VerifyManager e;
    public boolean isEnglishLanguage = true;

    static {
        if (h() == null) {
            b(new int[2]);
        }
        INSTANCE = new TrosSense(a(99, a("\u0016`91\u0010v\u0011\u001a")), 0);
        f = g();
        isInit = false;
    }

    private TrosSense(String str, int i) {
    }

    private static String a(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 60;
                    break;
                case 1:
                    i2 = 77;
                    break;
                case 2:
                    i2 = 9;
                    break;
                case 3:
                    i2 = 6;
                    break;
                case 4:
                    i2 = 50;
                    break;
                case 5:
                    i2 = 91;
                    break;
                default:
                    i2 = 49;
                    break;
            }
            cArr[i3] = (char) (c ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    private static char[] a(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ '1');
        }
        return charArray;
    }

    public static void b(int[] iArr) {
        g = iArr;
    }

    private static /* synthetic */ TrosSense[] g() {
        return new TrosSense[]{INSTANCE};
    }

    public static int[] h() {
        return g;
    }

    public static TrosSense valueOf(String str) {
        return (TrosSense) Enum.valueOf(TrosSense.class, str);
    }

    public static TrosSense[] values() {
        return (TrosSense[]) f.clone();
    }

    public VerifyManager a() {
        return this.e;
    }

    public void b(short s, int i, short s2) {
        long j = (((s << 48) | ((i << 32) >>> 16)) | ((s2 << 48) >>> 48)) ^ h;
        long j2 = 86627491682991L ^ j;
        long j3 = 71851258104307L ^ j;
        int i2 = (int) (j3 >>> 48);
        int i3 = (int) ((j3 << 16) >>> 32);
        int i4 = (int) ((j3 << 48) >>> 48);
        long j4 = j ^ 106625984489878L;
        if (isInit) {
            return;
        }
        isInit = true;
        this.a = new bi((short) i2, i3, (short) i4);
        this.b = new ch(j2);
        this.d = new cw(j4);
        this.e = new VerifyManager();
    }

    public cw c() {
        return this.d;
    }

    public bi d() {
        return this.a;
    }

    public cx e() {
        return this.c;
    }

    public ch f() {
        return this.b;
    }

    public native EntityLocalPlayer getLocalPlayer();
}
