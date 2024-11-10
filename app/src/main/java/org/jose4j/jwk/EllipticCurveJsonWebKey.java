package org.jose4j.jwk;

import java.math.BigInteger;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.EllipticCurve;
import java.util.HashMap;
import java.util.Map;
import org.jose4j.keys.EcKeyUtil;
import org.jose4j.keys.EllipticCurves;
import org.jose4j.lang.InvalidKeyException;
import org.jose4j.lang.JoseException;

/* loaded from: classes5.dex */
public class EllipticCurveJsonWebKey extends PublicJsonWebKey {
    public static final String CURVE_MEMBER_NAME = "crv";
    public static final String KEY_TYPE = "EC";
    public static final String PRIVATE_KEY_MEMBER_NAME = "d";
    public static final String X_MEMBER_NAME = "x";
    public static final String Y_MEMBER_NAME = "y";
    private String curveName;

    public EllipticCurveJsonWebKey(ECPublicKey publicKey) {
        super(publicKey);
        ECParameterSpec spec = publicKey.getParams();
        EllipticCurve curve = spec.getCurve();
        this.curveName = EllipticCurves.getName(curve);
    }

    public EllipticCurveJsonWebKey(Map<String, Object> params) throws JoseException {
        this(params, null);
    }

    public EllipticCurveJsonWebKey(Map<String, Object> params, String jcaProvider) throws JoseException {
        super(params, jcaProvider);
        this.curveName = getString(params, "crv", true);
        ECParameterSpec curve = EllipticCurves.getSpec(this.curveName);
        if (curve == null) {
            throw new InvalidKeyException("\"" + this.curveName + "\" is an unknown or unsupported value for the \"crv\" parameter.");
        }
        BigInteger x = getBigIntFromBase64UrlEncodedParam(params, "x", true);
        BigInteger y = getBigIntFromBase64UrlEncodedParam(params, Y_MEMBER_NAME, true);
        EcKeyUtil keyUtil = new EcKeyUtil(jcaProvider, null);
        this.key = keyUtil.publicKey(x, y, curve);
        checkForBareKeyCertMismatch();
        if (params.containsKey("d")) {
            BigInteger d = getBigIntFromBase64UrlEncodedParam(params, "d", false);
            this.privateKey = keyUtil.privateKey(d, curve);
        }
        removeFromOtherParams("crv", "x", Y_MEMBER_NAME, "d");
    }

    public ECPublicKey getECPublicKey() {
        return (ECPublicKey) this.key;
    }

    public ECPrivateKey getEcPrivateKey() {
        return (ECPrivateKey) this.privateKey;
    }

    @Override // org.jose4j.jwk.JsonWebKey
    public String getKeyType() {
        return "EC";
    }

    public String getCurveName() {
        return this.curveName;
    }

    private int getCoordinateByteLength() {
        ECParameterSpec spec = EllipticCurves.getSpec(getCurveName());
        return (int) Math.ceil(spec.getCurve().getField().getFieldSize() / 8.0d);
    }

    @Override // org.jose4j.jwk.PublicJsonWebKey
    protected void fillPublicTypeSpecificParams(Map<String, Object> params) {
        ECPublicKey ecPublicKey = getECPublicKey();
        ECPoint w = ecPublicKey.getW();
        int coordinateByteLength = getCoordinateByteLength();
        putBigIntAsBase64UrlEncodedParam(params, "x", w.getAffineX(), coordinateByteLength);
        putBigIntAsBase64UrlEncodedParam(params, Y_MEMBER_NAME, w.getAffineY(), coordinateByteLength);
        params.put("crv", getCurveName());
    }

    @Override // org.jose4j.jwk.PublicJsonWebKey
    protected void fillPrivateTypeSpecificParams(Map<String, Object> params) {
        ECPrivateKey ecPrivateKey = getEcPrivateKey();
        if (ecPrivateKey != null) {
            int coordinateByteLength = getCoordinateByteLength();
            putBigIntAsBase64UrlEncodedParam(params, "d", ecPrivateKey.getS(), coordinateByteLength);
        }
    }

    @Override // org.jose4j.jwk.JsonWebKey
    protected String produceThumbprintHashInput() {
        HashMap<String, Object> params = new HashMap<>();
        fillPublicTypeSpecificParams(params);
        Object crv = params.get("crv");
        Object x = params.get("x");
        Object y = params.get(Y_MEMBER_NAME);
        return String.format("{\"crv\":\"%s\",\"kty\":\"EC\",\"x\":\"%s\",\"y\":\"%s\"}", crv, x, y);
    }
}
