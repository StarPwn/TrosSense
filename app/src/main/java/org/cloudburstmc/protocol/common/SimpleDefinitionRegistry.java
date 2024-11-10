package org.cloudburstmc.protocol.common;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.cloudburstmc.protocol.common.Definition;
import org.cloudburstmc.protocol.common.util.Preconditions;

/* loaded from: classes5.dex */
public class SimpleDefinitionRegistry<D extends Definition> implements DefinitionRegistry<D> {
    private final Int2ObjectMap<D> runtimeMap;

    private SimpleDefinitionRegistry(Int2ObjectMap<D> runtimeMap, Map<String, D> identifierMap) {
        this.runtimeMap = runtimeMap;
    }

    public static <D extends Definition> Builder<D> builder() {
        return new Builder<>();
    }

    @Override // org.cloudburstmc.protocol.common.DefinitionRegistry
    public D getDefinition(int runtimeId) {
        return this.runtimeMap.get(runtimeId);
    }

    @Override // org.cloudburstmc.protocol.common.DefinitionRegistry
    public boolean isRegistered(D definition) {
        return this.runtimeMap.get(definition.getRuntimeId()) == definition;
    }

    public Builder<D> toBuilder() {
        return new Builder().addAll(this.runtimeMap.values());
    }

    /* loaded from: classes5.dex */
    public static class Builder<D extends Definition> {
        private final Int2ObjectMap<D> runtimeMap = new Int2ObjectOpenHashMap();
        private final Map<String, D> identifierMap = new HashMap();

        public Builder<D> addAll(Collection<? extends D> definitions) {
            for (D definition : definitions) {
                add(definition);
            }
            return this;
        }

        public Builder<D> add(D definition) {
            Preconditions.checkNotNull(definition, "definition");
            Preconditions.checkArgument(!this.runtimeMap.containsKey(definition.getRuntimeId()), "Runtime ID is already registered: " + definition.getRuntimeId());
            this.runtimeMap.put(definition.getRuntimeId(), (int) definition);
            return this;
        }

        public Builder<D> remove(D definition) {
            Preconditions.checkNotNull(definition, "definition");
            Preconditions.checkArgument(this.runtimeMap.containsKey(definition.getRuntimeId()), "Runtime ID is not registered: " + definition.getRuntimeId());
            this.runtimeMap.remove(definition.getRuntimeId());
            return this;
        }

        public SimpleDefinitionRegistry<D> build() {
            return new SimpleDefinitionRegistry<>(this.runtimeMap, this.identifierMap);
        }
    }
}
