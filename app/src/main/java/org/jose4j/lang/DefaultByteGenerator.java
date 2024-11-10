package org.jose4j.lang;

import java.security.SecureRandom;

/* loaded from: classes5.dex */
public class DefaultByteGenerator implements ByteGenerator {
    private final SecureRandom random = new SecureRandom();

    @Override // org.jose4j.lang.ByteGenerator
    public byte[] randomBytes(int length) {
        byte[] bytes = new byte[length];
        this.random.nextBytes(bytes);
        return bytes;
    }
}
