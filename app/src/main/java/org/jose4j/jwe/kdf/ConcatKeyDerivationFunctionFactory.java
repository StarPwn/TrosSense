package org.jose4j.jwe.kdf;

import org.jose4j.lang.HashUtil;
import org.msgpack.core.MessagePack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: classes5.dex */
class ConcatKeyDerivationFunctionFactory {
    private static Class<ConcatenationKeyDerivationFunctionWithSha256> customKdfClass;
    private static final Logger log = LoggerFactory.getLogger((Class<?>) ConcatKeyDerivationFunctionFactory.class);

    ConcatKeyDerivationFunctionFactory() {
    }

    static {
        String name = System.getProperty("org.jose4j.jwe.kdf.ConcatenationKeyDerivationFunctionWithSha256");
        if (name != null) {
            try {
                customKdfClass = Class.forName(name);
                ConcatenationKeyDerivationFunctionWithSha256 kdf = customKdfClass.newInstance();
                byte[] z = {124, -81, 43, 14, -71, -72, -84, 75, 115, 73, MessagePack.Code.UINT8, MessagePack.Code.STR8, 74, MessagePack.Code.BIN32, 77, -83};
                kdf.kdf(z, 512, new byte[8]);
                log.debug("Using custom ConcatenationKeyDerivationFunctionWithSha256 implementation: " + kdf.getClass());
            } catch (Throwable e) {
                customKdfClass = null;
                log.debug("Using jose4j's concatenation key derivation function implementation because of problems with " + name, e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ConcatenationKeyDerivationFunctionWithSha256 make(String provider) {
        if (customKdfClass != null) {
            try {
                return customKdfClass.newInstance();
            } catch (Exception e) {
                log.debug("Unable to create new instance of " + customKdfClass, (Throwable) e);
            }
        }
        return new ConcatKeyDerivationFunction(HashUtil.SHA_256, provider);
    }
}
