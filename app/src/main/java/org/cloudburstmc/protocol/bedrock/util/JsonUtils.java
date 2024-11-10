package org.cloudburstmc.protocol.bedrock.util;

import java.util.Map;

/* loaded from: classes5.dex */
public class JsonUtils {
    public static <T> T childAsType(Map<?, ?> map, String str, Class<T> cls) {
        T t = (T) map.get(str);
        if (!cls.isInstance(t)) {
            throw new IllegalStateException(str + " node is missing");
        }
        return t;
    }
}
