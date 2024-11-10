package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.Pair;
import java.io.Serializable;
import java.util.Objects;

/* loaded from: classes4.dex */
public class LongBooleanMutablePair implements LongBooleanPair, Serializable {
    private static final long serialVersionUID = 0;
    protected long left;
    protected boolean right;

    public LongBooleanMutablePair(long left, boolean right) {
        this.left = left;
        this.right = right;
    }

    public static LongBooleanMutablePair of(long left, boolean right) {
        return new LongBooleanMutablePair(left, right);
    }

    @Override // it.unimi.dsi.fastutil.longs.LongBooleanPair
    public long leftLong() {
        return this.left;
    }

    @Override // it.unimi.dsi.fastutil.longs.LongBooleanPair
    public LongBooleanMutablePair left(long l) {
        this.left = l;
        return this;
    }

    @Override // it.unimi.dsi.fastutil.longs.LongBooleanPair
    public boolean rightBoolean() {
        return this.right;
    }

    @Override // it.unimi.dsi.fastutil.longs.LongBooleanPair
    public LongBooleanMutablePair right(boolean r) {
        this.right = r;
        return this;
    }

    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        return other instanceof LongBooleanPair ? this.left == ((LongBooleanPair) other).leftLong() && this.right == ((LongBooleanPair) other).rightBoolean() : (other instanceof Pair) && Objects.equals(Long.valueOf(this.left), ((Pair) other).left()) && Objects.equals(Boolean.valueOf(this.right), ((Pair) other).right());
    }

    public int hashCode() {
        return (HashCommon.long2int(this.left) * 19) + (this.right ? 1231 : 1237);
    }

    public String toString() {
        return "<" + leftLong() + "," + rightBoolean() + ">";
    }
}
