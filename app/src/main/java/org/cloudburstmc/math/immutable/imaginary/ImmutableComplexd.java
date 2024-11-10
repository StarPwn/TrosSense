package org.cloudburstmc.math.immutable.imaginary;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.cloudburstmc.math.GenericMath;
import org.cloudburstmc.math.imaginary.Complexd;

@ParametersAreNonnullByDefault
/* loaded from: classes5.dex */
public class ImmutableComplexd extends Complexd {
    private static final long serialVersionUID = 1;
    private final double x;
    private final double y;
    private volatile transient boolean hashed = false;
    private volatile transient int hashCode = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ImmutableComplexd(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override // org.cloudburstmc.math.imaginary.Complexd
    public double getX() {
        return this.x;
    }

    @Override // org.cloudburstmc.math.imaginary.Complexd
    public double getY() {
        return this.y;
    }

    @Override // org.cloudburstmc.math.imaginary.Complexd
    @Nonnull
    public Complexd add(double x, double y) {
        return Complexd.from(this.x + x, this.y + y);
    }

    @Override // org.cloudburstmc.math.imaginary.Complexd
    @Nonnull
    public Complexd sub(double x, double y) {
        return Complexd.from(this.x - x, this.y - y);
    }

    @Override // org.cloudburstmc.math.imaginary.Complexd, org.cloudburstmc.math.imaginary.Imaginaryd
    @Nonnull
    public Complexd mul(double a) {
        return Complexd.from(this.x * a, this.y * a);
    }

    @Override // org.cloudburstmc.math.imaginary.Complexd
    @Nonnull
    public Complexd mul(double x, double y) {
        return Complexd.from((this.x * x) - (this.y * y), (this.x * y) + (this.y * x));
    }

    @Override // org.cloudburstmc.math.imaginary.Complexd, org.cloudburstmc.math.imaginary.Imaginaryd
    @Nonnull
    public Complexd div(double a) {
        return Complexd.from(this.x / a, this.y / a);
    }

    @Override // org.cloudburstmc.math.imaginary.Complexd
    @Nonnull
    public Complexd div(double x, double y) {
        double d = (x * x) + (y * y);
        return Complexd.from(((this.x * x) + (this.y * y)) / d, ((this.y * x) - (this.x * y)) / d);
    }

    @Override // org.cloudburstmc.math.imaginary.Complexd
    public double dot(Complexd c) {
        return dot(c.getX(), c.getY());
    }

    @Override // org.cloudburstmc.math.imaginary.Complexd
    public double dot(float x, float y) {
        return dot(x, y);
    }

    @Override // org.cloudburstmc.math.imaginary.Complexd
    public double dot(double x, double y) {
        return (this.x * x) + (this.y * y);
    }

    @Override // org.cloudburstmc.math.imaginary.Complexd, org.cloudburstmc.math.imaginary.Imaginaryd
    @Nonnull
    public Complexd conjugate() {
        return Complexd.from(this.x, -this.y);
    }

    @Override // org.cloudburstmc.math.imaginary.Complexd, org.cloudburstmc.math.imaginary.Imaginaryd
    @Nonnull
    public Complexd invert() {
        double lengthSquared = lengthSquared();
        if (Math.abs(lengthSquared) < GenericMath.DBL_EPSILON) {
            throw new ArithmeticException("Cannot invert a complex of length zero");
        }
        return conjugate().div(lengthSquared);
    }

    @Override // org.cloudburstmc.math.imaginary.Complexd, org.cloudburstmc.math.imaginary.Imaginaryd
    @Nonnull
    public Complexd normalize() {
        double length = length();
        if (Math.abs(length) < GenericMath.DBL_EPSILON) {
            throw new ArithmeticException("Cannot normalize the zero complex");
        }
        return Complexd.from(this.x / length, this.y / length);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Complexd)) {
            return false;
        }
        Complexd complex = (Complexd) o;
        return Double.compare(complex.getX(), this.x) == 0 && Double.compare(complex.getY(), this.y) == 0;
    }

    public int hashCode() {
        if (!this.hashed) {
            int result = this.x != 0.0d ? Double.hashCode(this.x) : 0;
            this.hashCode = (result * 31) + (this.y != 0.0d ? Double.hashCode(this.y) : 0);
            this.hashed = true;
        }
        int result2 = this.hashCode;
        return result2;
    }
}
