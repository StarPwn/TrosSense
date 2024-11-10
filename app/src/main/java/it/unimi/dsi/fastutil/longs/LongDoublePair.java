package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.Pair;
import java.util.Comparator;

/* loaded from: classes4.dex */
public interface LongDoublePair extends Pair<Long, Double> {
    long leftLong();

    double rightDouble();

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Long left() {
        return Long.valueOf(leftLong());
    }

    default LongDoublePair left(long l) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default LongDoublePair left(Long l) {
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

    default LongDoublePair first(long l) {
        return left(l);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default LongDoublePair first(Long l) {
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

    default LongDoublePair key(long l) {
        return left(l);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default LongDoublePair key(Long l) {
        return key(l.longValue());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Double right() {
        return Double.valueOf(rightDouble());
    }

    default LongDoublePair right(double r) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default LongDoublePair right(Double l) {
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

    default LongDoublePair second(double r) {
        return right(r);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default LongDoublePair second(Double l) {
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

    default LongDoublePair value(double r) {
        return right(r);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default LongDoublePair value(Double l) {
        return value(l.doubleValue());
    }

    static LongDoublePair of(long left, double right) {
        return new LongDoubleImmutablePair(left, right);
    }

    static Comparator<LongDoublePair> lexComparator() {
        return new Comparator() { // from class: it.unimi.dsi.fastutil.longs.LongDoublePair$$ExternalSyntheticLambda0
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return LongDoublePair.lambda$lexComparator$0((LongDoublePair) obj, (LongDoublePair) obj2);
            }
        };
    }

    static /* synthetic */ int lambda$lexComparator$0(LongDoublePair x, LongDoublePair y) {
        int t = Long.compare(x.leftLong(), y.leftLong());
        return t != 0 ? t : Double.compare(x.rightDouble(), y.rightDouble());
    }
}
