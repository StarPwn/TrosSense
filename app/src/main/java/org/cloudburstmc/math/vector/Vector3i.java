package org.cloudburstmc.math.vector;

import java.io.Serializable;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.cloudburstmc.math.GenericMath;

@ParametersAreNonnullByDefault
/* loaded from: classes5.dex */
public abstract class Vector3i implements Vectori, Comparable<Vector3i>, Serializable, Cloneable {
    public static final Vector3i ZERO = from(0, 0, 0);
    public static final Vector3i UNIT_X = from(1, 0, 0);
    public static final Vector3i UNIT_Y = from(0, 1, 0);
    public static final Vector3i UNIT_Z = from(0, 0, 1);
    public static final Vector3i ONE = from(1, 1, 1);
    public static final Vector3i RIGHT = UNIT_X;
    public static final Vector3i UP = UNIT_Y;
    public static final Vector3i FORWARD = UNIT_Z;

    @Override // org.cloudburstmc.math.vector.Vectori
    @Nonnull
    public abstract Vector3i abs();

    @Nonnull
    public abstract Vector3i add(int i, int i2, int i3);

    @Nonnull
    public abstract Vector3i cross(int i, int i2, int i3);

    @Nonnull
    public abstract Vector3i div(int i, int i2, int i3);

    @Nonnull
    public abstract Vector3i down(int i);

    @Nonnull
    public abstract Vector3i east(int i);

    public abstract int getX();

    public abstract int getY();

    public abstract int getZ();

    @Nonnull
    public abstract Vector3i max(int i, int i2, int i3);

    @Nonnull
    public abstract Vector3i min(int i, int i2, int i3);

    @Nonnull
    public abstract Vector3i mul(int i, int i2, int i3);

    @Override // org.cloudburstmc.math.vector.Vectori
    @Nonnull
    public abstract Vector3i negate();

    @Nonnull
    public abstract Vector3i north(int i);

    @Override // org.cloudburstmc.math.vector.Vectori
    @Nonnull
    public abstract Vector3i pow(int i);

    @Nonnull
    public abstract Vector3i project(int i, int i2, int i3);

    @Nonnull
    public abstract Vector3i south(int i);

    @Nonnull
    public abstract Vector3i sub(int i, int i2, int i3);

    @Nonnull
    public abstract Vector3i up(int i);

    @Nonnull
    public abstract Vector3i west(int i);

    @Nonnull
    public Vector3i add(Vector3i v) {
        return add(v.getX(), v.getY(), v.getZ());
    }

    @Nonnull
    public Vector3i add(double x, double y, double z) {
        return add(GenericMath.floor(x), GenericMath.floor(y), GenericMath.floor(z));
    }

    @Nonnull
    public Vector3i sub(Vector3i v) {
        return sub(v.getX(), v.getY(), v.getZ());
    }

    @Nonnull
    public Vector3i sub(double x, double y, double z) {
        return sub(GenericMath.floor(x), GenericMath.floor(y), GenericMath.floor(z));
    }

    @Nonnull
    public Vector3i mul(double a) {
        return mul(GenericMath.floor(a));
    }

    @Override // org.cloudburstmc.math.vector.Vectori
    @Nonnull
    public Vector3i mul(int a) {
        return mul(a, a, a);
    }

    @Nonnull
    public Vector3i mul(Vector3i v) {
        return mul(v.getX(), v.getY(), v.getZ());
    }

    @Nonnull
    public Vector3i mul(double x, double y, double z) {
        return mul(GenericMath.floor(x), GenericMath.floor(y), GenericMath.floor(z));
    }

    @Nonnull
    public Vector3i div(double a) {
        return div(GenericMath.floor(a));
    }

    @Override // org.cloudburstmc.math.vector.Vectori
    @Nonnull
    public Vector3i div(int a) {
        return div(a, a, a);
    }

    @Nonnull
    public Vector3i div(Vector3i v) {
        return div(v.getX(), v.getY(), v.getZ());
    }

    @Nonnull
    public Vector3i div(double x, double y, double z) {
        return div(GenericMath.floor(x), GenericMath.floor(y), GenericMath.floor(z));
    }

    public int dot(Vector3i v) {
        return dot(v.getX(), v.getY(), v.getZ());
    }

    public int dot(double x, double y, double z) {
        return dot(GenericMath.floor(x), GenericMath.floor(y), GenericMath.floor(z));
    }

    public int dot(int x, int y, int z) {
        return (getX() * x) + (getY() * y) + (getZ() * z);
    }

    @Nonnull
    public Vector3i project(Vector3i v) {
        return project(v.getX(), v.getY(), v.getZ());
    }

    @Nonnull
    public Vector3i project(double x, double y, double z) {
        return project(GenericMath.floor(x), GenericMath.floor(y), GenericMath.floor(z));
    }

    @Nonnull
    public Vector3i cross(Vector3i v) {
        return cross(v.getX(), v.getY(), v.getZ());
    }

    @Nonnull
    public Vector3i cross(double x, double y, double z) {
        return cross(GenericMath.floor(x), GenericMath.floor(y), GenericMath.floor(z));
    }

    @Nonnull
    public Vector3i pow(double pow) {
        return pow(GenericMath.floor(pow));
    }

    @Nonnull
    public Vector3i min(Vector3i v) {
        return min(v.getX(), v.getY(), v.getZ());
    }

    @Nonnull
    public Vector3i min(double x, double y, double z) {
        return min(GenericMath.floor(x), GenericMath.floor(y), GenericMath.floor(z));
    }

