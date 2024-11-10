package org.jose4j.jwa;

import java.security.Security;
import java.util.Arrays;
import org.jose4j.jwe.AesCbcHmacSha2ContentEncryptionAlgorithm;
import org.jose4j.jwe.AesGcmContentEncryptionAlgorithm;
import org.jose4j.jwe.AesGcmKeyEncryptionAlgorithm;
import org.jose4j.jwe.AesKeyWrapManagementAlgorithm;
import org.jose4j.jwe.ContentEncryptionAlgorithm;
import org.jose4j.jwe.DirectKeyManagementAlgorithm;
import org.jose4j.jwe.EcdhKeyAgreementAlgorithm;
import org.jose4j.jwe.EcdhKeyAgreementWithAesKeyWrapAlgorithm;
import org.jose4j.jwe.KeyManagementAlgorithm;
import org.jose4j.jwe.Pbes2HmacShaWithAesKeyWrapAlgorithm;
import org.jose4j.jwe.RsaKeyManagementAlgorithm;
import org.jose4j.jws.EcdsaUsingShaAlgorithm;
import org.jose4j.jws.EdDsaAlgorithm;
import org.jose4j.jws.HmacUsingShaAlgorithm;
import org.jose4j.jws.JsonWebSignatureAlgorithm;
import org.jose4j.jws.RsaUsingShaAlgorithm;
import org.jose4j.jws.UnsecuredNoneAlgorithm;
import org.jose4j.jwx.HeaderParameterNames;
import org.jose4j.zip.CompressionAlgorithm;
import org.jose4j.zip.DeflateRFC1951CompressionAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: classes5.dex */
public class AlgorithmFactoryFactory {
    private AlgorithmFactory<CompressionAlgorithm> compressionAlgorithmFactory;
    private AlgorithmFactory<ContentEncryptionAlgorithm> jweContentEncryptionAlgorithmFactory;
    private AlgorithmFactory<KeyManagementAlgorithm> jweKeyMgmtModeAlgorithmFactory;
    private AlgorithmFactory<JsonWebSignatureAlgorithm> jwsAlgorithmFactory;
    private static final Logger log = LoggerFactory.getLogger((Class<?>) AlgorithmFactoryFactory.class);
    private static final AlgorithmFactoryFactory factoryFactory = new AlgorithmFactoryFactory();

    private AlgorithmFactoryFactory() {
        initialize();
    }

    void reinitialize() {
        log.debug("Reinitializing jose4j...");
        initialize();
    }

