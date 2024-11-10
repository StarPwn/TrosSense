package org.jose4j.jwe;

/* loaded from: classes5.dex */
public class ContentEncryptionKeyDescriptor {
    private final String contentEncryptionKeyAlgorithm;
    private final int contentEncryptionKeyByteLength;

    public ContentEncryptionKeyDescriptor(int contentEncryptionKeyByteLength, String contentEncryptionKeyAlgorithm) {
        this.contentEncryptionKeyByteLength = contentEncryptionKeyByteLength;
        this.contentEncryptionKeyAlgorithm = contentEncryptionKeyAlgorithm;
    }

    public int getContentEncryptionKeyByteLength() {
        return this.contentEncryptionKeyByteLength;
    }

    public String getContentEncryptionKeyAlgorithm() {
        return this.contentEncryptionKeyAlgorithm;
    }
}
