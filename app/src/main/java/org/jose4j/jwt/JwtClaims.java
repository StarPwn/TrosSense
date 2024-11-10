package org.jose4j.jwt;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jose4j.base64url.Base64Url;
import org.jose4j.json.JsonUtil;
import org.jose4j.jwt.consumer.ErrorCodeValidator;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtContext;
import org.jose4j.lang.ByteUtil;
import org.jose4j.lang.JoseException;

/* loaded from: classes5.dex */
public class JwtClaims {
    private Map<String, Object> claimsMap;
    private String rawJson;

    public JwtClaims() {
        this.claimsMap = new LinkedHashMap();
    }

    private JwtClaims(String jsonClaims, JwtContext jwtContext) throws InvalidJwtException {
        this.rawJson = jsonClaims;
        try {
            Map<String, Object> parsed = JsonUtil.parseJson(jsonClaims);
            this.claimsMap = new LinkedHashMap(parsed);
        } catch (JoseException e) {
            String msg = "Unable to parse what was expected to be the JWT Claim Set JSON: \"" + jsonClaims + "\"";
            ErrorCodeValidator.Error error = new ErrorCodeValidator.Error(16, "Invalid JSON.");
            throw new InvalidJwtException(msg, error, e, jwtContext);
        }
    }

    public static JwtClaims parse(String jsonClaims, JwtContext jwtContext) throws InvalidJwtException {
        return new JwtClaims(jsonClaims, jwtContext);
    }

    public static JwtClaims parse(String jsonClaims) throws InvalidJwtException {
        return new JwtClaims(jsonClaims, null);
    }

    public String getIssuer() throws MalformedClaimException {
        return (String) getClaimValue(ReservedClaimNames.ISSUER, String.class);
    }

    public void setIssuer(String issuer) {
        this.claimsMap.put(ReservedClaimNames.ISSUER, issuer);
    }

    public String getSubject() throws MalformedClaimException {
        return (String) getClaimValue(ReservedClaimNames.SUBJECT, String.class);
    }

    public void setSubject(String subject) {
        this.claimsMap.put(ReservedClaimNames.SUBJECT, subject);
    }

    public void setAudience(String audience) {
        this.claimsMap.put(ReservedClaimNames.AUDIENCE, audience);
    }

    public void setAudience(String... audience) {
        setAudience(Arrays.asList(audience));
    }

    public void setAudience(List<String> audiences) {
        if (audiences.size() == 1) {
            setAudience(audiences.get(0));
        } else {
            this.claimsMap.put(ReservedClaimNames.AUDIENCE, audiences);
        }
    }

    public boolean hasAudience() {
        return hasClaim(ReservedClaimNames.AUDIENCE);
    }

    public List<String> getAudience() throws MalformedClaimException {
        Object audienceObject = this.claimsMap.get(ReservedClaimNames.AUDIENCE);
        if (audienceObject instanceof String) {
            return Collections.singletonList((String) audienceObject);
        }
        if ((audienceObject instanceof List) || audienceObject == null) {
            List audienceList = (List) audienceObject;
            return toStringList(audienceList, ReservedClaimNames.AUDIENCE);
        }
        throw new MalformedClaimException("The value of the 'aud' claim is not an array of strings or a single string value.");
    }

    private List<String> toStringList(List list, String claimName) throws MalformedClaimException {
        if (list == null) {
            return Collections.emptyList();
        }
        List<String> values = new ArrayList<>();
        for (Object object : list) {
            try {
                values.add((String) object);
            } catch (ClassCastException e) {
                throw new MalformedClaimException("The array value of the '" + claimName + "' claim contains non string values " + classCastMsg(e, object), e);
            }
        }
        return values;
    }

    public NumericDate getExpirationTime() throws MalformedClaimException {
        return getNumericDateClaimValue(ReservedClaimNames.EXPIRATION_TIME);
    }

    public void setExpirationTime(NumericDate expirationTime) {
        setNumericDateClaim(ReservedClaimNames.EXPIRATION_TIME, expirationTime);
    }

    public void setExpirationTimeMinutesInTheFuture(float minutes) {
        setExpirationTime(offsetFromNow(minutes));
    }

    private NumericDate offsetFromNow(float offsetMinutes) {
        NumericDate numericDate = NumericDate.now();
        float secondsOffset = 60.0f * offsetMinutes;
        numericDate.addSeconds(secondsOffset);
        return numericDate;
    }

    public NumericDate getNotBefore() throws MalformedClaimException {
        return getNumericDateClaimValue(ReservedClaimNames.NOT_BEFORE);
    }

    public void setNotBefore(NumericDate notBefore) {
        setNumericDateClaim(ReservedClaimNames.NOT_BEFORE, notBefore);
    }

    public void setNotBeforeMinutesInThePast(float minutes) {
        setNotBefore(offsetFromNow((-1.0f) * minutes));
    }

    public NumericDate getIssuedAt() throws MalformedClaimException {
        return getNumericDateClaimValue(ReservedClaimNames.ISSUED_AT);
    }

    public void setIssuedAt(NumericDate issuedAt) {
        setNumericDateClaim(ReservedClaimNames.ISSUED_AT, issuedAt);
    }

    public void setIssuedAtToNow() {
        setIssuedAt(NumericDate.now());
    }

    public String getJwtId() throws MalformedClaimException {
        return (String) getClaimValue(ReservedClaimNames.JWT_ID, String.class);
    }

    public void setJwtId(String jwtId) {
        this.claimsMap.put(ReservedClaimNames.JWT_ID, jwtId);
    }

