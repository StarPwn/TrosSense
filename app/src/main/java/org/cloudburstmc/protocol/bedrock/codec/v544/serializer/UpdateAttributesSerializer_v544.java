package org.cloudburstmc.protocol.bedrock.codec.v544.serializer;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.function.BiFunction;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v419.serializer.UpdateAttributesSerializer_v419;
import org.cloudburstmc.protocol.bedrock.data.AttributeData;
import org.cloudburstmc.protocol.bedrock.data.attribute.AttributeModifierData;
import org.cloudburstmc.protocol.bedrock.data.attribute.AttributeOperation;
import org.cloudburstmc.protocol.common.util.TriConsumer;

/* loaded from: classes5.dex */
public class UpdateAttributesSerializer_v544 extends UpdateAttributesSerializer_v419 {
    protected static final AttributeOperation[] VALUES = AttributeOperation.values();

    @Override // org.cloudburstmc.protocol.bedrock.codec.v291.serializer.UpdateAttributesSerializer_v291
    public void writeAttribute(ByteBuf buffer, BedrockCodecHelper helper, AttributeData attribute) {
        super.writeAttribute(buffer, helper, attribute);
        helper.writeArray(buffer, attribute.getModifiers(), new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v544.serializer.UpdateAttributesSerializer_v544$$ExternalSyntheticLambda0
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                UpdateAttributesSerializer_v544.this.writeModifier((ByteBuf) obj, (BedrockCodecHelper) obj2, (AttributeModifierData) obj3);
            }
        });
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v291.serializer.UpdateAttributesSerializer_v291
    public AttributeData readAttribute(ByteBuf buffer, BedrockCodecHelper helper) {
        float min = buffer.readFloatLE();
        float max = buffer.readFloatLE();
        float val = buffer.readFloatLE();
        float def = buffer.readFloatLE();
        String name = helper.readString(buffer);
        ObjectArrayList objectArrayList = new ObjectArrayList();
        helper.readArray(buffer, objectArrayList, new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v544.serializer.UpdateAttributesSerializer_v544$$ExternalSyntheticLambda1
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return UpdateAttributesSerializer_v544.this.readModifier((ByteBuf) obj, (BedrockCodecHelper) obj2);
            }
        });
        return new AttributeData(name, min, max, val, def, objectArrayList);
    }

    public void writeModifier(ByteBuf buffer, BedrockCodecHelper helper, AttributeModifierData modifier) {
        helper.writeString(buffer, modifier.getId());
        helper.writeString(buffer, modifier.getName());
        buffer.writeFloatLE(modifier.getAmount());
        buffer.writeIntLE(modifier.getOperation().ordinal());
        buffer.writeIntLE(modifier.getOperand());
        buffer.writeBoolean(modifier.isSerializable());
    }

    public AttributeModifierData readModifier(ByteBuf buffer, BedrockCodecHelper helper) {
        String id = helper.readString(buffer);
        String name = helper.readString(buffer);
        float amount = buffer.readFloatLE();
        AttributeOperation operation = VALUES[buffer.readIntLE()];
        int operand = buffer.readIntLE();
        boolean serializable = buffer.readBoolean();
        return new AttributeModifierData(id, name, amount, operation, operand, serializable);
    }
}
