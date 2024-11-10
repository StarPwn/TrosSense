package org.cloudburstmc.math.imaginary;

import java.io.Serializable;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.cloudburstmc.math.GenericMath;
import org.cloudburstmc.math.TrigMath;
import org.cloudburstmc.math.matrix.Matrix3f;
import org.cloudburstmc.math.vector.Vector3f;

@ParametersAreNonnullByDefault
/* loaded from: classes5.dex */
public abstract class Quaternionf implements Imaginaryf, Comparable<Quaternionf>, Serializable, Cloneable {
    public static final Quaternionf ZERO = from(0.0f, 0.0f, 0.0f, 0.0f);
    public static final Quaternionf IDENTITY = from(0.0f, 0.0f, 0.0f, 1.0f);

    @Nonnull
    public abstract Quaternionf add(float f, float f2, float f3, float f4);

    @Override // org.cloudburstmc.math.imaginary.Imaginaryf
    @Nonnull
    public abstract Quaternionf conjugate();

    @Override // org.cloudburstmc.math.imaginary.Imaginaryf
    @Nonnull
    public abstract Quaternionf div(float f);

    @Nonnull
    public abstract Quaternionf div(float f, float f2, float f3, float f4);

    public abstract float getW();

    public abstract float getX();

    public abstract float getY();

    public abstract float getZ();

    @Override // org.cloudburstmc.math.imaginary.Imaginaryf
    @Nonnull
    public abstract Quaternionf invert();

    @Override // org.cloudburstmc.math.imaginary.Imaginaryf
    @Nonnull
    public abstract Quaternionf mul(float f);

    @Nonnull
    public abstract Quaternionf mul(float f, float f2, float f3, float f4);

    @Override // org.cloudburstmc.math.imaginary.Imaginaryf
    @Nonnull
    public abstract Quaternionf normalize();

    @Nonnull
    public abstract Quaternionf sub(float f, float f2, float f3, float f4);

    @Nonnull
    public Quaternionf add(Quaternionf q) {
        return add(q.getX(), q.getY(), q.getZ(), q.getW());
    }

    @Nonnull
    public Quaternionf add(double x, double y, double z, double w) {
        return add((float) x, (float) y, (float) z, (float) w);
    }

    @Nonnull
    public Quaternionf sub(Quaternionf q) {
        return sub(q.getX(), q.getY(), q.getZ(), q.getW());
    }

    @Nonnull
    public Quaternionf sub(double x, double y, double z, double w) {
        return sub((float) x, (float) y, (float) z, (float) w);
    }

    @Nonnull
    public Quaternionf mul(double a) {
        return mul((float) a);
    }

    @Nonnull
    public Quaternionf mul(Quaternionf q) {
        return mul(q.getX(), q.getY(), q.getZ(), q.getW());
    }

    @Nonnull
    public Quaternionf mul(double x, double y, double z, double w) {
        return mul((float) x, (float) y, (float) z, (float) w);
    }

    @Nonnull
    public Quaternionf div(double a) {
        return div((float) a);
    }

    @Nonnull
    public Quaternionf div(Quaternionf q) {
        return div(q.getX(), q.getY(), q.getZ(), q.getW());
    }

    @Nonnull
    public Quaternionf div(double x, double y, double z, double w) {
        return div((float) x, (float) y, (float) z, (float) w);
    }

    public float dot(Quaternionf q) {
        return dot(q.getX(), q.getY(), q.getZ(), q.getW());
    }

    public float dot(double x, double y, double z, double w) {
        return dot((float) x, (float) y, (float) z, (float) w);
    }

    public float dot(float x, float y, float z, float w) {
        return (getX() * x) + (getY() * y) + (getZ() * z) + (getW() * w);
    }

    @Nonnull
    public Vector3f rotate(Vector3f v) {
        return rotate(v.getX(), v.getY(), v.getZ());
    }

    @Nonnull
    public Vector3f rotate(double x, double y, double z) {
        return rotate((float) x, (float) y, (float) z);
    }

    @Nonnull
    public Vector3f rotate(float x, float y, float z) {
        float length = length();
        if (Math.abs(length) < GenericMath.FLT_EPSILON) {
            throw new ArithmeticException("Cannot rotate by the zero quaternion");
        }
        float nx = getX() / length;
        float ny = getY() / length;
        float nz = getZ() / length;
        float nw = getW() / length;
        float px = ((nw * x) + (ny * z)) - (nz * y);
        float py = ((nw * y) + (nz * x)) - (nx * z);
        float pz = ((nw * z) + (nx * y)) - (ny * x);
        float pw = (((-nx) * x) - (ny * y)) - (nz * z);
        return Vector3f.from(((((-nx) * pw) + (px * nw)) - (py * nz)) + (pz * ny), ((((-ny) * pw) + (py * nw)) - (pz * nx)) + (px * nz), ((((-nz) * pw) + (pz * nw)) - (px * ny)) + (py * nx));
    }