    private void initialize() {
        String version = System.getProperty("java.version");
        String vendor = System.getProperty("java.vendor");
        String home = System.getProperty("java.home");
        String providers = Arrays.toString(Security.getProviders());
        log.debug("Initializing jose4j (running with Java {} from {} at {} with {} security providers installed)...", version, vendor, home, providers);
        long startTime = System.currentTimeMillis();
        this.jwsAlgorithmFactory = new AlgorithmFactory<>("alg", JsonWebSignatureAlgorithm.class);
        this.jwsAlgorithmFactory.registerAlgorithm(new UnsecuredNoneAlgorithm());
        this.jwsAlgorithmFactory.registerAlgorithm(new HmacUsingShaAlgorithm.HmacSha256());
        this.jwsAlgorithmFactory.registerAlgorithm(new HmacUsingShaAlgorithm.HmacSha384());
        this.jwsAlgorithmFactory.registerAlgorithm(new HmacUsingShaAlgorithm.HmacSha512());
        this.jwsAlgorithmFactory.registerAlgorithm(new EdDsaAlgorithm());
        this.jwsAlgorithmFactory.registerAlgorithm(new EcdsaUsingShaAlgorithm.EcdsaP256UsingSha256());
        this.jwsAlgorithmFactory.registerAlgorithm(new EcdsaUsingShaAlgorithm.EcdsaP384UsingSha384());
        this.jwsAlgorithmFactory.registerAlgorithm(new EcdsaUsingShaAlgorithm.EcdsaP521UsingSha512());
        this.jwsAlgorithmFactory.registerAlgorithm(new EcdsaUsingShaAlgorithm.EcdsaSECP256K1UsingSha256());
        this.jwsAlgorithmFactory.registerAlgorithm(new RsaUsingShaAlgorithm.RsaSha256());
        this.jwsAlgorithmFactory.registerAlgorithm(new RsaUsingShaAlgorithm.RsaSha384());
        this.jwsAlgorithmFactory.registerAlgorithm(new RsaUsingShaAlgorithm.RsaSha512());
        this.jwsAlgorithmFactory.registerAlgorithm(new RsaUsingShaAlgorithm.RsaPssSha256());
        this.jwsAlgorithmFactory.registerAlgorithm(new RsaUsingShaAlgorithm.RsaPssSha384());
        this.jwsAlgorithmFactory.registerAlgorithm(new RsaUsingShaAlgorithm.RsaPssSha512());
        log.debug("JWS signature algorithms: {}", this.jwsAlgorithmFactory.getSupportedAlgorithms());
        this.jweKeyMgmtModeAlgorithmFactory = new AlgorithmFactory<>("alg", KeyManagementAlgorithm.class);
        this.jweKeyMgmtModeAlgorithmFactory.registerAlgorithm(new RsaKeyManagementAlgorithm.Rsa1_5());
        this.jweKeyMgmtModeAlgorithmFactory.registerAlgorithm(new RsaKeyManagementAlgorithm.RsaOaep());
        this.jweKeyMgmtModeAlgorithmFactory.registerAlgorithm(new RsaKeyManagementAlgorithm.RsaOaep256());
        this.jweKeyMgmtModeAlgorithmFactory.registerAlgorithm(new DirectKeyManagementAlgorithm());
        this.jweKeyMgmtModeAlgorithmFactory.registerAlgorithm(new AesKeyWrapManagementAlgorithm.Aes128());
        this.jweKeyMgmtModeAlgorithmFactory.registerAlgorithm(new AesKeyWrapManagementAlgorithm.Aes192());
        this.jweKeyMgmtModeAlgorithmFactory.registerAlgorithm(new AesKeyWrapManagementAlgorithm.Aes256());
        this.jweKeyMgmtModeAlgorithmFactory.registerAlgorithm(new EcdhKeyAgreementAlgorithm());
        this.jweKeyMgmtModeAlgorithmFactory.registerAlgorithm(new EcdhKeyAgreementWithAesKeyWrapAlgorithm.EcdhKeyAgreementWithAes128KeyWrapAlgorithm());
        this.jweKeyMgmtModeAlgorithmFactory.registerAlgorithm(new EcdhKeyAgreementWithAesKeyWrapAlgorithm.EcdhKeyAgreementWithAes192KeyWrapAlgorithm());
        this.jweKeyMgmtModeAlgorithmFactory.registerAlgorithm(new EcdhKeyAgreementWithAesKeyWrapAlgorithm.EcdhKeyAgreementWithAes256KeyWrapAlgorithm());
        this.jweKeyMgmtModeAlgorithmFactory.registerAlgorithm(new Pbes2HmacShaWithAesKeyWrapAlgorithm.HmacSha256Aes128());
        this.jweKeyMgmtModeAlgorithmFactory.registerAlgorithm(new Pbes2HmacShaWithAesKeyWrapAlgorithm.HmacSha384Aes192());
        this.jweKeyMgmtModeAlgorithmFactory.registerAlgorithm(new Pbes2HmacShaWithAesKeyWrapAlgorithm.HmacSha512Aes256());
        this.jweKeyMgmtModeAlgorithmFactory.registerAlgorithm(new AesGcmKeyEncryptionAlgorithm.Aes128Gcm());
        this.jweKeyMgmtModeAlgorithmFactory.registerAlgorithm(new AesGcmKeyEncryptionAlgorithm.Aes192Gcm());
        this.jweKeyMgmtModeAlgorithmFactory.registerAlgorithm(new AesGcmKeyEncryptionAlgorithm.Aes256Gcm());
        log.debug("JWE key management algorithms: {}", this.jweKeyMgmtModeAlgorithmFactory.getSupportedAlgorithms());
        this.jweContentEncryptionAlgorithmFactory = new AlgorithmFactory<>("enc", ContentEncryptionAlgorithm.class);
        this.jweContentEncryptionAlgorithmFactory.registerAlgorithm(new AesCbcHmacSha2ContentEncryptionAlgorithm.Aes128CbcHmacSha256());
        this.jweContentEncryptionAlgorithmFactory.registerAlgorithm(new AesCbcHmacSha2ContentEncryptionAlgorithm.Aes192CbcHmacSha384());
        this.jweContentEncryptionAlgorithmFactory.registerAlgorithm(new AesCbcHmacSha2ContentEncryptionAlgorithm.Aes256CbcHmacSha512());
        this.jweContentEncryptionAlgorithmFactory.registerAlgorithm(new AesGcmContentEncryptionAlgorithm.Aes128Gcm());
        this.jweContentEncryptionAlgorithmFactory.registerAlgorithm(new AesGcmContentEncryptionAlgorithm.Aes192Gcm());
        this.jweContentEncryptionAlgorithmFactory.registerAlgorithm(new AesGcmContentEncryptionAlgorithm.Aes256Gcm());
        log.debug("JWE content encryption algorithms: {}", this.jweContentEncryptionAlgorithmFactory.getSupportedAlgorithms());
        this.compressionAlgorithmFactory = new AlgorithmFactory<>(HeaderParameterNames.ZIP, CompressionAlgorithm.class);
        this.compressionAlgorithmFactory.registerAlgorithm(new DeflateRFC1951CompressionAlgorithm());
        log.debug("JWE compression algorithms: {}", this.compressionAlgorithmFactory.getSupportedAlgorithms());
        log.debug("Initialized jose4j in {}ms", Long.valueOf(System.currentTimeMillis() - startTime));
    }

    public static AlgorithmFactoryFactory getInstance() {
        return factoryFactory;
    }

    public AlgorithmFactory<JsonWebSignatureAlgorithm> getJwsAlgorithmFactory() {
        return this.jwsAlgorithmFactory;
    }

    public AlgorithmFactory<KeyManagementAlgorithm> getJweKeyManagementAlgorithmFactory() {
        return this.jweKeyMgmtModeAlgorithmFactory;
    }

    public AlgorithmFactory<ContentEncryptionAlgorithm> getJweContentEncryptionAlgorithmFactory() {
        return this.jweContentEncryptionAlgorithmFactory;
    }

    public AlgorithmFactory<CompressionAlgorithm> getCompressionAlgorithmFactory() {
        return this.compressionAlgorithmFactory;
    }
}
