package io.netty.handler.ssl;

import io.netty.util.internal.ObjectUtil;
import java.util.ArrayList;
import java.util.List;
import org.jose4j.jwk.RsaJsonWebKey;

/* loaded from: classes4.dex */
final class ApplicationProtocolUtil {
    private static final int DEFAULT_LIST_SIZE = 2;

    private ApplicationProtocolUtil() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static List<String> toList(Iterable<String> protocols) {
        return toList(2, protocols);
    }

    static List<String> toList(int initialListSize, Iterable<String> protocols) {
        if (protocols == null) {
            return null;
        }
        List<String> result = new ArrayList<>(initialListSize);
        for (String p : protocols) {
            result.add(ObjectUtil.checkNonEmpty(p, RsaJsonWebKey.FIRST_PRIME_FACTOR_MEMBER_NAME));
        }
        return (List) ObjectUtil.checkNonEmpty(result, "result");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static List<String> toList(String... protocols) {
        return toList(2, protocols);
    }

    static List<String> toList(int initialListSize, String... protocols) {
        if (protocols == null) {
            return null;
        }
        List<String> result = new ArrayList<>(initialListSize);
        for (String p : protocols) {
            result.add(ObjectUtil.checkNonEmpty(p, RsaJsonWebKey.FIRST_PRIME_FACTOR_MEMBER_NAME));
        }
        return (List) ObjectUtil.checkNonEmpty(result, "result");
    }
}
