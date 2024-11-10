package org.jose4j.keys;

import javax.crypto.spec.SecretKeySpec;
import org.jose4j.lang.ByteUtil;

/* loaded from: classes5.dex */
public class AesKey extends SecretKeySpec {
    public static final String ALGORITHM = "AES";

    public AesKey(byte[] bytes) {
        super(bytes, ALGORITHM);
    }

    public String toString() {
        return ByteUtil.bitLength(getEncoded().length) + " bit " + ALGORITHM + " key";
    }
}
