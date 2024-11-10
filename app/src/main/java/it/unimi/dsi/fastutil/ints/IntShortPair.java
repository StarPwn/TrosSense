package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.Pair;
import java.util.Comparator;

/* loaded from: classes4.dex */
public interface IntShortPair extends Pair<Integer, Short> {
    int leftInt();

    short rightShort();

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Integer left() {
        return Integer.valueOf(leftInt());
    }

    default IntShortPair left(int l) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default IntShortPair left(Integer l) {
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

    default IntShortPair first(int l) {
        return left(l);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default IntShortPair first(Integer l) {
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

    default IntShortPair key(int l) {
        return left(l);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default IntShortPair key(Integer l) {
        return key(l.intValue());
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Short right() {
        return Short.valueOf(rightShort());
    }

    default IntShortPair right(short r) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default IntShortPair right(Short l) {
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

    default IntShortPair second(short r) {
        return right(r);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default IntShortPair second(Short l) {
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

    default IntShortPair value(short r) {
        return right(r);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default IntShortPair value(Short l) {
        return value(l.shortValue());
    }

    static IntShortPair of(int left, short right) {
        return new IntShortImmutablePair(left, right);
    }

    static Comparator<IntShortPair> lexComparator() {
        return new Comparator() { // from class: it.unimi.dsi.fastutil.ints.IntShortPair$$ExternalSyntheticLambda0
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return IntShortPair.lambda$lexComparator$0((IntShortPair) obj, (IntShortPair) obj2);
            }
        };
    }

    static /* synthetic */ int lambda$lexComparator$0(IntShortPair x, IntShortPair y) {
        int t = Integer.compare(x.leftInt(), y.leftInt());
        return t != 0 ? t : Short.compare(x.rightShort(), y.rightShort());
    }
}
