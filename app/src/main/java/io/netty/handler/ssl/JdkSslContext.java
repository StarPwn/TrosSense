package io.netty.handler.ssl;

import io.netty.buffer.ByteBufAllocator;
import io.netty.handler.ssl.JdkApplicationProtocolNegotiator;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.internal.EmptyArrays;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.crypto.NoSuchPaddingException;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSessionContext;

/* loaded from: classes4.dex */
public class JdkSslContext extends SslContext {
    private static final List<String> DEFAULT_CIPHERS;
    private static final List<String> DEFAULT_CIPHERS_NON_TLSV13;
    private static final String[] DEFAULT_PROTOCOLS;
    private static final Provider DEFAULT_PROVIDER;
    static final String PROTOCOL = "TLS";
    private static final Set<String> SUPPORTED_CIPHERS;
    private static final Set<String> SUPPORTED_CIPHERS_NON_TLSV13;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) JdkSslContext.class);
    private final JdkApplicationProtocolNegotiator apn;
    private final String[] cipherSuites;
    private final ClientAuth clientAuth;
    private final boolean isClient;
    private final String[] protocols;
    private final SSLContext sslContext;
    private final List<String> unmodifiableCipherSuites;

    static {
        Defaults defaults = new Defaults();
        defaults.init();
        DEFAULT_PROVIDER = defaults.defaultProvider;
        DEFAULT_PROTOCOLS = defaults.defaultProtocols;
        SUPPORTED_CIPHERS = defaults.supportedCiphers;
        DEFAULT_CIPHERS = defaults.defaultCiphers;
        DEFAULT_CIPHERS_NON_TLSV13 = defaults.defaultCiphersNonTLSv13;
        SUPPORTED_CIPHERS_NON_TLSV13 = defaults.supportedCiphersNonTLSv13;
        if (logger.isDebugEnabled()) {
            logger.debug("Default protocols (JDK): {} ", Arrays.asList(DEFAULT_PROTOCOLS));
            logger.debug("Default cipher suites (JDK): {}", DEFAULT_CIPHERS);
        }
    }

    /* loaded from: classes4.dex */
    private static final class Defaults {
        List<String> defaultCiphers;
        List<String> defaultCiphersNonTLSv13;
        String[] defaultProtocols;
        Provider defaultProvider;
        Set<String> supportedCiphers;
        Set<String> supportedCiphersNonTLSv13;

        private Defaults() {
        }

        void init() {
            try {
                SSLContext context = SSLContext.getInstance(JdkSslContext.PROTOCOL);
                context.init(null, null, null);
                this.defaultProvider = context.getProvider();
                SSLEngine engine = context.createSSLEngine();
                this.defaultProtocols = JdkSslContext.defaultProtocols(context, engine);
                this.supportedCiphers = Collections.unmodifiableSet(JdkSslContext.supportedCiphers(engine));
                this.defaultCiphers = Collections.unmodifiableList(JdkSslContext.defaultCiphers(engine, this.supportedCiphers));
                List<String> ciphersNonTLSv13 = new ArrayList<>(this.defaultCiphers);
                ciphersNonTLSv13.removeAll(Arrays.asList(SslUtils.DEFAULT_TLSV13_CIPHER_SUITES));
                this.defaultCiphersNonTLSv13 = Collections.unmodifiableList(ciphersNonTLSv13);
                Set<String> suppertedCiphersNonTLSv13 = new LinkedHashSet<>(this.supportedCiphers);
                suppertedCiphersNonTLSv13.removeAll(Arrays.asList(SslUtils.DEFAULT_TLSV13_CIPHER_SUITES));
                this.supportedCiphersNonTLSv13 = Collections.unmodifiableSet(suppertedCiphersNonTLSv13);
            } catch (Exception e) {
                throw new Error("failed to initialize the default SSL context", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String[] defaultProtocols(SSLContext context, SSLEngine engine) {
        String[] supportedProtocols = context.getDefaultSSLParameters().getProtocols();
        Set<String> supportedProtocolsSet = new HashSet<>(supportedProtocols.length);
        Collections.addAll(supportedProtocolsSet, supportedProtocols);
        List<String> protocols = new ArrayList<>();
        SslUtils.addIfSupported(supportedProtocolsSet, protocols, SslProtocols.TLS_v1_3, SslProtocols.TLS_v1_2, SslProtocols.TLS_v1_1, SslProtocols.TLS_v1);
        if (!protocols.isEmpty()) {
            return (String[]) protocols.toArray(EmptyArrays.EMPTY_STRINGS);
        }
        return engine.getEnabledProtocols();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Set<String> supportedCiphers(SSLEngine engine) {
        String[] supportedCiphers = engine.getSupportedCipherSuites();
        Set<String> supportedCiphersSet = new LinkedHashSet<>(supportedCiphers.length);
        for (String supportedCipher : supportedCiphers) {
            supportedCiphersSet.add(supportedCipher);
            if (supportedCipher.startsWith("SSL_")) {
                String tlsPrefixedCipherName = "TLS_" + supportedCipher.substring("SSL_".length());
                try {
                    engine.setEnabledCipherSuites(new String[]{tlsPrefixedCipherName});
                    supportedCiphersSet.add(tlsPrefixedCipherName);
                } catch (IllegalArgumentException e) {
                }
            }
        }
        return supportedCiphersSet;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static List<String> defaultCiphers(SSLEngine engine, Set<String> supportedCiphers) {
        List<String> ciphers = new ArrayList<>();
        SslUtils.addIfSupported(supportedCiphers, ciphers, SslUtils.DEFAULT_CIPHER_SUITES);
        SslUtils.useFallbackCiphersIfDefaultIsEmpty(ciphers, engine.getEnabledCipherSuites());
        return ciphers;
    }

    private static boolean isTlsV13Supported(String[] protocols) {
        for (String protocol : protocols) {
            if (SslProtocols.TLS_v1_3.equals(protocol)) {
                return true;
            }
        }
        return false;
    }

    @Deprecated
    public JdkSslContext(SSLContext sslContext, boolean isClient, ClientAuth clientAuth) {
        this(sslContext, isClient, (Iterable<String>) null, (CipherSuiteFilter) IdentityCipherSuiteFilter.INSTANCE, (JdkApplicationProtocolNegotiator) JdkDefaultApplicationProtocolNegotiator.INSTANCE, clientAuth, (String[]) null, false);
    }

    @Deprecated
    public JdkSslContext(SSLContext sslContext, boolean isClient, Iterable<String> ciphers, CipherSuiteFilter cipherFilter, ApplicationProtocolConfig apn, ClientAuth clientAuth) {
        this(sslContext, isClient, ciphers, cipherFilter, apn, clientAuth, (String[]) null, false);
    }

    public JdkSslContext(SSLContext sslContext, boolean isClient, Iterable<String> ciphers, CipherSuiteFilter cipherFilter, ApplicationProtocolConfig apn, ClientAuth clientAuth, String[] protocols, boolean startTls) {
        this(sslContext, isClient, ciphers, cipherFilter, toNegotiator(apn, !isClient), clientAuth, protocols == null ? null : (String[]) protocols.clone(), startTls);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public JdkSslContext(SSLContext sslContext, boolean isClient, Iterable<String> ciphers, CipherSuiteFilter cipherFilter, JdkApplicationProtocolNegotiator apn, ClientAuth clientAuth, String[] protocols, boolean startTls) {
        super(startTls);
        Set<String> supportedCiphers;
        List<String> defaultCiphers;
        this.apn = (JdkApplicationProtocolNegotiator) ObjectUtil.checkNotNull(apn, "apn");
        this.clientAuth = (ClientAuth) ObjectUtil.checkNotNull(clientAuth, "clientAuth");
        this.sslContext = (SSLContext) ObjectUtil.checkNotNull(sslContext, "sslContext");
        if (DEFAULT_PROVIDER.equals(sslContext.getProvider())) {
            this.protocols = protocols == null ? DEFAULT_PROTOCOLS : protocols;
            if (isTlsV13Supported(this.protocols)) {
                supportedCiphers = SUPPORTED_CIPHERS;
                defaultCiphers = DEFAULT_CIPHERS;
            } else {
                supportedCiphers = SUPPORTED_CIPHERS_NON_TLSV13;
                defaultCiphers = DEFAULT_CIPHERS_NON_TLSV13;
            }
        } else {
            SSLEngine engine = sslContext.createSSLEngine();
            try {
                if (protocols == null) {
                    this.protocols = defaultProtocols(sslContext, engine);
                } else {
                    this.protocols = protocols;
                }
                Set<String> supportedCiphers2 = supportedCiphers(engine);
                List<String> defaultCiphers2 = defaultCiphers(engine, supportedCiphers2);
                if (!isTlsV13Supported(this.protocols)) {
                    for (String cipher : SslUtils.DEFAULT_TLSV13_CIPHER_SUITES) {
                        supportedCiphers2.remove(cipher);
                        defaultCiphers2.remove(cipher);
                    }
                }
                ReferenceCountUtil.release(engine);
                supportedCiphers = supportedCiphers2;
                defaultCiphers = defaultCiphers2;
            } catch (Throwable th) {
                ReferenceCountUtil.release(engine);
                throw th;
            }
        }
        this.cipherSuites = ((CipherSuiteFilter) ObjectUtil.checkNotNull(cipherFilter, "cipherFilter")).filterCipherSuites(ciphers, defaultCiphers, supportedCiphers);
        this.unmodifiableCipherSuites = Collections.unmodifiableList(Arrays.asList(this.cipherSuites));
        this.isClient = isClient;
    }

    public final SSLContext context() {
        return this.sslContext;
    }

    @Override // io.netty.handler.ssl.SslContext
    public final boolean isClient() {
        return this.isClient;
    }

    @Override // io.netty.handler.ssl.SslContext
    public final SSLSessionContext sessionContext() {
        if (isServer()) {
            return context().getServerSessionContext();
        }
        return context().getClientSessionContext();
    }

    @Override // io.netty.handler.ssl.SslContext
    public final List<String> cipherSuites() {
        return this.unmodifiableCipherSuites;
    }

    @Override // io.netty.handler.ssl.SslContext
    public final SSLEngine newEngine(ByteBufAllocator alloc) {
        return configureAndWrapEngine(context().createSSLEngine(), alloc);
    }

    @Override // io.netty.handler.ssl.SslContext
    public final SSLEngine newEngine(ByteBufAllocator alloc, String peerHost, int peerPort) {
        return configureAndWrapEngine(context().createSSLEngine(peerHost, peerPort), alloc);
    }

    private SSLEngine configureAndWrapEngine(SSLEngine engine, ByteBufAllocator alloc) {
        engine.setEnabledCipherSuites(this.cipherSuites);
        engine.setEnabledProtocols(this.protocols);
        engine.setUseClientMode(isClient());
        if (isServer()) {
            switch (this.clientAuth) {
                case OPTIONAL:
                    engine.setWantClientAuth(true);
                    break;
                case REQUIRE:
                    engine.setNeedClientAuth(true);
                    break;
                case NONE:
                    break;
                default:
                    throw new Error("Unknown auth " + this.clientAuth);
            }
        }
        JdkApplicationProtocolNegotiator.SslEngineWrapperFactory factory = this.apn.wrapperFactory();
        if (factory instanceof JdkApplicationProtocolNegotiator.AllocatorAwareSslEngineWrapperFactory) {
            return ((JdkApplicationProtocolNegotiator.AllocatorAwareSslEngineWrapperFactory) factory).wrapSslEngine(engine, alloc, this.apn, isServer());
        }
        return factory.wrapSslEngine(engine, this.apn, isServer());
    }

    @Override // io.netty.handler.ssl.SslContext
    public final JdkApplicationProtocolNegotiator applicationProtocolNegotiator() {
        return this.apn;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static JdkApplicationProtocolNegotiator toNegotiator(ApplicationProtocolConfig config, boolean isServer) {
        if (config == null) {
            return JdkDefaultApplicationProtocolNegotiator.INSTANCE;
        }
        switch (config.protocol()) {
            case NONE:
                return JdkDefaultApplicationProtocolNegotiator.INSTANCE;
            case ALPN:
                if (isServer) {
                    switch (config.selectorFailureBehavior()) {
                        case FATAL_ALERT:
                            return new JdkAlpnApplicationProtocolNegotiator(true, (Iterable<String>) config.supportedProtocols());
                        case NO_ADVERTISE:
                            return new JdkAlpnApplicationProtocolNegotiator(false, (Iterable<String>) config.supportedProtocols());
                        default:
                            throw new UnsupportedOperationException("JDK provider does not support " + config.selectorFailureBehavior() + " failure behavior");
                    }
                }
                switch (config.selectedListenerFailureBehavior()) {
                    case ACCEPT:
                        return new JdkAlpnApplicationProtocolNegotiator(false, (Iterable<String>) config.supportedProtocols());
                    case FATAL_ALERT:
                        return new JdkAlpnApplicationProtocolNegotiator(true, (Iterable<String>) config.supportedProtocols());
                    default:
                        throw new UnsupportedOperationException("JDK provider does not support " + config.selectedListenerFailureBehavior() + " failure behavior");
                }
            case NPN:
                if (isServer) {
                    switch (config.selectedListenerFailureBehavior()) {
                        case ACCEPT:
                            return new JdkNpnApplicationProtocolNegotiator(false, (Iterable<String>) config.supportedProtocols());
                        case FATAL_ALERT:
                            return new JdkNpnApplicationProtocolNegotiator(true, (Iterable<String>) config.supportedProtocols());
                        default:
                            throw new UnsupportedOperationException("JDK provider does not support " + config.selectedListenerFailureBehavior() + " failure behavior");
                    }
                }
                switch (config.selectorFailureBehavior()) {
                    case FATAL_ALERT:
                        return new JdkNpnApplicationProtocolNegotiator(true, (Iterable<String>) config.supportedProtocols());
                    case NO_ADVERTISE:
                        return new JdkNpnApplicationProtocolNegotiator(false, (Iterable<String>) config.supportedProtocols());
                    default:
                        throw new UnsupportedOperationException("JDK provider does not support " + config.selectorFailureBehavior() + " failure behavior");
                }
            default:
                throw new UnsupportedOperationException("JDK provider does not support " + config.protocol() + " protocol");
        }
    }

    static KeyManagerFactory buildKeyManagerFactory(File certChainFile, File keyFile, String keyPassword, KeyManagerFactory kmf, String keyStore) throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidAlgorithmParameterException, CertificateException, KeyException, IOException {
        String algorithm = Security.getProperty("ssl.KeyManagerFactory.algorithm");
        if (algorithm == null) {
            algorithm = "SunX509";
        }
        return buildKeyManagerFactory(certChainFile, algorithm, keyFile, keyPassword, kmf, keyStore);
    }

    @Deprecated
    protected static KeyManagerFactory buildKeyManagerFactory(File certChainFile, File keyFile, String keyPassword, KeyManagerFactory kmf) throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidAlgorithmParameterException, CertificateException, KeyException, IOException {
        return buildKeyManagerFactory(certChainFile, keyFile, keyPassword, kmf, KeyStore.getDefaultType());
    }

    static KeyManagerFactory buildKeyManagerFactory(File certChainFile, String keyAlgorithm, File keyFile, String keyPassword, KeyManagerFactory kmf, String keyStore) throws KeyStoreException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidAlgorithmParameterException, IOException, CertificateException, KeyException, UnrecoverableKeyException {
        return buildKeyManagerFactory(toX509Certificates(certChainFile), keyAlgorithm, toPrivateKey(keyFile, keyPassword), keyPassword, kmf, keyStore);
    }

    @Deprecated
    protected static KeyManagerFactory buildKeyManagerFactory(File certChainFile, String keyAlgorithm, File keyFile, String keyPassword, KeyManagerFactory kmf) throws KeyStoreException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidAlgorithmParameterException, IOException, CertificateException, KeyException, UnrecoverableKeyException {
        return buildKeyManagerFactory(toX509Certificates(certChainFile), keyAlgorithm, toPrivateKey(keyFile, keyPassword), keyPassword, kmf, KeyStore.getDefaultType());
    }
}
