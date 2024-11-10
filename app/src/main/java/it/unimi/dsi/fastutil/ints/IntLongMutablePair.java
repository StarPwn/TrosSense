package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.Pair;
import java.io.Serializable;
import java.util.Objects;

/* loaded from: classes4.dex */
public class IntLongMutablePair implements IntLongPair, Serializable {
    private static final long serialVersionUID = 0;
    protected int left;
    protected long right;

    public IntLongMutablePair(int left, long right) {
        this.left = left;
        this.right = right;
    }

    public static IntLongMutablePair of(int left, long right) {
        return new IntLongMutablePair(left, right);
    }

    @Override // it.unimi.dsi.fastutil.ints.IntLongPair
    public int leftInt() {
        return this.left;
    }

    @Override // it.unimi.dsi.fastutil.ints.IntLongPair
    public IntLongMutablePair left(int l) {
        this.left = l;
        return this;
    }

    @Override // it.unimi.dsi.fastutil.ints.IntLongPair
    public long rightLong() {
        return this.right;
    }

    @Override // it.unimi.dsi.fastutil.ints.IntLongPair
    public IntLongMutablePair right(long r) {
        this.right = r;
        return this;
    }

    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        return other instanceof IntLongPair ? this.left == ((IntLongPair) other).leftInt() && this.right == ((IntLongPair) other).rightLong() : (other instanceof Pair) && Objects.equals(Integer.valueOf(this.left), ((Pair) other).left()) && Objects.equals(Long.valueOf(this.right), ((Pair) other).right());
    }

    public int hashCode() {
        return (this.left * 19) + HashCommon.long2int(this.right);
    }

    public String toString() {
        return "<" + leftInt() + "," + rightLong() + ">";
    }
}
