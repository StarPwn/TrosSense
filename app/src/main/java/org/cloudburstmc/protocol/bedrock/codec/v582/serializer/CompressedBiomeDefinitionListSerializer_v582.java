package org.cloudburstmc.protocol.bedrock.codec.v582.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.CompressedBiomeDefinitionListPacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class CompressedBiomeDefinitionListSerializer_v582 implements BedrockPacketSerializer<CompressedBiomeDefinitionListPacket> {
    public static final CompressedBiomeDefinitionListSerializer_v582 INSTANCE = new CompressedBiomeDefinitionListSerializer_v582();
    protected static final byte[] COMPRESSED_INDICATOR = {67, 79, 77, 80, 82, 69, 83, 83, 69, 68};

    protected CompressedBiomeDefinitionListSerializer_v582() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, CompressedBiomeDefinitionListPacket packet) {
        ByteBuf compressed = buffer.alloc().ioBuffer();
        writeCompressed(packet.getDefinitions(), compressed, helper);
        VarInts.writeUnsignedInt(buffer, compressed.readableBytes());
        buffer.writeBytes(compressed);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, CompressedBiomeDefinitionListPacket packet) {
        int length = VarInts.readUnsignedInt(buffer);
        packet.setDefinitions(readCompressed(buffer.readBytes(length), helper, COMPRESSED_INDICATOR.length));
    }

    protected NbtMap readCompressed(ByteBuf buffer, BedrockCodecHelper helper, int length) {
        buffer.skipBytes(length);
        ByteBuf[] dictionary = new ByteBuf[buffer.readUnsignedShortLE()];
        ByteBuf decompressed = buffer.alloc().ioBuffer();
        int index = 0;
        while (true) {
            int i = 0;
            try {
                if (index >= dictionary.length) {
                    break;
                }
                dictionary[index] = buffer.readBytes(buffer.readUnsignedByte());
                index++;
            } finally {
                decompressed.release();
                int length2 = dictionary.length;
                while (i < length2) {
                    ByteBuf buf = dictionary[i];
                    if (buf != null) {
                        buf.release();
                    }
                    i++;
                }
            }
        }
        while (buffer.isReadable()) {
            int key = buffer.readUnsignedByte();
            if (key != 255) {
                decompressed.writeByte(key);
            } else {
                int index2 = buffer.readUnsignedShortLE();
                if (index2 < 0 || index2 >= dictionary.length) {
                    decompressed.writeByte(key);
                } else {
                    decompressed.writeBytes(dictionary[index2].slice());
                }
            }
        }
        return (NbtMap) helper.readTag(decompressed, NbtMap.class);
    }

    private void writeCompressed(NbtMap nbtMap, ByteBuf buffer, BedrockCodecHelper helper) {
        buffer.writeBytes(COMPRESSED_INDICATOR);
        buffer.writeShortLE(0);
        ByteBuf serialized = buffer.alloc().ioBuffer();
        helper.writeTag(serialized, nbtMap);
        while (serialized.isReadable()) {
            int key = serialized.readUnsignedByte();
            buffer.writeByte(key);
            if (key == 255) {
                buffer.writeShortLE(1);
            }
        }
    }
}
