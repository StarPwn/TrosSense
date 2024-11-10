package org.cloudburstmc.math.imaginary;

import javax.annotation.Nonnull;

/* loaded from: classes5.dex */
public interface Imaginaryd {
    @Nonnull
    Imaginaryd conjugate();

    @Nonnull
    Imaginaryd div(double d);

    @Nonnull
    Imaginaryd invert();

    double length();

    double lengthSquared();

    @Nonnull
    Imaginaryd mul(double d);

    @Nonnull
    Imaginaryd normalize();

    @Nonnull
    Imaginaryd toDouble();

    @Nonnull
    Imaginaryf toFloat();
}
