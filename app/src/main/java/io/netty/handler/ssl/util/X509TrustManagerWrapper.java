package io.netty.handler.ssl.util;

import io.netty.util.internal.ObjectUtil;
import java.net.Socket;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.X509ExtendedTrustManager;
import javax.net.ssl.X509TrustManager;

/* loaded from: classes4.dex */
final class X509TrustManagerWrapper extends X509ExtendedTrustManager {
    private final X509TrustManager delegate;

    /* JADX INFO: Access modifiers changed from: package-private */
    public X509TrustManagerWrapper(X509TrustManager delegate) {
        this.delegate = (X509TrustManager) ObjectUtil.checkNotNull(delegate, "delegate");
    }

    @Override // javax.net.ssl.X509TrustManager
    public void checkClientTrusted(X509Certificate[] chain, String s) throws CertificateException {
        this.delegate.checkClientTrusted(chain, s);
    }

    @Override // javax.net.ssl.X509ExtendedTrustManager
    public void checkClientTrusted(X509Certificate[] chain, String s, Socket socket) throws CertificateException {
        this.delegate.checkClientTrusted(chain, s);
    }

    @Override // javax.net.ssl.X509ExtendedTrustManager
    public void checkClientTrusted(X509Certificate[] chain, String s, SSLEngine sslEngine) throws CertificateException {
        this.delegate.checkClientTrusted(chain, s);
    }

    @Override // javax.net.ssl.X509TrustManager
    public void checkServerTrusted(X509Certificate[] chain, String s) throws CertificateException {
        this.delegate.checkServerTrusted(chain, s);
    }

    @Override // javax.net.ssl.X509ExtendedTrustManager
    public void checkServerTrusted(X509Certificate[] chain, String s, Socket socket) throws CertificateException {
        this.delegate.checkServerTrusted(chain, s);
    }

    @Override // javax.net.ssl.X509ExtendedTrustManager
    public void checkServerTrusted(X509Certificate[] chain, String s, SSLEngine sslEngine) throws CertificateException {
        this.delegate.checkServerTrusted(chain, s);
    }

    @Override // javax.net.ssl.X509TrustManager
    public X509Certificate[] getAcceptedIssuers() {
        return this.delegate.getAcceptedIssuers();
    }
}
