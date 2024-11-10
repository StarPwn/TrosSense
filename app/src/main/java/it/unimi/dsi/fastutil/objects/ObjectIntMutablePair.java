package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.Pair;
import java.io.Serializable;
import java.util.Objects;

/* loaded from: classes4.dex */
public class ObjectIntMutablePair<K> implements ObjectIntPair<K>, Serializable {
    private static final long serialVersionUID = 0;
    protected K left;
    protected int right;

    /* JADX WARN: Multi-variable type inference failed */
    @Override // it.unimi.dsi.fastutil.Pair
    public /* bridge */ /* synthetic */ Pair left(Object obj) {
        return left((ObjectIntMutablePair<K>) obj);
    }

    public ObjectIntMutablePair(K left, int right) {
        this.left = left;
        this.right = right;
    }

    public static <K> ObjectIntMutablePair<K> of(K left, int right) {
        return new ObjectIntMutablePair<>(left, right);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    public K left() {
        return this.left;
    }

    @Override // it.unimi.dsi.fastutil.Pair
    public ObjectIntMutablePair<K> left(K l) {
        this.left = l;
        return this;
    }

    @Override // it.unimi.dsi.fastutil.objects.ObjectIntPair
    public int rightInt() {
        return this.right;
    }

    @Override // it.unimi.dsi.fastutil.objects.ObjectIntPair
    public ObjectIntMutablePair<K> right(int r) {
        this.right = r;
        return this;
    }

    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        return other instanceof ObjectIntPair ? Objects.equals(this.left, ((ObjectIntPair) other).left()) && this.right == ((ObjectIntPair) other).rightInt() : (other instanceof Pair) && Objects.equals(this.left, ((Pair) other).left()) && Objects.equals(Integer.valueOf(this.right), ((Pair) other).right());
    }

    public int hashCode() {
        return ((this.left == null ? 0 : this.left.hashCode()) * 19) + this.right;
    }

    public String toString() {
        return "<" + left() + "," + rightInt() + ">";
    }
}
