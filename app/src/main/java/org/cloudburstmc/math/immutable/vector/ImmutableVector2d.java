package org.cloudburstmc.math.immutable.vector;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.cloudburstmc.math.GenericMath;
import org.cloudburstmc.math.vector.Vector2d;

@ParametersAreNonnullByDefault
/* loaded from: classes5.dex */
public class ImmutableVector2d extends Vector2d {
    private static final long serialVersionUID = 1;
    private final double x;
    private final double y;
    private volatile transient boolean hashed = false;
    private volatile transient int hashCode = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ImmutableVector2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override // org.cloudburstmc.math.vector.Vector2d
    public double getX() {
        return this.x;
    }

    @Override // org.cloudburstmc.math.vector.Vector2d
    public double getY() {
        return this.y;
    }

    @Override // org.cloudburstmc.math.vector.Vector2d
    @Nonnull
    public Vector2d add(double x, double y) {
        return Vector2d.from(getX() + x, getY() + y);
    }

    @Override // org.cloudburstmc.math.vector.Vector2d
    @Nonnull
    public Vector2d sub(double x, double y) {
        return Vector2d.from(getX() - x, getY() - y);
    }

    @Override // org.cloudburstmc.math.vector.Vector2d
    @Nonnull
    public Vector2d mul(double x, double y) {
        return Vector2d.from(getX() * x, getY() * y);
    }

    @Override // org.cloudburstmc.math.vector.Vector2d
    @Nonnull
    public Vector2d div(double x, double y) {
        return Vector2d.from(getX() / x, getY() / y);
    }

    @Override // org.cloudburstmc.math.vector.Vector2d
    @Nonnull
    public Vector2d project(double x, double y) {
        double lengthSquared = (x * x) + (y * y);
        if (Math.abs(lengthSquared) < GenericMath.DBL_EPSILON) {
            throw new ArithmeticException("Cannot project onto the zero vector");
        }
        double a = dot(x, y) / lengthSquared;
        return Vector2d.from(a * x, a * y);
    }

    @Override // org.cloudburstmc.math.vector.Vector2d, org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public Vector2d pow(double power) {
        return Vector2d.from(Math.pow(getX(), power), Math.pow(getY(), power));
    }

    @Override // org.cloudburstmc.math.vector.Vector2d, org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public Vector2d ceil() {
        return Vector2d.from(Math.ceil(getX()), Math.ceil(getY()));
    }

    @Override // org.cloudburstmc.math.vector.Vector2d, org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public Vector2d floor() {
        return Vector2d.from(GenericMath.floor(getX()), GenericMath.floor(getY()));
    }

    @Override // org.cloudburstmc.math.vector.Vector2d, org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public Vector2d round() {
        return Vector2d.from((float) Math.round(getX()), (float) Math.round(getY()));
    }

    @Override // org.cloudburstmc.math.vector.Vector2d, org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public Vector2d abs() {
        return Vector2d.from(Math.abs(getX()), Math.abs(getY()));
    }

    @Override // org.cloudburstmc.math.vector.Vector2d, org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public Vector2d negate() {
        return Vector2d.from(-getX(), -getY());
    }

    @Override // org.cloudburstmc.math.vector.Vector2d
    @Nonnull
    public Vector2d min(double x, double y) {
        return Vector2d.from(Math.min(getX(), x), Math.min(getY(), y));
    }

    @Override // org.cloudburstmc.math.vector.Vector2d
    @Nonnull
    public Vector2d max(double x, double y) {
        return Vector2d.from(Math.max(getX(), x), Math.max(getY(), y));
    }

    @Override // org.cloudburstmc.math.vector.Vector2d
    @Nonnull
    public Vector2d north(double v) {
        return Vector2d.from(getX(), getY() - v);
    }

    @Override // org.cloudburstmc.math.vector.Vector2d
    @Nonnull
    public Vector2d south(double v) {
        return Vector2d.from(getX(), getY() + v);
    }

    @Override // org.cloudburstmc.math.vector.Vector2d
    @Nonnull
    public Vector2d east(double v) {
        return Vector2d.from(getX() + v, getY());
    }

    @Override // org.cloudburstmc.math.vector.Vector2d
    @Nonnull
    public Vector2d west(double v) {
        return Vector2d.from(getX() - v, getY());
    }

    @Override // org.cloudburstmc.math.vector.Vector2d, org.cloudburstmc.math.vector.Vectord
    @Nonnull
    public Vector2d normalize() {
        double length = length();
        if (Math.abs(length) < GenericMath.DBL_EPSILON) {
            throw new ArithmeticException("Cannot normalize the zero vector");
        }
        return Vector2d.from(getX() / length, getY() / length);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vector2d)) {
            return false;
        }
        Vector2d vector2 = (Vector2d) o;
        return Double.compare(vector2.getX(), this.x) == 0 && Double.compare(vector2.getY(), this.y) == 0;
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
