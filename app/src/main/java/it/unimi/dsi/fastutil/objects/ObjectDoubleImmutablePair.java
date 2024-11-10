package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.Pair;
import java.io.Serializable;
import java.util.Objects;

/* loaded from: classes4.dex */
public class ObjectDoubleImmutablePair<K> implements ObjectDoublePair<K>, Serializable {
    private static final long serialVersionUID = 0;
    protected final K left;
    protected final double right;

    public ObjectDoubleImmutablePair(K left, double right) {
        this.left = left;
        this.right = right;
    }

    public static <K> ObjectDoubleImmutablePair<K> of(K left, double right) {
        return new ObjectDoubleImmutablePair<>(left, right);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    public K left() {
        return this.left;
    }

    @Override // it.unimi.dsi.fastutil.objects.ObjectDoublePair
    public double rightDouble() {
        return this.right;
    }

    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        return other instanceof ObjectDoublePair ? Objects.equals(this.left, ((ObjectDoublePair) other).left()) && this.right == ((ObjectDoublePair) other).rightDouble() : (other instanceof Pair) && Objects.equals(this.left, ((Pair) other).left()) && Objects.equals(Double.valueOf(this.right), ((Pair) other).right());
    }

    public int hashCode() {
        return ((this.left == null ? 0 : this.left.hashCode()) * 19) + HashCommon.double2int(this.right);
    }

    public String toString() {
        return "<" + left() + "," + rightDouble() + ">";
    }
}
