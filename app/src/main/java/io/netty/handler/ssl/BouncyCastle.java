package io.netty.handler.ssl;

import javax.net.ssl.SSLEngine;

/* loaded from: classes4.dex */
final class BouncyCastle {
    private static final boolean BOUNCY_CASTLE_ON_CLASSPATH;

    static {
        boolean bcOnClasspath = false;
        try {
            Class.forName("org.bouncycastle.jsse.provider.BouncyCastleJsseProvider");
            bcOnClasspath = true;
        } catch (Throwable th) {
        }
        BOUNCY_CASTLE_ON_CLASSPATH = bcOnClasspath;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isAvailable() {
        return BOUNCY_CASTLE_ON_CLASSPATH;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isInUse(SSLEngine engine) {
        return engine.getClass().getPackage().getName().startsWith("org.bouncycastle.jsse.provider");
    }

    private BouncyCastle() {
    }
}
