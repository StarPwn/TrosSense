package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.Pair;
import java.util.Comparator;

/* loaded from: classes4.dex */
public interface IntCharPair extends Pair<Integer, Character> {
    int leftInt();

    char rightChar();

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Integer left() {
        return Integer.valueOf(leftInt());
    }

    default IntCharPair left(int l) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default IntCharPair left(Integer l) {
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

    default IntCharPair first(int l) {
        return left(l);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default IntCharPair first(Integer l) {
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

    default IntCharPair key(int l) {
        return left(l);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default IntCharPair key(Integer l) {
        return key(l.intValue());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Character right() {
        return Character.valueOf(rightChar());
    }

    default IntCharPair right(char r) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default IntCharPair right(Character l) {
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

    default IntCharPair second(char r) {
        return right(r);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default IntCharPair second(Character l) {
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

    default IntCharPair value(char r) {
        return right(r);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default IntCharPair value(Character l) {
        return value(l.charValue());
    }

    static IntCharPair of(int left, char right) {
        return new IntCharImmutablePair(left, right);
    }

    static Comparator<IntCharPair> lexComparator() {
        return new Comparator() { // from class: it.unimi.dsi.fastutil.ints.IntCharPair$$ExternalSyntheticLambda0
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return IntCharPair.lambda$lexComparator$0((IntCharPair) obj, (IntCharPair) obj2);
            }
        };
    }

    static /* synthetic */ int lambda$lexComparator$0(IntCharPair x, IntCharPair y) {
        int t = Integer.compare(x.leftInt(), y.leftInt());
        return t != 0 ? t : Character.compare(x.rightChar(), y.rightChar());
    }
}
