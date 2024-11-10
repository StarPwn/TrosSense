package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.Pair;
import java.io.Serializable;
import java.util.Objects;

/* loaded from: classes4.dex */
public class IntBooleanImmutablePair implements IntBooleanPair, Serializable {
    private static final long serialVersionUID = 0;
    protected final int left;
    protected final boolean right;

    public IntBooleanImmutablePair(int left, boolean right) {
        this.left = left;
        this.right = right;
    }

    public static IntBooleanImmutablePair of(int left, boolean right) {
        return new IntBooleanImmutablePair(left, right);
    }

    @Override // it.unimi.dsi.fastutil.ints.IntBooleanPair
    public int leftInt() {
        return this.left;
    }

    @Override // it.unimi.dsi.fastutil.ints.IntBooleanPair
    public boolean rightBoolean() {
        return this.right;
    }

    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        return other instanceof IntBooleanPair ? this.left == ((IntBooleanPair) other).leftInt() && this.right == ((IntBooleanPair) other).rightBoolean() : (other instanceof Pair) && Objects.equals(Integer.valueOf(this.left), ((Pair) other).left()) && Objects.equals(Boolean.valueOf(this.right), ((Pair) other).right());
    }

    public int hashCode() {
        return (this.left * 19) + (this.right ? 1231 : 1237);
    }

    public String toString() {
        return "<" + leftInt() + "," + rightBoolean() + ">";
    }
}
