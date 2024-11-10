package it.unimi.dsi.fastutil.ints;

import java.util.Comparator;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface IntComparator extends Comparator<Integer> {
    int compare(int i, int i2);

    @Override // java.util.Comparator
    /* renamed from: reversed */
    default Comparator<Integer> reversed2() {
        return IntComparators.oppositeComparator(this);
    }

    @Override // java.util.Comparator
    @Deprecated
    default int compare(Integer ok1, Integer ok2) {
        return compare(ok1.intValue(), ok2.intValue());
    }

    default IntComparator thenComparing(IntComparator second) {
        return new IntComparator$$ExternalSyntheticLambda0(this, second);
    }

    static /* synthetic */ int lambda$thenComparing$931d6fed$1(IntComparator _this, IntComparator second, int k1, int k2) {
        int comp = _this.compare(k1, k2);
        return comp == 0 ? second.compare(k1, k2) : comp;
    }

    @Override // java.util.Comparator
    default Comparator<Integer> thenComparing(Comparator<? super Integer> second) {
        return second instanceof IntComparator ? thenComparing((IntComparator) second) : super.thenComparing(second);
    }
}
