package org.cloudburstmc.math.immutable.vector;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.cloudburstmc.math.GenericMath;
import org.cloudburstmc.math.vector.Vector3d;

@ParametersAreNonnullByDefault
/* loaded from: classes5.dex */
public class ImmutableVector3d extends Vector3d {
    private static final long serialVersionUID = 1;
    private final double x;
    private final double y;
    private final double z;
    private volatile transient boolean hashed = false;
    private volatile transient int hashCode = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ImmutableVector3d(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override // org.cloudburstmc.math.vector.Vector3d
    public double getX() {
        return this.x;
    }

    @Override // org.cloudburstmc.math.vector.Vector3d
    public double getY() {
        return this.y;
    }

    @Override // org.cloudburstmc.math.vector.Vector3d
    public double getZ() {
        return this.z;
    }

    @Override // org.cloudburstmc.math.vector.Vector3d
    @Nonnull
    public Vector3d add(double x, double y, double z) {
        return Vector3d.from(getX() + x, getY() + y, getZ() + z);
    }

    @Override // org.cloudburstmc.math.vector.Vector3d
    @Nonnull
    public Vector3d sub(double x, double y, double z) {
        return Vector3d.from(getX() - x, getY() - y, getZ() - z);
    }

    @Override // org.cloudburstmc.math.vector.Vector3d
    @Nonnull
    public Vector3d mul(double x, double y, double z) {
        return Vector3d.from(getX() * x, getY() * y, getZ() * z);
    }

    @Override // org.cloudburstmc.math.vector.Vector3d
    @Nonnull
    public Vector3d div(double x, double y, double z) {
        return Vector3d.from(getX() / x, getY() / y, getZ() / z);
    }

    @Override // org.cloudburstmc.math.vector.Vector3d
    @Nonnull
    public Vector3d project(double x, double y, double z) {
        double lengthSquared = (x * x) + (y * y) + (z * z);
        if (Math.abs(lengthSquared) < GenericMath.DBL_EPSILON) {
            throw new ArithmeticException("Cannot project onto the zero vector");
        }
        double a = dot(x, y, z) / lengthSquared;
        return Vector3d.from(a * x, a * y, a * z);
    }

    @Override // org.cloudburstmc.math.vector.Vector3d
    @Nonnull
    public Vector3d cross(double x, double y, double z) {
        return Vector3d.from((getY() * z) - (getZ() * y), (getZ() * x) - (getX() * z), (getX() * y) - (getY() * x));
    }

    @Override // org.cloudburstmc.math.vector.Vector3d, org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public Vector3d pow(double power) {
        return Vector3d.from(Math.pow(this.x, power), Math.pow(this.y, power), Math.pow(this.z, power));
    }

    @Override // org.cloudburstmc.math.vector.Vector3d, org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public Vector3d ceil() {
        return Vector3d.from(Math.ceil(getX()), Math.ceil(getY()), Math.ceil(getZ()));
    }

    @Override // org.cloudburstmc.math.vector.Vector3d, org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public Vector3d floor() {
        return Vector3d.from(GenericMath.floor(getX()), GenericMath.floor(getY()), GenericMath.floor(getZ()));
    }

    @Override // org.cloudburstmc.math.vector.Vector3d, org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public Vector3d round() {
        return Vector3d.from((float) Math.round(getX()), (float) Math.round(getY()), (float) Math.round(getZ()));
    }

    @Override // org.cloudburstmc.math.vector.Vector3d, org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public Vector3d abs() {
        return Vector3d.from(Math.abs(getX()), Math.abs(getY()), Math.abs(getZ()));
    }

    @Override // org.cloudburstmc.math.vector.Vector3d, org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public Vector3d negate() {
        return Vector3d.from(-getX(), -getY(), -getZ());
    }

    @Override // org.cloudburstmc.math.vector.Vector3d
    @Nonnull
    public Vector3d min(double x, double y, double z) {
        return Vector3d.from(Math.min(getX(), x), Math.min(getY(), y), Math.min(getZ(), z));
    }

    @Override // org.cloudburstmc.math.vector.Vector3d
    @Nonnull
    public Vector3d max(double x, double y, double z) {
        return Vector3d.from(Math.max(getX(), x), Math.max(getY(), y), Math.max(getZ(), z));
    }

    @Override // org.cloudburstmc.math.vector.Vector3d
    @Nonnull
    public Vector3d up(double v) {
        return Vector3d.from(getX(), getY() + v, getZ());
    }

    @Override // org.cloudburstmc.math.vector.Vector3d
    @Nonnull
    public Vector3d down(double v) {
        return Vector3d.from(getX(), getY() - v, getZ());
    }

    @Override // org.cloudburstmc.math.vector.Vector3d
    @Nonnull
    public Vector3d north(double v) {
        return Vector3d.from(getX(), getY(), getZ() - v);
    }

    @Override // org.cloudburstmc.math.vector.Vector3d
    @Nonnull
    public Vector3d south(double v) {
        return Vector3d.from(getX(), getY(), getZ() + v);
    }

    @Override // org.cloudburstmc.math.vector.Vector3d
    @Nonnull
    public Vector3d east(double v) {
        return Vector3d.from(getX() + v, getY(), getZ());
    }

    @Override // org.cloudburstmc.math.vector.Vector3d
    @Nonnull
    public Vector3d west(double v) {
        return Vector3d.from(getX() - v, getY(), getZ());
    }

    @Override // org.cloudburstmc.math.vector.Vector3d, org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public Vector3d normalize() {
        double length = length();
        if (Math.abs(length) < GenericMath.DBL_EPSILON) {
            throw new ArithmeticException("Cannot normalize the zero vector");
        }
        return Vector3d.from(getX() / length, getY() / length, getZ() / length);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vector3d)) {
            return false;
        }
        Vector3d vector3 = (Vector3d) o;
        return Double.compare(vector3.getX(), this.x) == 0 && Double.compare(vector3.getY(), this.y) == 0 && Double.compare(vector3.getZ(), this.z) == 0;
    }

    public int hashCode() {
        if (!this.hashed) {
            int result = this.x != 0.0d ? Double.hashCode(this.x) : 0;
            this.hashCode = (((result * 31) + (this.y != 0.0d ? Double.hashCode(this.y) : 0)) * 31) + (this.z != 0.0d ? Double.hashCode(this.z) : 0);
            this.hashed = true;
        }
        return this.hashCode;
    }
}
