package com.google.common.collect;

import java.io.Serializable;
import java.lang.Comparable;
import java.math.BigInteger;
import java.util.NoSuchElementException;

/* loaded from: classes.dex */
public abstract class DiscreteDomain<C extends Comparable> {
    public abstract long distance(C c, C c2);

    public abstract C next(C c);

    public abstract C previous(C c);

    public static DiscreteDomain<Integer> integers() {
        return IntegerDomain.INSTANCE;
    }

    /* loaded from: classes.dex */
    private static final class IntegerDomain extends DiscreteDomain<Integer> implements Serializable {
        private static final IntegerDomain INSTANCE = new IntegerDomain();
        private static final long serialVersionUID = 0;

        private IntegerDomain() {
        }

        @Override // com.google.common.collect.DiscreteDomain
        public Integer next(Integer value) {
            int i = value.intValue();
            if (i == Integer.MAX_VALUE) {
                return null;
            }
            return Integer.valueOf(i + 1);
        }

        @Override // com.google.common.collect.DiscreteDomain
        public Integer previous(Integer value) {
            int i = value.intValue();
            if (i == Integer.MIN_VALUE) {
                return null;
            }
            return Integer.valueOf(i - 1);
        }

        @Override // com.google.common.collect.DiscreteDomain
        public long distance(Integer start, Integer end) {
            return end.intValue() - start.intValue();
        }

        @Override // com.google.common.collect.DiscreteDomain
        public Integer minValue() {
            return Integer.MIN_VALUE;
        }

        @Override // com.google.common.collect.DiscreteDomain
        public Integer maxValue() {
            return Integer.MAX_VALUE;
        }

        private Object readResolve() {
            return INSTANCE;
        }

        public String toString() {
            return "DiscreteDomain.integers()";
        }
    }

    public static DiscreteDomain<Long> longs() {
        return LongDomain.INSTANCE;
    }

    /* loaded from: classes.dex */
    private static final class LongDomain extends DiscreteDomain<Long> implements Serializable {
        private static final LongDomain INSTANCE = new LongDomain();
        private static final long serialVersionUID = 0;

        private LongDomain() {
        }

        @Override // com.google.common.collect.DiscreteDomain
        public Long next(Long value) {
            long l = value.longValue();
            if (l == Long.MAX_VALUE) {
                return null;
            }
            return Long.valueOf(1 + l);
        }

        @Override // com.google.common.collect.DiscreteDomain
        public Long previous(Long value) {
            long l = value.longValue();
            if (l == Long.MIN_VALUE) {
                return null;
            }
            return Long.valueOf(l - 1);
        }

        @Override // com.google.common.collect.DiscreteDomain
        public long distance(Long start, Long end) {
            long result = end.longValue() - start.longValue();
            if (end.longValue() > start.longValue() && result < 0) {
                return Long.MAX_VALUE;
            }
            if (end.longValue() < start.longValue() && result > 0) {
                return Long.MIN_VALUE;
            }
            return result;
        }

        @Override // com.google.common.collect.DiscreteDomain
        public Long minValue() {
            return Long.MIN_VALUE;
        }

        @Override // com.google.common.collect.DiscreteDomain
        public Long maxValue() {
            return Long.MAX_VALUE;
        }

        private Object readResolve() {
            return INSTANCE;
        }

        public String toString() {
            return "DiscreteDomain.longs()";
        }
    }

    public static DiscreteDomain<BigInteger> bigIntegers() {
        return BigIntegerDomain.INSTANCE;
    }

    /* loaded from: classes.dex */
    private static final class BigIntegerDomain extends DiscreteDomain<BigInteger> implements Serializable {
        private static final long serialVersionUID = 0;
        private static final BigIntegerDomain INSTANCE = new BigIntegerDomain();
        private static final BigInteger MIN_LONG = BigInteger.valueOf(Long.MIN_VALUE);
        private static final BigInteger MAX_LONG = BigInteger.valueOf(Long.MAX_VALUE);

        private BigIntegerDomain() {
        }

        @Override // com.google.common.collect.DiscreteDomain
        public BigInteger next(BigInteger value) {
            return value.add(BigInteger.ONE);
        }

        @Override // com.google.common.collect.DiscreteDomain
        public BigInteger previous(BigInteger value) {
            return value.subtract(BigInteger.ONE);
        }

        @Override // com.google.common.collect.DiscreteDomain
        public long distance(BigInteger start, BigInteger end) {
            return end.subtract(start).max(MIN_LONG).min(MAX_LONG).longValue();
        }

        private Object readResolve() {
            return INSTANCE;
        }

        public String toString() {
            return "DiscreteDomain.bigIntegers()";
        }
    }

    protected DiscreteDomain() {
    }

    public C minValue() {
        throw new NoSuchElementException();
    }

    public C maxValue() {
        throw new NoSuchElementException();
    }
}
