package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.Pair;
import java.io.Serializable;
import java.util.Objects;

/* loaded from: classes4.dex */
public class IntIntMutablePair implements IntIntPair, Serializable {
    private static final long serialVersionUID = 0;
    protected int left;
    protected int right;

    public IntIntMutablePair(int left, int right) {
        this.left = left;
        this.right = right;
    }

    public static IntIntMutablePair of(int left, int right) {
        return new IntIntMutablePair(left, right);
    }

    @Override // it.unimi.dsi.fastutil.ints.IntIntPair
    public int leftInt() {
        return this.left;
    }

    @Override // it.unimi.dsi.fastutil.ints.IntIntPair
    public IntIntMutablePair left(int l) {
        this.left = l;
        return this;
    }

    @Override // it.unimi.dsi.fastutil.ints.IntIntPair
    public int rightInt() {
        return this.right;
    }

    @Override // it.unimi.dsi.fastutil.ints.IntIntPair
    public IntIntMutablePair right(int r) {
        this.right = r;
        return this;
    }

    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        return other instanceof IntIntPair ? this.left == ((IntIntPair) other).leftInt() && this.right == ((IntIntPair) other).rightInt() : (other instanceof Pair) && Objects.equals(Integer.valueOf(this.left), ((Pair) other).left()) && Objects.equals(Integer.valueOf(this.right), ((Pair) other).right());
    }

    public int hashCode() {
        return (this.left * 19) + this.right;
    }

    public String toString() {
        return "<" + leftInt() + "," + rightInt() + ">";
    }
}
