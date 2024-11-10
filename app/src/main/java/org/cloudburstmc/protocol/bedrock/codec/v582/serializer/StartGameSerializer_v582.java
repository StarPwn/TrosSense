package org.cloudburstmc.protocol.bedrock.codec.v582.serializer;

import io.netty.buffer.ByteBuf;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.GameRulesChangedSerializer_v291$$ExternalSyntheticLambda0;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.GameRulesChangedSerializer_v291$$ExternalSyntheticLambda1;
import org.cloudburstmc.protocol.bedrock.codec.v407.serializer.StartGameSerializer_v407$$ExternalSyntheticLambda1;
import org.cloudburstmc.protocol.bedrock.codec.v567.serializer.StartGameSerializer_v567;
import org.cloudburstmc.protocol.bedrock.data.ChatRestrictionLevel;
import org.cloudburstmc.protocol.bedrock.data.EduSharedUriResource;
import org.cloudburstmc.protocol.bedrock.data.GamePublishSetting;
import org.cloudburstmc.protocol.bedrock.data.GameRuleData;
import org.cloudburstmc.protocol.bedrock.data.GameType;
import org.cloudburstmc.protocol.bedrock.data.SpawnBiomeType;
import org.cloudburstmc.protocol.bedrock.packet.StartGamePacket;
import org.cloudburstmc.protocol.common.util.OptionalBoolean;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class StartGameSerializer_v582 extends StartGameSerializer_v567 {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v544.serializer.StartGameSerializer_v544, org.cloudburstmc.protocol.bedrock.codec.v527.serializer.StartGameSerializer_v527, org.cloudburstmc.protocol.bedrock.codec.v440.serializer.StartGameSerializer_v440, org.cloudburstmc.protocol.bedrock.codec.v428.serializer.StartGameSerializer_v428, org.cloudburstmc.protocol.bedrock.codec.v419.serializer.StartGameSerializer_v419, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, StartGamePacket packet) {
        super.serialize(buffer, helper, packet);
        buffer.writeBoolean(packet.isBlockNetworkIdsHashed());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v544.serializer.StartGameSerializer_v544, org.cloudburstmc.protocol.bedrock.codec.v527.serializer.StartGameSerializer_v527, org.cloudburstmc.protocol.bedrock.codec.v440.serializer.StartGameSerializer_v440, org.cloudburstmc.protocol.bedrock.codec.v428.serializer.StartGameSerializer_v428, org.cloudburstmc.protocol.bedrock.codec.v419.serializer.StartGameSerializer_v419, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, StartGamePacket packet) {
        super.deserialize(buffer, helper, packet);
        packet.setBlockNetworkIdsHashed(buffer.readBoolean());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v567.serializer.StartGameSerializer_v567, org.cloudburstmc.protocol.bedrock.codec.v544.serializer.StartGameSerializer_v544, org.cloudburstmc.protocol.bedrock.codec.v534.serializer.StartGameSerializer_v534, org.cloudburstmc.protocol.bedrock.codec.v465.serializer.StartGameSerializer_v465, org.cloudburstmc.protocol.bedrock.codec.v419.serializer.StartGameSerializer_v419
    protected void writeLevelSettings(ByteBuf buffer, BedrockCodecHelper helper, StartGamePacket packet) {
        writeSeed(buffer, packet.getSeed());
        buffer.writeShortLE(packet.getSpawnBiomeType().ordinal());
        helper.writeString(buffer, packet.getCustomBiomeName());
        VarInts.writeInt(buffer, packet.getDimensionId());
        VarInts.writeInt(buffer, packet.getGeneratorId());
        VarInts.writeInt(buffer, packet.getLevelGameType().ordinal());
        VarInts.writeInt(buffer, packet.getDifficulty());
        helper.writeBlockPosition(buffer, packet.getDefaultSpawn());
        buffer.writeBoolean(packet.isAchievementsDisabled());
        buffer.writeBoolean(packet.isWorldEditor());
        buffer.writeBoolean(packet.isCreatedInEditor());
        buffer.writeBoolean(packet.isExportedFromEditor());
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
        buffer.writeBoolean(packet.isDisablingPersonas());
        buffer.writeBoolean(packet.isDisablingCustomSkins());
        buffer.writeBoolean(packet.isEmoteChatMuted());
        helper.writeString(buffer, packet.getVanillaVersion());
        buffer.writeIntLE(packet.getLimitedWorldWidth());
        buffer.writeIntLE(packet.getLimitedWorldHeight());
        buffer.writeBoolean(packet.isNetherType());
        helper.writeString(buffer, packet.getEduSharedUriResource().getButtonName());
        helper.writeString(buffer, packet.getEduSharedUriResource().getLinkUri());
        helper.writeOptional(buffer, new StartGameSerializer_v407$$ExternalSyntheticLambda1(), packet.getForceExperimentalGameplay(), new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v582.serializer.StartGameSerializer_v582$$ExternalSyntheticLambda1
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ((ByteBuf) obj).writeBoolean(((OptionalBoolean) obj2).getAsBoolean());
            }
        });
        buffer.writeByte(packet.getChatRestrictionLevel().ordinal());
        buffer.writeBoolean(packet.isDisablingPlayerInteractions());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v567.serializer.StartGameSerializer_v567, org.cloudburstmc.protocol.bedrock.codec.v544.serializer.StartGameSerializer_v544, org.cloudburstmc.protocol.bedrock.codec.v534.serializer.StartGameSerializer_v534, org.cloudburstmc.protocol.bedrock.codec.v465.serializer.StartGameSerializer_v465, org.cloudburstmc.protocol.bedrock.codec.v419.serializer.StartGameSerializer_v419
    protected void readLevelSettings(ByteBuf buffer, BedrockCodecHelper helper, StartGamePacket packet) {
        packet.setSeed(readSeed(buffer));
        packet.setSpawnBiomeType(SpawnBiomeType.byId(buffer.readShortLE()));
        packet.setCustomBiomeName(helper.readString(buffer));
        packet.setDimensionId(VarInts.readInt(buffer));
        packet.setGeneratorId(VarInts.readInt(buffer));
        packet.setLevelGameType(GameType.from(VarInts.readInt(buffer)));
        packet.setDifficulty(VarInts.readInt(buffer));
        packet.setDefaultSpawn(helper.readBlockPosition(buffer));
        packet.setAchievementsDisabled(buffer.readBoolean());
        packet.setWorldEditor(buffer.readBoolean());
        packet.setCreatedInEditor(buffer.readBoolean());
        packet.setExportedFromEditor(buffer.readBoolean());
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
        packet.setDisablingPersonas(buffer.readBoolean());
        packet.setDisablingCustomSkins(buffer.readBoolean());
        packet.setEmoteChatMuted(buffer.readBoolean());
        packet.setVanillaVersion(helper.readString(buffer));
        packet.setLimitedWorldWidth(buffer.readIntLE());
        packet.setLimitedWorldHeight(buffer.readIntLE());
        packet.setNetherType(buffer.readBoolean());
        packet.setEduSharedUriResource(new EduSharedUriResource(helper.readString(buffer), helper.readString(buffer)));
        packet.setForceExperimentalGameplay((OptionalBoolean) helper.readOptional(buffer, OptionalBoolean.empty(), new Function() { // from class: org.cloudburstmc.protocol.bedrock.codec.v582.serializer.StartGameSerializer_v582$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                OptionalBoolean of;
                of = OptionalBoolean.of(((ByteBuf) obj).readBoolean());
                return of;
            }
        }));
        packet.setChatRestrictionLevel(ChatRestrictionLevel.values()[buffer.readByte()]);
        packet.setDisablingPlayerInteractions(buffer.readBoolean());
    }
}
