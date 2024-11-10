package it.unimi.dsi.fastutil;

import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import java.util.Comparator;

/* loaded from: classes4.dex */
public interface Pair<L, R> {
    L left();

    R right();

    default Pair<L, R> left(L l) {
        throw new UnsupportedOperationException();
    }

    default Pair<L, R> right(R r) {
        throw new UnsupportedOperationException();
    }

    default L first() {
        return left();
    }

    default R second() {
        return right();
    }

    default Pair<L, R> first(L l) {
        return left(l);
    }

    default Pair<L, R> second(R r) {
        return right(r);
    }

    default Pair<L, R> key(L l) {
        return left(l);
    }

    default Pair<L, R> value(R r) {
        return right(r);
    }

    default L key() {
        return left();
    }

    default R value() {
        return right();
    }

    static <L, R> Pair<L, R> of(L l, R r) {
        return new ObjectObjectImmutablePair(l, r);
    }

    static <L, R> Comparator<Pair<L, R>> lexComparator() {
        return new Comparator() { // from class: it.unimi.dsi.fastutil.Pair$$ExternalSyntheticLambda0
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return Pair.lambda$lexComparator$0((Pair) obj, (Pair) obj2);
            }
        };
    }

    static /* synthetic */ int lambda$lexComparator$0(Pair x, Pair y) {
        int t = ((Comparable) x.left()).compareTo(y.left());
        return t != 0 ? t : ((Comparable) x.right()).compareTo(y.right());
    }
}
