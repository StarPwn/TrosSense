package org.cloudburstmc.protocol.bedrock.packet;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.UUID;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.protocol.bedrock.data.AbilityLayer;
import org.cloudburstmc.protocol.bedrock.data.GameType;
import org.cloudburstmc.protocol.bedrock.data.PlayerAbilityHolder;
import org.cloudburstmc.protocol.bedrock.data.PlayerPermission;
import org.cloudburstmc.protocol.bedrock.data.command.CommandPermission;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataMap;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityLinkData;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityProperties;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class AddPlayerPacket implements BedrockPacket, PlayerAbilityHolder {
    private int buildPlatform;
    private String deviceId;
    private GameType gameType;
    private ItemData hand;
    private Vector3f motion;
    private String platformChatId;
    private Vector3f position;
    private Vector3f rotation;
    private long runtimeEntityId;
    private long uniqueEntityId;
    private String username;
    private UUID uuid;
    private final EntityDataMap metadata = new EntityDataMap();
    private final List<EntityLinkData> entityLinks = new ObjectArrayList();
    private final AdventureSettingsPacket adventureSettings = new AdventureSettingsPacket();
    private List<AbilityLayer> abilityLayers = new ObjectArrayList();
    private final EntityProperties properties = new EntityProperties();

    @Override // org.cloudburstmc.protocol.bedrock.data.PlayerAbilityHolder
    public void setAbilityLayers(List<AbilityLayer> abilityLayers) {
        this.abilityLayers = abilityLayers;
    }

    public void setBuildPlatform(int buildPlatform) {
        this.buildPlatform = buildPlatform;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public void setHand(ItemData hand) {
        this.hand = hand;
    }

    public void setMotion(Vector3f motion) {
        this.motion = motion;
    }

    public void setPlatformChatId(String platformChatId) {
        this.platformChatId = platformChatId;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }

    public void setRuntimeEntityId(long runtimeEntityId) {
        this.runtimeEntityId = runtimeEntityId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    protected boolean canEqual(Object other) {
        return other instanceof AddPlayerPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AddPlayerPacket)) {
            return false;
        }
        AddPlayerPacket other = (AddPlayerPacket) o;
        if (!other.canEqual(this) || this.uniqueEntityId != other.uniqueEntityId || this.runtimeEntityId != other.runtimeEntityId || this.buildPlatform != other.buildPlatform) {
            return false;
        }
        Object this$metadata = this.metadata;
        Object other$metadata = other.metadata;
        if (this$metadata != null ? !this$metadata.equals(other$metadata) : other$metadata != null) {
            return false;
        }
        Object this$entityLinks = this.entityLinks;
        Object other$entityLinks = other.entityLinks;
        if (this$entityLinks != null ? !this$entityLinks.equals(other$entityLinks) : other$entityLinks != null) {
            return false;
        }
        Object this$uuid = this.uuid;
        Object other$uuid = other.uuid;
        if (this$uuid != null ? !this$uuid.equals(other$uuid) : other$uuid != null) {
            return false;
        }
        Object this$username = this.username;
        Object other$username = other.username;
        if (this$username != null ? !this$username.equals(other$username) : other$username != null) {
            return false;
        }
        Object this$platformChatId = this.platformChatId;
        Object other$platformChatId = other.platformChatId;
        if (this$platformChatId != null ? !this$platformChatId.equals(other$platformChatId) : other$platformChatId != null) {
            return false;
        }
        Object this$position = this.position;
        Object other$position = other.position;
        if (this$position != null ? !this$position.equals(other$position) : other$position != null) {
            return false;
        }
        Object this$motion = this.motion;
        Object other$motion = other.motion;
        if (this$motion == null) {
            if (other$motion != null) {
                return false;
            }
        } else if (!this$motion.equals(other$motion)) {
            return false;
        }
        Object other$motion2 = this.rotation;
        Object other$position2 = other.rotation;
        if (other$motion2 == null) {
            if (other$position2 != null) {
                return false;
            }
        } else if (!other$motion2.equals(other$position2)) {
            return false;
        }
        Object this$rotation = this.hand;
        Object other$rotation = other.hand;
        if (this$rotation == null) {
            if (other$rotation != null) {
                return false;
            }
        } else if (!this$rotation.equals(other$rotation)) {
            return false;
        }
        Object this$hand = this.adventureSettings;
        Object other$hand = other.adventureSettings;
        if (this$hand == null) {
            if (other$hand != null) {
                return false;
            }
        } else if (!this$hand.equals(other$hand)) {
            return false;
        }
        Object this$adventureSettings = this.deviceId;
        Object other$adventureSettings = other.deviceId;
        if (this$adventureSettings == null) {
            if (other$adventureSettings != null) {
                return false;
            }
        } else if (!this$adventureSettings.equals(other$adventureSettings)) {
            return false;
        }
        Object this$deviceId = this.gameType;
        Object other$deviceId = other.gameType;
        if (this$deviceId == null) {
            if (other$deviceId != null) {
                return false;
            }
        } else if (!this$deviceId.equals(other$deviceId)) {
            return false;
        }
        Object this$gameType = this.abilityLayers;
        Object other$gameType = other.abilityLayers;
        if (this$gameType == null) {
            if (other$gameType != null) {
                return false;
            }
        } else if (!this$gameType.equals(other$gameType)) {
            return false;
        }
        Object this$abilityLayers = this.properties;
        Object other$properties = other.properties;
        return this$abilityLayers == null ? other$properties == null : this$abilityLayers.equals(other$properties);
    }

    public int hashCode() {
        long $uniqueEntityId = this.uniqueEntityId;
        int result = (1 * 59) + ((int) (($uniqueEntityId >>> 32) ^ $uniqueEntityId));
        long $runtimeEntityId = this.runtimeEntityId;
        int result2 = (((result * 59) + ((int) (($runtimeEntityId >>> 32) ^ $runtimeEntityId))) * 59) + this.buildPlatform;
        Object $metadata = this.metadata;
        int result3 = (result2 * 59) + ($metadata == null ? 43 : $metadata.hashCode());
        Object $entityLinks = this.entityLinks;
        int result4 = (result3 * 59) + ($entityLinks == null ? 43 : $entityLinks.hashCode());
        Object $uuid = this.uuid;
        int result5 = (result4 * 59) + ($uuid == null ? 43 : $uuid.hashCode());
        Object $username = this.username;
        int result6 = (result5 * 59) + ($username == null ? 43 : $username.hashCode());
        Object $platformChatId = this.platformChatId;
        int result7 = (result6 * 59) + ($platformChatId == null ? 43 : $platformChatId.hashCode());
        Object $position = this.position;
        int result8 = (result7 * 59) + ($position == null ? 43 : $position.hashCode());
        Object $motion = this.motion;
        int result9 = (result8 * 59) + ($motion == null ? 43 : $motion.hashCode());
        Object $rotation = this.rotation;
        int result10 = (result9 * 59) + ($rotation == null ? 43 : $rotation.hashCode());
        Object $hand = this.hand;
        int result11 = (result10 * 59) + ($hand == null ? 43 : $hand.hashCode());
        Object $adventureSettings = this.adventureSettings;
        int result12 = (result11 * 59) + ($adventureSettings == null ? 43 : $adventureSettings.hashCode());
        Object $deviceId = this.deviceId;
        int result13 = (result12 * 59) + ($deviceId == null ? 43 : $deviceId.hashCode());
        Object $deviceId2 = this.gameType;
        int result14 = (result13 * 59) + ($deviceId2 == null ? 43 : $deviceId2.hashCode());
        Object $gameType = this.abilityLayers;
        int result15 = (result14 * 59) + ($gameType == null ? 43 : $gameType.hashCode());
        Object $abilityLayers = this.properties;
        return (result15 * 59) + ($abilityLayers == null ? 43 : $abilityLayers.hashCode());
    }

    public String toString() {
        return "AddPlayerPacket(metadata=" + this.metadata + ", entityLinks=" + this.entityLinks + ", uuid=" + this.uuid + ", username=" + this.username + ", uniqueEntityId=" + this.uniqueEntityId + ", runtimeEntityId=" + this.runtimeEntityId + ", platformChatId=" + this.platformChatId + ", position=" + this.position + ", motion=" + this.motion + ", rotation=" + this.rotation + ", hand=" + this.hand + ", adventureSettings=" + this.adventureSettings + ", deviceId=" + this.deviceId + ", buildPlatform=" + this.buildPlatform + ", gameType=" + this.gameType + ", abilityLayers=" + this.abilityLayers + ", properties=" + this.properties + ")";
    }

    public EntityDataMap getMetadata() {
        return this.metadata;
    }

    public List<EntityLinkData> getEntityLinks() {
        return this.entityLinks;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public String getUsername() {
        return this.username;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.PlayerAbilityHolder
    public long getUniqueEntityId() {
        return this.uniqueEntityId;
    }

    public long getRuntimeEntityId() {
        return this.runtimeEntityId;
    }

    public String getPlatformChatId() {
        return this.platformChatId;
    }

    public Vector3f getPosition() {
        return this.position;
    }

    public Vector3f getMotion() {
        return this.motion;
    }

    public Vector3f getRotation() {
        return this.rotation;
    }

    public ItemData getHand() {
        return this.hand;
    }

    public AdventureSettingsPacket getAdventureSettings() {
        return this.adventureSettings;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public int getBuildPlatform() {
        return this.buildPlatform;
    }

    public GameType getGameType() {
        return this.gameType;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.PlayerAbilityHolder
    public List<AbilityLayer> getAbilityLayers() {
        return this.abilityLayers;
    }

    public EntityProperties getProperties() {
        return this.properties;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.PlayerAbilityHolder
    public void setUniqueEntityId(long uniqueEntityId) {
        this.uniqueEntityId = uniqueEntityId;
        this.adventureSettings.setUniqueEntityId(uniqueEntityId);
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.PlayerAbilityHolder
    public PlayerPermission getPlayerPermission() {
        return this.adventureSettings.getPlayerPermission();
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.PlayerAbilityHolder
    public void setPlayerPermission(PlayerPermission playerPermission) {
        this.adventureSettings.setPlayerPermission(playerPermission);
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.PlayerAbilityHolder
    public CommandPermission getCommandPermission() {
        return this.adventureSettings.getCommandPermission();
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.PlayerAbilityHolder
    public void setCommandPermission(CommandPermission commandPermission) {
        this.adventureSettings.setCommandPermission(commandPermission);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.ADD_PLAYER;
    }
}
