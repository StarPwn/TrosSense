package org.jose4j.keys;

import java.security.InvalidAlgorithmParameterException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.NamedParameterSpec;
import org.jose4j.lang.ExceptionHelp;
import org.jose4j.lang.JoseException;

/* loaded from: classes5.dex */
public abstract class OctetKeyPairUtil extends KeyPairUtil {
    public abstract PrivateKey privateKey(byte[] bArr, String str) throws JoseException;

    public abstract PublicKey publicKey(byte[] bArr, String str) throws JoseException;

    public abstract byte[] rawPrivateKey(PrivateKey privateKey);

    public abstract byte[] rawPublicKey(Key key);

    @Override // org.jose4j.keys.KeyPairUtil
    public /* bridge */ /* synthetic */ PublicKey fromPemEncoded(String str) throws JoseException, InvalidKeySpecException {
        return super.fromPemEncoded(str);
    }

    @Override // org.jose4j.keys.KeyPairUtil
    public /* bridge */ /* synthetic */ boolean isAvailable() {
        return super.isAvailable();
    }

    public OctetKeyPairUtil(String provider, SecureRandom secureRandom) {
        super(provider, secureRandom);
    }

    public static OctetKeyPairUtil getOctetKeyPairUtil(String subtypeName, String provider, SecureRandom secureRandom) {
        if (subtypeName.equals("Ed25519") || subtypeName.equals("Ed448")) {
            return new EdDsaKeyUtil(provider, secureRandom);
        }
        if (subtypeName.equals("X25519") || subtypeName.equals("X448")) {
            return new XDHKeyUtil(provider, secureRandom);
        }
        return null;
    }

    public KeyPair generateKeyPair(String name) throws JoseException {
        KeyPairGenerator keyGenerator = getKeyPairGenerator();
        NamedParameterSpec spec = getNamedParameterSpec(name);
        try {
            if (this.secureRandom == null) {
                keyGenerator.initialize(spec);
            } else {
                keyGenerator.initialize(spec, this.secureRandom);
            }
            return keyGenerator.generateKeyPair();
        } catch (InvalidAlgorithmParameterException e) {
            throw new JoseException("Unable to create EdDSA key pair: " + e, e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public NamedParameterSpec getNamedParameterSpec(String name) throws JoseException {
        try {
            return new NamedParameterSpec(name);
        } catch (NoClassDefFoundError ncd) {
            throw new JoseException(name + " NamedParameterSpec not available. " + ExceptionHelp.toStringWithCauses(ncd));
        }
    }
}
