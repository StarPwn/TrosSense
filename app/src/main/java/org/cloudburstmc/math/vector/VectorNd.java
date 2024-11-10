package org.cloudburstmc.math.vector;

import java.io.Serializable;
import java.util.Arrays;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.cloudburstmc.math.GenericMath;

@ParametersAreNonnullByDefault
/* loaded from: classes5.dex */
public class VectorNd implements Vectord, Comparable<VectorNd>, Serializable, Cloneable {
    public static VectorNd ZERO_2 = new ImmutableZeroVectorN(0.0d, 0.0d);
    public static VectorNd ZERO_3 = new ImmutableZeroVectorN(0.0d, 0.0d, 0.0d);
    public static VectorNd ZERO_4 = new ImmutableZeroVectorN(0.0d, 0.0d, 0.0d, 0.0d);
    private static final long serialVersionUID = 1;
    private final double[] vec;

    private VectorNd(double[] v) {
        this.vec = (double[]) v.clone();
    }

    public int size() {
        return this.vec.length;
    }

    public double get(int comp) {
        return this.vec[comp];
    }

    public int getFloored(int comp) {
        return GenericMath.floor(get(comp));
    }

    public void set(int comp, float val) {
        set(comp, val);
    }

    public void set(int comp, double val) {
        this.vec[comp] = val;
    }

    public void setZero() {
        Arrays.fill(this.vec, 0.0d);
    }

    @Nonnull
    public VectorNd resize(int size) {
        VectorNd d = from(size);
        System.arraycopy(this.vec, 0, d.vec, 0, Math.min(size, size()));
        return d;
    }

    @Nonnull
    public VectorNd add(VectorNd v) {
        return add(v.vec);
    }

    @Nonnull
    public VectorNd add(double... v) {
        int size = size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        VectorNd d = from(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = this.vec[comp] + v[comp];
        }
        return d;
    }

    @Nonnull
    public VectorNd sub(VectorNd v) {
        return sub(v.vec);
    }

    @Nonnull
    public VectorNd sub(double... v) {
        int size = size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        VectorNd d = from(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = this.vec[comp] - v[comp];
        }
        return d;
    }

    @Nonnull
    public VectorNd mul(float a) {
        return mul(a);
    }

