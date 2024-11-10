package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.Pair;
import java.io.Serializable;
import java.util.Objects;

/* loaded from: classes4.dex */
public class IntFloatMutablePair implements IntFloatPair, Serializable {
    private static final long serialVersionUID = 0;
    protected int left;
    protected float right;

    public IntFloatMutablePair(int left, float right) {
        this.left = left;
        this.right = right;
    }

    public static IntFloatMutablePair of(int left, float right) {
        return new IntFloatMutablePair(left, right);
    }

    @Override // it.unimi.dsi.fastutil.ints.IntFloatPair
    public int leftInt() {
        return this.left;
    }

    @Override // it.unimi.dsi.fastutil.ints.IntFloatPair
    public IntFloatMutablePair left(int l) {
        this.left = l;
        return this;
    }

    @Override // it.unimi.dsi.fastutil.ints.IntFloatPair
    public float rightFloat() {
        return this.right;
    }

    @Override // it.unimi.dsi.fastutil.ints.IntFloatPair
    public IntFloatMutablePair right(float r) {
        this.right = r;
        return this;
    }

    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        return other instanceof IntFloatPair ? this.left == ((IntFloatPair) other).leftInt() && this.right == ((IntFloatPair) other).rightFloat() : (other instanceof Pair) && Objects.equals(Integer.valueOf(this.left), ((Pair) other).left()) && Objects.equals(Float.valueOf(this.right), ((Pair) other).right());
    }

    public int hashCode() {
        return (this.left * 19) + HashCommon.float2int(this.right);
    }

    public String toString() {
        return "<" + leftInt() + "," + rightFloat() + ">";
    }
}
