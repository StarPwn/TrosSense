package it.unimi.dsi.fastutil.booleans;

import java.util.Comparator;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface BooleanComparator extends Comparator<Boolean> {
    int compare(boolean z, boolean z2);

    @Override // java.util.Comparator
    /* renamed from: reversed */
    default Comparator<Boolean> reversed2() {
        return BooleanComparators.oppositeComparator(this);
    }

    @Override // java.util.Comparator
    @Deprecated
    default int compare(Boolean ok1, Boolean ok2) {
        return compare(ok1.booleanValue(), ok2.booleanValue());
    }

    default BooleanComparator thenComparing(BooleanComparator second) {
        return new BooleanComparator$$ExternalSyntheticLambda0(this, second);
    }

    static /* synthetic */ int lambda$thenComparing$e8be742d$1(BooleanComparator _this, BooleanComparator second, boolean k1, boolean k2) {
        int comp = _this.compare(k1, k2);
        return comp == 0 ? second.compare(k1, k2) : comp;
    }

    @Override // java.util.Comparator
    default Comparator<Boolean> thenComparing(Comparator<? super Boolean> second) {
        return second instanceof BooleanComparator ? thenComparing((BooleanComparator) second) : super.thenComparing(second);
    }
}
