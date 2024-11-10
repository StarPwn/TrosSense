package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.Pair;
import java.io.Serializable;
import java.util.Objects;

/* loaded from: classes4.dex */
public class ObjectFloatMutablePair<K> implements ObjectFloatPair<K>, Serializable {
    private static final long serialVersionUID = 0;
    protected K left;
    protected float right;

    /* JADX WARN: Multi-variable type inference failed */
    @Override // it.unimi.dsi.fastutil.Pair
    public /* bridge */ /* synthetic */ Pair left(Object obj) {
        return left((ObjectFloatMutablePair<K>) obj);
    }

    public ObjectFloatMutablePair(K left, float right) {
        this.left = left;
        this.right = right;
    }

    public static <K> ObjectFloatMutablePair<K> of(K left, float right) {
        return new ObjectFloatMutablePair<>(left, right);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    public K left() {
        return this.left;
    }

    @Override // it.unimi.dsi.fastutil.Pair
    public ObjectFloatMutablePair<K> left(K l) {
        this.left = l;
        return this;
    }

    @Override // it.unimi.dsi.fastutil.objects.ObjectFloatPair
    public float rightFloat() {
        return this.right;
    }

    @Override // it.unimi.dsi.fastutil.objects.ObjectFloatPair
    public ObjectFloatMutablePair<K> right(float r) {
        this.right = r;
        return this;
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
