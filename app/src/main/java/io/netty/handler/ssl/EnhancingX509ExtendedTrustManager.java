package io.netty.handler.ssl;

import java.net.Socket;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.List;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.X509ExtendedTrustManager;
import javax.net.ssl.X509TrustManager;

/* loaded from: classes4.dex */
final class EnhancingX509ExtendedTrustManager extends X509ExtendedTrustManager {
    private final X509ExtendedTrustManager wrapped;

    /* JADX INFO: Access modifiers changed from: package-private */
    public EnhancingX509ExtendedTrustManager(X509TrustManager wrapped) {
        this.wrapped = (X509ExtendedTrustManager) wrapped;
    }

    @Override // javax.net.ssl.X509ExtendedTrustManager
    public void checkClientTrusted(X509Certificate[] chain, String authType, Socket socket) throws CertificateException {
        this.wrapped.checkClientTrusted(chain, authType, socket);
    }

    @Override // javax.net.ssl.X509ExtendedTrustManager
    public void checkServerTrusted(X509Certificate[] chain, String authType, Socket socket) throws CertificateException {
        try {
            this.wrapped.checkServerTrusted(chain, authType, socket);
        } catch (CertificateException e) {
            throwEnhancedCertificateException(chain, e);
        }
    }

    @Override // javax.net.ssl.X509ExtendedTrustManager
    public void checkClientTrusted(X509Certificate[] chain, String authType, SSLEngine engine) throws CertificateException {
        this.wrapped.checkClientTrusted(chain, authType, engine);
    }

    @Override // javax.net.ssl.X509ExtendedTrustManager
    public void checkServerTrusted(X509Certificate[] chain, String authType, SSLEngine engine) throws CertificateException {
        try {
            this.wrapped.checkServerTrusted(chain, authType, engine);
        } catch (CertificateException e) {
            throwEnhancedCertificateException(chain, e);
        }
    }

    @Override // javax.net.ssl.X509TrustManager
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        this.wrapped.checkClientTrusted(chain, authType);
    }

    @Override // javax.net.ssl.X509TrustManager
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        try {
            this.wrapped.checkServerTrusted(chain, authType);
        } catch (CertificateException e) {
            throwEnhancedCertificateException(chain, e);
        }
    }

    @Override // javax.net.ssl.X509TrustManager
    public X509Certificate[] getAcceptedIssuers() {
        return this.wrapped.getAcceptedIssuers();
    }

    private static void throwEnhancedCertificateException(X509Certificate[] chain, CertificateException e) throws CertificateException {
        String message = e.getMessage();
        if (message == null) {
            throw e;
        }
        if (e.getMessage().startsWith("No subject alternative DNS name matching")) {
            StringBuilder names = new StringBuilder(64);
            for (X509Certificate cert : chain) {
                Collection<List<?>> collection = cert.getSubjectAlternativeNames();
                if (collection != null) {
                    for (List<?> altNames : collection) {
                        if (altNames.size() >= 2 && ((Integer) altNames.get(0)).intValue() == 2) {
                            names.append((String) altNames.get(1)).append(",");
                        }
                    }
                }
            }
            int i = names.length();
            if (i != 0) {
                names.setLength(names.length() - 1);
                throw new CertificateException(message + " Subject alternative DNS names in the certificate chain of " + chain.length + " certificate(s): " + ((Object) names), e);
            }
            throw e;
        }
        throw e;
    }
}
