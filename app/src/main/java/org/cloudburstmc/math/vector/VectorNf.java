package org.cloudburstmc.math.vector;

import java.io.Serializable;
import java.util.Arrays;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.cloudburstmc.math.GenericMath;

@ParametersAreNonnullByDefault
/* loaded from: classes5.dex */
public class VectorNf implements Vectorf, Comparable<VectorNf>, Serializable, Cloneable {
    public static VectorNf ZERO_2 = new ImmutableZeroVectorN(0.0f, 0.0f);
    public static VectorNf ZERO_3 = new ImmutableZeroVectorN(0.0f, 0.0f, 0.0f);
    public static VectorNf ZERO_4 = new ImmutableZeroVectorN(0.0f, 0.0f, 0.0f, 0.0f);
    private static final long serialVersionUID = 1;
    private final float[] vec;

    private VectorNf(float[] v) {
        this.vec = (float[]) v.clone();
    }

    public int size() {
        return this.vec.length;
    }

    public float get(int comp) {
        return this.vec[comp];
    }

    public int getFloored(int comp) {
        return GenericMath.floor(get(comp));
    }

    public void set(int comp, double val) {
        set(comp, (float) val);
    }

    public void set(int comp, float val) {
        this.vec[comp] = val;
    }

    public void setZero() {
        Arrays.fill(this.vec, 0.0f);
    }

    @Nonnull
    public VectorNf resize(int size) {
        VectorNf d = from(size);
        System.arraycopy(this.vec, 0, d.vec, 0, Math.min(size, size()));
        return d;
    }

    @Nonnull
    public VectorNf add(VectorNf v) {
        return add(v.vec);
    }

    @Nonnull
    public VectorNf add(float... v) {
        int size = size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        VectorNf d = from(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = this.vec[comp] + v[comp];
        }
        return d;
    }

    @Nonnull
    public VectorNf sub(VectorNf v) {
        return sub(v.vec);
    }

    @Nonnull
    public VectorNf sub(float... v) {
        int size = size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        VectorNf d = from(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = this.vec[comp] - v[comp];
        }
        return d;
    }

    @Nonnull
    public VectorNf mul(double a) {
        return mul((float) a);
    }

