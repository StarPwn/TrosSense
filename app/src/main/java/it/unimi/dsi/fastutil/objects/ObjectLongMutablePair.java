package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.Pair;
import java.io.Serializable;
import java.util.Objects;

/* loaded from: classes4.dex */
public class ObjectLongMutablePair<K> implements ObjectLongPair<K>, Serializable {
    private static final long serialVersionUID = 0;
    protected K left;
    protected long right;

    /* JADX WARN: Multi-variable type inference failed */
    @Override // it.unimi.dsi.fastutil.Pair
    public /* bridge */ /* synthetic */ Pair left(Object obj) {
        return left((ObjectLongMutablePair<K>) obj);
    }

    public ObjectLongMutablePair(K left, long right) {
        this.left = left;
        this.right = right;
    }

    public static <K> ObjectLongMutablePair<K> of(K left, long right) {
        return new ObjectLongMutablePair<>(left, right);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    public K left() {
        return this.left;
    }

    @Override // it.unimi.dsi.fastutil.Pair
    public ObjectLongMutablePair<K> left(K l) {
        this.left = l;
        return this;
    }

    @Override // it.unimi.dsi.fastutil.objects.ObjectLongPair
    public long rightLong() {
        return this.right;
    }

    @Override // it.unimi.dsi.fastutil.objects.ObjectLongPair
    public ObjectLongMutablePair<K> right(long r) {
        this.right = r;
        return this;
    }

    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        return other instanceof ObjectLongPair ? Objects.equals(this.left, ((ObjectLongPair) other).left()) && this.right == ((ObjectLongPair) other).rightLong() : (other instanceof Pair) && Objects.equals(this.left, ((Pair) other).left()) && Objects.equals(Long.valueOf(this.right), ((Pair) other).right());
    }

    public int hashCode() {
        return ((this.left == null ? 0 : this.left.hashCode()) * 19) + HashCommon.long2int(this.right);
    }

    public String toString() {
        return "<" + left() + "," + rightLong() + ">";
    }
}
