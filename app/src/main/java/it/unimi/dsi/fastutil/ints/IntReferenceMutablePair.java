package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.Pair;
import java.io.Serializable;
import java.util.Objects;

/* loaded from: classes4.dex */
public class IntReferenceMutablePair<V> implements IntReferencePair<V>, Serializable {
    private static final long serialVersionUID = 0;
    protected int left;
    protected V right;

    /* JADX WARN: Multi-variable type inference failed */
    @Override // it.unimi.dsi.fastutil.Pair
    public /* bridge */ /* synthetic */ Pair right(Object obj) {
        return right((IntReferenceMutablePair<V>) obj);
    }

    public IntReferenceMutablePair(int left, V right) {
        this.left = left;
        this.right = right;
    }

    public static <V> IntReferenceMutablePair<V> of(int left, V right) {
        return new IntReferenceMutablePair<>(left, right);
    }

    @Override // it.unimi.dsi.fastutil.ints.IntReferencePair
    public int leftInt() {
        return this.left;
    }

    @Override // it.unimi.dsi.fastutil.ints.IntReferencePair
    public IntReferenceMutablePair<V> left(int l) {
        this.left = l;
        return this;
    }

    @Override // it.unimi.dsi.fastutil.Pair
    public V right() {
        return this.right;
    }

    @Override // it.unimi.dsi.fastutil.Pair
    public IntReferenceMutablePair<V> right(V r) {
        this.right = r;
        return this;
    }

    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        return other instanceof IntReferencePair ? this.left == ((IntReferencePair) other).leftInt() && this.right == ((IntReferencePair) other).right() : (other instanceof Pair) && Objects.equals(Integer.valueOf(this.left), ((Pair) other).left()) && this.right == ((Pair) other).right();
    }

    public int hashCode() {
        return (this.left * 19) + (this.right == null ? 0 : System.identityHashCode(this.right));
    }

    public String toString() {
        return "<" + leftInt() + "," + right() + ">";
    }
}
