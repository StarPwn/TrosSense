package org.cloudburstmc.protocol.bedrock.codec.v291.serializer;

import io.netty.buffer.ByteBuf;
import java.util.Objects;
import java.util.function.BiFunction;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.data.command.CommandOutputMessage;
import org.cloudburstmc.protocol.bedrock.data.command.CommandOutputType;
import org.cloudburstmc.protocol.bedrock.packet.CommandOutputPacket;
import org.cloudburstmc.protocol.common.util.TriConsumer;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class CommandOutputSerializer_v291 implements BedrockPacketSerializer<CommandOutputPacket> {
    public static final CommandOutputSerializer_v291 INSTANCE = new CommandOutputSerializer_v291();

    protected CommandOutputSerializer_v291() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, CommandOutputPacket packet) {
        helper.writeCommandOrigin(buffer, packet.getCommandOriginData());
        buffer.writeByte(packet.getType().ordinal());
        VarInts.writeUnsignedInt(buffer, packet.getSuccessCount());
        helper.writeArray(buffer, packet.getMessages(), new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.CommandOutputSerializer_v291$$ExternalSyntheticLambda0
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                CommandOutputSerializer_v291.this.writeMessage((ByteBuf) obj, (BedrockCodecHelper) obj2, (CommandOutputMessage) obj3);
            }
        });
        if (packet.getType() == CommandOutputType.DATA_SET) {
            helper.writeString(buffer, packet.getData());
        }
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, CommandOutputPacket packet) {
        packet.setCommandOriginData(helper.readCommandOrigin(buffer));
        packet.setType(CommandOutputType.values()[buffer.readUnsignedByte()]);
        packet.setSuccessCount(VarInts.readUnsignedInt(buffer));
        helper.readArray(buffer, packet.getMessages(), new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.CommandOutputSerializer_v291$$ExternalSyntheticLambda1
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return CommandOutputSerializer_v291.this.readMessage((ByteBuf) obj, (BedrockCodecHelper) obj2);
            }
        });
        if (packet.getType() == CommandOutputType.DATA_SET) {
            packet.setData(helper.readString(buffer));
        }
    }

    public CommandOutputMessage readMessage(ByteBuf buffer, BedrockCodecHelper helper) {
        boolean internal = buffer.readBoolean();
        String messageId = helper.readString(buffer);
        helper.getClass();
        String[] parameters = (String[]) helper.readArray(buffer, new String[0], new AvailableCommandsSerializer_v291$$ExternalSyntheticLambda3(helper));
        return new CommandOutputMessage(internal, messageId, parameters);
    }

    public void writeMessage(ByteBuf buffer, BedrockCodecHelper helper, CommandOutputMessage outputMessage) {
        Objects.requireNonNull(outputMessage, "CommandOutputMessage is null");
        buffer.writeBoolean(outputMessage.isInternal());
        helper.writeString(buffer, outputMessage.getMessageId());
        String[] parameters = outputMessage.getParameters();
        helper.getClass();
        helper.writeArray(buffer, parameters, new AvailableCommandsSerializer_v291$$ExternalSyntheticLambda14(helper));
    }
}
