package org.jose4j.jwa;

import java.security.Key;
import java.security.Signature;
import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.Mac;

/* loaded from: classes5.dex */
public class CryptoPrimitive {
    private final Cipher cip;
    private final KeyAgreement kag;
    private final Key key;
    private final Mac mac;
    private final Signature sig;

    public CryptoPrimitive(Signature sig) {
        this(sig, null, null, null, null);
    }

    public CryptoPrimitive(Cipher cipher) {
        this(null, cipher, null, null, null);
    }

    public CryptoPrimitive(Mac mac) {
        this(null, null, mac, null, null);
    }

    public CryptoPrimitive(Key key) {
        this(null, null, null, key, null);
    }

    public CryptoPrimitive(KeyAgreement keyAgreement) {
        this(null, null, null, null, keyAgreement);
    }

    private CryptoPrimitive(Signature sig, Cipher cip, Mac mac, Key key, KeyAgreement kag) {
        this.sig = sig;
        this.cip = cip;
        this.mac = mac;
        this.key = key;
        this.kag = kag;
    }

    public Signature getSignature() {
        return this.sig;
    }

    public Cipher getCipher() {
        return this.cip;
    }

    public Mac getMac() {
        return this.mac;
    }

    public Key getKey() {
        return this.key;
    }

    public KeyAgreement getKeyAgreement() {
        return this.kag;
    }
}
