package org.cloudburstmc.protocol.bedrock.codec.v388.serializer;

import io.netty.buffer.ByteBuf;
import java.util.function.BiFunction;
import org.cloudburstmc.nbt.NbtList;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v361.serializer.StartGameSerializer_v361;
import org.cloudburstmc.protocol.bedrock.data.AuthoritativeMovementMode;
import org.cloudburstmc.protocol.bedrock.data.GameType;
import org.cloudburstmc.protocol.bedrock.data.definitions.ItemDefinition;
import org.cloudburstmc.protocol.bedrock.data.definitions.SimpleItemDefinition;
import org.cloudburstmc.protocol.bedrock.packet.StartGamePacket;
import org.cloudburstmc.protocol.common.util.TriConsumer;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class StartGameSerializer_v388 extends StartGameSerializer_v361 {
    public static final StartGameSerializer_v388 INSTANCE = new StartGameSerializer_v388();

    @Override // org.cloudburstmc.protocol.bedrock.codec.v361.serializer.StartGameSerializer_v361, org.cloudburstmc.protocol.bedrock.codec.v291.serializer.StartGameSerializer_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
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
        buffer.writeBoolean(packet.getAuthoritativeMovementMode() != AuthoritativeMovementMode.CLIENT);
        buffer.writeLongLE(packet.getCurrentTick());
        VarInts.writeInt(buffer, packet.getEnchantmentSeed());
        helper.writeTag(buffer, packet.getBlockPalette());
        helper.writeArray(buffer, packet.getItemDefinitions(), new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v388.serializer.StartGameSerializer_v388$$ExternalSyntheticLambda0
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                StartGameSerializer_v388.lambda$serialize$0((ByteBuf) obj, (BedrockCodecHelper) obj2, (ItemDefinition) obj3);
            }
        });
        helper.writeString(buffer, packet.getMultiplayerCorrelationId());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$serialize$0(ByteBuf buf, BedrockCodecHelper h, ItemDefinition entry) {
        h.writeString(buf, entry.getIdentifier());
        buf.writeShortLE(entry.getRuntimeId());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v361.serializer.StartGameSerializer_v361, org.cloudburstmc.protocol.bedrock.codec.v291.serializer.StartGameSerializer_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
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
        packet.setAuthoritativeMovementMode(buffer.readBoolean() ? AuthoritativeMovementMode.SERVER : AuthoritativeMovementMode.CLIENT);
        packet.setCurrentTick(buffer.readLongLE());
        packet.setEnchantmentSeed(VarInts.readInt(buffer));
        packet.setBlockPalette((NbtList) helper.readTag(buffer, NbtList.class));
        helper.readArray(buffer, packet.getItemDefinitions(), new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v388.serializer.StartGameSerializer_v388$$ExternalSyntheticLambda1
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return StartGameSerializer_v388.lambda$deserialize$1((ByteBuf) obj, (BedrockCodecHelper) obj2);
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
    @Override // org.cloudburstmc.protocol.bedrock.codec.v361.serializer.StartGameSerializer_v361, org.cloudburstmc.protocol.bedrock.codec.v332.serializer.StartGameSerializer_v332, org.cloudburstmc.protocol.bedrock.codec.v291.serializer.StartGameSerializer_v291
    public void readLevelSettings(ByteBuf buffer, BedrockCodecHelper helper, StartGamePacket packet) {
        super.readLevelSettings(buffer, helper, packet);
        packet.setVanillaVersion(helper.readString(buffer));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v361.serializer.StartGameSerializer_v361, org.cloudburstmc.protocol.bedrock.codec.v332.serializer.StartGameSerializer_v332, org.cloudburstmc.protocol.bedrock.codec.v291.serializer.StartGameSerializer_v291
    public void writeLevelSettings(ByteBuf buffer, BedrockCodecHelper helper, StartGamePacket packet) {
        super.writeLevelSettings(buffer, helper, packet);
        helper.writeString(buffer, packet.getVanillaVersion());
    }
}
