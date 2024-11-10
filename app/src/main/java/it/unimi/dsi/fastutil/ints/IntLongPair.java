package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.Pair;
import java.util.Comparator;

/* loaded from: classes4.dex */
public interface IntLongPair extends Pair<Integer, Long> {
    int leftInt();

    long rightLong();

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Integer left() {
        return Integer.valueOf(leftInt());
    }

    default IntLongPair left(int l) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default IntLongPair left(Integer l) {
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

    default IntLongPair first(int l) {
        return left(l);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default IntLongPair first(Integer l) {
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

    default IntLongPair key(int l) {
        return left(l);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default IntLongPair key(Integer l) {
        return key(l.intValue());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Long right() {
        return Long.valueOf(rightLong());
    }

    default IntLongPair right(long r) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default IntLongPair right(Long l) {
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

    default IntLongPair second(long r) {
        return right(r);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default IntLongPair second(Long l) {
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

    default IntLongPair value(long r) {
        return right(r);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default IntLongPair value(Long l) {
        return value(l.longValue());
    }

    static IntLongPair of(int left, long right) {
        return new IntLongImmutablePair(left, right);
    }

    static Comparator<IntLongPair> lexComparator() {
        return new Comparator() { // from class: it.unimi.dsi.fastutil.ints.IntLongPair$$ExternalSyntheticLambda0
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return IntLongPair.lambda$lexComparator$0((IntLongPair) obj, (IntLongPair) obj2);
            }
        };
    }

    static /* synthetic */ int lambda$lexComparator$0(IntLongPair x, IntLongPair y) {
        int t = Integer.compare(x.leftInt(), y.leftInt());
        return t != 0 ? t : Long.compare(x.rightLong(), y.rightLong());
    }
}
