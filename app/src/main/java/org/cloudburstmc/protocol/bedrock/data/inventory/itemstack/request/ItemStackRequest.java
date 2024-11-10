package org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request;

import java.util.Arrays;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.ItemStackRequestAction;

/* loaded from: classes5.dex */
public final class ItemStackRequest {
    private final ItemStackRequestAction[] actions;
    private final String[] filterStrings;
    private final int requestId;
    private final TextProcessingEventOrigin textProcessingEventOrigin;

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ItemStackRequest)) {
            return false;
        }
        ItemStackRequest other = (ItemStackRequest) o;
        if (getRequestId() != other.getRequestId() || !Arrays.deepEquals(getActions(), other.getActions()) || !Arrays.deepEquals(getFilterStrings(), other.getFilterStrings())) {
            return false;
        }
        Object this$textProcessingEventOrigin = getTextProcessingEventOrigin();
        Object other$textProcessingEventOrigin = other.getTextProcessingEventOrigin();
        return this$textProcessingEventOrigin != null ? this$textProcessingEventOrigin.equals(other$textProcessingEventOrigin) : other$textProcessingEventOrigin == null;
    }

    public int hashCode() {
        int result = (1 * 59) + getRequestId();
        int result2 = (((result * 59) + Arrays.deepHashCode(getActions())) * 59) + Arrays.deepHashCode(getFilterStrings());
        Object $textProcessingEventOrigin = getTextProcessingEventOrigin();
        return (result2 * 59) + ($textProcessingEventOrigin == null ? 43 : $textProcessingEventOrigin.hashCode());
    }

    public String toString() {
        return "ItemStackRequest(requestId=" + getRequestId() + ", actions=" + Arrays.deepToString(getActions()) + ", filterStrings=" + Arrays.deepToString(getFilterStrings()) + ", textProcessingEventOrigin=" + getTextProcessingEventOrigin() + ")";
    }

    public ItemStackRequest(int requestId, ItemStackRequestAction[] actions, String[] filterStrings, TextProcessingEventOrigin textProcessingEventOrigin) {
        this.requestId = requestId;
        this.actions = actions;
        this.filterStrings = filterStrings;
        this.textProcessingEventOrigin = textProcessingEventOrigin;
    }

    public int getRequestId() {
        return this.requestId;
    }

    public ItemStackRequestAction[] getActions() {
        return this.actions;
    }

    public String[] getFilterStrings() {
        return this.filterStrings;
    }

    public TextProcessingEventOrigin getTextProcessingEventOrigin() {
        return this.textProcessingEventOrigin;
    }

    public ItemStackRequest(int requestId, ItemStackRequestAction[] actions, String[] filterStrings) {
        this(requestId, actions, filterStrings, TextProcessingEventOrigin.BLOCK_ENTITY_DATA_TEXT);
    }
}
