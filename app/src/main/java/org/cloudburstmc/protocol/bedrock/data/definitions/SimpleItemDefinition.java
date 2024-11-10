package org.cloudburstmc.protocol.bedrock.data.definitions;

/* loaded from: classes5.dex */
public class SimpleItemDefinition implements ItemDefinition {
    private final boolean componentBased;
    private final String identifier;
    private final int runtimeId;

    public SimpleItemDefinition(String identifier, int runtimeId, boolean componentBased) {
        this.identifier = identifier;
        this.runtimeId = runtimeId;
        this.componentBased = componentBased;
    }

    protected boolean canEqual(Object other) {
        return other instanceof SimpleItemDefinition;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SimpleItemDefinition)) {
            return false;
        }
        SimpleItemDefinition other = (SimpleItemDefinition) o;
        if (!other.canEqual(this) || getRuntimeId() != other.getRuntimeId() || isComponentBased() != other.isComponentBased()) {
            return false;
        }
        Object this$identifier = getIdentifier();
        Object other$identifier = other.getIdentifier();
        return this$identifier != null ? this$identifier.equals(other$identifier) : other$identifier == null;
    }

    public int hashCode() {
        int result = (1 * 59) + getRuntimeId();
        int result2 = result * 59;
        int i = isComponentBased() ? 79 : 97;
        Object $identifier = getIdentifier();
        return ((result2 + i) * 59) + ($identifier == null ? 43 : $identifier.hashCode());
    }

    public String toString() {
        return "SimpleItemDefinition(identifier=" + getIdentifier() + ", runtimeId=" + getRuntimeId() + ", componentBased=" + isComponentBased() + ")";
    }

    @Override // org.cloudburstmc.protocol.common.NamedDefinition
    public String getIdentifier() {
        return this.identifier;
    }

    @Override // org.cloudburstmc.protocol.common.Definition
    public int getRuntimeId() {
        return this.runtimeId;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.definitions.ItemDefinition
    public boolean isComponentBased() {
        return this.componentBased;
    }
}
