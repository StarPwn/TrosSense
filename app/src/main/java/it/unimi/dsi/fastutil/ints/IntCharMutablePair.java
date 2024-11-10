package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.Pair;
import java.io.Serializable;
import java.util.Objects;

/* loaded from: classes4.dex */
public class IntCharMutablePair implements IntCharPair, Serializable {
    private static final long serialVersionUID = 0;
    protected int left;
    protected char right;

    public IntCharMutablePair(int left, char right) {
        this.left = left;
        this.right = right;
    }

    public static IntCharMutablePair of(int left, char right) {
        return new IntCharMutablePair(left, right);
    }

    @Override // it.unimi.dsi.fastutil.ints.IntCharPair
    public int leftInt() {
        return this.left;
    }

    @Override // it.unimi.dsi.fastutil.ints.IntCharPair
    public IntCharMutablePair left(int l) {
        this.left = l;
        return this;
    }

    @Override // it.unimi.dsi.fastutil.ints.IntCharPair
    public char rightChar() {
        return this.right;
    }

    @Override // it.unimi.dsi.fastutil.ints.IntCharPair
    public IntCharMutablePair right(char r) {
        this.right = r;
        return this;
    }

    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        return other instanceof IntCharPair ? this.left == ((IntCharPair) other).leftInt() && this.right == ((IntCharPair) other).rightChar() : (other instanceof Pair) && Objects.equals(Integer.valueOf(this.left), ((Pair) other).left()) && Objects.equals(Character.valueOf(this.right), ((Pair) other).right());
    }

    public int hashCode() {
        return (this.left * 19) + this.right;
    }

    public String toString() {
        return "<" + leftInt() + "," + rightChar() + ">";
    }
}
