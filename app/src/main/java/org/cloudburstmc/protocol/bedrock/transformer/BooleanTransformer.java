package org.cloudburstmc.protocol.bedrock.transformer;

import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataMap;

/* loaded from: classes5.dex */
public final class BooleanTransformer implements EntityDataTransformer<Byte, Boolean> {
    public static final BooleanTransformer INSTANCE = new BooleanTransformer();

    private BooleanTransformer() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.transformer.EntityDataTransformer
    public Byte serialize(BedrockCodecHelper helper, EntityDataMap map, Boolean value) {
        return Byte.valueOf((byte) (value == Boolean.TRUE ? 1 : 0));
    }

    @Override // org.cloudburstmc.protocol.bedrock.transformer.EntityDataTransformer
    public Boolean deserialize(BedrockCodecHelper helper, EntityDataMap map, Byte value) {
        return value.byteValue() == 1 ? Boolean.TRUE : Boolean.FALSE;
    }
}
