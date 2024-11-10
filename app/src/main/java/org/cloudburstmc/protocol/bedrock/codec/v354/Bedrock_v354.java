package org.cloudburstmc.protocol.bedrock.codec.v354;

import com.trossense.bl;
import java.util.function.Supplier;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodec;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.EntityDataTypeMap;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.LevelEventSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.LevelSoundEvent1Serializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v313.serializer.LevelSoundEvent2Serializer_v313;
import org.cloudburstmc.protocol.bedrock.codec.v332.serializer.LevelSoundEventSerializer_v332;
import org.cloudburstmc.protocol.bedrock.codec.v340.BedrockCodecHelper_v340;
import org.cloudburstmc.protocol.bedrock.codec.v340.Bedrock_v340;
import org.cloudburstmc.protocol.bedrock.codec.v354.serializer.ClientboundMapItemDataSerializer_v354;
import org.cloudburstmc.protocol.bedrock.codec.v354.serializer.CraftingDataSerializer_v354;
import org.cloudburstmc.protocol.bedrock.codec.v354.serializer.EventSerializer_v354;
import org.cloudburstmc.protocol.bedrock.codec.v354.serializer.LecternUpdateSerializer_v354;
import org.cloudburstmc.protocol.bedrock.codec.v354.serializer.MapCreateLockedCopySerializer_v354;
import org.cloudburstmc.protocol.bedrock.codec.v354.serializer.OnScreenTextureAnimationSerializer_v354;
import org.cloudburstmc.protocol.bedrock.codec.v354.serializer.UpdateTradeSerializer_v354;
import org.cloudburstmc.protocol.bedrock.data.LevelEvent;
import org.cloudburstmc.protocol.bedrock.data.LevelEventType;
import org.cloudburstmc.protocol.bedrock.data.SoundEvent;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataFormat;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataTypes;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityFlag;
import org.cloudburstmc.protocol.bedrock.packet.ClientboundMapItemDataPacket;
import org.cloudburstmc.protocol.bedrock.packet.CraftingDataPacket;
import org.cloudburstmc.protocol.bedrock.packet.EventPacket;
import org.cloudburstmc.protocol.bedrock.packet.LecternUpdatePacket;
import org.cloudburstmc.protocol.bedrock.packet.LevelEventPacket;
import org.cloudburstmc.protocol.bedrock.packet.LevelSoundEvent1Packet;
import org.cloudburstmc.protocol.bedrock.packet.LevelSoundEvent2Packet;
import org.cloudburstmc.protocol.bedrock.packet.LevelSoundEventPacket;
import org.cloudburstmc.protocol.bedrock.packet.MapCreateLockedCopyPacket;
import org.cloudburstmc.protocol.bedrock.packet.OnScreenTextureAnimationPacket;
import org.cloudburstmc.protocol.bedrock.packet.UpdateTradePacket;
import org.cloudburstmc.protocol.bedrock.transformer.FlagTransformer;
import org.cloudburstmc.protocol.common.util.TypeMap;

