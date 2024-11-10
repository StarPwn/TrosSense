package io.netty.handler.ssl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.X509ExtendedKeyManager;
import javax.net.ssl.X509KeyManager;
import javax.security.auth.x500.X500Principal;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public final class OpenSslKeyMaterialManager {
    private static final Map<String, String> KEY_TYPES = new HashMap();
    static final String KEY_TYPE_DH_RSA = "DH_RSA";
    static final String KEY_TYPE_EC = "EC";
    static final String KEY_TYPE_EC_EC = "EC_EC";
    static final String KEY_TYPE_EC_RSA = "EC_RSA";
    static final String KEY_TYPE_RSA = "RSA";
    private final OpenSslKeyMaterialProvider provider;

    static {
        KEY_TYPES.put("RSA", "RSA");
        KEY_TYPES.put("DHE_RSA", "RSA");
        KEY_TYPES.put("ECDHE_RSA", "RSA");
        KEY_TYPES.put("ECDHE_ECDSA", "EC");
        KEY_TYPES.put("ECDH_RSA", KEY_TYPE_EC_RSA);
        KEY_TYPES.put("ECDH_ECDSA", KEY_TYPE_EC_EC);
        KEY_TYPES.put(KEY_TYPE_DH_RSA, KEY_TYPE_DH_RSA);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public OpenSslKeyMaterialManager(OpenSslKeyMaterialProvider provider) {
        this.provider = provider;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setKeyMaterialServerSide(ReferenceCountedOpenSslEngine engine) throws SSLException {
        String alias;
        String[] authMethods = engine.authMethods();
        if (authMethods.length == 0) {
            throw new SSLHandshakeException("Unable to find key material");
        }
        Set<String> typeSet = new HashSet<>(KEY_TYPES.size());
        for (String authMethod : authMethods) {
            String type = KEY_TYPES.get(authMethod);
            if (type != null && typeSet.add(type) && (alias = chooseServerAlias(engine, type)) != null) {
                setKeyMaterial(engine, alias);
                return;
            }
        }
        throw new SSLHandshakeException("Unable to find key material for auth method(s): " + Arrays.toString(authMethods));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setKeyMaterialClientSide(ReferenceCountedOpenSslEngine engine, String[] keyTypes, X500Principal[] issuer) throws SSLException {
        String alias = chooseClientAlias(engine, keyTypes, issuer);
        if (alias != null) {
            setKeyMaterial(engine, alias);
        }
    }

    private void setKeyMaterial(ReferenceCountedOpenSslEngine engine, String alias) throws SSLException {
        OpenSslKeyMaterial keyMaterial = null;
        try {
            try {
                keyMaterial = this.provider.chooseKeyMaterial(engine.alloc, alias);
                if (keyMaterial == null) {
                    if (keyMaterial != null) {
                        return;
                    } else {
                        return;
                    }
                }
                engine.setKeyMaterial(keyMaterial);
                if (keyMaterial != null) {
                    keyMaterial.release();
                }
            } catch (SSLException e) {
                throw e;
            } catch (Exception e2) {
                throw new SSLException(e2);
            }
        } finally {
            if (keyMaterial != null) {
                keyMaterial.release();
            }
        }
    }

    private String chooseClientAlias(ReferenceCountedOpenSslEngine engine, String[] keyTypes, X500Principal[] issuer) {
        X509KeyManager manager = this.provider.keyManager();
        if (manager instanceof X509ExtendedKeyManager) {
            return ((X509ExtendedKeyManager) manager).chooseEngineClientAlias(keyTypes, issuer, engine);
        }
        return manager.chooseClientAlias(keyTypes, issuer, null);
    }

    private String chooseServerAlias(ReferenceCountedOpenSslEngine engine, String type) {
        X509KeyManager manager = this.provider.keyManager();
        if (manager instanceof X509ExtendedKeyManager) {
            return ((X509ExtendedKeyManager) manager).chooseEngineServerAlias(type, null, engine);
        }
        return manager.chooseServerAlias(type, null, null);
    }
}
