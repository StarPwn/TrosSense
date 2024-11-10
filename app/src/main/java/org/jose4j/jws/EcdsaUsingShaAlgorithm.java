package org.jose4j.jws;

import com.trossense.bl;
import io.netty.handler.codec.memcache.binary.DefaultBinaryMemcacheResponse;
import java.io.IOException;
import java.math.BigInteger;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.ECKey;
import java.security.spec.ECParameterSpec;
import java.security.spec.EllipticCurve;
import org.jose4j.jca.ProviderContext;
import org.jose4j.jwa.CryptoPrimitive;
import org.jose4j.jwk.PublicJsonWebKey;
import org.jose4j.keys.BigEndianBigInteger;
import org.jose4j.keys.EllipticCurves;
import org.jose4j.lang.ByteUtil;
import org.jose4j.lang.ExceptionHelp;
import org.jose4j.lang.InvalidKeyException;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: classes5.dex */
public class EcdsaUsingShaAlgorithm extends BaseSignatureAlgorithm implements JsonWebSignatureAlgorithm {
    private final String curveName;
    private final int signatureByteLength;

    public EcdsaUsingShaAlgorithm(String id, String javaAlgo, String curveName, int signatureByteLength) {
        super(id, javaAlgo, "EC");
        this.curveName = curveName;
        this.signatureByteLength = signatureByteLength;
    }

    @Override // org.jose4j.jws.BaseSignatureAlgorithm, org.jose4j.jws.JsonWebSignatureAlgorithm
    public boolean verifySignature(byte[] signatureBytes, Key key, byte[] securedInputBytes, ProviderContext providerContext) throws JoseException {
        if (signatureBytes.length > this.signatureByteLength) {
            return false;
        }
        byte[] rb = ByteUtil.leftHalf(signatureBytes);
        BigInteger r = BigEndianBigInteger.fromBytes(rb);
        byte[] sb = ByteUtil.rightHalf(signatureBytes);
        BigInteger s = BigEndianBigInteger.fromBytes(sb);
        ECParameterSpec ecParams = EllipticCurves.getSpec(this.curveName);
        BigInteger orderN = ecParams.getOrder();
        if (r.mod(orderN).equals(BigInteger.ZERO) || s.mod(orderN).equals(BigInteger.ZERO)) {
            return false;
        }
        try {
            byte[] derEncodedSignatureBytes = convertConcatenatedToDer(signatureBytes);
            return super.verifySignature(derEncodedSignatureBytes, key, securedInputBytes, providerContext);
        } catch (IOException e) {
            throw new JoseException("Unable to convert R and S as a concatenated byte array to DER encoding.", e);
        }
    }

    @Override // org.jose4j.jws.BaseSignatureAlgorithm, org.jose4j.jws.JsonWebSignatureAlgorithm
    public byte[] sign(CryptoPrimitive cryptoPrimitive, byte[] securedInputBytes) throws JoseException {
        byte[] derEncodedSignatureBytes = super.sign(cryptoPrimitive, securedInputBytes);
        try {
            return convertDerToConcatenated(derEncodedSignatureBytes, this.signatureByteLength);
        } catch (IOException e) {
            throw new JoseException("Unable to convert DER encoding to R and S as a concatenated byte array.", e);
        }
    }

    public static byte[] convertConcatenatedToDer(byte[] concatenatedSignatureBytes) throws IOException {
        int offset;
        byte[] derEncodedSignatureBytes;
        int rawLen = concatenatedSignatureBytes.length / 2;
        int i = rawLen;
        while (i > 1 && concatenatedSignatureBytes[rawLen - i] == 0) {
            i--;
        }
        int j = i;
        if (concatenatedSignatureBytes[rawLen - i] < 0) {
            j++;
        }
        int k = rawLen;
        while (k > 1 && concatenatedSignatureBytes[(rawLen * 2) - k] == 0) {
            k--;
        }
        int l = k;
        if (concatenatedSignatureBytes[(rawLen * 2) - k] < 0) {
            l++;
        }
        int len = j + 2 + 2 + l;
        if (len > 255) {
            throw new IOException("Invalid format of ECDSA signature");
        }
        if (len < 128) {
            derEncodedSignatureBytes = new byte[j + 4 + 2 + l];
            offset = 1;
        } else {
            int offset2 = j + 5;
            byte[] derEncodedSignatureBytes2 = new byte[offset2 + 2 + l];
            derEncodedSignatureBytes2[1] = DefaultBinaryMemcacheResponse.RESPONSE_MAGIC_BYTE;
            offset = 2;
            derEncodedSignatureBytes = derEncodedSignatureBytes2;
        }
        derEncodedSignatureBytes[0] = 48;
        int offset3 = offset + 1;
        derEncodedSignatureBytes[offset] = (byte) len;
        int offset4 = offset3 + 1;
        derEncodedSignatureBytes[offset3] = 2;
        int offset5 = offset4 + 1;
        derEncodedSignatureBytes[offset4] = (byte) j;
        System.arraycopy(concatenatedSignatureBytes, rawLen - i, derEncodedSignatureBytes, (offset5 + j) - i, i);
        int offset6 = offset5 + j;
        int offset7 = offset6 + 1;
        derEncodedSignatureBytes[offset6] = 2;
        derEncodedSignatureBytes[offset7] = (byte) l;
        System.arraycopy(concatenatedSignatureBytes, (rawLen * 2) - k, derEncodedSignatureBytes, ((offset7 + 1) + l) - k, k);
        return derEncodedSignatureBytes;
    }

