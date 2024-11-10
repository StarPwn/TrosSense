package org.cloudburstmc.protocol.bedrock.data.skin;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Collections;
import java.util.List;
import org.cloudburstmc.protocol.common.util.Preconditions;
import org.jose4j.json.internal.json_simple.JSONObject;
import org.jose4j.json.internal.json_simple.JSONValue;

/* loaded from: classes5.dex */
public class SerializedSkin {
    public static final int DOUBLE_SKIN_SIZE = 16384;
    private static final int PIXEL_SIZE = 4;
    public static final int SINGLE_SKIN_SIZE = 8192;
    public static final int SKIN_128_128_SIZE = 65536;
    public static final int SKIN_128_64_SIZE = 32768;
    private final String animationData;
    private final List<AnimationData> animations;
    private final String armSize;
    private final ImageData capeData;
    private final String capeId;
    private final boolean capeOnClassic;
    private final String fullSkinId;
    private final String geometryData;
    private final String geometryDataEngineVersion;
    private final String geometryName;
    private final boolean overridingPlayerAppearance;
    private final boolean persona;
    private final List<PersonaPieceData> personaPieces;
    private final String playFabId;
    private final boolean premium;
    private final boolean primaryUser;
    private final String skinColor;
    private final ImageData skinData;
    private final String skinId;
    private final String skinResourcePatch;
    private final List<PersonaPieceTintData> tintColors;

    public String toString() {
        return "SerializedSkin(skinId=" + getSkinId() + ", playFabId=" + getPlayFabId() + ", geometryName=" + getGeometryName() + ", skinResourcePatch=" + getSkinResourcePatch() + ", skinData=" + getSkinData() + ", animations=" + getAnimations() + ", capeData=" + getCapeData() + ", geometryDataEngineVersion=" + getGeometryDataEngineVersion() + ", animationData=" + getAnimationData() + ", premium=" + isPremium() + ", persona=" + isPersona() + ", capeOnClassic=" + isCapeOnClassic() + ", primaryUser=" + isPrimaryUser() + ", capeId=" + getCapeId() + ", fullSkinId=" + getFullSkinId() + ", armSize=" + getArmSize() + ", skinColor=" + getSkinColor() + ", personaPieces=" + getPersonaPieces() + ", tintColors=" + getTintColors() + ", overridingPlayerAppearance=" + isOverridingPlayerAppearance() + ")";
    }

