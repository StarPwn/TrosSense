package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.Pair;
import java.util.Comparator;

/* loaded from: classes4.dex */
public interface LongCharPair extends Pair<Long, Character> {
    long leftLong();

    char rightChar();

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Long left() {
        return Long.valueOf(leftLong());
    }

    default LongCharPair left(long l) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default LongCharPair left(Long l) {
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

    default LongCharPair first(long l) {
        return left(l);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default LongCharPair first(Long l) {
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

    default LongCharPair key(long l) {
        return left(l);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default LongCharPair key(Long l) {
        return key(l.longValue());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Character right() {
        return Character.valueOf(rightChar());
    }

    default LongCharPair right(char r) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default LongCharPair right(Character l) {
        return right(l.charValue());
    }

    default char secondChar() {
        return rightChar();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Character second() {
        return Character.valueOf(secondChar());
    }

    default LongCharPair second(char r) {
        return right(r);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default LongCharPair second(Character l) {
        return second(l.charValue());
    }

    default char valueChar() {
        return rightChar();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Character value() {
        return Character.valueOf(valueChar());
    }

    default LongCharPair value(char r) {
        return right(r);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default LongCharPair value(Character l) {
        return value(l.charValue());
    }

    static LongCharPair of(long left, char right) {
        return new LongCharImmutablePair(left, right);
    }

    static Comparator<LongCharPair> lexComparator() {
        return new Comparator() { // from class: it.unimi.dsi.fastutil.longs.LongCharPair$$ExternalSyntheticLambda0
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return LongCharPair.lambda$lexComparator$0((LongCharPair) obj, (LongCharPair) obj2);
            }
        };
    }

    static /* synthetic */ int lambda$lexComparator$0(LongCharPair x, LongCharPair y) {
        int t = Long.compare(x.leftLong(), y.leftLong());
        return t != 0 ? t : Character.compare(x.rightChar(), y.rightChar());
    }
}
