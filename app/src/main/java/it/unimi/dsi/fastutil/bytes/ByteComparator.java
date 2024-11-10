package it.unimi.dsi.fastutil.bytes;

import java.util.Comparator;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface ByteComparator extends Comparator<Byte> {
    int compare(byte b, byte b2);

    @Override // java.util.Comparator
    /* renamed from: reversed */
    default Comparator<Byte> reversed2() {
        return ByteComparators.oppositeComparator(this);
    }

    @Override // java.util.Comparator
    @Deprecated
    default int compare(Byte ok1, Byte ok2) {
        return compare(ok1.byteValue(), ok2.byteValue());
    }

    default ByteComparator thenComparing(ByteComparator second) {
        return new ByteComparator$$ExternalSyntheticLambda0(this, second);
    }

    static /* synthetic */ int lambda$thenComparing$6e387fbf$1(ByteComparator _this, ByteComparator second, byte k1, byte k2) {
        int comp = _this.compare(k1, k2);
        return comp == 0 ? second.compare(k1, k2) : comp;
    }

    @Override // java.util.Comparator
    default Comparator<Byte> thenComparing(Comparator<? super Byte> second) {
        return second instanceof ByteComparator ? thenComparing((ByteComparator) second) : super.thenComparing(second);
    }
}
