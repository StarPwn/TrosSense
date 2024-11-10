package org.cloudburstmc.math.vector;

import javax.annotation.Nonnull;

/* loaded from: classes5.dex */
public interface Vectori {
    @Nonnull
    Vectori abs();

    @Nonnull
    Vectori div(int i);

    int getMaxAxis();

    int getMinAxis();

    float length();

    int lengthSquared();

    @Nonnull
    Vectori mul(int i);

    @Nonnull
    Vectori negate();

    @Nonnull
    Vectori pow(int i);

    @Nonnull
    int[] toArray();

    @Nonnull
    Vectord toDouble();

    @Nonnull
    Vectorf toFloat();

    @Nonnull
    Vectori toInt();

    @Nonnull
    Vectorl toLong();
}
