package org.cloudburstmc.protocol.bedrock.codec.v291.serializer;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Iterator;
import java.util.List;
import org.cloudburstmc.nbt.NbtList;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtType;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.data.GamePublishSetting;
import org.cloudburstmc.protocol.bedrock.data.GameRuleData;
import org.cloudburstmc.protocol.bedrock.data.GameType;
import org.cloudburstmc.protocol.bedrock.data.PlayerPermission;
import org.cloudburstmc.protocol.bedrock.packet.StartGamePacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class StartGameSerializer_v291 implements BedrockPacketSerializer<StartGamePacket> {
    public static final StartGameSerializer_v291 INSTANCE = new StartGameSerializer_v291();
    protected static final PlayerPermission[] PLAYER_PERMISSIONS = PlayerPermission.values();

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
        buffer.writeLongLE(packet.getCurrentTick());
        VarInts.writeInt(buffer, packet.getEnchantmentSeed());
        NbtList<NbtMap> palette = packet.getBlockPalette();
        VarInts.writeUnsignedInt(buffer, palette.size());
        Iterator<NbtMap> it2 = palette.iterator();
        while (it2.hasNext()) {
            NbtMap entry = it2.next();
            NbtMap blockTag = entry.getCompound("block");
            helper.writeString(buffer, blockTag.getString("name"));
            buffer.writeShortLE(entry.getShort("meta"));
        }
        helper.writeString(buffer, packet.getMultiplayerCorrelationId());
    }

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
        packet.setCurrentTick(buffer.readLongLE());
        packet.setEnchantmentSeed(VarInts.readInt(buffer));
        int paletteLength = VarInts.readUnsignedInt(buffer);
        List<NbtMap> palette = new ObjectArrayList<>(paletteLength);
        for (int i = 0; i < paletteLength; i++) {
            palette.add(NbtMap.builder().putCompound("block", NbtMap.builder().putString("name", helper.readString(buffer)).build()).putShort("meta", buffer.readShortLE()).build());
        }
        packet.setBlockPalette(new NbtList<>(NbtType.COMPOUND, palette));
        packet.setMultiplayerCorrelationId(helper.readString(buffer));
    }

    /* JADX INFO: Access modifiers changed from: protected */
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
        buffer.writeBoolean(packet.isMultiplayerGame());
        buffer.writeBoolean(packet.isBroadcastingToLan());
        buffer.writeBoolean(packet.getXblBroadcastMode() != GamePublishSetting.NO_MULTI_PLAY);
        buffer.writeBoolean(packet.isCommandsEnabled());
        buffer.writeBoolean(packet.isTexturePacksRequired());
        List<GameRuleData<?>> gamerules = packet.getGamerules();
        helper.getClass();
        helper.writeArray(buffer, gamerules, new GameRulesChangedSerializer_v291$$ExternalSyntheticLambda1(helper));
        buffer.writeBoolean(packet.isBonusChestEnabled());
        buffer.writeBoolean(packet.isStartingWithMap());
        buffer.writeBoolean(packet.isTrustingPlayers());
        VarInts.writeInt(buffer, packet.getDefaultPlayerPermission().ordinal());
        VarInts.writeInt(buffer, packet.getXblBroadcastMode().ordinal());
        buffer.writeIntLE(packet.getServerChunkTickRange());
        buffer.writeBoolean(packet.getPlatformBroadcastMode() != GamePublishSetting.NO_MULTI_PLAY);
        VarInts.writeInt(buffer, packet.getPlatformBroadcastMode().ordinal());
        buffer.writeBoolean(packet.getXblBroadcastMode() != GamePublishSetting.NO_MULTI_PLAY);
        buffer.writeBoolean(packet.isBehaviorPackLocked());
        buffer.writeBoolean(packet.isResourcePackLocked());
        buffer.writeBoolean(packet.isFromLockedWorldTemplate());
        buffer.writeBoolean(packet.isUsingMsaGamertagsOnly());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void readLevelSettings(ByteBuf byteBuf, BedrockCodecHelper bedrockCodecHelper, StartGamePacket startGamePacket) {
        startGamePacket.setSeed(readSeed(byteBuf));
        startGamePacket.setDimensionId(VarInts.readInt(byteBuf));
        startGamePacket.setGeneratorId(VarInts.readInt(byteBuf));
        startGamePacket.setLevelGameType(GameType.from(VarInts.readInt(byteBuf)));
        startGamePacket.setDifficulty(VarInts.readInt(byteBuf));
        startGamePacket.setDefaultSpawn(bedrockCodecHelper.readBlockPosition(byteBuf));
        startGamePacket.setAchievementsDisabled(byteBuf.readBoolean());
        startGamePacket.setDayCycleStopTime(VarInts.readInt(byteBuf));
        startGamePacket.setEduEditionOffers(byteBuf.readBoolean() ? 1 : 0);
        startGamePacket.setEduFeaturesEnabled(byteBuf.readBoolean());
        startGamePacket.setRainLevel(byteBuf.readFloatLE());
        startGamePacket.setLightningLevel(byteBuf.readFloatLE());
        startGamePacket.setMultiplayerGame(byteBuf.readBoolean());
        startGamePacket.setBroadcastingToLan(byteBuf.readBoolean());
        byteBuf.readBoolean();
        startGamePacket.setCommandsEnabled(byteBuf.readBoolean());
        startGamePacket.setTexturePacksRequired(byteBuf.readBoolean());
        List<GameRuleData<?>> gamerules = startGamePacket.getGamerules();
        bedrockCodecHelper.getClass();
        bedrockCodecHelper.readArray(byteBuf, gamerules, new GameRulesChangedSerializer_v291$$ExternalSyntheticLambda0(bedrockCodecHelper));
        startGamePacket.setBonusChestEnabled(byteBuf.readBoolean());
        startGamePacket.setStartingWithMap(byteBuf.readBoolean());
        startGamePacket.setTrustingPlayers(byteBuf.readBoolean());
        startGamePacket.setDefaultPlayerPermission(PLAYER_PERMISSIONS[VarInts.readInt(byteBuf)]);
        startGamePacket.setXblBroadcastMode(GamePublishSetting.byId(VarInts.readInt(byteBuf)));
        startGamePacket.setServerChunkTickRange(byteBuf.readIntLE());
        byteBuf.readBoolean();
        startGamePacket.setPlatformBroadcastMode(GamePublishSetting.byId(VarInts.readInt(byteBuf)));
        byteBuf.readBoolean();
        startGamePacket.setBehaviorPackLocked(byteBuf.readBoolean());
        startGamePacket.setResourcePackLocked(byteBuf.readBoolean());
        startGamePacket.setFromLockedWorldTemplate(byteBuf.readBoolean());
        startGamePacket.setUsingMsaGamertagsOnly(byteBuf.readBoolean());
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
