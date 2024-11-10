package com.trossense;

import com.trossense.sdk.entity.type.EntityActor;
import com.trossense.sdk.entity.type.EntityLocalPlayer;
import com.trossense.sdk.math.Vec2f;
import com.trossense.sdk.math.Vec3f;
import java.lang.invoke.MethodHandles;
import java.util.Iterator;
import java.util.List;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;

@cg(a = "KillAura", b = b.Combat, c = "杀戮光环", d = 46)
/* loaded from: classes3.dex */
public class bq extends bm {
    private static final long D = dj.a(-5506182465047435397L, -492564226984050218L, MethodHandles.lookup().lookupClass()).a(8282675924603L);
    private static final String[] F;
    private c7 A;
    private EntityActor B;
    private final ct j;
    private final ct k;
    private final ct l;
    private final ct m;
    private final cp n;
    private final ct o;
    private final cr p;
    private final ct q;
    private final ct r;
    private final cp s;
    private final cr t;
    private final ct u;
    private final ct v;
    private final cr w;
    private final cp x;
    private List<EntityActor> y;
    private c7 z;

    /* JADX WARN: Failed to find 'out' block for switch in B:4:0x0045. Please report as an issue. */
    static {
        int i;
        int i2;
        String[] strArr = new String[60];
        String str = "\u0017OOQX\u0006杖尲轎夊逵廡\u0004禭劕俌夳\b杖多毭秬炓冼欭攦\u0007\u001bTL\u001ei\u0017_\u0004\u0018RL[\t\u0012TP[I3e9S\u0004剐籆樃弱\u0007\u001bRT[l.t\u0002翲佮\u0006\u0005JKJI/\u0006\u0005TLYF\"\u0005\u001bHNJC\b杖多毭秬炓冼欭攦\u0002霏黥\u0012\u001bTL\u001ex(x7IKQDg_&XGZ\u0007\u001b\\Z\u001ei\u0017_\u0006匃欜紀敲樋彈\u0005\u0017OOQX\b杖尲宼佭轄挥旺関\u0007\u001bTL\u001ei\u0017_\u0010\u001bTL\u001ey0e\"^J\u001en\"`7D\u0006\u0005TN[D3\t\u0005RPJ\n\nc2X\f\u0018R\u0002}Z4,\u001aTOW^\u0004\u0018RL[\u0005\u0005JKPM\u0005\u001aXEW^\u0003乛轑外\u0006\u001eXCR^/\u0002趋禆\u0002呞注\u0002蠖釲\b彖吒曖髦皮\u0004\\\u0005\u0005\u0004\\LYO\u0012\u001b\\Z\u001ex(x7IKQDg_&XGZ\u0004\u001bRF[\u0002袓夺\r\u0004RV_^.c8\u001doQN\"\u0007\u001b\\Z\u001ei\u0017_\b\u0012TQJK)o3\u0010\u001b\\Z\u001ey0e\"^J\u001en\"`7D\u0006\u0005JKJI/\u0006\u0005TN[D3\u0002捳扶\b杖多宼佭轄挥旺関\u0002匃亇\b\u0012TQJK)o3\u0005\u001aXEW^\u0006\u0005TLYF\"\b杖尲毭秬炓冼欭攦\u0005\u001bHNJC\u0004戽揲跿禅\t\u0012TP[I3e9S\u0002覐觯\u0004輺変樃弱\u0006\u001eXCR^/\b杖尲毭秬炓冼欭攦";
        int length = "\u0017OOQX\u0006杖尲轎夊逵廡\u0004禭劕俌夳\b杖多毭秬炓冼欭攦\u0007\u001bTL\u001ei\u0017_\u0004\u0018RL[\t\u0012TP[I3e9S\u0004剐籆樃弱\u0007\u001bRT[l.t\u0002翲佮\u0006\u0005JKJI/\u0006\u0005TLYF\"\u0005\u001bHNJC\b杖多毭秬炓冼欭攦\u0002霏黥\u0012\u001bTL\u001ex(x7IKQDg_&XGZ\u0007\u001b\\Z\u001ei\u0017_\u0006匃欜紀敲樋彈\u0005\u0017OOQX\b杖尲宼佭轄挥旺関\u0007\u001bTL\u001ei\u0017_\u0010\u001bTL\u001ey0e\"^J\u001en\"`7D\u0006\u0005TN[D3\t\u0005RPJ\n\nc2X\f\u0018R\u0002}Z4,\u001aTOW^\u0004\u0018RL[\u0005\u0005JKPM\u0005\u001aXEW^\u0003乛轑外\u0006\u001eXCR^/\u0002趋禆\u0002呞注\u0002蠖釲\b彖吒曖髦皮\u0004\\\u0005\u0005\u0004\\LYO\u0012\u001b\\Z\u001ex(x7IKQDg_&XGZ\u0004\u001bRF[\u0002袓夺\r\u0004RV_^.c8\u001doQN\"\u0007\u001b\\Z\u001ei\u0017_\b\u0012TQJK)o3\u0010\u001b\\Z\u001ey0e\"^J\u001en\"`7D\u0006\u0005JKJI/\u0006\u0005TN[D3\u0002捳扶\b杖多宼佭轄挥旺関\u0002匃亇\b\u0012TQJK)o3\u0005\u001aXEW^\u0006\u0005TLYF\"\b杖尲毭秬炓冼欭攦\u0005\u001bHNJC\u0004戽揲跿禅\t\u0012TP[I3e9S\u0002覐觯\u0004輺変樃弱\u0006\u001eXCR^/\b杖尲毭秬炓冼欭攦".length();
        char c = 5;
        int i3 = 0;
        int i4 = -1;
        while (true) {
            int i5 = 8;
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
                        str = "杫大轳夷逈廜\u0002輅换";
                        length = "杫大轳夷逈廜\u0002輅换".length();
                        c = 6;
                        i2 = -1;
                        i3 = i;
                        i5 = 53;
                        i6 = i2 + 1;
                        substring = str.substring(i6, i6 + c);
                        z = false;
                        break;
                }
                strArr[i3] = b;
                i2 = i6 + c;
                if (i2 >= length) {
                    F = strArr;
                    return;
                }
                c = str.charAt(i2);
                i3 = i;
                i5 = 53;
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
    public bq(char r40, int r41, int r42) {
        /*
            Method dump skipped, instructions count: 830
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.bq.<init>(char, int, int):void");
    }

    private double a(EntityActor entityActor, EntityActor entityActor2) {
        Vec3f N = entityActor.N();
        Vec3f N2 = entityActor2.N();
        float f = N.x - N2.x;
        float f2 = N.y - N2.y;
        float f3 = N.z - N2.z;
        return Math.sqrt((f * f) + (f2 * f2) + (f3 * f3));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float a(EntityActor entityActor) {
        long j = (D ^ 116910986887138L) ^ 118691329674571L;
        float f = 0.0f;
        for (int i = 0; i < 4; i++) {
            ItemData q = entityActor.h(i).q();
            if (q.getDefinition() instanceof com.trossense.sdk.f) {
                f += c4.a(j, q, (com.trossense.sdk.f) q.getDefinition());
            }
        }
        return f;
    }

    private void a(long j, EntityLocalPlayer entityLocalPlayer) {
        double doubleValue;
        ct ctVar;
        long j2 = j ^ D;
        long j3 = j2 ^ 54976850367104L;
        long j4 = 116314132195998L ^ j2;
        long j5 = j2 ^ 47737579526365L;
        long j6 = j5 >>> 8;
        int i = (int) ((j5 << 56) >>> 56);
        if (this.y.isEmpty()) {
            return;
        }
        if (this.n.e().booleanValue()) {
            doubleValue = this.m.e().doubleValue();
            ctVar = this.l;
        } else {
            doubleValue = this.k.e().doubleValue();
            ctVar = this.j;
        }
        double a = 1000.0d / ((int) c3.a(doubleValue, j3, ctVar.e().doubleValue()));
        if (this.z.b(j6, (byte) i, (long) a)) {
            a(entityLocalPlayer, this.n.e().booleanValue() ? (int) (this.z.c() / a) : 1, j4);
            this.z.a();
        }
    }

    private void a(EntityLocalPlayer entityLocalPlayer, int i, long j) {
        for (int i2 = 0; i2 < i; i2++) {
            Iterator<EntityActor> it2 = this.y.iterator();
            while (it2.hasNext()) {
                entityLocalPlayer.c(it2.next());
                if (this.x.e().booleanValue()) {
                    entityLocalPlayer.u();
                }
            }
        }
    }

    private static String b(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 94;
                    break;
                case 1:
                    i2 = 53;
                    break;
                case 2:
                    i2 = 42;
                    break;
                case 3:
                    i2 = 54;
                    break;
                case 4:
                    i2 = 34;
                    break;
                case 5:
                    i2 = 79;
                    break;
                default:
                    i2 = 4;
                    break;
            }
            cArr[i3] = (char) (c ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x008d, code lost:            if (r1.equals(r5[22]) != false) goto L18;     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void b(long r13, com.trossense.sdk.entity.type.EntityLocalPlayer r15) {
        /*
            r12 = this;
            long r0 = com.trossense.bq.D
            long r13 = r13 ^ r0
            r0 = 137549583875(0x2006981603, double:6.79585239924E-313)
            long r0 = r0 ^ r13
            r2 = 50040948611661(0x2d8310f75a4d, double:2.4723513594329E-310)
            long r2 = r2 ^ r13
            r4 = 87026560827786(0x4f26733e818a, double:4.29968339807226E-310)
            long r8 = r13 ^ r4
            r4 = 2259538461610(0x20e16fcc3aa, double:1.1163603293385E-311)
            long r13 = r13 ^ r4
            com.trossense.cr r4 = r12.p
            com.trossense.cu r4 = r4.j(r2)
            java.lang.String r4 = r4.a
            java.lang.String[] r5 = com.trossense.bq.F
            r6 = 25
            r6 = r5[r6]
            boolean r4 = r4.equals(r6)
            if (r4 == 0) goto L31
            return
        L31:
            com.trossense.ct r4 = r12.r
            java.lang.Object r4 = r4.e()
            java.lang.Number r4 = (java.lang.Number) r4
            double r6 = r4.doubleValue()
            com.trossense.ct r4 = r12.q
            java.lang.Object r4 = r4.e()
            java.lang.Number r4 = (java.lang.Number) r4
            double r10 = r4.doubleValue()
            double r6 = com.trossense.c3.a(r6, r8, r10)
            float r4 = (float) r6
            java.util.List<com.trossense.sdk.entity.type.EntityActor> r6 = r12.y
            boolean r6 = r6.isEmpty()
            if (r6 == 0) goto L57
            return
        L57:
            java.util.List<com.trossense.sdk.entity.type.EntityActor> r6 = r12.y
            r7 = 0
            java.lang.Object r6 = r6.get(r7)
            com.trossense.sdk.entity.type.EntityActor r6 = (com.trossense.sdk.entity.type.EntityActor) r6
            com.trossense.sdk.math.Vec3f r6 = r6.N()
            com.trossense.sdk.math.Vec2f r0 = com.trossense.c6.a(r15, r0, r6)
            com.trossense.cr r1 = r12.p
            com.trossense.cu r1 = r1.j(r2)
            java.lang.String r1 = r1.a
            r2 = -1
            int r3 = r1.hashCode()
            switch(r3) {
                case -1818460043: goto L85;
                case 73298841: goto L79;
                default: goto L78;
            }
        L78:
            goto L90
        L79:
            r3 = 27
            r3 = r5[r3]
            boolean r1 = r1.equals(r3)
            if (r1 == 0) goto L90
            r7 = 1
            goto L91
        L85:
            r3 = 22
            r3 = r5[r3]
            boolean r1 = r1.equals(r3)
            if (r1 == 0) goto L90
            goto L91
        L90:
            r7 = r2
        L91:
            switch(r7) {
                case 0: goto Lc0;
                case 1: goto L95;
                default: goto L94;
            }
        L94:
            goto Lcf
        L95:
            com.trossense.sdk.math.Vec2f r1 = r15.L()
            float r1 = r1.y
            float r2 = r0.y
            float r1 = com.trossense.c6.a(r13, r1, r2, r4)
            com.trossense.sdk.math.Vec2f r2 = r15.L()
            float r2 = r2.x
            float r0 = r0.x
            float r13 = com.trossense.c6.a(r13, r2, r0, r4)
            com.trossense.sdk.math.Vec2f r14 = r15.L()
            com.trossense.sdk.math.Vec2f r0 = new com.trossense.sdk.math.Vec2f
            float r2 = r14.x
            float r2 = r2 - r13
            float r13 = r14.y
            float r1 = r1 - r13
            r0.<init>(r2, r1)
            r15.d(r0)
            goto Lcf
        Lc0:
            com.trossense.cp r13 = r12.s
            java.lang.Object r13 = r13.e()
            java.lang.Boolean r13 = (java.lang.Boolean) r13
            boolean r13 = r13.booleanValue()
            com.trossense.a8.a(r0, r4, r13)
        Lcf:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.bq.b(long, com.trossense.sdk.entity.type.EntityLocalPlayer):void");
    }

    private boolean b(EntityActor entityActor) {
        return entityActor instanceof com.trossense.sdk.entity.type.c;
    }

    private static char[] b(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ 4);
        }
        return charArray;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:104:0x007a, code lost:            if (r15 == null) goto L17;     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x008e, code lost:            if (r15 == null) goto L23;     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x00a1, code lost:            if (r15 == null) goto L33;     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x00db, code lost:            if (r15 == null) goto L46;     */
    /* JADX WARN: Code restructure failed: missing block: B:76:0x0201, code lost:            if (r15 != null) goto L109;     */
    /* JADX WARN: Code restructure failed: missing block: B:81:0x01f5, code lost:            if (r15 == null) goto L106;     */
    /* JADX WARN: Code restructure failed: missing block: B:90:0x00ed, code lost:            if (r15 == null) goto L49;     */
    /* JADX WARN: Code restructure failed: missing block: B:93:0x00ff, code lost:            if (r15 == null) goto L54;     */
    /* JADX WARN: Failed to find 'out' block for switch in B:26:0x00c7. Please report as an issue. */
    /* JADX WARN: Failed to find 'out' block for switch in B:57:0x016a. Please report as an issue. */
    /* JADX WARN: Failed to find 'out' block for switch in B:7:0x0060. Please report as an issue. */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:27:0x00cb  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x0121  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x0144  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x0158  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x016a  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x01b4  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x01d3  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x0188  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x01b1  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x00dd A[FALL_THROUGH] */
    /* JADX WARN: Removed duplicated region for block: B:91:0x00ef A[FALL_THROUGH] */
    /* JADX WARN: Removed duplicated region for block: B:94:0x0104 A[FALL_THROUGH] */
    /* JADX WARN: Type inference failed for: r3v16 */
    /* JADX WARN: Type inference failed for: r3v18 */
    /* JADX WARN: Type inference failed for: r3v20 */
    /* JADX WARN: Type inference failed for: r3v21 */
    /* JADX WARN: Type inference failed for: r3v23 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void c(long r21, final com.trossense.sdk.entity.type.EntityLocalPlayer r23) {
        /*
            Method dump skipped, instructions count: 594
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.bq.c(long, com.trossense.sdk.entity.type.EntityLocalPlayer):void");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static double lambda$findEntity$14(EntityLocalPlayer entityLocalPlayer, EntityActor entityActor) {
        Vec2f b = c6.b((D ^ 52013723000503L) ^ 80962952315216L, entityLocalPlayer, entityActor.N());
        return (b.x * b.x) + (b.y * b.y);
    }

    @bk(3)
    public void a(bg bgVar) {
        long j = D ^ 124934165311879L;
        long j2 = 2479630802837L ^ j;
        long j3 = 101620085019660L ^ j;
        long j4 = j ^ 140082189094047L;
        EntityLocalPlayer a = bgVar.a();
        c(j3, a);
        if (this.y.isEmpty()) {
            return;
        }
        b(j4, a);
        a(j2, a);
    }

    @Override // com.trossense.bm
    public void j(long j) {
        this.z.a();
        this.A.a();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$findEntity$15$com-trossense-bq, reason: not valid java name */
    public double m181lambda$findEntity$15$comtrossensebq(EntityLocalPlayer entityLocalPlayer, EntityActor entityActor) {
        return a(entityLocalPlayer, entityActor);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-trossense-bq, reason: not valid java name */
    public Number m182lambda$new$0$comtrossensebq(Number number) {
        return Double.valueOf(Math.min(number.doubleValue(), this.j.e().doubleValue()));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$1$com-trossense-bq, reason: not valid java name */
    public Number m183lambda$new$1$comtrossensebq(Number number) {
        return Double.valueOf(Math.max(number.doubleValue(), this.k.e().doubleValue()));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$10$com-trossense-bq, reason: not valid java name */
    public Number m184lambda$new$10$comtrossensebq(Number number) {
        return Double.valueOf(Math.min(number.doubleValue(), this.q.e().doubleValue()));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$11$com-trossense-bq, reason: not valid java name */
    public Number m185lambda$new$11$comtrossensebq(Number number) {
        return Double.valueOf(Math.max(number.doubleValue(), this.r.e().doubleValue()));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$12$com-trossense-bq, reason: not valid java name */
    public boolean m186lambda$new$12$comtrossensebq() {
        return this.t.j((D ^ 97374186466506L) ^ 122078497342208L).a.equalsIgnoreCase(F[5]);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$13$com-trossense-bq, reason: not valid java name */
    public boolean m187lambda$new$13$comtrossensebq() {
        return this.t.j((D ^ 83658028330594L) ^ 135918731968936L).a.equalsIgnoreCase(F[5]);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$2$com-trossense-bq, reason: not valid java name */
    public Number m188lambda$new$2$comtrossensebq(Number number) {
        return Double.valueOf(Math.min(number.doubleValue(), this.l.e().doubleValue()));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$3$com-trossense-bq, reason: not valid java name */
    public Number m189lambda$new$3$comtrossensebq(Number number) {
        return Double.valueOf(Math.max(number.doubleValue(), this.m.e().doubleValue()));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$4$com-trossense-bq, reason: not valid java name */
    public boolean m190lambda$new$4$comtrossensebq() {
        return !this.n.e().booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$5$com-trossense-bq, reason: not valid java name */
    public boolean m191lambda$new$5$comtrossensebq() {
        return !this.n.e().booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$6$com-trossense-bq, reason: not valid java name */
    public Number m192lambda$new$6$comtrossensebq(Number number) {
        return Double.valueOf(Math.min(number.doubleValue(), this.u.e().doubleValue()));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$7$com-trossense-bq, reason: not valid java name */
    public Number m193lambda$new$7$comtrossensebq(Number number) {
        return Double.valueOf(Math.max(number.doubleValue(), this.v.e().doubleValue()));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$8$com-trossense-bq, reason: not valid java name */
    public boolean m194lambda$new$8$comtrossensebq() {
        return !this.t.j((D ^ 77070809597765L) ^ 124864875325583L).a.equalsIgnoreCase(F[10]);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$9$com-trossense-bq, reason: not valid java name */
    public boolean m195lambda$new$9$comtrossensebq() {
        return !this.t.j((D ^ 100640062616533L) ^ 118748256001055L).a.equalsIgnoreCase(F[42]);
    }

    public List<EntityActor> n() {
        return this.y;
    }
}
