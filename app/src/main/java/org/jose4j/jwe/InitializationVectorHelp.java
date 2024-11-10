package org.jose4j.jwe;

import java.security.SecureRandom;
import org.jose4j.lang.ByteUtil;

/* loaded from: classes5.dex */
public class InitializationVectorHelp {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] iv(int byteLength, byte[] ivOverride, SecureRandom secureRandom) {
        return ivOverride == null ? ByteUtil.randomBytes(byteLength, secureRandom) : ivOverride;
    }
}
