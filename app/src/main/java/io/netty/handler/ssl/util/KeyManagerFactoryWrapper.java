package io.netty.handler.ssl.util;

import io.netty.util.internal.ObjectUtil;
import java.security.KeyStore;
import javax.net.ssl.KeyManager;
import javax.net.ssl.ManagerFactoryParameters;

/* loaded from: classes4.dex */
public final class KeyManagerFactoryWrapper extends SimpleKeyManagerFactory {
    private final KeyManager km;

    public KeyManagerFactoryWrapper(KeyManager km) {
        this.km = (KeyManager) ObjectUtil.checkNotNull(km, "km");
    }

    @Override // io.netty.handler.ssl.util.SimpleKeyManagerFactory
    protected void engineInit(KeyStore keyStore, char[] var2) throws Exception {
    }

    @Override // io.netty.handler.ssl.util.SimpleKeyManagerFactory
    protected void engineInit(ManagerFactoryParameters managerFactoryParameters) throws Exception {
    }

    @Override // io.netty.handler.ssl.util.SimpleKeyManagerFactory
    protected KeyManager[] engineGetKeyManagers() {
        return new KeyManager[]{this.km};
    }
}
