package org.cloudburstmc.protocol.bedrock.codec.v407.serializer;

import io.netty.buffer.ByteBuf;
import java.util.function.Function;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;

/* compiled from: D8$$SyntheticClass */
/* loaded from: classes5.dex */
public final /* synthetic */ class CraftingDataSerializer_v407$$ExternalSyntheticLambda1 implements Function {
    public final /* synthetic */ BedrockCodecHelper f$0;

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        return this.f$0.readItemInstance((ByteBuf) obj);
    }
}
