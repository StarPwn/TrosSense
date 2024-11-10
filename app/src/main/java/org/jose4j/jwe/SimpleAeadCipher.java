package org.jose4j.jwe;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.GCMParameterSpec;
import org.jose4j.keys.AesKey;
import org.jose4j.lang.ByteUtil;
import org.jose4j.lang.ExceptionHelp;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;

/* loaded from: classes5.dex */
public class SimpleAeadCipher {
    public static final String GCM_TRANSFORMATION_NAME = "AES/GCM/NoPadding";
    private String algorithm;
    private int tagByteLength;

    public SimpleAeadCipher(String algorithm, int tagByteLength) {
        this.algorithm = algorithm;
        this.tagByteLength = tagByteLength;
    }

    public Cipher getInitialisedCipher(Key key, byte[] iv, int mode, String provider) throws JoseException {
        Cipher cipher = CipherUtil.getCipher(this.algorithm, provider);
        try {
            GCMParameterSpec parameterSpec = new GCMParameterSpec(ByteUtil.bitLength(this.tagByteLength), iv);
            cipher.init(mode, key, parameterSpec);
            return cipher;
        } catch (InvalidAlgorithmParameterException e) {
            throw new JoseException(e.toString(), e);
        } catch (InvalidKeyException e2) {
            throw new JoseException("Invalid key for " + this.algorithm, e2);
        }
    }

    public CipherOutput encrypt(Key key, byte[] iv, byte[] plaintext, byte[] aad, String provider) throws JoseException {
        Cipher cipher = getInitialisedCipher(key, iv, 1, provider);
        updateAad(cipher, aad);
        try {
            byte[] cipherOutput = cipher.doFinal(plaintext);
            CipherOutput result = new CipherOutput();
            int tagIndex = cipherOutput.length - this.tagByteLength;
            result.ciphertext = ByteUtil.subArray(cipherOutput, 0, tagIndex);
            result.tag = ByteUtil.subArray(cipherOutput, tagIndex, this.tagByteLength);
            return result;
        } catch (BadPaddingException | IllegalBlockSizeException e) {
            throw new JoseException(e.toString(), e);
        }
    }

    private void updateAad(Cipher cipher, byte[] aad) {
        if (aad != null && aad.length > 0) {
            cipher.updateAAD(aad);
        }
    }

    public byte[] decrypt(Key key, byte[] iv, byte[] ciphertext, byte[] tag, byte[] aad, String provider) throws JoseException {
        Cipher cipher = getInitialisedCipher(key, iv, 2, provider);
        return decrypt(ciphertext, tag, aad, cipher);
    }

    public byte[] decrypt(byte[] ciphertext, byte[] tag, byte[] aad, Cipher cipher) throws JoseException {
        updateAad(cipher, aad);
        try {
            return cipher.doFinal(ByteUtil.concat(ciphertext, tag));
        } catch (BadPaddingException | IllegalBlockSizeException e) {
            throw new JoseException(e.toString(), e);
        }
    }

    public boolean isAvailable(Logger log, int keyByteLength, int ivByteLength, String joseAlg) {
        if (!CipherStrengthSupport.isAvailable(this.algorithm, keyByteLength)) {
            return false;
        }
        byte[] plain = {112, 108, 97, 105, 110, 116, 101, 120, 116};
        byte[] aad = {97, 97, 100};
        byte[] cek = ByteUtil.randomBytes(keyByteLength);
        byte[] iv = ByteUtil.randomBytes(ivByteLength);
        try {
            encrypt(new AesKey(cek), iv, plain, aad, null);
            return true;
        } catch (Throwable e) {
            log.debug("{} is not available ({}).", joseAlg, ExceptionHelp.toStringWithCauses(e));
            return false;
        }
    }

    /* loaded from: classes5.dex */
    public static class CipherOutput {
        private byte[] ciphertext;
        private byte[] tag;

        public byte[] getCiphertext() {
            return this.ciphertext;
        }

        public byte[] getTag() {
            return this.tag;
        }
    }
}
