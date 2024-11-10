package org.cloudburstmc.math.vector;

import java.io.Serializable;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.cloudburstmc.math.GenericMath;

@ParametersAreNonnullByDefault
/* loaded from: classes5.dex */
public abstract class Vector4l implements Vectorl, Comparable<Vector4l>, Serializable, Cloneable {
    public static final Vector4l ZERO = from(0L, 0L, 0L, 0L);
    public static final Vector4l UNIT_X = from(1L, 0L, 0L, 0L);
    public static final Vector4l UNIT_Y = from(0L, 1L, 0L, 0L);
    public static final Vector4l UNIT_Z = from(0L, 0L, 1L, 0L);
    public static final Vector4l UNIT_W = from(0L, 0L, 0L, 1L);
    public static final Vector4l ONE = from(1L, 1L, 1L, 1L);

    @Override // org.cloudburstmc.math.vector.Vectorl
    @Nonnull
    public abstract Vector4l abs();

    @Nonnull
    public abstract Vector4l add(long j, long j2, long j3, long j4);

    @Nonnull
    public abstract Vector4l div(long j, long j2, long j3, long j4);

    public abstract long getW();

    public abstract long getX();

    public abstract long getY();

    public abstract long getZ();

    @Nonnull
    public abstract Vector4l max(long j, long j2, long j3, long j4);

    @Nonnull
    public abstract Vector4l min(long j, long j2, long j3, long j4);

    @Nonnull
    public abstract Vector4l mul(long j, long j2, long j3, long j4);

    @Override // org.cloudburstmc.math.vector.Vectorl
    @Nonnull
    public abstract Vector4l negate();

    @Override // org.cloudburstmc.math.vector.Vectorl
    @Nonnull
    public abstract Vector4l pow(long j);

    @Nonnull
    public abstract Vector4l project(long j, long j2, long j3, long j4);

    @Nonnull
    public abstract Vector4l sub(long j, long j2, long j3, long j4);

    @Nonnull
    public Vector4l add(Vector4l v) {
        return add(v.getX(), v.getY(), v.getZ(), v.getW());
    }

    @Nonnull
    public Vector4l add(double x, double y, double z, double w) {
        return add(GenericMath.floor64(x), GenericMath.floor64(y), GenericMath.floor64(z), GenericMath.floor64(w));
    }

    @Nonnull
    public Vector4l sub(Vector4l v) {
        return sub(v.getX(), v.getY(), v.getZ(), v.getW());
    }

    @Nonnull
    public Vector4l sub(double x, double y, double z, double w) {
        return sub(GenericMath.floor64(x), GenericMath.floor64(y), GenericMath.floor64(z), GenericMath.floor64(w));
    }

    @Nonnull
    public Vector4l mul(double a) {
        return mul(GenericMath.floor64(a));
    }

    @Override // org.cloudburstmc.math.vector.Vectorl
    @Nonnull
    public Vector4l mul(long a) {
        return mul(a, a, a, a);
    }

    @Nonnull
    public Vector4l mul(Vector4l v) {
        return mul(v.getX(), v.getY(), v.getZ(), v.getW());
    }

    @Nonnull
    public Vector4l mul(double x, double y, double z, double w) {
        return mul(GenericMath.floor64(x), GenericMath.floor64(y), GenericMath.floor64(z), GenericMath.floor64(w));
    }

    @Nonnull
    public Vector4l div(double a) {
        return div(GenericMath.floor64(a));
    }

    @Override // org.cloudburstmc.math.vector.Vectorl
    @Nonnull
    public Vector4l div(long a) {
        return div(a, a, a, a);
    }

    @Nonnull
    public Vector4l div(Vector4l v) {
        return div(v.getX(), v.getY(), v.getZ(), v.getW());
    }

    @Nonnull
    public Vector4l div(double x, double y, double z, double w) {
        return div(GenericMath.floor64(x), GenericMath.floor64(y), GenericMath.floor64(z), GenericMath.floor64(w));
    }

    public long dot(Vector4l v) {
        return dot(v.getX(), v.getY(), v.getZ(), v.getW());
    }

    public long dot(double x, double y, double z, double w) {
        return dot(GenericMath.floor64(x), GenericMath.floor64(y), GenericMath.floor64(z), GenericMath.floor64(w));
    }

    public long dot(long x, long y, long z, long w) {
        return (getX() * x) + (getY() * y) + (getZ() * z) + (getW() * w);
    }

    @Nonnull
    public Vector4l project(Vector4l v) {
        return project(v.getX(), v.getY(), v.getZ(), v.getW());
    }

    @Nonnull
    public Vector4l project(double x, double y, double z, double w) {
        return project(GenericMath.floor64(x), GenericMath.floor64(y), GenericMath.floor64(z), GenericMath.floor64(w));
    }

    @Nonnull
    public Vector4l pow(double pow) {
        return pow(GenericMath.floor64(pow));
    }

    @Nonnull
    public Vector4l min(Vector4l v) {
        return min(v.getX(), v.getY(), v.getZ(), v.getW());
    }

    @Nonnull
    public Vector4l min(double x, double y, double z, double w) {
        return min(GenericMath.floor64(x), GenericMath.floor64(y), GenericMath.floor64(z), GenericMath.floor64(w));
    }

    @Nonnull
    public Vector4l max(Vector4l v) {
        return max(v.getX(), v.getY(), v.getZ(), v.getW());
    }

