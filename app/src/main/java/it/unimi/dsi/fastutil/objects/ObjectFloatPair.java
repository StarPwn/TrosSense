package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.Pair;
import java.util.Comparator;

/* loaded from: classes4.dex */
public interface ObjectFloatPair<K> extends Pair<K, Float> {
    float rightFloat();

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Float right() {
        return Float.valueOf(rightFloat());
    }

    default ObjectFloatPair<K> right(float r) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default ObjectFloatPair<K> right(Float l) {
        return right(l.floatValue());
    }

    default float secondFloat() {
        return rightFloat();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Float second() {
        return Float.valueOf(secondFloat());
    }

    default ObjectFloatPair<K> second(float r) {
        return right(r);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default ObjectFloatPair<K> second(Float l) {
        return second(l.floatValue());
    }

    default float valueFloat() {
        return rightFloat();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Float value() {
        return Float.valueOf(valueFloat());
    }

    default ObjectFloatPair<K> value(float r) {
        return right(r);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default ObjectFloatPair<K> value(Float l) {
        return value(l.floatValue());
    }

    static <K> ObjectFloatPair<K> of(K left, float right) {
        return new ObjectFloatImmutablePair(left, right);
    }

    static <K> Comparator<ObjectFloatPair<K>> lexComparator() {
        return new Comparator() { // from class: it.unimi.dsi.fastutil.objects.ObjectFloatPair$$ExternalSyntheticLambda0
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return ObjectFloatPair.lambda$lexComparator$0((ObjectFloatPair) obj, (ObjectFloatPair) obj2);
            }
        };
    }

    static /* synthetic */ int lambda$lexComparator$0(ObjectFloatPair x, ObjectFloatPair y) {
        int t = ((Comparable) x.left()).compareTo(y.left());
        return t != 0 ? t : Float.compare(x.rightFloat(), y.rightFloat());
    }
}
