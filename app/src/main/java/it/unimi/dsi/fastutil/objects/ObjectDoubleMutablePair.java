package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.Pair;
import java.io.Serializable;
import java.util.Objects;

/* loaded from: classes4.dex */
public class ObjectDoubleMutablePair<K> implements ObjectDoublePair<K>, Serializable {
    private static final long serialVersionUID = 0;
    protected K left;
    protected double right;

    /* JADX WARN: Multi-variable type inference failed */
    @Override // it.unimi.dsi.fastutil.Pair
    public /* bridge */ /* synthetic */ Pair left(Object obj) {
        return left((ObjectDoubleMutablePair<K>) obj);
    }

    public ObjectDoubleMutablePair(K left, double right) {
        this.left = left;
        this.right = right;
    }

    public static <K> ObjectDoubleMutablePair<K> of(K left, double right) {
        return new ObjectDoubleMutablePair<>(left, right);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    public K left() {
        return this.left;
    }

    @Override // it.unimi.dsi.fastutil.Pair
    public ObjectDoubleMutablePair<K> left(K l) {
        this.left = l;
        return this;
    }

    @Override // it.unimi.dsi.fastutil.objects.ObjectDoublePair
    public double rightDouble() {
        return this.right;
    }

    @Override // it.unimi.dsi.fastutil.objects.ObjectDoublePair
    public ObjectDoubleMutablePair<K> right(double r) {
        this.right = r;
        return this;
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
