package org.cloudburstmc.math.immutable.vector;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.cloudburstmc.math.vector.Vector2l;

@ParametersAreNonnullByDefault
/* loaded from: classes5.dex */
public class ImmutableVector2l extends Vector2l {
    private static final long serialVersionUID = 1;
    private final long x;
    private final long y;
    private volatile transient boolean hashed = false;
    private volatile transient int hashCode = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ImmutableVector2l(long x, long y) {
        this.x = x;
        this.y = y;
    }

    @Override // org.cloudburstmc.math.vector.Vector2l
    public long getX() {
        return this.x;
    }

    @Override // org.cloudburstmc.math.vector.Vector2l
    public long getY() {
        return this.y;
    }

    @Override // org.cloudburstmc.math.vector.Vector2l
    @Nonnull
    public Vector2l add(long x, long y) {
        return Vector2l.from(this.x + x, this.y + y);
    }

    @Override // org.cloudburstmc.math.vector.Vector2l
    @Nonnull
    public Vector2l sub(long x, long y) {
        return Vector2l.from(this.x - x, this.y - y);
    }

    @Override // org.cloudburstmc.math.vector.Vector2l
    @Nonnull
    public Vector2l mul(long x, long y) {
        return Vector2l.from(this.x * x, this.y * y);
    }

    @Override // org.cloudburstmc.math.vector.Vector2l
    @Nonnull
    public Vector2l div(long x, long y) {
        return Vector2l.from(this.x / x, this.y / y);
    }

    @Override // org.cloudburstmc.math.vector.Vector2l
    @Nonnull
    public Vector2l project(long x, long y) {
        long lengthSquared = (x * x) + (y * y);
        if (lengthSquared == 0) {
            throw new ArithmeticException("Cannot project onto the zero vector");
        }
        double a = dot(x, y) / lengthSquared;
        return Vector2l.from(x * a, y * a);
    }

    @Override // org.cloudburstmc.math.vector.Vector2l, org.cloudburstmc.math.vector.Vectorl
    @Nonnull
    public Vector2l pow(long power) {
        return Vector2l.from(Math.pow(this.x, power), Math.pow(this.y, power));
    }

    @Override // org.cloudburstmc.math.vector.Vector2l, org.cloudburstmc.math.vector.Vectorl
    @Nonnull
    public Vector2l abs() {
        return Vector2l.from(Math.abs(this.x), Math.abs(this.y));
    }

    @Override // org.cloudburstmc.math.vector.Vector2l, org.cloudburstmc.math.vector.Vectorl
    @Nonnull
    public Vector2l negate() {
        return Vector2l.from(-this.x, -this.y);
    }

    @Override // org.cloudburstmc.math.vector.Vector2l
    @Nonnull
    public Vector2l min(long x, long y) {
        return Vector2l.from(Math.min(this.x, x), Math.min(this.y, y));
    }

    @Override // org.cloudburstmc.math.vector.Vector2l
    @Nonnull
    public Vector2l max(long x, long y) {
        return Vector2l.from(Math.max(this.x, x), Math.max(this.y, y));
    }

    @Override // org.cloudburstmc.math.vector.Vector2l
    @Nonnull
    public Vector2l north(long v) {
        return Vector2l.from(this.x, this.y - v);
    }

    @Override // org.cloudburstmc.math.vector.Vector2l
    @Nonnull
    public Vector2l south(long v) {
        return Vector2l.from(this.x, this.y + v);
    }

    @Override // org.cloudburstmc.math.vector.Vector2l
    @Nonnull
    public Vector2l east(long v) {
        return Vector2l.from(this.x + v, this.y);
    }

    @Override // org.cloudburstmc.math.vector.Vector2l
    @Nonnull
    public Vector2l west(long v) {
        return Vector2l.from(this.x - v, this.y);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vector2l)) {
            return false;
        }
        Vector2l vector2 = (Vector2l) o;
        return vector2.getX() == this.x && vector2.getY() == this.y;
    }

    public int hashCode() {
        if (!this.hashed) {
            int result = ((float) this.x) != 0.0f ? Long.hashCode(this.x) : 0;
            this.hashCode = (result * 31) + (((float) this.y) != 0.0f ? Long.hashCode(this.y) : 0);
            this.hashed = true;
        }
        int result2 = this.hashCode;
        return result2;
    }
}
