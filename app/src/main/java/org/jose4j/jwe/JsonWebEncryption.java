package org.jose4j.jwe;

import java.security.Key;
import org.jose4j.base64url.Base64Url;
import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jwa.AlgorithmFactory;
import org.jose4j.jwa.AlgorithmFactoryFactory;
import org.jose4j.jwa.CryptoPrimitive;
import org.jose4j.jwx.CompactSerializer;
import org.jose4j.jwx.HeaderParameterNames;
import org.jose4j.jwx.Headers;
import org.jose4j.jwx.JsonWebStructure;
import org.jose4j.lang.ByteUtil;
import org.jose4j.lang.InvalidAlgorithmException;
import org.jose4j.lang.InvalidKeyException;
import org.jose4j.lang.JoseException;
import org.jose4j.lang.StringUtil;
import org.jose4j.zip.CompressionAlgorithm;
import org.jose4j.zip.CompressionAlgorithmIdentifiers;

/* loaded from: classes5.dex */
public class JsonWebEncryption extends JsonWebStructure {
    private static final AlgorithmConstraints BLOCK_RSA1_5 = new AlgorithmConstraints(AlgorithmConstraints.ConstraintType.BLOCK, KeyManagementAlgorithmIdentifiers.RSA1_5);
    public static final short COMPACT_SERIALIZATION_PARTS = 5;
    byte[] ciphertext;
    byte[] contentEncryptionKey;
    private CryptoPrimitive decryptingPrimitive;
    byte[] encryptedKey;
    byte[] iv;
    private byte[] plaintext;
    private Base64Url base64url = new Base64Url();
    private String plaintextCharEncoding = StringUtil.UTF_8;
    private AlgorithmConstraints contentEncryptionAlgorithmConstraints = AlgorithmConstraints.NO_CONSTRAINTS;

    public JsonWebEncryption() {
        setAlgorithmConstraints(BLOCK_RSA1_5);
    }

    public void setPlainTextCharEncoding(String plaintextCharEncoding) {
        this.plaintextCharEncoding = plaintextCharEncoding;
    }

    public void setPlaintext(byte[] plaintext) {
        this.plaintext = plaintext;
    }

    public void setPlaintext(String plaintext) {
        this.plaintext = StringUtil.getBytesUnchecked(plaintext, this.plaintextCharEncoding);
    }

    public String getPlaintextString() throws JoseException {
        return StringUtil.newString(getPlaintextBytes(), this.plaintextCharEncoding);
    }

    public byte[] getPlaintextBytes() throws JoseException {
        if (this.plaintext == null) {
            decrypt();
        }
        return this.plaintext;
    }

    @Override // org.jose4j.jwx.JsonWebStructure
    public String getPayload() throws JoseException {
        return getPlaintextString();
    }

    @Override // org.jose4j.jwx.JsonWebStructure
    public void setPayload(String payload) {
        setPlaintext(payload);
    }

    public void setEncryptionMethodHeaderParameter(String enc) {
        setHeader("enc", enc);
    }

    public String getEncryptionMethodHeaderParameter() {
        return getHeader("enc");
    }

    public void setCompressionAlgorithmHeaderParameter(String zip) {
        setHeader(HeaderParameterNames.ZIP, zip);
    }

    public String getCompressionAlgorithmHeaderParameter() {
        return getHeader(HeaderParameterNames.ZIP);
    }

    public void enableDefaultCompression() {
        setCompressionAlgorithmHeaderParameter(CompressionAlgorithmIdentifiers.DEFLATE);
    }

    public void setContentEncryptionAlgorithmConstraints(AlgorithmConstraints contentEncryptionAlgorithmConstraints) {
        this.contentEncryptionAlgorithmConstraints = contentEncryptionAlgorithmConstraints;
    }

    public ContentEncryptionAlgorithm getContentEncryptionAlgorithm() throws InvalidAlgorithmException {
        String encValue = getEncryptionMethodHeaderParameter();
        if (encValue == null) {
            throw new InvalidAlgorithmException("Content encryption header (enc) not set.");
        }
        this.contentEncryptionAlgorithmConstraints.checkConstraint(encValue);
        AlgorithmFactoryFactory factoryFactory = AlgorithmFactoryFactory.getInstance();
        AlgorithmFactory<ContentEncryptionAlgorithm> factory = factoryFactory.getJweContentEncryptionAlgorithmFactory();
        return factory.getAlgorithm(encValue);
    }

    public KeyManagementAlgorithm getKeyManagementModeAlgorithm() throws InvalidAlgorithmException {
        return getKeyManagementModeAlgorithm(true);
    }

