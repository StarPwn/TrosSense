package io.netty.handler.ssl;

import io.netty.handler.ssl.util.KeyManagerFactoryWrapper;
import io.netty.handler.ssl.util.TrustManagerFactoryWrapper;
import io.netty.util.internal.EmptyArrays;
import io.netty.util.internal.ObjectUtil;
import java.io.File;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLException;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

/* loaded from: classes4.dex */
public final class SslContextBuilder {
    private static final Map.Entry[] EMPTY_ENTRIES = new Map.Entry[0];
    private ApplicationProtocolConfig apn;
    private Iterable<String> ciphers;
    private boolean enableOcsp;
    private final boolean forServer;
    private PrivateKey key;
    private X509Certificate[] keyCertChain;
    private KeyManagerFactory keyManagerFactory;
    private String keyPassword;
    private String[] protocols;
    private SslProvider provider;
    private long sessionCacheSize;
    private long sessionTimeout;
    private Provider sslContextProvider;
    private boolean startTls;
    private X509Certificate[] trustCertCollection;
    private TrustManagerFactory trustManagerFactory;
    private CipherSuiteFilter cipherFilter = IdentityCipherSuiteFilter.INSTANCE;
    private ClientAuth clientAuth = ClientAuth.NONE;
    private String keyStoreType = KeyStore.getDefaultType();
    private final Map<SslContextOption<?>, Object> options = new HashMap();

    public static SslContextBuilder forClient() {
        return new SslContextBuilder(false);
    }

    public static SslContextBuilder forServer(File keyCertChainFile, File keyFile) {
        return new SslContextBuilder(true).keyManager(keyCertChainFile, keyFile);
    }

    public static SslContextBuilder forServer(InputStream keyCertChainInputStream, InputStream keyInputStream) {
        return new SslContextBuilder(true).keyManager(keyCertChainInputStream, keyInputStream);
    }

    public static SslContextBuilder forServer(PrivateKey key, X509Certificate... keyCertChain) {
        return new SslContextBuilder(true).keyManager(key, keyCertChain);
    }

    public static SslContextBuilder forServer(PrivateKey key, Iterable<? extends X509Certificate> keyCertChain) {
        return forServer(key, (X509Certificate[]) toArray(keyCertChain, EmptyArrays.EMPTY_X509_CERTIFICATES));
    }

    public static SslContextBuilder forServer(File keyCertChainFile, File keyFile, String keyPassword) {
        return new SslContextBuilder(true).keyManager(keyCertChainFile, keyFile, keyPassword);
    }

    public static SslContextBuilder forServer(InputStream keyCertChainInputStream, InputStream keyInputStream, String keyPassword) {
        return new SslContextBuilder(true).keyManager(keyCertChainInputStream, keyInputStream, keyPassword);
    }

    public static SslContextBuilder forServer(PrivateKey key, String keyPassword, X509Certificate... keyCertChain) {
        return new SslContextBuilder(true).keyManager(key, keyPassword, keyCertChain);
    }

    public static SslContextBuilder forServer(PrivateKey key, String keyPassword, Iterable<? extends X509Certificate> keyCertChain) {
        return forServer(key, keyPassword, (X509Certificate[]) toArray(keyCertChain, EmptyArrays.EMPTY_X509_CERTIFICATES));
    }

    public static SslContextBuilder forServer(KeyManagerFactory keyManagerFactory) {
        return new SslContextBuilder(true).keyManager(keyManagerFactory);
    }

    public static SslContextBuilder forServer(KeyManager keyManager) {
        return new SslContextBuilder(true).keyManager(keyManager);
    }

    private SslContextBuilder(boolean forServer) {
        this.forServer = forServer;
    }

    public <T> SslContextBuilder option(SslContextOption<T> option, T value) {
        if (value == null) {
            this.options.remove(option);
        } else {
            this.options.put(option, value);
        }
        return this;
    }

    public SslContextBuilder sslProvider(SslProvider provider) {
        this.provider = provider;
        return this;
    }

    public SslContextBuilder keyStoreType(String keyStoreType) {
        this.keyStoreType = keyStoreType;
        return this;
    }

    public SslContextBuilder sslContextProvider(Provider sslContextProvider) {
        this.sslContextProvider = sslContextProvider;
        return this;
    }

