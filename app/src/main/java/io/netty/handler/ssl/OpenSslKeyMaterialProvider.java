package io.netty.handler.ssl;

import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.internal.tcnative.SSL;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLException;
import javax.net.ssl.X509KeyManager;

/* loaded from: classes4.dex */
class OpenSslKeyMaterialProvider {
    private final X509KeyManager keyManager;
    private final String password;

    /* JADX INFO: Access modifiers changed from: package-private */
    public OpenSslKeyMaterialProvider(X509KeyManager keyManager, String password) {
        this.keyManager = keyManager;
        this.password = password;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void validateKeyMaterialSupported(X509Certificate[] keyCertChain, PrivateKey key, String keyPassword) throws SSLException {
        validateSupported(keyCertChain);
        validateSupported(key, keyPassword);
    }

    private static void validateSupported(PrivateKey key, String password) throws SSLException {
        if (key == null) {
            return;
        }
        long pkeyBio = 0;
        long pkey = 0;
        try {
            try {
                pkeyBio = ReferenceCountedOpenSslContext.toBIO(UnpooledByteBufAllocator.DEFAULT, key);
                pkey = SSL.parsePrivateKey(pkeyBio, password);
            } catch (Exception e) {
                throw new SSLException("PrivateKey type not supported " + key.getFormat(), e);
            }
        } finally {
            SSL.freeBIO(pkeyBio);
            if (pkey != 0) {
                SSL.freePrivateKey(pkey);
            }
        }
    }

    private static void validateSupported(X509Certificate[] certificates) throws SSLException {
        if (certificates == null || certificates.length == 0) {
            return;
        }
        long chainBio = 0;
        long chain = 0;
        PemEncoded encoded = null;
        try {
            try {
                encoded = PemX509Certificate.toPEM(UnpooledByteBufAllocator.DEFAULT, true, certificates);
                chainBio = ReferenceCountedOpenSslContext.toBIO(UnpooledByteBufAllocator.DEFAULT, encoded.retain());
                chain = SSL.parseX509Chain(chainBio);
            } catch (Exception e) {
                throw new SSLException("Certificate type not supported", e);
            }
        } finally {
            SSL.freeBIO(chainBio);
            if (chain != 0) {
                SSL.freeX509Chain(chain);
            }
            if (encoded != null) {
                encoded.release();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public X509KeyManager keyManager() {
        return this.keyManager;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:38:0x00ba  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00c1  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public io.netty.handler.ssl.OpenSslKeyMaterial chooseKeyMaterial(io.netty.buffer.ByteBufAllocator r24, java.lang.String r25) throws java.lang.Exception {
        /*
            Method dump skipped, instructions count: 202
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.handler.ssl.OpenSslKeyMaterialProvider.chooseKeyMaterial(io.netty.buffer.ByteBufAllocator, java.lang.String):io.netty.handler.ssl.OpenSslKeyMaterial");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void destroy() {
    }
}
