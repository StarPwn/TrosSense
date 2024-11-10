package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.Pair;
import java.io.Serializable;
import java.util.Objects;

/* loaded from: classes4.dex */
public class LongShortMutablePair implements LongShortPair, Serializable {
    private static final long serialVersionUID = 0;
    protected long left;
    protected short right;

    public LongShortMutablePair(long left, short right) {
        this.left = left;
        this.right = right;
    }

    public static LongShortMutablePair of(long left, short right) {
        return new LongShortMutablePair(left, right);
    }

    @Override // it.unimi.dsi.fastutil.longs.LongShortPair
    public long leftLong() {
        return this.left;
    }

    @Override // it.unimi.dsi.fastutil.longs.LongShortPair
    public LongShortMutablePair left(long l) {
        this.left = l;
        return this;
    }

    @Override // it.unimi.dsi.fastutil.longs.LongShortPair
    public short rightShort() {
        return this.right;
    }

    @Override // it.unimi.dsi.fastutil.longs.LongShortPair
    public LongShortMutablePair right(short r) {
        this.right = r;
        return this;
    }

    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        return other instanceof LongShortPair ? this.left == ((LongShortPair) other).leftLong() && this.right == ((LongShortPair) other).rightShort() : (other instanceof Pair) && Objects.equals(Long.valueOf(this.left), ((Pair) other).left()) && Objects.equals(Short.valueOf(this.right), ((Pair) other).right());
    }

    public int hashCode() {
        return (HashCommon.long2int(this.left) * 19) + this.right;
    }

    public String toString() {
        return "<" + leftLong() + "," + ((int) rightShort()) + ">";
    }
}
