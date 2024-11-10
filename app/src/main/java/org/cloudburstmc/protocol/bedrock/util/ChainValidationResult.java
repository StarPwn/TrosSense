package org.cloudburstmc.protocol.bedrock.util;

import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import org.jose4j.json.JsonUtil;
import org.jose4j.lang.JoseException;

/* loaded from: classes5.dex */
public final class ChainValidationResult {
    private IdentityClaims identityClaims;
    private final Map<String, Object> parsedPayload;
    private final boolean signed;

    public ChainValidationResult(boolean signed, String rawPayload) throws JoseException {
        this(signed, JsonUtil.parseJson(rawPayload));
    }

    public ChainValidationResult(boolean signed, Map<String, Object> parsedPayload) {
        this.signed = signed;
        this.parsedPayload = (Map) Objects.requireNonNull(parsedPayload);
    }

    public boolean signed() {
        return this.signed;
    }

    public Map<String, Object> rawIdentityClaims() {
        return new HashMap(this.parsedPayload);
    }

    public IdentityClaims identityClaims() throws IllegalStateException {
        if (this.identityClaims == null) {
            String identityPublicKey = (String) JsonUtils.childAsType(this.parsedPayload, "identityPublicKey", String.class);
            Map<?, ?> extraData = (Map) JsonUtils.childAsType(this.parsedPayload, "extraData", Map.class);
            String displayName = (String) JsonUtils.childAsType(extraData, "displayName", String.class);
            String identityString = (String) JsonUtils.childAsType(extraData, "identity", String.class);
            String xuid = (String) JsonUtils.childAsType(extraData, "XUID", String.class);
            Object titleId = extraData.get("titleId");
            try {
                UUID identity = UUID.fromString(identityString);
                this.identityClaims = new IdentityClaims(new IdentityData(displayName, identity, xuid, (String) titleId), identityPublicKey);
            } catch (Exception e) {
                throw new IllegalStateException("identity node is an invalid UUID");
            }
        }
        return this.identityClaims;
    }

    /* loaded from: classes5.dex */
    public static final class IdentityClaims {
        public final IdentityData extraData;
        public final String identityPublicKey;
        private PublicKey parsedIdentityPublicKey;

        private IdentityClaims(IdentityData extraData, String identityPublicKey) {
            this.extraData = extraData;
            this.identityPublicKey = identityPublicKey;
        }

        public PublicKey parsedIdentityPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
            if (this.parsedIdentityPublicKey == null) {
                this.parsedIdentityPublicKey = EncryptionUtils.parseKey(this.identityPublicKey);
            }
            return this.parsedIdentityPublicKey;
        }
    }

    /* loaded from: classes5.dex */
    public static final class IdentityData {
        public final String displayName;
        public final UUID identity;
        public final String titleId;
        public final String xuid;

        private IdentityData(String displayName, UUID identity, String xuid, String titleId) {
            this.displayName = displayName;
            this.identity = identity;
            this.xuid = xuid;
            this.titleId = titleId;
        }
    }
}
