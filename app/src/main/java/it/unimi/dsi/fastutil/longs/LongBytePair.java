package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.Pair;
import java.util.Comparator;

/* loaded from: classes4.dex */
public interface LongBytePair extends Pair<Long, Byte> {
    long leftLong();

    byte rightByte();

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Long left() {
        return Long.valueOf(leftLong());
    }

    default LongBytePair left(long l) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default LongBytePair left(Long l) {
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

    default LongBytePair first(long l) {
        return left(l);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default LongBytePair first(Long l) {
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

    default LongBytePair key(long l) {
        return left(l);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default LongBytePair key(Long l) {
        return key(l.longValue());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Byte right() {
        return Byte.valueOf(rightByte());
    }

    default LongBytePair right(byte r) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default LongBytePair right(Byte l) {
        return right(l.byteValue());
    }

    default byte secondByte() {
        return rightByte();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Byte second() {
        return Byte.valueOf(secondByte());
    }

    default LongBytePair second(byte r) {
        return right(r);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default LongBytePair second(Byte l) {
        return second(l.byteValue());
    }

    default byte valueByte() {
        return rightByte();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Byte value() {
        return Byte.valueOf(valueByte());
    }

    default LongBytePair value(byte r) {
        return right(r);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default LongBytePair value(Byte l) {
        return value(l.byteValue());
    }

    static LongBytePair of(long left, byte right) {
        return new LongByteImmutablePair(left, right);
    }

    static Comparator<LongBytePair> lexComparator() {
        return new Comparator() { // from class: it.unimi.dsi.fastutil.longs.LongBytePair$$ExternalSyntheticLambda0
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return LongBytePair.lambda$lexComparator$0((LongBytePair) obj, (LongBytePair) obj2);
            }
        };
    }

    static /* synthetic */ int lambda$lexComparator$0(LongBytePair x, LongBytePair y) {
        int t = Long.compare(x.leftLong(), y.leftLong());
        return t != 0 ? t : Byte.compare(x.rightByte(), y.rightByte());
    }
}
