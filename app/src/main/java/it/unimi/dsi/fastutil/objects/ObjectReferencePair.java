package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.Pair;

/* loaded from: classes4.dex */
public interface ObjectReferencePair<K, V> extends Pair<K, V> {
    static <K, V> ObjectReferencePair<K, V> of(K left, V right) {
        return new ObjectReferenceImmutablePair(left, right);
    }
}
