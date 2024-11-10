package org.cloudburstmc.protocol.bedrock.transformer;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.EnumSet;
import java.util.Iterator;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataMap;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityFlag;
import org.cloudburstmc.protocol.common.util.TypeMap;

/* loaded from: classes5.dex */
public final class FlagTransformer implements EntityDataTransformer<Long, EnumSet<EntityFlag>> {
    private static final InternalLogger log = InternalLoggerFactory.getInstance((Class<?>) FlagTransformer.class);
    private final int index;
    private final TypeMap<EntityFlag> typeMap;

    public FlagTransformer(TypeMap<EntityFlag> typeMap, int index) {
        this.typeMap = typeMap;
        this.index = index;
    }

    @Override // org.cloudburstmc.protocol.bedrock.transformer.EntityDataTransformer
    public Long serialize(BedrockCodecHelper helper, EntityDataMap map, EnumSet<EntityFlag> flags) {
        long value = 0;
        int lower = this.index * 64;
        int upper = lower + 64;
        Iterator it2 = flags.iterator();
        while (it2.hasNext()) {
            EntityFlag flag = (EntityFlag) it2.next();
            int flagIndex = this.typeMap.getId(flag);
            if (flagIndex >= lower && flagIndex < upper) {
                value |= 1 << (flagIndex & 63);
            }
        }
        return Long.valueOf(value);
    }

    @Override // org.cloudburstmc.protocol.bedrock.transformer.EntityDataTransformer
    public EnumSet<EntityFlag> deserialize(BedrockCodecHelper helper, EntityDataMap map, Long value) {
        EnumSet<EntityFlag> flags = map.getOrCreateFlags();
        int lower = this.index * 64;
        int upper = lower + 64;
        for (int i = lower; i < upper; i++) {
            int idx = i & 63;
            if ((value.longValue() & (1 << idx)) != 0) {
                EntityFlag flag = this.typeMap.getType(i);
                if (flag != null) {
                    flags.add(flag);
                } else {
                    log.debug("Unknown entity flag detected with index {}", Integer.valueOf(i));
                }
            }
        }
        return flags;
    }
}
