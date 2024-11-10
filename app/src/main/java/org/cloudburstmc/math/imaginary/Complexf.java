package org.cloudburstmc.math.imaginary;

import java.io.Serializable;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.cloudburstmc.math.GenericMath;
import org.cloudburstmc.math.TrigMath;
import org.cloudburstmc.math.vector.Vector2f;
import org.cloudburstmc.math.vector.Vector3f;

@ParametersAreNonnullByDefault
/* loaded from: classes5.dex */
public abstract class Complexf implements Imaginaryf, Comparable<Complexf>, Serializable, Cloneable {
    public static final Complexf ZERO = from(0.0f, 0.0f);
    public static final Complexf IDENTITY = from(1.0f, 0.0f);

    @Nonnull
    public abstract Complexf add(float f, float f2);

    @Override // org.cloudburstmc.math.imaginary.Imaginaryf
    @Nonnull
    public abstract Complexf conjugate();

    @Override // org.cloudburstmc.math.imaginary.Imaginaryf
    @Nonnull
    public abstract Complexf div(float f);

    @Nonnull
    public abstract Complexf div(float f, float f2);

    public abstract float getX();

    public abstract float getY();

    @Override // org.cloudburstmc.math.imaginary.Imaginaryf
    @Nonnull
    public abstract Complexf invert();

    @Override // org.cloudburstmc.math.imaginary.Imaginaryf
    @Nonnull
    public abstract Complexf mul(float f);

    @Nonnull
    public abstract Complexf mul(float f, float f2);

    @Override // org.cloudburstmc.math.imaginary.Imaginaryf
    @Nonnull
    public abstract Complexf normalize();

    @Nonnull
    public abstract Complexf sub(float f, float f2);

    @Nonnull
    public Complexf add(Complexf c) {
        return add(c.getX(), c.getY());
    }

    @Nonnull
    public Complexf add(double x, double y) {
        return add((float) x, (float) y);
    }

    @Nonnull
    public Complexf sub(Complexf c) {
        return sub(c.getX(), c.getY());
    }

    @Nonnull
    public Complexf sub(double x, double y) {
        return sub((float) x, (float) y);
    }

    @Nonnull
    public Complexf mul(double a) {
        return mul((float) a);
    }

    @Nonnull
    public Complexf mul(Complexf c) {
        return mul(c.getX(), c.getY());
    }

    @Nonnull
    public Complexf mul(double x, double y) {
        return mul((float) x, (float) y);
    }

    @Nonnull
    public Complexf div(double a) {
        return div((float) a);
    }

    @Nonnull
    public Complexf div(Complexf c) {
        return div(c.getX(), c.getY());
    }

    @Nonnull
    public Complexf div(double x, double y) {
        return div((float) x, (float) y);
    }

    public float dot(Complexf c) {
        return dot(c.getX(), c.getY());
    }

    public float dot(double x, double y) {
        return dot((float) x, (float) y);
    }

    public float dot(float x, float y) {
        return (getX() * x) + (getY() * y);
    }

    @Nonnull
    public Vector2f rotate(Vector2f v) {
        return rotate(v.getX(), v.getY());
    }

    @Nonnull
    public Vector2f rotate(double x, double y) {
        return rotate((float) x, (float) y);
    }

    @Nonnull
    public Vector2f rotate(float x, float y) {
        float length = length();
        if (Math.abs(length) < GenericMath.FLT_EPSILON) {
            throw new ArithmeticException("Cannot rotate by the zero complex");
        }
        float nx = getX() / length;
        float ny = getY() / length;
        return Vector2f.from((x * nx) - (y * ny), (y * nx) + (x * ny));
    }

    @Nonnull
    public Vector2f getDirection() {
        return Vector2f.from(getX(), getY()).normalize();
    }

    public float getAngleRad() {
        return (float) TrigMath.atan2(getY(), getX());
    }

    public float getAngleDeg() {
        return (float) Math.toDegrees(getAngleRad());
    }

    @Override // org.cloudburstmc.math.imaginary.Imaginaryf
    public float lengthSquared() {
        return (getX() * getX()) + (getY() * getY());
    }

    @Override // org.cloudburstmc.math.imaginary.Imaginaryf
    public float length() {
        return (float) Math.sqrt(lengthSquared());
    }

    @Nonnull
    public Quaternionf toQuaternion() {
        return toQuaternion(Vector3f.UNIT_Z);
    }

    @Nonnull
    public Quaternionf toQuaternion(Vector3f axis) {
        return toQuaternion(axis.getX(), axis.getY(), axis.getZ());
    }

    @Nonnull
    public Quaternionf toQuaternion(double x, double y, double z) {
        return toQuaternion((float) x, (float) y, (float) z);
    }

    @Nonnull
    public Quaternionf toQuaternion(float x, float y, float z) {
        return Quaternionf.fromAngleRadAxis(getAngleRad(), x, y, z);
    }

    @Override // org.cloudburstmc.math.imaginary.Imaginaryf
    @Nonnull
    public Complexf toFloat() {
        return from(getX(), getY());
    }

    @Override // org.cloudburstmc.math.imaginary.Imaginaryf
    @Nonnull
    public Complexd toDouble() {
        return Complexd.from(getX(), getY());
    }

    @Override // java.lang.Comparable
    public int compareTo(Complexf c) {
        return (int) Math.signum(lengthSquared() - c.lengthSquared());
    }

    @Nonnull
    public Complexf clone() {
        return from(this);
    }

    @Nonnull
    public String toString() {
        return "(" + getX() + ", " + getY() + ")";
    }

    @Nonnull
    public static Complexf fromReal(float x) {
        return Imaginary.createComplexf(x, 0.0f);
    }

    @Nonnull
    public static Complexf fromImaginary(float y) {
        return Imaginary.createComplexf(0.0f, y);
    }

    @Nonnull
    public static Complexf from(Complexf c) {
        return from(c.getX(), c.getY());
    }

    @Nonnull
    public static Complexf from(double x, double y) {
        return from((float) x, (float) y);
    }

    @Nonnull
    public static Complexf from(float x, float y) {
        return Imaginary.createComplexf(x, y);
    }

    @Nonnull
    public static Complexf fromRotationTo(Vector2f from, Vector2f to) {
        return fromAngleRad(TrigMath.acos(from.dot(to) / (from.length() * to.length())));
    }

    @Nonnull
    public static Complexf fromRotationTo(Vector3f from, Vector3f to) {
        return fromAngleRad(TrigMath.acos(from.dot(to) / (from.length() * to.length())));
    }

    @Nonnull
    public static Complexf fromAngleDeg(double angle) {
        return fromAngleRad(Math.toRadians(angle));
    }

    @Nonnull
    public static Complexf fromAngleRad(double angle) {
        return fromAngleRad((float) angle);
    }

    @Nonnull
    public static Complexf fromAngleDeg(float angle) {
        return fromAngleRad((float) Math.toRadians(angle));
    }

    @Nonnull
    public static Complexf fromAngleRad(float angle) {
        return from(TrigMath.cos(angle), TrigMath.sin(angle));
    }
}
