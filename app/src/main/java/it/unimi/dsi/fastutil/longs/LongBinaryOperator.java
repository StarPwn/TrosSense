package it.unimi.dsi.fastutil.longs;

import java.util.function.BinaryOperator;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface LongBinaryOperator extends BinaryOperator<Long>, java.util.function.LongBinaryOperator {
    long apply(long j, long j2);

    @Override // java.util.function.LongBinaryOperator
    @Deprecated
    default long applyAsLong(long x, long y) {
        return apply(x, y);
    }

    @Override // java.util.function.BiFunction
    @Deprecated
    default Long apply(Long x, Long y) {
        return Long.valueOf(apply(x.longValue(), y.longValue()));
    }
}
