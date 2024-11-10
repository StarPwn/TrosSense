package org.cloudburstmc.protocol.bedrock.codec.v422;

import com.trossense.bl;
import java.util.function.Supplier;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodec;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v419.Bedrock_v419;
import org.cloudburstmc.protocol.bedrock.codec.v422.serializer.FilterTextSerializer_v422;
import org.cloudburstmc.protocol.bedrock.codec.v422.serializer.ItemStackResponseSerializer_v422;
import org.cloudburstmc.protocol.bedrock.codec.v422.serializer.ResourcePacksInfoSerializer_v422;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.ItemStackRequestActionType;
import org.cloudburstmc.protocol.bedrock.packet.FilterTextPacket;
import org.cloudburstmc.protocol.bedrock.packet.ItemStackResponsePacket;
import org.cloudburstmc.protocol.bedrock.packet.ResourcePacksInfoPacket;
import org.cloudburstmc.protocol.common.util.TypeMap;

/* loaded from: classes5.dex */
public class Bedrock_v422 extends Bedrock_v419 {
    protected static final TypeMap<ItemStackRequestActionType> ITEM_STACK_REQUEST_TYPES = Bedrock_v419.ITEM_STACK_REQUEST_TYPES.toBuilder().shift(12, 1).insert(12, (int) ItemStackRequestActionType.CRAFT_RECIPE_OPTIONAL).build();
    public static BedrockCodec CODEC = Bedrock_v419.CODEC.toBuilder().protocolVersion(422).minecraftVersion("1.16.200").helper(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v422.Bedrock_v422$$ExternalSyntheticLambda0
        @Override // java.util.function.Supplier
        public final Object get() {
            return Bedrock_v422.lambda$static$0();
        }
    }).updateSerializer(ResourcePacksInfoPacket.class, ResourcePacksInfoSerializer_v422.INSTANCE).updateSerializer(ItemStackResponsePacket.class, ItemStackResponseSerializer_v422.INSTANCE).registerPacket(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.codec.v422.Bedrock_v422$$ExternalSyntheticLambda1
        @Override // java.util.function.Supplier
        public final Object get() {
            return new FilterTextPacket();
        }
    }, FilterTextSerializer_v422.INSTANCE, bl.bY).build();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ BedrockCodecHelper lambda$static$0() {
        return new BedrockCodecHelper_v422(ENTITY_DATA, GAME_RULE_TYPES, ITEM_STACK_REQUEST_TYPES, CONTAINER_SLOT_TYPES);
    }
}
