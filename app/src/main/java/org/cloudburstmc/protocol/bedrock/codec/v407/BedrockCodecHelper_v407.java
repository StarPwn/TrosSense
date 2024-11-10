package org.cloudburstmc.protocol.bedrock.codec.v407;

import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.EntityDataTypeMap;
import org.cloudburstmc.protocol.bedrock.codec.v390.BedrockCodecHelper_v390;
import org.cloudburstmc.protocol.bedrock.data.definitions.ItemDefinition;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityLinkData;
import org.cloudburstmc.protocol.bedrock.data.inventory.ContainerSlotType;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;
import org.cloudburstmc.protocol.bedrock.data.inventory.descriptor.DefaultDescriptor;
import org.cloudburstmc.protocol.bedrock.data.inventory.descriptor.InvalidDescriptor;
import org.cloudburstmc.protocol.bedrock.data.inventory.descriptor.ItemDescriptorWithCount;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.ItemStackRequest;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.ItemStackRequestSlotData;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.AutoCraftRecipeAction;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.BeaconPaymentAction;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.ConsumeAction;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.CraftCreativeAction;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.CraftNonImplementedAction;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.CraftRecipeAction;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.CraftResultsDeprecatedAction;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.CreateAction;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.DestroyAction;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.DropAction;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.ItemStackRequestAction;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.ItemStackRequestActionType;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.LabTableCombineAction;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.PlaceAction;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.RecipeItemStackRequestAction;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.SwapAction;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.TakeAction;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.TransferItemStackRequestAction;
import org.cloudburstmc.protocol.bedrock.data.inventory.transaction.InventoryActionData;
import org.cloudburstmc.protocol.bedrock.data.inventory.transaction.InventorySource;
import org.cloudburstmc.protocol.common.util.Preconditions;
import org.cloudburstmc.protocol.common.util.TriConsumer;
import org.cloudburstmc.protocol.common.util.TypeMap;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class BedrockCodecHelper_v407 extends BedrockCodecHelper_v390 {
    protected final TypeMap<ContainerSlotType> containerSlotTypes;
    protected final TypeMap<ItemStackRequestActionType> stackRequestActionTypes;

    public BedrockCodecHelper_v407(EntityDataTypeMap entityData, TypeMap<Class<?>> gameRulesTypes, TypeMap<ItemStackRequestActionType> stackRequestActionTypes, TypeMap<ContainerSlotType> containerSlotTypes) {
        super(entityData, gameRulesTypes);
        this.stackRequestActionTypes = stackRequestActionTypes;
        this.containerSlotTypes = containerSlotTypes;
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v291.BedrockCodecHelper_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public EntityLinkData readEntityLink(ByteBuf buffer) {
        return new EntityLinkData(VarInts.readLong(buffer), VarInts.readLong(buffer), EntityLinkData.Type.byId(buffer.readUnsignedByte()), buffer.readBoolean(), buffer.readBoolean());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v291.BedrockCodecHelper_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writeEntityLink(ByteBuf buffer, EntityLinkData entityLink) {
        Preconditions.checkNotNull(entityLink, "entityLink");
        VarInts.writeLong(buffer, entityLink.getFrom());
        VarInts.writeLong(buffer, entityLink.getTo());
        buffer.writeByte(entityLink.getType().ordinal());
        buffer.writeBoolean(entityLink.isImmediate());
        buffer.writeBoolean(entityLink.isRiderInitiated());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BaseBedrockCodecHelper, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public boolean readInventoryActions(ByteBuf buffer, List<InventoryActionData> actions) {
        final boolean hasNetworkIds = buffer.readBoolean();
        readArray(buffer, actions, new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v407.BedrockCodecHelper_v407$$ExternalSyntheticLambda0
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return BedrockCodecHelper_v407.this.m2069x7abb7912(hasNetworkIds, (ByteBuf) obj, (BedrockCodecHelper) obj2);
            }
        });
        return hasNetworkIds;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$readInventoryActions$0$org-cloudburstmc-protocol-bedrock-codec-v407-BedrockCodecHelper_v407, reason: not valid java name */
    public /* synthetic */ InventoryActionData m2069x7abb7912(boolean hasNetworkIds, ByteBuf buf, BedrockCodecHelper helper) {
        int networkStackId;
        InventorySource source = readSource(buf);
        int slot = VarInts.readUnsignedInt(buf);
        ItemData fromItem = helper.readItem(buf);
        ItemData toItem = helper.readItem(buf);
        if (!hasNetworkIds) {
            networkStackId = 0;
        } else {
            int networkStackId2 = VarInts.readInt(buf);
            networkStackId = networkStackId2;
        }
        return new InventoryActionData(source, slot, fromItem, toItem, networkStackId);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BaseBedrockCodecHelper, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writeInventoryActions(final ByteBuf buffer, List<InventoryActionData> actions, final boolean hasNetworkIds) {
        buffer.writeBoolean(hasNetworkIds);
        writeArray(buffer, actions, new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v407.BedrockCodecHelper_v407$$ExternalSyntheticLambda3
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                BedrockCodecHelper_v407.this.m2071x20cf6c62(buffer, hasNetworkIds, (ByteBuf) obj, (BedrockCodecHelper) obj2, (InventoryActionData) obj3);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$writeInventoryActions$1$org-cloudburstmc-protocol-bedrock-codec-v407-BedrockCodecHelper_v407, reason: not valid java name */
    public /* synthetic */ void m2071x20cf6c62(ByteBuf buffer, boolean hasNetworkIds, ByteBuf buf, BedrockCodecHelper helper, InventoryActionData action) {
        writeSource(buffer, action.getSource());
        VarInts.writeUnsignedInt(buffer, action.getSlot());
        helper.writeItem(buffer, action.getFromItem());
        helper.writeItem(buffer, action.getToItem());
        if (hasNetworkIds) {
            VarInts.writeInt(buffer, action.getStackNetworkId());
        }
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v291.BedrockCodecHelper_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public ItemData readNetItem(ByteBuf buffer) {
        int netId = VarInts.readInt(buffer);
        ItemData item = readItem(buffer);
        item.setNetId(netId);
        return item;
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v291.BedrockCodecHelper_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writeNetItem(ByteBuf buffer, ItemData item) {
        VarInts.writeInt(buffer, item.getNetId());
        writeItem(buffer, item);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BaseBedrockCodecHelper, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public ItemStackRequest readItemStackRequest(ByteBuf buffer) {
        int requestId = VarInts.readInt(buffer);
        ArrayList arrayList = new ArrayList();
        readArray(buffer, arrayList, new Function() { // from class: org.cloudburstmc.protocol.bedrock.codec.v407.BedrockCodecHelper_v407$$ExternalSyntheticLambda5
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return BedrockCodecHelper_v407.this.m2070xef0dd297((ByteBuf) obj);
            }
        });
        return new ItemStackRequest(requestId, (ItemStackRequestAction[]) arrayList.toArray(new ItemStackRequestAction[0]), new String[0]);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$readItemStackRequest$2$org-cloudburstmc-protocol-bedrock-codec-v407-BedrockCodecHelper_v407, reason: not valid java name */
    public /* synthetic */ ItemStackRequestAction m2070xef0dd297(ByteBuf byteBuf) {
        ItemStackRequestActionType type = this.stackRequestActionTypes.getType(byteBuf.readByte());
        return readRequestActionData(byteBuf, type);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BaseBedrockCodecHelper, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writeItemStackRequest(ByteBuf buffer, ItemStackRequest request) {
        VarInts.writeInt(buffer, request.getRequestId());
        writeArray(buffer, request.getActions(), new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v407.BedrockCodecHelper_v407$$ExternalSyntheticLambda1
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                BedrockCodecHelper_v407.this.m2072x9521c5e7((ByteBuf) obj, (ItemStackRequestAction) obj2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$writeItemStackRequest$3$org-cloudburstmc-protocol-bedrock-codec-v407-BedrockCodecHelper_v407, reason: not valid java name */
    public /* synthetic */ void m2072x9521c5e7(ByteBuf byteBuf, ItemStackRequestAction action) {
        ItemStackRequestActionType type = action.getType();
        byteBuf.writeByte(this.stackRequestActionTypes.getId(type));
        writeRequestActionData(byteBuf, action);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writeRequestActionData(ByteBuf byteBuf, ItemStackRequestAction action) {
        switch (action.getType()) {
            case TAKE:
            case PLACE:
                byteBuf.writeByte(((TransferItemStackRequestAction) action).getCount());
                writeStackRequestSlotInfo(byteBuf, ((TransferItemStackRequestAction) action).getSource());
                writeStackRequestSlotInfo(byteBuf, ((TransferItemStackRequestAction) action).getDestination());
                return;
            case SWAP:
                writeStackRequestSlotInfo(byteBuf, ((SwapAction) action).getSource());
                writeStackRequestSlotInfo(byteBuf, ((SwapAction) action).getDestination());
                return;
            case DROP:
                byteBuf.writeByte(((DropAction) action).getCount());
                writeStackRequestSlotInfo(byteBuf, ((DropAction) action).getSource());
                byteBuf.writeBoolean(((DropAction) action).isRandomly());
                return;
            case DESTROY:
                byteBuf.writeByte(((DestroyAction) action).getCount());
                writeStackRequestSlotInfo(byteBuf, ((DestroyAction) action).getSource());
                return;
            case CONSUME:
                byteBuf.writeByte(((ConsumeAction) action).getCount());
                writeStackRequestSlotInfo(byteBuf, ((ConsumeAction) action).getSource());
                return;
            case CREATE:
                byteBuf.writeByte(((CreateAction) action).getSlot());
                return;
            case LAB_TABLE_COMBINE:
            case CRAFT_NON_IMPLEMENTED_DEPRECATED:
                return;
            case BEACON_PAYMENT:
                VarInts.writeInt(byteBuf, ((BeaconPaymentAction) action).getPrimaryEffect());
                VarInts.writeInt(byteBuf, ((BeaconPaymentAction) action).getSecondaryEffect());
                return;
            case CRAFT_RECIPE:
            case CRAFT_RECIPE_AUTO:
                VarInts.writeUnsignedInt(byteBuf, ((RecipeItemStackRequestAction) action).getRecipeNetworkId());
                return;
            case CRAFT_CREATIVE:
                VarInts.writeUnsignedInt(byteBuf, ((CraftCreativeAction) action).getCreativeItemNetworkId());
                return;
            case CRAFT_RESULTS_DEPRECATED:
                writeArray(byteBuf, ((CraftResultsDeprecatedAction) action).getResultItems(), new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v407.BedrockCodecHelper_v407$$ExternalSyntheticLambda4
                    @Override // java.util.function.BiConsumer
                    public final void accept(Object obj, Object obj2) {
                        BedrockCodecHelper_v407.this.m2073x524fe195((ByteBuf) obj, (ItemData) obj2);
                    }
                });
                byteBuf.writeByte(((CraftResultsDeprecatedAction) action).getTimesCrafted());
                return;
            default:
                throw new UnsupportedOperationException("Unhandled stack request action type: " + action.getType());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$writeRequestActionData$4$org-cloudburstmc-protocol-bedrock-codec-v407-BedrockCodecHelper_v407, reason: not valid java name */
    public /* synthetic */ void m2073x524fe195(ByteBuf buf2, ItemData item) {
        writeItem(buf2, item);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ItemStackRequestAction readRequestActionData(ByteBuf byteBuf, ItemStackRequestActionType type) {
        switch (type) {
            case TAKE:
                return new TakeAction(byteBuf.readUnsignedByte(), readStackRequestSlotInfo(byteBuf), readStackRequestSlotInfo(byteBuf));
            case PLACE:
                return new PlaceAction(byteBuf.readUnsignedByte(), readStackRequestSlotInfo(byteBuf), readStackRequestSlotInfo(byteBuf));
            case SWAP:
                return new SwapAction(readStackRequestSlotInfo(byteBuf), readStackRequestSlotInfo(byteBuf));
            case DROP:
                return new DropAction(byteBuf.readUnsignedByte(), readStackRequestSlotInfo(byteBuf), byteBuf.readBoolean());
            case DESTROY:
                return new DestroyAction(byteBuf.readUnsignedByte(), readStackRequestSlotInfo(byteBuf));
            case CONSUME:
                return new ConsumeAction(byteBuf.readUnsignedByte(), readStackRequestSlotInfo(byteBuf));
            case CREATE:
                return new CreateAction(byteBuf.readUnsignedByte());
            case LAB_TABLE_COMBINE:
                return new LabTableCombineAction();
            case BEACON_PAYMENT:
                return new BeaconPaymentAction(VarInts.readInt(byteBuf), VarInts.readInt(byteBuf));
            case CRAFT_RECIPE:
                return new CraftRecipeAction(VarInts.readUnsignedInt(byteBuf));
            case CRAFT_RECIPE_AUTO:
                return new AutoCraftRecipeAction(VarInts.readUnsignedInt(byteBuf), 0, Collections.emptyList());
            case CRAFT_CREATIVE:
                return new CraftCreativeAction(VarInts.readUnsignedInt(byteBuf));
            case CRAFT_NON_IMPLEMENTED_DEPRECATED:
                return new CraftNonImplementedAction();
            case CRAFT_RESULTS_DEPRECATED:
                return new CraftResultsDeprecatedAction((ItemData[]) readArray(byteBuf, new ItemData[0], new Function() { // from class: org.cloudburstmc.protocol.bedrock.codec.v407.BedrockCodecHelper_v407$$ExternalSyntheticLambda2
                    @Override // java.util.function.Function
                    public final Object apply(Object obj) {
                        return BedrockCodecHelper_v407.this.readItem((ByteBuf) obj);
                    }
                }), byteBuf.readUnsignedByte());
            default:
                throw new UnsupportedOperationException("Unhandled stack request action type: " + type);
        }
    }

    protected ItemStackRequestSlotData readStackRequestSlotInfo(ByteBuf buffer) {
        return new ItemStackRequestSlotData(readContainerSlotType(buffer), buffer.readUnsignedByte(), VarInts.readInt(buffer));
    }

    protected void writeStackRequestSlotInfo(ByteBuf buffer, ItemStackRequestSlotData data) {
        writeContainerSlotType(buffer, data.getContainer());
        buffer.writeByte(data.getSlot());
        VarInts.writeInt(buffer, data.getStackNetworkId());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BaseBedrockCodecHelper, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public ContainerSlotType readContainerSlotType(ByteBuf buffer) {
        return this.containerSlotTypes.getType(buffer.readByte());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BaseBedrockCodecHelper, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writeContainerSlotType(ByteBuf buffer, ContainerSlotType slotType) {
        buffer.writeByte(this.containerSlotTypes.getId(slotType));
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BaseBedrockCodecHelper, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public ItemDescriptorWithCount readIngredient(ByteBuf buffer) {
        int runtimeId = VarInts.readInt(buffer);
        if (runtimeId == 0) {
            return ItemDescriptorWithCount.EMPTY;
        }
        ItemDefinition definition = getItemDefinitions().getDefinition(runtimeId);
        int meta = fromAuxValue(VarInts.readInt(buffer));
        int count = VarInts.readInt(buffer);
        return new ItemDescriptorWithCount(new DefaultDescriptor(definition, meta), count);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BaseBedrockCodecHelper, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writeIngredient(ByteBuf buffer, ItemDescriptorWithCount ingredient) {
        Objects.requireNonNull(ingredient, "ingredient is null");
        if (ingredient == ItemDescriptorWithCount.EMPTY || ingredient.getDescriptor() == InvalidDescriptor.INSTANCE) {
            VarInts.writeInt(buffer, 0);
            return;
        }
        Preconditions.checkArgument(ingredient.getDescriptor() instanceof DefaultDescriptor, "Descriptor must be of type DefaultDescriptor");
        DefaultDescriptor descriptor = (DefaultDescriptor) ingredient.getDescriptor();
        VarInts.writeInt(buffer, descriptor.getItemId().getRuntimeId());
        VarInts.writeInt(buffer, toAuxValue(descriptor.getAuxValue()));
        VarInts.writeInt(buffer, ingredient.getCount());
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
