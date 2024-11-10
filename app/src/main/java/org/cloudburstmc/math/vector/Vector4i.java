package org.cloudburstmc.math.vector;

import java.io.Serializable;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.cloudburstmc.math.GenericMath;

@ParametersAreNonnullByDefault
/* loaded from: classes5.dex */
public abstract class Vector4i implements Vectori, Comparable<Vector4i>, Serializable, Cloneable {
    public static final Vector4i ZERO = from(0, 0, 0, 0);
    public static final Vector4i UNIT_X = from(1, 0, 0, 0);
    public static final Vector4i UNIT_Y = from(0, 1, 0, 0);
    public static final Vector4i UNIT_Z = from(0, 0, 1, 0);
    public static final Vector4i UNIT_W = from(0, 0, 0, 1);
    public static final Vector4i ONE = from(1, 1, 1, 1);

    @Override // org.cloudburstmc.math.vector.Vectori
    @Nonnull
    public abstract Vector4i abs();

    @Nonnull
    public abstract Vector4i add(int i, int i2, int i3, int i4);

    @Nonnull
    public abstract Vector4i div(int i, int i2, int i3, int i4);

    public abstract int getW();

    public abstract int getX();

    public abstract int getY();

    public abstract int getZ();

    @Nonnull
    public abstract Vector4i max(int i, int i2, int i3, int i4);

    @Nonnull
    public abstract Vector4i min(int i, int i2, int i3, int i4);

    @Nonnull
    public abstract Vector4i mul(int i, int i2, int i3, int i4);

    @Override // org.cloudburstmc.math.vector.Vectori
    @Nonnull
    public abstract Vector4i negate();

    @Override // org.cloudburstmc.math.vector.Vectori
    @Nonnull
    public abstract Vector4i pow(int i);

    @Nonnull
    public abstract Vector4i project(int i, int i2, int i3, int i4);

    @Nonnull
    public abstract Vector4i sub(int i, int i2, int i3, int i4);

    @Nonnull
    public Vector4i add(Vector4i v) {
        return add(v.getX(), v.getY(), v.getZ(), v.getW());
    }

    @Nonnull
    public Vector4i add(double x, double y, double z, double w) {
        return add(GenericMath.floor(x), GenericMath.floor(y), GenericMath.floor(z), GenericMath.floor(w));
    }

    @Nonnull
    public Vector4i sub(Vector4i v) {
        return sub(v.getX(), v.getY(), v.getZ(), v.getW());
    }

    @Nonnull
    public Vector4i sub(double x, double y, double z, double w) {
        return sub(GenericMath.floor(x), GenericMath.floor(y), GenericMath.floor(z), GenericMath.floor(w));
    }

    @Nonnull
    public Vector4i mul(double a) {
        return mul(GenericMath.floor(a));
    }

    @Override // org.cloudburstmc.math.vector.Vectori
    @Nonnull
    public Vector4i mul(int a) {
        return mul(a, a, a, a);
    }

    @Nonnull
    public Vector4i mul(Vector4i v) {
        return mul(v.getX(), v.getY(), v.getZ(), v.getW());
    }

    @Nonnull
    public Vector4i mul(double x, double y, double z, double w) {
        return mul(GenericMath.floor(x), GenericMath.floor(y), GenericMath.floor(z), GenericMath.floor(w));
    }

    @Nonnull
    public Vector4i div(double a) {
        return div(GenericMath.floor(a));
    }

    @Override // org.cloudburstmc.math.vector.Vectori
    @Nonnull
    public Vector4i div(int a) {
        return div(a, a, a, a);
    }

    @Nonnull
    public Vector4i div(Vector4i v) {
        return div(v.getX(), v.getY(), v.getZ(), v.getW());
    }

    @Nonnull
    public Vector4i div(double x, double y, double z, double w) {
        return div(GenericMath.floor(x), GenericMath.floor(y), GenericMath.floor(z), GenericMath.floor(w));
    }

    public int dot(Vector4i v) {
        return dot(v.getX(), v.getY(), v.getZ(), v.getW());
    }

    public int dot(double x, double y, double z, double w) {
        return dot(GenericMath.floor(x), GenericMath.floor(y), GenericMath.floor(z), GenericMath.floor(w));
    }

    public int dot(int x, int y, int z, int w) {
        return (getX() * x) + (getY() * y) + (getZ() * z) + (getW() * w);
    }

    @Nonnull
    public Vector4i project(Vector4i v) {
        return project(v.getX(), v.getY(), v.getZ(), v.getW());
    }

    @Nonnull
    public Vector4i project(double x, double y, double z, double w) {
        return project(GenericMath.floor(x), GenericMath.floor(y), GenericMath.floor(z), GenericMath.floor(w));
    }

    @Nonnull
    public Vector4i pow(double pow) {
        return pow(GenericMath.floor(pow));
    }

    @Nonnull
    public Vector4i min(Vector4i v) {
        return min(v.getX(), v.getY(), v.getZ(), v.getW());
    }

    @Nonnull
    public Vector4i min(double x, double y, double z, double w) {
        return min(GenericMath.floor(x), GenericMath.floor(y), GenericMath.floor(z), GenericMath.floor(w));
    }

