package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.Pair;
import java.util.Comparator;

/* loaded from: classes4.dex */
public interface ObjectDoublePair<K> extends Pair<K, Double> {
    double rightDouble();

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Double right() {
        return Double.valueOf(rightDouble());
    }

    default ObjectDoublePair<K> right(double r) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default ObjectDoublePair<K> right(Double l) {
        return right(l.doubleValue());
    }

    default double secondDouble() {
        return rightDouble();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Double second() {
        return Double.valueOf(secondDouble());
    }

    default ObjectDoublePair<K> second(double r) {
        return right(r);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default ObjectDoublePair<K> second(Double l) {
        return second(l.doubleValue());
    }

    default double valueDouble() {
        return rightDouble();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Double value() {
        return Double.valueOf(valueDouble());
    }

    default ObjectDoublePair<K> value(double r) {
        return right(r);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default ObjectDoublePair<K> value(Double l) {
        return value(l.doubleValue());
    }

    static <K> ObjectDoublePair<K> of(K left, double right) {
        return new ObjectDoubleImmutablePair(left, right);
    }

    static <K> Comparator<ObjectDoublePair<K>> lexComparator() {
        return new Comparator() { // from class: it.unimi.dsi.fastutil.objects.ObjectDoublePair$$ExternalSyntheticLambda0
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return ObjectDoublePair.lambda$lexComparator$0((ObjectDoublePair) obj, (ObjectDoublePair) obj2);
            }
        };
    }

    static /* synthetic */ int lambda$lexComparator$0(ObjectDoublePair x, ObjectDoublePair y) {
        int t = ((Comparable) x.left()).compareTo(y.left());
        return t != 0 ? t : Double.compare(x.rightDouble(), y.rightDouble());
    }
}
