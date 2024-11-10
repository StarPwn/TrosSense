package org.jose4j.jwe.kdf;

import org.jose4j.base64url.Base64Url;
import org.jose4j.lang.ByteUtil;
import org.jose4j.lang.StringUtil;

/* loaded from: classes5.dex */
public class KdfUtil {
    private Base64Url base64Url;
    private ConcatenationKeyDerivationFunctionWithSha256 kdf;

    public KdfUtil() {
        this(null);
    }

    public KdfUtil(String provider) {
        this.base64Url = new Base64Url();
        this.kdf = ConcatKeyDerivationFunctionFactory.make(provider);
    }

    public byte[] kdf(byte[] sharedSecret, int keydatalen, String algorithmId, String partyUInfo, String partyVInfo) {
        byte[] algorithmIdBytes = prependDatalen(StringUtil.getBytesUtf8(algorithmId));
        byte[] partyUInfoBytes = getDatalenDataFormat(partyUInfo);
        byte[] partyVInfoBytes = getDatalenDataFormat(partyVInfo);
        byte[] suppPubInfo = ByteUtil.getBytes(keydatalen);
        byte[] suppPrivInfo = ByteUtil.EMPTY_BYTES;
        byte[] otherInfo = ByteUtil.concat(algorithmIdBytes, partyUInfoBytes, partyVInfoBytes, suppPubInfo, suppPrivInfo);
        return this.kdf.kdf(sharedSecret, keydatalen, otherInfo);
    }

    byte[] prependDatalen(byte[] data) {
        if (data == null) {
            data = ByteUtil.EMPTY_BYTES;
        }
        byte[] datalen = ByteUtil.getBytes(data.length);
        return ByteUtil.concat(datalen, data);
    }

    byte[] getDatalenDataFormat(String encodedValue) {
        byte[] data = this.base64Url.base64UrlDecode(encodedValue);
        return prependDatalen(data);
    }
}
