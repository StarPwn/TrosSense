package org.cloudburstmc.protocol.bedrock.transformer;

import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataMap;
import org.cloudburstmc.protocol.common.util.TypeMap;

/* loaded from: classes5.dex */
public final class TypeMapTransformer<T> implements EntityDataTransformer<Integer, T> {
    private final TypeMap<T> typeMap;

    public TypeMapTransformer(TypeMap<T> typeMap) {
        this.typeMap = typeMap;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.cloudburstmc.protocol.bedrock.transformer.EntityDataTransformer
    public /* bridge */ /* synthetic */ Integer serialize(BedrockCodecHelper bedrockCodecHelper, EntityDataMap entityDataMap, Object obj) {
        return serialize(bedrockCodecHelper, entityDataMap, (EntityDataMap) obj);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.cloudburstmc.protocol.bedrock.transformer.EntityDataTransformer
    public Integer serialize(BedrockCodecHelper helper, EntityDataMap map, T value) {
        return Integer.valueOf(this.typeMap.getId(value));
    }

    @Override // org.cloudburstmc.protocol.bedrock.transformer.EntityDataTransformer
    public T deserialize(BedrockCodecHelper helper, EntityDataMap map, Integer value) {
        return this.typeMap.getType(value.intValue());
    }
}
