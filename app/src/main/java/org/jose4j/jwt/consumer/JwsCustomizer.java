package org.jose4j.jwt.consumer;

import java.util.List;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwx.JsonWebStructure;

/* loaded from: classes5.dex */
public interface JwsCustomizer {
    void customize(JsonWebSignature jsonWebSignature, List<JsonWebStructure> list);
}
