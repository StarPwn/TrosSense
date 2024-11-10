package org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action;

import java.util.Arrays;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;

/* loaded from: classes5.dex */
public final class CraftResultsDeprecatedAction implements ItemStackRequestAction {
    private final ItemData[] resultItems;
    private final int timesCrafted;

    public CraftResultsDeprecatedAction(ItemData[] resultItems, int timesCrafted) {
        this.resultItems = resultItems;
        this.timesCrafted = timesCrafted;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CraftResultsDeprecatedAction)) {
            return false;
        }
        CraftResultsDeprecatedAction other = (CraftResultsDeprecatedAction) o;
        return getTimesCrafted() == other.getTimesCrafted() && Arrays.deepEquals(getResultItems(), other.getResultItems());
    }

    public int hashCode() {
        int result = (1 * 59) + getTimesCrafted();
        return (result * 59) + Arrays.deepHashCode(getResultItems());
    }

    public String toString() {
        return "CraftResultsDeprecatedAction(resultItems=" + Arrays.deepToString(getResultItems()) + ", timesCrafted=" + getTimesCrafted() + ")";
    }

    public ItemData[] getResultItems() {
        return this.resultItems;
    }

    public int getTimesCrafted() {
        return this.timesCrafted;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.ItemStackRequestAction
    public ItemStackRequestActionType getType() {
        return ItemStackRequestActionType.CRAFT_RESULTS_DEPRECATED;
    }
}
