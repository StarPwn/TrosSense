package org.cloudburstmc.math.vector;

import java.io.Serializable;
import java.util.Random;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.cloudburstmc.math.GenericMath;
import org.cloudburstmc.math.TrigMath;

@ParametersAreNonnullByDefault
/* loaded from: classes5.dex */
public abstract class Vector2f implements Vectorf, Comparable<Vector2f>, Serializable, Cloneable {
    public static final Vector2f ZERO = from(0.0f, 0.0f);
    public static final Vector2f UNIT_X = from(1.0f, 0.0f);
    public static final Vector2f UNIT_Y = from(0.0f, 1.0f);
    public static final Vector2f ONE = from(1.0f, 1.0f);

    @Override // org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public abstract Vector2f abs();

    @Nonnull
    public abstract Vector2f add(float f, float f2);

    @Override // org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public abstract Vector2f ceil();

    @Nonnull
    public abstract Vector2f div(float f, float f2);

    @Nonnull
    public abstract Vector2f east(float f);

    @Override // org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public abstract Vector2f floor();

    public abstract float getX();

    public abstract float getY();

    @Nonnull
    public abstract Vector2f max(float f, float f2);

    @Nonnull
    public abstract Vector2f min(float f, float f2);

    @Nonnull
    public abstract Vector2f mul(float f, float f2);

    @Override // org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public abstract Vector2f negate();

    @Override // org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public abstract Vector2f normalize();

    @Nonnull
    public abstract Vector2f north(float f);

    @Override // org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public abstract Vector2f pow(float f);

    @Nonnull
    public abstract Vector2f project(float f, float f2);

    @Override // org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public abstract Vector2f round();

    @Nonnull
    public abstract Vector2f south(float f);

    @Nonnull
    public abstract Vector2f sub(float f, float f2);

    @Nonnull
    public abstract Vector2f west(float f);

    public int getFloorX() {
        return GenericMath.floor(getX());
    }

    public int getFloorY() {
        return GenericMath.floor(getY());
    }

    @Nonnull
    public Vector2f add(Vector2f v) {
        return add(v.getX(), v.getY());
    }

    @Nonnull
    public Vector2f add(double x, double y) {
        return add((float) x, (float) y);
    }

    @Nonnull
    public Vector2f sub(Vector2f v) {
        return sub(v.getX(), v.getY());
    }

    @Nonnull
    public Vector2f sub(double x, double y) {
        return sub((float) x, (float) y);
    }

    @Nonnull
    public Vector2f mul(double a) {
        return mul((float) a);
    }

