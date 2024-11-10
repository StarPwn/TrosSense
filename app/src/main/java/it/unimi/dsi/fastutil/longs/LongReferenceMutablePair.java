package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.Pair;
import java.io.Serializable;
import java.util.Objects;

/* loaded from: classes4.dex */
public class LongReferenceMutablePair<V> implements LongReferencePair<V>, Serializable {
    private static final long serialVersionUID = 0;
    protected long left;
    protected V right;

    /* JADX WARN: Multi-variable type inference failed */
    @Override // it.unimi.dsi.fastutil.Pair
    public /* bridge */ /* synthetic */ Pair right(Object obj) {
        return right((LongReferenceMutablePair<V>) obj);
    }

    public LongReferenceMutablePair(long left, V right) {
        this.left = left;
        this.right = right;
    }

    public static <V> LongReferenceMutablePair<V> of(long left, V right) {
        return new LongReferenceMutablePair<>(left, right);
    }

    @Override // it.unimi.dsi.fastutil.longs.LongReferencePair
    public long leftLong() {
        return this.left;
    }

    @Override // it.unimi.dsi.fastutil.longs.LongReferencePair
    public LongReferenceMutablePair<V> left(long l) {
        this.left = l;
        return this;
    }

    @Override // it.unimi.dsi.fastutil.Pair
    public V right() {
        return this.right;
    }

    @Override // it.unimi.dsi.fastutil.Pair
    public LongReferenceMutablePair<V> right(V r) {
        this.right = r;
        return this;
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
