package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.Pair;
import java.util.Comparator;

/* loaded from: classes4.dex */
public interface IntDoublePair extends Pair<Integer, Double> {
    int leftInt();

    double rightDouble();

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Integer left() {
        return Integer.valueOf(leftInt());
    }

    default IntDoublePair left(int l) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default IntDoublePair left(Integer l) {
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

    default IntDoublePair first(int l) {
        return left(l);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default IntDoublePair first(Integer l) {
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

    default IntDoublePair key(int l) {
        return left(l);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default IntDoublePair key(Integer l) {
        return key(l.intValue());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Double right() {
        return Double.valueOf(rightDouble());
    }

    default IntDoublePair right(double r) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default IntDoublePair right(Double l) {
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

    default IntDoublePair second(double r) {
        return right(r);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default IntDoublePair second(Double l) {
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

    default IntDoublePair value(double r) {
        return right(r);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default IntDoublePair value(Double l) {
        return value(l.doubleValue());
    }

    static IntDoublePair of(int left, double right) {
        return new IntDoubleImmutablePair(left, right);
    }

    static Comparator<IntDoublePair> lexComparator() {
        return new Comparator() { // from class: it.unimi.dsi.fastutil.ints.IntDoublePair$$ExternalSyntheticLambda0
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return IntDoublePair.lambda$lexComparator$0((IntDoublePair) obj, (IntDoublePair) obj2);
            }
        };
    }

    static /* synthetic */ int lambda$lexComparator$0(IntDoublePair x, IntDoublePair y) {
        int t = Integer.compare(x.leftInt(), y.leftInt());
        return t != 0 ? t : Double.compare(x.rightDouble(), y.rightDouble());
    }
}
