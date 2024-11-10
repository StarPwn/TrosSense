package org.cloudburstmc.math.vector;

import java.io.Serializable;
import java.util.Arrays;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.cloudburstmc.math.GenericMath;

@ParametersAreNonnullByDefault
/* loaded from: classes5.dex */
public class VectorNi implements Vectori, Comparable<VectorNi>, Serializable, Cloneable {
    public static VectorNi ZERO_2 = new ImmutableZeroVectorN(0, 0);
    public static VectorNi ZERO_3 = new ImmutableZeroVectorN(0, 0, 0);
    public static VectorNi ZERO_4 = new ImmutableZeroVectorN(0, 0, 0, 0);
    private static final long serialVersionUID = 1;
    private final int[] vec;

    private VectorNi(int[] v) {
        this.vec = (int[]) v.clone();
    }

    public int size() {
        return this.vec.length;
    }

    public int get(int comp) {
        return this.vec[comp];
    }

    public void set(int comp, int val) {
        this.vec[comp] = val;
    }

    public void setZero() {
        Arrays.fill(this.vec, 0);
    }

    @Nonnull
    public VectorNi resize(int size) {
        VectorNi d = from(size);
        System.arraycopy(this.vec, 0, d.vec, 0, Math.min(size, size()));
        return d;
    }

    @Nonnull
    public VectorNi add(VectorNi v) {
        return add(v.vec);
    }

    @Nonnull
    public VectorNi add(int... v) {
        int size = size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        VectorNi d = from(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = this.vec[comp] + v[comp];
        }
        return d;
    }

    @Nonnull
    public VectorNi sub(VectorNi v) {
        return sub(v.vec);
    }

    @Nonnull
    public VectorNi sub(int... v) {
        int size = size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        VectorNi d = from(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = this.vec[comp] - v[comp];
        }
        return d;
    }

    @Nonnull
    public VectorNi mul(double a) {
        return mul(GenericMath.floor(a));
    }

    @Override // org.cloudburstmc.math.vector.Vectori
    @Nonnull
    public VectorNi mul(int a) {
        int size = size();
        VectorNi d = from(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = this.vec[comp] * a;
        }
        return d;
    }

    @Nonnull
    public VectorNi mul(VectorNi v) {
        return mul(v.vec);
    }

    @Nonnull
    public VectorNi mul(int... v) {
        int size = size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        VectorNi d = from(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = this.vec[comp] * v[comp];
        }
        return d;
    }

    @Nonnull
    public VectorNi div(double a) {
        return div(GenericMath.floor(a));
    }

    @Override // org.cloudburstmc.math.vector.Vectori
    @Nonnull
    public VectorNi div(int a) {
        int size = size();
        VectorNi d = from(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = this.vec[comp] / a;
        }
        return d;
    }

    @Nonnull
    public VectorNi div(VectorNi v) {
        return div(v.vec);
    }

    @Nonnull
    public VectorNi div(int... v) {
        int size = size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        VectorNi d = from(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = this.vec[comp] / v[comp];
        }
        return d;
    }

    public int dot(VectorNi v) {
        return dot(v.vec);
    }

    public int dot(int... v) {
        int size = size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        int d = 0;
        for (int comp = 0; comp < size; comp++) {
            d += this.vec[comp] * v[comp];
        }
        return d;
    }

    @Nonnull
    public VectorNi project(VectorNi v) {
        return project(v.vec);
    }

    @Nonnull
    public VectorNi project(int... v) {
        int size = size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        int lengthSquared = 0;
        for (int comp = 0; comp < size; comp++) {
            lengthSquared += v[comp] * v[comp];
        }
        if (lengthSquared == 0) {
            throw new ArithmeticException("Cannot project onto the zero vector");
        }
        float a = dot(v) / lengthSquared;
        VectorNi d = from(size);
        for (int comp2 = 0; comp2 < size; comp2++) {
            d.vec[comp2] = GenericMath.floor(v[comp2] * a);
        }
        return d;
    }

    @Nonnull
    public VectorNi pow(double pow) {
        return pow(GenericMath.floor(pow));
    }

    @Override // org.cloudburstmc.math.vector.Vectori
    @Nonnull
    public VectorNi pow(int power) {
        int size = size();
        VectorNi d = from(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = GenericMath.floor(Math.pow(this.vec[comp], power));
        }
        return d;
    }

