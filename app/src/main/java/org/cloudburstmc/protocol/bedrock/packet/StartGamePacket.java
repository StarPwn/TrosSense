package org.cloudburstmc.protocol.bedrock.packet;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.UUID;
import org.cloudburstmc.math.vector.Vector2f;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.nbt.NbtList;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.protocol.bedrock.data.AuthoritativeMovementMode;
import org.cloudburstmc.protocol.bedrock.data.BlockPropertyData;
import org.cloudburstmc.protocol.bedrock.data.ChatRestrictionLevel;
import org.cloudburstmc.protocol.bedrock.data.EduSharedUriResource;
import org.cloudburstmc.protocol.bedrock.data.ExperimentData;
import org.cloudburstmc.protocol.bedrock.data.GamePublishSetting;
import org.cloudburstmc.protocol.bedrock.data.GameRuleData;
import org.cloudburstmc.protocol.bedrock.data.GameType;
import org.cloudburstmc.protocol.bedrock.data.NetworkPermissions;
import org.cloudburstmc.protocol.bedrock.data.PlayerPermission;
import org.cloudburstmc.protocol.bedrock.data.SpawnBiomeType;
import org.cloudburstmc.protocol.bedrock.data.definitions.ItemDefinition;
import org.cloudburstmc.protocol.common.PacketSignal;
import org.cloudburstmc.protocol.common.util.OptionalBoolean;

/* loaded from: classes5.dex */
public class StartGamePacket implements BedrockPacket {
    private static final InternalLogger log = InternalLoggerFactory.getInstance((Class<?>) StartGamePacket.class);
    private boolean achievementsDisabled;
    private AuthoritativeMovementMode authoritativeMovementMode;
    private boolean behaviorPackLocked;
    private boolean blockNetworkIdsHashed;
    private NbtList<NbtMap> blockPalette;
    private long blockRegistryChecksum;
    private boolean bonusChestEnabled;
    private boolean broadcastingToLan;
    private ChatRestrictionLevel chatRestrictionLevel;
    private boolean clientSideGenerationEnabled;
    private boolean commandsEnabled;
    private boolean createdInEditor;
    private long currentTick;
    private String customBiomeName;
    private int dayCycleStopTime;
    private PlayerPermission defaultPlayerPermission;
    private Vector3i defaultSpawn;
    private int difficulty;
    private int dimensionId;
    private boolean disablingCustomSkins;
    private boolean disablingPersonas;
    private boolean disablingPlayerInteractions;
    private int eduEditionOffers;
    private boolean eduFeaturesEnabled;
    private String educationProductionId;
    private boolean emoteChatMuted;
    private int enchantmentSeed;
    private boolean experimentsPreviouslyToggled;
    private boolean exportedFromEditor;
    private OptionalBoolean forceExperimentalGameplay;
    private boolean fromLockedWorldTemplate;
    private boolean fromWorldTemplate;
    private int generatorId;
    private boolean inventoriesServerAuthoritative;
    private GameType levelGameType;
    private String levelId;
    private String levelName;
    private float lightningLevel;
    private int limitedWorldHeight;
    private int limitedWorldWidth;
    private String multiplayerCorrelationId;
    private boolean multiplayerGame;
    private boolean netherType;
    private boolean onlySpawningV1Villagers;
    private GamePublishSetting platformBroadcastMode;
    private boolean platformLockedContentConfirmed;
    private GameType playerGameType;
    private Vector3f playerPosition;
    private NbtMap playerPropertyData;
    private String premiumWorldTemplateId;
    private float rainLevel;
    private boolean resourcePackLocked;
    private int rewindHistorySize;
    private Vector2f rotation;
    private long runtimeEntityId;
    private long seed;
    boolean serverAuthoritativeBlockBreaking;
    private int serverChunkTickRange;
    private String serverEngine;
    private SpawnBiomeType spawnBiomeType;
    private boolean startingWithMap;
    private boolean texturePacksRequired;
    private boolean trial;
    private boolean trustingPlayers;
    private long uniqueEntityId;
    private boolean usingMsaGamertagsOnly;
    private String vanillaVersion;
    private boolean worldEditor;
    private UUID worldTemplateId;
    private boolean worldTemplateOptionLocked;
    private GamePublishSetting xblBroadcastMode;
    private final List<GameRuleData<?>> gamerules = new ObjectArrayList();
    private final List<ExperimentData> experiments = new ObjectArrayList();
    private EduSharedUriResource eduSharedUriResource = EduSharedUriResource.EMPTY;
    private final List<BlockPropertyData> blockProperties = new ObjectArrayList();
    private List<ItemDefinition> itemDefinitions = new ObjectArrayList();
    private NetworkPermissions networkPermissions = NetworkPermissions.DEFAULT;

    public void setAchievementsDisabled(boolean achievementsDisabled) {
        this.achievementsDisabled = achievementsDisabled;
    }

    public void setAuthoritativeMovementMode(AuthoritativeMovementMode authoritativeMovementMode) {
        this.authoritativeMovementMode = authoritativeMovementMode;
    }

    public void setBehaviorPackLocked(boolean behaviorPackLocked) {
        this.behaviorPackLocked = behaviorPackLocked;
    }

    public void setBlockNetworkIdsHashed(boolean blockNetworkIdsHashed) {
        this.blockNetworkIdsHashed = blockNetworkIdsHashed;
    }

    public void setBlockPalette(NbtList<NbtMap> blockPalette) {
        this.blockPalette = blockPalette;
    }

    public void setBlockRegistryChecksum(long blockRegistryChecksum) {
        this.blockRegistryChecksum = blockRegistryChecksum;
    }

    public void setBonusChestEnabled(boolean bonusChestEnabled) {
        this.bonusChestEnabled = bonusChestEnabled;
    }

