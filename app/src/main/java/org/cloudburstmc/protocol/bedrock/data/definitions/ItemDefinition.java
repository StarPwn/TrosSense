package org.cloudburstmc.protocol.bedrock.data.definitions;

import org.cloudburstmc.protocol.common.NamedDefinition;

/* loaded from: classes5.dex */
public interface ItemDefinition extends NamedDefinition {
    public static final ItemDefinition AIR = new SimpleItemDefinition("minecraft:air", 0, false);
    public static final ItemDefinition LEGACY_FIREWORK = new SimpleItemDefinition("minecraft:fireworks_rocket", 401, false);

    boolean isComponentBased();
}
