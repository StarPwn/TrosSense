package io.netty.handler.ssl;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.handler.ssl.ApplicationProtocolConfig;
import io.netty.handler.ssl.util.LazyX509Certificate;
import io.netty.internal.tcnative.AsyncSSLPrivateKeyMethod;
import io.netty.internal.tcnative.CertificateCompressionAlgo;
import io.netty.internal.tcnative.CertificateVerifier;
import io.netty.internal.tcnative.ResultCallback;
import io.netty.internal.tcnative.SSL;
import io.netty.internal.tcnative.SSLContext;
import io.netty.internal.tcnative.SSLPrivateKeyMethod;
import io.netty.util.AbstractReferenceCounted;
import io.netty.util.ReferenceCounted;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.ResourceLeakDetectorFactory;
import io.netty.util.ResourceLeakTracker;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.security.cert.CertPathValidatorException;
import java.security.cert.Certificate;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.CertificateRevokedException;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLException;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509ExtendedTrustManager;
import javax.net.ssl.X509KeyManager;
import javax.net.ssl.X509TrustManager;

/* loaded from: classes4.dex */
public abstract class ReferenceCountedOpenSslContext extends SslContext implements ReferenceCounted {
    private static final Integer DH_KEY_LENGTH;
    protected static final int VERIFY_DEPTH = 10;
    private final OpenSslApplicationProtocolNegotiator apn;
    private volatile int bioNonApplicationBufferSize;
    final ClientAuth clientAuth;
    protected long ctx;
    final ReadWriteLock ctxLock;
    final boolean enableOcsp;
    final OpenSslEngineMap engineMap;
    final boolean hasTLSv13Cipher;
    final Certificate[] keyCertChain;
    private final ResourceLeakTracker<ReferenceCountedOpenSslContext> leak;
    private final int mode;
    final String[] protocols;
    private final AbstractReferenceCounted refCnt;
    final boolean tlsFalseStart;
    private final List<String> unmodifiableCiphers;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) ReferenceCountedOpenSslContext.class);
    private static final int DEFAULT_BIO_NON_APPLICATION_BUFFER_SIZE = Math.max(1, SystemPropertyUtil.getInt("io.netty.handler.ssl.openssl.bioNonApplicationBufferSize", 2048));
    static final boolean USE_TASKS = SystemPropertyUtil.getBoolean("io.netty.handler.ssl.openssl.useTasks", true);
    private static final ResourceLeakDetector<ReferenceCountedOpenSslContext> leakDetector = ResourceLeakDetectorFactory.instance().newResourceLeakDetector(ReferenceCountedOpenSslContext.class);
    static final boolean CLIENT_ENABLE_SESSION_TICKET = SystemPropertyUtil.getBoolean("jdk.tls.client.enableSessionTicketExtension", false);
    static final boolean CLIENT_ENABLE_SESSION_TICKET_TLSV13 = SystemPropertyUtil.getBoolean("jdk.tls.client.enableSessionTicketExtension", true);
    static final boolean SERVER_ENABLE_SESSION_TICKET = SystemPropertyUtil.getBoolean("jdk.tls.server.enableSessionTicketExtension", false);
    static final boolean SERVER_ENABLE_SESSION_TICKET_TLSV13 = SystemPropertyUtil.getBoolean("jdk.tls.server.enableSessionTicketExtension", true);
    static final boolean SERVER_ENABLE_SESSION_CACHE = SystemPropertyUtil.getBoolean("io.netty.handler.ssl.openssl.sessionCacheServer", true);
    static final boolean CLIENT_ENABLE_SESSION_CACHE = SystemPropertyUtil.getBoolean("io.netty.handler.ssl.openssl.sessionCacheClient", true);
    static final OpenSslApplicationProtocolNegotiator NONE_PROTOCOL_NEGOTIATOR = new OpenSslApplicationProtocolNegotiator() { // from class: io.netty.handler.ssl.ReferenceCountedOpenSslContext.2
        @Override // io.netty.handler.ssl.OpenSslApplicationProtocolNegotiator
        public ApplicationProtocolConfig.Protocol protocol() {
            return ApplicationProtocolConfig.Protocol.NONE;
        }

        @Override // io.netty.handler.ssl.ApplicationProtocolNegotiator
        public List<String> protocols() {
            return Collections.emptyList();
        }

        @Override // io.netty.handler.ssl.OpenSslApplicationProtocolNegotiator
        public ApplicationProtocolConfig.SelectorFailureBehavior selectorFailureBehavior() {
            return ApplicationProtocolConfig.SelectorFailureBehavior.CHOOSE_MY_LAST_PROTOCOL;
        }

        @Override // io.netty.handler.ssl.OpenSslApplicationProtocolNegotiator
        public ApplicationProtocolConfig.SelectedListenerFailureBehavior selectedListenerFailureBehavior() {
            return ApplicationProtocolConfig.SelectedListenerFailureBehavior.ACCEPT;
        }
    };

    @Override // io.netty.handler.ssl.SslContext
    public abstract OpenSslSessionContext sessionContext();

    static {
        Integer dhLen = null;
        try {
            String dhKeySize = SystemPropertyUtil.get("jdk.tls.ephemeralDHKeySize");
            if (dhKeySize != null) {
                try {
                    dhLen = Integer.valueOf(dhKeySize);
                } catch (NumberFormatException e) {
                    logger.debug("ReferenceCountedOpenSslContext supports -Djdk.tls.ephemeralDHKeySize={int}, but got: " + dhKeySize);
                }
            }
        } catch (Throwable th) {
        }
        DH_KEY_LENGTH = dhLen;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Removed duplicated region for block: B:103:0x02cb A[Catch: all -> 0x03c2, TryCatch #1 {all -> 0x03c2, blocks: (B:98:0x02a3, B:99:0x02c3, B:100:0x02c6, B:101:0x02a6, B:103:0x02cb, B:104:0x02d4, B:106:0x02db, B:108:0x02e9, B:110:0x02f7, B:111:0x02fb, B:113:0x0301, B:114:0x0321, B:123:0x0324, B:124:0x034c, B:125:0x034f, B:115:0x0329, B:118:0x0333, B:120:0x033d, B:129:0x035b, B:130:0x0364, B:137:0x02b3, B:138:0x02bb, B:160:0x038f, B:161:0x03a9, B:171:0x03b4, B:177:0x03b6, B:178:0x03c1, B:70:0x01a5), top: B:69:0x01a5, inners: #6 }] */
    /* JADX WARN: Removed duplicated region for block: B:106:0x02db A[Catch: all -> 0x03c2, TryCatch #1 {all -> 0x03c2, blocks: (B:98:0x02a3, B:99:0x02c3, B:100:0x02c6, B:101:0x02a6, B:103:0x02cb, B:104:0x02d4, B:106:0x02db, B:108:0x02e9, B:110:0x02f7, B:111:0x02fb, B:113:0x0301, B:114:0x0321, B:123:0x0324, B:124:0x034c, B:125:0x034f, B:115:0x0329, B:118:0x0333, B:120:0x033d, B:129:0x035b, B:130:0x0364, B:137:0x02b3, B:138:0x02bb, B:160:0x038f, B:161:0x03a9, B:171:0x03b4, B:177:0x03b6, B:178:0x03c1, B:70:0x01a5), top: B:69:0x01a5, inners: #6 }] */
    /* JADX WARN: Removed duplicated region for block: B:108:0x02e9 A[Catch: all -> 0x03c2, TryCatch #1 {all -> 0x03c2, blocks: (B:98:0x02a3, B:99:0x02c3, B:100:0x02c6, B:101:0x02a6, B:103:0x02cb, B:104:0x02d4, B:106:0x02db, B:108:0x02e9, B:110:0x02f7, B:111:0x02fb, B:113:0x0301, B:114:0x0321, B:123:0x0324, B:124:0x034c, B:125:0x034f, B:115:0x0329, B:118:0x0333, B:120:0x033d, B:129:0x035b, B:130:0x0364, B:137:0x02b3, B:138:0x02bb, B:160:0x038f, B:161:0x03a9, B:171:0x03b4, B:177:0x03b6, B:178:0x03c1, B:70:0x01a5), top: B:69:0x01a5, inners: #6 }] */
    /* JADX WARN: Removed duplicated region for block: B:110:0x02f7 A[Catch: all -> 0x03c2, TryCatch #1 {all -> 0x03c2, blocks: (B:98:0x02a3, B:99:0x02c3, B:100:0x02c6, B:101:0x02a6, B:103:0x02cb, B:104:0x02d4, B:106:0x02db, B:108:0x02e9, B:110:0x02f7, B:111:0x02fb, B:113:0x0301, B:114:0x0321, B:123:0x0324, B:124:0x034c, B:125:0x034f, B:115:0x0329, B:118:0x0333, B:120:0x033d, B:129:0x035b, B:130:0x0364, B:137:0x02b3, B:138:0x02bb, B:160:0x038f, B:161:0x03a9, B:171:0x03b4, B:177:0x03b6, B:178:0x03c1, B:70:0x01a5), top: B:69:0x01a5, inners: #6 }] */
    /* JADX WARN: Removed duplicated region for block: B:129:0x035b A[Catch: all -> 0x03c2, TryCatch #1 {all -> 0x03c2, blocks: (B:98:0x02a3, B:99:0x02c3, B:100:0x02c6, B:101:0x02a6, B:103:0x02cb, B:104:0x02d4, B:106:0x02db, B:108:0x02e9, B:110:0x02f7, B:111:0x02fb, B:113:0x0301, B:114:0x0321, B:123:0x0324, B:124:0x034c, B:125:0x034f, B:115:0x0329, B:118:0x0333, B:120:0x033d, B:129:0x035b, B:130:0x0364, B:137:0x02b3, B:138:0x02bb, B:160:0x038f, B:161:0x03a9, B:171:0x03b4, B:177:0x03b6, B:178:0x03c1, B:70:0x01a5), top: B:69:0x01a5, inners: #6 }] */
    /* JADX WARN: Removed duplicated region for block: B:133:0x036e  */
    /* JADX WARN: Removed duplicated region for block: B:135:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:136:0x0355  */
    /* JADX WARN: Removed duplicated region for block: B:145:0x02c7  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x023c A[Catch: all -> 0x01cf, TRY_ENTER, TryCatch #10 {all -> 0x01cf, blocks: (B:78:0x01c0, B:80:0x01c8, B:85:0x023c, B:87:0x024d, B:91:0x026d, B:151:0x01fd), top: B:75:0x01bc }] */
    /* JADX WARN: Removed duplicated region for block: B:87:0x024d A[Catch: all -> 0x01cf, TRY_LEAVE, TryCatch #10 {all -> 0x01cf, blocks: (B:78:0x01c0, B:80:0x01c8, B:85:0x023c, B:87:0x024d, B:91:0x026d, B:151:0x01fd), top: B:75:0x01bc }] */
    /* JADX WARN: Removed duplicated region for block: B:91:0x026d A[Catch: all -> 0x01cf, TRY_ENTER, TRY_LEAVE, TryCatch #10 {all -> 0x01cf, blocks: (B:78:0x01c0, B:80:0x01c8, B:85:0x023c, B:87:0x024d, B:91:0x026d, B:151:0x01fd), top: B:75:0x01bc }] */
    /* JADX WARN: Removed duplicated region for block: B:95:0x0282 A[Catch: all -> 0x0372, TRY_LEAVE, TryCatch #2 {all -> 0x0372, blocks: (B:83:0x021b, B:89:0x0250, B:93:0x0278, B:95:0x0282, B:149:0x01e8), top: B:148:0x01e8 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public ReferenceCountedOpenSslContext(java.lang.Iterable<java.lang.String> r25, io.netty.handler.ssl.CipherSuiteFilter r26, io.netty.handler.ssl.OpenSslApplicationProtocolNegotiator r27, int r28, java.security.cert.Certificate[] r29, io.netty.handler.ssl.ClientAuth r30, java.lang.String[] r31, boolean r32, boolean r33, boolean r34, java.util.Map.Entry<io.netty.handler.ssl.SslContextOption<?>, java.lang.Object>... r35) throws javax.net.ssl.SSLException {
        /*
            Method dump skipped, instructions count: 996
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.handler.ssl.ReferenceCountedOpenSslContext.<init>(java.lang.Iterable, io.netty.handler.ssl.CipherSuiteFilter, io.netty.handler.ssl.OpenSslApplicationProtocolNegotiator, int, java.security.cert.Certificate[], io.netty.handler.ssl.ClientAuth, java.lang.String[], boolean, boolean, boolean, java.util.Map$Entry[]):void");
    }

    private static int opensslSelectorFailureBehavior(ApplicationProtocolConfig.SelectorFailureBehavior behavior) {
        switch (behavior) {
            case NO_ADVERTISE:
                return 0;
            case CHOOSE_MY_LAST_PROTOCOL:
                return 1;
            default:
                throw new Error();
        }
    }

    @Override // io.netty.handler.ssl.SslContext
    public final List<String> cipherSuites() {
        return this.unmodifiableCiphers;
    }

    @Override // io.netty.handler.ssl.SslContext
    public ApplicationProtocolNegotiator applicationProtocolNegotiator() {
        return this.apn;
    }

    @Override // io.netty.handler.ssl.SslContext
    public final boolean isClient() {
        return this.mode == 0;
    }

    @Override // io.netty.handler.ssl.SslContext
    public final SSLEngine newEngine(ByteBufAllocator alloc, String peerHost, int peerPort) {
        return newEngine0(alloc, peerHost, peerPort, true);
    }

    @Override // io.netty.handler.ssl.SslContext
    protected final SslHandler newHandler(ByteBufAllocator alloc, boolean startTls) {
        return new SslHandler(newEngine0(alloc, null, -1, false), startTls);
    }

    @Override // io.netty.handler.ssl.SslContext
    protected final SslHandler newHandler(ByteBufAllocator alloc, String peerHost, int peerPort, boolean startTls) {
        return new SslHandler(newEngine0(alloc, peerHost, peerPort, false), startTls);
    }

    @Override // io.netty.handler.ssl.SslContext
    protected SslHandler newHandler(ByteBufAllocator alloc, boolean startTls, Executor executor) {
        return new SslHandler(newEngine0(alloc, null, -1, false), startTls, executor);
    }

    @Override // io.netty.handler.ssl.SslContext
    protected SslHandler newHandler(ByteBufAllocator alloc, String peerHost, int peerPort, boolean startTls, Executor executor) {
        return new SslHandler(newEngine0(alloc, peerHost, peerPort, false), executor);
    }

    SSLEngine newEngine0(ByteBufAllocator alloc, String peerHost, int peerPort, boolean jdkCompatibilityMode) {
        return new ReferenceCountedOpenSslEngine(this, alloc, peerHost, peerPort, jdkCompatibilityMode, true);
    }

    @Override // io.netty.handler.ssl.SslContext
    public final SSLEngine newEngine(ByteBufAllocator alloc) {
        return newEngine(alloc, null, -1);
    }

    @Deprecated
    public final long context() {
        return sslCtxPointer();
    }

    @Deprecated
    public final OpenSslSessionStats stats() {
        return sessionContext().stats();
    }

    @Deprecated
    public void setRejectRemoteInitiatedRenegotiation(boolean rejectRemoteInitiatedRenegotiation) {
        if (!rejectRemoteInitiatedRenegotiation) {
            throw new UnsupportedOperationException("Renegotiation is not supported");
        }
    }

    @Deprecated
    public boolean getRejectRemoteInitiatedRenegotiation() {
        return true;
    }

    public void setBioNonApplicationBufferSize(int bioNonApplicationBufferSize) {
        this.bioNonApplicationBufferSize = ObjectUtil.checkPositiveOrZero(bioNonApplicationBufferSize, "bioNonApplicationBufferSize");
    }

    public int getBioNonApplicationBufferSize() {
        return this.bioNonApplicationBufferSize;
    }

    @Deprecated
    public final void setTicketKeys(byte[] keys) {
        sessionContext().setTicketKeys(keys);
    }

    @Deprecated
    public final long sslCtxPointer() {
        Lock readerLock = this.ctxLock.readLock();
        readerLock.lock();
        try {
            return SSLContext.getSslCtx(this.ctx);
        } finally {
            readerLock.unlock();
        }
    }

    @Deprecated
    public final void setPrivateKeyMethod(OpenSslPrivateKeyMethod method) {
        ObjectUtil.checkNotNull(method, "method");
        Lock writerLock = this.ctxLock.writeLock();
        writerLock.lock();
        try {
            SSLContext.setPrivateKeyMethod(this.ctx, new PrivateKeyMethod(this.engineMap, method));
        } finally {
            writerLock.unlock();
        }
    }

    @Deprecated
    public final void setUseTasks(boolean useTasks) {
        Lock writerLock = this.ctxLock.writeLock();
        writerLock.lock();
        try {
            SSLContext.setUseTasks(this.ctx, useTasks);
        } finally {
            writerLock.unlock();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void destroy() {
        Lock writerLock = this.ctxLock.writeLock();
        writerLock.lock();
        try {
            if (this.ctx != 0) {
                if (this.enableOcsp) {
                    SSLContext.disableOcsp(this.ctx);
                }
                SSLContext.free(this.ctx);
                this.ctx = 0L;
                OpenSslSessionContext context = sessionContext();
                if (context != null) {
                    context.destroy();
                }
            }
        } finally {
            writerLock.unlock();
        }
    }

    protected static X509Certificate[] certificates(byte[][] chain) {
        X509Certificate[] peerCerts = new X509Certificate[chain.length];
        for (int i = 0; i < peerCerts.length; i++) {
            peerCerts[i] = new LazyX509Certificate(chain[i]);
        }
        return peerCerts;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static X509TrustManager chooseTrustManager(TrustManager[] managers) {
        for (TrustManager m : managers) {
            if (m instanceof X509TrustManager) {
                X509TrustManager tm = (X509TrustManager) m;
                if (PlatformDependent.javaVersion() >= 7) {
                    X509TrustManager tm2 = OpenSslX509TrustManagerWrapper.wrapIfNeeded((X509TrustManager) m);
                    if (useExtendedTrustManager(tm2)) {
                        return new EnhancingX509ExtendedTrustManager(tm2);
                    }
                    return tm2;
                }
                return tm;
            }
        }
        throw new IllegalStateException("no X509TrustManager found");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static X509KeyManager chooseX509KeyManager(KeyManager[] kms) {
        for (KeyManager km : kms) {
            if (km instanceof X509KeyManager) {
                return (X509KeyManager) km;
            }
        }
        throw new IllegalStateException("no X509KeyManager found");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static OpenSslApplicationProtocolNegotiator toNegotiator(ApplicationProtocolConfig config) {
        if (config == null) {
            return NONE_PROTOCOL_NEGOTIATOR;
        }
        switch (config.protocol()) {
            case NPN:
            case ALPN:
            case NPN_AND_ALPN:
                switch (config.selectedListenerFailureBehavior()) {
                    case CHOOSE_MY_LAST_PROTOCOL:
                    case ACCEPT:
                        switch (config.selectorFailureBehavior()) {
                            case NO_ADVERTISE:
                            case CHOOSE_MY_LAST_PROTOCOL:
                                return new OpenSslDefaultApplicationProtocolNegotiator(config);
                            default:
                                throw new UnsupportedOperationException("OpenSSL provider does not support " + config.selectorFailureBehavior() + " behavior");
                        }
                    default:
                        throw new UnsupportedOperationException("OpenSSL provider does not support " + config.selectedListenerFailureBehavior() + " behavior");
                }
            case NONE:
                return NONE_PROTOCOL_NEGOTIATOR;
            default:
                throw new Error();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean useExtendedTrustManager(X509TrustManager trustManager) {
        return PlatformDependent.javaVersion() >= 7 && (trustManager instanceof X509ExtendedTrustManager);
    }

    @Override // io.netty.util.ReferenceCounted
    public final int refCnt() {
        return this.refCnt.refCnt();
    }

    @Override // io.netty.util.ReferenceCounted
    public final ReferenceCounted retain() {
        this.refCnt.retain();
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public final ReferenceCounted retain(int increment) {
        this.refCnt.retain(increment);
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public final ReferenceCounted touch() {
        this.refCnt.touch();
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public final ReferenceCounted touch(Object hint) {
        this.refCnt.touch(hint);
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public final boolean release() {
        return this.refCnt.release();
    }

    @Override // io.netty.util.ReferenceCounted
    public final boolean release(int decrement) {
        return this.refCnt.release(decrement);
    }

    /* loaded from: classes4.dex */
    static abstract class AbstractCertificateVerifier extends CertificateVerifier {
        private final OpenSslEngineMap engineMap;

        abstract void verify(ReferenceCountedOpenSslEngine referenceCountedOpenSslEngine, X509Certificate[] x509CertificateArr, String str) throws Exception;

        /* JADX INFO: Access modifiers changed from: package-private */
        public AbstractCertificateVerifier(OpenSslEngineMap engineMap) {
            this.engineMap = engineMap;
        }

        public final int verify(long ssl, byte[][] chain, String auth) {
            ReferenceCountedOpenSslEngine engine = this.engineMap.get(ssl);
            if (engine == null) {
                return CertificateVerifier.X509_V_ERR_UNSPECIFIED;
            }
            X509Certificate[] peerCerts = ReferenceCountedOpenSslContext.certificates(chain);
            try {
                verify(engine, peerCerts, auth);
                return CertificateVerifier.X509_V_OK;
            } catch (Throwable cause) {
                ReferenceCountedOpenSslContext.logger.debug("verification of certificate failed", cause);
                engine.initHandshakeException(cause);
                if (cause instanceof OpenSslCertificateException) {
                    return ((OpenSslCertificateException) cause).errorCode();
                }
                if (cause instanceof CertificateExpiredException) {
                    return CertificateVerifier.X509_V_ERR_CERT_HAS_EXPIRED;
                }
                if (cause instanceof CertificateNotYetValidException) {
                    return CertificateVerifier.X509_V_ERR_CERT_NOT_YET_VALID;
                }
                if (PlatformDependent.javaVersion() >= 7) {
                    return translateToError(cause);
                }
                return CertificateVerifier.X509_V_ERR_UNSPECIFIED;
            }
        }

        private static int translateToError(Throwable cause) {
            if (cause instanceof CertificateRevokedException) {
                return CertificateVerifier.X509_V_ERR_CERT_REVOKED;
            }
            for (Throwable wrapped = cause.getCause(); wrapped != null; wrapped = wrapped.getCause()) {
                if (wrapped instanceof CertPathValidatorException) {
                    CertPathValidatorException ex = (CertPathValidatorException) wrapped;
                    CertPathValidatorException.Reason reason = ex.getReason();
                    if (reason == CertPathValidatorException.BasicReason.EXPIRED) {
                        return CertificateVerifier.X509_V_ERR_CERT_HAS_EXPIRED;
                    }
                    if (reason == CertPathValidatorException.BasicReason.NOT_YET_VALID) {
                        return CertificateVerifier.X509_V_ERR_CERT_NOT_YET_VALID;
                    }
                    if (reason == CertPathValidatorException.BasicReason.REVOKED) {
                        return CertificateVerifier.X509_V_ERR_CERT_REVOKED;
                    }
                }
            }
            return CertificateVerifier.X509_V_ERR_UNSPECIFIED;
        }
    }

    /* loaded from: classes4.dex */
    private static final class DefaultOpenSslEngineMap implements OpenSslEngineMap {
        private final Map<Long, ReferenceCountedOpenSslEngine> engines;

        private DefaultOpenSslEngineMap() {
            this.engines = PlatformDependent.newConcurrentHashMap();
        }

        @Override // io.netty.handler.ssl.OpenSslEngineMap
        public ReferenceCountedOpenSslEngine remove(long ssl) {
            return this.engines.remove(Long.valueOf(ssl));
        }

        @Override // io.netty.handler.ssl.OpenSslEngineMap
        public void add(ReferenceCountedOpenSslEngine engine) {
            this.engines.put(Long.valueOf(engine.sslPointer()), engine);
        }

        @Override // io.netty.handler.ssl.OpenSslEngineMap
        public ReferenceCountedOpenSslEngine get(long ssl) {
            return this.engines.get(Long.valueOf(ssl));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void setKeyMaterial(long ctx, X509Certificate[] keyCertChain, PrivateKey key, String keyPassword) throws SSLException {
        long keyCertChainBio;
        long keyCertChainBio2 = 0;
        long keyCertChainBio22 = 0;
        PemEncoded encoded = null;
        try {
            try {
                try {
                    encoded = PemX509Certificate.toPEM(ByteBufAllocator.DEFAULT, true, keyCertChain);
                    keyCertChainBio = toBIO(ByteBufAllocator.DEFAULT, encoded.retain());
                    try {
                        keyCertChainBio22 = toBIO(ByteBufAllocator.DEFAULT, encoded.retain());
                        keyBio = key != null ? toBIO(ByteBufAllocator.DEFAULT, key) : 0L;
                        SSLContext.setCertificateBio(ctx, keyCertChainBio, keyBio, keyPassword == null ? "" : keyPassword);
                    } catch (SSLException e) {
                        e = e;
                    } catch (Exception e2) {
                        e = e2;
                    } catch (Throwable th) {
                        e = th;
                    }
                } catch (SSLException e3) {
                    throw e3;
                } catch (Exception e4) {
                    e = e4;
                } catch (Throwable th2) {
                    e = th2;
                }
            } catch (SSLException e5) {
                throw e5;
            } catch (Exception e6) {
                e = e6;
            } catch (Throwable th3) {
                e = th3;
            }
            try {
                SSLContext.setCertificateChainBio(ctx, keyCertChainBio22, true);
                freeBio(keyBio);
                freeBio(keyCertChainBio);
                freeBio(keyCertChainBio22);
                if (encoded != null) {
                    encoded.release();
                }
            } catch (SSLException e7) {
                e = e7;
                throw e;
            } catch (Exception e8) {
                e = e8;
                throw new SSLException("failed to set certificate and key", e);
            } catch (Throwable th4) {
                e = th4;
                keyCertChainBio2 = keyCertChainBio;
                freeBio(keyBio);
                freeBio(keyCertChainBio2);
                freeBio(keyCertChainBio22);
                if (encoded != null) {
                    encoded.release();
                }
                throw e;
            }
        } catch (Throwable th5) {
            e = th5;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void freeBio(long bio) {
        if (bio != 0) {
            SSL.freeBIO(bio);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long toBIO(ByteBufAllocator allocator, PrivateKey key) throws Exception {
        if (key == null) {
            return 0L;
        }
        PemEncoded pem = PemPrivateKey.toPEM(allocator, true, key);
        try {
            return toBIO(allocator, pem.retain());
        } finally {
            pem.release();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long toBIO(ByteBufAllocator allocator, X509Certificate... certChain) throws Exception {
        if (certChain == null) {
            return 0L;
        }
        ObjectUtil.checkNonEmpty(certChain, "certChain");
        PemEncoded pem = PemX509Certificate.toPEM(allocator, true, certChain);
        try {
            return toBIO(allocator, pem.retain());
        } finally {
            pem.release();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long toBIO(ByteBufAllocator allocator, PemEncoded pem) throws Exception {
        try {
            ByteBuf content = pem.content();
            if (content.isDirect()) {
                return newBIO(content.retainedSlice());
            }
            ByteBuf buffer = allocator.directBuffer(content.readableBytes());
            try {
                buffer.writeBytes(content, content.readerIndex(), content.readableBytes());
                long newBIO = newBIO(buffer.retainedSlice());
                try {
                    if (pem.isSensitive()) {
                        SslUtils.zeroout(buffer);
                    }
                    return newBIO;
                } catch (Throwable th) {
                    throw th;
                }
            } catch (Throwable th2) {
                try {
                    if (pem.isSensitive()) {
                        SslUtils.zeroout(buffer);
                    }
                    throw th2;
                } finally {
                    buffer.release();
                }
            }
        } finally {
            pem.release();
        }
    }

    private static long newBIO(ByteBuf buffer) throws Exception {
        try {
            long bio = SSL.newMemBIO();
            int readable = buffer.readableBytes();
            if (SSL.bioWrite(bio, OpenSsl.memoryAddress(buffer) + buffer.readerIndex(), readable) != readable) {
                SSL.freeBIO(bio);
                throw new IllegalStateException("Could not write data to memory BIO");
            }
            return bio;
        } finally {
            buffer.release();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static OpenSslKeyMaterialProvider providerFor(KeyManagerFactory factory, String password) {
        if (factory instanceof OpenSslX509KeyManagerFactory) {
            return ((OpenSslX509KeyManagerFactory) factory).newProvider();
        }
        if (factory instanceof OpenSslCachingX509KeyManagerFactory) {
            return ((OpenSslCachingX509KeyManagerFactory) factory).newProvider(password);
        }
        return new OpenSslKeyMaterialProvider(chooseX509KeyManager(factory.getKeyManagers()), password);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static ReferenceCountedOpenSslEngine retrieveEngine(OpenSslEngineMap engineMap, long ssl) throws SSLException {
        ReferenceCountedOpenSslEngine engine = engineMap.get(ssl);
        if (engine == null) {
            throw new SSLException("Could not find a " + StringUtil.simpleClassName((Class<?>) ReferenceCountedOpenSslEngine.class) + " for sslPointer " + ssl);
        }
        return engine;
    }

    /* loaded from: classes4.dex */
    private static final class PrivateKeyMethod implements SSLPrivateKeyMethod {
        private final OpenSslEngineMap engineMap;
        private final OpenSslPrivateKeyMethod keyMethod;

        PrivateKeyMethod(OpenSslEngineMap engineMap, OpenSslPrivateKeyMethod keyMethod) {
            this.engineMap = engineMap;
            this.keyMethod = keyMethod;
        }

        public byte[] sign(long ssl, int signatureAlgorithm, byte[] digest) throws Exception {
            ReferenceCountedOpenSslEngine engine = ReferenceCountedOpenSslContext.retrieveEngine(this.engineMap, ssl);
            try {
                return ReferenceCountedOpenSslContext.verifyResult(this.keyMethod.sign(engine, signatureAlgorithm, digest));
            } catch (Exception e) {
                engine.initHandshakeException(e);
                throw e;
            }
        }

        public byte[] decrypt(long ssl, byte[] input) throws Exception {
            ReferenceCountedOpenSslEngine engine = ReferenceCountedOpenSslContext.retrieveEngine(this.engineMap, ssl);
            try {
                return ReferenceCountedOpenSslContext.verifyResult(this.keyMethod.decrypt(engine, input));
            } catch (Exception e) {
                engine.initHandshakeException(e);
                throw e;
            }
        }
    }

    /* loaded from: classes4.dex */
    private static final class AsyncPrivateKeyMethod implements AsyncSSLPrivateKeyMethod {
        private final OpenSslEngineMap engineMap;
        private final OpenSslAsyncPrivateKeyMethod keyMethod;

        AsyncPrivateKeyMethod(OpenSslEngineMap engineMap, OpenSslAsyncPrivateKeyMethod keyMethod) {
            this.engineMap = engineMap;
            this.keyMethod = keyMethod;
        }

        public void sign(long ssl, int signatureAlgorithm, byte[] bytes, ResultCallback<byte[]> resultCallback) {
            try {
                ReferenceCountedOpenSslEngine engine = ReferenceCountedOpenSslContext.retrieveEngine(this.engineMap, ssl);
                this.keyMethod.sign(engine, signatureAlgorithm, bytes).addListener(new ResultCallbackListener(engine, ssl, resultCallback));
            } catch (SSLException e) {
                resultCallback.onError(ssl, e);
            }
        }

        public void decrypt(long ssl, byte[] bytes, ResultCallback<byte[]> resultCallback) {
            try {
                ReferenceCountedOpenSslEngine engine = ReferenceCountedOpenSslContext.retrieveEngine(this.engineMap, ssl);
                this.keyMethod.decrypt(engine, bytes).addListener(new ResultCallbackListener(engine, ssl, resultCallback));
            } catch (SSLException e) {
                resultCallback.onError(ssl, e);
            }
        }

        /* loaded from: classes4.dex */
        private static final class ResultCallbackListener implements FutureListener<byte[]> {
            private final ReferenceCountedOpenSslEngine engine;
            private final ResultCallback<byte[]> resultCallback;
            private final long ssl;

            ResultCallbackListener(ReferenceCountedOpenSslEngine engine, long ssl, ResultCallback<byte[]> resultCallback) {
                this.engine = engine;
                this.ssl = ssl;
                this.resultCallback = resultCallback;
            }

            @Override // io.netty.util.concurrent.GenericFutureListener
            public void operationComplete(Future<byte[]> future) {
                Throwable cause = future.cause();
                if (cause == null) {
                    try {
                        byte[] result = ReferenceCountedOpenSslContext.verifyResult(future.getNow());
                        this.resultCallback.onSuccess(this.ssl, result);
                        return;
                    } catch (SignatureException e) {
                        cause = e;
                        this.engine.initHandshakeException(e);
                    }
                }
                this.resultCallback.onError(this.ssl, cause);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static byte[] verifyResult(byte[] result) throws SignatureException {
        if (result == null) {
            throw new SignatureException();
        }
        return result;
    }

    /* loaded from: classes4.dex */
    private static final class CompressionAlgorithm implements CertificateCompressionAlgo {
        private final OpenSslCertificateCompressionAlgorithm compressionAlgorithm;
        private final OpenSslEngineMap engineMap;

        CompressionAlgorithm(OpenSslEngineMap engineMap, OpenSslCertificateCompressionAlgorithm compressionAlgorithm) {
            this.engineMap = engineMap;
            this.compressionAlgorithm = compressionAlgorithm;
        }

        public byte[] compress(long ssl, byte[] bytes) throws Exception {
            ReferenceCountedOpenSslEngine engine = ReferenceCountedOpenSslContext.retrieveEngine(this.engineMap, ssl);
            return this.compressionAlgorithm.compress(engine, bytes);
        }

        public byte[] decompress(long ssl, int len, byte[] bytes) throws Exception {
            ReferenceCountedOpenSslEngine engine = ReferenceCountedOpenSslContext.retrieveEngine(this.engineMap, ssl);
            return this.compressionAlgorithm.decompress(engine, len, bytes);
        }

        public int algorithmId() {
            return this.compressionAlgorithm.algorithmId();
        }
    }
}
