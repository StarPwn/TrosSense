package org.cloudburstmc.protocol.bedrock.codec.v291.serializer;

import io.netty.buffer.ByteBuf;
import java.util.function.BiConsumer;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.data.GameRuleData;

/* compiled from: D8$$SyntheticClass */
/* loaded from: classes5.dex */
public final /* synthetic */ class GameRulesChangedSerializer_v291$$ExternalSyntheticLambda1 implements BiConsumer {
    public final /* synthetic */ BedrockCodecHelper f$0;

    public /* synthetic */ GameRulesChangedSerializer_v291$$ExternalSyntheticLambda1(BedrockCodecHelper bedrockCodecHelper) {
        this.f$0 = bedrockCodecHelper;
    }

    @Override // java.util.function.BiConsumer
    public final void accept(Object obj, Object obj2) {
        this.f$0.writeGameRule((ByteBuf) obj, (GameRuleData) obj2);
    }
}
