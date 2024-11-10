package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.Pair;
import java.io.Serializable;
import java.util.Objects;

/* loaded from: classes4.dex */
public class LongCharMutablePair implements LongCharPair, Serializable {
    private static final long serialVersionUID = 0;
    protected long left;
    protected char right;

    public LongCharMutablePair(long left, char right) {
        this.left = left;
        this.right = right;
    }

    public static LongCharMutablePair of(long left, char right) {
        return new LongCharMutablePair(left, right);
    }

    @Override // it.unimi.dsi.fastutil.longs.LongCharPair
    public long leftLong() {
        return this.left;
    }

    @Override // it.unimi.dsi.fastutil.longs.LongCharPair
    public LongCharMutablePair left(long l) {
        this.left = l;
        return this;
    }

    @Override // it.unimi.dsi.fastutil.longs.LongCharPair
    public char rightChar() {
        return this.right;
    }

    @Override // it.unimi.dsi.fastutil.longs.LongCharPair
    public LongCharMutablePair right(char r) {
        this.right = r;
        return this;
    }

    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        return other instanceof LongCharPair ? this.left == ((LongCharPair) other).leftLong() && this.right == ((LongCharPair) other).rightChar() : (other instanceof Pair) && Objects.equals(Long.valueOf(this.left), ((Pair) other).left()) && Objects.equals(Character.valueOf(this.right), ((Pair) other).right());
    }

    public int hashCode() {
        return (HashCommon.long2int(this.left) * 19) + this.right;
    }

    public String toString() {
        return "<" + leftLong() + "," + rightChar() + ">";
    }
}