    KeyManagementAlgorithm getKeyManagementModeAlgorithm(boolean checkConstraints) throws InvalidAlgorithmException {
        String algo = getAlgorithmHeaderValue();
        if (algo == null) {
            throw new InvalidAlgorithmException("Encryption key management algorithm header (alg) not set.");
        }
        if (checkConstraints) {
            getAlgorithmConstraints().checkConstraint(algo);
        }
        AlgorithmFactoryFactory factoryFactory = AlgorithmFactoryFactory.getInstance();
        AlgorithmFactory<KeyManagementAlgorithm> factory = factoryFactory.getJweKeyManagementAlgorithmFactory();
        return factory.getAlgorithm(algo);
    }

    @Override // org.jose4j.jwx.JsonWebStructure
    public KeyManagementAlgorithm getAlgorithmNoConstraintCheck() throws InvalidAlgorithmException {
        return getKeyManagementModeAlgorithm(false);
    }

    @Override // org.jose4j.jwx.JsonWebStructure
    public KeyManagementAlgorithm getAlgorithm() throws InvalidAlgorithmException {
        return getKeyManagementModeAlgorithm();
    }

    @Override // org.jose4j.jwx.JsonWebStructure
    protected void setCompactSerializationParts(String[] parts) throws JoseException {
        if (parts.length != 5) {
            throw new JoseException("A JWE Compact Serialization must have exactly 5 parts separated by period ('.') characters");
        }
        setEncodedHeader(parts[0]);
        this.encryptedKey = this.base64url.base64UrlDecode(parts[1]);
        setEncodedIv(parts[2]);
        String encodedCiphertext = parts[3];
        checkNotEmptyPart(encodedCiphertext, "Encoded JWE Ciphertext");
        this.ciphertext = this.base64url.base64UrlDecode(encodedCiphertext);
        String encodedAuthenticationTag = parts[4];
        checkNotEmptyPart(encodedAuthenticationTag, "Encoded JWE Authentication Tag");
        byte[] tag = this.base64url.base64UrlDecode(encodedAuthenticationTag);
        setIntegrity(tag);
    }

    public CryptoPrimitive prepareDecryptingPrimitive() throws JoseException {
        this.decryptingPrimitive = createDecryptingPrimitive();
        return this.decryptingPrimitive;
    }

    private CryptoPrimitive createDecryptingPrimitive() throws JoseException {
        KeyManagementAlgorithm keyManagementModeAlg = getKeyManagementModeAlgorithm();
        Key managmentKey = getKey();
        if (isDoKeyValidation()) {
            ContentEncryptionAlgorithm contentEncryptionAlg = getContentEncryptionAlgorithm();
            keyManagementModeAlg.validateDecryptionKey(managmentKey, contentEncryptionAlg);
        }
        return keyManagementModeAlg.prepareForDecrypt(managmentKey, this.headers, getProviderCtx());
    }

    private void decrypt() throws JoseException {
        KeyManagementAlgorithm keyManagementModeAlg = getKeyManagementModeAlgorithm();
        ContentEncryptionAlgorithm contentEncryptionAlg = getContentEncryptionAlgorithm();
        ContentEncryptionKeyDescriptor contentEncryptionKeyDesc = contentEncryptionAlg.getContentEncryptionKeyDescriptor();
        checkCrit();
        CryptoPrimitive cryptoPrimitive = this.decryptingPrimitive == null ? createDecryptingPrimitive() : this.decryptingPrimitive;
        Key cek = keyManagementModeAlg.manageForDecrypt(cryptoPrimitive, getEncryptedKey(), contentEncryptionKeyDesc, getHeaders(), getProviderCtx());
        ContentEncryptionParts contentEncryptionParts = new ContentEncryptionParts(this.iv, this.ciphertext, getIntegrity());
        byte[] aad = getEncodedHeaderAsciiBytesForAdditionalAuthenticatedData();
        byte[] rawCek = cek.getEncoded();
        checkCek(contentEncryptionAlg, contentEncryptionKeyDesc, rawCek);
        byte[] decrypted = contentEncryptionAlg.decrypt(contentEncryptionParts, aad, rawCek, getHeaders(), getProviderCtx());
        setPlaintext(decompress(getHeaders(), decrypted));
    }

    private void checkCek(ContentEncryptionAlgorithm contentEncryptionAlg, ContentEncryptionKeyDescriptor contentEncryptionKeyDesc, byte[] rawCek) throws InvalidKeyException {
        int contentEncryptionKeyByteLength = contentEncryptionKeyDesc.getContentEncryptionKeyByteLength();
        if (rawCek.length != contentEncryptionKeyByteLength) {
            throw new InvalidKeyException(ByteUtil.bitLength(rawCek) + " bit content encryption key is not the correct size for the " + contentEncryptionAlg.getAlgorithmIdentifier() + " content encryption algorithm (" + ByteUtil.bitLength(contentEncryptionKeyByteLength) + ").");
        }
    }

    public byte[] getEncryptedKey() {
        return this.encryptedKey;
    }

