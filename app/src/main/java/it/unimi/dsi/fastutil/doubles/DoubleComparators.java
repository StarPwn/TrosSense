package it.unimi.dsi.fastutil.doubles;

import java.io.Serializable;
import java.util.Comparator;

/* loaded from: classes4.dex */
public final class DoubleComparators {
    public static final DoubleComparator NATURAL_COMPARATOR = new NaturalImplicitComparator();
    public static final DoubleComparator OPPOSITE_COMPARATOR = new OppositeImplicitComparator();

    private DoubleComparators() {
    }

    /* loaded from: classes4.dex */
    protected static class NaturalImplicitComparator implements DoubleComparator, Serializable {
        private static final long serialVersionUID = 1;

        protected NaturalImplicitComparator() {
        }

        @Override // it.unimi.dsi.fastutil.doubles.DoubleComparator
        public final int compare(double a, double b) {
            return Double.compare(a, b);
        }

        @Override // it.unimi.dsi.fastutil.doubles.DoubleComparator, java.util.Comparator
        /* renamed from: reversed */
        public Comparator<Double> reversed2() {
            return DoubleComparators.OPPOSITE_COMPARATOR;
        }

        private Object readResolve() {
            return DoubleComparators.NATURAL_COMPARATOR;
        }
    }

    /* loaded from: classes4.dex */
    protected static class OppositeImplicitComparator implements DoubleComparator, Serializable {
        private static final long serialVersionUID = 1;

        protected OppositeImplicitComparator() {
        }

        @Override // it.unimi.dsi.fastutil.doubles.DoubleComparator
        public final int compare(double a, double b) {
            return -Double.compare(a, b);
        }

        @Override // it.unimi.dsi.fastutil.doubles.DoubleComparator, java.util.Comparator
        /* renamed from: reversed */
        public Comparator<Double> reversed2() {
            return DoubleComparators.NATURAL_COMPARATOR;
        }

        private Object readResolve() {
            return DoubleComparators.OPPOSITE_COMPARATOR;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes4.dex */
    public static class OppositeComparator implements DoubleComparator, Serializable {
        private static final long serialVersionUID = 1;
        final DoubleComparator comparator;

        protected OppositeComparator(DoubleComparator c) {
            this.comparator = c;
        }

        @Override // it.unimi.dsi.fastutil.doubles.DoubleComparator
        public final int compare(double a, double b) {
            return this.comparator.compare(b, a);
        }

        @Override // it.unimi.dsi.fastutil.doubles.DoubleComparator, java.util.Comparator
        /* renamed from: reversed */
        public final Comparator<Double> reversed2() {
            return this.comparator;
        }
    }

    public static DoubleComparator oppositeComparator(DoubleComparator c) {
        return c instanceof OppositeComparator ? ((OppositeComparator) c).comparator : new OppositeComparator(c);
    }

    public static DoubleComparator asDoubleComparator(final Comparator<? super Double> c) {
        if (c == null || (c instanceof DoubleComparator)) {
            return (DoubleComparator) c;
        }
        return new DoubleComparator() { // from class: it.unimi.dsi.fastutil.doubles.DoubleComparators.1
            @Override // it.unimi.dsi.fastutil.doubles.DoubleComparator
            public int compare(double x, double y) {
                return c.compare(Double.valueOf(x), Double.valueOf(y));
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // it.unimi.dsi.fastutil.doubles.DoubleComparator, java.util.Comparator
            public int compare(Double x, Double y) {
                return c.compare(x, y);
            }
        };
    }
}
