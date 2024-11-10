package org.cloudburstmc.math.vector;

import java.io.Serializable;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.cloudburstmc.math.GenericMath;

@ParametersAreNonnullByDefault
/* loaded from: classes5.dex */
public abstract class Vector4f implements Vectorf, Comparable<Vector4f>, Serializable, Cloneable {
    public static final Vector4f ZERO = from(0.0f, 0.0f, 0.0f, 0.0f);
    public static final Vector4f UNIT_X = from(1.0f, 0.0f, 0.0f, 0.0f);
    public static final Vector4f UNIT_Y = from(0.0f, 1.0f, 0.0f, 0.0f);
    public static final Vector4f UNIT_Z = from(0.0f, 0.0f, 1.0f, 0.0f);
    public static final Vector4f UNIT_W = from(0.0f, 0.0f, 0.0f, 1.0f);
    public static final Vector4f ONE = from(1.0f, 1.0f, 1.0f, 1.0f);

    @Override // org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public abstract Vector4f abs();

    @Nonnull
    public abstract Vector4f add(float f, float f2, float f3, float f4);

    @Override // org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public abstract Vector4f ceil();

    @Nonnull
    public abstract Vector4f div(float f, float f2, float f3, float f4);

    @Override // org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public abstract Vector4f floor();

    public abstract float getW();

    public abstract float getX();

    public abstract float getY();

    public abstract float getZ();

    @Nonnull
    public abstract Vector4f max(float f, float f2, float f3, float f4);

    @Nonnull
    public abstract Vector4f min(float f, float f2, float f3, float f4);

    @Nonnull
    public abstract Vector4f mul(float f, float f2, float f3, float f4);

    @Override // org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public abstract Vector4f negate();

    @Override // org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public abstract Vector4f normalize();

    @Override // org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public abstract Vector4f pow(float f);

    @Nonnull
    public abstract Vector4f project(float f, float f2, float f3, float f4);

    @Override // org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public abstract Vector4f round();

    @Nonnull
    public abstract Vector4f sub(float f, float f2, float f3, float f4);

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
    public Vector4f add(Vector4f v) {
        return add(v.getX(), v.getY(), v.getZ(), v.getW());
    }

    @Nonnull
    public Vector4f add(double x, double y, double z, double w) {
        return add((float) x, (float) y, (float) z, (float) w);
    }

    @Nonnull
    public Vector4f sub(Vector4f v) {
        return sub(v.getX(), v.getY(), v.getZ(), v.getW());
    }

    @Nonnull
    public Vector4f sub(double x, double y, double z, double w) {
        return sub((float) x, (float) y, (float) z, (float) w);
    }

    @Nonnull
    public Vector4f mul(double a) {
        return mul((float) a);
    }