    byte[] getEncodedHeaderAsciiBytesForAdditionalAuthenticatedData() {
        String encodedHeader = getEncodedHeader();
        return StringUtil.getBytesAscii(encodedHeader);
    }

    byte[] decompress(Headers headers, byte[] data) throws JoseException {
        String zipHeaderValue = headers.getStringHeaderValue(HeaderParameterNames.ZIP);
        if (zipHeaderValue != null) {
            AlgorithmFactoryFactory factoryFactory = AlgorithmFactoryFactory.getInstance();
            AlgorithmFactory<CompressionAlgorithm> zipAlgFactory = factoryFactory.getCompressionAlgorithmFactory();
            CompressionAlgorithm compressionAlgorithm = zipAlgFactory.getAlgorithm(zipHeaderValue);
            return compressionAlgorithm.decompress(data);
        }
        return data;
    }

    byte[] compress(Headers headers, byte[] data) throws InvalidAlgorithmException {
        String zipHeaderValue = headers.getStringHeaderValue(HeaderParameterNames.ZIP);
        if (zipHeaderValue != null) {
            AlgorithmFactoryFactory factoryFactory = AlgorithmFactoryFactory.getInstance();
            AlgorithmFactory<CompressionAlgorithm> zipAlgFactory = factoryFactory.getCompressionAlgorithmFactory();
            CompressionAlgorithm compressionAlgorithm = zipAlgFactory.getAlgorithm(zipHeaderValue);
            return compressionAlgorithm.compress(data);
        }
        return data;
    }

    @Override // org.jose4j.jwx.JsonWebStructure
    public String getCompactSerialization() throws JoseException {
        KeyManagementAlgorithm keyManagementModeAlg = getKeyManagementModeAlgorithm();
        ContentEncryptionAlgorithm contentEncryptionAlg = getContentEncryptionAlgorithm();
        ContentEncryptionKeyDescriptor contentEncryptionKeyDesc = contentEncryptionAlg.getContentEncryptionKeyDescriptor();
        Key managementKey = getKey();
        if (isDoKeyValidation()) {
            keyManagementModeAlg.validateEncryptionKey(getKey(), contentEncryptionAlg);
        }
        ContentEncryptionKeys contentEncryptionKeys = keyManagementModeAlg.manageForEncrypt(managementKey, contentEncryptionKeyDesc, getHeaders(), this.contentEncryptionKey, getProviderCtx());
        setContentEncryptionKey(contentEncryptionKeys.getContentEncryptionKey());
        this.encryptedKey = contentEncryptionKeys.getEncryptedKey();
        byte[] aad = getEncodedHeaderAsciiBytesForAdditionalAuthenticatedData();
        byte[] contentEncryptionKey = contentEncryptionKeys.getContentEncryptionKey();
        byte[] plaintextBytes = this.plaintext;
        if (plaintextBytes != null) {
            byte[] plaintextBytes2 = compress(getHeaders(), plaintextBytes);
            checkCek(contentEncryptionAlg, contentEncryptionKeyDesc, contentEncryptionKey);
            ContentEncryptionParts contentEncryptionParts = contentEncryptionAlg.encrypt(plaintextBytes2, aad, contentEncryptionKey, getHeaders(), getIv(), getProviderCtx());
            setIv(contentEncryptionParts.getIv());
            this.ciphertext = contentEncryptionParts.getCiphertext();
            String encodedIv = this.base64url.base64UrlEncode(contentEncryptionParts.getIv());
            String encodedCiphertext = this.base64url.base64UrlEncode(contentEncryptionParts.getCiphertext());
            String encodedTag = this.base64url.base64UrlEncode(contentEncryptionParts.getAuthenticationTag());
            byte[] encryptedKey = contentEncryptionKeys.getEncryptedKey();
            String encodedEncryptedKey = this.base64url.base64UrlEncode(encryptedKey);
            return CompactSerializer.serialize(getEncodedHeader(), encodedEncryptedKey, encodedIv, encodedCiphertext, encodedTag);
        }
        throw new NullPointerException("The plaintext payload for the JWE has not been set.");
    }

    public byte[] getContentEncryptionKey() {
        return this.contentEncryptionKey;
    }

    public void setContentEncryptionKey(byte[] contentEncryptionKey) {
        this.contentEncryptionKey = contentEncryptionKey;
    }

    public void setEncodedContentEncryptionKey(String encodedContentEncryptionKey) {
        setContentEncryptionKey(Base64Url.decode(encodedContentEncryptionKey));
    }

    public byte[] getIv() {
        return this.iv;
    }

    public void setIv(byte[] iv) {
        this.iv = iv;
    }

    public void setEncodedIv(String encodedIv) {
        setIv(this.base64url.base64UrlDecode(encodedIv));
    }
}
