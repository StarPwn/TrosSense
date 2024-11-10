package it.unimi.dsi.fastutil.ints;

import java.util.function.BinaryOperator;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface IntBinaryOperator extends BinaryOperator<Integer>, java.util.function.IntBinaryOperator {
    int apply(int i, int i2);

    @Override // java.util.function.IntBinaryOperator
    @Deprecated
    default int applyAsInt(int x, int y) {
        return apply(x, y);
    }

    @Override // java.util.function.BiFunction
    @Deprecated
    default Integer apply(Integer x, Integer y) {
        return Integer.valueOf(apply(x.intValue(), y.intValue()));
    }
}
