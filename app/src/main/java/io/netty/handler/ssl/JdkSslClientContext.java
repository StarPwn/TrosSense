package io.netty.handler.ssl;

import java.io.File;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.cert.X509Certificate;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLException;
import javax.net.ssl.TrustManagerFactory;

@Deprecated
/* loaded from: classes4.dex */
public final class JdkSslClientContext extends JdkSslContext {
    @Deprecated
    public JdkSslClientContext() throws SSLException {
        this(null, null);
    }

    @Deprecated
    public JdkSslClientContext(File certChainFile) throws SSLException {
        this(certChainFile, null);
    }

    @Deprecated
    public JdkSslClientContext(TrustManagerFactory trustManagerFactory) throws SSLException {
        this(null, trustManagerFactory);
    }

    @Deprecated
    public JdkSslClientContext(File certChainFile, TrustManagerFactory trustManagerFactory) throws SSLException {
        this(certChainFile, trustManagerFactory, (Iterable<String>) null, IdentityCipherSuiteFilter.INSTANCE, JdkDefaultApplicationProtocolNegotiator.INSTANCE, 0L, 0L);
    }

    @Deprecated
    public JdkSslClientContext(File certChainFile, TrustManagerFactory trustManagerFactory, Iterable<String> ciphers, Iterable<String> nextProtocols, long sessionCacheSize, long sessionTimeout) throws SSLException {
        this(certChainFile, trustManagerFactory, ciphers, IdentityCipherSuiteFilter.INSTANCE, toNegotiator(toApplicationProtocolConfig(nextProtocols), false), sessionCacheSize, sessionTimeout);
    }

    @Deprecated
    public JdkSslClientContext(File certChainFile, TrustManagerFactory trustManagerFactory, Iterable<String> ciphers, CipherSuiteFilter cipherFilter, ApplicationProtocolConfig apn, long sessionCacheSize, long sessionTimeout) throws SSLException {
        this(certChainFile, trustManagerFactory, ciphers, cipherFilter, toNegotiator(apn, false), sessionCacheSize, sessionTimeout);
    }

    @Deprecated
    public JdkSslClientContext(File certChainFile, TrustManagerFactory trustManagerFactory, Iterable<String> ciphers, CipherSuiteFilter cipherFilter, JdkApplicationProtocolNegotiator apn, long sessionCacheSize, long sessionTimeout) throws SSLException {
        this(null, certChainFile, trustManagerFactory, ciphers, cipherFilter, apn, sessionCacheSize, sessionTimeout);
    }

    JdkSslClientContext(Provider provider, File trustCertCollectionFile, TrustManagerFactory trustManagerFactory, Iterable<String> ciphers, CipherSuiteFilter cipherFilter, JdkApplicationProtocolNegotiator apn, long sessionCacheSize, long sessionTimeout) throws SSLException {
        super(newSSLContext(provider, toX509CertificatesInternal(trustCertCollectionFile), trustManagerFactory, null, null, null, null, sessionCacheSize, sessionTimeout, KeyStore.getDefaultType()), true, ciphers, cipherFilter, apn, ClientAuth.NONE, (String[]) null, false);
    }

    @Deprecated
    public JdkSslClientContext(File trustCertCollectionFile, TrustManagerFactory trustManagerFactory, File keyCertChainFile, File keyFile, String keyPassword, KeyManagerFactory keyManagerFactory, Iterable<String> ciphers, CipherSuiteFilter cipherFilter, ApplicationProtocolConfig apn, long sessionCacheSize, long sessionTimeout) throws SSLException {
        this(trustCertCollectionFile, trustManagerFactory, keyCertChainFile, keyFile, keyPassword, keyManagerFactory, ciphers, cipherFilter, toNegotiator(apn, false), sessionCacheSize, sessionTimeout);
    }

