package org.cloudburstmc.math.imaginary;

import java.io.Serializable;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.cloudburstmc.math.GenericMath;
import org.cloudburstmc.math.TrigMath;
import org.cloudburstmc.math.matrix.Matrix3d;
import org.cloudburstmc.math.vector.Vector3d;

@ParametersAreNonnullByDefault
/* loaded from: classes5.dex */
public abstract class Quaterniond implements Imaginaryd, Comparable<Quaterniond>, Serializable, Cloneable {
    public static final Quaterniond ZERO = from(0.0f, 0.0f, 0.0f, 0.0f);
    public static final Quaterniond IDENTITY = from(0.0f, 0.0f, 0.0f, 1.0f);

    @Nonnull
    public abstract Quaterniond add(double d, double d2, double d3, double d4);

    @Override // org.cloudburstmc.math.imaginary.Imaginaryd
    @Nonnull
    public abstract Quaterniond conjugate();

    @Override // org.cloudburstmc.math.imaginary.Imaginaryd
    @Nonnull
    public abstract Quaterniond div(double d);

    @Nonnull
    public abstract Quaterniond div(double d, double d2, double d3, double d4);

    public abstract double getW();

    public abstract double getX();

    public abstract double getY();

    public abstract double getZ();

    @Override // org.cloudburstmc.math.imaginary.Imaginaryd
    @Nonnull
    public abstract Quaterniond invert();

    @Override // org.cloudburstmc.math.imaginary.Imaginaryd
    @Nonnull
    public abstract Quaterniond mul(double d);

    @Nonnull
    public abstract Quaterniond mul(double d, double d2, double d3, double d4);

    @Override // org.cloudburstmc.math.imaginary.Imaginaryd
    @Nonnull
    public abstract Quaterniond normalize();

    @Nonnull
    public abstract Quaterniond sub(double d, double d2, double d3, double d4);

    @Nonnull
    public Quaterniond add(Quaterniond q) {
        return add(q.getX(), q.getY(), q.getZ(), q.getW());
    }

    @Nonnull
    public Quaterniond add(float x, float y, float z, float w) {
        return add(x, y, z, w);
    }

    @Nonnull
    public Quaterniond sub(Quaterniond q) {
        return sub(q.getX(), q.getY(), q.getZ(), q.getW());
    }

    @Nonnull
    public Quaterniond sub(float x, float y, float z, float w) {
        return sub(x, y, z, w);
    }

    @Nonnull
    public Quaterniond mul(float a) {
        return mul(a);
    }

    @Nonnull
    public Quaterniond mul(Quaterniond q) {
        return mul(q.getX(), q.getY(), q.getZ(), q.getW());
    }

    @Nonnull
    public Quaterniond mul(float x, float y, float z, float w) {
        return mul(x, y, z, w);
    }

    @Nonnull
    public Quaterniond div(float a) {
        return div(a);
    }

    @Nonnull
    public Quaterniond div(Quaterniond q) {
        return div(q.getX(), q.getY(), q.getZ(), q.getW());
    }

    @Nonnull
    public Quaterniond div(float x, float y, float z, float w) {
        return div(x, y, z, w);
    }

    public double dot(Quaterniond q) {
        return dot(q.getX(), q.getY(), q.getZ(), q.getW());
    }

    public double dot(float x, float y, float z, float w) {
        return dot(x, y, z, w);
    }

    public double dot(double x, double y, double z, double w) {
        return (getX() * x) + (getY() * y) + (getZ() * z) + (getW() * w);
    }

    @Nonnull
    public Vector3d rotate(Vector3d v) {
        return rotate(v.getX(), v.getY(), v.getZ());
    }

    @Nonnull
    public Vector3d rotate(float x, float y, float z) {
        return rotate(x, y, z);
    }

    @Nonnull
    public Vector3d rotate(double x, double y, double z) {
        double length = length();
        if (Math.abs(length) < GenericMath.DBL_EPSILON) {
            throw new ArithmeticException("Cannot rotate by the zero quaternion");
        }
        double nx = getX() / length;
        double ny = getY() / length;
        double nz = getZ() / length;
        double nw = getW() / length;
        double px = ((nw * x) + (ny * z)) - (nz * y);
        double py = ((nw * y) + (nz * x)) - (nx * z);
        double pz = ((nw * z) + (nx * y)) - (ny * x);
        double pw = (((-nx) * x) - (ny * y)) - (nz * z);
        return Vector3d.from(((((-nx) * pw) + (px * nw)) - (py * nz)) + (pz * ny), ((((-ny) * pw) + (py * nw)) - (pz * nx)) + (px * nz), ((((-nz) * pw) + (pz * nw)) - (px * ny)) + (py * nx));
    }

