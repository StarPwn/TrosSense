package com.trossense.sdk;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;

/* loaded from: classes3.dex */
public class w implements BedrockPacketSerializer<m> {
    public static final w a = new w();

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public void serialize(ByteBuf byteBuf, BedrockCodecHelper bedrockCodecHelper, m mVar) {
        bedrockCodecHelper.writeByteArray(byteBuf, mVar.b());
        byteBuf.writeIntLE((int) mVar.c());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    /* renamed from: b, reason: merged with bridge method [inline-methods] */
    public void deserialize(ByteBuf byteBuf, BedrockCodecHelper bedrockCodecHelper, m mVar) {
        mVar.a(bedrockCodecHelper.readByteArray(byteBuf));
        mVar.a(byteBuf.readUnsignedIntLE());
    }
}
