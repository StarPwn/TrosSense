package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.Pair;
import java.io.Serializable;
import java.util.Objects;

/* loaded from: classes4.dex */
public class ObjectBooleanImmutablePair<K> implements ObjectBooleanPair<K>, Serializable {
    private static final long serialVersionUID = 0;
    protected final K left;
    protected final boolean right;

    public ObjectBooleanImmutablePair(K left, boolean right) {
        this.left = left;
        this.right = right;
    }

    public static <K> ObjectBooleanImmutablePair<K> of(K left, boolean right) {
        return new ObjectBooleanImmutablePair<>(left, right);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    public K left() {
        return this.left;
    }

    @Override // it.unimi.dsi.fastutil.objects.ObjectBooleanPair
    public boolean rightBoolean() {
        return this.right;
    }

    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        return other instanceof ObjectBooleanPair ? Objects.equals(this.left, ((ObjectBooleanPair) other).left()) && this.right == ((ObjectBooleanPair) other).rightBoolean() : (other instanceof Pair) && Objects.equals(this.left, ((Pair) other).left()) && Objects.equals(Boolean.valueOf(this.right), ((Pair) other).right());
    }

    public int hashCode() {
        return ((this.left == null ? 0 : this.left.hashCode()) * 19) + (this.right ? 1231 : 1237);
    }

    public String toString() {
        return "<" + left() + "," + rightBoolean() + ">";
    }
}
