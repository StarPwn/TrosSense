package org.cloudburstmc.math.matrix;

import javax.annotation.Nonnull;
import org.cloudburstmc.math.vector.Vectorf;

/* loaded from: classes5.dex */
public interface Matrixf {
    @Nonnull
    Matrixf abs();

    @Nonnull
    Matrixf ceil();

    float determinant();

    @Nonnull
    Matrixf div(float f);

    @Nonnull
    Matrixf floor();

    float get(int i, int i2);

    @Nonnull
    Vectorf getColumn(int i);

    @Nonnull
    Vectorf getRow(int i);

    @Nonnull
    Matrixf invert();

    @Nonnull
    Matrixf mul(float f);

    @Nonnull
    Matrixf negate();

    @Nonnull
    Matrixf pow(float f);

    @Nonnull
    Matrixf round();

    @Nonnull
    float[] toArray(boolean z);

    @Nonnull
    Matrixd toDouble();

    @Nonnull
    Matrixf toFloat();

    float trace();

    @Nonnull
    Matrixf transpose();
}
