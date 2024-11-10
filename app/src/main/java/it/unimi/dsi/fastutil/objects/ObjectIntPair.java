package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.Pair;
import java.util.Comparator;

/* loaded from: classes4.dex */
public interface ObjectIntPair<K> extends Pair<K, Integer> {
    int rightInt();

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Integer right() {
        return Integer.valueOf(rightInt());
    }

    default ObjectIntPair<K> right(int r) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default ObjectIntPair<K> right(Integer l) {
        return right(l.intValue());
    }

    default int secondInt() {
        return rightInt();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Integer second() {
        return Integer.valueOf(secondInt());
    }

    default ObjectIntPair<K> second(int r) {
        return right(r);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default ObjectIntPair<K> second(Integer l) {
        return second(l.intValue());
    }

    default int valueInt() {
        return rightInt();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Integer value() {
        return Integer.valueOf(valueInt());
    }

    default ObjectIntPair<K> value(int r) {
        return right(r);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default ObjectIntPair<K> value(Integer l) {
        return value(l.intValue());
    }

    static <K> ObjectIntPair<K> of(K left, int right) {
        return new ObjectIntImmutablePair(left, right);
    }

    static <K> Comparator<ObjectIntPair<K>> lexComparator() {
        return new Comparator() { // from class: it.unimi.dsi.fastutil.objects.ObjectIntPair$$ExternalSyntheticLambda0
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return ObjectIntPair.lambda$lexComparator$0((ObjectIntPair) obj, (ObjectIntPair) obj2);
            }
        };
    }

    static /* synthetic */ int lambda$lexComparator$0(ObjectIntPair x, ObjectIntPair y) {
        int t = ((Comparable) x.left()).compareTo(y.left());
        return t != 0 ? t : Integer.compare(x.rightInt(), y.rightInt());
    }
}
