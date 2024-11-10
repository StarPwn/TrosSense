package org.jose4j.jwx;

import org.jose4j.lang.JoseException;

/* loaded from: classes5.dex */
public class CompactSerialization {
    public static String[] deserialize(String cs) {
        return CompactSerializer.deserialize(cs);
    }

    public static String serialize(String... parts) throws JoseException {
        return CompactSerializer.serialize(parts);
    }
}
