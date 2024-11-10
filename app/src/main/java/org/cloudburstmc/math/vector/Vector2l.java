package org.cloudburstmc.math.vector;

import java.io.Serializable;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.cloudburstmc.math.GenericMath;

@ParametersAreNonnullByDefault
/* loaded from: classes5.dex */
public abstract class Vector2l implements Vectorl, Comparable<Vector2l>, Serializable, Cloneable {
    public static final Vector2l ZERO = from(0L, 0L);
    public static final Vector2l UNIT_X = from(1L, 0L);
    public static final Vector2l UNIT_Y = from(0L, 1L);
    public static final Vector2l ONE = from(1L, 1L);

    @Override // org.cloudburstmc.math.vector.Vectorl
    @Nonnull
    public abstract Vector2l abs();

    @Nonnull
    public abstract Vector2l add(long j, long j2);

    @Nonnull
    public abstract Vector2l div(long j, long j2);

    @Nonnull
    public abstract Vector2l east(long j);

    public abstract long getX();

    public abstract long getY();

    @Nonnull
    public abstract Vector2l max(long j, long j2);

    @Nonnull
    public abstract Vector2l min(long j, long j2);

    @Nonnull
    public abstract Vector2l mul(long j, long j2);

    @Override // org.cloudburstmc.math.vector.Vectorl
    @Nonnull
    public abstract Vector2l negate();

    @Nonnull
    public abstract Vector2l north(long j);

    @Override // org.cloudburstmc.math.vector.Vectorl
    @Nonnull
    public abstract Vector2l pow(long j);

    @Nonnull
    public abstract Vector2l project(long j, long j2);

    @Nonnull
    public abstract Vector2l south(long j);

    @Nonnull
    public abstract Vector2l sub(long j, long j2);

    @Nonnull
    public abstract Vector2l west(long j);

    @Nonnull
    public Vector2l add(Vector2l v) {
        return add(v.getX(), v.getY());
    }

    @Nonnull
    public Vector2l add(double x, double y) {
        return add(GenericMath.floor64(x), GenericMath.floor64(y));
    }

    @Nonnull
    public Vector2l sub(Vector2l v) {
        return sub(v.getX(), v.getY());
    }

    @Nonnull
    public Vector2l sub(double x, double y) {
        return sub(GenericMath.floor64(x), GenericMath.floor64(y));
    }

    @Nonnull
    public Vector2l mul(double a) {
        return mul(GenericMath.floor64(a));
    }

    @Override // org.cloudburstmc.math.vector.Vectorl
    @Nonnull
    public Vector2l mul(long a) {
        return mul(a, a);
    }

    @Nonnull
    public Vector2l mul(Vector2l v) {
        return mul(v.getX(), v.getY());
    }

    @Nonnull
    public Vector2l mul(double x, double y) {
        return mul(GenericMath.floor64(x), GenericMath.floor64(y));
    }

    @Nonnull
    public Vector2l div(double a) {
        return div(GenericMath.floor64(a));
    }

    @Override // org.cloudburstmc.math.vector.Vectorl
    @Nonnull
    public Vector2l div(long a) {
        return div(a, a);
    }

    @Nonnull
    public Vector2l div(Vector2l v) {
        return div(v.getX(), v.getY());
    }

    @Nonnull
    public Vector2l div(double x, double y) {
        return div(GenericMath.floor64(x), GenericMath.floor64(y));
    }

    public long dot(Vector2l v) {
        return dot(v.getX(), v.getY());
    }

    public long dot(double x, double y) {
        return dot(GenericMath.floor64(x), GenericMath.floor64(y));
    }

    public long dot(long x, long y) {
        return (getX() * x) + (getY() * y);
    }

    @Nonnull
    public Vector2l project(Vector2l v) {
        return project(v.getX(), v.getY());
    }

    @Nonnull
    public Vector2l project(double x, double y) {
        return project(GenericMath.floor64(x), GenericMath.floor64(y));
    }

    @Nonnull
    public Vector2l pow(double pow) {
        return pow(GenericMath.floor64(pow));
    }

    @Nonnull
    public Vector2l min(Vector2l v) {
        return min(v.getX(), v.getY());
    }

    @Nonnull
    public Vector2l min(double x, double y) {
        return min(GenericMath.floor64(x), GenericMath.floor64(y));
    }

    @Nonnull
    public Vector2l max(Vector2l v) {
        return max(v.getX(), v.getY());
    }

    @Nonnull
    public Vector2l max(double x, double y) {
        return max(GenericMath.floor64(x), GenericMath.floor64(y));
    }

