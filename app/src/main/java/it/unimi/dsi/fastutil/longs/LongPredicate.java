package it.unimi.dsi.fastutil.longs;

import java.util.Objects;
import java.util.function.Predicate;

@FunctionalInterface
/* loaded from: classes4.dex */
public interface LongPredicate extends Predicate<Long>, java.util.function.LongPredicate {
    @Override // java.util.function.Predicate
    @Deprecated
    default boolean test(Long t) {
        return test(t.longValue());
    }

    @Override // java.util.function.LongPredicate
    default LongPredicate and(final java.util.function.LongPredicate other) {
        Objects.requireNonNull(other);
        return new LongPredicate() { // from class: it.unimi.dsi.fastutil.longs.LongPredicate$$ExternalSyntheticLambda2
            @Override // java.util.function.LongPredicate
            public final boolean test(long j) {
                return LongPredicate.lambda$and$0(LongPredicate.this, other, j);
            }
        };
    }

    static /* synthetic */ boolean lambda$and$0(LongPredicate _this, java.util.function.LongPredicate other, long t) {
        return _this.test(t) && other.test(t);
    }

    default LongPredicate and(LongPredicate other) {
        return and((java.util.function.LongPredicate) other);
    }

    @Override // java.util.function.Predicate
    @Deprecated
    default Predicate<Long> and(Predicate<? super Long> other) {
        return super.and(other);
    }

    static /* synthetic */ boolean lambda$negate$1(LongPredicate _this, long t) {
        return !_this.test(t);
    }

    @Override // java.util.function.Predicate, java.util.function.LongPredicate
    default LongPredicate negate() {
        return new LongPredicate() { // from class: it.unimi.dsi.fastutil.longs.LongPredicate$$ExternalSyntheticLambda1
            @Override // java.util.function.LongPredicate
            public final boolean test(long j) {
                return LongPredicate.lambda$negate$1(LongPredicate.this, j);
            }
        };
    }

    @Override // java.util.function.LongPredicate
    default LongPredicate or(final java.util.function.LongPredicate other) {
        Objects.requireNonNull(other);
        return new LongPredicate() { // from class: it.unimi.dsi.fastutil.longs.LongPredicate$$ExternalSyntheticLambda0
            @Override // java.util.function.LongPredicate
            public final boolean test(long j) {
                return LongPredicate.lambda$or$2(LongPredicate.this, other, j);
            }
        };
    }

    static /* synthetic */ boolean lambda$or$2(LongPredicate _this, java.util.function.LongPredicate other, long t) {
        return _this.test(t) || other.test(t);
    }

    default LongPredicate or(LongPredicate other) {
        return or((java.util.function.LongPredicate) other);
    }

    @Override // java.util.function.Predicate
    @Deprecated
    default Predicate<Long> or(Predicate<? super Long> other) {
        return super.or(other);
    }
}
