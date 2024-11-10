package org.jose4j.jwe;

/* loaded from: classes5.dex */
public class ContentEncryptionParts {
    private byte[] authenticationTag;
    private byte[] ciphertext;
    private byte[] iv;

    public ContentEncryptionParts(byte[] iv, byte[] ciphertext, byte[] authenticationTag) {
        this.iv = iv;
        this.ciphertext = ciphertext;
        this.authenticationTag = authenticationTag;
    }

    public byte[] getIv() {
        return this.iv;
    }

    public byte[] getCiphertext() {
        return this.ciphertext;
    }

    public byte[] getAuthenticationTag() {
        return this.authenticationTag;
    }
}
