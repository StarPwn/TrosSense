package it.unimi.dsi.fastutil.ints;

import java.util.function.UnaryOperator;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface IntUnaryOperator extends UnaryOperator<Integer>, java.util.function.IntUnaryOperator {
    int apply(int i);

    static IntUnaryOperator identity() {
        return new IntUnaryOperator() { // from class: it.unimi.dsi.fastutil.ints.IntUnaryOperator$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.ints.IntUnaryOperator
            public final int apply(int i) {
                return IntUnaryOperator.lambda$identity$0(i);
            }
        };
    }

    static /* synthetic */ int lambda$identity$0(int i) {
        return i;
    }

    static /* synthetic */ int lambda$negation$1(int i) {
        return -i;
    }

    static IntUnaryOperator negation() {
        return new IntUnaryOperator() { // from class: it.unimi.dsi.fastutil.ints.IntUnaryOperator$$ExternalSyntheticLambda1
            @Override // it.unimi.dsi.fastutil.ints.IntUnaryOperator
            public final int apply(int i) {
                return IntUnaryOperator.lambda$negation$1(i);
            }
        };
    }

    @Override // java.util.function.IntUnaryOperator
    @Deprecated
    default int applyAsInt(int x) {
        return apply(x);
    }

    @Override // java.util.function.Function
    @Deprecated
    default Integer apply(Integer x) {
        return Integer.valueOf(apply(x.intValue()));
    }
}
