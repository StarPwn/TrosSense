package io.netty.handler.ssl;

import io.netty.util.CharsetUtil;
import io.netty.util.internal.PlatformDependent;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.NoSuchPaddingException;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509ExtendedTrustManager;

@Deprecated
/* loaded from: classes4.dex */
public final class JdkSslServerContext extends JdkSslContext {
    private static final boolean WRAP_TRUST_MANAGER;

    static {
        boolean wrapTrustManager = false;
        if (PlatformDependent.javaVersion() >= 7) {
            try {
                checkIfWrappingTrustManagerIsSupported();
                wrapTrustManager = true;
            } catch (Throwable th) {
            }
        }
        WRAP_TRUST_MANAGER = wrapTrustManager;
    }

    static void checkIfWrappingTrustManagerIsSupported() throws CertificateException, InvalidAlgorithmParameterException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, IOException, KeyException, KeyStoreException, UnrecoverableKeyException {
        X509Certificate[] certs = toX509Certificates(new ByteArrayInputStream("-----BEGIN CERTIFICATE-----\nMIICrjCCAZagAwIBAgIIdSvQPv1QAZQwDQYJKoZIhvcNAQELBQAwFjEUMBIGA1UEAxMLZXhhbXBs\nZS5jb20wIBcNMTgwNDA2MjIwNjU5WhgPOTk5OTEyMzEyMzU5NTlaMBYxFDASBgNVBAMTC2V4YW1w\nbGUuY29tMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAggbWsmDQ6zNzRZ5AW8E3eoGl\nqWvOBDb5Fs1oBRrVQHuYmVAoaqwDzXYJ0LOwa293AgWEQ1jpcbZ2hpoYQzqEZBTLnFhMrhRFlH6K\nbJND8Y33kZ/iSVBBDuGbdSbJShlM+4WwQ9IAso4MZ4vW3S1iv5fGGpLgbtXRmBf/RU8omN0Gijlv\nWlLWHWijLN8xQtySFuBQ7ssW8RcKAary3pUm6UUQB+Co6lnfti0Tzag8PgjhAJq2Z3wbsGRnP2YS\nvYoaK6qzmHXRYlp/PxrjBAZAmkLJs4YTm/XFF+fkeYx4i9zqHbyone5yerRibsHaXZWLnUL+rFoe\nMdKvr0VS3sGmhQIDAQABMA0GCSqGSIb3DQEBCwUAA4IBAQADQi441pKmXf9FvUV5EHU4v8nJT9Iq\nyqwsKwXnr7AsUlDGHBD7jGrjAXnG5rGxuNKBQ35wRxJATKrUtyaquFUL6H8O6aGQehiFTk6zmPbe\n12Gu44vqqTgIUxnv3JQJiox8S2hMxsSddpeCmSdvmalvD6WG4NthH6B9ZaBEiep1+0s0RUaBYn73\nI7CCUaAtbjfR6pcJjrFk5ei7uwdQZFSJtkP2z8r7zfeANJddAKFlkaMWn7u+OIVuB4XPooWicObk\nNAHFtP65bocUYnDpTVdiyvn8DdqyZ/EO8n1bBKBzuSLplk2msW4pdgaFgY7Vw/0wzcFXfUXmL1uy\nG8sQD/wx\n-----END CERTIFICATE-----".getBytes(CharsetUtil.US_ASCII)));
        PrivateKey privateKey = toPrivateKey(new ByteArrayInputStream("-----BEGIN PRIVATE KEY-----\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCCBtayYNDrM3NFnkBbwTd6gaWp\na84ENvkWzWgFGtVAe5iZUChqrAPNdgnQs7Brb3cCBYRDWOlxtnaGmhhDOoRkFMucWEyuFEWUfops\nk0PxjfeRn+JJUEEO4Zt1JslKGUz7hbBD0gCyjgxni9bdLWK/l8YakuBu1dGYF/9FTyiY3QaKOW9a\nUtYdaKMs3zFC3JIW4FDuyxbxFwoBqvLelSbpRRAH4KjqWd+2LRPNqDw+COEAmrZnfBuwZGc/ZhK9\nihorqrOYddFiWn8/GuMEBkCaQsmzhhOb9cUX5+R5jHiL3OodvKid7nJ6tGJuwdpdlYudQv6sWh4x\n0q+vRVLewaaFAgMBAAECggEAP8tPJvFtTxhNJAkCloHz0D0vpDHqQBMgntlkgayqmBqLwhyb18pR\ni0qwgh7HHc7wWqOOQuSqlEnrWRrdcI6TSe8R/sErzfTQNoznKWIPYcI/hskk4sdnQ//Yn9/Jvnsv\nU/BBjOTJxtD+sQbhAl80JcA3R+5sArURQkfzzHOL/YMqzAsn5hTzp7HZCxUqBk3KaHRxV7NefeOE\nxlZuWSmxYWfbFIs4kx19/1t7h8CHQWezw+G60G2VBtSBBxDnhBWvqG6R/wpzJ3nEhPLLY9T+XIHe\nipzdMOOOUZorfIg7M+pyYPji+ZIZxIpY5OjrOzXHciAjRtr5Y7l99K1CG1LguQKBgQDrQfIMxxtZ\nvxU/1cRmUV9l7pt5bjV5R6byXq178LxPKVYNjdZ840Q0/OpZEVqaT1xKVi35ohP1QfNjxPLlHD+K\niDAR9z6zkwjIrbwPCnb5kuXy4lpwPcmmmkva25fI7qlpHtbcuQdoBdCfr/KkKaUCMPyY89LCXgEw\n5KTDj64UywKBgQCNfbO+eZLGzhiHhtNJurresCsIGWlInv322gL8CSfBMYl6eNfUTZvUDdFhPISL\nUljKWzXDrjw0ujFSPR0XhUGtiq89H+HUTuPPYv25gVXO+HTgBFZEPl4PpA+BUsSVZy0NddneyqLk\n42Wey9omY9Q8WsdNQS5cbUvy0uG6WFoX7wKBgQDZ1jpW8pa0x2bZsQsm4vo+3G5CRnZlUp+XlWt2\ndDcp5dC0xD1zbs1dc0NcLeGDOTDv9FSl7hok42iHXXq8AygjEm/QcuwwQ1nC2HxmQP5holAiUs4D\nWHM8PWs3wFYPzE459EBoKTxeaeP/uWAn+he8q7d5uWvSZlEcANs/6e77eQKBgD21Ar0hfFfj7mK8\n9E0FeRZBsqK3omkfnhcYgZC11Xa2SgT1yvs2Va2n0RcdM5kncr3eBZav2GYOhhAdwyBM55XuE/sO\neokDVutNeuZ6d5fqV96TRaRBpvgfTvvRwxZ9hvKF4Vz+9wfn/JvCwANaKmegF6ejs7pvmF3whq2k\ndrZVAoGAX5YxQ5XMTD0QbMAl7/6qp6S58xNoVdfCkmkj1ZLKaHKIjS/benkKGlySVQVPexPfnkZx\np/Vv9yyphBoudiTBS9Uog66ueLYZqpgxlM/6OhYg86Gm3U2ycvMxYjBM1NFiyze21AqAhI+HX+Ot\nmraV2/guSgDgZAhukRZzeQ2RucI=\n-----END PRIVATE KEY-----".getBytes(CharsetUtil.UTF_8)), (String) null);
        char[] keyStorePassword = keyStorePassword(null);
        KeyStore ks = buildKeyStore(certs, privateKey, keyStorePassword, null);
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(ks, keyStorePassword);
        SSLContext ctx = SSLContext.getInstance("TLS");
        TrustManagerFactory tm = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tm.init((KeyStore) null);
        TrustManager[] managers = tm.getTrustManagers();
        ctx.init(kmf.getKeyManagers(), wrapTrustManagerIfNeeded(managers), null);
    }

