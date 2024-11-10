package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.Pair;
import java.io.Serializable;
import java.util.Objects;

/* loaded from: classes4.dex */
public class LongByteMutablePair implements LongBytePair, Serializable {
    private static final long serialVersionUID = 0;
    protected long left;
    protected byte right;

    public LongByteMutablePair(long left, byte right) {
        this.left = left;
        this.right = right;
    }

    public static LongByteMutablePair of(long left, byte right) {
        return new LongByteMutablePair(left, right);
    }

    @Override // it.unimi.dsi.fastutil.longs.LongBytePair
    public long leftLong() {
        return this.left;
    }

    @Override // it.unimi.dsi.fastutil.longs.LongBytePair
    public LongByteMutablePair left(long l) {
        this.left = l;
        return this;
    }

    @Override // it.unimi.dsi.fastutil.longs.LongBytePair
    public byte rightByte() {
        return this.right;
    }

    @Override // it.unimi.dsi.fastutil.longs.LongBytePair
    public LongByteMutablePair right(byte r) {
        this.right = r;
        return this;
    }

    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        return other instanceof LongBytePair ? this.left == ((LongBytePair) other).leftLong() && this.right == ((LongBytePair) other).rightByte() : (other instanceof Pair) && Objects.equals(Long.valueOf(this.left), ((Pair) other).left()) && Objects.equals(Byte.valueOf(this.right), ((Pair) other).right());
    }

    public int hashCode() {
        return (HashCommon.long2int(this.left) * 19) + this.right;
    }

    public String toString() {
        return "<" + leftLong() + "," + ((int) rightByte()) + ">";
    }
}
