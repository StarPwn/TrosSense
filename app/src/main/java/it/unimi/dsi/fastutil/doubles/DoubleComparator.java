package it.unimi.dsi.fastutil.doubles;

import java.util.Comparator;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface DoubleComparator extends Comparator<Double> {
    int compare(double d, double d2);

    @Override // java.util.Comparator
    /* renamed from: reversed */
    default Comparator<Double> reversed2() {
        return DoubleComparators.oppositeComparator(this);
    }

    @Override // java.util.Comparator
    @Deprecated
    default int compare(Double ok1, Double ok2) {
        return compare(ok1.doubleValue(), ok2.doubleValue());
    }

    default DoubleComparator thenComparing(DoubleComparator second) {
        return new DoubleComparator$$ExternalSyntheticLambda0(this, second);
    }

    static /* synthetic */ int lambda$thenComparing$f8e9881b$1(DoubleComparator _this, DoubleComparator second, double k1, double k2) {
        int comp = _this.compare(k1, k2);
        return comp == 0 ? second.compare(k1, k2) : comp;
    }

    @Override // java.util.Comparator
    default Comparator<Double> thenComparing(Comparator<? super Double> second) {
        return second instanceof DoubleComparator ? thenComparing((DoubleComparator) second) : super.thenComparing(second);
    }
}
