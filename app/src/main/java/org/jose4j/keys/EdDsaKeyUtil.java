package org.jose4j.keys;

import java.math.BigInteger;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.EdECPrivateKey;
import java.security.interfaces.EdECPublicKey;
import java.security.spec.EdECPoint;
import java.security.spec.EdECPrivateKeySpec;
import java.security.spec.EdECPublicKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.NamedParameterSpec;
import java.util.Arrays;
import java.util.Optional;
import org.jose4j.lang.ByteUtil;
import org.jose4j.lang.JoseException;

/* loaded from: classes5.dex */
public class EdDsaKeyUtil extends OctetKeyPairUtil {
    private static final byte BYTE_00000000 = 0;
    private static final byte BYTE_01111111 = Byte.MAX_VALUE;
    private static final byte BYTE_10000000 = Byte.MIN_VALUE;
    public static final String ED25519 = "Ed25519";
    public static final String ED448 = "Ed448";

    public EdDsaKeyUtil() {
        this(null, null);
    }

    public EdDsaKeyUtil(String provider, SecureRandom secureRandom) {
        super(provider, secureRandom);
    }

    @Override // org.jose4j.keys.KeyPairUtil
    String getAlgorithm() {
        return "EDDSA";
    }

    @Override // org.jose4j.keys.OctetKeyPairUtil
    public byte[] rawPublicKey(Key key) {
        EdECPublicKey edECPublicKey = (EdECPublicKey) key;
        EdECPoint edEdPoint = edECPublicKey.getPoint();
        BigInteger y = edEdPoint.getY();
        byte[] yBytes = y.toByteArray();
        byte[] yReversedBytes = ByteUtil.reverse(yBytes);
        int byteLen = edECPublicKey.getParams().getName().equals("Ed25519") ? 32 : 57;
        if (yReversedBytes.length != byteLen) {
            yReversedBytes = Arrays.copyOf(yReversedBytes, byteLen);
        }
        byte byteToOrWith = edEdPoint.isXOdd() ? Byte.MIN_VALUE : (byte) 0;
        int length = yReversedBytes.length - 1;
        yReversedBytes[length] = (byte) (yReversedBytes[length] | byteToOrWith);
        return yReversedBytes;
    }

    @Override // org.jose4j.keys.OctetKeyPairUtil
    public byte[] rawPrivateKey(PrivateKey privateKey) {
        EdECPrivateKey edECPrivateKey = (EdECPrivateKey) privateKey;
        Optional<byte[]> optionalBytes = edECPrivateKey.getBytes();
        return optionalBytes.orElse(ByteUtil.EMPTY_BYTES);
    }

    @Override // org.jose4j.keys.OctetKeyPairUtil
    public EdECPublicKey publicKey(byte[] publicKeyBytes, String name) throws JoseException {
        byte[] publicKeyBytes2 = (byte[]) publicKeyBytes.clone();
        byte rightByte = publicKeyBytes2[publicKeyBytes2.length - 1];
        int length = publicKeyBytes2.length - 1;
        publicKeyBytes2[length] = (byte) (publicKeyBytes2[length] & Byte.MAX_VALUE);
        boolean xIsOdd = (rightByte & Byte.MIN_VALUE) != 0;
        BigInteger y = BigEndianBigInteger.fromBytes(ByteUtil.reverse(publicKeyBytes2));
        NamedParameterSpec paramSpec = getNamedParameterSpec(name);
        EdECPoint ep = new EdECPoint(xIsOdd, y);
        EdECPublicKeySpec keySpec = new EdECPublicKeySpec(paramSpec, ep);
        try {
            PublicKey publicKey = getKeyFactory().generatePublic(keySpec);
            return (EdECPublicKey) publicKey;
        } catch (InvalidKeySpecException e) {
            throw new JoseException("Invalid key spec: " + e, e);
        }
    }

    @Override // org.jose4j.keys.OctetKeyPairUtil
    public EdECPrivateKey privateKey(byte[] privateKeyBytes, String name) throws JoseException {
        NamedParameterSpec paramSpec = getNamedParameterSpec(name);
        EdECPrivateKeySpec privateKeySpec = new EdECPrivateKeySpec(paramSpec, privateKeyBytes);
        try {
            PrivateKey privateKey = getKeyFactory().generatePrivate(privateKeySpec);
            return (EdECPrivateKey) privateKey;
        } catch (InvalidKeySpecException e) {
            throw new JoseException("Invalid key spec: " + e, e);
        }
    }

    public static boolean isEdECPublicKey(Key key) {
        try {
            return key instanceof EdECPublicKey;
        } catch (NoClassDefFoundError e) {
            return false;
        }
    }

    public static boolean isEdECPrivateKey(Key key) {
        try {
            return key instanceof EdECPrivateKey;
        } catch (NoClassDefFoundError e) {
            return false;
        }
    }
}
