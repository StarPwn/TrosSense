package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.Pair;
import java.util.Comparator;

/* loaded from: classes4.dex */
public interface ObjectShortPair<K> extends Pair<K, Short> {
    short rightShort();

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Short right() {
        return Short.valueOf(rightShort());
    }

    default ObjectShortPair<K> right(short r) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default ObjectShortPair<K> right(Short l) {
        return right(l.shortValue());
    }

    default short secondShort() {
        return rightShort();
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Short second() {
        return Short.valueOf(secondShort());
    }

    default ObjectShortPair<K> second(short r) {
        return right(r);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default ObjectShortPair<K> second(Short l) {
        return second(l.shortValue());
    }

    default short valueShort() {
        return rightShort();
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Short value() {
        return Short.valueOf(valueShort());
    }

    default ObjectShortPair<K> value(short r) {
        return right(r);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default ObjectShortPair<K> value(Short l) {
        return value(l.shortValue());
    }

    static <K> ObjectShortPair<K> of(K left, short right) {
        return new ObjectShortImmutablePair(left, right);
    }

    static <K> Comparator<ObjectShortPair<K>> lexComparator() {
        return new Comparator() { // from class: it.unimi.dsi.fastutil.objects.ObjectShortPair$$ExternalSyntheticLambda0
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return ObjectShortPair.lambda$lexComparator$0((ObjectShortPair) obj, (ObjectShortPair) obj2);
            }
        };
    }

    static /* synthetic */ int lambda$lexComparator$0(ObjectShortPair x, ObjectShortPair y) {
        int t = ((Comparable) x.left()).compareTo(y.left());
        return t != 0 ? t : Short.compare(x.rightShort(), y.rightShort());
    }
}
