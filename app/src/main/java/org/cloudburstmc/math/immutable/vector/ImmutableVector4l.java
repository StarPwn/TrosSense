package org.cloudburstmc.math.immutable.vector;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.cloudburstmc.math.vector.Vector4l;

@ParametersAreNonnullByDefault
/* loaded from: classes5.dex */
public class ImmutableVector4l extends Vector4l {
    private static final long serialVersionUID = 1;
    private final long w;
    private final long x;
    private final long y;
    private final long z;
    private volatile transient boolean hashed = false;
    private volatile transient int hashCode = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ImmutableVector4l(long x, long y, long z, long w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    @Override // org.cloudburstmc.math.vector.Vector4l
    public long getX() {
        return this.x;
    }

    @Override // org.cloudburstmc.math.vector.Vector4l
    public long getY() {
        return this.y;
    }

    @Override // org.cloudburstmc.math.vector.Vector4l
    public long getZ() {
        return this.z;
    }

    @Override // org.cloudburstmc.math.vector.Vector4l
    public long getW() {
        return this.w;
    }

    @Override // org.cloudburstmc.math.vector.Vector4l
    @Nonnull
    public Vector4l add(long x, long y, long z, long w) {
        return Vector4l.from(this.x + x, this.y + y, this.z + z, this.w + w);
    }

    @Override // org.cloudburstmc.math.vector.Vector4l
    @Nonnull
    public Vector4l sub(long x, long y, long z, long w) {
        return Vector4l.from(this.x - x, this.y - y, this.z - z, this.w - w);
    }

    @Override // org.cloudburstmc.math.vector.Vector4l
    @Nonnull
    public Vector4l mul(long x, long y, long z, long w) {
        return Vector4l.from(this.x * x, this.y * y, this.z * z, this.w * w);
    }

    @Override // org.cloudburstmc.math.vector.Vector4l
    @Nonnull
    public Vector4l div(long x, long y, long z, long w) {
        return Vector4l.from(this.x / x, this.y / y, this.z / z, this.w / w);
    }

    @Override // org.cloudburstmc.math.vector.Vector4l
    @Nonnull
    public Vector4l project(long x, long y, long z, long w) {
        long lengthSquared = (x * x) + (y * y) + (z * z) + (w * w);
        if (lengthSquared == 0) {
            throw new ArithmeticException("Cannot project onto the zero vector");
        }
        double a = dot(x, y, z, w) / lengthSquared;
        return Vector4l.from(a * x, a * y, a * z, a * w);
    }

    @Override // org.cloudburstmc.math.vector.Vector4l, org.cloudburstmc.math.vector.Vectorl
    @Nonnull
    public Vector4l pow(long power) {
        return Vector4l.from(Math.pow(this.x, power), Math.pow(this.y, power), Math.pow(this.z, power), Math.pow(this.w, power));
    }

    @Override // org.cloudburstmc.math.vector.Vector4l, org.cloudburstmc.math.vector.Vectorl
    @Nonnull
    public Vector4l abs() {
        return Vector4l.from(Math.abs(this.x), Math.abs(this.y), Math.abs(this.z), Math.abs(this.w));
    }

    @Override // org.cloudburstmc.math.vector.Vector4l, org.cloudburstmc.math.vector.Vectorl
    @Nonnull
    public Vector4l negate() {
        return Vector4l.from(-this.x, -this.y, -this.z, -this.w);
    }

    @Override // org.cloudburstmc.math.vector.Vector4l
    @Nonnull
    public Vector4l min(long x, long y, long z, long w) {
        return Vector4l.from(Math.min(this.x, x), Math.min(this.y, y), Math.min(this.z, z), Math.min(this.w, w));
    }

    @Override // org.cloudburstmc.math.vector.Vector4l
    @Nonnull
    public Vector4l max(long x, long y, long z, long w) {
        return Vector4l.from(Math.max(this.x, x), Math.max(this.y, y), Math.max(this.z, z), Math.max(this.w, w));
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vector4l)) {
            return false;
        }
        Vector4l vector4 = (Vector4l) o;
        return vector4.getX() == this.x && vector4.getY() == this.y && vector4.getZ() == this.z && vector4.getW() == this.w;
    }

    public int hashCode() {
        if (!this.hashed) {
            int result = ((float) this.x) != 0.0f ? Long.hashCode(this.x) : 0;
            this.hashCode = (((((result * 31) + (((float) this.y) != 0.0f ? Long.hashCode(this.y) : 0)) * 31) + (((float) this.z) != 0.0f ? Long.hashCode(this.z) : 0)) * 31) + (((float) this.w) != 0.0f ? Long.hashCode(this.w) : 0);
            this.hashed = true;
        }
        int result2 = this.hashCode;
        return result2;
    }
}
