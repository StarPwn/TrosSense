package org.cloudburstmc.math.imaginary;

import java.util.Iterator;
import java.util.ServiceLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes5.dex */
public class Imaginary {
    private static final ServiceLoader<ImaginaryProvider> IMAGINARY_PROVIDER_LOADER = ServiceLoader.load(ImaginaryProvider.class);
    private static ImaginaryProvider cached;

    Imaginary() {
    }

    public static ImaginaryProvider provider() {
        if (cached != null) {
            return cached;
        }
        Iterator<ImaginaryProvider> iterator = IMAGINARY_PROVIDER_LOADER.iterator();
        if (!iterator.hasNext()) {
            throw new RuntimeException("Could not initialize imaginary provider as no implementation was provided!");
        }
        ImaginaryProvider next = iterator.next();
        cached = next;
        return next;
    }

    public static Complexd createComplexd(double x, double y) {
        return provider().createComplexd(x, y);
    }

    public static Complexf createComplexf(float x, float y) {
        return provider().createComplexf(x, y);
    }

    public static Quaterniond createQuaterniond(double x, double y, double z, double w) {
        return provider().createQuaterniond(x, y, z, w);
    }

    public static Quaternionf createQuaternionf(float x, float y, float z, float w) {
        return provider().createQuaternionf(x, y, z, w);
    }
}
