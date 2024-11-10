package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.SortedPair;
import java.io.Serializable;
import java.lang.Comparable;
import java.util.Objects;

/* loaded from: classes4.dex */
public class ObjectObjectImmutableSortedPair<K extends Comparable<K>> extends ObjectObjectImmutablePair<K, K> implements SortedPair<K>, Serializable {
    private static final long serialVersionUID = 0;

    private ObjectObjectImmutableSortedPair(K left, K right) {
        super(left, right);
    }

    public static <K extends Comparable<K>> ObjectObjectImmutableSortedPair<K> of(K left, K right) {
        return left.compareTo(right) <= 0 ? new ObjectObjectImmutableSortedPair<>(left, right) : new ObjectObjectImmutableSortedPair<>(right, left);
    }

    @Override // it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair
    public boolean equals(Object other) {
        return other != null && (other instanceof SortedPair) && Objects.equals(this.left, ((SortedPair) other).left()) && Objects.equals(this.right, ((SortedPair) other).right());
    }

    @Override // it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair
    public String toString() {
        return "{" + left() + "," + right() + "}";
    }
}
