package org.jose4j.keys;

import javax.crypto.spec.SecretKeySpec;

/* loaded from: classes5.dex */
public class HmacKey extends SecretKeySpec {
    public static final String ALGORITHM = "HMAC";

    public HmacKey(byte[] bytes) {
        super(bytes, ALGORITHM);
    }
}