    @Override // org.cloudburstmc.math.vector.Vectori
    @Nonnull
    public VectorNi abs() {
        int size = size();
        VectorNi d = from(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = Math.abs(this.vec[comp]);
        }
        return d;
    }

    @Override // org.cloudburstmc.math.vector.Vectori
    @Nonnull
    public VectorNi negate() {
        int size = size();
        VectorNi d = from(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = -this.vec[comp];
        }
        return d;
    }

    @Nonnull
    public VectorNi min(VectorNi v) {
        return min(v.vec);
    }

    @Nonnull
    public VectorNi min(int... v) {
        int size = size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        VectorNi d = from(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = Math.min(this.vec[comp], v[comp]);
        }
        return d;
    }

    @Nonnull
    public VectorNi max(VectorNi v) {
        return max(v.vec);
    }

    @Nonnull
    public VectorNi max(int... v) {
        int size = size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        VectorNi d = from(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = Math.max(this.vec[comp], v[comp]);
        }
        return d;
    }

    public int distanceSquared(VectorNi v) {
        return distanceSquared(v.vec);
    }

    public int distanceSquared(int... v) {
        int size = size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        int d = 0;
        for (int comp = 0; comp < size; comp++) {
            int delta = this.vec[comp] - v[comp];
            d += delta * delta;
        }
        return d;
    }

    public float distance(VectorNi v) {
        return distance(v.vec);
    }

    public float distance(int... v) {
        return (float) Math.sqrt(distanceSquared(v));
    }

    @Override // org.cloudburstmc.math.vector.Vectori
    public int lengthSquared() {
        int size = size();
        int l = 0;
        for (int comp = 0; comp < size; comp++) {
            l += this.vec[comp] * this.vec[comp];
        }
        return l;
    }

    @Override // org.cloudburstmc.math.vector.Vectori
    public float length() {
        return (float) Math.sqrt(lengthSquared());
    }

    @Override // org.cloudburstmc.math.vector.Vectori
    public int getMinAxis() {
        int axis = 0;
        int value = this.vec[0];
        int size = size();
        for (int comp = 1; comp < size; comp++) {
            if (this.vec[comp] < value) {
                value = this.vec[comp];
                axis = comp;
            }
        }
        return axis;
    }

    @Override // org.cloudburstmc.math.vector.Vectori
    public int getMaxAxis() {
        int axis = 0;
        int value = this.vec[0];
        int size = size();
        for (int comp = 1; comp < size; comp++) {
            if (this.vec[comp] > value) {
                value = this.vec[comp];
                axis = comp;
            }
        }
        return axis;
    }

    @Nonnull
    public Vector2i toVector2() {
        return Vector2i.from(this);
    }

    @Nonnull
    public Vector3i toVector3() {
        return Vector3i.from(this);
    }

    @Nonnull
    public Vector4i toVector4() {
        return Vector4i.from(this);
    }

    @Override // org.cloudburstmc.math.vector.Vectori
    @Nonnull
    public int[] toArray() {
        return (int[]) this.vec.clone();
    }

    @Override // org.cloudburstmc.math.vector.Vectori
    @Nonnull
    public VectorNi toInt() {
        int size = size();
        int[] intVec = new int[size];
        for (int comp = 0; comp < size; comp++) {
            intVec[comp] = this.vec[comp];
        }
        return from(intVec);
    }

    @Override // org.cloudburstmc.math.vector.Vectori
    @Nonnull
    public VectorNl toLong() {
        int size = size();
        long[] longVec = new long[size];
        for (int comp = 0; comp < size; comp++) {
            longVec[comp] = this.vec[comp];
        }
        return VectorNl.from(longVec);
    }

    @Override // org.cloudburstmc.math.vector.Vectori
    @Nonnull
    public VectorNf toFloat() {
        int size = size();
        float[] floatVec = new float[size];
        for (int comp = 0; comp < size; comp++) {
            floatVec[comp] = this.vec[comp];
        }
        return VectorNf.from(floatVec);
    }

    @Override // org.cloudburstmc.math.vector.Vectori
    @Nonnull
    public VectorNd toDouble() {
        int size = size();
        double[] doubleVec = new double[size];
        for (int comp = 0; comp < size; comp++) {
            doubleVec[comp] = this.vec[comp];
        }
        return VectorNd.from(doubleVec);
    }

