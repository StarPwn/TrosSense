package it.unimi.dsi.fastutil.floats;

import java.util.Comparator;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface FloatComparator extends Comparator<Float> {
    int compare(float f, float f2);

    @Override // java.util.Comparator
    /* renamed from: reversed */
    default Comparator<Float> reversed2() {
        return FloatComparators.oppositeComparator(this);
    }

    @Override // java.util.Comparator
    @Deprecated
    default int compare(Float ok1, Float ok2) {
        return compare(ok1.floatValue(), ok2.floatValue());
    }

    default FloatComparator thenComparing(FloatComparator second) {
        return new FloatComparator$$ExternalSyntheticLambda0(this, second);
    }

    static /* synthetic */ int lambda$thenComparing$99a1156d$1(FloatComparator _this, FloatComparator second, float k1, float k2) {
        int comp = _this.compare(k1, k2);
        return comp == 0 ? second.compare(k1, k2) : comp;
    }

    @Override // java.util.Comparator
    default Comparator<Float> thenComparing(Comparator<? super Float> second) {
        return second instanceof FloatComparator ? thenComparing((FloatComparator) second) : super.thenComparing(second);
    }
}