/* loaded from: classes5.dex */
public class Bedrock_v354 extends Bedrock_v340 {
    protected static final TypeMap<EntityFlag> ENTITY_FLAGS = Bedrock_v340.ENTITY_FLAGS.toBuilder().shift(74, 1).insert(74, (int) EntityFlag.BLOCKED_USING_DAMAGED_SHIELD).insert(81, (int) EntityFlag.IS_ILLAGER_CAPTAIN).insert(82, (int) EntityFlag.STUNNED).insert(83, (int) EntityFlag.ROARING).insert(84, (int) EntityFlag.DELAYED_ATTACK).insert(85, (int) EntityFlag.IS_AVOIDING_MOBS).insert(86, (int) EntityFlag.FACING_TARGET_TO_RANGE_ATTACK).build();
    protected static final EntityDataTypeMap ENTITY_DATA = Bedrock_v340.ENTITY_DATA.toBuilder().update(EntityDataTypes.FLAGS, new FlagTransformer(ENTITY_FLAGS, 0)).update(EntityDataTypes.FLAGS_2, new FlagTransformer(ENTITY_FLAGS, 1)).insert(EntityDataTypes.TRADE_EXPERIENCE, 102, EntityDataFormat.INT).build();
    protected static final TypeMap<SoundEvent> SOUND_EVENTS = Bedrock_v340.SOUND_EVENTS.toBuilder().insert(bl.cl, (int) SoundEvent.FLETCHING_TABLE_USE).replace(257, SoundEvent.GRINDSTONE_USE).insert(258, (int) SoundEvent.BELL).insert(bl.dv, (int) SoundEvent.CAMPFIRE_CRACKLE).insert(bl.dy, (int) SoundEvent.SWEET_BERRY_BUSH_HURT).insert(263, (int) SoundEvent.SWEET_BERRY_BUSH_PICK).insert(bl.dw, (int) SoundEvent.ROAR).insert(bl.dx, (int) SoundEvent.STUN).insert(bl.dA, (int) SoundEvent.CARTOGRAPHY_TABLE_USE).insert(bl.dB, (int) SoundEvent.STONECUTTER_USE).insert(bl.dC, (int) SoundEvent.COMPOSTER_EMPTY).insert(bl.dD, (int) SoundEvent.COMPOSTER_FILL).insert(bl.dE, (int) SoundEvent.COMPOSTER_FILL_LAYER).insert(bl.dF, (int) SoundEvent.COMPOSTER_READY).insert(bl.dG, (int) SoundEvent.BARREL_OPEN).insert(bl.dH, (int) SoundEvent.BARREL_CLOSE).insert(bl.dI, (int) SoundEvent.RAID_HORN).insert(bl.dJ, (int) SoundEvent.LOOM_USE).insert(bl.dK, (int) SoundEvent.UNDEFINED).build();
    protected static final TypeMap<LevelEventType> LEVEL_EVENTS = Bedrock_v340.LEVEL_EVENTS.toBuilder().insert(2022, (int) LevelEvent.PARTICLE_KNOCKBACK_ROAR).build();
    public static final BedrockCodec CODEC = Bedrock_v340.CODEC.toBuilder().protocolVersion(354).minecraftVersion("1.11.0").helper(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v354.Bedrock_v354$$ExternalSyntheticLambda0
        @Override // java.util.function.Supplier
        public final Object get() {
            return Bedrock_v354.lambda$static$0();
        }
    }).updateSerializer(CraftingDataPacket.class, CraftingDataSerializer_v354.INSTANCE).updateSerializer(EventPacket.class, EventSerializer_v354.INSTANCE).updateSerializer(ClientboundMapItemDataPacket.class, ClientboundMapItemDataSerializer_v354.INSTANCE).updateSerializer(UpdateTradePacket.class, UpdateTradeSerializer_v354.INSTANCE).updateSerializer(LecternUpdatePacket.class, LecternUpdateSerializer_v354.INSTANCE).updateSerializer(LevelEventPacket.class, new LevelEventSerializer_v291(LEVEL_EVENTS)).updateSerializer(LevelSoundEvent1Packet.class, new LevelSoundEvent1Serializer_v291(SOUND_EVENTS)).updateSerializer(LevelSoundEvent2Packet.class, new LevelSoundEvent2Serializer_v313(SOUND_EVENTS)).updateSerializer(LevelSoundEventPacket.class, new LevelSoundEventSerializer_v332(SOUND_EVENTS)).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v354.Bedrock_v354$$ExternalSyntheticLambda1
        @Override // java.util.function.Supplier
        public final Object get() {
            return new MapCreateLockedCopyPacket();
        }
    }, MapCreateLockedCopySerializer_v354.INSTANCE, 126).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v354.Bedrock_v354$$ExternalSyntheticLambda2
        @Override // java.util.function.Supplier
        public final Object get() {
            return new OnScreenTextureAnimationPacket();
        }
    }, OnScreenTextureAnimationSerializer_v354.INSTANCE, 127).build();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ BedrockCodecHelper lambda$static$0() {
        return new BedrockCodecHelper_v340(ENTITY_DATA, GAME_RULE_TYPES);
    }
}
