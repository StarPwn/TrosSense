package org.cloudburstmc.protocol.bedrock.codec.v407.serializer;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v388.serializer.CraftingDataSerializer_v388;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;
import org.cloudburstmc.protocol.bedrock.data.inventory.crafting.ContainerMixData;
import org.cloudburstmc.protocol.bedrock.data.inventory.crafting.CraftingDataType;
import org.cloudburstmc.protocol.bedrock.data.inventory.crafting.PotionMixData;
import org.cloudburstmc.protocol.bedrock.data.inventory.crafting.recipe.FurnaceRecipeData;
import org.cloudburstmc.protocol.bedrock.data.inventory.crafting.recipe.MultiRecipeData;
import org.cloudburstmc.protocol.bedrock.data.inventory.crafting.recipe.RecipeData;
import org.cloudburstmc.protocol.bedrock.data.inventory.crafting.recipe.ShapedRecipeData;
import org.cloudburstmc.protocol.bedrock.data.inventory.crafting.recipe.ShapelessRecipeData;
import org.cloudburstmc.protocol.bedrock.data.inventory.descriptor.ItemDescriptorWithCount;
import org.cloudburstmc.protocol.common.util.Preconditions;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class CraftingDataSerializer_v407 extends CraftingDataSerializer_v388 {
    public static final CraftingDataSerializer_v407 INSTANCE = new CraftingDataSerializer_v407();

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v291.serializer.CraftingDataSerializer_v291
    public RecipeData readEntry(ByteBuf buffer, BedrockCodecHelper helper) {
        int typeInt = VarInts.readInt(buffer);
        CraftingDataType type = CraftingDataType.byId(typeInt);
        switch (type) {
            case SHAPELESS:
            case SHAPELESS_CHEMISTRY:
            case SHULKER_BOX:
                return readShapelessRecipe(buffer, helper, type);
            case SHAPED:
            case SHAPED_CHEMISTRY:
                return readShapedRecipe(buffer, helper, type);
            case FURNACE:
                return readFurnaceRecipe(buffer, helper, type);
            case FURNACE_DATA:
                return readFurnaceDataRecipe(buffer, helper, type);
            case MULTI:
                return readMultiRecipe(buffer, helper, type);
            default:
                throw new IllegalArgumentException("Unhandled crafting data type: " + type);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v291.serializer.CraftingDataSerializer_v291
    public void writeEntry(ByteBuf buffer, BedrockCodecHelper helper, RecipeData craftingData) {
        VarInts.writeInt(buffer, craftingData.getType().ordinal());
        switch (craftingData.getType()) {
            case SHAPELESS:
            case SHAPELESS_CHEMISTRY:
            case SHULKER_BOX:
                writeShapelessRecipe(buffer, helper, (ShapelessRecipeData) craftingData);
                return;
            case SHAPED:
            case SHAPED_CHEMISTRY:
                writeShapedRecipe(buffer, helper, (ShapedRecipeData) craftingData);
                return;
            case FURNACE:
                writeFurnaceRecipe(buffer, helper, (FurnaceRecipeData) craftingData);
                return;
            case FURNACE_DATA:
                writeFurnaceDataRecipe(buffer, helper, (FurnaceRecipeData) craftingData);
                return;
            case MULTI:
                writeMultiRecipe(buffer, helper, (MultiRecipeData) craftingData);
                return;
            default:
                return;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v361.serializer.CraftingDataSerializer_v361, org.cloudburstmc.protocol.bedrock.codec.v354.serializer.CraftingDataSerializer_v354, org.cloudburstmc.protocol.bedrock.codec.v291.serializer.CraftingDataSerializer_v291
    public ShapelessRecipeData readShapelessRecipe(ByteBuf buffer, final BedrockCodecHelper helper, CraftingDataType type) {
        String recipeId = helper.readString(buffer);
        ObjectArrayList objectArrayList = new ObjectArrayList();
        helper.getClass();
        helper.readArray(buffer, objectArrayList, new Function() { // from class: org.cloudburstmc.protocol.bedrock.codec.v407.serializer.CraftingDataSerializer_v407$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return BedrockCodecHelper.this.readIngredient((ByteBuf) obj);
            }
        });
        ObjectArrayList objectArrayList2 = new ObjectArrayList();
        helper.getClass();
        helper.readArray(buffer, objectArrayList2, new CraftingDataSerializer_v407$$ExternalSyntheticLambda1(helper));
        UUID uuid = helper.readUuid(buffer);
        String craftingTag = helper.readString(buffer);
        int priority = VarInts.readInt(buffer);
        int networkId = VarInts.readUnsignedInt(buffer);
        return ShapelessRecipeData.of(type, recipeId, objectArrayList, objectArrayList2, uuid, craftingTag, priority, networkId);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v361.serializer.CraftingDataSerializer_v361, org.cloudburstmc.protocol.bedrock.codec.v354.serializer.CraftingDataSerializer_v354, org.cloudburstmc.protocol.bedrock.codec.v291.serializer.CraftingDataSerializer_v291
    public void writeShapelessRecipe(ByteBuf buffer, final BedrockCodecHelper helper, ShapelessRecipeData data) {
        helper.writeString(buffer, data.getId());
        List<ItemDescriptorWithCount> ingredients = data.getIngredients();
        helper.getClass();
        helper.writeArray(buffer, ingredients, new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v407.serializer.CraftingDataSerializer_v407$$ExternalSyntheticLambda2
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                BedrockCodecHelper.this.writeIngredient((ByteBuf) obj, (ItemDescriptorWithCount) obj2);
            }
        });
        List<ItemData> results = data.getResults();
        helper.getClass();
        helper.writeArray(buffer, results, new CraftingDataSerializer_v407$$ExternalSyntheticLambda3(helper));
        helper.writeUuid(buffer, data.getUuid());
        helper.writeString(buffer, data.getTag());
        VarInts.writeInt(buffer, data.getPriority());
        VarInts.writeUnsignedInt(buffer, data.getNetId());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v361.serializer.CraftingDataSerializer_v361, org.cloudburstmc.protocol.bedrock.codec.v354.serializer.CraftingDataSerializer_v354, org.cloudburstmc.protocol.bedrock.codec.v291.serializer.CraftingDataSerializer_v291
    public ShapedRecipeData readShapedRecipe(ByteBuf buffer, BedrockCodecHelper helper, CraftingDataType type) {
        String recipeId = helper.readString(buffer);
        int width = VarInts.readInt(buffer);
        int height = VarInts.readInt(buffer);
        int inputCount = width * height;
        List<ItemDescriptorWithCount> inputs = new ObjectArrayList<>(inputCount);
        for (int i = 0; i < inputCount; i++) {
            inputs.add(helper.readIngredient(buffer));
        }
        ObjectArrayList objectArrayList = new ObjectArrayList();
        helper.getClass();
        helper.readArray(buffer, objectArrayList, new CraftingDataSerializer_v407$$ExternalSyntheticLambda1(helper));
        UUID uuid = helper.readUuid(buffer);
        String craftingTag = helper.readString(buffer);
        int priority = VarInts.readInt(buffer);
        int networkId = VarInts.readUnsignedInt(buffer);
        return ShapedRecipeData.of(type, recipeId, width, height, inputs, objectArrayList, uuid, craftingTag, priority, networkId);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v361.serializer.CraftingDataSerializer_v361, org.cloudburstmc.protocol.bedrock.codec.v354.serializer.CraftingDataSerializer_v354, org.cloudburstmc.protocol.bedrock.codec.v291.serializer.CraftingDataSerializer_v291
    public void writeShapedRecipe(ByteBuf buffer, BedrockCodecHelper helper, ShapedRecipeData data) {
        helper.writeString(buffer, data.getId());
        VarInts.writeInt(buffer, data.getWidth());
        VarInts.writeInt(buffer, data.getHeight());
        int count = data.getWidth() * data.getHeight();
        List<ItemDescriptorWithCount> inputs = data.getIngredients();
        for (int i = 0; i < count; i++) {
            helper.writeIngredient(buffer, inputs.get(i));
        }
        List<ItemData> results = data.getResults();
        helper.getClass();
        helper.writeArray(buffer, results, new CraftingDataSerializer_v407$$ExternalSyntheticLambda3(helper));
        helper.writeUuid(buffer, data.getUuid());
        helper.writeString(buffer, data.getTag());
        VarInts.writeInt(buffer, data.getPriority());
        VarInts.writeUnsignedInt(buffer, data.getNetId());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v354.serializer.CraftingDataSerializer_v354, org.cloudburstmc.protocol.bedrock.codec.v291.serializer.CraftingDataSerializer_v291
    public FurnaceRecipeData readFurnaceRecipe(ByteBuf buffer, BedrockCodecHelper helper, CraftingDataType type) {
        int inputId = VarInts.readInt(buffer);
        ItemData result = helper.readItemInstance(buffer);
        String craftingTag = helper.readString(buffer);
        return FurnaceRecipeData.of(type, inputId, -1, result, craftingTag);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v354.serializer.CraftingDataSerializer_v354, org.cloudburstmc.protocol.bedrock.codec.v291.serializer.CraftingDataSerializer_v291
    public void writeFurnaceRecipe(ByteBuf buffer, BedrockCodecHelper helper, FurnaceRecipeData data) {
        VarInts.writeInt(buffer, data.getInputId());
        helper.writeItemInstance(buffer, data.getResult());
        helper.writeString(buffer, data.getTag());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public FurnaceRecipeData readFurnaceDataRecipe(ByteBuf buffer, BedrockCodecHelper helper, CraftingDataType type) {
        int inputId = VarInts.readInt(buffer);
        int inputDamage = VarInts.readInt(buffer);
        ItemData result = helper.readItemInstance(buffer);
        String craftingTag = helper.readString(buffer);
        return FurnaceRecipeData.of(type, inputId, inputDamage, result, craftingTag);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writeFurnaceDataRecipe(ByteBuf buffer, BedrockCodecHelper helper, FurnaceRecipeData data) {
        VarInts.writeInt(buffer, data.getInputId());
        VarInts.writeInt(buffer, data.getInputData());
        helper.writeItemInstance(buffer, data.getResult());
        helper.writeString(buffer, data.getTag());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v291.serializer.CraftingDataSerializer_v291
    public MultiRecipeData readMultiRecipe(ByteBuf buffer, BedrockCodecHelper helper, CraftingDataType type) {
        UUID uuid = helper.readUuid(buffer);
        int networkId = VarInts.readUnsignedInt(buffer);
        return MultiRecipeData.of(uuid, networkId);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v291.serializer.CraftingDataSerializer_v291
    public void writeMultiRecipe(ByteBuf buffer, BedrockCodecHelper helper, MultiRecipeData data) {
        helper.writeUuid(buffer, data.getUuid());
        VarInts.writeUnsignedInt(buffer, data.getNetId());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v388.serializer.CraftingDataSerializer_v388
    public PotionMixData readPotionMixData(ByteBuf buffer, BedrockCodecHelper helper) {
        return new PotionMixData(VarInts.readInt(buffer), VarInts.readInt(buffer), VarInts.readInt(buffer), VarInts.readInt(buffer), VarInts.readInt(buffer), VarInts.readInt(buffer));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v388.serializer.CraftingDataSerializer_v388
    public void writePotionMixData(ByteBuf buffer, BedrockCodecHelper helper, PotionMixData data) {
        Preconditions.checkNotNull(data, "data is null");
        VarInts.writeInt(buffer, data.getInputId());
        VarInts.writeInt(buffer, data.getInputMeta());
        VarInts.writeInt(buffer, data.getReagentId());
        VarInts.writeInt(buffer, data.getReagentMeta());
        VarInts.writeInt(buffer, data.getOutputId());
        VarInts.writeInt(buffer, data.getOutputMeta());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v388.serializer.CraftingDataSerializer_v388
    public ContainerMixData readContainerMixData(ByteBuf buffer, BedrockCodecHelper helper) {
        return new ContainerMixData(VarInts.readInt(buffer), VarInts.readInt(buffer), VarInts.readInt(buffer));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v388.serializer.CraftingDataSerializer_v388
    public void writeContainerMixData(ByteBuf buffer, BedrockCodecHelper helper, ContainerMixData data) {
        Preconditions.checkNotNull(data, "data is null");
        VarInts.writeInt(buffer, data.getInputId());
        VarInts.writeInt(buffer, data.getReagentId());
        VarInts.writeInt(buffer, data.getOutputId());
    }
}
