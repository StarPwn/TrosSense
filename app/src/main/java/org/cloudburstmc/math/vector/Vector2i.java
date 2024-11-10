package org.cloudburstmc.math.vector;

import java.io.Serializable;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.cloudburstmc.math.GenericMath;

@ParametersAreNonnullByDefault
/* loaded from: classes5.dex */
public abstract class Vector2i implements Vectori, Comparable<Vector2i>, Serializable, Cloneable {
    public static final Vector2i ZERO = from(0, 0);
    public static final Vector2i UNIT_X = from(1, 0);
    public static final Vector2i UNIT_Y = from(0, 1);
    public static final Vector2i ONE = from(1, 1);

    @Override // org.cloudburstmc.math.vector.Vectori
    @Nonnull
    public abstract Vector2i abs();

    @Nonnull
    public abstract Vector2i add(int i, int i2);

    @Nonnull
    public abstract Vector2i div(int i, int i2);

    @Nonnull
    public abstract Vector2i east(int i);

    public abstract int getX();

    public abstract int getY();

    @Nonnull
    public abstract Vector2i max(int i, int i2);

    @Nonnull
    public abstract Vector2i min(int i, int i2);

    @Nonnull
    public abstract Vector2i mul(int i, int i2);

    @Override // org.cloudburstmc.math.vector.Vectori
    @Nonnull
    public abstract Vector2i negate();

    @Nonnull
    public abstract Vector2i north(int i);

    @Override // org.cloudburstmc.math.vector.Vectori
    @Nonnull
    public abstract Vector2i pow(int i);

    @Nonnull
    public abstract Vector2i project(int i, int i2);

    @Nonnull
    public abstract Vector2i south(int i);

    @Nonnull
    public abstract Vector2i sub(int i, int i2);

    @Nonnull
    public abstract Vector2i west(int i);

    @Nonnull
    public Vector2i add(Vector2i v) {
        return add(v.getX(), v.getY());
    }

    @Nonnull
    public Vector2i add(double x, double y) {
        return add(GenericMath.floor(x), GenericMath.floor(y));
    }

    @Nonnull
    public Vector2i sub(Vector2i v) {
        return sub(v.getX(), v.getY());
    }

    @Nonnull
    public Vector2i sub(double x, double y) {
        return sub(GenericMath.floor(x), GenericMath.floor(y));
    }

    @Nonnull
    public Vector2i mul(double a) {
        return mul(GenericMath.floor(a));
    }

    @Override // org.cloudburstmc.math.vector.Vectori
    @Nonnull
    public Vector2i mul(int a) {
        return mul(a, a);
    }

    @Nonnull
    public Vector2i mul(Vector2i v) {
        return mul(v.getX(), v.getY());
    }

    @Nonnull
    public Vector2i mul(double x, double y) {
        return mul(GenericMath.floor(x), GenericMath.floor(y));
    }

    @Nonnull
    public Vector2i div(double a) {
        return div(GenericMath.floor(a));
    }

    @Override // org.cloudburstmc.math.vector.Vectori
    @Nonnull
    public Vector2i div(int a) {
        return div(a, a);
    }

    @Nonnull
    public Vector2i div(Vector2i v) {
        return div(v.getX(), v.getY());
    }

    @Nonnull
    public Vector2i div(double x, double y) {
        return div(GenericMath.floor(x), GenericMath.floor(y));
    }

    public int dot(Vector2i v) {
        return dot(v.getX(), v.getY());
    }

    public int dot(double x, double y) {
        return dot(GenericMath.floor(x), GenericMath.floor(y));
    }

    public int dot(int x, int y) {
        return (getX() * x) + (getY() * y);
    }

    @Nonnull
    public Vector2i project(Vector2i v) {
        return project(v.getX(), v.getY());
    }

    @Nonnull
    public Vector2i project(double x, double y) {
        return project(GenericMath.floor(x), GenericMath.floor(y));
    }

    @Nonnull
    public Vector2i pow(double pow) {
        return pow(GenericMath.floor(pow));
    }

    @Nonnull
    public Vector2i min(Vector2i v) {
        return min(v.getX(), v.getY());
    }

    @Nonnull
    public Vector2i min(double x, double y) {
        return min(GenericMath.floor(x), GenericMath.floor(y));
    }

    @Nonnull
    public Vector2i max(Vector2i v) {
        return max(v.getX(), v.getY());
    }

