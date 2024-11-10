package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.Pair;
import java.io.Serializable;
import java.util.Objects;

/* loaded from: classes4.dex */
public class IntShortMutablePair implements IntShortPair, Serializable {
    private static final long serialVersionUID = 0;
    protected int left;
    protected short right;

    public IntShortMutablePair(int left, short right) {
        this.left = left;
        this.right = right;
    }

    public static IntShortMutablePair of(int left, short right) {
        return new IntShortMutablePair(left, right);
    }

    @Override // it.unimi.dsi.fastutil.ints.IntShortPair
    public int leftInt() {
        return this.left;
    }

    @Override // it.unimi.dsi.fastutil.ints.IntShortPair
    public IntShortMutablePair left(int l) {
        this.left = l;
        return this;
    }

    @Override // it.unimi.dsi.fastutil.ints.IntShortPair
    public short rightShort() {
        return this.right;
    }

    @Override // it.unimi.dsi.fastutil.ints.IntShortPair
    public IntShortMutablePair right(short r) {
        this.right = r;
        return this;
    }

    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        return other instanceof IntShortPair ? this.left == ((IntShortPair) other).leftInt() && this.right == ((IntShortPair) other).rightShort() : (other instanceof Pair) && Objects.equals(Integer.valueOf(this.left), ((Pair) other).left()) && Objects.equals(Short.valueOf(this.right), ((Pair) other).right());
    }

    public int hashCode() {
        return (this.left * 19) + this.right;
    }

    public String toString() {
        return "<" + leftInt() + "," + ((int) rightShort()) + ">";
    }
}
