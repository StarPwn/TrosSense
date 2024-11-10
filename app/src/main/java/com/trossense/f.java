package com.trossense;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import com.trossense.clients.TrosSense;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.BooleanSupplier;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import org.jose4j.jwk.RsaJsonWebKey;

/* loaded from: classes3.dex */
public class f implements GLSurfaceView.Renderer {
    public static float a = 0.0f;
    public static float b = 0.0f;
    private static f d = null;
    public static final float g = 1920.0f;
    public static boolean h = false;
    public static final float i = 1080.0f;
    private static final int r = 6;
    private static az s;
    private static int t;
    private static final long v;
    private static final String[] w;
    private x c;
    private List<p> e;
    private df f;
    public p j;
    public p k;
    public p l;
    public p m;
    public p n;
    public p o;
    private b9 p;
    private b_ q;
    private long u;

    /* JADX WARN: Failed to find 'out' block for switch in B:4:0x004e. Please report as an issue. */
    static {
        int i2;
        int i3;
        long a2 = dj.a(6141699173357831221L, -1670879737225123937L, MethodHandles.lookup().lookupClass()).a(62374281181575L);
        v = a2;
        long j = (a2 ^ 131463747122235L) ^ 119381232835221L;
        String[] strArr = new String[9];
        String str = "dC\fi\u0006{O\u0011nlQ\bdE\todF}]\u0013hi+CFmLye6D]fAvn0]G\u0011hi+CFmLye6D]fAv\u007f/\u0005~E\rfm\u0006jE\u0012hhW";
        int length = "dC\fi\u0006{O\u0011nlQ\bdE\todF}]\u0013hi+CFmLye6D]fAvn0]G\u0011hi+CFmLye6D]fAv\u007f/\u0005~E\rfm\u0006jE\u0012hhW".length();
        char c = 4;
        int i4 = -1;
        int i5 = 0;
        while (true) {
            int i6 = i4 + 1;
            String substring = str.substring(i6, i6 + c);
            boolean z = -1;
            int i7 = 1;
            while (true) {
                String a3 = a(i7, a(substring));
                switch (z) {
                    case false:
                        break;
                    default:
                        i2 = i5 + 1;
                        strArr[i5] = a3;
                        i4 = i6 + c;
                        if (i4 < length) {
                            break;
                        }
                        str = "Ze=POr\bcj3G\u0004tDl";
                        length = "Ze=POr\bcj3G\u0004tDl".length();
                        c = 6;
                        i3 = -1;
                        i5 = i2;
                        i7 = 34;
                        i6 = i3 + 1;
                        substring = str.substring(i6, i6 + c);
                        z = false;
                        break;
                }
                i2 = i5 + 1;
                strArr[i5] = a3;
                i3 = i6 + c;
                if (i3 >= length) {
                    w = strArr;
                    h = false;
                    s = a1.a(strArr[8], 40, j);
                    t = 0;
                    return;
                }
                c = str.charAt(i3);
                i5 = i2;
                i7 = 34;
                i6 = i3 + 1;
                substring = str.substring(i6, i6 + c);
                z = false;
            }
            c = str.charAt(i4);
            i5 = i2;
        }
    }

