package org.cloudburstmc.protocol.bedrock.codec.v291.serializer;

import io.netty.buffer.ByteBuf;
import java.util.function.Function;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;

/* compiled from: D8$$SyntheticClass */
/* loaded from: classes5.dex */
public final /* synthetic */ class GameRulesChangedSerializer_v291$$ExternalSyntheticLambda0 implements Function {
    public final /* synthetic */ BedrockCodecHelper f$0;

    public /* synthetic */ GameRulesChangedSerializer_v291$$ExternalSyntheticLambda0(BedrockCodecHelper bedrockCodecHelper) {
        this.f$0 = bedrockCodecHelper;
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        return this.f$0.readGameRule((ByteBuf) obj);
    }
}
