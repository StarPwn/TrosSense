package org.cloudburstmc.math.vector;

import java.io.Serializable;
import java.util.Arrays;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.cloudburstmc.math.GenericMath;

@ParametersAreNonnullByDefault
/* loaded from: classes5.dex */
public class VectorNl implements Vectorl, Comparable<VectorNl>, Serializable, Cloneable {
    public static VectorNl ZERO_2 = new ImmutableZeroVectorN(0, 0);
    public static VectorNl ZERO_3 = new ImmutableZeroVectorN(0, 0, 0);
    public static VectorNl ZERO_4 = new ImmutableZeroVectorN(0, 0, 0, 0);
    private static final long serialVersionUID = 1;
    private final long[] vec;

    private VectorNl(long[] v) {
        this.vec = (long[]) v.clone();
    }

    public int size() {
        return this.vec.length;
    }

    public long get(int comp) {
        return this.vec[comp];
    }

    public void set(int comp, long val) {
        this.vec[comp] = val;
    }

    public void setZero() {
        Arrays.fill(this.vec, 0L);
    }

    @Nonnull
    public VectorNl resize(int size) {
        VectorNl d = from(size);
        System.arraycopy(this.vec, 0, d.vec, 0, Math.min(size, size()));
        return d;
    }

    @Nonnull
    public VectorNl add(VectorNl v) {
        return add(v.vec);
    }

    @Nonnull
    public VectorNl add(long... v) {
        int size = size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        VectorNl d = from(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = this.vec[comp] + v[comp];
        }
        return d;
    }

    @Nonnull
    public VectorNl sub(VectorNl v) {
        return sub(v.vec);
    }

    @Nonnull
    public VectorNl sub(long... v) {
        int size = size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        VectorNl d = from(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = this.vec[comp] - v[comp];
        }
        return d;
    }

    @Nonnull
    public VectorNl mul(double a) {
        return mul(GenericMath.floor64(a));
    }

    @Override // org.cloudburstmc.math.vector.Vectorl
    @Nonnull
    public VectorNl mul(long a) {
        int size = size();
        VectorNl d = from(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = this.vec[comp] * a;
        }
        return d;
    }

    @Nonnull
    public VectorNl mul(VectorNl v) {
        return mul(v.vec);
    }

    @Nonnull
    public VectorNl mul(long... v) {
        int size = size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        VectorNl d = from(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = this.vec[comp] * v[comp];
        }
        return d;
    }

    @Nonnull
    public VectorNl div(double a) {
        return div(GenericMath.floor64(a));
    }

    @Override // org.cloudburstmc.math.vector.Vectorl
    @Nonnull
    public VectorNl div(long a) {
        int size = size();
        VectorNl d = from(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = this.vec[comp] / a;
        }
        return d;
    }

    @Nonnull
    public VectorNl div(VectorNl v) {
        return div(v.vec);
    }

    @Nonnull
    public VectorNl div(long... v) {
        int size = size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        VectorNl d = from(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = this.vec[comp] / v[comp];
        }
        return d;
    }

    public long dot(VectorNl v) {
        return dot(v.vec);
    }

    public long dot(long... v) {
        int size = size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        long d = 0;
        for (int comp = 0; comp < size; comp++) {
            d += this.vec[comp] * v[comp];
        }
        return d;
    }

    @Nonnull
    public VectorNl project(VectorNl v) {
        return project(v.vec);
    }

    @Nonnull
    public VectorNl project(long... v) {
        int size = size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        long lengthSquared = 0;
        for (int comp = 0; comp < size; comp++) {
            lengthSquared += v[comp] * v[comp];
        }
        if (lengthSquared == 0) {
            throw new ArithmeticException("Cannot project onto the zero vector");
        }
        double a = dot(v) / lengthSquared;
        VectorNl d = from(size);
        for (int comp2 = 0; comp2 < size; comp2++) {
            d.vec[comp2] = GenericMath.floor64(v[comp2] * a);
        }
        return d;
    }

    @Nonnull
    public VectorNl pow(double pow) {
        return pow(GenericMath.floor64(pow));
    }

