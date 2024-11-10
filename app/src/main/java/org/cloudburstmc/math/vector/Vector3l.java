package org.cloudburstmc.math.vector;

import java.io.Serializable;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.cloudburstmc.math.GenericMath;

@ParametersAreNonnullByDefault
/* loaded from: classes5.dex */
public abstract class Vector3l implements Vectorl, Comparable<Vector3l>, Serializable, Cloneable {
    public static final Vector3l ZERO = from(0L, 0L, 0L);
    public static final Vector3l UNIT_X = from(1L, 0L, 0L);
    public static final Vector3l UNIT_Y = from(0L, 1L, 0L);
    public static final Vector3l UNIT_Z = from(0L, 0L, 1L);
    public static final Vector3l ONE = from(1L, 1L, 1L);
    public static final Vector3l RIGHT = UNIT_X;
    public static final Vector3l UP = UNIT_Y;
    public static final Vector3l FORWARD = UNIT_Z;

    @Override // org.cloudburstmc.math.vector.Vectorl
    @Nonnull
    public abstract Vector3l abs();

    @Nonnull
    public abstract Vector3l add(long j, long j2, long j3);

    @Nonnull
    public abstract Vector3l cross(long j, long j2, long j3);

    @Nonnull
    public abstract Vector3l div(long j, long j2, long j3);

    @Nonnull
    public abstract Vector3l down(long j);

    @Nonnull
    public abstract Vector3l east(long j);

    public abstract long getX();

    public abstract long getY();

    public abstract long getZ();

    @Nonnull
    public abstract Vector3l max(long j, long j2, long j3);

    @Nonnull
    public abstract Vector3l min(long j, long j2, long j3);

    @Nonnull
    public abstract Vector3l mul(long j, long j2, long j3);

    @Override // org.cloudburstmc.math.vector.Vectorl
    @Nonnull
    public abstract Vector3l negate();

    @Nonnull
    public abstract Vector3l north(long j);

    @Override // org.cloudburstmc.math.vector.Vectorl
    @Nonnull
    public abstract Vector3l pow(long j);

    @Nonnull
    public abstract Vector3l project(long j, long j2, long j3);

    @Nonnull
    public abstract Vector3l south(long j);

    @Nonnull
    public abstract Vector3l sub(long j, long j2, long j3);

    @Nonnull
    public abstract Vector3l up(long j);

    @Nonnull
    public abstract Vector3l west(long j);

    @Nonnull
    public Vector3l add(Vector3l v) {
        return add(v.getX(), v.getY(), v.getZ());
    }

    @Nonnull
    public Vector3l add(double x, double y, double z) {
        return add(GenericMath.floor64(x), GenericMath.floor64(y), GenericMath.floor64(z));
    }

    @Nonnull
    public Vector3l sub(Vector3l v) {
        return sub(v.getX(), v.getY(), v.getZ());
    }

    @Nonnull
    public Vector3l sub(double x, double y, double z) {
        return sub(GenericMath.floor64(x), GenericMath.floor64(y), GenericMath.floor64(z));
    }

    @Nonnull
    public Vector3l mul(double a) {
        return mul(GenericMath.floor64(a));
    }

    @Override // org.cloudburstmc.math.vector.Vectorl
    @Nonnull
    public Vector3l mul(long a) {
        return mul(a, a, a);
    }

    @Nonnull
    public Vector3l mul(Vector3l v) {
        return mul(v.getX(), v.getY(), v.getZ());
    }

    @Nonnull
    public Vector3l mul(double x, double y, double z) {
        return mul(GenericMath.floor64(x), GenericMath.floor64(y), GenericMath.floor64(z));
    }

    @Nonnull
    public Vector3l div(double a) {
        return div(GenericMath.floor64(a));
    }

    @Override // org.cloudburstmc.math.vector.Vectorl
    @Nonnull
    public Vector3l div(long a) {
        return div(a, a, a);
    }

    @Nonnull
    public Vector3l div(Vector3l v) {
        return div(v.getX(), v.getY(), v.getZ());
    }

    @Nonnull
    public Vector3l div(double x, double y, double z) {
        return div(GenericMath.floor64(x), GenericMath.floor64(y), GenericMath.floor64(z));
    }

    public long dot(Vector3l v) {
        return dot(v.getX(), v.getY(), v.getZ());
    }

    public long dot(double x, double y, double z) {
        return dot(GenericMath.floor64(x), GenericMath.floor64(y), GenericMath.floor64(z));
    }

    public long dot(long x, long y, long z) {
        return (getX() * x) + (getY() * y) + (getZ() * z);
    }

    @Nonnull
    public Vector3l project(Vector3l v) {
        return project(v.getX(), v.getY(), v.getZ());
    }

    @Nonnull
    public Vector3l project(double x, double y, double z) {
        return project(GenericMath.floor64(x), GenericMath.floor64(y), GenericMath.floor64(z));
    }

    @Nonnull
    public Vector3l cross(Vector3l v) {
        return cross(v.getX(), v.getY(), v.getZ());
    }

    @Nonnull
    public Vector3l cross(double x, double y, double z) {
        return cross(GenericMath.floor64(x), GenericMath.floor64(y), GenericMath.floor64(z));
    }

    @Nonnull
    public Vector3l pow(double pow) {
        return pow(GenericMath.floor64(pow));
    }

    @Nonnull
    public Vector3l min(Vector3l v) {
        return min(v.getX(), v.getY(), v.getZ());
    }

    @Nonnull
    public Vector3l min(double x, double y, double z) {
        return min(GenericMath.floor64(x), GenericMath.floor64(y), GenericMath.floor64(z));
    }