    @Nonnull
    public Vector3i max(Vector3i v) {
        return max(v.getX(), v.getY(), v.getZ());
    }

    @Nonnull
    public Vector3i max(double x, double y, double z) {
        return max(GenericMath.floor(x), GenericMath.floor(y), GenericMath.floor(z));
    }

    public int distanceSquared(Vector3i v) {
        return distanceSquared(v.getX(), v.getY(), v.getZ());
    }

    public int distanceSquared(double x, double y, double z) {
        return distanceSquared(GenericMath.floor(x), GenericMath.floor(y), GenericMath.floor(z));
    }

    public int distanceSquared(int x, int y, int z) {
        int dx = getX() - x;
        int dy = getY() - y;
        int dz = getZ() - z;
        return (dx * dx) + (dy * dy) + (dz * dz);
    }

    public float distance(Vector3i v) {
        return distance(v.getX(), v.getY(), v.getZ());
    }

    public float distance(double x, double y, double z) {
        return distance(GenericMath.floor(x), GenericMath.floor(y), GenericMath.floor(z));
    }

    public float distance(int x, int y, int z) {
        return (float) Math.sqrt(distanceSquared(x, y, z));
    }

    @Nonnull
    public Vector3i up() {
        return up(1);
    }

    @Nonnull
    public Vector3i down() {
        return down(1);
    }

    @Nonnull
    public Vector3i north() {
        return north(1);
    }

    @Nonnull
    public Vector3i south() {
        return south(1);
    }

    @Nonnull
    public Vector3i east() {
        return east(1);
    }

    @Nonnull
    public Vector3i west() {
        return west(1);
    }

    @Override // org.cloudburstmc.math.vector.Vectori
    public int lengthSquared() {
        return (getX() * getX()) + (getY() * getY()) + (getZ() * getZ());
    }

    @Override // org.cloudburstmc.math.vector.Vectori
    public float length() {
        return (float) Math.sqrt(lengthSquared());
    }

    @Override // org.cloudburstmc.math.vector.Vectori
    public int getMinAxis() {
        return getX() < getY() ? getX() < getZ() ? 0 : 2 : getY() < getZ() ? 1 : 2;
    }

    @Override // org.cloudburstmc.math.vector.Vectori
    public int getMaxAxis() {
        return getX() < getY() ? getY() < getZ() ? 2 : 1 : getX() < getZ() ? 2 : 0;
    }

    @Nonnull
    public Vector2i toVector2() {
        return Vector2i.from(this);
    }

    @Nonnull
    public Vector2i toVector2(boolean useZ) {
        return Vector2i.from(getX(), useZ ? getZ() : getY());
    }

    @Nonnull
    public Vector4i toVector4() {
        return toVector4(0);
    }

    @Nonnull
    public Vector4i toVector4(double w) {
        return toVector4(GenericMath.floor(w));
    }

    @Nonnull
    public Vector4i toVector4(int w) {
        return Vector4i.from(this, w);
    }

    @Nonnull
    public VectorNi toVectorN() {
        return VectorNi.from(this);
    }

    @Override // org.cloudburstmc.math.vector.Vectori
    @Nonnull
    public int[] toArray() {
        return new int[]{getX(), getY(), getZ()};
    }

    @Override // org.cloudburstmc.math.vector.Vectori
    @Nonnull
    public Vector3i toInt() {
        return from(getX(), getY(), getZ());
    }

    @Override // org.cloudburstmc.math.vector.Vectori
    @Nonnull
    public Vector3l toLong() {
        return Vector3l.from(getX(), getY(), getZ());
    }

    @Override // org.cloudburstmc.math.vector.Vectori
    @Nonnull
    public Vector3f toFloat() {
        return Vector3f.from(getX(), getY(), getZ());
    }

    @Override // org.cloudburstmc.math.vector.Vectori
    @Nonnull
    public Vector3d toDouble() {
        return Vector3d.from(getX(), getY(), getZ());
    }

    @Override // java.lang.Comparable
    public int compareTo(Vector3i v) {
        return lengthSquared() - v.lengthSquared();
    }

    @Nonnull
    public Vector3i clone() {
        return from(this);
    }

    @Nonnull
    public String toString() {
        return "(" + getX() + ", " + getY() + ", " + getZ() + ")";
    }

    @Nonnull
    public static Vector3i from(int n) {
        return Vectors.createVector3i(n, n, n);
    }

    @Nonnull
    public static Vector3i from(Vector2i v) {
        return from(v, 0);
    }

    @Nonnull
    public static Vector3i from(Vector2i v, double z) {
        return from(v, GenericMath.floor(z));
    }

    @Nonnull
    public static Vector3i from(Vector2i v, int z) {
        return from(v.getX(), v.getY(), z);
    }

    @Nonnull
    public static Vector3i from(Vector3i v) {
        return from(v.getX(), v.getY(), v.getZ());
    }

    @Nonnull
    public static Vector3i from(Vector4i v) {
        return from(v.getX(), v.getY(), v.getZ());
    }

    @Nonnull
    public static Vector3i from(VectorNi v) {
        return from(v.get(0), v.get(1), v.size() > 2 ? v.get(2) : 0);
    }

    @Nonnull
    public static Vector3i from(double x, double y, double z) {
        return from(GenericMath.floor(x), GenericMath.floor(y), GenericMath.floor(z));
    }

    @Nonnull
    public static Vector3i from(int x, int y, int z) {
        return Vectors.createVector3i(x, y, z);
    }
}
