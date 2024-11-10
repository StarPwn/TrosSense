package org.cloudburstmc.protocol.bedrock.codec.v428.serializer;

import io.netty.buffer.ByteBuf;
import java.util.function.BiFunction;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v419.serializer.StartGameSerializer_v419;
import org.cloudburstmc.protocol.bedrock.data.BlockPropertyData;
import org.cloudburstmc.protocol.bedrock.data.GameType;
import org.cloudburstmc.protocol.bedrock.data.definitions.ItemDefinition;
import org.cloudburstmc.protocol.bedrock.data.definitions.SimpleItemDefinition;
import org.cloudburstmc.protocol.bedrock.packet.StartGamePacket;
import org.cloudburstmc.protocol.common.util.TriConsumer;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class StartGameSerializer_v428 extends StartGameSerializer_v419 {
    public static final StartGameSerializer_v428 INSTANCE = new StartGameSerializer_v428();

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v419.serializer.StartGameSerializer_v419, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
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
        writeSyncedPlayerMovementSettings(buffer, packet);
        buffer.writeLongLE(packet.getCurrentTick());
        VarInts.writeInt(buffer, packet.getEnchantmentSeed());
        helper.writeArray(buffer, packet.getBlockProperties(), new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v428.serializer.StartGameSerializer_v428$$ExternalSyntheticLambda2
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                StartGameSerializer_v428.lambda$serialize$0((ByteBuf) obj, (BedrockCodecHelper) obj2, (BlockPropertyData) obj3);
            }
        });
        helper.writeArray(buffer, packet.getItemDefinitions(), new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v428.serializer.StartGameSerializer_v428$$ExternalSyntheticLambda3
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                StartGameSerializer_v428.lambda$serialize$1((ByteBuf) obj, (BedrockCodecHelper) obj2, (ItemDefinition) obj3);
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
    @Override // org.cloudburstmc.protocol.bedrock.codec.v419.serializer.StartGameSerializer_v419, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
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
        readSyncedPlayerMovementSettings(buffer, packet);
        packet.setCurrentTick(buffer.readLongLE());
        packet.setEnchantmentSeed(VarInts.readInt(buffer));
        helper.readArray(buffer, packet.getBlockProperties(), new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v428.serializer.StartGameSerializer_v428$$ExternalSyntheticLambda0
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return StartGameSerializer_v428.lambda$deserialize$2((ByteBuf) obj, (BedrockCodecHelper) obj2);
            }
        });
        helper.readArray(buffer, packet.getItemDefinitions(), new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v428.serializer.StartGameSerializer_v428$$ExternalSyntheticLambda1
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return StartGameSerializer_v428.lambda$deserialize$3((ByteBuf) obj, (BedrockCodecHelper) obj2);
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

    protected void writeSyncedPlayerMovementSettings(ByteBuf buffer, StartGamePacket packet) {
        VarInts.writeInt(buffer, packet.getAuthoritativeMovementMode().ordinal());
        VarInts.writeInt(buffer, packet.getRewindHistorySize());
        buffer.writeBoolean(packet.isServerAuthoritativeBlockBreaking());
    }

    protected void readSyncedPlayerMovementSettings(ByteBuf buffer, StartGamePacket packet) {
        packet.setAuthoritativeMovementMode(MOVEMENT_MODES[VarInts.readInt(buffer)]);
        packet.setRewindHistorySize(VarInts.readInt(buffer));
        packet.setServerAuthoritativeBlockBreaking(buffer.readBoolean());
    }
}
