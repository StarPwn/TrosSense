package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.Pair;
import java.util.Comparator;

/* loaded from: classes4.dex */
public interface ObjectLongPair<K> extends Pair<K, Long> {
    long rightLong();

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Long right() {
        return Long.valueOf(rightLong());
    }

    default ObjectLongPair<K> right(long r) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default ObjectLongPair<K> right(Long l) {
        return right(l.longValue());
    }

    default long secondLong() {
        return rightLong();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Long second() {
        return Long.valueOf(secondLong());
    }

    default ObjectLongPair<K> second(long r) {
        return right(r);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default ObjectLongPair<K> second(Long l) {
        return second(l.longValue());
    }

    default long valueLong() {
        return rightLong();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Long value() {
        return Long.valueOf(valueLong());
    }

    default ObjectLongPair<K> value(long r) {
        return right(r);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default ObjectLongPair<K> value(Long l) {
        return value(l.longValue());
    }

    static <K> ObjectLongPair<K> of(K left, long right) {
        return new ObjectLongImmutablePair(left, right);
    }

    static <K> Comparator<ObjectLongPair<K>> lexComparator() {
        return new Comparator() { // from class: it.unimi.dsi.fastutil.objects.ObjectLongPair$$ExternalSyntheticLambda0
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return ObjectLongPair.lambda$lexComparator$0((ObjectLongPair) obj, (ObjectLongPair) obj2);
            }
        };
    }

    static /* synthetic */ int lambda$lexComparator$0(ObjectLongPair x, ObjectLongPair y) {
        int t = ((Comparable) x.left()).compareTo(y.left());
        return t != 0 ? t : Long.compare(x.rightLong(), y.rightLong());
    }
}
