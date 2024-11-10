package io.netty.handler.ssl;

import io.netty.handler.ssl.ReferenceCountedOpenSslContext;
import io.netty.internal.tcnative.CertificateCallback;
import io.netty.internal.tcnative.SSLContext;
import io.netty.internal.tcnative.SniHostNameMatcher;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Map;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLException;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509ExtendedTrustManager;
import javax.net.ssl.X509TrustManager;

/* loaded from: classes4.dex */
public final class ReferenceCountedOpenSslServerContext extends ReferenceCountedOpenSslContext {
    private final OpenSslServerSessionContext sessionContext;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) ReferenceCountedOpenSslServerContext.class);
    private static final byte[] ID = {110, 101, 116, 116, 121};

    /* JADX INFO: Access modifiers changed from: package-private */
    public ReferenceCountedOpenSslServerContext(X509Certificate[] trustCertCollection, TrustManagerFactory trustManagerFactory, X509Certificate[] keyCertChain, PrivateKey key, String keyPassword, KeyManagerFactory keyManagerFactory, Iterable<String> ciphers, CipherSuiteFilter cipherFilter, ApplicationProtocolConfig apn, long sessionCacheSize, long sessionTimeout, ClientAuth clientAuth, String[] protocols, boolean startTls, boolean enableOcsp, String keyStore, Map.Entry<SslContextOption<?>, Object>... options) throws SSLException {
        this(trustCertCollection, trustManagerFactory, keyCertChain, key, keyPassword, keyManagerFactory, ciphers, cipherFilter, toNegotiator(apn), sessionCacheSize, sessionTimeout, clientAuth, protocols, startTls, enableOcsp, keyStore, options);
    }

    ReferenceCountedOpenSslServerContext(X509Certificate[] trustCertCollection, TrustManagerFactory trustManagerFactory, X509Certificate[] keyCertChain, PrivateKey key, String keyPassword, KeyManagerFactory keyManagerFactory, Iterable<String> ciphers, CipherSuiteFilter cipherFilter, OpenSslApplicationProtocolNegotiator apn, long sessionCacheSize, long sessionTimeout, ClientAuth clientAuth, String[] protocols, boolean startTls, boolean enableOcsp, String keyStore, Map.Entry<SslContextOption<?>, Object>... options) throws SSLException {
        super(ciphers, cipherFilter, apn, 1, keyCertChain, clientAuth, protocols, startTls, enableOcsp, true, options);
        try {
            try {
            } catch (Throwable th) {
                th = th;
            }
        } catch (Throwable th2) {
            th = th2;
        }
        try {
            this.sessionContext = newSessionContext(this, this.ctx, this.engineMap, trustCertCollection, trustManagerFactory, keyCertChain, key, keyPassword, keyManagerFactory, keyStore, sessionCacheSize, sessionTimeout);
            if (SERVER_ENABLE_SESSION_TICKET) {
                this.sessionContext.setTicketKeys(new OpenSslSessionTicketKey[0]);
            }
            if (1 == 0) {
                release();
            }
        } catch (Throwable th3) {
            th = th3;
            if (0 == 0) {
                release();
            }
            throw th;
        }
    }

    @Override // io.netty.handler.ssl.ReferenceCountedOpenSslContext, io.netty.handler.ssl.SslContext
    public OpenSslServerSessionContext sessionContext() {
        return this.sessionContext;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Can't wrap try/catch for region: R(5:7|(13:(1:9)(15:(4:77|78|79|80)|12|13|14|(2:16|(7:18|19|20|21|22|23|(10:25|26|(1:28)|29|(1:31)|32|(1:34)|35|(1:37)|38)(5:40|41|42|43|44)))|56|26|(0)|29|(0)|32|(0)|35|(0)|38)|14|(0)|56|26|(0)|29|(0)|32|(0)|35|(0)|38)|10|12|13) */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x015a, code lost:            r0 = move-exception;     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x015e, code lost:            throw r0;     */
    /* JADX WARN: Code restructure failed: missing block: B:72:0x014f, code lost:            r0 = e;     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x014b, code lost:            r0 = th;     */
    /* JADX WARN: Removed duplicated region for block: B:16:0x00b2 A[Catch: all -> 0x014b, Exception -> 0x014f, SSLException -> 0x015a, TRY_LEAVE, TryCatch #13 {SSLException -> 0x015a, Exception -> 0x014f, all -> 0x014b, blocks: (B:13:0x009f, B:16:0x00b2), top: B:12:0x009f }] */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0100 A[Catch: all -> 0x013f, Exception -> 0x0143, SSLException -> 0x0147, TRY_LEAVE, TryCatch #17 {all -> 0x013f, blocks: (B:25:0x00c9, B:26:0x00f8, B:28:0x0100, B:29:0x0109, B:31:0x0123, B:34:0x012f, B:47:0x00f1, B:48:0x00f5), top: B:14:0x00b0 }] */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0123 A[Catch: all -> 0x013f, TryCatch #17 {all -> 0x013f, blocks: (B:25:0x00c9, B:26:0x00f8, B:28:0x0100, B:29:0x0109, B:31:0x0123, B:34:0x012f, B:47:0x00f1, B:48:0x00f5), top: B:14:0x00b0 }] */
    /* JADX WARN: Removed duplicated region for block: B:34:0x012f A[Catch: all -> 0x013f, TRY_LEAVE, TryCatch #17 {all -> 0x013f, blocks: (B:25:0x00c9, B:26:0x00f8, B:28:0x0100, B:29:0x0109, B:31:0x0123, B:34:0x012f, B:47:0x00f1, B:48:0x00f5), top: B:14:0x00b0 }] */
    /* JADX WARN: Removed duplicated region for block: B:37:0x013b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static io.netty.handler.ssl.OpenSslServerSessionContext newSessionContext(io.netty.handler.ssl.ReferenceCountedOpenSslContext r20, long r21, io.netty.handler.ssl.OpenSslEngineMap r23, java.security.cert.X509Certificate[] r24, javax.net.ssl.TrustManagerFactory r25, java.security.cert.X509Certificate[] r26, java.security.PrivateKey r27, java.lang.String r28, javax.net.ssl.KeyManagerFactory r29, java.lang.String r30, long r31, long r33) throws javax.net.ssl.SSLException {
        /*
            Method dump skipped, instructions count: 377
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.handler.ssl.ReferenceCountedOpenSslServerContext.newSessionContext(io.netty.handler.ssl.ReferenceCountedOpenSslContext, long, io.netty.handler.ssl.OpenSslEngineMap, java.security.cert.X509Certificate[], javax.net.ssl.TrustManagerFactory, java.security.cert.X509Certificate[], java.security.PrivateKey, java.lang.String, javax.net.ssl.KeyManagerFactory, java.lang.String, long, long):io.netty.handler.ssl.OpenSslServerSessionContext");
    }

    private static void setVerifyCallback(long ctx, OpenSslEngineMap engineMap, X509TrustManager manager) {
        if (useExtendedTrustManager(manager)) {
            SSLContext.setCertVerifyCallback(ctx, new ExtendedTrustManagerVerifyCallback(engineMap, (X509ExtendedTrustManager) manager));
        } else {
            SSLContext.setCertVerifyCallback(ctx, new TrustManagerVerifyCallback(engineMap, manager));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class OpenSslServerCertificateCallback implements CertificateCallback {
        private final OpenSslEngineMap engineMap;
        private final OpenSslKeyMaterialManager keyManagerHolder;

        OpenSslServerCertificateCallback(OpenSslEngineMap engineMap, OpenSslKeyMaterialManager keyManagerHolder) {
            this.engineMap = engineMap;
            this.keyManagerHolder = keyManagerHolder;
        }

        public void handle(long ssl, byte[] keyTypeBytes, byte[][] asn1DerEncodedPrincipals) throws Exception {
            ReferenceCountedOpenSslEngine engine = this.engineMap.get(ssl);
            if (engine == null) {
                return;
            }
            try {
                this.keyManagerHolder.setKeyMaterialServerSide(engine);
            } catch (Throwable cause) {
                engine.initHandshakeException(cause);
                if (cause instanceof Exception) {
                    throw ((Exception) cause);
                }
                throw new SSLException(cause);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class TrustManagerVerifyCallback extends ReferenceCountedOpenSslContext.AbstractCertificateVerifier {
        private final X509TrustManager manager;

        TrustManagerVerifyCallback(OpenSslEngineMap engineMap, X509TrustManager manager) {
            super(engineMap);
            this.manager = manager;
        }

        @Override // io.netty.handler.ssl.ReferenceCountedOpenSslContext.AbstractCertificateVerifier
        void verify(ReferenceCountedOpenSslEngine engine, X509Certificate[] peerCerts, String auth) throws Exception {
            this.manager.checkClientTrusted(peerCerts, auth);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class ExtendedTrustManagerVerifyCallback extends ReferenceCountedOpenSslContext.AbstractCertificateVerifier {
        private final X509ExtendedTrustManager manager;

        ExtendedTrustManagerVerifyCallback(OpenSslEngineMap engineMap, X509ExtendedTrustManager manager) {
            super(engineMap);
            this.manager = manager;
        }

        @Override // io.netty.handler.ssl.ReferenceCountedOpenSslContext.AbstractCertificateVerifier
        void verify(ReferenceCountedOpenSslEngine engine, X509Certificate[] peerCerts, String auth) throws Exception {
            this.manager.checkClientTrusted(peerCerts, auth, engine);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class OpenSslSniHostnameMatcher implements SniHostNameMatcher {
        private final OpenSslEngineMap engineMap;

        OpenSslSniHostnameMatcher(OpenSslEngineMap engineMap) {
            this.engineMap = engineMap;
        }

        public boolean match(long ssl, String hostname) {
            ReferenceCountedOpenSslEngine engine = this.engineMap.get(ssl);
            if (engine == null) {
                ReferenceCountedOpenSslServerContext.logger.warn("No ReferenceCountedOpenSslEngine found for SSL pointer: {}", Long.valueOf(ssl));
                return false;
            }
            return engine.checkSniHostnameMatch(hostname.getBytes(CharsetUtil.UTF_8));
        }
    }
}
