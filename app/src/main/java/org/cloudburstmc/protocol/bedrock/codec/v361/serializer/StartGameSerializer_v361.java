package org.cloudburstmc.protocol.bedrock.codec.v361.serializer;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import org.cloudburstmc.nbt.NbtList;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtType;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v332.serializer.StartGameSerializer_v332;
import org.cloudburstmc.protocol.bedrock.data.GameType;
import org.cloudburstmc.protocol.bedrock.data.definitions.ItemDefinition;
import org.cloudburstmc.protocol.bedrock.data.definitions.SimpleItemDefinition;
import org.cloudburstmc.protocol.bedrock.packet.StartGamePacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class StartGameSerializer_v361 extends StartGameSerializer_v332 {
    public static final StartGameSerializer_v361 INSTANCE = new StartGameSerializer_v361();

    @Override // org.cloudburstmc.protocol.bedrock.codec.v291.serializer.StartGameSerializer_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, final BedrockCodecHelper helper, StartGamePacket packet) {
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
        List<NbtMap> palette = packet.getBlockPalette();
        VarInts.writeUnsignedInt(buffer, palette.size());
        for (NbtMap entry : palette) {
            NbtMap blockTag = entry.getCompound("block");
            helper.writeString(buffer, blockTag.getString("name"));
            buffer.writeShortLE(entry.getShort("meta"));
            buffer.writeShortLE(entry.getShort("id"));
        }
        helper.writeArray(buffer, packet.getItemDefinitions(), new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v361.serializer.StartGameSerializer_v361$$ExternalSyntheticLambda0
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                StartGameSerializer_v361.lambda$serialize$0(BedrockCodecHelper.this, (ByteBuf) obj, (ItemDefinition) obj2);
            }
        });
        helper.writeString(buffer, packet.getMultiplayerCorrelationId());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$serialize$0(BedrockCodecHelper helper, ByteBuf buf, ItemDefinition entry) {
        helper.writeString(buf, entry.getIdentifier());
        buf.writeShortLE(entry.getRuntimeId());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v291.serializer.StartGameSerializer_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
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
            palette.add(NbtMap.builder().putCompound("block", NbtMap.builder().putString("name", helper.readString(buffer)).build()).putShort("meta", buffer.readShortLE()).putShort("id", buffer.readShortLE()).build());
        }
        packet.setBlockPalette(new NbtList<>(NbtType.COMPOUND, palette));
        helper.readArray(buffer, packet.getItemDefinitions(), new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v361.serializer.StartGameSerializer_v361$$ExternalSyntheticLambda1
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return StartGameSerializer_v361.lambda$deserialize$1((ByteBuf) obj, (BedrockCodecHelper) obj2);
            }
        });
        packet.setMultiplayerCorrelationId(helper.readString(buffer));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ ItemDefinition lambda$deserialize$1(ByteBuf buf, BedrockCodecHelper packetHelper) {
        String identifier = packetHelper.readString(buf);
        short id = buf.readShortLE();
        return new SimpleItemDefinition(identifier, id, false);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v332.serializer.StartGameSerializer_v332, org.cloudburstmc.protocol.bedrock.codec.v291.serializer.StartGameSerializer_v291
    public void readLevelSettings(ByteBuf buffer, BedrockCodecHelper helper, StartGamePacket packet) {
        super.readLevelSettings(buffer, helper, packet);
        packet.setOnlySpawningV1Villagers(buffer.readBoolean());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v332.serializer.StartGameSerializer_v332, org.cloudburstmc.protocol.bedrock.codec.v291.serializer.StartGameSerializer_v291
    public void writeLevelSettings(ByteBuf buffer, BedrockCodecHelper helper, StartGamePacket packet) {
        super.writeLevelSettings(buffer, helper, packet);
        buffer.writeBoolean(packet.isOnlySpawningV1Villagers());
    }
}