    @Nonnull
    public Vector3f getDirection() {
        return rotate(Vector3f.FORWARD);
    }

    @Nonnull
    public Vector3f getAxis() {
        float q = (float) Math.sqrt(1.0f - (getW() * getW()));
        return Vector3f.from(getX() / q, getY() / q, getZ() / q);
    }

    @Nonnull
    public Vector3f getAxesAnglesDeg() {
        return getAxesAnglesRad().mul(57.29577951308232d);
    }

    @Nonnull
    public Vector3f getAxesAnglesRad() {
        double roll;
        double pitch;
        double yaw;
        double test = (getW() * getX()) - (getY() * getZ());
        if (Math.abs(test) < 0.4999d) {
            roll = TrigMath.atan2(((getW() * getZ()) + (getX() * getY())) * 2.0f, 1.0f - (((getX() * getX()) + (getZ() * getZ())) * 2.0f));
            pitch = TrigMath.asin(2.0d * test);
            yaw = TrigMath.atan2(((getW() * getY()) + (getZ() * getX())) * 2.0f, 1.0f - (((getX() * getX()) + (getY() * getY())) * 2.0f));
        } else {
            int sign = test < 0.0d ? -1 : 1;
            roll = 0.0d;
            double pitch2 = (sign * 3.141592653589793d) / 2.0d;
            double atan2 = (-sign) * 2 * TrigMath.atan2(getZ(), getW());
            pitch = pitch2;
            yaw = atan2;
        }
        return Vector3f.from(pitch, yaw, roll);
    }

    @Override // org.cloudburstmc.math.imaginary.Imaginaryf
    public float lengthSquared() {
        return (getX() * getX()) + (getY() * getY()) + (getZ() * getZ()) + (getW() * getW());
    }

    @Override // org.cloudburstmc.math.imaginary.Imaginaryf
    public float length() {
        return (float) Math.sqrt(lengthSquared());
    }

    @Nonnull
    public Complexf toComplex() {
        float w2 = getW() * getW();
        return Complexf.from((w2 * 2.0f) - 1.0f, getW() * 2.0f * ((float) Math.sqrt(1.0f - w2)));
    }

    @Override // org.cloudburstmc.math.imaginary.Imaginaryf
    @Nonnull
    public Quaternionf toFloat() {
        return from(getX(), getY(), getZ(), getW());
    }

    @Override // org.cloudburstmc.math.imaginary.Imaginaryf
    @Nonnull
    public Quaterniond toDouble() {
        return Quaterniond.from(getX(), getY(), getZ(), getW());
    }

    @Override // java.lang.Comparable
    public int compareTo(Quaternionf q) {
        return (int) Math.signum(lengthSquared() - q.lengthSquared());
    }

    @Nonnull
    public Quaternionf clone() {
        return from(this);
    }

    @Nonnull
    public String toString() {
        return "(" + getX() + ", " + getY() + ", " + getZ() + ", " + getW() + ")";
    }

    @Nonnull
    public static Quaternionf fromReal(float w) {
        return Imaginary.createQuaternionf(0.0f, 0.0f, 0.0f, w);
    }

    @Nonnull
    public static Quaternionf fromImaginary(float x, float y, float z) {
        return Imaginary.createQuaternionf(x, y, z, 0.0f);
    }

    public static Quaternionf from(Quaternionf q) {
        return from(q.getX(), q.getY(), q.getZ(), q.getW());
    }

    @Nonnull
    public static Quaternionf from(double x, double y, double z, double w) {
        return from((float) x, (float) y, (float) z, (float) w);
    }

    @Nonnull
    public static Quaternionf from(float x, float y, float z, float w) {
        return Imaginary.createQuaternionf(x, y, z, w);
    }

    @Nonnull
    public static Quaternionf fromAxesAnglesDeg(double pitch, double yaw, double roll) {
        return fromAxesAnglesDeg((float) pitch, (float) yaw, (float) roll);
    }

    @Nonnull
    public static Quaternionf fromAxesAnglesRad(double pitch, double yaw, double roll) {
        return fromAxesAnglesRad((float) pitch, (float) yaw, (float) roll);
    }

    @Nonnull
    public static Quaternionf fromAxesAnglesDeg(float pitch, float yaw, float roll) {
        return fromAngleDegAxis(yaw, Vector3f.UNIT_Y).mul(fromAngleDegAxis(pitch, Vector3f.UNIT_X)).mul(fromAngleDegAxis(roll, Vector3f.UNIT_Z));
    }

