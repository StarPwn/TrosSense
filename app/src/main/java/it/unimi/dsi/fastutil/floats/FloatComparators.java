package it.unimi.dsi.fastutil.floats;

import java.io.Serializable;
import java.util.Comparator;

/* loaded from: classes4.dex */
public final class FloatComparators {
    public static final FloatComparator NATURAL_COMPARATOR = new NaturalImplicitComparator();
    public static final FloatComparator OPPOSITE_COMPARATOR = new OppositeImplicitComparator();

    private FloatComparators() {
    }

    /* loaded from: classes4.dex */
    protected static class NaturalImplicitComparator implements FloatComparator, Serializable {
        private static final long serialVersionUID = 1;

        protected NaturalImplicitComparator() {
        }

        @Override // it.unimi.dsi.fastutil.floats.FloatComparator
        public final int compare(float a, float b) {
            return Float.compare(a, b);
        }

        @Override // it.unimi.dsi.fastutil.floats.FloatComparator, java.util.Comparator
        /* renamed from: reversed */
        public Comparator<Float> reversed2() {
            return FloatComparators.OPPOSITE_COMPARATOR;
        }

        private Object readResolve() {
            return FloatComparators.NATURAL_COMPARATOR;
        }
    }

    /* loaded from: classes4.dex */
    protected static class OppositeImplicitComparator implements FloatComparator, Serializable {
        private static final long serialVersionUID = 1;

        protected OppositeImplicitComparator() {
        }

        @Override // it.unimi.dsi.fastutil.floats.FloatComparator
        public final int compare(float a, float b) {
            return -Float.compare(a, b);
        }

        @Override // it.unimi.dsi.fastutil.floats.FloatComparator, java.util.Comparator
        /* renamed from: reversed */
        public Comparator<Float> reversed2() {
            return FloatComparators.NATURAL_COMPARATOR;
        }

        private Object readResolve() {
            return FloatComparators.OPPOSITE_COMPARATOR;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes4.dex */
    public static class OppositeComparator implements FloatComparator, Serializable {
        private static final long serialVersionUID = 1;
        final FloatComparator comparator;

        protected OppositeComparator(FloatComparator c) {
            this.comparator = c;
        }

        @Override // it.unimi.dsi.fastutil.floats.FloatComparator
        public final int compare(float a, float b) {
            return this.comparator.compare(b, a);
        }

        @Override // it.unimi.dsi.fastutil.floats.FloatComparator, java.util.Comparator
        /* renamed from: reversed */
        public final Comparator<Float> reversed2() {
            return this.comparator;
        }
    }

    public static FloatComparator oppositeComparator(FloatComparator c) {
        return c instanceof OppositeComparator ? ((OppositeComparator) c).comparator : new OppositeComparator(c);
    }

    public static FloatComparator asFloatComparator(final Comparator<? super Float> c) {
        if (c == null || (c instanceof FloatComparator)) {
            return (FloatComparator) c;
        }
        return new FloatComparator() { // from class: it.unimi.dsi.fastutil.floats.FloatComparators.1
            @Override // it.unimi.dsi.fastutil.floats.FloatComparator
            public int compare(float x, float y) {
                return c.compare(Float.valueOf(x), Float.valueOf(y));
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // it.unimi.dsi.fastutil.floats.FloatComparator, java.util.Comparator
            public int compare(Float x, Float y) {
                return c.compare(x, y);
            }
        };
    }
}
