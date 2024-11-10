package org.cloudburstmc.protocol.bedrock.codec.v594;

import com.trossense.bl;
import it.unimi.dsi.fastutil.BigArrays;
import java.util.function.Supplier;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodec;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.EntityDataTypeMap;
import org.cloudburstmc.protocol.bedrock.codec.v575.BedrockCodecHelper_v575;
import org.cloudburstmc.protocol.bedrock.codec.v575.Bedrock_v575;
import org.cloudburstmc.protocol.bedrock.codec.v582.Bedrock_v582;
import org.cloudburstmc.protocol.bedrock.codec.v589.Bedrock_v589;
import org.cloudburstmc.protocol.bedrock.codec.v594.serializer.AgentAnimationSerializer_v594;
import org.cloudburstmc.protocol.bedrock.codec.v594.serializer.AvailableCommandsSerializer_v594;
import org.cloudburstmc.protocol.bedrock.data.command.CommandParam;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataFormat;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataTypes;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityFlag;
import org.cloudburstmc.protocol.bedrock.packet.AgentAnimationPacket;
import org.cloudburstmc.protocol.bedrock.packet.AvailableCommandsPacket;
import org.cloudburstmc.protocol.bedrock.packet.ScriptCustomEventPacket;
import org.cloudburstmc.protocol.bedrock.transformer.FlagTransformer;
import org.cloudburstmc.protocol.common.util.TypeMap;

/* loaded from: classes5.dex */
public class Bedrock_v594 extends Bedrock_v589 {
    protected static final TypeMap<EntityFlag> ENTITY_FLAGS = Bedrock_v575.ENTITY_FLAGS.toBuilder().insert(114, (int) EntityFlag.CRAWLING).build();
    protected static final EntityDataTypeMap ENTITY_DATA = Bedrock_v589.ENTITY_DATA.toBuilder().insert(EntityDataTypes.COLLISION_BOX, bl.br, EntityDataFormat.VECTOR3F).update(EntityDataTypes.FLAGS, new FlagTransformer(ENTITY_FLAGS, 0)).update(EntityDataTypes.FLAGS_2, new FlagTransformer(ENTITY_FLAGS, 1)).build();
    protected static final TypeMap<CommandParam> COMMAND_PARAMS = Bedrock_v582.COMMAND_PARAMS.toBuilder().insert(BigArrays.SEGMENT_SIZE, (int) CommandParam.CHAINED_COMMAND).build();
    public static final BedrockCodec CODEC = Bedrock_v589.CODEC.toBuilder().raknetProtocolVersion(11).protocolVersion(594).minecraftVersion("1.20.10").helper(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v594.Bedrock_v594$$ExternalSyntheticLambda0
        @Override // java.util.function.Supplier
        public final Object get() {
            return Bedrock_v594.lambda$static$0();
        }
    }).deregisterPacket(ScriptCustomEventPacket.class).updateSerializer(AvailableCommandsPacket.class, new AvailableCommandsSerializer_v594(COMMAND_PARAMS)).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v594.Bedrock_v594$$ExternalSyntheticLambda1
        @Override // java.util.function.Supplier
        public final Object get() {
            return new AgentAnimationPacket();
        }
    }, new AgentAnimationSerializer_v594(), bl.ed).build();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ BedrockCodecHelper lambda$static$0() {
        return new BedrockCodecHelper_v575(ENTITY_DATA, GAME_RULE_TYPES, ITEM_STACK_REQUEST_TYPES, CONTAINER_SLOT_TYPES, PLAYER_ABILITIES, TEXT_PROCESSING_ORIGINS);
    }
}