    public void setBroadcastingToLan(boolean broadcastingToLan) {
        this.broadcastingToLan = broadcastingToLan;
    }

    public void setChatRestrictionLevel(ChatRestrictionLevel chatRestrictionLevel) {
        this.chatRestrictionLevel = chatRestrictionLevel;
    }

    public void setClientSideGenerationEnabled(boolean clientSideGenerationEnabled) {
        this.clientSideGenerationEnabled = clientSideGenerationEnabled;
    }

    public void setCommandsEnabled(boolean commandsEnabled) {
        this.commandsEnabled = commandsEnabled;
    }

    public void setCreatedInEditor(boolean createdInEditor) {
        this.createdInEditor = createdInEditor;
    }

    public void setCurrentTick(long currentTick) {
        this.currentTick = currentTick;
    }

    public void setCustomBiomeName(String customBiomeName) {
        this.customBiomeName = customBiomeName;
    }

    public void setDayCycleStopTime(int dayCycleStopTime) {
        this.dayCycleStopTime = dayCycleStopTime;
    }

    public void setDefaultPlayerPermission(PlayerPermission defaultPlayerPermission) {
        this.defaultPlayerPermission = defaultPlayerPermission;
    }

    public void setDefaultSpawn(Vector3i defaultSpawn) {
        this.defaultSpawn = defaultSpawn;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public void setDimensionId(int dimensionId) {
        this.dimensionId = dimensionId;
    }

    public void setDisablingCustomSkins(boolean disablingCustomSkins) {
        this.disablingCustomSkins = disablingCustomSkins;
    }

    public void setDisablingPersonas(boolean disablingPersonas) {
        this.disablingPersonas = disablingPersonas;
    }

    public void setDisablingPlayerInteractions(boolean disablingPlayerInteractions) {
        this.disablingPlayerInteractions = disablingPlayerInteractions;
    }

    public void setEduEditionOffers(int eduEditionOffers) {
        this.eduEditionOffers = eduEditionOffers;
    }

    public void setEduFeaturesEnabled(boolean eduFeaturesEnabled) {
        this.eduFeaturesEnabled = eduFeaturesEnabled;
    }

    public void setEduSharedUriResource(EduSharedUriResource eduSharedUriResource) {
        this.eduSharedUriResource = eduSharedUriResource;
    }

    public void setEducationProductionId(String educationProductionId) {
        this.educationProductionId = educationProductionId;
    }

    public void setEmoteChatMuted(boolean emoteChatMuted) {
        this.emoteChatMuted = emoteChatMuted;
    }

    public void setEnchantmentSeed(int enchantmentSeed) {
        this.enchantmentSeed = enchantmentSeed;
    }

    public void setExperimentsPreviouslyToggled(boolean experimentsPreviouslyToggled) {
        this.experimentsPreviouslyToggled = experimentsPreviouslyToggled;
    }

    public void setExportedFromEditor(boolean exportedFromEditor) {
        this.exportedFromEditor = exportedFromEditor;
    }

    public void setForceExperimentalGameplay(OptionalBoolean forceExperimentalGameplay) {
        this.forceExperimentalGameplay = forceExperimentalGameplay;
    }

    public void setFromLockedWorldTemplate(boolean fromLockedWorldTemplate) {
        this.fromLockedWorldTemplate = fromLockedWorldTemplate;
    }

    public void setFromWorldTemplate(boolean fromWorldTemplate) {
        this.fromWorldTemplate = fromWorldTemplate;
    }

    public void setGeneratorId(int generatorId) {
        this.generatorId = generatorId;
    }

    public void setInventoriesServerAuthoritative(boolean inventoriesServerAuthoritative) {
        this.inventoriesServerAuthoritative = inventoriesServerAuthoritative;
    }

    public void setItemDefinitions(List<ItemDefinition> itemDefinitions) {
        this.itemDefinitions = itemDefinitions;
    }

    public void setLevelGameType(GameType levelGameType) {
        this.levelGameType = levelGameType;
    }

    public void setLevelId(String levelId) {
        this.levelId = levelId;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public void setLightningLevel(float lightningLevel) {
        this.lightningLevel = lightningLevel;
    }

    public void setLimitedWorldHeight(int limitedWorldHeight) {
        this.limitedWorldHeight = limitedWorldHeight;
    }

    public void setLimitedWorldWidth(int limitedWorldWidth) {
        this.limitedWorldWidth = limitedWorldWidth;
    }

    public void setMultiplayerCorrelationId(String multiplayerCorrelationId) {
        this.multiplayerCorrelationId = multiplayerCorrelationId;
    }

    public void setMultiplayerGame(boolean multiplayerGame) {
        this.multiplayerGame = multiplayerGame;
    }

    public void setNetherType(boolean netherType) {
        this.netherType = netherType;
    }

    public void setNetworkPermissions(NetworkPermissions networkPermissions) {
        this.networkPermissions = networkPermissions;
    }

    public void setOnlySpawningV1Villagers(boolean onlySpawningV1Villagers) {
        this.onlySpawningV1Villagers = onlySpawningV1Villagers;
    }

    public void setPlatformBroadcastMode(GamePublishSetting platformBroadcastMode) {
        this.platformBroadcastMode = platformBroadcastMode;
    }

    public void setPlatformLockedContentConfirmed(boolean platformLockedContentConfirmed) {
        this.platformLockedContentConfirmed = platformLockedContentConfirmed;
    }

    public void setPlayerGameType(GameType playerGameType) {
        this.playerGameType = playerGameType;
    }

    public void setPlayerPosition(Vector3f playerPosition) {
        this.playerPosition = playerPosition;
    }

    public void setPlayerPropertyData(NbtMap playerPropertyData) {
        this.playerPropertyData = playerPropertyData;
    }

    public void setPremiumWorldTemplateId(String premiumWorldTemplateId) {
        this.premiumWorldTemplateId = premiumWorldTemplateId;
    }

    public void setRainLevel(float rainLevel) {
        this.rainLevel = rainLevel;
    }

    public void setResourcePackLocked(boolean resourcePackLocked) {
        this.resourcePackLocked = resourcePackLocked;
    }

    public void setRewindHistorySize(int rewindHistorySize) {
        this.rewindHistorySize = rewindHistorySize;
    }

    public void setRotation(Vector2f rotation) {
        this.rotation = rotation;
    }

    public void setRuntimeEntityId(long runtimeEntityId) {
        this.runtimeEntityId = runtimeEntityId;
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }

    public void setServerAuthoritativeBlockBreaking(boolean serverAuthoritativeBlockBreaking) {
        this.serverAuthoritativeBlockBreaking = serverAuthoritativeBlockBreaking;
    }

    public void setServerChunkTickRange(int serverChunkTickRange) {
        this.serverChunkTickRange = serverChunkTickRange;
    }

    public void setServerEngine(String serverEngine) {
        this.serverEngine = serverEngine;
    }

    public void setSpawnBiomeType(SpawnBiomeType spawnBiomeType) {
        this.spawnBiomeType = spawnBiomeType;
    }

    public void setStartingWithMap(boolean startingWithMap) {
        this.startingWithMap = startingWithMap;
    }

    public void setTexturePacksRequired(boolean texturePacksRequired) {
        this.texturePacksRequired = texturePacksRequired;
    }

    public void setTrial(boolean trial) {
        this.trial = trial;
    }

    public void setTrustingPlayers(boolean trustingPlayers) {
        this.trustingPlayers = trustingPlayers;
    }

    public void setUniqueEntityId(long uniqueEntityId) {
        this.uniqueEntityId = uniqueEntityId;
    }

    public void setUsingMsaGamertagsOnly(boolean usingMsaGamertagsOnly) {
        this.usingMsaGamertagsOnly = usingMsaGamertagsOnly;
    }

    public void setVanillaVersion(String vanillaVersion) {
        this.vanillaVersion = vanillaVersion;
    }

    public void setWorldEditor(boolean worldEditor) {
        this.worldEditor = worldEditor;
    }

    public void setWorldTemplateId(UUID worldTemplateId) {
        this.worldTemplateId = worldTemplateId;
    }

    public void setWorldTemplateOptionLocked(boolean worldTemplateOptionLocked) {
        this.worldTemplateOptionLocked = worldTemplateOptionLocked;
    }

    public void setXblBroadcastMode(GamePublishSetting xblBroadcastMode) {
        this.xblBroadcastMode = xblBroadcastMode;
    }

    protected boolean canEqual(Object other) {
        return other instanceof StartGamePacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof StartGamePacket)) {
            return false;
        }
        StartGamePacket other = (StartGamePacket) o;
        if (!other.canEqual(this) || this.uniqueEntityId != other.uniqueEntityId || this.runtimeEntityId != other.runtimeEntityId || this.seed != other.seed || this.dimensionId != other.dimensionId || this.generatorId != other.generatorId || this.difficulty != other.difficulty || this.achievementsDisabled != other.achievementsDisabled || this.dayCycleStopTime != other.dayCycleStopTime || this.eduEditionOffers != other.eduEditionOffers || this.eduFeaturesEnabled != other.eduFeaturesEnabled || Float.compare(this.rainLevel, other.rainLevel) != 0 || Float.compare(this.lightningLevel, other.lightningLevel) != 0 || this.platformLockedContentConfirmed != other.platformLockedContentConfirmed || this.multiplayerGame != other.multiplayerGame || this.broadcastingToLan != other.broadcastingToLan || this.commandsEnabled != other.commandsEnabled || this.texturePacksRequired != other.texturePacksRequired || this.experimentsPreviouslyToggled != other.experimentsPreviouslyToggled || this.bonusChestEnabled != other.bonusChestEnabled || this.startingWithMap != other.startingWithMap || this.trustingPlayers != other.trustingPlayers || this.serverChunkTickRange != other.serverChunkTickRange || this.behaviorPackLocked != other.behaviorPackLocked || this.resourcePackLocked != other.resourcePackLocked || this.fromLockedWorldTemplate != other.fromLockedWorldTemplate || this.usingMsaGamertagsOnly != other.usingMsaGamertagsOnly || this.fromWorldTemplate != other.fromWorldTemplate || this.worldTemplateOptionLocked != other.worldTemplateOptionLocked || this.onlySpawningV1Villagers != other.onlySpawningV1Villagers || this.limitedWorldWidth != other.limitedWorldWidth || this.limitedWorldHeight != other.limitedWorldHeight || this.netherType != other.netherType || this.disablingPlayerInteractions != other.disablingPlayerInteractions || this.disablingPersonas != other.disablingPersonas || this.disablingCustomSkins != other.disablingCustomSkins || this.trial != other.trial || this.rewindHistorySize != other.rewindHistorySize || this.serverAuthoritativeBlockBreaking != other.serverAuthoritativeBlockBreaking || this.currentTick != other.currentTick || this.enchantmentSeed != other.enchantmentSeed || this.inventoriesServerAuthoritative != other.inventoriesServerAuthoritative || this.blockRegistryChecksum != other.blockRegistryChecksum || this.worldEditor != other.worldEditor || this.clientSideGenerationEnabled != other.clientSideGenerationEnabled || this.emoteChatMuted != other.emoteChatMuted || this.blockNetworkIdsHashed != other.blockNetworkIdsHashed || this.createdInEditor != other.createdInEditor || this.exportedFromEditor != other.exportedFromEditor) {
            return false;
        }
        Object this$gamerules = this.gamerules;
        Object other$gamerules = other.gamerules;
        if (this$gamerules != null ? !this$gamerules.equals(other$gamerules) : other$gamerules != null) {
            return false;
        }
        Object this$playerGameType = this.playerGameType;
        Object other$playerGameType = other.playerGameType;
        if (this$playerGameType != null ? !this$playerGameType.equals(other$playerGameType) : other$playerGameType != null) {
            return false;
        }
        Object this$playerPosition = this.playerPosition;
        Object other$playerPosition = other.playerPosition;
        if (this$playerPosition != null ? !this$playerPosition.equals(other$playerPosition) : other$playerPosition != null) {
            return false;
        }
        Object this$rotation = this.rotation;
        Object other$rotation = other.rotation;
        if (this$rotation != null ? !this$rotation.equals(other$rotation) : other$rotation != null) {
            return false;
        }
        Object this$spawnBiomeType = this.spawnBiomeType;
        Object other$spawnBiomeType = other.spawnBiomeType;
        if (this$spawnBiomeType != null ? !this$spawnBiomeType.equals(other$spawnBiomeType) : other$spawnBiomeType != null) {
            return false;
        }
        Object this$customBiomeName = this.customBiomeName;
        Object other$customBiomeName = other.customBiomeName;
        if (this$customBiomeName != null ? !this$customBiomeName.equals(other$customBiomeName) : other$customBiomeName != null) {
            return false;
        }
        Object this$levelGameType = this.levelGameType;
        Object other$levelGameType = other.levelGameType;
        if (this$levelGameType == null) {
            if (other$levelGameType != null) {
                return false;
            }
        } else if (!this$levelGameType.equals(other$levelGameType)) {
            return false;
        }
        Object other$levelGameType2 = this.defaultSpawn;
        Object other$customBiomeName2 = other.defaultSpawn;
        if (other$levelGameType2 == null) {
            if (other$customBiomeName2 != null) {
                return false;
            }
        } else if (!other$levelGameType2.equals(other$customBiomeName2)) {
            return false;
        }
        Object this$defaultSpawn = this.educationProductionId;
        Object other$defaultSpawn = other.educationProductionId;
        if (this$defaultSpawn == null) {
            if (other$defaultSpawn != null) {
                return false;
            }
        } else if (!this$defaultSpawn.equals(other$defaultSpawn)) {
            return false;
        }
        Object this$educationProductionId = this.xblBroadcastMode;
        Object other$educationProductionId = other.xblBroadcastMode;
        if (this$educationProductionId == null) {
            if (other$educationProductionId != null) {
                return false;
            }
        } else if (!this$educationProductionId.equals(other$educationProductionId)) {
            return false;
        }
        Object this$xblBroadcastMode = this.platformBroadcastMode;
        Object other$xblBroadcastMode = other.platformBroadcastMode;
        if (this$xblBroadcastMode == null) {
            if (other$xblBroadcastMode != null) {
                return false;
            }
        } else if (!this$xblBroadcastMode.equals(other$xblBroadcastMode)) {
            return false;
        }
        Object this$platformBroadcastMode = this.experiments;
        Object other$platformBroadcastMode = other.experiments;
        if (this$platformBroadcastMode == null) {
            if (other$platformBroadcastMode != null) {
                return false;
            }
        } else if (!this$platformBroadcastMode.equals(other$platformBroadcastMode)) {
            return false;
        }
        Object this$experiments = this.defaultPlayerPermission;
        Object other$experiments = other.defaultPlayerPermission;
        if (this$experiments == null) {
            if (other$experiments != null) {
                return false;
            }
        } else if (!this$experiments.equals(other$experiments)) {
            return false;
        }
        Object this$defaultPlayerPermission = this.vanillaVersion;
        Object other$defaultPlayerPermission = other.vanillaVersion;
        if (this$defaultPlayerPermission == null) {
            if (other$defaultPlayerPermission != null) {
                return false;
            }
        } else if (!this$defaultPlayerPermission.equals(other$defaultPlayerPermission)) {
            return false;
        }
        Object this$vanillaVersion = this.eduSharedUriResource;
        Object other$vanillaVersion = other.eduSharedUriResource;
        if (this$vanillaVersion == null) {
            if (other$vanillaVersion != null) {
                return false;
            }
        } else if (!this$vanillaVersion.equals(other$vanillaVersion)) {
            return false;
        }
        Object this$eduSharedUriResource = this.forceExperimentalGameplay;
        Object other$eduSharedUriResource = other.forceExperimentalGameplay;
        if (this$eduSharedUriResource == null) {
            if (other$eduSharedUriResource != null) {
                return false;
            }
        } else if (!this$eduSharedUriResource.equals(other$eduSharedUriResource)) {
            return false;
        }
        Object this$forceExperimentalGameplay = this.chatRestrictionLevel;
        Object other$forceExperimentalGameplay = other.chatRestrictionLevel;
        if (this$forceExperimentalGameplay == null) {
            if (other$forceExperimentalGameplay != null) {
                return false;
            }
        } else if (!this$forceExperimentalGameplay.equals(other$forceExperimentalGameplay)) {
            return false;
        }
        Object this$chatRestrictionLevel = this.levelId;
        Object other$chatRestrictionLevel = other.levelId;
        if (this$chatRestrictionLevel == null) {
            if (other$chatRestrictionLevel != null) {
                return false;
            }
        } else if (!this$chatRestrictionLevel.equals(other$chatRestrictionLevel)) {
            return false;
        }
        Object this$levelId = this.levelName;
        Object other$levelId = other.levelName;
        if (this$levelId == null) {
            if (other$levelId != null) {
                return false;
            }
        } else if (!this$levelId.equals(other$levelId)) {
            return false;
        }
        Object this$levelName = this.premiumWorldTemplateId;
        Object other$levelName = other.premiumWorldTemplateId;
        if (this$levelName == null) {
            if (other$levelName != null) {
                return false;
            }
        } else if (!this$levelName.equals(other$levelName)) {
            return false;
        }
        Object this$premiumWorldTemplateId = this.authoritativeMovementMode;
        Object other$premiumWorldTemplateId = other.authoritativeMovementMode;
        if (this$premiumWorldTemplateId == null) {
            if (other$premiumWorldTemplateId != null) {
                return false;
            }
        } else if (!this$premiumWorldTemplateId.equals(other$premiumWorldTemplateId)) {
            return false;
        }
        Object this$authoritativeMovementMode = this.blockPalette;
        Object other$authoritativeMovementMode = other.blockPalette;
        if (this$authoritativeMovementMode == null) {
            if (other$authoritativeMovementMode != null) {
                return false;
            }
        } else if (!this$authoritativeMovementMode.equals(other$authoritativeMovementMode)) {
            return false;
        }
        Object this$blockPalette = this.blockProperties;
        Object other$blockPalette = other.blockProperties;
        if (this$blockPalette == null) {
            if (other$blockPalette != null) {
                return false;
            }
        } else if (!this$blockPalette.equals(other$blockPalette)) {
            return false;
        }
        Object this$blockProperties = this.itemDefinitions;
        Object other$blockProperties = other.itemDefinitions;
        if (this$blockProperties == null) {
            if (other$blockProperties != null) {
                return false;
            }
        } else if (!this$blockProperties.equals(other$blockProperties)) {
            return false;
        }
        Object this$itemDefinitions = this.multiplayerCorrelationId;
        Object other$itemDefinitions = other.multiplayerCorrelationId;
        if (this$itemDefinitions == null) {
            if (other$itemDefinitions != null) {
                return false;
            }
        } else if (!this$itemDefinitions.equals(other$itemDefinitions)) {
            return false;
        }
        Object this$multiplayerCorrelationId = this.serverEngine;
        Object other$multiplayerCorrelationId = other.serverEngine;
        if (this$multiplayerCorrelationId == null) {
            if (other$multiplayerCorrelationId != null) {
                return false;
            }
        } else if (!this$multiplayerCorrelationId.equals(other$multiplayerCorrelationId)) {
            return false;
        }
        Object this$serverEngine = this.playerPropertyData;
        Object other$serverEngine = other.playerPropertyData;
        if (this$serverEngine == null) {
            if (other$serverEngine != null) {
                return false;
            }
        } else if (!this$serverEngine.equals(other$serverEngine)) {
            return false;
        }
        Object this$playerPropertyData = this.worldTemplateId;
        Object other$playerPropertyData = other.worldTemplateId;
        if (this$playerPropertyData == null) {
            if (other$playerPropertyData != null) {
                return false;
            }
        } else if (!this$playerPropertyData.equals(other$playerPropertyData)) {
            return false;
        }
        Object this$worldTemplateId = this.networkPermissions;
        Object other$networkPermissions = other.networkPermissions;
        return this$worldTemplateId == null ? other$networkPermissions == null : this$worldTemplateId.equals(other$networkPermissions);
    }

