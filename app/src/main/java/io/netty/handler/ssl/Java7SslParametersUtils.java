package io.netty.handler.ssl;

import java.security.AlgorithmConstraints;
import javax.net.ssl.SSLParameters;

/* loaded from: classes4.dex */
final class Java7SslParametersUtils {
    private Java7SslParametersUtils() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void setAlgorithmConstraints(SSLParameters sslParameters, Object algorithmConstraints) {
        sslParameters.setAlgorithmConstraints((AlgorithmConstraints) algorithmConstraints);
    }
}
