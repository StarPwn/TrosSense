package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.Pair;
import java.io.Serializable;
import java.util.Objects;

/* loaded from: classes4.dex */
public class ObjectObjectMutablePair<K, V> implements Pair<K, V>, Serializable {
    private static final long serialVersionUID = 0;
    protected K left;
    protected V right;

    /* JADX WARN: Multi-variable type inference failed */
    @Override // it.unimi.dsi.fastutil.Pair
    public /* bridge */ /* synthetic */ Pair left(Object obj) {
        return left((ObjectObjectMutablePair<K, V>) obj);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // it.unimi.dsi.fastutil.Pair
    public /* bridge */ /* synthetic */ Pair right(Object obj) {
        return right((ObjectObjectMutablePair<K, V>) obj);
    }

    public ObjectObjectMutablePair(K left, V right) {
        this.left = left;
        this.right = right;
    }

    public static <K, V> ObjectObjectMutablePair<K, V> of(K left, V right) {
        return new ObjectObjectMutablePair<>(left, right);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    public K left() {
        return this.left;
    }

    @Override // it.unimi.dsi.fastutil.Pair
    public ObjectObjectMutablePair<K, V> left(K l) {
        this.left = l;
        return this;
    }

    @Override // it.unimi.dsi.fastutil.Pair
    public V right() {
        return this.right;
    }

    @Override // it.unimi.dsi.fastutil.Pair
    public ObjectObjectMutablePair<K, V> right(V r) {
        this.right = r;
        return this;
    }

    public boolean equals(Object other) {
        return other != null && (other instanceof Pair) && Objects.equals(this.left, ((Pair) other).left()) && Objects.equals(this.right, ((Pair) other).right());
    }

    public int hashCode() {
        return ((this.left == null ? 0 : this.left.hashCode()) * 19) + (this.right != null ? this.right.hashCode() : 0);
    }

    public String toString() {
        return "<" + left() + "," + right() + ">";
    }
}
