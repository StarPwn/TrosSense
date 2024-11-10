package org.jose4j.jwt.consumer;

import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jose4j.jca.ProviderContext;
import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jwt.NumericDate;
import org.jose4j.keys.resolvers.DecryptionKeyResolver;
import org.jose4j.keys.resolvers.VerificationKeyResolver;

/* loaded from: classes5.dex */
public class JwtConsumerBuilder {
    private AudValidator audValidator;
    private String expectedSubject;
    private IssValidator issValidator;
    private AlgorithmConstraints jweAlgorithmConstraints;
    private AlgorithmConstraints jweContentEncryptionAlgorithmConstraints;
    private JweCustomizer jweCustomizer;
    private ProviderContext jweProviderContext;
    private AlgorithmConstraints jwsAlgorithmConstraints;
    private JwsCustomizer jwsCustomizer;
    private ProviderContext jwsProviderContext;
    private boolean liberalContentTypeHandling;
    private boolean relaxDecryptionKeyValidation;
    private boolean relaxVerificationKeyValidation;
    private boolean requireEncryption;
    private boolean requireIntegrity;
    private boolean requireJti;
    private boolean requireSubject;
    private boolean skipDefaultAudienceValidation;
    private boolean skipVerificationKeyResolutionOnNone;
    private TypeValidator typeValidator;
    private VerificationKeyResolver verificationKeyResolver = new SimpleKeyResolver(null);
    private DecryptionKeyResolver decryptionKeyResolver = new SimpleKeyResolver(null);
    private NumericDateValidator dateClaimsValidator = new NumericDateValidator();
    private List<ErrorCodeValidator> customValidators = new ArrayList();
    private boolean requireSignature = true;
    private boolean skipSignatureVerification = false;
    private boolean skipAllValidators = false;
    private boolean skipAllDefaultValidators = false;

    public JwtConsumerBuilder setEnableRequireEncryption() {
        this.requireEncryption = true;
        return this;
    }

    public JwtConsumerBuilder setEnableRequireIntegrity() {
        this.requireIntegrity = true;
        return this;
    }

    public JwtConsumerBuilder setDisableRequireSignature() {
        this.requireSignature = false;
        return this;
    }

    public JwtConsumerBuilder setEnableLiberalContentTypeHandling() {
        this.liberalContentTypeHandling = true;
        return this;
    }

    public JwtConsumerBuilder setSkipSignatureVerification() {
        this.skipSignatureVerification = true;
        return this;
    }

    public JwtConsumerBuilder setSkipAllValidators() {
        this.skipAllValidators = true;
        return this;
    }

    public JwtConsumerBuilder setSkipAllDefaultValidators() {
        this.skipAllDefaultValidators = true;
        return this;
    }

    public JwtConsumerBuilder setJwsAlgorithmConstraints(AlgorithmConstraints constraints) {
        this.jwsAlgorithmConstraints = constraints;
        return this;
    }

    public JwtConsumerBuilder setJweAlgorithmConstraints(AlgorithmConstraints constraints) {
        this.jweAlgorithmConstraints = constraints;
        return this;
    }

    public JwtConsumerBuilder setJweContentEncryptionAlgorithmConstraints(AlgorithmConstraints constraints) {
        this.jweContentEncryptionAlgorithmConstraints = constraints;
        return this;
    }

    public JwtConsumerBuilder setJwsAlgorithmConstraints(AlgorithmConstraints.ConstraintType type, String... algorithms) {
        this.jwsAlgorithmConstraints = new AlgorithmConstraints(type, algorithms);
        return this;
    }

    public JwtConsumerBuilder setJweAlgorithmConstraints(AlgorithmConstraints.ConstraintType type, String... algorithms) {
        this.jweAlgorithmConstraints = new AlgorithmConstraints(type, algorithms);
        return this;
    }

    public JwtConsumerBuilder setJweContentEncryptionAlgorithmConstraints(AlgorithmConstraints.ConstraintType type, String... algorithms) {
        this.jweContentEncryptionAlgorithmConstraints = new AlgorithmConstraints(type, algorithms);
        return this;
    }