    @Override // org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public VectorNf mul(float a) {
        int size = size();
        VectorNf d = from(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = this.vec[comp] * a;
        }
        return d;
    }

    @Nonnull
    public VectorNf mul(VectorNf v) {
        return mul(v.vec);
    }

    @Nonnull
    public VectorNf mul(float... v) {
        int size = size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        VectorNf d = from(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = this.vec[comp] * v[comp];
        }
        return d;
    }

    @Nonnull
    public VectorNf div(double a) {
        return div((float) a);
    }

    @Override // org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public VectorNf div(float a) {
        int size = size();
        VectorNf d = from(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = this.vec[comp] / a;
        }
        return d;
    }

    @Nonnull
    public VectorNf div(VectorNf v) {
        return div(v.vec);
    }

    @Nonnull
    public VectorNf div(float... v) {
        int size = size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        VectorNf d = from(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = this.vec[comp] / v[comp];
        }
        return d;
    }

    public float dot(VectorNf v) {
        return dot(v.vec);
    }

    public float dot(float... v) {
        int size = size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        float d = 0.0f;
        for (int comp = 0; comp < size; comp++) {
            d += this.vec[comp] * v[comp];
        }
        return d;
    }

    @Nonnull
    public VectorNf project(VectorNf v) {
        return project(v.vec);
    }

    @Nonnull
    public VectorNf project(float... v) {
        int size = size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        float lengthSquared = 0.0f;
        for (int comp = 0; comp < size; comp++) {
            lengthSquared += v[comp] * v[comp];
        }
        if (Math.abs(lengthSquared) < GenericMath.FLT_EPSILON) {
            throw new ArithmeticException("Cannot project onto the zero vector");
        }
        float a = dot(v) / lengthSquared;
        VectorNf d = from(size);
        for (int comp2 = 0; comp2 < size; comp2++) {
            d.vec[comp2] = v[comp2] * a;
        }
        return d;
    }

    @Nonnull
    public VectorNf pow(double pow) {
        return pow((float) pow);
    }

    @Override // org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public VectorNf pow(float power) {
        int size = size();
        VectorNf d = from(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = (float) Math.pow(this.vec[comp], power);
        }
        return d;
    }

    @Override // org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public VectorNf ceil() {
        int size = size();
        VectorNf d = from(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = (float) Math.ceil(this.vec[comp]);
        }
        return d;
    }

    @Override // org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public VectorNf floor() {
        int size = size();
        VectorNf d = from(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = GenericMath.floor(this.vec[comp]);
        }
        return d;
    }

    @Override // org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public VectorNf round() {
        int size = size();
        VectorNf d = from(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = Math.round(this.vec[comp]);
        }
        return d;
    }

    @Override // org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public VectorNf abs() {
        int size = size();
        VectorNf d = from(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = Math.abs(this.vec[comp]);
        }
        return d;
    }

    @Override // org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public VectorNf negate() {
        int size = size();
        VectorNf d = from(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = -this.vec[comp];
        }
        return d;
    }

    @Nonnull
    public VectorNf min(VectorNf v) {
        return min(v.vec);
    }

    @Nonnull
    public VectorNf min(float... v) {
        int size = size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        VectorNf d = from(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = Math.min(this.vec[comp], v[comp]);
        }
        return d;
    }

    @Nonnull
    public VectorNf max(VectorNf v) {
        return max(v.vec);
    }

    @Nonnull
    public VectorNf max(float... v) {
        int size = size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        VectorNf d = from(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = Math.max(this.vec[comp], v[comp]);
        }
        return d;
    }

    public float distanceSquared(VectorNf v) {
        return distanceSquared(v.vec);
    }

    public float distanceSquared(float... v) {
        int size = size();
        if (size != v.length) {
            throw new IllegalArgumentException("Vector sizes must be the same");
        }
        float d = 0.0f;
        for (int comp = 0; comp < size; comp++) {
            float delta = this.vec[comp] - v[comp];
            d += delta * delta;
        }
        return d;
    }

    public float distance(VectorNf v) {
        return distance(v.vec);
    }

    public float distance(float... v) {
        return (float) Math.sqrt(distanceSquared(v));
    }

    @Override // org.cloudburstmc.math.vector.Vectorf
    public float lengthSquared() {
        int size = size();
        float l = 0.0f;
        for (int comp = 0; comp < size; comp++) {
            l += this.vec[comp] * this.vec[comp];
        }
        return l;
    }

    @Override // org.cloudburstmc.math.vector.Vectorf
    public float length() {
        return (float) Math.sqrt(lengthSquared());
    }

    @Override // org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public VectorNf normalize() {
        float length = length();
        if (Math.abs(length) < GenericMath.FLT_EPSILON) {
            throw new ArithmeticException("Cannot normalize the zero vector");
        }
        int size = size();
        VectorNf d = from(size);
        for (int comp = 0; comp < size; comp++) {
            d.vec[comp] = this.vec[comp] / length;
        }
        return d;
    }

    @Override // org.cloudburstmc.math.vector.Vectorf
    public int getMinAxis() {
        int axis = 0;
        float value = this.vec[0];
        int size = size();
        for (int comp = 1; comp < size; comp++) {
            if (this.vec[comp] < value) {
                value = this.vec[comp];
                axis = comp;
            }
        }
        return axis;
    }

    @Override // org.cloudburstmc.math.vector.Vectorf
    public int getMaxAxis() {
        int axis = 0;
        float value = this.vec[0];
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
    public Vector2f toVector2() {
        return Vector2f.from(this);
    }

    @Nonnull
    public Vector3f toVector3() {
        return Vector3f.from(this);
    }

    @Nonnull
    public Vector4f toVector4() {
        return Vector4f.from(this);
    }

    @Override // org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public float[] toArray() {
        return (float[]) this.vec.clone();
    }

    @Override // org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public VectorNi toInt() {
        int size = size();
        int[] intVec = new int[size];
        for (int comp = 0; comp < size; comp++) {
            intVec[comp] = GenericMath.floor(this.vec[comp]);
        }
        return VectorNi.from(intVec);
    }

    @Override // org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public VectorNl toLong() {
        int size = size();
        long[] longVec = new long[size];
        for (int comp = 0; comp < size; comp++) {
            longVec[comp] = GenericMath.floor64(this.vec[comp]);
        }
        return VectorNl.from(longVec);
    }

    @Override // org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public VectorNf toFloat() {
        int size = size();
        float[] floatVec = new float[size];
        for (int comp = 0; comp < size; comp++) {
            floatVec[comp] = this.vec[comp];
        }
        return from(floatVec);
    }

    @Override // org.cloudburstmc.math.vector.Vectorf
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
    public int compareTo(VectorNf v) {
        return (int) Math.signum(lengthSquared() - v.lengthSquared());
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof VectorNf)) {
            return false;
        }
        return Arrays.equals(this.vec, ((VectorNf) obj).vec);
    }

    public int hashCode() {
        return Arrays.hashCode(this.vec) + 335;
    }

    @Override // 
    @Nonnull
    public VectorNf clone() {
        return from(this);
    }

    @Nonnull
    public String toString() {
        return Arrays.toString(this.vec).replace('[', '(').replace(']', ')');
    }

    /* loaded from: classes5.dex */
    private static class ImmutableZeroVectorN extends VectorNf {
        @Override // org.cloudburstmc.math.vector.VectorNf, org.cloudburstmc.math.vector.Vectorf
        @Nonnull
        public /* bridge */ /* synthetic */ Vectorf abs() {
            return super.abs();
        }

        @Override // org.cloudburstmc.math.vector.VectorNf, org.cloudburstmc.math.vector.Vectorf
        @Nonnull
        public /* bridge */ /* synthetic */ Vectorf ceil() {
            return super.ceil();
        }

        @Override // org.cloudburstmc.math.vector.VectorNf
        @Nonnull
        public /* bridge */ /* synthetic */ Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        @Override // org.cloudburstmc.math.vector.VectorNf, java.lang.Comparable
        public /* bridge */ /* synthetic */ int compareTo(VectorNf vectorNf) {
            return super.compareTo(vectorNf);
        }

        @Override // org.cloudburstmc.math.vector.VectorNf, org.cloudburstmc.math.vector.Vectorf
        @Nonnull
        public /* bridge */ /* synthetic */ Vectorf div(float f) {
            return super.div(f);
        }

        @Override // org.cloudburstmc.math.vector.VectorNf, org.cloudburstmc.math.vector.Vectorf
        @Nonnull
        public /* bridge */ /* synthetic */ Vectorf floor() {
            return super.floor();
        }

        @Override // org.cloudburstmc.math.vector.VectorNf, org.cloudburstmc.math.vector.Vectorf
        @Nonnull
        public /* bridge */ /* synthetic */ Vectorf mul(float f) {
            return super.mul(f);
        }

        @Override // org.cloudburstmc.math.vector.VectorNf, org.cloudburstmc.math.vector.Vectorf
        @Nonnull
        public /* bridge */ /* synthetic */ Vectorf negate() {
            return super.negate();
        }

        @Override // org.cloudburstmc.math.vector.VectorNf, org.cloudburstmc.math.vector.Vectorf
        @Nonnull
        public /* bridge */ /* synthetic */ Vectorf normalize() {
            return super.normalize();
        }

        @Override // org.cloudburstmc.math.vector.VectorNf, org.cloudburstmc.math.vector.Vectorf
        @Nonnull
        public /* bridge */ /* synthetic */ Vectorf pow(float f) {
            return super.pow(f);
        }

        @Override // org.cloudburstmc.math.vector.VectorNf, org.cloudburstmc.math.vector.Vectorf
        @Nonnull
        public /* bridge */ /* synthetic */ Vectorf round() {
            return super.round();
        }

        @Override // org.cloudburstmc.math.vector.VectorNf, org.cloudburstmc.math.vector.Vectorf
        @Nonnull
        public /* bridge */ /* synthetic */ Vectord toDouble() {
            return super.toDouble();
        }

        @Override // org.cloudburstmc.math.vector.VectorNf, org.cloudburstmc.math.vector.Vectorf
        @Nonnull
        public /* bridge */ /* synthetic */ Vectorf toFloat() {
            return super.toFloat();
        }

        @Override // org.cloudburstmc.math.vector.VectorNf, org.cloudburstmc.math.vector.Vectorf
        @Nonnull
        public /* bridge */ /* synthetic */ Vectori toInt() {
            return super.toInt();
        }

        @Override // org.cloudburstmc.math.vector.VectorNf, org.cloudburstmc.math.vector.Vectorf
        @Nonnull
        public /* bridge */ /* synthetic */ Vectorl toLong() {
            return super.toLong();
        }

        public ImmutableZeroVectorN(float... v) {
            super(v);
        }

        @Override // org.cloudburstmc.math.vector.VectorNf
        public void set(int comp, float val) {
            throw new UnsupportedOperationException("You may not alter this vector");
        }
    }

    @Nonnull
    public static VectorNf from(int size) {
        return from(new float[size]);
    }

    @Nonnull
    public static VectorNf from(Vector2f v) {
        return from(v.getX(), v.getY());
    }

    @Nonnull
    public static VectorNf from(Vector3f v) {
        return from(v.getX(), v.getY(), v.getZ());
    }

    @Nonnull
    public static VectorNf from(Vector4f v) {
        return from(v.getX(), v.getY(), v.getZ(), v.getW());
    }

    @Nonnull
    public static VectorNf from(VectorNf v) {
        return from(v.vec);
    }

    @Nonnull
    public static VectorNf from(float... v) {
        if (v.length < 2) {
            throw new IllegalArgumentException("Minimum vector size is 2");
        }
        return new VectorNf(v);
    }
}
