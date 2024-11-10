package org.cloudburstmc.math.immutable.vector;

import com.trossense.bl;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.cloudburstmc.math.GenericMath;
import org.cloudburstmc.math.vector.Vector2l;
import org.cloudburstmc.math.vector.Vector3d;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.math.vector.Vector3l;
import org.cloudburstmc.math.vector.Vector4l;
import org.cloudburstmc.math.vector.VectorNl;

@ParametersAreNonnullByDefault
/* loaded from: classes5.dex */
public class ImmutableVector3l extends Vector3l {
    private static final long serialVersionUID = 1;
    private final long x;
    private final long y;
    private final long z;
    private volatile transient boolean hashed = false;
    private volatile transient int hashCode = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ImmutableVector3l(long x, long y, long z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override // org.cloudburstmc.math.vector.Vector3l
    public long getX() {
        return this.x;
    }

    @Override // org.cloudburstmc.math.vector.Vector3l
    public long getY() {
        return this.y;
    }

    @Override // org.cloudburstmc.math.vector.Vector3l
    public long getZ() {
        return this.z;
    }

    @Override // org.cloudburstmc.math.vector.Vector3l
    @Nonnull
    public Vector3l add(long x, long y, long z) {
        return Vector3l.from(this.x + x, this.y + y, this.z + z);
    }

    @Override // org.cloudburstmc.math.vector.Vector3l
    @Nonnull
    public Vector3l sub(long x, long y, long z) {
        return Vector3l.from(this.x - x, this.y - y, this.z - z);
    }

    @Override // org.cloudburstmc.math.vector.Vector3l
    @Nonnull
    public Vector3l mul(long x, long y, long z) {
        return Vector3l.from(this.x * x, this.y * y, this.z * z);
    }

    @Override // org.cloudburstmc.math.vector.Vector3l
    @Nonnull
    public Vector3l div(long x, long y, long z) {
        return Vector3l.from(this.x / x, this.y / y, this.z / z);
    }

    @Override // org.cloudburstmc.math.vector.Vector3l
    @Nonnull
    public Vector3l project(long x, long y, long z) {
        long lengthSquared = (x * x) + (y * y) + (z * z);
        if (lengthSquared == 0) {
            throw new ArithmeticException("Cannot project onto the zero vector");
        }
        double a = dot(x, y, z) / lengthSquared;
        return Vector3l.from(a * x, a * y, a * z);
    }

    @Override // org.cloudburstmc.math.vector.Vector3l
    @Nonnull
    public Vector3l cross(long x, long y, long z) {
        return Vector3l.from((this.y * z) - (this.z * y), (this.z * x) - (this.x * z), (this.x * y) - (this.y * x));
    }

    @Override // org.cloudburstmc.math.vector.Vector3l, org.cloudburstmc.math.vector.Vectorl
    @Nonnull
    public Vector3l pow(long power) {
        return Vector3l.from(Math.pow(this.x, power), Math.pow(this.y, power), Math.pow(this.z, power));
    }

    @Override // org.cloudburstmc.math.vector.Vector3l, org.cloudburstmc.math.vector.Vectorl
    @Nonnull
    public Vector3l abs() {
        return Vector3l.from(Math.abs(this.x), Math.abs(this.y), Math.abs(this.z));
    }

    @Override // org.cloudburstmc.math.vector.Vector3l, org.cloudburstmc.math.vector.Vectorl
    @Nonnull
    public Vector3l negate() {
        return Vector3l.from(-this.x, -this.y, -this.z);
    }

    @Override // org.cloudburstmc.math.vector.Vector3l
    @Nonnull
    public Vector3l min(long x, long y, long z) {
        return Vector3l.from(Math.min(this.x, x), Math.min(this.y, y), Math.min(this.z, z));
    }

    @Override // org.cloudburstmc.math.vector.Vector3l
    @Nonnull
    public Vector3l max(long x, long y, long z) {
        return Vector3l.from(Math.max(this.x, x), Math.max(this.y, y), Math.max(this.z, z));
    }

    @Override // org.cloudburstmc.math.vector.Vector3l
    @Nonnull
    public Vector3l up(long v) {
        return Vector3l.from(this.x, this.y + v, this.z);
    }

    @Override // org.cloudburstmc.math.vector.Vector3l
    @Nonnull
    public Vector3l down(long v) {
        return Vector3l.from(this.x, this.y - v, this.z);
    }

    @Override // org.cloudburstmc.math.vector.Vector3l
    @Nonnull
    public Vector3l north(long v) {
        return Vector3l.from(this.x, this.y, this.z - v);
    }

    @Override // org.cloudburstmc.math.vector.Vector3l
    @Nonnull
    public Vector3l south(long v) {
        return Vector3l.from(this.x, this.y, this.z + v);
    }

    @Override // org.cloudburstmc.math.vector.Vector3l
    @Nonnull
    public Vector3l east(long v) {
        return Vector3l.from(this.x + v, this.y, this.z);
    }

    @Override // org.cloudburstmc.math.vector.Vector3l
    @Nonnull
    public Vector3l west(long v) {
        return Vector3l.from(this.x - v, this.y, this.z);
    }

    @Override // org.cloudburstmc.math.vector.Vector3l
    @Nonnull
    public Vector2l toVector2() {
        return Vector2l.from(this);
    }

    @Override // org.cloudburstmc.math.vector.Vector3l
    @Nonnull
    public Vector2l toVector2(boolean useZ) {
        return Vector2l.from(this.x, useZ ? this.z : this.y);
    }

    @Override // org.cloudburstmc.math.vector.Vector3l
    @Nonnull
    public Vector4l toVector4() {
        return toVector4(0L);
    }

    @Override // org.cloudburstmc.math.vector.Vector3l
    @Nonnull
    public Vector4l toVector4(double w) {
        return toVector4(GenericMath.floor64(w));
    }

    @Override // org.cloudburstmc.math.vector.Vector3l
    @Nonnull
    public Vector4l toVector4(long w) {
        return Vector4l.from(this, w);
    }

    @Override // org.cloudburstmc.math.vector.Vector3l
    @Nonnull
    public VectorNl toVectorN() {
        return VectorNl.from(this);
    }

    @Override // org.cloudburstmc.math.vector.Vector3l, org.cloudburstmc.math.vector.Vectorl
    @Nonnull
    public long[] toArray() {
        return new long[]{this.x, this.y, this.z};
    }

    @Override // org.cloudburstmc.math.vector.Vector3l, org.cloudburstmc.math.vector.Vectorl
    @Nonnull
    public Vector3i toInt() {
        return Vector3i.from(this.x, this.y, this.z);
    }

    @Override // org.cloudburstmc.math.vector.Vector3l, org.cloudburstmc.math.vector.Vectorl
    @Nonnull
    public Vector3l toLong() {
        return Vector3l.from(this.x, this.y, this.z);
    }

    @Override // org.cloudburstmc.math.vector.Vector3l, org.cloudburstmc.math.vector.Vectorl
    @Nonnull
    public Vector3f toFloat() {
        return Vector3f.from((float) this.x, (float) this.y, (float) this.z);
    }

    @Override // org.cloudburstmc.math.vector.Vector3l, org.cloudburstmc.math.vector.Vectorl
    @Nonnull
    public Vector3d toDouble() {
        return Vector3d.from((float) this.x, (float) this.y, (float) this.z);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vector3l)) {
            return false;
        }
        Vector3l vector3 = (Vector3l) o;
        return vector3.getX() == this.x && vector3.getY() == this.y && vector3.getZ() == this.z;
    }

    public int hashCode() {
        if (!this.hashed) {
            this.hashCode = (((Long.hashCode(this.x) * bl.cJ) + Long.hashCode(this.y)) * 97) + Long.hashCode(this.z);
            this.hashed = true;
        }
        return this.hashCode;
    }
}
