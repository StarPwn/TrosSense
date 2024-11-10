package org.cloudburstmc.protocol.bedrock.codec.v554;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.function.Function;
import org.cloudburstmc.protocol.bedrock.codec.EntityDataTypeMap;
import org.cloudburstmc.protocol.bedrock.codec.v534.BedrockCodecHelper_v534;
import org.cloudburstmc.protocol.bedrock.data.Ability;
import org.cloudburstmc.protocol.bedrock.data.definitions.ItemDefinition;
import org.cloudburstmc.protocol.bedrock.data.inventory.ContainerSlotType;
import org.cloudburstmc.protocol.bedrock.data.inventory.descriptor.DefaultDescriptor;
import org.cloudburstmc.protocol.bedrock.data.inventory.descriptor.DeferredDescriptor;
import org.cloudburstmc.protocol.bedrock.data.inventory.descriptor.InvalidDescriptor;
import org.cloudburstmc.protocol.bedrock.data.inventory.descriptor.ItemDescriptor;
import org.cloudburstmc.protocol.bedrock.data.inventory.descriptor.ItemDescriptorType;
import org.cloudburstmc.protocol.bedrock.data.inventory.descriptor.ItemDescriptorWithCount;
import org.cloudburstmc.protocol.bedrock.data.inventory.descriptor.ItemTagDescriptor;
import org.cloudburstmc.protocol.bedrock.data.inventory.descriptor.MolangDescriptor;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.ItemStackRequest;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.TextProcessingEventOrigin;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.ItemStackRequestAction;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.ItemStackRequestActionType;
import org.cloudburstmc.protocol.common.util.TypeMap;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class BedrockCodecHelper_v554 extends BedrockCodecHelper_v534 {
    protected static final ItemDescriptorType[] DESCRIPTOR_TYPES = ItemDescriptorType.values();
    protected final TypeMap<TextProcessingEventOrigin> textProcessingEventOrigins;

    public BedrockCodecHelper_v554(EntityDataTypeMap entityData, TypeMap<Class<?>> gameRulesTypes, TypeMap<ItemStackRequestActionType> stackRequestActionTypes, TypeMap<ContainerSlotType> containerSlotTypes, TypeMap<Ability> abilities, TypeMap<TextProcessingEventOrigin> textProcessingEventOrigins) {
        super(entityData, gameRulesTypes, stackRequestActionTypes, containerSlotTypes, abilities);
        this.textProcessingEventOrigins = textProcessingEventOrigins;
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v422.BedrockCodecHelper_v422, org.cloudburstmc.protocol.bedrock.codec.v407.BedrockCodecHelper_v407, org.cloudburstmc.protocol.bedrock.codec.BaseBedrockCodecHelper, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public ItemStackRequest readItemStackRequest(ByteBuf buffer) {
        int requestId = VarInts.readInt(buffer);
        ObjectArrayList objectArrayList = new ObjectArrayList();
        readArray(buffer, objectArrayList, new Function() { // from class: org.cloudburstmc.protocol.bedrock.codec.v554.BedrockCodecHelper_v554$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return BedrockCodecHelper_v554.this.m2093x334bdf0b((ByteBuf) obj);
            }
        });
        ObjectArrayList objectArrayList2 = new ObjectArrayList();
        readArray(buffer, objectArrayList2, new Function() { // from class: org.cloudburstmc.protocol.bedrock.codec.v554.BedrockCodecHelper_v554$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return BedrockCodecHelper_v554.this.readString((ByteBuf) obj);
            }
        });
        int originVal = buffer.readIntLE();
        TextProcessingEventOrigin origin = originVal == -1 ? null : this.textProcessingEventOrigins.getType(originVal);
        return new ItemStackRequest(requestId, (ItemStackRequestAction[]) objectArrayList.toArray(new ItemStackRequestAction[0]), (String[]) objectArrayList2.toArray(new String[0]), origin);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$readItemStackRequest$0$org-cloudburstmc-protocol-bedrock-codec-v554-BedrockCodecHelper_v554, reason: not valid java name */
    public /* synthetic */ ItemStackRequestAction m2093x334bdf0b(ByteBuf byteBuf) {
        ItemStackRequestActionType type = this.stackRequestActionTypes.getType(byteBuf.readByte());
        return readRequestActionData(byteBuf, type);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v422.BedrockCodecHelper_v422, org.cloudburstmc.protocol.bedrock.codec.v407.BedrockCodecHelper_v407, org.cloudburstmc.protocol.bedrock.codec.BaseBedrockCodecHelper, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writeItemStackRequest(ByteBuf buffer, ItemStackRequest request) {
        super.writeItemStackRequest(buffer, request);
        TextProcessingEventOrigin origin = request.getTextProcessingEventOrigin();
        buffer.writeIntLE(origin == null ? -1 : this.textProcessingEventOrigins.getId(origin));
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v407.BedrockCodecHelper_v407, org.cloudburstmc.protocol.bedrock.codec.BaseBedrockCodecHelper, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public ItemDescriptorWithCount readIngredient(ByteBuf buffer) {
        ItemDescriptorType type = DESCRIPTOR_TYPES[buffer.readUnsignedByte()];
        ItemDescriptor descriptor = readItemDescriptor(buffer, type);
        return new ItemDescriptorWithCount(descriptor, VarInts.readInt(buffer));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ItemDescriptor readItemDescriptor(ByteBuf buffer, ItemDescriptorType type) {
        switch (type) {
            case DEFAULT:
                int itemId = buffer.readShortLE();
                ItemDefinition definition = itemId == 0 ? ItemDefinition.AIR : getItemDefinitions().getDefinition(itemId);
                int auxValue = itemId != 0 ? buffer.readShortLE() : 0;
                ItemDescriptor descriptor = new DefaultDescriptor(definition, auxValue);
                return descriptor;
            case MOLANG:
                ItemDescriptor descriptor2 = new MolangDescriptor(readString(buffer), buffer.readUnsignedByte());
                return descriptor2;
            case ITEM_TAG:
                ItemDescriptor descriptor3 = new ItemTagDescriptor(readString(buffer));
                return descriptor3;
            case DEFERRED:
                ItemDescriptor descriptor4 = new DeferredDescriptor(readString(buffer), buffer.readShortLE());
                return descriptor4;
            default:
                ItemDescriptor descriptor5 = InvalidDescriptor.INSTANCE;
                return descriptor5;
        }
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v407.BedrockCodecHelper_v407, org.cloudburstmc.protocol.bedrock.codec.BaseBedrockCodecHelper, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writeIngredient(ByteBuf buffer, ItemDescriptorWithCount ingredient) {
        buffer.writeByte(ingredient.getDescriptor().getType().ordinal());
        writeItemDescriptor(buffer, ingredient.getDescriptor());
        VarInts.writeInt(buffer, ingredient.getCount());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writeItemDescriptor(ByteBuf buffer, ItemDescriptor descriptor) {
        switch (descriptor.getType()) {
            case DEFAULT:
                DefaultDescriptor defaultDescriptor = (DefaultDescriptor) descriptor;
                boolean empty = defaultDescriptor.getItemId() == null || defaultDescriptor.getItemId().getRuntimeId() == 0;
                buffer.writeShortLE(empty ? 0 : defaultDescriptor.getItemId().getRuntimeId());
                if (!empty) {
                    buffer.writeShortLE(defaultDescriptor.getAuxValue());
                    return;
                }
                return;
            case MOLANG:
                MolangDescriptor molangDescriptor = (MolangDescriptor) descriptor;
                writeString(buffer, molangDescriptor.getTagExpression());
                buffer.writeByte(molangDescriptor.getMolangVersion());
                return;
            case ITEM_TAG:
                ItemTagDescriptor tagDescriptor = (ItemTagDescriptor) descriptor;
                writeString(buffer, tagDescriptor.getItemTag());
                return;
            case DEFERRED:
                DeferredDescriptor deferredDescriptor = (DeferredDescriptor) descriptor;
                writeString(buffer, deferredDescriptor.getFullName());
                buffer.writeShortLE(deferredDescriptor.getAuxValue());
                return;
            default:
                return;
        }
    }
}
