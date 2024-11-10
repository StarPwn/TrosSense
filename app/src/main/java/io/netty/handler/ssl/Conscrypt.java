package io.netty.handler.ssl;

import io.netty.util.internal.PlatformDependent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.net.ssl.SSLEngine;

/* loaded from: classes4.dex */
final class Conscrypt {
    private static final Method IS_CONSCRYPT_SSLENGINE;

    static {
        Method isConscryptSSLEngine = null;
        if ((PlatformDependent.javaVersion() >= 8 && PlatformDependent.javaVersion() < 15) || PlatformDependent.isAndroid()) {
            try {
                Class<?> providerClass = Class.forName("org.conscrypt.OpenSSLProvider", true, PlatformDependent.getClassLoader(ConscryptAlpnSslEngine.class));
                providerClass.newInstance();
                Class<?> conscryptClass = Class.forName("org.conscrypt.Conscrypt", true, PlatformDependent.getClassLoader(ConscryptAlpnSslEngine.class));
                isConscryptSSLEngine = conscryptClass.getMethod("isConscrypt", SSLEngine.class);
            } catch (Throwable th) {
            }
        }
        IS_CONSCRYPT_SSLENGINE = isConscryptSSLEngine;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isAvailable() {
        return IS_CONSCRYPT_SSLENGINE != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isEngineSupported(SSLEngine engine) {
        try {
            if (IS_CONSCRYPT_SSLENGINE != null) {
                return ((Boolean) IS_CONSCRYPT_SSLENGINE.invoke(null, engine)).booleanValue();
            }
            return false;
        } catch (IllegalAccessException e) {
            return false;
        } catch (InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
    }

    private Conscrypt() {
    }
}
