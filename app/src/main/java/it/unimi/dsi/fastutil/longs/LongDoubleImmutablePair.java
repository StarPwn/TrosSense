package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.Pair;
import java.io.Serializable;
import java.util.Objects;

/* loaded from: classes4.dex */
public class LongDoubleImmutablePair implements LongDoublePair, Serializable {
    private static final long serialVersionUID = 0;
    protected final long left;
    protected final double right;

    public LongDoubleImmutablePair(long left, double right) {
        this.left = left;
        this.right = right;
    }

    public static LongDoubleImmutablePair of(long left, double right) {
        return new LongDoubleImmutablePair(left, right);
    }

    @Override // it.unimi.dsi.fastutil.longs.LongDoublePair
    public long leftLong() {
        return this.left;
    }

    @Override // it.unimi.dsi.fastutil.longs.LongDoublePair
    public double rightDouble() {
        return this.right;
    }

    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        return other instanceof LongDoublePair ? this.left == ((LongDoublePair) other).leftLong() && this.right == ((LongDoublePair) other).rightDouble() : (other instanceof Pair) && Objects.equals(Long.valueOf(this.left), ((Pair) other).left()) && Objects.equals(Double.valueOf(this.right), ((Pair) other).right());
    }

    public int hashCode() {
        return (HashCommon.long2int(this.left) * 19) + HashCommon.double2int(this.right);
    }

    public String toString() {
        return "<" + leftLong() + "," + rightDouble() + ">";
    }
}
