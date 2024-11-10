package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.Pair;
import java.io.Serializable;
import java.util.Objects;

/* loaded from: classes4.dex */
public class LongCharImmutablePair implements LongCharPair, Serializable {
    private static final long serialVersionUID = 0;
    protected final long left;
    protected final char right;

    public LongCharImmutablePair(long left, char right) {
        this.left = left;
        this.right = right;
    }

    public static LongCharImmutablePair of(long left, char right) {
        return new LongCharImmutablePair(left, right);
    }

    @Override // it.unimi.dsi.fastutil.longs.LongCharPair
    public long leftLong() {
        return this.left;
    }

    @Override // it.unimi.dsi.fastutil.longs.LongCharPair
    public char rightChar() {
        return this.right;
    }

    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        return other instanceof LongCharPair ? this.left == ((LongCharPair) other).leftLong() && this.right == ((LongCharPair) other).rightChar() : (other instanceof Pair) && Objects.equals(Long.valueOf(this.left), ((Pair) other).left()) && Objects.equals(Character.valueOf(this.right), ((Pair) other).right());
    }

    public int hashCode() {
        return (HashCommon.long2int(this.left) * 19) + this.right;
    }

    public String toString() {
        return "<" + leftLong() + "," + rightChar() + ">";
    }
}