    public SslContextBuilder trustManager(File trustCertCollectionFile) {
        try {
            return trustManager(SslContext.toX509Certificates(trustCertCollectionFile));
        } catch (Exception e) {
            throw new IllegalArgumentException("File does not contain valid certificates: " + trustCertCollectionFile, e);
        }
    }

    public SslContextBuilder trustManager(InputStream trustCertCollectionInputStream) {
        try {
            return trustManager(SslContext.toX509Certificates(trustCertCollectionInputStream));
        } catch (Exception e) {
            throw new IllegalArgumentException("Input stream does not contain valid certificates.", e);
        }
    }

    public SslContextBuilder trustManager(X509Certificate... trustCertCollection) {
        this.trustCertCollection = trustCertCollection != null ? (X509Certificate[]) trustCertCollection.clone() : null;
        this.trustManagerFactory = null;
        return this;
    }

    public SslContextBuilder trustManager(Iterable<? extends X509Certificate> trustCertCollection) {
        return trustManager((X509Certificate[]) toArray(trustCertCollection, EmptyArrays.EMPTY_X509_CERTIFICATES));
    }

    public SslContextBuilder trustManager(TrustManagerFactory trustManagerFactory) {
        this.trustCertCollection = null;
        this.trustManagerFactory = trustManagerFactory;
        return this;
    }

    public SslContextBuilder trustManager(TrustManager trustManager) {
        if (trustManager != null) {
            this.trustManagerFactory = new TrustManagerFactoryWrapper(trustManager);
        } else {
            this.trustManagerFactory = null;
        }
        this.trustCertCollection = null;
        return this;
    }

    public SslContextBuilder keyManager(File keyCertChainFile, File keyFile) {
        return keyManager(keyCertChainFile, keyFile, (String) null);
    }

    public SslContextBuilder keyManager(InputStream keyCertChainInputStream, InputStream keyInputStream) {
        return keyManager(keyCertChainInputStream, keyInputStream, (String) null);
    }

    public SslContextBuilder keyManager(PrivateKey key, X509Certificate... keyCertChain) {
        return keyManager(key, (String) null, keyCertChain);
    }

    public SslContextBuilder keyManager(PrivateKey key, Iterable<? extends X509Certificate> keyCertChain) {
        return keyManager(key, (X509Certificate[]) toArray(keyCertChain, EmptyArrays.EMPTY_X509_CERTIFICATES));
    }

    public SslContextBuilder keyManager(File keyCertChainFile, File keyFile, String keyPassword) {
        try {
            X509Certificate[] keyCertChain = SslContext.toX509Certificates(keyCertChainFile);
            try {
                PrivateKey key = SslContext.toPrivateKey(keyFile, keyPassword);
                return keyManager(key, keyPassword, keyCertChain);
            } catch (Exception e) {
                throw new IllegalArgumentException("File does not contain valid private key: " + keyFile, e);
            }
        } catch (Exception e2) {
            throw new IllegalArgumentException("File does not contain valid certificates: " + keyCertChainFile, e2);
        }
    }

    public SslContextBuilder keyManager(InputStream keyCertChainInputStream, InputStream keyInputStream, String keyPassword) {
        try {
            X509Certificate[] keyCertChain = SslContext.toX509Certificates(keyCertChainInputStream);
            try {
                PrivateKey key = SslContext.toPrivateKey(keyInputStream, keyPassword);
                return keyManager(key, keyPassword, keyCertChain);
            } catch (Exception e) {
                throw new IllegalArgumentException("Input stream does not contain valid private key.", e);
            }
        } catch (Exception e2) {
            throw new IllegalArgumentException("Input stream not contain valid certificates.", e2);
        }
    }

    public SslContextBuilder keyManager(PrivateKey key, String keyPassword, X509Certificate... keyCertChain) {
        if (this.forServer) {
            ObjectUtil.checkNonEmpty(keyCertChain, "keyCertChain");
            ObjectUtil.checkNotNull(key, "key required for servers");
        }
        if (keyCertChain == null || keyCertChain.length == 0) {
            this.keyCertChain = null;
        } else {
            for (X509Certificate cert : keyCertChain) {
                ObjectUtil.checkNotNullWithIAE(cert, "cert");
            }
            this.keyCertChain = (X509Certificate[]) keyCertChain.clone();
        }
        this.key = key;
        this.keyPassword = keyPassword;
        this.keyManagerFactory = null;
        return this;
    }