    @Nonnull
    public Vector3l max(Vector3l v) {
        return max(v.getX(), v.getY(), v.getZ());
    }

    @Nonnull
    public Vector3l max(double x, double y, double z) {
        return max(GenericMath.floor64(x), GenericMath.floor64(y), GenericMath.floor64(z));
    }

    public long distanceSquared(Vector3l v) {
        return distanceSquared(v.getX(), v.getY(), v.getZ());
    }

    public long distanceSquared(double x, double y, double z) {
        return distanceSquared(GenericMath.floor64(x), GenericMath.floor64(y), GenericMath.floor64(z));
    }

    public long distanceSquared(long x, long y, long z) {
        long dx = getX() - x;
        long dy = getY() - y;
        long dz = getZ() - z;
        return (dx * dx) + (dy * dy) + (dz * dz);
    }

    public double distance(Vector3l v) {
        return distance(v.getX(), v.getY(), v.getZ());
    }

    public double distance(double x, double y, double z) {
        return distance(GenericMath.floor64(x), GenericMath.floor64(y), GenericMath.floor64(z));
    }

    public double distance(long x, long y, long z) {
        return Math.sqrt(distanceSquared(x, y, z));
    }

    @Nonnull
    public Vector3l up() {
        return up(1L);
    }

    @Nonnull
    public Vector3l down() {
        return down(1L);
    }

    @Nonnull
    public Vector3l north() {
        return north(1L);
    }

    @Nonnull
    public Vector3l south() {
        return south(1L);
    }

    @Nonnull
    public Vector3l east() {
        return east(1L);
    }

    @Nonnull
    public Vector3l west() {
        return west(1L);
    }

    @Override // org.cloudburstmc.math.vector.Vectorl
    public long lengthSquared() {
        return (getX() * getX()) + (getY() * getY()) + (getZ() * getZ());
    }

    @Override // org.cloudburstmc.math.vector.Vectorl
    public double length() {
        return Math.sqrt(lengthSquared());
    }

    @Override // org.cloudburstmc.math.vector.Vectorl
    public int getMinAxis() {
        return getX() < getY() ? getX() < getZ() ? 0 : 2 : getY() < getZ() ? 1 : 2;
    }

    @Override // org.cloudburstmc.math.vector.Vectorl
    public int getMaxAxis() {
        return getX() < getY() ? getY() < getZ() ? 2 : 1 : getX() < getZ() ? 2 : 0;
    }

    @Nonnull
    public Vector2l toVector2() {
        return Vector2l.from(this);
    }

    @Nonnull
    public Vector2l toVector2(boolean useZ) {
        return Vector2l.from(getX(), useZ ? getZ() : getY());
    }

    @Nonnull
    public Vector4l toVector4() {
        return toVector4(0L);
    }

    @Nonnull
    public Vector4l toVector4(double w) {
        return toVector4(GenericMath.floor64(w));
    }

    @Nonnull
    public Vector4l toVector4(long w) {
        return Vector4l.from(this, w);
    }

    @Nonnull
    public VectorNl toVectorN() {
        return VectorNl.from(this);
    }

    @Override // org.cloudburstmc.math.vector.Vectorl
    @Nonnull
    public long[] toArray() {
        return new long[]{getX(), getY(), getZ()};
    }

    @Override // org.cloudburstmc.math.vector.Vectorl
    @Nonnull
    public Vector3i toInt() {
        return Vector3i.from(getX(), getY(), getZ());
    }

    @Override // org.cloudburstmc.math.vector.Vectorl
    @Nonnull
    public Vector3l toLong() {
        return from(getX(), getY(), getZ());
    }

    @Override // org.cloudburstmc.math.vector.Vectorl
    @Nonnull
    public Vector3f toFloat() {
        return Vector3f.from((float) getX(), (float) getY(), (float) getZ());
    }

    @Override // org.cloudburstmc.math.vector.Vectorl
    @Nonnull
    public Vector3d toDouble() {
        return Vector3d.from((float) getX(), (float) getY(), (float) getZ());
    }

    @Override // java.lang.Comparable
    public int compareTo(Vector3l v) {
        return (int) (lengthSquared() - v.lengthSquared());
    }

    @Nonnull
    public Vector3l clone() {
        return from(this);
    }

    @Nonnull
    public String toString() {
        return "(" + getX() + ", " + getY() + ", " + getZ() + ")";
    }

    @Nonnull
    public static Vector3l from(long n) {
        return Vectors.createVector3l(n, n, n);
    }

    @Nonnull
    public static Vector3l from(Vector2l v) {
        return from(v, 0L);
    }

    @Nonnull
    public static Vector3l from(Vector2l v, double z) {
        return from(v, GenericMath.floor64(z));
    }

    @Nonnull
    public static Vector3l from(Vector2l v, long z) {
        return from(v.getX(), v.getY(), z);
    }

    @Nonnull
    public static Vector3l from(Vector3l v) {
        return from(v.getX(), v.getY(), v.getZ());
    }

    @Nonnull
    public static Vector3l from(Vector4l v) {
        return from(v.getX(), v.getY(), v.getZ());
    }

    @Nonnull
    public static Vector3l from(VectorNl v) {
        return from(v.get(0), v.get(1), v.size() > 2 ? v.get(2) : 0L);
    }

    @Nonnull
    public static Vector3l from(double x, double y, double z) {
        return from(GenericMath.floor64(x), GenericMath.floor64(y), GenericMath.floor64(z));
    }

    @Nonnull
    public static Vector3l from(long x, long y, long z) {
        return Vectors.createVector3l(x, y, z);
    }
}
