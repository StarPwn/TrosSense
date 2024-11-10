package org.cloudburstmc.protocol.bedrock.codec.v503.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.v475.serializer.StartGameSerializer_v475;

/* loaded from: classes5.dex */
public class StartGameSerializer_v503 extends StartGameSerializer_v475 {
    @Override // org.cloudburstmc.protocol.bedrock.codec.v419.serializer.StartGameSerializer_v419
    protected long readSeed(ByteBuf buf) {
        return buf.readLongLE();
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v419.serializer.StartGameSerializer_v419
    protected void writeSeed(ByteBuf buf, long seed) {
        buf.writeLongLE(seed);
    }
}
