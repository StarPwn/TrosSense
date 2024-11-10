package io.netty.util.internal;

/* loaded from: classes4.dex */
final class NativeLibraryUtil {
    public static void loadLibrary(String libName, boolean absolute) {
        if (absolute) {
            System.load(libName);
        } else {
            System.loadLibrary(libName);
        }
    }

    private NativeLibraryUtil() {
    }
}
