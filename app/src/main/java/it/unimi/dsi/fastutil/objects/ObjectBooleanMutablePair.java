package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.Pair;
import java.io.Serializable;
import java.util.Objects;

/* loaded from: classes4.dex */
public class ObjectBooleanMutablePair<K> implements ObjectBooleanPair<K>, Serializable {
    private static final long serialVersionUID = 0;
    protected K left;
    protected boolean right;

    /* JADX WARN: Multi-variable type inference failed */
    @Override // it.unimi.dsi.fastutil.Pair
    public /* bridge */ /* synthetic */ Pair left(Object obj) {
        return left((ObjectBooleanMutablePair<K>) obj);
    }

    public ObjectBooleanMutablePair(K left, boolean right) {
        this.left = left;
        this.right = right;
    }

    public static <K> ObjectBooleanMutablePair<K> of(K left, boolean right) {
        return new ObjectBooleanMutablePair<>(left, right);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    public K left() {
        return this.left;
    }

    @Override // it.unimi.dsi.fastutil.Pair
    public ObjectBooleanMutablePair<K> left(K l) {
        this.left = l;
        return this;
    }

    @Override // it.unimi.dsi.fastutil.objects.ObjectBooleanPair
    public boolean rightBoolean() {
        return this.right;
    }

    @Override // it.unimi.dsi.fastutil.objects.ObjectBooleanPair
    public ObjectBooleanMutablePair<K> right(boolean r) {
        this.right = r;
        return this;
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
