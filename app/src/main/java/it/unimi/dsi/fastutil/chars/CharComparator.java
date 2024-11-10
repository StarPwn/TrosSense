package it.unimi.dsi.fastutil.chars;

import java.util.Comparator;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface CharComparator extends Comparator<Character> {
    int compare(char c, char c2);

    @Override // java.util.Comparator
    /* renamed from: reversed */
    default Comparator<Character> reversed2() {
        return CharComparators.oppositeComparator(this);
    }

    @Override // java.util.Comparator
    @Deprecated
    default int compare(Character ok1, Character ok2) {
        return compare(ok1.charValue(), ok2.charValue());
    }

    default CharComparator thenComparing(CharComparator second) {
        return new CharComparator$$ExternalSyntheticLambda0(this, second);
    }

    static /* synthetic */ int lambda$thenComparing$2b1ecd07$1(CharComparator _this, CharComparator second, char k1, char k2) {
        int comp = _this.compare(k1, k2);
        return comp == 0 ? second.compare(k1, k2) : comp;
    }

    @Override // java.util.Comparator
    default Comparator<Character> thenComparing(Comparator<? super Character> second) {
        return second instanceof CharComparator ? thenComparing((CharComparator) second) : super.thenComparing(second);
    }
}
