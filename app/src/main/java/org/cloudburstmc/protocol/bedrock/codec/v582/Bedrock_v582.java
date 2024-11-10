package org.cloudburstmc.protocol.bedrock.codec.v582;

import com.trossense.bl;
import java.util.function.Supplier;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodec;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.LevelEventSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v361.serializer.LevelEventGenericSerializer_v361;
import org.cloudburstmc.protocol.bedrock.codec.v448.serializer.AvailableCommandsSerializer_v448;
import org.cloudburstmc.protocol.bedrock.codec.v575.BedrockCodecHelper_v575;
import org.cloudburstmc.protocol.bedrock.codec.v575.Bedrock_v575;
import org.cloudburstmc.protocol.bedrock.codec.v582.serializer.CompressedBiomeDefinitionListSerializer_v582;
import org.cloudburstmc.protocol.bedrock.codec.v582.serializer.CraftingDataSerializer_v582;
import org.cloudburstmc.protocol.bedrock.codec.v582.serializer.OpenSignSerializer_v582;
import org.cloudburstmc.protocol.bedrock.codec.v582.serializer.RequestChunkRadiusSerializer_v582;
import org.cloudburstmc.protocol.bedrock.codec.v582.serializer.StartGameSerializer_v582;
import org.cloudburstmc.protocol.bedrock.codec.v582.serializer.TrimDataSerializer_v582;
import org.cloudburstmc.protocol.bedrock.data.LevelEvent;
import org.cloudburstmc.protocol.bedrock.data.LevelEventType;
import org.cloudburstmc.protocol.bedrock.data.command.CommandParam;
import org.cloudburstmc.protocol.bedrock.data.inventory.ContainerSlotType;
import org.cloudburstmc.protocol.bedrock.packet.AvailableCommandsPacket;
import org.cloudburstmc.protocol.bedrock.packet.CompressedBiomeDefinitionListPacket;
import org.cloudburstmc.protocol.bedrock.packet.CraftingDataPacket;
import org.cloudburstmc.protocol.bedrock.packet.LevelEventGenericPacket;
import org.cloudburstmc.protocol.bedrock.packet.LevelEventPacket;
import org.cloudburstmc.protocol.bedrock.packet.OpenSignPacket;
import org.cloudburstmc.protocol.bedrock.packet.RequestChunkRadiusPacket;
import org.cloudburstmc.protocol.bedrock.packet.StartGamePacket;
import org.cloudburstmc.protocol.bedrock.packet.TrimDataPacket;
import org.cloudburstmc.protocol.common.util.TypeMap;

/* loaded from: classes5.dex */
public class Bedrock_v582 extends Bedrock_v575 {
    protected static final TypeMap<ContainerSlotType> CONTAINER_SLOT_TYPES = Bedrock_v575.CONTAINER_SLOT_TYPES.toBuilder().insert(61, (int) ContainerSlotType.SMITHING_TABLE_TEMPLATE).build();
    protected static final TypeMap<LevelEventType> LEVEL_EVENTS = Bedrock_v575.LEVEL_EVENTS.toBuilder().insert(1067, (int) LevelEvent.SOUND_AMETHYST_RESONATE).insert(3603, (int) LevelEvent.PARTICLE_BREAK_BLOCK_DOWN).insert(3604, (int) LevelEvent.PARTICLE_BREAK_BLOCK_UP).insert(3605, (int) LevelEvent.PARTICLE_BREAK_BLOCK_NORTH).insert(3606, (int) LevelEvent.PARTICLE_BREAK_BLOCK_SOUTH).insert(3607, (int) LevelEvent.PARTICLE_BREAK_BLOCK_WEST).insert(3608, (int) LevelEvent.PARTICLE_BREAK_BLOCK_EAST).insert(3609, (int) LevelEvent.ALL_PLAYERS_SLEEPING).insert(16384, PARTICLE_TYPES).remove(9800).insert(9810, (int) LevelEvent.JUMP_PREVENTED).build();
    protected static final TypeMap<CommandParam> COMMAND_PARAMS = Bedrock_v575.COMMAND_PARAMS.toBuilder().shift(32, 5).insert(32, (int) CommandParam.PERMISSION).insert(33, (int) CommandParam.PERMISSIONS).insert(34, (int) CommandParam.PERMISSION_SELECTOR).insert(35, (int) CommandParam.PERMISSION_ELEMENT).insert(36, (int) CommandParam.PERMISSION_ELEMENTS).build();
    public static final BedrockCodec CODEC = Bedrock_v575.CODEC.toBuilder().raknetProtocolVersion(11).protocolVersion(582).minecraftVersion("1.19.80").helper(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v582.Bedrock_v582$$ExternalSyntheticLambda0
        @Override // java.util.function.Supplier
        public final Object get() {
            return Bedrock_v582.lambda$static$0();
        }
    }).updateSerializer(StartGamePacket.class, new StartGameSerializer_v582()).updateSerializer(RequestChunkRadiusPacket.class, RequestChunkRadiusSerializer_v582.INSTANCE).updateSerializer(CraftingDataPacket.class, new CraftingDataSerializer_v582()).updateSerializer(LevelEventPacket.class, new LevelEventSerializer_v291(LEVEL_EVENTS)).updateSerializer(LevelEventGenericPacket.class, new LevelEventGenericSerializer_v361(LEVEL_EVENTS)).updateSerializer(AvailableCommandsPacket.class, new AvailableCommandsSerializer_v448(COMMAND_PARAMS)).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v582.Bedrock_v582$$ExternalSyntheticLambda1
        @Override // java.util.function.Supplier
        public final Object get() {
            return new CompressedBiomeDefinitionListPacket();
        }
    }, CompressedBiomeDefinitionListSerializer_v582.INSTANCE, bl.ea).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v582.Bedrock_v582$$ExternalSyntheticLambda2
        @Override // java.util.function.Supplier
        public final Object get() {
            return new TrimDataPacket();
        }
    }, TrimDataSerializer_v582.INSTANCE, bl.eb).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v582.Bedrock_v582$$ExternalSyntheticLambda3
        @Override // java.util.function.Supplier
        public final Object get() {
            return new OpenSignPacket();
        }
    }, OpenSignSerializer_v582.INSTANCE, bl.ec).build();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ BedrockCodecHelper lambda$static$0() {
        return new BedrockCodecHelper_v575(ENTITY_DATA, GAME_RULE_TYPES, ITEM_STACK_REQUEST_TYPES, CONTAINER_SLOT_TYPES, PLAYER_ABILITIES, TEXT_PROCESSING_ORIGINS);
    }
}
