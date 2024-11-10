package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.Pair;
import java.util.Comparator;

/* loaded from: classes4.dex */
public interface LongShortPair extends Pair<Long, Short> {
    long leftLong();

    short rightShort();

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Long left() {
        return Long.valueOf(leftLong());
    }

    default LongShortPair left(long l) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default LongShortPair left(Long l) {
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

    default LongShortPair first(long l) {
        return left(l);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default LongShortPair first(Long l) {
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

    default LongShortPair key(long l) {
        return left(l);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default LongShortPair key(Long l) {
        return key(l.longValue());
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Short right() {
        return Short.valueOf(rightShort());
    }

    default LongShortPair right(short r) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default LongShortPair right(Short l) {
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

    default LongShortPair second(short r) {
        return right(r);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default LongShortPair second(Short l) {
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

    default LongShortPair value(short r) {
        return right(r);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default LongShortPair value(Short l) {
        return value(l.shortValue());
    }

    static LongShortPair of(long left, short right) {
        return new LongShortImmutablePair(left, right);
    }

    static Comparator<LongShortPair> lexComparator() {
        return new Comparator() { // from class: it.unimi.dsi.fastutil.longs.LongShortPair$$ExternalSyntheticLambda0
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return LongShortPair.lambda$lexComparator$0((LongShortPair) obj, (LongShortPair) obj2);
            }
        };
    }

    static /* synthetic */ int lambda$lexComparator$0(LongShortPair x, LongShortPair y) {
        int t = Long.compare(x.leftLong(), y.leftLong());
        return t != 0 ? t : Short.compare(x.rightShort(), y.rightShort());
    }
}
