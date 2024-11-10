package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.Pair;
import java.util.Comparator;

/* loaded from: classes4.dex */
public interface ObjectBooleanPair<K> extends Pair<K, Boolean> {
    boolean rightBoolean();

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Boolean right() {
        return Boolean.valueOf(rightBoolean());
    }

    default ObjectBooleanPair<K> right(boolean r) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default ObjectBooleanPair<K> right(Boolean l) {
        return right(l.booleanValue());
    }

    default boolean secondBoolean() {
        return rightBoolean();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Boolean second() {
        return Boolean.valueOf(secondBoolean());
    }

    default ObjectBooleanPair<K> second(boolean r) {
        return right(r);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default ObjectBooleanPair<K> second(Boolean l) {
        return second(l.booleanValue());
    }

    default boolean valueBoolean() {
        return rightBoolean();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Boolean value() {
        return Boolean.valueOf(valueBoolean());
    }

    default ObjectBooleanPair<K> value(boolean r) {
        return right(r);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default ObjectBooleanPair<K> value(Boolean l) {
        return value(l.booleanValue());
    }

    static <K> ObjectBooleanPair<K> of(K left, boolean right) {
        return new ObjectBooleanImmutablePair(left, right);
    }

    static <K> Comparator<ObjectBooleanPair<K>> lexComparator() {
        return new Comparator() { // from class: it.unimi.dsi.fastutil.objects.ObjectBooleanPair$$ExternalSyntheticLambda0
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return ObjectBooleanPair.lambda$lexComparator$0((ObjectBooleanPair) obj, (ObjectBooleanPair) obj2);
            }
        };
    }

    static /* synthetic */ int lambda$lexComparator$0(ObjectBooleanPair x, ObjectBooleanPair y) {
        int t = ((Comparable) x.left()).compareTo(y.left());
        return t != 0 ? t : Boolean.compare(x.rightBoolean(), y.rightBoolean());
    }
}
