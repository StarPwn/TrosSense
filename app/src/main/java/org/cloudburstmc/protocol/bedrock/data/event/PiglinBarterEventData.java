package org.cloudburstmc.protocol.bedrock.data.event;

import org.cloudburstmc.protocol.bedrock.data.definitions.ItemDefinition;

/* loaded from: classes5.dex */
public class PiglinBarterEventData implements EventData {
    private final ItemDefinition definition;
    private final boolean targetingPlayer;

    public PiglinBarterEventData(ItemDefinition definition, boolean targetingPlayer) {
        this.definition = definition;
        this.targetingPlayer = targetingPlayer;
    }

    protected boolean canEqual(Object other) {
        return other instanceof PiglinBarterEventData;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PiglinBarterEventData)) {
            return false;
        }
        PiglinBarterEventData other = (PiglinBarterEventData) o;
        if (!other.canEqual(this) || isTargetingPlayer() != other.isTargetingPlayer()) {
            return false;
        }
        Object this$definition = getDefinition();
        Object other$definition = other.getDefinition();
        return this$definition != null ? this$definition.equals(other$definition) : other$definition == null;
    }

    public int hashCode() {
        int result = (1 * 59) + (isTargetingPlayer() ? 79 : 97);
        Object $definition = getDefinition();
        return (result * 59) + ($definition == null ? 43 : $definition.hashCode());
    }

    public String toString() {
        return "PiglinBarterEventData(definition=" + getDefinition() + ", targetingPlayer=" + isTargetingPlayer() + ")";
    }

    public ItemDefinition getDefinition() {
        return this.definition;
    }

    public boolean isTargetingPlayer() {
        return this.targetingPlayer;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.event.EventData
    public EventDataType getType() {
        return EventDataType.PIGLIN_BARTER;
    }
}
