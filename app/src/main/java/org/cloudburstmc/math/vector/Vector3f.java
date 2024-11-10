package org.cloudburstmc.math.vector;

import java.io.Serializable;
import java.util.Random;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.cloudburstmc.math.GenericMath;
import org.cloudburstmc.math.TrigMath;

@ParametersAreNonnullByDefault
/* loaded from: classes5.dex */
public abstract class Vector3f implements Vectorf, Comparable<Vector3f>, Serializable, Cloneable {
    public static final Vector3f ZERO = from(0.0f, 0.0f, 0.0f);
    public static final Vector3f UNIT_X = from(1.0f, 0.0f, 0.0f);
    public static final Vector3f UNIT_Y = from(0.0f, 1.0f, 0.0f);
    public static final Vector3f UNIT_Z = from(0.0f, 0.0f, 1.0f);
    public static final Vector3f ONE = from(1.0f, 1.0f, 1.0f);
    public static final Vector3f RIGHT = UNIT_X;
    public static final Vector3f UP = UNIT_Y;
    public static final Vector3f FORWARD = UNIT_Z;

    @Override // org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public abstract Vector3f abs();

    @Nonnull
    public abstract Vector3f add(float f, float f2, float f3);

    @Override // org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public abstract Vector3f ceil();

    @Nonnull
    public abstract Vector3f cross(float f, float f2, float f3);

    @Nonnull
    public abstract Vector3f div(float f, float f2, float f3);

    @Nonnull
    public abstract Vector3f down(float f);

    @Nonnull
    public abstract Vector3f east(float f);

    @Override // org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public abstract Vector3f floor();

    public abstract float getX();

    public abstract float getY();

    public abstract float getZ();

    @Nonnull
    public abstract Vector3f max(float f, float f2, float f3);

    @Nonnull
    public abstract Vector3f min(float f, float f2, float f3);

    @Nonnull
    public abstract Vector3f mul(float f, float f2, float f3);

    @Override // org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public abstract Vector3f negate();

    @Override // org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public abstract Vector3f normalize();

    @Nonnull
    public abstract Vector3f north(float f);

    @Override // org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public abstract Vector3f pow(float f);

    @Nonnull
    public abstract Vector3f project(float f, float f2, float f3);

    @Override // org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public abstract Vector3f round();

    @Nonnull
    public abstract Vector3f south(float f);

    @Nonnull
    public abstract Vector3f sub(float f, float f2, float f3);

    @Nonnull
    public abstract Vector3f up(float f);

    @Nonnull
    public abstract Vector3f west(float f);

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
    public Vector3f add(Vector3f v) {
        return add(v.getX(), v.getY(), v.getZ());
    }

    @Nonnull
    public Vector3f add(double x, double y, double z) {
        return add((float) x, (float) y, (float) z);
    }

    @Nonnull
    public Vector3f sub(Vector3f v) {
        return sub(v.getX(), v.getY(), v.getZ());
    }

    @Nonnull
    public Vector3f sub(double x, double y, double z) {
        return sub((float) x, (float) y, (float) z);
    }

    @Nonnull
    public Vector3f mul(double a) {
        return mul((float) a);
    }