    protected boolean canEqual(Object other) {
        return other instanceof SerializedSkin;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SerializedSkin)) {
            return false;
        }
        SerializedSkin other = (SerializedSkin) o;
        if (!other.canEqual(this) || isPremium() != other.isPremium() || isPersona() != other.isPersona() || isCapeOnClassic() != other.isCapeOnClassic() || isPrimaryUser() != other.isPrimaryUser() || isOverridingPlayerAppearance() != other.isOverridingPlayerAppearance()) {
            return false;
        }
        Object this$skinId = getSkinId();
        Object other$skinId = other.getSkinId();
        if (this$skinId != null ? !this$skinId.equals(other$skinId) : other$skinId != null) {
            return false;
        }
        Object this$playFabId = getPlayFabId();
        Object other$playFabId = other.getPlayFabId();
        if (this$playFabId != null ? !this$playFabId.equals(other$playFabId) : other$playFabId != null) {
            return false;
        }
        Object this$geometryName = getGeometryName();
        Object other$geometryName = other.getGeometryName();
        if (this$geometryName != null ? !this$geometryName.equals(other$geometryName) : other$geometryName != null) {
            return false;
        }
        Object this$skinResourcePatch = getSkinResourcePatch();
        Object other$skinResourcePatch = other.getSkinResourcePatch();
        if (this$skinResourcePatch != null ? !this$skinResourcePatch.equals(other$skinResourcePatch) : other$skinResourcePatch != null) {
            return false;
        }
        Object this$skinData = getSkinData();
        Object other$skinData = other.getSkinData();
        if (this$skinData != null ? !this$skinData.equals(other$skinData) : other$skinData != null) {
            return false;
        }
        Object this$animations = getAnimations();
        Object other$animations = other.getAnimations();
        if (this$animations != null ? !this$animations.equals(other$animations) : other$animations != null) {
            return false;
        }
        Object this$capeData = getCapeData();
        Object other$capeData = other.getCapeData();
        if (this$capeData == null) {
            if (other$capeData != null) {
                return false;
            }
        } else if (!this$capeData.equals(other$capeData)) {
            return false;
        }
        Object this$geometryData = getGeometryData();
        Object other$geometryData = other.getGeometryData();
        if (this$geometryData == null) {
            if (other$geometryData != null) {
                return false;
            }
        } else if (!this$geometryData.equals(other$geometryData)) {
            return false;
        }
        Object this$geometryDataEngineVersion = getGeometryDataEngineVersion();
        Object other$geometryDataEngineVersion = other.getGeometryDataEngineVersion();
        if (this$geometryDataEngineVersion == null) {
            if (other$geometryDataEngineVersion != null) {
                return false;
            }
        } else if (!this$geometryDataEngineVersion.equals(other$geometryDataEngineVersion)) {
            return false;
        }
        Object this$animationData = getAnimationData();
        Object other$animationData = other.getAnimationData();
        if (this$animationData == null) {
            if (other$animationData != null) {
                return false;
            }
        } else if (!this$animationData.equals(other$animationData)) {
            return false;
        }
        Object this$capeId = getCapeId();
        Object other$capeId = other.getCapeId();
        if (this$capeId == null) {
            if (other$capeId != null) {
                return false;
            }
        } else if (!this$capeId.equals(other$capeId)) {
            return false;
        }
        Object this$fullSkinId = getFullSkinId();
        Object other$fullSkinId = other.getFullSkinId();
        if (this$fullSkinId == null) {
            if (other$fullSkinId != null) {
                return false;
            }
        } else if (!this$fullSkinId.equals(other$fullSkinId)) {
            return false;
        }
        Object this$armSize = getArmSize();
        Object other$armSize = other.getArmSize();
        if (this$armSize == null) {
            if (other$armSize != null) {
                return false;
            }
        } else if (!this$armSize.equals(other$armSize)) {
            return false;
        }
        Object this$skinColor = getSkinColor();
        Object other$skinColor = other.getSkinColor();
        if (this$skinColor == null) {
            if (other$skinColor != null) {
                return false;
            }
        } else if (!this$skinColor.equals(other$skinColor)) {
            return false;
        }
        Object this$personaPieces = getPersonaPieces();
        Object other$personaPieces = other.getPersonaPieces();
        if (this$personaPieces == null) {
            if (other$personaPieces != null) {
                return false;
            }
        } else if (!this$personaPieces.equals(other$personaPieces)) {
            return false;
        }
        Object this$tintColors = getTintColors();
        Object other$tintColors = other.getTintColors();
        return this$tintColors == null ? other$tintColors == null : this$tintColors.equals(other$tintColors);
    }

    public int hashCode() {
        int result = (1 * 59) + (isPremium() ? 79 : 97);
        int result2 = ((((((result * 59) + (isPersona() ? 79 : 97)) * 59) + (isCapeOnClassic() ? 79 : 97)) * 59) + (isPrimaryUser() ? 79 : 97)) * 59;
        int i = isOverridingPlayerAppearance() ? 79 : 97;
        Object $skinId = getSkinId();
        int result3 = ((result2 + i) * 59) + ($skinId == null ? 43 : $skinId.hashCode());
        Object $playFabId = getPlayFabId();
        int result4 = (result3 * 59) + ($playFabId == null ? 43 : $playFabId.hashCode());
        Object $geometryName = getGeometryName();
        int result5 = (result4 * 59) + ($geometryName == null ? 43 : $geometryName.hashCode());
        Object $skinResourcePatch = getSkinResourcePatch();
        int result6 = (result5 * 59) + ($skinResourcePatch == null ? 43 : $skinResourcePatch.hashCode());
        Object $skinData = getSkinData();
        int result7 = (result6 * 59) + ($skinData == null ? 43 : $skinData.hashCode());
        Object $animations = getAnimations();
        int result8 = (result7 * 59) + ($animations == null ? 43 : $animations.hashCode());
        Object $capeData = getCapeData();
        int result9 = (result8 * 59) + ($capeData == null ? 43 : $capeData.hashCode());
        Object $geometryData = getGeometryData();
        int result10 = (result9 * 59) + ($geometryData == null ? 43 : $geometryData.hashCode());
        Object $geometryDataEngineVersion = getGeometryDataEngineVersion();
        int result11 = (result10 * 59) + ($geometryDataEngineVersion == null ? 43 : $geometryDataEngineVersion.hashCode());
        Object $animationData = getAnimationData();
        int result12 = (result11 * 59) + ($animationData == null ? 43 : $animationData.hashCode());
        Object $capeId = getCapeId();
        int result13 = (result12 * 59) + ($capeId == null ? 43 : $capeId.hashCode());
        Object $fullSkinId = getFullSkinId();
        int result14 = (result13 * 59) + ($fullSkinId == null ? 43 : $fullSkinId.hashCode());
        Object $armSize = getArmSize();
        int result15 = (result14 * 59) + ($armSize == null ? 43 : $armSize.hashCode());
        Object $skinColor = getSkinColor();
        int result16 = (result15 * 59) + ($skinColor == null ? 43 : $skinColor.hashCode());
        Object $personaPieces = getPersonaPieces();
        int result17 = (result16 * 59) + ($personaPieces == null ? 43 : $personaPieces.hashCode());
        Object $tintColors = getTintColors();
        return (result17 * 59) + ($tintColors != null ? $tintColors.hashCode() : 43);
    }

    private SerializedSkin(String skinId, String playFabId, String geometryName, String skinResourcePatch, ImageData skinData, List<AnimationData> animations, ImageData capeData, String geometryData, String geometryDataEngineVersion, String animationData, boolean premium, boolean persona, boolean capeOnClassic, boolean primaryUser, String capeId, String fullSkinId, String armSize, String skinColor, List<PersonaPieceData> personaPieces, List<PersonaPieceTintData> tintColors, boolean overridingPlayerAppearance) {
        this.skinId = skinId;
        this.playFabId = playFabId;
        this.geometryName = geometryName;
        this.skinResourcePatch = skinResourcePatch;
        this.skinData = skinData;
        this.animations = animations;
        this.capeData = capeData;
        this.geometryData = geometryData;
        this.geometryDataEngineVersion = geometryDataEngineVersion;
        this.animationData = animationData;
        this.premium = premium;
        this.persona = persona;
        this.capeOnClassic = capeOnClassic;
        this.primaryUser = primaryUser;
        this.capeId = capeId;
        this.fullSkinId = fullSkinId;
        this.armSize = armSize;
        this.skinColor = skinColor;
        this.personaPieces = personaPieces;
        this.tintColors = tintColors;
        this.overridingPlayerAppearance = overridingPlayerAppearance;
    }

    public String getSkinId() {
        return this.skinId;
    }

    public String getPlayFabId() {
        return this.playFabId;
    }

    public ImageData getSkinData() {
        return this.skinData;
    }

    public List<AnimationData> getAnimations() {
        return this.animations;
    }

    public ImageData getCapeData() {
        return this.capeData;
    }

    public String getGeometryData() {
        return this.geometryData;
    }

    public String getGeometryDataEngineVersion() {
        return this.geometryDataEngineVersion;
    }

    public String getAnimationData() {
        return this.animationData;
    }

    public boolean isPremium() {
        return this.premium;
    }

    public boolean isPersona() {
        return this.persona;
    }

    public boolean isCapeOnClassic() {
        return this.capeOnClassic;
    }

    public boolean isPrimaryUser() {
        return this.primaryUser;
    }

    public String getCapeId() {
        return this.capeId;
    }

    public String getFullSkinId() {
        return this.fullSkinId;
    }

    public String getArmSize() {
        return this.armSize;
    }

    public String getSkinColor() {
        return this.skinColor;
    }

    public List<PersonaPieceData> getPersonaPieces() {
        return this.personaPieces;
    }

    public List<PersonaPieceTintData> getTintColors() {
        return this.tintColors;
    }

    public boolean isOverridingPlayerAppearance() {
        return this.overridingPlayerAppearance;
    }

    public static SerializedSkin of(String skinId, String playFabId, ImageData skinData, ImageData capeData, String geometryName, String geometryData, boolean premiumSkin) {
        skinData.checkLegacySkinSize();
        capeData.checkLegacyCapeSize();
        return new SerializedSkin(skinId, playFabId, geometryName, null, skinData, Collections.emptyList(), capeData, geometryData, "", "", premiumSkin, false, false, true, "", "", "wide", "#0", Collections.emptyList(), Collections.emptyList(), true);
    }

    public static SerializedSkin of(String skinId, String playFabId, String skinResourcePatch, ImageData skinData, List<AnimationData> animations, ImageData capeData, String geometryData, String animationData, boolean premium, boolean persona, boolean capeOnClassic, String capeId, String fullSkinId) {
        return of(skinId, playFabId, skinResourcePatch, skinData, Collections.unmodifiableList(new ObjectArrayList(animations)), capeData, geometryData, animationData, premium, persona, capeOnClassic, capeId, fullSkinId, "wide", "#0", Collections.emptyList(), Collections.emptyList());
    }

    public static SerializedSkin of(String skinId, String playFabId, String skinResourcePatch, ImageData skinData, List<AnimationData> animations, ImageData capeData, String geometryData, String animationData, boolean premium, boolean persona, boolean capeOnClassic, String capeId, String fullSkinId, String armSize, String skinColor, List<PersonaPieceData> personaPieces, List<PersonaPieceTintData> tintColors) {
        return of(skinId, playFabId, skinResourcePatch, skinData, animations, capeData, geometryData, animationData, premium, persona, capeOnClassic, true, capeId, fullSkinId, armSize, skinColor, personaPieces, tintColors);
    }

    public static SerializedSkin of(String skinId, String playFabId, String skinResourcePatch, ImageData skinData, List<AnimationData> animations, ImageData capeData, String geometryData, String animationData, boolean premium, boolean persona, boolean capeOnClassic, boolean primaryUser, String capeId, String fullSkinId, String armSize, String skinColor, List<PersonaPieceData> personaPieces, List<PersonaPieceTintData> tintColors) {
        return new SerializedSkin(skinId, playFabId, null, skinResourcePatch, skinData, Collections.unmodifiableList(new ObjectArrayList(animations)), capeData, geometryData, "", animationData, premium, persona, capeOnClassic, primaryUser, capeId, fullSkinId, armSize, skinColor, personaPieces, tintColors, true);
    }

    public static SerializedSkin of(String skinId, String playFabId, String skinResourcePatch, ImageData skinData, List<AnimationData> animations, ImageData capeData, String geometryData, String geometryDataEngineVersion, String animationData, boolean premium, boolean persona, boolean capeOnClassic, boolean primaryUser, String capeId, String fullSkinId, String armSize, String skinColor, List<PersonaPieceData> personaPieces, List<PersonaPieceTintData> tintColors) {
        return new SerializedSkin(skinId, playFabId, null, skinResourcePatch, skinData, Collections.unmodifiableList(new ObjectArrayList(animations)), capeData, geometryData, geometryDataEngineVersion, animationData, premium, persona, capeOnClassic, primaryUser, capeId, fullSkinId, armSize, skinColor, personaPieces, tintColors, true);
    }

    public static SerializedSkin of(String skinId, String playFabId, String skinResourcePatch, ImageData skinData, List<AnimationData> animations, ImageData capeData, String geometryData, String geometryDataEngineVersion, String animationData, boolean premium, boolean persona, boolean capeOnClassic, boolean primaryUser, String capeId, String fullSkinId, String armSize, String skinColor, List<PersonaPieceData> personaPieces, List<PersonaPieceTintData> tintColors, boolean overridingPlayerAppearance) {
        return new SerializedSkin(skinId, playFabId, null, skinResourcePatch, skinData, Collections.unmodifiableList(new ObjectArrayList(animations)), capeData, geometryData, geometryDataEngineVersion, animationData, premium, persona, capeOnClassic, primaryUser, capeId, fullSkinId, armSize, skinColor, personaPieces, tintColors, overridingPlayerAppearance);
    }

    public static Builder builder() {
        return new Builder();
    }

    public boolean isValid() {
        return isValidSkin() && isValidResourcePatch();
    }

    private boolean isValidSkin() {
        return this.skinId != null && !this.skinId.trim().isEmpty() && this.skinData != null && this.skinData.getWidth() >= 64 && this.skinData.getHeight() >= 32 && this.skinData.getImage().length >= 8192;
    }

    public String getSkinResourcePatch() {
        if (this.skinResourcePatch == null && this.geometryName != null) {
            return convertLegacyGeometryName(this.geometryName);
        }
        return this.skinResourcePatch;
    }

    public String getGeometryName() {
        if (this.geometryName == null && this.skinResourcePatch != null) {
            return convertSkinPatchToLegacy(this.skinResourcePatch);
        }
        return this.geometryName;
    }

    private static String convertLegacyGeometryName(String geometryName) {
        return "{\"geometry\" : {\"default\" : \"" + JSONValue.escape(geometryName) + "\"}}";
    }

    private static String convertSkinPatchToLegacy(String skinResourcePatch) {
        Preconditions.checkArgument(validateSkinResourcePatch(skinResourcePatch), "Invalid skin resource patch");
        JSONObject object = (JSONObject) JSONValue.parse(skinResourcePatch);
        JSONObject geometry = (JSONObject) object.get("geometry");
        return (String) geometry.get("default");
    }

    private boolean isValidResourcePatch() {
        return this.skinResourcePatch != null && validateSkinResourcePatch(this.skinResourcePatch);
    }

    private static boolean validateSkinResourcePatch(String skinResourcePatch) {
        try {
            JSONObject object = (JSONObject) JSONValue.parse(skinResourcePatch);
            JSONObject geometry = (JSONObject) object.get("geometry");
            if (geometry.containsKey("default")) {
                return geometry.get("default") instanceof String;
            }
            return false;
        } catch (ClassCastException | NullPointerException e) {
            return false;
        }
    }

    public Builder toBuilder() {
        return new Builder().skinId(this.skinId).geometryData(this.geometryName).skinResourcePatch(this.skinResourcePatch).skinData(this.skinData).animations(this.animations).capeData(this.capeData).geometryData(this.geometryData).animationData(this.animationData).premium(this.premium).persona(this.persona).capeOnClassic(this.capeOnClassic).capeId(this.capeId).fullSkinId(this.fullSkinId).armSize(this.armSize).skinColor(this.skinColor).personaPieces(this.personaPieces).tintColors(this.tintColors);
    }

    /* loaded from: classes5.dex */
    public static class Builder {
        private String animationData;
        private List<AnimationData> animations;
        private String armSize;
        private ImageData capeData;
        private String capeId;
        private boolean capeOnClassic;
        private String fullSkinId;
        private String geometryData;
        private String geometryDataEngineVersion;
        private String geometryName;
        private boolean persona;
        private List<PersonaPieceData> personaPieces;
        private String playFabId;
        private boolean premium;
        private boolean primaryUser;
        private String skinColor;
        private ImageData skinData;
        private String skinId;
        private String skinResourcePatch;
        private List<PersonaPieceTintData> tintColors;

        Builder() {
        }

        public Builder skinId(String skinId) {
            this.skinId = skinId;
            return this;
        }

        public Builder playFabId(String playFabId) {
            this.playFabId = playFabId;
            return this;
        }

        public Builder geometryName(String geometryName) {
            this.geometryName = geometryName;
            return this;
        }

        public Builder skinResourcePatch(String skinResourcePatch) {
            this.skinResourcePatch = skinResourcePatch;
            return this;
        }

        public Builder skinData(ImageData skinData) {
            this.skinData = skinData;
            return this;
        }

        public Builder animations(List<AnimationData> animations) {
            this.animations = animations;
            return this;
        }

        public Builder capeData(ImageData capeData) {
            this.capeData = capeData;
            return this;
        }

        public Builder geometryData(String geometryData) {
            this.geometryData = geometryData;
            return this;
        }

        public Builder animationData(String animationData) {
            this.animationData = animationData;
            return this;
        }

        public Builder premium(boolean premium) {
            this.premium = premium;
            return this;
        }

        public Builder persona(boolean persona) {
            this.persona = persona;
            return this;
        }

        public Builder capeOnClassic(boolean capeOnClassic) {
            this.capeOnClassic = capeOnClassic;
            return this;
        }

        public Builder capeId(String capeId) {
            this.capeId = capeId;
            return this;
        }

        public Builder fullSkinId(String fullSkinId) {
            this.fullSkinId = fullSkinId;
            return this;
        }

        public Builder armSize(String armSize) {
            this.armSize = armSize;
            return this;
        }

        public Builder skinColor(String skinColor) {
            this.skinColor = skinColor;
            return this;
        }

        public Builder personaPieces(List<PersonaPieceData> personaPieces) {
            this.personaPieces = personaPieces;
            return this;
        }

        public Builder tintColors(List<PersonaPieceTintData> tintColors) {
            this.tintColors = tintColors;
            return this;
        }

        public Builder geometryDataEngineVersion(String version) {
            this.geometryDataEngineVersion = version;
            return this;
        }

        public Builder primaryUser(boolean primaryUser) {
            this.primaryUser = primaryUser;
            return this;
        }

        public SerializedSkin build() {
            if (this.playFabId == null) {
                this.playFabId = "";
            }
            if (this.animationData == null) {
                this.animationData = "";
            }
            if (this.capeData == null) {
                this.capeData = ImageData.EMPTY;
            }
            if (this.capeId == null) {
                this.capeId = "";
            }
            if (this.fullSkinId == null) {
                this.fullSkinId = this.skinId + this.capeId;
            }
            if (this.armSize == null) {
                this.armSize = "wide";
            }
            if (this.geometryDataEngineVersion == null) {
                this.geometryDataEngineVersion = "";
            }
            if (this.skinColor == null) {
                this.skinColor = "#0";
            }
            if (this.personaPieces == null) {
                this.personaPieces = Collections.emptyList();
            }
            if (this.tintColors == null) {
                this.tintColors = Collections.emptyList();
            }
            return this.skinResourcePatch == null ? SerializedSkin.of(this.skinId, this.playFabId, this.geometryName, this.skinData, this.animations, this.capeData, this.geometryData, this.animationData, this.premium, this.persona, this.capeOnClassic, this.capeId, this.fullSkinId) : SerializedSkin.of(this.skinId, this.playFabId, this.skinResourcePatch, this.skinData, this.animations, this.capeData, this.geometryData, this.geometryDataEngineVersion, this.animationData, this.premium, this.persona, this.capeOnClassic, this.primaryUser, this.capeId, this.fullSkinId, this.armSize, this.skinColor, this.personaPieces, this.tintColors);
        }

        public String toString() {
            return "SerializedSkin.Builder(skinId=" + this.skinId + ", playFabId=" + this.playFabId + ", geometryName=" + this.geometryName + ", skinResourcePatch=" + this.skinResourcePatch + ", skinData=" + this.skinData + ", animations=" + this.animations + ", capeData=" + this.capeData + ", geometryData=" + this.geometryData + ", animationData=" + this.animationData + ", premium=" + this.premium + ", persona=" + this.persona + ", capeOnClassic=" + this.capeOnClassic + ", capeId=" + this.capeId + ", fullSkinId=" + this.fullSkinId + ", armSize=" + this.armSize + ", skinColor=" + this.skinColor + ", personaPieces=" + this.personaPieces + ", tintColors=" + this.tintColors + ")";
        }
    }
}
