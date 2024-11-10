package it.unimi.dsi.fastutil.longs;

import java.util.function.UnaryOperator;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface LongUnaryOperator extends UnaryOperator<Long>, java.util.function.LongUnaryOperator {
    long apply(long j);

    static LongUnaryOperator identity() {
        return new LongUnaryOperator() { // from class: it.unimi.dsi.fastutil.longs.LongUnaryOperator$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.longs.LongUnaryOperator
            public final long apply(long j) {
                return LongUnaryOperator.lambda$identity$0(j);
            }
        };
    }

    static /* synthetic */ long lambda$identity$0(long i) {
        return i;
    }

    static /* synthetic */ long lambda$negation$1(long i) {
        return -i;
    }

    static LongUnaryOperator negation() {
        return new LongUnaryOperator() { // from class: it.unimi.dsi.fastutil.longs.LongUnaryOperator$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.longs.LongUnaryOperator
            public final long apply(long j) {
                return LongUnaryOperator.lambda$negation$1(j);
            }
        };
    }

    @Override // java.util.function.LongUnaryOperator
    @Deprecated
    default long applyAsLong(long x) {
        return apply(x);
    }

    @Override // java.util.function.Function
    @Deprecated
    default Long apply(Long x) {
        return Long.valueOf(apply(x.longValue()));
    }
}