    @Deprecated
    public JdkSslServerContext(File certChainFile, File keyFile) throws SSLException {
        this(null, certChainFile, keyFile, null, null, IdentityCipherSuiteFilter.INSTANCE, JdkDefaultApplicationProtocolNegotiator.INSTANCE, 0L, 0L, null);
    }

    @Deprecated
    public JdkSslServerContext(File certChainFile, File keyFile, String keyPassword) throws SSLException {
        this(certChainFile, keyFile, keyPassword, (Iterable<String>) null, IdentityCipherSuiteFilter.INSTANCE, JdkDefaultApplicationProtocolNegotiator.INSTANCE, 0L, 0L);
    }

    @Deprecated
    public JdkSslServerContext(File certChainFile, File keyFile, String keyPassword, Iterable<String> ciphers, Iterable<String> nextProtocols, long sessionCacheSize, long sessionTimeout) throws SSLException {
        this(null, certChainFile, keyFile, keyPassword, ciphers, IdentityCipherSuiteFilter.INSTANCE, toNegotiator(toApplicationProtocolConfig(nextProtocols), true), sessionCacheSize, sessionTimeout, KeyStore.getDefaultType());
    }

    @Deprecated
    public JdkSslServerContext(File certChainFile, File keyFile, String keyPassword, Iterable<String> ciphers, CipherSuiteFilter cipherFilter, ApplicationProtocolConfig apn, long sessionCacheSize, long sessionTimeout) throws SSLException {
        this(null, certChainFile, keyFile, keyPassword, ciphers, cipherFilter, toNegotiator(apn, true), sessionCacheSize, sessionTimeout, KeyStore.getDefaultType());
    }

