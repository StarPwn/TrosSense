package org.cloudburstmc.protocol.bedrock.codec.v419.serializer;

import io.netty.buffer.ByteBuf;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.GameRulesChangedSerializer_v291$$ExternalSyntheticLambda0;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.GameRulesChangedSerializer_v291$$ExternalSyntheticLambda1;
import org.cloudburstmc.protocol.bedrock.codec.v407.serializer.StartGameSerializer_v407$$ExternalSyntheticLambda1;
import org.cloudburstmc.protocol.bedrock.data.AuthoritativeMovementMode;
import org.cloudburstmc.protocol.bedrock.data.BlockPropertyData;
import org.cloudburstmc.protocol.bedrock.data.GamePublishSetting;
import org.cloudburstmc.protocol.bedrock.data.GameRuleData;
import org.cloudburstmc.protocol.bedrock.data.GameType;
import org.cloudburstmc.protocol.bedrock.data.PlayerPermission;
import org.cloudburstmc.protocol.bedrock.data.SpawnBiomeType;
import org.cloudburstmc.protocol.bedrock.data.definitions.ItemDefinition;
import org.cloudburstmc.protocol.bedrock.data.definitions.SimpleItemDefinition;
import org.cloudburstmc.protocol.bedrock.packet.StartGamePacket;
import org.cloudburstmc.protocol.common.util.OptionalBoolean;
import org.cloudburstmc.protocol.common.util.TriConsumer;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class StartGameSerializer_v419 implements BedrockPacketSerializer<StartGamePacket> {
    public static final StartGameSerializer_v419 INSTANCE = new StartGameSerializer_v419();
    protected static final PlayerPermission[] PLAYER_PERMISSIONS = PlayerPermission.values();
    protected static final AuthoritativeMovementMode[] MOVEMENT_MODES = AuthoritativeMovementMode.values();

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, StartGamePacket packet) {
        VarInts.writeLong(buffer, packet.getUniqueEntityId());
        VarInts.writeUnsignedLong(buffer, packet.getRuntimeEntityId());
        VarInts.writeInt(buffer, packet.getPlayerGameType().ordinal());
        helper.writeVector3f(buffer, packet.getPlayerPosition());
        helper.writeVector2f(buffer, packet.getRotation());
        writeLevelSettings(buffer, helper, packet);
        helper.writeString(buffer, packet.getLevelId());
        helper.writeString(buffer, packet.getLevelName());
        helper.writeString(buffer, packet.getPremiumWorldTemplateId());
        buffer.writeBoolean(packet.isTrial());
        VarInts.writeInt(buffer, packet.getAuthoritativeMovementMode().ordinal());
        buffer.writeLongLE(packet.getCurrentTick());
        VarInts.writeInt(buffer, packet.getEnchantmentSeed());
        helper.writeArray(buffer, packet.getBlockProperties(), new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v419.serializer.StartGameSerializer_v419$$ExternalSyntheticLambda4
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                StartGameSerializer_v419.lambda$serialize$0((ByteBuf) obj, (BedrockCodecHelper) obj2, (BlockPropertyData) obj3);
            }
        });
        helper.writeArray(buffer, packet.getItemDefinitions(), new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v419.serializer.StartGameSerializer_v419$$ExternalSyntheticLambda5
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                StartGameSerializer_v419.lambda$serialize$1((ByteBuf) obj, (BedrockCodecHelper) obj2, (ItemDefinition) obj3);
            }
        });
        helper.writeString(buffer, packet.getMultiplayerCorrelationId());
        buffer.writeBoolean(packet.isInventoriesServerAuthoritative());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$serialize$0(ByteBuf buf, BedrockCodecHelper packetHelper, BlockPropertyData block) {
        packetHelper.writeString(buf, block.getName());
        packetHelper.writeTag(buf, block.getProperties());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$serialize$1(ByteBuf buf, BedrockCodecHelper packetHelper, ItemDefinition entry) {
        packetHelper.writeString(buf, entry.getIdentifier());
        buf.writeShortLE(entry.getRuntimeId());
        buf.writeBoolean(entry.isComponentBased());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, StartGamePacket packet) {
        packet.setUniqueEntityId(VarInts.readLong(buffer));
        packet.setRuntimeEntityId(VarInts.readUnsignedLong(buffer));
        packet.setPlayerGameType(GameType.from(VarInts.readInt(buffer)));
        packet.setPlayerPosition(helper.readVector3f(buffer));
        packet.setRotation(helper.readVector2f(buffer));
        readLevelSettings(buffer, helper, packet);
        packet.setLevelId(helper.readString(buffer));
        packet.setLevelName(helper.readString(buffer));
        packet.setPremiumWorldTemplateId(helper.readString(buffer));
        packet.setTrial(buffer.readBoolean());
        packet.setAuthoritativeMovementMode(MOVEMENT_MODES[VarInts.readInt(buffer)]);
        packet.setCurrentTick(buffer.readLongLE());
        packet.setEnchantmentSeed(VarInts.readInt(buffer));
        helper.readArray(buffer, packet.getBlockProperties(), new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v419.serializer.StartGameSerializer_v419$$ExternalSyntheticLambda1
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return StartGameSerializer_v419.lambda$deserialize$2((ByteBuf) obj, (BedrockCodecHelper) obj2);
            }
        });
        helper.readArray(buffer, packet.getItemDefinitions(), new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v419.serializer.StartGameSerializer_v419$$ExternalSyntheticLambda2
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return StartGameSerializer_v419.lambda$deserialize$3((ByteBuf) obj, (BedrockCodecHelper) obj2);
            }
        });
        packet.setMultiplayerCorrelationId(helper.readString(buffer));
        packet.setInventoriesServerAuthoritative(buffer.readBoolean());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ BlockPropertyData lambda$deserialize$2(ByteBuf buf, BedrockCodecHelper packetHelper) {
        String name = packetHelper.readString(buf);
        NbtMap properties = (NbtMap) packetHelper.readTag(buf, NbtMap.class);
        return new BlockPropertyData(name, properties);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ ItemDefinition lambda$deserialize$3(ByteBuf buf, BedrockCodecHelper packetHelper) {
        String identifier = packetHelper.readString(buf);
        short id = buf.readShortLE();
        boolean componentBased = buf.readBoolean();
        return new SimpleItemDefinition(identifier, id, componentBased);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writeLevelSettings(ByteBuf buffer, BedrockCodecHelper helper, StartGamePacket packet) {
        writeSeed(buffer, packet.getSeed());
        buffer.writeShortLE(packet.getSpawnBiomeType().ordinal());
        helper.writeString(buffer, packet.getCustomBiomeName());
        VarInts.writeInt(buffer, packet.getDimensionId());
        VarInts.writeInt(buffer, packet.getGeneratorId());
        VarInts.writeInt(buffer, packet.getLevelGameType().ordinal());
        VarInts.writeInt(buffer, packet.getDifficulty());
        helper.writeBlockPosition(buffer, packet.getDefaultSpawn());
        buffer.writeBoolean(packet.isAchievementsDisabled());
        VarInts.writeInt(buffer, packet.getDayCycleStopTime());
        VarInts.writeInt(buffer, packet.getEduEditionOffers());
        buffer.writeBoolean(packet.isEduFeaturesEnabled());
        helper.writeString(buffer, packet.getEducationProductionId());
        buffer.writeFloatLE(packet.getRainLevel());
        buffer.writeFloatLE(packet.getLightningLevel());
        buffer.writeBoolean(packet.isPlatformLockedContentConfirmed());
        buffer.writeBoolean(packet.isMultiplayerGame());
        buffer.writeBoolean(packet.isBroadcastingToLan());
        VarInts.writeInt(buffer, packet.getXblBroadcastMode().ordinal());
        VarInts.writeInt(buffer, packet.getPlatformBroadcastMode().ordinal());
        buffer.writeBoolean(packet.isCommandsEnabled());
        buffer.writeBoolean(packet.isTexturePacksRequired());
        List<GameRuleData<?>> gamerules = packet.getGamerules();
        helper.getClass();
        helper.writeArray(buffer, gamerules, new GameRulesChangedSerializer_v291$$ExternalSyntheticLambda1(helper));
        helper.writeExperiments(buffer, packet.getExperiments());
        buffer.writeBoolean(packet.isExperimentsPreviouslyToggled());
        buffer.writeBoolean(packet.isBonusChestEnabled());
        buffer.writeBoolean(packet.isStartingWithMap());
        VarInts.writeInt(buffer, packet.getDefaultPlayerPermission().ordinal());
        buffer.writeIntLE(packet.getServerChunkTickRange());
        buffer.writeBoolean(packet.isBehaviorPackLocked());
        buffer.writeBoolean(packet.isResourcePackLocked());
        buffer.writeBoolean(packet.isFromLockedWorldTemplate());
        buffer.writeBoolean(packet.isUsingMsaGamertagsOnly());
        buffer.writeBoolean(packet.isFromWorldTemplate());
        buffer.writeBoolean(packet.isWorldTemplateOptionLocked());
        buffer.writeBoolean(packet.isOnlySpawningV1Villagers());
        helper.writeString(buffer, packet.getVanillaVersion());
        buffer.writeIntLE(packet.getLimitedWorldWidth());
        buffer.writeIntLE(packet.getLimitedWorldHeight());
        buffer.writeBoolean(packet.isNetherType());
        helper.writeOptional(buffer, new StartGameSerializer_v407$$ExternalSyntheticLambda1(), packet.getForceExperimentalGameplay(), new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v419.serializer.StartGameSerializer_v419$$ExternalSyntheticLambda3
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ((ByteBuf) obj).writeBoolean(((OptionalBoolean) obj2).getAsBoolean());
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void readLevelSettings(ByteBuf buffer, BedrockCodecHelper helper, StartGamePacket packet) {
        packet.setSeed(readSeed(buffer));
        packet.setSpawnBiomeType(SpawnBiomeType.byId(buffer.readShortLE()));
        packet.setCustomBiomeName(helper.readString(buffer));
        packet.setDimensionId(VarInts.readInt(buffer));
        packet.setGeneratorId(VarInts.readInt(buffer));
        packet.setLevelGameType(GameType.from(VarInts.readInt(buffer)));
        packet.setDifficulty(VarInts.readInt(buffer));
        packet.setDefaultSpawn(helper.readBlockPosition(buffer));
        packet.setAchievementsDisabled(buffer.readBoolean());
        packet.setDayCycleStopTime(VarInts.readInt(buffer));
        packet.setEduEditionOffers(VarInts.readInt(buffer));
        packet.setEduFeaturesEnabled(buffer.readBoolean());
        packet.setEducationProductionId(helper.readString(buffer));
        packet.setRainLevel(buffer.readFloatLE());
        packet.setLightningLevel(buffer.readFloatLE());
        packet.setPlatformLockedContentConfirmed(buffer.readBoolean());
        packet.setMultiplayerGame(buffer.readBoolean());
        packet.setBroadcastingToLan(buffer.readBoolean());
        packet.setXblBroadcastMode(GamePublishSetting.byId(VarInts.readInt(buffer)));
        packet.setPlatformBroadcastMode(GamePublishSetting.byId(VarInts.readInt(buffer)));
        packet.setCommandsEnabled(buffer.readBoolean());
        packet.setTexturePacksRequired(buffer.readBoolean());
        List<GameRuleData<?>> gamerules = packet.getGamerules();
        helper.getClass();
        helper.readArray(buffer, gamerules, new GameRulesChangedSerializer_v291$$ExternalSyntheticLambda0(helper));
        helper.readExperiments(buffer, packet.getExperiments());
        packet.setExperimentsPreviouslyToggled(buffer.readBoolean());
        packet.setBonusChestEnabled(buffer.readBoolean());
        packet.setStartingWithMap(buffer.readBoolean());
        packet.setDefaultPlayerPermission(PLAYER_PERMISSIONS[VarInts.readInt(buffer)]);
        packet.setServerChunkTickRange(buffer.readIntLE());
        packet.setBehaviorPackLocked(buffer.readBoolean());
        packet.setResourcePackLocked(buffer.readBoolean());
        packet.setFromLockedWorldTemplate(buffer.readBoolean());
        packet.setUsingMsaGamertagsOnly(buffer.readBoolean());
        packet.setFromWorldTemplate(buffer.readBoolean());
        packet.setWorldTemplateOptionLocked(buffer.readBoolean());
        packet.setOnlySpawningV1Villagers(buffer.readBoolean());
        packet.setVanillaVersion(helper.readString(buffer));
        packet.setLimitedWorldWidth(buffer.readIntLE());
        packet.setLimitedWorldHeight(buffer.readIntLE());
        packet.setNetherType(buffer.readBoolean());
        packet.setForceExperimentalGameplay((OptionalBoolean) helper.readOptional(buffer, OptionalBoolean.empty(), new Function() { // from class: org.cloudburstmc.protocol.bedrock.codec.v419.serializer.StartGameSerializer_v419$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                OptionalBoolean of;
                of = OptionalBoolean.of(((ByteBuf) obj).readBoolean());
                return of;
            }
        }));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public long readSeed(ByteBuf buffer) {
        return VarInts.readInt(buffer);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writeSeed(ByteBuf buffer, long seed) {
        VarInts.writeInt(buffer, (int) seed);
    }
}
