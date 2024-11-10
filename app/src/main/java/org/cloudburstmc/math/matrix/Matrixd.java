package org.cloudburstmc.math.matrix;

import javax.annotation.Nonnull;
import org.cloudburstmc.math.vector.Vectord;

/* loaded from: classes5.dex */
public interface Matrixd {
    @Nonnull
    Matrixd abs();

    @Nonnull
    Matrixd ceil();

    double determinant();

    @Nonnull
    Matrixd div(double d);

    @Nonnull
    Matrixd floor();

    double get(int i, int i2);

    @Nonnull
    Vectord getColumn(int i);

    @Nonnull
    Vectord getRow(int i);

    @Nonnull
    Matrixd invert();

    @Nonnull
    Matrixd mul(double d);

    @Nonnull
    Matrixd negate();

    @Nonnull
    Matrixd pow(double d);

    @Nonnull
    Matrixd round();

    @Nonnull
    double[] toArray(boolean z);

    @Nonnull
    Matrixd toDouble();

    @Nonnull
    Matrixf toFloat();

    double trace();

    @Nonnull
    Matrixd transpose();
}