    public long distanceSquared(Vector2l v) {
        return distanceSquared(v.getX(), v.getY());
    }

    public long distanceSquared(double x, double y) {
        return distanceSquared(GenericMath.floor64(x), GenericMath.floor64(y));
    }

    public long distanceSquared(long x, long y) {
        long dx = getX() - x;
        long dy = getY() - y;
        return (dx * dx) + (dy * dy);
    }

    public double distance(Vector2l v) {
        return distance(v.getX(), v.getY());
    }

    public double distance(double x, double y) {
        return distance(GenericMath.floor64(x), GenericMath.floor64(y));
    }

    public double distance(long x, long y) {
        return Math.sqrt(distanceSquared(x, y));
    }

    @Nonnull
    public Vector2l north() {
        return north(1L);
    }

    @Nonnull
    public Vector2l south() {
        return south(1L);
    }

    @Nonnull
    public Vector2l east() {
        return east(1L);
    }

    @Nonnull
    public Vector2l west() {
        return west(1L);
    }

    @Override // org.cloudburstmc.math.vector.Vectorl
    public long lengthSquared() {
        return (getX() * getX()) + (getY() * getY());
    }

    @Override // org.cloudburstmc.math.vector.Vectorl
    public double length() {
        return Math.sqrt(lengthSquared());
    }

    @Override // org.cloudburstmc.math.vector.Vectorl
    public int getMinAxis() {
        return getX() < getY() ? 0 : 1;
    }

    @Override // org.cloudburstmc.math.vector.Vectorl
    public int getMaxAxis() {
        return getX() > getY() ? 0 : 1;
    }

    @Nonnull
    public Vector3l toVector3() {
        return toVector3(0L);
    }

    @Nonnull
    public Vector3l toVector3(double z) {
        return toVector3(GenericMath.floor64(z));
    }

    @Nonnull
    public Vector3l toVector3(long z) {
        return Vector3l.from(this, z);
    }

    @Nonnull
    public Vector4l toVector4() {
        return toVector4(0L, 0L);
    }

    @Nonnull
    public Vector4l toVector4(double z, double w) {
        return toVector4(GenericMath.floor64(z), GenericMath.floor64(w));
    }

    @Nonnull
    public Vector4l toVector4(long z, long w) {
        return Vector4l.from(this, z, w);
    }

    @Nonnull
    public VectorNl toVectorN() {
        return VectorNl.from(this);
    }

    @Override // org.cloudburstmc.math.vector.Vectorl
    @Nonnull
    public long[] toArray() {
        return new long[]{getX(), getY()};
    }

    @Override // org.cloudburstmc.math.vector.Vectorl
    @Nonnull
    public Vector2i toInt() {
        return Vector2i.from(getX(), getY());
    }

    @Override // org.cloudburstmc.math.vector.Vectorl
    @Nonnull
    public Vector2l toLong() {
        return from(getX(), getY());
    }

    @Override // org.cloudburstmc.math.vector.Vectorl
    @Nonnull
    public Vector2f toFloat() {
        return Vector2f.from((float) getX(), (float) getY());
    }

    @Override // org.cloudburstmc.math.vector.Vectorl
    @Nonnull
    public Vector2d toDouble() {
        return Vector2d.from((float) getX(), (float) getY());
    }

    @Override // java.lang.Comparable
    public int compareTo(Vector2l v) {
        return (int) (lengthSquared() - v.lengthSquared());
    }

    @Nonnull
    public Vector2l clone() {
        return from(this);
    }

    @Nonnull
    public String toString() {
        return "(" + getX() + ", " + getY() + ")";
    }

    @Nonnull
    public static Vector2l from(long n) {
        return Vectors.createVector2l(n, n);
    }

    @Nonnull
    public static Vector2l from(Vector2l v) {
        return from(v.getX(), v.getY());
    }

    @Nonnull
    public static Vector2l from(Vector3l v) {
        return from(v.getX(), v.getY());
    }

    @Nonnull
    public static Vector2l from(Vector4l v) {
        return from(v.getX(), v.getY());
    }

    @Nonnull
    public static Vector2l from(VectorNl v) {
        return from(v.get(0), v.get(1));
    }

    @Nonnull
    public static Vector2l from(double x, double y) {
        return from(GenericMath.floor64(x), GenericMath.floor64(y));
    }

    @Nonnull
    public static Vector2l from(long x, long y) {
        return Vectors.createVector2l(x, y);
    }
}
