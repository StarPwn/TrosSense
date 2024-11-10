package org.cloudburstmc.math.imaginary;

import java.io.Serializable;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.cloudburstmc.math.GenericMath;
import org.cloudburstmc.math.TrigMath;
import org.cloudburstmc.math.vector.Vector2d;
import org.cloudburstmc.math.vector.Vector3d;

@ParametersAreNonnullByDefault
/* loaded from: classes5.dex */
public abstract class Complexd implements Imaginaryd, Comparable<Complexd>, Serializable, Cloneable {
    public static final Complexd ZERO = from(0.0f, 0.0f);
    public static final Complexd IDENTITY = from(1.0f, 0.0f);

    @Nonnull
    public abstract Complexd add(double d, double d2);

    @Override // org.cloudburstmc.math.imaginary.Imaginaryd
    @Nonnull
    public abstract Complexd conjugate();

    @Override // org.cloudburstmc.math.imaginary.Imaginaryd
    @Nonnull
    public abstract Complexd div(double d);

    @Nonnull
    public abstract Complexd div(double d, double d2);

    public abstract double getX();

    public abstract double getY();

    @Override // org.cloudburstmc.math.imaginary.Imaginaryd
    @Nonnull
    public abstract Complexd invert();

    @Override // org.cloudburstmc.math.imaginary.Imaginaryd
    @Nonnull
    public abstract Complexd mul(double d);

    @Nonnull
    public abstract Complexd mul(double d, double d2);

    @Override // org.cloudburstmc.math.imaginary.Imaginaryd
    @Nonnull
    public abstract Complexd normalize();

    @Nonnull
    public abstract Complexd sub(double d, double d2);

    @Nonnull
    public Complexd add(Complexd c) {
        return add(c.getX(), c.getY());
    }

    @Nonnull
    public Complexd add(float x, float y) {
        return add(x, y);
    }

    @Nonnull
    public Complexd sub(Complexd c) {
        return sub(c.getX(), c.getY());
    }

    @Nonnull
    public Complexd sub(float x, float y) {
        return sub(x, y);
    }

    @Nonnull
    public Complexd mul(float a) {
        return mul(a);
    }

    @Nonnull
    public Complexd mul(Complexd c) {
        return mul(c.getX(), c.getY());
    }

    @Nonnull
    public Complexd mul(float x, float y) {
        return mul(x, y);
    }

    @Nonnull
    public Complexd div(float a) {
        return div(a);
    }

    @Nonnull
    public Complexd div(Complexd c) {
        return div(c.getX(), c.getY());
    }

    @Nonnull
    public Complexd div(float x, float y) {
        return div(x, y);
    }

    public double dot(Complexd c) {
        return dot(c.getX(), c.getY());
    }

    public double dot(float x, float y) {
        return dot(x, y);
    }

    public double dot(double x, double y) {
        return (getX() * x) + (getY() * y);
    }

    @Nonnull
    public Vector2d rotate(Vector2d v) {
        return rotate(v.getX(), v.getY());
    }

    @Nonnull
    public Vector2d rotate(float x, float y) {
        return rotate(x, y);
    }

    @Nonnull
    public Vector2d rotate(double x, double y) {
        double length = length();
        if (Math.abs(length) < GenericMath.DBL_EPSILON) {
            throw new ArithmeticException("Cannot rotate by the zero complex");
        }
        double nx = getX() / length;
        double ny = getY() / length;
        return Vector2d.from((x * nx) - (y * ny), (y * nx) + (x * ny));
    }

    @Nonnull
    public Vector2d getDirection() {
        return Vector2d.from(getX(), getY()).normalize();
    }

    public double getAngleRad() {
        return TrigMath.atan2(getY(), getX());
    }

    public double getAngleDeg() {
        return Math.toDegrees(getAngleRad());
    }

    @Override // org.cloudburstmc.math.imaginary.Imaginaryd
    public double lengthSquared() {
        return (getX() * getX()) + (getY() * getY());
    }

    @Override // org.cloudburstmc.math.imaginary.Imaginaryd
    public double length() {
        return Math.sqrt(lengthSquared());
    }

    @Nonnull
    public Quaterniond toQuaternion() {
        return toQuaternion(Vector3d.UNIT_Z);
    }

    @Nonnull
    public Quaterniond toQuaternion(Vector3d axis) {
        return toQuaternion(axis.getX(), axis.getY(), axis.getZ());
    }

    @Nonnull
    public Quaterniond toQuaternion(float x, float y, float z) {
        return toQuaternion(x, y, z);
    }

    @Nonnull
    public Quaterniond toQuaternion(double x, double y, double z) {
        return Quaterniond.fromAngleRadAxis(getAngleRad(), x, y, z);
    }

    @Override // org.cloudburstmc.math.imaginary.Imaginaryd
    @Nonnull
    public Complexf toFloat() {
        return Complexf.from(getX(), getY());
    }

    @Override // org.cloudburstmc.math.imaginary.Imaginaryd
    @Nonnull
    public Complexd toDouble() {
        return from(getX(), getY());
    }

    @Override // java.lang.Comparable
    public int compareTo(Complexd c) {
        return (int) Math.signum(lengthSquared() - c.lengthSquared());
    }

    @Nonnull
    public Complexd clone() {
        return from(this);
    }

    @Nonnull
    public String toString() {
        return "(" + getX() + ", " + getY() + ")";
    }

    @Nonnull
    public static Complexd fromReal(double x) {
        return Imaginary.createComplexd(x, 0.0d);
    }

    @Nonnull
    public static Complexd fromImaginary(double y) {
        return Imaginary.createComplexd(0.0d, y);
    }

    @Nonnull
    public static Complexd from(Complexd c) {
        return from(c.getX(), c.getY());
    }

    @Nonnull
    public static Complexd from(float x, float y) {
        return from(x, y);
    }

    @Nonnull
    public static Complexd from(double x, double y) {
        return Imaginary.createComplexd(x, y);
    }

    @Nonnull
    public static Complexd fromRotationTo(Vector2d from, Vector2d to) {
        return fromAngleRad(TrigMath.acos(from.dot(to) / (from.length() * to.length())));
    }

    @Nonnull
    public static Complexd fromRotationTo(Vector3d from, Vector3d to) {
        return fromAngleRad(TrigMath.acos(from.dot(to) / (from.length() * to.length())));
    }

    @Nonnull
    public static Complexd fromAngleDeg(float angle) {
        return fromAngleRad(Math.toRadians(angle));
    }

    @Nonnull
    public static Complexd fromAngleRad(float angle) {
        return fromAngleRad(angle);
    }

    @Nonnull
    public static Complexd fromAngleDeg(double angle) {
        return fromAngleRad(Math.toRadians(angle));
    }

    @Nonnull
    public static Complexd fromAngleRad(double angle) {
        return from(TrigMath.cos(angle), TrigMath.sin(angle));
    }
}
