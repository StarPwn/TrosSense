package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.Pair;
import java.util.Comparator;

/* loaded from: classes4.dex */
public interface LongBooleanPair extends Pair<Long, Boolean> {
    long leftLong();

    boolean rightBoolean();

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Long left() {
        return Long.valueOf(leftLong());
    }

    default LongBooleanPair left(long l) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default LongBooleanPair left(Long l) {
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

    default LongBooleanPair first(long l) {
        return left(l);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default LongBooleanPair first(Long l) {
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

    default LongBooleanPair key(long l) {
        return left(l);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default LongBooleanPair key(Long l) {
        return key(l.longValue());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Boolean right() {
        return Boolean.valueOf(rightBoolean());
    }

    default LongBooleanPair right(boolean r) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default LongBooleanPair right(Boolean l) {
        return right(l.booleanValue());
    }

    default boolean secondBoolean() {
        return rightBoolean();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Boolean second() {
        return Boolean.valueOf(secondBoolean());
    }

    default LongBooleanPair second(boolean r) {
        return right(r);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default LongBooleanPair second(Boolean l) {
        return second(l.booleanValue());
    }

    default boolean valueBoolean() {
        return rightBoolean();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Boolean value() {
        return Boolean.valueOf(valueBoolean());
    }

    default LongBooleanPair value(boolean r) {
        return right(r);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default LongBooleanPair value(Boolean l) {
        return value(l.booleanValue());
    }

    static LongBooleanPair of(long left, boolean right) {
        return new LongBooleanImmutablePair(left, right);
    }

    static Comparator<LongBooleanPair> lexComparator() {
        return new Comparator() { // from class: it.unimi.dsi.fastutil.longs.LongBooleanPair$$ExternalSyntheticLambda0
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return LongBooleanPair.lambda$lexComparator$0((LongBooleanPair) obj, (LongBooleanPair) obj2);
            }
        };
    }

    static /* synthetic */ int lambda$lexComparator$0(LongBooleanPair x, LongBooleanPair y) {
        int t = Long.compare(x.leftLong(), y.leftLong());
        return t != 0 ? t : Boolean.compare(x.rightBoolean(), y.rightBoolean());
    }
}
