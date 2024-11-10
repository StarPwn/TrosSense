package org.cloudburstmc.protocol.bedrock.codec.v291.serializer;

import io.netty.buffer.ByteBuf;
import java.util.function.Function;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;

/* compiled from: D8$$SyntheticClass */
/* loaded from: classes5.dex */
public final /* synthetic */ class AddEntitySerializer_v291$$ExternalSyntheticLambda3 implements Function {
    public final /* synthetic */ BedrockCodecHelper f$0;

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        return this.f$0.readEntityLink((ByteBuf) obj);
    }
}
