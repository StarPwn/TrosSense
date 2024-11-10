package org.jose4j.keys;

import java.math.BigInteger;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.XECPrivateKey;
import java.security.interfaces.XECPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.NamedParameterSpec;
import java.security.spec.XECPrivateKeySpec;
import java.security.spec.XECPublicKeySpec;
import java.util.Arrays;
import java.util.Optional;
import org.jose4j.lang.ByteUtil;
import org.jose4j.lang.JoseException;

/* loaded from: classes5.dex */
public class XDHKeyUtil extends OctetKeyPairUtil {
    private static final BigInteger P_X25519 = new BigInteger("57896044618658097711785492504343953926634992332820282019728792003956564819949");
    private static final BigInteger P_X448 = new BigInteger("726838724295606890549323807888004534353641360687318060281490199180612328166730772686396383698676545930088884461843637361053498018365439");
    public static final String X25519 = "X25519";
    public static final String X448 = "X448";

    public XDHKeyUtil() {
        this(null, null);
    }

    public XDHKeyUtil(String provider, SecureRandom secureRandom) {
        super(provider, secureRandom);
    }

    @Override // org.jose4j.keys.OctetKeyPairUtil
    public byte[] rawPublicKey(Key key) {
        XECPublicKey xecPublicKey = (XECPublicKey) key;
        BigInteger theU = xecPublicKey.getU();
        String name = ((NamedParameterSpec) xecPublicKey.getParams()).getName();
        boolean is25519 = "X25519".equals(name);
        BigInteger p = is25519 ? P_X25519 : P_X448;
        byte[] backwordsU = ByteUtil.reverse(theU.mod(p).toByteArray());
        int byteLen = is25519 ? 32 : 57;
        if (backwordsU.length != byteLen) {
            return Arrays.copyOf(backwordsU, byteLen);
        }
        return backwordsU;
    }

    @Override // org.jose4j.keys.OctetKeyPairUtil
    public byte[] rawPrivateKey(PrivateKey privateKey) {
        XECPrivateKey xecPrivateKey = (XECPrivateKey) privateKey;
        Optional<byte[]> scalar = xecPrivateKey.getScalar();
        return scalar.orElse(ByteUtil.EMPTY_BYTES);
    }

    @Override // org.jose4j.keys.OctetKeyPairUtil
    public XECPublicKey publicKey(byte[] publicKeyBytes, String name) throws JoseException {
        NamedParameterSpec namedParameterSpec = getNamedParameterSpec(name);
        byte[] reversedBytes = ByteUtil.reverse(publicKeyBytes);
        int numBits = "X25519".equals(name) ? 255 : 448;
        int numBitsMod8 = numBits % 8;
        if (numBitsMod8 != 0) {
            int andMask = (1 << numBitsMod8) - 1;
            reversedBytes[0] = (byte) (reversedBytes[0] & andMask);
        }
        BigInteger u = new BigInteger(1, reversedBytes);
        XECPublicKeySpec keySpec = new XECPublicKeySpec(namedParameterSpec, u);
        try {
            PublicKey publicKey = getKeyFactory().generatePublic(keySpec);
            return (XECPublicKey) publicKey;
        } catch (InvalidKeySpecException e) {
            throw new JoseException("Invalid key spec: " + e, e);
        }
    }

    @Override // org.jose4j.keys.OctetKeyPairUtil
    public XECPrivateKey privateKey(byte[] privateKeyBytes, String name) throws JoseException {
        NamedParameterSpec paramSpec = getNamedParameterSpec(name);
        XECPrivateKeySpec privateKeySpec = new XECPrivateKeySpec(paramSpec, privateKeyBytes);
        try {
            PrivateKey privateKey = getKeyFactory().generatePrivate(privateKeySpec);
            return (XECPrivateKey) privateKey;
        } catch (InvalidKeySpecException e) {
            throw new JoseException("Invalid key spec: " + e, e);
        }
    }

    @Override // org.jose4j.keys.KeyPairUtil
    String getAlgorithm() {
        return "XDH";
    }

    public static boolean isXECPublicKey(Key key) {
        try {
            return key instanceof XECPublicKey;
        } catch (NoClassDefFoundError e) {
            return false;
        }
    }

    public static boolean isXECPrivateKey(Key key) {
        try {
            return key instanceof XECPrivateKey;
        } catch (NoClassDefFoundError e) {
            return false;
        }
    }
}
