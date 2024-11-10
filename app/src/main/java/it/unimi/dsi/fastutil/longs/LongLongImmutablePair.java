package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.Pair;
import java.io.Serializable;
import java.util.Objects;

/* loaded from: classes4.dex */
public class LongLongImmutablePair implements LongLongPair, Serializable {
    private static final long serialVersionUID = 0;
    protected final long left;
    protected final long right;

    public LongLongImmutablePair(long left, long right) {
        this.left = left;
        this.right = right;
    }

    public static LongLongImmutablePair of(long left, long right) {
        return new LongLongImmutablePair(left, right);
    }

    @Override // it.unimi.dsi.fastutil.longs.LongLongPair
    public long leftLong() {
        return this.left;
    }

    @Override // it.unimi.dsi.fastutil.longs.LongLongPair
    public long rightLong() {
        return this.right;
    }

    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        return other instanceof LongLongPair ? this.left == ((LongLongPair) other).leftLong() && this.right == ((LongLongPair) other).rightLong() : (other instanceof Pair) && Objects.equals(Long.valueOf(this.left), ((Pair) other).left()) && Objects.equals(Long.valueOf(this.right), ((Pair) other).right());
    }

    public int hashCode() {
        return (HashCommon.long2int(this.left) * 19) + HashCommon.long2int(this.right);
    }

    public String toString() {
        return "<" + leftLong() + "," + rightLong() + ">";
    }
}
