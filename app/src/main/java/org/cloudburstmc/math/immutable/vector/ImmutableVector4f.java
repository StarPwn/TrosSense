package org.cloudburstmc.math.immutable.vector;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.cloudburstmc.math.GenericMath;
import org.cloudburstmc.math.vector.Vector4f;

@ParametersAreNonnullByDefault
/* loaded from: classes5.dex */
public class ImmutableVector4f extends Vector4f {
    private static final long serialVersionUID = 1;
    private final float w;
    private final float x;
    private final float y;
    private final float z;
    private volatile transient boolean hashed = false;
    private volatile transient int hashCode = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ImmutableVector4f(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    @Override // org.cloudburstmc.math.vector.Vector4f
    public float getX() {
        return this.x;
    }

    @Override // org.cloudburstmc.math.vector.Vector4f
    public float getY() {
        return this.y;
    }

    @Override // org.cloudburstmc.math.vector.Vector4f
    public float getZ() {
        return this.z;
    }

    @Override // org.cloudburstmc.math.vector.Vector4f
    public float getW() {
        return this.w;
    }

    @Override // org.cloudburstmc.math.vector.Vector4f
    @Nonnull
    public Vector4f add(float x, float y, float z, float w) {
        return Vector4f.from(getX() + x, getY() + y, getZ() + z, getW() + w);
    }

    @Override // org.cloudburstmc.math.vector.Vector4f
    @Nonnull
    public Vector4f sub(float x, float y, float z, float w) {
        return Vector4f.from(getX() - x, getY() - y, getZ() - z, getW() - w);
    }

    @Override // org.cloudburstmc.math.vector.Vector4f
    @Nonnull
    public Vector4f mul(float x, float y, float z, float w) {
        return Vector4f.from(getX() * x, getY() * y, getZ() * z, getW() * w);
    }

    @Override // org.cloudburstmc.math.vector.Vector4f
    @Nonnull
    public Vector4f div(float x, float y, float z, float w) {
        return Vector4f.from(getX() / x, getY() / y, getZ() / z, getW() / w);
    }

    @Override // org.cloudburstmc.math.vector.Vector4f
    @Nonnull
    public Vector4f project(float x, float y, float z, float w) {
        float lengthSquared = (x * x) + (y * y) + (z * z) + (w * w);
        if (Math.abs(lengthSquared) < GenericMath.FLT_EPSILON) {
            throw new ArithmeticException("Cannot project onto the zero vector");
        }
        float a = dot(x, y, z, w) / lengthSquared;
        return Vector4f.from(a * x, a * y, a * z, a * w);
    }

    @Override // org.cloudburstmc.math.vector.Vector4f, org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public Vector4f pow(float power) {
        return Vector4f.from(Math.pow(getX(), power), Math.pow(getY(), power), Math.pow(getZ(), power), Math.pow(getW(), power));
    }

    @Override // org.cloudburstmc.math.vector.Vector4f, org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public Vector4f ceil() {
        return Vector4f.from(Math.ceil(getX()), Math.ceil(getY()), Math.ceil(getZ()), Math.ceil(getW()));
    }

    @Override // org.cloudburstmc.math.vector.Vector4f, org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public Vector4f floor() {
        return Vector4f.from(GenericMath.floor(getX()), GenericMath.floor(getY()), GenericMath.floor(getZ()), GenericMath.floor(getW()));
    }

    @Override // org.cloudburstmc.math.vector.Vector4f, org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public Vector4f round() {
        return Vector4f.from(Math.round(getX()), Math.round(getY()), Math.round(getZ()), Math.round(getW()));
    }

    @Override // org.cloudburstmc.math.vector.Vector4f, org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public Vector4f abs() {
        return Vector4f.from(Math.abs(getX()), Math.abs(getY()), Math.abs(getZ()), Math.abs(getW()));
    }

    @Override // org.cloudburstmc.math.vector.Vector4f, org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public Vector4f negate() {
        return Vector4f.from(-getX(), -getY(), -getZ(), -getW());
    }

    @Override // org.cloudburstmc.math.vector.Vector4f
    @Nonnull
    public Vector4f min(float x, float y, float z, float w) {
        return Vector4f.from(Math.min(getX(), x), Math.min(getY(), y), Math.min(getZ(), z), Math.min(getW(), w));
    }

    @Override // org.cloudburstmc.math.vector.Vector4f
    @Nonnull
    public Vector4f max(float x, float y, float z, float w) {
        return Vector4f.from(Math.max(getX(), x), Math.max(getY(), y), Math.max(getZ(), z), Math.max(getW(), w));
    }

    @Override // org.cloudburstmc.math.vector.Vector4f, org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public Vector4f normalize() {
        float length = length();
        if (Math.abs(length) < GenericMath.FLT_EPSILON) {
            throw new ArithmeticException("Cannot normalize the zero vector");
        }
        return Vector4f.from(getX() / length, getY() / length, getZ() / length, getW() / length);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vector4f)) {
            return false;
        }
        Vector4f vector4 = (Vector4f) o;
        return Float.compare(vector4.getW(), this.w) == 0 && Float.compare(vector4.getX(), this.x) == 0 && Float.compare(vector4.getY(), this.y) == 0 && Float.compare(vector4.getZ(), this.z) == 0;
    }

    public int hashCode() {
        if (!this.hashed) {
            int result = this.x != 0.0f ? Float.hashCode(this.x) : 0;
            this.hashCode = (((((result * 31) + (this.y != 0.0f ? Float.hashCode(this.y) : 0)) * 31) + (this.z != 0.0f ? Float.hashCode(this.z) : 0)) * 31) + (this.w != 0.0f ? Float.hashCode(this.w) : 0);
            this.hashed = true;
        }
        int result2 = this.hashCode;
        return result2;
    }
}
