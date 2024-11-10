package io.netty.handler.ssl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.jose4j.keys.EllipticCurves;

/* loaded from: classes4.dex */
final class GroupsConverter {
    private static final Map<String, String> mappings;

    static {
        Map<String, String> map = new HashMap<>();
        map.put("secp224r1", "P-224");
        map.put("prime256v1", EllipticCurves.P_256);
        map.put("secp256r1", EllipticCurves.P_256);
        map.put("secp384r1", EllipticCurves.P_384);
        map.put("secp521r1", EllipticCurves.P_521);
        map.put("x25519", "X25519");
        mappings = Collections.unmodifiableMap(map);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String toOpenSsl(String key) {
        String mapping = mappings.get(key);
        if (mapping == null) {
            return key;
        }
        return mapping;
    }

    private GroupsConverter() {
    }
}
