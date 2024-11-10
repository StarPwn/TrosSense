package org.jose4j.jwk;

import java.security.Key;
import java.security.PublicKey;
import java.security.interfaces.EdECKey;
import java.security.interfaces.XECKey;
import java.security.spec.NamedParameterSpec;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.jose4j.base64url.Base64Url;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.keys.EdDsaKeyUtil;
import org.jose4j.keys.OctetKeyPairUtil;
import org.jose4j.keys.XDHKeyUtil;
import org.jose4j.lang.ExceptionHelp;
import org.jose4j.lang.InvalidKeyException;
import org.jose4j.lang.JoseException;
import org.jose4j.lang.UncheckedJoseException;

/* loaded from: classes5.dex */
public class OctetKeyPairJsonWebKey extends PublicJsonWebKey {
    static final Set<String> APPLICABLE_KEY_ALGORITHMS = new HashSet(Arrays.asList("Ed448", "Ed25519", AlgorithmIdentifiers.EDDSA, "X25519", "X448", "XDH"));
    public static final String KEY_TYPE = "OKP";
    public static final String PRIVATE_KEY_MEMBER_NAME = "d";
    public static final String PUBLIC_KEY_MEMBER_NAME = "x";
    public static final String SUBTYPE_ED25519 = "Ed25519";
    public static final String SUBTYPE_ED448 = "Ed448";
    public static final String SUBTYPE_MEMBER_NAME = "crv";
    public static final String SUBTYPE_X25519 = "X25519";
    public static final String SUBTYPE_X448 = "X448";
    private final String subtype;

    public OctetKeyPairJsonWebKey(PublicKey publicKey) {
        super(publicKey);
        if (XDHKeyUtil.isXECPublicKey(publicKey)) {
            XECKey xecKey = (XECKey) publicKey;
            this.subtype = ((NamedParameterSpec) xecKey.getParams()).getName();
        } else {
            if (EdDsaKeyUtil.isEdECPublicKey(publicKey)) {
                EdECKey edECKey = (EdECKey) publicKey;
                this.subtype = edECKey.getParams().getName();
                return;
            }
            throw new UncheckedJoseException("Unable to determine OKP subtype from " + publicKey);
        }
    }

    public OctetKeyPairJsonWebKey(Map<String, Object> params) throws JoseException {
        this(params, null);
    }

    public OctetKeyPairJsonWebKey(Map<String, Object> params, String jcaProvider) throws JoseException {
        super(params, jcaProvider);
        this.subtype = getString(params, "crv", true);
        try {
            OctetKeyPairUtil keyUtil = subtypeKeyUtil();
            if (keyUtil == null) {
                throw new InvalidKeyException("\"" + this.subtype + "\" is an unknown or unsupported subtype value for the \"crv\" parameter.");
            }
            String encodedX = getString(params, "x", true);
            byte[] x = Base64Url.decode(encodedX);
            this.key = keyUtil.publicKey(x, this.subtype);
            checkForBareKeyCertMismatch();
            if (params.containsKey("d")) {
                String encodedD = getString(params, "d", false);
                byte[] d = Base64Url.decode(encodedD);
                this.privateKey = keyUtil.privateKey(d, this.subtype);
            }
            removeFromOtherParams("crv", "x", "d");
        } catch (NoClassDefFoundError ncd) {
            throw new JoseException("Unable to instantiate key for OKP JWK with " + this.subtype + ". " + ExceptionHelp.toStringWithCauses(ncd));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isApplicable(Key key) {
        return APPLICABLE_KEY_ALGORITHMS.contains(key.getAlgorithm());
    }

    @Override // org.jose4j.jwk.JsonWebKey
    public String getKeyType() {
        return KEY_TYPE;
    }

    public String getSubtype() {
        return this.subtype;
    }

    @Override // org.jose4j.jwk.JsonWebKey
    protected String produceThumbprintHashInput() {
        HashMap<String, Object> params = new HashMap<>();
        fillPublicTypeSpecificParams(params);
        Object crv = params.get("crv");
        Object x = params.get("x");
        return String.format("{\"crv\":\"%s\",\"kty\":\"OKP\",\"x\":\"%s\"}", crv, x);
    }

    @Override // org.jose4j.jwk.PublicJsonWebKey
    protected void fillPublicTypeSpecificParams(Map<String, Object> params) {
        OctetKeyPairUtil ku = subtypeKeyUtil();
        byte[] publicKeyBytes = ku.rawPublicKey(this.key);
        params.put("crv", this.subtype);
        params.put("x", Base64Url.encode(publicKeyBytes));
    }

    @Override // org.jose4j.jwk.PublicJsonWebKey
    protected void fillPrivateTypeSpecificParams(Map<String, Object> params) {
        if (this.privateKey != null) {
            OctetKeyPairUtil ku = subtypeKeyUtil();
            byte[] privateKeyBytes = ku.rawPrivateKey(this.privateKey);
            params.put("d", Base64Url.encode(privateKeyBytes));
        }
    }

    OctetKeyPairUtil subtypeKeyUtil() {
        return OctetKeyPairUtil.getOctetKeyPairUtil(this.subtype, this.jcaProvider, null);
    }
}
