package io.netty.handler.ssl;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.handler.ssl.ApplicationProtocolConfig;
import io.netty.handler.ssl.util.LazyJavaxX509Certificate;
import io.netty.handler.ssl.util.LazyX509Certificate;
import io.netty.internal.tcnative.AsyncTask;
import io.netty.internal.tcnative.Buffer;
import io.netty.internal.tcnative.SSL;
import io.netty.util.AbstractReferenceCounted;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCounted;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.ResourceLeakDetectorFactory;
import io.netty.util.ResourceLeakTracker;
import io.netty.util.internal.EmptyArrays;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.ThrowableUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.nio.ByteBuffer;
import java.security.Principal;
import java.security.cert.Certificate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSessionBindingEvent;
import javax.net.ssl.SSLSessionBindingListener;
import javax.security.cert.X509Certificate;
import org.jose4j.keys.AesKey;

/* loaded from: classes4.dex */
public class ReferenceCountedOpenSslEngine extends SSLEngine implements ReferenceCounted, ApplicationProtocolAccessor {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int OPENSSL_OP_NO_PROTOCOL_INDEX_SSLV2 = 0;
    private static final int OPENSSL_OP_NO_PROTOCOL_INDEX_SSLV3 = 1;
    private static final int OPENSSL_OP_NO_PROTOCOL_INDEX_TLSv1 = 2;
    private static final int OPENSSL_OP_NO_PROTOCOL_INDEX_TLSv1_1 = 3;
    private static final int OPENSSL_OP_NO_PROTOCOL_INDEX_TLSv1_2 = 4;
    private static final int OPENSSL_OP_NO_PROTOCOL_INDEX_TLSv1_3 = 5;
    private Object algorithmConstraints;
    final ByteBufAllocator alloc;
    private final OpenSslApplicationProtocolNegotiator apn;
    private volatile String applicationProtocol;
    private volatile ClientAuth clientAuth;
    private final boolean clientMode;
    private volatile boolean destroyed;
    private final boolean enableOcsp;
    private final Set<String> enabledProtocols;
    private String endPointIdentificationAlgorithm;
    private final OpenSslEngineMap engineMap;
    private HandshakeState handshakeState;
    private boolean hasTLSv13Cipher;
    private boolean isInboundDone;
    final boolean jdkCompatibilityMode;
    private final ResourceLeakTracker<ReferenceCountedOpenSslEngine> leak;
    private volatile Collection<?> matchers;
    private int maxWrapBufferSize;
    private int maxWrapOverhead;
    private volatile boolean needTask;
    private long networkBIO;
    private boolean outboundClosed;
    private final ReferenceCountedOpenSslContext parentContext;
    private Throwable pendingException;
    private boolean receivedShutdown;
    private final AbstractReferenceCounted refCnt;
    private final OpenSslSession session;
    private boolean sessionSet;
    private final ByteBuffer[] singleDstBuffer;
    private final ByteBuffer[] singleSrcBuffer;
    private List<String> sniHostNames;
    private long ssl;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) ReferenceCountedOpenSslEngine.class);
    private static final ResourceLeakDetector<ReferenceCountedOpenSslEngine> leakDetector = ResourceLeakDetectorFactory.instance().newResourceLeakDetector(ReferenceCountedOpenSslEngine.class);
    private static final int[] OPENSSL_OP_NO_PROTOCOLS = {SSL.SSL_OP_NO_SSLv2, SSL.SSL_OP_NO_SSLv3, SSL.SSL_OP_NO_TLSv1, SSL.SSL_OP_NO_TLSv1_1, SSL.SSL_OP_NO_TLSv1_2, SSL.SSL_OP_NO_TLSv1_3};
    static final int MAX_PLAINTEXT_LENGTH = SSL.SSL_MAX_PLAINTEXT_LENGTH;
    static final int MAX_RECORD_SIZE = SSL.SSL_MAX_RECORD_LENGTH;
    private static final SSLEngineResult NEED_UNWRAP_OK = new SSLEngineResult(SSLEngineResult.Status.OK, SSLEngineResult.HandshakeStatus.NEED_UNWRAP, 0, 0);
    private static final SSLEngineResult NEED_UNWRAP_CLOSED = new SSLEngineResult(SSLEngineResult.Status.CLOSED, SSLEngineResult.HandshakeStatus.NEED_UNWRAP, 0, 0);
    private static final SSLEngineResult NEED_WRAP_OK = new SSLEngineResult(SSLEngineResult.Status.OK, SSLEngineResult.HandshakeStatus.NEED_WRAP, 0, 0);
    private static final SSLEngineResult NEED_WRAP_CLOSED = new SSLEngineResult(SSLEngineResult.Status.CLOSED, SSLEngineResult.HandshakeStatus.NEED_WRAP, 0, 0);
    private static final SSLEngineResult CLOSED_NOT_HANDSHAKING = new SSLEngineResult(SSLEngineResult.Status.CLOSED, SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING, 0, 0);
    private static final X509Certificate[] JAVAX_CERTS_NOT_SUPPORTED = new X509Certificate[0];

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public enum HandshakeState {
        NOT_STARTED,
        STARTED_IMPLICITLY,
        STARTED_EXPLICITLY,
        FINISHED
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public interface NativeSslException {
        int errorCode();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ReferenceCountedOpenSslEngine(ReferenceCountedOpenSslContext context, ByteBufAllocator alloc, String peerHost, int peerPort, boolean jdkCompatibilityMode, boolean leakDetection) {
        super(peerHost, peerPort);
        this.handshakeState = HandshakeState.NOT_STARTED;
        this.refCnt = new AbstractReferenceCounted() { // from class: io.netty.handler.ssl.ReferenceCountedOpenSslEngine.1
            static final /* synthetic */ boolean $assertionsDisabled = false;

            @Override // io.netty.util.ReferenceCounted
            public ReferenceCounted touch(Object hint) {
                if (ReferenceCountedOpenSslEngine.this.leak != null) {
                    ReferenceCountedOpenSslEngine.this.leak.record(hint);
                }
                return ReferenceCountedOpenSslEngine.this;
            }

            @Override // io.netty.util.AbstractReferenceCounted
            protected void deallocate() {
                ReferenceCountedOpenSslEngine.this.shutdown();
                if (ReferenceCountedOpenSslEngine.this.leak != null) {
                    boolean closed = ReferenceCountedOpenSslEngine.this.leak.close(ReferenceCountedOpenSslEngine.this);
                    if (!closed) {
                        throw new AssertionError();
                    }
                }
                ReferenceCountedOpenSslEngine.this.parentContext.release();
            }
        };
        this.enabledProtocols = new LinkedHashSet();
        this.clientAuth = ClientAuth.NONE;
        boolean z = true;
        this.singleSrcBuffer = new ByteBuffer[1];
        this.singleDstBuffer = new ByteBuffer[1];
        OpenSsl.ensureAvailability();
        this.engineMap = context.engineMap;
        this.enableOcsp = context.enableOcsp;
        this.jdkCompatibilityMode = jdkCompatibilityMode;
        this.alloc = (ByteBufAllocator) ObjectUtil.checkNotNull(alloc, "alloc");
        this.apn = (OpenSslApplicationProtocolNegotiator) context.applicationProtocolNegotiator();
        this.clientMode = context.isClient();
        if (PlatformDependent.javaVersion() >= 7) {
            this.session = new ExtendedOpenSslSession(new DefaultOpenSslSession(context.sessionContext())) { // from class: io.netty.handler.ssl.ReferenceCountedOpenSslEngine.2
                private String[] peerSupportedSignatureAlgorithms;
                private List requestedServerNames;

                @Override // io.netty.handler.ssl.ExtendedOpenSslSession, javax.net.ssl.ExtendedSSLSession
                public List getRequestedServerNames() {
                    List list;
                    if (ReferenceCountedOpenSslEngine.this.clientMode) {
                        return Java8SslUtils.getSniHostNames((List<String>) ReferenceCountedOpenSslEngine.this.sniHostNames);
                    }
                    synchronized (ReferenceCountedOpenSslEngine.this) {
                        if (this.requestedServerNames == null) {
                            if (!ReferenceCountedOpenSslEngine.this.isDestroyed()) {
                                String name = SSL.getSniHostname(ReferenceCountedOpenSslEngine.this.ssl);
                                if (name == null) {
                                    this.requestedServerNames = Collections.emptyList();
                                } else {
                                    this.requestedServerNames = Java8SslUtils.getSniHostName(SSL.getSniHostname(ReferenceCountedOpenSslEngine.this.ssl).getBytes(CharsetUtil.UTF_8));
                                }
                            } else {
                                this.requestedServerNames = Collections.emptyList();
                            }
                        }
                        list = this.requestedServerNames;
                    }
                    return list;
                }

                @Override // io.netty.handler.ssl.ExtendedOpenSslSession, javax.net.ssl.ExtendedSSLSession
                public String[] getPeerSupportedSignatureAlgorithms() {
                    String[] strArr;
                    synchronized (ReferenceCountedOpenSslEngine.this) {
                        if (this.peerSupportedSignatureAlgorithms == null) {
                            if (!ReferenceCountedOpenSslEngine.this.isDestroyed()) {
                                String[] algs = SSL.getSigAlgs(ReferenceCountedOpenSslEngine.this.ssl);
                                if (algs == null) {
                                    this.peerSupportedSignatureAlgorithms = EmptyArrays.EMPTY_STRINGS;
                                } else {
                                    Set<String> algorithmList = new LinkedHashSet<>(algs.length);
                                    for (String alg : algs) {
                                        String converted = SignatureAlgorithmConverter.toJavaName(alg);
                                        if (converted != null) {
                                            algorithmList.add(converted);
                                        }
                                    }
                                    this.peerSupportedSignatureAlgorithms = (String[]) algorithmList.toArray(EmptyArrays.EMPTY_STRINGS);
                                }
                            } else {
                                this.peerSupportedSignatureAlgorithms = EmptyArrays.EMPTY_STRINGS;
                            }
                        }
                        strArr = (String[]) this.peerSupportedSignatureAlgorithms.clone();
                    }
                    return strArr;
                }

                @Override // io.netty.handler.ssl.ExtendedOpenSslSession
                public List<byte[]> getStatusResponses() {
                    byte[] ocspResponse = null;
                    if (ReferenceCountedOpenSslEngine.this.enableOcsp && ReferenceCountedOpenSslEngine.this.clientMode) {
                        synchronized (ReferenceCountedOpenSslEngine.this) {
                            if (!ReferenceCountedOpenSslEngine.this.isDestroyed()) {
                                ocspResponse = SSL.getOcspResponse(ReferenceCountedOpenSslEngine.this.ssl);
                            }
                        }
                    }
                    return ocspResponse == null ? Collections.emptyList() : Collections.singletonList(ocspResponse);
                }
            };
        } else {
            this.session = new DefaultOpenSslSession(context.sessionContext());
        }
        if (!context.sessionContext().useKeyManager()) {
            this.session.setLocalCertificate(context.keyCertChain);
        }
        Lock readerLock = context.ctxLock.readLock();
        readerLock.lock();
        try {
            long j = context.ctx;
            if (context.isClient()) {
                z = false;
            }
            long finalSsl = SSL.newSSL(j, z);
            synchronized (this) {
                this.ssl = finalSsl;
                try {
                    this.networkBIO = SSL.bioNewByteBuffer(this.ssl, context.getBioNonApplicationBufferSize());
                    setClientAuth(this.clientMode ? ClientAuth.NONE : context.clientAuth);
                } catch (Throwable cause) {
                    shutdown();
                    PlatformDependent.throwException(cause);
                }
                if (context.protocols == null) {
                    throw new AssertionError();
                }
                this.hasTLSv13Cipher = context.hasTLSv13Cipher;
                setEnabledProtocols(context.protocols);
                if (this.clientMode && SslUtils.isValidHostNameForSNI(peerHost)) {
                    if (PlatformDependent.javaVersion() >= 8) {
                        if (Java8SslUtils.isValidHostNameForSNI(peerHost)) {
                            SSL.setTlsExtHostName(this.ssl, peerHost);
                            this.sniHostNames = Collections.singletonList(peerHost);
                        }
                    } else {
                        SSL.setTlsExtHostName(this.ssl, peerHost);
                        this.sniHostNames = Collections.singletonList(peerHost);
                    }
                }
                if (this.enableOcsp) {
                    SSL.enableOcsp(this.ssl);
                }
                if (!jdkCompatibilityMode) {
                    SSL.setMode(this.ssl, SSL.getMode(this.ssl) | SSL.SSL_MODE_ENABLE_PARTIAL_WRITE);
                }
                if (isProtocolEnabled(SSL.getOptions(this.ssl), SSL.SSL_OP_NO_TLSv1_3, SslProtocols.TLS_v1_3)) {
                    boolean enableTickets = this.clientMode ? ReferenceCountedOpenSslContext.CLIENT_ENABLE_SESSION_TICKET_TLSV13 : ReferenceCountedOpenSslContext.SERVER_ENABLE_SESSION_TICKET_TLSV13;
                    if (enableTickets) {
                        SSL.clearOptions(this.ssl, SSL.SSL_OP_NO_TICKET);
                    }
                }
                boolean enableTickets2 = OpenSsl.isBoringSSL();
                if (enableTickets2 && this.clientMode) {
                    SSL.setRenegotiateMode(this.ssl, SSL.SSL_RENEGOTIATE_ONCE);
                }
                calculateMaxWrapOverhead();
            }
            this.parentContext = context;
            this.parentContext.retain();
            this.leak = leakDetection ? leakDetector.track(this) : null;
        } finally {
            readerLock.unlock();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final synchronized String[] authMethods() {
        if (isDestroyed()) {
            return EmptyArrays.EMPTY_STRINGS;
        }
        return SSL.authenticationMethods(this.ssl);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean setKeyMaterial(OpenSslKeyMaterial keyMaterial) throws Exception {
        synchronized (this) {
            if (isDestroyed()) {
                return false;
            }
            SSL.setKeyMaterial(this.ssl, keyMaterial.certificateChainAddress(), keyMaterial.privateKeyAddress());
            this.session.setLocalCertificate(keyMaterial.certificateChain());
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final synchronized SecretKeySpec masterKey() {
        if (isDestroyed()) {
            return null;
        }
        return new SecretKeySpec(SSL.getMasterKey(this.ssl), AesKey.ALGORITHM);
    }

    synchronized boolean isSessionReused() {
        if (isDestroyed()) {
            return false;
        }
        return SSL.isSessionReused(this.ssl);
    }

    public void setOcspResponse(byte[] response) {
        if (!this.enableOcsp) {
            throw new IllegalStateException("OCSP stapling is not enabled");
        }
        if (this.clientMode) {
            throw new IllegalStateException("Not a server SSLEngine");
        }
        synchronized (this) {
            if (!isDestroyed()) {
                SSL.setOcspResponse(this.ssl, response);
            }
        }
    }

    public byte[] getOcspResponse() {
        if (!this.enableOcsp) {
            throw new IllegalStateException("OCSP stapling is not enabled");
        }
        if (!this.clientMode) {
            throw new IllegalStateException("Not a client SSLEngine");
        }
        synchronized (this) {
            if (isDestroyed()) {
                return EmptyArrays.EMPTY_BYTES;
            }
            return SSL.getOcspResponse(this.ssl);
        }
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

    @Override // javax.net.ssl.SSLEngine
    public String getApplicationProtocol() {
        return this.applicationProtocol;
    }

    @Override // javax.net.ssl.SSLEngine
    public String getHandshakeApplicationProtocol() {
        return this.applicationProtocol;
    }

    @Override // javax.net.ssl.SSLEngine
    public final synchronized SSLSession getHandshakeSession() {
        switch (this.handshakeState) {
            case NOT_STARTED:
            case FINISHED:
                return null;
            default:
                return this.session;
        }
    }

    public final synchronized long sslPointer() {
        return this.ssl;
    }

    public final synchronized void shutdown() {
        if (!this.destroyed) {
            this.destroyed = true;
            if (this.engineMap != null) {
                this.engineMap.remove(this.ssl);
            }
            SSL.freeSSL(this.ssl);
            this.networkBIO = 0L;
            this.ssl = 0L;
            this.outboundClosed = true;
            this.isInboundDone = true;
        }
        SSL.clearError();
    }

    private int writePlaintextData(ByteBuffer src, int len) {
        int pos = src.position();
        int limit = src.limit();
        if (src.isDirect()) {
            int sslWrote = SSL.writeToSSL(this.ssl, bufferAddress(src) + pos, len);
            if (sslWrote > 0) {
                src.position(pos + sslWrote);
                return sslWrote;
            }
            return sslWrote;
        }
        ByteBuf buf = this.alloc.directBuffer(len);
        try {
            src.limit(pos + len);
            buf.setBytes(0, src);
            src.limit(limit);
            int sslWrote2 = SSL.writeToSSL(this.ssl, OpenSsl.memoryAddress(buf), len);
            if (sslWrote2 > 0) {
                src.position(pos + sslWrote2);
            } else {
                src.position(pos);
            }
            return sslWrote2;
        } finally {
            buf.release();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void bioSetFd(int fd) {
        if (!isDestroyed()) {
            SSL.bioSetFd(this.ssl, fd);
        }
    }

    private ByteBuf writeEncryptedData(ByteBuffer src, int len) throws SSLException {
        int pos = src.position();
        if (src.isDirect()) {
            SSL.bioSetByteBuffer(this.networkBIO, bufferAddress(src) + pos, len, false);
            return null;
        }
        ByteBuf buf = this.alloc.directBuffer(len);
        try {
            int limit = src.limit();
            src.limit(pos + len);
            buf.writeBytes(src);
            src.position(pos);
            src.limit(limit);
            SSL.bioSetByteBuffer(this.networkBIO, OpenSsl.memoryAddress(buf), len, false);
            return buf;
        } catch (Throwable cause) {
            buf.release();
            PlatformDependent.throwException(cause);
            return null;
        }
    }

    private int readPlaintextData(ByteBuffer dst) throws SSLException {
        int pos = dst.position();
        if (dst.isDirect()) {
            int sslRead = SSL.readFromSSL(this.ssl, bufferAddress(dst) + pos, dst.limit() - pos);
            if (sslRead > 0) {
                dst.position(pos + sslRead);
                return sslRead;
            }
            return sslRead;
        }
        int limit = dst.limit();
        int len = Math.min(maxEncryptedPacketLength0(), limit - pos);
        ByteBuf buf = this.alloc.directBuffer(len);
        try {
            int sslRead2 = SSL.readFromSSL(this.ssl, OpenSsl.memoryAddress(buf), len);
            if (sslRead2 > 0) {
                dst.limit(pos + sslRead2);
                buf.getBytes(buf.readerIndex(), dst);
                dst.limit(limit);
            }
            return sslRead2;
        } finally {
            buf.release();
        }
    }

    final synchronized int maxWrapOverhead() {
        return this.maxWrapOverhead;
    }

    final synchronized int maxEncryptedPacketLength() {
        return maxEncryptedPacketLength0();
    }

    final int maxEncryptedPacketLength0() {
        return this.maxWrapOverhead + MAX_PLAINTEXT_LENGTH;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final int calculateMaxLengthForWrap(int plaintextLength, int numComponents) {
        return (int) Math.min(this.maxWrapBufferSize, plaintextLength + (this.maxWrapOverhead * numComponents));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final int calculateOutNetBufSize(int plaintextLength, int numComponents) {
        return (int) Math.min(2147483647L, plaintextLength + (this.maxWrapOverhead * numComponents));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final synchronized int sslPending() {
        return sslPending0();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void calculateMaxWrapOverhead() {
        this.maxWrapOverhead = SSL.getMaxWrapOverhead(this.ssl);
        this.maxWrapBufferSize = this.jdkCompatibilityMode ? maxEncryptedPacketLength0() : maxEncryptedPacketLength0() << 4;
    }

    private int sslPending0() {
        if (this.handshakeState != HandshakeState.FINISHED) {
            return 0;
        }
        return SSL.sslPending(this.ssl);
    }

    private boolean isBytesAvailableEnoughForWrap(int bytesAvailable, int plaintextLength, int numComponents) {
        return ((long) bytesAvailable) - (((long) this.maxWrapOverhead) * ((long) numComponents)) >= ((long) plaintextLength);
    }

    /* JADX WARN: Removed duplicated region for block: B:177:0x0500 A[Catch: all -> 0x093d, TRY_ENTER, TryCatch #2 {all -> 0x093d, blocks: (B:23:0x003f, B:25:0x0045, B:26:0x0073, B:28:0x007f, B:30:0x008a, B:41:0x00e2, B:43:0x00eb, B:54:0x013e, B:56:0x0144, B:67:0x0198, B:78:0x01f4, B:80:0x01fe, B:82:0x0204, B:83:0x0208, B:86:0x0215, B:97:0x0268, B:108:0x02bd, B:110:0x02ce, B:122:0x0322, B:125:0x0328, B:126:0x0338, B:137:0x032b, B:138:0x0336, B:139:0x038e, B:141:0x0392, B:143:0x0398, B:154:0x039b, B:155:0x03eb, B:157:0x03ef, B:169:0x044a, B:171:0x044e, B:177:0x0500, B:179:0x0509, B:190:0x055a, B:363:0x0925, B:364:0x0932, B:365:0x0933, B:366:0x093c, B:369:0x045c, B:371:0x0460, B:375:0x0473, B:376:0x0465, B:380:0x0470, B:383:0x0477, B:384:0x0497, B:386:0x0498, B:388:0x04a4, B:399:0x005a), top: B:22:0x003f }] */
    /* JADX WARN: Removed duplicated region for block: B:365:0x0933 A[Catch: all -> 0x093d, TryCatch #2 {all -> 0x093d, blocks: (B:23:0x003f, B:25:0x0045, B:26:0x0073, B:28:0x007f, B:30:0x008a, B:41:0x00e2, B:43:0x00eb, B:54:0x013e, B:56:0x0144, B:67:0x0198, B:78:0x01f4, B:80:0x01fe, B:82:0x0204, B:83:0x0208, B:86:0x0215, B:97:0x0268, B:108:0x02bd, B:110:0x02ce, B:122:0x0322, B:125:0x0328, B:126:0x0338, B:137:0x032b, B:138:0x0336, B:139:0x038e, B:141:0x0392, B:143:0x0398, B:154:0x039b, B:155:0x03eb, B:157:0x03ef, B:169:0x044a, B:171:0x044e, B:177:0x0500, B:179:0x0509, B:190:0x055a, B:363:0x0925, B:364:0x0932, B:365:0x0933, B:366:0x093c, B:369:0x045c, B:371:0x0460, B:375:0x0473, B:376:0x0465, B:380:0x0470, B:383:0x0477, B:384:0x0497, B:386:0x0498, B:388:0x04a4, B:399:0x005a), top: B:22:0x003f }] */
    @Override // javax.net.ssl.SSLEngine
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final javax.net.ssl.SSLEngineResult wrap(java.nio.ByteBuffer[] r22, int r23, int r24, java.nio.ByteBuffer r25) throws javax.net.ssl.SSLException {
        /*
            Method dump skipped, instructions count: 2509
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.handler.ssl.ReferenceCountedOpenSslEngine.wrap(java.nio.ByteBuffer[], int, int, java.nio.ByteBuffer):javax.net.ssl.SSLEngineResult");
    }

    private SSLEngineResult newResult(SSLEngineResult.HandshakeStatus hs, int bytesConsumed, int bytesProduced) {
        return newResult(SSLEngineResult.Status.OK, hs, bytesConsumed, bytesProduced);
    }

    private SSLEngineResult newResult(SSLEngineResult.Status status, SSLEngineResult.HandshakeStatus hs, int bytesConsumed, int bytesProduced) {
        if (isOutboundDone()) {
            if (isInboundDone()) {
                hs = SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING;
                shutdown();
            }
            return new SSLEngineResult(SSLEngineResult.Status.CLOSED, hs, bytesConsumed, bytesProduced);
        }
        if (hs == SSLEngineResult.HandshakeStatus.NEED_TASK) {
            this.needTask = true;
        }
        return new SSLEngineResult(status, hs, bytesConsumed, bytesProduced);
    }

    private SSLEngineResult newResultMayFinishHandshake(SSLEngineResult.HandshakeStatus hs, int bytesConsumed, int bytesProduced) throws SSLException {
        return newResult(mayFinishHandshake(hs, bytesConsumed, bytesProduced), bytesConsumed, bytesProduced);
    }

    private SSLEngineResult newResultMayFinishHandshake(SSLEngineResult.Status status, SSLEngineResult.HandshakeStatus hs, int bytesConsumed, int bytesProduced) throws SSLException {
        return newResult(status, mayFinishHandshake(hs, bytesConsumed, bytesProduced), bytesConsumed, bytesProduced);
    }

    private SSLException shutdownWithError(String operations, int sslError) {
        return shutdownWithError(operations, sslError, SSL.getLastErrorNumber());
    }

    private SSLException shutdownWithError(String operation, int sslError, int error) {
        if (logger.isDebugEnabled()) {
            String errorString = SSL.getErrorString(error);
            logger.debug("{} failed with {}: OpenSSL error: {} {}", operation, Integer.valueOf(sslError), Integer.valueOf(error), errorString);
        }
        shutdown();
        SSLException exception = newSSLExceptionForError(error);
        if (this.pendingException != null) {
            exception.initCause(this.pendingException);
            this.pendingException = null;
        }
        return exception;
    }

    private SSLEngineResult handleUnwrapException(int bytesConsumed, int bytesProduced, SSLException e) throws SSLException {
        int lastError = SSL.getLastErrorNumber();
        if (lastError != 0) {
            return sslReadErrorResult(SSL.SSL_ERROR_SSL, lastError, bytesConsumed, bytesProduced);
        }
        throw e;
    }

    /* JADX WARN: Code restructure failed: missing block: B:126:0x0235, code lost:            if (r19 <= 0) goto L329;     */
    /* JADX WARN: Code restructure failed: missing block: B:127:0x0237, code lost:            r13 = newResult(javax.net.ssl.SSLEngineResult.Status.BUFFER_OVERFLOW, r15, r4, r8);     */
    /* JADX WARN: Code restructure failed: missing block: B:128:0x0256, code lost:            if (r6 == null) goto L163;     */
    /* JADX WARN: Code restructure failed: missing block: B:131:0x026b, code lost:            io.netty.internal.tcnative.SSL.bioClearByteBuffer(r25.networkBIO);        rejectRemoteInitiatedRenegotiation();     */
    /* JADX WARN: Code restructure failed: missing block: B:133:0x0274, code lost:            return r13;     */
    /* JADX WARN: Code restructure failed: missing block: B:135:0x0275, code lost:            r0 = th;     */
    /* JADX WARN: Code restructure failed: missing block: B:140:0x0258, code lost:            r6.release();     */
    /* JADX WARN: Code restructure failed: missing block: B:142:0x025c, code lost:            r0 = th;     */
    /* JADX WARN: Code restructure failed: missing block: B:148:0x024a, code lost:            if (isInboundDone() == false) goto L155;     */
    /* JADX WARN: Code restructure failed: missing block: B:149:0x024c, code lost:            r13 = javax.net.ssl.SSLEngineResult.Status.CLOSED;     */
    /* JADX WARN: Code restructure failed: missing block: B:150:0x0251, code lost:            r13 = newResultMayFinishHandshake(r13, r15, r4, r8);     */
    /* JADX WARN: Code restructure failed: missing block: B:152:0x024f, code lost:            r13 = javax.net.ssl.SSLEngineResult.Status.OK;     */
    /* JADX WARN: Code restructure failed: missing block: B:153:0x027b, code lost:            r0 = th;     */
    /* JADX WARN: Code restructure failed: missing block: B:155:0x03c6, code lost:            if (r6 != null) goto L274;     */
    /* JADX WARN: Code restructure failed: missing block: B:156:0x03c8, code lost:            r6.release();     */
    /* JADX WARN: Code restructure failed: missing block: B:157:0x03cc, code lost:            throw r0;     */
    /* JADX WARN: Code restructure failed: missing block: B:165:0x0299, code lost:            if (r6 == null) goto L183;     */
    /* JADX WARN: Code restructure failed: missing block: B:166:0x029b, code lost:            r6.release();     */
    /* JADX WARN: Code restructure failed: missing block: B:167:0x02a8, code lost:            r14 = r4;        r7 = r8;     */
    /* JADX WARN: Code restructure failed: missing block: B:185:0x02b8, code lost:            r2 = io.netty.internal.tcnative.SSL.getError(r25.ssl, r0);     */
    /* JADX WARN: Code restructure failed: missing block: B:186:0x02c0, code lost:            if (r2 == io.netty.internal.tcnative.SSL.SSL_ERROR_WANT_READ) goto L233;     */
    /* JADX WARN: Code restructure failed: missing block: B:188:0x02c4, code lost:            if (r2 != io.netty.internal.tcnative.SSL.SSL_ERROR_WANT_WRITE) goto L360;     */
    /* JADX WARN: Code restructure failed: missing block: B:189:0x02c6, code lost:            r13 = r10;     */
    /* JADX WARN: Code restructure failed: missing block: B:190:0x0341, code lost:            r3 = r30 + 1;     */
    /* JADX WARN: Code restructure failed: missing block: B:191:0x0343, code lost:            if (r3 < r11) goto L251;     */
    /* JADX WARN: Code restructure failed: missing block: B:192:0x037b, code lost:            if (r6 == null) goto L256;     */
    /* JADX WARN: Code restructure failed: missing block: B:193:0x0387, code lost:            r2 = r26;        r14 = r4;        r6 = r7;        r7 = r8;        r10 = r13;        r4 = r16;        r8 = r17;        r12 = r21;     */
    /* JADX WARN: Code restructure failed: missing block: B:197:0x037d, code lost:            r6.release();     */
    /* JADX WARN: Code restructure failed: missing block: B:198:0x0381, code lost:            r0 = th;     */
    /* JADX WARN: Code restructure failed: missing block: B:201:0x0345, code lost:            if (r6 == null) goto L238;     */
    /* JADX WARN: Code restructure failed: missing block: B:202:0x0347, code lost:            r6.release();     */
    /* JADX WARN: Code restructure failed: missing block: B:204:0x034a, code lost:            r14 = r4;        r7 = r8;     */
    /* JADX WARN: Code restructure failed: missing block: B:207:0x02cd, code lost:            if (r2 != io.netty.internal.tcnative.SSL.SSL_ERROR_ZERO_RETURN) goto L209;     */
    /* JADX WARN: Code restructure failed: missing block: B:209:0x02d1, code lost:            if (r25.receivedShutdown != false) goto L197;     */
    /* JADX WARN: Code restructure failed: missing block: B:210:0x02d3, code lost:            closeAll();     */
    /* JADX WARN: Code restructure failed: missing block: B:212:0x02da, code lost:            if (isInboundDone() == false) goto L201;     */
    /* JADX WARN: Code restructure failed: missing block: B:213:0x02dc, code lost:            r3 = javax.net.ssl.SSLEngineResult.Status.CLOSED;     */
    /* JADX WARN: Code restructure failed: missing block: B:214:0x02e1, code lost:            r3 = newResultMayFinishHandshake(r3, r15, r4, r8);     */
    /* JADX WARN: Code restructure failed: missing block: B:215:0x02e5, code lost:            if (r6 == null) goto L205;     */
    /* JADX WARN: Code restructure failed: missing block: B:216:0x02e7, code lost:            r6.release();     */
    /* JADX WARN: Code restructure failed: missing block: B:218:0x02ed, code lost:            io.netty.internal.tcnative.SSL.bioClearByteBuffer(r25.networkBIO);        rejectRemoteInitiatedRenegotiation();     */
    /* JADX WARN: Code restructure failed: missing block: B:220:0x02f6, code lost:            return r3;     */
    /* JADX WARN: Code restructure failed: missing block: B:223:0x02df, code lost:            r3 = javax.net.ssl.SSLEngineResult.Status.OK;     */
    /* JADX WARN: Code restructure failed: missing block: B:226:0x02fc, code lost:            if (r2 == io.netty.internal.tcnative.SSL.SSL_ERROR_WANT_X509_LOOKUP) goto L223;     */
    /* JADX WARN: Code restructure failed: missing block: B:228:0x0300, code lost:            if (r2 == io.netty.internal.tcnative.SSL.SSL_ERROR_WANT_CERTIFICATE_VERIFY) goto L223;     */
    /* JADX WARN: Code restructure failed: missing block: B:230:0x0304, code lost:            if (r2 != io.netty.internal.tcnative.SSL.SSL_ERROR_WANT_PRIVATE_KEY_OPERATION) goto L217;     */
    /* JADX WARN: Code restructure failed: missing block: B:232:0x0307, code lost:            r3 = sslReadErrorResult(r2, io.netty.internal.tcnative.SSL.getLastErrorNumber(), r4, r8);     */
    /* JADX WARN: Code restructure failed: missing block: B:233:0x030f, code lost:            if (r6 == null) goto L220;     */
    /* JADX WARN: Code restructure failed: missing block: B:234:0x0311, code lost:            r6.release();     */
    /* JADX WARN: Code restructure failed: missing block: B:235:0x0314, code lost:            io.netty.internal.tcnative.SSL.bioClearByteBuffer(r25.networkBIO);        rejectRemoteInitiatedRenegotiation();     */
    /* JADX WARN: Code restructure failed: missing block: B:237:0x031d, code lost:            return r3;     */
    /* JADX WARN: Code restructure failed: missing block: B:239:0x0322, code lost:            if (isInboundDone() == false) goto L226;     */
    /* JADX WARN: Code restructure failed: missing block: B:240:0x0324, code lost:            r3 = javax.net.ssl.SSLEngineResult.Status.CLOSED;     */
    /* JADX WARN: Code restructure failed: missing block: B:241:0x0329, code lost:            r3 = newResult(r3, javax.net.ssl.SSLEngineResult.HandshakeStatus.NEED_TASK, r4, r8);     */
    /* JADX WARN: Code restructure failed: missing block: B:242:0x032f, code lost:            if (r6 == null) goto L230;     */
    /* JADX WARN: Code restructure failed: missing block: B:243:0x0331, code lost:            r6.release();     */
    /* JADX WARN: Code restructure failed: missing block: B:244:0x0334, code lost:            io.netty.internal.tcnative.SSL.bioClearByteBuffer(r25.networkBIO);        rejectRemoteInitiatedRenegotiation();     */
    /* JADX WARN: Code restructure failed: missing block: B:246:0x033d, code lost:            return r3;     */
    /* JADX WARN: Code restructure failed: missing block: B:247:0x0327, code lost:            r3 = javax.net.ssl.SSLEngineResult.Status.OK;     */
    /* JADX WARN: Code restructure failed: missing block: B:248:0x033e, code lost:            r13 = r10;     */
    /* JADX WARN: Code restructure failed: missing block: B:273:0x01e8, code lost:            if (r6 == null) goto L133;     */
    /* JADX WARN: Code restructure failed: missing block: B:274:0x01f7, code lost:            r14 = r4;        r7 = r8;     */
    /* JADX WARN: Code restructure failed: missing block: B:276:0x01ea, code lost:            r6.release();     */
    /* JADX WARN: Code restructure failed: missing block: B:278:0x01ee, code lost:            r0 = th;     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:323:0x041c A[Catch: all -> 0x0436, TryCatch #6 {all -> 0x0436, blocks: (B:321:0x0416, B:322:0x041b, B:323:0x041c, B:324:0x0426, B:325:0x0427, B:326:0x0435), top: B:62:0x00d2 }] */
    /* JADX WARN: Removed duplicated region for block: B:88:0x0180  */
    /* JADX WARN: Type inference failed for: r25v0, types: [io.netty.handler.ssl.ReferenceCountedOpenSslEngine] */
    /* JADX WARN: Type inference failed for: r30v12, types: [int] */
    /* JADX WARN: Type inference failed for: r30v2, types: [int] */
    /* JADX WARN: Type inference failed for: r30v5 */
    /* JADX WARN: Type inference failed for: r30v6 */
    /* JADX WARN: Type inference failed for: r30v8, types: [java.nio.ByteBuffer] */
    /* JADX WARN: Type inference failed for: r30v9 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final javax.net.ssl.SSLEngineResult unwrap(java.nio.ByteBuffer[] r26, int r27, int r28, java.nio.ByteBuffer[] r29, int r30, int r31) throws javax.net.ssl.SSLException {
        /*
            Method dump skipped, instructions count: 1208
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.handler.ssl.ReferenceCountedOpenSslEngine.unwrap(java.nio.ByteBuffer[], int, int, java.nio.ByteBuffer[], int, int):javax.net.ssl.SSLEngineResult");
    }

    private boolean needWrapAgain(int stackError) {
        if (SSL.bioLengthNonApplication(this.networkBIO) > 0) {
            if (this.pendingException == null) {
                this.pendingException = newSSLExceptionForError(stackError);
            } else if (shouldAddSuppressed(this.pendingException, stackError)) {
                ThrowableUtil.addSuppressed(this.pendingException, newSSLExceptionForError(stackError));
            }
            SSL.clearError();
            return true;
        }
        return false;
    }

    private SSLException newSSLExceptionForError(int stackError) {
        String message = SSL.getErrorString(stackError);
        return this.handshakeState == HandshakeState.FINISHED ? new OpenSslException(message, stackError) : new OpenSslHandshakeException(message, stackError);
    }

    private static boolean shouldAddSuppressed(Throwable target, int errorCode) {
        for (Object obj : ThrowableUtil.getSuppressed(target)) {
            if ((obj instanceof NativeSslException) && ((NativeSslException) obj).errorCode() == errorCode) {
                return false;
            }
        }
        return true;
    }

    private SSLEngineResult sslReadErrorResult(int error, int stackError, int bytesConsumed, int bytesProduced) throws SSLException {
        if (needWrapAgain(stackError)) {
            return new SSLEngineResult(SSLEngineResult.Status.OK, SSLEngineResult.HandshakeStatus.NEED_WRAP, bytesConsumed, bytesProduced);
        }
        throw shutdownWithError("SSL_read", error, stackError);
    }

    private void closeAll() throws SSLException {
        this.receivedShutdown = true;
        closeOutbound();
        closeInbound();
    }

    private void rejectRemoteInitiatedRenegotiation() throws SSLHandshakeException {
        if (!isDestroyed()) {
            if (((!this.clientMode && SSL.getHandshakeCount(this.ssl) > 1) || (this.clientMode && SSL.getHandshakeCount(this.ssl) > 2)) && !SslProtocols.TLS_v1_3.equals(this.session.getProtocol()) && this.handshakeState == HandshakeState.FINISHED) {
                shutdown();
                throw new SSLHandshakeException("remote-initiated renegotiation not allowed");
            }
        }
    }

    public final SSLEngineResult unwrap(ByteBuffer[] srcs, ByteBuffer[] dsts) throws SSLException {
        return unwrap(srcs, 0, srcs.length, dsts, 0, dsts.length);
    }

    private ByteBuffer[] singleSrcBuffer(ByteBuffer src) {
        this.singleSrcBuffer[0] = src;
        return this.singleSrcBuffer;
    }

    private void resetSingleSrcBuffer() {
        this.singleSrcBuffer[0] = null;
    }

    private ByteBuffer[] singleDstBuffer(ByteBuffer src) {
        this.singleDstBuffer[0] = src;
        return this.singleDstBuffer;
    }

    private void resetSingleDstBuffer() {
        this.singleDstBuffer[0] = null;
    }

    @Override // javax.net.ssl.SSLEngine
    public final synchronized SSLEngineResult unwrap(ByteBuffer src, ByteBuffer[] dsts, int offset, int length) throws SSLException {
        try {
        } finally {
            resetSingleSrcBuffer();
        }
        return unwrap(singleSrcBuffer(src), 0, 1, dsts, offset, length);
    }

    @Override // javax.net.ssl.SSLEngine
    public final synchronized SSLEngineResult wrap(ByteBuffer src, ByteBuffer dst) throws SSLException {
        try {
        } finally {
            resetSingleSrcBuffer();
        }
        return wrap(singleSrcBuffer(src), dst);
    }

    @Override // javax.net.ssl.SSLEngine
    public final synchronized SSLEngineResult unwrap(ByteBuffer src, ByteBuffer dst) throws SSLException {
        try {
        } finally {
            resetSingleSrcBuffer();
            resetSingleDstBuffer();
        }
        return unwrap(singleSrcBuffer(src), singleDstBuffer(dst));
    }

    @Override // javax.net.ssl.SSLEngine
    public final synchronized SSLEngineResult unwrap(ByteBuffer src, ByteBuffer[] dsts) throws SSLException {
        try {
        } finally {
            resetSingleSrcBuffer();
        }
        return unwrap(singleSrcBuffer(src), dsts);
    }

    /* loaded from: classes4.dex */
    private class TaskDecorator<R extends Runnable> implements Runnable {
        protected final R task;

        TaskDecorator(R task) {
            this.task = task;
        }

        @Override // java.lang.Runnable
        public void run() {
            ReferenceCountedOpenSslEngine.this.runAndResetNeedTask(this.task);
        }
    }

    /* loaded from: classes4.dex */
    private final class AsyncTaskDecorator extends TaskDecorator<AsyncTask> implements AsyncRunnable {
        AsyncTaskDecorator(AsyncTask task) {
            super(task);
        }

        @Override // io.netty.handler.ssl.AsyncRunnable
        public void run(Runnable runnable) {
            if (ReferenceCountedOpenSslEngine.this.isDestroyed()) {
                return;
            }
            this.task.runAsync(new TaskDecorator(runnable));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void runAndResetNeedTask(Runnable task) {
        synchronized (this) {
            try {
                if (isDestroyed()) {
                    return;
                }
                task.run();
                if (this.handshakeState != HandshakeState.FINISHED && !isDestroyed() && SSL.doHandshake(this.ssl) <= 0) {
                    SSL.clearError();
                }
            } finally {
                this.needTask = false;
            }
        }
    }

    @Override // javax.net.ssl.SSLEngine
    public final synchronized Runnable getDelegatedTask() {
        if (isDestroyed()) {
            return null;
        }
        AsyncTask task = SSL.getTask(this.ssl);
        if (task == null) {
            return null;
        }
        if (task instanceof AsyncTask) {
            return new AsyncTaskDecorator(task);
        }
        return new TaskDecorator(task);
    }

    @Override // javax.net.ssl.SSLEngine
    public final synchronized void closeInbound() throws SSLException {
        if (this.isInboundDone) {
            return;
        }
        this.isInboundDone = true;
        if (isOutboundDone()) {
            shutdown();
        }
        if (this.handshakeState != HandshakeState.NOT_STARTED && !this.receivedShutdown) {
            throw new SSLException("Inbound closed before receiving peer's close_notify: possible truncation attack?");
        }
    }

    @Override // javax.net.ssl.SSLEngine
    public final synchronized boolean isInboundDone() {
        return this.isInboundDone;
    }

    @Override // javax.net.ssl.SSLEngine
    public final synchronized void closeOutbound() {
        if (this.outboundClosed) {
            return;
        }
        this.outboundClosed = true;
        if (this.handshakeState != HandshakeState.NOT_STARTED && !isDestroyed()) {
            int mode = SSL.getShutdown(this.ssl);
            if ((SSL.SSL_SENT_SHUTDOWN & mode) != SSL.SSL_SENT_SHUTDOWN) {
                doSSLShutdown();
            }
        } else {
            shutdown();
        }
    }

    private boolean doSSLShutdown() {
        if (SSL.isInInit(this.ssl) != 0) {
            return false;
        }
        int err = SSL.shutdownSSL(this.ssl);
        if (err < 0) {
            int sslErr = SSL.getError(this.ssl, err);
            if (sslErr == SSL.SSL_ERROR_SYSCALL || sslErr == SSL.SSL_ERROR_SSL) {
                if (logger.isDebugEnabled()) {
                    int error = SSL.getLastErrorNumber();
                    logger.debug("SSL_shutdown failed: OpenSSL error: {} {}", Integer.valueOf(error), SSL.getErrorString(error));
                }
                shutdown();
                return false;
            }
            SSL.clearError();
            return true;
        }
        return true;
    }

    /* JADX WARN: Code restructure failed: missing block: B:8:0x0013, code lost:            if (io.netty.internal.tcnative.SSL.bioLengthNonApplication(r4.networkBIO) == 0) goto L9;     */
    @Override // javax.net.ssl.SSLEngine
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final synchronized boolean isOutboundDone() {
        /*
            r4 = this;
            monitor-enter(r4)
            boolean r0 = r4.outboundClosed     // Catch: java.lang.Throwable -> L1a
            if (r0 == 0) goto L17
            long r0 = r4.networkBIO     // Catch: java.lang.Throwable -> L1a
            r2 = 0
            int r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r0 == 0) goto L15
            long r0 = r4.networkBIO     // Catch: java.lang.Throwable -> L1a
            int r0 = io.netty.internal.tcnative.SSL.bioLengthNonApplication(r0)     // Catch: java.lang.Throwable -> L1a
            if (r0 != 0) goto L17
        L15:
            r0 = 1
            goto L18
        L17:
            r0 = 0
        L18:
            monitor-exit(r4)
            return r0
        L1a:
            r0 = move-exception
            monitor-exit(r4)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.handler.ssl.ReferenceCountedOpenSslEngine.isOutboundDone():boolean");
    }

    @Override // javax.net.ssl.SSLEngine
    public final String[] getSupportedCipherSuites() {
        return (String[]) OpenSsl.AVAILABLE_CIPHER_SUITES.toArray(EmptyArrays.EMPTY_STRINGS);
    }

    @Override // javax.net.ssl.SSLEngine
    public final String[] getEnabledCipherSuites() {
        String[] extraCiphers;
        boolean tls13Enabled;
        synchronized (this) {
            if (!isDestroyed()) {
                String[] enabled = SSL.getCiphers(this.ssl);
                int opts = SSL.getOptions(this.ssl);
                if (isProtocolEnabled(opts, SSL.SSL_OP_NO_TLSv1_3, SslProtocols.TLS_v1_3)) {
                    extraCiphers = OpenSsl.EXTRA_SUPPORTED_TLS_1_3_CIPHERS;
                    tls13Enabled = true;
                } else {
                    extraCiphers = EmptyArrays.EMPTY_STRINGS;
                    tls13Enabled = false;
                }
                if (enabled == null) {
                    return EmptyArrays.EMPTY_STRINGS;
                }
                Set<String> enabledSet = new LinkedHashSet<>(enabled.length + extraCiphers.length);
                synchronized (this) {
                    for (int i = 0; i < enabled.length; i++) {
                        String mapped = toJavaCipherSuite(enabled[i]);
                        String cipher = mapped == null ? enabled[i] : mapped;
                        if ((tls13Enabled && OpenSsl.isTlsv13Supported()) || !SslUtils.isTLSv13Cipher(cipher)) {
                            enabledSet.add(cipher);
                        }
                    }
                    Collections.addAll(enabledSet, extraCiphers);
                }
                return (String[]) enabledSet.toArray(EmptyArrays.EMPTY_STRINGS);
            }
            return EmptyArrays.EMPTY_STRINGS;
        }
    }

    @Override // javax.net.ssl.SSLEngine
    public final void setEnabledCipherSuites(String[] cipherSuites) {
        ObjectUtil.checkNotNull(cipherSuites, "cipherSuites");
        StringBuilder buf = new StringBuilder();
        StringBuilder bufTLSv13 = new StringBuilder();
        CipherSuiteConverter.convertToCipherStrings(Arrays.asList(cipherSuites), buf, bufTLSv13, OpenSsl.isBoringSSL());
        String cipherSuiteSpec = buf.toString();
        String cipherSuiteSpecTLSv13 = bufTLSv13.toString();
        if (!OpenSsl.isTlsv13Supported() && !cipherSuiteSpecTLSv13.isEmpty()) {
            throw new IllegalArgumentException("TLSv1.3 is not supported by this java version.");
        }
        synchronized (this) {
            boolean z = true;
            this.hasTLSv13Cipher = !cipherSuiteSpecTLSv13.isEmpty();
            if (!isDestroyed()) {
                try {
                    SSL.setCipherSuites(this.ssl, cipherSuiteSpec, false);
                    if (OpenSsl.isTlsv13Supported()) {
                        SSL.setCipherSuites(this.ssl, OpenSsl.checkTls13Ciphers(logger, cipherSuiteSpecTLSv13), true);
                    }
                    Set<String> protocols = new HashSet<>(this.enabledProtocols);
                    if (cipherSuiteSpec.isEmpty()) {
                        protocols.remove(SslProtocols.TLS_v1);
                        protocols.remove(SslProtocols.TLS_v1_1);
                        protocols.remove(SslProtocols.TLS_v1_2);
                        protocols.remove(SslProtocols.SSL_v3);
                        protocols.remove(SslProtocols.SSL_v2);
                        protocols.remove(SslProtocols.SSL_v2_HELLO);
                    }
                    if (cipherSuiteSpecTLSv13.isEmpty()) {
                        protocols.remove(SslProtocols.TLS_v1_3);
                    }
                    String[] strArr = (String[]) protocols.toArray(EmptyArrays.EMPTY_STRINGS);
                    if (this.hasTLSv13Cipher) {
                        z = false;
                    }
                    setEnabledProtocols0(strArr, z);
                } catch (Exception e) {
                    throw new IllegalStateException("failed to enable cipher suites: " + cipherSuiteSpec, e);
                }
            } else {
                throw new IllegalStateException("failed to enable cipher suites: " + cipherSuiteSpec);
            }
        }
    }

    @Override // javax.net.ssl.SSLEngine
    public final String[] getSupportedProtocols() {
        return (String[]) OpenSsl.SUPPORTED_PROTOCOLS_SET.toArray(EmptyArrays.EMPTY_STRINGS);
    }

    @Override // javax.net.ssl.SSLEngine
    public final String[] getEnabledProtocols() {
        return (String[]) this.enabledProtocols.toArray(EmptyArrays.EMPTY_STRINGS);
    }

    private static boolean isProtocolEnabled(int opts, int disableMask, String protocolString) {
        return (opts & disableMask) == 0 && OpenSsl.SUPPORTED_PROTOCOLS_SET.contains(protocolString);
    }

    @Override // javax.net.ssl.SSLEngine
    public final void setEnabledProtocols(String[] protocols) {
        ObjectUtil.checkNotNullWithIAE(protocols, "protocols");
        synchronized (this) {
            this.enabledProtocols.clear();
            this.enabledProtocols.add(SslProtocols.SSL_v2_HELLO);
            Collections.addAll(this.enabledProtocols, protocols);
            setEnabledProtocols0(protocols, !this.hasTLSv13Cipher);
        }
    }

    private void setEnabledProtocols0(String[] protocols, boolean explicitDisableTLSv13) {
        if (!Thread.holdsLock(this)) {
            throw new AssertionError();
        }
        int minProtocolIndex = OPENSSL_OP_NO_PROTOCOLS.length;
        int maxProtocolIndex = 0;
        for (String p : protocols) {
            if (!OpenSsl.SUPPORTED_PROTOCOLS_SET.contains(p)) {
                throw new IllegalArgumentException("Protocol " + p + " is not supported.");
            }
            if (p.equals(SslProtocols.SSL_v2)) {
                if (minProtocolIndex > 0) {
                    minProtocolIndex = 0;
                }
                if (maxProtocolIndex < 0) {
                    maxProtocolIndex = 0;
                }
            } else if (p.equals(SslProtocols.SSL_v3)) {
                if (minProtocolIndex > 1) {
                    minProtocolIndex = 1;
                }
                if (maxProtocolIndex < 1) {
                    maxProtocolIndex = 1;
                }
            } else if (p.equals(SslProtocols.TLS_v1)) {
                if (minProtocolIndex > 2) {
                    minProtocolIndex = 2;
                }
                if (maxProtocolIndex < 2) {
                    maxProtocolIndex = 2;
                }
            } else if (p.equals(SslProtocols.TLS_v1_1)) {
                if (minProtocolIndex > 3) {
                    minProtocolIndex = 3;
                }
                if (maxProtocolIndex < 3) {
                    maxProtocolIndex = 3;
                }
            } else if (p.equals(SslProtocols.TLS_v1_2)) {
                if (minProtocolIndex > 4) {
                    minProtocolIndex = 4;
                }
                if (maxProtocolIndex < 4) {
                    maxProtocolIndex = 4;
                }
            } else if (!explicitDisableTLSv13 && p.equals(SslProtocols.TLS_v1_3)) {
                if (minProtocolIndex > 5) {
                    minProtocolIndex = 5;
                }
                if (maxProtocolIndex < 5) {
                    maxProtocolIndex = 5;
                }
            }
        }
        if (!isDestroyed()) {
            SSL.clearOptions(this.ssl, SSL.SSL_OP_NO_SSLv2 | SSL.SSL_OP_NO_SSLv3 | SSL.SSL_OP_NO_TLSv1 | SSL.SSL_OP_NO_TLSv1_1 | SSL.SSL_OP_NO_TLSv1_2 | SSL.SSL_OP_NO_TLSv1_3);
            int opts = 0;
            for (int i = 0; i < minProtocolIndex; i++) {
                opts |= OPENSSL_OP_NO_PROTOCOLS[i];
            }
            if (maxProtocolIndex == Integer.MAX_VALUE) {
                throw new AssertionError();
            }
            for (int i2 = maxProtocolIndex + 1; i2 < OPENSSL_OP_NO_PROTOCOLS.length; i2++) {
                opts |= OPENSSL_OP_NO_PROTOCOLS[i2];
            }
            SSL.setOptions(this.ssl, opts);
            return;
        }
        throw new IllegalStateException("failed to enable protocols: " + Arrays.asList(protocols));
    }

    @Override // javax.net.ssl.SSLEngine
    public final SSLSession getSession() {
        return this.session;
    }

    @Override // javax.net.ssl.SSLEngine
    public final synchronized void beginHandshake() throws SSLException {
        switch (this.handshakeState) {
            case NOT_STARTED:
                this.handshakeState = HandshakeState.STARTED_EXPLICITLY;
                if (handshake() == SSLEngineResult.HandshakeStatus.NEED_TASK) {
                    this.needTask = true;
                }
                calculateMaxWrapOverhead();
                break;
            case FINISHED:
                throw new SSLException("renegotiation unsupported");
            case STARTED_IMPLICITLY:
                checkEngineClosed();
                this.handshakeState = HandshakeState.STARTED_EXPLICITLY;
                calculateMaxWrapOverhead();
                break;
            case STARTED_EXPLICITLY:
                break;
            default:
                throw new Error();
        }
    }

    private void checkEngineClosed() throws SSLException {
        if (isDestroyed()) {
            throw new SSLException("engine closed");
        }
    }

    private static SSLEngineResult.HandshakeStatus pendingStatus(int pendingStatus) {
        return pendingStatus > 0 ? SSLEngineResult.HandshakeStatus.NEED_WRAP : SSLEngineResult.HandshakeStatus.NEED_UNWRAP;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isEmpty(Object[] arr) {
        return arr == null || arr.length == 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isEmpty(byte[] cert) {
        return cert == null || cert.length == 0;
    }

    private SSLEngineResult.HandshakeStatus handshakeException() throws SSLException {
        if (SSL.bioLengthNonApplication(this.networkBIO) > 0) {
            return SSLEngineResult.HandshakeStatus.NEED_WRAP;
        }
        Throwable exception = this.pendingException;
        if (exception == null) {
            throw new AssertionError();
        }
        this.pendingException = null;
        shutdown();
        if (exception instanceof SSLHandshakeException) {
            throw ((SSLHandshakeException) exception);
        }
        SSLHandshakeException e = new SSLHandshakeException("General OpenSslEngine problem");
        e.initCause(exception);
        throw e;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void initHandshakeException(Throwable cause) {
        if (this.pendingException == null) {
            this.pendingException = cause;
        } else {
            ThrowableUtil.addSuppressed(this.pendingException, cause);
        }
    }

    private SSLEngineResult.HandshakeStatus handshake() throws SSLException {
        if (this.needTask) {
            return SSLEngineResult.HandshakeStatus.NEED_TASK;
        }
        if (this.handshakeState == HandshakeState.FINISHED) {
            return SSLEngineResult.HandshakeStatus.FINISHED;
        }
        checkEngineClosed();
        if (this.pendingException != null) {
            if (SSL.doHandshake(this.ssl) <= 0) {
                SSL.clearError();
            }
            return handshakeException();
        }
        this.engineMap.add(this);
        if (!this.sessionSet) {
            if (!this.parentContext.sessionContext().setSessionFromCache(this.ssl, this.session, getPeerHost(), getPeerPort())) {
                this.session.prepareHandshake();
            }
            this.sessionSet = true;
        }
        int code = SSL.doHandshake(this.ssl);
        if (code <= 0) {
            int sslError = SSL.getError(this.ssl, code);
            if (sslError == SSL.SSL_ERROR_WANT_READ || sslError == SSL.SSL_ERROR_WANT_WRITE) {
                return pendingStatus(SSL.bioLengthNonApplication(this.networkBIO));
            }
            if (sslError == SSL.SSL_ERROR_WANT_X509_LOOKUP || sslError == SSL.SSL_ERROR_WANT_CERTIFICATE_VERIFY || sslError == SSL.SSL_ERROR_WANT_PRIVATE_KEY_OPERATION) {
                return SSLEngineResult.HandshakeStatus.NEED_TASK;
            }
            if (needWrapAgain(SSL.getLastErrorNumber())) {
                return SSLEngineResult.HandshakeStatus.NEED_WRAP;
            }
            if (this.pendingException != null) {
                return handshakeException();
            }
            throw shutdownWithError("SSL_do_handshake", sslError);
        }
        if (SSL.bioLengthNonApplication(this.networkBIO) > 0) {
            return SSLEngineResult.HandshakeStatus.NEED_WRAP;
        }
        this.session.handshakeFinished(SSL.getSessionId(this.ssl), SSL.getCipherForSSL(this.ssl), SSL.getVersion(this.ssl), SSL.getPeerCertificate(this.ssl), SSL.getPeerCertChain(this.ssl), SSL.getTime(this.ssl) * 1000, 1000 * this.parentContext.sessionTimeout());
        selectApplicationProtocol();
        return SSLEngineResult.HandshakeStatus.FINISHED;
    }

    private SSLEngineResult.HandshakeStatus mayFinishHandshake(SSLEngineResult.HandshakeStatus hs, int bytesConsumed, int bytesProduced) throws SSLException {
        if ((hs != SSLEngineResult.HandshakeStatus.NEED_UNWRAP || bytesProduced <= 0) && (hs != SSLEngineResult.HandshakeStatus.NEED_WRAP || bytesConsumed <= 0)) {
            return mayFinishHandshake(hs != SSLEngineResult.HandshakeStatus.FINISHED ? getHandshakeStatus() : SSLEngineResult.HandshakeStatus.FINISHED);
        }
        return handshake();
    }

    private SSLEngineResult.HandshakeStatus mayFinishHandshake(SSLEngineResult.HandshakeStatus status) throws SSLException {
        if (status == SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING) {
            if (this.handshakeState != HandshakeState.FINISHED) {
                return handshake();
            }
            if (!isDestroyed() && SSL.bioLengthNonApplication(this.networkBIO) > 0) {
                return SSLEngineResult.HandshakeStatus.NEED_WRAP;
            }
        }
        return status;
    }

    @Override // javax.net.ssl.SSLEngine
    public final synchronized SSLEngineResult.HandshakeStatus getHandshakeStatus() {
        if (needPendingStatus()) {
            if (this.needTask) {
                return SSLEngineResult.HandshakeStatus.NEED_TASK;
            }
            return pendingStatus(SSL.bioLengthNonApplication(this.networkBIO));
        }
        return SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING;
    }

    private SSLEngineResult.HandshakeStatus getHandshakeStatus(int pending) {
        if (needPendingStatus()) {
            if (this.needTask) {
                return SSLEngineResult.HandshakeStatus.NEED_TASK;
            }
            return pendingStatus(pending);
        }
        return SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING;
    }

    private boolean needPendingStatus() {
        return (this.handshakeState == HandshakeState.NOT_STARTED || isDestroyed() || (this.handshakeState == HandshakeState.FINISHED && !isInboundDone() && !isOutboundDone())) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String toJavaCipherSuite(String openSslCipherSuite) {
        if (openSslCipherSuite == null) {
            return null;
        }
        String version = SSL.getVersion(this.ssl);
        String prefix = toJavaCipherSuitePrefix(version);
        return CipherSuiteConverter.toJava(openSslCipherSuite, prefix);
    }

    private static String toJavaCipherSuitePrefix(String protocolVersion) {
        char c;
        if (protocolVersion == null || protocolVersion.isEmpty()) {
            c = 0;
        } else {
            c = protocolVersion.charAt(0);
        }
        switch (c) {
            case 'S':
                return "SSL";
            case 'T':
                return "TLS";
            default:
                return "UNKNOWN";
        }
    }

    @Override // javax.net.ssl.SSLEngine
    public final void setUseClientMode(boolean clientMode) {
        if (clientMode != this.clientMode) {
            throw new UnsupportedOperationException();
        }
    }

    @Override // javax.net.ssl.SSLEngine
    public final boolean getUseClientMode() {
        return this.clientMode;
    }

    @Override // javax.net.ssl.SSLEngine
    public final void setNeedClientAuth(boolean b) {
        setClientAuth(b ? ClientAuth.REQUIRE : ClientAuth.NONE);
    }

    @Override // javax.net.ssl.SSLEngine
    public final boolean getNeedClientAuth() {
        return this.clientAuth == ClientAuth.REQUIRE;
    }

    @Override // javax.net.ssl.SSLEngine
    public final void setWantClientAuth(boolean b) {
        setClientAuth(b ? ClientAuth.OPTIONAL : ClientAuth.NONE);
    }

    @Override // javax.net.ssl.SSLEngine
    public final boolean getWantClientAuth() {
        return this.clientAuth == ClientAuth.OPTIONAL;
    }

    public final synchronized void setVerify(int verifyMode, int depth) {
        if (!isDestroyed()) {
            SSL.setVerify(this.ssl, verifyMode, depth);
        }
    }

    private void setClientAuth(ClientAuth mode) {
        if (this.clientMode) {
            return;
        }
        synchronized (this) {
            if (this.clientAuth == mode) {
                return;
            }
            if (!isDestroyed()) {
                switch (mode) {
                    case NONE:
                        SSL.setVerify(this.ssl, 0, 10);
                        break;
                    case REQUIRE:
                        SSL.setVerify(this.ssl, 2, 10);
                        break;
                    case OPTIONAL:
                        SSL.setVerify(this.ssl, 1, 10);
                        break;
                    default:
                        throw new Error(mode.toString());
                }
            }
            this.clientAuth = mode;
        }
    }

    @Override // javax.net.ssl.SSLEngine
    public final void setEnableSessionCreation(boolean b) {
        if (b) {
            throw new UnsupportedOperationException();
        }
    }

    @Override // javax.net.ssl.SSLEngine
    public final boolean getEnableSessionCreation() {
        return false;
    }

    @Override // javax.net.ssl.SSLEngine
    public final synchronized SSLParameters getSSLParameters() {
        SSLParameters sslParameters;
        sslParameters = super.getSSLParameters();
        int version = PlatformDependent.javaVersion();
        if (version >= 7) {
            sslParameters.setEndpointIdentificationAlgorithm(this.endPointIdentificationAlgorithm);
            Java7SslParametersUtils.setAlgorithmConstraints(sslParameters, this.algorithmConstraints);
            if (version >= 8) {
                if (this.sniHostNames != null) {
                    Java8SslUtils.setSniHostNames(sslParameters, this.sniHostNames);
                }
                if (!isDestroyed()) {
                    Java8SslUtils.setUseCipherSuitesOrder(sslParameters, (SSL.getOptions(this.ssl) & SSL.SSL_OP_CIPHER_SERVER_PREFERENCE) != 0);
                }
                Java8SslUtils.setSNIMatchers(sslParameters, this.matchers);
            }
        }
        return sslParameters;
    }

    @Override // javax.net.ssl.SSLEngine
    public final synchronized void setSSLParameters(SSLParameters sslParameters) {
        int version = PlatformDependent.javaVersion();
        if (version >= 7) {
            if (sslParameters.getAlgorithmConstraints() != null) {
                throw new IllegalArgumentException("AlgorithmConstraints are not supported.");
            }
            boolean isDestroyed = isDestroyed();
            if (version >= 8) {
                if (!isDestroyed) {
                    if (this.clientMode) {
                        List<String> sniHostNames = Java8SslUtils.getSniHostNames(sslParameters);
                        for (String name : sniHostNames) {
                            SSL.setTlsExtHostName(this.ssl, name);
                        }
                        this.sniHostNames = sniHostNames;
                    }
                    if (Java8SslUtils.getUseCipherSuitesOrder(sslParameters)) {
                        SSL.setOptions(this.ssl, SSL.SSL_OP_CIPHER_SERVER_PREFERENCE);
                    } else {
                        SSL.clearOptions(this.ssl, SSL.SSL_OP_CIPHER_SERVER_PREFERENCE);
                    }
                }
                this.matchers = sslParameters.getSNIMatchers();
            }
            String endPointIdentificationAlgorithm = sslParameters.getEndpointIdentificationAlgorithm();
            if (!isDestroyed && this.clientMode && isEndPointVerificationEnabled(endPointIdentificationAlgorithm)) {
                SSL.setVerify(this.ssl, 2, -1);
            }
            this.endPointIdentificationAlgorithm = endPointIdentificationAlgorithm;
            this.algorithmConstraints = sslParameters.getAlgorithmConstraints();
        }
        super.setSSLParameters(sslParameters);
    }

    private static boolean isEndPointVerificationEnabled(String endPointIdentificationAlgorithm) {
        return (endPointIdentificationAlgorithm == null || endPointIdentificationAlgorithm.isEmpty()) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isDestroyed() {
        return this.destroyed;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean checkSniHostnameMatch(byte[] hostname) {
        return Java8SslUtils.checkSniHostnameMatch(this.matchers, hostname);
    }

    @Override // io.netty.handler.ssl.ApplicationProtocolAccessor
    public String getNegotiatedApplicationProtocol() {
        return this.applicationProtocol;
    }

    private static long bufferAddress(ByteBuffer b) {
        if (!b.isDirect()) {
            throw new AssertionError();
        }
        if (PlatformDependent.hasUnsafe()) {
            return PlatformDependent.directBufferAddress(b);
        }
        return Buffer.address(b);
    }

    private void selectApplicationProtocol() throws SSLException {
        ApplicationProtocolConfig.SelectedListenerFailureBehavior behavior = this.apn.selectedListenerFailureBehavior();
        List<String> protocols = this.apn.protocols();
        switch (this.apn.protocol()) {
            case NONE:
                return;
            case ALPN:
                String applicationProtocol = SSL.getAlpnSelected(this.ssl);
                if (applicationProtocol != null) {
                    this.applicationProtocol = selectApplicationProtocol(protocols, behavior, applicationProtocol);
                    return;
                }
                return;
            case NPN:
                String applicationProtocol2 = SSL.getNextProtoNegotiated(this.ssl);
                if (applicationProtocol2 != null) {
                    this.applicationProtocol = selectApplicationProtocol(protocols, behavior, applicationProtocol2);
                    return;
                }
                return;
            case NPN_AND_ALPN:
                String applicationProtocol3 = SSL.getAlpnSelected(this.ssl);
                if (applicationProtocol3 == null) {
                    applicationProtocol3 = SSL.getNextProtoNegotiated(this.ssl);
                }
                if (applicationProtocol3 != null) {
                    this.applicationProtocol = selectApplicationProtocol(protocols, behavior, applicationProtocol3);
                    return;
                }
                return;
            default:
                throw new Error();
        }
    }

    private String selectApplicationProtocol(List<String> protocols, ApplicationProtocolConfig.SelectedListenerFailureBehavior behavior, String applicationProtocol) throws SSLException {
        if (behavior == ApplicationProtocolConfig.SelectedListenerFailureBehavior.ACCEPT) {
            return applicationProtocol;
        }
        int size = protocols.size();
        if (size <= 0) {
            throw new AssertionError();
        }
        if (protocols.contains(applicationProtocol)) {
            return applicationProtocol;
        }
        if (behavior == ApplicationProtocolConfig.SelectedListenerFailureBehavior.CHOOSE_MY_LAST_PROTOCOL) {
            return protocols.get(size - 1);
        }
        throw new SSLException("unknown protocol " + applicationProtocol);
    }

    /* loaded from: classes4.dex */
    private final class DefaultOpenSslSession implements OpenSslSession {
        private String cipher;
        private long creationTime;
        private volatile Certificate[] localCertificateChain;
        private Certificate[] peerCerts;
        private String protocol;
        private final OpenSslSessionContext sessionContext;
        private X509Certificate[] x509PeerCerts;
        private boolean valid = true;
        private OpenSslSessionId id = OpenSslSessionId.NULL_ID;
        private long lastAccessed = -1;
        private volatile int applicationBufferSize = ReferenceCountedOpenSslEngine.MAX_PLAINTEXT_LENGTH;
        private volatile Map<String, Object> keyValueStorage = new ConcurrentHashMap();

        DefaultOpenSslSession(OpenSslSessionContext sessionContext) {
            this.sessionContext = sessionContext;
        }

        private SSLSessionBindingEvent newSSLSessionBindingEvent(String name) {
            return new SSLSessionBindingEvent(ReferenceCountedOpenSslEngine.this.session, name);
        }

        @Override // io.netty.handler.ssl.OpenSslSession
        public void prepareHandshake() {
            this.keyValueStorage.clear();
        }

        @Override // io.netty.handler.ssl.OpenSslSession
        public void setSessionDetails(long creationTime, long lastAccessedTime, OpenSslSessionId sessionId, Map<String, Object> keyValueStorage) {
            synchronized (ReferenceCountedOpenSslEngine.this) {
                if (this.id == OpenSslSessionId.NULL_ID) {
                    this.id = sessionId;
                    this.creationTime = creationTime;
                    this.lastAccessed = lastAccessedTime;
                    this.keyValueStorage = keyValueStorage;
                }
            }
        }

        @Override // io.netty.handler.ssl.OpenSslSession
        public Map<String, Object> keyValueStorage() {
            return this.keyValueStorage;
        }

        @Override // io.netty.handler.ssl.OpenSslSession
        public OpenSslSessionId sessionId() {
            OpenSslSessionId openSslSessionId;
            byte[] sessionId;
            synchronized (ReferenceCountedOpenSslEngine.this) {
                if (this.id == OpenSslSessionId.NULL_ID && !ReferenceCountedOpenSslEngine.this.isDestroyed() && (sessionId = SSL.getSessionId(ReferenceCountedOpenSslEngine.this.ssl)) != null) {
                    this.id = new OpenSslSessionId(sessionId);
                }
                openSslSessionId = this.id;
            }
            return openSslSessionId;
        }

        @Override // io.netty.handler.ssl.OpenSslSession
        public void setLocalCertificate(Certificate[] localCertificate) {
            this.localCertificateChain = localCertificate;
        }

        @Override // javax.net.ssl.SSLSession
        public byte[] getId() {
            return sessionId().cloneBytes();
        }

        @Override // javax.net.ssl.SSLSession
        public OpenSslSessionContext getSessionContext() {
            return this.sessionContext;
        }

        @Override // javax.net.ssl.SSLSession
        public long getCreationTime() {
            long j;
            synchronized (ReferenceCountedOpenSslEngine.this) {
                j = this.creationTime;
            }
            return j;
        }

        @Override // io.netty.handler.ssl.OpenSslSession
        public void setLastAccessedTime(long time) {
            synchronized (ReferenceCountedOpenSslEngine.this) {
                this.lastAccessed = time;
            }
        }

        @Override // javax.net.ssl.SSLSession
        public long getLastAccessedTime() {
            long j;
            synchronized (ReferenceCountedOpenSslEngine.this) {
                j = this.lastAccessed == -1 ? this.creationTime : this.lastAccessed;
            }
            return j;
        }

        @Override // javax.net.ssl.SSLSession
        public void invalidate() {
            synchronized (ReferenceCountedOpenSslEngine.this) {
                this.valid = false;
                this.sessionContext.removeFromCache(this.id);
            }
        }

        @Override // javax.net.ssl.SSLSession
        public boolean isValid() {
            boolean z;
            synchronized (ReferenceCountedOpenSslEngine.this) {
                z = this.valid || this.sessionContext.isInCache(this.id);
            }
            return z;
        }

        @Override // javax.net.ssl.SSLSession
        public void putValue(String name, Object value) {
            ObjectUtil.checkNotNull(name, "name");
            ObjectUtil.checkNotNull(value, "value");
            Object old = this.keyValueStorage.put(name, value);
            if (value instanceof SSLSessionBindingListener) {
                ((SSLSessionBindingListener) value).valueBound(newSSLSessionBindingEvent(name));
            }
            notifyUnbound(old, name);
        }

        @Override // javax.net.ssl.SSLSession
        public Object getValue(String name) {
            ObjectUtil.checkNotNull(name, "name");
            return this.keyValueStorage.get(name);
        }

        @Override // javax.net.ssl.SSLSession
        public void removeValue(String name) {
            ObjectUtil.checkNotNull(name, "name");
            Object old = this.keyValueStorage.remove(name);
            notifyUnbound(old, name);
        }

        @Override // javax.net.ssl.SSLSession
        public String[] getValueNames() {
            return (String[]) this.keyValueStorage.keySet().toArray(EmptyArrays.EMPTY_STRINGS);
        }

        private void notifyUnbound(Object value, String name) {
            if (value instanceof SSLSessionBindingListener) {
                ((SSLSessionBindingListener) value).valueUnbound(newSSLSessionBindingEvent(name));
            }
        }

        @Override // io.netty.handler.ssl.OpenSslSession
        public void handshakeFinished(byte[] id, String cipher, String protocol, byte[] peerCertificate, byte[][] peerCertificateChain, long creationTime, long timeout) throws SSLException {
            synchronized (ReferenceCountedOpenSslEngine.this) {
                if (!ReferenceCountedOpenSslEngine.this.isDestroyed()) {
                    if (this.id == OpenSslSessionId.NULL_ID) {
                        this.id = id == null ? OpenSslSessionId.NULL_ID : new OpenSslSessionId(id);
                        this.lastAccessed = creationTime;
                        this.creationTime = creationTime;
                    }
                    this.cipher = ReferenceCountedOpenSslEngine.this.toJavaCipherSuite(cipher);
                    this.protocol = protocol;
                    if (ReferenceCountedOpenSslEngine.this.clientMode) {
                        if (ReferenceCountedOpenSslEngine.isEmpty(peerCertificateChain)) {
                            this.peerCerts = EmptyArrays.EMPTY_CERTIFICATES;
                            if (!OpenSsl.JAVAX_CERTIFICATE_CREATION_SUPPORTED) {
                                this.x509PeerCerts = ReferenceCountedOpenSslEngine.JAVAX_CERTS_NOT_SUPPORTED;
                            } else {
                                this.x509PeerCerts = EmptyArrays.EMPTY_JAVAX_X509_CERTIFICATES;
                            }
                        } else {
                            this.peerCerts = new Certificate[peerCertificateChain.length];
                            if (!OpenSsl.JAVAX_CERTIFICATE_CREATION_SUPPORTED) {
                                this.x509PeerCerts = ReferenceCountedOpenSslEngine.JAVAX_CERTS_NOT_SUPPORTED;
                            } else {
                                this.x509PeerCerts = new X509Certificate[peerCertificateChain.length];
                            }
                            initCerts(peerCertificateChain, 0);
                        }
                    } else if (!ReferenceCountedOpenSslEngine.isEmpty(peerCertificate)) {
                        if (ReferenceCountedOpenSslEngine.isEmpty(peerCertificateChain)) {
                            this.peerCerts = new Certificate[]{new LazyX509Certificate(peerCertificate)};
                            if (!OpenSsl.JAVAX_CERTIFICATE_CREATION_SUPPORTED) {
                                this.x509PeerCerts = ReferenceCountedOpenSslEngine.JAVAX_CERTS_NOT_SUPPORTED;
                            } else {
                                this.x509PeerCerts = new X509Certificate[]{new LazyJavaxX509Certificate(peerCertificate)};
                            }
                        } else {
                            this.peerCerts = new Certificate[peerCertificateChain.length + 1];
                            this.peerCerts[0] = new LazyX509Certificate(peerCertificate);
                            if (!OpenSsl.JAVAX_CERTIFICATE_CREATION_SUPPORTED) {
                                this.x509PeerCerts = ReferenceCountedOpenSslEngine.JAVAX_CERTS_NOT_SUPPORTED;
                            } else {
                                this.x509PeerCerts = new X509Certificate[peerCertificateChain.length + 1];
                                this.x509PeerCerts[0] = new LazyJavaxX509Certificate(peerCertificate);
                            }
                            initCerts(peerCertificateChain, 1);
                        }
                    } else {
                        this.peerCerts = EmptyArrays.EMPTY_CERTIFICATES;
                        this.x509PeerCerts = EmptyArrays.EMPTY_JAVAX_X509_CERTIFICATES;
                    }
                    ReferenceCountedOpenSslEngine.this.calculateMaxWrapOverhead();
                    ReferenceCountedOpenSslEngine.this.handshakeState = HandshakeState.FINISHED;
                } else {
                    throw new SSLException("Already closed");
                }
            }
        }

        private void initCerts(byte[][] chain, int startPos) {
            for (int i = 0; i < chain.length; i++) {
                int certPos = startPos + i;
                this.peerCerts[certPos] = new LazyX509Certificate(chain[i]);
                if (this.x509PeerCerts != ReferenceCountedOpenSslEngine.JAVAX_CERTS_NOT_SUPPORTED) {
                    this.x509PeerCerts[certPos] = new LazyJavaxX509Certificate(chain[i]);
                }
            }
        }

        @Override // javax.net.ssl.SSLSession
        public Certificate[] getPeerCertificates() throws SSLPeerUnverifiedException {
            Certificate[] certificateArr;
            synchronized (ReferenceCountedOpenSslEngine.this) {
                if (ReferenceCountedOpenSslEngine.isEmpty(this.peerCerts)) {
                    throw new SSLPeerUnverifiedException("peer not verified");
                }
                certificateArr = (Certificate[]) this.peerCerts.clone();
            }
            return certificateArr;
        }

        @Override // javax.net.ssl.SSLSession
        public Certificate[] getLocalCertificates() {
            Certificate[] localCerts = this.localCertificateChain;
            if (localCerts == null) {
                return null;
            }
            return (Certificate[]) localCerts.clone();
        }

        @Override // javax.net.ssl.SSLSession
        public X509Certificate[] getPeerCertificateChain() throws SSLPeerUnverifiedException {
            X509Certificate[] x509CertificateArr;
            synchronized (ReferenceCountedOpenSslEngine.this) {
                if (this.x509PeerCerts != ReferenceCountedOpenSslEngine.JAVAX_CERTS_NOT_SUPPORTED) {
                    if (ReferenceCountedOpenSslEngine.isEmpty(this.x509PeerCerts)) {
                        throw new SSLPeerUnverifiedException("peer not verified");
                    }
                    x509CertificateArr = (X509Certificate[]) this.x509PeerCerts.clone();
                } else {
                    throw new UnsupportedOperationException();
                }
            }
            return x509CertificateArr;
        }

        @Override // javax.net.ssl.SSLSession
        public Principal getPeerPrincipal() throws SSLPeerUnverifiedException {
            Certificate[] peer = getPeerCertificates();
            return ((java.security.cert.X509Certificate) peer[0]).getSubjectX500Principal();
        }

        @Override // javax.net.ssl.SSLSession
        public Principal getLocalPrincipal() {
            Certificate[] local = this.localCertificateChain;
            if (local == null || local.length == 0) {
                return null;
            }
            return ((java.security.cert.X509Certificate) local[0]).getSubjectX500Principal();
        }

        @Override // javax.net.ssl.SSLSession
        public String getCipherSuite() {
            synchronized (ReferenceCountedOpenSslEngine.this) {
                if (this.cipher == null) {
                    return "SSL_NULL_WITH_NULL_NULL";
                }
                return this.cipher;
            }
        }

        @Override // javax.net.ssl.SSLSession
        public String getProtocol() {
            String protocol = this.protocol;
            if (protocol == null) {
                synchronized (ReferenceCountedOpenSslEngine.this) {
                    if (!ReferenceCountedOpenSslEngine.this.isDestroyed()) {
                        protocol = SSL.getVersion(ReferenceCountedOpenSslEngine.this.ssl);
                    } else {
                        protocol = "";
                    }
                }
            }
            return protocol;
        }

        @Override // javax.net.ssl.SSLSession
        public String getPeerHost() {
            return ReferenceCountedOpenSslEngine.this.getPeerHost();
        }

        @Override // javax.net.ssl.SSLSession
        public int getPeerPort() {
            return ReferenceCountedOpenSslEngine.this.getPeerPort();
        }

        @Override // javax.net.ssl.SSLSession
        public int getPacketBufferSize() {
            return SSL.SSL_MAX_ENCRYPTED_LENGTH;
        }

        @Override // javax.net.ssl.SSLSession
        public int getApplicationBufferSize() {
            return this.applicationBufferSize;
        }

        @Override // io.netty.handler.ssl.OpenSslSession
        public void tryExpandApplicationBufferSize(int packetLengthDataOnly) {
            if (packetLengthDataOnly > ReferenceCountedOpenSslEngine.MAX_PLAINTEXT_LENGTH && this.applicationBufferSize != ReferenceCountedOpenSslEngine.MAX_RECORD_SIZE) {
                this.applicationBufferSize = ReferenceCountedOpenSslEngine.MAX_RECORD_SIZE;
            }
        }

        public String toString() {
            return "DefaultOpenSslSession{sessionContext=" + this.sessionContext + ", id=" + this.id + '}';
        }

        public int hashCode() {
            return sessionId().hashCode();
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof OpenSslSession)) {
                return false;
            }
            return sessionId().equals(((OpenSslSession) o).sessionId());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class OpenSslException extends SSLException implements NativeSslException {
        private final int errorCode;

        OpenSslException(String reason, int errorCode) {
            super(reason);
            this.errorCode = errorCode;
        }

        @Override // io.netty.handler.ssl.ReferenceCountedOpenSslEngine.NativeSslException
        public int errorCode() {
            return this.errorCode;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class OpenSslHandshakeException extends SSLHandshakeException implements NativeSslException {
        private final int errorCode;

        OpenSslHandshakeException(String reason, int errorCode) {
            super(reason);
            this.errorCode = errorCode;
        }

        @Override // io.netty.handler.ssl.ReferenceCountedOpenSslEngine.NativeSslException
        public int errorCode() {
            return this.errorCode;
        }
    }
}
