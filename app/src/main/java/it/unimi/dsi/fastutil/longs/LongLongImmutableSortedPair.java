package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.SortedPair;
import java.io.Serializable;
import java.util.Objects;

/* loaded from: classes4.dex */
public class LongLongImmutableSortedPair extends LongLongImmutablePair implements LongLongSortedPair, Serializable {
    private static final long serialVersionUID = 0;

    private LongLongImmutableSortedPair(long left, long right) {
        super(left, right);
    }

    public static LongLongImmutableSortedPair of(long left, long right) {
        return left <= right ? new LongLongImmutableSortedPair(left, right) : new LongLongImmutableSortedPair(right, left);
    }

    @Override // it.unimi.dsi.fastutil.longs.LongLongImmutablePair
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        return other instanceof LongLongSortedPair ? this.left == ((LongLongSortedPair) other).leftLong() && this.right == ((LongLongSortedPair) other).rightLong() : (other instanceof SortedPair) && Objects.equals(Long.valueOf(this.left), ((SortedPair) other).left()) && Objects.equals(Long.valueOf(this.right), ((SortedPair) other).right());
    }

    @Override // it.unimi.dsi.fastutil.longs.LongLongImmutablePair
    public String toString() {
        return "{" + leftLong() + "," + rightLong() + "}";
    }
}
