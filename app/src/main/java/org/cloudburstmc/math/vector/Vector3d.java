package org.cloudburstmc.math.vector;

import java.io.Serializable;
import java.util.Random;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.cloudburstmc.math.GenericMath;
import org.cloudburstmc.math.TrigMath;

@ParametersAreNonnullByDefault
/* loaded from: classes5.dex */
public abstract class Vector3d implements Vectord, Comparable<Vector3d>, Serializable, Cloneable {
    public static final Vector3d ZERO = from(0.0f, 0.0f, 0.0f);
    public static final Vector3d UNIT_X = from(1.0f, 0.0f, 0.0f);
    public static final Vector3d UNIT_Y = from(0.0f, 1.0f, 0.0f);
    public static final Vector3d UNIT_Z = from(0.0f, 0.0f, 1.0f);
    public static final Vector3d ONE = from(1.0f, 1.0f, 1.0f);
    public static final Vector3d RIGHT = UNIT_X;
    public static final Vector3d UP = UNIT_Y;
    public static final Vector3d FORWARD = UNIT_Z;

    @Override // org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public abstract Vector3d abs();

    @Nonnull
    public abstract Vector3d add(double d, double d2, double d3);

    @Override // org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public abstract Vector3d ceil();

    @Nonnull
    public abstract Vector3d cross(double d, double d2, double d3);

    @Nonnull
    public abstract Vector3d div(double d, double d2, double d3);

    @Nonnull
    public abstract Vector3d down(double d);

    @Nonnull
    public abstract Vector3d east(double d);

    @Override // org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public abstract Vector3d floor();

    public abstract double getX();

    public abstract double getY();

    public abstract double getZ();

    @Nonnull
    public abstract Vector3d max(double d, double d2, double d3);

    @Nonnull
    public abstract Vector3d min(double d, double d2, double d3);

    @Nonnull
    public abstract Vector3d mul(double d, double d2, double d3);

    @Override // org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public abstract Vector3d negate();

    @Override // org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public abstract Vector3d normalize();

    @Nonnull
    public abstract Vector3d north(double d);

    @Override // org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public abstract Vector3d pow(double d);

    @Nonnull
    public abstract Vector3d project(double d, double d2, double d3);

    @Override // org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public abstract Vector3d round();

    @Nonnull
    public abstract Vector3d south(double d);

    @Nonnull
    public abstract Vector3d sub(double d, double d2, double d3);

    @Nonnull
    public abstract Vector3d up(double d);

    @Nonnull
    public abstract Vector3d west(double d);

    public int getFloorX() {
        return GenericMath.floor(getX());
    }

    public int getFloorY() {
        return GenericMath.floor(getY());
    }

    public int getFloorZ() {
        return GenericMath.floor(getZ());
    }

    @Nonnull
    public Vector3d add(Vector3d v) {
        return add(v.getX(), v.getY(), v.getZ());
    }

    @Nonnull
    public Vector3d add(float x, float y, float z) {
        return add(x, y, z);
    }

    @Nonnull
    public Vector3d sub(Vector3d v) {
        return sub(v.getX(), v.getY(), v.getZ());
    }

    @Nonnull
    public Vector3d sub(float x, float y, float z) {
        return sub(x, y, z);
    }

    @Nonnull
    public Vector3d mul(float a) {
        return mul(a);
    }