    @Override // org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public Vector4f mul(float a) {
        return mul(a, a, a, a);
    }

    @Nonnull
    public Vector4f mul(Vector4f v) {
        return mul(v.getX(), v.getY(), v.getZ(), v.getW());
    }

    @Nonnull
    public Vector4f mul(double x, double y, double z, double w) {
        return mul((float) x, (float) y, (float) z, (float) w);
    }

    @Nonnull
    public Vector4f div(double a) {
        return div((float) a);
    }

    @Override // org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public Vector4f div(float a) {
        return div(a, a, a, a);
    }

    @Nonnull
    public Vector4f div(Vector4f v) {
        return div(v.getX(), v.getY(), v.getZ(), v.getW());
    }

    @Nonnull
    public Vector4f div(double x, double y, double z, double w) {
        return div((float) x, (float) y, (float) z, (float) w);
    }

    public float dot(Vector4f v) {
        return dot(v.getX(), v.getY(), v.getZ(), v.getW());
    }

    public float dot(double x, double y, double z, double w) {
        return dot((float) x, (float) y, (float) z, (float) w);
    }

    public float dot(float x, float y, float z, float w) {
        return (getX() * x) + (getY() * y) + (getZ() * z) + (getW() * w);
    }

    @Nonnull
    public Vector4f project(Vector4f v) {
        return project(v.getX(), v.getY(), v.getZ(), v.getW());
    }

    @Nonnull
    public Vector4f project(double x, double y, double z, double w) {
        return project((float) x, (float) y, (float) z, (float) w);
    }

    @Nonnull
    public Vector4f pow(double pow) {
        return pow((float) pow);
    }

    @Nonnull
    public Vector4f min(Vector4f v) {
        return min(v.getX(), v.getY(), v.getZ(), v.getW());
    }

    @Nonnull
    public Vector4f min(double x, double y, double z, double w) {
        return min((float) x, (float) y, (float) z, (float) w);
    }

    @Nonnull
    public Vector4f max(Vector4f v) {
        return max(v.getX(), v.getY(), v.getZ(), v.getW());
    }

    @Nonnull
    public Vector4f max(double x, double y, double z, double w) {
        return max((float) x, (float) y, (float) z, (float) w);
    }

    public float distanceSquared(Vector4f v) {
        return distanceSquared(v.getX(), v.getY(), v.getZ(), v.getW());
    }

    public float distanceSquared(double x, double y, double z, double w) {
        return distanceSquared((float) x, (float) y, (float) z, (float) w);
    }

    public float distanceSquared(float x, float y, float z, float w) {
        float dx = getX() - x;
        float dy = getY() - y;
        float dz = getZ() - z;
        float dw = getW() - w;
        return (dx * dx) + (dy * dy) + (dz * dz) + (dw * dw);
    }

    public float distance(Vector4f v) {
        return distance(v.getX(), v.getY(), v.getZ(), v.getW());
    }

    public float distance(double x, double y, double z, double w) {
        return distance((float) x, (float) y, (float) z, (float) w);
    }

    public float distance(float x, float y, float z, float w) {
        return (float) Math.sqrt(distanceSquared(x, y, z, w));
    }

    @Override // org.cloudburstmc.math.vector.Vectorf
    public float lengthSquared() {
        return (getX() * getX()) + (getY() * getY()) + (getZ() * getZ()) + (getW() * getW());
    }

    @Override // org.cloudburstmc.math.vector.Vectorf
    public float length() {
        return (float) Math.sqrt(lengthSquared());
    }

    @Override // org.cloudburstmc.math.vector.Vectorf
    public int getMinAxis() {
        float value = getX();
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

    @Override // org.cloudburstmc.math.vector.Vectorf
    public int getMaxAxis() {
        float value = getX();
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
    public Vector2f toVector2() {
        return Vector2f.from(this);
    }

    @Nonnull
    public Vector3f toVector3() {
        return Vector3f.from(this);
    }

    @Nonnull
    public VectorNf toVectorN() {
        return VectorNf.from(this);
    }

    @Override // org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public float[] toArray() {
        return new float[]{getX(), getY(), getZ(), getW()};
    }

    @Override // org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public Vector4i toInt() {
        return Vector4i.from(getX(), getY(), getZ(), getW());
    }

    @Override // org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public Vector4l toLong() {
        return Vector4l.from(getX(), getY(), getZ(), getW());
    }

    @Override // org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public Vector4f toFloat() {
        return from(getX(), getY(), getZ(), getW());
    }

    @Override // org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public Vector4d toDouble() {
        return Vector4d.from(getX(), getY(), getZ(), getW());
    }

    @Override // java.lang.Comparable
    public int compareTo(Vector4f v) {
        return (int) Math.signum(lengthSquared() - v.lengthSquared());
    }

    @Nonnull
    public Vector4f clone() {
        return from(this);
    }

    @Nonnull
    public String toString() {
        return "(" + getX() + ", " + getY() + ", " + getZ() + ", " + getW() + ")";
    }

    @Nonnull
    public static Vector4f from(float n) {
        return from(n, n, n, n);
    }

    @Nonnull
    public static Vector4f from(Vector2f v) {
        return from(v, 0.0f, 0.0f);
    }

    @Nonnull
    public static Vector4f from(Vector2f v, double z, double w) {
        return from(v, (float) z, (float) w);
    }

    @Nonnull
    public static Vector4f from(Vector2f v, float z, float w) {
        return from(v.getX(), v.getY(), z, w);
    }

    @Nonnull
    public static Vector4f from(Vector3f v) {
        return from(v, 0.0f);
    }

    @Nonnull
    public static Vector4f from(Vector3f v, double w) {
        return from(v, (float) w);
    }

    @Nonnull
    public static Vector4f from(Vector3f v, float w) {
        return from(v.getX(), v.getY(), v.getZ(), w);
    }

    @Nonnull
    public static Vector4f from(Vector4f v) {
        return from(v.getX(), v.getY(), v.getZ(), v.getW());
    }

    @Nonnull
    public static Vector4f from(VectorNf v) {
        return from(v.get(0), v.get(1), v.size() > 2 ? v.get(2) : 0.0f, v.size() > 3 ? v.get(3) : 0.0f);
    }

    @Nonnull
    public static Vector4f from(double x, double y, double z, double w) {
        return from((float) x, (float) y, (float) z, (float) w);
    }

    @Nonnull
    public static Vector4f from(float x, float y, float z, float w) {
        return Vectors.createVector4f(x, y, z, w);
    }
}
