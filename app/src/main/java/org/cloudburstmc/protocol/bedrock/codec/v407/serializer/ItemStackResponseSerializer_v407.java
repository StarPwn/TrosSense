package org.cloudburstmc.protocol.bedrock.codec.v407.serializer;

import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.data.inventory.ContainerSlotType;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.response.ItemStackResponse;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.response.ItemStackResponseContainer;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.response.ItemStackResponseSlot;
import org.cloudburstmc.protocol.bedrock.packet.ItemStackResponsePacket;
import org.cloudburstmc.protocol.common.util.TriConsumer;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class ItemStackResponseSerializer_v407 implements BedrockPacketSerializer<ItemStackResponsePacket> {
    public static final ItemStackResponseSerializer_v407 INSTANCE = new ItemStackResponseSerializer_v407();

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(final ByteBuf buffer, final BedrockCodecHelper helper, ItemStackResponsePacket packet) {
        helper.writeArray(buffer, packet.getEntries(), new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v407.serializer.ItemStackResponseSerializer_v407$$ExternalSyntheticLambda1
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ItemStackResponseSerializer_v407.this.m2078xa41363b3(buffer, helper, (ByteBuf) obj, (ItemStackResponse) obj2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$serialize$1$org-cloudburstmc-protocol-bedrock-codec-v407-serializer-ItemStackResponseSerializer_v407, reason: not valid java name */
    public /* synthetic */ void m2078xa41363b3(ByteBuf buffer, final BedrockCodecHelper helper, ByteBuf buf, ItemStackResponse response) {
        buf.writeBoolean(response.isSuccess());
        VarInts.writeInt(buffer, response.getRequestId());
        if (!response.isSuccess()) {
            return;
        }
        helper.writeArray(buf, response.getContainers(), new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v407.serializer.ItemStackResponseSerializer_v407$$ExternalSyntheticLambda5
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ItemStackResponseSerializer_v407.this.m2075xf0b829f3(helper, (ByteBuf) obj, (ItemStackResponseContainer) obj2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$null$0$org-cloudburstmc-protocol-bedrock-codec-v407-serializer-ItemStackResponseSerializer_v407, reason: not valid java name */
    public /* synthetic */ void m2075xf0b829f3(BedrockCodecHelper helper, ByteBuf buf2, ItemStackResponseContainer containerEntry) {
        helper.writeContainerSlotType(buf2, containerEntry.getContainer());
        helper.writeArray(buf2, containerEntry.getItems(), new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v407.serializer.ItemStackResponseSerializer_v407$$ExternalSyntheticLambda3
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                ItemStackResponseSerializer_v407.this.writeItemEntry((ByteBuf) obj, (BedrockCodecHelper) obj2, (ItemStackResponseSlot) obj3);
            }
        });
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, final BedrockCodecHelper helper, ItemStackResponsePacket packet) {
        List<ItemStackResponse> entries = packet.getEntries();
        helper.readArray(buffer, entries, new Function() { // from class: org.cloudburstmc.protocol.bedrock.codec.v407.serializer.ItemStackResponseSerializer_v407$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ItemStackResponseSerializer_v407.this.m2074x74c351d1(helper, (ByteBuf) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$deserialize$4$org-cloudburstmc-protocol-bedrock-codec-v407-serializer-ItemStackResponseSerializer_v407, reason: not valid java name */
    public /* synthetic */ ItemStackResponse m2074x74c351d1(final BedrockCodecHelper helper, ByteBuf buf) {
        boolean success = buf.readBoolean();
        int requestId = VarInts.readInt(buf);
        if (!success) {
            return new ItemStackResponse(success, requestId, (List<ItemStackResponseContainer>) Collections.emptyList());
        }
        ArrayList arrayList = new ArrayList();
        helper.readArray(buf, arrayList, new Function() { // from class: org.cloudburstmc.protocol.bedrock.codec.v407.serializer.ItemStackResponseSerializer_v407$$ExternalSyntheticLambda4
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ItemStackResponseSerializer_v407.this.m2077x988abb50(helper, (ByteBuf) obj);
            }
        });
        return new ItemStackResponse(success, requestId, arrayList);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$null$3$org-cloudburstmc-protocol-bedrock-codec-v407-serializer-ItemStackResponseSerializer_v407, reason: not valid java name */
    public /* synthetic */ ItemStackResponseContainer m2077x988abb50(final BedrockCodecHelper helper, ByteBuf buf2) {
        ContainerSlotType container = helper.readContainerSlotType(buf2);
        ArrayList arrayList = new ArrayList();
        helper.readArray(buf2, arrayList, new Function() { // from class: org.cloudburstmc.protocol.bedrock.codec.v407.serializer.ItemStackResponseSerializer_v407$$ExternalSyntheticLambda2
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ItemStackResponseSerializer_v407.this.m2076x6099e031(helper, (ByteBuf) obj);
            }
        });
        return new ItemStackResponseContainer(container, arrayList);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: readItemEntry, reason: merged with bridge method [inline-methods] */
    public ItemStackResponseSlot m2076x6099e031(ByteBuf buffer, BedrockCodecHelper helper) {
        return new ItemStackResponseSlot(buffer.readUnsignedByte(), buffer.readUnsignedByte(), buffer.readUnsignedByte(), VarInts.readInt(buffer), "", 0);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writeItemEntry(ByteBuf buffer, BedrockCodecHelper helper, ItemStackResponseSlot itemEntry) {
        buffer.writeByte(itemEntry.getSlot());
        buffer.writeByte(itemEntry.getHotbarSlot());
        buffer.writeByte(itemEntry.getCount());
        VarInts.writeInt(buffer, itemEntry.getStackNetworkId());
    }
}
