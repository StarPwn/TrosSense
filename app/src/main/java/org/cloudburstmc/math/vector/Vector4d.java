package org.cloudburstmc.math.vector;

import java.io.Serializable;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.cloudburstmc.math.GenericMath;

@ParametersAreNonnullByDefault
/* loaded from: classes5.dex */
public abstract class Vector4d implements Vectord, Comparable<Vector4d>, Serializable, Cloneable {
    public static final Vector4d ZERO = from(0.0f, 0.0f, 0.0f, 0.0f);
    public static final Vector4d UNIT_X = from(1.0f, 0.0f, 0.0f, 0.0f);
    public static final Vector4d UNIT_Y = from(0.0f, 1.0f, 0.0f, 0.0f);
    public static final Vector4d UNIT_Z = from(0.0f, 0.0f, 1.0f, 0.0f);
    public static final Vector4d UNIT_W = from(0.0f, 0.0f, 0.0f, 1.0f);
    public static final Vector4d ONE = from(1.0f, 1.0f, 1.0f, 1.0f);

    @Override // org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public abstract Vector4d abs();

    @Nonnull
    public abstract Vector4d add(double d, double d2, double d3, double d4);

    @Override // org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public abstract Vector4d ceil();

    @Nonnull
    public abstract Vector4d div(double d, double d2, double d3, double d4);

    @Override // org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public abstract Vector4d floor();

    public abstract double getW();

    public abstract double getX();

    public abstract double getY();

    public abstract double getZ();

    @Nonnull
    public abstract Vector4d max(double d, double d2, double d3, double d4);

    @Nonnull
    public abstract Vector4d min(double d, double d2, double d3, double d4);

    @Nonnull
    public abstract Vector4d mul(double d, double d2, double d3, double d4);

    @Override // org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public abstract Vector4d negate();

    @Override // org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public abstract Vector4d normalize();

    @Override // org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public abstract Vector4d pow(double d);

    @Nonnull
    public abstract Vector4d project(double d, double d2, double d3, double d4);

    @Override // org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public abstract Vector4d round();

    @Nonnull
    public abstract Vector4d sub(double d, double d2, double d3, double d4);

    public int getFloorX() {
        return GenericMath.floor(getX());
    }

    public int getFloorY() {
        return GenericMath.floor(getY());
    }

    public int getFloorZ() {
        return GenericMath.floor(getZ());
    }

    public int getFloorW() {
        return GenericMath.floor(getW());
    }

    @Nonnull
    public Vector4d add(Vector4d v) {
        return add(v.getX(), v.getY(), v.getZ(), v.getW());
    }

    @Nonnull
    public Vector4d add(float x, float y, float z, float w) {
        return add(x, y, z, w);
    }

    @Nonnull
    public Vector4d sub(Vector4d v) {
        return sub(v.getX(), v.getY(), v.getZ(), v.getW());
    }

    @Nonnull
    public Vector4d sub(float x, float y, float z, float w) {
        return sub(x, y, z, w);
    }

    @Nonnull
    public Vector4d mul(float a) {
        return mul(a);
    }

