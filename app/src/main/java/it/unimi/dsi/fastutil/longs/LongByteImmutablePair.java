package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.Pair;
import java.io.Serializable;
import java.util.Objects;

/* loaded from: classes4.dex */
public class LongByteImmutablePair implements LongBytePair, Serializable {
    private static final long serialVersionUID = 0;
    protected final long left;
    protected final byte right;

    public LongByteImmutablePair(long left, byte right) {
        this.left = left;
        this.right = right;
    }

    public static LongByteImmutablePair of(long left, byte right) {
        return new LongByteImmutablePair(left, right);
    }

    @Override // it.unimi.dsi.fastutil.longs.LongBytePair
    public long leftLong() {
        return this.left;
    }

    @Override // it.unimi.dsi.fastutil.longs.LongBytePair
    public byte rightByte() {
        return this.right;
    }

    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        return other instanceof LongBytePair ? this.left == ((LongBytePair) other).leftLong() && this.right == ((LongBytePair) other).rightByte() : (other instanceof Pair) && Objects.equals(Long.valueOf(this.left), ((Pair) other).left()) && Objects.equals(Byte.valueOf(this.right), ((Pair) other).right());
    }

    public int hashCode() {
        return (HashCommon.long2int(this.left) * 19) + this.right;
    }

    public String toString() {
        return "<" + leftLong() + "," + ((int) rightByte()) + ">";
    }
}
