package com.trossense;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.function.ToIntFunction;

/* loaded from: classes3.dex */
public class bi {
    private static final long c = dj.a(1500344749930720616L, -5420335478412211497L, MethodHandles.lookup().lookupClass()).a(106617912154578L);
    public final HashMap<Class<?>, CopyOnWriteArrayList<bh>> a;
    public List<bh> b;

    public bi(short s, int i, short s2) {
        long j = ((((s2 << 48) >>> 48) | ((s << 48) | ((i << 32) >>> 16))) ^ c) ^ 114413516059203L;
        this.a = new HashMap<>();
        this.b = new ArrayList();
        a(j);
    }

    private void a(long j) {
        long j2 = j ^ c;
        long j3 = 6794696088142L ^ j2;
        long j4 = j2 ^ 103361504144347L;
        int i = (int) (j4 >>> 48);
        int i2 = (int) ((j4 << 16) >>> 32);
        int i3 = (int) ((j4 << 48) >>> 48);
        char c2 = (char) i;
        b(c2, i2, i3, new a4());
        b(c2, i2, i3, new a7(j3));
        b(c2, i2, i3, new a8());
        b(c2, i2, i3, new a5());
        b(c2, i2, i3, new cn());
        b(c2, i2, i3, new a6());
    }

    private void b() {
        this.b.clear();
        this.a.values().forEach(new Consumer() { // from class: com.trossense.bi$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                bi.this.m178lambda$sortHandler$0$comtrossensebi((CopyOnWriteArrayList) obj);
            }
        });
        this.b.sort(Comparator.comparingInt(new ToIntFunction() { // from class: com.trossense.bi$$ExternalSyntheticLambda1
            @Override // java.util.function.ToIntFunction
            public final int applyAsInt(Object obj) {
                return ((bh) obj).d();
            }
        }));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void lambda$callEvent$1(a9 a9Var, bh bhVar) {
        if (bhVar.b().equals(a9Var.getClass())) {
            Method a = bhVar.a();
            Object c2 = bhVar.c();
            a.setAccessible(true);
            if (!(c2 instanceof bm) || ((bm) c2).l()) {
                try {
                    a.invoke(c2, a9Var);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public synchronized void a(final a9 a9Var) {
        this.b.forEach(new Consumer() { // from class: com.trossense.bi$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                bi.lambda$callEvent$1(a9.this, (bh) obj);
            }
        });
    }

    public synchronized void a(Object obj) {
        this.a.remove(obj.getClass());
        b();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:13:0x00ca A[LOOP:0: B:2:0x0033->B:13:0x00ca, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:14:0x00ce A[EDGE_INSN: B:14:0x00ce->B:15:0x00ce BREAK  A[LOOP:0: B:2:0x0033->B:13:0x00ca], SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void b(char r20, int r21, int r22, java.lang.Object r23) {
        /*
            r19 = this;
            r0 = r19
            r1 = r20
            r2 = r21
            long r3 = (long) r1
            r5 = 48
            long r3 = r3 << r5
            long r6 = (long) r2
            r8 = 32
            long r6 = r6 << r8
            r8 = 16
            long r6 = r6 >>> r8
            long r3 = r3 | r6
            r6 = r22
            long r6 = (long) r6
            long r6 = r6 << r5
            long r5 = r6 >>> r5
            long r3 = r3 | r5
            long r5 = com.trossense.bi.c
            long r3 = r3 ^ r5
            r5 = 108427443414979(0x629d3b3fa7c3, double:5.3570274857737E-310)
            long r3 = r3 ^ r5
            java.lang.Class r5 = r23.getClass()
            java.lang.String r6 = com.trossense.a9.b()
            java.lang.reflect.Method[] r14 = r5.getDeclaredMethods()
            int r15 = r14.length
            r16 = 0
            r13 = r16
        L33:
            if (r13 >= r15) goto Lce
            r8 = r14[r13]
            if (r6 != 0) goto Ld1
            java.lang.Class[] r7 = r8.getParameterTypes()
            int r7 = r7.length
            if (r2 < 0) goto L50
            r9 = 1
            if (r7 != r9) goto L4a
            java.lang.Class<com.trossense.bk> r7 = com.trossense.bk.class
            boolean r7 = r8.isAnnotationPresent(r7)
            goto L50
        L4a:
            r18 = r13
            r22 = r14
            goto Lc5
        L50:
            if (r2 < 0) goto L60
            if (r7 == 0) goto L4a
            java.lang.Class<com.trossense.a9> r7 = com.trossense.a9.class
            java.lang.Class[] r9 = r8.getParameterTypes()
            r9 = r9[r16]
            boolean r7 = r7.isAssignableFrom(r9)
        L60:
            if (r7 == 0) goto L4a
            java.lang.Class<com.trossense.bk> r7 = com.trossense.bk.class
            java.lang.annotation.Annotation r7 = r8.getAnnotation(r7)
            com.trossense.bk r7 = (com.trossense.bk) r7
            com.trossense.bh r11 = new com.trossense.bh
            java.lang.Class[] r9 = r8.getParameterTypes()
            r9 = r9[r16]
            byte r17 = r7.value()
            r7 = r11
            r10 = r23
            r22 = r14
            r14 = r11
            r11 = r3
            r18 = r13
            r13 = r17
            r7.<init>(r8, r9, r10, r11, r13)
            java.util.HashMap<java.lang.Class<?>, java.util.concurrent.CopyOnWriteArrayList<com.trossense.bh>> r7 = r0.a
            if (r1 < 0) goto Lb9
            boolean r7 = r7.containsKey(r5)
            if (r7 == 0) goto Lb7
            java.util.HashMap<java.lang.Class<?>, java.util.concurrent.CopyOnWriteArrayList<com.trossense.bh>> r7 = r0.a
            java.lang.Object r7 = r7.get(r5)
            java.util.concurrent.CopyOnWriteArrayList r7 = (java.util.concurrent.CopyOnWriteArrayList) r7
            boolean r7 = r7.contains(r14)
            if (r2 <= 0) goto La9
            if (r7 != 0) goto Lc5
            java.util.HashMap<java.lang.Class<?>, java.util.concurrent.CopyOnWriteArrayList<com.trossense.bh>> r7 = r0.a
            java.lang.Object r7 = r7.get(r5)
            java.util.concurrent.CopyOnWriteArrayList r7 = (java.util.concurrent.CopyOnWriteArrayList) r7
            r7.add(r14)
        La9:
            if (r1 < 0) goto Lb4
            if (r6 == 0) goto Lc5
            r7 = 3
            int[] r7 = new int[r7]
            com.trossense.sdk.PointerHolder.b(r7)
            goto Lb7
        Lb4:
            r13 = r18
            goto Lc7
        Lb7:
            java.util.HashMap<java.lang.Class<?>, java.util.concurrent.CopyOnWriteArrayList<com.trossense.bh>> r7 = r0.a
        Lb9:
            java.util.concurrent.CopyOnWriteArrayList r8 = new java.util.concurrent.CopyOnWriteArrayList
            java.util.List r9 = java.util.Collections.singletonList(r14)
            r8.<init>(r9)
            r7.put(r5, r8)
        Lc5:
            int r13 = r18 + 1
        Lc7:
            if (r6 == 0) goto Lca
            goto Lce
        Lca:
            r14 = r22
            goto L33
        Lce:
            r19.b()
        Ld1:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.bi.b(char, int, int, java.lang.Object):void");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$sortHandler$0$com-trossense-bi, reason: not valid java name */
    public void m178lambda$sortHandler$0$comtrossensebi(CopyOnWriteArrayList copyOnWriteArrayList) {
        this.b.addAll(copyOnWriteArrayList);
    }
}
