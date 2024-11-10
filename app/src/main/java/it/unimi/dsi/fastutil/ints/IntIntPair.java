package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.Pair;
import java.util.Comparator;

/* loaded from: classes4.dex */
public interface IntIntPair extends Pair<Integer, Integer> {
    int leftInt();

    int rightInt();

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Integer left() {
        return Integer.valueOf(leftInt());
    }

    default IntIntPair left(int l) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default IntIntPair left(Integer l) {
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

    default IntIntPair first(int l) {
        return left(l);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default IntIntPair first(Integer l) {
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

    default IntIntPair key(int l) {
        return left(l);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default IntIntPair key(Integer l) {
        return key(l.intValue());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Integer right() {
        return Integer.valueOf(rightInt());
    }

    default IntIntPair right(int r) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default IntIntPair right(Integer l) {
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

    default IntIntPair second(int r) {
        return right(r);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default IntIntPair second(Integer l) {
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

    default IntIntPair value(int r) {
        return right(r);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default IntIntPair value(Integer l) {
        return value(l.intValue());
    }

    static IntIntPair of(int left, int right) {
        return new IntIntImmutablePair(left, right);
    }

    static Comparator<IntIntPair> lexComparator() {
        return new Comparator() { // from class: it.unimi.dsi.fastutil.ints.IntIntPair$$ExternalSyntheticLambda0
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return IntIntPair.lambda$lexComparator$0((IntIntPair) obj, (IntIntPair) obj2);
            }
        };
    }

    static /* synthetic */ int lambda$lexComparator$0(IntIntPair x, IntIntPair y) {
        int t = Integer.compare(x.leftInt(), y.leftInt());
        return t != 0 ? t : Integer.compare(x.rightInt(), y.rightInt());
    }
}
