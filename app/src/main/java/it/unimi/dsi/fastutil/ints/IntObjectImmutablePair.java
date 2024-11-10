package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.Pair;
import java.io.Serializable;
import java.util.Objects;

/* loaded from: classes4.dex */
public class IntObjectImmutablePair<V> implements IntObjectPair<V>, Serializable {
    private static final long serialVersionUID = 0;
    protected final int left;
    protected final V right;

    public IntObjectImmutablePair(int left, V right) {
        this.left = left;
        this.right = right;
    }

    public static <V> IntObjectImmutablePair<V> of(int left, V right) {
        return new IntObjectImmutablePair<>(left, right);
    }

    @Override // it.unimi.dsi.fastutil.ints.IntObjectPair
    public int leftInt() {
        return this.left;
    }

    @Override // it.unimi.dsi.fastutil.Pair
    public V right() {
        return this.right;
    }

    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        return other instanceof IntObjectPair ? this.left == ((IntObjectPair) other).leftInt() && Objects.equals(this.right, ((IntObjectPair) other).right()) : (other instanceof Pair) && Objects.equals(Integer.valueOf(this.left), ((Pair) other).left()) && Objects.equals(this.right, ((Pair) other).right());
    }

    public int hashCode() {
        return (this.left * 19) + (this.right == null ? 0 : this.right.hashCode());
    }

    public String toString() {
        return "<" + leftInt() + "," + right() + ">";
    }
}
