package org.cloudburstmc.math.vector;

import javax.annotation.Nonnull;

/* loaded from: classes5.dex */
public interface Vectorl {
    @Nonnull
    Vectorl abs();

    @Nonnull
    Vectorl div(long j);

    int getMaxAxis();

    int getMinAxis();

    double length();

    long lengthSquared();

    @Nonnull
    Vectorl mul(long j);

    @Nonnull
    Vectorl negate();

    @Nonnull
    Vectorl pow(long j);

    @Nonnull
    long[] toArray();

    @Nonnull
    Vectord toDouble();

    @Nonnull
    Vectorf toFloat();

    @Nonnull
    Vectori toInt();

    @Nonnull
    Vectorl toLong();
}
