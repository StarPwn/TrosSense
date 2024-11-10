package org.cloudburstmc.protocol.bedrock.codec.v388.serializer;

import io.netty.buffer.ByteBuf;
import java.util.function.BiFunction;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v361.serializer.CraftingDataSerializer_v361;
import org.cloudburstmc.protocol.bedrock.data.inventory.crafting.ContainerMixData;
import org.cloudburstmc.protocol.bedrock.data.inventory.crafting.PotionMixData;
import org.cloudburstmc.protocol.bedrock.data.inventory.crafting.recipe.RecipeData;
import org.cloudburstmc.protocol.bedrock.packet.CraftingDataPacket;
import org.cloudburstmc.protocol.common.util.TriConsumer;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class CraftingDataSerializer_v388 extends CraftingDataSerializer_v361 {
    public static final CraftingDataSerializer_v388 INSTANCE = new CraftingDataSerializer_v388();

    @Override // org.cloudburstmc.protocol.bedrock.codec.v291.serializer.CraftingDataSerializer_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, CraftingDataPacket packet) {
        helper.writeArray(buffer, packet.getCraftingData(), new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v388.serializer.CraftingDataSerializer_v388$$ExternalSyntheticLambda0
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                CraftingDataSerializer_v388.this.writeEntry((ByteBuf) obj, (BedrockCodecHelper) obj2, (RecipeData) obj3);
            }
        });
        helper.writeArray(buffer, packet.getPotionMixData(), new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v388.serializer.CraftingDataSerializer_v388$$ExternalSyntheticLambda1
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                CraftingDataSerializer_v388.this.writePotionMixData((ByteBuf) obj, (BedrockCodecHelper) obj2, (PotionMixData) obj3);
            }
        });
        helper.writeArray(buffer, packet.getContainerMixData(), new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v388.serializer.CraftingDataSerializer_v388$$ExternalSyntheticLambda2
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                CraftingDataSerializer_v388.this.writeContainerMixData((ByteBuf) obj, (BedrockCodecHelper) obj2, (ContainerMixData) obj3);
            }
        });
        buffer.writeBoolean(packet.isCleanRecipes());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v291.serializer.CraftingDataSerializer_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, CraftingDataPacket packet) {
        helper.readArray(buffer, packet.getCraftingData(), new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v388.serializer.CraftingDataSerializer_v388$$ExternalSyntheticLambda3
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                RecipeData readEntry;
                readEntry = CraftingDataSerializer_v388.this.readEntry((ByteBuf) obj, (BedrockCodecHelper) obj2);
                return readEntry;
            }
        });
        helper.readArray(buffer, packet.getPotionMixData(), new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v388.serializer.CraftingDataSerializer_v388$$ExternalSyntheticLambda4
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return CraftingDataSerializer_v388.this.readPotionMixData((ByteBuf) obj, (BedrockCodecHelper) obj2);
            }
        });
        helper.readArray(buffer, packet.getContainerMixData(), new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v388.serializer.CraftingDataSerializer_v388$$ExternalSyntheticLambda5
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return CraftingDataSerializer_v388.this.readContainerMixData((ByteBuf) obj, (BedrockCodecHelper) obj2);
            }
        });
        packet.setCleanRecipes(buffer.readBoolean());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public PotionMixData readPotionMixData(ByteBuf buffer, BedrockCodecHelper helper) {
        int fromPotionId = VarInts.readInt(buffer);
        int ingredient = VarInts.readInt(buffer);
        int toPotionId = VarInts.readInt(buffer);
        return new PotionMixData(fromPotionId, 0, ingredient, 0, toPotionId, 0);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writePotionMixData(ByteBuf buffer, BedrockCodecHelper helper, PotionMixData potionMixData) {
        VarInts.writeInt(buffer, potionMixData.getInputId());
        VarInts.writeInt(buffer, potionMixData.getReagentId());
        VarInts.writeInt(buffer, potionMixData.getOutputId());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ContainerMixData readContainerMixData(ByteBuf buffer, BedrockCodecHelper helper) {
        int fromItemId = VarInts.readInt(buffer);
        int ingredient = VarInts.readInt(buffer);
        int toItemId = VarInts.readInt(buffer);
        return new ContainerMixData(fromItemId, ingredient, toItemId);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writeContainerMixData(ByteBuf buffer, BedrockCodecHelper helper, ContainerMixData containerMixData) {
        VarInts.writeInt(buffer, containerMixData.getInputId());
        VarInts.writeInt(buffer, containerMixData.getReagentId());
        VarInts.writeInt(buffer, containerMixData.getOutputId());
    }
}