    @Deprecated
    public JdkSslClientContext(File trustCertCollectionFile, TrustManagerFactory trustManagerFactory, File keyCertChainFile, File keyFile, String keyPassword, KeyManagerFactory keyManagerFactory, Iterable<String> ciphers, CipherSuiteFilter cipherFilter, JdkApplicationProtocolNegotiator apn, long sessionCacheSize, long sessionTimeout) throws SSLException {
        super(newSSLContext(null, toX509CertificatesInternal(trustCertCollectionFile), trustManagerFactory, toX509CertificatesInternal(keyCertChainFile), toPrivateKeyInternal(keyFile, keyPassword), keyPassword, keyManagerFactory, sessionCacheSize, sessionTimeout, KeyStore.getDefaultType()), true, ciphers, cipherFilter, apn, ClientAuth.NONE, (String[]) null, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public JdkSslClientContext(Provider sslContextProvider, X509Certificate[] trustCertCollection, TrustManagerFactory trustManagerFactory, X509Certificate[] keyCertChain, PrivateKey key, String keyPassword, KeyManagerFactory keyManagerFactory, Iterable<String> ciphers, CipherSuiteFilter cipherFilter, ApplicationProtocolConfig apn, String[] protocols, long sessionCacheSize, long sessionTimeout, String keyStoreType) throws SSLException {
        super(newSSLContext(sslContextProvider, trustCertCollection, trustManagerFactory, keyCertChain, key, keyPassword, keyManagerFactory, sessionCacheSize, sessionTimeout, keyStoreType), true, ciphers, cipherFilter, toNegotiator(apn, false), ClientAuth.NONE, protocols, false);
    }

    /* JADX WARN: Removed duplicated region for block: B:36:0x0083  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x0087  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static javax.net.ssl.SSLContext newSSLContext(java.security.Provider r15, java.security.cert.X509Certificate[] r16, javax.net.ssl.TrustManagerFactory r17, java.security.cert.X509Certificate[] r18, java.security.PrivateKey r19, java.lang.String r20, javax.net.ssl.KeyManagerFactory r21, long r22, long r24, java.lang.String r26) throws javax.net.ssl.SSLException {
        /*
            r1 = r15
            r2 = r16
            r3 = r22
            r5 = r24
            if (r2 == 0) goto L19
            r7 = r17
            r13 = r26
            javax.net.ssl.TrustManagerFactory r0 = buildTrustManagerFactory(r2, r7, r13)     // Catch: java.lang.Exception -> L13
            r14 = r0
            goto L1e
        L13:
            r0 = move-exception
            r14 = r7
            r7 = r21
            goto L7f
        L19:
            r7 = r17
            r13 = r26
            r14 = r7
        L1e:
            if (r18 == 0) goto L35
            r8 = 0
            r7 = r18
            r9 = r19
            r10 = r20
            r11 = r21
            r12 = r26
            javax.net.ssl.KeyManagerFactory r0 = buildKeyManagerFactory(r7, r8, r9, r10, r11, r12)     // Catch: java.lang.Exception -> L31
            r7 = r0
            goto L37
        L31:
            r0 = move-exception
            r7 = r21
            goto L7f
        L35:
            r7 = r21
        L37:
            java.lang.String r0 = "TLS"
            if (r1 != 0) goto L40
            javax.net.ssl.SSLContext r0 = javax.net.ssl.SSLContext.getInstance(r0)     // Catch: java.lang.Exception -> L7e
            goto L44
        L40:
            javax.net.ssl.SSLContext r0 = javax.net.ssl.SSLContext.getInstance(r0, r15)     // Catch: java.lang.Exception -> L7e
        L44:
            r8 = 0
            if (r7 != 0) goto L4a
            r9 = r8
            goto L4e
        L4a:
            javax.net.ssl.KeyManager[] r9 = r7.getKeyManagers()     // Catch: java.lang.Exception -> L7e
        L4e:
            if (r14 != 0) goto L52
            r10 = r8
            goto L56
        L52:
            javax.net.ssl.TrustManager[] r10 = r14.getTrustManagers()     // Catch: java.lang.Exception -> L7e
        L56:
            r0.init(r9, r10, r8)     // Catch: java.lang.Exception -> L7e
            javax.net.ssl.SSLSessionContext r8 = r0.getClientSessionContext()     // Catch: java.lang.Exception -> L7e
            r9 = 0
            int r11 = (r3 > r9 ? 1 : (r3 == r9 ? 0 : -1))
            r9 = 2147483647(0x7fffffff, double:1.060997895E-314)
            if (r11 <= 0) goto L6f
            long r11 = java.lang.Math.min(r3, r9)     // Catch: java.lang.Exception -> L7e
            int r11 = (int) r11     // Catch: java.lang.Exception -> L7e
            r8.setSessionCacheSize(r11)     // Catch: java.lang.Exception -> L7e
        L6f:
            r11 = 0
            int r11 = (r5 > r11 ? 1 : (r5 == r11 ? 0 : -1))
            if (r11 <= 0) goto L7d
            long r9 = java.lang.Math.min(r5, r9)     // Catch: java.lang.Exception -> L7e
            int r9 = (int) r9     // Catch: java.lang.Exception -> L7e
            r8.setSessionTimeout(r9)     // Catch: java.lang.Exception -> L7e
        L7d:
            return r0
        L7e:
            r0 = move-exception
        L7f:
            boolean r8 = r0 instanceof javax.net.ssl.SSLException
            if (r8 == 0) goto L87
            r8 = r0
            javax.net.ssl.SSLException r8 = (javax.net.ssl.SSLException) r8
            throw r8
        L87:
            javax.net.ssl.SSLException r8 = new javax.net.ssl.SSLException
            java.lang.String r9 = "failed to initialize the client-side SSL context"
            r8.<init>(r9, r0)
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.handler.ssl.JdkSslClientContext.newSSLContext(java.security.Provider, java.security.cert.X509Certificate[], javax.net.ssl.TrustManagerFactory, java.security.cert.X509Certificate[], java.security.PrivateKey, java.lang.String, javax.net.ssl.KeyManagerFactory, long, long, java.lang.String):javax.net.ssl.SSLContext");
    }
}