    @Nonnull
    public static Quaternionf fromAxesAnglesRad(float pitch, float yaw, float roll) {
        return fromAngleRadAxis(yaw, Vector3f.UNIT_Y).mul(fromAngleRadAxis(pitch, Vector3f.UNIT_X)).mul(fromAngleRadAxis(roll, Vector3f.UNIT_Z));
    }

    @Nonnull
    public static Quaternionf fromRotationTo(Vector3f from, Vector3f to) {
        return fromAngleRadAxis(TrigMath.acos(from.dot(to) / (from.length() * to.length())), from.cross(to));
    }

    @Nonnull
    public static Quaternionf fromAngleDegAxis(double angle, Vector3f axis) {
        return fromAngleRadAxis(Math.toRadians(angle), axis);
    }

    @Nonnull
    public static Quaternionf fromAngleRadAxis(double angle, Vector3f axis) {
        return fromAngleRadAxis((float) angle, axis);
    }

    @Nonnull
    public static Quaternionf fromAngleDegAxis(float angle, Vector3f axis) {
        return fromAngleRadAxis((float) Math.toRadians(angle), axis);
    }

    @Nonnull
    public static Quaternionf fromAngleRadAxis(float angle, Vector3f axis) {
        return fromAngleRadAxis(angle, axis.getX(), axis.getY(), axis.getZ());
    }

    @Nonnull
    public static Quaternionf fromAngleDegAxis(double angle, double x, double y, double z) {
        return fromAngleRadAxis(Math.toRadians(angle), x, y, z);
    }

    @Nonnull
    public static Quaternionf fromAngleRadAxis(double angle, double x, double y, double z) {
        return fromAngleRadAxis((float) angle, (float) x, (float) y, (float) z);
    }

    @Nonnull
    public static Quaternionf fromAngleDegAxis(float angle, float x, float y, float z) {
        return fromAngleRadAxis((float) Math.toRadians(angle), x, y, z);
    }

    @Nonnull
    public static Quaternionf fromAngleRadAxis(float angle, float x, float y, float z) {
        float halfAngle = angle / 2.0f;
        float q = TrigMath.sin(halfAngle) / ((float) Math.sqrt(((x * x) + (y * y)) + (z * z)));
        return from(x * q, y * q, z * q, TrigMath.cos(halfAngle));
    }

    @Nonnull
    public static Quaternionf fromRotationMatrix(Matrix3f matrix) {
        float trace = matrix.trace();
        if (trace >= 0.0f) {
            float r = (float) Math.sqrt(1.0f + trace);
            float s = 0.5f / r;
            return from((matrix.get(2, 1) - matrix.get(1, 2)) * s, (matrix.get(0, 2) - matrix.get(2, 0)) * s, (matrix.get(1, 0) - matrix.get(0, 1)) * s, 0.5f * r);
        }
        if (matrix.get(1, 1) > matrix.get(0, 0)) {
            if (matrix.get(2, 2) > matrix.get(1, 1)) {
                float r2 = (float) Math.sqrt(((matrix.get(2, 2) - matrix.get(0, 0)) - matrix.get(1, 1)) + 1.0f);
                float s2 = 0.5f / r2;
                return from((matrix.get(2, 0) + matrix.get(0, 2)) * s2, (matrix.get(1, 2) + matrix.get(2, 1)) * s2, 0.5f * r2, (matrix.get(1, 0) - matrix.get(0, 1)) * s2);
            }
            float r3 = (float) Math.sqrt(((matrix.get(1, 1) - matrix.get(2, 2)) - matrix.get(0, 0)) + 1.0f);
            float s3 = 0.5f / r3;
            return from((matrix.get(0, 1) + matrix.get(1, 0)) * s3, 0.5f * r3, (matrix.get(1, 2) + matrix.get(2, 1)) * s3, (matrix.get(0, 2) - matrix.get(2, 0)) * s3);
        }
        if (matrix.get(2, 2) > matrix.get(0, 0)) {
            float r4 = (float) Math.sqrt(((matrix.get(2, 2) - matrix.get(0, 0)) - matrix.get(1, 1)) + 1.0f);
            float s4 = 0.5f / r4;
            return from((matrix.get(2, 0) + matrix.get(0, 2)) * s4, (matrix.get(1, 2) + matrix.get(2, 1)) * s4, 0.5f * r4, (matrix.get(1, 0) - matrix.get(0, 1)) * s4);
        }
        float r5 = (float) Math.sqrt(((matrix.get(0, 0) - matrix.get(1, 1)) - matrix.get(2, 2)) + 1.0f);
        float s5 = 0.5f / r5;
        return from(0.5f * r5, (matrix.get(0, 1) + matrix.get(1, 0)) * s5, (matrix.get(2, 0) - matrix.get(0, 2)) * s5, (matrix.get(2, 1) - matrix.get(1, 2)) * s5);
    }
}