    @Nonnull
    public Vector2i max(double x, double y) {
        return max(GenericMath.floor(x), GenericMath.floor(y));
    }

    public int distanceSquared(Vector2i v) {
        return distanceSquared(v.getX(), v.getY());
    }

    public int distanceSquared(double x, double y) {
        return distanceSquared(GenericMath.floor(x), GenericMath.floor(y));
    }

    public int distanceSquared(int x, int y) {
        int dx = getX() - x;
        int dy = getY() - y;
        return (dx * dx) + (dy * dy);
    }

    public float distance(Vector2i v) {
        return distance(v.getX(), v.getY());
    }

    public float distance(double x, double y) {
        return distance(GenericMath.floor(x), GenericMath.floor(y));
    }

    public float distance(int x, int y) {
        return (float) Math.sqrt(distanceSquared(x, y));
    }

    @Nonnull
    public Vector2i north() {
        return north(1);
    }

    @Nonnull
    public Vector2i south() {
        return south(1);
    }

    @Nonnull
    public Vector2i east() {
        return east(1);
    }

    @Nonnull
    public Vector2i west() {
        return west(1);
    }

    @Override // org.cloudburstmc.math.vector.Vectori
    public int lengthSquared() {
        return (getX() * getX()) + (getY() * getY());
    }

    @Override // org.cloudburstmc.math.vector.Vectori
    public float length() {
        return (float) Math.sqrt(lengthSquared());
    }

    @Override // org.cloudburstmc.math.vector.Vectori
    public int getMinAxis() {
        return getX() < getY() ? 0 : 1;
    }

    @Override // org.cloudburstmc.math.vector.Vectori
    public int getMaxAxis() {
        return getX() > getY() ? 0 : 1;
    }

    @Nonnull
    public Vector3i toVector3() {
        return toVector3(0);
    }

    @Nonnull
    public Vector3i toVector3(double z) {
        return toVector3(GenericMath.floor(z));
    }

    @Nonnull
    public Vector3i toVector3(int z) {
        return Vector3i.from(this, z);
    }

    @Nonnull
    public Vector4i toVector4() {
        return toVector4(0, 0);
    }

    @Nonnull
    public Vector4i toVector4(double z, double w) {
        return toVector4(GenericMath.floor(z), GenericMath.floor(w));
    }

    @Nonnull
    public Vector4i toVector4(int z, int w) {
        return Vector4i.from(this, z, w);
    }

    @Nonnull
    public VectorNi toVectorN() {
        return VectorNi.from(this);
    }

    @Override // org.cloudburstmc.math.vector.Vectori
    @Nonnull
    public int[] toArray() {
        return new int[]{getX(), getY()};
    }

    @Override // org.cloudburstmc.math.vector.Vectori
    @Nonnull
    public Vector2i toInt() {
        return from(getX(), getY());
    }

    @Override // org.cloudburstmc.math.vector.Vectori
    @Nonnull
    public Vector2l toLong() {
        return Vector2l.from(getX(), getY());
    }

    @Override // org.cloudburstmc.math.vector.Vectori
    @Nonnull
    public Vector2f toFloat() {
        return Vector2f.from(getX(), getY());
    }

    @Override // org.cloudburstmc.math.vector.Vectori
    @Nonnull
    public Vector2d toDouble() {
        return Vector2d.from(getX(), getY());
    }

    @Override // java.lang.Comparable
    public int compareTo(Vector2i v) {
        return lengthSquared() - v.lengthSquared();
    }

    @Nonnull
    public Vector2i clone() {
        return from(this);
    }

    @Nonnull
    public String toString() {
        return "(" + getX() + ", " + getY() + ")";
    }

    @Nonnull
    public static Vector2i from(int n) {
        return Vectors.createVector2i(n, n);
    }

    @Nonnull
    public static Vector2i from(Vector2i v) {
        return from(v.getX(), v.getY());
    }

    @Nonnull
    public static Vector2i from(Vector3i v) {
        return from(v.getX(), v.getY());
    }

    @Nonnull
    public static Vector2i from(Vector4i v) {
        return from(v.getX(), v.getY());
    }

    @Nonnull
    public static Vector2i from(VectorNi v) {
        return from(v.get(0), v.get(1));
    }

    @Nonnull
    public static Vector2i from(double x, double y) {
        return from(GenericMath.floor(x), GenericMath.floor(y));
    }

    @Nonnull
    public static Vector2i from(int x, int y) {
        return Vectors.createVector2i(x, y);
    }
}
