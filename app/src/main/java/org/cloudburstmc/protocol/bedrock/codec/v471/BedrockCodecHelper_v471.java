package org.cloudburstmc.protocol.bedrock.codec.v471;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.EntityDataTypeMap;
import org.cloudburstmc.protocol.bedrock.codec.v465.BedrockCodecHelper_v465;
import org.cloudburstmc.protocol.bedrock.data.inventory.ContainerSlotType;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.CraftGrindstoneAction;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.CraftLoomAction;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.ItemStackRequestAction;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.ItemStackRequestActionType;
import org.cloudburstmc.protocol.common.util.TypeMap;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class BedrockCodecHelper_v471 extends BedrockCodecHelper_v465 {
    public BedrockCodecHelper_v471(EntityDataTypeMap entityData, TypeMap<Class<?>> gameRulesTypes, TypeMap<ItemStackRequestActionType> stackRequestActionTypes, TypeMap<ContainerSlotType> containerSlotTypes) {
        super(entityData, gameRulesTypes, stackRequestActionTypes, containerSlotTypes);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v448.BedrockCodecHelper_v448, org.cloudburstmc.protocol.bedrock.codec.v431.BedrockCodecHelper_v431, org.cloudburstmc.protocol.bedrock.codec.v428.BedrockCodecHelper_v428, org.cloudburstmc.protocol.bedrock.codec.v422.BedrockCodecHelper_v422, org.cloudburstmc.protocol.bedrock.codec.v407.BedrockCodecHelper_v407
    public ItemStackRequestAction readRequestActionData(ByteBuf byteBuf, ItemStackRequestActionType type) {
        switch (type) {
            case CRAFT_REPAIR_AND_DISENCHANT:
                return new CraftGrindstoneAction(VarInts.readUnsignedInt(byteBuf), VarInts.readInt(byteBuf));
            case CRAFT_LOOM:
                return new CraftLoomAction(readString(byteBuf));
            default:
                return super.readRequestActionData(byteBuf, type);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v448.BedrockCodecHelper_v448, org.cloudburstmc.protocol.bedrock.codec.v431.BedrockCodecHelper_v431, org.cloudburstmc.protocol.bedrock.codec.v428.BedrockCodecHelper_v428, org.cloudburstmc.protocol.bedrock.codec.v422.BedrockCodecHelper_v422, org.cloudburstmc.protocol.bedrock.codec.v407.BedrockCodecHelper_v407
    public void writeRequestActionData(ByteBuf byteBuf, ItemStackRequestAction action) {
        switch (action.getType()) {
            case CRAFT_REPAIR_AND_DISENCHANT:
                CraftGrindstoneAction actionData = (CraftGrindstoneAction) action;
                VarInts.writeUnsignedInt(byteBuf, actionData.getRecipeNetworkId());
                VarInts.writeInt(byteBuf, actionData.getRepairCost());
                return;
            case CRAFT_LOOM:
                writeString(byteBuf, ((CraftLoomAction) action).getPatternId());
                return;
            default:
                super.writeRequestActionData(byteBuf, action);
                return;
        }
    }
}
