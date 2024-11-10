package org.jose4j.jwe.kdf;

import java.io.ByteArrayOutputStream;
import javax.crypto.Mac;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.ByteUtil;
import org.jose4j.lang.JoseException;
import org.jose4j.lang.UncheckedJoseException;
import org.jose4j.mac.MacUtil;

/* loaded from: classes5.dex */
public class PasswordBasedKeyDerivationFunction2 {
    private String hmacAlgorithm;

    public PasswordBasedKeyDerivationFunction2(String hmacAlgorithm) {
        this.hmacAlgorithm = hmacAlgorithm;
    }

    public byte[] derive(byte[] password, byte[] salt, int iterationCount, int dkLen) throws JoseException {
        return derive(password, salt, iterationCount, dkLen, null);
    }

    public byte[] derive(byte[] password, byte[] salt, int iterationCount, int dkLen, String provider) throws JoseException {
        PasswordBasedKeyDerivationFunction2 passwordBasedKeyDerivationFunction2 = this;
        Mac prf = MacUtil.getInitializedMac(passwordBasedKeyDerivationFunction2.hmacAlgorithm, new HmacKey(password), provider);
        int hLen = prf.getMacLength();
        if (dkLen > 4294967295L) {
            throw new UncheckedJoseException("derived key too long " + dkLen);
        }
        int l = (int) Math.ceil(dkLen / hLen);
        int r = dkLen - ((l - 1) * hLen);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i = 0;
        while (i < l) {
            byte[] block = passwordBasedKeyDerivationFunction2.f(salt, iterationCount, i + 1, prf);
            if (i == l - 1) {
                block = ByteUtil.subArray(block, 0, r);
            }
            byteArrayOutputStream.write(block, 0, block.length);
            i++;
            passwordBasedKeyDerivationFunction2 = this;
        }
        return byteArrayOutputStream.toByteArray();
    }

    byte[] f(byte[] salt, int iterationCount, int blockIndex, Mac prf) {
        byte[] currentU;
        byte[] lastU = null;
        byte[] xorU = null;
        for (int i = 1; i <= iterationCount; i++) {
            if (i == 1) {
                byte[] inputBytes = ByteUtil.concat(salt, ByteUtil.getBytes(blockIndex));
                currentU = prf.doFinal(inputBytes);
                xorU = currentU;
            } else {
                currentU = prf.doFinal(lastU);
                for (int j = 0; j < currentU.length; j++) {
                    xorU[j] = (byte) (currentU[j] ^ xorU[j]);
                }
            }
            lastU = currentU;
        }
        return xorU;
    }
}
