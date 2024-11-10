package org.cloudburstmc.protocol.bedrock.codec.v419.serializer;

import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v407.serializer.ItemStackResponseSerializer_v407;
import org.cloudburstmc.protocol.bedrock.data.inventory.ContainerSlotType;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.response.ItemStackResponse;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.response.ItemStackResponseContainer;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.response.ItemStackResponseSlot;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.response.ItemStackResponseStatus;
import org.cloudburstmc.protocol.bedrock.packet.ItemStackResponsePacket;
import org.cloudburstmc.protocol.common.util.TriConsumer;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class ItemStackResponseSerializer_v419 extends ItemStackResponseSerializer_v407 {
    public static final ItemStackResponseSerializer_v419 INSTANCE = new ItemStackResponseSerializer_v419();

    @Override // org.cloudburstmc.protocol.bedrock.codec.v407.serializer.ItemStackResponseSerializer_v407, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(final ByteBuf buffer, final BedrockCodecHelper helper, ItemStackResponsePacket packet) {
        helper.writeArray(buffer, packet.getEntries(), new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v419.serializer.ItemStackResponseSerializer_v419$$ExternalSyntheticLambda1
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ItemStackResponseSerializer_v419.this.m2083x92cc7675(buffer, helper, (ByteBuf) obj, (ItemStackResponse) obj2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$serialize$1$org-cloudburstmc-protocol-bedrock-codec-v419-serializer-ItemStackResponseSerializer_v419, reason: not valid java name */
    public /* synthetic */ void m2083x92cc7675(ByteBuf buffer, final BedrockCodecHelper helper, ByteBuf buf, ItemStackResponse response) {
        buf.writeByte(response.getResult().ordinal());
        VarInts.writeInt(buffer, response.getRequestId());
        if (response.getResult() != ItemStackResponseStatus.OK) {
            return;
        }
        helper.writeArray(buf, response.getContainers(), new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v419.serializer.ItemStackResponseSerializer_v419$$ExternalSyntheticLambda2
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ItemStackResponseSerializer_v419.this.m2080xdf713cb5(helper, (ByteBuf) obj, (ItemStackResponseContainer) obj2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$null$0$org-cloudburstmc-protocol-bedrock-codec-v419-serializer-ItemStackResponseSerializer_v419, reason: not valid java name */
    public /* synthetic */ void m2080xdf713cb5(BedrockCodecHelper helper, ByteBuf buf2, ItemStackResponseContainer containerEntry) {
        helper.writeContainerSlotType(buf2, containerEntry.getContainer());
        helper.writeArray(buf2, containerEntry.getItems(), new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v419.serializer.ItemStackResponseSerializer_v419$$ExternalSyntheticLambda5
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                ItemStackResponseSerializer_v419.this.writeItemEntry((ByteBuf) obj, (BedrockCodecHelper) obj2, (ItemStackResponseSlot) obj3);
            }
        });
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v407.serializer.ItemStackResponseSerializer_v407, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, final BedrockCodecHelper helper, ItemStackResponsePacket packet) {
        List<ItemStackResponse> entries = packet.getEntries();
        helper.readArray(buffer, entries, new Function() { // from class: org.cloudburstmc.protocol.bedrock.codec.v419.serializer.ItemStackResponseSerializer_v419$$ExternalSyntheticLambda4
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ItemStackResponseSerializer_v419.this.m2079x637c6493(helper, (ByteBuf) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$deserialize$4$org-cloudburstmc-protocol-bedrock-codec-v419-serializer-ItemStackResponseSerializer_v419, reason: not valid java name */
    public /* synthetic */ ItemStackResponse m2079x637c6493(final BedrockCodecHelper helper, ByteBuf buf) {
        ItemStackResponseStatus result = ItemStackResponseStatus.values()[buf.readByte()];
        int requestId = VarInts.readInt(buf);
        if (result != ItemStackResponseStatus.OK) {
            return new ItemStackResponse(result, requestId, (List<ItemStackResponseContainer>) Collections.emptyList());
        }
        ArrayList arrayList = new ArrayList();
        helper.readArray(buf, arrayList, new Function() { // from class: org.cloudburstmc.protocol.bedrock.codec.v419.serializer.ItemStackResponseSerializer_v419$$ExternalSyntheticLambda3
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ItemStackResponseSerializer_v419.this.m2082x8743ce12(helper, (ByteBuf) obj);
            }
        });
        return new ItemStackResponse(result, requestId, arrayList);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$null$3$org-cloudburstmc-protocol-bedrock-codec-v419-serializer-ItemStackResponseSerializer_v419, reason: not valid java name */
    public /* synthetic */ ItemStackResponseContainer m2082x8743ce12(final BedrockCodecHelper helper, final ByteBuf buf2) {
        ContainerSlotType container = helper.readContainerSlotType(buf2);
        ArrayList arrayList = new ArrayList();
        helper.readArray(buf2, arrayList, new Function() { // from class: org.cloudburstmc.protocol.bedrock.codec.v419.serializer.ItemStackResponseSerializer_v419$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ItemStackResponseSerializer_v419.this.m2081x4f52f2f3(buf2, helper, (ByteBuf) obj);
            }
        });
        return new ItemStackResponseContainer(container, arrayList);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$null$2$org-cloudburstmc-protocol-bedrock-codec-v419-serializer-ItemStackResponseSerializer_v419, reason: not valid java name */
    public /* synthetic */ ItemStackResponseSlot m2081x4f52f2f3(ByteBuf buf2, BedrockCodecHelper helper, ByteBuf byteBuf) {
        return m2076x6099e031(buf2, helper);
    }
}
