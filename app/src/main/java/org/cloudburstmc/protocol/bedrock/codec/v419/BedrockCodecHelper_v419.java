package org.cloudburstmc.protocol.bedrock.codec.v419;

import io.netty.buffer.ByteBuf;
import java.util.List;
import org.cloudburstmc.protocol.bedrock.codec.EntityDataTypeMap;
import org.cloudburstmc.protocol.bedrock.codec.v407.BedrockCodecHelper_v407;
import org.cloudburstmc.protocol.bedrock.data.ExperimentData;
import org.cloudburstmc.protocol.bedrock.data.inventory.ContainerSlotType;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.ItemStackRequestActionType;
import org.cloudburstmc.protocol.bedrock.data.skin.AnimatedTextureType;
import org.cloudburstmc.protocol.bedrock.data.skin.AnimationData;
import org.cloudburstmc.protocol.bedrock.data.skin.AnimationExpressionType;
import org.cloudburstmc.protocol.bedrock.data.skin.ImageData;
import org.cloudburstmc.protocol.common.util.TypeMap;

/* loaded from: classes5.dex */
public class BedrockCodecHelper_v419 extends BedrockCodecHelper_v407 {
    protected static final AnimationExpressionType[] EXPRESSION_TYPES = AnimationExpressionType.values();

    public BedrockCodecHelper_v419(EntityDataTypeMap entityData, TypeMap<Class<?>> gameRulesTypes, TypeMap<ItemStackRequestActionType> stackRequestActionTypes, TypeMap<ContainerSlotType> containerSlotTypes) {
        super(entityData, gameRulesTypes, stackRequestActionTypes, containerSlotTypes);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BaseBedrockCodecHelper, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void readExperiments(ByteBuf buffer, List<ExperimentData> experiments) {
        int count = buffer.readIntLE();
        for (int i = 0; i < count; i++) {
            experiments.add(new ExperimentData(readString(buffer), buffer.readBoolean()));
        }
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BaseBedrockCodecHelper, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writeExperiments(ByteBuf buffer, List<ExperimentData> experiments) {
        buffer.writeIntLE(experiments.size());
        for (ExperimentData experiment : experiments) {
            writeString(buffer, experiment.getName());
            buffer.writeBoolean(experiment.isEnabled());
        }
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v388.BedrockCodecHelper_v388, org.cloudburstmc.protocol.bedrock.codec.BaseBedrockCodecHelper
    public AnimationData readAnimationData(ByteBuf buffer) {
        ImageData image = readImage(buffer);
        AnimatedTextureType textureType = TEXTURE_TYPES[buffer.readIntLE()];
        float frames = buffer.readFloatLE();
        AnimationExpressionType expressionType = EXPRESSION_TYPES[buffer.readIntLE()];
        return new AnimationData(image, textureType, frames, expressionType);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v388.BedrockCodecHelper_v388, org.cloudburstmc.protocol.bedrock.codec.BaseBedrockCodecHelper
    public void writeAnimationData(ByteBuf buffer, AnimationData animation) {
        super.writeAnimationData(buffer, animation);
        buffer.writeIntLE(animation.getExpressionType().ordinal());
    }
}