    @Override // java.lang.Comparable
    public int compareTo(VectorNi v) {
        return lengthSquared() - v.lengthSquared();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof VectorNi)) {
            return false;
        }
        return Arrays.equals(this.vec, ((VectorNi) obj).vec);
    }

    public int hashCode() {
        return Arrays.hashCode(this.vec) + 335;
    }

    @Override // 
    @Nonnull
    public VectorNi clone() {
        return from(this);
    }

    @Nonnull
    public String toString() {
        return Arrays.toString(this.vec).replace('[', '(').replace(']', ')');
    }

    @ParametersAreNonnullByDefault
    /* loaded from: classes5.dex */
    private static class ImmutableZeroVectorN extends VectorNi {
        @Override // org.cloudburstmc.math.vector.VectorNi, org.cloudburstmc.math.vector.Vectori
        @Nonnull
        public /* bridge */ /* synthetic */ Vectori abs() {
            return super.abs();
        }

        @Override // org.cloudburstmc.math.vector.VectorNi
        @Nonnull
        public /* bridge */ /* synthetic */ Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        @Override // org.cloudburstmc.math.vector.VectorNi, java.lang.Comparable
        public /* bridge */ /* synthetic */ int compareTo(VectorNi vectorNi) {
            return super.compareTo(vectorNi);
        }

        @Override // org.cloudburstmc.math.vector.VectorNi, org.cloudburstmc.math.vector.Vectori
        @Nonnull
        public /* bridge */ /* synthetic */ Vectori div(int i) {
            return super.div(i);
        }

        @Override // org.cloudburstmc.math.vector.VectorNi, org.cloudburstmc.math.vector.Vectori
        @Nonnull
        public /* bridge */ /* synthetic */ Vectori mul(int i) {
            return super.mul(i);
        }

        @Override // org.cloudburstmc.math.vector.VectorNi, org.cloudburstmc.math.vector.Vectori
        @Nonnull
        public /* bridge */ /* synthetic */ Vectori negate() {
            return super.negate();
        }

        @Override // org.cloudburstmc.math.vector.VectorNi, org.cloudburstmc.math.vector.Vectori
        @Nonnull
        public /* bridge */ /* synthetic */ Vectori pow(int i) {
            return super.pow(i);
        }

        @Override // org.cloudburstmc.math.vector.VectorNi, org.cloudburstmc.math.vector.Vectori
        @Nonnull
        public /* bridge */ /* synthetic */ Vectord toDouble() {
            return super.toDouble();
        }

        @Override // org.cloudburstmc.math.vector.VectorNi, org.cloudburstmc.math.vector.Vectori
        @Nonnull
        public /* bridge */ /* synthetic */ Vectorf toFloat() {
            return super.toFloat();
        }

        @Override // org.cloudburstmc.math.vector.VectorNi, org.cloudburstmc.math.vector.Vectori
        @Nonnull
        public /* bridge */ /* synthetic */ Vectori toInt() {
            return super.toInt();
        }

        @Override // org.cloudburstmc.math.vector.VectorNi, org.cloudburstmc.math.vector.Vectori
        @Nonnull
        public /* bridge */ /* synthetic */ Vectorl toLong() {
            return super.toLong();
        }

        public ImmutableZeroVectorN(int... v) {
            super(v);
        }

        @Override // org.cloudburstmc.math.vector.VectorNi
        public void set(int comp, int val) {
            throw new UnsupportedOperationException("You may not alter this vector");
        }
    }

    @Nonnull
    public static VectorNi from(int size) {
        return from(new int[size]);
    }

    @Nonnull
    public static VectorNi from(Vector2i v) {
        return from(v.getX(), v.getY());
    }

    @Nonnull
    public static VectorNi from(Vector3i v) {
        return from(v.getX(), v.getY(), v.getZ());
    }

    @Nonnull
    public static VectorNi from(Vector4i v) {
        return from(v.getX(), v.getY(), v.getZ(), v.getW());
    }

    @Nonnull
    public static VectorNi from(VectorNi v) {
        return from(v.vec);
    }

    @Nonnull
    public static VectorNi from(int... v) {
        if (v.length < 2) {
            throw new IllegalArgumentException("Minimum vector size is 2");
        }
        return new VectorNi((int[]) v.clone());
    }
}
