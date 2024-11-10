package com.trossense;

import com.trossense.sdk.entity.type.EntityLocalPlayer;
import com.trossense.sdk.math.Vec3f;
import com.trossense.sdk.math.Vec3i;
import java.lang.invoke.MethodHandles;
import java.util.List;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.protocol.bedrock.packet.SetSpawnPositionPacket;

@cg(a = "ChestAura", b = b.World, c = "自动拿箱子", d = 30)
/* loaded from: classes3.dex */
public class ca extends bm {
    private static int[] q;
    private static final long r = dj.a(-2298508559189820516L, -3773604139182034290L, MethodHandles.lookup().lookupClass()).a(2287179820668L);
    private static final String[] s;
    public List<Vector3i> j;
    private final ct k;
    private final cp l;
    private final ct m;
    private final ct n;
    private final cp o;
    private Vector3i p;

    /* JADX WARN: Failed to find 'out' block for switch in B:4:0x004a. Please report as an issue. */
    static {
        int i;
        int i2;
        String[] strArr = new String[12];
        String str = "9-]\u0001AsV\u0013>S\u0006杶屒轔奛遾庖\u0012;4VO3_J\u0017)Q\u0000\u000f\u0010m\u00068]\u000b\u0012;<@O3_J\u0017)Q\u0000\u000f\u0010m\u00068]\u000b\u0004戥彝棸枊\r$2L\u000e\u0015YQ\u0018}u\u0000\u0005U\u0005$<V\b\u0004\u000f\u001b4V\n\u0002B_\u0010)\u0002\f\tUM\u0002\u000f\u001b4V\n\u0002B_\u0010)\u0002\f\tUM\u0002\u0006杶奺轔奛遾庖";
        int length = "9-]\u0001AsV\u0013>S\u0006杶屒轔奛遾庖\u0012;4VO3_J\u0017)Q\u0000\u000f\u0010m\u00068]\u000b\u0012;<@O3_J\u0017)Q\u0000\u000f\u0010m\u00068]\u000b\u0004戥彝棸枊\r$2L\u000e\u0015YQ\u0018}u\u0000\u0005U\u0005$<V\b\u0004\u000f\u001b4V\n\u0002B_\u0010)\u0002\f\tUM\u0002\u000f\u001b4V\n\u0002B_\u0010)\u0002\f\tUM\u0002\u0006杶奺轔奛遾庖".length();
        b(new int[2]);
        char c = '\n';
        int i3 = 0;
        int i4 = -1;
        while (true) {
            int i5 = i4 + 1;
            String substring = str.substring(i5, i5 + c);
            boolean z = -1;
            while (true) {
                String b = b(19, b(substring));
                switch (z) {
                    case false:
                        break;
                    default:
                        i = i3 + 1;
                        strArr[i3] = b;
                        i4 = i5 + c;
                        if (i4 < length) {
                            break;
                        }
                        str = "戥彝跥秔\u0004輚奩標彠";
                        length = "戥彝跥秔\u0004輚奩標彠".length();
                        c = 4;
                        i2 = -1;
                        i3 = i;
                        i5 = i2 + 1;
                        substring = str.substring(i5, i5 + c);
                        z = false;
                        break;
                }
                i = i3 + 1;
                strArr[i3] = b;
                i2 = i5 + c;
                if (i2 >= length) {
                    s = strArr;
                    return;
                }
                c = str.charAt(i2);
                i3 = i;
                i5 = i2 + 1;
                substring = str.substring(i5, i5 + c);
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
    public ca(short r23, short r24, int r25) {
        /*
            Method dump skipped, instructions count: 222
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.ca.<init>(short, short, int):void");
    }

    /* JADX WARN: Code restructure failed: missing block: B:19:0x00bb, code lost:            r8 = r8 + 1;     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x00bd, code lost:            if (r1 != null) goto L31;     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x00b3, code lost:            r9 = r10 ? 1 : 0;     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x00c8, code lost:            r8 = r6;     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void a(com.trossense.sdk.entity.type.EntityLocalPlayer r18, long r19) {
        /*
            Method dump skipped, instructions count: 223
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.ca.a(com.trossense.sdk.entity.type.EntityLocalPlayer, long):void");
    }

    private static String b(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 101;
                    break;
                case 1:
                    i2 = 78;
                    break;
                case 2:
                    i2 = 43;
                    break;
                case 3:
                    i2 = 124;
                    break;
                case 4:
                    i2 = 114;
                    break;
                case 5:
                    i2 = 35;
                    break;
                default:
                    i2 = 45;
                    break;
            }
            cArr[i3] = (char) (c ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void b(long j, EntityLocalPlayer entityLocalPlayer) {
        long j2 = r ^ j;
        long j3 = 10896050811097L ^ j2;
        long j4 = j2 ^ 77990609971024L;
        Vector3i vector3i = this.p;
        if (vector3i == null || j2 < 0 || this.j.contains(vector3i) || (j2 > 0 && !entityLocalPlayer.W().c(new Vec3i(this.p.getX(), this.p.getY(), this.p.getZ())).d().equals(s[8]))) {
            this.p = null;
            return;
        }
        Vec3f N = entityLocalPlayer.N();
        int i = (this.p.distance(N.x, N.y, N.z) > this.k.e().doubleValue() ? 1 : (this.p.distance(N.x, N.y, N.z) == this.k.e().doubleValue() ? 0 : -1));
        boolean z = i;
        if (j2 >= 0) {
            if (i > 0) {
                this.p = null;
                return;
            }
            z = this.l.e().booleanValue();
        }
        if (j2 >= 0) {
            if (z != 0) {
                a8.a(c6.a(entityLocalPlayer, j3, new Vec3f(this.p.getX() + 0.5f, this.p.getY() + 0.5f, this.p.getZ() + 0.5f)), (float) c3.a(this.n.e().doubleValue(), j4, this.m.e().doubleValue()));
            }
            entityLocalPlayer.a(new Vec3i(this.p.getX(), this.p.getY(), this.p.getZ()), 2, true);
            z = this.o.e().booleanValue();
        }
        if (j2 >= 0) {
            if (z != 0) {
                return;
            } else {
                this.j.add(this.p);
            }
        }
        this.p = null;
    }

    public static void b(int[] iArr) {
        q = iArr;
    }

    private static char[] b(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ '-');
        }
        return charArray;
    }

    public static int[] n() {
        return q;
    }

    @bk
    public void a(ba baVar) {
        if (baVar.e() instanceof SetSpawnPositionPacket) {
            this.j.clear();
        }
        if (baVar.e() instanceof com.trossense.sdk.p) {
            com.trossense.sdk.p pVar = (com.trossense.sdk.p) baVar.e();
            if (!this.o.e().booleanValue() || this.p == null || pVar.a() == 0) {
                return;
            }
            this.j.add(this.p);
            this.p = null;
        }
    }

    @bk
    public void a(bg bgVar) {
        long j = r ^ 51192221899915L;
        long j2 = 15744034505923L ^ j;
        long j3 = j ^ 45630168926025L;
        EntityLocalPlayer a = bgVar.a();
        if (a7.a() != null) {
            return;
        }
        if (this.p == null) {
            a(a, j2);
            if (this.p == null) {
                return;
            }
        }
        b(j3, a);
    }

    @Override // com.trossense.bm
    public void j(long j) {
        this.j.clear();
        this.p = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-trossense-ca, reason: not valid java name */
    public Number m196lambda$new$0$comtrossenseca(Number number) {
        return Double.valueOf(Math.min(number.doubleValue(), this.m.e().doubleValue()));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$1$com-trossense-ca, reason: not valid java name */
    public Number m197lambda$new$1$comtrossenseca(Number number) {
        return Double.valueOf(Math.max(number.doubleValue(), this.n.e().doubleValue()));
    }
}
