package org.cloudburstmc.protocol.bedrock.packet;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class ResourcePacksInfoPacket implements BedrockPacket {
    private boolean forcedToAccept;
    private boolean forcingServerPacksEnabled;
    private boolean hasAddonPacks;
    private boolean scriptingEnabled;
    private final List<Entry> behaviorPackInfos = new ObjectArrayList();
    private final List<Entry> resourcePackInfos = new ObjectArrayList();
    private List<CDNEntry> CDNEntries = new ObjectArrayList();

    public void setCDNEntries(List<CDNEntry> CDNEntries) {
        this.CDNEntries = CDNEntries;
    }

    public void setForcedToAccept(boolean forcedToAccept) {
        this.forcedToAccept = forcedToAccept;
    }

    public void setForcingServerPacksEnabled(boolean forcingServerPacksEnabled) {
        this.forcingServerPacksEnabled = forcingServerPacksEnabled;
    }

    public void setHasAddonPacks(boolean hasAddonPacks) {
        this.hasAddonPacks = hasAddonPacks;
    }

    public void setScriptingEnabled(boolean scriptingEnabled) {
        this.scriptingEnabled = scriptingEnabled;
    }

    protected boolean canEqual(Object other) {
        return other instanceof ResourcePacksInfoPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ResourcePacksInfoPacket)) {
            return false;
        }
        ResourcePacksInfoPacket other = (ResourcePacksInfoPacket) o;
        if (!other.canEqual(this) || this.forcedToAccept != other.forcedToAccept || this.hasAddonPacks != other.hasAddonPacks || this.scriptingEnabled != other.scriptingEnabled || this.forcingServerPacksEnabled != other.forcingServerPacksEnabled) {
            return false;
        }
        Object this$behaviorPackInfos = this.behaviorPackInfos;
        Object other$behaviorPackInfos = other.behaviorPackInfos;
        if (this$behaviorPackInfos != null ? !this$behaviorPackInfos.equals(other$behaviorPackInfos) : other$behaviorPackInfos != null) {
            return false;
        }
        Object this$resourcePackInfos = this.resourcePackInfos;
        Object other$resourcePackInfos = other.resourcePackInfos;
        if (this$resourcePackInfos != null ? !this$resourcePackInfos.equals(other$resourcePackInfos) : other$resourcePackInfos != null) {
            return false;
        }
        Object this$CDNEntries = this.CDNEntries;
        Object other$CDNEntries = other.CDNEntries;
        return this$CDNEntries != null ? this$CDNEntries.equals(other$CDNEntries) : other$CDNEntries == null;
    }

    public int hashCode() {
        int result = (1 * 59) + (this.forcedToAccept ? 79 : 97);
        int result2 = ((((result * 59) + (this.hasAddonPacks ? 79 : 97)) * 59) + (this.scriptingEnabled ? 79 : 97)) * 59;
        int i = this.forcingServerPacksEnabled ? 79 : 97;
        Object $behaviorPackInfos = this.behaviorPackInfos;
        int result3 = ((result2 + i) * 59) + ($behaviorPackInfos == null ? 43 : $behaviorPackInfos.hashCode());
        Object $resourcePackInfos = this.resourcePackInfos;
        int result4 = (result3 * 59) + ($resourcePackInfos == null ? 43 : $resourcePackInfos.hashCode());
        Object $CDNEntries = this.CDNEntries;
        return (result4 * 59) + ($CDNEntries != null ? $CDNEntries.hashCode() : 43);
    }

    public String toString() {
        return "ResourcePacksInfoPacket(behaviorPackInfos=" + this.behaviorPackInfos + ", resourcePackInfos=" + this.resourcePackInfos + ", forcedToAccept=" + this.forcedToAccept + ", hasAddonPacks=" + this.hasAddonPacks + ", scriptingEnabled=" + this.scriptingEnabled + ", forcingServerPacksEnabled=" + this.forcingServerPacksEnabled + ", CDNEntries=" + this.CDNEntries + ")";
    }

    public List<Entry> getBehaviorPackInfos() {
        return this.behaviorPackInfos;
    }

    public List<Entry> getResourcePackInfos() {
        return this.resourcePackInfos;
    }

    public boolean isForcedToAccept() {
        return this.forcedToAccept;
    }

    public boolean isHasAddonPacks() {
        return this.hasAddonPacks;
    }

    public boolean isScriptingEnabled() {
        return this.scriptingEnabled;
    }

    public boolean isForcingServerPacksEnabled() {
        return this.forcingServerPacksEnabled;
    }

    public List<CDNEntry> getCDNEntries() {
        return this.CDNEntries;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.RESOURCE_PACKS_INFO;
    }

    /* loaded from: classes5.dex */
    public static final class Entry {
        private final String contentId;
        private final String contentKey;
        private final String packId;
        private final long packSize;
        private final String packVersion;
        private final boolean raytracingCapable;
        private final boolean scripting;
        private final String subPackName;

        public Entry(String packId, String packVersion, long packSize, String contentKey, String subPackName, String contentId, boolean scripting, boolean raytracingCapable) {
            this.packId = packId;
            this.packVersion = packVersion;
            this.packSize = packSize;
            this.contentKey = contentKey;
            this.subPackName = subPackName;
            this.contentId = contentId;
            this.scripting = scripting;
            this.raytracingCapable = raytracingCapable;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof Entry)) {
                return false;
            }
            Entry other = (Entry) o;
            if (getPackSize() != other.getPackSize() || isScripting() != other.isScripting() || isRaytracingCapable() != other.isRaytracingCapable()) {
                return false;
            }
            Object this$packId = getPackId();
            Object other$packId = other.getPackId();
            if (this$packId != null ? !this$packId.equals(other$packId) : other$packId != null) {
                return false;
            }
            Object this$packVersion = getPackVersion();
            Object other$packVersion = other.getPackVersion();
            if (this$packVersion != null ? !this$packVersion.equals(other$packVersion) : other$packVersion != null) {
                return false;
            }
            Object this$contentKey = getContentKey();
            Object other$contentKey = other.getContentKey();
            if (this$contentKey != null ? !this$contentKey.equals(other$contentKey) : other$contentKey != null) {
                return false;
            }
            Object this$subPackName = getSubPackName();
            Object other$subPackName = other.getSubPackName();
            if (this$subPackName != null ? !this$subPackName.equals(other$subPackName) : other$subPackName != null) {
                return false;
            }
            Object this$contentId = getContentId();
            Object other$contentId = other.getContentId();
            return this$contentId != null ? this$contentId.equals(other$contentId) : other$contentId == null;
        }

        public int hashCode() {
            long $packSize = getPackSize();
            int result = (1 * 59) + ((int) (($packSize >>> 32) ^ $packSize));
            int result2 = ((result * 59) + (isScripting() ? 79 : 97)) * 59;
            int i = isRaytracingCapable() ? 79 : 97;
            Object $packId = getPackId();
            int result3 = ((result2 + i) * 59) + ($packId == null ? 43 : $packId.hashCode());
            Object $packVersion = getPackVersion();
            int result4 = (result3 * 59) + ($packVersion == null ? 43 : $packVersion.hashCode());
            Object $contentKey = getContentKey();
            int result5 = (result4 * 59) + ($contentKey == null ? 43 : $contentKey.hashCode());
            Object $subPackName = getSubPackName();
            int result6 = (result5 * 59) + ($subPackName == null ? 43 : $subPackName.hashCode());
            Object $contentId = getContentId();
            return (result6 * 59) + ($contentId != null ? $contentId.hashCode() : 43);
        }

        public String toString() {
            return "ResourcePacksInfoPacket.Entry(packId=" + getPackId() + ", packVersion=" + getPackVersion() + ", packSize=" + getPackSize() + ", contentKey=" + getContentKey() + ", subPackName=" + getSubPackName() + ", contentId=" + getContentId() + ", scripting=" + isScripting() + ", raytracingCapable=" + isRaytracingCapable() + ")";
        }

        public String getPackId() {
            return this.packId;
        }

        public String getPackVersion() {
            return this.packVersion;
        }

        public long getPackSize() {
            return this.packSize;
        }

        public String getContentKey() {
            return this.contentKey;
        }

        public String getSubPackName() {
            return this.subPackName;
        }

        public String getContentId() {
            return this.contentId;
        }

        public boolean isScripting() {
            return this.scripting;
        }

        public boolean isRaytracingCapable() {
            return this.raytracingCapable;
        }
    }

    /* loaded from: classes5.dex */
    public static final class CDNEntry {
        private final String packId;
        private final String remoteUrl;

        public CDNEntry(String packId, String remoteUrl) {
            this.packId = packId;
            this.remoteUrl = remoteUrl;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof CDNEntry)) {
                return false;
            }
            CDNEntry other = (CDNEntry) o;
            Object this$packId = getPackId();
            Object other$packId = other.getPackId();
            if (this$packId != null ? !this$packId.equals(other$packId) : other$packId != null) {
                return false;
            }
            Object this$remoteUrl = getRemoteUrl();
            Object other$remoteUrl = other.getRemoteUrl();
            return this$remoteUrl != null ? this$remoteUrl.equals(other$remoteUrl) : other$remoteUrl == null;
        }

        public int hashCode() {
            Object $packId = getPackId();
            int result = (1 * 59) + ($packId == null ? 43 : $packId.hashCode());
            Object $remoteUrl = getRemoteUrl();
            return (result * 59) + ($remoteUrl != null ? $remoteUrl.hashCode() : 43);
        }

        public String toString() {
            return "ResourcePacksInfoPacket.CDNEntry(packId=" + getPackId() + ", remoteUrl=" + getRemoteUrl() + ")";
        }

        public String getPackId() {
            return this.packId;
        }

        public String getRemoteUrl() {
            return this.remoteUrl;
        }
    }
}
