package org.cloudburstmc.protocol.bedrock.util;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.jose4j.json.JsonUtil;
import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.keys.AesKey;
import org.jose4j.lang.HashUtil;
import org.jose4j.lang.JoseException;

/* loaded from: classes5.dex */
public final class EncryptionUtils {
    public static final String ALGORITHM_TYPE = "ES384";
    private static final KeyPairGenerator KEY_PAIR_GEN;
    private static final ECPublicKey MOJANG_PUBLIC_KEY;
    private static final String MOJANG_PUBLIC_KEY_BASE64 = "MHYwEAYHKoZIzj0CAQYFK4EEACIDYgAECRXueJeTDqNRRgJi/vlRufByu/2G0i2Ebt6YMar5QX/R0DIIyrJMcUpruK4QveTfJSTp3Shlq4Gk34cD/4GUWwkv0DVuzeuB+tXija7HBxii03NHDbPAD0AKnLr2wdAp";
    private static final ECPublicKey OLD_MOJANG_PUBLIC_KEY;
    private static final String OLD_MOJANG_PUBLIC_KEY_BASE64 = "MHYwEAYHKoZIzj0CAQYFK4EEACIDYgAE8ELkixyLcwlZryUQcu1TvPOmI2B7vX83ndnWRUaXm74wFfa5f/lwQNTfrLVHa2PmenpGI6JhIMUJaWZrjmMj90NoKNFSNBuKdm8rYiXsfaz3K36x/1U26HpG0ZxK/V1V";
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final AlgorithmConstraints ALGORITHM_CONSTRAINTS = new AlgorithmConstraints(AlgorithmConstraints.ConstraintType.PERMIT, "ES384");

    private EncryptionUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    static {
        String namedGroups = System.getProperty("jdk.tls.namedGroups");
        System.setProperty("jdk.tls.namedGroups", (namedGroups == null || namedGroups.isEmpty()) ? "secp384r1" : ", secp384r1");
        try {
            KEY_PAIR_GEN = KeyPairGenerator.getInstance("EC");
            KEY_PAIR_GEN.initialize(new ECGenParameterSpec("secp384r1"));
            MOJANG_PUBLIC_KEY = parseKey(MOJANG_PUBLIC_KEY_BASE64);
            OLD_MOJANG_PUBLIC_KEY = parseKey(OLD_MOJANG_PUBLIC_KEY_BASE64);
        } catch (InvalidAlgorithmParameterException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AssertionError("Unable to initialize required encryption", e);
        }
    }