    public static byte[] convertDerToConcatenated(byte[] derEncodedBytes, int outputLength) throws IOException {
        int offset;
        if (derEncodedBytes.length < 8 || derEncodedBytes[0] != 48) {
            throw new IOException("Invalid format of ECDSA signature");
        }
        if (derEncodedBytes[1] > 0) {
            offset = 2;
        } else {
            int offset2 = derEncodedBytes[1];
            if (offset2 == -127) {
                offset = 3;
            } else {
                throw new IOException("Invalid format of ECDSA signature");
            }
        }
        byte rLength = derEncodedBytes[offset + 1];
        int i = rLength;
        while (i > 0 && derEncodedBytes[((offset + 2) + rLength) - i] == 0) {
            i--;
        }
        byte sLength = derEncodedBytes[offset + 2 + rLength + 1];
        int j = sLength;
        while (j > 0 && derEncodedBytes[((((offset + 2) + rLength) + 2) + sLength) - j] == 0) {
            j--;
        }
        int rawLen = Math.max(Math.max(i, j), outputLength / 2);
        if ((derEncodedBytes[offset - 1] & 255) != derEncodedBytes.length - offset || (derEncodedBytes[offset - 1] & 255) != rLength + 2 + 2 + sLength || derEncodedBytes[offset] != 2 || derEncodedBytes[offset + 2 + rLength] != 2) {
            throw new IOException("Invalid format of ECDSA signature");
        }
        byte[] concatenatedSignatureBytes = new byte[rawLen * 2];
        System.arraycopy(derEncodedBytes, ((offset + 2) + rLength) - i, concatenatedSignatureBytes, rawLen - i, i);
        System.arraycopy(derEncodedBytes, ((((offset + 2) + rLength) + 2) + sLength) - j, concatenatedSignatureBytes, (rawLen * 2) - j, j);
        return concatenatedSignatureBytes;
    }

    @Override // org.jose4j.jws.BaseSignatureAlgorithm
    public void validatePrivateKey(PrivateKey privateKey) throws InvalidKeyException {
        validateKeySpec(privateKey);
    }

    @Override // org.jose4j.jws.BaseSignatureAlgorithm
    public void validatePublicKey(PublicKey publicKey) throws InvalidKeyException {
        validateKeySpec(publicKey);
    }

    private void validateKeySpec(Key key) throws InvalidKeyException {
        if (key instanceof ECKey) {
            ECKey ecKey = (ECKey) key;
            ECParameterSpec spec = ecKey.getParams();
            EllipticCurve curve = spec.getCurve();
            String name = EllipticCurves.getName(curve);
            if (!getCurveName().equals(name)) {
                throw new InvalidKeyException(getAlgorithmIdentifier() + "/" + getJavaAlgorithm() + " expects a key using " + getCurveName() + " but was " + name);
            }
        }
    }

    public String getCurveName() {
        return this.curveName;
    }

    /* loaded from: classes5.dex */
    public static class EcdsaP256UsingSha256 extends EcdsaUsingShaAlgorithm {
        public EcdsaP256UsingSha256() {
            super(AlgorithmIdentifiers.ECDSA_USING_P256_CURVE_AND_SHA256, "SHA256withECDSA", EllipticCurves.P_256, 64);
        }
    }

    /* loaded from: classes5.dex */
    public static class EcdsaP384UsingSha384 extends EcdsaUsingShaAlgorithm {
        public EcdsaP384UsingSha384() {
            super("ES384", "SHA384withECDSA", EllipticCurves.P_384, 96);
        }
    }

    /* loaded from: classes5.dex */
    public static class EcdsaP521UsingSha512 extends EcdsaUsingShaAlgorithm {
        public EcdsaP521UsingSha512() {
            super(AlgorithmIdentifiers.ECDSA_USING_P521_CURVE_AND_SHA512, "SHA512withECDSA", EllipticCurves.P_521, bl.bt);
        }
    }

    /* loaded from: classes5.dex */
    public static class EcdsaSECP256K1UsingSha256 extends EcdsaUsingShaAlgorithm {
        Logger log;

        public EcdsaSECP256K1UsingSha256() {
            super(AlgorithmIdentifiers.ECDSA_USING_SECP256K1_CURVE_AND_SHA256, "SHA256withECDSA", EllipticCurves.SECP_256K1, 64);
            this.log = LoggerFactory.getLogger(getClass());
        }

        @Override // org.jose4j.jws.BaseSignatureAlgorithm, org.jose4j.jwa.Algorithm
        public boolean isAvailable() {
            if (!super.isAvailable()) {
                return false;
            }
            try {
                PublicJsonWebKey jwk = PublicJsonWebKey.Factory.newPublicJwk("{\"kty\":\"EC\",\"x\":\"gi0g9DzM2SvjVV7iD_upIU0urmZRjpoIc4Efu8563y8\",\"y\":\"Y5K6GofrdlWNLlfT8-AEyJyVZ3yJJcGgkGroHQCAhmk\",\"crv\":\"secp256k1\",\"d\":\"Vd99BKh6pxt3mXSDJzHuVrCq52xBXAKVahbuFb6dqBc\"}");
                CryptoPrimitive cryptoPrimitive = prepareForSign(jwk.getPrivateKey(), new ProviderContext());
                byte[] sig = sign(cryptoPrimitive, new byte[]{2, 6});
                return sig != null;
            } catch (JoseException e) {
                this.log.debug(getAlgorithmIdentifier() + " is not available due to " + ExceptionHelp.toStringWithCauses(e));
                return false;
            }
        }
    }
}
