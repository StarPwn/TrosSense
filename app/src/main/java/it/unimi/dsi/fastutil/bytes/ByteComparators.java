package it.unimi.dsi.fastutil.bytes;

import java.io.Serializable;
import java.util.Comparator;

/* loaded from: classes4.dex */
public final class ByteComparators {
    public static final ByteComparator NATURAL_COMPARATOR = new NaturalImplicitComparator();
    public static final ByteComparator OPPOSITE_COMPARATOR = new OppositeImplicitComparator();

    private ByteComparators() {
    }

    /* loaded from: classes4.dex */
    protected static class NaturalImplicitComparator implements ByteComparator, Serializable {
        private static final long serialVersionUID = 1;

        protected NaturalImplicitComparator() {
        }

        @Override // it.unimi.dsi.fastutil.bytes.ByteComparator
        public final int compare(byte a, byte b) {
            return Byte.compare(a, b);
        }

        @Override // it.unimi.dsi.fastutil.bytes.ByteComparator, java.util.Comparator
        /* renamed from: reversed */
        public Comparator<Byte> reversed2() {
            return ByteComparators.OPPOSITE_COMPARATOR;
        }

        private Object readResolve() {
            return ByteComparators.NATURAL_COMPARATOR;
        }
    }

    /* loaded from: classes4.dex */
    protected static class OppositeImplicitComparator implements ByteComparator, Serializable {
        private static final long serialVersionUID = 1;

        protected OppositeImplicitComparator() {
        }

        @Override // it.unimi.dsi.fastutil.bytes.ByteComparator
        public final int compare(byte a, byte b) {
            return -Byte.compare(a, b);
        }

        @Override // it.unimi.dsi.fastutil.bytes.ByteComparator, java.util.Comparator
        /* renamed from: reversed */
        public Comparator<Byte> reversed2() {
            return ByteComparators.NATURAL_COMPARATOR;
        }

        private Object readResolve() {
            return ByteComparators.OPPOSITE_COMPARATOR;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes4.dex */
    public static class OppositeComparator implements ByteComparator, Serializable {
        private static final long serialVersionUID = 1;
        final ByteComparator comparator;

        protected OppositeComparator(ByteComparator c) {
            this.comparator = c;
        }

        @Override // it.unimi.dsi.fastutil.bytes.ByteComparator
        public final int compare(byte a, byte b) {
            return this.comparator.compare(b, a);
        }

        @Override // it.unimi.dsi.fastutil.bytes.ByteComparator, java.util.Comparator
        /* renamed from: reversed */
        public final Comparator<Byte> reversed2() {
            return this.comparator;
        }
    }

    public static ByteComparator oppositeComparator(ByteComparator c) {
        return c instanceof OppositeComparator ? ((OppositeComparator) c).comparator : new OppositeComparator(c);
    }

    public static ByteComparator asByteComparator(final Comparator<? super Byte> c) {
        if (c == null || (c instanceof ByteComparator)) {
            return (ByteComparator) c;
        }
        return new ByteComparator() { // from class: it.unimi.dsi.fastutil.bytes.ByteComparators.1
            @Override // it.unimi.dsi.fastutil.bytes.ByteComparator
            public int compare(byte x, byte y) {
                return c.compare(Byte.valueOf(x), Byte.valueOf(y));
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // it.unimi.dsi.fastutil.bytes.ByteComparator, java.util.Comparator
            public int compare(Byte x, Byte y) {
                return c.compare(x, y);
            }
        };
    }
}
