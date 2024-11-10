package org.cloudburstmc.protocol.bedrock.codec.v354.serializer;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.CraftingDataSerializer_v291;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.CraftingDataSerializer_v291$$ExternalSyntheticLambda4;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;
import org.cloudburstmc.protocol.bedrock.data.inventory.crafting.CraftingDataType;
import org.cloudburstmc.protocol.bedrock.data.inventory.crafting.recipe.FurnaceRecipeData;
import org.cloudburstmc.protocol.bedrock.data.inventory.crafting.recipe.ShapedRecipeData;
import org.cloudburstmc.protocol.bedrock.data.inventory.crafting.recipe.ShapelessRecipeData;
import org.cloudburstmc.protocol.bedrock.data.inventory.descriptor.ItemDescriptorWithCount;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class CraftingDataSerializer_v354 extends CraftingDataSerializer_v291 {
    public static final CraftingDataSerializer_v354 INSTANCE = new CraftingDataSerializer_v354();

    @Override // org.cloudburstmc.protocol.bedrock.codec.v291.serializer.CraftingDataSerializer_v291
    protected ShapelessRecipeData readShapelessRecipe(ByteBuf buffer, final BedrockCodecHelper helper, CraftingDataType type) {
        ObjectArrayList objectArrayList = new ObjectArrayList();
        helper.readArray(buffer, objectArrayList, new Function() { // from class: org.cloudburstmc.protocol.bedrock.codec.v354.serializer.CraftingDataSerializer_v354$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                ItemDescriptorWithCount fromItem;
                fromItem = ItemDescriptorWithCount.fromItem(BedrockCodecHelper.this.readItem((ByteBuf) obj));
                return fromItem;
            }
        });
        ObjectArrayList objectArrayList2 = new ObjectArrayList();
        helper.getClass();
        helper.readArray(buffer, objectArrayList2, new CraftingDataSerializer_v291$$ExternalSyntheticLambda4(helper));
        UUID uuid = helper.readUuid(buffer);
        String craftingTag = helper.readString(buffer);
        return ShapelessRecipeData.of(type, "", objectArrayList, objectArrayList2, uuid, craftingTag, 0, -1);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v291.serializer.CraftingDataSerializer_v291
    protected void writeShapelessRecipe(ByteBuf buffer, BedrockCodecHelper helper, ShapelessRecipeData data) {
        super.writeShapelessRecipe(buffer, helper, data);
        helper.writeString(buffer, data.getTag());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v291.serializer.CraftingDataSerializer_v291
    protected ShapedRecipeData readShapedRecipe(ByteBuf buffer, BedrockCodecHelper helper, CraftingDataType type) {
        int width = VarInts.readInt(buffer);
        int height = VarInts.readInt(buffer);
        int inputCount = width * height;
        List<ItemDescriptorWithCount> inputs = new ObjectArrayList<>(inputCount);
        for (int i = 0; i < inputCount; i++) {
            inputs.add(ItemDescriptorWithCount.fromItem(helper.readItem(buffer)));
        }
        ObjectArrayList objectArrayList = new ObjectArrayList();
        helper.getClass();
        helper.readArray(buffer, objectArrayList, new CraftingDataSerializer_v291$$ExternalSyntheticLambda4(helper));
        UUID uuid = helper.readUuid(buffer);
        String craftingTag = helper.readString(buffer);
        return ShapedRecipeData.of(type, "", width, height, inputs, objectArrayList, uuid, craftingTag, 0, -1);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v291.serializer.CraftingDataSerializer_v291
    protected void writeShapedRecipe(ByteBuf buffer, BedrockCodecHelper helper, ShapedRecipeData data) {
        super.writeShapedRecipe(buffer, helper, data);
        helper.writeString(buffer, data.getTag());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v291.serializer.CraftingDataSerializer_v291
    protected FurnaceRecipeData readFurnaceRecipe(ByteBuf buffer, BedrockCodecHelper helper, CraftingDataType type) {
        int inputId = VarInts.readInt(buffer);
        int inputData = type == CraftingDataType.FURNACE_DATA ? VarInts.readInt(buffer) : -1;
        ItemData result = helper.readItem(buffer);
        String craftingTag = helper.readString(buffer);
        return FurnaceRecipeData.of(type, inputId, inputData, result, craftingTag);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v291.serializer.CraftingDataSerializer_v291
    protected void writeFurnaceRecipe(ByteBuf buffer, BedrockCodecHelper helper, FurnaceRecipeData data) {
        super.writeFurnaceRecipe(buffer, helper, data);
        helper.writeString(buffer, data.getTag());
    }
}
