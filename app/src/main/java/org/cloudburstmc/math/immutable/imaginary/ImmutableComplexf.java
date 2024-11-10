package org.cloudburstmc.math.immutable.imaginary;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.cloudburstmc.math.GenericMath;
import org.cloudburstmc.math.imaginary.Complexf;

@ParametersAreNonnullByDefault
/* loaded from: classes5.dex */
public class ImmutableComplexf extends Complexf {
    private static final long serialVersionUID = 1;
    private final float x;
    private final float y;
    private volatile transient boolean hashed = false;
    private volatile transient int hashCode = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ImmutableComplexf(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override // org.cloudburstmc.math.imaginary.Complexf
    public float getX() {
        return this.x;
    }

    @Override // org.cloudburstmc.math.imaginary.Complexf
    public float getY() {
        return this.y;
    }

    @Override // org.cloudburstmc.math.imaginary.Complexf
    @Nonnull
    public Complexf add(float x, float y) {
        return Complexf.from(this.x + x, this.y + y);
    }

    @Override // org.cloudburstmc.math.imaginary.Complexf
    @Nonnull
    public Complexf sub(float x, float y) {
        return Complexf.from(this.x - x, this.y - y);
    }

    @Override // org.cloudburstmc.math.imaginary.Complexf, org.cloudburstmc.math.imaginary.Imaginaryf
    @Nonnull
    public Complexf mul(float a) {
        return Complexf.from(this.x * a, this.y * a);
    }

    @Override // org.cloudburstmc.math.imaginary.Complexf
    @Nonnull
    public Complexf mul(float x, float y) {
        return Complexf.from((this.x * x) - (this.y * y), (this.x * y) + (this.y * x));
    }

    @Override // org.cloudburstmc.math.imaginary.Complexf, org.cloudburstmc.math.imaginary.Imaginaryf
    @Nonnull
    public Complexf div(float a) {
        return Complexf.from(this.x / a, this.y / a);
    }

    @Override // org.cloudburstmc.math.imaginary.Complexf
    @Nonnull
    public Complexf div(float x, float y) {
        float d = (x * x) + (y * y);
        return Complexf.from(((this.x * x) + (this.y * y)) / d, ((this.y * x) - (this.x * y)) / d);
    }

    @Override // org.cloudburstmc.math.imaginary.Complexf
    public float dot(Complexf c) {
        return dot(c.getX(), c.getY());
    }

    @Override // org.cloudburstmc.math.imaginary.Complexf
    public float dot(double x, double y) {
        return dot((float) x, (float) y);
    }

    @Override // org.cloudburstmc.math.imaginary.Complexf
    public float dot(float x, float y) {
        return (this.x * x) + (this.y * y);
    }

    @Override // org.cloudburstmc.math.imaginary.Complexf, org.cloudburstmc.math.imaginary.Imaginaryf
    @Nonnull
    public Complexf conjugate() {
        return Complexf.from(this.x, -this.y);
    }

    @Override // org.cloudburstmc.math.imaginary.Complexf, org.cloudburstmc.math.imaginary.Imaginaryf
    @Nonnull
    public Complexf invert() {
        float lengthSquared = lengthSquared();
        if (Math.abs(lengthSquared) < GenericMath.FLT_EPSILON) {
            throw new ArithmeticException("Cannot invert a complex of length zero");
        }
        return conjugate().div(lengthSquared);
    }

    @Override // org.cloudburstmc.math.imaginary.Complexf, org.cloudburstmc.math.imaginary.Imaginaryf
    @Nonnull
    public Complexf normalize() {
        float length = length();
        if (Math.abs(length) < GenericMath.FLT_EPSILON) {
            throw new ArithmeticException("Cannot normalize the zero complex");
        }
        return Complexf.from(this.x / length, this.y / length);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Complexf)) {
            return false;
        }
        Complexf complex = (Complexf) o;
        return Float.compare(complex.getX(), this.x) == 0 && Float.compare(complex.getY(), this.y) == 0;
    }

    public int hashCode() {
        if (!this.hashed) {
            int result = this.x != 0.0f ? Float.hashCode(this.x) : 0;
            this.hashCode = (result * 31) + (this.y != 0.0f ? Float.hashCode(this.y) : 0);
            this.hashed = true;
        }
        int result2 = this.hashCode;
        return result2;
    }
}
