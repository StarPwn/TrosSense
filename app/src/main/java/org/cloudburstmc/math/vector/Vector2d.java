package org.cloudburstmc.math.vector;

import java.io.Serializable;
import java.util.Random;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.cloudburstmc.math.GenericMath;
import org.cloudburstmc.math.TrigMath;

@ParametersAreNonnullByDefault
/* loaded from: classes5.dex */
public abstract class Vector2d implements Vectord, Comparable<Vector2d>, Serializable, Cloneable {
    public static final Vector2d ZERO = from(0.0f, 0.0f);
    public static final Vector2d UNIT_X = from(1.0f, 0.0f);
    public static final Vector2d UNIT_Y = from(0.0f, 1.0f);
    public static final Vector2d ONE = from(1.0f, 1.0f);

    @Override // org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public abstract Vector2d abs();

    @Nonnull
    public abstract Vector2d add(double d, double d2);

    @Override // org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public abstract Vector2d ceil();

    @Nonnull
    public abstract Vector2d div(double d, double d2);

    @Nonnull
    public abstract Vector2d east(double d);

    @Override // org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public abstract Vector2d floor();

    public abstract double getX();

    public abstract double getY();

    @Nonnull
    public abstract Vector2d max(double d, double d2);

    @Nonnull
    public abstract Vector2d min(double d, double d2);

    @Nonnull
    public abstract Vector2d mul(double d, double d2);

    @Override // org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public abstract Vector2d negate();

    @Override // org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public abstract Vector2d normalize();

    @Nonnull
    public abstract Vector2d north(double d);

    @Override // org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public abstract Vector2d pow(double d);

    @Nonnull
    public abstract Vector2d project(double d, double d2);

    @Override // org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public abstract Vector2d round();

    @Nonnull
    public abstract Vector2d south(double d);

    @Nonnull
    public abstract Vector2d sub(double d, double d2);

    @Nonnull
    public abstract Vector2d west(double d);

    public int getFloorX() {
        return GenericMath.floor(getX());
    }

    public int getFloorY() {
        return GenericMath.floor(getY());
    }

    @Nonnull
    public Vector2d add(Vector2d v) {
        return add(v.getX(), v.getY());
    }

    @Nonnull
    public Vector2d add(float x, float y) {
        return add(x, y);
    }

    @Nonnull
    public Vector2d sub(Vector2d v) {
        return sub(v.getX(), v.getY());
    }

    @Nonnull
    public Vector2d sub(float x, float y) {
        return sub(x, y);
    }

    @Nonnull
    public Vector2d mul(float a) {
        return mul(a);
    }

