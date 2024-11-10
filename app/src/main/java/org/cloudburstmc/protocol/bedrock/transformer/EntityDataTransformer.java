package org.cloudburstmc.protocol.bedrock.transformer;

import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataMap;

/* loaded from: classes5.dex */
public interface EntityDataTransformer<S, D> {
    public static final EntityDataTransformer<?, ?> IDENTITY = new EntityDataTransformer<Object, Object>() { // from class: org.cloudburstmc.protocol.bedrock.transformer.EntityDataTransformer.1
        @Override // org.cloudburstmc.protocol.bedrock.transformer.EntityDataTransformer
        public Object serialize(BedrockCodecHelper helper, EntityDataMap map, Object value) {
            return value;
        }

        @Override // org.cloudburstmc.protocol.bedrock.transformer.EntityDataTransformer
        public Object deserialize(BedrockCodecHelper helper, EntityDataMap map, Object value) {
            return value;
        }
    };

    D deserialize(BedrockCodecHelper bedrockCodecHelper, EntityDataMap entityDataMap, S s);

    S serialize(BedrockCodecHelper bedrockCodecHelper, EntityDataMap entityDataMap, D d);

    static <S, D> EntityDataTransformer<S, D> identity() {
        return (EntityDataTransformer<S, D>) IDENTITY;
    }
}
