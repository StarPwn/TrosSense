package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.Pair;
import java.util.Comparator;

/* loaded from: classes4.dex */
public interface LongFloatPair extends Pair<Long, Float> {
    long leftLong();

    float rightFloat();

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Long left() {
        return Long.valueOf(leftLong());
    }

    default LongFloatPair left(long l) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default LongFloatPair left(Long l) {
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

    default LongFloatPair first(long l) {
        return left(l);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default LongFloatPair first(Long l) {
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

    default LongFloatPair key(long l) {
        return left(l);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default LongFloatPair key(Long l) {
        return key(l.longValue());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Float right() {
        return Float.valueOf(rightFloat());
    }

    default LongFloatPair right(float r) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default LongFloatPair right(Float l) {
        return right(l.floatValue());
    }

    default float secondFloat() {
        return rightFloat();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Float second() {
        return Float.valueOf(secondFloat());
    }

    default LongFloatPair second(float r) {
        return right(r);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default LongFloatPair second(Float l) {
        return second(l.floatValue());
    }

    default float valueFloat() {
        return rightFloat();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Float value() {
        return Float.valueOf(valueFloat());
    }

    default LongFloatPair value(float r) {
        return right(r);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default LongFloatPair value(Float l) {
        return value(l.floatValue());
    }

    static LongFloatPair of(long left, float right) {
        return new LongFloatImmutablePair(left, right);
    }

    static Comparator<LongFloatPair> lexComparator() {
        return new Comparator() { // from class: it.unimi.dsi.fastutil.longs.LongFloatPair$$ExternalSyntheticLambda0
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return LongFloatPair.lambda$lexComparator$0((LongFloatPair) obj, (LongFloatPair) obj2);
            }
        };
    }

    static /* synthetic */ int lambda$lexComparator$0(LongFloatPair x, LongFloatPair y) {
        int t = Long.compare(x.leftLong(), y.leftLong());
        return t != 0 ? t : Float.compare(x.rightFloat(), y.rightFloat());
    }
}
