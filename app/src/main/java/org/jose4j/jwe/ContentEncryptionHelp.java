package org.jose4j.jwe;

import org.jose4j.jca.ProviderContext;
import org.jose4j.jwx.Headers;

/* loaded from: classes5.dex */
class ContentEncryptionHelp {
    ContentEncryptionHelp() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String getCipherProvider(Headers headers, ProviderContext providerCtx) {
        ProviderContext.Context ctx = choseContext(headers, providerCtx);
        return ctx.getCipherProvider();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String getMacProvider(Headers headers, ProviderContext providerContext) {
        ProviderContext.Context ctx = choseContext(headers, providerContext);
        return ctx.getMacProvider();
    }

    private static ProviderContext.Context choseContext(Headers headers, ProviderContext providerCtx) {
        boolean isDir = headers != null && KeyManagementAlgorithmIdentifiers.DIRECT.equals(headers.getStringHeaderValue("alg"));
        return isDir ? providerCtx.getSuppliedKeyProviderContext() : providerCtx.getGeneralProviderContext();
    }
}
