package org.cloudburstmc.math.immutable.vector;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.cloudburstmc.math.GenericMath;
import org.cloudburstmc.math.vector.Vector4d;

@ParametersAreNonnullByDefault
/* loaded from: classes5.dex */
public class ImmutableVector4d extends Vector4d {
    private static final long serialVersionUID = 1;
    private final double w;
    private final double x;
    private final double y;
    private final double z;
    private volatile transient boolean hashed = false;
    private volatile transient int hashCode = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ImmutableVector4d(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    @Override // org.cloudburstmc.math.vector.Vector4d
    public double getX() {
        return this.x;
    }

    @Override // org.cloudburstmc.math.vector.Vector4d
    public double getY() {
        return this.y;
    }

    @Override // org.cloudburstmc.math.vector.Vector4d
    public double getZ() {
        return this.z;
    }

    @Override // org.cloudburstmc.math.vector.Vector4d
    public double getW() {
        return this.w;
    }

    @Override // org.cloudburstmc.math.vector.Vector4d
    @Nonnull
    public Vector4d add(double x, double y, double z, double w) {
        return Vector4d.from(getX() + x, getY() + y, getZ() + z, getW() + w);
    }

    @Override // org.cloudburstmc.math.vector.Vector4d
    @Nonnull
    public Vector4d sub(double x, double y, double z, double w) {
        return Vector4d.from(getX() - x, getY() - y, getZ() - z, getW() - w);
    }

    @Override // org.cloudburstmc.math.vector.Vector4d
    @Nonnull
    public Vector4d mul(double x, double y, double z, double w) {
        return Vector4d.from(getX() * x, getY() * y, getZ() * z, getW() * w);
    }

    @Override // org.cloudburstmc.math.vector.Vector4d
    @Nonnull
    public Vector4d div(double x, double y, double z, double w) {
        return Vector4d.from(getX() / x, getY() / y, getZ() / z, getW() / w);
    }

    @Override // org.cloudburstmc.math.vector.Vector4d
    @Nonnull
    public Vector4d project(double x, double y, double z, double w) {
        double lengthSquared = (x * x) + (y * y) + (z * z) + (w * w);
        if (Math.abs(lengthSquared) < GenericMath.DBL_EPSILON) {
            throw new ArithmeticException("Cannot project onto the zero vector");
        }
        double a = dot(x, y, z, w) / lengthSquared;
        return Vector4d.from(a * x, a * y, a * z, a * w);
    }

    @Override // org.cloudburstmc.math.vector.Vector4d, org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public Vector4d pow(double power) {
        return Vector4d.from(Math.pow(getX(), power), Math.pow(getY(), power), Math.pow(getZ(), power), Math.pow(getW(), power));
    }

    @Override // org.cloudburstmc.math.vector.Vector4d, org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public Vector4d ceil() {
        return Vector4d.from(Math.ceil(getX()), Math.ceil(getY()), Math.ceil(getZ()), Math.ceil(getW()));
    }

    @Override // org.cloudburstmc.math.vector.Vector4d, org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public Vector4d floor() {
        return Vector4d.from(GenericMath.floor(getX()), GenericMath.floor(getY()), GenericMath.floor(getZ()), GenericMath.floor(getW()));
    }

    @Override // org.cloudburstmc.math.vector.Vector4d, org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public Vector4d round() {
        return Vector4d.from((float) Math.round(getX()), (float) Math.round(getY()), (float) Math.round(getZ()), (float) Math.round(getW()));
    }

    @Override // org.cloudburstmc.math.vector.Vector4d, org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public Vector4d abs() {
        return Vector4d.from(Math.abs(getX()), Math.abs(getY()), Math.abs(getZ()), Math.abs(getW()));
    }

    @Override // org.cloudburstmc.math.vector.Vector4d, org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public Vector4d negate() {
        return Vector4d.from(-getX(), -getY(), -getZ(), -getW());
    }

    @Override // org.cloudburstmc.math.vector.Vector4d
    @Nonnull
    public Vector4d min(double x, double y, double z, double w) {
        return Vector4d.from(Math.min(getX(), x), Math.min(getY(), y), Math.min(getZ(), z), Math.min(getW(), w));
    }

    @Override // org.cloudburstmc.math.vector.Vector4d
    @Nonnull
    public Vector4d max(double x, double y, double z, double w) {
        return Vector4d.from(Math.max(getX(), x), Math.max(getY(), y), Math.max(getZ(), z), Math.max(getW(), w));
    }

    @Override // org.cloudburstmc.math.vector.Vector4d, org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public Vector4d normalize() {
        double length = length();
        if (Math.abs(length) < GenericMath.DBL_EPSILON) {
            throw new ArithmeticException("Cannot normalize the zero vector");
        }
        return Vector4d.from(getX() / length, getY() / length, getZ() / length, getW() / length);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vector4d)) {
            return false;
        }
        Vector4d vector4 = (Vector4d) o;
        return Double.compare(vector4.getW(), this.w) == 0 && Double.compare(vector4.getX(), this.x) == 0 && Double.compare(vector4.getY(), this.y) == 0 && Double.compare(vector4.getZ(), this.z) == 0;
    }

    public int hashCode() {
        if (!this.hashed) {
            int result = this.x != 0.0d ? Double.hashCode(this.x) : 0;
            this.hashCode = (((((result * 31) + (this.y != 0.0d ? Double.hashCode(this.y) : 0)) * 31) + (this.z != 0.0d ? Double.hashCode(this.z) : 0)) * 31) + (this.w != 0.0d ? Double.hashCode(this.w) : 0);
            this.hashed = true;
        }
        int result2 = this.hashCode;
        return result2;
    }
}
