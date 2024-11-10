package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.Pair;
import java.util.Comparator;

/* loaded from: classes4.dex */
public interface LongObjectPair<V> extends Pair<Long, V> {
    long leftLong();

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Long left() {
        return Long.valueOf(leftLong());
    }

    default LongObjectPair<V> left(long l) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default LongObjectPair<V> left(Long l) {
        return left(l.longValue());
    }

    default long firstLong() {
        return leftLong();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Long first() {
        return Long.valueOf(firstLong());
    }

    default LongObjectPair<V> first(long l) {
        return left(l);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default LongObjectPair<V> first(Long l) {
        return first(l.longValue());
    }

    default long keyLong() {
        return firstLong();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Long key() {
        return Long.valueOf(keyLong());
    }

    default LongObjectPair<V> key(long l) {
        return left(l);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default LongObjectPair<V> key(Long l) {
        return key(l.longValue());
    }

    static <V> LongObjectPair<V> of(long left, V right) {
        return new LongObjectImmutablePair(left, right);
    }

    static <V> Comparator<LongObjectPair<V>> lexComparator() {
        return new Comparator() { // from class: it.unimi.dsi.fastutil.longs.LongObjectPair$$ExternalSyntheticLambda0
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return LongObjectPair.lambda$lexComparator$0((LongObjectPair) obj, (LongObjectPair) obj2);
            }
        };
    }

    static /* synthetic */ int lambda$lexComparator$0(LongObjectPair x, LongObjectPair y) {
        int t = Long.compare(x.leftLong(), y.leftLong());
        return t != 0 ? t : ((Comparable) x.right()).compareTo(y.right());
    }
}
