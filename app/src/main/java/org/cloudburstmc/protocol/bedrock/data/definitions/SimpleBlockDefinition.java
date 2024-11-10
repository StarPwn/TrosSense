package org.cloudburstmc.protocol.bedrock.data.definitions;

import java.util.TreeMap;
import java.util.function.BiConsumer;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.protocol.common.NamedDefinition;

/* loaded from: classes5.dex */
public class SimpleBlockDefinition implements BlockDefinition, NamedDefinition {
    private final String identifier;
    private transient String persistentIdentifier;
    private final int runtimeId;
    private final NbtMap state;

    public SimpleBlockDefinition(String identifier, int runtimeId, NbtMap state) {
        this.identifier = identifier;
        this.runtimeId = runtimeId;
        this.state = state;
    }

    protected boolean canEqual(Object other) {
        return other instanceof SimpleBlockDefinition;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SimpleBlockDefinition)) {
            return false;
        }
        SimpleBlockDefinition other = (SimpleBlockDefinition) o;
        if (!other.canEqual(this) || getRuntimeId() != other.getRuntimeId()) {
            return false;
        }
        Object this$identifier = getIdentifier();
        Object other$identifier = other.getIdentifier();
        if (this$identifier != null ? !this$identifier.equals(other$identifier) : other$identifier != null) {
            return false;
        }
        Object this$state = getState();
        Object other$state = other.getState();
        return this$state != null ? this$state.equals(other$state) : other$state == null;
    }

    public int hashCode() {
        int result = (1 * 59) + getRuntimeId();
        Object $identifier = getIdentifier();
        int result2 = (result * 59) + ($identifier == null ? 43 : $identifier.hashCode());
        Object $state = getState();
        return (result2 * 59) + ($state != null ? $state.hashCode() : 43);
    }

    public void setPersistentIdentifier(String persistentIdentifier) {
        this.persistentIdentifier = persistentIdentifier;
    }

    public String toString() {
        return "SimpleBlockDefinition(identifier=" + getIdentifier() + ", runtimeId=" + getRuntimeId() + ", state=" + getState() + ", persistentIdentifier=" + getPersistentIdentifier() + ")";
    }

    @Override // org.cloudburstmc.protocol.common.NamedDefinition
    public String getIdentifier() {
        return this.identifier;
    }

    @Override // org.cloudburstmc.protocol.common.Definition
    public int getRuntimeId() {
        return this.runtimeId;
    }

    public NbtMap getState() {
        return this.state;
    }

    public String getPersistentIdentifier() {
        if (this.persistentIdentifier == null) {
            final StringBuilder builder = new StringBuilder(getIdentifier());
            if (!getState().isEmpty()) {
                TreeMap<String, String> properties = new TreeMap<>();
                NbtMap states = getState().getCompound("states");
                for (String stateName : states.keySet()) {
                    String value = states.get(stateName).toString();
                    properties.put(stateName, value);
                }
                properties.forEach(new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.data.definitions.SimpleBlockDefinition$$ExternalSyntheticLambda0
                    @Override // java.util.function.BiConsumer
                    public final void accept(Object obj, Object obj2) {
                        String str = (String) obj2;
                        builder.append("|").append((String) obj).append("=").append(str);
                    }
                });
            }
            this.persistentIdentifier = builder.toString();
        }
        return this.persistentIdentifier;
    }
}
