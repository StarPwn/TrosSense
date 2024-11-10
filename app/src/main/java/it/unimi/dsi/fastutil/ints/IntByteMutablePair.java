package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.Pair;
import java.io.Serializable;
import java.util.Objects;

/* loaded from: classes4.dex */
public class IntByteMutablePair implements IntBytePair, Serializable {
    private static final long serialVersionUID = 0;
    protected int left;
    protected byte right;

    public IntByteMutablePair(int left, byte right) {
        this.left = left;
        this.right = right;
    }

    public static IntByteMutablePair of(int left, byte right) {
        return new IntByteMutablePair(left, right);
    }

    @Override // it.unimi.dsi.fastutil.ints.IntBytePair
    public int leftInt() {
        return this.left;
    }

    @Override // it.unimi.dsi.fastutil.ints.IntBytePair
    public IntByteMutablePair left(int l) {
        this.left = l;
        return this;
    }

    @Override // it.unimi.dsi.fastutil.ints.IntBytePair
    public byte rightByte() {
        return this.right;
    }

    @Override // it.unimi.dsi.fastutil.ints.IntBytePair
    public IntByteMutablePair right(byte r) {
        this.right = r;
        return this;
    }

    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        return other instanceof IntBytePair ? this.left == ((IntBytePair) other).leftInt() && this.right == ((IntBytePair) other).rightByte() : (other instanceof Pair) && Objects.equals(Integer.valueOf(this.left), ((Pair) other).left()) && Objects.equals(Byte.valueOf(this.right), ((Pair) other).right());
    }

    public int hashCode() {
        return (this.left * 19) + this.right;
    }

    public String toString() {
        return "<" + leftInt() + "," + ((int) rightByte()) + ">";
    }
}
