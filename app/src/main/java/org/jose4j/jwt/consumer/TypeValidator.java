package org.jose4j.jwt.consumer;

import java.util.Locale;
import org.jose4j.jwt.consumer.ErrorCodeValidator;
import org.jose4j.jwx.JsonWebStructure;
import org.jose4j.lang.UncheckedJoseException;

/* loaded from: classes5.dex */
public class TypeValidator implements ErrorCodeValidator {
    private static final String APPLICATION_PRIMARY_TYPE = "application";
    private SimpleMediaType expectedType;
    private boolean requireType;

    public TypeValidator(boolean requireType, String expectedType) {
        try {
            this.expectedType = toMediaType(expectedType);
            if (this.expectedType.getSubType().equals("*")) {
                throw new UncheckedJoseException("cannot use wildcard in subtype of expected type");
            }
            this.requireType = requireType;
        } catch (MediaTypeParseException e) {
            throw new UncheckedJoseException("The given expected type '" + expectedType + "' isn't a valid media type in this context.", e);
        }
    }

    @Override // org.jose4j.jwt.consumer.ErrorCodeValidator
    public ErrorCodeValidator.Error validate(JwtContext jwtContext) {
        JsonWebStructure jsonWebThing = jwtContext.getJoseObjects().get(0);
        String type = jsonWebThing.getHeader("typ");
        return validate(type);
    }

    ErrorCodeValidator.Error validate(String type) {
        if (type == null) {
            if (this.requireType) {
                return new ErrorCodeValidator.Error(21, "No typ header parameter present in the innermost JWS/JWE");
            }
            return null;
        }
        if (this.expectedType != null) {
            try {
                SimpleMediaType mediaType = toMediaType(type);
                if (this.expectedType.match(mediaType) && !mediaType.getSubType().equals("*")) {
                }
                StringBuilder msg = new StringBuilder();
                msg.append("Invalid typ header parameter value '").append(type).append("'. Expecting '");
                msg.append(this.expectedType).append("'");
                if (this.expectedType.getPrimaryType().equals(APPLICATION_PRIMARY_TYPE)) {
                    msg.append(" or just '").append(this.expectedType.getSubType()).append("'");
                }
                msg.append(".");
                return new ErrorCodeValidator.Error(22, msg.toString());
            } catch (MediaTypeParseException e) {
                return new ErrorCodeValidator.Error(22, "typ header parameter value '" + type + "' not parsable as a media type " + e);
            }
        }
        return null;
    }

    private SimpleMediaType toMediaType(String typ) throws MediaTypeParseException {
        return typ.contains("/") ? new SimpleMediaType(typ) : new SimpleMediaType(APPLICATION_PRIMARY_TYPE, typ);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes5.dex */
    public static class MediaTypeParseException extends Exception {
        MediaTypeParseException(String message) {
            super(message);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes5.dex */
    public static class SimpleMediaType {
        private String primaryType;
        private String subType;

        SimpleMediaType(String mediaTypeString) throws MediaTypeParseException {
            parse(mediaTypeString);
        }

        SimpleMediaType(String primary, String sub) throws MediaTypeParseException {
            this.primaryType = primary.toLowerCase(Locale.ENGLISH);
            checkToken(this.primaryType);
            this.subType = sub.toLowerCase(Locale.ENGLISH);
            checkToken(this.subType);
        }

        private void parse(String mediaTypeString) throws MediaTypeParseException {
            int slashIdx = mediaTypeString.indexOf(47);
            if (slashIdx < 0) {
                throw new MediaTypeParseException("Cannot find sub type.");
            }
            int semiIdx = mediaTypeString.indexOf(59);
            if (semiIdx < 0) {
                this.primaryType = mediaTypeString.substring(0, slashIdx).trim().toLowerCase(Locale.ENGLISH);
                this.subType = mediaTypeString.substring(slashIdx + 1).trim().toLowerCase(Locale.ENGLISH);
            } else {
                if (slashIdx >= semiIdx) {
                    throw new MediaTypeParseException("Cannot find sub type.");
                }
                this.primaryType = mediaTypeString.substring(0, slashIdx).trim().toLowerCase(Locale.ENGLISH);
                this.subType = mediaTypeString.substring(slashIdx + 1, semiIdx).trim().toLowerCase(Locale.ENGLISH);
            }
            checkToken(this.primaryType);
            checkToken(this.subType);
        }

        String getPrimaryType() {
            return this.primaryType;
        }

        String getSubType() {
            return this.subType;
        }

        public String toString() {
            return getBaseType();
        }

        String getBaseType() {
            return this.primaryType + "/" + this.subType;
        }

        boolean match(SimpleMediaType type) {
            return this.primaryType.equals(type.getPrimaryType()) && (this.subType.equals(type.getSubType()) || this.subType.equals("*") || type.getSubType().equals("*"));
        }

        private static boolean isLegitTokenChar(char c) {
            return c > ' ' && c <= '~' && "()<>@,;:/[]?=\\\"".indexOf(c) < 0;
        }

        private static void checkToken(String t) throws MediaTypeParseException {
            if (t == null || t.length() == 0) {
                throw new MediaTypeParseException("cannot have empty part");
            }
            for (int i = 0; i < t.length(); i++) {
                char c = t.charAt(i);
                if (!isLegitTokenChar(c)) {
                    throw new MediaTypeParseException("Invalid token char " + c);
                }
            }
        }
    }
}
