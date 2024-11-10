package org.cloudburstmc.protocol.bedrock.codec.v448;

import io.netty.buffer.ByteBuf;
import java.util.Collections;
import org.cloudburstmc.protocol.bedrock.codec.EntityDataTypeMap;
import org.cloudburstmc.protocol.bedrock.codec.v440.BedrockCodecHelper_v440;
import org.cloudburstmc.protocol.bedrock.data.inventory.ContainerSlotType;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.AutoCraftRecipeAction;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.ItemStackRequestAction;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.ItemStackRequestActionType;
import org.cloudburstmc.protocol.common.util.TypeMap;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class BedrockCodecHelper_v448 extends BedrockCodecHelper_v440 {
    public BedrockCodecHelper_v448(EntityDataTypeMap entityData, TypeMap<Class<?>> gameRulesTypes, TypeMap<ItemStackRequestActionType> stackRequestActionTypes, TypeMap<ContainerSlotType> containerSlotTypes) {
        super(entityData, gameRulesTypes, stackRequestActionTypes, containerSlotTypes);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v431.BedrockCodecHelper_v431, org.cloudburstmc.protocol.bedrock.codec.v428.BedrockCodecHelper_v428, org.cloudburstmc.protocol.bedrock.codec.v422.BedrockCodecHelper_v422, org.cloudburstmc.protocol.bedrock.codec.v407.BedrockCodecHelper_v407
    public ItemStackRequestAction readRequestActionData(ByteBuf byteBuf, ItemStackRequestActionType type) {
        if (type == ItemStackRequestActionType.CRAFT_RECIPE_AUTO) {
            return new AutoCraftRecipeAction(VarInts.readUnsignedInt(byteBuf), byteBuf.readUnsignedByte(), Collections.emptyList());
        }
        return super.readRequestActionData(byteBuf, type);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v431.BedrockCodecHelper_v431, org.cloudburstmc.protocol.bedrock.codec.v428.BedrockCodecHelper_v428, org.cloudburstmc.protocol.bedrock.codec.v422.BedrockCodecHelper_v422, org.cloudburstmc.protocol.bedrock.codec.v407.BedrockCodecHelper_v407
    public void writeRequestActionData(ByteBuf byteBuf, ItemStackRequestAction action) {
        super.writeRequestActionData(byteBuf, action);
        if (action.getType() == ItemStackRequestActionType.CRAFT_RECIPE_AUTO) {
            byteBuf.writeByte(((AutoCraftRecipeAction) action).getTimesCrafted());
        }
    }
}