    public int hashCode() {
        long $uniqueEntityId = this.uniqueEntityId;
        int result = (1 * 59) + ((int) (($uniqueEntityId >>> 32) ^ $uniqueEntityId));
        long $runtimeEntityId = this.runtimeEntityId;
        long $seed = this.seed;
        int result2 = ((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((result * 59) + ((int) (($runtimeEntityId >>> 32) ^ $runtimeEntityId))) * 59) + ((int) (($seed >>> 32) ^ $seed))) * 59) + this.dimensionId) * 59) + this.generatorId) * 59) + this.difficulty) * 59) + (this.achievementsDisabled ? 79 : 97)) * 59) + this.dayCycleStopTime) * 59) + this.eduEditionOffers) * 59) + (this.eduFeaturesEnabled ? 79 : 97)) * 59) + Float.floatToIntBits(this.rainLevel)) * 59) + Float.floatToIntBits(this.lightningLevel)) * 59) + (this.platformLockedContentConfirmed ? 79 : 97)) * 59) + (this.multiplayerGame ? 79 : 97)) * 59) + (this.broadcastingToLan ? 79 : 97)) * 59) + (this.commandsEnabled ? 79 : 97)) * 59) + (this.texturePacksRequired ? 79 : 97)) * 59) + (this.experimentsPreviouslyToggled ? 79 : 97)) * 59) + (this.bonusChestEnabled ? 79 : 97)) * 59) + (this.startingWithMap ? 79 : 97)) * 59) + (this.trustingPlayers ? 79 : 97)) * 59) + this.serverChunkTickRange) * 59) + (this.behaviorPackLocked ? 79 : 97)) * 59) + (this.resourcePackLocked ? 79 : 97)) * 59) + (this.fromLockedWorldTemplate ? 79 : 97)) * 59) + (this.usingMsaGamertagsOnly ? 79 : 97)) * 59) + (this.fromWorldTemplate ? 79 : 97)) * 59) + (this.worldTemplateOptionLocked ? 79 : 97)) * 59) + (this.onlySpawningV1Villagers ? 79 : 97)) * 59) + this.limitedWorldWidth) * 59) + this.limitedWorldHeight) * 59) + (this.netherType ? 79 : 97)) * 59) + (this.disablingPlayerInteractions ? 79 : 97)) * 59) + (this.disablingPersonas ? 79 : 97)) * 59) + (this.disablingCustomSkins ? 79 : 97)) * 59) + (this.trial ? 79 : 97)) * 59) + this.rewindHistorySize) * 59;
        int i = this.serverAuthoritativeBlockBreaking ? 79 : 97;
        long $currentTick = this.currentTick;
        int result3 = ((((((result2 + i) * 59) + ((int) (($currentTick >>> 32) ^ $currentTick))) * 59) + this.enchantmentSeed) * 59) + (this.inventoriesServerAuthoritative ? 79 : 97);
        long $blockRegistryChecksum = this.blockRegistryChecksum;
        int result4 = ((((((((((((result3 * 59) + ((int) (($blockRegistryChecksum >>> 32) ^ $blockRegistryChecksum))) * 59) + (this.worldEditor ? 79 : 97)) * 59) + (this.clientSideGenerationEnabled ? 79 : 97)) * 59) + (this.emoteChatMuted ? 79 : 97)) * 59) + (this.blockNetworkIdsHashed ? 79 : 97)) * 59) + (this.createdInEditor ? 79 : 97)) * 59;
        int i2 = this.exportedFromEditor ? 79 : 97;
        Object $gamerules = this.gamerules;
        int result5 = ((result4 + i2) * 59) + ($gamerules == null ? 43 : $gamerules.hashCode());
        Object $playerGameType = this.playerGameType;
        int result6 = (result5 * 59) + ($playerGameType == null ? 43 : $playerGameType.hashCode());
        Object $playerPosition = this.playerPosition;
        int result7 = (result6 * 59) + ($playerPosition == null ? 43 : $playerPosition.hashCode());
        Object $rotation = this.rotation;
        int result8 = (result7 * 59) + ($rotation == null ? 43 : $rotation.hashCode());
        Object $spawnBiomeType = this.spawnBiomeType;
        int result9 = (result8 * 59) + ($spawnBiomeType == null ? 43 : $spawnBiomeType.hashCode());
        Object $customBiomeName = this.customBiomeName;
        int result10 = (result9 * 59) + ($customBiomeName == null ? 43 : $customBiomeName.hashCode());
        Object $levelGameType = this.levelGameType;
        int result11 = (result10 * 59) + ($levelGameType == null ? 43 : $levelGameType.hashCode());
        Object $levelGameType2 = this.defaultSpawn;
        int result12 = (result11 * 59) + ($levelGameType2 == null ? 43 : $levelGameType2.hashCode());
        Object $defaultSpawn = this.educationProductionId;
        int result13 = (result12 * 59) + ($defaultSpawn == null ? 43 : $defaultSpawn.hashCode());
        Object $educationProductionId = this.xblBroadcastMode;
        int result14 = (result13 * 59) + ($educationProductionId == null ? 43 : $educationProductionId.hashCode());
        Object $xblBroadcastMode = this.platformBroadcastMode;
        int result15 = (result14 * 59) + ($xblBroadcastMode == null ? 43 : $xblBroadcastMode.hashCode());
        Object $platformBroadcastMode = this.experiments;
        int result16 = (result15 * 59) + ($platformBroadcastMode == null ? 43 : $platformBroadcastMode.hashCode());
        Object $experiments = this.defaultPlayerPermission;
        int result17 = (result16 * 59) + ($experiments == null ? 43 : $experiments.hashCode());
        Object $defaultPlayerPermission = this.vanillaVersion;
        int result18 = (result17 * 59) + ($defaultPlayerPermission == null ? 43 : $defaultPlayerPermission.hashCode());
        Object $vanillaVersion = this.eduSharedUriResource;
        int result19 = (result18 * 59) + ($vanillaVersion == null ? 43 : $vanillaVersion.hashCode());
        Object $eduSharedUriResource = this.forceExperimentalGameplay;
        int result20 = (result19 * 59) + ($eduSharedUriResource == null ? 43 : $eduSharedUriResource.hashCode());
        Object $forceExperimentalGameplay = this.chatRestrictionLevel;
        int result21 = (result20 * 59) + ($forceExperimentalGameplay == null ? 43 : $forceExperimentalGameplay.hashCode());
        Object $chatRestrictionLevel = this.levelId;
        int result22 = (result21 * 59) + ($chatRestrictionLevel == null ? 43 : $chatRestrictionLevel.hashCode());
        Object $levelId = this.levelName;
        int result23 = (result22 * 59) + ($levelId == null ? 43 : $levelId.hashCode());
        Object $levelName = this.premiumWorldTemplateId;
        int result24 = (result23 * 59) + ($levelName == null ? 43 : $levelName.hashCode());
        Object $premiumWorldTemplateId = this.authoritativeMovementMode;
        int result25 = (result24 * 59) + ($premiumWorldTemplateId == null ? 43 : $premiumWorldTemplateId.hashCode());
        Object $authoritativeMovementMode = this.blockPalette;
        int result26 = (result25 * 59) + ($authoritativeMovementMode == null ? 43 : $authoritativeMovementMode.hashCode());
        Object $blockPalette = this.blockProperties;
        int result27 = (result26 * 59) + ($blockPalette == null ? 43 : $blockPalette.hashCode());
        Object $blockProperties = this.itemDefinitions;
        int result28 = (result27 * 59) + ($blockProperties == null ? 43 : $blockProperties.hashCode());
        Object $itemDefinitions = this.multiplayerCorrelationId;
        int result29 = (result28 * 59) + ($itemDefinitions == null ? 43 : $itemDefinitions.hashCode());
        Object $multiplayerCorrelationId = this.serverEngine;
        int result30 = (result29 * 59) + ($multiplayerCorrelationId == null ? 43 : $multiplayerCorrelationId.hashCode());
        Object $serverEngine = this.playerPropertyData;
        int result31 = (result30 * 59) + ($serverEngine == null ? 43 : $serverEngine.hashCode());
        Object $playerPropertyData = this.worldTemplateId;
        int result32 = (result31 * 59) + ($playerPropertyData == null ? 43 : $playerPropertyData.hashCode());
        Object $worldTemplateId = this.networkPermissions;
        return (result32 * 59) + ($worldTemplateId == null ? 43 : $worldTemplateId.hashCode());
    }

    public String toString() {
        return "StartGamePacket(gamerules=" + this.gamerules + ", uniqueEntityId=" + this.uniqueEntityId + ", runtimeEntityId=" + this.runtimeEntityId + ", playerGameType=" + this.playerGameType + ", playerPosition=" + this.playerPosition + ", rotation=" + this.rotation + ", seed=" + this.seed + ", spawnBiomeType=" + this.spawnBiomeType + ", customBiomeName=" + this.customBiomeName + ", dimensionId=" + this.dimensionId + ", generatorId=" + this.generatorId + ", levelGameType=" + this.levelGameType + ", difficulty=" + this.difficulty + ", defaultSpawn=" + this.defaultSpawn + ", achievementsDisabled=" + this.achievementsDisabled + ", dayCycleStopTime=" + this.dayCycleStopTime + ", eduEditionOffers=" + this.eduEditionOffers + ", eduFeaturesEnabled=" + this.eduFeaturesEnabled + ", educationProductionId=" + this.educationProductionId + ", rainLevel=" + this.rainLevel + ", lightningLevel=" + this.lightningLevel + ", platformLockedContentConfirmed=" + this.platformLockedContentConfirmed + ", multiplayerGame=" + this.multiplayerGame + ", broadcastingToLan=" + this.broadcastingToLan + ", xblBroadcastMode=" + this.xblBroadcastMode + ", platformBroadcastMode=" + this.platformBroadcastMode + ", commandsEnabled=" + this.commandsEnabled + ", texturePacksRequired=" + this.texturePacksRequired + ", experiments=" + this.experiments + ", experimentsPreviouslyToggled=" + this.experimentsPreviouslyToggled + ", bonusChestEnabled=" + this.bonusChestEnabled + ", startingWithMap=" + this.startingWithMap + ", trustingPlayers=" + this.trustingPlayers + ", defaultPlayerPermission=" + this.defaultPlayerPermission + ", serverChunkTickRange=" + this.serverChunkTickRange + ", behaviorPackLocked=" + this.behaviorPackLocked + ", resourcePackLocked=" + this.resourcePackLocked + ", fromLockedWorldTemplate=" + this.fromLockedWorldTemplate + ", usingMsaGamertagsOnly=" + this.usingMsaGamertagsOnly + ", fromWorldTemplate=" + this.fromWorldTemplate + ", worldTemplateOptionLocked=" + this.worldTemplateOptionLocked + ", onlySpawningV1Villagers=" + this.onlySpawningV1Villagers + ", vanillaVersion=" + this.vanillaVersion + ", limitedWorldWidth=" + this.limitedWorldWidth + ", limitedWorldHeight=" + this.limitedWorldHeight + ", netherType=" + this.netherType + ", eduSharedUriResource=" + this.eduSharedUriResource + ", forceExperimentalGameplay=" + this.forceExperimentalGameplay + ", chatRestrictionLevel=" + this.chatRestrictionLevel + ", disablingPlayerInteractions=" + this.disablingPlayerInteractions + ", disablingPersonas=" + this.disablingPersonas + ", disablingCustomSkins=" + this.disablingCustomSkins + ", levelId=" + this.levelId + ", levelName=" + this.levelName + ", premiumWorldTemplateId=" + this.premiumWorldTemplateId + ", trial=" + this.trial + ", authoritativeMovementMode=" + this.authoritativeMovementMode + ", rewindHistorySize=" + this.rewindHistorySize + ", serverAuthoritativeBlockBreaking=" + this.serverAuthoritativeBlockBreaking + ", currentTick=" + this.currentTick + ", enchantmentSeed=" + this.enchantmentSeed + ", blockProperties=" + this.blockProperties + ", multiplayerCorrelationId=" + this.multiplayerCorrelationId + ", inventoriesServerAuthoritative=" + this.inventoriesServerAuthoritative + ", serverEngine=" + this.serverEngine + ", playerPropertyData=" + this.playerPropertyData + ", blockRegistryChecksum=" + this.blockRegistryChecksum + ", worldTemplateId=" + this.worldTemplateId + ", worldEditor=" + this.worldEditor + ", clientSideGenerationEnabled=" + this.clientSideGenerationEnabled + ", emoteChatMuted=" + this.emoteChatMuted + ", blockNetworkIdsHashed=" + this.blockNetworkIdsHashed + ", createdInEditor=" + this.createdInEditor + ", exportedFromEditor=" + this.exportedFromEditor + ", networkPermissions=" + this.networkPermissions + ")";
    }

    public List<GameRuleData<?>> getGamerules() {
        return this.gamerules;
    }

    public long getUniqueEntityId() {
        return this.uniqueEntityId;
    }

    public long getRuntimeEntityId() {
        return this.runtimeEntityId;
    }

    public GameType getPlayerGameType() {
        return this.playerGameType;
    }

    public Vector3f getPlayerPosition() {
        return this.playerPosition;
    }

    public Vector2f getRotation() {
        return this.rotation;
    }

    public long getSeed() {
        return this.seed;
    }

    public SpawnBiomeType getSpawnBiomeType() {
        return this.spawnBiomeType;
    }

    public String getCustomBiomeName() {
        return this.customBiomeName;
    }

    public int getDimensionId() {
        return this.dimensionId;
    }

    public int getGeneratorId() {
        return this.generatorId;
    }

    public GameType getLevelGameType() {
        return this.levelGameType;
    }

    public int getDifficulty() {
        return this.difficulty;
    }

    public Vector3i getDefaultSpawn() {
        return this.defaultSpawn;
    }

    public boolean isAchievementsDisabled() {
        return this.achievementsDisabled;
    }

    public int getDayCycleStopTime() {
        return this.dayCycleStopTime;
    }

    public int getEduEditionOffers() {
        return this.eduEditionOffers;
    }

    public boolean isEduFeaturesEnabled() {
        return this.eduFeaturesEnabled;
    }

    public String getEducationProductionId() {
        return this.educationProductionId;
    }

    public float getRainLevel() {
        return this.rainLevel;
    }

    public float getLightningLevel() {
        return this.lightningLevel;
    }

    public boolean isPlatformLockedContentConfirmed() {
        return this.platformLockedContentConfirmed;
    }

    public boolean isMultiplayerGame() {
        return this.multiplayerGame;
    }

    public boolean isBroadcastingToLan() {
        return this.broadcastingToLan;
    }

    public GamePublishSetting getXblBroadcastMode() {
        return this.xblBroadcastMode;
    }

    public GamePublishSetting getPlatformBroadcastMode() {
        return this.platformBroadcastMode;
    }

    public boolean isCommandsEnabled() {
        return this.commandsEnabled;
    }

    public boolean isTexturePacksRequired() {
        return this.texturePacksRequired;
    }

    public List<ExperimentData> getExperiments() {
        return this.experiments;
    }

    public boolean isExperimentsPreviouslyToggled() {
        return this.experimentsPreviouslyToggled;
    }

    public boolean isBonusChestEnabled() {
        return this.bonusChestEnabled;
    }

    public boolean isStartingWithMap() {
        return this.startingWithMap;
    }

    public boolean isTrustingPlayers() {
        return this.trustingPlayers;
    }

    public PlayerPermission getDefaultPlayerPermission() {
        return this.defaultPlayerPermission;
    }

    public int getServerChunkTickRange() {
        return this.serverChunkTickRange;
    }

    public boolean isBehaviorPackLocked() {
        return this.behaviorPackLocked;
    }

    public boolean isResourcePackLocked() {
        return this.resourcePackLocked;
    }

    public boolean isFromLockedWorldTemplate() {
        return this.fromLockedWorldTemplate;
    }

    public boolean isUsingMsaGamertagsOnly() {
        return this.usingMsaGamertagsOnly;
    }

    public boolean isFromWorldTemplate() {
        return this.fromWorldTemplate;
    }

    public boolean isWorldTemplateOptionLocked() {
        return this.worldTemplateOptionLocked;
    }

    public boolean isOnlySpawningV1Villagers() {
        return this.onlySpawningV1Villagers;
    }

    public String getVanillaVersion() {
        return this.vanillaVersion;
    }

    public int getLimitedWorldWidth() {
        return this.limitedWorldWidth;
    }

    public int getLimitedWorldHeight() {
        return this.limitedWorldHeight;
    }

    public boolean isNetherType() {
        return this.netherType;
    }

    public EduSharedUriResource getEduSharedUriResource() {
        return this.eduSharedUriResource;
    }

    public OptionalBoolean getForceExperimentalGameplay() {
        return this.forceExperimentalGameplay;
    }

    public ChatRestrictionLevel getChatRestrictionLevel() {
        return this.chatRestrictionLevel;
    }

    public boolean isDisablingPlayerInteractions() {
        return this.disablingPlayerInteractions;
    }

    public boolean isDisablingPersonas() {
        return this.disablingPersonas;
    }

    public boolean isDisablingCustomSkins() {
        return this.disablingCustomSkins;
    }

    public String getLevelId() {
        return this.levelId;
    }

    public String getLevelName() {
        return this.levelName;
    }

    public String getPremiumWorldTemplateId() {
        return this.premiumWorldTemplateId;
    }

    public boolean isTrial() {
        return this.trial;
    }

    public AuthoritativeMovementMode getAuthoritativeMovementMode() {
        return this.authoritativeMovementMode;
    }

    public int getRewindHistorySize() {
        return this.rewindHistorySize;
    }

    public boolean isServerAuthoritativeBlockBreaking() {
        return this.serverAuthoritativeBlockBreaking;
    }

    public long getCurrentTick() {
        return this.currentTick;
    }

    public int getEnchantmentSeed() {
        return this.enchantmentSeed;
    }

    public NbtList<NbtMap> getBlockPalette() {
        return this.blockPalette;
    }

    public List<BlockPropertyData> getBlockProperties() {
        return this.blockProperties;
    }

    public List<ItemDefinition> getItemDefinitions() {
        return this.itemDefinitions;
    }

    public String getMultiplayerCorrelationId() {
        return this.multiplayerCorrelationId;
    }

    public boolean isInventoriesServerAuthoritative() {
        return this.inventoriesServerAuthoritative;
    }

    public String getServerEngine() {
        return this.serverEngine;
    }

    public NbtMap getPlayerPropertyData() {
        return this.playerPropertyData;
    }

    public long getBlockRegistryChecksum() {
        return this.blockRegistryChecksum;
    }

    public UUID getWorldTemplateId() {
        return this.worldTemplateId;
    }

    public boolean isWorldEditor() {
        return this.worldEditor;
    }

    public boolean isClientSideGenerationEnabled() {
        return this.clientSideGenerationEnabled;
    }

    public boolean isEmoteChatMuted() {
        return this.emoteChatMuted;
    }

    public boolean isBlockNetworkIdsHashed() {
        return this.blockNetworkIdsHashed;
    }

    public boolean isCreatedInEditor() {
        return this.createdInEditor;
    }

    public boolean isExportedFromEditor() {
        return this.exportedFromEditor;
    }

    public NetworkPermissions getNetworkPermissions() {
        return this.networkPermissions;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.START_GAME;
    }
}
