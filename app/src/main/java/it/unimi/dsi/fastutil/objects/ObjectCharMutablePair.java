package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.Pair;
import java.io.Serializable;
import java.util.Objects;

/* loaded from: classes4.dex */
public class ObjectCharMutablePair<K> implements ObjectCharPair<K>, Serializable {
    private static final long serialVersionUID = 0;
    protected K left;
    protected char right;

    /* JADX WARN: Multi-variable type inference failed */
    @Override // it.unimi.dsi.fastutil.Pair
    public /* bridge */ /* synthetic */ Pair left(Object obj) {
        return left((ObjectCharMutablePair<K>) obj);
    }

    public ObjectCharMutablePair(K left, char right) {
        this.left = left;
        this.right = right;
    }

    public static <K> ObjectCharMutablePair<K> of(K left, char right) {
        return new ObjectCharMutablePair<>(left, right);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    public K left() {
        return this.left;
    }

    @Override // it.unimi.dsi.fastutil.Pair
    public ObjectCharMutablePair<K> left(K l) {
        this.left = l;
        return this;
    }

    @Override // it.unimi.dsi.fastutil.objects.ObjectCharPair
    public char rightChar() {
        return this.right;
    }

    @Override // it.unimi.dsi.fastutil.objects.ObjectCharPair
    public ObjectCharMutablePair<K> right(char r) {
        this.right = r;
        return this;
    }

    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        return other instanceof ObjectCharPair ? Objects.equals(this.left, ((ObjectCharPair) other).left()) && this.right == ((ObjectCharPair) other).rightChar() : (other instanceof Pair) && Objects.equals(this.left, ((Pair) other).left()) && Objects.equals(Character.valueOf(this.right), ((Pair) other).right());
    }

    public int hashCode() {
        return ((this.left == null ? 0 : this.left.hashCode()) * 19) + this.right;
    }

    public String toString() {
        return "<" + left() + "," + rightChar() + ">";
    }
}