    public SslContextBuilder keyManager(PrivateKey key, String keyPassword, Iterable<? extends X509Certificate> keyCertChain) {
        return keyManager(key, keyPassword, (X509Certificate[]) toArray(keyCertChain, EmptyArrays.EMPTY_X509_CERTIFICATES));
    }

    public SslContextBuilder keyManager(KeyManagerFactory keyManagerFactory) {
        if (this.forServer) {
            ObjectUtil.checkNotNull(keyManagerFactory, "keyManagerFactory required for servers");
        }
        this.keyCertChain = null;
        this.key = null;
        this.keyPassword = null;
        this.keyManagerFactory = keyManagerFactory;
        return this;
    }

    public SslContextBuilder keyManager(KeyManager keyManager) {
        if (this.forServer) {
            ObjectUtil.checkNotNull(keyManager, "keyManager required for servers");
        }
        if (keyManager != null) {
            this.keyManagerFactory = new KeyManagerFactoryWrapper(keyManager);
        } else {
            this.keyManagerFactory = null;
        }
        this.keyCertChain = null;
        this.key = null;
        this.keyPassword = null;
        return this;
    }

    public SslContextBuilder ciphers(Iterable<String> ciphers) {
        return ciphers(ciphers, IdentityCipherSuiteFilter.INSTANCE);
    }

    public SslContextBuilder ciphers(Iterable<String> ciphers, CipherSuiteFilter cipherFilter) {
        this.cipherFilter = (CipherSuiteFilter) ObjectUtil.checkNotNull(cipherFilter, "cipherFilter");
        this.ciphers = ciphers;
        return this;
    }

    public SslContextBuilder applicationProtocolConfig(ApplicationProtocolConfig apn) {
        this.apn = apn;
        return this;
    }

    public SslContextBuilder sessionCacheSize(long sessionCacheSize) {
        this.sessionCacheSize = sessionCacheSize;
        return this;
    }

    public SslContextBuilder sessionTimeout(long sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
        return this;
    }

    public SslContextBuilder clientAuth(ClientAuth clientAuth) {
        this.clientAuth = (ClientAuth) ObjectUtil.checkNotNull(clientAuth, "clientAuth");
        return this;
    }

    public SslContextBuilder protocols(String... protocols) {
        this.protocols = protocols == null ? null : (String[]) protocols.clone();
        return this;
    }

    public SslContextBuilder protocols(Iterable<String> protocols) {
        return protocols((String[]) toArray(protocols, EmptyArrays.EMPTY_STRINGS));
    }

    public SslContextBuilder startTls(boolean startTls) {
        this.startTls = startTls;
        return this;
    }

    public SslContextBuilder enableOcsp(boolean enableOcsp) {
        this.enableOcsp = enableOcsp;
        return this;
    }

    public SslContext build() throws SSLException {
        if (this.forServer) {
            return SslContext.newServerContextInternal(this.provider, this.sslContextProvider, this.trustCertCollection, this.trustManagerFactory, this.keyCertChain, this.key, this.keyPassword, this.keyManagerFactory, this.ciphers, this.cipherFilter, this.apn, this.sessionCacheSize, this.sessionTimeout, this.clientAuth, this.protocols, this.startTls, this.enableOcsp, this.keyStoreType, (Map.Entry[]) toArray(this.options.entrySet(), EMPTY_ENTRIES));
        }
        return SslContext.newClientContextInternal(this.provider, this.sslContextProvider, this.trustCertCollection, this.trustManagerFactory, this.keyCertChain, this.key, this.keyPassword, this.keyManagerFactory, this.ciphers, this.cipherFilter, this.apn, this.protocols, this.sessionCacheSize, this.sessionTimeout, this.enableOcsp, this.keyStoreType, (Map.Entry[]) toArray(this.options.entrySet(), EMPTY_ENTRIES));
    }

    private static <T> T[] toArray(Iterable<? extends T> iterable, T[] tArr) {
        if (iterable == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        Iterator<? extends T> it2 = iterable.iterator();
        while (it2.hasNext()) {
            arrayList.add(it2.next());
        }
        return (T[]) arrayList.toArray(tArr);
    }
}
