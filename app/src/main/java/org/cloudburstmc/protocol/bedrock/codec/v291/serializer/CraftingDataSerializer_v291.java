package org.cloudburstmc.protocol.bedrock.codec.v291.serializer;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;
import org.cloudburstmc.protocol.bedrock.data.inventory.crafting.CraftingDataType;
import org.cloudburstmc.protocol.bedrock.data.inventory.crafting.recipe.FurnaceRecipeData;
import org.cloudburstmc.protocol.bedrock.data.inventory.crafting.recipe.MultiRecipeData;
import org.cloudburstmc.protocol.bedrock.data.inventory.crafting.recipe.RecipeData;
import org.cloudburstmc.protocol.bedrock.data.inventory.crafting.recipe.ShapedRecipeData;
import org.cloudburstmc.protocol.bedrock.data.inventory.crafting.recipe.ShapelessRecipeData;
import org.cloudburstmc.protocol.bedrock.data.inventory.descriptor.ItemDescriptorWithCount;
import org.cloudburstmc.protocol.bedrock.packet.CraftingDataPacket;
import org.cloudburstmc.protocol.common.util.TriConsumer;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class CraftingDataSerializer_v291 implements BedrockPacketSerializer<CraftingDataPacket> {
    public static final CraftingDataSerializer_v291 INSTANCE = new CraftingDataSerializer_v291();

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, CraftingDataPacket packet) {
        helper.writeArray(buffer, packet.getCraftingData(), new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.CraftingDataSerializer_v291$$ExternalSyntheticLambda0
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                CraftingDataSerializer_v291.this.writeEntry((ByteBuf) obj, (BedrockCodecHelper) obj2, (RecipeData) obj3);
            }
        });
        buffer.writeBoolean(packet.isCleanRecipes());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, CraftingDataPacket packet) {
        helper.readArray(buffer, packet.getCraftingData(), new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.CraftingDataSerializer_v291$$ExternalSyntheticLambda3
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return CraftingDataSerializer_v291.this.readEntry((ByteBuf) obj, (BedrockCodecHelper) obj2);
            }
        });
        packet.setCleanRecipes(buffer.readBoolean());
    }

    /* JADX INFO: Access modifiers changed from: protected */
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
            case FURNACE_DATA:
                return readFurnaceRecipe(buffer, helper, type);
            case MULTI:
                return readMultiRecipe(buffer, helper, type);
            default:
                throw new IllegalArgumentException("Unhandled crafting data type: " + type);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
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
            case FURNACE_DATA:
                writeFurnaceRecipe(buffer, helper, (FurnaceRecipeData) craftingData);
                return;
            case MULTI:
                writeMultiRecipe(buffer, helper, (MultiRecipeData) craftingData);
                return;
            default:
                return;
        }
    }

    protected ShapelessRecipeData readShapelessRecipe(ByteBuf buffer, final BedrockCodecHelper helper, CraftingDataType type) {
        ObjectArrayList objectArrayList = new ObjectArrayList();
        helper.readArray(buffer, objectArrayList, new Function() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.CraftingDataSerializer_v291$$ExternalSyntheticLambda5
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
        return ShapelessRecipeData.of(type, "", objectArrayList, objectArrayList2, uuid, "", 0, -1);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writeShapelessRecipe(ByteBuf buffer, final BedrockCodecHelper helper, ShapelessRecipeData data) {
        helper.writeArray(buffer, data.getIngredients(), new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.CraftingDataSerializer_v291$$ExternalSyntheticLambda1
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                BedrockCodecHelper.this.writeItem((ByteBuf) obj, ((ItemDescriptorWithCount) obj2).toItem());
            }
        });
        List<ItemData> results = data.getResults();
        helper.getClass();
        helper.writeArray(buffer, results, new CraftingDataSerializer_v291$$ExternalSyntheticLambda2(helper));
        helper.writeUuid(buffer, data.getUuid());
    }

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
        return ShapedRecipeData.of(type, "", width, height, inputs, objectArrayList, uuid, "", 0, -1);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writeShapedRecipe(ByteBuf buffer, BedrockCodecHelper helper, ShapedRecipeData data) {
        VarInts.writeInt(buffer, data.getWidth());
        VarInts.writeInt(buffer, data.getHeight());
        int count = data.getWidth() * data.getHeight();
        List<ItemDescriptorWithCount> inputs = data.getIngredients();
        for (int i = 0; i < count; i++) {
            helper.writeItem(buffer, inputs.get(i).toItem());
        }
        List<ItemData> results = data.getResults();
        helper.getClass();
        helper.writeArray(buffer, results, new CraftingDataSerializer_v291$$ExternalSyntheticLambda2(helper));
        helper.writeUuid(buffer, data.getUuid());
    }

    protected FurnaceRecipeData readFurnaceRecipe(ByteBuf buffer, BedrockCodecHelper helper, CraftingDataType type) {
        int inputId = VarInts.readInt(buffer);
        int inputDamage = type == CraftingDataType.FURNACE_DATA ? VarInts.readInt(buffer) : -1;
        ItemData result = helper.readItem(buffer);
        return FurnaceRecipeData.of(type, inputId, inputDamage, result, "");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writeFurnaceRecipe(ByteBuf buffer, BedrockCodecHelper helper, FurnaceRecipeData data) {
        VarInts.writeInt(buffer, data.getInputId());
        if (data.getType() == CraftingDataType.FURNACE_DATA) {
            VarInts.writeInt(buffer, data.getInputData());
        }
        helper.writeItem(buffer, data.getResult());
    }

    protected MultiRecipeData readMultiRecipe(ByteBuf buffer, BedrockCodecHelper helper, CraftingDataType type) {
        UUID uuid = helper.readUuid(buffer);
        return MultiRecipeData.of(uuid, -1);
    }

    protected void writeMultiRecipe(ByteBuf buffer, BedrockCodecHelper helper, MultiRecipeData data) {
        helper.writeUuid(buffer, data.getUuid());
    }
}
