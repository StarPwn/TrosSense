package org.cloudburstmc.math.immutable.vector;

import org.cloudburstmc.math.vector.Vector2d;
import org.cloudburstmc.math.vector.Vector2f;
import org.cloudburstmc.math.vector.Vector2i;
import org.cloudburstmc.math.vector.Vector2l;
import org.cloudburstmc.math.vector.Vector3d;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.math.vector.Vector3l;
import org.cloudburstmc.math.vector.Vector4d;
import org.cloudburstmc.math.vector.Vector4f;
import org.cloudburstmc.math.vector.Vector4i;
import org.cloudburstmc.math.vector.Vector4l;
import org.cloudburstmc.math.vector.VectorProvider;

/* loaded from: classes5.dex */
public class ImmutableVectorProvider implements VectorProvider {
    @Override // org.cloudburstmc.math.vector.VectorProvider
    public Vector2d createVector2d(double x, double y) {
        return new ImmutableVector2d(x, y);
    }

    @Override // org.cloudburstmc.math.vector.VectorProvider
    public Vector2f createVector2f(float x, float y) {
        return new ImmutableVector2f(x, y);
    }

    @Override // org.cloudburstmc.math.vector.VectorProvider
    public Vector2i createVector2i(int x, int y) {
        return new ImmutableVector2i(x, y);
    }

    @Override // org.cloudburstmc.math.vector.VectorProvider
    public Vector2l createVector2l(long x, long y) {
        return new ImmutableVector2l(x, y);
    }

    @Override // org.cloudburstmc.math.vector.VectorProvider
    public Vector3d createVector3d(double x, double y, double z) {
        return new ImmutableVector3d(x, y, z);
    }

    @Override // org.cloudburstmc.math.vector.VectorProvider
    public Vector3f createVector3f(float x, float y, float z) {
        return new ImmutableVector3f(x, y, z);
    }

    @Override // org.cloudburstmc.math.vector.VectorProvider
    public Vector3i createVector3i(int x, int y, int z) {
        return new ImmutableVector3i(x, y, z);
    }

    @Override // org.cloudburstmc.math.vector.VectorProvider
    public Vector3l createVector3l(long x, long y, long z) {
        return new ImmutableVector3l(x, y, z);
    }

    @Override // org.cloudburstmc.math.vector.VectorProvider
    public Vector4d createVector4d(double x, double y, double z, double w) {
        return new ImmutableVector4d(x, y, z, w);
    }

    @Override // org.cloudburstmc.math.vector.VectorProvider
    public Vector4f createVector4f(float x, float y, float z, float w) {
        return new ImmutableVector4f(x, y, z, w);
    }

    @Override // org.cloudburstmc.math.vector.VectorProvider
    public Vector4i createVector4i(int x, int y, int z, int w) {
        return new ImmutableVector4i(x, y, z, w);
    }

    @Override // org.cloudburstmc.math.vector.VectorProvider
    public Vector4l createVector4l(long x, long y, long z, long w) {
        return new ImmutableVector4l(x, y, z, w);
    }
}
