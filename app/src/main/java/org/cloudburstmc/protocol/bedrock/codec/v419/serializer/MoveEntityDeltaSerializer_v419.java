package org.cloudburstmc.protocol.bedrock.codec.v419.serializer;

import io.netty.buffer.ByteBuf;
import java.util.EnumMap;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v388.serializer.MoveEntityDeltaSerializer_v388;
import org.cloudburstmc.protocol.bedrock.packet.MoveEntityDeltaPacket;
import org.cloudburstmc.protocol.common.util.TriConsumer;

/* loaded from: classes5.dex */
public class MoveEntityDeltaSerializer_v419 extends MoveEntityDeltaSerializer_v388 {
    protected static final TriConsumer<ByteBuf, BedrockCodecHelper, MoveEntityDeltaPacket> READER_X = new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v419.serializer.MoveEntityDeltaSerializer_v419$$ExternalSyntheticLambda0
        @Override // org.cloudburstmc.protocol.common.util.TriConsumer
        public final void accept(Object obj, Object obj2, Object obj3) {
            ((MoveEntityDeltaPacket) obj3).setX(((ByteBuf) obj).readFloatLE());
        }
    };
    protected static final TriConsumer<ByteBuf, BedrockCodecHelper, MoveEntityDeltaPacket> READER_Y = new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v419.serializer.MoveEntityDeltaSerializer_v419$$ExternalSyntheticLambda1
        @Override // org.cloudburstmc.protocol.common.util.TriConsumer
        public final void accept(Object obj, Object obj2, Object obj3) {
            ((MoveEntityDeltaPacket) obj3).setY(((ByteBuf) obj).readFloatLE());
        }
    };
    protected static final TriConsumer<ByteBuf, BedrockCodecHelper, MoveEntityDeltaPacket> READER_Z = new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v419.serializer.MoveEntityDeltaSerializer_v419$$ExternalSyntheticLambda2
        @Override // org.cloudburstmc.protocol.common.util.TriConsumer
        public final void accept(Object obj, Object obj2, Object obj3) {
            ((MoveEntityDeltaPacket) obj3).setZ(((ByteBuf) obj).readFloatLE());
        }
    };
    protected static final TriConsumer<ByteBuf, BedrockCodecHelper, MoveEntityDeltaPacket> WRITER_X = new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v419.serializer.MoveEntityDeltaSerializer_v419$$ExternalSyntheticLambda3
        @Override // org.cloudburstmc.protocol.common.util.TriConsumer
        public final void accept(Object obj, Object obj2, Object obj3) {
            ((ByteBuf) obj).writeFloatLE(((MoveEntityDeltaPacket) obj3).getX());
        }
    };
    protected static final TriConsumer<ByteBuf, BedrockCodecHelper, MoveEntityDeltaPacket> WRITER_Y = new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v419.serializer.MoveEntityDeltaSerializer_v419$$ExternalSyntheticLambda4
        @Override // org.cloudburstmc.protocol.common.util.TriConsumer
        public final void accept(Object obj, Object obj2, Object obj3) {
            ((ByteBuf) obj).writeFloatLE(((MoveEntityDeltaPacket) obj3).getY());
        }
    };
    protected static final TriConsumer<ByteBuf, BedrockCodecHelper, MoveEntityDeltaPacket> WRITER_Z = new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v419.serializer.MoveEntityDeltaSerializer_v419$$ExternalSyntheticLambda5
        @Override // org.cloudburstmc.protocol.common.util.TriConsumer
        public final void accept(Object obj, Object obj2, Object obj3) {
            ((ByteBuf) obj).writeFloatLE(((MoveEntityDeltaPacket) obj3).getZ());
        }
    };
    public static final MoveEntityDeltaSerializer_v419 INSTANCE = new MoveEntityDeltaSerializer_v419();

    protected MoveEntityDeltaSerializer_v419() {
        this.readers.put((EnumMap<MoveEntityDeltaPacket.Flag, TriConsumer<ByteBuf, BedrockCodecHelper, MoveEntityDeltaPacket>>) MoveEntityDeltaPacket.Flag.HAS_X, (MoveEntityDeltaPacket.Flag) READER_X);
        this.readers.put((EnumMap<MoveEntityDeltaPacket.Flag, TriConsumer<ByteBuf, BedrockCodecHelper, MoveEntityDeltaPacket>>) MoveEntityDeltaPacket.Flag.HAS_Y, (MoveEntityDeltaPacket.Flag) READER_Y);
        this.readers.put((EnumMap<MoveEntityDeltaPacket.Flag, TriConsumer<ByteBuf, BedrockCodecHelper, MoveEntityDeltaPacket>>) MoveEntityDeltaPacket.Flag.HAS_Z, (MoveEntityDeltaPacket.Flag) READER_Z);
        this.writers.put((EnumMap<MoveEntityDeltaPacket.Flag, TriConsumer<ByteBuf, BedrockCodecHelper, MoveEntityDeltaPacket>>) MoveEntityDeltaPacket.Flag.HAS_X, (MoveEntityDeltaPacket.Flag) WRITER_X);
        this.writers.put((EnumMap<MoveEntityDeltaPacket.Flag, TriConsumer<ByteBuf, BedrockCodecHelper, MoveEntityDeltaPacket>>) MoveEntityDeltaPacket.Flag.HAS_Y, (MoveEntityDeltaPacket.Flag) WRITER_Y);
        this.writers.put((EnumMap<MoveEntityDeltaPacket.Flag, TriConsumer<ByteBuf, BedrockCodecHelper, MoveEntityDeltaPacket>>) MoveEntityDeltaPacket.Flag.HAS_Z, (MoveEntityDeltaPacket.Flag) WRITER_Z);
    }
}
