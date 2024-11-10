package org.cloudburstmc.protocol.bedrock.codec.v567.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v465.serializer.CraftingDataSerializer_v465;
import org.cloudburstmc.protocol.bedrock.data.inventory.crafting.CraftingDataType;
import org.cloudburstmc.protocol.bedrock.data.inventory.crafting.recipe.FurnaceRecipeData;
import org.cloudburstmc.protocol.bedrock.data.inventory.crafting.recipe.MultiRecipeData;
import org.cloudburstmc.protocol.bedrock.data.inventory.crafting.recipe.RecipeData;
import org.cloudburstmc.protocol.bedrock.data.inventory.crafting.recipe.ShapedRecipeData;
import org.cloudburstmc.protocol.bedrock.data.inventory.crafting.recipe.ShapelessRecipeData;
import org.cloudburstmc.protocol.bedrock.data.inventory.crafting.recipe.SmithingTransformRecipeData;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class CraftingDataSerializer_v567 extends CraftingDataSerializer_v465 {
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v407.serializer.CraftingDataSerializer_v407, org.cloudburstmc.protocol.bedrock.codec.v291.serializer.CraftingDataSerializer_v291
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
            case SMITHING_TRANSFORM:
                return readSmithingTransformRecipe(buffer, helper, type);
            default:
                throw new IllegalArgumentException("Unhandled crafting data type: " + type);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v407.serializer.CraftingDataSerializer_v407, org.cloudburstmc.protocol.bedrock.codec.v291.serializer.CraftingDataSerializer_v291
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
            case SMITHING_TRANSFORM:
                writeSmithingTransformRecipe(buffer, helper, (SmithingTransformRecipeData) craftingData);
                return;
            default:
                return;
        }
    }

    protected SmithingTransformRecipeData readSmithingTransformRecipe(ByteBuf buffer, BedrockCodecHelper helper, CraftingDataType type) {
        return SmithingTransformRecipeData.of(helper.readString(buffer), helper.readIngredient(buffer), helper.readIngredient(buffer), helper.readItemInstance(buffer), helper.readString(buffer), VarInts.readUnsignedInt(buffer));
    }

    protected void writeSmithingTransformRecipe(ByteBuf buffer, BedrockCodecHelper helper, SmithingTransformRecipeData data) {
        helper.writeString(buffer, data.getId());
        helper.writeIngredient(buffer, data.getBase());
        helper.writeIngredient(buffer, data.getAddition());
        helper.writeItemInstance(buffer, data.getResult());
        helper.writeString(buffer, data.getTag());
        VarInts.writeUnsignedInt(buffer, data.getNetId());
    }
}
