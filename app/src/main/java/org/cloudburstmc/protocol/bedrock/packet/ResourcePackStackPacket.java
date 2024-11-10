package org.cloudburstmc.protocol.bedrock.packet;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import org.cloudburstmc.protocol.bedrock.data.ExperimentData;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class ResourcePackStackPacket implements BedrockPacket {
    private boolean experimentsPreviouslyToggled;
    private boolean forcedToAccept;
    private String gameVersion;
    private final List<Entry> behaviorPacks = new ObjectArrayList();
    private final List<Entry> resourcePacks = new ObjectArrayList();
    private final List<ExperimentData> experiments = new ObjectArrayList();

    public void setExperimentsPreviouslyToggled(boolean experimentsPreviouslyToggled) {
        this.experimentsPreviouslyToggled = experimentsPreviouslyToggled;
    }

    public void setForcedToAccept(boolean forcedToAccept) {
        this.forcedToAccept = forcedToAccept;
    }

    public void setGameVersion(String gameVersion) {
        this.gameVersion = gameVersion;
    }

    protected boolean canEqual(Object other) {
        return other instanceof ResourcePackStackPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ResourcePackStackPacket)) {
            return false;
        }
        ResourcePackStackPacket other = (ResourcePackStackPacket) o;
        if (!other.canEqual(this) || this.forcedToAccept != other.forcedToAccept || this.experimentsPreviouslyToggled != other.experimentsPreviouslyToggled) {
            return false;
        }
        Object this$behaviorPacks = this.behaviorPacks;
        Object other$behaviorPacks = other.behaviorPacks;
        if (this$behaviorPacks != null ? !this$behaviorPacks.equals(other$behaviorPacks) : other$behaviorPacks != null) {
            return false;
        }
        Object this$resourcePacks = this.resourcePacks;
        Object other$resourcePacks = other.resourcePacks;
        if (this$resourcePacks != null ? !this$resourcePacks.equals(other$resourcePacks) : other$resourcePacks != null) {
            return false;
        }
        Object this$gameVersion = this.gameVersion;
        Object other$gameVersion = other.gameVersion;
        if (this$gameVersion != null ? !this$gameVersion.equals(other$gameVersion) : other$gameVersion != null) {
            return false;
        }
        Object this$experiments = this.experiments;
        Object other$experiments = other.experiments;
        return this$experiments != null ? this$experiments.equals(other$experiments) : other$experiments == null;
    }

    public int hashCode() {
        int result = (1 * 59) + (this.forcedToAccept ? 79 : 97);
        int result2 = result * 59;
        int i = this.experimentsPreviouslyToggled ? 79 : 97;
        Object $behaviorPacks = this.behaviorPacks;
        int result3 = ((result2 + i) * 59) + ($behaviorPacks == null ? 43 : $behaviorPacks.hashCode());
        Object $resourcePacks = this.resourcePacks;
        int result4 = (result3 * 59) + ($resourcePacks == null ? 43 : $resourcePacks.hashCode());
        Object $gameVersion = this.gameVersion;
        int result5 = (result4 * 59) + ($gameVersion == null ? 43 : $gameVersion.hashCode());
        Object $experiments = this.experiments;
        return (result5 * 59) + ($experiments != null ? $experiments.hashCode() : 43);
    }

    public String toString() {
        return "ResourcePackStackPacket(forcedToAccept=" + this.forcedToAccept + ", behaviorPacks=" + this.behaviorPacks + ", resourcePacks=" + this.resourcePacks + ", gameVersion=" + this.gameVersion + ", experiments=" + this.experiments + ", experimentsPreviouslyToggled=" + this.experimentsPreviouslyToggled + ")";
    }

    public boolean isForcedToAccept() {
        return this.forcedToAccept;
    }

    public List<Entry> getBehaviorPacks() {
        return this.behaviorPacks;
    }

    public List<Entry> getResourcePacks() {
        return this.resourcePacks;
    }

    public String getGameVersion() {
        return this.gameVersion;
    }

    public List<ExperimentData> getExperiments() {
        return this.experiments;
    }

    public boolean isExperimentsPreviouslyToggled() {
        return this.experimentsPreviouslyToggled;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.RESOURCE_PACK_STACK;
    }

    /* loaded from: classes5.dex */
    public static final class Entry {
        private final String packId;
        private final String packVersion;
        private final String subPackName;

        public Entry(String packId, String packVersion, String subPackName) {
            this.packId = packId;
            this.packVersion = packVersion;
            this.subPackName = subPackName;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof Entry)) {
                return false;
            }
            Entry other = (Entry) o;
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
            Object this$subPackName = getSubPackName();
            Object other$subPackName = other.getSubPackName();
            return this$subPackName != null ? this$subPackName.equals(other$subPackName) : other$subPackName == null;
        }

        public int hashCode() {
            Object $packId = getPackId();
            int result = (1 * 59) + ($packId == null ? 43 : $packId.hashCode());
            Object $packVersion = getPackVersion();
            int result2 = (result * 59) + ($packVersion == null ? 43 : $packVersion.hashCode());
            Object $subPackName = getSubPackName();
            return (result2 * 59) + ($subPackName != null ? $subPackName.hashCode() : 43);
        }

        public String toString() {
            return "ResourcePackStackPacket.Entry(packId=" + getPackId() + ", packVersion=" + getPackVersion() + ", subPackName=" + getSubPackName() + ")";
        }

        public String getPackId() {
            return this.packId;
        }

        public String getPackVersion() {
            return this.packVersion;
        }

        public String getSubPackName() {
            return this.subPackName;
        }
    }
}
