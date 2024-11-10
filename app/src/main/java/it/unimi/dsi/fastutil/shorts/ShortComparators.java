package it.unimi.dsi.fastutil.shorts;

import java.io.Serializable;
import java.util.Comparator;

/* loaded from: classes4.dex */
public final class ShortComparators {
    public static final ShortComparator NATURAL_COMPARATOR = new NaturalImplicitComparator();
    public static final ShortComparator OPPOSITE_COMPARATOR = new OppositeImplicitComparator();

    private ShortComparators() {
    }

    /* loaded from: classes4.dex */
    protected static class NaturalImplicitComparator implements ShortComparator, Serializable {
        private static final long serialVersionUID = 1;

        protected NaturalImplicitComparator() {
        }

        @Override // it.unimi.dsi.fastutil.shorts.ShortComparator
        public final int compare(short a, short b) {
            return Short.compare(a, b);
        }

        @Override // it.unimi.dsi.fastutil.shorts.ShortComparator, java.util.Comparator
        /* renamed from: reversed */
        public Comparator<Short> reversed2() {
            return ShortComparators.OPPOSITE_COMPARATOR;
        }

        private Object readResolve() {
            return ShortComparators.NATURAL_COMPARATOR;
        }
    }

    /* loaded from: classes4.dex */
    protected static class OppositeImplicitComparator implements ShortComparator, Serializable {
        private static final long serialVersionUID = 1;

        protected OppositeImplicitComparator() {
        }

        @Override // it.unimi.dsi.fastutil.shorts.ShortComparator
        public final int compare(short a, short b) {
            return -Short.compare(a, b);
        }

        @Override // it.unimi.dsi.fastutil.shorts.ShortComparator, java.util.Comparator
        /* renamed from: reversed */
        public Comparator<Short> reversed2() {
            return ShortComparators.NATURAL_COMPARATOR;
        }

        private Object readResolve() {
            return ShortComparators.OPPOSITE_COMPARATOR;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes4.dex */
    public static class OppositeComparator implements ShortComparator, Serializable {
        private static final long serialVersionUID = 1;
        final ShortComparator comparator;

        protected OppositeComparator(ShortComparator c) {
            this.comparator = c;
        }

        @Override // it.unimi.dsi.fastutil.shorts.ShortComparator
        public final int compare(short a, short b) {
            return this.comparator.compare(b, a);
        }

        @Override // it.unimi.dsi.fastutil.shorts.ShortComparator, java.util.Comparator
        /* renamed from: reversed */
        public final Comparator<Short> reversed2() {
            return this.comparator;
        }
    }

    public static ShortComparator oppositeComparator(ShortComparator c) {
        return c instanceof OppositeComparator ? ((OppositeComparator) c).comparator : new OppositeComparator(c);
    }

    public static ShortComparator asShortComparator(final Comparator<? super Short> c) {
        if (c == null || (c instanceof ShortComparator)) {
            return (ShortComparator) c;
        }
        return new ShortComparator() { // from class: it.unimi.dsi.fastutil.shorts.ShortComparators.1
            @Override // it.unimi.dsi.fastutil.shorts.ShortComparator
            public int compare(short x, short y) {
                return c.compare(Short.valueOf(x), Short.valueOf(y));
            }

            @Override // it.unimi.dsi.fastutil.shorts.ShortComparator, java.util.Comparator
            public int compare(Short x, Short y) {
                return c.compare(x, y);
            }
        };
    }
}