    @Nonnull
    public Vector4i max(Vector4i v) {
        return max(v.getX(), v.getY(), v.getZ(), v.getW());
    }

    @Nonnull
    public Vector4i max(double x, double y, double z, double w) {
        return max(GenericMath.floor(x), GenericMath.floor(y), GenericMath.floor(z), GenericMath.floor(w));
    }

    public int distanceSquared(Vector4i v) {
        return distanceSquared(v.getX(), v.getY(), v.getZ(), v.getW());
    }

    public int distanceSquared(double x, double y, double z, double w) {
        return distanceSquared(GenericMath.floor(x), GenericMath.floor(y), GenericMath.floor(z), GenericMath.floor(w));
    }

    public int distanceSquared(int x, int y, int z, int w) {
        int dx = getX() - x;
        int dy = getY() - y;
        int dz = getZ() - z;
        int dw = getW() - w;
        return (dx * dx) + (dy * dy) + (dz * dz) + (dw * dw);
    }

    public float distance(Vector4i v) {
        return distance(v.getX(), v.getY(), v.getZ(), v.getW());
    }

    public float distance(double x, double y, double z, double w) {
        return distance(GenericMath.floor(x), GenericMath.floor(y), GenericMath.floor(z), GenericMath.floor(w));
    }

    public float distance(int x, int y, int z, int w) {
        return (float) Math.sqrt(distanceSquared(x, y, z, w));
    }

    @Override // org.cloudburstmc.math.vector.Vectori
    public int lengthSquared() {
        return (getX() * getX()) + (getY() * getY()) + (getZ() * getZ()) + (getW() * getW());
    }

    @Override // org.cloudburstmc.math.vector.Vectori
    public float length() {
        return (float) Math.sqrt(lengthSquared());
    }

    @Override // org.cloudburstmc.math.vector.Vectori
    public int getMinAxis() {
        int value = getX();
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

    @Override // org.cloudburstmc.math.vector.Vectori
    public int getMaxAxis() {
        int value = getX();
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
    public Vector2i toVector2() {
        return Vector2i.from(this);
    }

    @Nonnull
    public Vector3i toVector3() {
        return Vector3i.from(this);
    }

    @Nonnull
    public VectorNi toVectorN() {
        return VectorNi.from(this);
    }

    @Override // org.cloudburstmc.math.vector.Vectori
    @Nonnull
    public int[] toArray() {
        return new int[]{getX(), getY(), getZ(), getW()};
    }

    @Override // org.cloudburstmc.math.vector.Vectori
    @Nonnull
    public Vector4i toInt() {
        return from(getX(), getY(), getZ(), getW());
    }

    @Override // org.cloudburstmc.math.vector.Vectori
    @Nonnull
    public Vector4l toLong() {
        return Vector4l.from(getX(), getY(), getZ(), getW());
    }

    @Override // org.cloudburstmc.math.vector.Vectori
    @Nonnull
    public Vector4f toFloat() {
        return Vector4f.from(getX(), getY(), getZ(), getW());
    }

    @Override // org.cloudburstmc.math.vector.Vectori
    @Nonnull
    public Vector4d toDouble() {
        return Vector4d.from(getX(), getY(), getZ(), getW());
    }

    @Override // java.lang.Comparable
    public int compareTo(Vector4i v) {
        return lengthSquared() - v.lengthSquared();
    }

    @Nonnull
    public Vector4i clone() {
        return from(this);
    }

    @Nonnull
    public String toString() {
        return "(" + getX() + ", " + getY() + ", " + getZ() + ", " + getW() + ")";
    }

    @Nonnull
    public static Vector4i from(int n) {
        return Vectors.createVector4i(n, n, n, n);
    }

    @Nonnull
    public static Vector4i from(Vector2i v) {
        return from(v, 0, 0);
    }

    @Nonnull
    public static Vector4i from(Vector2i v, double z, double w) {
        return from(v, GenericMath.floor(z), GenericMath.floor(w));
    }

    @Nonnull
    public static Vector4i from(Vector2i v, int z, int w) {
        return from(v.getX(), v.getY(), z, w);
    }

    @Nonnull
    public static Vector4i from(Vector3i v) {
        return from(v, 0);
    }

    @Nonnull
    public static Vector4i from(Vector3i v, int w) {
        return from(v.getX(), v.getY(), v.getZ(), w);
    }

    @Nonnull
    public static Vector4i from(Vector4i v) {
        return from(v.getX(), v.getY(), v.getZ(), v.getW());
    }

    @Nonnull
    public static Vector4i from(VectorNi v) {
        return from(v.get(0), v.get(1), v.size() > 2 ? v.get(2) : 0, v.size() > 3 ? v.get(3) : 0);
    }

    @Nonnull
    public static Vector4i from(double x, double y, double z, double w) {
        return from(GenericMath.floor(x), GenericMath.floor(y), GenericMath.floor(z), GenericMath.floor(w));
    }

    @Nonnull
    public static Vector4i from(int x, int y, int z, int w) {
        return Vectors.createVector4i(x, y, z, w);
    }
}