    @Nonnull
    public Vector4l max(double x, double y, double z, double w) {
        return max(GenericMath.floor64(x), GenericMath.floor64(y), GenericMath.floor64(z), GenericMath.floor64(w));
    }

    public long distanceSquared(Vector4l v) {
        return distanceSquared(v.getX(), v.getY(), v.getZ(), v.getW());
    }

    public long distanceSquared(double x, double y, double z, double w) {
        return distanceSquared(GenericMath.floor64(x), GenericMath.floor64(y), GenericMath.floor64(z), GenericMath.floor64(w));
    }

    public long distanceSquared(long x, long y, long z, long w) {
        long dx = getX() - x;
        long dy = getY() - y;
        long dz = getZ() - z;
        long dw = getW() - w;
        return (dx * dx) + (dy * dy) + (dz * dz) + (dw * dw);
    }

    public double distance(Vector4l v) {
        return distance(v.getX(), v.getY(), v.getZ(), v.getW());
    }

    public double distance(double x, double y, double z, double w) {
        return distance(GenericMath.floor64(x), GenericMath.floor64(y), GenericMath.floor64(z), GenericMath.floor64(w));
    }

    public double distance(long x, long y, long z, long w) {
        return Math.sqrt(distanceSquared(x, y, z, w));
    }

    @Override // org.cloudburstmc.math.vector.Vectorl
    public long lengthSquared() {
        return (getX() * getX()) + (getY() * getY()) + (getZ() * getZ()) + (getW() * getW());
    }

    @Override // org.cloudburstmc.math.vector.Vectorl
    public double length() {
        return Math.sqrt(lengthSquared());
    }

    @Override // org.cloudburstmc.math.vector.Vectorl
    public int getMinAxis() {
        long value = getX();
        int axis = 0;
        if (getY() < value) {
            value = getY();
            axis = 1;
        }
        if (getZ() < value) {
            value = getZ();
            axis = 2;
        }
        if (getW() < value) {
            return 3;
        }
        return axis;
    }

    @Override // org.cloudburstmc.math.vector.Vectorl
    public int getMaxAxis() {
        long value = getX();
        int axis = 0;
        if (getY() > value) {
            value = getY();
            axis = 1;
        }
        if (getZ() > value) {
            value = getZ();
            axis = 2;
        }
        if (getW() > value) {
            return 3;
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
    public VectorNl toVectorN() {
        return VectorNl.from(this);
    }

    @Override // org.cloudburstmc.math.vector.Vectorl
    @Nonnull
    public long[] toArray() {
        return new long[]{getX(), getY(), getZ(), getW()};
    }

    @Override // org.cloudburstmc.math.vector.Vectorl
    @Nonnull
    public Vector4i toInt() {
        return Vector4i.from(getX(), getY(), getZ(), getW());
    }

    @Override // org.cloudburstmc.math.vector.Vectorl
    @Nonnull
    public Vector4l toLong() {
        return from(getX(), getY(), getZ(), getW());
    }

    @Override // org.cloudburstmc.math.vector.Vectorl
    @Nonnull
    public Vector4f toFloat() {
        return Vector4f.from((float) getX(), (float) getY(), (float) getZ(), (float) getW());
    }

    @Override // org.cloudburstmc.math.vector.Vectorl
    @Nonnull
    public Vector4d toDouble() {
        return Vector4d.from((float) getX(), (float) getY(), (float) getZ(), (float) getW());
    }

    @Override // java.lang.Comparable
    public int compareTo(Vector4l v) {
        return (int) (lengthSquared() - v.lengthSquared());
    }

    @Nonnull
    public Vector4l clone() {
        return from(this);
    }

    @Nonnull
    public String toString() {
        return "(" + getX() + ", " + getY() + ", " + getZ() + ", " + getW() + ")";
    }

    @Nonnull
    public static Vector4l from(long n) {
        return Vectors.createVector4l(n, n, n, n);
    }

    @Nonnull
    public static Vector4l from(Vector2l v) {
        return from(v, 0L, 0L);
    }

    @Nonnull
    public static Vector4l from(Vector2l v, double z, double w) {
        return from(v, GenericMath.floor64(z), GenericMath.floor64(w));
    }

    @Nonnull
    public static Vector4l from(Vector2l v, long z, long w) {
        return from(v.getX(), v.getY(), z, w);
    }

    @Nonnull
    public static Vector4l from(Vector3l v) {
        return from(v, 0L);
    }

    @Nonnull
    public static Vector4l from(Vector3l v, long w) {
        return from(v.getX(), v.getY(), v.getZ(), w);
    }

    @Nonnull
    public static Vector4l from(Vector4l v) {
        return from(v.getX(), v.getY(), v.getZ(), v.getW());
    }

    @Nonnull
    public static Vector4l from(VectorNl v) {
        return from(v.get(0), v.get(1), v.size() > 2 ? v.get(2) : 0L, v.size() > 3 ? v.get(3) : 0L);
    }

    @Nonnull
    public static Vector4l from(double x, double y, double z, double w) {
        return from(GenericMath.floor64(x), GenericMath.floor64(y), GenericMath.floor64(z), GenericMath.floor64(w));
    }

    @Nonnull
    public static Vector4l from(long x, long y, long z, long w) {
        return Vectors.createVector4l(x, y, z, w);
    }
}
