package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.Pair;
import java.io.Serializable;
import java.util.Objects;

/* loaded from: classes4.dex */
public class LongIntImmutablePair implements LongIntPair, Serializable {
    private static final long serialVersionUID = 0;
    protected final long left;
    protected final int right;

    public LongIntImmutablePair(long left, int right) {
        this.left = left;
        this.right = right;
    }

    public static LongIntImmutablePair of(long left, int right) {
        return new LongIntImmutablePair(left, right);
    }

    @Override // it.unimi.dsi.fastutil.longs.LongIntPair
    public long leftLong() {
        return this.left;
    }

    @Override // it.unimi.dsi.fastutil.longs.LongIntPair
    public int rightInt() {
        return this.right;
    }

    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        return other instanceof LongIntPair ? this.left == ((LongIntPair) other).leftLong() && this.right == ((LongIntPair) other).rightInt() : (other instanceof Pair) && Objects.equals(Long.valueOf(this.left), ((Pair) other).left()) && Objects.equals(Integer.valueOf(this.right), ((Pair) other).right());
    }

    public int hashCode() {
        return (HashCommon.long2int(this.left) * 19) + this.right;
    }

    public String toString() {
        return "<" + leftLong() + "," + rightInt() + ">";
    }
}