    @Override // org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public Vector2d mul(double a) {
        return mul(a, a);
    }

    @Nonnull
    public Vector2d mul(Vector2d v) {
        return mul(v.getX(), v.getY());
    }

    @Nonnull
    public Vector2d mul(float x, float y) {
        return mul(x, y);
    }

    @Nonnull
    public Vector2d div(float a) {
        return div(a);
    }

    @Override // org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public Vector2d div(double a) {
        return div(a, a);
    }

    @Nonnull
    public Vector2d div(Vector2d v) {
        return div(v.getX(), v.getY());
    }

    @Nonnull
    public Vector2d div(float x, float y) {
        return div(x, y);
    }

    public double dot(Vector2d v) {
        return dot(v.getX(), v.getY());
    }

    public double dot(float x, float y) {
        return dot(x, y);
    }

    public double dot(double x, double y) {
        return (getX() * x) + (getY() * y);
    }

    @Nonnull
    public Vector2d project(Vector2d v) {
        return project(v.getX(), v.getY());
    }

    @Nonnull
    public Vector2d project(float x, float y) {
        return project(x, y);
    }

    @Nonnull
    public Vector2d pow(float pow) {
        return pow(pow);
    }

    @Nonnull
    public Vector2d min(Vector2d v) {
        return min(v.getX(), v.getY());
    }

    @Nonnull
    public Vector2d min(float x, float y) {
        return min(x, y);
    }

    @Nonnull
    public Vector2d max(Vector2d v) {
        return max(v.getX(), v.getY());
    }

    @Nonnull
    public Vector2d max(float x, float y) {
        return max(x, y);
    }

    public double distanceSquared(Vector2d v) {
        return distanceSquared(v.getX(), v.getY());
    }

    public double distanceSquared(float x, float y) {
        return distanceSquared(x, y);
    }

    public double distanceSquared(double x, double y) {
        double dx = getX() - x;
        double dy = getY() - y;
        return (dx * dx) + (dy * dy);
    }

    public double distance(Vector2d v) {
        return distance(v.getX(), v.getY());
    }

    public double distance(float x, float y) {
        return distance(x, y);
    }

    public double distance(double x, double y) {
        return Math.sqrt(distanceSquared(x, y));
    }

    @Nonnull
    public Vector2d north() {
        return north(1.0d);
    }

    @Nonnull
    public Vector2d south() {
        return south(1.0d);
    }

    @Nonnull
    public Vector2d east() {
        return east(1.0d);
    }

    @Nonnull
    public Vector2d west() {
        return west(1.0d);
    }

    @Override // org.cloudburstmc.math.vector.Vectord
    public double lengthSquared() {
        return (getX() * getX()) + (getY() * getY());
    }

    @Override // org.cloudburstmc.math.vector.Vectord
    public double length() {
        return Math.sqrt(lengthSquared());
    }

    @Override // org.cloudburstmc.math.vector.Vectord
    public int getMinAxis() {
        return getX() < getY() ? 0 : 1;
    }

    @Override // org.cloudburstmc.math.vector.Vectord
    public int getMaxAxis() {
        return getX() > getY() ? 0 : 1;
    }

    @Nonnull
    public Vector3d toVector3() {
        return toVector3(0.0f);
    }

    @Nonnull
    public Vector3d toVector3(float z) {
        return toVector3(z);
    }

    @Nonnull
    public Vector3d toVector3(double z) {
        return Vector3d.from(this, z);
    }

    @Nonnull
    public Vector4d toVector4() {
        return toVector4(0.0f, 0.0f);
    }

    @Nonnull
    public Vector4d toVector4(float z, float w) {
        return toVector4(z, w);
    }

    @Nonnull
    public Vector4d toVector4(double z, double w) {
        return Vector4d.from(this, z, w);
    }

    @Nonnull
    public VectorNd toVectorN() {
        return VectorNd.from(this);
    }

    @Override // org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public double[] toArray() {
        return new double[]{getX(), getY()};
    }

    @Override // org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public Vector2i toInt() {
        return Vector2i.from(getX(), getY());
    }

    @Override // org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public Vector2l toLong() {
        return Vector2l.from(getX(), getY());
    }

    @Override // org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public Vector2f toFloat() {
        return Vector2f.from(getX(), getY());
    }

    @Override // org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public Vector2d toDouble() {
        return from(getX(), getY());
    }

    @Override // java.lang.Comparable
    public int compareTo(Vector2d v) {
        return (int) Math.signum(lengthSquared() - v.lengthSquared());
    }

    @Nonnull
    public Vector2d clone() {
        return from(this);
    }

    @Nonnull
    public String toString() {
        return "(" + getX() + ", " + getY() + ")";
    }

    @Nonnull
    public static Vector2d from(double n) {
        return Vectors.createVector2d(n, n);
    }

    @Nonnull
    public static Vector2d from(Vector2d v) {
        return from(v.getX(), v.getY());
    }

    @Nonnull
    public static Vector2d from(Vector3d v) {
        return from(v.getX(), v.getY());
    }

    @Nonnull
    public static Vector2d from(Vector4d v) {
        return from(v.getX(), v.getY());
    }

    @Nonnull
    public static Vector2d from(VectorNd v) {
        return from(v.get(0), v.get(1));
    }

    @Nonnull
    public static Vector2d from(float x, float y) {
        return from(x, y);
    }

    @Nonnull
    public static Vector2d from(double x, double y) {
        return Vectors.createVector2d(x, y);
    }

    @Nonnull
    public static Vector2d createRandomDirection(Random random) {
        return createDirectionRad(random.nextDouble() * 6.283185307179586d);
    }

    @Nonnull
    public static Vector2d createDirectionDeg(float angle) {
        return createDirectionDeg(angle);
    }

    @Nonnull
    public static Vector2d createDirectionDeg(double angle) {
        return createDirectionRad(Math.toRadians(angle));
    }

    @Nonnull
    public static Vector2d createDirectionRad(float angle) {
        return createDirectionRad(angle);
    }

    @Nonnull
    public static Vector2d createDirectionRad(double angle) {
        return from(TrigMath.cos(angle), TrigMath.sin(angle));
    }
}
