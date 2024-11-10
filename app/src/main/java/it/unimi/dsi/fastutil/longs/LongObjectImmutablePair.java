package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.Pair;
import java.io.Serializable;
import java.util.Objects;

/* loaded from: classes4.dex */
public class LongObjectImmutablePair<V> implements LongObjectPair<V>, Serializable {
    private static final long serialVersionUID = 0;
    protected final long left;
    protected final V right;

    public LongObjectImmutablePair(long left, V right) {
        this.left = left;
        this.right = right;
    }

    public static <V> LongObjectImmutablePair<V> of(long left, V right) {
        return new LongObjectImmutablePair<>(left, right);
    }

    @Override // it.unimi.dsi.fastutil.longs.LongObjectPair
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
        return other instanceof LongObjectPair ? this.left == ((LongObjectPair) other).leftLong() && Objects.equals(this.right, ((LongObjectPair) other).right()) : (other instanceof Pair) && Objects.equals(Long.valueOf(this.left), ((Pair) other).left()) && Objects.equals(this.right, ((Pair) other).right());
    }

    public int hashCode() {
        return (HashCommon.long2int(this.left) * 19) + (this.right == null ? 0 : this.right.hashCode());
    }

    public String toString() {
        return "<" + leftLong() + "," + right() + ">";
    }
}
