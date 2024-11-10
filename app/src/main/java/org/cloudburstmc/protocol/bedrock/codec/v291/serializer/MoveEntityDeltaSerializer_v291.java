package org.cloudburstmc.protocol.bedrock.codec.v291.serializer;

import io.netty.buffer.ByteBuf;
import java.util.EnumMap;
import java.util.Set;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.MoveEntityDeltaPacket;
import org.cloudburstmc.protocol.common.util.TriConsumer;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class MoveEntityDeltaSerializer_v291 implements BedrockPacketSerializer<MoveEntityDeltaPacket> {
    protected final EnumMap<MoveEntityDeltaPacket.Flag, TriConsumer<ByteBuf, BedrockCodecHelper, MoveEntityDeltaPacket>> readers = new EnumMap<>(MoveEntityDeltaPacket.Flag.class);
    protected final EnumMap<MoveEntityDeltaPacket.Flag, TriConsumer<ByteBuf, BedrockCodecHelper, MoveEntityDeltaPacket>> writers = new EnumMap<>(MoveEntityDeltaPacket.Flag.class);
    protected static final TriConsumer<ByteBuf, BedrockCodecHelper, MoveEntityDeltaPacket> READER_DELTA_X = new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.MoveEntityDeltaSerializer_v291$$ExternalSyntheticLambda0
        @Override // org.cloudburstmc.protocol.common.util.TriConsumer
        public final void accept(Object obj, Object obj2, Object obj3) {
            ((MoveEntityDeltaPacket) obj3).setDeltaX(VarInts.readInt((ByteBuf) obj));
        }
    };
    protected static final TriConsumer<ByteBuf, BedrockCodecHelper, MoveEntityDeltaPacket> READER_DELTA_Y = new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.MoveEntityDeltaSerializer_v291$$ExternalSyntheticLambda5
        @Override // org.cloudburstmc.protocol.common.util.TriConsumer
        public final void accept(Object obj, Object obj2, Object obj3) {
            ((MoveEntityDeltaPacket) obj3).setDeltaY(VarInts.readInt((ByteBuf) obj));
        }
    };
    protected static final TriConsumer<ByteBuf, BedrockCodecHelper, MoveEntityDeltaPacket> READER_DELTA_Z = new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.MoveEntityDeltaSerializer_v291$$ExternalSyntheticLambda6
        @Override // org.cloudburstmc.protocol.common.util.TriConsumer
        public final void accept(Object obj, Object obj2, Object obj3) {
            ((MoveEntityDeltaPacket) obj3).setDeltaZ(VarInts.readInt((ByteBuf) obj));
        }
    };
    protected static final TriConsumer<ByteBuf, BedrockCodecHelper, MoveEntityDeltaPacket> READER_PITCH = new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.MoveEntityDeltaSerializer_v291$$ExternalSyntheticLambda7
        @Override // org.cloudburstmc.protocol.common.util.TriConsumer
        public final void accept(Object obj, Object obj2, Object obj3) {
            MoveEntityDeltaPacket moveEntityDeltaPacket = (MoveEntityDeltaPacket) obj3;
            moveEntityDeltaPacket.setPitch(((BedrockCodecHelper) obj2).readByteAngle((ByteBuf) obj));
        }
    };
    protected static final TriConsumer<ByteBuf, BedrockCodecHelper, MoveEntityDeltaPacket> READER_YAW = new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.MoveEntityDeltaSerializer_v291$$ExternalSyntheticLambda8
        @Override // org.cloudburstmc.protocol.common.util.TriConsumer
        public final void accept(Object obj, Object obj2, Object obj3) {
            MoveEntityDeltaPacket moveEntityDeltaPacket = (MoveEntityDeltaPacket) obj3;
            moveEntityDeltaPacket.setYaw(((BedrockCodecHelper) obj2).readByteAngle((ByteBuf) obj));
        }
    };
    protected static final TriConsumer<ByteBuf, BedrockCodecHelper, MoveEntityDeltaPacket> READER_HEAD_YAW = new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.MoveEntityDeltaSerializer_v291$$ExternalSyntheticLambda9
        @Override // org.cloudburstmc.protocol.common.util.TriConsumer
        public final void accept(Object obj, Object obj2, Object obj3) {
            MoveEntityDeltaPacket moveEntityDeltaPacket = (MoveEntityDeltaPacket) obj3;
            moveEntityDeltaPacket.setHeadYaw(((BedrockCodecHelper) obj2).readByteAngle((ByteBuf) obj));
        }
    };
    protected static final TriConsumer<ByteBuf, BedrockCodecHelper, MoveEntityDeltaPacket> WRITER_DELTA_X = new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.MoveEntityDeltaSerializer_v291$$ExternalSyntheticLambda10
        @Override // org.cloudburstmc.protocol.common.util.TriConsumer
        public final void accept(Object obj, Object obj2, Object obj3) {
            VarInts.writeInt((ByteBuf) obj, ((MoveEntityDeltaPacket) obj3).getDeltaX());
        }
    };
    protected static final TriConsumer<ByteBuf, BedrockCodecHelper, MoveEntityDeltaPacket> WRITER_DELTA_Y = new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.MoveEntityDeltaSerializer_v291$$ExternalSyntheticLambda11
        @Override // org.cloudburstmc.protocol.common.util.TriConsumer
        public final void accept(Object obj, Object obj2, Object obj3) {
            VarInts.writeInt((ByteBuf) obj, ((MoveEntityDeltaPacket) obj3).getDeltaY());
        }
    };
    protected static final TriConsumer<ByteBuf, BedrockCodecHelper, MoveEntityDeltaPacket> WRITER_DELTA_Z = new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.MoveEntityDeltaSerializer_v291$$ExternalSyntheticLambda1
        @Override // org.cloudburstmc.protocol.common.util.TriConsumer
        public final void accept(Object obj, Object obj2, Object obj3) {
            VarInts.writeInt((ByteBuf) obj, ((MoveEntityDeltaPacket) obj3).getDeltaZ());
        }
    };
    protected static final TriConsumer<ByteBuf, BedrockCodecHelper, MoveEntityDeltaPacket> WRITER_PITCH = new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.MoveEntityDeltaSerializer_v291$$ExternalSyntheticLambda2
        @Override // org.cloudburstmc.protocol.common.util.TriConsumer
        public final void accept(Object obj, Object obj2, Object obj3) {
            ((BedrockCodecHelper) obj2).writeByteAngle((ByteBuf) obj, ((MoveEntityDeltaPacket) obj3).getPitch());
        }
    };
    protected static final TriConsumer<ByteBuf, BedrockCodecHelper, MoveEntityDeltaPacket> WRITER_YAW = new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.MoveEntityDeltaSerializer_v291$$ExternalSyntheticLambda3
        @Override // org.cloudburstmc.protocol.common.util.TriConsumer
        public final void accept(Object obj, Object obj2, Object obj3) {
            ((BedrockCodecHelper) obj2).writeByteAngle((ByteBuf) obj, ((MoveEntityDeltaPacket) obj3).getYaw());
        }
    };
    protected static final TriConsumer<ByteBuf, BedrockCodecHelper, MoveEntityDeltaPacket> WRITER_HEAD_YAW = new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.MoveEntityDeltaSerializer_v291$$ExternalSyntheticLambda4
        @Override // org.cloudburstmc.protocol.common.util.TriConsumer
        public final void accept(Object obj, Object obj2, Object obj3) {
            ((BedrockCodecHelper) obj2).writeByteAngle((ByteBuf) obj, ((MoveEntityDeltaPacket) obj3).getHeadYaw());
        }
    };
    protected static final MoveEntityDeltaPacket.Flag[] FLAGS = MoveEntityDeltaPacket.Flag.values();
    public static final MoveEntityDeltaSerializer_v291 INSTANCE = new MoveEntityDeltaSerializer_v291();

    /* JADX INFO: Access modifiers changed from: protected */
    public MoveEntityDeltaSerializer_v291() {
        this.readers.put((EnumMap<MoveEntityDeltaPacket.Flag, TriConsumer<ByteBuf, BedrockCodecHelper, MoveEntityDeltaPacket>>) MoveEntityDeltaPacket.Flag.HAS_X, (MoveEntityDeltaPacket.Flag) READER_DELTA_X);
        this.readers.put((EnumMap<MoveEntityDeltaPacket.Flag, TriConsumer<ByteBuf, BedrockCodecHelper, MoveEntityDeltaPacket>>) MoveEntityDeltaPacket.Flag.HAS_Y, (MoveEntityDeltaPacket.Flag) READER_DELTA_Y);
        this.readers.put((EnumMap<MoveEntityDeltaPacket.Flag, TriConsumer<ByteBuf, BedrockCodecHelper, MoveEntityDeltaPacket>>) MoveEntityDeltaPacket.Flag.HAS_Z, (MoveEntityDeltaPacket.Flag) READER_DELTA_Z);
        this.readers.put((EnumMap<MoveEntityDeltaPacket.Flag, TriConsumer<ByteBuf, BedrockCodecHelper, MoveEntityDeltaPacket>>) MoveEntityDeltaPacket.Flag.HAS_PITCH, (MoveEntityDeltaPacket.Flag) READER_PITCH);
        this.readers.put((EnumMap<MoveEntityDeltaPacket.Flag, TriConsumer<ByteBuf, BedrockCodecHelper, MoveEntityDeltaPacket>>) MoveEntityDeltaPacket.Flag.HAS_YAW, (MoveEntityDeltaPacket.Flag) READER_YAW);
        this.readers.put((EnumMap<MoveEntityDeltaPacket.Flag, TriConsumer<ByteBuf, BedrockCodecHelper, MoveEntityDeltaPacket>>) MoveEntityDeltaPacket.Flag.HAS_HEAD_YAW, (MoveEntityDeltaPacket.Flag) READER_HEAD_YAW);
        this.writers.put((EnumMap<MoveEntityDeltaPacket.Flag, TriConsumer<ByteBuf, BedrockCodecHelper, MoveEntityDeltaPacket>>) MoveEntityDeltaPacket.Flag.HAS_X, (MoveEntityDeltaPacket.Flag) WRITER_DELTA_X);
        this.writers.put((EnumMap<MoveEntityDeltaPacket.Flag, TriConsumer<ByteBuf, BedrockCodecHelper, MoveEntityDeltaPacket>>) MoveEntityDeltaPacket.Flag.HAS_Y, (MoveEntityDeltaPacket.Flag) WRITER_DELTA_Y);
        this.writers.put((EnumMap<MoveEntityDeltaPacket.Flag, TriConsumer<ByteBuf, BedrockCodecHelper, MoveEntityDeltaPacket>>) MoveEntityDeltaPacket.Flag.HAS_Z, (MoveEntityDeltaPacket.Flag) WRITER_DELTA_Z);
        this.writers.put((EnumMap<MoveEntityDeltaPacket.Flag, TriConsumer<ByteBuf, BedrockCodecHelper, MoveEntityDeltaPacket>>) MoveEntityDeltaPacket.Flag.HAS_PITCH, (MoveEntityDeltaPacket.Flag) WRITER_PITCH);
        this.writers.put((EnumMap<MoveEntityDeltaPacket.Flag, TriConsumer<ByteBuf, BedrockCodecHelper, MoveEntityDeltaPacket>>) MoveEntityDeltaPacket.Flag.HAS_YAW, (MoveEntityDeltaPacket.Flag) WRITER_YAW);
        this.writers.put((EnumMap<MoveEntityDeltaPacket.Flag, TriConsumer<ByteBuf, BedrockCodecHelper, MoveEntityDeltaPacket>>) MoveEntityDeltaPacket.Flag.HAS_HEAD_YAW, (MoveEntityDeltaPacket.Flag) WRITER_HEAD_YAW);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, MoveEntityDeltaPacket packet) {
        VarInts.writeUnsignedLong(buffer, packet.getRuntimeEntityId());
        int flagsIndex = buffer.writerIndex();
        buffer.writeByte(0);
        int flags = 0;
        for (MoveEntityDeltaPacket.Flag flag : packet.getFlags()) {
            flags |= 1 << flag.ordinal();
            TriConsumer<ByteBuf, BedrockCodecHelper, MoveEntityDeltaPacket> writer = this.writers.get(flag);
            if (writer != null) {
                writer.accept(buffer, helper, packet);
            }
        }
        int currentIndex = buffer.writerIndex();
        buffer.writerIndex(flagsIndex);
        buffer.writeByte(flags);
        buffer.writerIndex(currentIndex);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, MoveEntityDeltaPacket packet) {
        packet.setRuntimeEntityId(VarInts.readUnsignedLong(buffer));
        int flags = buffer.readUnsignedByte();
        Set<MoveEntityDeltaPacket.Flag> flagSet = packet.getFlags();
        for (MoveEntityDeltaPacket.Flag flag : FLAGS) {
            if (((1 << flag.ordinal()) & flags) != 0) {
                flagSet.add(flag);
                TriConsumer<ByteBuf, BedrockCodecHelper, MoveEntityDeltaPacket> reader = this.readers.get(flag);
                if (reader != null) {
                    reader.accept(buffer, helper, packet);
                }
            }
        }
    }
}