    @Override // org.cloudburstmc.math.vector.Vectorl
    @Nonnull
    public VectorNl pow(long power) {
        int size = size();
        VectorNl d = from(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = GenericMath.floor64(Math.pow(this.vec[comp], power));
        }
        return d;
    }

    @Override // org.cloudburstmc.math.vector.Vectorl
    @Nonnull
    public VectorNl abs() {
        int size = size();
        VectorNl d = from(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = Math.abs(this.vec[comp]);
        }
        return d;
    }

    @Override // org.cloudburstmc.math.vector.Vectorl
    @Nonnull
    public VectorNl negate() {
        int size = size();
        VectorNl d = from(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = -this.vec[comp];
        }
        return d;
    }

    @Nonnull
    public VectorNl min(VectorNl v) {
        return min(v.vec);
    }

    @Nonnull
    public VectorNl min(long... v) {
        int size = size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        VectorNl d = from(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = Math.min(this.vec[comp], v[comp]);
        }
        return d;
    }

    @Nonnull
    public VectorNl max(VectorNl v) {
        return max(v.vec);
    }

    @Nonnull
    public VectorNl max(long... v) {
        int size = size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        VectorNl d = from(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = Math.max(this.vec[comp], v[comp]);
        }
        return d;
    }

    public long distanceSquared(VectorNl v) {
        return distanceSquared(v.vec);
    }

    public long distanceSquared(long... v) {
        int size = size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        long d = 0;
        for (int comp = 0; comp < size; comp++) {
            long delta = this.vec[comp] - v[comp];
            d += delta * delta;
        }
        return d;
    }

    public double distance(VectorNl v) {
        return distance(v.vec);
    }

    public double distance(long... v) {
        return Math.sqrt(distanceSquared(v));
    }

    @Override // org.cloudburstmc.math.vector.Vectorl
    public long lengthSquared() {
        int size = size();
        long l = 0;
        for (int comp = 0; comp < size; comp++) {
            l += this.vec[comp] * this.vec[comp];
        }
        return l;
    }

    @Override // org.cloudburstmc.math.vector.Vectorl
    public double length() {
        return Math.sqrt(lengthSquared());
    }

    @Override // org.cloudburstmc.math.vector.Vectorl
    public int getMinAxis() {
        int axis = 0;
        long value = this.vec[0];
        int size = size();
        for (int comp = 1; comp < size; comp++) {
            if (this.vec[comp] < value) {
                value = this.vec[comp];
                axis = comp;
            }
        }
        return axis;
    }

