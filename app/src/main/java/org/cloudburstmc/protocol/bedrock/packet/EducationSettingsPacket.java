package org.cloudburstmc.protocol.bedrock.packet;

import java.util.Optional;
import org.cloudburstmc.protocol.common.PacketSignal;
import org.cloudburstmc.protocol.common.util.OptionalBoolean;

/* loaded from: classes5.dex */
public class EducationSettingsPacket implements BedrockPacket {
    private boolean canResizeCodeBuilder;
    private String codeBuilderTitle;
    private String codeBuilderUri;
    private boolean disableLegacyTitle;
    private OptionalBoolean entityCapabilities;
    private OptionalBoolean externalLinkSettings;
    private Optional<String> overrideUri;
    private String postProcessFilter;
    private boolean quizAttached;
    private String screenshotBorderPath;

    public void setCanResizeCodeBuilder(boolean canResizeCodeBuilder) {
        this.canResizeCodeBuilder = canResizeCodeBuilder;
    }

    public void setCodeBuilderTitle(String codeBuilderTitle) {
        this.codeBuilderTitle = codeBuilderTitle;
    }

    public void setCodeBuilderUri(String codeBuilderUri) {
        this.codeBuilderUri = codeBuilderUri;
    }

    public void setDisableLegacyTitle(boolean disableLegacyTitle) {
        this.disableLegacyTitle = disableLegacyTitle;
    }

    public void setEntityCapabilities(OptionalBoolean entityCapabilities) {
        this.entityCapabilities = entityCapabilities;
    }

    public void setExternalLinkSettings(OptionalBoolean externalLinkSettings) {
        this.externalLinkSettings = externalLinkSettings;
    }

    public void setOverrideUri(Optional<String> overrideUri) {
        this.overrideUri = overrideUri;
    }

    public void setPostProcessFilter(String postProcessFilter) {
        this.postProcessFilter = postProcessFilter;
    }

    public void setQuizAttached(boolean quizAttached) {
        this.quizAttached = quizAttached;
    }

    public void setScreenshotBorderPath(String screenshotBorderPath) {
        this.screenshotBorderPath = screenshotBorderPath;
    }

