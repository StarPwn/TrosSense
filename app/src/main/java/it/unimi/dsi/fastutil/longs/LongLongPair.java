package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.Pair;
import java.util.Comparator;

/* loaded from: classes4.dex */
public interface LongLongPair extends Pair<Long, Long> {
    long leftLong();

    long rightLong();

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Long left() {
        return Long.valueOf(leftLong());
    }

    default LongLongPair left(long l) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default LongLongPair left(Long l) {
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

    default LongLongPair first(long l) {
        return left(l);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default LongLongPair first(Long l) {
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

    default LongLongPair key(long l) {
        return left(l);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default LongLongPair key(Long l) {
        return key(l.longValue());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Long right() {
        return Long.valueOf(rightLong());
    }

    default LongLongPair right(long r) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default LongLongPair right(Long l) {
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

    default LongLongPair second(long r) {
        return right(r);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default LongLongPair second(Long l) {
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

    default LongLongPair value(long r) {
        return right(r);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default LongLongPair value(Long l) {
        return value(l.longValue());
    }

    static LongLongPair of(long left, long right) {
        return new LongLongImmutablePair(left, right);
    }

    static Comparator<LongLongPair> lexComparator() {
        return new Comparator() { // from class: it.unimi.dsi.fastutil.longs.LongLongPair$$ExternalSyntheticLambda0
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return LongLongPair.lambda$lexComparator$0((LongLongPair) obj, (LongLongPair) obj2);
            }
        };
    }

    static /* synthetic */ int lambda$lexComparator$0(LongLongPair x, LongLongPair y) {
        int t = Long.compare(x.leftLong(), y.leftLong());
        return t != 0 ? t : Long.compare(x.rightLong(), y.rightLong());
    }
}
