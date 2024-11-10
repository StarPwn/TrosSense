package io.netty.util.internal;

/* loaded from: classes4.dex */
public final class ClassInitializerUtil {
    private ClassInitializerUtil() {
    }

    public static void tryLoadClasses(Class<?> loadingClass, Class<?>... classes) {
        ClassLoader loader = PlatformDependent.getClassLoader(loadingClass);
        for (Class<?> clazz : classes) {
            tryLoadClass(loader, clazz.getName());
        }
    }

    private static void tryLoadClass(ClassLoader classLoader, String className) {
        try {
            Class.forName(className, true, classLoader);
        } catch (ClassNotFoundException e) {
        } catch (SecurityException e2) {
        }
    }
}
