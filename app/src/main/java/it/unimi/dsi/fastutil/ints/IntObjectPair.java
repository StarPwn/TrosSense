package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.Pair;
import java.util.Comparator;

/* loaded from: classes4.dex */
public interface IntObjectPair<V> extends Pair<Integer, V> {
    int leftInt();

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Integer left() {
        return Integer.valueOf(leftInt());
    }

    default IntObjectPair<V> left(int l) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default IntObjectPair<V> left(Integer l) {
        return left(l.intValue());
    }

    default int firstInt() {
        return leftInt();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Integer first() {
        return Integer.valueOf(firstInt());
    }

    default IntObjectPair<V> first(int l) {
        return left(l);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default IntObjectPair<V> first(Integer l) {
        return first(l.intValue());
    }

    default int keyInt() {
        return firstInt();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Integer key() {
        return Integer.valueOf(keyInt());
    }

    default IntObjectPair<V> key(int l) {
        return left(l);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default IntObjectPair<V> key(Integer l) {
        return key(l.intValue());
    }

    static <V> IntObjectPair<V> of(int left, V right) {
        return new IntObjectImmutablePair(left, right);
    }

    static <V> Comparator<IntObjectPair<V>> lexComparator() {
        return new Comparator() { // from class: it.unimi.dsi.fastutil.ints.IntObjectPair$$ExternalSyntheticLambda0
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return IntObjectPair.lambda$lexComparator$0((IntObjectPair) obj, (IntObjectPair) obj2);
            }
        };
    }

    static /* synthetic */ int lambda$lexComparator$0(IntObjectPair x, IntObjectPair y) {
        int t = Integer.compare(x.leftInt(), y.leftInt());
        return t != 0 ? t : ((Comparable) x.right()).compareTo(y.right());
    }
}
