package io.netty.handler.codec.serialization;

/* loaded from: classes4.dex */
class ClassLoaderClassResolver implements ClassResolver {
    private final ClassLoader classLoader;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ClassLoaderClassResolver(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override // io.netty.handler.codec.serialization.ClassResolver
    public Class<?> resolve(String className) throws ClassNotFoundException {
        try {
            return this.classLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
            return Class.forName(className, false, this.classLoader);
        }
    }
}
