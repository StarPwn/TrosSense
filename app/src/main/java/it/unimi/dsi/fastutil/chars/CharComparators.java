package it.unimi.dsi.fastutil.chars;

import java.io.Serializable;
import java.util.Comparator;

/* loaded from: classes4.dex */
public final class CharComparators {
    public static final CharComparator NATURAL_COMPARATOR = new NaturalImplicitComparator();
    public static final CharComparator OPPOSITE_COMPARATOR = new OppositeImplicitComparator();

    private CharComparators() {
    }

    /* loaded from: classes4.dex */
    protected static class NaturalImplicitComparator implements CharComparator, Serializable {
        private static final long serialVersionUID = 1;

        protected NaturalImplicitComparator() {
        }

        @Override // it.unimi.dsi.fastutil.chars.CharComparator
        public final int compare(char a, char b) {
            return Character.compare(a, b);
        }

        @Override // it.unimi.dsi.fastutil.chars.CharComparator, java.util.Comparator
        /* renamed from: reversed */
        public Comparator<Character> reversed2() {
            return CharComparators.OPPOSITE_COMPARATOR;
        }

        private Object readResolve() {
            return CharComparators.NATURAL_COMPARATOR;
        }
    }

    /* loaded from: classes4.dex */
    protected static class OppositeImplicitComparator implements CharComparator, Serializable {
        private static final long serialVersionUID = 1;

        protected OppositeImplicitComparator() {
        }

        @Override // it.unimi.dsi.fastutil.chars.CharComparator
        public final int compare(char a, char b) {
            return -Character.compare(a, b);
        }

        @Override // it.unimi.dsi.fastutil.chars.CharComparator, java.util.Comparator
        /* renamed from: reversed */
        public Comparator<Character> reversed2() {
            return CharComparators.NATURAL_COMPARATOR;
        }

        private Object readResolve() {
            return CharComparators.OPPOSITE_COMPARATOR;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes4.dex */
    public static class OppositeComparator implements CharComparator, Serializable {
        private static final long serialVersionUID = 1;
        final CharComparator comparator;

        protected OppositeComparator(CharComparator c) {
            this.comparator = c;
        }

        @Override // it.unimi.dsi.fastutil.chars.CharComparator
        public final int compare(char a, char b) {
            return this.comparator.compare(b, a);
        }

        @Override // it.unimi.dsi.fastutil.chars.CharComparator, java.util.Comparator
        /* renamed from: reversed */
        public final Comparator<Character> reversed2() {
            return this.comparator;
        }
    }

    public static CharComparator oppositeComparator(CharComparator c) {
        return c instanceof OppositeComparator ? ((OppositeComparator) c).comparator : new OppositeComparator(c);
    }

    public static CharComparator asCharComparator(final Comparator<? super Character> c) {
        if (c == null || (c instanceof CharComparator)) {
            return (CharComparator) c;
        }
        return new CharComparator() { // from class: it.unimi.dsi.fastutil.chars.CharComparators.1
            @Override // it.unimi.dsi.fastutil.chars.CharComparator
            public int compare(char x, char y) {
                return c.compare(Character.valueOf(x), Character.valueOf(y));
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // it.unimi.dsi.fastutil.chars.CharComparator, java.util.Comparator
            public int compare(Character x, Character y) {
                return c.compare(x, y);
            }
        };
    }
}