    @Deprecated
    public JdkSslServerContext(File certChainFile, File keyFile, String keyPassword, Iterable<String> ciphers, CipherSuiteFilter cipherFilter, JdkApplicationProtocolNegotiator apn, long sessionCacheSize, long sessionTimeout) throws SSLException {
        this(null, certChainFile, keyFile, keyPassword, ciphers, cipherFilter, apn, sessionCacheSize, sessionTimeout, KeyStore.getDefaultType());
    }

    JdkSslServerContext(Provider provider, File certChainFile, File keyFile, String keyPassword, Iterable<String> ciphers, CipherSuiteFilter cipherFilter, JdkApplicationProtocolNegotiator apn, long sessionCacheSize, long sessionTimeout, String keyStore) throws SSLException {
        super(newSSLContext(provider, null, null, toX509CertificatesInternal(certChainFile), toPrivateKeyInternal(keyFile, keyPassword), keyPassword, null, sessionCacheSize, sessionTimeout, keyStore), false, ciphers, cipherFilter, apn, ClientAuth.NONE, (String[]) null, false);
    }

    @Deprecated
    public JdkSslServerContext(File trustCertCollectionFile, TrustManagerFactory trustManagerFactory, File keyCertChainFile, File keyFile, String keyPassword, KeyManagerFactory keyManagerFactory, Iterable<String> ciphers, CipherSuiteFilter cipherFilter, ApplicationProtocolConfig apn, long sessionCacheSize, long sessionTimeout) throws SSLException {
        super(newSSLContext(null, toX509CertificatesInternal(trustCertCollectionFile), trustManagerFactory, toX509CertificatesInternal(keyCertChainFile), toPrivateKeyInternal(keyFile, keyPassword), keyPassword, keyManagerFactory, sessionCacheSize, sessionTimeout, null), false, ciphers, cipherFilter, apn, ClientAuth.NONE, (String[]) null, false);
    }

