package com.trossense;

import com.trossense.sdk.math.Vec2f;
import java.lang.invoke.MethodHandles;

@cg(a = "ArrayList", b = b.Render, c = "功能列表")
/* loaded from: classes3.dex */
public class b9 extends bm {
    private static az j;
    private static final long w;
    private static final String[] x;
    private final cr k;
    private final ct l;
    private final ct m;
    private final ct n;
    private final cp o;
    private final ct p;
    private final cr q;
    private final cr r;
    private final cq s;
    private final ct t;
    private final cr u;
    private am v;

    /* JADX WARN: Failed to find 'out' block for switch in B:4:0x0051. Please report as an issue. */
    static {
        int i;
        int i2;
        long a = dj.a(1033078281267309895L, -6745750993693598678L, MethodHandles.lookup().lookupClass()).a(138020794587913L);
        w = a;
        long j2 = (a ^ 38294691853014L) ^ 85802836077188L;
        String[] strArr = new String[51];
        String str = "勚甆\u0007?R4o_b\n\u000b&X:~_C\u0001\u001bZ*~\u0007 \\+d\u001dd\u0013\u0002禉迦\t3S+g\u001e\u007f\r\u001dS\u0006!I#~\u0016h\u00051R.e\r\u0002嶔辄\u0007?R4o_b\n\u0005湢句餳咆廙\u0004湢句剄艸\u0005哾斺嬕盲味\u0002罛迦\u0003湢句盆\u0004旵孪陶彻\u0003霫怼盆\u0004旵孪肎晥\u0004湢句遝庬\n0\\!a\u0018y\u000b\u0007S&\t X!~\u001ee\u0003\u001eX\u0004旵孪瞫彨\u0002弛虄\b5O#n\u0016n\n\u0006\u0002頄竒\u0007=H6f\u0016e\u0001\u0004旵孪橣弅\u000b1R.e\r+7\u0002X'n\f!\\/o__\u000bRi'r\u000b\u0003&R2\b!^#f\u001a+\r\u001c\u00061H1~\u0010f\f\u0006X,k\u001cb\u0010\u000b\u00136~\u0019\u0004>X$~\u0004旵孪骚庬\t&X:~_F\u000b\u0016X\u0007 \\+d\u001dd\u0013\u0003膘宧下\u0005肾晒遍昄廙\u0004肾晒飞艸\u0005 T%b\u000b\u0002厁辄\u000b&X:~_X\f\u0013Y-}\u00061H1~\u0010f\u00101R.e\r+7\u0017M#x\u001e\u007f\r\u001dS\u0006!I#~\u0016h\u0002迋桻\u00100\\!a\u0018y\u000b\u0007S&*>g\u0014\u001a\\\u0004<R,o";
        int length = "勚甆\u0007?R4o_b\n\u000b&X:~_C\u0001\u001bZ*~\u0007 \\+d\u001dd\u0013\u0002禉迦\t3S+g\u001e\u007f\r\u001dS\u0006!I#~\u0016h\u00051R.e\r\u0002嶔辄\u0007?R4o_b\n\u0005湢句餳咆廙\u0004湢句剄艸\u0005哾斺嬕盲味\u0002罛迦\u0003湢句盆\u0004旵孪陶彻\u0003霫怼盆\u0004旵孪肎晥\u0004湢句遝庬\n0\\!a\u0018y\u000b\u0007S&\t X!~\u001ee\u0003\u001eX\u0004旵孪瞫彨\u0002弛虄\b5O#n\u0016n\n\u0006\u0002頄竒\u0007=H6f\u0016e\u0001\u0004旵孪橣弅\u000b1R.e\r+7\u0002X'n\f!\\/o__\u000bRi'r\u000b\u0003&R2\b!^#f\u001a+\r\u001c\u00061H1~\u0010f\f\u0006X,k\u001cb\u0010\u000b\u00136~\u0019\u0004>X$~\u0004旵孪骚庬\t&X:~_F\u000b\u0016X\u0007 \\+d\u001dd\u0013\u0003膘宧下\u0005肾晒遍昄廙\u0004肾晒飞艸\u0005 T%b\u000b\u0002厁辄\u000b&X:~_X\f\u0013Y-}\u00061H1~\u0010f\u00101R.e\r+7\u0017M#x\u001e\u007f\r\u001dS\u0006!I#~\u0016h\u0002迋桻\u00100\\!a\u0018y\u000b\u0007S&*>g\u0014\u001a\\\u0004<R,o".length();
        char c = 2;
        int i3 = 0;
        int i4 = -1;
        while (true) {
            int i5 = 80;
            int i6 = i4 + 1;
            String substring = str.substring(i6, i6 + c);
            boolean z = -1;
            while (true) {
                String b = b(i5, b(substring));
                i = i3 + 1;
                switch (z) {
                    case false:
                        break;
                    default:
                        strArr[i3] = b;
                        i4 = i6 + c;
                        if (i4 < length) {
                            break;
                        }
                        str = "@.P\u0013\u0010M.R\u0019qWKo5K\u0004b\u0003qa/";
                        length = "@.P\u0013\u0010M.R\u0019qWKo5K\u0004b\u0003qa/".length();
                        c = 4;
                        i2 = -1;
                        i3 = i;
                        i5 = 44;
                        i6 = i2 + 1;
                        substring = str.substring(i6, i6 + c);
                        z = false;
                        break;
                }
                strArr[i3] = b;
                i2 = i6 + c;
                if (i2 >= length) {
                    x = strArr;
                    j = a1.a(strArr[32], 40, j2);
                    return;
                }
                c = str.charAt(i2);
                i3 = i;
                i5 = 44;
                i6 = i2 + 1;
                substring = str.substring(i6, i6 + c);
                z = false;
            }
            c = str.charAt(i4);
            i3 = i;
        }
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public b9(long r39) {
        /*
            Method dump skipped, instructions count: 669
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.b9.<init>(long):void");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ct a(b9 b9Var) {
        return b9Var.p;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ct b(b9 b9Var) {
        return b9Var.m;
    }

    private static String b(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 34;
                    break;
                case 1:
                    i2 = 109;
                    break;
                case 2:
                    i2 = 18;
                    break;
                case 3:
                    i2 = 90;
                    break;
                case 4:
                    i2 = 47;
                    break;
                case 5:
                    i2 = 91;
                    break;
                default:
                    i2 = 52;
                    break;
            }
            cArr[i3] = (char) (c ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    private static char[] b(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ '4');
        }
        return charArray;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static cr c(b9 b9Var) {
        return b9Var.u;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static cr d(b9 b9Var) {
        return b9Var.k;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ct e(b9 b9Var) {
        return b9Var.n;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ct f(b9 b9Var) {
        return b9Var.l;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static cr g(b9 b9Var) {
        return b9Var.r;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ct h(b9 b9Var) {
        return b9Var.t;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static cq i(b9 b9Var) {
        return b9Var.s;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static cr j(b9 b9Var) {
        return b9Var.q;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static cp k(b9 b9Var) {
        return b9Var.o;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static az o() {
        return j;
    }

    @Override // com.trossense.bm
    public void a(long j2, Vec2f vec2f) {
        am.a(this.v, vec2f.x, j2 ^ 75340936936173L, vec2f.y);
    }

    @Override // com.trossense.bm
    public Vec2f f() {
        return new Vec2f(this.v.e(), this.v.h());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-trossense-b9, reason: not valid java name */
    public boolean m171lambda$new$0$comtrossenseb9() {
        return !this.k.j((w ^ 45707663725789L) ^ 82007509705530L).a.equals(x[3]);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$1$com-trossense-b9, reason: not valid java name */
    public boolean m172lambda$new$1$comtrossenseb9() {
        return this.k.j((w ^ 48376706540325L) ^ 80231819629762L).a.equals(x[45]);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$2$com-trossense-b9, reason: not valid java name */
    public boolean m173lambda$new$2$comtrossenseb9() {
        return this.k.j((w ^ 130175787439352L) ^ 23515229250335L).a.equals(x[6]);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$3$com-trossense-b9, reason: not valid java name */
    public boolean m174lambda$new$3$comtrossenseb9() {
        return !this.r.j((w ^ 31768424676601L) ^ 140614290737950L).a.equals(x[31]);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$4$com-trossense-b9, reason: not valid java name */
    public boolean m175lambda$new$4$comtrossenseb9() {
        return this.r.j((w ^ 120208101890165L) ^ 15753273487250L).a.equals(x[48]);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$5$com-trossense-b9, reason: not valid java name */
    public boolean m176lambda$new$5$comtrossenseb9() {
        return !l();
    }

    public am n() {
        return this.v;
    }
}
