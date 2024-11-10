package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.Pair;
import java.io.Serializable;
import java.util.Objects;

/* loaded from: classes4.dex */
public class ObjectShortMutablePair<K> implements ObjectShortPair<K>, Serializable {
    private static final long serialVersionUID = 0;
    protected K left;
    protected short right;

    /* JADX WARN: Multi-variable type inference failed */
    @Override // it.unimi.dsi.fastutil.Pair
    public /* bridge */ /* synthetic */ Pair left(Object obj) {
        return left((ObjectShortMutablePair<K>) obj);
    }

    public ObjectShortMutablePair(K left, short right) {
        this.left = left;
        this.right = right;
    }

    public static <K> ObjectShortMutablePair<K> of(K left, short right) {
        return new ObjectShortMutablePair<>(left, right);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    public K left() {
        return this.left;
    }

    @Override // it.unimi.dsi.fastutil.Pair
    public ObjectShortMutablePair<K> left(K l) {
        this.left = l;
        return this;
    }

    @Override // it.unimi.dsi.fastutil.objects.ObjectShortPair
    public short rightShort() {
        return this.right;
    }

    @Override // it.unimi.dsi.fastutil.objects.ObjectShortPair
    public ObjectShortMutablePair<K> right(short r) {
        this.right = r;
        return this;
    }

    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        return other instanceof ObjectShortPair ? Objects.equals(this.left, ((ObjectShortPair) other).left()) && this.right == ((ObjectShortPair) other).rightShort() : (other instanceof Pair) && Objects.equals(this.left, ((Pair) other).left()) && Objects.equals(Short.valueOf(this.right), ((Pair) other).right());
    }

    public int hashCode() {
        return ((this.left == null ? 0 : this.left.hashCode()) * 19) + this.right;
    }

    public String toString() {
        return "<" + left() + "," + ((int) rightShort()) + ">";
    }
}
