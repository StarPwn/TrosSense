package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.Pair;
import java.util.Comparator;

/* loaded from: classes4.dex */
public interface LongIntPair extends Pair<Long, Integer> {
    long leftLong();

    int rightInt();

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Long left() {
        return Long.valueOf(leftLong());
    }

    default LongIntPair left(long l) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default LongIntPair left(Long l) {
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

    default LongIntPair first(long l) {
        return left(l);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default LongIntPair first(Long l) {
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

    default LongIntPair key(long l) {
        return left(l);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default LongIntPair key(Long l) {
        return key(l.longValue());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Integer right() {
        return Integer.valueOf(rightInt());
    }

    default LongIntPair right(int r) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default LongIntPair right(Integer l) {
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

    default LongIntPair second(int r) {
        return right(r);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default LongIntPair second(Integer l) {
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

    default LongIntPair value(int r) {
        return right(r);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default LongIntPair value(Integer l) {
        return value(l.intValue());
    }

    static LongIntPair of(long left, int right) {
        return new LongIntImmutablePair(left, right);
    }

    static Comparator<LongIntPair> lexComparator() {
        return new Comparator() { // from class: it.unimi.dsi.fastutil.longs.LongIntPair$$ExternalSyntheticLambda0
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return LongIntPair.lambda$lexComparator$0((LongIntPair) obj, (LongIntPair) obj2);
            }
        };
    }

    static /* synthetic */ int lambda$lexComparator$0(LongIntPair x, LongIntPair y) {
        int t = Long.compare(x.leftLong(), y.leftLong());
        return t != 0 ? t : Integer.compare(x.rightInt(), y.rightInt());
    }
}
