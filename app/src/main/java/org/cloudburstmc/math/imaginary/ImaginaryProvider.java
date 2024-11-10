package org.cloudburstmc.math.imaginary;

/* loaded from: classes5.dex */
public interface ImaginaryProvider {
    Complexd createComplexd(double d, double d2);

    Complexf createComplexf(float f, float f2);

    Quaterniond createQuaterniond(double d, double d2, double d3, double d4);

    Quaternionf createQuaternionf(float f, float f2, float f3, float f4);
}
