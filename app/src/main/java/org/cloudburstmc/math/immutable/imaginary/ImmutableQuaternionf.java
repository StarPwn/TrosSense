package org.cloudburstmc.math.immutable.imaginary;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.cloudburstmc.math.GenericMath;
import org.cloudburstmc.math.imaginary.Quaternionf;

@ParametersAreNonnullByDefault
/* loaded from: classes5.dex */
public class ImmutableQuaternionf extends Quaternionf {
    private static final long serialVersionUID = 1;
    private final float w;
    private final float x;
    private final float y;
    private final float z;
    private volatile transient boolean hashed = false;
    private volatile transient int hashCode = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ImmutableQuaternionf(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    @Override // org.cloudburstmc.math.imaginary.Quaternionf
    public float getX() {
        return this.x;
    }

    @Override // org.cloudburstmc.math.imaginary.Quaternionf
    public float getY() {
        return this.y;
    }

    @Override // org.cloudburstmc.math.imaginary.Quaternionf
    public float getZ() {
        return this.z;
    }

    @Override // org.cloudburstmc.math.imaginary.Quaternionf
    public float getW() {
        return this.w;
    }

    @Override // org.cloudburstmc.math.imaginary.Quaternionf
    @Nonnull
    public Quaternionf add(float x, float y, float z, float w) {
        return Quaternionf.from(this.x + x, this.y + y, this.z + z, this.w + w);
    }

    @Override // org.cloudburstmc.math.imaginary.Quaternionf
    @Nonnull
    public Quaternionf sub(float x, float y, float z, float w) {
        return Quaternionf.from(this.x - x, this.y - y, this.z - z, this.w - w);
    }

    @Override // org.cloudburstmc.math.imaginary.Quaternionf, org.cloudburstmc.math.imaginary.Imaginaryf
    @Nonnull
    public Quaternionf mul(float a) {
        return Quaternionf.from(this.x * a, this.y * a, this.z * a, this.w * a);
    }

    @Override // org.cloudburstmc.math.imaginary.Quaternionf
    @Nonnull
    public Quaternionf mul(float x, float y, float z, float w) {
        return Quaternionf.from((((this.w * x) + (this.x * w)) + (this.y * z)) - (this.z * y), (((this.w * y) + (this.y * w)) + (this.z * x)) - (this.x * z), (((this.w * z) + (this.z * w)) + (this.x * y)) - (this.y * x), (((this.w * w) - (this.x * x)) - (this.y * y)) - (this.z * z));
    }

    @Override // org.cloudburstmc.math.imaginary.Quaternionf, org.cloudburstmc.math.imaginary.Imaginaryf
    @Nonnull
    public Quaternionf div(float a) {
        return Quaternionf.from(this.x / a, this.y / a, this.z / a, this.w / a);
    }

    @Override // org.cloudburstmc.math.imaginary.Quaternionf
    @Nonnull
    public Quaternionf div(float x, float y, float z, float w) {
        float d = (x * x) + (y * y) + (z * z) + (w * w);
        return Quaternionf.from(((((this.x * w) - (this.w * x)) - (this.z * y)) + (this.y * z)) / d, ((((this.y * w) + (this.z * x)) - (this.w * y)) - (this.x * z)) / d, ((((this.z * w) - (this.y * x)) + (this.x * y)) - (this.w * z)) / d, ((((this.w * w) + (this.x * x)) + (this.y * y)) + (this.z * z)) / d);
    }

    @Override // org.cloudburstmc.math.imaginary.Quaternionf, org.cloudburstmc.math.imaginary.Imaginaryf
    @Nonnull
    public Quaternionf conjugate() {
        return Quaternionf.from(-this.x, -this.y, -this.z, this.w);
    }

    @Override // org.cloudburstmc.math.imaginary.Quaternionf, org.cloudburstmc.math.imaginary.Imaginaryf
    @Nonnull
    public Quaternionf invert() {
        float lengthSquared = lengthSquared();
        if (Math.abs(lengthSquared) < GenericMath.FLT_EPSILON) {
            throw new ArithmeticException("Cannot invert a quaternion of length zero");
        }
        return conjugate().div(lengthSquared);
    }

    @Override // org.cloudburstmc.math.imaginary.Quaternionf, org.cloudburstmc.math.imaginary.Imaginaryf
    @Nonnull
    public Quaternionf normalize() {
        float length = length();
        if (Math.abs(length) < GenericMath.FLT_EPSILON) {
            throw new ArithmeticException("Cannot normalize the zero quaternion");
        }
        return Quaternionf.from(this.x / length, this.y / length, this.z / length, this.w / length);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Quaternionf)) {
            return false;
        }
        Quaternionf quaternion = (Quaternionf) o;
        return Float.compare(quaternion.getW(), this.w) == 0 && Float.compare(quaternion.getX(), this.x) == 0 && Float.compare(quaternion.getY(), this.y) == 0 && Float.compare(quaternion.getZ(), this.z) == 0;
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