    @Override // org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public Vector3d mul(double a) {
        return mul(a, a, a);
    }

    @Nonnull
    public Vector3d mul(Vector3d v) {
        return mul(v.getX(), v.getY(), v.getZ());
    }

    @Nonnull
    public Vector3d mul(float x, float y, float z) {
        return mul(x, y, z);
    }

    @Nonnull
    public Vector3d div(float a) {
        return div(a);
    }

    @Override // org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public Vector3d div(double a) {
        return div(a, a, a);
    }

    @Nonnull
    public Vector3d div(Vector3d v) {
        return div(v.getX(), v.getY(), v.getZ());
    }

    @Nonnull
    public Vector3d div(float x, float y, float z) {
        return div(x, y, z);
    }

    public double dot(Vector3d v) {
        return dot(v.getX(), v.getY(), v.getZ());
    }

    public double dot(float x, float y, float z) {
        return dot(x, y, z);
    }

    public double dot(double x, double y, double z) {
        return (getX() * x) + (getY() * y) + (getZ() * z);
    }

    @Nonnull
    public Vector3d project(Vector3d v) {
        return project(v.getX(), v.getY(), v.getZ());
    }

    @Nonnull
    public Vector3d project(float x, float y, float z) {
        return project(x, y, z);
    }

    @Nonnull
    public Vector3d cross(Vector3d v) {
        return cross(v.getX(), v.getY(), v.getZ());
    }

    @Nonnull
    public Vector3d cross(float x, float y, float z) {
        return cross(x, y, z);
    }

    @Nonnull
    public Vector3d pow(float pow) {
        return pow(pow);
    }

    @Nonnull
    public Vector3d min(Vector3d v) {
        return min(v.getX(), v.getY(), v.getZ());
    }

    @Nonnull
    public Vector3d min(float x, float y, float z) {
        return min(x, y, z);
    }

    @Nonnull
    public Vector3d max(Vector3d v) {
        return max(v.getX(), v.getY(), v.getZ());
    }

    @Nonnull
    public Vector3d max(float x, float y, float z) {
        return max(x, y, z);
    }

    public double distanceSquared(Vector3d v) {
        return distanceSquared(v.getX(), v.getY(), v.getZ());
    }

    public double distanceSquared(float x, float y, float z) {
        return distanceSquared(x, y, z);
    }

    public double distanceSquared(double x, double y, double z) {
        double dx = getX() - x;
        double dy = getY() - y;
        double dz = getZ() - z;
        return (dx * dx) + (dy * dy) + (dz * dz);
    }

    public double distance(Vector3d v) {
        return distance(v.getX(), v.getY(), v.getZ());
    }

    public double distance(float x, float y, float z) {
        return distance(x, y, z);
    }

    public double distance(double x, double y, double z) {
        return Math.sqrt(distanceSquared(x, y, z));
    }

    @Nonnull
    public Vector3d up() {
        return up(1.0d);
    }

    @Nonnull
    public Vector3d down() {
        return down(1.0d);
    }

    @Nonnull
    public Vector3d north() {
        return north(1.0d);
    }

    @Nonnull
    public Vector3d south() {
        return south(1.0d);
    }

    @Nonnull
    public Vector3d east() {
        return east(1.0d);
    }

    @Nonnull
    public Vector3d west() {
        return west(1.0d);
    }

    @Override // org.cloudburstmc.math.vector.Vectord
    public double lengthSquared() {
        return (getX() * getX()) + (getY() * getY()) + (getZ() * getZ());
    }

    @Override // org.cloudburstmc.math.vector.Vectord
    public double length() {
        return Math.sqrt(lengthSquared());
    }

    @Override // org.cloudburstmc.math.vector.Vectord
    public int getMinAxis() {
        return getX() < getY() ? getX() < getZ() ? 0 : 2 : getY() < getZ() ? 1 : 2;
    }

    @Override // org.cloudburstmc.math.vector.Vectord
    public int getMaxAxis() {
        return getX() < getY() ? getY() < getZ() ? 2 : 1 : getX() < getZ() ? 2 : 0;
    }

    @Nonnull
    public Vector2d toVector2() {
        return Vector2d.from(this);
    }

    @Nonnull
    public Vector2d toVector2(boolean useZ) {
        return Vector2d.from(getX(), useZ ? getZ() : getY());
    }

    @Nonnull
    public Vector4d toVector4() {
        return toVector4(0.0f);
    }

    @Nonnull
    public Vector4d toVector4(float w) {
        return toVector4(w);
    }

    @Nonnull
    public Vector4d toVector4(double w) {
        return Vector4d.from(this, w);
    }

    @Nonnull
    public VectorNd toVectorN() {
        return VectorNd.from(this);
    }

    @Override // org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public double[] toArray() {
        return new double[]{getX(), getY(), getZ()};
    }

    @Override // org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public Vector3i toInt() {
        return Vector3i.from(getX(), getY(), getZ());
    }

    @Override // org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public Vector3l toLong() {
        return Vector3l.from(getX(), getY(), getZ());
    }

    @Override // org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public Vector3f toFloat() {
        return Vector3f.from(getX(), getY(), getZ());
    }

    @Override // org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public Vector3d toDouble() {
        return from(getX(), getY(), getZ());
    }

    @Override // java.lang.Comparable
    public int compareTo(Vector3d v) {
        return (int) Math.signum(lengthSquared() - v.lengthSquared());
    }

    @Nonnull
    public Vector3d clone() {
        return from(this);
    }

    @Nonnull
    public String toString() {
        return "(" + getX() + ", " + getY() + ", " + getZ() + ")";
    }

    @Nonnull
    public static Vector3d from(double n) {
        return Vectors.createVector3d(n, n, n);
    }

    @Nonnull
    public static Vector3d from(Vector2d v) {
        return from(v, 0.0f);
    }

    @Nonnull
    public static Vector3d from(Vector2d v, float z) {
        return from(v, z);
    }

    @Nonnull
    public static Vector3d from(Vector2d v, double z) {
        return from(v.getX(), v.getY(), z);
    }

    @Nonnull
    public static Vector3d from(Vector3d v) {
        return from(v.getX(), v.getY(), v.getZ());
    }

    @Nonnull
    public static Vector3d from(Vector4d v) {
        return from(v.getX(), v.getY(), v.getZ());
    }

    @Nonnull
    public static Vector3d from(VectorNd v) {
        return from(v.get(0), v.get(1), v.size() > 2 ? v.get(2) : 0.0d);
    }

    @Nonnull
    public static Vector3d from(float x, float y, float z) {
        return from(x, y, z);
    }

    @Nonnull
    public static Vector3d from(double x, double y, double z) {
        return Vectors.createVector3d(x, y, z);
    }

    @Nonnull
    public static Vector3d createRandomDirection(Random random) {
        return createDirectionRad(random.nextDouble() * 6.283185307179586d, random.nextDouble() * 6.283185307179586d);
    }

    @Nonnull
    public static Vector3d createDirectionDeg(float theta, float phi) {
        return createDirectionDeg(theta, phi);
    }

    @Nonnull
    public static Vector3d createDirectionDeg(double theta, double phi) {
        return createDirectionRad(Math.toRadians(theta), Math.toRadians(phi));
    }

    @Nonnull
    public static Vector3d createDirectionRad(float theta, float phi) {
        return createDirectionRad(theta, phi);
    }

    @Nonnull
    public static Vector3d createDirectionRad(double theta, double phi) {
        double f = TrigMath.sin(phi);
        return from(f * TrigMath.cos(theta), f * TrigMath.sin(theta), TrigMath.cos(phi));
    }
}
