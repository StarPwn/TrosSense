package org.jose4j.keys.resolvers;

import java.security.Key;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwx.JsonWebStructure;
import org.jose4j.keys.X509Util;
import org.jose4j.lang.ExceptionHelp;
import org.jose4j.lang.JoseException;
import org.jose4j.lang.UncheckedJoseException;
import org.jose4j.lang.UnresolvableKeyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: classes5.dex */
public class X509VerificationKeyResolver implements VerificationKeyResolver {
    private static final Logger log = LoggerFactory.getLogger((Class<?>) X509VerificationKeyResolver.class);
    private boolean tryAllOnNoThumbHeader;
    private Map<String, X509Certificate> x5tMap;
    private Map<String, X509Certificate> x5tS256Map;

    public X509VerificationKeyResolver(List<X509Certificate> certificates) {
        this.x5tMap = new LinkedHashMap();
        this.x5tS256Map = new LinkedHashMap();
        for (X509Certificate cert : certificates) {
            try {
                String x5t = X509Util.x5t(cert);
                this.x5tMap.put(x5t, cert);
                String x5tS256 = X509Util.x5tS256(cert);
                this.x5tS256Map.put(x5tS256, cert);
            } catch (UncheckedJoseException e) {
                log.warn("Unable to get certificate thumbprint.", (Throwable) e);
            }
        }
    }

    public X509VerificationKeyResolver(X509Certificate... certificates) {
        this((List<X509Certificate>) Arrays.asList(certificates));
    }

    public void setTryAllOnNoThumbHeader(boolean tryAllOnNoThumbHeader) {
        this.tryAllOnNoThumbHeader = tryAllOnNoThumbHeader;
    }

    @Override // org.jose4j.keys.resolvers.VerificationKeyResolver
    public Key resolveKey(JsonWebSignature jws, List<JsonWebStructure> nestingContext) throws UnresolvableKeyException {
        String x5t = jws.getX509CertSha1ThumbprintHeaderValue();
        String x5tS256 = jws.getX509CertSha256ThumbprintHeaderValue();
        if (x5t == null && x5tS256 == null) {
            if (this.tryAllOnNoThumbHeader) {
                return attemptAll(jws);
            }
            throw new UnresolvableKeyException("Neither the x5t header nor the x5t#S256 header are present in the JWS.");
        }
        X509Certificate x509Certificate = this.x5tMap.get(x5t);
        if (x509Certificate == null) {
            x509Certificate = this.x5tS256Map.get(x5tS256);
        }
        if (x509Certificate == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("The X.509 Certificate Thumbprint header(s) in the JWS do not identify any of the provided Certificates -");
            if (x5t != null) {
                sb.append(" ").append("x5t").append("=").append(x5t);
                sb.append(" vs. SHA-1 thumbs:").append(this.x5tMap.keySet());
            }
            if (x5tS256 != null) {
                sb.append(" ").append("x5t#S256").append("=").append(x5tS256);
                sb.append(" vs. SHA-256 thumbs:").append(this.x5tS256Map.keySet());
            }
            sb.append(".");
            throw new UnresolvableKeyException(sb.toString());
        }
        return x509Certificate.getPublicKey();
    }

    private Key attemptAll(JsonWebSignature jws) throws UnresolvableKeyException {
        for (X509Certificate certificate : this.x5tMap.values()) {
            PublicKey publicKey = certificate.getPublicKey();
            jws.setKey(publicKey);
            try {
            } catch (JoseException e) {
                log.debug("Verify signature didn't work: {}", ExceptionHelp.toStringWithCauses(e));
            }
            if (jws.verifySignature()) {
                return publicKey;
            }
        }
        throw new UnresolvableKeyException("Unable to verify the signature with any of the provided keys - SHA-1 thumbs of provided certificates: " + this.x5tMap.keySet() + ".");
    }
}
