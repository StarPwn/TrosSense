package org.jose4j.jwt.consumer;

import java.security.Key;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.jose4j.jca.ProviderContext;
import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.ErrorCodeValidator;
import org.jose4j.jwx.JsonWebStructure;
import org.jose4j.keys.KeyPersuasion;
import org.jose4j.keys.resolvers.DecryptionKeyResolver;
import org.jose4j.keys.resolvers.VerificationKeyResolver;
import org.jose4j.lang.ExceptionHelp;
import org.jose4j.lang.JoseException;

/* loaded from: classes5.dex */
public class JwtConsumer {
    private DecryptionKeyResolver decryptionKeyResolver;
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
    private boolean requireSignature = true;
    private boolean skipSignatureVerification;
    private boolean skipVerificationKeyResolutionOnNone;
    private List<ErrorCodeValidator> validators;
    private VerificationKeyResolver verificationKeyResolver;

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setJwsAlgorithmConstraints(AlgorithmConstraints constraints) {
        this.jwsAlgorithmConstraints = constraints;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setJweAlgorithmConstraints(AlgorithmConstraints constraints) {
        this.jweAlgorithmConstraints = constraints;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setJweContentEncryptionAlgorithmConstraints(AlgorithmConstraints constraints) {
        this.jweContentEncryptionAlgorithmConstraints = constraints;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setVerificationKeyResolver(VerificationKeyResolver verificationKeyResolver) {
        this.verificationKeyResolver = verificationKeyResolver;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setDecryptionKeyResolver(DecryptionKeyResolver decryptionKeyResolver) {
        this.decryptionKeyResolver = decryptionKeyResolver;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setValidators(List<ErrorCodeValidator> validators) {
        this.validators = validators;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setRequireSignature(boolean requireSignature) {
        this.requireSignature = requireSignature;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setRequireEncryption(boolean requireEncryption) {
        this.requireEncryption = requireEncryption;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setRequireIntegrity(boolean requireIntegrity) {
        this.requireIntegrity = requireIntegrity;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setLiberalContentTypeHandling(boolean liberalContentTypeHandling) {
        this.liberalContentTypeHandling = liberalContentTypeHandling;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setSkipSignatureVerification(boolean skipSignatureVerification) {
        this.skipSignatureVerification = skipSignatureVerification;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setRelaxVerificationKeyValidation(boolean relaxVerificationKeyValidation) {
        this.relaxVerificationKeyValidation = relaxVerificationKeyValidation;
    }

    public void setSkipVerificationKeyResolutionOnNone(boolean skipVerificationKeyResolutionOnNone) {
        this.skipVerificationKeyResolutionOnNone = skipVerificationKeyResolutionOnNone;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setRelaxDecryptionKeyValidation(boolean relaxDecryptionKeyValidation) {
        this.relaxDecryptionKeyValidation = relaxDecryptionKeyValidation;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setJwsProviderContext(ProviderContext jwsProviderContext) {
        this.jwsProviderContext = jwsProviderContext;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setJweProviderContext(ProviderContext jweProviderContext) {
        this.jweProviderContext = jweProviderContext;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setJwsCustomizer(JwsCustomizer jwsCustomizer) {
        this.jwsCustomizer = jwsCustomizer;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setJweCustomizer(JweCustomizer jweCustomizer) {
        this.jweCustomizer = jweCustomizer;
    }

    public JwtClaims processToClaims(String jwt) throws InvalidJwtException {
        return process(jwt).getJwtClaims();
    }

    public void processContext(JwtContext jwtContext) throws InvalidJwtException {
        ArrayList<JsonWebStructure> originalJoseObjects = new ArrayList<>(jwtContext.getJoseObjects());
        boolean hasSymmetricEncryption = false;
        boolean hasSymmetricEncryption2 = false;
        boolean hasSignature = false;
        for (int idx = originalJoseObjects.size() - 1; idx >= 0; idx--) {
            List<JsonWebStructure> joseObjects = originalJoseObjects.subList(idx + 1, originalJoseObjects.size());
            List<JsonWebStructure> nestingContext = Collections.unmodifiableList(joseObjects);
            JsonWebStructure currentJoseObject = originalJoseObjects.get(idx);
            try {
                if (currentJoseObject instanceof JsonWebSignature) {
                    JsonWebSignature jws = (JsonWebSignature) currentJoseObject;
                    boolean isNoneAlg = "none".equals(jws.getAlgorithmHeaderValue());
                    if (!this.skipSignatureVerification) {
                        if (this.jwsProviderContext != null) {
                            jws.setProviderContext(this.jwsProviderContext);
                        }
                        if (this.relaxVerificationKeyValidation) {
                            jws.setDoKeyValidation(false);
                        }
                        if (this.jwsAlgorithmConstraints != null) {
                            jws.setAlgorithmConstraints(this.jwsAlgorithmConstraints);
                        }
                        if (!isNoneAlg || !this.skipVerificationKeyResolutionOnNone) {
                            Key key = this.verificationKeyResolver.resolveKey(jws, nestingContext);
                            jws.setKey(key);
                        }
                        if (this.jwsCustomizer != null) {
                            this.jwsCustomizer.customize(jws, nestingContext);
                        }
                        if (!jws.verifySignature()) {
                            throw new InvalidJwtSignatureException(jws, jwtContext);
                        }
                    }
                    if (!isNoneAlg) {
                        hasSignature = true;
                    }
                } else {
                    JsonWebEncryption jwe = (JsonWebEncryption) currentJoseObject;
                    if (this.jweAlgorithmConstraints != null) {
                        this.jweAlgorithmConstraints.checkConstraint(jwe.getAlgorithmHeaderValue());
                    }
                    if (this.jweContentEncryptionAlgorithmConstraints != null) {
                        this.jweContentEncryptionAlgorithmConstraints.checkConstraint(jwe.getEncryptionMethodHeaderParameter());
                    }
                    hasSymmetricEncryption2 = true;
                    hasSymmetricEncryption = jwe.getKeyManagementModeAlgorithm().getKeyPersuasion() == KeyPersuasion.SYMMETRIC;
                }
            } catch (InvalidJwtException e) {
                throw e;
            } catch (JoseException e2) {
                StringBuilder sb = new StringBuilder();
                sb.append("Unable to process");
                if (!joseObjects.isEmpty()) {
                    sb.append(" nested");
                }
                sb.append(" JOSE object (cause: ").append(e2).append("): ").append(currentJoseObject);
                ErrorCodeValidator.Error error = new ErrorCodeValidator.Error(17, sb.toString());
                throw new InvalidJwtException("JWT processing failed.", error, e2, jwtContext);
            } catch (Exception e3) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Unexpected exception encountered while processing");
                if (!joseObjects.isEmpty()) {
                    sb2.append(" nested");
                }
                sb2.append(" JOSE object (").append(e3).append("): ").append(currentJoseObject);
                ErrorCodeValidator.Error error2 = new ErrorCodeValidator.Error(17, sb2.toString());
                throw new InvalidJwtException("JWT processing failed.", error2, e3, jwtContext);
            }
        }
        if (this.requireSignature && !hasSignature) {
            List<ErrorCodeValidator.Error> errors = Collections.singletonList(new ErrorCodeValidator.Error(10, "Missing signature."));
            throw new InvalidJwtException("The JWT has no signature but the JWT Consumer is configured to require one: " + jwtContext.getJwt(), errors, jwtContext);
        }
        if (this.requireEncryption && !hasSymmetricEncryption2) {
            List<ErrorCodeValidator.Error> errors2 = Collections.singletonList(new ErrorCodeValidator.Error(19, "No encryption."));
            throw new InvalidJwtException("The JWT has no encryption but the JWT Consumer is configured to require it: " + jwtContext.getJwt(), errors2, jwtContext);
        }
        if (this.requireIntegrity && !hasSignature && !hasSymmetricEncryption) {
            List<ErrorCodeValidator.Error> errors3 = Collections.singletonList(new ErrorCodeValidator.Error(20, "Missing Integrity Protection"));
            throw new InvalidJwtException("The JWT has no integrity protection (signature/MAC or symmetric AEAD encryption) but the JWT Consumer is configured to require it: " + jwtContext.getJwt(), errors3, jwtContext);
        }
        validate(jwtContext);
    }

    public JwtContext process(String jwt) throws InvalidJwtException {
        String payload;
        String workingJwt = jwt;
        JwtClaims jwtClaims = null;
        LinkedList<JsonWebStructure> joseObjects = new LinkedList<>();
        JwtContext jwtContext = new JwtContext(jwt, null, Collections.unmodifiableList(joseObjects));
        while (jwtClaims == null) {
            try {
                try {
                    try {
                        JsonWebStructure joseObject = JsonWebStructure.fromCompactSerialization(workingJwt);
                        if (joseObject instanceof JsonWebSignature) {
                            JsonWebSignature jws = (JsonWebSignature) joseObject;
                            payload = jws.getUnverifiedPayload();
                        } else {
                            JsonWebEncryption jwe = (JsonWebEncryption) joseObject;
                            if (this.jweProviderContext != null) {
                                jwe.setProviderContext(this.jweProviderContext);
                            }
                            if (this.relaxDecryptionKeyValidation) {
                                jwe.setDoKeyValidation(false);
                            }
                            if (this.jweContentEncryptionAlgorithmConstraints != null) {
                                jwe.setContentEncryptionAlgorithmConstraints(this.jweContentEncryptionAlgorithmConstraints);
                            }
                            List<JsonWebStructure> nestingContext = Collections.unmodifiableList(joseObjects);
                            Key key = this.decryptionKeyResolver.resolveKey(jwe, nestingContext);
                            jwe.setKey(key);
                            if (this.jweAlgorithmConstraints != null) {
                                jwe.setAlgorithmConstraints(this.jweAlgorithmConstraints);
                            }
                            if (this.jweCustomizer != null) {
                                this.jweCustomizer.customize(jwe, nestingContext);
                            }
                            payload = jwe.getPayload();
                        }
                        if (isNestedJwt(joseObject)) {
                            workingJwt = payload;
                        } else {
                            try {
                                jwtClaims = JwtClaims.parse(payload, jwtContext);
                                jwtContext.setJwtClaims(jwtClaims);
                            } catch (InvalidJwtException ije) {
                                if (!this.liberalContentTypeHandling) {
                                    throw ije;
                                }
                                try {
                                    JsonWebStructure.fromCompactSerialization(jwt);
                                    workingJwt = payload;
                                } catch (JoseException e) {
                                    throw ije;
                                }
                            }
                        }
                        joseObjects.addFirst(joseObject);
                    } catch (Exception e2) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Unexpected exception encountered while processing");
                        if (!joseObjects.isEmpty()) {
                            sb.append(" nested");
                        }
                        sb.append(" JOSE object (").append(e2).append("): ").append(workingJwt);
                        ErrorCodeValidator.Error error = new ErrorCodeValidator.Error(17, sb.toString());
                        throw new InvalidJwtException("JWT processing failed.", error, e2, jwtContext);
                    }
                } catch (JoseException e3) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Unable to process");
                    if (!joseObjects.isEmpty()) {
                        sb2.append(" nested");
                    }
                    sb2.append(" JOSE object (cause: ").append(e3).append("): ").append(workingJwt);
                    ErrorCodeValidator.Error error2 = new ErrorCodeValidator.Error(17, sb2.toString());
                    throw new InvalidJwtException("JWT processing failed.", error2, e3, jwtContext);
                }
            } catch (InvalidJwtException e4) {
                throw e4;
            }
        }
        processContext(jwtContext);
        return jwtContext;
    }

    void validate(JwtContext jwtCtx) throws InvalidJwtException {
        ErrorCodeValidator.Error error;
        List<ErrorCodeValidator.Error> issues = new ArrayList<>();
        for (ErrorCodeValidator validator : this.validators) {
            try {
                error = validator.validate(jwtCtx);
            } catch (MalformedClaimException e) {
                error = new ErrorCodeValidator.Error(18, e.getMessage());
            } catch (Exception e2) {
                String msg = "Unexpected exception thrown from validator " + validator.getClass().getName() + ": " + ExceptionHelp.toStringWithCausesAndAbbreviatedStack(e2, getClass());
                error = new ErrorCodeValidator.Error(17, msg);
            }
            if (error != null) {
                issues.add(error);
            }
        }
        if (!issues.isEmpty()) {
            String msg2 = "JWT (claims->" + jwtCtx.getJwtClaims().getRawJson() + ") rejected due to invalid claims or other invalid content.";
            throw new InvalidJwtException(msg2, issues, jwtCtx);
        }
    }

    private boolean isNestedJwt(JsonWebStructure joseObject) {
        String cty = joseObject.getContentTypeHeaderValue();
        return cty != null && (cty.equalsIgnoreCase("jwt") || cty.equalsIgnoreCase("application/jwt"));
    }
}
