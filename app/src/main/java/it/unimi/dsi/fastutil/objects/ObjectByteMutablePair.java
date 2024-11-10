package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.Pair;
import java.io.Serializable;
import java.util.Objects;

/* loaded from: classes4.dex */
public class ObjectByteMutablePair<K> implements ObjectBytePair<K>, Serializable {
    private static final long serialVersionUID = 0;
    protected K left;
    protected byte right;

    /* JADX WARN: Multi-variable type inference failed */
    @Override // it.unimi.dsi.fastutil.Pair
    public /* bridge */ /* synthetic */ Pair left(Object obj) {
        return left((ObjectByteMutablePair<K>) obj);
    }

    public ObjectByteMutablePair(K left, byte right) {
        this.left = left;
        this.right = right;
    }

    public static <K> ObjectByteMutablePair<K> of(K left, byte right) {
        return new ObjectByteMutablePair<>(left, right);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    public K left() {
        return this.left;
    }

    @Override // it.unimi.dsi.fastutil.Pair
    public ObjectByteMutablePair<K> left(K l) {
        this.left = l;
        return this;
    }

    @Override // it.unimi.dsi.fastutil.objects.ObjectBytePair
    public byte rightByte() {
        return this.right;
    }

    @Override // it.unimi.dsi.fastutil.objects.ObjectBytePair
    public ObjectByteMutablePair<K> right(byte r) {
        this.right = r;
        return this;
    }

    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        return other instanceof ObjectBytePair ? Objects.equals(this.left, ((ObjectBytePair) other).left()) && this.right == ((ObjectBytePair) other).rightByte() : (other instanceof Pair) && Objects.equals(this.left, ((Pair) other).left()) && Objects.equals(Byte.valueOf(this.right), ((Pair) other).right());
    }

    public int hashCode() {
        return ((this.left == null ? 0 : this.left.hashCode()) * 19) + this.right;
    }

    public String toString() {
        return "<" + left() + "," + ((int) rightByte()) + ">";
    }
}
