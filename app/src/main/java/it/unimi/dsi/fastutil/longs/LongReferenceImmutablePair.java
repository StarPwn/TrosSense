package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.Pair;
import java.io.Serializable;
import java.util.Objects;

/* loaded from: classes4.dex */
public class LongReferenceImmutablePair<V> implements LongReferencePair<V>, Serializable {
    private static final long serialVersionUID = 0;
    protected final long left;
    protected final V right;

    public LongReferenceImmutablePair(long left, V right) {
        this.left = left;
        this.right = right;
    }

    public static <V> LongReferenceImmutablePair<V> of(long left, V right) {
        return new LongReferenceImmutablePair<>(left, right);
    }

    @Override // it.unimi.dsi.fastutil.longs.LongReferencePair
    public long leftLong() {
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
        return other instanceof LongReferencePair ? this.left == ((LongReferencePair) other).leftLong() && this.right == ((LongReferencePair) other).right() : (other instanceof Pair) && Objects.equals(Long.valueOf(this.left), ((Pair) other).left()) && this.right == ((Pair) other).right();
    }

    public int hashCode() {
        return (HashCommon.long2int(this.left) * 19) + (this.right == null ? 0 : System.identityHashCode(this.right));
    }

    public String toString() {
        return "<" + leftLong() + "," + right() + ">";
    }
}
