package org.cloudburstmc.protocol.common;

import org.cloudburstmc.protocol.common.Definition;

/* loaded from: classes5.dex */
public interface DefinitionRegistry<D extends Definition> {
    D getDefinition(int i);

    boolean isRegistered(D d);
}
