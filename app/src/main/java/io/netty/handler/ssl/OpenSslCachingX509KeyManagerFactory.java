package io.netty.handler.ssl;

import io.netty.util.internal.ObjectUtil;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.KeyManagerFactorySpi;
import javax.net.ssl.ManagerFactoryParameters;
import javax.net.ssl.X509KeyManager;

/* loaded from: classes4.dex */
public final class OpenSslCachingX509KeyManagerFactory extends KeyManagerFactory {
    private final int maxCachedEntries;

    public OpenSslCachingX509KeyManagerFactory(KeyManagerFactory factory) {
        this(factory, 1024);
    }

    public OpenSslCachingX509KeyManagerFactory(final KeyManagerFactory factory, int maxCachedEntries) {
        super(new KeyManagerFactorySpi() { // from class: io.netty.handler.ssl.OpenSslCachingX509KeyManagerFactory.1
            @Override // javax.net.ssl.KeyManagerFactorySpi
            protected void engineInit(KeyStore keyStore, char[] chars) throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException {
                factory.init(keyStore, chars);
            }

            @Override // javax.net.ssl.KeyManagerFactorySpi
            protected void engineInit(ManagerFactoryParameters managerFactoryParameters) throws InvalidAlgorithmParameterException {
                factory.init(managerFactoryParameters);
            }

            @Override // javax.net.ssl.KeyManagerFactorySpi
            protected KeyManager[] engineGetKeyManagers() {
                return factory.getKeyManagers();
            }
        }, factory.getProvider(), factory.getAlgorithm());
        this.maxCachedEntries = ObjectUtil.checkPositive(maxCachedEntries, "maxCachedEntries");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public OpenSslKeyMaterialProvider newProvider(String password) {
        X509KeyManager keyManager = ReferenceCountedOpenSslContext.chooseX509KeyManager(getKeyManagers());
        if ("sun.security.ssl.X509KeyManagerImpl".equals(keyManager.getClass().getName())) {
            return new OpenSslKeyMaterialProvider(keyManager, password);
        }
        return new OpenSslCachingKeyMaterialProvider(ReferenceCountedOpenSslContext.chooseX509KeyManager(getKeyManagers()), password, this.maxCachedEntries);
    }
}
