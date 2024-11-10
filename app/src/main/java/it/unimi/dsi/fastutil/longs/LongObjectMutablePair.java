package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.Pair;
import java.io.Serializable;
import java.util.Objects;

/* loaded from: classes4.dex */
public class LongObjectMutablePair<V> implements LongObjectPair<V>, Serializable {
    private static final long serialVersionUID = 0;
    protected long left;
    protected V right;

    /* JADX WARN: Multi-variable type inference failed */
    @Override // it.unimi.dsi.fastutil.Pair
    public /* bridge */ /* synthetic */ Pair right(Object obj) {
        return right((LongObjectMutablePair<V>) obj);
    }

    public LongObjectMutablePair(long left, V right) {
        this.left = left;
        this.right = right;
    }

    public static <V> LongObjectMutablePair<V> of(long left, V right) {
        return new LongObjectMutablePair<>(left, right);
    }

    @Override // it.unimi.dsi.fastutil.longs.LongObjectPair
    public long leftLong() {
        return this.left;
    }

    @Override // it.unimi.dsi.fastutil.longs.LongObjectPair
    public LongObjectMutablePair<V> left(long l) {
        this.left = l;
        return this;
    }

    @Override // it.unimi.dsi.fastutil.Pair
    public V right() {
        return this.right;
    }

    @Override // it.unimi.dsi.fastutil.Pair
    public LongObjectMutablePair<V> right(V r) {
        this.right = r;
        return this;
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
