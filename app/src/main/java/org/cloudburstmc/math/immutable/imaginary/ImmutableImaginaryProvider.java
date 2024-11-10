package org.cloudburstmc.math.immutable.imaginary;

import org.cloudburstmc.math.imaginary.Complexd;
import org.cloudburstmc.math.imaginary.Complexf;
import org.cloudburstmc.math.imaginary.ImaginaryProvider;
import org.cloudburstmc.math.imaginary.Quaterniond;
import org.cloudburstmc.math.imaginary.Quaternionf;

/* loaded from: classes5.dex */
public class ImmutableImaginaryProvider implements ImaginaryProvider {
    @Override // org.cloudburstmc.math.imaginary.ImaginaryProvider
    public Complexd createComplexd(double x, double y) {
        return new ImmutableComplexd(x, y);
    }

    @Override // org.cloudburstmc.math.imaginary.ImaginaryProvider
    public Complexf createComplexf(float x, float y) {
        return new ImmutableComplexf(x, y);
    }

    @Override // org.cloudburstmc.math.imaginary.ImaginaryProvider
    public Quaterniond createQuaterniond(double x, double y, double z, double w) {
        return new ImmutableQuaterniond(x, y, z, w);
    }

    @Override // org.cloudburstmc.math.imaginary.ImaginaryProvider
    public Quaternionf createQuaternionf(float x, float y, float z, float w) {
        return new ImmutableQuaternionf(x, y, z, w);
    }
}
