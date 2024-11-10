package org.cloudburstmc.math.vector;

import javax.annotation.Nonnull;

/* loaded from: classes5.dex */
public interface Vectorf {
    @Nonnull
    Vectorf abs();

    @Nonnull
    Vectorf ceil();

    @Nonnull
    Vectorf div(float f);

    @Nonnull
    Vectorf floor();

    int getMaxAxis();

    int getMinAxis();

    float length();

    float lengthSquared();

    @Nonnull
    Vectorf mul(float f);

    @Nonnull
    Vectorf negate();

    @Nonnull
    Vectorf normalize();

    @Nonnull
    Vectorf pow(float f);

    @Nonnull
    Vectorf round();

    @Nonnull
    float[] toArray();

    @Nonnull
    Vectord toDouble();

    @Nonnull
    Vectorf toFloat();

    @Nonnull
    Vectori toInt();

    @Nonnull
    Vectorl toLong();
}
