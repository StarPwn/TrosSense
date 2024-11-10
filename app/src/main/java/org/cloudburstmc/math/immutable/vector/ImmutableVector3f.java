package org.cloudburstmc.math.immutable.vector;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.cloudburstmc.math.GenericMath;
import org.cloudburstmc.math.vector.Vector3f;

@ParametersAreNonnullByDefault
/* loaded from: classes5.dex */
public class ImmutableVector3f extends Vector3f {
    private static final long serialVersionUID = 1;
    private final float x;
    private final float y;
    private final float z;
    private volatile transient boolean hashed = false;
    private volatile transient int hashCode = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ImmutableVector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override // org.cloudburstmc.math.vector.Vector3f
    public float getX() {
        return this.x;
    }

    @Override // org.cloudburstmc.math.vector.Vector3f
    public float getY() {
        return this.y;
    }

    @Override // org.cloudburstmc.math.vector.Vector3f
    public float getZ() {
        return this.z;
    }

    @Override // org.cloudburstmc.math.vector.Vector3f
    @Nonnull
    public Vector3f add(float x, float y, float z) {
        return Vector3f.from(getX() + x, getY() + y, getZ() + z);
    }

    @Override // org.cloudburstmc.math.vector.Vector3f
    @Nonnull
    public Vector3f sub(float x, float y, float z) {
        return Vector3f.from(getX() - x, getY() - y, getZ() - z);
    }

    @Override // org.cloudburstmc.math.vector.Vector3f
    @Nonnull
    public Vector3f mul(float x, float y, float z) {
        return Vector3f.from(getX() * x, getY() * y, getZ() * z);
    }

    @Override // org.cloudburstmc.math.vector.Vector3f
    @Nonnull
    public Vector3f div(float x, float y, float z) {
        return Vector3f.from(getX() / x, getY() / y, getZ() / z);
    }

    @Override // org.cloudburstmc.math.vector.Vector3f
    @Nonnull
    public Vector3f project(float x, float y, float z) {
        float lengthSquared = (x * x) + (y * y) + (z * z);
        if (Math.abs(lengthSquared) < GenericMath.FLT_EPSILON) {
            throw new ArithmeticException("Cannot project onto the zero vector");
        }
        float a = dot(x, y, z) / lengthSquared;
        return Vector3f.from(a * x, a * y, a * z);
    }

    @Override // org.cloudburstmc.math.vector.Vector3f
    @Nonnull
    public Vector3f cross(float x, float y, float z) {
        return Vector3f.from((getY() * z) - (getZ() * y), (getZ() * x) - (getX() * z), (getX() * y) - (getY() * x));
    }

    @Override // org.cloudburstmc.math.vector.Vector3f, org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public Vector3f pow(float power) {
        return Vector3f.from(Math.pow(this.x, power), Math.pow(this.y, power), Math.pow(this.z, power));
    }

    @Override // org.cloudburstmc.math.vector.Vector3f, org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public Vector3f ceil() {
        return Vector3f.from(Math.ceil(getX()), Math.ceil(getY()), Math.ceil(getZ()));
    }

    @Override // org.cloudburstmc.math.vector.Vector3f, org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public Vector3f floor() {
        return Vector3f.from(GenericMath.floor(getX()), GenericMath.floor(getY()), GenericMath.floor(getZ()));
    }

    @Override // org.cloudburstmc.math.vector.Vector3f, org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public Vector3f round() {
        return Vector3f.from(Math.round(getX()), Math.round(getY()), Math.round(getZ()));
    }

    @Override // org.cloudburstmc.math.vector.Vector3f, org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public Vector3f abs() {
        return Vector3f.from(Math.abs(getX()), Math.abs(getY()), Math.abs(getZ()));
    }

    @Override // org.cloudburstmc.math.vector.Vector3f, org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public Vector3f negate() {
        return Vector3f.from(-getX(), -getY(), -getZ());
    }

    @Override // org.cloudburstmc.math.vector.Vector3f
    @Nonnull
    public Vector3f min(float x, float y, float z) {
        return Vector3f.from(Math.min(getX(), x), Math.min(getY(), y), Math.min(getZ(), z));
    }

    @Override // org.cloudburstmc.math.vector.Vector3f
    @Nonnull
    public Vector3f max(float x, float y, float z) {
        return Vector3f.from(Math.max(getX(), x), Math.max(getY(), y), Math.max(getZ(), z));
    }

    @Override // org.cloudburstmc.math.vector.Vector3f
    @Nonnull
    public Vector3f up(float v) {
        return Vector3f.from(getX(), getY() + v, getZ());
    }

    @Override // org.cloudburstmc.math.vector.Vector3f
    @Nonnull
    public Vector3f down(float v) {
        return Vector3f.from(getX(), getY() - v, getZ());
    }

    @Override // org.cloudburstmc.math.vector.Vector3f
    @Nonnull
    public Vector3f north(float v) {
        return Vector3f.from(getX(), getY(), getZ() - v);
    }

    @Override // org.cloudburstmc.math.vector.Vector3f
    @Nonnull
    public Vector3f south(float v) {
        return Vector3f.from(getX(), getY(), getZ() + v);
    }

    @Override // org.cloudburstmc.math.vector.Vector3f
    @Nonnull
    public Vector3f east(float v) {
        return Vector3f.from(getX() + v, getY(), getZ());
    }

    @Override // org.cloudburstmc.math.vector.Vector3f
    @Nonnull
    public Vector3f west(float v) {
        return Vector3f.from(getX() - v, getY(), getZ());
    }

    @Override // org.cloudburstmc.math.vector.Vector3f, org.cloudburstmc.math.vector.Vectorf
    @Nonnull
    public Vector3f normalize() {
        float length = length();
        if (Math.abs(length) < GenericMath.FLT_EPSILON) {
            throw new ArithmeticException("Cannot normalize the zero vector");
        }
        return Vector3f.from(getX() / length, getY() / length, getZ() / length);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vector3f)) {
            return false;
        }
        Vector3f vector3 = (Vector3f) o;
        return Float.compare(vector3.getX(), this.x) == 0 && Float.compare(vector3.getY(), this.y) == 0 && Float.compare(vector3.getZ(), this.z) == 0;
    }

    public int hashCode() {
        if (!this.hashed) {
            int result = this.x != 0.0f ? Float.hashCode(this.x) : 0;
            this.hashCode = (((result * 31) + (this.y != 0.0f ? Float.hashCode(this.y) : 0)) * 31) + (this.z != 0.0f ? Float.hashCode(this.z) : 0);
            this.hashed = true;
        }
        return this.hashCode;
    }
}
