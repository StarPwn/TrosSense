package com.trossense;

import com.trossense.clients.TrosSense;
import com.trossense.sdk.PointerHolder;
import com.trossense.sdk.math.Vec2f;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import kotlin.text.Typography;

/* loaded from: classes3.dex */
public class bm {
    private static final long C = dj.a(-3287272372495988338L, 5379634824849533412L, MethodHandles.lookup().lookupClass()).a(137277188580939L);
    private static final String[] E;
    private static String[] i;
    public df a;
    private String b;
    private int c;
    private String d;
    protected List<co> e;
    protected boolean f;
    private b g;
    ak h;

    /* JADX WARN: Failed to find 'out' block for switch in B:4:0x004b. Please report as an issue. */
    static {
        int i2;
        int i3;
        String[] strArr = new String[15];
        String str = "\u0018裟彋呌亐\u0004袓儇閦以\u0007s\u00112!\u007fq\u0004\u0006\u009f\u0017ì\u000f樷坈\r\u0018\u0003*\u00106{\tK\u0015)\u000fs{#u\u001b/\u0016zz)V\u0012$Cwq\u000eW\u0000*\u0017\u007fp\u000e\u0018\u001a$\u00176y\u000fM\u001a/Cyq@\u000eu\u001b/\u0016zz@L\u001b,\u0004zz\u0004\u0004標圣儸閎\u0003忓挃镥\u0006\u009f\u0016ì\u000f樷坈\u0005\u0018裟儸閎亐\f\u0018\u0003*\u00106z\u000eY\u0016'\u0006r\u000eu\u001b/\u0016zz@L\u001b,\u0004zz\u0004";
        int length = "\u0018裟彋呌亐\u0004袓儇閦以\u0007s\u00112!\u007fq\u0004\u0006\u009f\u0017ì\u000f樷坈\r\u0018\u0003*\u00106{\tK\u0015)\u000fs{#u\u001b/\u0016zz)V\u0012$Cwq\u000eW\u0000*\u0017\u007fp\u000e\u0018\u001a$\u00176y\u000fM\u001a/Cyq@\u000eu\u001b/\u0016zz@L\u001b,\u0004zz\u0004\u0004標圣儸閎\u0003忓挃镥\u0006\u009f\u0016ì\u000f樷坈\u0005\u0018裟儸閎亐\f\u0018\u0003*\u00106z\u000eY\u0016'\u0006r\u000eu\u001b/\u0016zz@L\u001b,\u0004zz\u0004".length();
        b(new String[3]);
        char c = 5;
        int i4 = 0;
        int i5 = -1;
        while (true) {
            int i6 = 94;
            int i7 = i5 + 1;
            String substring = str.substring(i7, i7 + c);
            boolean z = -1;
            while (true) {
                String a = a(i6, a(substring));
                i2 = i4 + 1;
                switch (z) {
                    case false:
                        break;
                    default:
                        strArr[i4] = a;
                        i5 = i7 + c;
                        if (i5 < length) {
                            break;
                        }
                        str = "裐強吧亦\u0004橚坠弈吏";
                        length = "裐強吧亦\u0004橚坠弈吏".length();
                        c = 4;
                        i3 = -1;
                        i4 = i2;
                        i6 = 29;
                        i7 = i3 + 1;
                        substring = str.substring(i7, i7 + c);
                        z = false;
                        break;
                }
                strArr[i4] = a;
                i3 = i7 + c;
                if (i3 >= length) {
                    E = strArr;
                    return;
                }
                c = str.charAt(i3);
                i4 = i2;
                i6 = 29;
                i7 = i3 + 1;
                substring = str.substring(i7, i7 + c);
                z = false;
            }
            c = str.charAt(i5);
            i4 = i2;
        }
    }

    public bm(long j) {
        long j2 = C ^ j;
        long j3 = 137870823639139L ^ j2;
        long j4 = j3 >>> 16;
        int i2 = (int) ((j3 << 48) >>> 48);
        long j5 = 37291099925339L ^ j2;
        String[] m = m();
        this.a = new df(d.Decelerate, 250L, j2 ^ 14854496987613L);
        this.e = new ArrayList();
        if (getClass().isAnnotationPresent(cg.class)) {
            cg cgVar = (cg) getClass().getAnnotation(cg.class);
            this.b = cgVar.a();
            this.d = cgVar.c();
            this.g = cgVar.b();
            this.c = cgVar.d();
            if (j2 > 0) {
                if (m != null) {
                    this.h = new ak(0.0f, j5, 0.0f, this);
                } else {
                    PointerHolder.b(new int[1]);
                }
            }
            String[] strArr = E;
            final cp cpVar = new cp(j4, (short) i2, strArr[2], strArr[8], this, false);
            this.h.a(new BooleanSupplier() { // from class: com.trossense.bm$$ExternalSyntheticLambda0
                @Override // java.util.function.BooleanSupplier
                public final boolean getAsBoolean() {
                    return bm.lambda$new$0(cp.this);
                }
            });
            return;
        }
        throw new RuntimeException(E[5] + getClass().getSimpleName());
    }

    private static String a(int i2, char[] cArr) {
        int i3;
        int length = cArr.length;
        for (int i4 = 0; length > i4; i4++) {
            char c = cArr[i4];
            switch (i4 % 7) {
                case 0:
                    i3 = 102;
                    break;
                case 1:
                    i3 = 42;
                    break;
                case 2:
                    i3 = 21;
                    break;
                case 3:
                    i3 = 61;
                    break;
                case 4:
                    i3 = 72;
                    break;
                case 5:
                    i3 = 65;
                    break;
                default:
                    i3 = 62;
                    break;
            }
            cArr[i4] = (char) (c ^ (i3 ^ i2));
        }
        return new String(cArr).intern();
    }

    private static char[] a(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ Typography.greater);
        }
        return charArray;
    }

    public static void b(String[] strArr) {
        i = strArr;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean lambda$new$0(cp cpVar) {
        return !cpVar.e().booleanValue();
    }

    public static String[] m() {
        return i;
    }

    public ak a() {
        return this.h;
    }

    public void a(char c, int i2, char c2, boolean z) {
        long j = ((((c2 << 48) >>> 48) | ((c << 48) | ((i2 << 32) >>> 16))) ^ C) ^ 4877016085513L;
        if (this.f != z) {
            g(j);
        }
    }

    public void a(long j, Vec2f vec2f) {
    }

    public final void a(short s, int i2, short s2, int i3) {
        long j = ((((s2 << 48) >>> 48) | ((s << 48) | ((i2 << 32) >>> 16))) ^ C) ^ 18298819838331L;
        if (i3 != 0 && i3 == this.c) {
            g(j);
        }
    }

    public List<co> b() {
        return this.e;
    }

    public b c() {
        return this.g;
    }

    public String d(long j) {
        return TrosSense.INSTANCE.isEnglishLanguage ? this.b : this.d;
    }

    public String e() {
        return this.b;
    }

    public Vec2f f() {
        return null;
    }

    /* JADX WARN: Removed duplicated region for block: B:30:0x0129  */
    /* JADX WARN: Removed duplicated region for block: B:33:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final void g(long r22) {
        /*
            Method dump skipped, instructions count: 304
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.bm.g(long):void");
    }

    public String h() {
        return "";
    }

    public boolean i(long j) {
        return !h().isEmpty();
    }

    public void j(long j) {
    }

    public void k(long j) {
    }

    public boolean l() {
        return this.f;
    }
}