    public f(long j, float f, float f2) {
        long j2 = v ^ j;
        long j3 = 65494389607665L ^ j2;
        long j4 = 1002414490381L ^ j2;
        long j5 = j2 ^ 138915388151197L;
        this.f = new df(d.Decelerate, 250L, j2 ^ 10083234099832L);
        d = this;
        a = f;
        b = f2;
        this.e = new ArrayList();
        this.c = new x(0.0f, j2 ^ 35465923136378L, 0.0f, 100.0f, 100.0f);
        List<p> list = this.e;
        String[] strArr = w;
        p a2 = new ad(j3, 100.0f, 100.0f, strArr[6], "c", b.Combat).a(new BooleanSupplier() { // from class: com.trossense.f$$ExternalSyntheticLambda0
            @Override // java.util.function.BooleanSupplier
            public final boolean getAsBoolean() {
                return f.this.m203lambda$new$0$comtrossensef();
            }
        });
        this.j = a2;
        list.add(a2);
        List<p> list2 = this.e;
        p a3 = new ad(j3, 300.0f, 100.0f, strArr[2], "f", b.Movement).a(new BooleanSupplier() { // from class: com.trossense.f$$ExternalSyntheticLambda1
            @Override // java.util.function.BooleanSupplier
            public final boolean getAsBoolean() {
                return f.this.m204lambda$new$1$comtrossensef();
            }
        });
        this.k = a3;
        list2.add(a3);
        List<p> list3 = this.e;
        p a4 = new ad(j3, 600.0f, 100.0f, strArr[0], "b", b.Misc).a(new BooleanSupplier() { // from class: com.trossense.f$$ExternalSyntheticLambda2
            @Override // java.util.function.BooleanSupplier
            public final boolean getAsBoolean() {
                return f.this.m205lambda$new$2$comtrossensef();
            }
        });
        this.l = a4;
        list3.add(a4);
        List<p> list4 = this.e;
        p a5 = new ad(j3, 900.0f, 100.0f, strArr[7], RsaJsonWebKey.EXPONENT_MEMBER_NAME, b.Player).a(new BooleanSupplier() { // from class: com.trossense.f$$ExternalSyntheticLambda3
            @Override // java.util.function.BooleanSupplier
            public final boolean getAsBoolean() {
                return f.this.m206lambda$new$3$comtrossensef();
            }
        });
        this.m = a5;
        list4.add(a5);
        List<p> list5 = this.e;
        p a6 = new ad(j3, 1200.0f, 100.0f, strArr[1], "d", b.Render).a(new BooleanSupplier() { // from class: com.trossense.f$$ExternalSyntheticLambda4
            @Override // java.util.function.BooleanSupplier
            public final boolean getAsBoolean() {
                return f.this.m207lambda$new$4$comtrossensef();
            }
        });
        this.n = a6;
        list5.add(a6);
        List<p> list6 = this.e;
        p a7 = new ad(j3, 1500.0f, 100.0f, strArr[5], "a", b.World).a(new BooleanSupplier() { // from class: com.trossense.f$$ExternalSyntheticLambda5
            @Override // java.util.function.BooleanSupplier
            public final boolean getAsBoolean() {
                return f.this.m208lambda$new$5$comtrossensef();
            }
        });
        this.o = a7;
        list6.add(a7);
        this.p = (b9) TrosSense.INSTANCE.f().a(b9.class);
        this.q = (b_) TrosSense.INSTANCE.f().a(b_.class);
        this.e.add(this.p.n());
        this.e.add(this.q.n());
        this.e.add(this.c);
        this.e.add(new al((int) (j4 >>> 32), (byte) ((j4 << 32) >>> 56), (int) ((j4 << 40) >>> 40)));
        TrosSense.INSTANCE.c().c(j5);
        this.u = System.currentTimeMillis();
    }

    private static String a(int i2, char[] cArr) {
        int i3;
        int length = cArr.length;
        for (int i4 = 0; length > i4; i4++) {
            char c = cArr[i4];
            switch (i4 % 7) {
                case 0:
                    i3 = 40;
                    break;
                case 1:
                    i3 = 43;
                    break;
                case 2:
                    i3 = 126;
                    break;
                case 3:
                    i3 = 11;
                    break;
                case 4:
                    i3 = 8;
                    break;
                case 5:
                    i3 = 34;
                    break;
                default:
                    i3 = 18;
                    break;
            }
            cArr[i4] = (char) (c ^ (i3 ^ i2));
        }
        return new String(cArr).intern();
    }

