package com.trossense;

import com.trossense.clients.TrosSense;
import com.trossense.sdk.block.Block;
import com.trossense.sdk.entity.type.EntityLocalPlayer;
import com.trossense.sdk.inventory.PlayerInventory;
import com.trossense.sdk.level.Level;
import com.trossense.sdk.math.Vec3f;
import com.trossense.sdk.math.Vec3i;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.protocol.bedrock.data.PlayerActionType;
import org.cloudburstmc.protocol.bedrock.data.PlayerAuthInputData;
import org.cloudburstmc.protocol.bedrock.data.PlayerBlockActionData;

@cg(a = "Fucker", b = b.Misc, c = "隔空挖床")
/* loaded from: classes3.dex */
public class bw extends bm {
    private static final String l;
    private static final List<Vec3i> q;
    private static final List<Vec3i> r;
    private static final long t = dj.a(-3054740726099273962L, -7217281755534905886L, MethodHandles.lookup().lookupClass()).a(192939063247684L);
    private static final String[] u;
    private final ct j;
    private final cp k;
    private final cr m;
    private Vector3i n;
    private int o;
    private int p;
    private Vec3i s;

    /* JADX WARN: Failed to find 'out' block for switch in B:4:0x0045. Please report as an issue. */
    static {
        int i;
        int i2;
        String[] strArr = new String[14];
        String str = "樶彖\u0003U\u0014^\u0003_\u0000]\u0003U\u0014^\rz0g\u001e\u0010Guq-3\u0019\u0016Q\u0003芦隱庤\u0004持廓跔秀\rz0g\u001e\u0010Guq-3\u0019\u0016Q\u0003帔呐岒\u0004Z6m\u001e\u0003_\u0000]\u0002呿嚭";
        int length = "樶彖\u0003U\u0014^\u0003_\u0000]\u0003U\u0014^\rz0g\u001e\u0010Guq-3\u0019\u0016Q\u0003芦隱庤\u0004持廓跔秀\rz0g\u001e\u0010Guq-3\u0019\u0016Q\u0003帔呐岒\u0004Z6m\u001e\u0003_\u0000]\u0002呿嚭".length();
        char c = 2;
        int i3 = 0;
        int i4 = -1;
        while (true) {
            int i5 = 120;
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
                        str = "D9f\u001d\u0017\bE-z\b\u001dA{r";
                        length = "D9f\u001d\u0017\bE-z\b\u001dA{r".length();
                        c = 5;
                        i2 = -1;
                        i3 = i;
                        i5 = 121;
                        i6 = i2 + 1;
                        substring = str.substring(i6, i6 + c);
                        z = false;
                        break;
                }
                strArr[i3] = b;
                i2 = i6 + c;
                if (i2 >= length) {
                    u = strArr;
                    l = strArr[7];
                    ArrayList arrayList = new ArrayList();
                    q = arrayList;
                    ArrayList arrayList2 = new ArrayList();
                    r = arrayList2;
                    arrayList.add(com.trossense.sdk.block.a.NORTH.b());
                    arrayList.add(com.trossense.sdk.block.a.SOUTH.b());
                    arrayList.add(com.trossense.sdk.block.a.WEST.b());
                    arrayList.add(com.trossense.sdk.block.a.EAST.b());
                    arrayList2.add(com.trossense.sdk.block.a.NORTH.b());
                    arrayList2.add(com.trossense.sdk.block.a.SOUTH.b());
                    arrayList2.add(com.trossense.sdk.block.a.WEST.b());
                    arrayList2.add(com.trossense.sdk.block.a.EAST.b());
                    arrayList2.add(com.trossense.sdk.block.a.UP.b());
                    return;
                }
                c = str.charAt(i2);
                i3 = i;
                i5 = 121;
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
    public bw(short r26, long r27) {
        /*
            r25 = this;
            r10 = r25
            r0 = r26
            long r0 = (long) r0
            r2 = 48
            long r0 = r0 << r2
            r3 = 16
            long r4 = r27 << r3
            long r4 = r4 >>> r3
            long r0 = r0 | r4
            long r4 = com.trossense.bw.t
            long r0 = r0 ^ r4
            r4 = 37215831837414(0x21d8fc6fcae6, double:1.8387063992271E-310)
            long r4 = r4 ^ r0
            r6 = 96383772201858(0x57a918811382, double:4.76199106615266E-310)
            long r6 = r6 ^ r0
            r8 = 19443664326704(0x11af14b49030, double:9.6064465730934E-311)
            long r8 = r8 ^ r0
            long r11 = r8 >>> r3
            long r8 = r8 << r2
            long r8 = r8 >>> r2
            int r13 = (int) r8
            r8 = 129330879870422(0x75a0312735d6, double:6.38979446903967E-310)
            long r8 = r8 ^ r0
            long r14 = r8 >>> r2
            int r14 = (int) r14
            long r15 = r8 << r3
            r3 = 32
            r26 = r14
            long r14 = r15 >>> r3
            int r14 = (int) r14
            long r8 = r8 << r2
            long r2 = r8 >>> r2
            int r15 = (int) r2
            r2 = 28403789650902(0x19d545cadbd6, double:1.4033336678212E-310)
            long r16 = r0 ^ r2
            r10.<init>(r6)
            com.trossense.ct r9 = new com.trossense.ct
            java.lang.String[] r22 = com.trossense.bw.u
            r0 = 12
            r1 = r22[r0]
            r0 = 6
            r2 = r22[r0]
            r23 = 5
            java.lang.Integer r6 = java.lang.Integer.valueOf(r23)
            r18 = 1
            java.lang.Integer r7 = java.lang.Integer.valueOf(r18)
            r24 = 10
            java.lang.Integer r8 = java.lang.Integer.valueOf(r24)
            r19 = 4591870180066957722(0x3fb999999999999a, double:0.1)
            java.lang.Double r19 = java.lang.Double.valueOf(r19)
            r0 = r9
            r3 = r25
            r27 = r14
            r14 = r9
            r9 = r19
            r0.<init>(r1, r2, r3, r4, r6, r7, r8, r9)
            r10.j = r14
            com.trossense.cp r8 = new com.trossense.cp
            short r3 = (short) r13
            r0 = 13
            r4 = r22[r0]
            r0 = 11
            r5 = r22[r0]
            java.lang.Boolean r7 = java.lang.Boolean.valueOf(r18)
            r0 = r8
            r1 = r11
            r6 = r25
            r0.<init>(r1, r3, r4, r5, r6, r7)
            r10.k = r8
            com.trossense.cr r7 = new com.trossense.cr
            r0 = 9
            r1 = r22[r0]
            r0 = 0
            r2 = r22[r0]
            r6 = r22[r18]
            r0 = r7
            r3 = r25
            r4 = r16
            r0.<init>(r1, r2, r3, r4, r6)
            r0 = r26
            short r0 = (short) r0
            r1 = 3
            r19 = r22[r1]
            char r1 = (char) r15
            r2 = 8
            r21 = r22[r2]
            r16 = r7
            r17 = r0
            r18 = r27
            r20 = r1
            com.trossense.cr r16 = r16.a(r17, r18, r19, r20, r21)
            r19 = r22[r24]
            r21 = r22[r23]
            com.trossense.cr r0 = r16.a(r17, r18, r19, r20, r21)
            r10.m = r0
            r0 = -1
            r10.p = r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.bw.<init>(short, long):void");
    }

    private double a(Vec3i vec3i, Vec3f vec3f) {
        float f = vec3i.x - vec3f.x;
        float f2 = vec3i.y - vec3f.y;
        float f3 = vec3i.z - vec3f.z;
        return Math.sqrt((f * f) + (f2 * f2) + (f3 * f3));
    }

    private ci a(long j, EntityLocalPlayer entityLocalPlayer, Block block) {
        long j2 = (j ^ t) ^ 7395307624241L;
        int d = entityLocalPlayer.ak().d();
        PlayerInventory ak = entityLocalPlayer.ak();
        ci ciVar = new ci(this, d, j2, entityLocalPlayer.a(block));
        for (int i = 0; i < 9; i++) {
            if (i != d) {
                ak.a(i);
                float a = entityLocalPlayer.a(block);
                if (ciVar.b() < a) {
                    ciVar = new ci(this, i, j2, a);
                }
            }
        }
        ak.a(d);
        return ciVar;
    }

    /* JADX WARN: Code restructure failed: missing block: B:24:0x0243, code lost:            r10 = r25 + 1;     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x0245, code lost:            if (r14 == null) goto L57;     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x0255, code lost:            return;     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x020a, code lost:            r4 = r23;        r1 = r24;        r10 = r25;        r11 = r26;        r5 = r5;     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x0235, code lost:            r11 = r5;     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r37v0, types: [com.trossense.sdk.entity.type.EntityLocalPlayer] */
    /* JADX WARN: Type inference failed for: r5v25 */
    /* JADX WARN: Type inference failed for: r5v8 */
    /* JADX WARN: Type inference failed for: r5v9 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void a(short r34, int r35, int r36, com.trossense.sdk.entity.type.EntityLocalPlayer r37) {
        /*
            Method dump skipped, instructions count: 598
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.bw.a(short, int, int, com.trossense.sdk.entity.type.EntityLocalPlayer):void");
    }

    private static String b(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 111;
                    break;
                case 1:
                    i2 = 33;
                    break;
                case 2:
                    i2 = 113;
                    break;
                case 3:
                    i2 = 3;
                    break;
                case 4:
                    i2 = 11;
                    break;
                case 5:
                    i2 = 77;
                    break;
                default:
                    i2 = 108;
                    break;
            }
            cArr[i3] = (char) (c ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    /* JADX WARN: Code restructure failed: missing block: B:32:0x014b, code lost:            if (r8 != null) goto L34;     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void b(com.trossense.sdk.entity.type.EntityLocalPlayer r17, long r18) {
        /*
            Method dump skipped, instructions count: 497
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.bw.b(com.trossense.sdk.entity.type.EntityLocalPlayer, long):void");
    }

    private static char[] b(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ 'l');
        }
        return charArray;
    }

    private double c(EntityLocalPlayer entityLocalPlayer) {
        Vec3f N = entityLocalPlayer.N();
        float x = N.x - this.n.getX();
        float y = N.y - this.n.getY();
        float z = N.z - this.n.getZ();
        return Math.sqrt((x * x) + (y * y) + (z * z));
    }

    List a(EntityLocalPlayer entityLocalPlayer, long j, Vec3i vec3i) {
        Vec3i vec3i2;
        Level W = entityLocalPlayer.W();
        ArrayList<Vec3i> arrayList = new ArrayList();
        Iterator<Vec3i> it2 = q.iterator();
        while (true) {
            if (!it2.hasNext()) {
                vec3i2 = null;
                break;
            }
            vec3i2 = it2.next();
            if (W.c(new Vec3i(vec3i.x + vec3i2.x, vec3i.y + vec3i2.y, vec3i.z + vec3i2.z)).d().equals(u[7])) {
                break;
            }
        }
        if (vec3i2 == null) {
            return arrayList;
        }
        for (Vec3i vec3i3 : r) {
            Vec3i vec3i4 = new Vec3i(vec3i.x + vec3i3.x, vec3i.y + vec3i3.y, vec3i.z + vec3i3.z);
            if (!W.c(vec3i4).d().equals(u[7])) {
                arrayList.add(vec3i4);
            }
        }
        for (Vec3i vec3i5 : r) {
            Vec3i vec3i6 = new Vec3i(vec3i.x + vec3i5.x + vec3i2.x, vec3i.y + vec3i5.y + vec3i2.y, vec3i.z + vec3i5.z + vec3i2.z);
            if (!W.c(vec3i6).d().equals(u[7])) {
                boolean z = false;
                for (Vec3i vec3i7 : arrayList) {
                    if (vec3i7.x == vec3i6.x && vec3i7.y == vec3i6.y && vec3i7.z == vec3i6.z) {
                        z = true;
                    }
                }
                if (!z) {
                    arrayList.add(vec3i6);
                }
            }
        }
        return arrayList;
    }

    @bk(1)
    public void a(bb bbVar) {
        String str = this.m.j((t ^ 9475841957524L) ^ 51792465745504L).a;
        String[] strArr = u;
        if (str.equals(strArr[2]) && (bbVar.e() instanceof com.trossense.sdk.ad)) {
            com.trossense.sdk.ad adVar = (com.trossense.sdk.ad) bbVar.e();
            EntityLocalPlayer localPlayer = TrosSense.INSTANCE.getLocalPlayer();
            if (localPlayer == null || this.s == null) {
                return;
            }
            Vec3f N = localPlayer.N();
            adVar.getInputData().add(PlayerAuthInputData.PERFORM_BLOCK_ACTIONS);
            if (a(this.s, N) > this.j.e().floatValue() || !localPlayer.W().c(this.s).d().equals(strArr[7])) {
                PlayerBlockActionData playerBlockActionData = new PlayerBlockActionData();
                playerBlockActionData.setAction(PlayerActionType.ABORT_BREAK);
                playerBlockActionData.setFace(1);
                playerBlockActionData.setBlockPosition(Vector3i.from(this.s.x, this.s.y, this.s.z));
                adVar.getPlayerActions().add(playerBlockActionData);
                this.s = null;
                return;
            }
            Vector3i from = Vector3i.from(this.s.x, this.s.y, this.s.z);
            PlayerBlockActionData playerBlockActionData2 = new PlayerBlockActionData();
            playerBlockActionData2.setAction(PlayerActionType.START_BREAK);
            playerBlockActionData2.setFace(1);
            playerBlockActionData2.setBlockPosition(from);
            adVar.getPlayerActions().add(playerBlockActionData2);
            PlayerBlockActionData playerBlockActionData3 = new PlayerBlockActionData();
            playerBlockActionData3.setAction(PlayerActionType.BLOCK_CONTINUE_DESTROY);
            playerBlockActionData3.setFace(1);
            playerBlockActionData3.setBlockPosition(from);
            adVar.getPlayerActions().add(playerBlockActionData3);
            PlayerBlockActionData playerBlockActionData4 = new PlayerBlockActionData();
            playerBlockActionData4.setAction(PlayerActionType.BLOCK_PREDICT_DESTROY);
            playerBlockActionData4.setFace(1);
            playerBlockActionData4.setBlockPosition(from);
            adVar.getPlayerActions().add(playerBlockActionData4);
        }
    }

    @bk
    public void a(bg bgVar) {
        long j = t ^ 21767947351717L;
        long j2 = 35797870111672L ^ j;
        int i = (int) (j2 >>> 48);
        int i2 = (int) ((j2 << 16) >>> 32);
        int i3 = (int) ((j2 << 48) >>> 48);
        long j3 = 57486617919057L ^ j;
        long j4 = j ^ 34219240367323L;
        EntityLocalPlayer a = bgVar.a();
        if (this.m.j(j3).a.equals(u[3])) {
            if (this.n != null) {
                b(a, j4);
                return;
            } else {
                a((short) i, i2, i3, a);
                return;
            }
        }
        Level W = a.W();
        Vec3f N = a.N();
        if (this.s != null) {
            return;
        }
        Vector3i from = Vector3i.from((int) Math.floor(N.x), (int) Math.floor(N.y), (int) Math.floor(N.z));
        int intValue = this.j.e().intValue();
        int i4 = -intValue;
        for (int i5 = i4; i5 <= intValue; i5++) {
            for (int i6 = i4; i6 <= intValue; i6++) {
                for (int i7 = i4; i7 <= intValue; i7++) {
                    Vector3i add = from.add(i5, i6, i7);
                    if (W.c(new Vec3i(add.getX(), add.getY(), add.getZ())).d().equals(u[7]) && add.distance(N.x, N.y, N.z) <= this.j.e().floatValue()) {
                        this.s = new Vec3i(add.getX(), add.getY(), add.getZ());
                    }
                }
            }
        }
    }

    @Override // com.trossense.bm
    public void j(long j) {
        this.s = null;
    }
}