    @Override // org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public VectorNd mul(double a) {
        int size = size();
        VectorNd d = from(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = this.vec[comp] * a;
        }
        return d;
    }

    @Nonnull
    public VectorNd mul(VectorNd v) {
        return mul(v.vec);
    }

    @Nonnull
    public VectorNd mul(double... v) {
        int size = size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        VectorNd d = from(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = this.vec[comp] * v[comp];
        }
        return d;
    }

    @Nonnull
    public VectorNd div(float a) {
        return div(a);
    }

    @Override // org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public VectorNd div(double a) {
        int size = size();
        VectorNd d = from(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = this.vec[comp] / a;
        }
        return d;
    }

    @Nonnull
    public VectorNd div(VectorNd v) {
        return div(v.vec);
    }

    @Nonnull
    public VectorNd div(double... v) {
        int size = size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        VectorNd d = from(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = this.vec[comp] / v[comp];
        }
        return d;
    }

    public double dot(VectorNd v) {
        return dot(v.vec);
    }

    public double dot(double... v) {
        int size = size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        double d = 0.0d;
        for (int comp = 0; comp < size; comp++) {
            d += this.vec[comp] * v[comp];
        }
        return d;
    }

    @Nonnull
    public VectorNd project(VectorNd v) {
        return project(v.vec);
    }

    @Nonnull
    public VectorNd project(double... v) {
        int size = size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        double lengthSquared = 0.0d;
        for (int comp = 0; comp < size; comp++) {
            lengthSquared += v[comp] * v[comp];
        }
        if (Math.abs(lengthSquared) < GenericMath.DBL_EPSILON) {
            throw new ArithmeticException("Cannot project onto the zero vector");
        }
        double a = dot(v) / lengthSquared;
        VectorNd d = from(size);
        for (int comp2 = 0; comp2 < size; comp2++) {
            d.vec[comp2] = v[comp2] * a;
        }
        return d;
    }

    @Nonnull
    public VectorNd pow(float pow) {
        return pow(pow);
    }

    @Override // org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public VectorNd pow(double power) {
        int size = size();
        VectorNd d = from(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = Math.pow(this.vec[comp], power);
        }
        return d;
    }

    @Override // org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public VectorNd ceil() {
        int size = size();
        VectorNd d = from(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = Math.ceil(this.vec[comp]);
        }
        return d;
    }

    @Override // org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public VectorNd floor() {
        int size = size();
        VectorNd d = from(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = GenericMath.floor(this.vec[comp]);
        }
        return d;
    }

    @Override // org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public VectorNd round() {
        int size = size();
        VectorNd d = from(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = Math.round(this.vec[comp]);
        }
        return d;
    }

    @Override // org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public VectorNd abs() {
        int size = size();
        VectorNd d = from(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = Math.abs(this.vec[comp]);
        }
        return d;
    }

    @Override // org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public VectorNd negate() {
        int size = size();
        VectorNd d = from(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = -this.vec[comp];
        }
        return d;
    }

    @Nonnull
    public VectorNd min(VectorNd v) {
        return min(v.vec);
    }

    @Nonnull
    public VectorNd min(double... v) {
        int size = size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        VectorNd d = from(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = Math.min(this.vec[comp], v[comp]);
        }
        return d;
    }

    @Nonnull
    public VectorNd max(VectorNd v) {
        return max(v.vec);
    }

    @Nonnull
    public VectorNd max(double... v) {
        int size = size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        VectorNd d = from(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = Math.max(this.vec[comp], v[comp]);
        }
        return d;
    }

    public double distanceSquared(VectorNd v) {
        return distanceSquared(v.vec);
    }

    public double distanceSquared(double... v) {
        int size = size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        double d = 0.0d;
        for (int comp = 0; comp < size; comp++) {
            double delta = this.vec[comp] - v[comp];
            d += delta * delta;
        }
        return d;
    }

    public double distance(VectorNd v) {
        return distance(v.vec);
    }

    public double distance(double... v) {
        return Math.sqrt(distanceSquared(v));
    }

    @Override // org.cloudburstmc.math.vector.Vectord
    public double lengthSquared() {
        int size = size();
        double l = 0.0d;
        for (int comp = 0; comp < size; comp++) {
            l += this.vec[comp] * this.vec[comp];
        }
        return l;
    }

    @Override // org.cloudburstmc.math.vector.Vectord
    public double length() {
        return Math.sqrt(lengthSquared());
    }

    @Override // org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public VectorNd normalize() {
        double length = length();
        if (Math.abs(length) < GenericMath.DBL_EPSILON) {
            throw new ArithmeticException("Cannot normalize the zero vector");
        }
        int size = size();
        VectorNd d = from(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = this.vec[comp] / length;
        }
        return d;
    }

    @Override // org.cloudburstmc.math.vector.Vectord
    public int getMinAxis() {
        int axis = 0;
        double value = this.vec[0];
        int size = size();
        for (int comp = 1; comp < size; comp++) {
            if (this.vec[comp] < value) {
                value = this.vec[comp];
                axis = comp;
            }
        }
        return axis;
    }

    @Override // org.cloudburstmc.math.vector.Vectord
    public int getMaxAxis() {
        int axis = 0;
        double value = this.vec[0];
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
    public Vector2d toVector2() {
        return Vector2d.from(this);
    }

    @Nonnull
    public Vector3d toVector3() {
        return Vector3d.from(this);
    }

    @Nonnull
    public Vector4d toVector4() {
        return Vector4d.from(this);
    }

    @Override // org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public double[] toArray() {
        return (double[]) this.vec.clone();
    }

    @Override // org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public VectorNi toInt() {
        int size = size();
        int[] intVec = new int[size];
        for (int comp = 0; comp < size; comp++) {
            intVec[comp] = GenericMath.floor(this.vec[comp]);
        }
        return VectorNi.from(intVec);
    }

    @Override // org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public VectorNl toLong() {
        int size = size();
        long[] longVec = new long[size];
        for (int comp = 0; comp < size; comp++) {
            longVec[comp] = GenericMath.floor64(this.vec[comp]);
        }
        return VectorNl.from(longVec);
    }

    @Override // org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public VectorNf toFloat() {
        int size = size();
        float[] floatVec = new float[size];
        for (int comp = 0; comp < size; comp++) {
            floatVec[comp] = (float) this.vec[comp];
        }
        return VectorNf.from(floatVec);
    }

    @Override // org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public VectorNd toDouble() {
        int size = size();
        double[] doubleVec = new double[size];
        for (int comp = 0; comp < size; comp++) {
            doubleVec[comp] = this.vec[comp];
        }
        return from(doubleVec);
    }

    @Override // java.lang.Comparable
    public int compareTo(VectorNd v) {
        return (int) Math.signum(lengthSquared() - v.lengthSquared());
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof VectorNd)) {
            return false;
        }
        return Arrays.equals(this.vec, ((VectorNd) obj).vec);
    }

    public int hashCode() {
        return Arrays.hashCode(this.vec) + 335;
    }

    @Override // 
    @Nonnull
    public VectorNd clone() {
        return from(this);
    }

    @Nonnull
    public String toString() {
        return Arrays.toString(this.vec).replace('[', '(').replace(']', ')');
    }

    /* loaded from: classes5.dex */
    private static class ImmutableZeroVectorN extends VectorNd {
        @Override // org.cloudburstmc.math.vector.VectorNd, org.cloudburstmc.math.vector.Vectord
        @Nonnull
        public /* bridge */ /* synthetic */ Vectord abs() {
            return super.abs();
        }

        @Override // org.cloudburstmc.math.vector.VectorNd, org.cloudburstmc.math.vector.Vectord
        @Nonnull
        public /* bridge */ /* synthetic */ Vectord ceil() {
            return super.ceil();
        }

        @Override // org.cloudburstmc.math.vector.VectorNd
        @Nonnull
        public /* bridge */ /* synthetic */ Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        @Override // org.cloudburstmc.math.vector.VectorNd, java.lang.Comparable
        public /* bridge */ /* synthetic */ int compareTo(VectorNd vectorNd) {
            return super.compareTo(vectorNd);
        }

        @Override // org.cloudburstmc.math.vector.VectorNd, org.cloudburstmc.math.vector.Vectord
        @Nonnull
        public /* bridge */ /* synthetic */ Vectord div(double d) {
            return super.div(d);
        }

        @Override // org.cloudburstmc.math.vector.VectorNd, org.cloudburstmc.math.vector.Vectord
        @Nonnull
        public /* bridge */ /* synthetic */ Vectord floor() {
            return super.floor();
        }

        @Override // org.cloudburstmc.math.vector.VectorNd, org.cloudburstmc.math.vector.Vectord
        @Nonnull
        public /* bridge */ /* synthetic */ Vectord mul(double d) {
            return super.mul(d);
        }

        @Override // org.cloudburstmc.math.vector.VectorNd, org.cloudburstmc.math.vector.Vectord
        @Nonnull
        public /* bridge */ /* synthetic */ Vectord negate() {
            return super.negate();
        }

        @Override // org.cloudburstmc.math.vector.VectorNd, org.cloudburstmc.math.vector.Vectord
        @Nonnull
        public /* bridge */ /* synthetic */ Vectord normalize() {
            return super.normalize();
        }

        @Override // org.cloudburstmc.math.vector.VectorNd, org.cloudburstmc.math.vector.Vectord
        @Nonnull
        public /* bridge */ /* synthetic */ Vectord pow(double d) {
            return super.pow(d);
        }

        @Override // org.cloudburstmc.math.vector.VectorNd, org.cloudburstmc.math.vector.Vectord
        @Nonnull
        public /* bridge */ /* synthetic */ Vectord round() {
            return super.round();
        }

        @Override // org.cloudburstmc.math.vector.VectorNd, org.cloudburstmc.math.vector.Vectord
        @Nonnull
        public /* bridge */ /* synthetic */ Vectord toDouble() {
            return super.toDouble();
        }

        @Override // org.cloudburstmc.math.vector.VectorNd, org.cloudburstmc.math.vector.Vectord
        @Nonnull
        public /* bridge */ /* synthetic */ Vectorf toFloat() {
            return super.toFloat();
        }

        @Override // org.cloudburstmc.math.vector.VectorNd, org.cloudburstmc.math.vector.Vectord
        @Nonnull
        public /* bridge */ /* synthetic */ Vectori toInt() {
            return super.toInt();
        }

        @Override // org.cloudburstmc.math.vector.VectorNd, org.cloudburstmc.math.vector.Vectord
        @Nonnull
        public /* bridge */ /* synthetic */ Vectorl toLong() {
            return super.toLong();
        }

        public ImmutableZeroVectorN(double... v) {
            super(v);
        }

        @Override // org.cloudburstmc.math.vector.VectorNd
        public void set(int comp, double val) {
            throw new UnsupportedOperationException("You may not alter this vector");
        }
    }

    @Nonnull
    public static VectorNd from(int size) {
        return from(new double[size]);
    }

    @Nonnull
    public static VectorNd from(Vector2d v) {
        return from(v.getX(), v.getY());
    }

    @Nonnull
    public static VectorNd from(Vector3d v) {
        return from(v.getX(), v.getY(), v.getZ());
    }

    @Nonnull
    public static VectorNd from(Vector4d v) {
        return from(v.getX(), v.getY(), v.getZ(), v.getW());
    }

    @Nonnull
    public static VectorNd from(VectorNd v) {
        return from(v.vec);
    }

    @Nonnull
    public static VectorNd from(double... v) {
        if (v.length < 2) {
            throw new IllegalArgumentException("Minimum vector size is 2");
        }
        return new VectorNd(v);
    }
}
