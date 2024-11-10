package org.cloudburstmc.math.imaginary;

import javax.annotation.Nonnull;

/* loaded from: classes5.dex */
public interface Imaginaryf {
    @Nonnull
    Imaginaryf conjugate();

    @Nonnull
    Imaginaryf div(float f);

    @Nonnull
    Imaginaryf invert();

    float length();

    float lengthSquared();

    @Nonnull
    Imaginaryf mul(float f);

    @Nonnull
    Imaginaryf normalize();

    @Nonnull
    Imaginaryd toDouble();

    @Nonnull
    Imaginaryf toFloat();
}
