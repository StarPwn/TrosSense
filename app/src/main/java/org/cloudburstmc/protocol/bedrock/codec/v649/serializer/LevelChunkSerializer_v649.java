package org.cloudburstmc.protocol.bedrock.codec.v649.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.v486.serializer.LevelChunkSerializer_v486;
import org.cloudburstmc.protocol.bedrock.packet.LevelChunkPacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class LevelChunkSerializer_v649 extends LevelChunkSerializer_v486 {
    public static final LevelChunkSerializer_v649 INSTANCE = new LevelChunkSerializer_v649();

    protected LevelChunkSerializer_v649() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v486.serializer.LevelChunkSerializer_v486
    public void writeChunkLocation(ByteBuf buffer, LevelChunkPacket packet) {
        super.writeChunkLocation(buffer, packet);
        VarInts.writeInt(buffer, packet.getDimension());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v486.serializer.LevelChunkSerializer_v486
    public void readChunkLocation(ByteBuf buffer, LevelChunkPacket packet) {
        super.readChunkLocation(buffer, packet);
        packet.setDimension(VarInts.readInt(buffer));
    }
}
