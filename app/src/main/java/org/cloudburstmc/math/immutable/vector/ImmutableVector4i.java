package org.cloudburstmc.math.immutable.vector;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.cloudburstmc.math.vector.Vector4i;

@ParametersAreNonnullByDefault
/* loaded from: classes5.dex */
public class ImmutableVector4i extends Vector4i {
    private static final long serialVersionUID = 1;
    private final int w;
    private final int x;
    private final int y;
    private final int z;
    private volatile transient boolean hashed = false;
    private volatile transient int hashCode = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ImmutableVector4i(int x, int y, int z, int w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    @Override // org.cloudburstmc.math.vector.Vector4i
    public int getX() {
        return this.x;
    }

    @Override // org.cloudburstmc.math.vector.Vector4i
    public int getY() {
        return this.y;
    }

    @Override // org.cloudburstmc.math.vector.Vector4i
    public int getZ() {
        return this.z;
    }

    @Override // org.cloudburstmc.math.vector.Vector4i
    public int getW() {
        return this.w;
    }

    @Override // org.cloudburstmc.math.vector.Vector4i
    @Nonnull
    public Vector4i add(int x, int y, int z, int w) {
        return Vector4i.from(this.x + x, this.y + y, this.z + z, this.w + w);
    }

    @Override // org.cloudburstmc.math.vector.Vector4i
    @Nonnull
    public Vector4i sub(int x, int y, int z, int w) {
        return Vector4i.from(this.x - x, this.y - y, this.z - z, this.w - w);
    }

    @Override // org.cloudburstmc.math.vector.Vector4i
    @Nonnull
    public Vector4i mul(int x, int y, int z, int w) {
        return Vector4i.from(this.x * x, this.y * y, this.z * z, this.w * w);
    }

    @Override // org.cloudburstmc.math.vector.Vector4i
    @Nonnull
    public Vector4i div(int x, int y, int z, int w) {
        return Vector4i.from(this.x / x, this.y / y, this.z / z, this.w / w);
    }

    @Override // org.cloudburstmc.math.vector.Vector4i
    @Nonnull
    public Vector4i project(int x, int y, int z, int w) {
        int lengthSquared = (x * x) + (y * y) + (z * z) + (w * w);
        if (lengthSquared == 0) {
            throw new ArithmeticException("Cannot project onto the zero vector");
        }
        float a = dot(x, y, z, w) / lengthSquared;
        return Vector4i.from(x * a, y * a, z * a, w * a);
    }

    @Override // org.cloudburstmc.math.vector.Vector4i, org.cloudburstmc.math.vector.Vectori
    @Nonnull
    public Vector4i pow(int power) {
        return Vector4i.from(Math.pow(this.x, power), Math.pow(this.y, power), Math.pow(this.z, power), Math.pow(this.w, power));
    }

    @Override // org.cloudburstmc.math.vector.Vector4i, org.cloudburstmc.math.vector.Vectori
    @Nonnull
    public Vector4i abs() {
        return Vector4i.from(Math.abs(this.x), Math.abs(this.y), Math.abs(this.z), Math.abs(this.w));
    }

    @Override // org.cloudburstmc.math.vector.Vector4i, org.cloudburstmc.math.vector.Vectori
    @Nonnull
    public Vector4i negate() {
        return Vector4i.from(-this.x, -this.y, -this.z, -this.w);
    }

    @Override // org.cloudburstmc.math.vector.Vector4i
    @Nonnull
    public Vector4i min(int x, int y, int z, int w) {
        return Vector4i.from(Math.min(this.x, x), Math.min(this.y, y), Math.min(this.z, z), Math.min(this.w, w));
    }

    @Override // org.cloudburstmc.math.vector.Vector4i
    @Nonnull
    public Vector4i max(int x, int y, int z, int w) {
        return Vector4i.from(Math.max(this.x, x), Math.max(this.y, y), Math.max(this.z, z), Math.max(this.w, w));
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vector4i)) {
            return false;
        }
        Vector4i vector4 = (Vector4i) o;
        return vector4.getX() == this.x && vector4.getY() == this.y && vector4.getZ() == this.z && vector4.getW() == this.w;
    }

    public int hashCode() {
        if (!this.hashed) {
            int result = ((float) this.x) != 0.0f ? Integer.hashCode(this.x) : 0;
            this.hashCode = (((((result * 31) + (((float) this.y) != 0.0f ? Integer.hashCode(this.y) : 0)) * 31) + (((float) this.z) != 0.0f ? Integer.hashCode(this.z) : 0)) * 31) + (((float) this.w) != 0.0f ? Integer.hashCode(this.w) : 0);
            this.hashed = true;
        }
        int result2 = this.hashCode;
        return result2;
    }
}
