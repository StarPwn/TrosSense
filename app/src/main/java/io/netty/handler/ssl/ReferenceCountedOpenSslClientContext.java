package io.netty.handler.ssl;

import io.netty.handler.ssl.ReferenceCountedOpenSslContext;
import io.netty.internal.tcnative.CertificateCallback;
import io.netty.internal.tcnative.SSL;
import io.netty.internal.tcnative.SSLContext;
import io.netty.util.internal.EmptyArrays;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLException;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509ExtendedTrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.auth.x500.X500Principal;

/* loaded from: classes4.dex */
public final class ReferenceCountedOpenSslClientContext extends ReferenceCountedOpenSslContext {
    private static final Set<String> SUPPORTED_KEY_TYPES = Collections.unmodifiableSet(new LinkedHashSet(Arrays.asList("RSA", "DH_RSA", "EC", "EC_RSA", "EC_EC")));
    private final OpenSslSessionContext sessionContext;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ReferenceCountedOpenSslClientContext(X509Certificate[] trustCertCollection, TrustManagerFactory trustManagerFactory, X509Certificate[] keyCertChain, PrivateKey key, String keyPassword, KeyManagerFactory keyManagerFactory, Iterable<String> ciphers, CipherSuiteFilter cipherFilter, ApplicationProtocolConfig apn, String[] protocols, long sessionCacheSize, long sessionTimeout, boolean enableOcsp, String keyStore, Map.Entry<SslContextOption<?>, Object>... options) throws SSLException {
        super(ciphers, cipherFilter, toNegotiator(apn), 0, keyCertChain, ClientAuth.NONE, protocols, false, enableOcsp, true, options);
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
    public OpenSslSessionContext sessionContext() {
        return this.sessionContext;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Not initialized variable reg: 16, insn: 0x00ad: MOVE (r15 I:??[OBJECT, ARRAY]) = (r16 I:??[OBJECT, ARRAY] A[D('keyMaterialProvider' io.netty.handler.ssl.OpenSslKeyMaterialProvider)]), block:B:112:0x00ab */
    /* JADX WARN: Not initialized variable reg: 16, insn: 0x00b4: MOVE (r15 I:??[OBJECT, ARRAY]) = (r16 I:??[OBJECT, ARRAY] A[D('keyMaterialProvider' io.netty.handler.ssl.OpenSslKeyMaterialProvider)]), block:B:110:0x00b2 */
    /* JADX WARN: Removed duplicated region for block: B:27:0x011a A[Catch: all -> 0x0154, TryCatch #9 {all -> 0x0154, blocks: (B:25:0x0109, B:27:0x011a, B:30:0x0126, B:31:0x012e, B:33:0x0132, B:48:0x0149, B:49:0x014c, B:50:0x0153), top: B:18:0x00db }] */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0126 A[Catch: all -> 0x0154, TryCatch #9 {all -> 0x0154, blocks: (B:25:0x0109, B:27:0x011a, B:30:0x0126, B:31:0x012e, B:33:0x0132, B:48:0x0149, B:49:0x014c, B:50:0x0153), top: B:18:0x00db }] */
    /* JADX WARN: Removed duplicated region for block: B:33:0x0132 A[Catch: all -> 0x0154, TRY_LEAVE, TryCatch #9 {all -> 0x0154, blocks: (B:25:0x0109, B:27:0x011a, B:30:0x0126, B:31:0x012e, B:33:0x0132, B:48:0x0149, B:49:0x014c, B:50:0x0153), top: B:18:0x00db }] */
    /* JADX WARN: Removed duplicated region for block: B:36:0x013c  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x016c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static io.netty.handler.ssl.OpenSslSessionContext newSessionContext(io.netty.handler.ssl.ReferenceCountedOpenSslContext r18, long r19, io.netty.handler.ssl.OpenSslEngineMap r21, java.security.cert.X509Certificate[] r22, javax.net.ssl.TrustManagerFactory r23, java.security.cert.X509Certificate[] r24, java.security.PrivateKey r25, java.lang.String r26, javax.net.ssl.KeyManagerFactory r27, java.lang.String r28, long r29, long r31) throws javax.net.ssl.SSLException {
        /*
            Method dump skipped, instructions count: 368
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.handler.ssl.ReferenceCountedOpenSslClientContext.newSessionContext(io.netty.handler.ssl.ReferenceCountedOpenSslContext, long, io.netty.handler.ssl.OpenSslEngineMap, java.security.cert.X509Certificate[], javax.net.ssl.TrustManagerFactory, java.security.cert.X509Certificate[], java.security.PrivateKey, java.lang.String, javax.net.ssl.KeyManagerFactory, java.lang.String, long, long):io.netty.handler.ssl.OpenSslSessionContext");
    }

    private static void setVerifyCallback(long ctx, OpenSslEngineMap engineMap, X509TrustManager manager) {
        if (useExtendedTrustManager(manager)) {
            SSLContext.setCertVerifyCallback(ctx, new ExtendedTrustManagerVerifyCallback(engineMap, (X509ExtendedTrustManager) manager));
        } else {
            SSLContext.setCertVerifyCallback(ctx, new TrustManagerVerifyCallback(engineMap, manager));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public static final class OpenSslClientSessionContext extends OpenSslSessionContext {
        OpenSslClientSessionContext(ReferenceCountedOpenSslContext context, OpenSslKeyMaterialProvider provider) {
            super(context, provider, SSL.SSL_SESS_CACHE_CLIENT, new OpenSslClientSessionCache(context.engineMap));
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
            this.manager.checkServerTrusted(peerCerts, auth);
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
            this.manager.checkServerTrusted(peerCerts, auth, engine);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class OpenSslClientCertificateCallback implements CertificateCallback {
        private final OpenSslEngineMap engineMap;
        private final OpenSslKeyMaterialManager keyManagerHolder;

        OpenSslClientCertificateCallback(OpenSslEngineMap engineMap, OpenSslKeyMaterialManager keyManagerHolder) {
            this.engineMap = engineMap;
            this.keyManagerHolder = keyManagerHolder;
        }

        public void handle(long ssl, byte[] keyTypeBytes, byte[][] asn1DerEncodedPrincipals) throws Exception {
            X500Principal[] issuers;
            ReferenceCountedOpenSslEngine engine = this.engineMap.get(ssl);
            if (engine == null) {
                return;
            }
            try {
                Set<String> keyTypesSet = supportedClientKeyTypes(keyTypeBytes);
                String[] keyTypes = (String[]) keyTypesSet.toArray(EmptyArrays.EMPTY_STRINGS);
                if (asn1DerEncodedPrincipals == null) {
                    issuers = null;
                } else {
                    issuers = new X500Principal[asn1DerEncodedPrincipals.length];
                    for (int i = 0; i < asn1DerEncodedPrincipals.length; i++) {
                        issuers[i] = new X500Principal(asn1DerEncodedPrincipals[i]);
                    }
                }
                this.keyManagerHolder.setKeyMaterialClientSide(engine, keyTypes, issuers);
            } catch (Throwable cause) {
                engine.initHandshakeException(cause);
                if (cause instanceof Exception) {
                    throw ((Exception) cause);
                }
                throw new SSLException(cause);
            }
        }

        private static Set<String> supportedClientKeyTypes(byte[] clientCertificateTypes) {
            if (clientCertificateTypes == null) {
                return ReferenceCountedOpenSslClientContext.SUPPORTED_KEY_TYPES;
            }
            Set<String> result = new HashSet<>(clientCertificateTypes.length);
            for (byte keyTypeCode : clientCertificateTypes) {
                String keyType = clientKeyType(keyTypeCode);
                if (keyType != null) {
                    result.add(keyType);
                }
            }
            return result;
        }

        private static String clientKeyType(byte clientCertificateType) {
            switch (clientCertificateType) {
                case 1:
                    return "RSA";
                case 3:
                    return "DH_RSA";
                case 64:
                    return "EC";
                case 65:
                    return "EC_RSA";
                case 66:
                    return "EC_EC";
                default:
                    return null;
            }
        }
    }
}
