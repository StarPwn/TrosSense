package org.cloudburstmc.protocol.bedrock.transformer;

import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.data.definitions.BlockDefinition;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataMap;
import org.cloudburstmc.protocol.common.util.DefinitionUtils;

/* loaded from: classes5.dex */
public class BlockDefinitionTransformer implements EntityDataTransformer<Integer, BlockDefinition> {
    @Override // org.cloudburstmc.protocol.bedrock.transformer.EntityDataTransformer
    public Integer serialize(BedrockCodecHelper helper, EntityDataMap map, BlockDefinition value) {
        if (helper.getBlockDefinitions() == null) {
            return Integer.valueOf(value.getRuntimeId());
        }
        return Integer.valueOf(((BlockDefinition) DefinitionUtils.checkDefinition(helper.getBlockDefinitions(), value)).getRuntimeId());
    }

    @Override // org.cloudburstmc.protocol.bedrock.transformer.EntityDataTransformer
    public BlockDefinition deserialize(BedrockCodecHelper helper, EntityDataMap map, Integer value) {
        if (helper.getBlockDefinitions() == null) {
            return null;
        }
        return helper.getBlockDefinitions().getDefinition(value.intValue());
    }
}
