package org.cloudburstmc.protocol.bedrock.codec.v534;

import com.trossense.bl;
import java.util.function.Supplier;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodec;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.EntityEventSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.LevelSoundEvent1Serializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v313.serializer.LevelSoundEvent2Serializer_v313;
import org.cloudburstmc.protocol.bedrock.codec.v332.serializer.LevelSoundEventSerializer_v332;
import org.cloudburstmc.protocol.bedrock.codec.v527.Bedrock_v527;
import org.cloudburstmc.protocol.bedrock.codec.v534.serializer.AddEntitySerializer_v534;
import org.cloudburstmc.protocol.bedrock.codec.v534.serializer.AddPlayerSerializer_v534;
import org.cloudburstmc.protocol.bedrock.codec.v534.serializer.DeathInfoSerializer_v534;
import org.cloudburstmc.protocol.bedrock.codec.v534.serializer.EditorNetworkSerializer_v534;
import org.cloudburstmc.protocol.bedrock.codec.v534.serializer.StartGameSerializer_v534;
import org.cloudburstmc.protocol.bedrock.codec.v534.serializer.UpdateAbilitiesSerializer_v534;
import org.cloudburstmc.protocol.bedrock.codec.v534.serializer.UpdateAdventureSettingsSerializer_v534;
import org.cloudburstmc.protocol.bedrock.data.Ability;
import org.cloudburstmc.protocol.bedrock.data.SoundEvent;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityEventType;
import org.cloudburstmc.protocol.bedrock.packet.AddEntityPacket;
import org.cloudburstmc.protocol.bedrock.packet.AddPlayerPacket;
import org.cloudburstmc.protocol.bedrock.packet.DeathInfoPacket;
import org.cloudburstmc.protocol.bedrock.packet.EditorNetworkPacket;
import org.cloudburstmc.protocol.bedrock.packet.EntityEventPacket;
import org.cloudburstmc.protocol.bedrock.packet.LevelSoundEvent1Packet;
import org.cloudburstmc.protocol.bedrock.packet.LevelSoundEvent2Packet;
import org.cloudburstmc.protocol.bedrock.packet.LevelSoundEventPacket;
import org.cloudburstmc.protocol.bedrock.packet.StartGamePacket;
import org.cloudburstmc.protocol.bedrock.packet.UpdateAbilitiesPacket;
import org.cloudburstmc.protocol.bedrock.packet.UpdateAdventureSettingsPacket;
import org.cloudburstmc.protocol.common.util.TypeMap;

/* loaded from: classes5.dex */
public class Bedrock_v534 extends Bedrock_v527 {
    protected static final TypeMap<EntityEventType> ENTITY_EVENTS = Bedrock_v527.ENTITY_EVENTS.toBuilder().insert(78, (int) EntityEventType.DRINK_MILK).build();
    protected static final TypeMap<SoundEvent> SOUND_EVENTS = Bedrock_v527.SOUND_EVENTS.toBuilder().insert(432, (int) SoundEvent.MILK_DRINK).replace(441, SoundEvent.RECORD_PLAYING).insert(442, (int) SoundEvent.UNDEFINED).build();
    protected static final TypeMap<Ability> PLAYER_ABILITIES = TypeMap.builder(Ability.class).insert(0, (int) Ability.BUILD).insert(1, (int) Ability.MINE).insert(2, (int) Ability.DOORS_AND_SWITCHES).insert(3, (int) Ability.OPEN_CONTAINERS).insert(4, (int) Ability.ATTACK_PLAYERS).insert(5, (int) Ability.ATTACK_MOBS).insert(6, (int) Ability.OPERATOR_COMMANDS).insert(7, (int) Ability.TELEPORT).insert(8, (int) Ability.INVULNERABLE).insert(9, (int) Ability.FLYING).insert(10, (int) Ability.MAY_FLY).insert(11, (int) Ability.INSTABUILD).insert(12, (int) Ability.LIGHTNING).insert(13, (int) Ability.FLY_SPEED).insert(14, (int) Ability.WALK_SPEED).insert(15, (int) Ability.MUTED).insert(16, (int) Ability.WORLD_BUILDER).insert(17, (int) Ability.NO_CLIP).build();
    public static final BedrockCodec CODEC = Bedrock_v527.CODEC.toBuilder().protocolVersion(534).minecraftVersion("1.19.10").helper(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v534.Bedrock_v534$$ExternalSyntheticLambda0
        @Override // java.util.function.Supplier
        public final Object get() {
            return Bedrock_v534.lambda$static$0();
        }
    }).updateSerializer(StartGamePacket.class, StartGameSerializer_v534.INSTANCE).updateSerializer(AddEntityPacket.class, AddEntitySerializer_v534.INSTANCE).updateSerializer(AddPlayerPacket.class, AddPlayerSerializer_v534.INSTANCE).updateSerializer(EntityEventPacket.class, new EntityEventSerializer_v291(ENTITY_EVENTS)).updateSerializer(LevelSoundEvent1Packet.class, new LevelSoundEvent1Serializer_v291(SOUND_EVENTS)).updateSerializer(LevelSoundEvent2Packet.class, new LevelSoundEvent2Serializer_v313(SOUND_EVENTS)).updateSerializer(LevelSoundEventPacket.class, new LevelSoundEventSerializer_v332(SOUND_EVENTS)).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v534.Bedrock_v534$$ExternalSyntheticLambda1
        @Override // java.util.function.Supplier
        public final Object get() {
            return new UpdateAbilitiesPacket();
        }
    }, UpdateAbilitiesSerializer_v534.INSTANCE, bl.cl).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v534.Bedrock_v534$$ExternalSyntheticLambda2
        @Override // java.util.function.Supplier
        public final Object get() {
            return new UpdateAdventureSettingsPacket();
        }
    }, UpdateAdventureSettingsSerializer_v534.INSTANCE, bl.cm).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v534.Bedrock_v534$$ExternalSyntheticLambda3
        @Override // java.util.function.Supplier
        public final Object get() {
            return new DeathInfoPacket();
        }
    }, DeathInfoSerializer_v534.INSTANCE, bl.cn).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v534.Bedrock_v534$$ExternalSyntheticLambda4
        @Override // java.util.function.Supplier
        public final Object get() {
            return new EditorNetworkPacket();
        }
    }, EditorNetworkSerializer_v534.INSTANCE, bl.co).build();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ BedrockCodecHelper lambda$static$0() {
        return new BedrockCodecHelper_v534(ENTITY_DATA, GAME_RULE_TYPES, ITEM_STACK_REQUEST_TYPES, CONTAINER_SLOT_TYPES, PLAYER_ABILITIES);
    }
}
