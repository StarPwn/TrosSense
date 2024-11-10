package org.cloudburstmc.math.immutable.vector;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.cloudburstmc.math.GenericMath;
import org.cloudburstmc.math.vector.Vector2f;

@ParametersAreNonnullByDefault
/* loaded from: classes5.dex */
public class ImmutableVector2f extends Vector2f {
    private static final long serialVersionUID = 1;
    private final float x;
    private final float y;
    private volatile transient boolean hashed = false;
    private volatile transient int hashCode = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ImmutableVector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override // org.cloudburstmc.math.vector.Vector2f
    public float getX() {
        return this.x;
    }

    @Override // org.cloudburstmc.math.vector.Vector2f
    public float getY() {
        return this.y;
    }

    @Override // org.cloudburstmc.math.vector.Vector2f
    @Nonnull
    public Vector2f add(float x, float y) {
        return Vector2f.from(getX() + x, getY() + y);
    }

    @Override // org.cloudburstmc.math.vector.Vector2f
    @Nonnull
    public Vector2f sub(float x, float y) {
        return Vector2f.from(getX() - x, getY() - y);
    }

    @Override // org.cloudburstmc.math.vector.Vector2f
    @Nonnull
    public Vector2f mul(float x, float y) {
        return Vector2f.from(getX() * x, getY() * y);
    }

    @Override // org.cloudburstmc.math.vector.Vector2f
    @Nonnull
    public Vector2f div(float x, float y) {
        return Vector2f.from(getX() / x, getY() / y);
    }

    @Override // org.cloudburstmc.math.vector.Vector2f
    @Nonnull
    public Vector2f project(float x, float y) {
        float lengthSquared = (x * x) + (y * y);
        if (Math.abs(lengthSquared) < GenericMath.FLT_EPSILON) {
            throw new ArithmeticException("Cannot project onto the zero vector");
        }
        float a = dot(x, y) / lengthSquared;
        return Vector2f.from(a * x, a * y);
    }

    @Override // org.cloudburstmc.math.vector.Vector2f, org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public Vector2f pow(float power) {
        return Vector2f.from(Math.pow(getX(), power), Math.pow(getY(), power));
    }

    @Override // org.cloudburstmc.math.vector.Vector2f, org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public Vector2f ceil() {
        return Vector2f.from(Math.ceil(getX()), Math.ceil(getY()));
    }

    @Override // org.cloudburstmc.math.vector.Vector2f, org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public Vector2f floor() {
        return Vector2f.from(GenericMath.floor(getX()), GenericMath.floor(getY()));
    }

    @Override // org.cloudburstmc.math.vector.Vector2f, org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public Vector2f round() {
        return Vector2f.from(Math.round(getX()), Math.round(getY()));
    }

    @Override // org.cloudburstmc.math.vector.Vector2f, org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public Vector2f abs() {
        return Vector2f.from(Math.abs(getX()), Math.abs(getY()));
    }

    @Override // org.cloudburstmc.math.vector.Vector2f, org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public Vector2f negate() {
        return Vector2f.from(-getX(), -getY());
    }

    @Override // org.cloudburstmc.math.vector.Vector2f
    @Nonnull
    public Vector2f min(float x, float y) {
        return Vector2f.from(Math.min(getX(), x), Math.min(getY(), y));
    }

    @Override // org.cloudburstmc.math.vector.Vector2f
    @Nonnull
    public Vector2f max(float x, float y) {
        return Vector2f.from(Math.max(getX(), x), Math.max(getY(), y));
    }

    @Override // org.cloudburstmc.math.vector.Vector2f
    @Nonnull
    public Vector2f north(float v) {
        return Vector2f.from(getX(), getY() - v);
    }

    @Override // org.cloudburstmc.math.vector.Vector2f
    @Nonnull
    public Vector2f south(float v) {
        return Vector2f.from(getX(), getY() + v);
    }

    @Override // org.cloudburstmc.math.vector.Vector2f
    @Nonnull
    public Vector2f east(float v) {
        return Vector2f.from(getX() + v, getY());
    }

    @Override // org.cloudburstmc.math.vector.Vector2f
    @Nonnull
    public Vector2f west(float v) {
        return Vector2f.from(getX() - v, getY());
    }

    @Override // org.cloudburstmc.math.vector.Vector2f, org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public Vector2f normalize() {
        float length = length();
        if (Math.abs(length) < GenericMath.FLT_EPSILON) {
            throw new ArithmeticException("Cannot normalize the zero vector");
        }
        return Vector2f.from(getX() / length, getY() / length);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vector2f)) {
            return false;
        }
        Vector2f vector2 = (Vector2f) o;
        return Float.compare(vector2.getX(), this.x) == 0 && Float.compare(vector2.getY(), this.y) == 0;
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
