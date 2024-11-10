package org.cloudburstmc.protocol.bedrock.codec.v291.serializer;

import io.netty.buffer.ByteBuf;
import java.util.function.BiFunction;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.data.AttributeData;
import org.cloudburstmc.protocol.bedrock.packet.UpdateAttributesPacket;
import org.cloudburstmc.protocol.common.util.Preconditions;
import org.cloudburstmc.protocol.common.util.TriConsumer;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class UpdateAttributesSerializer_v291 implements BedrockPacketSerializer<UpdateAttributesPacket> {
    public static final UpdateAttributesSerializer_v291 INSTANCE = new UpdateAttributesSerializer_v291();

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, UpdateAttributesPacket packet) {
        VarInts.writeUnsignedLong(buffer, packet.getRuntimeEntityId());
        helper.writeArray(buffer, packet.getAttributes(), new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.UpdateAttributesSerializer_v291$$ExternalSyntheticLambda1
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                UpdateAttributesSerializer_v291.this.writeAttribute((ByteBuf) obj, (BedrockCodecHelper) obj2, (AttributeData) obj3);
            }
        });
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, UpdateAttributesPacket packet) {
        packet.setRuntimeEntityId(VarInts.readUnsignedLong(buffer));
        helper.readArray(buffer, packet.getAttributes(), new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.UpdateAttributesSerializer_v291$$ExternalSyntheticLambda0
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return UpdateAttributesSerializer_v291.this.readAttribute((ByteBuf) obj, (BedrockCodecHelper) obj2);
            }
        });
    }

    public AttributeData readAttribute(ByteBuf buffer, BedrockCodecHelper helper) {
        float min = buffer.readFloatLE();
        float max = buffer.readFloatLE();
        float val = buffer.readFloatLE();
        float def = buffer.readFloatLE();
        String name = helper.readString(buffer);
        return new AttributeData(name, min, max, val, def);
    }

    public void writeAttribute(ByteBuf buffer, BedrockCodecHelper helper, AttributeData attribute) {
        Preconditions.checkNotNull(attribute, "attribute");
        buffer.writeFloatLE(attribute.getMinimum());
        buffer.writeFloatLE(attribute.getMaximum());
        buffer.writeFloatLE(attribute.getValue());
        buffer.writeFloatLE(attribute.getDefaultValue());
        helper.writeString(buffer, attribute.getName());
    }
}
