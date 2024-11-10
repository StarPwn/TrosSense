package org.cloudburstmc.protocol.bedrock.codec.v332.serializer;

import io.netty.buffer.ByteBuf;
import java.util.List;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.GameRulesChangedSerializer_v291$$ExternalSyntheticLambda0;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.GameRulesChangedSerializer_v291$$ExternalSyntheticLambda1;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.StartGameSerializer_v291;
import org.cloudburstmc.protocol.bedrock.data.GamePublishSetting;
import org.cloudburstmc.protocol.bedrock.data.GameRuleData;
import org.cloudburstmc.protocol.bedrock.data.GameType;
import org.cloudburstmc.protocol.bedrock.packet.StartGamePacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class StartGameSerializer_v332 extends StartGameSerializer_v291 {
    public static final StartGameSerializer_v332 INSTANCE = new StartGameSerializer_v332();

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v291.serializer.StartGameSerializer_v291
    public void writeLevelSettings(ByteBuf buffer, BedrockCodecHelper helper, StartGamePacket packet) {
        writeSeed(buffer, packet.getSeed());
        VarInts.writeInt(buffer, packet.getDimensionId());
        VarInts.writeInt(buffer, packet.getGeneratorId());
        VarInts.writeInt(buffer, packet.getLevelGameType().ordinal());
        VarInts.writeInt(buffer, packet.getDifficulty());
        helper.writeBlockPosition(buffer, packet.getDefaultSpawn());
        buffer.writeBoolean(packet.isAchievementsDisabled());
        VarInts.writeInt(buffer, packet.getDayCycleStopTime());
        buffer.writeBoolean(packet.getEduEditionOffers() != 0);
        buffer.writeBoolean(packet.isEduFeaturesEnabled());
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
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v291.serializer.StartGameSerializer_v291
    public void readLevelSettings(ByteBuf byteBuf, BedrockCodecHelper bedrockCodecHelper, StartGamePacket startGamePacket) {
        startGamePacket.setSeed(readSeed(byteBuf));
        startGamePacket.setDimensionId(VarInts.readInt(byteBuf));
        startGamePacket.setGeneratorId(VarInts.readInt(byteBuf));
        startGamePacket.setLevelGameType(GameType.values()[VarInts.readInt(byteBuf)]);
        startGamePacket.setDifficulty(VarInts.readInt(byteBuf));
        startGamePacket.setDefaultSpawn(bedrockCodecHelper.readBlockPosition(byteBuf));
        startGamePacket.setAchievementsDisabled(byteBuf.readBoolean());
        startGamePacket.setDayCycleStopTime(VarInts.readInt(byteBuf));
        startGamePacket.setEduEditionOffers(byteBuf.readBoolean() ? 1 : 0);
        startGamePacket.setEduFeaturesEnabled(byteBuf.readBoolean());
        startGamePacket.setRainLevel(byteBuf.readFloatLE());
        startGamePacket.setLightningLevel(byteBuf.readFloatLE());
        startGamePacket.setPlatformLockedContentConfirmed(byteBuf.readBoolean());
        startGamePacket.setMultiplayerGame(byteBuf.readBoolean());
        startGamePacket.setBroadcastingToLan(byteBuf.readBoolean());
        startGamePacket.setXblBroadcastMode(GamePublishSetting.byId(VarInts.readInt(byteBuf)));
        startGamePacket.setPlatformBroadcastMode(GamePublishSetting.byId(VarInts.readInt(byteBuf)));
        startGamePacket.setCommandsEnabled(byteBuf.readBoolean());
        startGamePacket.setTexturePacksRequired(byteBuf.readBoolean());
        List<GameRuleData<?>> gamerules = startGamePacket.getGamerules();
        bedrockCodecHelper.getClass();
        bedrockCodecHelper.readArray(byteBuf, gamerules, new GameRulesChangedSerializer_v291$$ExternalSyntheticLambda0(bedrockCodecHelper));
        startGamePacket.setBonusChestEnabled(byteBuf.readBoolean());
        startGamePacket.setStartingWithMap(byteBuf.readBoolean());
        startGamePacket.setDefaultPlayerPermission(PLAYER_PERMISSIONS[VarInts.readInt(byteBuf)]);
        startGamePacket.setServerChunkTickRange(byteBuf.readIntLE());
        startGamePacket.setBehaviorPackLocked(byteBuf.readBoolean());
        startGamePacket.setResourcePackLocked(byteBuf.readBoolean());
        startGamePacket.setFromLockedWorldTemplate(byteBuf.readBoolean());
        startGamePacket.setUsingMsaGamertagsOnly(byteBuf.readBoolean());
        startGamePacket.setFromWorldTemplate(byteBuf.readBoolean());
        startGamePacket.setWorldTemplateOptionLocked(byteBuf.readBoolean());
    }
}
