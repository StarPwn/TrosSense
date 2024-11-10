package org.cloudburstmc.math.vector;

import javax.annotation.Nonnull;

/* loaded from: classes5.dex */
public interface Vectord {
    @Nonnull
    Vectord abs();

    @Nonnull
    Vectord ceil();

    @Nonnull
    Vectord div(double d);

    @Nonnull
    Vectord floor();

    int getMaxAxis();

    int getMinAxis();

    double length();

    double lengthSquared();

    @Nonnull
    Vectord mul(double d);

    @Nonnull
    Vectord negate();

    @Nonnull
    Vectord normalize();

    @Nonnull
    Vectord pow(double d);

    @Nonnull
    Vectord round();

    @Nonnull
    double[] toArray();

    @Nonnull
    Vectord toDouble();

    @Nonnull
    Vectorf toFloat();

    @Nonnull
    Vectori toInt();

    @Nonnull
    Vectorl toLong();
}
