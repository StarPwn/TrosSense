package org.cloudburstmc.protocol.bedrock.data.event;

import org.cloudburstmc.protocol.bedrock.data.definitions.BlockDefinition;

/* loaded from: classes5.dex */
public class CopperWaxedOrUnwaxedEventData implements EventData {
    private final BlockDefinition definition;

    public CopperWaxedOrUnwaxedEventData(BlockDefinition definition) {
        this.definition = definition;
    }

    protected boolean canEqual(Object other) {
        return other instanceof CopperWaxedOrUnwaxedEventData;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CopperWaxedOrUnwaxedEventData)) {
            return false;
        }
        CopperWaxedOrUnwaxedEventData other = (CopperWaxedOrUnwaxedEventData) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$definition = getDefinition();
        Object other$definition = other.getDefinition();
        return this$definition != null ? this$definition.equals(other$definition) : other$definition == null;
    }

    public int hashCode() {
        Object $definition = getDefinition();
        int result = (1 * 59) + ($definition == null ? 43 : $definition.hashCode());
        return result;
    }

    public String toString() {
        return "CopperWaxedOrUnwaxedEventData(definition=" + getDefinition() + ")";
    }

    public BlockDefinition getDefinition() {
        return this.definition;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.event.EventData
    public EventDataType getType() {
        return EventDataType.COPPER_WAXED_OR_UNWAXED;
    }
}
