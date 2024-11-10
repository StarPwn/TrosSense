package org.cloudburstmc.math.immutable.vector;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.cloudburstmc.math.vector.Vector2i;

@ParametersAreNonnullByDefault
/* loaded from: classes5.dex */
public class ImmutableVector2i extends Vector2i {
    private static final long serialVersionUID = 1;
    private final int x;
    private final int y;
    private volatile transient boolean hashed = false;
    private volatile transient int hashCode = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ImmutableVector2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override // org.cloudburstmc.math.vector.Vector2i
    public int getX() {
        return this.x;
    }

    @Override // org.cloudburstmc.math.vector.Vector2i
    public int getY() {
        return this.y;
    }

    @Override // org.cloudburstmc.math.vector.Vector2i
    @Nonnull
    public Vector2i add(int x, int y) {
        return Vector2i.from(this.x + x, this.y + y);
    }

    @Override // org.cloudburstmc.math.vector.Vector2i
    @Nonnull
    public Vector2i sub(int x, int y) {
        return Vector2i.from(this.x - x, this.y - y);
    }

    @Override // org.cloudburstmc.math.vector.Vector2i
    @Nonnull
    public Vector2i mul(int x, int y) {
        return Vector2i.from(this.x * x, this.y * y);
    }

    @Override // org.cloudburstmc.math.vector.Vector2i
    @Nonnull
    public Vector2i div(int x, int y) {
        return Vector2i.from(this.x / x, this.y / y);
    }

    @Override // org.cloudburstmc.math.vector.Vector2i
    @Nonnull
    public Vector2i project(int x, int y) {
        int lengthSquared = (x * x) + (y * y);
        if (lengthSquared == 0) {
            throw new ArithmeticException("Cannot project onto the zero vector");
        }
        float a = dot(x, y) / lengthSquared;
        return Vector2i.from(x * a, y * a);
    }

    @Override // org.cloudburstmc.math.vector.Vector2i, org.cloudburstmc.math.vector.Vectori
    @Nonnull
    public Vector2i pow(int power) {
        return Vector2i.from(Math.pow(this.x, power), Math.pow(this.y, power));
    }

    @Override // org.cloudburstmc.math.vector.Vector2i, org.cloudburstmc.math.vector.Vectori
    @Nonnull
    public Vector2i abs() {
        return Vector2i.from(Math.abs(this.x), Math.abs(this.y));
    }

    @Override // org.cloudburstmc.math.vector.Vector2i, org.cloudburstmc.math.vector.Vectori
    @Nonnull
    public Vector2i negate() {
        return Vector2i.from(-this.x, -this.y);
    }

    @Override // org.cloudburstmc.math.vector.Vector2i
    @Nonnull
    public Vector2i min(int x, int y) {
        return Vector2i.from(Math.min(this.x, x), Math.min(this.y, y));
    }

    @Override // org.cloudburstmc.math.vector.Vector2i
    @Nonnull
    public Vector2i max(int x, int y) {
        return Vector2i.from(Math.max(this.x, x), Math.max(this.y, y));
    }

    @Override // org.cloudburstmc.math.vector.Vector2i
    @Nonnull
    public Vector2i north(int v) {
        return Vector2i.from(this.x, this.y - v);
    }

    @Override // org.cloudburstmc.math.vector.Vector2i
    @Nonnull
    public Vector2i south(int v) {
        return Vector2i.from(this.x, this.y + v);
    }

    @Override // org.cloudburstmc.math.vector.Vector2i
    @Nonnull
    public Vector2i east(int v) {
        return Vector2i.from(this.x + v, this.y);
    }

    @Override // org.cloudburstmc.math.vector.Vector2i
    @Nonnull
    public Vector2i west(int v) {
        return Vector2i.from(this.x - v, this.y);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vector2i)) {
            return false;
        }
        Vector2i vector2 = (Vector2i) o;
        return vector2.getX() == this.x && vector2.getY() == this.y;
    }

    public int hashCode() {
        if (!this.hashed) {
            int result = ((float) this.x) != 0.0f ? Integer.hashCode(this.x) : 0;
            this.hashCode = (result * 31) + (((float) this.y) != 0.0f ? Integer.hashCode(this.y) : 0);
            this.hashed = true;
        }
        int result2 = this.hashCode;
        return result2;
    }
}
