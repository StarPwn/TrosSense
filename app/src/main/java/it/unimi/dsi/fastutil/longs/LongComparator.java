package it.unimi.dsi.fastutil.longs;

import java.util.Comparator;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface LongComparator extends Comparator<Long> {
    int compare(long j, long j2);

    @Override // java.util.Comparator
    /* renamed from: reversed */
    default Comparator<Long> reversed2() {
        return LongComparators.oppositeComparator(this);
    }

    @Override // java.util.Comparator
    @Deprecated
    default int compare(Long ok1, Long ok2) {
        return compare(ok1.longValue(), ok2.longValue());
    }

    default LongComparator thenComparing(LongComparator second) {
        return new LongComparator$$ExternalSyntheticLambda0(this, second);
    }

    static /* synthetic */ int lambda$thenComparing$3d6e68ef$1(LongComparator _this, LongComparator second, long k1, long k2) {
        int comp = _this.compare(k1, k2);
        return comp == 0 ? second.compare(k1, k2) : comp;
    }

    @Override // java.util.Comparator
    default Comparator<Long> thenComparing(Comparator<? super Long> second) {
        return second instanceof LongComparator ? thenComparing((LongComparator) second) : super.thenComparing(second);
    }
}