    @Override // org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public Vector4d mul(double a) {
        return mul(a, a, a, a);
    }

    @Nonnull
    public Vector4d mul(Vector4d v) {
        return mul(v.getX(), v.getY(), v.getZ(), v.getW());
    }

    @Nonnull
    public Vector4d mul(float x, float y, float z, float w) {
        return mul(x, y, z, w);
    }

    @Nonnull
    public Vector4d div(float a) {
        return div(a);
    }

    @Override // org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public Vector4d div(double a) {
        return div(a, a, a, a);
    }

    @Nonnull
    public Vector4d div(Vector4d v) {
        return div(v.getX(), v.getY(), v.getZ(), v.getW());
    }

    @Nonnull
    public Vector4d div(float x, float y, float z, float w) {
        return div(x, y, z, w);
    }

    public double dot(Vector4d v) {
        return dot(v.getX(), v.getY(), v.getZ(), v.getW());
    }

    public double dot(float x, float y, float z, float w) {
        return dot(x, y, z, w);
    }

    public double dot(double x, double y, double z, double w) {
        return (getX() * x) + (getY() * y) + (getZ() * z) + (getW() * w);
    }

    @Nonnull
    public Vector4d project(Vector4d v) {
        return project(v.getX(), v.getY(), v.getZ(), v.getW());
    }

    @Nonnull
    public Vector4d project(float x, float y, float z, float w) {
        return project(x, y, z, w);
    }

    @Nonnull
    public Vector4d pow(float pow) {
        return pow(pow);
    }

    @Nonnull
    public Vector4d min(Vector4d v) {
        return min(v.getX(), v.getY(), v.getZ(), v.getW());
    }

    @Nonnull
    public Vector4d min(float x, float y, float z, float w) {
        return min(x, y, z, w);
    }

    @Nonnull
    public Vector4d max(Vector4d v) {
        return max(v.getX(), v.getY(), v.getZ(), v.getW());
    }

    @Nonnull
    public Vector4d max(float x, float y, float z, float w) {
        return max(x, y, z, w);
    }

    public double distanceSquared(Vector4d v) {
        return distanceSquared(v.getX(), v.getY(), v.getZ(), v.getW());
    }

    public double distanceSquared(float x, float y, float z, float w) {
        return distanceSquared(x, y, z, w);
    }

    public double distanceSquared(double x, double y, double z, double w) {
        double dx = getX() - x;
        double dy = getY() - y;
        double dz = getZ() - z;
        double dw = getW() - w;
        return (dx * dx) + (dy * dy) + (dz * dz) + (dw * dw);
    }

    public double distance(Vector4d v) {
        return distance(v.getX(), v.getY(), v.getZ(), v.getW());
    }

    public double distance(float x, float y, float z, float w) {
        return distance(x, y, z, w);
    }

    public double distance(double x, double y, double z, double w) {
        return Math.sqrt(distanceSquared(x, y, z, w));
    }

    @Override // org.cloudburstmc.math.vector.Vectord
    public double lengthSquared() {
        return (getX() * getX()) + (getY() * getY()) + (getZ() * getZ()) + (getW() * getW());
    }

    @Override // org.cloudburstmc.math.vector.Vectord
    public double length() {
        return Math.sqrt(lengthSquared());
    }

    @Override // org.cloudburstmc.math.vector.Vectord
    public int getMinAxis() {
        double value = getX();
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

    @Override // org.cloudburstmc.math.vector.Vectord
    public int getMaxAxis() {
        double value = getX();
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
    public Vector2d toVector2() {
        return Vector2d.from(this);
    }

    @Nonnull
    public Vector3d toVector3() {
        return Vector3d.from(this);
    }

    @Nonnull
    public VectorNd toVectorN() {
        return VectorNd.from(this);
    }

    @Override // org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public double[] toArray() {
        return new double[]{getX(), getY(), getZ(), getW()};
    }

    @Override // org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public Vector4i toInt() {
        return Vector4i.from(getX(), getY(), getZ(), getW());
    }

    @Override // org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public Vector4l toLong() {
        return Vector4l.from(getX(), getY(), getZ(), getW());
    }

    @Override // org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public Vector4f toFloat() {
        return Vector4f.from(getX(), getY(), getZ(), getW());
    }

    @Override // org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public Vector4d toDouble() {
        return from(getX(), getY(), getZ(), getW());
    }

    @Override // java.lang.Comparable
    public int compareTo(Vector4d v) {
        return (int) Math.signum(lengthSquared() - v.lengthSquared());
    }

    @Nonnull
    public Vector4d clone() {
        return from(this);
    }

    @Nonnull
    public String toString() {
        return "(" + getX() + ", " + getY() + ", " + getZ() + ", " + getW() + ")";
    }

    @Nonnull
    public static Vector4d from(double n) {
        return from(n, n, n, n);
    }

    @Nonnull
    public static Vector4d from(Vector2d v) {
        return from(v, 0.0f, 0.0f);
    }

    @Nonnull
    public static Vector4d from(Vector2d v, float z, float w) {
        return from(v, z, w);
    }

    @Nonnull
    public static Vector4d from(Vector2d v, double z, double w) {
        return from(v.getX(), v.getY(), z, w);
    }

    @Nonnull
    public static Vector4d from(Vector3d v) {
        return from(v, 0.0f);
    }

    @Nonnull
    public static Vector4d from(Vector3d v, float w) {
        return from(v, w);
    }

    @Nonnull
    public static Vector4d from(Vector3d v, double w) {
        return from(v.getX(), v.getY(), v.getZ(), w);
    }

    @Nonnull
    public static Vector4d from(Vector4d v) {
        return from(v.getX(), v.getY(), v.getZ(), v.getW());
    }

    @Nonnull
    public static Vector4d from(VectorNd v) {
        return from(v.get(0), v.get(1), v.size() > 2 ? v.get(2) : 0.0d, v.size() > 3 ? v.get(3) : 0.0d);
    }

    @Nonnull
    public static Vector4d from(float x, float y, float z, float w) {
        return from(x, y, z, w);
    }

    @Nonnull
    public static Vector4d from(double x, double y, double z, double w) {
        return Vectors.createVector4d(x, y, z, w);
    }
}
