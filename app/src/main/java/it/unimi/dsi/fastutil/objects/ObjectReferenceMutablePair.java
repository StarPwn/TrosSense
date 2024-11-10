package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.Pair;
import java.io.Serializable;
import java.util.Objects;

/* loaded from: classes4.dex */
public class ObjectReferenceMutablePair<K, V> implements ObjectReferencePair<K, V>, Serializable {
    private static final long serialVersionUID = 0;
    protected K left;
    protected V right;

    /* JADX WARN: Multi-variable type inference failed */
    @Override // it.unimi.dsi.fastutil.Pair
    public /* bridge */ /* synthetic */ Pair left(Object obj) {
        return left((ObjectReferenceMutablePair<K, V>) obj);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // it.unimi.dsi.fastutil.Pair
    public /* bridge */ /* synthetic */ Pair right(Object obj) {
        return right((ObjectReferenceMutablePair<K, V>) obj);
    }

    public ObjectReferenceMutablePair(K left, V right) {
        this.left = left;
        this.right = right;
    }

    public static <K, V> ObjectReferenceMutablePair<K, V> of(K left, V right) {
        return new ObjectReferenceMutablePair<>(left, right);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    public K left() {
        return this.left;
    }

    @Override // it.unimi.dsi.fastutil.Pair
    public ObjectReferenceMutablePair<K, V> left(K l) {
        this.left = l;
        return this;
    }

    @Override // it.unimi.dsi.fastutil.Pair
    public V right() {
        return this.right;
    }

    @Override // it.unimi.dsi.fastutil.Pair
    public ObjectReferenceMutablePair<K, V> right(V r) {
        this.right = r;
        return this;
    }

    public boolean equals(Object other) {
        return other != null && (other instanceof Pair) && Objects.equals(this.left, ((Pair) other).left()) && this.right == ((Pair) other).right();
    }

    public int hashCode() {
        return ((this.left == null ? 0 : this.left.hashCode()) * 19) + (this.right != null ? System.identityHashCode(this.right) : 0);
    }

    public String toString() {
        return "<" + left() + "," + right() + ">";
    }
}
