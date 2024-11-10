package org.cloudburstmc.protocol.bedrock.codec.v422;

import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Function;
import org.cloudburstmc.protocol.bedrock.codec.EntityDataTypeMap;
import org.cloudburstmc.protocol.bedrock.codec.v419.BedrockCodecHelper_v419;
import org.cloudburstmc.protocol.bedrock.data.inventory.ContainerSlotType;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.ItemStackRequest;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.CraftRecipeOptionalAction;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.ItemStackRequestAction;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.ItemStackRequestActionType;
import org.cloudburstmc.protocol.common.util.TypeMap;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class BedrockCodecHelper_v422 extends BedrockCodecHelper_v419 {
    public BedrockCodecHelper_v422(EntityDataTypeMap entityData, TypeMap<Class<?>> gameRulesTypes, TypeMap<ItemStackRequestActionType> stackRequestActionTypes, TypeMap<ContainerSlotType> containerSlotTypes) {
        super(entityData, gameRulesTypes, stackRequestActionTypes, containerSlotTypes);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v407.BedrockCodecHelper_v407, org.cloudburstmc.protocol.bedrock.codec.BaseBedrockCodecHelper, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public ItemStackRequest readItemStackRequest(ByteBuf buffer) {
        int requestId = VarInts.readInt(buffer);
        ArrayList arrayList = new ArrayList();
        readArray(buffer, arrayList, new Function() { // from class: org.cloudburstmc.protocol.bedrock.codec.v422.BedrockCodecHelper_v422$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return BedrockCodecHelper_v422.this.m2084xec0e36cb((ByteBuf) obj);
            }
        });
        ArrayList arrayList2 = new ArrayList();
        readArray(buffer, arrayList2, new Function() { // from class: org.cloudburstmc.protocol.bedrock.codec.v422.BedrockCodecHelper_v422$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return BedrockCodecHelper_v422.this.readString((ByteBuf) obj);
            }
        });
        return new ItemStackRequest(requestId, (ItemStackRequestAction[]) arrayList.toArray(new ItemStackRequestAction[0]), (String[]) arrayList2.toArray(new String[0]));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$readItemStackRequest$0$org-cloudburstmc-protocol-bedrock-codec-v422-BedrockCodecHelper_v422, reason: not valid java name */
    public /* synthetic */ ItemStackRequestAction m2084xec0e36cb(ByteBuf byteBuf) {
        ItemStackRequestActionType type = this.stackRequestActionTypes.getType(byteBuf.readByte());
        return readRequestActionData(byteBuf, type);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v407.BedrockCodecHelper_v407, org.cloudburstmc.protocol.bedrock.codec.BaseBedrockCodecHelper, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writeItemStackRequest(ByteBuf buffer, ItemStackRequest request) {
        VarInts.writeInt(buffer, request.getRequestId());
        writeArray(buffer, request.getActions(), new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v422.BedrockCodecHelper_v422$$ExternalSyntheticLambda2
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                BedrockCodecHelper_v422.this.m2085x92222a1b((ByteBuf) obj, (ItemStackRequestAction) obj2);
            }
        });
        writeArray(buffer, request.getFilterStrings(), new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v422.BedrockCodecHelper_v422$$ExternalSyntheticLambda3
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                BedrockCodecHelper_v422.this.writeString((ByteBuf) obj, (String) obj2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$writeItemStackRequest$1$org-cloudburstmc-protocol-bedrock-codec-v422-BedrockCodecHelper_v422, reason: not valid java name */
    public /* synthetic */ void m2085x92222a1b(ByteBuf byteBuf, ItemStackRequestAction action) {
        ItemStackRequestActionType type = action.getType();
        byteBuf.writeByte(this.stackRequestActionTypes.getId(type));
        writeRequestActionData(byteBuf, action);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v407.BedrockCodecHelper_v407
    public ItemStackRequestAction readRequestActionData(ByteBuf byteBuf, ItemStackRequestActionType type) {
        if (type == ItemStackRequestActionType.CRAFT_RECIPE_OPTIONAL) {
            ItemStackRequestAction action = new CraftRecipeOptionalAction(VarInts.readUnsignedInt(byteBuf), byteBuf.readIntLE());
            return action;
        }
        ItemStackRequestAction action2 = super.readRequestActionData(byteBuf, type);
        return action2;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v407.BedrockCodecHelper_v407
    public void writeRequestActionData(ByteBuf byteBuf, ItemStackRequestAction action) {
        if (action.getType() == ItemStackRequestActionType.CRAFT_RECIPE_OPTIONAL) {
            VarInts.writeUnsignedInt(byteBuf, ((CraftRecipeOptionalAction) action).getRecipeNetworkId());
            byteBuf.writeIntLE(((CraftRecipeOptionalAction) action).getFilteredStringIndex());
        } else {
            super.writeRequestActionData(byteBuf, action);
        }
    }
}