    @Override // org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public Vector2f mul(float a) {
        return mul(a, a);
    }

    @Nonnull
    public Vector2f mul(Vector2f v) {
        return mul(v.getX(), v.getY());
    }

    @Nonnull
    public Vector2f mul(double x, double y) {
        return mul((float) x, (float) y);
    }

    @Nonnull
    public Vector2f div(double a) {
        return div((float) a);
    }

    @Override // org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public Vector2f div(float a) {
        return div(a, a);
    }

    @Nonnull
    public Vector2f div(Vector2f v) {
        return div(v.getX(), v.getY());
    }

    @Nonnull
    public Vector2f div(double x, double y) {
        return div((float) x, (float) y);
    }

    public float dot(Vector2f v) {
        return dot(v.getX(), v.getY());
    }

    public float dot(double x, double y) {
        return dot((float) x, (float) y);
    }

    public float dot(float x, float y) {
        return (getX() * x) + (getY() * y);
    }

    @Nonnull
    public Vector2f project(Vector2f v) {
        return project(v.getX(), v.getY());
    }

    @Nonnull
    public Vector2f project(double x, double y) {
        return project((float) x, (float) y);
    }

    @Nonnull
    public Vector2f pow(double pow) {
        return pow((float) pow);
    }

    @Nonnull
    public Vector2f min(Vector2f v) {
        return min(v.getX(), v.getY());
    }

    @Nonnull
    public Vector2f min(double x, double y) {
        return min((float) x, (float) y);
    }

    @Nonnull
    public Vector2f max(Vector2f v) {
        return max(v.getX(), v.getY());
    }

    @Nonnull
    public Vector2f max(double x, double y) {
        return max((float) x, (float) y);
    }

    public float distanceSquared(Vector2f v) {
        return distanceSquared(v.getX(), v.getY());
    }

    public float distanceSquared(double x, double y) {
        return distanceSquared((float) x, (float) y);
    }

    public float distanceSquared(float x, float y) {
        float dx = getX() - x;
        float dy = getY() - y;
        return (dx * dx) + (dy * dy);
    }

    public float distance(Vector2f v) {
        return distance(v.getX(), v.getY());
    }

    public float distance(double x, double y) {
        return distance((float) x, (float) y);
    }

    public float distance(float x, float y) {
        return (float) Math.sqrt(distanceSquared(x, y));
    }

    @Nonnull
    public Vector2f north() {
        return north(1.0f);
    }

    @Nonnull
    public Vector2f south() {
        return south(1.0f);
    }

    @Nonnull
    public Vector2f east() {
        return east(1.0f);
    }

    @Nonnull
    public Vector2f west() {
        return west(1.0f);
    }

    @Override // org.cloudburstmc.math.vector.Vectorf
    public float lengthSquared() {
        return (getX() * getX()) + (getY() * getY());
    }

    @Override // org.cloudburstmc.math.vector.Vectorf
    public float length() {
        return (float) Math.sqrt(lengthSquared());
    }

    @Override // org.cloudburstmc.math.vector.Vectorf
    public int getMinAxis() {
        return getX() < getY() ? 0 : 1;
    }

    @Override // org.cloudburstmc.math.vector.Vectorf
    public int getMaxAxis() {
        return getX() > getY() ? 0 : 1;
    }

    @Nonnull
    public Vector3f toVector3() {
        return toVector3(0.0f);
    }

    @Nonnull
    public Vector3f toVector3(double z) {
        return toVector3((float) z);
    }

    @Nonnull
    public Vector3f toVector3(float z) {
        return Vector3f.from(this, z);
    }

    @Nonnull
    public Vector4f toVector4() {
        return toVector4(0.0f, 0.0f);
    }

    @Nonnull
    public Vector4f toVector4(double z, double w) {
        return toVector4((float) z, (float) w);
    }

    @Nonnull
    public Vector4f toVector4(float z, float w) {
        return Vector4f.from(this, z, w);
    }

    @Nonnull
    public VectorNf toVectorN() {
        return VectorNf.from(this);
    }

    @Override // org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public float[] toArray() {
        return new float[]{getX(), getY()};
    }

    @Override // org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public Vector2i toInt() {
        return Vector2i.from(getX(), getY());
    }

    @Override // org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public Vector2l toLong() {
        return Vector2l.from(getX(), getY());
    }

    @Override // org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public Vector2f toFloat() {
        return from(getX(), getY());
    }

    @Override // org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public Vector2d toDouble() {
        return Vector2d.from(getX(), getY());
    }

    @Override // java.lang.Comparable
    public int compareTo(Vector2f v) {
        return (int) Math.signum(lengthSquared() - v.lengthSquared());
    }

    @Nonnull
    public Vector2f clone() {
        return from(this);
    }

    @Nonnull
    public String toString() {
        return "(" + getX() + ", " + getY() + ")";
    }

    @Nonnull
    public static Vector2f from(float n) {
        return Vectors.createVector2f(n, n);
    }

    @Nonnull
    public static Vector2f from(Vector2f v) {
        return from(v.getX(), v.getY());
    }

    @Nonnull
    public static Vector2f from(Vector3f v) {
        return from(v.getX(), v.getY());
    }

    @Nonnull
    public static Vector2f from(Vector4f v) {
        return from(v.getX(), v.getY());
    }

    @Nonnull
    public static Vector2f from(VectorNf v) {
        return from(v.get(0), v.get(1));
    }

    @Nonnull
    public static Vector2f from(double x, double y) {
        return from((float) x, (float) y);
    }

    @Nonnull
    public static Vector2f from(float x, float y) {
        return Vectors.createVector2f(x, y);
    }

    @Nonnull
    public static Vector2f createRandomDirection(Random random) {
        return createDirectionRad(random.nextFloat() * 6.2831855f);
    }

    @Nonnull
    public static Vector2f createDirectionDeg(double angle) {
        return createDirectionDeg((float) angle);
    }

    @Nonnull
    public static Vector2f createDirectionDeg(float angle) {
        return createDirectionRad((float) Math.toRadians(angle));
    }

    @Nonnull
    public static Vector2f createDirectionRad(double angle) {
        return createDirectionRad((float) angle);
    }

    @Nonnull
    public static Vector2f createDirectionRad(float angle) {
        return from(TrigMath.cos(angle), TrigMath.sin(angle));
    }
}