    @Deprecated
    public JdkSslServerContext(File trustCertCollectionFile, TrustManagerFactory trustManagerFactory, File keyCertChainFile, File keyFile, String keyPassword, KeyManagerFactory keyManagerFactory, Iterable<String> ciphers, CipherSuiteFilter cipherFilter, JdkApplicationProtocolNegotiator apn, long sessionCacheSize, long sessionTimeout) throws SSLException {
        super(newSSLContext(null, toX509CertificatesInternal(trustCertCollectionFile), trustManagerFactory, toX509CertificatesInternal(keyCertChainFile), toPrivateKeyInternal(keyFile, keyPassword), keyPassword, keyManagerFactory, sessionCacheSize, sessionTimeout, KeyStore.getDefaultType()), false, ciphers, cipherFilter, apn, ClientAuth.NONE, (String[]) null, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public JdkSslServerContext(Provider provider, X509Certificate[] trustCertCollection, TrustManagerFactory trustManagerFactory, X509Certificate[] keyCertChain, PrivateKey key, String keyPassword, KeyManagerFactory keyManagerFactory, Iterable<String> ciphers, CipherSuiteFilter cipherFilter, ApplicationProtocolConfig apn, long sessionCacheSize, long sessionTimeout, ClientAuth clientAuth, String[] protocols, boolean startTls, String keyStore) throws SSLException {
        super(newSSLContext(provider, trustCertCollection, trustManagerFactory, keyCertChain, key, keyPassword, keyManagerFactory, sessionCacheSize, sessionTimeout, keyStore), false, ciphers, cipherFilter, toNegotiator(apn, true), clientAuth, protocols, startTls);
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x0045  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x005e A[Catch: Exception -> 0x009b, TRY_ENTER, TryCatch #1 {Exception -> 0x009b, blocks: (B:20:0x005e, B:21:0x0067, B:23:0x0084, B:26:0x0092, B:29:0x0063), top: B:18:0x005c }] */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0084 A[Catch: Exception -> 0x009b, TryCatch #1 {Exception -> 0x009b, blocks: (B:20:0x005e, B:21:0x0067, B:23:0x0084, B:26:0x0092, B:29:0x0063), top: B:18:0x005c }] */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0092 A[Catch: Exception -> 0x009b, TRY_LEAVE, TryCatch #1 {Exception -> 0x009b, blocks: (B:20:0x005e, B:21:0x0067, B:23:0x0084, B:26:0x0092, B:29:0x0063), top: B:18:0x005c }] */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0063 A[Catch: Exception -> 0x009b, TryCatch #1 {Exception -> 0x009b, blocks: (B:20:0x005e, B:21:0x0067, B:23:0x0084, B:26:0x0092, B:29:0x0063), top: B:18:0x005c }] */
    /* JADX WARN: Removed duplicated region for block: B:33:0x00a0  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x00a4  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x0058  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static javax.net.ssl.SSLContext newSSLContext(java.security.Provider r15, java.security.cert.X509Certificate[] r16, javax.net.ssl.TrustManagerFactory r17, java.security.cert.X509Certificate[] r18, java.security.PrivateKey r19, java.lang.String r20, javax.net.ssl.KeyManagerFactory r21, long r22, long r24, java.lang.String r26) throws javax.net.ssl.SSLException {
        /*
            r1 = r15
            r2 = r16
            r3 = r17
            r9 = r22
            r11 = r24
            if (r19 != 0) goto L16
            if (r21 == 0) goto Le
            goto L16
        Le:
            java.lang.NullPointerException r0 = new java.lang.NullPointerException
            java.lang.String r4 = "key, keyManagerFactory"
            r0.<init>(r4)
            throw r0
        L16:
            r0 = 0
            if (r2 == 0) goto L28
            r13 = r26
            javax.net.ssl.TrustManagerFactory r4 = buildTrustManagerFactory(r2, r3, r13)     // Catch: java.lang.Exception -> L22
            r3 = r4
            r14 = r3
            goto L43
        L22:
            r0 = move-exception
            r14 = r3
            r3 = r21
            goto L9c
        L28:
            r13 = r26
            if (r3 != 0) goto L42
            java.lang.String r4 = javax.net.ssl.TrustManagerFactory.getDefaultAlgorithm()     // Catch: java.lang.Exception -> L22
            javax.net.ssl.TrustManagerFactory r4 = javax.net.ssl.TrustManagerFactory.getInstance(r4)     // Catch: java.lang.Exception -> L22
            r3 = r4
            r4 = r0
            java.security.KeyStore r4 = (java.security.KeyStore) r4     // Catch: java.lang.Exception -> L3d
            r3.init(r0)     // Catch: java.lang.Exception -> L3d
            r14 = r3
            goto L43
        L3d:
            r0 = move-exception
            r14 = r3
            r3 = r21
            goto L9c
        L42:
            r14 = r3
        L43:
            if (r19 == 0) goto L58
            r4 = 0
            r8 = 0
            r3 = r18
            r5 = r19
            r6 = r20
            r7 = r21
            javax.net.ssl.KeyManagerFactory r3 = buildKeyManagerFactory(r3, r4, r5, r6, r7, r8)     // Catch: java.lang.Exception -> L54
            goto L5a
        L54:
            r0 = move-exception
            r3 = r21
            goto L9c
        L58:
            r3 = r21
        L5a:
            java.lang.String r4 = "TLS"
            if (r1 != 0) goto L63
            javax.net.ssl.SSLContext r4 = javax.net.ssl.SSLContext.getInstance(r4)     // Catch: java.lang.Exception -> L9b
            goto L67
        L63:
            javax.net.ssl.SSLContext r4 = javax.net.ssl.SSLContext.getInstance(r4, r15)     // Catch: java.lang.Exception -> L9b
        L67:
            javax.net.ssl.KeyManager[] r5 = r3.getKeyManagers()     // Catch: java.lang.Exception -> L9b
            javax.net.ssl.TrustManager[] r6 = r14.getTrustManagers()     // Catch: java.lang.Exception -> L9b
            javax.net.ssl.TrustManager[] r6 = wrapTrustManagerIfNeeded(r6)     // Catch: java.lang.Exception -> L9b
            r4.init(r5, r6, r0)     // Catch: java.lang.Exception -> L9b
            javax.net.ssl.SSLSessionContext r0 = r4.getServerSessionContext()     // Catch: java.lang.Exception -> L9b
            r5 = 0
            int r7 = (r9 > r5 ? 1 : (r9 == r5 ? 0 : -1))
            r5 = 2147483647(0x7fffffff, double:1.060997895E-314)
            if (r7 <= 0) goto L8c
            long r7 = java.lang.Math.min(r9, r5)     // Catch: java.lang.Exception -> L9b
            int r7 = (int) r7     // Catch: java.lang.Exception -> L9b
            r0.setSessionCacheSize(r7)     // Catch: java.lang.Exception -> L9b
        L8c:
            r7 = 0
            int r7 = (r11 > r7 ? 1 : (r11 == r7 ? 0 : -1))
            if (r7 <= 0) goto L9a
            long r5 = java.lang.Math.min(r11, r5)     // Catch: java.lang.Exception -> L9b
            int r5 = (int) r5     // Catch: java.lang.Exception -> L9b
            r0.setSessionTimeout(r5)     // Catch: java.lang.Exception -> L9b
        L9a:
            return r4
        L9b:
            r0 = move-exception
        L9c:
            boolean r4 = r0 instanceof javax.net.ssl.SSLException
            if (r4 == 0) goto La4
            r4 = r0
            javax.net.ssl.SSLException r4 = (javax.net.ssl.SSLException) r4
            throw r4
        La4:
            javax.net.ssl.SSLException r4 = new javax.net.ssl.SSLException
            java.lang.String r5 = "failed to initialize the server-side SSL context"
            r4.<init>(r5, r0)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.handler.ssl.JdkSslServerContext.newSSLContext(java.security.Provider, java.security.cert.X509Certificate[], javax.net.ssl.TrustManagerFactory, java.security.cert.X509Certificate[], java.security.PrivateKey, java.lang.String, javax.net.ssl.KeyManagerFactory, long, long, java.lang.String):javax.net.ssl.SSLContext");
    }

    private static TrustManager[] wrapTrustManagerIfNeeded(TrustManager[] trustManagers) {
        if (WRAP_TRUST_MANAGER && PlatformDependent.javaVersion() >= 7) {
            for (int i = 0; i < trustManagers.length; i++) {
                TrustManager tm = trustManagers[i];
                if (tm instanceof X509ExtendedTrustManager) {
                    trustManagers[i] = new EnhancingX509ExtendedTrustManager((X509ExtendedTrustManager) tm);
                }
            }
        }
        return trustManagers;
    }
}
