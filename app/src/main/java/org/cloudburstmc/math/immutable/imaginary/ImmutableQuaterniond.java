package org.cloudburstmc.math.immutable.imaginary;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.cloudburstmc.math.GenericMath;
import org.cloudburstmc.math.imaginary.Quaterniond;

@ParametersAreNonnullByDefault
/* loaded from: classes5.dex */
public class ImmutableQuaterniond extends Quaterniond {
    private static final long serialVersionUID = 1;
    private final double w;
    private final double x;
    private final double y;
    private final double z;
    private volatile transient boolean hashed = false;
    private volatile transient int hashCode = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ImmutableQuaterniond(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    @Override // org.cloudburstmc.math.imaginary.Quaterniond
    public double getX() {
        return this.x;
    }

    @Override // org.cloudburstmc.math.imaginary.Quaterniond
    public double getY() {
        return this.y;
    }

    @Override // org.cloudburstmc.math.imaginary.Quaterniond
    public double getZ() {
        return this.z;
    }

    @Override // org.cloudburstmc.math.imaginary.Quaterniond
    public double getW() {
        return this.w;
    }

    @Override // org.cloudburstmc.math.imaginary.Quaterniond
    @Nonnull
    public Quaterniond add(double x, double y, double z, double w) {
        return Quaterniond.from(this.x + x, this.y + y, this.z + z, this.w + w);
    }

    @Override // org.cloudburstmc.math.imaginary.Quaterniond
    @Nonnull
    public Quaterniond sub(double x, double y, double z, double w) {
        return Quaterniond.from(this.x - x, this.y - y, this.z - z, this.w - w);
    }

    @Override // org.cloudburstmc.math.imaginary.Quaterniond, org.cloudburstmc.math.imaginary.Imaginaryd
    @Nonnull
    public Quaterniond mul(double a) {
        return Quaterniond.from(this.x * a, this.y * a, this.z * a, this.w * a);
    }

    @Override // org.cloudburstmc.math.imaginary.Quaterniond
    @Nonnull
    public Quaterniond mul(double x, double y, double z, double w) {
        return Quaterniond.from((((this.w * x) + (this.x * w)) + (this.y * z)) - (this.z * y), (((this.w * y) + (this.y * w)) + (this.z * x)) - (this.x * z), (((this.w * z) + (this.z * w)) + (this.x * y)) - (this.y * x), (((this.w * w) - (this.x * x)) - (this.y * y)) - (this.z * z));
    }

    @Override // org.cloudburstmc.math.imaginary.Quaterniond, org.cloudburstmc.math.imaginary.Imaginaryd
    @Nonnull
    public Quaterniond div(double a) {
        return Quaterniond.from(this.x / a, this.y / a, this.z / a, this.w / a);
    }

    @Override // org.cloudburstmc.math.imaginary.Quaterniond
    @Nonnull
    public Quaterniond div(double x, double y, double z, double w) {
        double d = (x * x) + (y * y) + (z * z) + (w * w);
        return Quaterniond.from(((((this.x * w) - (this.w * x)) - (this.z * y)) + (this.y * z)) / d, ((((this.y * w) + (this.z * x)) - (this.w * y)) - (this.x * z)) / d, ((((this.z * w) - (this.y * x)) + (this.x * y)) - (this.w * z)) / d, ((((this.w * w) + (this.x * x)) + (this.y * y)) + (this.z * z)) / d);
    }

    @Override // org.cloudburstmc.math.imaginary.Quaterniond, org.cloudburstmc.math.imaginary.Imaginaryd
    @Nonnull
    public Quaterniond conjugate() {
        return Quaterniond.from(-this.x, -this.y, -this.z, this.w);
    }

    @Override // org.cloudburstmc.math.imaginary.Quaterniond, org.cloudburstmc.math.imaginary.Imaginaryd
    @Nonnull
    public Quaterniond invert() {
        double lengthSquared = lengthSquared();
        if (Math.abs(lengthSquared) < GenericMath.DBL_EPSILON) {
            throw new ArithmeticException("Cannot invert a quaternion of length zero");
        }
        return conjugate().div(lengthSquared);
    }

    @Override // org.cloudburstmc.math.imaginary.Quaterniond, org.cloudburstmc.math.imaginary.Imaginaryd
    @Nonnull
    public Quaterniond normalize() {
        double length = length();
        if (Math.abs(length) < GenericMath.DBL_EPSILON) {
            throw new ArithmeticException("Cannot normalize the zero quaternion");
        }
        return Quaterniond.from(this.x / length, this.y / length, this.z / length, this.w / length);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Quaterniond)) {
            return false;
        }
        Quaterniond quaternion = (Quaterniond) o;
        return Double.compare(quaternion.getW(), this.w) == 0 && Double.compare(quaternion.getX(), this.x) == 0 && Double.compare(quaternion.getY(), this.y) == 0 && Double.compare(quaternion.getZ(), this.z) == 0;
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
