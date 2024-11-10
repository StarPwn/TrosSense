package org.cloudburstmc.protocol.common.util;

import org.cloudburstmc.protocol.common.Definition;
import org.cloudburstmc.protocol.common.DefinitionRegistry;

/* loaded from: classes5.dex */
public class DefinitionUtils {
    public static <D extends Definition> D checkDefinition(DefinitionRegistry<D> registry, D definition) {
        if (!registry.isRegistered(definition)) {
            throw new IllegalArgumentException("Definition is not registered: " + definition);
        }
        return definition;
    }
}
