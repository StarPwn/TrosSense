package org.cloudburstmc.protocol.bedrock.codec.v422.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v419.serializer.ItemStackResponseSerializer_v419;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.response.ItemStackResponseSlot;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class ItemStackResponseSerializer_v422 extends ItemStackResponseSerializer_v419 {
    public static final ItemStackResponseSerializer_v422 INSTANCE = new ItemStackResponseSerializer_v422();

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v407.serializer.ItemStackResponseSerializer_v407
    /* renamed from: readItemEntry */
    public ItemStackResponseSlot m2076x6099e031(ByteBuf buffer, BedrockCodecHelper helper) {
        return new ItemStackResponseSlot(buffer.readUnsignedByte(), buffer.readUnsignedByte(), buffer.readUnsignedByte(), VarInts.readInt(buffer), helper.readString(buffer), 0);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v407.serializer.ItemStackResponseSerializer_v407
    public void writeItemEntry(ByteBuf buffer, BedrockCodecHelper helper, ItemStackResponseSlot itemEntry) {
        super.writeItemEntry(buffer, helper, itemEntry);
        helper.writeString(buffer, itemEntry.getCustomName());
    }
}
