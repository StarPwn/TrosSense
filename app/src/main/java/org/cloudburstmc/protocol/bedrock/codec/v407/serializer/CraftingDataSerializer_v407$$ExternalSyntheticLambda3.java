package org.cloudburstmc.protocol.bedrock.codec.v407.serializer;

import io.netty.buffer.ByteBuf;
import java.util.function.BiConsumer;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;

/* compiled from: D8$$SyntheticClass */
/* loaded from: classes5.dex */
public final /* synthetic */ class CraftingDataSerializer_v407$$ExternalSyntheticLambda3 implements BiConsumer {
    public final /* synthetic */ BedrockCodecHelper f$0;

    @Override // java.util.function.BiConsumer
    public final void accept(Object obj, Object obj2) {
        this.f$0.writeItemInstance((ByteBuf) obj, (ItemData) obj2);
    }
}
