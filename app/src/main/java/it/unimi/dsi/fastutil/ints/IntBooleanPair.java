package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.Pair;
import java.util.Comparator;

/* loaded from: classes4.dex */
public interface IntBooleanPair extends Pair<Integer, Boolean> {
    int leftInt();

    boolean rightBoolean();

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Integer left() {
        return Integer.valueOf(leftInt());
    }

    default IntBooleanPair left(int l) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default IntBooleanPair left(Integer l) {
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

    default IntBooleanPair first(int l) {
        return left(l);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default IntBooleanPair first(Integer l) {
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

    default IntBooleanPair key(int l) {
        return left(l);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default IntBooleanPair key(Integer l) {
        return key(l.intValue());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Boolean right() {
        return Boolean.valueOf(rightBoolean());
    }

    default IntBooleanPair right(boolean r) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default IntBooleanPair right(Boolean l) {
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

    default IntBooleanPair second(boolean r) {
        return right(r);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default IntBooleanPair second(Boolean l) {
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

    default IntBooleanPair value(boolean r) {
        return right(r);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default IntBooleanPair value(Boolean l) {
        return value(l.booleanValue());
    }

    static IntBooleanPair of(int left, boolean right) {
        return new IntBooleanImmutablePair(left, right);
    }

    static Comparator<IntBooleanPair> lexComparator() {
        return new Comparator() { // from class: it.unimi.dsi.fastutil.ints.IntBooleanPair$$ExternalSyntheticLambda0
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return IntBooleanPair.lambda$lexComparator$0((IntBooleanPair) obj, (IntBooleanPair) obj2);
            }
        };
    }

    static /* synthetic */ int lambda$lexComparator$0(IntBooleanPair x, IntBooleanPair y) {
        int t = Integer.compare(x.leftInt(), y.leftInt());
        return t != 0 ? t : Boolean.compare(x.rightBoolean(), y.rightBoolean());
    }
}
