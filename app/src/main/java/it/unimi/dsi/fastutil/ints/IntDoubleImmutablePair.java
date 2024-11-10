package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.Pair;
import java.io.Serializable;
import java.util.Objects;

/* loaded from: classes4.dex */
public class IntDoubleImmutablePair implements IntDoublePair, Serializable {
    private static final long serialVersionUID = 0;
    protected final int left;
    protected final double right;

    public IntDoubleImmutablePair(int left, double right) {
        this.left = left;
        this.right = right;
    }

    public static IntDoubleImmutablePair of(int left, double right) {
        return new IntDoubleImmutablePair(left, right);
    }

    @Override // it.unimi.dsi.fastutil.ints.IntDoublePair
    public int leftInt() {
        return this.left;
    }

    @Override // it.unimi.dsi.fastutil.ints.IntDoublePair
    public double rightDouble() {
        return this.right;
    }

    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        return other instanceof IntDoublePair ? this.left == ((IntDoublePair) other).leftInt() && this.right == ((IntDoublePair) other).rightDouble() : (other instanceof Pair) && Objects.equals(Integer.valueOf(this.left), ((Pair) other).left()) && Objects.equals(Double.valueOf(this.right), ((Pair) other).right());
    }

    public int hashCode() {
        return (this.left * 19) + HashCommon.double2int(this.right);
    }

    public String toString() {
        return "<" + leftInt() + "," + rightDouble() + ">";
    }
}
