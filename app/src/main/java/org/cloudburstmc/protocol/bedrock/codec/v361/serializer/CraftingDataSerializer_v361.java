package org.cloudburstmc.protocol.bedrock.codec.v361.serializer;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.CraftingDataSerializer_v291$$ExternalSyntheticLambda2;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.CraftingDataSerializer_v291$$ExternalSyntheticLambda4;
import org.cloudburstmc.protocol.bedrock.codec.v354.serializer.CraftingDataSerializer_v354;
import org.cloudburstmc.protocol.bedrock.data.definitions.ItemDefinition;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;
import org.cloudburstmc.protocol.bedrock.data.inventory.crafting.CraftingDataType;
import org.cloudburstmc.protocol.bedrock.data.inventory.crafting.recipe.ShapedRecipeData;
import org.cloudburstmc.protocol.bedrock.data.inventory.crafting.recipe.ShapelessRecipeData;
import org.cloudburstmc.protocol.bedrock.data.inventory.descriptor.DefaultDescriptor;
import org.cloudburstmc.protocol.bedrock.data.inventory.descriptor.InvalidDescriptor;
import org.cloudburstmc.protocol.bedrock.data.inventory.descriptor.ItemDescriptorWithCount;
import org.cloudburstmc.protocol.common.util.Preconditions;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class CraftingDataSerializer_v361 extends CraftingDataSerializer_v354 {
    public static final CraftingDataSerializer_v361 INSTANCE = new CraftingDataSerializer_v361();

    @Override // org.cloudburstmc.protocol.bedrock.codec.v354.serializer.CraftingDataSerializer_v354, org.cloudburstmc.protocol.bedrock.codec.v291.serializer.CraftingDataSerializer_v291
    protected ShapelessRecipeData readShapelessRecipe(ByteBuf buffer, BedrockCodecHelper helper, CraftingDataType type) {
        String recipeId = helper.readString(buffer);
        ObjectArrayList objectArrayList = new ObjectArrayList();
        helper.readArray(buffer, objectArrayList, new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v361.serializer.CraftingDataSerializer_v361$$ExternalSyntheticLambda0
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return CraftingDataSerializer_v361.this.readIngredient((ByteBuf) obj, (BedrockCodecHelper) obj2);
            }
        });
        ObjectArrayList objectArrayList2 = new ObjectArrayList();
        helper.getClass();
        helper.readArray(buffer, objectArrayList2, new CraftingDataSerializer_v291$$ExternalSyntheticLambda4(helper));
        UUID uuid = helper.readUuid(buffer);
        String craftingTag = helper.readString(buffer);
        int priority = VarInts.readInt(buffer);
        return ShapelessRecipeData.of(type, recipeId, objectArrayList, objectArrayList2, uuid, craftingTag, priority, -1);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v354.serializer.CraftingDataSerializer_v354, org.cloudburstmc.protocol.bedrock.codec.v291.serializer.CraftingDataSerializer_v291
    protected void writeShapelessRecipe(ByteBuf buffer, BedrockCodecHelper helper, ShapelessRecipeData data) {
        helper.writeString(buffer, data.getId());
        helper.writeArray(buffer, data.getIngredients(), new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v361.serializer.CraftingDataSerializer_v361$$ExternalSyntheticLambda1
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                CraftingDataSerializer_v361.this.writeIngredient((ByteBuf) obj, (ItemDescriptorWithCount) obj2);
            }
        });
        List<ItemData> results = data.getResults();
        helper.getClass();
        helper.writeArray(buffer, results, new CraftingDataSerializer_v291$$ExternalSyntheticLambda2(helper));
        helper.writeUuid(buffer, data.getUuid());
        helper.writeString(buffer, data.getTag());
        VarInts.writeInt(buffer, data.getPriority());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v354.serializer.CraftingDataSerializer_v354, org.cloudburstmc.protocol.bedrock.codec.v291.serializer.CraftingDataSerializer_v291
    protected ShapedRecipeData readShapedRecipe(ByteBuf buffer, BedrockCodecHelper helper, CraftingDataType type) {
        String recipeId = helper.readString(buffer);
        int width = VarInts.readInt(buffer);
        int height = VarInts.readInt(buffer);
        int inputCount = width * height;
        List<ItemDescriptorWithCount> inputs = new ObjectArrayList<>(inputCount);
        for (int i = 0; i < inputCount; i++) {
            inputs.add(readIngredient(buffer, helper));
        }
        ObjectArrayList objectArrayList = new ObjectArrayList();
        helper.getClass();
        helper.readArray(buffer, objectArrayList, new CraftingDataSerializer_v291$$ExternalSyntheticLambda4(helper));
        UUID uuid = helper.readUuid(buffer);
        String craftingTag = helper.readString(buffer);
        int priority = VarInts.readInt(buffer);
        return ShapedRecipeData.of(type, recipeId, width, height, inputs, objectArrayList, uuid, craftingTag, priority, -1);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v354.serializer.CraftingDataSerializer_v354, org.cloudburstmc.protocol.bedrock.codec.v291.serializer.CraftingDataSerializer_v291
    protected void writeShapedRecipe(ByteBuf buffer, BedrockCodecHelper helper, ShapedRecipeData data) {
        helper.writeString(buffer, data.getId());
        VarInts.writeInt(buffer, data.getWidth());
        VarInts.writeInt(buffer, data.getHeight());
        int count = data.getWidth() * data.getHeight();
        List<ItemDescriptorWithCount> inputs = data.getIngredients();
        for (int i = 0; i < count; i++) {
            writeIngredient(buffer, inputs.get(i));
        }
        List<ItemData> results = data.getResults();
        helper.getClass();
        helper.writeArray(buffer, results, new CraftingDataSerializer_v291$$ExternalSyntheticLambda2(helper));
        helper.writeUuid(buffer, data.getUuid());
        helper.writeString(buffer, data.getTag());
        VarInts.writeInt(buffer, data.getPriority());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ItemDescriptorWithCount readIngredient(ByteBuf buffer, BedrockCodecHelper helper) {
        int id = VarInts.readInt(buffer);
        ItemDefinition definition = helper.getItemDefinitions().getDefinition(id);
        if (id == 0) {
            return ItemDescriptorWithCount.EMPTY;
        }
        int auxValue = fromAuxValue(VarInts.readInt(buffer));
        int stackSize = VarInts.readInt(buffer);
        return new ItemDescriptorWithCount(new DefaultDescriptor(definition, auxValue), stackSize);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writeIngredient(ByteBuf buffer, ItemDescriptorWithCount ingredient) {
        Objects.requireNonNull(ingredient, "ingredient is null");
        if (ingredient == ItemDescriptorWithCount.EMPTY || ingredient.getDescriptor() == InvalidDescriptor.INSTANCE) {
            VarInts.writeInt(buffer, 0);
            return;
        }
        Preconditions.checkArgument(ingredient.getDescriptor() instanceof DefaultDescriptor, "Descriptor must be of type DefaultDescriptor");
        DefaultDescriptor descriptor = (DefaultDescriptor) ingredient.getDescriptor();
        int id = descriptor.getItemId().getRuntimeId();
        VarInts.writeInt(buffer, id);
        if (id != 0) {
            VarInts.writeInt(buffer, toAuxValue(descriptor.getAuxValue()));
            VarInts.writeInt(buffer, ingredient.getCount());
        }
    }

    protected int fromAuxValue(int value) {
        if (value == 32767) {
            return -1;
        }
        return value;
    }

    protected int toAuxValue(int value) {
        if (value == -1) {
            return 32767;
        }
        return value;
    }
}