    @Override // org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public Vector3f mul(float a) {
        return mul(a, a, a);
    }

    @Nonnull
    public Vector3f mul(Vector3f v) {
        return mul(v.getX(), v.getY(), v.getZ());
    }

    @Nonnull
    public Vector3f mul(double x, double y, double z) {
        return mul((float) x, (float) y, (float) z);
    }

    @Nonnull
    public Vector3f div(double a) {
        return div((float) a);
    }

    @Override // org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public Vector3f div(float a) {
        return div(a, a, a);
    }

    @Nonnull
    public Vector3f div(Vector3f v) {
        return div(v.getX(), v.getY(), v.getZ());
    }

    @Nonnull
    public Vector3f div(double x, double y, double z) {
        return div((float) x, (float) y, (float) z);
    }

    public float dot(Vector3f v) {
        return dot(v.getX(), v.getY(), v.getZ());
    }

    public float dot(double x, double y, double z) {
        return dot((float) x, (float) y, (float) z);
    }

    public float dot(float x, float y, float z) {
        return (getX() * x) + (getY() * y) + (getZ() * z);
    }

    @Nonnull
    public Vector3f project(Vector3f v) {
        return project(v.getX(), v.getY(), v.getZ());
    }

    @Nonnull
    public Vector3f project(double x, double y, double z) {
        return project((float) x, (float) y, (float) z);
    }

    @Nonnull
    public Vector3f cross(Vector3f v) {
        return cross(v.getX(), v.getY(), v.getZ());
    }

    @Nonnull
    public Vector3f cross(double x, double y, double z) {
        return cross((float) x, (float) y, (float) z);
    }

    @Nonnull
    public Vector3f pow(double pow) {
        return pow((float) pow);
    }

    @Nonnull
    public Vector3f min(Vector3f v) {
        return min(v.getX(), v.getY(), v.getZ());
    }

    @Nonnull
    public Vector3f min(double x, double y, double z) {
        return min((float) x, (float) y, (float) z);
    }

    @Nonnull
    public Vector3f max(Vector3f v) {
        return max(v.getX(), v.getY(), v.getZ());
    }

    @Nonnull
    public Vector3f max(double x, double y, double z) {
        return max((float) x, (float) y, (float) z);
    }

    public float distanceSquared(Vector3f v) {
        return distanceSquared(v.getX(), v.getY(), v.getZ());
    }

    public float distanceSquared(double x, double y, double z) {
        return distanceSquared((float) x, (float) y, (float) z);
    }

    public float distanceSquared(float x, float y, float z) {
        float dx = getX() - x;
        float dy = getY() - y;
        float dz = getZ() - z;
        return (dx * dx) + (dy * dy) + (dz * dz);
    }

    public float distance(Vector3f v) {
        return distance(v.getX(), v.getY(), v.getZ());
    }

    public float distance(double x, double y, double z) {
        return distance((float) x, (float) y, (float) z);
    }

    public float distance(float x, float y, float z) {
        return (float) Math.sqrt(distanceSquared(x, y, z));
    }

    @Nonnull
    public Vector3f up() {
        return up(1.0f);
    }

    @Nonnull
    public Vector3f down() {
        return down(1.0f);
    }

    @Nonnull
    public Vector3f north() {
        return north(1.0f);
    }

    @Nonnull
    public Vector3f south() {
        return south(1.0f);
    }

    @Nonnull
    public Vector3f east() {
        return east(1.0f);
    }

    @Nonnull
    public Vector3f west() {
        return west(1.0f);
    }

    @Override // org.cloudburstmc.math.vector.Vectorf
    public float lengthSquared() {
        return (getX() * getX()) + (getY() * getY()) + (getZ() * getZ());
    }

    @Override // org.cloudburstmc.math.vector.Vectorf
    public float length() {
        return (float) Math.sqrt(lengthSquared());
    }

    @Override // org.cloudburstmc.math.vector.Vectorf
    public int getMinAxis() {
        return getX() < getY() ? getX() < getZ() ? 0 : 2 : getY() < getZ() ? 1 : 2;
    }

    @Override // org.cloudburstmc.math.vector.Vectorf
    public int getMaxAxis() {
        return getX() < getY() ? getY() < getZ() ? 2 : 1 : getX() < getZ() ? 2 : 0;
    }

    @Nonnull
    public Vector2f toVector2() {
        return Vector2f.from(this);
    }

    @Nonnull
    public Vector2f toVector2(boolean useZ) {
        return Vector2f.from(getX(), useZ ? getZ() : getY());
    }

    @Nonnull
    public Vector4f toVector4() {
        return toVector4(0.0f);
    }

    @Nonnull
    public Vector4f toVector4(double w) {
        return toVector4((float) w);
    }

    @Nonnull
    public Vector4f toVector4(float w) {
        return Vector4f.from(this, w);
    }

    @Nonnull
    public VectorNf toVectorN() {
        return VectorNf.from(this);
    }

    @Override // org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public float[] toArray() {
        return new float[]{getX(), getY(), getZ()};
    }

    @Override // org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public Vector3i toInt() {
        return Vector3i.from(getX(), getY(), getZ());
    }

    @Override // org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public Vector3l toLong() {
        return Vector3l.from(getX(), getY(), getZ());
    }

    @Override // org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public Vector3f toFloat() {
        return from(getX(), getY(), getZ());
    }

    @Override // org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public Vector3d toDouble() {
        return Vector3d.from(getX(), getY(), getZ());
    }

    @Override // java.lang.Comparable
    public int compareTo(Vector3f v) {
        return (int) Math.signum(lengthSquared() - v.lengthSquared());
    }

    @Nonnull
    public Vector3f clone() {
        return from(this);
    }

    @Nonnull
    public String toString() {
        return "(" + getX() + ", " + getY() + ", " + getZ() + ")";
    }

    @Nonnull
    public static Vector3f from(float n) {
        return Vectors.createVector3f(n, n, n);
    }

    @Nonnull
    public static Vector3f from(Vector2f v) {
        return from(v, 0.0f);
    }

    @Nonnull
    public static Vector3f from(Vector2f v, double z) {
        return from(v, (float) z);
    }

    @Nonnull
    public static Vector3f from(Vector2f v, float z) {
        return from(v.getX(), v.getY(), z);
    }

    @Nonnull
    public static Vector3f from(Vector3f v) {
        return from(v.getX(), v.getY(), v.getZ());
    }

    @Nonnull
    public static Vector3f from(Vector4f v) {
        return from(v.getX(), v.getY(), v.getZ());
    }

    @Nonnull
    public static Vector3f from(VectorNf v) {
        return from(v.get(0), v.get(1), v.size() > 2 ? v.get(2) : 0.0f);
    }

    @Nonnull
    public static Vector3f from(double x, double y, double z) {
        return from((float) x, (float) y, (float) z);
    }

    @Nonnull
    public static Vector3f from(float x, float y, float z) {
        return Vectors.createVector3f(x, y, z);
    }

    @Nonnull
    public static Vector3f createRandomDirection(Random random) {
        return createDirectionRad(random.nextFloat() * 6.2831855f, random.nextFloat() * 6.2831855f);
    }

    @Nonnull
    public static Vector3f createDirectionDeg(double theta, double phi) {
        return createDirectionDeg((float) theta, (float) phi);
    }

    @Nonnull
    public static Vector3f createDirectionDeg(float theta, float phi) {
        return createDirectionRad((float) Math.toRadians(theta), (float) Math.toRadians(phi));
    }

    @Nonnull
    public static Vector3f createDirectionRad(double theta, double phi) {
        return createDirectionRad((float) theta, (float) phi);
    }

    @Nonnull
    public static Vector3f createDirectionRad(float theta, float phi) {
        float f = TrigMath.sin(phi);
        return from(TrigMath.cos(theta) * f, TrigMath.sin(theta) * f, TrigMath.cos(phi));
    }
}
