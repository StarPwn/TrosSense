package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.Pair;
import java.util.Comparator;

/* loaded from: classes4.dex */
public interface IntFloatPair extends Pair<Integer, Float> {
    int leftInt();

    float rightFloat();

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Integer left() {
        return Integer.valueOf(leftInt());
    }

    default IntFloatPair left(int l) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default IntFloatPair left(Integer l) {
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

    default IntFloatPair first(int l) {
        return left(l);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default IntFloatPair first(Integer l) {
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

    default IntFloatPair key(int l) {
        return left(l);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default IntFloatPair key(Integer l) {
        return key(l.intValue());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Float right() {
        return Float.valueOf(rightFloat());
    }

    default IntFloatPair right(float r) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default IntFloatPair right(Float l) {
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

    default IntFloatPair second(float r) {
        return right(r);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default IntFloatPair second(Float l) {
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

    default IntFloatPair value(float r) {
        return right(r);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default IntFloatPair value(Float l) {
        return value(l.floatValue());
    }

    static IntFloatPair of(int left, float right) {
        return new IntFloatImmutablePair(left, right);
    }

    static Comparator<IntFloatPair> lexComparator() {
        return new Comparator() { // from class: it.unimi.dsi.fastutil.ints.IntFloatPair$$ExternalSyntheticLambda0
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return IntFloatPair.lambda$lexComparator$0((IntFloatPair) obj, (IntFloatPair) obj2);
            }
        };
    }

    static /* synthetic */ int lambda$lexComparator$0(IntFloatPair x, IntFloatPair y) {
        int t = Integer.compare(x.leftInt(), y.leftInt());
        return t != 0 ? t : Float.compare(x.rightFloat(), y.rightFloat());
    }
}