    public JwtConsumerBuilder setVerificationKey(Key verificationKey) {
        return setVerificationKeyResolver(new SimpleKeyResolver(verificationKey));
    }

    public JwtConsumerBuilder setVerificationKeyResolver(VerificationKeyResolver verificationKeyResolver) {
        this.verificationKeyResolver = verificationKeyResolver;
        return this;
    }

    public JwtConsumerBuilder setSkipVerificationKeyResolutionOnNone() {
        this.skipVerificationKeyResolutionOnNone = true;
        return this;
    }

    public JwtConsumerBuilder setDecryptionKey(Key decryptionKey) {
        return setDecryptionKeyResolver(new SimpleKeyResolver(decryptionKey));
    }

    public JwtConsumerBuilder setDecryptionKeyResolver(DecryptionKeyResolver decryptionKeyResolver) {
        this.decryptionKeyResolver = decryptionKeyResolver;
        return this;
    }

    public JwtConsumerBuilder setExpectedAudience(String... audience) {
        return setExpectedAudience(true, audience);
    }

    public JwtConsumerBuilder setExpectedAudience(boolean requireAudienceClaim, String... audience) {
        Set<String> acceptableAudiences = new HashSet<>(Arrays.asList(audience));
        this.audValidator = new AudValidator(acceptableAudiences, requireAudienceClaim);
        return this;
    }

    public JwtConsumerBuilder setSkipDefaultAudienceValidation() {
        this.skipDefaultAudienceValidation = true;
        return this;
    }

    public JwtConsumerBuilder setExpectedIssuers(boolean requireIssuer, String... expectedIssuers) {
        this.issValidator = new IssValidator(requireIssuer, expectedIssuers);
        return this;
    }

    public JwtConsumerBuilder setExpectedIssuer(boolean requireIssuer, String expectedIssuer) {
        this.issValidator = new IssValidator(expectedIssuer, requireIssuer);
        return this;
    }

    public JwtConsumerBuilder setExpectedIssuer(String expectedIssuer) {
        return setExpectedIssuer(true, expectedIssuer);
    }

    public JwtConsumerBuilder setRequireSubject() {
        this.requireSubject = true;
        return this;
    }

    public JwtConsumerBuilder setExpectedSubject(String subject) {
        this.expectedSubject = subject;
        return setRequireSubject();
    }

    public JwtConsumerBuilder setRequireJwtId() {
        this.requireJti = true;
        return this;
    }

    public JwtConsumerBuilder setRequireExpirationTime() {
        this.dateClaimsValidator.setRequireExp(true);
        return this;
    }

    public JwtConsumerBuilder setRequireIssuedAt() {
        this.dateClaimsValidator.setRequireIat(true);
        return this;
    }

    public JwtConsumerBuilder setIssuedAtRestrictions(int allowedSecondsInTheFuture, int allowedSecondsInThePast) {
        this.dateClaimsValidator.setIatAllowedSecondsInTheFuture(allowedSecondsInTheFuture);
        this.dateClaimsValidator.setIatAllowedSecondsInThePast(allowedSecondsInThePast);
        return this;
    }

    public JwtConsumerBuilder setRequireNotBefore() {
        this.dateClaimsValidator.setRequireNbf(true);
        return this;
    }

    public JwtConsumerBuilder setEvaluationTime(NumericDate evaluationTime) {
        this.dateClaimsValidator.setEvaluationTime(evaluationTime);
        return this;
    }

    public JwtConsumerBuilder setAllowedClockSkewInSeconds(int secondsOfAllowedClockSkew) {
        this.dateClaimsValidator.setAllowedClockSkewSeconds(secondsOfAllowedClockSkew);
        return this;
    }

    public JwtConsumerBuilder setMaxFutureValidityInMinutes(int maxFutureValidityInMinutes) {
        this.dateClaimsValidator.setMaxFutureValidityInMinutes(maxFutureValidityInMinutes);
        return this;
    }

    public JwtConsumerBuilder setRelaxVerificationKeyValidation() {
        this.relaxVerificationKeyValidation = true;
        return this;
    }

