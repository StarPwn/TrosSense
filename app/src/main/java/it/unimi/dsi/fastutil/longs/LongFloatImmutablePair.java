package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.Pair;
import java.io.Serializable;
import java.util.Objects;

/* loaded from: classes4.dex */
public class LongFloatImmutablePair implements LongFloatPair, Serializable {
    private static final long serialVersionUID = 0;
    protected final long left;
    protected final float right;

    public LongFloatImmutablePair(long left, float right) {
        this.left = left;
        this.right = right;
    }

    public static LongFloatImmutablePair of(long left, float right) {
        return new LongFloatImmutablePair(left, right);
    }

    @Override // it.unimi.dsi.fastutil.longs.LongFloatPair
    public long leftLong() {
        return this.left;
    }

    @Override // it.unimi.dsi.fastutil.longs.LongFloatPair
    public float rightFloat() {
        return this.right;
    }

    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        return other instanceof LongFloatPair ? this.left == ((LongFloatPair) other).leftLong() && this.right == ((LongFloatPair) other).rightFloat() : (other instanceof Pair) && Objects.equals(Long.valueOf(this.left), ((Pair) other).left()) && Objects.equals(Float.valueOf(this.right), ((Pair) other).right());
    }

    public int hashCode() {
        return (HashCommon.long2int(this.left) * 19) + HashCommon.float2int(this.right);
    }

    public String toString() {
        return "<" + leftLong() + "," + rightFloat() + ">";
    }
}
