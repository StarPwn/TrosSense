package com.trossense;

import com.trossense.clients.TrosSense;
import java.lang.invoke.MethodHandles;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public class am extends p {
    private static final long r = dj.a(-8339379807895919780L, 3274850404511393161L, MethodHandles.lookup().lookupClass()).a(248460354378840L);
    private static final String[] s;
    private float o;
    private boolean p;
    final b9 q;

    /* JADX WARN: Failed to find 'out' block for switch in B:4:0x0046. Please report as an issue. */
    static {
        int i;
        int i2;
        String[] strArr = new String[12];
        String str = "|\u007f\\!\u0015M\u0019O\u0007ilT+\u001eG\u0000\bhn\\)\u0019\b\u001eU\u0003obM\fhlP \\|\u0018\u001bYX=\b\u0005idZ-\b\u0007vbK \\A\u0019\u0007vbK \\A\u0019\bhn\\)\u0019\b\u001eU\u0004wh[1";
        int length = "|\u007f\\!\u0015M\u0019O\u0007ilT+\u001eG\u0000\bhn\\)\u0019\b\u001eU\u0003obM\fhlP \\|\u0018\u001bYX=\b\u0005idZ-\b\u0007vbK \\A\u0019\u0007vbK \\A\u0019\bhn\\)\u0019\b\u001eU\u0004wh[1".length();
        char c = '\b';
        int i3 = 0;
        int i4 = -1;
        while (true) {
            int i5 = 123;
            int i6 = i4 + 1;
            String substring = str.substring(i6, i6 + c);
            boolean z = -1;
            while (true) {
                String a = a(i5, a(substring));
                i = i3 + 1;
                switch (z) {
                    case false:
                        break;
                    default:
                        strArr[i3] = a;
                        i4 = i6 + c;
                        if (i4 < length) {
                            break;
                        }
                        str = "/#\u0012rN\u001dI\u0006##\u0015jH\u001e";
                        length = "/#\u0012rN\u001dI\u0006##\u0015jH\u001e".length();
                        c = 7;
                        i2 = -1;
                        i3 = i;
                        i5 = 32;
                        i6 = i2 + 1;
                        substring = str.substring(i6, i6 + c);
                        z = false;
                        break;
                }
                strArr[i3] = a;
                i2 = i6 + c;
                if (i2 >= length) {
                    s = strArr;
                    return;
                }
                c = str.charAt(i2);
                i3 = i;
                i5 = 32;
                i6 = i2 + 1;
                substring = str.substring(i6, i6 + c);
                z = false;
            }
            c = str.charAt(i4);
            i3 = i;
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public am(b9 b9Var, float f, float f2) {
        super(f, f2);
        this.q = b9Var;
        this.o = -2.1474836E9f;
    }

    private static String a(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 64;
                    break;
                case 1:
                    i2 = 118;
                    break;
                case 2:
                    i2 = 70;
                    break;
                case 3:
                    i2 = 62;
                    break;
                case 4:
                    i2 = 7;
                    break;
                case 5:
                    i2 = 83;
                    break;
                default:
                    i2 = 12;
                    break;
            }
            cArr[i3] = (char) (c ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(am amVar, float f, long j, float f2) {
        amVar.g((j ^ r) ^ 106977374843456L, f, f2);
    }

    private static char[] a(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ '\f');
        }
        return charArray;
    }

    private void g(long j, float f, float f2) {
        this.p = f <= 960.0f;
        this.a = f;
        this.b = f2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean lambda$render$0(bm bmVar) {
        return bmVar.l() || !bmVar.a.d();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static double lambda$render$1(bm bmVar) {
        long j = r ^ 30651336922339L;
        long j2 = 62102888448924L ^ j;
        return b9.o().a(103325251645156L ^ j, bmVar.i(j ^ 121743708717573L) ? bmVar.d(j2) + " " + bmVar.h() : bmVar.d(j2)) * (-1.0f);
    }

    @Override // com.trossense.p, com.trossense.aq
    public void a(long j) {
        long j2 = j ^ 39159558870452L;
        long j3 = j ^ 19020623741841L;
        long j4 = j ^ 128430963502825L;
        long j5 = j ^ 37436946984816L;
        this.p = this.a <= 960.0f;
        float f = this.b + 10.0f;
        float f2 = 0.0f;
        for (bm bmVar : TrosSense.INSTANCE.f().c()) {
            df dfVar = bmVar.a;
            dfVar.a(bmVar.l() ? 1.0f : 0.0f, j2);
            if (bmVar.l() || !dfVar.d()) {
                float a = b9.o().a(j3, bmVar.i(j5) ? bmVar.d(j4) + " " + bmVar.h() : bmVar.d(j4));
                if (f2 < a) {
                    f2 = a;
                }
                f += b9.a(this.q).e().floatValue();
            }
        }
        this.d = 20.0f + f2;
        this.e = f;
        if (this.p) {
            return;
        }
        float f3 = this.o;
        if (f3 == -2.1474836E9f) {
            this.o = this.a + f2 + 10.0f;
        } else {
            this.a = (f3 - f2) - 10.0f;
        }
    }

    @Override // com.trossense.p
    public boolean a(long j, float f, float f2) {
        return true;
    }

    @Override // com.trossense.p
    public void b(long j, float f, float f2) {
        if (this.p) {
            this.a = f;
        } else {
            this.o = f + this.d;
        }
        this.b = f2;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:106:0x0799, code lost:            if (r6 != null) goto L249;     */
    /* JADX WARN: Code restructure failed: missing block: B:112:0x08c8, code lost:            if (r6 != null) goto L271;     */
    /* JADX WARN: Code restructure failed: missing block: B:150:0x09e1, code lost:            if (r6 != null) goto L297;     */
    /* JADX WARN: Code restructure failed: missing block: B:176:0x070b, code lost:            if (r6 != null) goto L228;     */
    /* JADX WARN: Code restructure failed: missing block: B:179:0x0729, code lost:            if (r6 != null) goto L231;     */
    /* JADX WARN: Code restructure failed: missing block: B:185:0x06d1, code lost:            if (r6 != null) goto L220;     */
    /* JADX WARN: Code restructure failed: missing block: B:186:0x06ea, code lost:            if (r6 == null) goto L261;     */
    /* JADX WARN: Code restructure failed: missing block: B:192:0x06b6, code lost:            if (r6 != null) goto L215;     */
    /* JADX WARN: Code restructure failed: missing block: B:200:0x054d, code lost:            if (r6 != null) goto L156;     */
    /* JADX WARN: Code restructure failed: missing block: B:205:0x058c, code lost:            if (r6 != null) goto L159;     */
    /* JADX WARN: Code restructure failed: missing block: B:210:0x05c9, code lost:            if (r6 != null) goto L166;     */
    /* JADX WARN: Code restructure failed: missing block: B:253:0x02e9, code lost:            if (r6 != null) goto L76;     */
    /* JADX WARN: Code restructure failed: missing block: B:270:0x02a0, code lost:            if (r6 != null) goto L62;     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x04ec, code lost:            if (r6 != null) goto L141;     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x0641, code lost:            if (r6 != null) goto L183;     */
    /* JADX WARN: Code restructure failed: missing block: B:72:0x0655, code lost:            if (r6 != null) goto L189;     */
    /* JADX WARN: Code restructure failed: missing block: B:79:0x0669, code lost:            if (r6 != null) goto L195;     */
    /* JADX WARN: Failed to find 'out' block for switch in B:119:0x09aa. Please report as an issue. */
    /* JADX WARN: Failed to find 'out' block for switch in B:132:0x09d5. Please report as an issue. */
    /* JADX WARN: Failed to find 'out' block for switch in B:14:0x020f. Please report as an issue. */
    /* JADX WARN: Failed to find 'out' block for switch in B:19:0x0250. Please report as an issue. */
    /* JADX WARN: Failed to find 'out' block for switch in B:25:0x0356. Please report as an issue. */
    /* JADX WARN: Failed to find 'out' block for switch in B:30:0x03aa. Please report as an issue. */
    /* JADX WARN: Failed to find 'out' block for switch in B:37:0x04d6. Please report as an issue. */
    /* JADX WARN: Failed to find 'out' block for switch in B:52:0x0507. Please report as an issue. */
    /* JADX WARN: Failed to find 'out' block for switch in B:59:0x062d. Please report as an issue. */
    /* JADX WARN: Failed to find 'out' block for switch in B:88:0x068a. Please report as an issue. */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:110:0x0810  */
    /* JADX WARN: Removed duplicated region for block: B:116:0x0985  */
    /* JADX WARN: Removed duplicated region for block: B:119:0x09aa  */
    /* JADX WARN: Removed duplicated region for block: B:129:0x09cb  */
    /* JADX WARN: Removed duplicated region for block: B:12:0x01fd  */
    /* JADX WARN: Removed duplicated region for block: B:133:0x09d9  */
    /* JADX WARN: Removed duplicated region for block: B:137:0x0a07  */
    /* JADX WARN: Removed duplicated region for block: B:140:0x0168  */
    /* JADX WARN: Removed duplicated region for block: B:141:0x0160 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:147:0x0a3a A[LOOP:0: B:2:0x00ee->B:147:0x0a3a, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:148:0x09dc  */
    /* JADX WARN: Removed duplicated region for block: B:152:0x099c A[PHI: r7
  0x099c: PHI (r7v9 long) = (r7v13 long), (r7v13 long), (r7v14 long) binds: [B:132:0x09d5, B:150:0x09e1, B:154:0x099a] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:155:0x09a2  */
    /* JADX WARN: Removed duplicated region for block: B:158:0x08d0  */
    /* JADX WARN: Removed duplicated region for block: B:195:0x080a  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0250  */
    /* JADX WARN: Removed duplicated region for block: B:213:0x0621  */
    /* JADX WARN: Removed duplicated region for block: B:217:0x0472  */
    /* JADX WARN: Removed duplicated region for block: B:220:0x047a  */
    /* JADX WARN: Removed duplicated region for block: B:227:0x04a3  */
    /* JADX WARN: Removed duplicated region for block: B:231:0x0383  */
    /* JADX WARN: Removed duplicated region for block: B:233:0x0388  */
    /* JADX WARN: Removed duplicated region for block: B:245:0x03a2  */
    /* JADX WARN: Removed duplicated region for block: B:256:0x02fb  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0356  */
    /* JADX WARN: Removed duplicated region for block: B:266:0x0289  */
    /* JADX WARN: Removed duplicated region for block: B:273:0x02b3  */
    /* JADX WARN: Removed duplicated region for block: B:280:0x0272  */
    /* JADX WARN: Removed duplicated region for block: B:284:0x023a  */
    /* JADX WARN: Removed duplicated region for block: B:295:0x0333  */
    /* JADX WARN: Removed duplicated region for block: B:298:0x01e9  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x03aa  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x04d6  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x0507  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x062b  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x01bf  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x0688  */
    /* JADX WARN: Type inference failed for: r1v43 */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:144:0x0168 -> B:5:0x0198). Please report as a decompilation issue!!! */
    @Override // com.trossense.p
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void d(long r81) {
        /*
            Method dump skipped, instructions count: 2768
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.am.d(long):void");
    }
}
