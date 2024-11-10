package it.unimi.dsi.fastutil.ints;

import java.util.Objects;
import java.util.function.Predicate;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface IntPredicate extends Predicate<Integer>, java.util.function.IntPredicate {
    @Override // java.util.function.Predicate
    @Deprecated
    default boolean test(Integer t) {
        return test(t.intValue());
    }

    @Override // java.util.function.IntPredicate
    default IntPredicate and(final java.util.function.IntPredicate other) {
        Objects.requireNonNull(other);
        return new IntPredicate() { // from class: it.unimi.dsi.fastutil.ints.IntPredicate$$ExternalSyntheticLambda1
            @Override // java.util.function.IntPredicate
            public final boolean test(int i) {
                return IntPredicate.lambda$and$0(IntPredicate.this, other, i);
            }
        };
    }

    static /* synthetic */ boolean lambda$and$0(IntPredicate _this, java.util.function.IntPredicate other, int t) {
        return _this.test(t) && other.test(t);
    }

    default IntPredicate and(IntPredicate other) {
        return and((java.util.function.IntPredicate) other);
    }

    @Override // java.util.function.Predicate
    @Deprecated
    default Predicate<Integer> and(Predicate<? super Integer> other) {
        return super.and(other);
    }

    static /* synthetic */ boolean lambda$negate$1(IntPredicate _this, int t) {
        return !_this.test(t);
    }

    @Override // java.util.function.Predicate, java.util.function.IntPredicate
    default IntPredicate negate() {
        return new IntPredicate() { // from class: it.unimi.dsi.fastutil.ints.IntPredicate$$ExternalSyntheticLambda0
            @Override // java.util.function.IntPredicate
            public final boolean test(int i) {
                return IntPredicate.lambda$negate$1(IntPredicate.this, i);
            }
        };
    }

    @Override // java.util.function.IntPredicate
    default IntPredicate or(final java.util.function.IntPredicate other) {
        Objects.requireNonNull(other);
        return new IntPredicate() { // from class: it.unimi.dsi.fastutil.ints.IntPredicate$$ExternalSyntheticLambda2
            @Override // java.util.function.IntPredicate
            public final boolean test(int i) {
                return IntPredicate.lambda$or$2(IntPredicate.this, other, i);
            }
        };
    }

    static /* synthetic */ boolean lambda$or$2(IntPredicate _this, java.util.function.IntPredicate other, int t) {
        return _this.test(t) || other.test(t);
    }

    default IntPredicate or(IntPredicate other) {
        return or((java.util.function.IntPredicate) other);
    }

    @Override // java.util.function.Predicate
    @Deprecated
    default Predicate<Integer> or(Predicate<? super Integer> other) {
        return super.or(other);
    }
}