    @Override // org.cloudburstmc.math.vector.Vectorl
    public int getMaxAxis() {
        int axis = 0;
        long value = this.vec[0];
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
    public Vector2l toVector2() {
        return Vector2l.from(this);
    }

    @Nonnull
    public Vector3l toVector3() {
        return Vector3l.from(this);
    }

    @Nonnull
    public Vector4l toVector4() {
        return Vector4l.from(this);
    }

    @Override // org.cloudburstmc.math.vector.Vectorl
    @Nonnull
    public long[] toArray() {
        return (long[]) this.vec.clone();
    }

    @Override // org.cloudburstmc.math.vector.Vectorl
    @Nonnull
    public VectorNi toInt() {
        int size = size();
        int[] intVec = new int[size];
        for (int comp = 0; comp < size; comp++) {
            intVec[comp] = (int) this.vec[comp];
        }
        return VectorNi.from(intVec);
    }

    @Override // org.cloudburstmc.math.vector.Vectorl
    @Nonnull
    public VectorNl toLong() {
        int size = size();
        long[] longVec = new long[size];
        for (int comp = 0; comp < size; comp++) {
            longVec[comp] = this.vec[comp];
        }
        return from(longVec);
    }

    @Override // org.cloudburstmc.math.vector.Vectorl
    @Nonnull
    public VectorNf toFloat() {
        int size = size();
        float[] floatVec = new float[size];
        for (int comp = 0; comp < size; comp++) {
            floatVec[comp] = (float) this.vec[comp];
        }
        return VectorNf.from(floatVec);
    }

    @Override // org.cloudburstmc.math.vector.Vectorl
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
    public int compareTo(VectorNl v) {
        return (int) (lengthSquared() - v.lengthSquared());
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof VectorNl)) {
            return false;
        }
        return Arrays.equals(this.vec, ((VectorNl) obj).vec);
    }

    public int hashCode() {
        return Arrays.hashCode(this.vec) + 335;
    }

    @Override // 
    @Nonnull
    public VectorNl clone() {
        return from(this);
    }

    @Nonnull
    public String toString() {
        return Arrays.toString(this.vec).replace('[', '(').replace(']', ')');
    }

    @ParametersAreNonnullByDefault
    /* loaded from: classes5.dex */
    private static class ImmutableZeroVectorN extends VectorNl {
        @Override // org.cloudburstmc.math.vector.VectorNl, org.cloudburstmc.math.vector.Vectorl
        @Nonnull
        public /* bridge */ /* synthetic */ Vectorl abs() {
            return super.abs();
        }

        @Override // org.cloudburstmc.math.vector.VectorNl
        @Nonnull
        public /* bridge */ /* synthetic */ Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        @Override // org.cloudburstmc.math.vector.VectorNl, java.lang.Comparable
        public /* bridge */ /* synthetic */ int compareTo(VectorNl vectorNl) {
            return super.compareTo(vectorNl);
        }

        @Override // org.cloudburstmc.math.vector.VectorNl, org.cloudburstmc.math.vector.Vectorl
        @Nonnull
        public /* bridge */ /* synthetic */ Vectorl div(long j) {
            return super.div(j);
        }

        @Override // org.cloudburstmc.math.vector.VectorNl, org.cloudburstmc.math.vector.Vectorl
        @Nonnull
        public /* bridge */ /* synthetic */ Vectorl mul(long j) {
            return super.mul(j);
        }

        @Override // org.cloudburstmc.math.vector.VectorNl, org.cloudburstmc.math.vector.Vectorl
        @Nonnull
        public /* bridge */ /* synthetic */ Vectorl negate() {
            return super.negate();
        }

        @Override // org.cloudburstmc.math.vector.VectorNl, org.cloudburstmc.math.vector.Vectorl
        @Nonnull
        public /* bridge */ /* synthetic */ Vectorl pow(long j) {
            return super.pow(j);
        }

        @Override // org.cloudburstmc.math.vector.VectorNl, org.cloudburstmc.math.vector.Vectorl
        @Nonnull
        public /* bridge */ /* synthetic */ Vectord toDouble() {
            return super.toDouble();
        }

        @Override // org.cloudburstmc.math.vector.VectorNl, org.cloudburstmc.math.vector.Vectorl
        @Nonnull
        public /* bridge */ /* synthetic */ Vectorf toFloat() {
            return super.toFloat();
        }

        @Override // org.cloudburstmc.math.vector.VectorNl, org.cloudburstmc.math.vector.Vectorl
        @Nonnull
        public /* bridge */ /* synthetic */ Vectori toInt() {
            return super.toInt();
        }

        @Override // org.cloudburstmc.math.vector.VectorNl, org.cloudburstmc.math.vector.Vectorl
        @Nonnull
        public /* bridge */ /* synthetic */ Vectorl toLong() {
            return super.toLong();
        }

        public ImmutableZeroVectorN(long... v) {
            super(v);
        }

        @Override // org.cloudburstmc.math.vector.VectorNl
        public void set(int comp, long val) {
            throw new UnsupportedOperationException("You may not alter this vector");
        }
    }

    @Nonnull
    public static VectorNl from(int size) {
        return from(new long[size]);
    }

    @Nonnull
    public static VectorNl from(Vector2l v) {
        return from(v.getX(), v.getY());
    }

    @Nonnull
    public static VectorNl from(Vector3l v) {
        return from(v.getX(), v.getY(), v.getZ());
    }

    @Nonnull
    public static VectorNl from(Vector4l v) {
        return from(v.getX(), v.getY(), v.getZ(), v.getW());
    }

    @Nonnull
    public static VectorNl from(VectorNl v) {
        return from(v.vec);
    }

    @Nonnull
    public static VectorNl from(long... v) {
        if (v.length < 2) {
            throw new IllegalArgumentException("Minimum vector size is 2");
        }
        return new VectorNl((long[]) v.clone());
    }
}
