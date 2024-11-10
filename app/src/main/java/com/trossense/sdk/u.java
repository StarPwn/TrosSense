package com.trossense.sdk;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;

/* loaded from: classes3.dex */
public class u implements BedrockPacketSerializer<l> {
    public static final u a = new u();

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public void serialize(ByteBuf byteBuf, BedrockCodecHelper bedrockCodecHelper, l lVar) {
        bedrockCodecHelper.writeString(byteBuf, lVar.a());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    /* renamed from: b, reason: merged with bridge method [inline-methods] */
    public void deserialize(ByteBuf byteBuf, BedrockCodecHelper bedrockCodecHelper, l lVar) {
        lVar.a(bedrockCodecHelper.readString(byteBuf));
    }
}
