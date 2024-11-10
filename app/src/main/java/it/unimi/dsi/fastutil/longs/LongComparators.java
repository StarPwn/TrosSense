package it.unimi.dsi.fastutil.longs;

import java.io.Serializable;
import java.util.Comparator;

/* loaded from: classes4.dex */
public final class LongComparators {
    public static final LongComparator NATURAL_COMPARATOR = new NaturalImplicitComparator();
    public static final LongComparator OPPOSITE_COMPARATOR = new OppositeImplicitComparator();

    private LongComparators() {
    }

    /* loaded from: classes4.dex */
    protected static class NaturalImplicitComparator implements LongComparator, Serializable {
        private static final long serialVersionUID = 1;

        protected NaturalImplicitComparator() {
        }

        @Override // it.unimi.dsi.fastutil.longs.LongComparator
        public final int compare(long a, long b) {
            return Long.compare(a, b);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongComparator, java.util.Comparator
        /* renamed from: reversed */
        public Comparator<Long> reversed2() {
            return LongComparators.OPPOSITE_COMPARATOR;
        }

        private Object readResolve() {
            return LongComparators.NATURAL_COMPARATOR;
        }
    }

    /* loaded from: classes4.dex */
    protected static class OppositeImplicitComparator implements LongComparator, Serializable {
        private static final long serialVersionUID = 1;

        protected OppositeImplicitComparator() {
        }

        @Override // it.unimi.dsi.fastutil.longs.LongComparator
        public final int compare(long a, long b) {
            return -Long.compare(a, b);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongComparator, java.util.Comparator
        /* renamed from: reversed */
        public Comparator<Long> reversed2() {
            return LongComparators.NATURAL_COMPARATOR;
        }

        private Object readResolve() {
            return LongComparators.OPPOSITE_COMPARATOR;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes4.dex */
    public static class OppositeComparator implements LongComparator, Serializable {
        private static final long serialVersionUID = 1;
        final LongComparator comparator;

        protected OppositeComparator(LongComparator c) {
            this.comparator = c;
        }

        @Override // it.unimi.dsi.fastutil.longs.LongComparator
        public final int compare(long a, long b) {
            return this.comparator.compare(b, a);
        }

        @Override // it.unimi.dsi.fastutil.longs.LongComparator, java.util.Comparator
        /* renamed from: reversed */
        public final Comparator<Long> reversed2() {
            return this.comparator;
        }
    }

    public static LongComparator oppositeComparator(LongComparator c) {
        return c instanceof OppositeComparator ? ((OppositeComparator) c).comparator : new OppositeComparator(c);
    }

    public static LongComparator asLongComparator(final Comparator<? super Long> c) {
        if (c == null || (c instanceof LongComparator)) {
            return (LongComparator) c;
        }
        return new LongComparator() { // from class: it.unimi.dsi.fastutil.longs.LongComparators.1
            @Override // it.unimi.dsi.fastutil.longs.LongComparator
            public int compare(long x, long y) {
                return c.compare(Long.valueOf(x), Long.valueOf(y));
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // it.unimi.dsi.fastutil.longs.LongComparator, java.util.Comparator
            public int compare(Long x, Long y) {
                return c.compare(x, y);
            }
        };
    }
}
