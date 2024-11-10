package org.cloudburstmc.protocol.bedrock.codec.v557;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.ToLongFunction;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.EntityDataTypeMap;
import org.cloudburstmc.protocol.bedrock.codec.v554.BedrockCodecHelper_v554;
import org.cloudburstmc.protocol.bedrock.data.Ability;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityProperties;
import org.cloudburstmc.protocol.bedrock.data.entity.FloatEntityProperty;
import org.cloudburstmc.protocol.bedrock.data.entity.IntEntityProperty;
import org.cloudburstmc.protocol.bedrock.data.inventory.ContainerSlotType;
import org.cloudburstmc.protocol.bedrock.data.inventory.descriptor.ItemDescriptorWithCount;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.TextProcessingEventOrigin;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.AutoCraftRecipeAction;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.ItemStackRequestAction;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.ItemStackRequestActionType;
import org.cloudburstmc.protocol.common.util.TypeMap;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class BedrockCodecHelper_v557 extends BedrockCodecHelper_v554 {
    public BedrockCodecHelper_v557(EntityDataTypeMap entityData, TypeMap<Class<?>> gameRulesTypes, TypeMap<ItemStackRequestActionType> stackRequestActionTypes, TypeMap<ContainerSlotType> containerSlotTypes, TypeMap<Ability> abilities, TypeMap<TextProcessingEventOrigin> textProcessingEventOrigins) {
        super(entityData, gameRulesTypes, stackRequestActionTypes, containerSlotTypes, abilities, textProcessingEventOrigins);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BaseBedrockCodecHelper, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void readEntityProperties(ByteBuf buffer, EntityProperties properties) {
        readArray(buffer, properties.getIntProperties(), new Function() { // from class: org.cloudburstmc.protocol.bedrock.codec.v557.BedrockCodecHelper_v557$$ExternalSyntheticLambda2
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return BedrockCodecHelper_v557.lambda$readEntityProperties$0((ByteBuf) obj);
            }
        });
        readArray(buffer, properties.getFloatProperties(), new Function() { // from class: org.cloudburstmc.protocol.bedrock.codec.v557.BedrockCodecHelper_v557$$ExternalSyntheticLambda3
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return BedrockCodecHelper_v557.lambda$readEntityProperties$1((ByteBuf) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ IntEntityProperty lambda$readEntityProperties$0(ByteBuf byteBuf) {
        int index = VarInts.readUnsignedInt(byteBuf);
        int value = VarInts.readInt(byteBuf);
        return new IntEntityProperty(index, value);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ FloatEntityProperty lambda$readEntityProperties$1(ByteBuf byteBuf) {
        int index = VarInts.readUnsignedInt(byteBuf);
        float value = byteBuf.readFloatLE();
        return new FloatEntityProperty(index, value);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BaseBedrockCodecHelper, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writeEntityProperties(ByteBuf buffer, EntityProperties properties) {
        writeArray(buffer, properties.getIntProperties(), new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v557.BedrockCodecHelper_v557$$ExternalSyntheticLambda0
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                BedrockCodecHelper_v557.lambda$writeEntityProperties$2((ByteBuf) obj, (IntEntityProperty) obj2);
            }
        });
        writeArray(buffer, properties.getFloatProperties(), new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v557.BedrockCodecHelper_v557$$ExternalSyntheticLambda1
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                BedrockCodecHelper_v557.lambda$writeEntityProperties$3((ByteBuf) obj, (FloatEntityProperty) obj2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$writeEntityProperties$2(ByteBuf byteBuf, IntEntityProperty property) {
        VarInts.writeUnsignedInt(byteBuf, property.getIndex());
        VarInts.writeInt(byteBuf, property.getValue());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$writeEntityProperties$3(ByteBuf byteBuf, FloatEntityProperty property) {
        VarInts.writeUnsignedInt(byteBuf, property.getIndex());
        byteBuf.writeFloatLE(property.getValue());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v471.BedrockCodecHelper_v471, org.cloudburstmc.protocol.bedrock.codec.v448.BedrockCodecHelper_v448, org.cloudburstmc.protocol.bedrock.codec.v431.BedrockCodecHelper_v431, org.cloudburstmc.protocol.bedrock.codec.v428.BedrockCodecHelper_v428, org.cloudburstmc.protocol.bedrock.codec.v422.BedrockCodecHelper_v422, org.cloudburstmc.protocol.bedrock.codec.v407.BedrockCodecHelper_v407
    public ItemStackRequestAction readRequestActionData(ByteBuf byteBuf, ItemStackRequestActionType type) {
        if (type == ItemStackRequestActionType.CRAFT_RECIPE_AUTO) {
            int recipeId = VarInts.readUnsignedInt(byteBuf);
            int timesCrafted = byteBuf.readUnsignedByte();
            ObjectArrayList objectArrayList = new ObjectArrayList();
            readArray(byteBuf, objectArrayList, new ToLongFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v557.BedrockCodecHelper_v557$$ExternalSyntheticLambda5
                @Override // java.util.function.ToLongFunction
                public final long applyAsLong(Object obj) {
                    short readUnsignedByte;
                    readUnsignedByte = ((ByteBuf) obj).readUnsignedByte();
                    return readUnsignedByte;
                }
            }, new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v557.BedrockCodecHelper_v557$$ExternalSyntheticLambda6
                @Override // java.util.function.BiFunction
                public final Object apply(Object obj, Object obj2) {
                    ItemDescriptorWithCount readIngredient;
                    readIngredient = ((BedrockCodecHelper) obj2).readIngredient((ByteBuf) obj);
                    return readIngredient;
                }
            });
            return new AutoCraftRecipeAction(recipeId, timesCrafted, objectArrayList);
        }
        return super.readRequestActionData(byteBuf, type);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v471.BedrockCodecHelper_v471, org.cloudburstmc.protocol.bedrock.codec.v448.BedrockCodecHelper_v448, org.cloudburstmc.protocol.bedrock.codec.v431.BedrockCodecHelper_v431, org.cloudburstmc.protocol.bedrock.codec.v428.BedrockCodecHelper_v428, org.cloudburstmc.protocol.bedrock.codec.v422.BedrockCodecHelper_v422, org.cloudburstmc.protocol.bedrock.codec.v407.BedrockCodecHelper_v407
    public void writeRequestActionData(ByteBuf byteBuf, ItemStackRequestAction action) {
        super.writeRequestActionData(byteBuf, action);
        if (action.getType() == ItemStackRequestActionType.CRAFT_RECIPE_AUTO) {
            List<ItemDescriptorWithCount> ingredients = ((AutoCraftRecipeAction) action).getIngredients();
            byteBuf.writeByte(ingredients.size());
            writeArray(byteBuf, ingredients, new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v557.BedrockCodecHelper_v557$$ExternalSyntheticLambda4
                @Override // java.util.function.BiConsumer
                public final void accept(Object obj, Object obj2) {
                    BedrockCodecHelper_v557.this.writeIngredient((ByteBuf) obj, (ItemDescriptorWithCount) obj2);
                }
            });
        }
    }
}
