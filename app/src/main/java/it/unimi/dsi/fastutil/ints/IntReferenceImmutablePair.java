package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.Pair;
import java.io.Serializable;
import java.util.Objects;

/* loaded from: classes4.dex */
public class IntReferenceImmutablePair<V> implements IntReferencePair<V>, Serializable {
    private static final long serialVersionUID = 0;
    protected final int left;
    protected final V right;

    public IntReferenceImmutablePair(int left, V right) {
        this.left = left;
        this.right = right;
    }

    public static <V> IntReferenceImmutablePair<V> of(int left, V right) {
        return new IntReferenceImmutablePair<>(left, right);
    }

    @Override // it.unimi.dsi.fastutil.ints.IntReferencePair
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
        return other instanceof IntReferencePair ? this.left == ((IntReferencePair) other).leftInt() && this.right == ((IntReferencePair) other).right() : (other instanceof Pair) && Objects.equals(Integer.valueOf(this.left), ((Pair) other).left()) && this.right == ((Pair) other).right();
    }

    public int hashCode() {
        return (this.left * 19) + (this.right == null ? 0 : System.identityHashCode(this.right));
    }

    public String toString() {
        return "<" + leftInt() + "," + right() + ">";
    }
}