    /*  JADX ERROR: JadxOverflowException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxOverflowException: Regions count limit reached
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:59)
        	at jadx.core.utils.ErrorsCounter.error(ErrorsCounter.java:31)
        	at jadx.core.dex.attributes.nodes.NotificationAttrNode.addError(NotificationAttrNode.java:19)
        */
    /* JADX WARN: Failed to find 'out' block for switch in B:15:0x004e. Please report as an issue. */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:102:? A[LOOP:3: B:82:0x014a->B:102:?, LOOP_END, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:123:0x0162 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:89:0x019a  */
    /* JADX WARN: Type inference failed for: r12v13, types: [boolean] */
    /* JADX WARN: Type inference failed for: r12v22 */
    /* JADX WARN: Type inference failed for: r12v23, types: [int] */
    /* JADX WARN: Type inference failed for: r12v25 */
    /* JADX WARN: Type inference failed for: r13v13, types: [boolean] */
    /* JADX WARN: Type inference failed for: r13v6, types: [boolean] */
    /* JADX WARN: Type inference failed for: r18v0 */
    /* JADX WARN: Type inference failed for: r18v1 */
    /* JADX WARN: Type inference failed for: r18v2 */
    /* JADX WARN: Type inference failed for: r18v3 */
    /* JADX WARN: Type inference failed for: r18v4 */
    /* JADX WARN: Type inference failed for: r18v5 */
    /* JADX WARN: Type inference failed for: r18v7 */
    /* JADX WARN: Type inference failed for: r19v3, types: [java.util.Iterator] */
    /* JADX WARN: Type inference failed for: r1v14, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r4v3, types: [boolean] */
    /* JADX WARN: Type inference failed for: r8v13, types: [boolean] */
    /* JADX WARN: Type inference failed for: r9v25, types: [java.util.List] */
    /* JADX WARN: Type inference failed for: r9v26, types: [java.util.List] */
    /* JADX WARN: Type inference failed for: r9v27, types: [java.util.List] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:160:0x0339 -> B:155:0x033c). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:164:0x02f4 -> B:150:0x02f7). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:171:0x0250 -> B:106:0x0256). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:34:0x00ae -> B:35:0x00b1). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:39:0x007f -> B:24:0x0083). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:57:0x012a -> B:53:0x012d). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:64:0x00e6 -> B:47:0x00ea). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:98:0x0160 -> B:71:0x018e). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static boolean a(int r22, char r23, float r24, long r25, float r27, int r28) {
        /*
            Method dump skipped, instructions count: 862
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.f.a(int, char, float, long, float, int):boolean");
    }

    private static char[] a(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ 18);
        }
        return charArray;
    }

    public static f b() {
        return d;
    }

    public x a() {
        return this.c;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-trossense-f, reason: not valid java name */
    public boolean m203lambda$new$0$comtrossensef() {
        return !this.c.n() && this.f.d();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$1$com-trossense-f, reason: not valid java name */
    public boolean m204lambda$new$1$comtrossensef() {
        return !this.c.n() && this.f.d();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$2$com-trossense-f, reason: not valid java name */
    public boolean m205lambda$new$2$comtrossensef() {
        return !this.c.n() && this.f.d();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$3$com-trossense-f, reason: not valid java name */
    public boolean m206lambda$new$3$comtrossensef() {
        return !this.c.n() && this.f.d();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$4$com-trossense-f, reason: not valid java name */
    public boolean m207lambda$new$4$comtrossensef() {
        return !this.c.n() && this.f.d();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$5$com-trossense-f, reason: not valid java name */
    public boolean m208lambda$new$5$comtrossensef() {
        return !this.c.n() && this.f.d();
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onDrawFrame(GL10 gl10) {
        long j;
        long j2 = v ^ 88967436464192L;
        long j3 = 116786420591786L ^ j2;
        long j4 = 117499973871459L ^ j2;
        long j5 = 58042060323982L ^ j2;
        long j6 = j5 >>> 16;
        int i2 = (int) ((j5 << 48) >>> 48);
        long j7 = 103076355068961L ^ j2;
        long j8 = 128592674005414L ^ j2;
        long j9 = j6;
        int i3 = (int) (j8 >>> 56);
        int i4 = (int) ((j8 << 8) >>> 32);
        long j10 = 11082700108335L ^ j2;
        int i5 = (int) ((j8 << 40) >>> 40);
        long j11 = 93245724065057L ^ j2;
        long j12 = j2 ^ 40567308284242L;
        if (VerifyManager.c && h) {
            GLES20.glClear(16640);
            if (System.currentTimeMillis() - this.u > 30000) {
                TrosSense.INSTANCE.c().b((byte) i3, i4, i5);
                this.u = System.currentTimeMillis();
            }
            float f = a / 1920.0f;
            float f2 = b / 1080.0f;
            c_.a(j3);
            c_.b(f, j7, f2, 0.0f);
            this.f.a(this.c.n() ? 1.0f : 0.0f, j4);
            int i6 = 0;
            while (i6 < this.e.size()) {
                if (i6 == 0) {
                    da.a(960.0f, 540.0f, j11, this.f.b());
                }
                long j13 = j10;
                this.e.get(i6).b(j13);
                if (i6 == 5) {
                    j = j9;
                    da.c(j, (short) i2);
                } else {
                    j = j9;
                }
                i6++;
                j10 = j13;
                j9 = j;
            }
            long j14 = j10;
            Iterator<bm> it2 = TrosSense.INSTANCE.f().c().iterator();
            while (it2.hasNext()) {
                it2.next().a().b(j14);
            }
            c_.c(j12);
        }
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onSurfaceChanged(GL10 gl10, int i2, int i3) {
        long j = v ^ 4306843003975L;
        GLES20.glViewport(0, 0, i2, i3);
        c_.a(0);
        c_.d(j ^ 81438150570337L);
        float f = i2;
        float f2 = i3;
        c_.a(0.0f, f, (short) (r4 >>> 48), ((63913091673767L ^ j) << 16) >>> 16, f2, 0.0f, -100.0f, 100.0f);
        c_.a(1);
        b = f2;
        a = f;
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    }
}