    public static ECPublicKey parseKey(String b64) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return (ECPublicKey) KeyFactory.getInstance("EC").generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(b64)));
    }

    public static KeyPair createKeyPair() {
        return KEY_PAIR_GEN.generateKeyPair();
    }

    public static byte[] verifyClientData(String clientDataJwt, String identityPublicKey) throws NoSuchAlgorithmException, InvalidKeySpecException, JoseException {
        return verifyClientData(clientDataJwt, parseKey(identityPublicKey));
    }

    public static byte[] verifyClientData(String clientDataJwt, PublicKey identityPublicKey) throws JoseException {
        JsonWebSignature clientData = new JsonWebSignature();
        clientData.setCompactSerialization(clientDataJwt);
        clientData.setKey(identityPublicKey);
        if (!clientData.verifySignature()) {
            return null;
        }
        return clientData.getUnverifiedPayloadBytes();
    }

    public static ChainValidationResult validateChain(List<String> chain) throws JoseException, NoSuchAlgorithmException, InvalidKeySpecException {
        switch (chain.size()) {
            case 1:
                JsonWebSignature identity = new JsonWebSignature();
                identity.setCompactSerialization(chain.get(0));
                return new ChainValidationResult(false, identity.getUnverifiedPayload());
            case 2:
            default:
                throw new IllegalStateException("Unexpected login chain length");
            case 3:
                ECPublicKey currentKey = null;
                Map<String, Object> parsedPayload = null;
                for (int i = 0; i < 3; i++) {
                    JsonWebSignature signature = new JsonWebSignature();
                    signature.setCompactSerialization(chain.get(i));
                    ECPublicKey expectedKey = parseKey(signature.getHeader("x5u"));
                    if (currentKey == null) {
                        currentKey = expectedKey;
                    } else if (!currentKey.equals(expectedKey)) {
                        throw new IllegalStateException("Received broken chain");
                    }
                    signature.setAlgorithmConstraints(ALGORITHM_CONSTRAINTS);
                    signature.setKey(currentKey);
                    if (!signature.verifySignature()) {
                        throw new IllegalStateException("Chain signature doesn't match content");
                    }
                    if (i == 1 && !currentKey.equals(MOJANG_PUBLIC_KEY) && !currentKey.equals(OLD_MOJANG_PUBLIC_KEY)) {
                        throw new IllegalStateException("The chain isn't signed by Mojang!");
                    }
                    parsedPayload = JsonUtil.parseJson(signature.getUnverifiedPayload());
                    String identityPublicKey = (String) JsonUtils.childAsType(parsedPayload, "identityPublicKey", String.class);
                    currentKey = parseKey(identityPublicKey);
                }
                return new ChainValidationResult(true, parsedPayload);
        }
    }

    public static SecretKey getSecretKey(PrivateKey localPrivateKey, PublicKey remotePublicKey, byte[] token) throws InvalidKeyException {
        byte[] sharedSecret = getEcdhSecret(localPrivateKey, remotePublicKey);
        try {
            MessageDigest digest = MessageDigest.getInstance(HashUtil.SHA_256);
            digest.update(token);
            digest.update(sharedSecret);
            byte[] secretKeyBytes = digest.digest();
            return new SecretKeySpec(secretKeyBytes, AesKey.ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError(e);
        }
    }

    private static byte[] getEcdhSecret(PrivateKey localPrivateKey, PublicKey remotePublicKey) throws InvalidKeyException {
        try {
            KeyAgreement agreement = KeyAgreement.getInstance("ECDH");
            agreement.init(localPrivateKey);
            agreement.doPhase(remotePublicKey, true);
            return agreement.generateSecret();
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError(e);
        }
    }

    public static String createHandshakeJwt(KeyPair serverKeyPair, byte[] token) throws JoseException {
        JsonWebSignature signature = new JsonWebSignature();
        signature.setAlgorithmHeaderValue("ES384");
        signature.setHeader("x5u", Base64.getEncoder().encodeToString(serverKeyPair.getPublic().getEncoded()));
        signature.setKey(serverKeyPair.getPrivate());
        JwtClaims claims = new JwtClaims();
        claims.setClaim("salt", Base64.getEncoder().encodeToString(token));
        signature.setPayload(claims.toJson());
        return signature.getCompactSerialization();
    }

    public static byte[] generateRandomToken() {
        byte[] token = new byte[16];
        SECURE_RANDOM.nextBytes(token);
        return token;
    }

    public static ECPublicKey getMojangPublicKey() {
        return MOJANG_PUBLIC_KEY;
    }

    public static Cipher createCipher(boolean gcm, boolean encrypt, SecretKey key) {
        byte[] iv;
        String transformation;
        try {
            if (gcm) {
                iv = new byte[16];
                System.arraycopy(key.getEncoded(), 0, iv, 0, 12);
                iv[15] = 2;
                transformation = "AES/CTR/NoPadding";
            } else {
                iv = Arrays.copyOf(key.getEncoded(), 16);
                transformation = "AES/CFB8/NoPadding";
            }
            Cipher cipher = Cipher.getInstance(transformation);
            cipher.init(encrypt ? 1 : 2, key, new IvParameterSpec(iv));
            return cipher;
        } catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new AssertionError("Unable to initialize required encryption", e);
        }
    }
}
