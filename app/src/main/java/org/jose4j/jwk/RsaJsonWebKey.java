package org.jose4j.jwk;

import java.math.BigInteger;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;
import org.jose4j.keys.RsaKeyUtil;
import org.jose4j.lang.JoseException;

/* loaded from: classes5.dex */
public class RsaJsonWebKey extends PublicJsonWebKey {
    public static final String EXPONENT_MEMBER_NAME = "e";
    public static final String FACTOR_CRT_COEFFICIENT = "t";
    public static final String FACTOR_CRT_EXPONENT_OTHER_MEMBER_NAME = "d";
    public static final String FIRST_CRT_COEFFICIENT_MEMBER_NAME = "qi";
    public static final String FIRST_FACTOR_CRT_EXPONENT_MEMBER_NAME = "dp";
    public static final String FIRST_PRIME_FACTOR_MEMBER_NAME = "p";
    public static final String KEY_TYPE = "RSA";
    public static final String MODULUS_MEMBER_NAME = "n";
    public static final String OTHER_PRIMES_INFO_MEMBER_NAME = "oth";
    public static final String PRIME_FACTOR_OTHER_MEMBER_NAME = "r";
    public static final String PRIVATE_EXPONENT_MEMBER_NAME = "d";
    public static final String SECOND_FACTOR_CRT_EXPONENT_MEMBER_NAME = "dq";
    public static final String SECOND_PRIME_FACTOR_MEMBER_NAME = "q";

    public RsaJsonWebKey(RSAPublicKey publicKey) {
        super(publicKey);
    }

    public RsaJsonWebKey(Map<String, Object> params) throws JoseException {
        this(params, null);
    }

    public RsaJsonWebKey(Map<String, Object> params, String jcaProvider) throws JoseException {
        super(params, jcaProvider);
        BigInteger modulus = getBigIntFromBase64UrlEncodedParam(params, MODULUS_MEMBER_NAME, true);
        BigInteger publicExponent = getBigIntFromBase64UrlEncodedParam(params, EXPONENT_MEMBER_NAME, true);
        RsaKeyUtil rsaKeyUtil = new RsaKeyUtil(jcaProvider, null);
        this.key = rsaKeyUtil.publicKey(modulus, publicExponent);
        checkForBareKeyCertMismatch();
        if (params.containsKey("d")) {
            BigInteger d = getBigIntFromBase64UrlEncodedParam(params, "d", false);
            if (!params.containsKey(FIRST_PRIME_FACTOR_MEMBER_NAME)) {
                this.privateKey = rsaKeyUtil.privateKey(modulus, d);
            } else {
                BigInteger p = getBigIntFromBase64UrlEncodedParam(params, FIRST_PRIME_FACTOR_MEMBER_NAME, false);
                BigInteger q = getBigIntFromBase64UrlEncodedParam(params, SECOND_PRIME_FACTOR_MEMBER_NAME, false);
                BigInteger dp = getBigIntFromBase64UrlEncodedParam(params, FIRST_FACTOR_CRT_EXPONENT_MEMBER_NAME, false);
                BigInteger dq = getBigIntFromBase64UrlEncodedParam(params, SECOND_FACTOR_CRT_EXPONENT_MEMBER_NAME, false);
                BigInteger qi = getBigIntFromBase64UrlEncodedParam(params, FIRST_CRT_COEFFICIENT_MEMBER_NAME, false);
                this.privateKey = rsaKeyUtil.privateKey(modulus, publicExponent, d, p, q, dp, dq, qi);
            }
        }
        removeFromOtherParams(MODULUS_MEMBER_NAME, EXPONENT_MEMBER_NAME, "d", FIRST_PRIME_FACTOR_MEMBER_NAME, SECOND_PRIME_FACTOR_MEMBER_NAME, FIRST_FACTOR_CRT_EXPONENT_MEMBER_NAME, SECOND_FACTOR_CRT_EXPONENT_MEMBER_NAME, FIRST_CRT_COEFFICIENT_MEMBER_NAME);
    }

    @Override // org.jose4j.jwk.JsonWebKey
    public String getKeyType() {
        return "RSA";
    }

    public RSAPublicKey getRsaPublicKey() {
        return (RSAPublicKey) this.key;
    }

    public RSAPublicKey getRSAPublicKey() {
        return getRsaPublicKey();
    }

    public RSAPrivateKey getRsaPrivateKey() {
        return (RSAPrivateKey) this.privateKey;
    }

    @Override // org.jose4j.jwk.PublicJsonWebKey
    protected void fillPublicTypeSpecificParams(Map<String, Object> params) {
        RSAPublicKey rsaPublicKey = getRsaPublicKey();
        putBigIntAsBase64UrlEncodedParam(params, MODULUS_MEMBER_NAME, rsaPublicKey.getModulus());
        putBigIntAsBase64UrlEncodedParam(params, EXPONENT_MEMBER_NAME, rsaPublicKey.getPublicExponent());
    }

    @Override // org.jose4j.jwk.PublicJsonWebKey
    protected void fillPrivateTypeSpecificParams(Map<String, Object> params) {
        RSAPrivateKey rsaPrivateKey = getRsaPrivateKey();
        if (rsaPrivateKey != null) {
            putBigIntAsBase64UrlEncodedParam(params, "d", rsaPrivateKey.getPrivateExponent());
            if (rsaPrivateKey instanceof RSAPrivateCrtKey) {
                RSAPrivateCrtKey crt = (RSAPrivateCrtKey) rsaPrivateKey;
                putBigIntAsBase64UrlEncodedParam(params, FIRST_PRIME_FACTOR_MEMBER_NAME, crt.getPrimeP());
                putBigIntAsBase64UrlEncodedParam(params, SECOND_PRIME_FACTOR_MEMBER_NAME, crt.getPrimeQ());
                putBigIntAsBase64UrlEncodedParam(params, FIRST_FACTOR_CRT_EXPONENT_MEMBER_NAME, crt.getPrimeExponentP());
                putBigIntAsBase64UrlEncodedParam(params, SECOND_FACTOR_CRT_EXPONENT_MEMBER_NAME, crt.getPrimeExponentQ());
                putBigIntAsBase64UrlEncodedParam(params, FIRST_CRT_COEFFICIENT_MEMBER_NAME, crt.getCrtCoefficient());
            }
        }
    }

    @Override // org.jose4j.jwk.JsonWebKey
    protected String produceThumbprintHashInput() {
        HashMap<String, Object> params = new HashMap<>();
        fillPublicTypeSpecificParams(params);
        return String.format("{\"e\":\"%s\",\"kty\":\"RSA\",\"n\":\"%s\"}", params.get(EXPONENT_MEMBER_NAME), params.get(MODULUS_MEMBER_NAME));
    }
}
