package org.cloudburstmc.protocol.bedrock.codec.v575;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.EntityDataTypeMap;
import org.cloudburstmc.protocol.bedrock.codec.v568.BedrockCodecHelper_v568;
import org.cloudburstmc.protocol.bedrock.data.Ability;
import org.cloudburstmc.protocol.bedrock.data.inventory.ContainerSlotType;
import org.cloudburstmc.protocol.bedrock.data.inventory.descriptor.ComplexAliasDescriptor;
import org.cloudburstmc.protocol.bedrock.data.inventory.descriptor.ItemDescriptor;
import org.cloudburstmc.protocol.bedrock.data.inventory.descriptor.ItemDescriptorType;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.TextProcessingEventOrigin;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.ItemStackRequestActionType;
import org.cloudburstmc.protocol.common.DefinitionRegistry;
import org.cloudburstmc.protocol.common.NamedDefinition;
import org.cloudburstmc.protocol.common.util.TypeMap;

/* loaded from: classes5.dex */
public class BedrockCodecHelper_v575 extends BedrockCodecHelper_v568 {
    protected DefinitionRegistry<NamedDefinition> cameraPresetDefinitions;

    @Override // org.cloudburstmc.protocol.bedrock.codec.BaseBedrockCodecHelper, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public DefinitionRegistry<NamedDefinition> getCameraPresetDefinitions() {
        return this.cameraPresetDefinitions;
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BaseBedrockCodecHelper, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void setCameraPresetDefinitions(DefinitionRegistry<NamedDefinition> cameraPresetDefinitions) {
        this.cameraPresetDefinitions = cameraPresetDefinitions;
    }

    public BedrockCodecHelper_v575(EntityDataTypeMap entityData, TypeMap<Class<?>> gameRulesTypes, TypeMap<ItemStackRequestActionType> stackRequestActionTypes, TypeMap<ContainerSlotType> containerSlotTypes, TypeMap<Ability> abilities, TypeMap<TextProcessingEventOrigin> textProcessingEventOrigins) {
        super(entityData, gameRulesTypes, stackRequestActionTypes, containerSlotTypes, abilities, textProcessingEventOrigins);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v554.BedrockCodecHelper_v554
    public ItemDescriptor readItemDescriptor(ByteBuf buffer, ItemDescriptorType type) {
        if (type == ItemDescriptorType.COMPLEX_ALIAS) {
            String name = readString(buffer);
            return new ComplexAliasDescriptor(name);
        }
        return super.readItemDescriptor(buffer, type);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v554.BedrockCodecHelper_v554
    public void writeItemDescriptor(ByteBuf buffer, ItemDescriptor descriptor) {
        if (descriptor.getType() == ItemDescriptorType.COMPLEX_ALIAS) {
            writeString(buffer, ((ComplexAliasDescriptor) descriptor).getName());
        } else {
            super.writeItemDescriptor(buffer, descriptor);
        }
    }
}