    protected boolean canEqual(Object other) {
        return other instanceof EducationSettingsPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof EducationSettingsPacket)) {
            return false;
        }
        EducationSettingsPacket other = (EducationSettingsPacket) o;
        if (!other.canEqual(this) || this.canResizeCodeBuilder != other.canResizeCodeBuilder || this.disableLegacyTitle != other.disableLegacyTitle || this.quizAttached != other.quizAttached) {
            return false;
        }
        Object this$codeBuilderUri = this.codeBuilderUri;
        Object other$codeBuilderUri = other.codeBuilderUri;
        if (this$codeBuilderUri != null ? !this$codeBuilderUri.equals(other$codeBuilderUri) : other$codeBuilderUri != null) {
            return false;
        }
        Object this$codeBuilderTitle = this.codeBuilderTitle;
        Object other$codeBuilderTitle = other.codeBuilderTitle;
        if (this$codeBuilderTitle != null ? !this$codeBuilderTitle.equals(other$codeBuilderTitle) : other$codeBuilderTitle != null) {
            return false;
        }
        Object this$postProcessFilter = this.postProcessFilter;
        Object other$postProcessFilter = other.postProcessFilter;
        if (this$postProcessFilter != null ? !this$postProcessFilter.equals(other$postProcessFilter) : other$postProcessFilter != null) {
            return false;
        }
        Object this$screenshotBorderPath = this.screenshotBorderPath;
        Object other$screenshotBorderPath = other.screenshotBorderPath;
        if (this$screenshotBorderPath != null ? !this$screenshotBorderPath.equals(other$screenshotBorderPath) : other$screenshotBorderPath != null) {
            return false;
        }
        Object this$entityCapabilities = this.entityCapabilities;
        Object other$entityCapabilities = other.entityCapabilities;
        if (this$entityCapabilities != null ? !this$entityCapabilities.equals(other$entityCapabilities) : other$entityCapabilities != null) {
            return false;
        }
        Object this$overrideUri = this.overrideUri;
        Object other$overrideUri = other.overrideUri;
        if (this$overrideUri != null ? !this$overrideUri.equals(other$overrideUri) : other$overrideUri != null) {
            return false;
        }
        Object this$externalLinkSettings = this.externalLinkSettings;
        Object other$externalLinkSettings = other.externalLinkSettings;
        return this$externalLinkSettings == null ? other$externalLinkSettings == null : this$externalLinkSettings.equals(other$externalLinkSettings);
    }

    public int hashCode() {
        int result = (1 * 59) + (this.canResizeCodeBuilder ? 79 : 97);
        int result2 = ((result * 59) + (this.disableLegacyTitle ? 79 : 97)) * 59;
        int i = this.quizAttached ? 79 : 97;
        Object $codeBuilderUri = this.codeBuilderUri;
        int result3 = ((result2 + i) * 59) + ($codeBuilderUri == null ? 43 : $codeBuilderUri.hashCode());
        Object $codeBuilderTitle = this.codeBuilderTitle;
        int result4 = (result3 * 59) + ($codeBuilderTitle == null ? 43 : $codeBuilderTitle.hashCode());
        Object $postProcessFilter = this.postProcessFilter;
        int result5 = (result4 * 59) + ($postProcessFilter == null ? 43 : $postProcessFilter.hashCode());
        Object $screenshotBorderPath = this.screenshotBorderPath;
        int result6 = (result5 * 59) + ($screenshotBorderPath == null ? 43 : $screenshotBorderPath.hashCode());
        Object $entityCapabilities = this.entityCapabilities;
        int result7 = (result6 * 59) + ($entityCapabilities == null ? 43 : $entityCapabilities.hashCode());
        Object $overrideUri = this.overrideUri;
        int result8 = (result7 * 59) + ($overrideUri == null ? 43 : $overrideUri.hashCode());
        Object $externalLinkSettings = this.externalLinkSettings;
        return (result8 * 59) + ($externalLinkSettings != null ? $externalLinkSettings.hashCode() : 43);
    }

    public String toString() {
        return "EducationSettingsPacket(codeBuilderUri=" + this.codeBuilderUri + ", codeBuilderTitle=" + this.codeBuilderTitle + ", canResizeCodeBuilder=" + this.canResizeCodeBuilder + ", disableLegacyTitle=" + this.disableLegacyTitle + ", postProcessFilter=" + this.postProcessFilter + ", screenshotBorderPath=" + this.screenshotBorderPath + ", entityCapabilities=" + this.entityCapabilities + ", overrideUri=" + this.overrideUri + ", quizAttached=" + this.quizAttached + ", externalLinkSettings=" + this.externalLinkSettings + ")";
    }

    public String getCodeBuilderUri() {
        return this.codeBuilderUri;
    }

    public String getCodeBuilderTitle() {
        return this.codeBuilderTitle;
    }

    public boolean isCanResizeCodeBuilder() {
        return this.canResizeCodeBuilder;
    }

    public boolean isDisableLegacyTitle() {
        return this.disableLegacyTitle;
    }

    public String getPostProcessFilter() {
        return this.postProcessFilter;
    }

    public String getScreenshotBorderPath() {
        return this.screenshotBorderPath;
    }

    public OptionalBoolean getEntityCapabilities() {
        return this.entityCapabilities;
    }

    public Optional<String> getOverrideUri() {
        return this.overrideUri;
    }

    public boolean isQuizAttached() {
        return this.quizAttached;
    }

    public OptionalBoolean getExternalLinkSettings() {
        return this.externalLinkSettings;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.EDUCATION_SETTINGS;
    }
}
