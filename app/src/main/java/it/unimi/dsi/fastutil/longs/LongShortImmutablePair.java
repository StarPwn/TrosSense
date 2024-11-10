package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.Pair;
import java.io.Serializable;
import java.util.Objects;

/* loaded from: classes4.dex */
public class LongShortImmutablePair implements LongShortPair, Serializable {
    private static final long serialVersionUID = 0;
    protected final long left;
    protected final short right;

    public LongShortImmutablePair(long left, short right) {
        this.left = left;
        this.right = right;
    }

    public static LongShortImmutablePair of(long left, short right) {
        return new LongShortImmutablePair(left, right);
    }

    @Override // it.unimi.dsi.fastutil.longs.LongShortPair
    public long leftLong() {
        return this.left;
    }

    @Override // it.unimi.dsi.fastutil.longs.LongShortPair
    public short rightShort() {
        return this.right;
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
