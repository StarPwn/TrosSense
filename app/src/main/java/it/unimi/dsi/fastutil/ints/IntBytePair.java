package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.Pair;
import java.util.Comparator;

/* loaded from: classes4.dex */
public interface IntBytePair extends Pair<Integer, Byte> {
    int leftInt();

    byte rightByte();

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Integer left() {
        return Integer.valueOf(leftInt());
    }

    default IntBytePair left(int l) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default IntBytePair left(Integer l) {
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

    default IntBytePair first(int l) {
        return left(l);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default IntBytePair first(Integer l) {
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

    default IntBytePair key(int l) {
        return left(l);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default IntBytePair key(Integer l) {
        return key(l.intValue());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Byte right() {
        return Byte.valueOf(rightByte());
    }

    default IntBytePair right(byte r) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default IntBytePair right(Byte l) {
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

    default IntBytePair second(byte r) {
        return right(r);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default IntBytePair second(Byte l) {
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

    default IntBytePair value(byte r) {
        return right(r);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default IntBytePair value(Byte l) {
        return value(l.byteValue());
    }

    static IntBytePair of(int left, byte right) {
        return new IntByteImmutablePair(left, right);
    }

    static Comparator<IntBytePair> lexComparator() {
        return new Comparator() { // from class: it.unimi.dsi.fastutil.ints.IntBytePair$$ExternalSyntheticLambda0
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return IntBytePair.lambda$lexComparator$0((IntBytePair) obj, (IntBytePair) obj2);
            }
        };
    }

    static /* synthetic */ int lambda$lexComparator$0(IntBytePair x, IntBytePair y) {
        int t = Integer.compare(x.leftInt(), y.leftInt());
        return t != 0 ? t : Byte.compare(x.rightByte(), y.rightByte());
    }
}
