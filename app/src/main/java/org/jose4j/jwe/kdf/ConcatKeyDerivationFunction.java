package org.jose4j.jwe.kdf;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import org.jose4j.lang.ByteUtil;
import org.jose4j.lang.HashUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: classes5.dex */
public class ConcatKeyDerivationFunction implements ConcatenationKeyDerivationFunctionWithSha256 {
    private static final Logger log = LoggerFactory.getLogger((Class<?>) ConcatKeyDerivationFunction.class);
    private int digestLength;
    private MessageDigest messageDigest;

    public ConcatKeyDerivationFunction(String hashAlgoritm) {
        this.messageDigest = HashUtil.getMessageDigest(hashAlgoritm);
        init();
    }

    public ConcatKeyDerivationFunction(String hashAlgoritm, String provider) {
        this.messageDigest = HashUtil.getMessageDigest(hashAlgoritm, provider);
        init();
    }

    private void init() {
        this.digestLength = ByteUtil.bitLength(this.messageDigest.getDigestLength());
        if (traceLog()) {
            log.trace("Hash Algorithm: {} with hashlen: {} bits", this.messageDigest.getAlgorithm(), Integer.valueOf(this.digestLength));
        }
    }

    public byte[] kdf(byte[] sharedSecret, int keydatalen, byte[] algorithmId, byte[] partyUInfo, byte[] partyVInfo, byte[] suppPubInfo, byte[] suppPrivInfo) {
        if (traceLog()) {
            StringBuilder msg = new StringBuilder();
            msg.append("KDF:").append("\n");
            msg.append("  z: ").append(ByteUtil.toDebugString(sharedSecret)).append("\n");
            msg.append("  keydatalen: ").append(keydatalen);
            msg.append("  algorithmId: ").append(ByteUtil.toDebugString(algorithmId)).append("\n");
            msg.append("  partyUInfo: ").append(ByteUtil.toDebugString(partyUInfo)).append("\n");
            msg.append("  partyVInfo: ").append(ByteUtil.toDebugString(partyVInfo)).append("\n");
            msg.append("  suppPubInfo: ").append(ByteUtil.toDebugString(suppPubInfo)).append("\n");
            msg.append("  suppPrivInfo: ").append(ByteUtil.toDebugString(suppPrivInfo));
            log.trace(msg.toString());
        }
        byte[] otherInfo = ByteUtil.concat(algorithmId, partyUInfo, partyVInfo, suppPubInfo, suppPrivInfo);
        return kdf(sharedSecret, keydatalen, otherInfo);
    }

    @Override // org.jose4j.jwe.kdf.ConcatenationKeyDerivationFunctionWithSha256
    public byte[] kdf(byte[] sharedSecret, int keydatalen, byte[] otherInfo) {
        long reps = getReps(keydatalen);
        if (traceLog()) {
            log.trace("reps: {}", String.valueOf(reps));
            log.trace("otherInfo: {}", ByteUtil.toDebugString(otherInfo));
        }
        ByteArrayOutputStream derivedByteOutputStream = new ByteArrayOutputStream();
        for (int i = 1; i <= reps; i++) {
            byte[] counterBytes = ByteUtil.getBytes(i);
            if (traceLog()) {
                log.trace("rep {} hashing ", Integer.valueOf(i));
                log.trace(" counter: {}", ByteUtil.toDebugString(counterBytes));
                log.trace(" z: {}", ByteUtil.toDebugString(sharedSecret));
                log.trace(" otherInfo: {}", ByteUtil.toDebugString(otherInfo));
            }
            this.messageDigest.update(counterBytes);
            this.messageDigest.update(sharedSecret);
            this.messageDigest.update(otherInfo);
            byte[] digest = this.messageDigest.digest();
            if (traceLog()) {
                log.trace(" k({}): {}", Integer.valueOf(i), ByteUtil.toDebugString(digest));
            }
            derivedByteOutputStream.write(digest, 0, digest.length);
        }
        int keyDateLenInBytes = ByteUtil.byteLength(keydatalen);
        byte[] derivedKeyMaterial = derivedByteOutputStream.toByteArray();
        if (traceLog()) {
            log.trace("derived key material: {}", ByteUtil.toDebugString(derivedKeyMaterial));
        }
        if (derivedKeyMaterial.length != keyDateLenInBytes) {
            byte[] newKeyMaterial = ByteUtil.subArray(derivedKeyMaterial, 0, keyDateLenInBytes);
            if (traceLog()) {
                log.trace("first {} bits of derived key material: {}", Integer.valueOf(keydatalen), ByteUtil.toDebugString(newKeyMaterial));
            }
            derivedKeyMaterial = newKeyMaterial;
        }
        if (traceLog()) {
            log.trace("final derived key material: {}", ByteUtil.toDebugString(derivedKeyMaterial));
        }
        return derivedKeyMaterial;
    }

    long getReps(int keydatalen) {
        double repsD = keydatalen / this.digestLength;
        return (int) Math.ceil(repsD);
    }

    private boolean traceLog() {
        return false;
    }
}