    @Nonnull
    public Vector3d getDirection() {
        return rotate(Vector3d.FORWARD);
    }

    @Nonnull
    public Vector3d getAxis() {
        double q = Math.sqrt(1.0d - (getW() * getW()));
        return Vector3d.from(getX() / q, getY() / q, getZ() / q);
    }

    @Nonnull
    public Vector3d getAxesAnglesDeg() {
        return getAxesAnglesRad().mul(57.29577951308232d);
    }

    @Nonnull
    public Vector3d getAxesAnglesRad() {
        double roll;
        double pitch;
        double yaw;
        double test = (getW() * getX()) - (getY() * getZ());
        if (Math.abs(test) < 0.4999d) {
            roll = TrigMath.atan2(((getW() * getZ()) + (getX() * getY())) * 2.0d, 1.0d - (((getX() * getX()) + (getZ() * getZ())) * 2.0d));
            pitch = TrigMath.asin(test * 2.0d);
            yaw = TrigMath.atan2(((getW() * getY()) + (getZ() * getX())) * 2.0d, 1.0d - (((getX() * getX()) + (getY() * getY())) * 2.0d));
        } else {
            int sign = test < 0.0d ? -1 : 1;
            roll = 0.0d;
            pitch = (sign * 3.141592653589793d) / 2.0d;
            yaw = (-sign) * 2 * TrigMath.atan2(getZ(), getW());
        }
        return Vector3d.from(pitch, yaw, roll);
    }

    @Override // org.cloudburstmc.math.imaginary.Imaginaryd
    public double lengthSquared() {
        return (getX() * getX()) + (getY() * getY()) + (getZ() * getZ()) + (getW() * getW());
    }

    @Override // org.cloudburstmc.math.imaginary.Imaginaryd
    public double length() {
        return Math.sqrt(lengthSquared());
    }

    @Nonnull
    public Complexd toComplex() {
        double w2 = getW() * getW();
        return Complexd.from((w2 * 2.0d) - 1.0d, getW() * 2.0d * Math.sqrt(1.0d - w2));
    }

    @Override // org.cloudburstmc.math.imaginary.Imaginaryd
    @Nonnull
    public Quaternionf toFloat() {
        return Quaternionf.from(getX(), getY(), getZ(), getW());
    }

    @Override // org.cloudburstmc.math.imaginary.Imaginaryd
    @Nonnull
    public Quaterniond toDouble() {
        return from(getX(), getY(), getZ(), getW());
    }

    @Override // java.lang.Comparable
    public int compareTo(Quaterniond q) {
        return (int) Math.signum(lengthSquared() - q.lengthSquared());
    }

    @Nonnull
    public Quaterniond clone() {
        return from(this);
    }

    @Nonnull
    public String toString() {
        return "(" + getX() + ", " + getY() + ", " + getZ() + ", " + getW() + ")";
    }

    @Nonnull
    public static Quaterniond fromReal(double w) {
        return Imaginary.createQuaterniond(0.0d, 0.0d, 0.0d, w);
    }

    @Nonnull
    public static Quaterniond fromImaginary(double x, double y, double z) {
        return Imaginary.createQuaterniond(x, y, z, 0.0d);
    }

    public static Quaterniond from(Quaterniond q) {
        return from(q.getX(), q.getY(), q.getZ(), q.getW());
    }

    @Nonnull
    public static Quaterniond from(float x, float y, float z, float w) {
        return from(x, y, z, w);
    }

    @Nonnull
    public static Quaterniond from(double x, double y, double z, double w) {
        return Imaginary.createQuaterniond(x, y, z, w);
    }

    @Nonnull
    public static Quaterniond fromAxesAnglesDeg(float pitch, float yaw, float roll) {
        return fromAxesAnglesDeg(pitch, yaw, roll);
    }

    @Nonnull
    public static Quaterniond fromAxesAnglesRad(float pitch, float yaw, float roll) {
        return fromAxesAnglesRad(pitch, yaw, roll);
    }

    @Nonnull
    public static Quaterniond fromAxesAnglesDeg(double pitch, double yaw, double roll) {
        return fromAngleDegAxis(yaw, Vector3d.UNIT_Y).mul(fromAngleDegAxis(pitch, Vector3d.UNIT_X)).mul(fromAngleDegAxis(roll, Vector3d.UNIT_Z));
    }

    @Nonnull
    public static Quaterniond fromAxesAnglesRad(double pitch, double yaw, double roll) {
        return fromAngleRadAxis(yaw, Vector3d.UNIT_Y).mul(fromAngleRadAxis(pitch, Vector3d.UNIT_X)).mul(fromAngleRadAxis(roll, Vector3d.UNIT_Z));
    }

