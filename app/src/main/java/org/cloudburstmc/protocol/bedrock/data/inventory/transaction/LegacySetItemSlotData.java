package org.cloudburstmc.protocol.bedrock.data.inventory.transaction;

import java.util.Arrays;

/* loaded from: classes5.dex */
public final class LegacySetItemSlotData {
    private final int containerId;
    private final byte[] slots;

    public LegacySetItemSlotData(int containerId, byte[] slots) {
        this.containerId = containerId;
        this.slots = slots;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof LegacySetItemSlotData)) {
            return false;
        }
        LegacySetItemSlotData other = (LegacySetItemSlotData) o;
        return getContainerId() == other.getContainerId() && Arrays.equals(getSlots(), other.getSlots());
    }

    public int hashCode() {
        int result = (1 * 59) + getContainerId();
        return (result * 59) + Arrays.hashCode(getSlots());
    }

    public String toString() {
        return "LegacySetItemSlotData(containerId=" + getContainerId() + ", slots=" + Arrays.toString(getSlots()) + ")";
    }

    public int getContainerId() {
        return this.containerId;
    }

    public byte[] getSlots() {
        return this.slots;
    }
}
