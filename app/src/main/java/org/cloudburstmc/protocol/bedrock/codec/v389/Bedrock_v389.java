package org.cloudburstmc.protocol.bedrock.codec.v389;

import java.util.function.Supplier;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodec;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.EntityDataTypeMap;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.LevelEventSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v361.serializer.LevelEventGenericSerializer_v361;
import org.cloudburstmc.protocol.bedrock.codec.v388.BedrockCodecHelper_v388;
import org.cloudburstmc.protocol.bedrock.codec.v388.Bedrock_v388;
import org.cloudburstmc.protocol.bedrock.codec.v389.serializer.EventSerializer_v389;
import org.cloudburstmc.protocol.bedrock.data.LevelEventType;
import org.cloudburstmc.protocol.bedrock.data.ParticleType;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataTypes;
import org.cloudburstmc.protocol.bedrock.packet.EventPacket;
import org.cloudburstmc.protocol.bedrock.packet.LevelEventGenericPacket;
import org.cloudburstmc.protocol.bedrock.packet.LevelEventPacket;
import org.cloudburstmc.protocol.bedrock.transformer.TypeMapTransformer;
import org.cloudburstmc.protocol.common.util.TypeMap;

/* loaded from: classes5.dex */
public class Bedrock_v389 extends Bedrock_v388 {
    protected static final TypeMap<ParticleType> PARTICLE_TYPES = Bedrock_v388.PARTICLE_TYPES.toBuilder().shift(28, 1).insert(28, (int) ParticleType.DRIP_HONEY).insert(59, (int) ParticleType.SHULKER_BULLET).insert(60, (int) ParticleType.BLEACH).insert(61, (int) ParticleType.DRAGON_DESTROY_BLOCK).insert(62, (int) ParticleType.MYCELIUM_DUST).insert(63, (int) ParticleType.FALLING_BORDER_DUST).insert(64, (int) ParticleType.CAMPFIRE_SMOKE).insert(65, (int) ParticleType.CAMPFIRE_SMOKE_TALL).insert(66, (int) ParticleType.DRAGON_BREATH_FIRE).insert(67, (int) ParticleType.DRAGON_BREATH_TRAIL).build();
    protected static final TypeMap<LevelEventType> LEVEL_EVENTS = Bedrock_v388.LEVEL_EVENTS.toBuilder().insert(16384, PARTICLE_TYPES).build();
    protected static final EntityDataTypeMap ENTITY_DATA = Bedrock_v388.ENTITY_DATA.toBuilder().update(EntityDataTypes.AREA_EFFECT_CLOUD_PARTICLE, new TypeMapTransformer(PARTICLE_TYPES)).build();
    public static BedrockCodec CODEC = Bedrock_v388.CODEC.toBuilder().protocolVersion(389).minecraftVersion("1.14.0").helper(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v389.Bedrock_v389$$ExternalSyntheticLambda0
        @Override // java.util.function.Supplier
        public final Object get() {
            return Bedrock_v389.lambda$static$0();
        }
    }).updateSerializer(EventPacket.class, EventSerializer_v389.INSTANCE).updateSerializer(LevelEventPacket.class, new LevelEventSerializer_v291(LEVEL_EVENTS)).updateSerializer(LevelEventGenericPacket.class, new LevelEventGenericSerializer_v361(LEVEL_EVENTS)).build();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ BedrockCodecHelper lambda$static$0() {
        return new BedrockCodecHelper_v388(ENTITY_DATA, GAME_RULE_TYPES);
    }
}
