package com.trossense;

import com.trossense.clients.TrosSense;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/* loaded from: classes3.dex */
public class ch {
    private static final long b = dj.a(6786870382890856619L, 3924675543148873474L, MethodHandles.lookup().lookupClass()).a(169797531924212L);
    private List<bm> a;

    public ch(long j) {
        long j2 = (j ^ b) ^ 115098117576429L;
        this.a = new ArrayList();
        a(j2);
    }

    private void a(long j) {
        b((j ^ b) ^ 113386642041864L);
    }

    private void b(long j) {
        long j2 = b ^ j;
        long j3 = 61694069845259L ^ j2;
        int i = (int) (j3 >>> 32);
        int i2 = (int) ((j3 << 32) >>> 48);
        int i3 = (int) ((j3 << 48) >>> 48);
        long j4 = j2 ^ 83177101443683L;
        long j5 = j2 ^ 137905999813502L;
        int i4 = (int) (j5 >>> 48);
        long j6 = (j5 << 16) >>> 16;
        long j7 = j2 ^ 20091627669949L;
        int i5 = (int) (j7 >>> 48);
        int i6 = (int) ((j7 << 16) >>> 32);
        int i7 = (int) ((j7 << 48) >>> 48);
        long j8 = j2 ^ 104481790290544L;
        long j9 = 30027026219172L ^ j2;
        int i8 = (int) (j9 >>> 32);
        int i9 = (int) ((j9 << 32) >>> 48);
        int i10 = (int) ((j9 << 48) >>> 48);
        long j10 = 87127583940612L ^ j2;
        int i11 = (int) (j10 >>> 48);
        int i12 = (int) ((j10 << 16) >>> 48);
        int i13 = (int) ((j10 << 32) >>> 32);
        long j11 = 11552326820222L ^ j2;
        int i14 = (int) (j11 >>> 48);
        long j12 = (j11 << 16) >>> 16;
        long j13 = 3501855550751L ^ j2;
        int i15 = (int) (j13 >>> 32);
        int i16 = (int) ((j13 << 32) >>> 48);
        int i17 = (int) ((j13 << 48) >>> 48);
        a(new bn(j2 ^ 80247303282828L), j4);
        a(new bo(j2 ^ 128675747848095L), j4);
        a(new bq((char) (j8 >>> 48), (int) ((j8 << 16) >>> 32), (int) ((j8 << 48) >>> 48)), j4);
        a(new br(j2 ^ 139313834432242L), j4);
        a(new bs(j2 ^ 41759635017584L), j4);
        a(new bp(46543346475588L ^ j2), j4);
        a(new bu((short) i5, i6, (char) i7), j4);
        a(new bt(j2 ^ 11430973019598L), j4);
        a(new bw((short) i4, j6), j4);
        a(new bv(j2 ^ 43044545519445L), j4);
        a(new bx(j2 ^ 80249449959901L), j4);
        a(new by(j2 ^ 111053188555241L), j4);
        a(new bz(j2 ^ 85541500739912L), j4);
        a(new b0(i15, (short) i16, (short) i17), j4);
        a(new b1(i8, (char) i9, (short) i10), j4);
        a(new b2((int) ((11952684725848L ^ j2) >>> 32), (char) ((r15 << 32) >>> 48), (short) ((r15 << 48) >>> 48)), j4);
        a(new b3(j2 ^ 62780391060449L), j4);
        a(new b4(j2 ^ 108322402406948L), j4);
        a(new b5(j2 ^ 4673690923313L), j4);
        a(new b6((char) i14, j12), j4);
        a(new b7(11755833293349L ^ j2), j4);
        a(new b8(j2 ^ 99181293662063L), j4);
        a(new b9(j2 ^ 70299337852955L), j4);
        a(new b_(j2 ^ 60703932104845L), j4);
        a(new ca((short) i11, (short) i12, i13), j4);
        a(new cb(i, (short) i2, (short) i3), j4);
        a(new cc(11591322374583L ^ j2), j4);
        a(new cd(7971168107189L ^ j2), j4);
        a(new ce(j2 ^ 29452747146848L), j4);
        a(new cf(j2 ^ 99898083871935L), j4);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean lambda$get$0(Class cls, bm bmVar) {
        return bmVar.getClass() == cls;
    }

    public <T extends bm> T a(final Class<T> cls) {
        return (T) this.a.stream().filter(new Predicate() { // from class: com.trossense.ch$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ch.lambda$get$0(cls, (bm) obj);
            }
        }).findAny().orElse(null);
    }

    /* JADX WARN: Removed duplicated region for block: B:10:? A[LOOP:0: B:2:0x0012->B:10:?, LOOP_END, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:11:0x0030 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0024  */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0023 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x002d  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:13:0x0024 -> B:5:0x0025). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.util.List a(com.trossense.b r6, long r7) {
        /*
            r5 = this;
            long r0 = com.trossense.ch.b
            long r7 = r7 ^ r0
            java.lang.String[] r0 = com.trossense.bm.m()
            java.util.ArrayList r1 = new java.util.ArrayList
            r1.<init>()
            java.util.List<com.trossense.bm> r2 = r5.a
            java.util.Iterator r2 = r2.iterator()
        L12:
            boolean r3 = r2.hasNext()
            if (r3 == 0) goto L1d
            java.lang.Object r3 = r2.next()
            goto L25
        L1d:
            r3 = 0
            int r3 = (r7 > r3 ? 1 : (r7 == r3 ? 0 : -1))
            if (r3 < 0) goto L24
            return r1
        L24:
            r3 = r1
        L25:
            com.trossense.bm r3 = (com.trossense.bm) r3
            com.trossense.b r4 = r3.c()
            if (r4 != r6) goto L30
            r1.add(r3)
        L30:
            if (r0 != 0) goto L12
            goto L1d
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.ch.a(com.trossense.b, long):java.util.List");
    }

    public void a(bm bmVar, long j) {
        long j2 = (j ^ b) ^ 138025020912194L;
        this.a.add(bmVar);
        TrosSense.INSTANCE.d().b((char) (j2 >>> 48), (int) ((j2 << 16) >>> 32), (int) ((j2 << 48) >>> 48), bmVar);
    }

    public void b(bm bmVar) {
        this.a.remove(bmVar);
        TrosSense.INSTANCE.d().a(bmVar);
    }

    public List<bm> c() {
        return this.a;
    }
}