    public JwtConsumerBuilder setRelaxDecryptionKeyValidation() {
        this.relaxDecryptionKeyValidation = true;
        return this;
    }

    public JwtConsumerBuilder registerValidator(Validator validator) {
        this.customValidators.add(new ErrorCodeValidatorAdapter(validator));
        return this;
    }

    public JwtConsumerBuilder registerValidator(ErrorCodeValidator validator) {
        this.customValidators.add(validator);
        return this;
    }

    public JwtConsumerBuilder setJwsCustomizer(JwsCustomizer jwsCustomizer) {
        this.jwsCustomizer = jwsCustomizer;
        return this;
    }

    public JwtConsumerBuilder setJweCustomizer(JweCustomizer jweCustomizer) {
        this.jweCustomizer = jweCustomizer;
        return this;
    }

    public JwtConsumerBuilder setJwsProviderContext(ProviderContext jwsProviderContext) {
        this.jwsProviderContext = jwsProviderContext;
        return this;
    }

    public JwtConsumerBuilder setJweProviderContext(ProviderContext jweProviderContext) {
        this.jweProviderContext = jweProviderContext;
        return this;
    }

    public JwtConsumerBuilder setExpectedType(boolean requireType, String expectedType) {
        this.typeValidator = new TypeValidator(requireType, expectedType);
        return this;
    }

    public JwtConsumer build() {
        List<ErrorCodeValidator> validators = new ArrayList<>();
        if (!this.skipAllValidators) {
            if (!this.skipAllDefaultValidators) {
                if (!this.skipDefaultAudienceValidation) {
                    if (this.audValidator == null) {
                        this.audValidator = new AudValidator(Collections.emptySet(), false);
                    }
                    validators.add(this.audValidator);
                }
                if (this.issValidator == null) {
                    this.issValidator = new IssValidator((String) null, false);
                }
                validators.add(this.issValidator);
                validators.add(this.dateClaimsValidator);
                SubValidator subValidator = this.expectedSubject == null ? new SubValidator(this.requireSubject) : new SubValidator(this.expectedSubject);
                validators.add(subValidator);
                validators.add(new JtiValidator(this.requireJti));
                if (this.typeValidator != null) {
                    validators.add(this.typeValidator);
                }
            }
            validators.addAll(this.customValidators);
        }
        JwtConsumer jwtConsumer = new JwtConsumer();
        jwtConsumer.setValidators(validators);
        jwtConsumer.setVerificationKeyResolver(this.verificationKeyResolver);
        jwtConsumer.setDecryptionKeyResolver(this.decryptionKeyResolver);
        jwtConsumer.setJwsAlgorithmConstraints(this.jwsAlgorithmConstraints);
        jwtConsumer.setJweAlgorithmConstraints(this.jweAlgorithmConstraints);
        jwtConsumer.setJweContentEncryptionAlgorithmConstraints(this.jweContentEncryptionAlgorithmConstraints);
        jwtConsumer.setRequireSignature(this.requireSignature);
        jwtConsumer.setRequireEncryption(this.requireEncryption);
        jwtConsumer.setRequireIntegrity(this.requireIntegrity);
        jwtConsumer.setLiberalContentTypeHandling(this.liberalContentTypeHandling);
        jwtConsumer.setSkipSignatureVerification(this.skipSignatureVerification);
        jwtConsumer.setSkipVerificationKeyResolutionOnNone(this.skipVerificationKeyResolutionOnNone);
        jwtConsumer.setRelaxVerificationKeyValidation(this.relaxVerificationKeyValidation);
        jwtConsumer.setRelaxDecryptionKeyValidation(this.relaxDecryptionKeyValidation);
        jwtConsumer.setJwsCustomizer(this.jwsCustomizer);
        jwtConsumer.setJweCustomizer(this.jweCustomizer);
        jwtConsumer.setJwsProviderContext(this.jwsProviderContext);
        jwtConsumer.setJweProviderContext(this.jweProviderContext);
        return jwtConsumer;
    }
}
