package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.SortedPair;
import java.io.Serializable;
import java.util.Objects;

/* loaded from: classes4.dex */
public class IntIntImmutableSortedPair extends IntIntImmutablePair implements IntIntSortedPair, Serializable {
    private static final long serialVersionUID = 0;

    private IntIntImmutableSortedPair(int left, int right) {
        super(left, right);
    }

    public static IntIntImmutableSortedPair of(int left, int right) {
        return left <= right ? new IntIntImmutableSortedPair(left, right) : new IntIntImmutableSortedPair(right, left);
    }

    @Override // it.unimi.dsi.fastutil.ints.IntIntImmutablePair
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        return other instanceof IntIntSortedPair ? this.left == ((IntIntSortedPair) other).leftInt() && this.right == ((IntIntSortedPair) other).rightInt() : (other instanceof SortedPair) && Objects.equals(Integer.valueOf(this.left), ((SortedPair) other).left()) && Objects.equals(Integer.valueOf(this.right), ((SortedPair) other).right());
    }

    @Override // it.unimi.dsi.fastutil.ints.IntIntImmutablePair
    public String toString() {
        return "{" + leftInt() + "," + rightInt() + "}";
    }
}
