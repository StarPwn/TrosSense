package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.Pair;
import java.util.Comparator;

/* loaded from: classes4.dex */
public interface ObjectCharPair<K> extends Pair<K, Character> {
    char rightChar();

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Character right() {
        return Character.valueOf(rightChar());
    }

    default ObjectCharPair<K> right(char r) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default ObjectCharPair<K> right(Character l) {
        return right(l.charValue());
    }

    default char secondChar() {
        return rightChar();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Character second() {
        return Character.valueOf(secondChar());
    }

    default ObjectCharPair<K> second(char r) {
        return right(r);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default ObjectCharPair<K> second(Character l) {
        return second(l.charValue());
    }

    default char valueChar() {
        return rightChar();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Character value() {
        return Character.valueOf(valueChar());
    }

    default ObjectCharPair<K> value(char r) {
        return right(r);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default ObjectCharPair<K> value(Character l) {
        return value(l.charValue());
    }

    static <K> ObjectCharPair<K> of(K left, char right) {
        return new ObjectCharImmutablePair(left, right);
    }

    static <K> Comparator<ObjectCharPair<K>> lexComparator() {
        return new Comparator() { // from class: it.unimi.dsi.fastutil.objects.ObjectCharPair$$ExternalSyntheticLambda0
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return ObjectCharPair.lambda$lexComparator$0((ObjectCharPair) obj, (ObjectCharPair) obj2);
            }
        };
    }

    static /* synthetic */ int lambda$lexComparator$0(ObjectCharPair x, ObjectCharPair y) {
        int t = ((Comparable) x.left()).compareTo(y.left());
        return t != 0 ? t : Character.compare(x.rightChar(), y.rightChar());
    }
}