    public void setGeneratedJwtId(int numberOfBytes) {
        byte[] rndbytes = ByteUtil.randomBytes(numberOfBytes);
        String jti = Base64Url.encode(rndbytes);
        setJwtId(jti);
    }

    public void setGeneratedJwtId() {
        setGeneratedJwtId(16);
    }

    public void unsetClaim(String claimName) {
        this.claimsMap.remove(claimName);
    }

    public <T> T getClaimValue(String claimName, Class<T> type) throws MalformedClaimException {
        Object o = this.claimsMap.get(claimName);
        try {
            return type.cast(o);
        } catch (ClassCastException e) {
            throw new MalformedClaimException("The value of the '" + claimName + "' claim is not the expected type " + classCastMsg(e, o), e);
        }
    }

    public Object getClaimValue(String claimName) {
        return this.claimsMap.get(claimName);
    }

    public boolean hasClaim(String claimName) {
        return getClaimValue(claimName) != null;
    }

    private String classCastMsg(ClassCastException e, Object o) {
        return "(" + o + " - " + e.getMessage() + ")";
    }

    public NumericDate getNumericDateClaimValue(String claimName) throws MalformedClaimException {
        Number number = (Number) getClaimValue(claimName, Number.class);
        if (number instanceof BigInteger) {
            throw new MalformedClaimException(number + " is unreasonable for a NumericDate");
        }
        if (number != null) {
            return NumericDate.fromSeconds(number.longValue());
        }
        return null;
    }

    public String getStringClaimValue(String claimName) throws MalformedClaimException {
        return (String) getClaimValue(claimName, String.class);
    }

    public String getClaimValueAsString(String claimName) {
        Object claimObjectValue = getClaimValue(claimName);
        if (claimObjectValue != null) {
            return claimObjectValue.toString();
        }
        return null;
    }

    public List<String> getStringListClaimValue(String claimName) throws MalformedClaimException {
        List listClaimValue = (List) getClaimValue(claimName, List.class);
        return toStringList(listClaimValue, claimName);
    }

    public void setNumericDateClaim(String claimName, NumericDate value) {
        this.claimsMap.put(claimName, value != null ? Long.valueOf(value.getValue()) : null);
    }

    public void setStringClaim(String claimName, String value) {
        this.claimsMap.put(claimName, value);
    }

    public void setStringListClaim(String claimName, List<String> values) {
        this.claimsMap.put(claimName, values);
    }

    public void setStringListClaim(String claimName, String... values) {
        this.claimsMap.put(claimName, Arrays.asList(values));
    }

    public void setClaim(String claimName, Object value) {
        this.claimsMap.put(claimName, value);
    }

    public boolean isClaimValueOfType(String claimName, Class type) {
        try {
            return getClaimValue(claimName, type) != null;
        } catch (MalformedClaimException e) {
            return false;
        }
    }

    public boolean isClaimValueString(String claimName) {
        return isClaimValueOfType(claimName, String.class);
    }

    public boolean isClaimValueStringList(String claimName) {
        try {
            if (hasClaim(claimName)) {
                return getStringListClaimValue(claimName) != null;
            }
            return false;
        } catch (MalformedClaimException e) {
            return false;
        }
    }

    public Map<String, List<Object>> flattenClaims() {
        return flattenClaims(null);
    }

    public Map<String, List<Object>> flattenClaims(Set<String> omittedClaims) {
        Set<String> omittedClaims2 = omittedClaims == null ? Collections.emptySet() : omittedClaims;
        Map<String, List<Object>> flattenedClaims = new LinkedHashMap<>();
        for (Map.Entry<String, Object> e : this.claimsMap.entrySet()) {
            String key = e.getKey();
            if (!omittedClaims2.contains(key)) {
                dfs(null, key, e.getValue(), flattenedClaims);
            }
        }
        return flattenedClaims;
    }

    private void dfs(String baseName, String name, Object value, Map<String, List<Object>> flattenedClaims) {
        String key = (baseName == null ? "" : baseName + ".") + name;
        if (value instanceof List) {
            List<Object> newList = new ArrayList<>();
            for (Object item : (List) value) {
                if (item instanceof Map) {
                    Map<?, ?> mv = (Map) item;
                    for (Map.Entry<?, ?> e : mv.entrySet()) {
                        dfs(key, e.getKey().toString(), e.getValue(), flattenedClaims);
                    }
                } else {
                    newList.add(item);
                }
            }
            flattenedClaims.put(key, newList);
            return;
        }
        if (value instanceof Map) {
            Map<?, ?> mapValue = (Map) value;
            for (Map.Entry<?, ?> e2 : mapValue.entrySet()) {
                dfs(key, e2.getKey().toString(), e2.getValue(), flattenedClaims);
            }
            return;
        }
        flattenedClaims.put(key, Collections.singletonList(value));
    }

    public Map<String, Object> getClaimsMap(Set<String> omittedClaims) {
        Set<String> omittedClaims2 = omittedClaims != null ? omittedClaims : Collections.emptySet();
        LinkedHashMap<String, Object> claims = new LinkedHashMap<>(this.claimsMap);
        for (String omittedClaim : omittedClaims2) {
            claims.remove(omittedClaim);
        }
        return claims;
    }

    public Map<String, Object> getClaimsMap() {
        return getClaimsMap(null);
    }

    public Collection<String> getClaimNames(Set<String> omittedClaims) {
        return getClaimsMap(omittedClaims).keySet();
    }

    public Collection<String> getClaimNames() {
        return getClaimNames(null);
    }

    public String toJson() {
        return JsonUtil.toJson(this.claimsMap);
    }

    public String getRawJson() {
        return this.rawJson;
    }

    public String toString() {
        return "JWT Claims Set:" + this.claimsMap;
    }
}