    @Nonnull
    public static Quaterniond fromRotationTo(Vector3d from, Vector3d to) {
        return fromAngleRadAxis(TrigMath.acos(from.dot(to) / (from.length() * to.length())), from.cross(to));
    }

    @Nonnull
    public static Quaterniond fromAngleDegAxis(float angle, Vector3d axis) {
        return fromAngleRadAxis(Math.toRadians(angle), axis);
    }

    @Nonnull
    public static Quaterniond fromAngleRadAxis(float angle, Vector3d axis) {
        return fromAngleRadAxis(angle, axis);
    }

    @Nonnull
    public static Quaterniond fromAngleDegAxis(double angle, Vector3d axis) {
        return fromAngleRadAxis(Math.toRadians(angle), axis);
    }

    @Nonnull
    public static Quaterniond fromAngleRadAxis(double angle, Vector3d axis) {
        return fromAngleRadAxis(angle, axis.getX(), axis.getY(), axis.getZ());
    }

    @Nonnull
    public static Quaterniond fromAngleDegAxis(float angle, float x, float y, float z) {
        return fromAngleRadAxis(Math.toRadians(angle), x, y, z);
    }

    @Nonnull
    public static Quaterniond fromAngleRadAxis(float angle, float x, float y, float z) {
        return fromAngleRadAxis(angle, x, y, z);
    }

    @Nonnull
    public static Quaterniond fromAngleDegAxis(double angle, double x, double y, double z) {
        return fromAngleRadAxis(Math.toRadians(angle), x, y, z);
    }

    @Nonnull
    public static Quaterniond fromAngleRadAxis(double angle, double x, double y, double z) {
        double halfAngle = angle / 2.0d;
        double q = TrigMath.sin(halfAngle) / Math.sqrt(((x * x) + (y * y)) + (z * z));
        return from(x * q, y * q, z * q, TrigMath.cos(halfAngle));
    }

    @Nonnull
    public static Quaterniond fromRotationMatrix(Matrix3d matrix) {
        double trace = matrix.trace();
        if (trace >= 0.0d) {
            double r = Math.sqrt(1.0d + trace);
            double s = 0.5d / r;
            return from((matrix.get(2, 1) - matrix.get(1, 2)) * s, (matrix.get(0, 2) - matrix.get(2, 0)) * s, (matrix.get(1, 0) - matrix.get(0, 1)) * s, r * 0.5d);
        }
        if (matrix.get(1, 1) > matrix.get(0, 0)) {
            if (matrix.get(2, 2) > matrix.get(1, 1)) {
                double r2 = Math.sqrt(((matrix.get(2, 2) - matrix.get(0, 0)) - matrix.get(1, 1)) + 1.0d);
                double s2 = 0.5d / r2;
                return from((matrix.get(2, 0) + matrix.get(0, 2)) * s2, (matrix.get(1, 2) + matrix.get(2, 1)) * s2, r2 * 0.5d, (matrix.get(1, 0) - matrix.get(0, 1)) * s2);
            }
            double r3 = Math.sqrt(((matrix.get(1, 1) - matrix.get(2, 2)) - matrix.get(0, 0)) + 1.0d);
            double s3 = 0.5d / r3;
            return from((matrix.get(0, 1) + matrix.get(1, 0)) * s3, r3 * 0.5d, (matrix.get(1, 2) + matrix.get(2, 1)) * s3, (matrix.get(0, 2) - matrix.get(2, 0)) * s3);
        }
        if (matrix.get(2, 2) > matrix.get(0, 0)) {
            double r4 = Math.sqrt(((matrix.get(2, 2) - matrix.get(0, 0)) - matrix.get(1, 1)) + 1.0d);
            double s4 = 0.5d / r4;
            return from((matrix.get(2, 0) + matrix.get(0, 2)) * s4, (matrix.get(1, 2) + matrix.get(2, 1)) * s4, r4 * 0.5d, (matrix.get(1, 0) - matrix.get(0, 1)) * s4);
        }
        double r5 = Math.sqrt(((matrix.get(0, 0) - matrix.get(1, 1)) - matrix.get(2, 2)) + 1.0d);
        double s5 = 0.5d / r5;
        return from(r5 * 0.5d, (matrix.get(0, 1) + matrix.get(1, 0)) * s5, (matrix.get(2, 0) - matrix.get(0, 2)) * s5, (matrix.get(2, 1) - matrix.get(1, 2)) * s5);
    }
}
