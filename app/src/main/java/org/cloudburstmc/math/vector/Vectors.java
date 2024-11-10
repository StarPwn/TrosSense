package org.cloudburstmc.math.vector;

import java.util.ServiceLoader;
import org.cloudburstmc.math.immutable.vector.ImmutableVectorProvider;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes5.dex */
public class Vectors {
    private static final ServiceLoader<VectorProvider> VECTOR_PROVIDER_LOADER = ServiceLoader.load(VectorProvider.class);
    private static VectorProvider cached;

    Vectors() {
    }

    public static VectorProvider provider() {
        if (cached != null) {
            return cached;
        }
        ImmutableVectorProvider immutableVectorProvider = new ImmutableVectorProvider();
        cached = immutableVectorProvider;
        return immutableVectorProvider;
    }

    public static Vector2d createVector2d(double x, double y) {
        return provider().createVector2d(x, y);
    }

    public static Vector2f createVector2f(float x, float y) {
        return provider().createVector2f(x, y);
    }

    public static Vector2i createVector2i(int x, int y) {
        return provider().createVector2i(x, y);
    }

    public static Vector2l createVector2l(long x, long y) {
        return provider().createVector2l(x, y);
    }

    public static Vector3d createVector3d(double x, double y, double z) {
        return provider().createVector3d(x, y, z);
    }

    public static Vector3f createVector3f(float x, float y, float z) {
        return provider().createVector3f(x, y, z);
    }

    public static Vector3i createVector3i(int x, int y, int z) {
        return provider().createVector3i(x, y, z);
    }

    public static Vector3l createVector3l(long x, long y, long z) {
        return provider().createVector3l(x, y, z);
    }

    public static Vector4d createVector4d(double x, double y, double z, double w) {
        return provider().createVector4d(x, y, z, w);
    }

    public static Vector4f createVector4f(float x, float y, float z, float w) {
        return provider().createVector4f(x, y, z, w);
    }

    public static Vector4i createVector4i(int x, int y, int z, int w) {
        return provider().createVector4i(x, y, z, w);
    }

    public static Vector4l createVector4l(long x, long y, long z, long w) {
        return provider().createVector4l(x, y, z, w);
    }
}
