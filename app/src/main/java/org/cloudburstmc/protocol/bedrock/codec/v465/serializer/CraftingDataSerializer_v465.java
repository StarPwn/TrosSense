package org.cloudburstmc.protocol.bedrock.codec.v465.serializer;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v407.serializer.CraftingDataSerializer_v407;
import org.cloudburstmc.protocol.bedrock.data.definitions.ItemDefinition;
import org.cloudburstmc.protocol.bedrock.data.inventory.crafting.ContainerMixData;
import org.cloudburstmc.protocol.bedrock.data.inventory.crafting.MaterialReducer;
import org.cloudburstmc.protocol.bedrock.data.inventory.crafting.PotionMixData;
import org.cloudburstmc.protocol.bedrock.data.inventory.crafting.recipe.RecipeData;
import org.cloudburstmc.protocol.bedrock.packet.CraftingDataPacket;
import org.cloudburstmc.protocol.common.util.TriConsumer;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class CraftingDataSerializer_v465 extends CraftingDataSerializer_v407 {
    public static final CraftingDataSerializer_v465 INSTANCE = new CraftingDataSerializer_v465();

    @Override // org.cloudburstmc.protocol.bedrock.codec.v388.serializer.CraftingDataSerializer_v388, org.cloudburstmc.protocol.bedrock.codec.v291.serializer.CraftingDataSerializer_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, CraftingDataPacket packet) {
        helper.writeArray(buffer, packet.getCraftingData(), new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v465.serializer.CraftingDataSerializer_v465$$ExternalSyntheticLambda5
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                CraftingDataSerializer_v465.this.writeEntry((ByteBuf) obj, (BedrockCodecHelper) obj2, (RecipeData) obj3);
            }
        });
        helper.writeArray(buffer, packet.getPotionMixData(), new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v465.serializer.CraftingDataSerializer_v465$$ExternalSyntheticLambda6
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                CraftingDataSerializer_v465.this.writePotionMixData((ByteBuf) obj, (BedrockCodecHelper) obj2, (PotionMixData) obj3);
            }
        });
        helper.writeArray(buffer, packet.getContainerMixData(), new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v465.serializer.CraftingDataSerializer_v465$$ExternalSyntheticLambda7
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                CraftingDataSerializer_v465.this.writeContainerMixData((ByteBuf) obj, (BedrockCodecHelper) obj2, (ContainerMixData) obj3);
            }
        });
        helper.writeArray(buffer, packet.getMaterialReducers(), new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v465.serializer.CraftingDataSerializer_v465$$ExternalSyntheticLambda8
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                CraftingDataSerializer_v465.this.writeMaterialReducer((ByteBuf) obj, (BedrockCodecHelper) obj2, (MaterialReducer) obj3);
            }
        });
        buffer.writeBoolean(packet.isCleanRecipes());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v388.serializer.CraftingDataSerializer_v388, org.cloudburstmc.protocol.bedrock.codec.v291.serializer.CraftingDataSerializer_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, CraftingDataPacket packet) {
        helper.readArray(buffer, packet.getCraftingData(), new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v465.serializer.CraftingDataSerializer_v465$$ExternalSyntheticLambda1
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                RecipeData readEntry;
                readEntry = CraftingDataSerializer_v465.this.readEntry((ByteBuf) obj, (BedrockCodecHelper) obj2);
                return readEntry;
            }
        });
        helper.readArray(buffer, packet.getPotionMixData(), new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v465.serializer.CraftingDataSerializer_v465$$ExternalSyntheticLambda2
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                PotionMixData readPotionMixData;
                readPotionMixData = CraftingDataSerializer_v465.this.readPotionMixData((ByteBuf) obj, (BedrockCodecHelper) obj2);
                return readPotionMixData;
            }
        });
        helper.readArray(buffer, packet.getContainerMixData(), new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v465.serializer.CraftingDataSerializer_v465$$ExternalSyntheticLambda3
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                ContainerMixData readContainerMixData;
                readContainerMixData = CraftingDataSerializer_v465.this.readContainerMixData((ByteBuf) obj, (BedrockCodecHelper) obj2);
                return readContainerMixData;
            }
        });
        helper.readArray(buffer, packet.getMaterialReducers(), new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v465.serializer.CraftingDataSerializer_v465$$ExternalSyntheticLambda4
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return CraftingDataSerializer_v465.this.readMaterialReducer((ByteBuf) obj, (BedrockCodecHelper) obj2);
            }
        });
        packet.setCleanRecipes(buffer.readBoolean());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writeMaterialReducer(final ByteBuf buffer, BedrockCodecHelper helper, MaterialReducer reducer) {
        VarInts.writeInt(buffer, reducer.getInputId());
        helper.writeArray(buffer, reducer.getItemCounts().object2IntEntrySet(), new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v465.serializer.CraftingDataSerializer_v465$$ExternalSyntheticLambda0
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                CraftingDataSerializer_v465.lambda$writeMaterialReducer$0(ByteBuf.this, (ByteBuf) obj, (Object2IntMap.Entry) obj2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$writeMaterialReducer$0(ByteBuf buffer, ByteBuf buf, Object2IntMap.Entry entry) {
        VarInts.writeInt(buffer, ((ItemDefinition) entry.getKey()).getRuntimeId());
        VarInts.writeInt(buffer, entry.getIntValue());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public MaterialReducer readMaterialReducer(ByteBuf buffer, BedrockCodecHelper helper) {
        int inputId = VarInts.readInt(buffer);
        Object2IntMap<ItemDefinition> definitions = new Object2IntOpenHashMap<>();
        int length = VarInts.readUnsignedInt(buffer);
        for (int i = 0; i < length; i++) {
            definitions.put((Object2IntMap<ItemDefinition>) helper.getItemDefinitions().getDefinition(VarInts.readInt(buffer)), VarInts.readInt(buffer));
        }
        return new MaterialReducer(inputId, definitions);
    }
}
