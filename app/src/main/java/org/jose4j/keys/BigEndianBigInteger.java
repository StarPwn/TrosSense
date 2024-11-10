package org.jose4j.keys;

import java.math.BigInteger;
import org.jose4j.base64url.Base64Url;
import org.jose4j.lang.ByteUtil;

/* loaded from: classes5.dex */
public class BigEndianBigInteger {
    public static BigInteger fromBytes(byte[] magnitude) {
        return new BigInteger(1, magnitude);
    }

    public static BigInteger fromBase64Url(String base64urlEncodedBytes) {
        Base64Url base64Url = new Base64Url();
        byte[] magnitude = base64Url.base64UrlDecode(base64urlEncodedBytes);
        return fromBytes(magnitude);
    }

    public static byte[] toByteArray(BigInteger bigInteger, int minArrayLength) {
        byte[] bytes = toByteArray(bigInteger);
        return minArrayLength > bytes.length ? ByteUtil.concat(new byte[minArrayLength - bytes.length], bytes) : bytes;
    }

    public static byte[] toByteArray(BigInteger bigInteger) {
        if (bigInteger.signum() < 0) {
            String msg = "Cannot convert negative values to an unsigned magnitude byte array: " + bigInteger;
            throw new IllegalArgumentException(msg);
        }
        byte[] twosComplementBytes = bigInteger.toByteArray();
        if (bigInteger.bitLength() % 8 == 0 && twosComplementBytes[0] == 0 && twosComplementBytes.length > 1) {
            byte[] magnitude = ByteUtil.subArray(twosComplementBytes, 1, twosComplementBytes.length - 1);
            return magnitude;
        }
        return twosComplementBytes;
    }

    public static String toBase64Url(BigInteger bigInteger) {
        Base64Url base64Url = new Base64Url();
        byte[] bytes = toByteArray(bigInteger);
        return base64Url.base64UrlEncode(bytes);
    }

    public static String toBase64Url(BigInteger bigInteger, int minByteArrayLength) {
        Base64Url base64Url = new Base64Url();
        byte[] bytes = toByteArray(bigInteger, minByteArrayLength);
        return base64Url.base64UrlEncode(bytes);
    }
}
