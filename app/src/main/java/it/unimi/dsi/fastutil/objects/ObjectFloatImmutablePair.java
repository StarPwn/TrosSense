package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.Pair;
import java.io.Serializable;
import java.util.Objects;

/* loaded from: classes4.dex */
public class ObjectFloatImmutablePair<K> implements ObjectFloatPair<K>, Serializable {
    private static final long serialVersionUID = 0;
    protected final K left;
    protected final float right;

    public ObjectFloatImmutablePair(K left, float right) {
        this.left = left;
        this.right = right;
    }

    public static <K> ObjectFloatImmutablePair<K> of(K left, float right) {
        return new ObjectFloatImmutablePair<>(left, right);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    public K left() {
        return this.left;
    }

    @Override // it.unimi.dsi.fastutil.objects.ObjectFloatPair
    public float rightFloat() {
        return this.right;
    }

    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        return other instanceof ObjectFloatPair ? Objects.equals(this.left, ((ObjectFloatPair) other).left()) && this.right == ((ObjectFloatPair) other).rightFloat() : (other instanceof Pair) && Objects.equals(this.left, ((Pair) other).left()) && Objects.equals(Float.valueOf(this.right), ((Pair) other).right());
    }

    public int hashCode() {
        return ((this.left == null ? 0 : this.left.hashCode()) * 19) + HashCommon.float2int(this.right);
    }

    public String toString() {
        return "<" + left() + "," + rightFloat() + ">";
    }
}
