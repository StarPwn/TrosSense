package org.cloudburstmc.math.immutable.vector;

import com.trossense.bl;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.cloudburstmc.math.GenericMath;
import org.cloudburstmc.math.vector.Vector2i;
import org.cloudburstmc.math.vector.Vector3d;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.math.vector.Vector3l;
import org.cloudburstmc.math.vector.Vector4i;
import org.cloudburstmc.math.vector.VectorNi;

@ParametersAreNonnullByDefault
/* loaded from: classes5.dex */
public class ImmutableVector3i extends Vector3i {
    private static final long serialVersionUID = 1;
    private final int x;
    private final int y;
    private final int z;
    private volatile transient boolean hashed = false;
    private volatile transient int hashCode = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ImmutableVector3i(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override // org.cloudburstmc.math.vector.Vector3i
    public int getX() {
        return this.x;
    }

    @Override // org.cloudburstmc.math.vector.Vector3i
    public int getY() {
        return this.y;
    }

    @Override // org.cloudburstmc.math.vector.Vector3i
    public int getZ() {
        return this.z;
    }

    @Override // org.cloudburstmc.math.vector.Vector3i
    @Nonnull
    public Vector3i add(int x, int y, int z) {
        return Vector3i.from(this.x + x, this.y + y, this.z + z);
    }

    @Override // org.cloudburstmc.math.vector.Vector3i
    @Nonnull
    public Vector3i sub(int x, int y, int z) {
        return Vector3i.from(this.x - x, this.y - y, this.z - z);
    }

    @Override // org.cloudburstmc.math.vector.Vector3i
    @Nonnull
    public Vector3i mul(int x, int y, int z) {
        return Vector3i.from(this.x * x, this.y * y, this.z * z);
    }

    @Override // org.cloudburstmc.math.vector.Vector3i
    @Nonnull
    public Vector3i div(int x, int y, int z) {
        return Vector3i.from(this.x / x, this.y / y, this.z / z);
    }

    @Override // org.cloudburstmc.math.vector.Vector3i
    @Nonnull
    public Vector3i project(int x, int y, int z) {
        int lengthSquared = (x * x) + (y * y) + (z * z);
        if (lengthSquared == 0) {
            throw new ArithmeticException("Cannot project onto the zero vector");
        }
        float a = dot(x, y, z) / lengthSquared;
        return Vector3i.from(x * a, y * a, z * a);
    }

    @Override // org.cloudburstmc.math.vector.Vector3i
    @Nonnull
    public Vector3i cross(int x, int y, int z) {
        return Vector3i.from((this.y * z) - (this.z * y), (this.z * x) - (this.x * z), (this.x * y) - (this.y * x));
    }

    @Override // org.cloudburstmc.math.vector.Vector3i, org.cloudburstmc.math.vector.Vectori
    @Nonnull
    public Vector3i pow(int power) {
        return Vector3i.from(Math.pow(this.x, power), Math.pow(this.y, power), Math.pow(this.z, power));
    }

    @Override // org.cloudburstmc.math.vector.Vector3i, org.cloudburstmc.math.vector.Vectori
    @Nonnull
    public Vector3i abs() {
        return Vector3i.from(Math.abs(this.x), Math.abs(this.y), Math.abs(this.z));
    }

    @Override // org.cloudburstmc.math.vector.Vector3i, org.cloudburstmc.math.vector.Vectori
    @Nonnull
    public Vector3i negate() {
        return Vector3i.from(-this.x, -this.y, -this.z);
    }

    @Override // org.cloudburstmc.math.vector.Vector3i
    @Nonnull
    public Vector3i min(int x, int y, int z) {
        return Vector3i.from(Math.min(this.x, x), Math.min(this.y, y), Math.min(this.z, z));
    }

    @Override // org.cloudburstmc.math.vector.Vector3i
    @Nonnull
    public Vector3i max(int x, int y, int z) {
        return Vector3i.from(Math.max(this.x, x), Math.max(this.y, y), Math.max(this.z, z));
    }

    @Override // org.cloudburstmc.math.vector.Vector3i
    @Nonnull
    public Vector3i up(int v) {
        return Vector3i.from(this.x, this.y + v, this.z);
    }

    @Override // org.cloudburstmc.math.vector.Vector3i
    @Nonnull
    public Vector3i down(int v) {
        return Vector3i.from(this.x, this.y - v, this.z);
    }

    @Override // org.cloudburstmc.math.vector.Vector3i
    @Nonnull
    public Vector3i north(int v) {
        return Vector3i.from(this.x, this.y, this.z - v);
    }

    @Override // org.cloudburstmc.math.vector.Vector3i
    @Nonnull
    public Vector3i south(int v) {
        return Vector3i.from(this.x, this.y, this.z + v);
    }

    @Override // org.cloudburstmc.math.vector.Vector3i
    @Nonnull
    public Vector3i east(int v) {
        return Vector3i.from(this.x + v, this.y, this.z);
    }

    @Override // org.cloudburstmc.math.vector.Vector3i
    @Nonnull
    public Vector3i west(int v) {
        return Vector3i.from(this.x - v, this.y, this.z);
    }

    @Override // org.cloudburstmc.math.vector.Vector3i
    @Nonnull
    public Vector2i toVector2() {
        return Vector2i.from(this);
    }

    @Override // org.cloudburstmc.math.vector.Vector3i
    @Nonnull
    public Vector2i toVector2(boolean useZ) {
        return Vector2i.from(this.x, useZ ? this.z : this.y);
    }

    @Override // org.cloudburstmc.math.vector.Vector3i
    @Nonnull
    public Vector4i toVector4() {
        return toVector4(0);
    }

    @Override // org.cloudburstmc.math.vector.Vector3i
    @Nonnull
    public Vector4i toVector4(double w) {
        return toVector4(GenericMath.floor(w));
    }

    @Override // org.cloudburstmc.math.vector.Vector3i
    @Nonnull
    public Vector4i toVector4(int w) {
        return Vector4i.from(this, w);
    }

    @Override // org.cloudburstmc.math.vector.Vector3i
    @Nonnull
    public VectorNi toVectorN() {
        return VectorNi.from(this);
    }

    @Override // org.cloudburstmc.math.vector.Vector3i, org.cloudburstmc.math.vector.Vectori
    @Nonnull
    public int[] toArray() {
        return new int[]{this.x, this.y, this.z};
    }

    @Override // org.cloudburstmc.math.vector.Vector3i, org.cloudburstmc.math.vector.Vectori
    @Nonnull
    public Vector3i toInt() {
        return Vector3i.from(this.x, this.y, this.z);
    }

    @Override // org.cloudburstmc.math.vector.Vector3i, org.cloudburstmc.math.vector.Vectori
    @Nonnull
    public Vector3l toLong() {
        return Vector3l.from(this.x, this.y, this.z);
    }

    @Override // org.cloudburstmc.math.vector.Vector3i, org.cloudburstmc.math.vector.Vectori
    @Nonnull
    public Vector3f toFloat() {
        return Vector3f.from(this.x, this.y, this.z);
    }

    @Override // org.cloudburstmc.math.vector.Vector3i, org.cloudburstmc.math.vector.Vectori
    @Nonnull
    public Vector3d toDouble() {
        return Vector3d.from(this.x, this.y, this.z);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vector3i)) {
            return false;
        }
        Vector3i vector3 = (Vector3i) o;
        return vector3.getX() == this.x && vector3.getY() == this.y && vector3.getZ() == this.z;
    }

    public int hashCode() {
        if (!this.hashed) {
            this.hashCode = (((Integer.hashCode(this.x) * bl.cJ) + Integer.hashCode(this.y)) * 97) + Integer.hashCode(this.z);
            this.hashed = true;
        }
        return this.hashCode;
    }
}
