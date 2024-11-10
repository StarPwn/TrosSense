package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.Pair;
import java.io.Serializable;
import java.util.Objects;

/* loaded from: classes4.dex */
public class IntLongImmutablePair implements IntLongPair, Serializable {
    private static final long serialVersionUID = 0;
    protected final int left;
    protected final long right;

    public IntLongImmutablePair(int left, long right) {
        this.left = left;
        this.right = right;
    }

    public static IntLongImmutablePair of(int left, long right) {
        return new IntLongImmutablePair(left, right);
    }

    @Override // it.unimi.dsi.fastutil.ints.IntLongPair
    public int leftInt() {
        return this.left;
    }

    @Override // it.unimi.dsi.fastutil.ints.IntLongPair
    public long rightLong() {
        return this.right;
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
