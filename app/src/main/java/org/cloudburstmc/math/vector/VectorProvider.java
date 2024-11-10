package org.cloudburstmc.math.vector;

/* loaded from: classes5.dex */
public interface VectorProvider {
    Vector2d createVector2d(double d, double d2);

    Vector2f createVector2f(float f, float f2);

    Vector2i createVector2i(int i, int i2);

    Vector2l createVector2l(long j, long j2);

    Vector3d createVector3d(double d, double d2, double d3);

    Vector3f createVector3f(float f, float f2, float f3);

    Vector3i createVector3i(int i, int i2, int i3);

    Vector3l createVector3l(long j, long j2, long j3);

    Vector4d createVector4d(double d, double d2, double d3, double d4);

    Vector4f createVector4f(float f, float f2, float f3, float f4);

    Vector4i createVector4i(int i, int i2, int i3, int i4);

    Vector4l createVector4l(long j, long j2, long j3, long j4);
}
