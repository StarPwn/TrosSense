package org.cloudburstmc.protocol.bedrock.packet;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.UUID;
import org.cloudburstmc.protocol.bedrock.data.skin.SerializedSkin;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class PlayerListPacket implements BedrockPacket {
    private Action action;
    private final List<Entry> entries = new ObjectArrayList();

    /* loaded from: classes5.dex */
    public enum Action {
        ADD,
        REMOVE
    }

    public void setAction(Action action) {
        this.action = action;
    }

    protected boolean canEqual(Object other) {
        return other instanceof PlayerListPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PlayerListPacket)) {
            return false;
        }
        PlayerListPacket other = (PlayerListPacket) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$entries = this.entries;
        Object other$entries = other.entries;
        if (this$entries != null ? !this$entries.equals(other$entries) : other$entries != null) {
            return false;
        }
        Object this$action = this.action;
        Object other$action = other.action;
        return this$action != null ? this$action.equals(other$action) : other$action == null;
    }

    public int hashCode() {
        Object $entries = this.entries;
        int result = (1 * 59) + ($entries == null ? 43 : $entries.hashCode());
        Object $action = this.action;
        return (result * 59) + ($action != null ? $action.hashCode() : 43);
    }

    public String toString() {
        return "PlayerListPacket(entries=" + this.entries + ", action=" + this.action + ")";
    }

    public List<Entry> getEntries() {
        return this.entries;
    }

    public Action getAction() {
        return this.action;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.PLAYER_LIST;
    }

    /* loaded from: classes5.dex */
    public static final class Entry {
        private int buildPlatform;
        private long entityId;
        private boolean host;
        private String name;
        private String platformChatId;
        private SerializedSkin skin;
        private boolean subClient;
        private boolean teacher;
        private boolean trustedSkin;
        private final UUID uuid;
        private String xuid;

        public Entry(UUID uuid) {
            this.uuid = uuid;
        }

        public void setBuildPlatform(int buildPlatform) {
            this.buildPlatform = buildPlatform;
        }

        public void setEntityId(long entityId) {
            this.entityId = entityId;
        }

        public void setHost(boolean host) {
            this.host = host;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setPlatformChatId(String platformChatId) {
            this.platformChatId = platformChatId;
        }

        public void setSkin(SerializedSkin skin) {
            this.skin = skin;
        }

        public void setSubClient(boolean subClient) {
            this.subClient = subClient;
        }

        public void setTeacher(boolean teacher) {
            this.teacher = teacher;
        }

        public void setTrustedSkin(boolean trustedSkin) {
            this.trustedSkin = trustedSkin;
        }

        public void setXuid(String xuid) {
            this.xuid = xuid;
        }

        public String toString() {
            return "PlayerListPacket.Entry(uuid=" + this.uuid + ", entityId=" + this.entityId + ", name=" + this.name + ", xuid=" + this.xuid + ", platformChatId=" + this.platformChatId + ", buildPlatform=" + this.buildPlatform + ", skin=" + this.skin + ", teacher=" + this.teacher + ", host=" + this.host + ", trustedSkin=" + this.trustedSkin + ", subClient=" + this.subClient + ")";
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof Entry)) {
                return false;
            }
            Entry other = (Entry) o;
            if (this.entityId != other.entityId || this.buildPlatform != other.buildPlatform || this.teacher != other.teacher || this.host != other.host || this.trustedSkin != other.trustedSkin || this.subClient != other.subClient) {
                return false;
            }
            Object this$uuid = this.uuid;
            Object other$uuid = other.uuid;
            if (this$uuid != null ? !this$uuid.equals(other$uuid) : other$uuid != null) {
                return false;
            }
            Object this$name = this.name;
            Object other$name = other.name;
            if (this$name != null ? !this$name.equals(other$name) : other$name != null) {
                return false;
            }
            Object this$xuid = this.xuid;
            Object other$xuid = other.xuid;
            if (this$xuid != null ? !this$xuid.equals(other$xuid) : other$xuid != null) {
                return false;
            }
            Object this$platformChatId = this.platformChatId;
            Object other$platformChatId = other.platformChatId;
            if (this$platformChatId != null ? !this$platformChatId.equals(other$platformChatId) : other$platformChatId != null) {
                return false;
            }
            Object this$skin = this.skin;
            Object other$skin = other.skin;
            return this$skin != null ? this$skin.equals(other$skin) : other$skin == null;
        }

        public int hashCode() {
            long $entityId = this.entityId;
            int result = (1 * 59) + ((int) (($entityId >>> 32) ^ $entityId));
            int result2 = ((((((((result * 59) + this.buildPlatform) * 59) + (this.teacher ? 79 : 97)) * 59) + (this.host ? 79 : 97)) * 59) + (this.trustedSkin ? 79 : 97)) * 59;
            int i = this.subClient ? 79 : 97;
            Object $uuid = this.uuid;
            int result3 = ((result2 + i) * 59) + ($uuid == null ? 43 : $uuid.hashCode());
            Object $name = this.name;
            int result4 = (result3 * 59) + ($name == null ? 43 : $name.hashCode());
            Object $xuid = this.xuid;
            int result5 = (result4 * 59) + ($xuid == null ? 43 : $xuid.hashCode());
            Object $platformChatId = this.platformChatId;
            int result6 = (result5 * 59) + ($platformChatId == null ? 43 : $platformChatId.hashCode());
            Object $skin = this.skin;
            return (result6 * 59) + ($skin != null ? $skin.hashCode() : 43);
        }

        public UUID getUuid() {
            return this.uuid;
        }

        public long getEntityId() {
            return this.entityId;
        }

        public String getName() {
            return this.name;
        }

        public String getXuid() {
            return this.xuid;
        }

        public String getPlatformChatId() {
            return this.platformChatId;
        }

        public int getBuildPlatform() {
            return this.buildPlatform;
        }

        public SerializedSkin getSkin() {
            return this.skin;
        }

        public boolean isTeacher() {
            return this.teacher;
        }

        public boolean isHost() {
            return this.host;
        }

        public boolean isTrustedSkin() {
            return this.trustedSkin;
        }

        public boolean isSubClient() {
            return this.subClient;
        }
    }
}
