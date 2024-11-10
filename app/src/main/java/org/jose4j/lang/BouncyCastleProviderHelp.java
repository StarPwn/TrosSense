package org.jose4j.lang;

import java.security.Provider;
import java.security.Security;

/* loaded from: classes5.dex */
public class BouncyCastleProviderHelp {
    private static final String BC_PROVIDER_FQCN = "org.bouncycastle.jce.provider.BouncyCastleProvider";

    public static boolean enableBouncyCastleProvider() {
        try {
            Class<?> cls = Class.forName(BC_PROVIDER_FQCN);
            for (Provider provider : Security.getProviders()) {
                if (cls.isInstance(provider)) {
                    return true;
                }
            }
            Security.addProvider((Provider) cls.newInstance());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
