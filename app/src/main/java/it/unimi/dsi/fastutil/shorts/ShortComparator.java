package it.unimi.dsi.fastutil.shorts;

import java.util.Comparator;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface ShortComparator extends Comparator<Short> {
    int compare(short s, short s2);

    @Override // java.util.Comparator
    /* renamed from: reversed */
    default Comparator<Short> reversed2() {
        return ShortComparators.oppositeComparator(this);
    }

    @Override // java.util.Comparator
    @Deprecated
    default int compare(Short ok1, Short ok2) {
        return compare(ok1.shortValue(), ok2.shortValue());
    }

    default ShortComparator thenComparing(ShortComparator second) {
        return new ShortComparator$$ExternalSyntheticLambda0(this, second);
    }

    static /* synthetic */ int lambda$thenComparing$953dd6d$1(ShortComparator _this, ShortComparator second, short k1, short k2) {
        int comp = _this.compare(k1, k2);
        return comp == 0 ? second.compare(k1, k2) : comp;
    }

    @Override // java.util.Comparator
    default Comparator<Short> thenComparing(Comparator<? super Short> second) {
        return second instanceof ShortComparator ? thenComparing((ShortComparator) second) : super.thenComparing(second);
    }
}
