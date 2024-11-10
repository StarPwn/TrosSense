package org.cloudburstmc.protocol.bedrock.codec.v332.serializer;

import io.netty.buffer.ByteBuf;
import java.util.List;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.AvailableCommandsSerializer_v291$$ExternalSyntheticLambda14;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.AvailableCommandsSerializer_v291$$ExternalSyntheticLambda3;
import org.cloudburstmc.protocol.bedrock.packet.TextPacket;

/* loaded from: classes5.dex */
public class TextSerializer_v332 implements BedrockPacketSerializer<TextPacket> {
    public static final TextSerializer_v332 INSTANCE = new TextSerializer_v332();

    /* JADX WARN: Failed to find 'out' block for switch in B:2:0x001a. Please report as an issue. */
    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, TextPacket packet) {
        TextPacket.Type type = packet.getType();
        buffer.writeByte(type.ordinal());
        buffer.writeBoolean(packet.isNeedsTranslation());
        switch (type) {
            case CHAT:
            case WHISPER:
            case ANNOUNCEMENT:
                helper.writeString(buffer, packet.getSourceName());
            case RAW:
            case TIP:
            case SYSTEM:
            case JSON:
            case WHISPER_JSON:
                helper.writeString(buffer, packet.getMessage());
                helper.writeString(buffer, packet.getXuid());
                helper.writeString(buffer, packet.getPlatformChatId());
                return;
            case TRANSLATION:
            case POPUP:
            case JUKEBOX_POPUP:
                helper.writeString(buffer, packet.getMessage());
                List<String> parameters = packet.getParameters();
                helper.getClass();
                helper.writeArray(buffer, parameters, new AvailableCommandsSerializer_v291$$ExternalSyntheticLambda14(helper));
                helper.writeString(buffer, packet.getXuid());
                helper.writeString(buffer, packet.getPlatformChatId());
                return;
            default:
                throw new UnsupportedOperationException("Unsupported TextType " + type);
        }
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:2:0x001c. Please report as an issue. */
    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, TextPacket packet) {
        TextPacket.Type type = TextPacket.Type.values()[buffer.readUnsignedByte()];
        packet.setType(type);
        packet.setNeedsTranslation(buffer.readBoolean());
        switch (type) {
            case CHAT:
            case WHISPER:
            case ANNOUNCEMENT:
                packet.setSourceName(helper.readString(buffer));
            case RAW:
            case TIP:
            case SYSTEM:
            case JSON:
            case WHISPER_JSON:
                packet.setMessage(helper.readString(buffer));
                packet.setXuid(helper.readString(buffer));
                packet.setPlatformChatId(helper.readString(buffer));
                return;
            case TRANSLATION:
            case POPUP:
            case JUKEBOX_POPUP:
                packet.setMessage(helper.readString(buffer));
                List<String> parameters = packet.getParameters();
                helper.getClass();
                helper.readArray(buffer, parameters, new AvailableCommandsSerializer_v291$$ExternalSyntheticLambda3(helper));
                packet.setXuid(helper.readString(buffer));
                packet.setPlatformChatId(helper.readString(buffer));
                return;
            default:
                throw new UnsupportedOperationException("Unsupported TextType " + type);
        }
    }
}
