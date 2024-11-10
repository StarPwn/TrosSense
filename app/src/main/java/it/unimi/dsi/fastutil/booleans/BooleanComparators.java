package it.unimi.dsi.fastutil.booleans;

import java.io.Serializable;
import java.util.Comparator;

/* loaded from: classes4.dex */
public final class BooleanComparators {
    public static final BooleanComparator NATURAL_COMPARATOR = new NaturalImplicitComparator();
    public static final BooleanComparator OPPOSITE_COMPARATOR = new OppositeImplicitComparator();

    private BooleanComparators() {
    }

    /* loaded from: classes4.dex */
    protected static class NaturalImplicitComparator implements BooleanComparator, Serializable {
        private static final long serialVersionUID = 1;

        protected NaturalImplicitComparator() {
        }

        @Override // it.unimi.dsi.fastutil.booleans.BooleanComparator
        public final int compare(boolean a, boolean b) {
            return Boolean.compare(a, b);
        }

        @Override // it.unimi.dsi.fastutil.booleans.BooleanComparator, java.util.Comparator
        /* renamed from: reversed */
        public Comparator<Boolean> reversed2() {
            return BooleanComparators.OPPOSITE_COMPARATOR;
        }

        private Object readResolve() {
            return BooleanComparators.NATURAL_COMPARATOR;
        }
    }

    /* loaded from: classes4.dex */
    protected static class OppositeImplicitComparator implements BooleanComparator, Serializable {
        private static final long serialVersionUID = 1;

        protected OppositeImplicitComparator() {
        }

        @Override // it.unimi.dsi.fastutil.booleans.BooleanComparator
        public final int compare(boolean a, boolean b) {
            return -Boolean.compare(a, b);
        }

        @Override // it.unimi.dsi.fastutil.booleans.BooleanComparator, java.util.Comparator
        /* renamed from: reversed */
        public Comparator<Boolean> reversed2() {
            return BooleanComparators.NATURAL_COMPARATOR;
        }

        private Object readResolve() {
            return BooleanComparators.OPPOSITE_COMPARATOR;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes4.dex */
    public static class OppositeComparator implements BooleanComparator, Serializable {
        private static final long serialVersionUID = 1;
        final BooleanComparator comparator;

        protected OppositeComparator(BooleanComparator c) {
            this.comparator = c;
        }

        @Override // it.unimi.dsi.fastutil.booleans.BooleanComparator
        public final int compare(boolean a, boolean b) {
            return this.comparator.compare(b, a);
        }

        @Override // it.unimi.dsi.fastutil.booleans.BooleanComparator, java.util.Comparator
        /* renamed from: reversed */
        public final Comparator<Boolean> reversed2() {
            return this.comparator;
        }
    }

    public static BooleanComparator oppositeComparator(BooleanComparator c) {
        return c instanceof OppositeComparator ? ((OppositeComparator) c).comparator : new OppositeComparator(c);
    }

    public static BooleanComparator asBooleanComparator(final Comparator<? super Boolean> c) {
        if (c == null || (c instanceof BooleanComparator)) {
            return (BooleanComparator) c;
        }
        return new BooleanComparator() { // from class: it.unimi.dsi.fastutil.booleans.BooleanComparators.1
            @Override // it.unimi.dsi.fastutil.booleans.BooleanComparator
            public int compare(boolean x, boolean y) {
                return c.compare(Boolean.valueOf(x), Boolean.valueOf(y));
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // it.unimi.dsi.fastutil.booleans.BooleanComparator, java.util.Comparator
            public int compare(Boolean x, Boolean y) {
                return c.compare(x, y);
            }
        };
    }
}
