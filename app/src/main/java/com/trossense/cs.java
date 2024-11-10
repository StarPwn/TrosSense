package com.trossense;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/* loaded from: classes3.dex */
public class cs extends co<Map<cu, Boolean>> {
    private static final long k = dj.a(4319969156101651311L, 4410211711385536647L, MethodHandles.lookup().lookupClass()).a(26059614389969L);
    private ArrayList<cu> i;
    private ArrayList<Boolean> j;

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public cs(java.lang.String r12, java.lang.String r13, com.trossense.bm r14, int r15, int r16, java.util.List r17, byte r18, java.util.List r19) {
        /*
            r11 = this;
            r0 = r15
            long r0 = (long) r0
            r2 = 32
            long r0 = r0 << r2
            r3 = r16
            long r3 = (long) r3
            r5 = 40
            long r3 = r3 << r5
            long r2 = r3 >>> r2
            long r0 = r0 | r2
            r2 = r18
            long r2 = (long) r2
            r4 = 56
            long r2 = r2 << r4
            long r2 = r2 >>> r4
            long r0 = r0 | r2
            long r2 = com.trossense.cs.k
            long r0 = r0 ^ r2
            r2 = 82061275452617(0x4aa2612c54c9, double:4.05436570550536E-310)
            long r7 = r0 ^ r2
            r2 = 86733849550308(0x4ee24c4adde4, double:4.28522153943704E-310)
            long r0 = r0 ^ r2
            r2 = r17
            r3 = r19
            java.util.LinkedHashMap r10 = a(r0, r2, r3)
            r4 = r11
            r5 = r12
            r6 = r13
            r9 = r14
            r4.<init>(r5, r6, r7, r9, r10)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.cs.<init>(java.lang.String, java.lang.String, com.trossense.bm, int, int, java.util.List, byte, java.util.List):void");
    }

    private static LinkedHashMap a(long j, List list, List list2) {
        long j2 = j ^ k;
        boolean h = co.h();
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        Iterator it2 = list.iterator();
        while (it2.hasNext()) {
            cu cuVar = (cu) it2.next();
            if (!h) {
                break;
            }
            boolean contains = list2.contains(cuVar.b);
            if (j2 >= 0) {
                if (!contains) {
                    contains = list2.contains(cuVar.a);
                    if (j2 > 0) {
                        if (!contains) {
                            contains = false;
                        }
                    }
                }
                contains = true;
            }
            linkedHashMap.put(cuVar, Boolean.valueOf(contains));
            if (!h) {
                break;
            }
        }
        return linkedHashMap;
    }

    public cu a(int i) {
        return this.i.get(i);
    }

    public void a(long j, Map map) {
        super.a(map, (char) (r3 >>> 48), (((j ^ k) ^ 127102960439554L) << 16) >>> 16);
        j();
    }

    @Override // com.trossense.co
    public /* bridge */ /* synthetic */ void a(Object obj, char c, long j) {
        a((((j << 16) >>> 16) | (c << 48)) ^ 38235620442608L, (Map) obj);
    }

    public Boolean b(int i) {
        return this.j.get(i);
    }

    public List<cu> i() {
        return (List) ((Map) this.d).entrySet().stream().filter(new Predicate() { // from class: com.trossense.cs$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ((Boolean) ((Map.Entry) obj).getValue()).booleanValue();
            }
        }).map(new Function() { // from class: com.trossense.cs$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return (cu) ((Map.Entry) obj).getKey();
            }
        }).collect(Collectors.toList());
    }

    public void j() {
        this.i = new ArrayList<>(((Map) this.d).keySet());
        this.j = new ArrayList<>(((Map) this.d).values());
    }
}
